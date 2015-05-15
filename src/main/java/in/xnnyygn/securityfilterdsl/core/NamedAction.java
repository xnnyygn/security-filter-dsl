package in.xnnyygn.securityfilterdsl.core;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.context.ActionReferenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Named action.
 * 
 * @author xnnyygn
 */
public class NamedAction extends AbstractAction {

  private static Log log = LogFactory.getLog(NamedAction.class);
  private final ActionReferenceContext context;
  private final String name;

  public NamedAction(ActionReferenceContext context, String name) {
    this.context = context;
    this.name = name;
  }

  @Override
  public AbstractAction execute(ActionExecutionContext context) {
    if (log.isDebugEnabled()) {
      log.debug("execute action " + name);
    }
    return this.context.get(name).execute(context);
  }

  @Override
  public String toString() {
    return "NamedAction[" + name + "]";
  }

}
