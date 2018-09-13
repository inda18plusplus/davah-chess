package chess.piece;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.Step;

import java.util.ArrayList;

/** Implements a specific chess piece, the king. */
public class King extends Piece {

  private static final Step[] KING_MOVES = {
          new Step(-1, 0),
          new Step(-1, 1),
          new Step(0, 1),
          new Step(1, 1),
          new Step(1, 0),
          new Step(1, -1),
          new Step(0, -1),
          new Step(-1, -1)
  };

  public King(Position position, Game.Player player) {
    super(position, player);
  }

  public ArrayList<Position> getReach(Board board) {
    ArrayList<Position> reach = new ArrayList<>();
    for (Step step : KING_MOVES) {
      Position posAfter = this.getPosition().getCopy();
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

  public char toAsciiSymbol() {
    return (this.getPlayer() == Game.Player.WHITE) ? 'K' : 'k';
  }

  public Piece getCopy() {
    return new King(this.getPosition().getCopy(), this.getPlayer());
  }

}