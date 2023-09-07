package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the Board class
 */
public class BoardTest {
  private Board board;

  @BeforeEach
  void setUp() {
    board = new Board();
  }

  @Test
  void testGetBoardData_emptyBoard() {
    board.height = 3;
    board.width = 3;
    char[][] expectedData = {
        {'-', '-', '-'},
        {'-', '-', '-'},
        {'-', '-', '-'}
    };
    assertArrayEquals(expectedData, board.getBoardData());
  }

  @Test
  void testGetBoardData_withShips() {
    board.height = 5;
    board.width = 5;
    Ship destroyer = new Ship(ShipType.DESTROYER, Arrays.asList(
        new Coord(1, 1), new Coord(2, 1), new Coord(3, 1)));
    Ship carrier = new Ship(ShipType.CARRIER, Arrays.asList(
        new Coord(0, 0), new Coord(0, 1), new Coord(0, 2),
        new Coord(0, 3), new Coord(0, 4)));

    board.allShips.add(destroyer);
    board.allShips.add(carrier);

    char[][] expectedData = {
        {'C', '-', '-', '-', '-'},
        {'C', 'D', 'D', 'D', '-'},
        {'C', '-', '-', '-', '-'},
        {'C', '-', '-', '-', '-'},
        {'C', '-', '-', '-', '-'}
    };
    assertArrayEquals(expectedData, board.getBoardData());
  }
}
