package chess;

import static chess.Game.FILE_COUNT;
import static chess.Game.RANK_COUNT;

import chess.move.Move;
import chess.piece.Piece;

import java.util.ArrayList;

/** Implements a chess board. */
public class Board {

  private Piece[][] board;
  private History history;

  public Board() {
    board = new Piece[RANK_COUNT][FILE_COUNT];
    history = new History();
  }

  public Piece[][] getBoard() {
    return board;
  }

  public History getHistory() {
    return history;
  }

  /**
   * Returns (but doesn't print) an ASCII representation of the board, as seen from White's
   * perspective.
   *
   * @return The string representing the board.
   */
  public String viewBoard() {
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

  /**
   * Gets the piece currently at a particular position.
   *
   * @param position Where to view the board.
   * @return Null if outside the board or position empty, otherwise the piece at that position.
   */
  public Piece atPosition(Position position) {
    if (!position.insideBoard()) {
      return null;
    }
    return board[position.getRank()][position.getFile()];
  }

  /**
   * Calculates whether the board is empty at a certain position.
   *
   * @param position The position.
   * @return Whether the board is empty at the position.
   */
  public boolean isEmpty(Position position) {
    if (!position.insideBoard()) {
      return false;
    }
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
    Game.Player oppositePlayer = Game.otherPlayer(player);
    return this.isPlayer(oppositePlayer, position);
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
   * Calculates all possible moves of a given player.
   *
   * @param player The player whose possible moves the function retrieves.
   * @return A list of all legal moves of the given player.
   */
  public ArrayList<Move> getMoves(Game.Player player) {
    ArrayList<Move> moves = new ArrayList<>();
    for (Piece piece : this.getPieces(player)) {
      moves.addAll(piece.getMoves(this));
    }
    return moves;
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
      // TODO: getReach skips special moves, which may be a problem in chess variants.
      for (Position position : piece.getReach(this)) {
        if (position.isEqual(kingPosition)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean inCheckMate(Game.Player player) {
    return this.inCheck(player) && (this.getMoves(player).size() == 0);
  }

  public boolean inStaleMate(Game.Player player) {
    return (!this.inCheck(player)) && (this.getMoves(player).size() == 0);
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

  //TODO: Change so usages use getHistory.
  public void addToHistory(Move move) {
    history.addMove(move);
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
    copy.history = this.history.getCopy();
    return copy;
  }
}
