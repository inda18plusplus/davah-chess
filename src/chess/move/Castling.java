package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

public class Castling extends Move {

  private RegularMove rookMove;
  private boolean queenside;

  public Castling(
      Position kingPosBefore,
      Position kingPosAfter,
      Position rookPosBefore,
      Position rookPosAfter,
      boolean queenside) {
    super(kingPosBefore, kingPosAfter);
    rookMove = new RegularMove(rookPosBefore, rookPosAfter);
    this.queenside = queenside;
  }

  public void applyTo(Board board) {
    Piece movingKing = board.atPosition(this.getPosBefore()).getCopy();
    movingKing.setPosition(this.getPosAfter());
    board.placePiece(movingKing);
    board.removePiece(this.getPosBefore());
    rookMove.applyTo(board);
  }

  public String getIdentifier() {
    if (queenside) {
      return "0-0-0";
    } else {
      return "0-0";
    }
  }
}
