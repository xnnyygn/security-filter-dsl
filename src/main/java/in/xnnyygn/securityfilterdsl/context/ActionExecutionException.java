package in.xnnyygn.securityfilterdsl.context;

/**
 * Exception for action execution.
 * 
 * @author xnnyygn
 */
public class ActionExecutionException extends RuntimeException {

  private static final long serialVersionUID = 3135228865681397108L;

  /**
   * Constructor.
   * 
   * @param message
   */
  public ActionExecutionException(String message) {
    super(message);
  }

  /**
   * Constructor.
   * 
   * @param message
   * @param cause
   */
  public ActionExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

}
