package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.core.PassAction;

/**
 * Empty one.
 * 
 * @author xnnyygn
 */
public class EmptyActionReferenceContext extends DefaultActionReferenceContext {

  /**
   * Constructor.
   */
  public EmptyActionReferenceContext() {
    addAction(ACTION_NAME_ROOT, new PassAction());
  }

}
