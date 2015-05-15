package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.core.AbstractAction;
import in.xnnyygn.securityfilterdsl.core.Condition;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract action reference context.
 * 
 * @author xnnyygn
 */
public abstract class AbstractActionReferenceContext implements ActionReferenceContext {

  protected Map<String, String> attributes = new HashMap<String, String>();

  public AbstractAction get(String name) {
    AbstractAction action = find(name);
    if (action == null) {
      throw new ActionExecutionException("action [" + name + "] required");
    }
    return action;
  }

  public AbstractAction getRoot() {
    return get(ACTION_NAME_ROOT);
  }

  public Condition findCondition(String name) {
    AbstractAction action = find(name);
    if (action == null)
      return null;
    if (!(action instanceof Condition)) {
      throw new ActionExecutionException("action [" + name + "] should be condition");
    }
    return (Condition) action;
  }

  public void setAttribute(String name, String value) {
    attributes.put(name, value);
  }

  public String getAttribute(String name) {
    return attributes.get(name);
  }

  public boolean containsAttribute(String name) {
    return attributes.containsKey(name);
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

}
