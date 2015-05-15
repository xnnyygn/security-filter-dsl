package in.xnnyygn.securityfilterdsl.core;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Action.
 * 
 * @author xnnyygn
 */
public abstract class Action extends AbstractAction {

  @Override
  public AbstractAction execute(ActionExecutionContext context) {
    doExecute(context);
    return null;
  }

  protected abstract void doExecute(ActionExecutionContext context);

}
