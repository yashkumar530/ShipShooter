package cs3500.pa03.model;

import java.util.List;

/**
 * Represents a ship in the game.
 */
public class Ship {
  private final ShipType shipType;
  private final List<Coord> coordinates;
  private boolean sunk;

  /**
   * Constructs a ship with the specified ship type and coordinates.
   *
   * @param shipType    the type of the ship
   * @param coordinates the list of coordinates occupied by the ship
   */
  public Ship(ShipType shipType, List<Coord> coordinates) {
    this.shipType = shipType;
    this.coordinates = coordinates;
    this.sunk = false;
  }

  /**
   * Returns the ship type.
   *
   * @return the ship type
   */
  public ShipType getShipType() {
    return shipType;
  }

  /**
   * Returns the list of coordinates occupied by the ship.
   *
   * @return the list of coordinates
   */
  public List<Coord> getCoordinates() {
    return coordinates;
  }

  /**
   * Checks if the ship is sunk.
   *
   * @return true if the ship is sunk, false otherwise
   */
  public boolean isSunk() {
    return sunk;
  }

  /**
   * Sets the sunk status of the ship.
   *
   * @param sunk the sunk status to set
   */
  public void setSunk(boolean sunk) {
    this.sunk = sunk;
  }

  /**
   * Returns the position of the ship.
   *
   * @return the position of the ship
   */
  public Position getPosition() {
    for (Coord c : this.coordinates) {
      if (this.coordinates.contains(new Coord(c.getXpos() + 1, c.getYpos()))
          || this.coordinates.contains(new Coord(c.getXpos() - 1, c.getYpos()))) {
        return Position.HORIZONTAL;
      }
    }
    return Position.VERTICAL;
  }
}