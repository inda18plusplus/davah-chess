package chess;

import static chess.Game.FILE_COUNT;
import static chess.Game.RANK_COUNT;

/**
 * Implements a position on a chess board, disregarding its content.
 */
public class Position {

  private int rank;
  private int file;

  public Position(int rank, int file) {
    this.rank = rank;
    this.file = file;
  }

  public boolean insideBoard() {
    boolean rankInsideBoard = 0 <= rank && rank < RANK_COUNT;
    boolean fileInsideBoard = 0 <= file && file < FILE_COUNT;
    return rankInsideBoard && fileInsideBoard;
  }

  public int getRank() {
    return rank;
  }

  public int getFile() {
    return file;
  }

  public boolean isEqual(Position otherPosition) {
    return otherPosition.rank == rank && otherPosition.file == file;
  }

  public Position getCopy() {
    return new Position(rank, file);
  }

  public static Position createPosition(String positionNotation) {
    if (positionNotation.matches("[a-f][1-8]")) {
      int rank = positionNotation.codePointAt(1) - (int) '1';
      int file = positionNotation.codePointAt(0) - (int) 'a';
      return new Position(rank, file);
    } else {
      return null;
    }
  }

  public String getNotation() {
    char fileNotation = (char) (file + (int) 'a');
    char rankNotation = (char) (rank + (int) '1');
    return "" + fileNotation + rankNotation;
  }

}
