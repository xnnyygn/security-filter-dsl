package in.xnnyygn.securityfilterdsl.condition;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Condition for request method.
 * 
 * @author xnnyygn
 */
public class RequestMethodCondition extends AbstractPatternMatchCondition {

  @Override
  protected String determineValue(ActionExecutionContext context) {
    return context.getRequestMethod();
  }

}
