package chess;

import chess.move.Move;

import java.util.ArrayList;

/**
 * Implements the history of a chess board.
 * TODO: Counting pawn moves
 * TODO: Three-time repetition rule
 */
public class History {

  private ArrayList<Move> moves;

  public History() {
    this.moves = new ArrayList<>();
  }

  /**
   * Returns the last executed move.
   *
   * @return That move.
   */
  public Move getLastMove() {
    if (moves.isEmpty()) {
      return null;
    }
    return moves.get(moves.size() - 1);
  }

  /**
   * Calculates whether the piece at the specified position has moved during the game.
   *
   * @param position The position to investigate.
   * @return True if the piece has moved, false if the piece hasn't moved or the position is empty.
   */
  public boolean hasMoved(Position position) {
    for (Move move : moves) {
      if (move.involves(position)) {
        return true;
      }
    }
    return false;
  }

  public void addMove(Move move) {
    moves.add(move);
  }

  /**
   * Constructs a shallow copy of this.
   *
   * @return The copy.
   */
  public History getCopy() {
    History copy = new History();
    for (Move move : moves) {
      copy.addMove(move);
    }
    return copy;
  }

}
