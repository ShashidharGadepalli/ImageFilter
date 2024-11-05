/**
 * This class is an implementation of the view interface.
 * The printFeedback prints out the corresponding output back to the user.
 * The behavior is dictated by the ImageController.
 */
public class ImageView implements View {
  @Override
  public void printFeedback(String feedback) {
    System.out.println(feedback);
  }
}
