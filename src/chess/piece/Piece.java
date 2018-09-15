package chess.piece;

import chess.Board;
import chess.Game;
import chess.History;
import chess.Position;
import chess.move.Move;

import java.util.ArrayList;

/** Implements a general chess piece. */
public abstract class Piece {

  private Position position;
  private Game.Player player;

  /**
   * Constructor for the Piece class.
   *
   * @param position The position of the piece on the chess board.
   * @param player The controlling player, black or white.
   */
  public Piece(Position position, Game.Player player) {
    this.position = position;
    this.player = player;
  }

  /**
   * Creates a piece based on an ascii identifier. Upper case = white, lower case = black.
   *
   * @param position The position of the piece.
   * @param asciiPiece A character identifying the type and ownership of the piece.
   * @return The piece.
   */
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

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Game.Player getPlayer() {
    return player;
  }

  /**
   * Abstract method to convert the piece to an ascii character according to type and ownership.
   *
   * @return The ascii symbol.
   */
  public abstract char toAsciiSymbol();

  /**
   * Abstract method to calculate all positions a piece can reach, except through castling or en
   * passant. Whether the own king is placed in check is also disregarded.
   *
   * @param board The board the piece is standing on.
   * @return A list of all possible moves for the piece.
   */
  public abstract ArrayList<Position> getReach(Board board);

  public abstract Piece getCopy();

  /**
   * Calculates all possible moves for a piece.
   *
   * @param board The board the piece is standing on.
   * @return A list of all possible moves for the piece.
   */
  public ArrayList<Move> getMoves(Board board) {
    ArrayList<Position> reach = this.getReach(board);
    ArrayList<Move> moves = new ArrayList<>();
    for (Position posAfter : reach) {
      Move move = new Move(this.getPosition(), posAfter);
      Board boardCopy = board.getCopy();
      move.applyTo(boardCopy);
      if (!boardCopy.inCheck(this.getPlayer())) {
        moves.add(move);
      }
    }
    return moves;
  }
}
