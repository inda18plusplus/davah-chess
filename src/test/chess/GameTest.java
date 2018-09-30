package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class GameTest {

  private Game game;

  @Test
  public void movement() {
    game = new Game();
    game.setupStandardBoard();
    game.startGame();
    assertFalse(game.tryMakeMove("d5"));
    assertTrue(game.tryMakeMove("d4"));
    assertEquals(
            "rnbqkbnr\npppppppp\n........\n........\n...P....\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard());
    assertSame(Game.Player.BLACK, game.getCurrentPlayer());
    assertFalse(game.tryMakeMove("e4"));
    assertTrue(game.tryMakeMove("e5"));
    assertEquals(
            "rnbqkbnr\npppp.ppp\n........\n....p...\n...P....\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard());
    assertSame(Game.Player.WHITE, game.getCurrentPlayer());
  }

  @Test
  public void capture() {
    game = new Game();
    game.setupStandardBoard();
    game.startGame();
    game.tryMakeMove("d4");
    game.tryMakeMove("e5");

    assertTrue(game.tryMakeMove("dxe5"));
    assertEquals(
            "rnbqkbnr\npppp.ppp\n........\n....P...\n........\n........\nPPP.PPPP\nRNBQKBNR\n",
            game.viewBoard());
  }

  @Test
  public void foolsMate() {
    game = new Game();
    game.setupStandardBoard();
    game.startGame();
    game.tryMakeMove("f3");
    game.tryMakeMove("e5");
    game.tryMakeMove("g4");
    game.tryMakeMove("Qh4#");
    assertEquals(Game.State.BLACK_WIN, game.getState());
  }

  @Test
  public void whereCanItMoveTo() {
    game = new Game();
    game.setupStandardBoard();
    game.startGame();
    ArrayList<Position> reach1 = game.whereCanItMoveTo(new Position("c2"));
    ArrayList<Position> reach2 = game.whereCanItMoveTo(new Position("d2"));
    ArrayList<Position> reach3 = game.whereCanItMoveTo(new Position("d2"));
    ArrayList<Position> reach4 = game.whereCanItMoveTo(new Position("d3"));
    ArrayList<Position> reach5 = game.whereCanItMoveTo(new Position("e1"));
    ArrayList<Position> reach6 = game.whereCanItMoveTo(new Position("g1"));
    ArrayList<Position> reach7 = game.whereCanItMoveTo(new Position("g1"));
  }

  @Test
  public void movingWithPositions() {
    game = new Game();
    game.setupStandardBoard();
    game.startGame();
    assertTrue(game.tryMakeMove(new Position(1,0), new Position(2, 0)));
    assertTrue(game.tryMakeMove(new Position(6,2), new Position(4, 2)));
    assertFalse(game.tryMakeMove(new Position(6,4), new Position(4, 4)));
  }

}