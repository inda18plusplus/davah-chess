package chess.move;

import chess.Board;
import chess.Game;
import chess.Position;

import static chess.Game.RANK_COUNT;

/** Implements a single move in a chess game. */
public abstract class Move {

  private Position posBefore;
  private Position posAfter;

  public Move(Position posBefore, Position posAfter) {
    this.posBefore = posBefore;
    this.posAfter = posAfter;
  }

  /**
   * Abstract method to execute the move on a Board object, changing it.
   *
   * @param board The board to execute the move on.
   */
  public abstract void applyTo(Board board);

  public abstract String getIdentifier();

  public boolean isEqual(Move otherMove) {
    return this.getIdentifier().equals(otherMove.getIdentifier());
  }

  /**
   * Parses pseudo-algebraic notation to create a move, given the board and current player.
   * It does not check that the move is legal.
   *
   * @param moveNotation Notation of a move.
   * @param board The board the move will be carried out on.
   * @param player The current player.
   * @return The move created by the function if successful, otherwise null.
   */
  public static Move createMove(String moveNotation, Board board, Game.Player player) {
    if (moveNotation.matches("[a-h][1-8][a-h][1-8]")) {
      Position posBefore = Position.createPosition(moveNotation.substring(0, 2));
      Position posAfter = Position.createPosition(moveNotation.substring(2, 4));
      return new RegularMove(posBefore, posAfter);
    }
    if (moveNotation.matches("[a-h][1-8][a-h][1-8]e.p.")) {
      Position posBefore = Position.createPosition(moveNotation.substring(0, 2));
      Position posAfter = Position.createPosition(moveNotation.substring(2, 4));
      Position capturePosition = new Position(posBefore.getRank(), posAfter.getFile());
      return new EnPassant(posBefore, posAfter, capturePosition);
    }
    if (moveNotation.matches("[a-h][1-8][a-h][1-8][BbNnQqRr]")) {
      Position posBefore = Position.createPosition(moveNotation.substring(0, 2));
      Position posAfter = Position.createPosition(moveNotation.substring(2, 4));
      char promoteTo = moveNotation.charAt(4);
      return new Promotion(posBefore, posAfter, promoteTo);
    }
    if (moveNotation.equals("0-0")) {
      int homeRank = (player == Game.Player.WHITE) ? 0 : RANK_COUNT - 1;
      Position kingPosBefore = new Position(homeRank, 4);
      Position kingPosAfter = new Position(homeRank, 6);
      Position rookPosBefore = new Position(homeRank, 7);
      Position rookPosAfter = new Position(homeRank, 5);
      return new Castling(kingPosBefore, kingPosAfter, rookPosBefore, rookPosAfter, false);
    }
    if (moveNotation.equals("0-0-0")) {
      int homeRank = (player == Game.Player.WHITE) ? 0 : RANK_COUNT - 1;
      Position kingPosBefore = new Position(homeRank, 4);
      Position kingPosAfter = new Position(homeRank, 2);
      Position rookPosBefore = new Position(homeRank, 0);
      Position rookPosAfter = new Position(homeRank, 3);
      return new Castling(kingPosBefore, kingPosAfter, rookPosBefore, rookPosAfter, true);
    }
    return null;
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
}
