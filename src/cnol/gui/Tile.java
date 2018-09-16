package cnol.gui;


import javafx.beans.binding.NumberBinding;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

  public void showMarker(Color markerColor) {
    if (markerColor != null) {
      Color newColor = this.color.interpolate(markerColor, 0.5);
      this.setBackgroundColor(newColor);
    } else {
      this.setBackgroundColor(this.color);
    }
  }
}
