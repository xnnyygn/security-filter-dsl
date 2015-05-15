package in.xnnyygn.securityfilterdsl.core;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Abstract action.
 * 
 * @author xnnyygn
 */
public abstract class AbstractAction {

  public abstract AbstractAction execute(ActionExecutionContext context);

}
