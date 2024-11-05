import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


import java.io.File;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class to test the save and load functions.
 */
public class SaveAndLoadTest {

  private ImageController Controller;
  private ImageModel model;
  Photo ppm;

  @Before
  public void setUp() {
    Controller = new ImageController();
    model = new ImageModel();
    ppm = Controller.Load("resources/Sample-ppm.ppm", "ImagePPM");
    model.updateDirectory("ImagePPM", ppm);
  }

  @Test
  public void testLoad() {
    assertNotNull(ppm);
    assertEquals(model.getImagePhoto("ImagePPM"), ppm);
  }

  @Test
  public void testLoadInvalid() {
    Photo newPH = Controller.Load("resources/null.ext", "ImagePPM");
    assertNull(newPH);
  }

  @Test
  public void testSaveInvalid() {
    Photo newPH = Controller.Save("resources/sample-jpg.jpg", "null");
    assertNull(newPH);
  }

  @Test
  public void testSavePPM() {
    String inputPath = "resources/sample-ppm.ppm";
    String imageName = "ImagePPM";
    String outputFile = "saved/saved-image.ppm";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSavePNG() {
    String inputPath = "resources/sample-png.png";
    String imageName = "ImagePNG";
    String outputFile = "saved/saved-image.png";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSaveJPG() {
    String inputPath = "resources/sample-jpg.jpg";
    String imageName = "ImageJPG";
    String outputFile = "saved/saved-image.jpg";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSavePPMtoJPG() {
    String inputPath = "resources/sample-ppm.ppm";
    String imageName = "ImageJPG";
    String outputFile = "saved/saved-image.jpg";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSavePPMtoPNG() {
    String inputPath = "resources/sample-ppm.ppm";
    String imageName = "ImagePNG";
    String outputFile = "saved/saved-image.png";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSavePNGtoPPM() {
    String inputPath = "resources/sample-png.png";
    String imageName = "ImageJPG";
    String outputFile = "saved/saved-image.ppm";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSavePNGtoJPG() {
    String inputPath = "resources/sample-png.png";
    String imageName = "ImageJPG";
    String outputFile = "saved/saved-image.jpg";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSaveJPGToPNG() {
    String inputPath = "resources/sample-jpg.jpg";
    String imageName = "ImagePNG";
    String outputFile = "saved/saved-image.png";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }

  @Test
  public void testSaveJPGToPPM() {
    String inputPath = "resources/sample-jpg.jpg";
    String imageName = "ImagePPM";
    String outputFile = "saved/saved-image.ppm";
    Controller.Load(inputPath, imageName);
    Photo savedImage = Controller.Save(outputFile, imageName);
    assertNotNull(savedImage);
    File output = new File(outputFile);
    assertTrue(output.exists());
  }
}
