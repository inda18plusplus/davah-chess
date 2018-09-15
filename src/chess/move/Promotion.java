package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

import java.util.ArrayList;

public class Promotion extends Move {

  private char promoteTo;

  public Promotion(Position posBefore, Position posAfter, char promoteTo) {
    super(posBefore, posAfter);
    this.promoteTo = promoteTo;
  }

  @Override
  public String getNotation(ArrayList<Move> legalMoves, Board board) {
    String notation = super.getNotation(legalMoves, board);
    notation += Character.toUpperCase(promoteTo);
    return notation;
  }

  @Override
  public void applyTo(Board board) {
    board.addToHistory(this);
    board.placePiece(Piece.createPiece(this.getPosAfter(), promoteTo));
    board.removePiece(this.getPosBefore());
  }

}
