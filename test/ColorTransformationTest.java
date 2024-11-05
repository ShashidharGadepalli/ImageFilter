import java.util.Random;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * Test class ColorTransformation to test transforming image color functions.
 * These functions AdjustComponent,Split, Combine.
 * AdjustComponent all 6 luma, intensity, red, green, value and blue.
 */
public class ColorTransformationTest {

  private ImageModel Model;
  private ImageController Controller;
  Photo png;
  Photo jpg;

  @Before
  public void setUp() {
    Model = new ImageModel();
    Controller = new ImageController();
    Controller.Load("resources/sample-png.png", "ImagePNG");
    Controller.Load("resources/sample-jpg.jpg", "ImageJPG");
    png = Model.getImagePhoto("ImagePNG");
    jpg = Model.getImagePhoto("ImageJPG");
  }

  @Test
  public void RedComponentTest() {
    Photo RedJpg = Model.AdjustComponent(jpg, "RedJPG", "red");
    Photo RedPng = Model.AdjustComponent(png, "RedPNG", "red");
    validateComponents(jpg, RedJpg, "red");
    validateComponents(png, RedPng, "red");
  }

  @Test
  public void BlueComponentTest() {
    Photo BlueJpg = Model.AdjustComponent(jpg,
        "BlueComponent", "blue");
    Photo BluePng = Model.AdjustComponent(png, "BluePNG",
        "blue");
    validateComponents(jpg, BlueJpg, "blue");
    validateComponents(png, BluePng, "blue");
  }

  @Test
  public void GreenComponentTest() {
    Photo GreenJpg = Model.AdjustComponent(jpg, "GreenComponent",
        "green");
    Photo GreenPng = Model.AdjustComponent(png, "GreenPNG", "green");
    validateComponents(jpg, GreenJpg, "green");
    validateComponents(png, GreenPng, "green");
  }

  @Test
  public void valueComponentTest() {
    Photo ValueJpg = Model.AdjustComponent(jpg, "ValueComponent",
        "value");
    Photo ValuePng = Model.AdjustComponent(png, "ValuePNG",
        "value");
    validateComponents(jpg, ValueJpg, "value");
    validateComponents(png, ValuePng, "value");
  }

  @Test
  public void InvalidComponentTest() {
    Photo InvalidJpg = Model.AdjustComponent(jpg, "InvalidCOmp",
        "invalid");
    Photo InvalidPng = Model.AdjustComponent(png, "InvalidPNG",
        "invalid");
    validateComponents(jpg, InvalidJpg, "invalid");
    validateComponents(png, InvalidPng, "invalid");
  }

