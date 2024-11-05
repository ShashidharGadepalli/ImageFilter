/**
 * The View interface acts as a view in the MVC (Model-View-Controller) pattern.
 * Its main function is to print feedback message to the user based on operations performed.
 * The view is responsible for displaying information to the user.
 */
public interface View {
  /**
   * Prints feedback to the user.
   * mostly used to inform the user to display the message, if function fails.
   * @param feedback a string message that will be printed or displayed to the user.
   */
  public void printFeedback(String feedback);
}
