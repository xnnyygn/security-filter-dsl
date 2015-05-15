package in.xnnyygn.securityfilterdsl.condition;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.context.ActionExecutionException;
import in.xnnyygn.securityfilterdsl.core.ConditionPattern;
import in.xnnyygn.securityfilterdsl.pattern.LiteralPattern;
import in.xnnyygn.securityfilterdsl.pattern.VariablePattern;

import org.apache.commons.lang.StringUtils;

/**
 * Condition for request URI end.
 * 
 * @author xnnyygn
 */
public class RequestURIEndCondition extends AbstractPatternMatchCondition {

  @Override
  protected String determineValue(ActionExecutionContext context) {
    return context.getRequestURI();
  }

  @Override
  protected boolean match(ConditionPattern pattern, String value, ActionExecutionContext context) {
    if (pattern instanceof VariablePattern) {
      String locationCode = ((VariablePattern) pattern).getVariable();
      return StringUtils.endsWith(context.findLocation(locationCode), value)
          || StringUtils.endsWith(context.getAttribute(locationCode), value);
    }
    if (pattern instanceof LiteralPattern) {
      return StringUtils.endsWith(((LiteralPattern) pattern).getValue(), value);
    }
    throw new ActionExecutionException("unsupported pattern " + pattern);
  }

}
