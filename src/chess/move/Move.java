package chess.move;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.piece.Piece;

import java.util.ArrayList;

/** Implements a single move in a chess game. */
public class Move {

  private Position posBefore;
  private Position posAfter;

  public Move(Position posBefore, Position posAfter) {
    this.posBefore = posBefore;
    this.posAfter = posAfter;
  }

  /**
   * Matches a movement given by the user to one of the current player's legal moves and returns it.
   *
   * @param posBefore The position the piece moves from.
   * @param posAfter The position the piece moves to.
   * @param legalMoves The current player's legal moves.
   * @return The move if a match was found, otherwise null.
   */
  public static Move createMove(Position posBefore, Position posAfter, ArrayList<Move> legalMoves) {
    for (Move move : legalMoves) {
      if (move.getPosBefore() == posBefore && move.getPosAfter() == posAfter) {
        return move;
      }
    }
    return null;
  }

  /**
   * Matches a move given in algebraic notation to one of the current player's legal moves and
   * returns it.
   *
   * @param moveNotation The algebraic notation of a move.
   * @param legalMoves The current player's legal moves.
   * @return The move given by moveNotation if a match was found, otherwise null.
   */
  public static Move createMove(String moveNotation, ArrayList<Move> legalMoves, Board board) {
    for (Move move : legalMoves) {
      if (move.getNotation(legalMoves, board).equals(moveNotation)) {
        return move;
      }
    }
    return null;
  }

  /**
   * Gets the algebraic notation of this move, given the board it is applied to, and that it is a
   * legal move on that board.
   * TODO: Maybe split into more methods; this is very long.
   *
   * @param legalMoves A list of the other legal moves for the same player on that board.
   * @param board The board.
   * @return The move in algebraic notation.
   */
  public String getNotation(ArrayList<Move> legalMoves, Board board) {
    String notation = "";
    char movingPiece = board.atPosition(this.getPosBefore()).toAsciiSymbol();
    movingPiece = Character.toUpperCase(movingPiece);

    if (movingPiece != 'P') {
      notation += movingPiece;
      boolean sameRank = false;
      boolean sameFile = false;
      for (Move move : legalMoves) {
        boolean samePiece = move.getPosBefore().isEqual(this.getPosBefore());
        boolean sameDestination = move.getPosAfter().isEqual(this.getPosAfter());
        if (samePiece && sameDestination) {
          continue;
        }
        boolean samePieceType =
            (board.atPosition(move.getPosBefore()).toAsciiSymbol() == movingPiece);
        if (samePieceType && sameDestination) {
          if (move.getPosBefore().getRank() == this.getPosBefore().getRank()) {
            sameRank = true;
          }
          if (move.getPosBefore().getFile() == this.getPosBefore().getFile()) {
            sameFile = true;
          }
        }
      }
      if (sameRank && sameFile) {
        notation += this.getPosBefore().getNotation();
      } else if (sameFile) {
        notation += this.getPosBefore().getNotation().charAt(1);
      } else if (sameRank) {
        notation += this.getPosBefore().getNotation().charAt(0);
      }
    }

    if (this.isCapture(board)) {
      if (movingPiece == 'P') {
        notation += this.getPosBefore().getNotation().charAt(0);
      }
      notation += 'x';
    }
    notation += this.getPosAfter().getNotation();

    Game.Player currentPlayer = board.atPosition(this.getPosBefore()).getPlayer();
    Board boardCopy = board.getCopy();
    this.applyTo(boardCopy);
    if (boardCopy.inCheckMate(Game.otherPlayer(currentPlayer))) {
      notation += '#';
    } else if (boardCopy.inCheck(Game.otherPlayer(currentPlayer))) {
      notation += '+';
    }
    return notation;
  }

  public Position getPosBefore() {
    return posBefore;
  }

  public Position getPosAfter() {
    return posAfter;
  }

  public boolean involves(Position position) {
    return posBefore.isEqual(position) || posAfter.isEqual(position);
  }

  public boolean isCapture(Board board) {
    return !board.isEmpty(this.getPosAfter());
  }

  /**
   * Abstract method to execute the move on a Board object, changing it.
   *
   * @param board The board to execute the move on.
   */
  public void applyTo(Board board) {
    board.addToHistory(this);
    Piece movingPiece = board.atPosition(this.getPosBefore()).getCopy();
    movingPiece.setPosition(this.getPosAfter());
    board.placePiece(movingPiece);
    board.removePiece(this.getPosBefore());
  }
}
