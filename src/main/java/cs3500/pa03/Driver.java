package cs3500.pa03;

import cs3500.pa03.controller.Game;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.HumanPlayer;
import cs3500.pa03.view.View;
import cs3500.pa04.json.ProxyDealer;
import java.io.IOException;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      View view = new View();
      HumanPlayer humanPlayer = new HumanPlayer("Your", view);
      AiPlayer aiPlayer = new AiPlayer("Opponent", view);
      Game game = new Game(view, humanPlayer, aiPlayer);
      game.start();
    } else {
      String host = args[0];
      int port = Integer.parseInt(args[1]);
      Driver.runClient(host, port);
    }
  }

  private static void runClient(String host, int port) {
    try {
      Socket server = new Socket(host, port);
      AiPlayer aiPlayer = new AiPlayer("Opponent", new View());
      ProxyDealer proxyDealer = new ProxyDealer(server, aiPlayer);
      proxyDealer.run();
    } catch (IOException e) {
      System.out.println("Failed to connect to the server: " + e.getMessage());
    }
  }
}