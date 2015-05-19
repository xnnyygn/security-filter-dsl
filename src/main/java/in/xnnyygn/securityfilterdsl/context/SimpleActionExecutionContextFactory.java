package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.SimpleURLManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple factory for {@link ActionExecutionContext}.
 * 
 * @author xnnyygn
 */
public class SimpleActionExecutionContextFactory implements ActionExecutionContextFactory {

  public ActionExecutionContext create(HttpServletRequest request, HttpServletResponse response,
      ActionReferenceContext referenceContext) {
    DefaultActionExecutionContext context = new DefaultActionExecutionContext();
    context.setReferenceContext(referenceContext);
    context.setRequest(request);
    context.setResponse(response);
    context.setUrlManager(new SimpleURLManager());
    return context;
  }

}
