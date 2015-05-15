package in.xnnyygn.securityfilterdsl.pattern;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.core.ConditionPattern;

import org.apache.commons.lang.StringUtils;

/**
 * Variable pattern.
 * 
 * @author xnnyygn
 */
public class VariablePattern implements ConditionPattern {

  private final String variable;

  public VariablePattern(String variable) {
    super();
    this.variable = variable;
  }

  public boolean compare(String value, ActionExecutionContext context) {
    return StringUtils.equals(context.getAttribute(variable), value);
  }

  public String getVariable() {
    return variable;
  }

  @Override
  public String toString() {
    return "VariablePattern [" + variable + "]";
  }

}
