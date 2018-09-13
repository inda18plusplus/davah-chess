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
  void movementI() {
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
  void movementII() {
    game.setupStandardBoard();
    game.startGame();
    game.makeMove("e2e4");
    game.makeMove("e7e5");
    game.makeMove("b1c3");
    game.makeMove("a7a6");
    assertTrue(game.makeMove("g1f3"));
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

  @Test
  void foolsMate() {
    game.setupStandardBoard();
    game.startGame();
    game.makeMove("f2f3");
    game.makeMove("e7e5");
    game.makeMove("g2g4");
    game.makeMove("d8h4");
    assertEquals("Black has won!", game.viewState());
  }

  @Test
  void castlingTrue() {
    game.setupStandardBoard();
    game.startGame();
    game.makeMove("g2g3");
    game.makeMove("b7b6");
    game.makeMove("f1h3");
    game.makeMove("e7e6");
    game.makeMove("g1f3");
    game.makeMove("d8e7");
    assertTrue(game.makeMove("0-0"));
    game.makeMove("c8a6");
    game.makeMove("a2a3");
    game.makeMove("b8c6");
    game.makeMove("a1a2");
    assertTrue(game.makeMove("0-0-0"));
    assertEquals(
            "..kr.bnr\np.ppqppp\nbpn.p...\n........\n........\nP....NPB\nRPPPPP.P\n.NBQ.RK.\n",
            game.viewBoard()
    );
  }

}








