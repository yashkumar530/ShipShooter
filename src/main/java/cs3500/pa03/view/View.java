package cs3500.pa03.view;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ShipType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * View component of the game
 */
public class View {
  private Scanner scanner;
  private int boardHeight;
  private int boardWidth;

  /**
   * Constructs a View object.
   */
  public View() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * Displays the given message.
   *
   * @param message the message to be displayed
   */
  public void displayMessage(String message) {
    System.out.println(message);
  }

  /**
   * Prompts the user to enter the dimension of the board.
   *
   * @param dimensionName the name of the dimension (e.g., "height", "width")
   * @return the dimension entered by the user
   * @throws IOException if an I/O error occurs
   */
  public int getBoardDimension(String dimensionName) throws IOException {
    System.out.print("Enter the " + dimensionName + " of the board:");
    int dimension = scanner.nextInt();
    if (dimension < 6 || dimension > 15) {
      System.out.println("Op! You entered an invalid dimension. "
          + "Board dimensions must be between 6 and 10. Try again.");
      return getBoardDimension(dimensionName);
    }
    return dimension;
  }

  /**
   * Prompts the user to enter the fleet specifications.
   *
   * @param height the height of the board
   * @param width  the width of the board
   * @return a map representing the fleet specifications
   */
  public Map<ShipType, Integer> getFleetSpecifications(int height, int width) {
    boardHeight = height;
    boardWidth = width;
    int smaller = Math.min(boardHeight, boardWidth);

    System.out.println("Enter the fleet specifications\nFleet size may not exceed "
        + smaller + " ships:");
    System.out.print("Carrier: ");
    int carrierCount = scanner.nextInt();
    System.out.print("Battleship: ");
    int battleshipCount = scanner.nextInt();
    System.out.print("Destroyer: ");
    int destroyerCount = scanner.nextInt();
    System.out.print("Submarine: ");
    int submarineCount = scanner.nextInt();

    if (carrierCount + battleshipCount + destroyerCount + submarineCount > smaller) {
      System.out.println("Op! You entered an invalid fleet. "
          + "Fleet size may not exceed 8 ships. Try again.");
      return getFleetSpecifications(boardHeight, boardWidth);
    }

    return Map.of(
        ShipType.CARRIER, carrierCount,
        ShipType.BATTLESHIP, battleshipCount,
        ShipType.DESTROYER, destroyerCount,
        ShipType.SUBMARINE, submarineCount
    );
  }

  /**
   * Displays the board and returns the board data.
   *
   * @param board the board to be displayed
   * @param name  the name of the board (e.g., "Opponent")
   * @return the board data
   */
  public char[][] displayBoard(Board board, String name) {
    char[][] boardData = board.getBoardData();
    System.out.println(name + " Board:");

    for (int i = 0; i < boardData.length; i++) {
      for (int j = 0; j < boardData[i].length; j++) {
        if (name.equals("Opponent")) {
          System.out.print("\t-");
        } else {
          System.out.print("\t" + boardData[i][j]);
        }
      }
      System.out.println();
    }
    return boardData;
  }

  /**
   * Prompts the user to enter shots.
   *
   * @param shipCount the number of shots to enter
   * @param allShots  the list of all shots entered so far
   * @return the list of entered shots
   */
  public List<Coord> promptShots(int shipCount, List<Coord> allShots) {
    System.out.println("Please enter " + shipCount + " shots: ");

    List<Coord> shots = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    for (int i = 0; i < shipCount; i++) {
      System.out.print("Enter shot " + (i + 1) + ": ");
      String shotInput = scanner.nextLine();
      String[] splitInput = shotInput.split(" ");
      try {
        int x = Integer.parseInt(splitInput[0].trim());
        int y = Integer.parseInt(splitInput[1].trim());

        if (x < 0 || x >= boardHeight || y < 0 || y >= boardWidth) {
          System.out.println("Invalid shot coordinates. Please enter x values between 0 and "
              + (boardHeight - 1) + " and y values between 0 and " + (boardWidth - 1) + ".");
          i--;
        } else if (allShots.contains(new Coord(x, y))) {
          System.out.println("You already shot at " + x + ", " + y + ". "
              + "Please enter a different coordinate.");
          i--;
        } else {
          shots.add(new Coord(x, y));
          allShots.add(new Coord(x, y));
        }
      } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
        System.out.println("Invalid input. Please enter coordinates as 'X Y'.");
        i--;
      }
    }
    return shots;
  }

  /**
   * Displays the board with successful hits.
   *
   * @param displayBoard the board to be displayed
   * @param name         the name of the board (e.g., "Opponent")
   */
  public void displaySuccessfulHits(char[][] displayBoard, String name) {
    System.out.println(name + " Board:");
    for (int i = 0; i < displayBoard.length; i++) {
      for (int j = 0; j < displayBoard[i].length; j++) {
        System.out.print("\t" + displayBoard[i][j]);
      }
      System.out.println();
    }
  }

  /**
   * Displays the end game result and reason.
   *
   * @param result the game result
   * @param reason the reason for the game end
   */
  public void displayEndGame(GameResult result, String reason) {
    System.out.println("Game over! Result: " + result);
    System.out.println("Reason: " + reason);
  }
}
