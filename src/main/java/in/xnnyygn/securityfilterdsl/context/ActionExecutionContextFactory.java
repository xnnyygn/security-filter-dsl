package in.xnnyygn.securityfilterdsl.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Factory for action execution context.
 * 
 * @author xnnyygn
 */
public interface ActionExecutionContextFactory {

  ActionExecutionContext create(HttpServletRequest request, HttpServletResponse response,
      ActionReferenceContext referenceContext);

}
