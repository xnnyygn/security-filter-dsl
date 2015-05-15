package in.xnnyygn.securityfilterdsl.pattern;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.core.ConditionPattern;

import org.apache.commons.lang.StringUtils;

/**
 * Literal pattern.
 * 
 * @author xnnyygn
 */
public class LiteralPattern implements ConditionPattern {

  private final String value;

  public LiteralPattern(String value) {
    super();
    this.value = value;
  }

  public boolean compare(String value, ActionExecutionContext context) {
    return StringUtils.equals(this.value, value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "LiteralPattern [" + value + "]";
  }

}
