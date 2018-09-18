package chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PositionTest {

  @Test
  void insideBoardTrue() {
    Position position1 = new Position(0, 0);
    Position position2 = new Position(2, 4);
    Position position3 = new Position(7, 7);
    assertTrue(position1.insideBoard() && position2.insideBoard() && position3.insideBoard());
  }

  @Test
  void insideBoardFalse() {
    Position position1 = new Position(-2, 4);
    Position position2 = new Position(3, 8);
    Position position3 = new Position(8, 0);
    assertFalse(position1.insideBoard() || position2.insideBoard() || position3.insideBoard());
  }

  @Test
  void getRank() {
    Position position = new Position(3, 4);
    assertEquals(3, position.getRank());
  }

  @Test
  void getFile() {
    Position position = new Position(3, 4);
    assertEquals(4, position.getFile());
  }

  @Test
  void isEqual() {
    Position position = new Position(2, 3);
    Position compare = new Position(2, 3);
    assertTrue(position.isEqual(compare));
  }

  @Test
  void positionNotation() {
    Position position = new Position("f3");
    Position compare = new Position(2, 5);
    assertTrue(position.isEqual(compare));
    assertEquals("f3", compare.getNotation());
  }

}