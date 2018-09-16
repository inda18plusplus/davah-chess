package cnol.gui;

import javafx.beans.binding.NumberBinding;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Tile extends StackPane {
  NumberBinding size;
  Color color;

  Tile(Color color, NumberBinding size) {
    this.setBackgroundColor(color);
    this.size = size;
    this.prefWidthProperty().bind(size);
    this.prefHeightProperty().bind(size);

    this.color = color;
  }

  private void setBackgroundColor(Color color) {
    super.setBackground(new Background(new BackgroundFill(color, null, null)));
  }

  /**
   * Sets the image to display on top of the tile.
   * @param image The image.
   */
  public void setImage(ImageView image) {
    this.getChildren().removeIf(node -> node instanceof ImageView);

    if (image != null) {
      image.fitWidthProperty().bind(this.size.multiply(0.9));
      image.fitHeightProperty().bind(this.size.multiply(0.9));

      image.minWidth(0.0);
      image.minHeight(0.0);

      this.getChildren().add(image);
    }
  }


  /**
   * Highlights this piece with a given color.
   * @param markerColor The color to use for the highlight.
   */
  public void highlight(Color markerColor) {
    if (markerColor != null) {
      Color newColor = this.color.interpolate(markerColor, 0.75);
      this.setBackgroundColor(newColor);
    } else {
      this.setBackgroundColor(this.color);
    }
  }
}
