package ggerholm.game;

import chess.Game;
import chess.Game.Player;
import chess.Position;
import chess.move.Move;
import chess.piece.Piece;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SwingGame extends JFrame implements Runnable {

  static int SQUARE_SIZE = 80;

  private int windowWidth = 960;
  private int windowHeight = 720;
  private int marginX;
  private int marginY;

  private Game board;
  private Position selected;
  private List<DrawablePiece> pieces = new ArrayList<>();

  private SwingGame() {
  }

  public static void main(String[] args) {
    SwingGame game = new SwingGame();
    game.start();
  }

  private void start() {
    createFrame();

    board = new Game();
    board.setupStandardBoard();
    board.startGame();
    updateBoardView();

    setupInput();

    Thread thread = new Thread(this);
    thread.start();
  }

  private void createFrame() {
    setTitle("Chess");
    setSize(windowWidth, windowHeight);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void setupInput() {
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        int rank = Game.RANK_COUNT - (e.getY() - marginY) / SQUARE_SIZE - 1;
        int file = (e.getX() - marginX) / SQUARE_SIZE;

        Position pos = new Position(rank, file);
        if (!pos.insideBoard()) {
          return;
        }

        Piece piece = board.getBoard().atPosition(pos);
        if (piece != null && piece.getPlayer() == board.getCurrentPlayer()) {
          selected = pos;
          return;
        }

        if (selected == null) {
          return;
        }

        if (tryForPromotion(selected, pos)) {
          selected = null;
          updateBoardView();
          return;
        }

        List<Position> possibleMoves = board.whereCanItMoveTo(selected);
        System.out
            .println(selected.getRank() + " " + selected.getFile() + " - " + possibleMoves.size());

        if (possibleMoves.stream().noneMatch(m -> m.isEqual(pos))) {
          selected = null;
          return;
        }

        if (board.makeMove(selected, pos)) {
          updateBoardView();
        }

        selected = null;
      }
    });

  }

  private boolean tryForPromotion(Position current, Position target) {
    char piece = board.viewBoard().replaceAll("\n", "")
        .charAt((Game.RANK_COUNT - current.getRank() - 1) * Game.FILE_COUNT + current.getFile());

    if (piece != 'P' && piece != 'p') {
      return false;
    }

    if (piece == 'P' && target.getRank() != Game.RANK_COUNT - 1) {
      return false;
    }

    if (piece == 'p' && target.getRank() != 0) {
      return false;
    }

    String message = "Input either q, b, k or r for queen, bishop, knight or rook respectively";
    String prom = JOptionPane.showInputDialog(message);
    while (!prom.equalsIgnoreCase("q")
        && !prom.equalsIgnoreCase("b")
        && !prom.equalsIgnoreCase("k")
        && !prom.equalsIgnoreCase("r")) {
      prom = JOptionPane.showInputDialog(message);
    }
    prom = prom.toUpperCase();

    ArrayList<Move> legalMoves = board.getBoard().getMoves(board.getCurrentPlayer());
    String notation = Move.createMove(current, target, legalMoves)
        .getNotation(legalMoves, board.getBoard());

    notation = notation.substring(0, notation.length() - 1) + prom;
    return board.makeMove(notation);
  }

  private void updateBoardView() {
    pieces.clear();

    String boardView = board.viewBoard().replaceAll("\n", "");
    for (int i = 0; i < Game.RANK_COUNT; i++) {
      for (int j = 0; j < Game.FILE_COUNT; j++) {
        char pieceAscii = boardView.charAt(i * Game.FILE_COUNT + j);

        if (pieceAscii == '.' || pieceAscii == '\n') {
          continue;
        }

        Position pos = new Position(i, j);
        pieces.add(new DrawablePiece(pieceAscii, pos));
      }
    }

  }

  private void render(Graphics2D g) {
    final Color teamColor = board.getCurrentPlayer() == Player.BLACK ? Color.BLACK : Color.WHITE;
    final Color enemyColor = board.getCurrentPlayer() == Player.BLACK ? Color.WHITE : Color.BLACK;

    g.setColor(teamColor);
    g.fillRect(0, 0, windowWidth, windowHeight);

    int boardWidth = SQUARE_SIZE * Game.FILE_COUNT;
    int boardHeight = SQUARE_SIZE * Game.RANK_COUNT;

    marginX = (windowWidth - boardWidth) / 2;
    marginY = (windowHeight - boardHeight) / 2;
    g.translate(marginX, marginY);

    for (int i = 0; i < Game.RANK_COUNT * Game.FILE_COUNT; i++) {
      int x = i % Game.FILE_COUNT * SQUARE_SIZE;
      int y = i / Game.FILE_COUNT * SQUARE_SIZE;

      g.setColor(i % 2 == i / Game.RANK_COUNT % 2 ? Color.WHITE : Color.BLACK);
      g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
    }

    for (int i = 0; i < pieces.size(); i++) {
      pieces.get(i).draw(g);
    }

    g.setColor(enemyColor);
    g.drawRect(0, 0, boardWidth, boardHeight);
    g.drawString(board.viewState(), (int) (-marginX * 0.75f), 50);

  }

  private void buffer() {
    BufferStrategy bufferStrategy = getBufferStrategy();
    if (bufferStrategy == null) {
      createBufferStrategy(3);
      return;
    }

    Graphics2D g2d = null;
    do {
      try {
        g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
        render(g2d);
      } finally {

        if (g2d != null) {
          g2d.dispose();
        }

      }
      bufferStrategy.show();
    } while (bufferStrategy.contentsLost());

  }

  @Override
  public void run() {
    int targetFps = 60;
    long optimalTime = 1000000000 / targetFps;

    while (isVisible()) {
      long lastLoopTime = System.nanoTime();

      buffer();

      try {
        Thread.sleep(Math.max(0, lastLoopTime - System.nanoTime() + optimalTime) / 1000000);
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.exit(1);
      }

    }

  }

}
