package cs3500.pa04.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.Position;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A class representing a proxy dealer that communicates with a server using JSON messages.
 */
public class ProxyDealer {
  private Socket server;
  private AiPlayer player;
  private final InputStream in;
  private final PrintStream out;
  private final ObjectMapper mapper = new ObjectMapper();
  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");
  private List<Coord> shotsOnUs;
  private List<Coord> allShots = new ArrayList<>();

  /**
   * Constructs a ProxyDealer object with the specified server and player.
   *
   * @param server the socket representing the server
   * @param player the player object
   * @throws IOException if an I/O error occurs when creating the input/output streams
   */
  public ProxyDealer(Socket server, AiPlayer player) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
  }

  private void initBoard() {
    int height = player.playerBoard.height;
    int width = player.playerBoard.width;
    char[][] boardData = new char[height][width];
    for (int i = 0; i < height; i++) {
      Arrays.fill(boardData[i], '-');
    }
    for (Ship ship : player.playerBoard.allShips) {
      for (Coord coord : ship.getCoordinates()) {
        boardData[coord.getYpos()][coord.getXpos()] = ship.getShipType().getIdentifier();
      }
    }
    player.boardVisual = boardData;
    player.boardHidden = boardData;
  }

  /**
   * Runs the proxy dealer, continuously listening for and handling incoming messages.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      System.out.print("Game over.");
    }
  }

  /**
   * Delegates the handling of a message to the appropriate handler method based on its name.
   *
   * @param message the incoming message
   */
  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      handleJoin(arguments);
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots(arguments);
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid message");
    }
  }

  /**
   * Handles the "join" message by sending a join request to the server.
   *
   * @param arguments the arguments of the join message
   */
  private void handleJoin(JsonNode arguments) {
    JoinJson args = new JoinJson("oprajapati1", "SINGLE");
    JsonNode joinArg = JsonUtils.serializeRecord(args);
    MessageJson joinGame = new MessageJson("join", joinArg);
    JsonNode jsonOutput = JsonUtils.serializeRecord(joinGame);
    this.out.println(jsonOutput);
  }

  /**
   * Handles the "setup" message by setting up the player's
   * fleet and sending the fleet information to the server.
   *
   * @param arguments the arguments of the setup message
   */
  private void handleSetup(JsonNode arguments) {
    SetUp args = this.mapper.convertValue(arguments, SetUp.class);

    int height = arguments.get("height").asInt();
    int width = arguments.get("width").asInt();

    Map<ShipType, Integer> specifications = args.fleetSpecs();
    List<Ship> fleet = player.setup(height, width, specifications);
    player.playerBoard.allShips.addAll(fleet);
    List<ShipJson> fleetJson = new ArrayList<>();

    for (Ship ship : fleet) {
      Coord start = ship.getCoordinates().get(0);
      int shipSize = ship.getCoordinates().size();
      Position position = ship.getPosition();

      CoordJson coordJson = new CoordJson(start.getXpos(), start.getYpos());
      ShipJson shipJson = new ShipJson(coordJson, shipSize, position);

      fleetJson.add(shipJson);
    }
    FleetJson fleetOutput = new FleetJson(fleetJson);
    JsonNode fleetJsonOutput = JsonUtils.serializeRecord(fleetOutput);
    MessageJson setup = new MessageJson("setup", fleetJsonOutput);
    JsonNode setupOutput = JsonUtils.serializeRecord(setup);
    System.out.println("setup output:" + setupOutput);
    this.out.println(setupOutput);
    initBoard();
  }

  /**
   * Handles the "take-shots" message by requesting shots from
   * the player and sending the shot information to the server.
   *
   * @param arguments the arguments of the take-shots message
   */
  private void handleTakeShots(JsonNode arguments) {
    List<Coord> shots = player.takeShots();
    List<CoordJson> shotsJson = new ArrayList<>();

    for (Coord c : shots) {
      CoordJson coordJson = new CoordJson(c.getXpos(), c.getYpos());
      shotsJson.add(coordJson);
    }

    VolleyJson volley = new VolleyJson(shotsJson);
    JsonNode shotOutput = JsonUtils.serializeRecord(volley);
    MessageJson takeShots = new MessageJson("take-shots", shotOutput);
    JsonNode takeShotsOutput = JsonUtils.serializeRecord(takeShots);
    this.out.println(takeShotsOutput);
  }

  /**
   * Handles the "report-damage" message by processing the
   * reported shots and sending the results to the server.
   *
   * @param arguments the arguments of the report-damage message
   */
  private void handleReportDamage(JsonNode arguments) {
    VolleyJson givenShots = mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> shots = new ArrayList<>();

    for (CoordJson cj : givenShots.getCoords()) {
      shots.add(new Coord(cj.getX(), cj.getY()));
      allShots.add(new Coord(cj.getX(), cj.getY()));
    }

    shotsOnUs = player.reportDamage(shots);

    List<CoordJson> hitsJson = new ArrayList<>();
    for (Coord c : shotsOnUs) {
      CoordJson coordJson = new CoordJson(c.getXpos(), c.getYpos());
      hitsJson.add(coordJson);
    }
    VolleyJson volley = new VolleyJson(hitsJson);
    JsonNode volleyOutput = JsonUtils.serializeRecord(volley);

    MessageJson damageReport = new MessageJson("report-damage", volleyOutput);
    JsonNode damageReportOutput = JsonUtils.serializeRecord(damageReport);
    this.out.println(damageReportOutput);
  }

  /**
   * Handles the "successful-hits" message by processing the successful hits made by the player.
   *
   * @param arguments the arguments of the successful-hits message
   */
  private void handleSuccessfulHits(JsonNode arguments) {
    VolleyJson successHitsVolley = mapper.convertValue(arguments, VolleyJson.class);

    List<Coord> successHits = new ArrayList<>();

    for (CoordJson cj : successHitsVolley.getCoords()) {
      successHits.add(new Coord(cj.getX(), cj.getY()));
    }

    player.successfulHits(shotsOnUs);
    player.updateIsSunk(allShots);
    JsonNode node = mapper.createObjectNode();
    MessageJson successHitsMessage = new MessageJson("successful-hits", node);
    JsonNode successHitsOutput = JsonUtils.serializeRecord(successHitsMessage);
    this.out.println(successHitsOutput);
  }

  /**
   * Handles the "end-game" message by processing the game result
   * and reason, and sending an acknowledgement to the server.
   *
   * @param arguments the arguments of the end-game message
   */
  private void handleEndGame(JsonNode arguments) {
    GameResult result = GameResult.valueOf(
        mapper.convertValue(arguments.get("result"), String.class));
    String reason = mapper.convertValue(arguments.get("reason"), String.class);
    player.endGame(result, reason);

    JsonNode node = mapper.createObjectNode();
    MessageJson messageJson = new MessageJson("end-game", node);
    JsonNode endGameOutput = JsonUtils.serializeRecord(messageJson);
    this.out.println(endGameOutput);
    try {
      server.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}