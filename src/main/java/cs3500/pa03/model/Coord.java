package cs3500.pa03.model;

import java.util.Objects;

/**
 * Represents a coordinate on a game board.
 */
public class Coord {

  private final int xpos;
  private final int ypos;

  /**
   * Constructs a Coord object with the specified x and y coordinates.
   *
   * @param xpos the x-coordinate
   * @param ypos the y-coordinate
   */
  public Coord(int xpos, int ypos) {
    this.xpos = xpos;
    this.ypos = ypos;
  }

  /**
   * Returns the x-coordinate.
   *
   * @return the x-coordinate
   */
  public int getXpos() {
    return xpos;
  }

  /**
   * Returns the y-coordinate.
   *
   * @return the y-coordinate
   */
  public int getYpos() {
    return ypos;
  }

  /**
   * Checks if this Coord object is equal to another object.
   * Two Coord objects are considered equal if they have the same x and y coordinates.
   *
   * @param obj the object to compare to
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Coord other = (Coord) obj;
    return xpos == other.xpos && ypos == other.ypos;
  }

  /**
   * Returns the hash code of this Coord object.
   *
   * @return the hash code of the object
   */
  @Override
  public int hashCode() {
    return Objects.hash(xpos, ypos);
  }

  /**
   * Returns a string representation of this Coord object.
   * The string representation is in the format "(x, y)".
   *
   * @return a string representation of the object
   */
  @Override
  public String toString() {
    return "(" + xpos + ", " + ypos + ")";
  }
}