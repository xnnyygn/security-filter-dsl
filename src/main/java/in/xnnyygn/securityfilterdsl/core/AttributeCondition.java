package in.xnnyygn.securityfilterdsl.core;

import in.xnnyygn.securityfilterdsl.condition.AbstractAttributeCondition;
import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Attribute condition.
 * 
 * @author xnnyygn
 */
public class AttributeCondition extends AbstractAttributeCondition {

  public AttributeCondition(String name) {
    super(name);
  }

  @Override
  protected String doDeterminePattern(ActionExecutionContext context) {
    return null;
  }

}