  @Test
  public void NullComponentTest() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setOut(System.out);
    String destImageName = "redNullImage";
    Controller.commandExecutor("red-component" + " null " + destImageName);
    String expectedFeedback = "Error: Given image is null";
    assertTrue(outContent.toString().contains(expectedFeedback));
  }

  @Test
  public void LumaComponentTest() {
    Photo LumaJpg = Model.AdjustComponent(jpg, "LumaComponent",
        "luma");
    Photo LumaPng = Model.AdjustComponent(png, "LumaPNG",
        "luma");
    validateComponents(jpg, LumaJpg, "luma");
    validateComponents(png, LumaPng, "luma");
  }

  @Test
  public void IntensityComponentTest() {
    Photo IntensityJpg = Model.AdjustComponent(jpg, "IntensityComponent",
        "intensity");
    Photo IntensityPng = Model.AdjustComponent(png, "IntensityPNG",
        "intensity");
    validateComponents(jpg, IntensityJpg, "intensity");
    validateComponents(png, IntensityPng, "intensity");
  }

  @Test
  public void SplitAndCombineTest() {
    String redImage = "RedImage";
    String blueImage = "BlueImage";
    String greenImage = "GreenImage";
    Photo[] SplitImagesJpg = Model.Split(jpg, redImage, greenImage, blueImage);
    Photo[] SplitImagesPng = Model.Split(png, redImage, greenImage, blueImage);
    validateSplit(jpg, SplitImagesJpg);
    validateSplit(png, SplitImagesPng);
    Photo CombinedJpg = Model.Combine(
        "CombinedJpg",
        SplitImagesJpg[0],
        SplitImagesJpg[1],
        SplitImagesJpg[2]
    );
    Photo CombinedPng = Model.Combine(
        "CombinedPng",
        SplitImagesPng[0],
        SplitImagesPng[1],
        SplitImagesPng[2]
    );
    validateCombine(jpg, CombinedJpg);
    validateCombine(png, CombinedPng);
  }

  @Test
  public void TestColorSplitComponent() {
    Photo RedPng = Model.AdjustComponent(png, "RedPNG", "red");
    Photo GreenPng = Model.AdjustComponent(png, "GreenPNG", "green");
    Photo BluePng = Model.AdjustComponent(png, "BluePNG", "blue");
    Photo[] SplitImages = Model.Split(png, "RedImage", "GreenImage",
        "BlueImage");
    Photo RedImagePng = SplitImages[0];
    Photo GreenImagePng = SplitImages[1];
    Photo BlueImagePng = SplitImages[2];
    assertEquals(RedImagePng, RedPng);
    assertEquals(GreenImagePng, GreenPng);
    assertEquals(BlueImagePng, BluePng);
  }


  private void validateComponents(Photo originalPhoto, Photo componentImage, String componentName) {
    Random rand = new Random();
    int width = originalPhoto.getWidth();
    int height = originalPhoto.getHeight();
    for (int i = 0; i < 50; i++) {
      int x = rand.nextInt(width);
      int y = rand.nextInt(height);
      int[] originalPixel = originalPhoto.getPixels()[x][y];
      int[] componentPixel = componentImage.getPixels()[x][y];
      switch (componentName) {
        case "red":
          assertEquals(originalPixel[0], componentPixel[0]);
          assertEquals(originalPixel[0], componentPixel[1]);
          assertEquals(originalPixel[0], componentPixel[2]);
          break;
        case "green":
          assertEquals(originalPixel[1], componentPixel[0]);
          assertEquals(originalPixel[1], componentPixel[1]);
          assertEquals(originalPixel[1], componentPixel[2]);
          break;
        case "blue":
          assertEquals(originalPixel[2], componentPixel[0]);
          assertEquals(originalPixel[2], componentPixel[1]);
          assertEquals(originalPixel[2], componentPixel[2]);
          break;
        case "luma":
          int lumaValue = (int) (0.2126 * originalPixel[0] + 0.7152 * originalPixel[1]
              + 0.0722 * originalPixel[2]);
          assertEquals(lumaValue, componentPixel[0]);
          assertEquals(lumaValue, componentPixel[1]);
          assertEquals(lumaValue, componentPixel[2]);
          break;
        case "value":
          int valueValue = Math.max(originalPixel[0], Math.max(originalPixel[1], originalPixel[2]));
          assertEquals(valueValue, componentPixel[0]);
          assertEquals(valueValue, componentPixel[1]);
          assertEquals(valueValue, componentPixel[2]);
          break;
        case "intensity":
          int intensityValue = (originalPixel[0] + originalPixel[1] + originalPixel[2]) / 3;
          assertEquals(intensityValue, componentPixel[0]);
          assertEquals(intensityValue, componentPixel[1]);
          assertEquals(intensityValue, componentPixel[2]);
          break;
        default:
          break;
      }
    }
  }

  private void validateSplit(Photo ext, Photo[] SplitImages) {
    Photo redImage = SplitImages[0];
    Photo greenImage = SplitImages[1];
    Photo blueImage = SplitImages[2];
    int width = ext.getWidth();
    int height = ext.getHeight();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] originalPixel = ext.getPixels()[j][i];
        int[] redPixel = redImage.getPixels()[j][i];
        int[] greenPixel = greenImage.getPixels()[j][i];
        int[] bluePixel = blueImage.getPixels()[j][i];

        assertEquals(originalPixel[0], redPixel[0]);
        assertEquals(originalPixel[0], redPixel[1]);
        assertEquals(originalPixel[0], redPixel[2]);

        assertEquals(originalPixel[1], greenPixel[0]);
        assertEquals(originalPixel[1], greenPixel[1]);
        assertEquals(originalPixel[1], greenPixel[2]);

        assertEquals(originalPixel[2], bluePixel[0]);
        assertEquals(originalPixel[2], bluePixel[1]);
        assertEquals(originalPixel[2], bluePixel[2]);
      }
    }
  }

  private void validateCombine(Photo ext, Photo Combined) {
    int width = ext.getWidth();
    int height = ext.getHeight();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] originalPixel = ext.getPixels()[j][i];
        int[] combinedPixel = Combined.getPixels()[j][i];
        assertEquals(originalPixel[0], combinedPixel[0]);
        assertEquals(originalPixel[1], combinedPixel[1]);
        assertEquals(originalPixel[2], combinedPixel[2]);
      }
    }
  }
}
