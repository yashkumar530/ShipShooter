package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a game board.
 */
public class Board {

  public int height;
  public int width;
  public List<Ship> allShips;
  public List<Coord> allShots;

  /**
   * Constructs a Board object with an empty list of ships and shots.
   */
  public Board() {
    this.allShips = new ArrayList<>();
    this.allShots = new ArrayList<>();
  }

  /**
   * Returns a 2D array representing the data of the board.
   * '-' represents empty cells, and ship identifiers represent occupied cells.
   *
   * @return a 2D char array representing the data of the board
   */
  public char[][] getBoardData() {
    char[][] boardData = new char[height][width];
    for (int i = 0; i < height; i++) {
      Arrays.fill(boardData[i], '-');
    }
    for (Ship ship : allShips) {
      for (Coord coord : ship.getCoordinates()) {
        boardData[coord.getYpos()][coord.getXpos()] = ship.getShipType().getIdentifier();
      }
    }

    return boardData;
  }
}