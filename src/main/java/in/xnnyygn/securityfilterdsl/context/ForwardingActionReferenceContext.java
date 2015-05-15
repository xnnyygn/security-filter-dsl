package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.core.AbstractAction;
import in.xnnyygn.securityfilterdsl.core.Condition;

import java.util.Map;

/**
 * Forwarding action reference context.
 * 
 * @author xnnyygn
 */
public class ForwardingActionReferenceContext implements ActionReferenceContext {

  protected volatile ActionReferenceContext underlying;

  public AbstractAction find(String name) {
    return underlying.find(name);
  }

  public AbstractAction get(String name) {
    return underlying.get(name);
  }

  public AbstractAction getRoot() {
    return underlying.getRoot();
  }

  public Condition findCondition(String name) {
    return underlying.findCondition(name);
  }

  public void addAction(String name, AbstractAction action) {
    underlying.addAction(name, action);
  }

  public void setAttribute(String name, String value) {
    underlying.setAttribute(name, value);
  }

  public String getAttribute(String name) {
    return underlying.getAttribute(name);
  }

  public boolean containsAttribute(String name) {
    return underlying.containsAttribute(name);
  }

  public Map<String, String> getAttributes() {
    return underlying.getAttributes();
  }

}
