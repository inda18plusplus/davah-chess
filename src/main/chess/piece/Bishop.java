package chess.piece;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.Step;

import java.util.ArrayList;

/** Implements a specific chess piece, the bishop. */
public class Bishop extends Piece {

  private static final Step[] BISHOP_MOVES = {
    new Step(-1, 1),
    new Step(1, 1),
    new Step(1, -1),
    new Step(-1, -1)
  };

  public Bishop(Position position, Game.Player player) {
    super(position, player);
  }

  public char toAsciiSymbol() {
    return (this.getPlayer() == Game.Player.WHITE) ? 'B' : 'b';
  }

  public ArrayList<Position> getReach(Board board) {
    ArrayList<Position> reach = new ArrayList<>();
    for (Step step : BISHOP_MOVES) {
      Position posAfter = this.getPosition();
      while (step.applyOn(posAfter).insideBoard()) {
        posAfter = step.applyOn(posAfter);
        if (board.isEmpty(posAfter)) {
          reach.add(posAfter);
          continue;
        } else if (board.isOpposite(this.getPlayer(), posAfter)) {
          reach.add(posAfter);
          break;
        }
        break;
      }
    }
    return reach;
  }

  public Piece getCopy() {
    return new Bishop(this.getPosition(), this.getPlayer());
  }

}