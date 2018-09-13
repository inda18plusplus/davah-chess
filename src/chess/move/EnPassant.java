package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

public class EnPassant extends Move {

  private Position capturedPawnPosition;

  public EnPassant(Position posBefore, Position posAfter, Position capturedPawnPosition) {
    super(posBefore, posAfter);
    this.capturedPawnPosition = capturedPawnPosition;
  }

  public boolean applyTo(Board board) {
    Piece movingPawn = board.atPosition(this.getPosBefore()).getCopy();
    movingPawn.setPosition(this.getPosAfter());
    board.placePiece(movingPawn);
    board.removePiece(this.getPosBefore());
    board.removePiece(this.capturedPawnPosition);
    return true;
  }

}