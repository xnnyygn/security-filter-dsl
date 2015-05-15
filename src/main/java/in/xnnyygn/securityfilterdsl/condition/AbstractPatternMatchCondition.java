package in.xnnyygn.securityfilterdsl.condition;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.core.AbstractAction;
import in.xnnyygn.securityfilterdsl.core.Condition;
import in.xnnyygn.securityfilterdsl.core.ConditionPattern;
import in.xnnyygn.securityfilterdsl.util.Tuple2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Abstract simple pattern condition.
 * 
 * @author xnnyygn
 */
public abstract class AbstractPatternMatchCondition extends Condition {

  private static final Log log = LogFactory.getLog(AbstractPatternMatchCondition.class);
  protected List<Tuple2<ConditionPattern, AbstractAction>> branches =
      new ArrayList<Tuple2<ConditionPattern, AbstractAction>>();

  @Override
  protected AbstractAction matchPattern(ActionExecutionContext context) {
    String value = determineValue(context);
    if (log.isDebugEnabled()) {
      log.debug(getClass().getSimpleName() + " match value " + value);
    }
    context.setRuntimeVariable("matchValue", value);
    for (Tuple2<ConditionPattern, AbstractAction> item : branches) {
      if (match(item._1, value, context)) {
        return item._2;
      }
    }
    return null;
  }

  protected boolean match(ConditionPattern pattern, String value, ActionExecutionContext context) {
    return pattern.compare(value, context);
  }

  protected abstract String determineValue(ActionExecutionContext context);

  @Override
  public void addBranchAction(ConditionPattern pattern, AbstractAction action) {
    branches.add(new Tuple2<ConditionPattern, AbstractAction>(pattern, action));
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[branches=" + branches + ",fallback=" + fallbackAction
        + "]";
  }

}
