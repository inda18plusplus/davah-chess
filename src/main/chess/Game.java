package chess;

import chess.move.Move;
import chess.piece.Piece;

import java.util.ArrayList;

/** Implements a single chess game. */
public class Game {

  public static final int RANK_COUNT = 8;
  public static final int FILE_COUNT = 8;

  public enum State {
    SETUP,
    PLAY,
    WHITE_WIN,
    DRAW,
    BLACK_WIN
  }

  public enum Player {
    BLACK,
    WHITE
  }

  private State state;
  private Board board;
  private Player currentPlayer;

  public Game() {
    state = State.SETUP;
    board = new Board();
  }

  public State getState() {
    return state;
  }

  /**
   * Displays the current game state in a human-readable format.
   *
   * @return The description of the game state.
   */
  public String viewState() {
    switch (state) {
      case SETUP:
        return "Setting up the board, can't play yet.";
      case PLAY:
        return (currentPlayer == Player.BLACK) ? "Black to play." : "White to play.";
      case WHITE_WIN:
        return "White has won!";
      case BLACK_WIN:
        return "Black has won!";
      case DRAW:
        return "It's a draw!";
      default:
        return "An error has occurred.";
    }
  }

  public Board getBoard() {
    return board;
  }

  public String viewBoard() {
    return board.viewBoard();
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public String viewCurrentPlayer() {
    return (currentPlayer == Player.BLACK) ? "Black" : "White";
  }

  public static Player otherPlayer(Player player) {
    return (player == Player.BLACK) ? Player.WHITE : Player.BLACK;
  }

  /**
   * Calculates where a piece can move this turn, for testing and use in GUIs.
   *
   * @param pieceLocation Where the piece is located.
   * @return Where it can move. Empty if the given position does not contain a piece controlled by
   *     the current player.
   */
  public ArrayList<Position> whereCanItMoveTo(Position pieceLocation) {
    ArrayList<Position> positions = new ArrayList<>();
    if (!pieceLocation.insideBoard()) {
      return positions;
    }
    if (!board.isPlayer(currentPlayer, pieceLocation)) {
      return positions;
    }
    for (Move move : board.atPosition(pieceLocation).getMoves(board)) {
      positions.add(move.getPosAfter());
    }
    return positions;
  }

  /**
   * Places a piece on the board, while in setup mode.
   *
   * @param position Position to place the piece on.
   * @param asciiPiece Ascii representation of the piece.
   * @return Whether the placement was successful.
   */
  public boolean placePiece(Position position, char asciiPiece) {
    if (state == State.SETUP) {
      Piece piece = Piece.createPiece(position, asciiPiece);
      return board.placePiece(piece);
    } else {
      return false;
    }
  }

  /**
   * Removes a piece from the board, during setup mode.
   *
   * @param position Position to remove the piece from.
   * @return Whether the removal was successful.
   */
  public boolean removePiece(Position position) {
    if (state == State.SETUP) {
      return board.removePiece(position);
    } else {
      return false;
    }
  }

  /**
   * Sets up the standard chess board.
   *
   * @return Whether the setup was successful.
   */
  public boolean setupStandardBoard() {
    if (state == State.SETUP && FILE_COUNT == 8 && RANK_COUNT >= 4) {
      String blackRank = "rnbqkbnr";
      String whiteRank = "RNBQKBNR";
      for (int i = 0; i < 8; i++) {
        this.placePiece(new Position(RANK_COUNT - 1, i), blackRank.charAt(i));
        this.placePiece(new Position(RANK_COUNT - 2, i), 'p');
        for (int j = 2; j < RANK_COUNT - 2; j++) {
          removePiece(new Position(j, i));
        }
        this.placePiece(new Position(1, i), 'P');
        this.placePiece(new Position(0, i), whiteRank.charAt(i));
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Starts the game, moving from "setup mode" to "play mode", given that both sides have exactly
   * one king each.
   *
   * @return Whether the game started successfully.
   */
  public boolean startGame() {
    if (state != State.SETUP
        || board.findKing(Player.WHITE) == null
        || board.findKing(Player.BLACK) == null) {
      return false;
    }
    state = State.PLAY;
    currentPlayer = Player.WHITE;
    return true;
  }

  /**
   * Validated and executes a move given by the user.
   *
   * @param moveNotation The move, in standard, actual (this time for real) algebraic notation.
   * @return Whether the move was legal and carried out properly.
   */
  public boolean tryMakeMove(String moveNotation) {
    Move move = Move.createMove(moveNotation, board.getMoves(currentPlayer), board);
    if (move == null) {
      return false;
    }
    performMove(move);
    return true;
  }

  /**
   * Validated and executes a move given by the user. The move cannot be a promotion.
   *
   * @param posBefore The position to move from.
   * @param posAfter The position to move to.
   * @return Whether the move was legal and carried out properly.
   */
  public boolean tryMakeMove(Position posBefore, Position posAfter) {
    Move move = Move.createMove(posBefore, posAfter, board.getMoves(currentPlayer));
    if (move == null) {
      return false;
    }
    performMove(move);
    return true;
  }

  /**
   * Validated and executes a move given by the user. The move must be a promotion.
   *
   * @param posBefore The position to move from.
   * @param posAfter The position to move to.
   * @param promoteTo The ascii identifier of the piece to promote to.
   * @return Whether the move was legal and carried out properly.
   */
  public boolean tryMakeMove(Position posBefore, Position posAfter, char promoteTo) {
    Move move = Move.createMove(posBefore, posAfter, promoteTo, board.getMoves(currentPlayer));
    if (move == null) {
      return false;
    }
    performMove(move);
    return true;
  }

  private void performMove(Move move) {
    move.applyTo(board);
    currentPlayer = otherPlayer(currentPlayer);
    checkEndOfGame();
  }

  private void checkEndOfGame() {
    if (board.inStaleMate(currentPlayer)) {
      state = State.DRAW;
    } else if (board.inCheckMate(Player.BLACK)) {
      state = State.WHITE_WIN;
    } else if (board.inCheckMate(Player.WHITE)) {
      state = State.BLACK_WIN;
    }
  }
}
