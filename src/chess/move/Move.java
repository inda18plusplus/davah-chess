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
   * Parses algebraic notation to create a move, given the board and current player.
   *
   * @param moveNotation Algebraic notation of a move.
   * @param board The board the move will be carried out on.
   * @param player The current player.
   * @return The move created by the function if successful, otherwise null.
   */
  public static Move createMove(String moveNotation, Board board, Game.Player player) {
    if (moveNotation.matches("[a-h][1-8][a-h][1-8]")) { // TODO: This is cheating, fix
      Position posBefore = Position.createPosition(moveNotation.substring(0, 2));
      Position posAfter = Position.createPosition(moveNotation.substring(2, 4));
      if (board.isEmpty(posBefore)) {
        return null;
      }
      if (board.atPosition(posBefore).getPlayer() != player) {
        return null;
      }
      if (!board.isPlayer(player, posAfter)) {
        return new RegularMove(posBefore, posAfter);
      }
    }
    if (moveNotation.equals("0-0")) {
      int homeRank = (player == Game.Player.WHITE) ? 0 : RANK_COUNT - 1;
      Position kingPosBefore = new Position(homeRank, 4);
      Position kingPosAfter = new Position(homeRank, 6);
      Position rookPosBefore = new Position(homeRank, 7);
      Position rookPosAfter = new Position(homeRank, 5);
      return new Castling(kingPosBefore, kingPosAfter, rookPosBefore, rookPosAfter);
    }
    if (moveNotation.equals("0-0-0")) {
      int homeRank = (player == Game.Player.WHITE) ? 0 : RANK_COUNT - 1;
      Position kingPosBefore = new Position(homeRank, 4);
      Position kingPosAfter = new Position(homeRank, 2);
      Position rookPosBefore = new Position(homeRank, 0);
      Position rookPosAfter = new Position(homeRank, 3);
      return new Castling(kingPosBefore, kingPosAfter, rookPosBefore, rookPosAfter);
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
