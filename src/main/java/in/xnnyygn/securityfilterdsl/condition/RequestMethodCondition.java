package in.xnnyygn.securityfilterdsl.condition;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Condition for request method.
 * 
 * @author xnnyygn
 */
public class RequestMethodCondition extends AbstractAttributeCondition {

  public RequestMethodCondition() {
    super("REQUEST_METHOD");
  }

  @Override
  protected String doDeterminePattern(ActionExecutionContext context) {
    return context.getRequestMethod();
  }

}
