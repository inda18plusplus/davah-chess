package chess;

import static chess.Game.FILE_COUNT;
import static chess.Game.RANK_COUNT;

import chess.move.Move;
import chess.piece.Piece;

import java.util.ArrayList;

/** Implements a chess board. */
public class Board {

  /** Stores the pieces on the board. Empty squares have null. */
  private Piece[][] board;

  public Board() {
    board = new Piece[RANK_COUNT][FILE_COUNT];
  }

  /**
   * Places a piece on the chess board.
   *
   * @param piece The piece to be placed (this contains position information too).
   * @return Whether the placement was successful.
   */
  public boolean placePiece(Piece piece) {
    Position position = piece.getPosition();
    if (!position.insideBoard()) {
      return false;
    }
    board[position.getRank()][position.getFile()] = piece;
    return true;
  }

  /**
   * Removes a piece from the chessboard at a given position.
   *
   * @param position The position to remove at.
   * @return Whether the removal was successful.
   */
  public boolean removePiece(Position position) {
    if (!position.insideBoard() || this.isEmpty(position)) {
      return false;
    }
    board[position.getRank()][position.getFile()] = null;
    return true;
  }

  public Piece atPosition(Position position) {
    return board[position.getRank()][position.getFile()];
  }

  public boolean isEmpty(Position position) {
    return this.atPosition(position) == null;
  }

  /**
   * Calculates whether the given player occupies the given position.
   *
   * @param player The player to check for.
   * @param position The position to check.
   * @return Whether the given player occupies the given position.
   */
  public boolean isPlayer(Game.Player player, Position position) {
    if (this.isEmpty(position)) {
      return false;
    }
    return this.atPosition(position).getPlayer() == player;
  }

  /**
   * Calculates whether the opposite player occupies the given position.
   *
   * @param player The player whose opposite to check for.
   * @param position The position to check.
   * @return Whether the opposite player occupies the given position.
   */
  public boolean isOpposite(Game.Player player, Position position) {
    switch (player) {
      case BLACK:
        return this.isPlayer(Game.Player.WHITE, position);
      case WHITE:
        return this.isPlayer(Game.Player.BLACK, position);
      default:
        return false;
    }
  }

  /**
   * Finds the king of a given color.
   *
   * @param player The player whose king to find.
   * @return The position of the king, null if there are none or multiple such positions.
   */
  public Position findKing(Game.Player player) {
    char lookingFor = (player == Game.Player.BLACK) ? 'k' : 'K';
    boolean foundIt = false;
    Position position = null;
    for (Piece[] rank : board) {
      for (Piece piece : rank) {
        if (piece == null) {
          continue;
        }
        if (piece.toAsciiSymbol() == lookingFor) {
          if (foundIt) {
            return null;
          } else {
            position = piece.getPosition();
            foundIt = true;
          }
        }
      }
    }
    return position;
  }

  /**
   * Calculates all possible moves of a given player, given the history of the game.
   *
   * @param player The player whose possible moves the function retrieves.
   * @param history The history of the game, including all past moves.
   * @return A list of all legal moves of the given player.
   */
  public ArrayList<Move> getMoves(Game.Player player, History history) {
    ArrayList<Move> moves = new ArrayList<>();
    for (Piece piece : this.getPieces(player)) {
      moves.addAll(piece.getMoves(this, history));
    }
    return moves;
  }

  /**
   * Calculates whether the given player is in check.
   *
   * @param player The player to investigate.
   * @return Whether the player is in check.
   */
  public boolean inCheck(Game.Player player) {
    Game.Player otherPlayer = Game.otherPlayer(player);
    Position kingPosition = this.findKing(player);
    if (kingPosition == null) {
      return false;
    }
    for (Piece piece : this.getPieces(otherPlayer)) {
      for (Position position : piece.getReach(this)) {
        if (position.isEqual(kingPosition)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Retrieves all pieces of a particular player.
   *
   * @param player The player whose pieces to retrieve.
   * @return The player's pieces.
   */
  public ArrayList<Piece> getPieces(Game.Player player) {
    ArrayList<Piece> pieces = new ArrayList<>();
    for (Piece[] rank : board) {
      for (Piece piece : rank) {
        if (piece == null) {
          continue;
        }
        if (piece.getPlayer() == player) {
          pieces.add(piece);
        }
      }
    }
    return pieces;
  }

  /**
   * Creates a deep copy of the board.
   *
   * @return The copy.
   */
  public Board getCopy() {
    Board copy = new Board();
    for (Piece[] rank : board) {
      for (Piece piece : rank) {
        if (piece == null) {
          continue;
        }
        copy.placePiece(piece.getCopy());
      }
    }
    return copy;
  }

  /**
   * Returns (but doesn't print) an ASCII representation of the board, as seen from White's
   * perspective.
   *
   * @return The string representing the board.
   */
  public String displayAsciiBoard() {
    StringBuilder asciiBoard = new StringBuilder();
    for (int i = RANK_COUNT - 1; i >= 0; i--) {
      for (int j = 0; j < FILE_COUNT; j++) {
        Position position = new Position(i, j);
        if (isEmpty(position)) {
          asciiBoard.append('.');
        } else {
          asciiBoard.append(board[i][j].toAsciiSymbol());
        }
      }
      asciiBoard.append('\n');
    }
    return asciiBoard.toString();
  }
}
