import java.util.Arrays;

/**
 * This class is an implementation of the Photo interface.
 * The implementation contains the name, height, width and pixels attributes.
 * and contains a constructor to initialize said values.
 * It also contains an overridden equals and hashcode function to properly compare Images.
 */
public class ImagePhoto implements Photo {

  private final String name;
  private final int height;
  private final int width;
  private final int[][][] pixels;

  /**
   * Constructor for the ImagePhoto class for initialization.
   * @param name Takes in the name for the ImagePhoto.
   * @param width Takes in the width for the ImagePhoto.
   * @param height Takes in the height for the ImagePhoto.
   */
  public ImagePhoto(String name, int width, int height) {
    this.name = name;
    this.height = height;
    this.width = width;
    this.pixels = new int[width][height][3];
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Photo)) {
      return false;
    }
    return Arrays.deepEquals(this.pixels, ((ImagePhoto) other).pixels);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(pixels);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int[][][] getPixels() {
    return pixels;
  }

  @Override
  public void setPixel(int x, int y, int[] rgb) {
    pixels[x][y] = rgb;
  }
}