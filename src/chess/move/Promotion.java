package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

public class Promotion extends Move {

  private Piece promoteTo;

  public Promotion(Position posBefore, Position posAfter, Piece promoteTo) {
    super(posBefore, posAfter);
    this.identifier = posBefore.getNotation() + posAfter.getNotation() + promoteTo.toAsciiSymbol();
    this.promoteTo = promoteTo;
  }

  public boolean applyTo(Board board) {
    board.placePiece(promoteTo);
    board.removePiece(this.getPosBefore());
    return true;
  }

}
