package chess.piece;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.Step;

import java.util.ArrayList;

import static chess.Game.RANK_COUNT;

/** Implements a specific chess piece, the pawn. */
public class Pawn extends Piece {

  private static final Step WHITE_FWD        = new Step(1, 0);
  private static final Step WHITE_DOUBLE_FWD = new Step(2, 0);
  private static final Step WHITE_LEFT       = new Step(1, -1);
  private static final Step WHITE_RIGHT      = new Step(1, 1);
  private static final Step BLACK_FWD        = new Step(-1, 0);
  private static final Step BLACK_DOUBLE_FWD = new Step(-2, 0);
  private static final Step BLACK_LEFT       = new Step(-1, 1);
  private static final Step BLACK_RIGHT      = new Step(-1, -1);

  public Pawn(Position position, Game.Player player) {
    super(position, player);
  }

  public ArrayList<Position> getReach(Board board) {
    Game.Player player = this.getPlayer();
    Position posBefore = this.getPosition();

    Position posForward   = (player == Game.Player.WHITE)
            ? WHITE_FWD.applyOn(posBefore)
            : BLACK_FWD.applyOn(posBefore);
    Position posDoubleFwd = (player == Game.Player.WHITE)
            ? WHITE_DOUBLE_FWD.applyOn(posBefore)
            : BLACK_DOUBLE_FWD.applyOn(posBefore);
    Position posLeft      = (player == Game.Player.WHITE)
            ? WHITE_LEFT.applyOn(posBefore)
            : BLACK_LEFT.applyOn(posBefore);
    Position posRight     = (player == Game.Player.WHITE)
            ? WHITE_RIGHT.applyOn(posBefore)
            : BLACK_RIGHT.applyOn(posBefore);

    ArrayList<Position> reach = new ArrayList<>();
    if (posForward.insideBoard()) { // Always enters under normal chess rules.
      if (board.isEmpty(posForward)) {
        reach.add(posForward);
        int doubleMoveRank = (player == Game.Player.WHITE) ? 1 : RANK_COUNT - 2;
        if (posBefore.getRank() == doubleMoveRank) {
          if (board.isEmpty(posDoubleFwd)) {
            reach.add(posDoubleFwd);
          }
        }
      }
    }
    if (posLeft.insideBoard()) {
      if (board.isOpposite(player, posLeft)) {
        reach.add(posLeft);
      }
    }
    if (posRight.insideBoard()) {
      if (board.isOpposite(player, posRight)) {
        reach.add(posRight);
      }
    }
    return reach;
  }

  public char toAsciiSymbol() {
    return (this.getPlayer() == Game.Player.WHITE) ? 'P' : 'p';
  }

  public Piece getCopy() {
    return new Pawn(this.getPosition().getCopy(), this.getPlayer());
  }

}