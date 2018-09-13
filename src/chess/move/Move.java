package chess.move;

import chess.Board;
import chess.Game;
import chess.Position;

/** Implements a single move in a chess game. */
public abstract class Move {

  private Position posBefore;
  private Position posAfter;
  String identifier;

  public Move(Position posBefore, Position posAfter) {
    this.posBefore = posBefore;
    this.posAfter = posAfter;
  }

  public abstract boolean applyTo(Board board);

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

  public String getIdentifier() {
    return identifier;
  }
}
