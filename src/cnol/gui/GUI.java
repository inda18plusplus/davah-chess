package cnol.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUI extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override public void start(Stage stage) throws Exception {
    stage.setTitle("Chess");

    Button button = new Button();
    button.setText("Play");

    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent actionEvent) {
        System.out.println("Hello Chess!");
      }
    });

    StackPane root = new StackPane();
    root.getChildren().add(button);

    stage.setScene(new Scene(root, 800, 600));
    stage.show();
  }
}
