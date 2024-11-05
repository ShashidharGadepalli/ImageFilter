import java.io.File;
import java.util.Random;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * Test class SimpleProcessing to test basic image processing functions.
 * These functions include Brighten, Split, Combine, Sepia and Flip.
 * It also contains a small sample script test.
 */
public class SimpleProcessingTest {

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
  public void combinedBrightenImageTest() {
    testImageBrightening(png);
    testImageBrightening(jpg);
  }

  @Test
  public void NullImageBrightenTest() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setOut(System.out);
    String destImageName = "brightenedNullImage";
    Controller.commandExecutor("brighten " + 45 + " null " + destImageName);
    String expectedFeedback = "Error: Given image is null";
    assertTrue(outContent.toString().contains(expectedFeedback));
  }

  @Test
  public void CombinedSepiaTest() {
    testImageSepia(png);
    testImageSepia(jpg);
  }

  @Test
  public void NullSepiaTest() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setOut(System.out);
    String destImageName = "NullSepia";
    Controller.commandExecutor("sepia " + " null " + destImageName);
    String expectedFeedback = "Error: Given image is null";
    assertTrue(outContent.toString().contains(expectedFeedback));
  }

  @Test
  public void CombineBlurTest() {
    testImageBlur(png);
    testImageBlur(jpg);
  }

  @Test
  public void NullBlurTest() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setOut(System.out);
    String destImageName = "NullBlur";
    Controller.commandExecutor("blur " + " null " + destImageName);
    String expectedFeedback = "Error: Given image is null";
    assertTrue(outContent.toString().contains(expectedFeedback));
  }

  @Test
  public void CombineSharpenTest() {
    testImageSharpen(jpg);
    testImageSharpen(png);
  }

  @Test
  public void NullSharpenTest() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setOut(System.out);
    String destImageName = "NullSharpen";
    Controller.commandExecutor("Sharpen " + " null " + destImageName);
    String expectedFeedback = "Error: Given image is null";
    assertTrue(outContent.toString().contains(expectedFeedback));
  }

  @Test
  public void HorizontalFlipTest() {
    testImageHFlip(jpg);
    testImageHFlip(png);
  }

  @Test
  public void VerticalFlipTest() {
    testImageVeFlip(png);
    testImageVeFlip(jpg);
  }

  @Test
  public void FlipNullImage() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setOut(System.out);
    String destImageName = "NullFLip";
    Controller.commandExecutor("horizontal-flip " + " null " + destImageName);
    String expectedFeedback = "Error: Given image is null";
    assertTrue(outContent.toString().contains(expectedFeedback));
    String destImageName2 = "Vertical";
    Controller.commandExecutor("Vertical-flip " + " null " + destImageName2);
    assertTrue(outContent.toString().contains(expectedFeedback));

  }

  private void testImageBrightening(Photo ext) {
    Photo brightenedImage = Model.Brighten(45, ext, "BrightenedImage");
    Photo darkenedImage = Model.Brighten(-45, ext, "DarkenedImage");
    Photo zeroImage = Model.Brighten(0, ext, "ZeroImage");
    validateBrightenedImage(ext, brightenedImage, 45);
    validateBrightenedImage(ext, darkenedImage, -45);
    validateBrightenedImage(ext, zeroImage, 0);
  }

  private void validateBrightenedImage(Photo originalPhoto, Photo brightenedPhoto, int intensity) {
    Random rand = new Random();
    int width = originalPhoto.getWidth();
    int height = originalPhoto.getHeight();

    for (int i = 0; i < 50; i++) {
      int x = rand.nextInt(width);
      int y = rand.nextInt(height);

      int[] originalPixel = originalPhoto.getPixels()[x][y];
      int[] brightenedPixel = brightenedPhoto.getPixels()[x][y];

      assertEquals(Math.min(255, Math.max(0, originalPixel[0] + intensity)), brightenedPixel[0]);
      assertEquals(Math.min(255, Math.max(0, originalPixel[1] + intensity)), brightenedPixel[1]);
      assertEquals(Math.min(255, Math.max(0, originalPixel[2] + intensity)), brightenedPixel[2]);
    }
  }


  private void testImageSepia(Photo ext) {
    Photo Sepia = Model.Sepia(ext, "SepiaImage");
    ValidateSepia(ext, Sepia);

  }

  private void ValidateSepia(Photo originalPhoto, Photo Sepia) {
    Random rand = new Random();
    int width = originalPhoto.getWidth();
    int height = originalPhoto.getHeight();
    for (int i = 0; i < 50; i++) {
      int x = rand.nextInt(width);
      int y = rand.nextInt(height);
      int[] originalPixel = originalPhoto.getPixels()[x][y];
      int[] sepiaPixel = Sepia.getPixels()[x][y];
      int r = originalPixel[0];
      int g = originalPixel[1];
      int b = originalPixel[2];

      int newR = (int) (0.393 * r + 0.769 * g + 0.189 * b);
      int newG = (int) (0.349 * r + 0.686 * g + 0.168 * b);
      int newB = (int) (0.272 * r + 0.534 * g + 0.131 * b);

      int[] expectedSepiaPixel = new int[]{
          Math.min(255, newR),
          Math.min(255, newG),
          Math.min(255, newB)
      };
      assertArrayEquals(expectedSepiaPixel, sepiaPixel);
    }
  }

  private void testImageBlur(Photo ext) {
    Photo Blur = Model.Blur(ext, "BlurImage");
    assertNotEquals(ext, Blur);
  }

  private void testImageSharpen(Photo ext) {
    Photo Sharpen = Model.Sharpen(ext, "SharpenImage");
    assertNotEquals(ext, Sharpen);
  }

  private void testImageHFlip(Photo ext) {
    Photo HFlip = Model.HorizontalFlip(ext, "HorizontalFlipped");
    assertNotEquals(ext, HFlip);
  }

  private void testImageVeFlip(Photo ext) {
    Photo VFlip = Model.VerticalFlip(ext, "VerticallFlipped");
    assertNotEquals(ext, VFlip);
  }

  @Test
  public void testSmallScript() {
    Controller.runScript("resources/small-script.txt");
    File output = new File("landscape/bright-landscape.png");
    assertTrue(output.exists());
  }

}

