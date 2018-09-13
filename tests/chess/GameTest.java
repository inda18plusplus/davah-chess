package chess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

  private Game game;

  @BeforeEach
  void setUp() {
    game = new Game();
  }

  @Test
  void standardBoard() {
    assertTrue(game.setupStandardBoard());
    assertEquals(
            "rnbqkbnr\npppppppp\n........\n........\n........\n........\nPPPPPPPP\nRNBQKBNR\n",
            game.viewBoard()
    );
  }

  @Test
  void startGame() {
    assertFalse(game.startGame()); // Both kings must be present.
    game.setupStandardBoard();
    assertTrue(game.startGame());
    assertFalse(game.startGame());
  }

  @Test
  void viewBoard() {
    assertEquals(
            "........\n........\n........\n........\n........\n........\n........\n........\n",
            game.viewBoard()
    );
    game.placePiece(1, 3, 'P');
    assertEquals(
            "........\n........\n........\n........\n........\n........\n...P....\n........\n",
            game.viewBoard()
    );
  }

  @Test
  void viewCurrentPlayer() {
    game.setupStandardBoard();
    game.startGame();
    assertEquals(game.viewCurrentPlayer(), "White");
  }


  @Test
  void movement() {
    game.setupStandardBoard();
    game.startGame();
    assertFalse(game.makeMove("d2d5"));
    assertTrue(game.makeMove("d2d4"));
    assertEquals(
            "rnbqkbnr\npppppppp\n........\n........\n...P....\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard()
    );
    assertSame(Game.Player.BLACK, game.getCurrentPlayer());
    assertFalse(game.makeMove("e2e4"));
    assertTrue(game.makeMove("e7e5"));
    assertEquals(
            "rnbqkbnr\npppp.ppp\n........\n....p...\n...P....\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard()
    );
    assertSame(Game.Player.WHITE, game.getCurrentPlayer());
  }

  @Test
  void capture() {
    game.setupStandardBoard();
    game.startGame();
    game.makeMove("d2d4");
    game.makeMove("e7e5");

    assertTrue(game.makeMove("d4e5"));
    assertEquals(
            "rnbqkbnr\npppp.ppp\n........\n....P...\n........\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard()
    );
  }

}