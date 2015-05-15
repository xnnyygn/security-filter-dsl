package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.core.AbstractAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Action reference context.
 * 
 * @author xnnyygn
 */
public class DefaultActionReferenceContext extends AbstractActionReferenceContext {

  private Map<String, AbstractAction> actions = new HashMap<String, AbstractAction>();

  public AbstractAction find(String name) {
    return actions.get(name);
  }

  public void addAction(String name, AbstractAction action) {
    actions.put(name, action);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " [actions=" + actions + ", attributes=" + attributes + "]";
  }

}
