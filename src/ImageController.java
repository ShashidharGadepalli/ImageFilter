import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * This class is an implementation of the Controller interface.
 * contains the attributes controllerModel and contactView.
 * communicates with the ImageView outputs and the ImageModel's methods and imageDirectory.
 * Responsible for receiving, parsing, and dictating all input scripts and commands.
 * The controller also is responsible for loading and saving files.
 */
public class ImageController implements Controller {
  private static final ImageModel controllerModel = new ImageModel();
  private static final ImageView contactView = new ImageView();

  @Override
  public Photo Load(String filename, String image_name) {
    String validateExtension = filename.substring(filename.lastIndexOf('.'));
    Photo loadedImage;
    if (validateExtension.equals(".ppm")) {
      loadedImage = readPPM(filename, image_name);
    } else if (validateExtension.equals(".jpg") || validateExtension.equals(".jpeg")
        || validateExtension.equals(".png")) {
      loadedImage = readOther(filename, image_name);
    } else {
      return null;
    }
    controllerModel.updateDirectory(image_name, loadedImage);
    return loadedImage;
  }

  /**
   * Helper function to read jpg and png files.
   * @param filename Given file.
   * @param image_name Given image name to be populated to the model map.
   * @return The successfully loaded Photo object or null if given an invalid input.
   */
  private Photo readOther(String filename, String image_name) {
    try {
      BufferedImage image = ImageIO.read(new File(filename));
      if (image == null) {
        return null;
      }
      int width = image.getWidth();
      int height = image.getHeight();
      Photo newImage = new ImagePhoto(image_name, width, height);
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int rgb = image.getRGB(j, i);
          int r = rgb >> 16 & 0xff;
          int g = rgb >> 8 & 0xff;
          int b = rgb & 0xff;
          newImage.setPixel(j, i, new int[]{r, g, b});
        }
      }
      return newImage;
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Helper function to read ppm files.
   * @param filename Given file.
   * @param image_name Given image name to be populated to the model map.
   * @return The successfully loaded Photo object or null if given an invalid input.
   */
  private Photo readPPM(String filename, String image_name) {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      return null;
    }

