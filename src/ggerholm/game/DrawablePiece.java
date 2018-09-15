package ggerholm.game;

import chess.Position;
import java.awt.Graphics2D;
import java.awt.Image;

class DrawablePiece {

  private int size = SwingGame.SQUARE_SIZE;

  private float drawX;
  private float drawY;
  private Image image;
  private char asciiRepresentation;

  DrawablePiece(char asciiRepresentation, Position pos) {
    this.asciiRepresentation = asciiRepresentation;

    drawX = pos.getFile() * SwingGame.SQUARE_SIZE;
    drawY = pos.getRank() * SwingGame.SQUARE_SIZE;

    loadImage();
  }

  /**
   * Draws the piece.
   *
   * @param g The graphic's object to draw onto.
   */
  void draw(Graphics2D g) {
    int margin = (SwingGame.SQUARE_SIZE - size) / 2;

    g.drawImage(image,
        (int) drawX + margin,
        (int) drawY + margin,
        size, size, null);

  }

  private void loadImage() {
    String name;
    switch (asciiRepresentation) {
      case 'P':
      case 'p':
        name = "Pawn";
        break;
      case 'R':
      case 'r':
        name = "Rook";
        break;
      case 'Q':
      case 'q':
        name = "Queen";
        break;
      case 'K':
      case 'k':
        name = "King";
        break;
      case 'N':
      case 'n':
        name = "Knight";
        break;
      case 'B':
      case 'b':
        name = "Bishop";
        break;
      default:
        return;
    }

    image = ResourceManager.getInstance()
        .getImage((Character.isUpperCase(asciiRepresentation) ? "white" : "black") + name);

  }

}
