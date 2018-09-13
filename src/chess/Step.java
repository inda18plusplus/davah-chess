package chess;

/** Implements an arbitrary step on a chess board. */
public class Step {

  private int rankDiff;
  private int fileDiff;

  public Step(int rankDiff, int fileDiff) {
    this.rankDiff = rankDiff;
    this.fileDiff = fileDiff;
  }

  /**
   * Returns a new Position object one step removed from the given position.
   *
   * @param posBefore Where to start from.
   * @return The resulting position.
   */
  public Position applyOn(Position posBefore) {
    int rankAfter = posBefore.getRank() + rankDiff;
    int fileAfter = posBefore.getFile() + fileDiff;
    return new Position(rankAfter, fileAfter);
  }
}
