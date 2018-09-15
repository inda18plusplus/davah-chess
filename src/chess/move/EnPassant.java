package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

import java.util.ArrayList;

public class EnPassant extends Move {

  private Position capturedPawnPosition;

  public EnPassant(Position posBefore, Position posAfter, Position capturedPawnPosition) {
    super(posBefore, posAfter);
    this.capturedPawnPosition = capturedPawnPosition;
  }

  @Override
  public String getNotation(ArrayList<Move> legalMoves, Board board) {
    String notation = super.getNotation(legalMoves, board);
    notation += "e.p.";
    return notation;
  }

  @Override
  public boolean involves(Position position) {
    if (position.isEqual(capturedPawnPosition)) {
      return true;
    }
    return super.involves(position);
  }

  @Override
  public boolean isCapture(Board board) {
    return true;
  }

  @Override
  public void applyTo(Board board) {
    super.applyTo(board);
    board.removePiece(this.capturedPawnPosition);
  }

}