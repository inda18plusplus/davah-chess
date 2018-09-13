package chess;

import static chess.Game.FILE_COUNT;
import static chess.Game.RANK_COUNT;

import chess.move.Move;
import chess.piece.Piece;

import java.util.ArrayList;

/**
 * Implements a chess board.
 */
public class Board {

  private Piece[][] board;

  public Board() {
    board = new Piece[RANK_COUNT][FILE_COUNT];
  }

  public boolean placePiece(Piece piece) {
    Position position = piece.getPosition();
    if (!position.insideBoard()) {
      return false;
    }
    board[position.getRank()][position.getFile()] = piece;
    return true;
  }

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

  public boolean isPlayer(Game.Player player, Position position) {
    if (this.isEmpty(position)) {
      return false;
    }
    return this.atPosition(position).getPlayer() == player;
  }

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

  public ArrayList<Move> getMoves(Game.Player player, History history) {
    ArrayList<Move> moves = new ArrayList<>();
    for (Piece piece : this.getPieces(player)) {
      moves.addAll(piece.getMoves(this, history));
    }
    return moves;
  }

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

  public String displayAsciiBoard() { //todo: upside down?
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
