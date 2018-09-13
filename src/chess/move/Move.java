package chess.move;

import chess.Board;
import chess.Game;
import chess.Position;

/**
 * Implements a single move in a chess game.
 */
public abstract class Move {

  private Position posBefore;
  private Position posAfter;
  protected String identifier;

  public Move(Position posBefore, Position posAfter) {
    this.posBefore = posBefore;
    this.posAfter = posAfter;
  }

  public abstract boolean applyTo(Board board);

  public boolean isEqual(Move otherMove) {
    return this.getIdentifier().equals(otherMove.getIdentifier());
  }

  public static Move createMove(String moveNotation, Board board) { //todo
    if (moveNotation.matches("[a-h][1-8][a-h][1-8]")) {
      Position posBefore = Position.createPosition(moveNotation.substring(0, 2));
      Position posAfter = Position.createPosition(moveNotation.substring(2, 4));
      if (board.isEmpty(posBefore)) {
        return null;
      }
      Game.Player player = board.atPosition(posBefore).getPlayer();
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

  public String getIdentifier() {
    return identifier;
  }

}
