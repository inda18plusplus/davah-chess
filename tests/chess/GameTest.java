package chess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

  private Game game;

  @BeforeEach
  void setUp() {
    game = new Game();
    game.setupStandardBoard();
    game.startGame();
  }

  @Test
  void movement() {
    assertFalse(game.makeMove("d5"));
    assertTrue(game.makeMove("d4"));
    assertEquals(
            "rnbqkbnr\npppppppp\n........\n........\n...P....\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard());
    assertSame(Game.Player.BLACK, game.getCurrentPlayer());
    assertFalse(game.makeMove("e4"));
    assertTrue(game.makeMove("e5"));
    assertEquals(
            "rnbqkbnr\npppp.ppp\n........\n....p...\n...P....\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard());
    assertSame(Game.Player.WHITE, game.getCurrentPlayer());
  }

  @Test
  void capture() {
    game.makeMove("d4");
    game.makeMove("e5");

    assertTrue(game.makeMove("dxe5"));
    assertEquals(
            "rnbqkbnr\npppp.ppp\n........\n....P...\n........\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard());
  }

  @Test
  void foolsMate() {
    game.makeMove("f3");
    game.makeMove("e5");
    game.makeMove("g4");
    game.makeMove("Qh4#");
    assertEquals(Game.State.BLACK_WIN, game.getState());
  }

  @Test
  void whereCanItMoveTo() {
    ArrayList<Position> reach1 = game.whereCanItMoveTo(new Position("c2"));
    ArrayList<Position> reach2 = game.whereCanItMoveTo(new Position("d2"));
    ArrayList<Position> reach3 = game.whereCanItMoveTo(new Position("d2"));
    ArrayList<Position> reach4 = game.whereCanItMoveTo(new Position("d3"));
    ArrayList<Position> reach5 = game.whereCanItMoveTo(new Position("e1"));
    ArrayList<Position> reach6 = game.whereCanItMoveTo(new Position("g1"));
    ArrayList<Position> reach7 = game.whereCanItMoveTo(new Position("g1"));
  }

  @Test
  void MovingWithPositions() {
    assertTrue(game.makeMove(new Position(1,0), new Position(2, 0)));
  }
}
