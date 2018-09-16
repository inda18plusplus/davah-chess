package chess.piece;

import chess.Board;
import chess.Game;
import chess.Position;
import chess.Step;
import chess.move.EnPassant;
import chess.move.Move;
import chess.move.Promotion;

import java.util.ArrayList;

import static chess.Game.Player.BLACK;
import static chess.Game.Player.WHITE;
import static chess.Game.RANK_COUNT;

/** Implements a specific chess piece, the pawn. */
public class Pawn extends Piece {

  private static final Step WHITE_FWD        = new Step(1, 0);
  private static final Step WHITE_DOUBLE_FWD = new Step(2, 0);
  private static final Step WHITE_LEFT       = new Step(1, -1);
  private static final Step WHITE_RIGHT      = new Step(1, 1);
  private static final Step BLACK_FWD        = new Step(-1, 0);
  private static final Step BLACK_DOUBLE_FWD = new Step(-2, 0);
  private static final Step BLACK_LEFT       = new Step(-1, 1);
  private static final Step BLACK_RIGHT      = new Step(-1, -1);

  public Pawn(Position position, Game.Player player) {
    super(position, player);
  }

  public char toAsciiSymbol() {
    return (this.getPlayer() == Game.Player.WHITE) ? 'P' : 'p';
  }

  public ArrayList<Position> getReach(Board board) {
    Game.Player player = this.getPlayer();
    Position posBefore = this.getPosition();

    Position posForward   = (player == Game.Player.WHITE)
            ? WHITE_FWD.applyOn(posBefore)
            : BLACK_FWD.applyOn(posBefore);
    Position posDoubleFwd = (player == Game.Player.WHITE)
            ? WHITE_DOUBLE_FWD.applyOn(posBefore)
            : BLACK_DOUBLE_FWD.applyOn(posBefore);
    Position posLeft      = (player == Game.Player.WHITE)
            ? WHITE_LEFT.applyOn(posBefore)
            : BLACK_LEFT.applyOn(posBefore);
    Position posRight     = (player == Game.Player.WHITE)
            ? WHITE_RIGHT.applyOn(posBefore)
            : BLACK_RIGHT.applyOn(posBefore);

    ArrayList<Position> reach = new ArrayList<>();
    if (posForward.insideBoard()) { // Always enters under normal chess rules, due to promotion.
      if (board.isEmpty(posForward)) {
        reach.add(posForward);
        int doubleMoveRank = (player == Game.Player.WHITE) ? 1 : RANK_COUNT - 2;
        if (posBefore.getRank() == doubleMoveRank) {
          if (board.isEmpty(posDoubleFwd)) {
            reach.add(posDoubleFwd);
          }
        }
      }
    }
    if (posLeft.insideBoard()) {
      if (board.isOpposite(player, posLeft)) {
        reach.add(posLeft);
      }
    }
    if (posRight.insideBoard()) {
      if (board.isOpposite(player, posRight)) {
        reach.add(posRight);
      }
    }
    return reach;
  }

  public Piece getCopy() {
    return new Pawn(this.getPosition(), this.getPlayer());
  }

  /**
   * Overrides Piece's getMoves() adding en passant moves and promotions.
   *
   * @param board The board the piece is standing on.
   * @return A list of all possible moves for the piece.
   */
  @Override
  public ArrayList<Move> getMoves(Board board) {
    ArrayList<Move> moves = super.getMoves(board);
    Move lastMove = board.getHistory().getLastMove();
    moves.addAll(this.enPassantMoves(board, lastMove));
    return this.replaceEligiblePromotions(moves);
  }

  private ArrayList<Move> enPassantMoves(Board board, Move lastMove) {
    ArrayList<Move> moves = new ArrayList<>();
    if (lastMove == null) {
      return moves;
    }
    Piece lastMovedPiece = board.atPosition(lastMove.getPosAfter());
    char lookingFor = (this.getPlayer() == Game.Player.WHITE) ? 'p' : 'P';
    if (lastMovedPiece.toAsciiSymbol() != lookingFor) {
      return moves;
    }
    int rankBeforeLastMove = lastMove.getPosBefore().getRank();
    int rankAfterLastMove = lastMove.getPosAfter().getRank();
    if (Math.abs(rankAfterLastMove - rankBeforeLastMove) != 2) {
      return moves;
    }
    if (lastMovedPiece.getPosition().getRank() != this.getPosition().getRank()) {
      return moves;
    }
    if (Math.abs(lastMovedPiece.getPosition().getFile() - this.getPosition().getFile()) != 1) {
      return moves;
    }
    int forwardRankDiff = (this.getPlayer() == Game.Player.WHITE) ? 1 : -1;
    int enPassantToRank = lastMovedPiece.getPosition().getRank() + forwardRankDiff;
    int enPassantToFile = lastMovedPiece.getPosition().getFile();
    Position enPassantTo = new Position(enPassantToRank, enPassantToFile);
    Move enPassant = new EnPassant(this.getPosition(), enPassantTo, lastMovedPiece.getPosition());
    moves.add(enPassant);
    return moves;
  }

  private ArrayList<Move> replaceEligiblePromotions(ArrayList<Move> moves) {
    ArrayList<Move> promotedMoves = new ArrayList<>();
    int promoteAtRank = (this.getPlayer() == BLACK) ? 0 : RANK_COUNT - 1;
    for (Move move : moves) {
      if (move.getPosAfter().getRank() != promoteAtRank) {
        promotedMoves.add(move);
        continue;
      }
      Position posBefore = move.getPosBefore();
      Position posAfter = move.getPosAfter();
      char bishop = (this.getPlayer() == WHITE) ? 'B' : 'b';
      promotedMoves.add(new Promotion(posBefore, posAfter, bishop));
      char rook = (this.getPlayer() == WHITE) ? 'R' : 'r';
      promotedMoves.add(new Promotion(posBefore, posAfter, rook));
      char knight = (this.getPlayer() == WHITE) ? 'N' : 'n';
      promotedMoves.add(new Promotion(posBefore, posAfter, knight));
      char queen = (this.getPlayer() == WHITE) ? 'Q' : 'q';
      promotedMoves.add(new Promotion(posBefore, posAfter, queen));
    }
    return promotedMoves;
  }

}