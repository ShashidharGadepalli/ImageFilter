/**
 * The Model interface defines the core operations related to image processing.
 * Does various image manipulations, such as flipping, brightening, color adjustments.
 * Also applies filters like blur sharpen, sepia.
 */
public interface Model {
  /**
   * Retrieves an image by its key from the image directory.
   * @param Key is a String Name for the image.
   * @return the Photo object associated with the given key.
   */
  public Photo getImagePhoto(String Key);

  /**
   * Updates the image directory by appending the provided key with the given Photo.
   * @param Key the String Name for the image.
   * @param ph the Photo object to be associated with the key.
   */
  public void updateDirectory(String Key, Photo ph);


  /**
   * Adjusts the color component (red, green, blue, value, intensity, luma) of an image.
   * @param image_to_use the original Photo object to adjust.
   * @param dest_image_name the name of the new image with the adjusted component.
   * @param component_name the color component to adjust (red, green, blue, etc.).
   * @return the Photo object with the adjusted component.
   */
  public Photo AdjustComponent(Photo image_to_use, String dest_image_name, String component_name);

  /**
   * Updates the imageDirectory with a horizontally flipped image from the given image.
   * @param image_to_use the original Photo object.
   * @param dest_image_name the name of the new horizontally flipped image.
   * @return the Photo object with the horizontal flip applied.
   */
  public Photo HorizontalFlip(Photo image_to_use, String dest_image_name);

  /**
   * Updates the imageDirectory with a vertically flipped image.
   * @param image_to_use the original Photo object.
   * @param dest_image_name the name of the new vertically flipped image.
   * @return the Photo object with the vertical flip applied.
   */
  public Photo VerticalFlip(Photo image_to_use, String dest_image_name);

  /**
   * Updates the imageDirectory with a brightened image.
   * @param intensity The brightening constant applied to all pixels.
   * of 0 and a maximum constraint of 255 on addition.
   * @param image_to_use the original Photo object.
   * @param dest_image_name the name of the new brightened image.
   * @return the Photo object with the brightening applied.
   */
  public Photo Brighten(int intensity, Photo image_to_use, String dest_image_name);

  /**
   * Splits a given image to 3 images containing into red, green and blue component.
   * Stores the 3 separate images in the directory.
   * @param image_to_use the original Photo object.
   * @param image_r the name of the new red image.
   * @param image_g the name of the new green image.
   * @param image_b the name of the new blue image.
   * @return The Photo object array of size 3 containing the new red, green, and blue images.
   */
  public Photo[] Split(Photo image_to_use, String image_r, String image_g, String image_b);

  /**
   * Combines a given 3 red, blue, and green components to form an image.
   * combined destination image contains components in the correct rgb values.
   * Updates the imageDirectory with the combined image.
   * @param dest_image_name the name of the new combined image.
   * @param image_r the red Photo object.
   * @param image_g the green Photo object.
   * @param image_b the blue Photo object.
   * @return The new combined image as a Photo object.
   */
  public Photo Combine(String dest_image_name, Photo image_r, Photo image_g, Photo image_b);

  /**
   * Applies a 3x3 Gaussian blur filter to the image.
   * @param image_to_use the original Image to blur.
   * @param dest_image_name the name of the new blurred image.
   * @return the blurred Image.
   */
  public Photo Blur(Photo image_to_use, String dest_image_name);

  /**
   * Applies a 5x5 sharpen filter to the image.
   * @param image_to_use the original Image to sharpen.
   * @param dest_image_name the name of the new sharpened image.
   * @return the sharpened Image.
   */
  public Photo Sharpen(Photo image_to_use, String dest_image_name);

  /**
   * Converts an image to sepia tone.
   * @param image_to_use the original image.
   * @param dest_image_name the name of the new sepia-toned image.
   * @return the sepia-toned Image.
   */
  public Photo Sepia(Photo image_to_use, String dest_image_name);
}
