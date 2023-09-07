package cs3500.pa03.model;

import cs3500.pa03.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an AI player in the game.
 */
public class AiPlayer implements Player {

  private final String name;
  public Board playerBoard;
  private final View view;
  public char[][] boardVisual;
  public char[][] boardHidden;

  /**
   * Constructs an AIPlayer with the given name and view.
   *
   * @param name the name of the AIPlayer
   * @param view the view used for displaying the game
   */
  public AiPlayer(String name, View view) {
    this.name = name;
    this.view = view;
    this.playerBoard = new Board();
  }

  /**
   * Returns the name of the AIPlayer.
   *
   * @return the name of the AIPlayer
   */
  @Override
  public String name() {
    return name;
  }

  /**
   * Sets up the player's fleet of ships based on the specified parameters.
   *
   * @param height        the height of the game board
   * @param width         the width of the game board
   * @param specifications the specifications for the fleet of ships
   * @return a list of ships in the fleet
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.playerBoard.height = height;
    this.playerBoard.width = width;

    List<Ship> fleet = new ArrayList<>();

    for (Map.Entry<ShipType, Integer> entry : specifications.entrySet()) {
      ShipType shipType = entry.getKey();
      int count = entry.getValue();

      for (int i = 0; i < count; i++) {
        List<Coord> shipCoords = generateShipCoordinates(height, width, shipType.getSize(), fleet);
        fleet.add(new Ship(shipType, shipCoords));
      }
    }
    return fleet;
  }

  /**
   * Generates a list of coordinates for a ship placement on the game board.
   *
   * @param height    the height of the game board
   * @param width     the width of the game board
   * @param shipSize  the size of the ship
   * @param fleet     the list of ships on the board
   * @return a list of coordinates representing the ship placement
   */
  private List<Coord> generateShipCoordinates(
      int height, int width, int shipSize, List<Ship> fleet) {
    Random random = new Random();
    boolean isVertical = random.nextBoolean();
    List<Coord> coords = new ArrayList<>();

    int x = random.nextInt(width);
    int y = random.nextInt(height);

    while (!isValidPlacement(isVertical, shipSize, x, y, height, width, fleet)) {
      x = random.nextInt(width);
      y = random.nextInt(height);
    }

    for (int i = 0; i < shipSize; i++) {
      if (isVertical) {
        coords.add(new Coord(x, y + i));
      } else {
        coords.add(new Coord(x + i, y));
      }
    }
    return coords;
  }

  /**
   * Checks if a ship placement is valid on the game board.
   *
   * @param isVertical whether the ship is placed vertically or horizontally
   * @param shipSize   the size of the ship
   * @param x          the x-coordinate of the ship's starting position
   * @param y          the y-coordinate of the ship's starting position
   * @param height     the height of the game board
   * @param width      the width of the game board
   * @param fleet      the list of ships on the board
   * @return true if the placement is valid, false otherwise
   */
  public boolean isValidPlacement(boolean isVertical, int shipSize, int x, int y, int height,
                                  int width, List<Ship> fleet) {
    if (isVertical) {
      if (y + shipSize > height) {
        return false;
      }
    } else {
      if (x + shipSize > width) {
        return false;
      }
    }

    for (Ship ship : fleet) {
      for (Coord coord : ship.getCoordinates()) {
        if (isVertical) {
          for (int i = 0; i < shipSize; i++) {
            if (coord.equals(new Coord(x, y + i))) {
              return false;
            }
          }
        } else {
          for (int i = 0; i < shipSize; i++) {
            if (coord.equals(new Coord(x + i, y))) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Returns a list of coordinates representing the shots taken by the AIPlayer.
   *
   * @return a list of coordinates representing the shots taken by the AIPlayer
   */
  @Override
  public List<Coord> takeShots() {
    int shipCount = 0;
    List<Coord> shots = new ArrayList<>();

    for (Ship ship : playerBoard.allShips) {
      if (!ship.isSunk()) {
        shipCount++;
      }
    }
    for (int i = 0; i < shipCount; i++) {
      int x = new Random().nextInt(playerBoard.width);
      int y = new Random().nextInt(playerBoard.height);
      if (playerBoard.allShots.contains(new Coord(x, y))) {
        i--;
      } else {
        shots.add(new Coord(x, y));
        playerBoard.allShots.add(new Coord(x, y));
      }
    }
    return shots;
  }

  /**
   * Reports the damage inflicted by the opponent's shots on the AIPlayer's ships.
   *
   * @param opponentShotsOnBoard the opponent's shots on the AIPlayer's board
   * @return a list of coordinates representing the hits
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Ship> fleet = this.playerBoard.allShips;
    List<Coord> hits = new ArrayList<>();

    for (Coord shot : opponentShotsOnBoard) {
      for (Ship ship : fleet) {
        if (ship.getCoordinates().contains(shot)) {
          hits.add(shot);
        }
      }
    }
    return hits;
  }

  /**
   * Updates the status of the AIPlayer's ships based on the opponent's hits.
   *
   * @param allOpponentHits the opponent's hits on the AIPlayer's ships
   */
  public void updateIsSunk(List<Coord> allOpponentHits) {
    for (Ship ship : this.playerBoard.allShips) {
      if (!ship.isSunk()) {
        boolean allCoordinatesHit = true;
        for (Coord coord : ship.getCoordinates()) {
          if (!allOpponentHits.contains(coord)) {
            allCoordinatesHit = false;
            break;
          }
        }
        if (allCoordinatesHit) {
          ship.setSunk(true);
        }
      }
    }
  }

  /**
   * Updates the AIPlayer's hidden board with successful hits on opponent's ships.
   *
   * @param shotsThatHitOpponentShips the shots that hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    for (Coord shot : shotsThatHitOpponentShips) {
      boardHidden[shot.getYpos()][shot.getXpos()] = 'X';
      boardVisual[shot.getYpos()][shot.getXpos()] = 'X';
    }
  }

  /**
   * Displays the end game result and reason using the provided view.
   *
   * @param result the result of the game
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    view.displayEndGame(result, reason);
  }
}
