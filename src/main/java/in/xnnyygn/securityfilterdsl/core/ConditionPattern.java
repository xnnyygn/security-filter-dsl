package in.xnnyygn.securityfilterdsl.core;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;

/**
 * Condition pattern
 * 
 * @author xnnyygn
 */
public interface ConditionPattern {

  boolean compare(String value, ActionExecutionContext context);

}
