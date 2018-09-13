package chess;

import chess.move.Move;
import chess.piece.Piece;

import java.util.ArrayList;

/**
 * Implements a single chess game.
 */
public class Game {

  public static final int RANK_COUNT = 8;
  public static final int FILE_COUNT = 8;

  private enum State {
    SETUP, PLAY, WHITE_WIN, DRAW, BLACK_WIN
  }

  public enum Player {
    BLACK, WHITE
  }

  public static Player otherPlayer(Player player) {
    return (player == Player.BLACK) ? Player.WHITE : Player.BLACK;
  }

  private State state;
  private Board board;
  private History history;
  private Player currentPlayer;

  public Game() {
    state = State.SETUP;
    board = new Board();
  }

  public boolean placePiece(int rank, int file, char asciiPiece) {
    if (state == State.SETUP) {
      Position position = new Position(rank, file);
      Piece piece = Piece.createPiece(position, asciiPiece);
      return board.placePiece(piece);
    } else {
      return false;
    }
  }

  public boolean removePiece(int rank, int file) {
    if (state == State.SETUP) {
      Position position = new Position(rank, file);
      return board.removePiece(position);
    } else {
      return false;
    }
  }

  /**
   * Sets up the standard chess board.
   * @return Whether the setup was successful.
   */
  public boolean setupStandardBoard() {
    if (state == State.SETUP && FILE_COUNT == 8 && RANK_COUNT >= 4) {
      String blackRank = "rnbqkbnr";
      String whiteRank = "RNBQKBNR";
      for (int i = 0; i < 8; i++) {
        this.placePiece(RANK_COUNT - 1, i, blackRank.charAt(i));
        this.placePiece(RANK_COUNT - 2, i, 'p');
        for (int j = 2; j < RANK_COUNT - 2; j++) {
          removePiece(j, i);
        }
        this.placePiece(1, i, 'P');
        this.placePiece(0, i, whiteRank.charAt(i));
      }
      return true;
    } else {
      return false;
    }
  }

  public boolean startGame() {
    if (state != State.SETUP
            || board.findKing(Player.WHITE) == null
            || board.findKing(Player.BLACK) == null) {
      return false;
    }
    state = State.PLAY;
    history = new History(board);
    currentPlayer = Player.WHITE;
    return true;
  }

  public String viewBoard() {
    return board.displayAsciiBoard();
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public String viewCurrentPlayer() {
    return (currentPlayer == Player.BLACK) ? "Black" : "White";
  }

  public ArrayList<Move> getMoves(Player player) {
    return board.getMoves(player, history);
  }

  public boolean isLegal(Move move) {
    ArrayList<Move> legalMoves = this.getMoves(currentPlayer);
    for (Move legalMove : legalMoves) {
      if (move.isEqual(legalMove)) {
        return true;
      }
    }
    return false;
  }

  public boolean makeMove(String moveNotation) {
    Move move = Move.createMove(moveNotation, board);
    if (move == null) {
      return false;
    }
    return makeMove(move);
  }

  public boolean makeMove(Move move) {
    if (this.isLegal(move)) {
      move.applyTo(board);
      currentPlayer = otherPlayer(currentPlayer);
      return true;
    }
    return false;
  }

}
