package chess.piece;

import static chess.Game.FILE_COUNT;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.Step;
import chess.move.Castling;
import chess.move.Move;

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

  public char toAsciiSymbol() {
    return (this.getPlayer() == Game.Player.WHITE) ? 'K' : 'k';
  }

  public ArrayList<Position> getReach(Board board) {
    ArrayList<Position> reach = new ArrayList<>();
    for (Step step : KING_MOVES) {
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
    return new King(this.getPosition(), this.getPlayer());
  }

  /**
   * Overrides Piece's getMoves() adding castling. TODO: Maybe split into more methods.
   *
   * @param board The board the piece is standing on.
   * @return A list of all possible moves for the piece.
   */
  @Override
  public ArrayList<Move> getMoves(Board board) {
    ArrayList<Move> moves = super.getMoves(board);
    Game.Player player = this.getPlayer();
    if (board.inCheck(player)) {
      return moves;
    }
    Position kingPosition = board.findKing(player);
    int kingRank = kingPosition.getRank();
    int kingFile = kingPosition.getFile();
    if (board.getHistory().hasMoved(kingPosition)) {
      return moves;
    }
    char lookingFor = (player == Game.Player.WHITE) ? 'R' : 'r';
    for (int i = kingFile - 1; i >= 0; i--) {
      Position positionAt = new Position(kingRank, i);
      if (board.isEmpty(positionAt)) {
        continue;
      }
      if (kingFile - i <= 2) {
        break;
      }
      if (board.atPosition(positionAt).toAsciiSymbol() == lookingFor) {
        Position kingPositionAfter = new Position(kingRank, kingFile - 2);
        Position rookPositionAfter = new Position(kingRank, kingFile - 1);
        Move castlingMove =
            new Castling(kingPosition, kingPositionAfter, positionAt, rookPositionAfter, true);
        moves.add(castlingMove);
      }
      break;
    }
    for (int i = kingPosition.getFile() + 1; i < FILE_COUNT; i++) {
      Position positionAt = new Position(kingRank, i);
      if (board.isEmpty(positionAt)) {
        continue;
      }
      if (i - kingFile <= 2) {
        break;
      }
      if (board.atPosition(positionAt).toAsciiSymbol() == lookingFor) {
        Position kingPositionAfter = new Position(kingRank, kingFile + 2);
        Position rookPositionAfter = new Position(kingRank, kingFile + 1);
        Move castlingMove =
            new Castling(kingPosition, kingPositionAfter, positionAt, rookPositionAfter, false);
        moves.add(castlingMove);
      }
      break;
    }
    return moves;
  }
}
