package chess;

import static chess.Game.FILE_COUNT;
import static chess.Game.RANK_COUNT;

import java.util.Objects;

/** Implements a position on a chess board, disregarding its content. */
public class Position {

  private int rank;
  private int file;

  public Position(int rank, int file) {
    this.rank = rank;
    this.file = file;
  }

  /**
   * Creates a position object from its usual name.
   *
   * @param notation The name.
   */
  public Position(String notation) {
    if (notation.matches("[a-h][1-8]")) {
      this.rank = notation.charAt(1) - '1';
      this.file = notation.charAt(0) - 'a';
    } else {
      this.rank = 0;
      this.file = 0;
    }
  }

  public int getRank() {
    return rank;
  }

  public int getFile() {
    return file;
  }

  /**
   * Converts the position into its usual name.
   *
   * @return The name.
   */
  public String getNotation() {
    char fileNotation = (char) (file + (int) 'a');
    char rankNotation = (char) (rank + (int) '1');
    return "" + fileNotation + rankNotation;
  }

  /**
   * Calculates whether this position is inside the chess board.
   *
   * @return Whether this position is inside the chess board.
   */
  public boolean insideBoard() {
    boolean rankInsideBoard = 0 <= rank && rank < RANK_COUNT;
    boolean fileInsideBoard = 0 <= file && file < FILE_COUNT;
    return rankInsideBoard && fileInsideBoard;
  }

  public boolean isEqual(Position otherPosition) {
    return otherPosition.rank == rank && otherPosition.file == file;
  }

  @Override public int hashCode() {
    return Objects.hash(this.file, this.rank);
  }

  @Override public boolean equals(Object o) {
    if (o instanceof Position) {
      Position p = (Position) o;

      return p.isEqual(this);
    }

    return false;
  }

  @Override public String toString() {
    return "[" + this.file + ", " + this.rank + "]";
  }
}
