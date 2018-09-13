package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

public class Promotion extends Move {

  private Piece promoteTo;

  public Promotion(Position posBefore, Position posAfter, Piece promoteTo) {
    super(posBefore, posAfter);
    this.promoteTo = promoteTo;
  }

  public void applyTo(Board board) {
    board.placePiece(promoteTo);
    board.removePiece(this.getPosBefore());
  }

  public String getIdentifier() {
    return this.getPosBefore().getNotation() + this.getPosAfter().getNotation() + promoteTo.toAsciiSymbol();
  }

}
