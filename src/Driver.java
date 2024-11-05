/**
 * This class is the driver class for the program.
 * It calls ImageController's runController to start the prompt I/O.
 */
public class Driver {
  /**
   * Main driving function to start ImageController's runController.
   * @param args Command-line arguments, which are meaningless in our implementation.
   */
  public static void main(String[] args) {
    ImageController controller = new ImageController();
    controller.runController();
  }
}
