import chess.Game;

import java.util.Scanner;

public class Main {

  /**
   * Plays a chess game using the command line.
   *
   * @param args Not used.
   */
  public static void main(String[] args) {
    Game game = new Game();
    game.setupStandardBoard();
    game.startGame();

    Scanner inputScanner = new Scanner(System.in);
    while (inputScanner.hasNextLine()) {
      String move = inputScanner.nextLine();
      game.makeMove(move);
      System.out.println(game.viewBoard());
    }
  }
}