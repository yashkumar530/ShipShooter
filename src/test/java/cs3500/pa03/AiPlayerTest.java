package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.View;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AiPlayer class.
 */
public class AiPlayerTest {
  private AiPlayer aiPlayer;
  private View view;

  @BeforeEach
  void setUp() {
    view = new View(); // Adjust this as per your setup
    aiPlayer = new AiPlayer("AI", view);
  }

  @Test
  void testName() {
    assertEquals("AI", aiPlayer.name());
  }

  @Test
  void testSetup() {
    Map<ShipType, Integer> specs = new HashMap<>();
    specs.put(ShipType.CARRIER, 1);
    specs.put(ShipType.BATTLESHIP, 1);
    specs.put(ShipType.DESTROYER, 1);
    specs.put(ShipType.SUBMARINE, 1);

    List<Ship> fleet = aiPlayer.setup(10, 10, specs);

    assertEquals(4, fleet.size());
    //used a hashset to check for duplicates
    Set<Coord> allCoords = new HashSet<>();
    fleet.forEach(ship -> allCoords.addAll(ship.getCoordinates()));
    assertEquals(allCoords.size(), fleet.stream().mapToInt(
        ship -> ship.getCoordinates().size()).sum());
  }

  @Test
  void testTakeShots() {
    aiPlayer.playerBoard.height = 5;
    aiPlayer.playerBoard.width = 5;
    Ship ship = new Ship(ShipType.DESTROYER, Arrays.asList(new Coord(1, 1),
        new Coord(2, 1), new Coord(3, 1)));
    aiPlayer.playerBoard.allShips.add(ship);

    List<Coord> shots = aiPlayer.takeShots();

    assertEquals(1, shots.size());
    assertTrue(aiPlayer.playerBoard.allShots.containsAll(shots));
  }

  @Test
  void testReportDamage() {
    Ship ship = new Ship(ShipType.DESTROYER, Arrays.asList(new Coord(1, 1),
        new Coord(2, 1), new Coord(3, 1)));
    aiPlayer.playerBoard.allShips.add(ship);

    List<Coord> hits = aiPlayer.reportDamage(Arrays.asList(new Coord(1, 1),
        new Coord(4, 4)));

    assertEquals(1, hits.size());
    assertTrue(hits.contains(new Coord(1, 1)));
  }

  @Test
  void testSuccessfulHits() {
    aiPlayer.boardHidden = new char[5][5];
    aiPlayer.boardVisual = new char[5][5];

    List<Coord> shotsThatHitOpponentShips = Arrays.asList(new Coord(1, 1),
        new Coord(2, 2));
    aiPlayer.successfulHits(shotsThatHitOpponentShips);

    assertEquals('X', aiPlayer.boardHidden[1][1]);
    assertEquals('X', aiPlayer.boardHidden[2][2]);
  }

  @Test
  void testUpdateIsSunk() {
    Ship ship = new Ship(ShipType.DESTROYER, Arrays.asList(new Coord(1, 1),
        new Coord(2, 1), new Coord(3, 1)));
    aiPlayer.playerBoard.allShips.add(ship);

    List<Coord> allOpponentHits = Arrays.asList(new Coord(1, 1),
        new Coord(2, 1), new Coord(3, 1));
    aiPlayer.updateIsSunk(allOpponentHits);

    assertTrue(ship.isSunk());
  }

  @Test
  void testIsValidPlacement_verticalPlacementOutOfBounds() {
    Map<ShipType, Integer> specs = new HashMap<>();
    specs.put(ShipType.DESTROYER, 1);
    List<Ship> fleet = aiPlayer.setup(5, 5, specs);

    boolean isValid = aiPlayer.isValidPlacement(true,
        4, 2, 3, 5, 5, fleet);

    assertFalse(isValid);
  }

  @Test
  void testIsValidPlacement_horizontalPlacementOutOfBounds() {
    Map<ShipType, Integer> specs = new HashMap<>();
    specs.put(ShipType.DESTROYER, 1);
    List<Ship> fleet = aiPlayer.setup(5, 5, specs);

    boolean isValid = aiPlayer.isValidPlacement(false, 4, 3, 2, 5, 5, fleet);

    assertFalse(isValid);
  }

  @Test
  void testIsValidPlacement_collisionWithExistingShip() {
    Map<ShipType, Integer> specs = new HashMap<>();
    specs.put(ShipType.DESTROYER, 1);
    List<Ship> fleet = aiPlayer.setup(5, 5, specs);

    Coord existingShipCoord = fleet.get(0).getCoordinates().get(0);

    boolean isValidVertical = aiPlayer.isValidPlacement(true, 3,
        existingShipCoord.getXpos(), existingShipCoord.getYpos(), 5, 5, fleet);
    boolean isValidHorizontal = aiPlayer.isValidPlacement(false, 3,
        existingShipCoord.getXpos(), existingShipCoord.getYpos(), 5, 5, fleet);

    assertFalse(isValidVertical);
    assertFalse(isValidHorizontal);
  }

  @Test
  void testIsValidPlacement_validPlacement() {
    Map<ShipType, Integer> specs = new HashMap<>();
    specs.put(ShipType.DESTROYER, 1);
    List<Ship> fleet = aiPlayer.setup(5, 5, specs);

    boolean isValidVertical = aiPlayer.isValidPlacement(true, 3, 5, 5, 5, 5, fleet);
    boolean isValidHorizontal = aiPlayer.isValidPlacement(false, 3, 0, 0, 5, 5, fleet);

    assertTrue(isValidHorizontal || isValidVertical);
  }
}
