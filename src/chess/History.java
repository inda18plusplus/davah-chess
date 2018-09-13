package chess;

import chess.move.Move;

import java.util.ArrayList;

public class History {

  private final Board initialBoard;
  private ArrayList<Move> moves;
  private int timeSincePawnMove;

  public History(Board initialBoard) {
    this.initialBoard = initialBoard;
  }

  public void addMove(Move move) {
    moves.add(move);
  }

  /**
   * Returns whether the piece at the specified position has moved during the game.
   * @param pos The position to investigate.
   * @return True if the piece has moved, false if the piece hasn't moved or the position is empty.
   */
  private boolean hasMoved(Position pos) {
    return true; //todo
  }

  public int getTimeSincePawnMove() {
    return timeSincePawnMove;
  }

}
