package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Ship class.
 */
public class ShipTest {
  private Ship ship;
  private List<Coord> coordinates;

  @BeforeEach
  void setUp() {
    coordinates = Arrays.asList(new Coord(1, 1), new Coord(2, 2), new Coord(3, 3));
    ship = new Ship(ShipType.DESTROYER, coordinates);
  }

  @Test
  void testGetShipType() {
    assertEquals(ShipType.DESTROYER, ship.getShipType());
  }

  @Test
  void testGetCoordinates() {
    assertEquals(coordinates, ship.getCoordinates());
  }

  @Test
  void testIsSunk_initiallyFalse() {
    assertFalse(ship.isSunk());
  }

  @Test
  void testSetSunk_true() {
    ship.setSunk(true);
    assertTrue(ship.isSunk());
  }

  @Test
  void testSetSunk_false() {
    ship.setSunk(true); // set to true first
    ship.setSunk(false); // then set to false
    assertFalse(ship.isSunk());
  }
}