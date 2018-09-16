package cnol.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

class ChessScene extends Scene {

  private final Stage stage;
  private BorderPane root;
  private HBox topBar;

  private ChessBoard board;

  ChessScene(Stage stage) {
    this(new BorderPane(), stage);
  }

  private ChessScene(BorderPane root, Stage stage) {
    super(root, 800, 600, Color.rgb(44, 62, 80));
    this.root = root;
    this.stage = stage;
    this.createChessBoard();

    this.root.setStyle("-fx-background-color: #34495e");

    this.addTopBar();

    Button backButton = new Button("Main menu");

    Scene previousScene = stage.getScene();
    backButton.setOnAction(actionEvent -> stage.setScene(previousScene));


    Button resetButton = new Button("New game");
    resetButton.setOnAction(actionEvent -> {
      this.createChessBoard();
    });


    this.topBar.getChildren().add(backButton);
    this.topBar.getChildren().add(resetButton);
  }

  private void createChessBoard() {
    this.board = new ChessBoard();
    this.board.setAlignment(Pos.CENTER);

    this.root.setCenter(this.board);
  }

  private void addTopBar() {
    this.topBar = new HBox();
    this.topBar.setPadding(new Insets(20));
    this.topBar.setSpacing(20);
    this.topBar.setStyle("-fx-background-color: #2c3e50");

    this.root.setTop(this.topBar);
  }
}
