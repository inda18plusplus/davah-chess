package chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
  void getCopy() {
    Position position = new Position(2, 3);
    Position compare = position.getCopy();
    assertTrue(position.isEqual(compare));
  }

  @Test
  void createPosition() {
    Position position = Position.createPosition("d2");
    Position compare = new Position(1, 3);
    assertTrue(position.isEqual(compare));
  }
}