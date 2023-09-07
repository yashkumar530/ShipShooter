package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.HumanPlayer;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.View;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the HumanPlayer class.
 */
public class HumanPlayerTest {
  private HumanPlayer humanPlayer;
  private View view;

  @BeforeEach
  void setUp() {
    view = mock(View.class);
    this.humanPlayer = new HumanPlayer("Human", view);
  }

  @Test
  void testName() {
    assertEquals("Human", this.humanPlayer.name());
  }

  @Test
  void testSetup() {
    Map<ShipType, Integer> specs = new HashMap<>();
    specs.put(ShipType.CARRIER, 1);
    specs.put(ShipType.BATTLESHIP, 1);
    specs.put(ShipType.DESTROYER, 1);
    specs.put(ShipType.SUBMARINE, 1);

    List<Ship> fleet = this.humanPlayer.setup(10, 10, specs);

    assertEquals(4, fleet.size());
    Set<Coord> allCoords = new HashSet<>();
    fleet.forEach(ship -> allCoords.addAll(ship.getCoordinates()));
    assertEquals(allCoords.size(), fleet.stream().mapToInt(ship ->
        ship.getCoordinates().size()).sum());
  }

  @Test
  void testTakeShots() {
    this.humanPlayer.playerBoard.height = 5;
    this.humanPlayer.playerBoard.width = 5;
    Ship ship = new Ship(ShipType.DESTROYER, Arrays.asList(new Coord(1, 1),
        new Coord(2, 1), new Coord(3, 1)));
    this.humanPlayer.playerBoard.allShips.add(ship);

    List<Coord> predefinedShots = Arrays.asList(new Coord(1, 1));
    // I mocked the view so that it returns the predefined shots
    when(view.promptShots(1, this.humanPlayer.playerBoard.allShots)).thenReturn(predefinedShots);

    List<Coord> shots = this.humanPlayer.takeShots();

    assertEquals(1, shots.size());
    assertTrue(this.humanPlayer.playerBoard.allShots.containsAll(shots));
  }

  @Test
  void testReportDamage() {
    Ship ship = new Ship(ShipType.DESTROYER, Arrays.asList(new Coord(1, 1),
        new Coord(2, 1), new Coord(3, 1)));
    this.humanPlayer.playerBoard.allShips.add(ship);

    List<Coord> hits = this.humanPlayer.reportDamage(Arrays.asList(new Coord(1, 1),
        new Coord(4, 4)));

    assertEquals(1, hits.size());
    assertTrue(hits.contains(new Coord(1, 1)));
  }

  @Test
  void testSuccessfulHits() {
    this.humanPlayer.boardVisual = new char[5][5];

    List<Coord> shotsThatHitOpponentShips = Arrays.asList(new Coord(1, 1),
        new Coord(2, 2));
    this.humanPlayer.successfulHits(shotsThatHitOpponentShips);

    assertEquals('X', this.humanPlayer.boardVisual[1][1]);
    assertEquals('X', this.humanPlayer.boardVisual[2][2]);
  }

  @Test
  void testUpdateIsSunk() {
    Ship ship = new Ship(ShipType.DESTROYER, Arrays.asList(
        new Coord(1, 1), new Coord(2, 1), new Coord(3, 1)));
    this.humanPlayer.playerBoard.allShips.add(ship);

    List<Coord> allOpponentHits = Arrays.asList(
        new Coord(1, 1), new Coord(2, 1), new Coord(3, 1));
    this.humanPlayer.updateIsSunk(allOpponentHits);

    assertTrue(ship.isSunk());
  }

  @Test
  void testIsValidPlacement_verticalShipInValidPlacement() {
    Ship ship1 = new Ship(ShipType.DESTROYER, Collections.singletonList(
        new Coord(1, 1)));
    Ship ship2 = new Ship(ShipType.SUBMARINE, Arrays.asList(
        new Coord(0, 1), new Coord(1, 1), new Coord(2, 1)));
    this.humanPlayer.playerBoard.allShips.add(ship1);
    this.humanPlayer.playerBoard.allShips.add(ship2);

    boolean isValid = this.humanPlayer.isValidPlacement(
        true, 3, 0, 0, 3, 3, this.humanPlayer.playerBoard.allShips);

    assertFalse(isValid);
  }

  @Test
  void testIsValidPlacement_horizontalShipInValidPlacement() {
    Ship ship1 = new Ship(ShipType.BATTLESHIP, Arrays.asList(
        new Coord(2, 0), new Coord(2, 1), new Coord(2, 2), new Coord(2, 3)));
    Ship ship2 = new Ship(ShipType.CARRIER, Arrays.asList(
        new Coord(3, 2), new Coord(4, 2), new Coord(5, 2), new Coord(6, 2), new Coord(7, 2)));
    this.humanPlayer.playerBoard.allShips.add(ship1);
    this.humanPlayer.playerBoard.allShips.add(ship2);

    boolean isValid = this.humanPlayer.isValidPlacement(
        false, 5, 1, 2, 8, 8, this.humanPlayer.playerBoard.allShips);

    assertFalse(isValid);
  }

  @Test
  void testIsValidPlacement_validPlacement() {
    Ship ship1 = new Ship(ShipType.DESTROYER, Collections.singletonList(new Coord(1, 1)));
    Ship ship2 = new Ship(ShipType.SUBMARINE, Arrays.asList(
        new Coord(0, 1), new Coord(1, 1), new Coord(2, 1)));
    this.humanPlayer.playerBoard.allShips.add(ship1);
    this.humanPlayer.playerBoard.allShips.add(ship2);

    boolean isValid = this.humanPlayer.isValidPlacement(
        true, 3, 4, 4, 6, 6, this.humanPlayer.playerBoard.allShips);

    assertFalse(isValid);
  }
}