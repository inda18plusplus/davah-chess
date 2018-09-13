package chess;

import chess.move.Move;
import chess.piece.Piece;

import java.util.ArrayList;

/** Implements a single chess game, and is the only class the user must interact with. */
public class Game {

  public static final int RANK_COUNT = 8;
  public static final int FILE_COUNT = 8;

  private enum State { // TODO: Write function to display this.
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

  /**
   * Places a piece on the board, while in setup mode.
   *
   * @param rank Rank to place the piece on.
   * @param file File to place the piece on.
   * @param asciiPiece Ascii representation of the piece.
   * @return Whether the placement was successful.
   */
  public boolean placePiece(int rank, int file, char asciiPiece) {
    if (state == State.SETUP) {
      Position position = new Position(rank, file);
      Piece piece = Piece.createPiece(position, asciiPiece);
      return board.placePiece(piece);
    } else {
      return false;
    }
  }

  /**
   * Removes a piece from the board, during setup mode.
   *
   * @param rank Rank to remove the piece from.
   * @param file File to remove the piece from.
   * @return Whether the removal was successful.
   */
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
   *
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
    history = new History();
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

  public ArrayList<Move> getMoves(Player player) {
    return board.getMoves(player, history);
  }

  /**
   * Calculates whether a move is legal for the current player.
   *
   * @param move The move whose legality to check.
   * @return Whether the move is legal.
   */
  public boolean isLegal(Move move) {
    ArrayList<Move> legalMoves = this.getMoves(currentPlayer);
    for (Move legalMove : legalMoves) {
      if (move.isEqual(legalMove)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Carries out a move given by the player in standard algebraic notation.
   *
   * @param moveNotation Algebraic notation of a move.
   * @return Whether the move was legal and carried out successfully.
   */
  public boolean makeMove(String moveNotation) {
    Move move = Move.createMove(moveNotation, board, currentPlayer);
    if (move == null) {
      return false;
    }
    return makeMove(move);
  }

  /**
   * Carries out a given move on the board.
   *
   * @param move The move to carry out.
   * @return Whether the move was legal and carried out successfully.
   */
  public boolean makeMove(Move move) {
    if (this.isLegal(move)) {
      move.applyTo(board);
      history.addMove(move);
      currentPlayer = otherPlayer(currentPlayer);
      checkEndOfGame();
      return true;
    }
    return false;
  }

  /** Checks if the game has ended, and updates the game state accordingly. */
  public void checkEndOfGame() {
    boolean check = board.inCheck(currentPlayer);
    int numberOfMoves = getMoves(currentPlayer).size();
    if (numberOfMoves == 0) {
      if (!check) {
        state = State.DRAW;
      } else if (currentPlayer == Player.BLACK) {
        state = State.WHITE_WIN;
      } else {
        state = State.BLACK_WIN;
      }
    }
  }
}
