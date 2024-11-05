/**
 * The Controller interface defines a set of operations for managing and processing images.
 * It includes methods for loading, saving, executing commands, and running script files.
 */
public interface Controller {
  /**
   * Loads an image from the specified file and gives it a name.
   * The image can be accessed using that given name.
   * The image can be loaded from a PPM, JPG, or PNG file.
   * @param filename the path of the file from which the image is loaded.
   * @param image_name the name by which the loaded image will be referenced in the program.
   * @return the loaded Photo object, or null if the file cannot be loaded.
   */
  Photo Load(String filename, String image_name);

  /**
   * Saves the specified image with a name to a file-path.
   * The file can be saved in any format PPM, JPG, or PNG.
   * @param filename the path and filename to which the image will be saved.
   * @param image_to_use the name of the image that will be saved from the program.
   * @return the saved Photo object, or null if the save operation fails.
   */
  Photo Save(String filename, String image_to_use);

  /**
   * Runs the script containing multiple image processing commands.
   * The script is expected to be a text file with each line representing a command.
   * @param filePath the path to the script file.
   */
  void runScript(String filePath);

  /**
   * Executes the commands related to image processing.
   * This can include commands like loading, brightening, sharpening, etc.
   * @param command the command to be executed.
   */
  void commandExecutor(String command);

  /**
   * Runs the image processing controller.
   * runController() either accepts a single command or a txt script.
   * The program will keep running until an "exit" command is issued.
   */
  void runController();
}
