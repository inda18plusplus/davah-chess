package cnol.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Tile extends StackPane {
  private Color baseColor;
  private Color currentColor;
  private Image image;

  Tile(Color baseColor) {
    this.baseColor = baseColor;
    this.currentColor = baseColor;

    this.updateBackground();
  }

  /**
   * Sets the image to display on top of the tile.
   *
   * @param image The image.
   */
  public void setImage(Image image) {
    this.image = image;
    this.updateBackground();
  }

  private void updateBackground() {
    BackgroundFill[] fills = {new BackgroundFill(this.currentColor, null, null)};

    if (this.image != null) {
      BackgroundImage[] images = {new BackgroundImage(image, null, null, null,
          new BackgroundSize(80, 80, true, true, true, false))};

      this.setBackground(new Background(
          fills, images
      ));
    } else {
      this.setBackground(new Background(fills));
    }
  }


  /**
   * Highlights this piece with a given baseColor.
   *
   * @param markerColor The baseColor to use for the highlight.
   */
  public void highlight(Color markerColor) {
    if (markerColor != null) {
      this.currentColor = this.baseColor.interpolate(markerColor, 0.75);
    } else {
      this.currentColor = this.baseColor;
    }

    this.updateBackground();
  }
}

