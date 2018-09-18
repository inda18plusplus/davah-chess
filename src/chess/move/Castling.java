package chess.move;

import chess.Board;
import chess.Position;
import chess.piece.Piece;

import java.util.ArrayList;

public class Castling extends Move {

  private Position rookPosBefore;
  private Position rookPosAfter;
  private boolean queenside;

  /**
   * Constructor for the Castling class.
   *
   * @param kingPosBefore The position of the king before the move.
   * @param kingPosAfter The position of the king after the move.
   * @param rookPosBefore The position of the rook before the move.
   * @param rookPosAfter The position of the rook after the move.
   * @param queenside True if queenside, false if kingside.
   */
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
