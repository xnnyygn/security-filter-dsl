package in.xnnyygn.securityfilterdsl.action;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.core.Action;

/**
 * Debug action.
 * 
 * @author xnnyygn
 */
public class DebugAction extends Action {

  private final String value;

  public DebugAction(String value) {
    super();
    this.value = value;
  }

  @Override
  protected void doExecute(ActionExecutionContext context) {
    context.debug(value);
  }

  @Override
  public String toString() {
    return "DebugAction [value=" + value + "]";
  }

}
