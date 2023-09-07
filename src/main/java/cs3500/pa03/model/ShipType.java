package cs3500.pa03.model;

/**
 * Represents the types of ships in the game.
 */
public enum ShipType {
  CARRIER(6, 'C'),
  BATTLESHIP(5, 'B'),
  DESTROYER(4, 'D'),
  SUBMARINE(3, 'S');

  private final int size;
  private final char identifier;

  /**
   * Constructs a ship type with the specified size and identifier.
   *
   * @param size       the size of the ship type
   * @param identifier the identifier character of the ship type
   */
  ShipType(int size, char identifier) {
    this.size = size;
    this.identifier = identifier;
  }

  /**
   * Returns the size of the ship type.
   *
   * @return the size of the ship type
   */
  public int getSize() {
    return size;
  }

  /**
   * Returns the identifier character of the ship type.
   *
   * @return the identifier character
   */
  public char getIdentifier() {
    return identifier;
  }
}
