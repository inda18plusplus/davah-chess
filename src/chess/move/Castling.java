package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

public class Castling extends Move {

  private RegularMove rookMove;

  public Castling(
      Position kingPosBefore,
      Position kingPosAfter,
      Position rookPosBefore,
      Position rookPosAfter) {
    super(kingPosBefore, kingPosAfter);
    rookMove = new RegularMove(rookPosBefore, rookPosAfter);
  }

  public void applyTo(Board board) {
    Piece movingKing = board.atPosition(this.getPosBefore()).getCopy();
    movingKing.setPosition(this.getPosAfter());
    board.placePiece(movingKing);
    board.removePiece(this.getPosBefore());
    rookMove.applyTo(board);
  }

  public String getIdentifier() {
    return this.getPosBefore().getNotation() + this.getPosAfter().getNotation();
  }
}
