package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

public class RegularMove extends Move {

  public RegularMove(Position posBefore, Position posAfter) {
    super(posBefore, posAfter);
    this.identifier = posBefore.getNotation() + posAfter.getNotation();
  }

  public boolean applyTo(Board board) {
    Piece movingPiece = board.atPosition(this.getPosBefore()).getCopy();
    movingPiece.setPosition(this.getPosAfter());
    board.placePiece(movingPiece);
    board.removePiece(this.getPosBefore());
    return true;
  }

}
