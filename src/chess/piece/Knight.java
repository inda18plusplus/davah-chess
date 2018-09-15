package chess.piece;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.Step;

import java.util.ArrayList;

/** Implements a specific chess piece, the knight. */
public class Knight extends Piece {

  private static final Step[] KNIGHT_MOVES = {
    new Step(-2, 1),
    new Step(-1, 2),
    new Step(1, 2),
    new Step(2, 1),
    new Step(2, -1),
    new Step(1, -2),
    new Step(-1, -2),
    new Step(-2, -1)
  };

  public Knight(Position position, Game.Player player) {
    super(position, player);
  }

  public char toAsciiSymbol() {
    return (this.getPlayer() == Game.Player.WHITE) ? 'N' : 'n';
  }

  public ArrayList<Position> getReach(Board board) {
    ArrayList<Position> reach = new ArrayList<>();
    for (Step step : KNIGHT_MOVES) {
      Position posAfter = this.getPosition();
      posAfter = step.applyOn(posAfter);
      if (!posAfter.insideBoard()) {
        continue;
      }
      if (board.isEmpty(posAfter)) {
        reach.add(posAfter);
      } else if (board.isOpposite(this.getPlayer(), posAfter)) {
        reach.add(posAfter);
      }
    }
    return reach;
  }

  public Piece getCopy() {
    return new Knight(this.getPosition(), this.getPlayer());
  }

}