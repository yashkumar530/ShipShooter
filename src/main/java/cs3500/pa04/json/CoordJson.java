package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a coordinate in JSON format.
 */
public record CoordJson(
    @JsonProperty("x") int x,
    @JsonProperty("y") int y) {

  /**
   * Returns the x-coordinate.
   *
   * @return the x-coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y-coordinate.
   *
   * @return the y-coordinate
   */
  public int getY() {
    return y;
  }
}
