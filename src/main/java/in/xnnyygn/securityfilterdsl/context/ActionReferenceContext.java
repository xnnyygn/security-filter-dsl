package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.core.AbstractAction;
import in.xnnyygn.securityfilterdsl.core.Condition;

import java.util.Map;

/**
 * Action reference context.
 * 
 * @author xnnyygn
 */
public interface ActionReferenceContext {

  // constants
  String ACTION_NAME_ROOT = "ROOT";

  // methods
  AbstractAction find(String name);

  AbstractAction get(String name);

  AbstractAction getRoot();

  Condition findCondition(String name);

  void addAction(String name, AbstractAction action);

  void setAttribute(String name, String value);

  boolean containsAttribute(String name);

  String getAttribute(String name);

  Map<String, String> getAttributes();

}
