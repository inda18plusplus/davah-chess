package cnol.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChessApplication extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override public void start(Stage stage) {
    stage.setTitle("Chess");

    Button button = new Button();
    button.setText("Play");

    button.setOnAction(actionEvent -> {
      stage.setScene(new ChessScene(stage));
    });

    StackPane root = new StackPane();
    root.setBackground(new Background(new BackgroundFill(Color.rgb(44, 62, 80), CornerRadii.EMPTY,
        Insets.EMPTY)));
    root.getChildren().add(button);

    stage.setScene(new Scene(root, 800, 600, Color.rgb(44, 62, 80)));
    stage.show();
  }
}
