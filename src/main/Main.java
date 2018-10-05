import chess.Game;
import chess.Position;
import chess.network.Connection;
import java.util.Scanner;

public class Main {

  /**
   * Plays a chess game using the command line.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      fail();
    } else {
      switch (args[0]) {
        case "n":
          playNormalGame();
          break;
        case "l":
          if (args.length == 2) {
            listenForGame(Integer.parseInt(args[1]));
          }
          break;
        case "c":
          if (args.length == 3) {
            connectToGame(args[1], Integer.parseInt(args[2]));
          }
          break;
        default:
          fail();
      }
    }
  }

  private static void playNormalGame() {
    Game game = new Game();
    game.setupStandardBoard();
    game.startGame();
    System.out.println(game.viewBoard());
    System.out.println(game.viewState());

    Scanner inputScanner = new Scanner(System.in);
    while (inputScanner.hasNextLine()) {
      String move = inputScanner.nextLine();
      game.tryMakeMove(move);
      System.out.println(game.viewBoard());
      System.out.println(game.viewState());
    }
  }

  private static void listenForGame(int port) {
    Connection connection = new Connection(port);
    Game.Player player = connection.initAsHost();
    playConnectedGame(connection, player);
  }

  private static void connectToGame(String host, int port) {
    Connection connection = new Connection(host, port);
    Game.Player player = connection.initAsJoin();
    playConnectedGame(connection, player);
  }

  private static void playConnectedGame(Connection connection, Game.Player player) {
    Game game = new Game();
    game.setupStandardBoard();
    game.startGame();

    System.out.println("You are " + player);
    Scanner inputScanner = new Scanner(System.in);
    while (game.getState() == Game.State.PLAY) {
      if (game.getCurrentPlayer() == player) {
        String move = inputScanner.nextLine();
        Position from = new Position(move.substring(0, 2));
        Position to = new Position(move.substring(2, 4));
        if (game.tryMakeMove(from, to)) {
          connection.sendMove(move, "");
          if (connection.readResponseOk()) {
            System.out.println(game.viewBoard());
          } else {
            System.out.println("Opponent didn't accept move. Disconnecting.");
            connection.disconnect();
            return;
          }
        }
      } else {
        String move = connection.readMove();
        Position from = new Position(move.substring(0, 2));
        Position to = new Position(move.substring(2, 4));
        connection.sendResponse(game.tryMakeMove(from, to));
        System.out.println(game.viewBoard());
      }
    }
  }

  private static void fail() {
    System.out.println("The command line arguments could not be parsed.");
  }
}
