package chess.piece;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.Step;

import java.util.ArrayList;

/** Implements a specific chess piece, the queen. */
public class Queen extends Piece {

  private static final Step[] QUEEN_MOVES = {
    new Step(-1, 0),
    new Step(-1, 1),
    new Step(0, 1),
    new Step(1, 1),
    new Step(1, 0),
    new Step(1, -1),
    new Step(0, -1),
    new Step(-1, -1)
  };

  public Queen(Position position, Game.Player player) {
    super(position, player);
  }

  public char toAsciiSymbol() {
    return (this.getPlayer() == Game.Player.WHITE) ? 'Q' : 'q';
  }

  public ArrayList<Position> getReach(Board board) {
    ArrayList<Position> reach = new ArrayList<>();
    for (Step step : QUEEN_MOVES) {
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
    return new Queen(this.getPosition(), this.getPlayer());
  }
}
