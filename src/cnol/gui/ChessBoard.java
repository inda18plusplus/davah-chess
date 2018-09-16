package cnol.gui;


import chess.Game;
import chess.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ChessBoard extends HBox {
  private Game game;

  private GridPane grid;

  private static final Map<Character, Image> pieceImages;

  private Position moveStart;

  static {
    pieceImages = new HashMap<>();

    pieceImages.put('p', new Image("BlackPawn.png"));
    pieceImages.put('n', new Image("BlackKnight.png"));
    pieceImages.put('b', new Image("BlackBishop.png"));
    pieceImages.put('r', new Image("BlackRook.png"));
    pieceImages.put('q', new Image("BlackQueen.png"));
    pieceImages.put('k', new Image("BlackKing.png"));
    pieceImages.put('P', new Image("WhitePawn.png"));
    pieceImages.put('N', new Image("WhiteKnight.png"));
    pieceImages.put('B', new Image("WhiteBishop.png"));
    pieceImages.put('R', new Image("WhiteRook.png"));
    pieceImages.put('Q', new Image("WhiteQueen.png"));
    pieceImages.put('K', new Image("WhiteKing.png"));
  }

  ChessBoard() {
    this.game = new Game();
    this.game.setupStandardBoard();
    this.game.startGame();

    this.createGrid();
    this.updatePieces();

    VBox vBox = new VBox();

    vBox.getChildren().add(grid);
    vBox.setAlignment(Pos.CENTER);

    this.getChildren().add(vBox);
    this.setAlignment(Pos.CENTER);


    grid.setOnMousePressed(mouseEvent -> {
      this.moveStart = this.boardToTile(mouseEvent.getX(), mouseEvent.getY());

      this.revealAvailableMoves(moveStart);
    });


    grid.setOnMouseReleased(mouseEvent -> {
      Position point = this.boardToTile(mouseEvent.getX(), mouseEvent.getY());

      this.game.makeMove(this.moveStart, point, 'q');
      this.moveStart = null;

      this.removeAllMarkers();

      this.updatePieces();
      this.updateGameStatus();
    });
  }

  private void updateGameStatus() {
    switch (this.game.getState()) {
      case PLAY:
        break;
      case WHITE_WIN:
        this.markKing(Game.Player.BLACK, Color.RED);
        break;
      case DRAW:
        this.markKing(Game.Player.BLACK, Color.BLUE);
        this.markKing(Game.Player.WHITE, Color.BLUE);
        break;
      case BLACK_WIN:
        this.markKing(Game.Player.WHITE, Color.RED);
        break;
      default:
        break;
    }
  }

  private void markKing(Game.Player player, Color color) {
    Position checkTile = this.game.getBoard().findKing(player);
    Tile tile = this.getTile(checkTile.getFile(), checkTile.getRank());

    if (tile != null) {
      tile.showMarker(color);
    }
  }

  private void removeAllMarkers() {
    for (Node node : this.grid.getChildren()) {
      if (node instanceof Tile) {
        Tile tile = (Tile) node;
        tile.showMarker(null);
      }
    }
  }

  private void revealAvailableMoves(Position position) {
    List<Position> destinations = this.game.whereCanItMoveTo(position);

    for (Position pos : destinations) {
      Tile tile = this.getTile(pos.getFile(), pos.getRank());

      if (tile != null) {
        tile.showMarker(Color.GREEN);
      }
    }
  }

  private void updatePieces() {
    String pieces = this.game.viewBoard();

    int row = Game.RANK_COUNT - 1;
    int col = 0;
    for (char piece : pieces.toCharArray()) {
      switch (piece) {
        case '\n':
          row--;
          col = 0;
          break;
        default:
          this.addPiece(piece, col, row);
          col++;
          break;
      }
    }
  }

  private void addPiece(char piece, int col, int row) {
    Image image = pieceImages.getOrDefault(piece, null);
    Tile tile = this.getTile(col, row);

    if (tile != null) {
      if (image != null) {
        tile.setImage(new ImageView(image));
      } else {
        tile.setImage(null);
      }
    }
  }


  private void createGrid() {
    grid = new GridPane();

    // Set the size of tiles
    NumberBinding tileSize = Bindings.min(this.widthProperty().divide(Game.FILE_COUNT),
        this.heightProperty().divide(Game.RANK_COUNT));

    grid.prefWidthProperty().bind(tileSize.multiply(Game.FILE_COUNT));
    grid.prefHeightProperty().bind(tileSize.multiply(Game.RANK_COUNT));

    for (int i = 0; i < Game.FILE_COUNT; i++) {
      ColumnConstraints constraints = new ColumnConstraints();
      constraints.setPercentWidth(100.0 / Game.FILE_COUNT);
      grid.getColumnConstraints().add(constraints);
    }
    for (int i = 0; i < Game.RANK_COUNT; i++) {
      RowConstraints constraints = new RowConstraints();
      constraints.setPercentHeight(100.0 / Game.RANK_COUNT);
      grid.getRowConstraints().add(constraints);
    }

    // Add tiles
    for (int i = 0; i < Game.RANK_COUNT; i++) {
      for (int j = 0; j < Game.FILE_COUNT; j++) {
        Tile tile = new Tile((i + j) % 2 == 0 ? Color.BEIGE : Color.DARKORANGE, tileSize);

        grid.add(tile, j, Game.RANK_COUNT - i -1);
      }
    }
  }

  private Position boardToTile(double x, double y) {
    return new Position(
        (int) (Game.RANK_COUNT * (1.0 - y / grid.getHeight())),
        (int) (Game.FILE_COUNT * x / grid.getWidth())
    );
  }

  private Tile getTile(int col, int row) {
    for (Node node : this.grid.getChildren()) {
      if (node instanceof Tile && col == GridPane.getColumnIndex(node)
          && Game.RANK_COUNT - row - 1 == GridPane.getRowIndex(node)) {
        return (Tile) node;
      }
    }

    return null;
  }
}
