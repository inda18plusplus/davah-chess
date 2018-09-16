package cnol.gui;

import chess.Game;
import chess.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
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

    VBox verticalBox = new VBox();

    verticalBox.getChildren().add(grid);
    verticalBox.setAlignment(Pos.CENTER);

    this.getChildren().add(verticalBox);
    this.setAlignment(Pos.CENTER);


    grid.setOnMousePressed(mouseEvent -> {
      this.moveStart = this.boardToTile(mouseEvent.getX(), mouseEvent.getY());

      this.revealAvailableMoves(moveStart);
    });


    grid.setOnMouseReleased(mouseEvent -> {
      Position point = this.boardToTile(mouseEvent.getX(), mouseEvent.getY());

      List<Position> available = this.game.whereCanItMoveTo(this.moveStart);

      if (available.contains(point)) {
        if (!this.game.makeMove(this.moveStart, point)) {
          // Has to promote
          char promotion = this.queryPromotion();
          this.game.makeMove(this.moveStart, point, promotion);
        }
      }

      this.moveStart = null;

      this.removeAllMarkers();

      this.updatePieces();
      this.updateGameStatus();
    });
  }

  private char queryPromotion() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Choose a promotion");
    alert.setHeaderText(null);
    alert.setGraphic(null);

    alert.getButtonTypes().clear();

    ButtonType queen = new ButtonType("Queen");
    ButtonType knight = new ButtonType("Knight");
    ButtonType rook = new ButtonType("Rook");
    ButtonType bishop = new ButtonType("Bishop");

    alert.getButtonTypes().addAll(queen, knight, rook, bishop);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.get() == queen) {
      return 'q';
    } else if (result.get() == knight) {
      return 'n';
    } else if (result.get() == rook) {
      return 'r';
    } else if (result.get() == bishop) {
      return 'b';
    }

    return 'q';
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
      tile.highlight(color);
    }
  }

  private void removeAllMarkers() {
    for (Node node : this.grid.getChildren()) {
      if (node instanceof Tile) {
        Tile tile = (Tile) node;
        tile.highlight(null);
      }
    }
  }

  private void revealAvailableMoves(Position position) {
    List<Position> destinations = this.game.whereCanItMoveTo(position);

    for (Position pos : destinations) {
      Tile tile = this.getTile(pos.getFile(), pos.getRank());

      if (tile != null) {
        tile.highlight(Color.GREEN);
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
        tile.setImage(image);
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
        Tile tile = new Tile((i + j) % 2 == 1 ? Color.BEIGE : Color.DARKORANGE);

        grid.add(tile, j, Game.RANK_COUNT - i - 1);
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
