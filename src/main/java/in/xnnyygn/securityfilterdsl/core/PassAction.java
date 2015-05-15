package in.xnnyygn.securityfilterdsl.core;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Pass action.
 * 
 * @author xnnyygn
 */
public class PassAction extends Action {

  @Override
  protected void doExecute(ActionExecutionContext context) {}

  @Override
  public String toString() {
    return "PassAction";
  }

}
