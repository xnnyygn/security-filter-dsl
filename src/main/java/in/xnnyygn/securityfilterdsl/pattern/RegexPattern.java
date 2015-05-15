package in.xnnyygn.securityfilterdsl.pattern;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.core.ConditionPattern;

import java.util.regex.Pattern;

/**
 * Regex pattern.
 * 
 * @author xnnyygn
 */
public class RegexPattern implements ConditionPattern {

  private final Pattern pattern;

  public RegexPattern(String regex) {
    pattern = Pattern.compile(regex);
  }

  public boolean compare(String value, ActionExecutionContext context) {
    return pattern.matcher(value).matches();
  }

  @Override
  public String toString() {
    return "RegexPattern [" + pattern + "]";
  }

}
