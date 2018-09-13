package chess.piece;

import chess.Board;
import chess.Game;
import chess.History;
import chess.Position;
import chess.move.Move;
import chess.move.RegularMove;

import java.util.ArrayList;

/** Implements a general chess piece. */
public abstract class Piece {

  private Position position;
  private Game.Player player;

  /**
   * Constructor for the Piece class.
   * @param position The position of the piece on the chess board.
   * @param player The controlling player, black or white.
   */
  public Piece(Position position, Game.Player player) {
    this.position = position;
    this.player = player;
  }

  /**
   * Abstract method to calculate all positions a piece can reach, except through castling.
   * @param board The board the piece is standing on.
   * @return A list of all possible moves for the piece.
   */
  public abstract ArrayList<Position> getReach(Board board);

  public abstract char toAsciiSymbol();

  public abstract Piece getCopy();

  /**
   * Calculates all possible moves for a piece.
   * @param board The board the piece is standing on.
   * @param history The history of that board (relevant to castling).
   * @return A list of all possible moves for the piece.
   */
  public ArrayList<Move> getMoves(Board board, History history) {
    ArrayList<Position> reach = this.getReach(board);
    ArrayList<Move> moves = new ArrayList<>();
    for (Position posAfter : reach) {
      Move move = new RegularMove(this.getPosition(), posAfter);
      Board boardCopy = board.getCopy();
      move.applyTo(boardCopy);
      if (!boardCopy.inCheck(this.getPlayer())) {
        moves.add(move);
      }
    }
    return moves;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Game.Player getPlayer() {
    return player;
  }

  public static Piece createPiece(Position position, char asciiPiece) {
    Game.Player player = Character.isUpperCase(asciiPiece) ? Game.Player.WHITE : Game.Player.BLACK;
    asciiPiece = Character.toLowerCase(asciiPiece);
    switch (asciiPiece) {
      case 'b':
        return new Bishop(position, player);
      case 'k':
        return new King(position, player);
      case 'n':
        return new Knight(position, player);
      case 'p':
        return new Pawn(position, player);
      case 'q':
        return new Queen(position, player);
      case 'r':
        return new Rook(position, player);
      default:
        return null;
    }
  }

}