    int width = sc.nextInt();
    int height = sc.nextInt();
    Photo newImage = new ImagePhoto(image_name, width, height);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        newImage.setPixel(j, i, new int[]{r, g, b});
      }
    }
    return newImage;
  }

  @Override
  public Photo Save(String filename, String image_to_use) {
    String validateExtension = filename.substring(filename.lastIndexOf('.'));
    Photo savedImage;
    Photo newImage = controllerModel.getImagePhoto(image_to_use);
    if (newImage == null) {
      return null;
    }
    if (validateExtension.equals(".ppm")) {
      savedImage = savePPM(filename, newImage);
    } else if (validateExtension.equals(".jpg") || validateExtension.equals(".jpeg")
        || validateExtension.equals(".png")) {
      savedImage = saveOther(filename, newImage, validateExtension);
    } else {
      return null;
    }
    return savedImage;
  }

  /**
   * Helper function to save jpg and png files.
   * @param filename Given file.
   * @param image_to_use Image from the ImageModel's imageDirectory to be saved in the file.
   * @param format The extension to differentiate between jpgs and pngs.
   * @return The successfully saved Photo object or null if given an invalid input.
   */
  private Photo saveOther(String filename, Photo image_to_use, String format) {
    try {
      int height = image_to_use.getHeight();
      int width = image_to_use.getWidth();
      int[][][] pixels = image_to_use.getPixels();
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int[] rgb = pixels[j][i];
          int color = (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
          image.setRGB(j, i, color);
        }
      }

      File file = new File(filename);
      File parentDir = file.getParentFile();

      if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs();
      }

      ImageIO.write(image, format.substring(1), file);
      return image_to_use;
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Helper function to save ppm files.
   * @param filename Given file.
   * @param image_to_use Image from the ImageModel's imageDirectory to be saved in the file.
   * @return The successfully saved Photo object or null if given an invalid input.
   */
  private Photo savePPM(String filename, Photo image_to_use) {
    int height = image_to_use.getHeight();
    int width = image_to_use.getWidth();
    int[][][] pixels = image_to_use.getPixels();
    File file = new File(filename);
    File parentDir = file.getParentFile();

    if (parentDir != null && !parentDir.exists()) {
      parentDir.mkdirs();
    }

    try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename))) {
      writer.println("P3");
      writer.println(width + " " + height);
      writer.println(255);
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int[] rgb = pixels[j][i];
          writer.println(rgb[0] + " " + rgb[1] + " " + rgb[2]);
        }
      }
      return image_to_use;
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public void runScript(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        commandExecutor(line.trim());
      }
    } catch (IOException e) {
      contactView.printFeedback("Error reading file " + e.getMessage());
    }
  }

  @Override
  public void commandExecutor(String command) {
    if (command.startsWith("#") || command.isEmpty()) {
      return;
    }
    String[] token = command.split("\\s+");

    switch (token[0].toLowerCase()) {
      case "load":
        if (token.length == 3) {
          Photo test = Load(token[1], token[2]);
          if (test == null) {
            contactView.printFeedback("Error loading image");
          } else {
            contactView.printFeedback("Loaded " + token[2] + " from " + token[1]);
          }
        } else {
          contactView.printFeedback("Invalid Load command. Please give correct command");
        }
        break;

      case "save":
        if (token.length == 3) {
          Photo test = Save(token[1], token[2]);
          if (test == null) {
            contactView.printFeedback("Error saving image");
          } else {
            contactView.printFeedback("Saved " + token[1] + " using " + token[2]);
          }
        } else {
          contactView.printFeedback("Invalid Save command. Please give correct command");
        }
        break;

      case "value-component":
      case "luma-component":
      case "red-component":
      case "green-component":
      case "blue-component":
      case "intensity-component":
        ComponentCommands(token);
        break;
      case "brighten":
        if (token.length == 4) {
          if (isNull(token[2])) {
            return;
          }
          int intensity = Integer.parseInt(token[1]);
          Photo ph = controllerModel.Brighten(intensity, controllerModel.getImagePhoto(token[2]),
              token[3]);
          controllerModel.updateDirectory(token[3], ph);
          contactView.printFeedback(
              "Created a brightened image by " + token[1] + " to " + token[3]);
        } else {
          contactView.printFeedback(
              "Invalid 'brighten' command format. Please give correct command");
        }
        break;
      case "sepia":
        if (token.length == 3) {
          if (isNull(token[1])) {
            return;
          }
          Photo ph = controllerModel.Sepia(controllerModel.getImagePhoto(token[1]), token[2]);
          controllerModel.updateDirectory(token[2], ph);
          contactView.printFeedback("Created Sepia image: " + token[2]);
        } else {
          contactView.printFeedback("Invalid 'sepia' command format. Please give correct command");
        }
        break;
      case "blur":
        if (token.length == 3) {
          if (isNull(token[1])) {
            return;
          }
          Photo ph = controllerModel.Blur(controllerModel.getImagePhoto(token[1]), token[2]);
          controllerModel.updateDirectory(token[2], ph);
          contactView.printFeedback("Created Blurred image: " + token[2]);
        } else {
          contactView.printFeedback("Invalid 'blur' command format. Please give correct command");
        }
        break;
      case "rgb-split":
        if (token.length == 5) {
          if (isNull(token[1])) {
            return;
          }
          Photo[] photos = controllerModel.Split(
              controllerModel.getImagePhoto(token[1]), token[2], token[3], token[4]);
          controllerModel.updateDirectory(token[2], photos[0]);
          controllerModel.updateDirectory(token[3], photos[1]);
          controllerModel.updateDirectory(token[4], photos[2]);
          contactView.printFeedback("Created Red image: " + token[2]);
          contactView.printFeedback("Created Green image: " + token[3]);
          contactView.printFeedback("Created Blue image: " + token[4]);
        } else {
          contactView.printFeedback(
              "Wrong 'rgb-split' command format. Please give correct command");
        }
        break;
      case "rgb-combine":
        if (token.length == 5) {
          Photo combine = controllerModel.Combine(
              token[1], controllerModel.getImagePhoto(token[2]),
              controllerModel.getImagePhoto(token[3]),
              controllerModel.getImagePhoto(token[4]));
          if (combine == null) {
            contactView.printFeedback("Invalid rgb images given");
          } else {
            controllerModel.updateDirectory(token[1], combine);
            contactView.printFeedback("Created Combined image: " + token[1]);
          }
        } else {
          contactView.printFeedback(
              "Wrong 'rgb-combine' command format. Please give correct command");
        }
        break;
      case "sharpen":
        if (token.length == 3) {
          if (isNull(token[1])) {
            return;
          }
          Photo ph = controllerModel.Sharpen(controllerModel.getImagePhoto(token[1]), token[2]);
          controllerModel.updateDirectory(token[2], ph);
          contactView.printFeedback("Created Sharpened image: " + token[2]);
        } else {
          contactView.printFeedback("Error: Invalid 'sharpen' command format.");
        }
        break;
      case "horizontal-flip":
      case "vertical-flip":
        handleFlipCommand(token);
        break;
      default:
        contactView.printFeedback("Invalid command. Please give correct command");
        break;
    }
  }

  /**
   * Helper function to differentiate between component commands.
   * @param token The given component command format.
   */
  private static void ComponentCommands(String[] token) {
    if (token.length == 3) {
      if (isNull(token[1])) {
        return;
      }
      String command = token[0];
      int index = command.indexOf("-component");
      String component = command.substring(0, index);
      Photo ph = controllerModel.AdjustComponent(controllerModel.getImagePhoto(token[1]), token[2],
          component);
      controllerModel.updateDirectory(token[2], ph);
      contactView.printFeedback("Created " + component + " component image: " + token[2]);
    } else {
      contactView.printFeedback("Invalid component command. Please give correct command");
    }
  }

  /**
   * Helper function to differentiate between flip commands.
   * @param token The given flip command format.
   */
  private static void handleFlipCommand(String[] token) {
    if (token.length == 3) {
      if (isNull(token[1])) {
        return;
      }
      String flipType = token[0];
      if (flipType.equals("vertical-flip")) {
        Photo ph = controllerModel.VerticalFlip(controllerModel.getImagePhoto(token[1]), token[2]);
        controllerModel.updateDirectory(token[2], ph);
        contactView.printFeedback("Created " + token[2] + " via vertical flip");
      } else if (flipType.equals("horizontal-flip")) {
        Photo ph = controllerModel.HorizontalFlip(controllerModel.getImagePhoto(token[1]),
            token[2]);
        controllerModel.updateDirectory(token[2], ph);
        contactView.printFeedback("Created " + token[2] + " via horizontal flip");
      }
    } else {
      contactView.printFeedback("Error: Invalid 'flip' command format.");
    }
  }

  /**
   * Helper function that checks if the given image name exists in the imageDirectory.
   * @param image_name Given image name.
   * @return True if it doesn't exist or is a null value and false if it does exist.
   */
  private static boolean isNull(String image_name) {
    if (controllerModel.getImagePhoto(image_name) == null) {
      contactView.printFeedback("Error: Given image is null or not found");
      return true;
    }
    return false;
  }

  @Override
  public void runController() {
    while (true) {
      Scanner input = new Scanner(System.in);
      System.out.println("Please provide a script txt file or a command input");
      String command = input.nextLine().trim();
      int validateExtension = command.lastIndexOf(".txt");
      if (validateExtension == -1) {
        if (command.equals("exit")) {
          break;
        }
        commandExecutor(command);
      } else {
        runScript(command);
      }
    }
  }
}
