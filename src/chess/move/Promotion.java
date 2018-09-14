package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

public class Promotion extends Move {

  private char promoteTo;

  public Promotion(Position posBefore, Position posAfter, char promoteTo) {
    super(posBefore, posAfter);
    this.promoteTo = promoteTo;
  }

  public void applyTo(Board board) {
    board.placePiece(Piece.createPiece(this.getPosAfter(), promoteTo));
    board.removePiece(this.getPosBefore());
  }

  public String getIdentifier() {
    return this.getPosBefore().getNotation() + this.getPosAfter().getNotation() + promoteTo;
  }

}
