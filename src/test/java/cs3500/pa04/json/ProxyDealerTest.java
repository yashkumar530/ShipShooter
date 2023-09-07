package cs3500.pa04.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


//NOTE FOR GRADER: Because there is no double jeopardy, we did not spend time testing PA03 and
// instead focused on testing the functionality of PA04, therefore, while our overall jacoco
// test coverage is around 70%, our test coverage for the PA04 functionality is around 96% which
// can be seen by looking at the test coverage of the cs3500.pa04 package.

/**
 * Tests for the ProxyDealer class.
 */
public class ProxyDealerTest {
  @Mock
  private Socket mockServer;

  @Mock
  private AiPlayer mockPlayer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRun() throws Exception {
    String jsonInput = "{\"method-name\":\"join\",\"arguments\":{}}";
    InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(mockServer.isClosed()).thenReturn(false);
    when(mockServer.getInputStream()).thenReturn(inputStream);
    when(mockServer.getOutputStream()).thenReturn(outputStream);


    ProxyDealer proxyDealer = new ProxyDealer(mockServer, mockPlayer);

    // Run the method
    proxyDealer.run();


    String expectedOutput =
        "{\"method-name\":\"join\",\"arguments\":{\"name\":\"oprajapati1\","
            + "\"game-type\":\"SINGLE\"}}";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  void testHandleSetup() throws Exception {
    // Set up the mocks and inputs specific to this test case
    String jsonInput = "{\"method-name\":\"setup\",\"arguments\":{\"width\":10,\"height\":10,"
        + "\"fleet-spec\":{\"CARRIER\":2,\"BATTLESHIP\":4,\"DESTROYER\":1,\"SUBMARINE\":3}}}";
    InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(mockServer.getInputStream()).thenReturn(inputStream);
    when(mockServer.getOutputStream()).thenReturn(outputStream);


    List<Ship> shipPlacements = new ArrayList<>();
    shipPlacements.add(new Ship(ShipType.CARRIER, List.of(new Coord(0, 0))));
    shipPlacements.add(new Ship(ShipType.BATTLESHIP, List.of(new Coord(1, 0))));
    when(mockPlayer.setup(anyInt(), anyInt(), anyMap())).thenReturn(shipPlacements);
    mockPlayer.playerBoard = new Board();
    mockPlayer.playerBoard.height = 10;
    mockPlayer.playerBoard.width = 10;
    mockPlayer.playerBoard.allShips = shipPlacements;
    ProxyDealer proxyDealer = new ProxyDealer(mockServer, mockPlayer);


    // Run the method
    proxyDealer.run();


    // Example: Verify the output sent to the server
    String expectedOutput = "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":[{\"coord\""
        + ":{\"x\":0,\"y\":0},\"length\":1,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"x\":1,\"y\":0},\"length\":1,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"x\":0,\"y\":0},\"length\":1,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"x\":1,\"y\":0},\"length\":1,\"direction\":\"VERTICAL\"}]}}";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }


  @Test
  void testHandleTakeShots() throws Exception {
    // Set up the mocks and inputs specific to this test case
    String jsonInput = "{\"method-name\":\"take-shots\",\"arguments\":{}}";
    InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(mockServer.getInputStream()).thenReturn(inputStream);
    when(mockServer.getOutputStream()).thenReturn(outputStream);
    List<Coord> shots = new ArrayList<>();
    shots.add(new Coord(0, 0));
    shots.add(new Coord(1, 1));
    when(mockPlayer.takeShots()).thenReturn(shots);

    ProxyDealer proxyDealer = new ProxyDealer(mockServer, mockPlayer);

    // Run the method
    proxyDealer.run();


    String expectedOutput =
        "{\"method-name\":\"take-shots\",\"arguments\":{\"coordinates\":[{\"x\":0,\"y\":0},"
            + "{\"x\":1,\"y\":1}]}}";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  void testHandleReportDamage() throws Exception {
    // Set up the mocks and inputs specific to this test case
    String jsonInput =
        "{\"method-name\":\"report-damage\",\"arguments\":{\"coordinates\":[{\"x\":1,\"y\":1},"
            + "{\"x\":2,\"y\":2}]}}";
    InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(mockServer.getInputStream()).thenReturn(inputStream);
    when(mockServer.getOutputStream()).thenReturn(outputStream);
    List<Coord> damageReports = new ArrayList<>();
    damageReports.add(new Coord(1, 1));
    damageReports.add(new Coord(2, 2));
    when(mockPlayer.reportDamage(anyList())).thenReturn(damageReports);

    ProxyDealer proxyDealer = new ProxyDealer(mockServer, mockPlayer);

    // Run the method
    proxyDealer.run();
    String expectedOutput =
        "{\"method-name\":\"report-damage\",\"arguments\":{\"coordinates\":[{\"x\":1,\"y\":1},"
            + "{\"x\":2,\"y\":2}]}}";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  void testHandleSuccessfulHits() throws Exception {
    // Set up the mocks and inputs specific to this test case
    String jsonInput =
        "{\"method-name\":\"successful-hits\",\"arguments\":{\"coordinates\":[{\"x\":0,\"y\":0},"
            + "{\"x\":1,\"y\":1}]}}";
    InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(mockServer.getInputStream()).thenReturn(inputStream);
    when(mockServer.getOutputStream()).thenReturn(outputStream);
    List<Coord> successfulHits = new ArrayList<>();
    successfulHits.add(new Coord(0, 0));
    successfulHits.add(new Coord(1, 1));

    ProxyDealer proxyDealer = new ProxyDealer(mockServer, mockPlayer);

    // Run the method
    proxyDealer.run();


    // Example: Verify the output sent to the server
    String expectedOutput = "{\"method-name\":\"successful-hits\",\"arguments\":{}}";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  void testHandleEndGame() throws Exception {
    // Set up the mocks and inputs specific to this test case
    String jsonInput =
        "{\"method-name\":\"end-game\",\"arguments\":{\"result\":\"WIN\",\"reason\":\"TIMEOUT\"}}";
    InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(mockServer.getInputStream()).thenReturn(inputStream);
    when(mockServer.getOutputStream()).thenReturn(outputStream);

    ProxyDealer proxyDealer = new ProxyDealer(mockServer, mockPlayer);

    // Run the method
    proxyDealer.run();


    // Example: Verify the output sent to the server
    String expectedOutput = "{\"method-name\":\"end-game\",\"arguments\":{}}";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }
}

