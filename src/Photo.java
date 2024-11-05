/**
 * The Photo interface represents an image or photo in the application.
 * It defines the behaviors and properties that an image object should have.
 * The interface provides methods to access the image's metadata (name, dimensions).
 */
public interface Photo {
  /**
   * Gets the name of the image.
   * @return the name of the image as a string.
   */
  public String getName();

  /**
   * Gets the height of the image.
   * @return the height of the image as an integer.
   */
  public int getHeight();

  /**
   * Gets the width of the image.
   * @return the width of the image as an integer.
   */
  public int getWidth();

  /**
   * Retrieves the pixel data of the image.
   * The pixel data is represented as a 3D array.
   * First 2 Dimensions are  the coordinates (x, y) of the pixel.
   * The 3rd dimension is the RGB values for each pixel.
   * @return a 3D array of integers representing the RGB values of each pixel in the image.
   */
  public int[][][] getPixels();

  /**
   * Sets the RGB value of a pixel at the specified coordinates.
   * @param x   the x-coordinate of the pixel.
   * @param y   the y-coordinate of the pixel.
   * @param rgb an array of three integers representing the RGB color values.
   */
  public void setPixel(int x, int y, int[] rgb);
}
