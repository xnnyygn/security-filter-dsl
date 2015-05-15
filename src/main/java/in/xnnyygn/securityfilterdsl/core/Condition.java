package in.xnnyygn.securityfilterdsl.core;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Condition.
 * 
 * @author xnnyygn
 */
public abstract class Condition extends AbstractAction {

  protected AbstractAction fallbackAction;

  @Override
  public AbstractAction execute(ActionExecutionContext context) {
    AbstractAction branchAction = matchPattern(context);
    return branchAction != null ? branchAction : fallbackAction;
  }

  protected abstract AbstractAction matchPattern(ActionExecutionContext context);

  public abstract void addBranchAction(ConditionPattern pattern, AbstractAction action);

  public void setFallbackAction(AbstractAction fallbackAction) {
    this.fallbackAction = fallbackAction;
  }

}
