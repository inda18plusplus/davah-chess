import chess.Game;

import java.util.Scanner;

public class Main {

  /**
   * Plays a chess game using the command line.
   */
  public static void main(String[] args) {
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
}
