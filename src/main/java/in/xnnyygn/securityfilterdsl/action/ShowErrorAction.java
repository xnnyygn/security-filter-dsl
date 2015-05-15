package in.xnnyygn.securityfilterdsl.action;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.core.Action;

/**
 * Action to show error page.
 * 
 * @author xnnyygn
 */
public class ShowErrorAction extends Action {

  private final String errorLocationCode;
  private final String errorCode;

  public ShowErrorAction(String errorLocationCode, String errorCode) {
    super();
    this.errorLocationCode = errorLocationCode;
    this.errorCode = errorCode;
  }

  @Override
  protected void doExecute(ActionExecutionContext context) {
    context.showError(errorLocationCode, errorCode);
  }

  @Override
  public String toString() {
    return "ShowErrorAction [errorLocationCode=" + errorLocationCode + ", errorCode=" + errorCode
        + "]";
  }

}
