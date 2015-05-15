package in.xnnyygn.securityfilterdsl.condition;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * A condition with attribute.
 * 
 * @author xnnyygn
 */
public abstract class AbstractAttributeCondition extends AbstractPatternMatchCondition {

  private final String name;

  public AbstractAttributeCondition(String name) {
    super();
    this.name = name;
  }

  @Override
  protected String determineValue(ActionExecutionContext context) {
    String value = context.getAttribute(name);
    return value != null ? value : doDeterminePattern(context);
  }

  protected abstract String doDeterminePattern(ActionExecutionContext context);

}
