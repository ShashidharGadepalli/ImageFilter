import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is an implementation of the model interface.
 * Uses a map imageDirectory that contains the name and Photo object for each image.
 * The imageDirectory on successful inputs directed by the ImageController.
 */
public class ImageModel implements Model {
  private static final Map<String, Photo> imageDirectory = new HashMap<>();

  @Override
  public Photo getImagePhoto(String Key) {
    return imageDirectory.get(Key);
  }

  @Override
  public void updateDirectory(String Key, Photo ph) {
    imageDirectory.put(Key, ph);
  }

  @Override
  public Photo AdjustComponent(Photo image_to_use, String dest_image_name, String component_name) {
    int height = image_to_use.getHeight();
    int width = image_to_use.getWidth();
    int[][][] pixels = image_to_use.getPixels();
    Photo adjustedImage = new ImagePhoto(dest_image_name, width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] pixel = pixels[j][i];
        int r = pixel[0];
        int g = pixel[1];
        int b = pixel[2];
        if (Objects.equals(component_name, "red")) {
          adjustedImage.setPixel(j, i, new int[]{r, r, r});
        }
        if (Objects.equals(component_name, "green")) {
          adjustedImage.setPixel(j, i, new int[]{g, g, g});
        }
        if (Objects.equals(component_name, "blue")) {
          adjustedImage.setPixel(j, i, new int[]{b, b, b});
        }
        if (Objects.equals(component_name, "luma")) {
          double v = 0.2126 * r + 0.7152 * g + 0.0722 * b;
          adjustedImage.setPixel(j, i, new int[]{(int) v, (int) v, (int) v});
        }
        if (Objects.equals(component_name, "value")) {
          int v = Math.max(r, Math.max(g, b));
          adjustedImage.setPixel(j, i, new int[]{v, v, v});
        }
        if (Objects.equals(component_name, "intensity")) {
          int v = (r + g + b) / 3;
          adjustedImage.setPixel(j, i, new int[]{v, v, v});
        }
      }
    }
    return adjustedImage;
  }

  @Override
  public Photo HorizontalFlip(Photo image_to_use, String dest_image_name) {
    int height = image_to_use.getHeight();
    int full_width = image_to_use.getWidth();
    int width = full_width / 2;
    int[][][] pixels = image_to_use.getPixels();
    Photo newImage = new ImagePhoto(dest_image_name, full_width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newImage.setPixel(j, i, pixels[full_width - j - 1][i]);
        newImage.setPixel(full_width - j - 1, i, pixels[j][i]);
      }
    }
    return newImage;
  }

  @Override
  public Photo VerticalFlip(Photo image_to_use, String dest_image_name) {
    int full_height = image_to_use.getHeight();
    int height = full_height / 2;
    int width = image_to_use.getWidth();
    int[][][] pixels = image_to_use.getPixels();
    Photo newImage = new ImagePhoto(dest_image_name, width, full_height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newImage.setPixel(j, i, pixels[j][full_height - i - 1]);
        newImage.setPixel(j, full_height - i - 1, pixels[j][i]);
      }
    }
    return newImage;
  }

  @Override
  public Photo Brighten(int intensity, Photo image_to_use, String dest_image_name) {
    int width = image_to_use.getWidth();
    int height = image_to_use.getHeight();
    int[][][] pixels = image_to_use.getPixels();
    Photo brightenedImage = new ImagePhoto(dest_image_name, width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] pixel = pixels[j][i];
        int r = Math.min(255, Math.max(0, pixel[0] + intensity));
        int g = Math.min(255, Math.max(0, pixel[1] + intensity));
        int b = Math.min(255, Math.max(0, pixel[2] + intensity));
        brightenedImage.setPixel(j, i, new int[]{r, g, b});

      }
    }
    return brightenedImage;
  }

  @Override
  public Photo[] Split(Photo image_to_use, String image_r, String image_g, String image_b) {
    Photo redImage = AdjustComponent(image_to_use, image_r, "red");
    Photo greenImage = AdjustComponent(image_to_use, image_g, "green");
    Photo blueImage = AdjustComponent(image_to_use, image_b, "blue");
    return new Photo[]{redImage, greenImage, blueImage};
  }

  @Override
  public Photo Combine(String dest_image_name, Photo image_r, Photo image_g, Photo image_b) {
    if (image_r == null || image_g == null || image_b == null) {
      return null;
    }
    int width = image_r.getWidth();
    int height = image_r.getHeight();
    int g_width = image_g.getWidth();
    int g_height = image_g.getHeight();
    int b_width = image_b.getWidth();
    int b_height = image_b.getHeight();
    if (width != g_width || width != b_width || height != b_height || height != g_height) {
      return null;
    }
    int[][][] r_pixels = image_r.getPixels();
    int[][][] g_pixels = image_g.getPixels();
    int[][][] b_pixels = image_b.getPixels();
    Photo newImage = new ImagePhoto(dest_image_name, width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newImage.setPixel(j, i, new int[]{r_pixels[j][i][0], g_pixels[j][i][1], b_pixels[j][i][2]});
      }
    }
    return newImage;
  }

  @Override
  public Photo Blur(Photo image_to_use, String dest_image_name) {
    int width = image_to_use.getWidth();
    int height = image_to_use.getHeight();
    int[][][] pixels = image_to_use.getPixels();
    Photo blurImage = new ImagePhoto(dest_image_name, width, height);

    double[][] blur = {
        {1 / 16.0, 1 / 8.0, 1 / 16.0},
        {1 / 8.0, 1 / 4.0, 1 / 8.0},
        {1 / 16.0, 1 / 8.0, 1 / 16.0}
    };

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        double[] newPixels = new double[3];

        for (int k = -1; k <= 1; k++) {
          for (int l = -1; l <= 1; l++) {
            if (j + l >= 0 && j + l < width && k + i >= 0 && k + i < height) {
              int[] pixel = pixels[j + l][i + k];
              newPixels[0] += pixel[0] * blur[k + 1][l + 1];
              newPixels[1] += pixel[1] * blur[k + 1][l + 1];
              newPixels[2] += pixel[2] * blur[k + 1][l + 1];
            }
          }
        }

        int newR = Math.min(255, Math.max(0, (int) newPixels[0]));
        int newG = Math.min(255, Math.max(0, (int) newPixels[1]));
        int newB = Math.min(255, Math.max(0, (int) newPixels[2]));
        blurImage.setPixel(j, i, new int[]{newR, newG, newB});
      }
    }
    return blurImage;
  }

  @Override
  public Photo Sharpen(Photo image_to_use, String dest_image_name) {
    int width = image_to_use.getWidth();
    int height = image_to_use.getHeight();
    int[][][] pixels = image_to_use.getPixels();
    Photo sharpImage = new ImagePhoto(dest_image_name, width, height);

    double[][] sharpen = {
        {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1.0, 1 / 4.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0},
        {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0}
    };

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        double[] newPixels = new double[3];

        for (int k = -2; k <= 2; k++) {
          for (int l = -2; l <= 2; l++) {
            if (j + l >= 0 && j + l < width && k + i >= 0 && k + i < height) {
              int[] pixel = pixels[j + l][i + k];
              newPixels[0] += pixel[0] * sharpen[k + 2][l + 2];
              newPixels[1] += pixel[1] * sharpen[k + 2][l + 2];
              newPixels[2] += pixel[2] * sharpen[k + 2][l + 2];
            }
          }
        }

        int newR = Math.min(255, Math.max(0, (int) newPixels[0]));
        int newG = Math.min(255, Math.max(0, (int) newPixels[1]));
        int newB = Math.min(255, Math.max(0, (int) newPixels[2]));
        sharpImage.setPixel(j, i, new int[]{newR, newG, newB});
      }
    }
    return sharpImage;
  }

  @Override
  public Photo Sepia(Photo image_to_use, String dest_image_name) {
    int height = image_to_use.getHeight();
    int width = image_to_use.getWidth();
    int[][][] pixels = image_to_use.getPixels();
    Photo sepiaImage = new ImagePhoto(dest_image_name, width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] pixel = pixels[j][i];
        int r = pixel[0];
        int g = pixel[1];
        int b = pixel[2];
        int newR = (int) (0.393 * r + 0.769 * g + 0.189 * b);
        int newG = (int) (0.349 * r + 0.686 * g + 0.168 * b);
        int newB = (int) (0.272 * r + 0.534 * g + 0.131 * b);
        newR = Math.min(255, newR);
        newG = Math.min(255, newG);
        newB = Math.min(255, newB);
        sepiaImage.setPixel(j, i, new int[]{newR, newG, newB});
      }
    }
    return sepiaImage;
  }
}
