package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

import java.util.ArrayList;

public class Castling extends Move {

  private Position rookPosBefore;
  private Position rookPosAfter;
  private boolean queenside;

  public Castling(
      Position kingPosBefore,
      Position kingPosAfter,
      Position rookPosBefore,
      Position rookPosAfter,
      boolean queenside) {
    super(kingPosBefore, kingPosAfter);
    this.rookPosBefore = rookPosBefore;
    this.rookPosAfter = rookPosAfter;
    this.queenside = queenside;
  }

  @Override
  public String getNotation(ArrayList<Move> legalMoves, Board board) {
    if (queenside) {
      return "0-0-0";
    } else {
      return "0-0";
    }
  }

  @Override
  public boolean involves(Position position) {
    if (position.isEqual(rookPosBefore) || position.isEqual(rookPosAfter)) {
      return true;
    }
    return super.involves(position);
  }

  @Override
  public void applyTo(Board board) {
    super.applyTo(board);
    Piece rook = board.atPosition(rookPosBefore).getCopy();
    rook.setPosition(rookPosAfter);
    board.placePiece(rook);
    board.removePiece(rookPosBefore);
  }
}
