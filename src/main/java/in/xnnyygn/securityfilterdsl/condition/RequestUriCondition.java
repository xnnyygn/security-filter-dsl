package in.xnnyygn.securityfilterdsl.condition;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Condition for requset URI.
 * 
 * @author xnnyygn
 */
public class RequestUriCondition extends AbstractPatternMatchCondition {

  @Override
  protected String determineValue(ActionExecutionContext context) {
    return context.getRequestURI();
  }

}
