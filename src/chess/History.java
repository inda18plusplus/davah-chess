package chess;

import chess.move.Move;

import java.util.ArrayList;

/** Implements the history of a chess board. */
public class History {

  private ArrayList<Move> moves;
  private int movesWithoutPawnMove;

  public History() {
    this.moves = new ArrayList<>();
    this.movesWithoutPawnMove = 0;
  }

  public void addMove(Move move) {
    moves.add(move);
  }

  /**
   * Calculates whether the piece at the specified position has moved during the game.
   *
   * @param position The position to investigate.
   * @return True if the piece has moved, false if the piece hasn't moved or the position is empty.
   */
  private boolean hasMoved(Position position) {
    for (Move move : moves) {
      if (move.involves(position)) {
        return true;
      }
    }
    return false;
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

  public int getMovesWithoutPawnMove() {
    return movesWithoutPawnMove;
  }
}
