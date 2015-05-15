package in.xnnyygn.securityfilterdsl;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.context.ActionExecutionContextFactory;
import in.xnnyygn.securityfilterdsl.context.ActionReferenceContext;
import in.xnnyygn.securityfilterdsl.context.RefreshableActionReferenceContext;
import in.xnnyygn.securityfilterdsl.parser.ActionConfigParser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor for security.
 * 
 * @author xnnyygn
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

  private static final Log log = LogFactory.getLog(SecurityInterceptor.class);
  private ActionExecutionContextFactory actionExecutionContextFactory;
  private ActionConfigParser actionConfigParser = new ActionConfigParser();
  private Resource actionConfig;
  private String actionConfigPath;
  private ActionReferenceContext actionReferenceContext;

  public void afterPropertiesSet() throws Exception {
    actionReferenceContext = determineActionReferenceContext();
    if (log.isInfoEnabled()) {
      log.info("action reference context " + actionReferenceContext);
    }
  }

  /**
   * Determine action reference context
   * 
   * @return context, never be null
   * @throws IOException
   */
  private ActionReferenceContext determineActionReferenceContext() throws IOException {
    if (actionConfig != null) {
      return actionConfigParser.parse(actionConfig.getInputStream());
    }
    RefreshableActionReferenceContext context =
        new RefreshableActionReferenceContext(actionConfigParser, 30, actionConfigPath);
    context.start();
    return context;
  }

  /**
   * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, java.lang.Object)
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    ActionExecutionContext context =
        actionExecutionContextFactory.create(request, response, actionReferenceContext);
    if (context.execute())
      return !context.shouldRedirect();

    // if exception is thrown, system will be redirected to error page
    return false;
  }

  /**
   * Setter method for property <tt>actionConfig</tt>.
   * 
   * @param actionConfig value to be assigned to property actionConfig
   */
  public void setActionConfig(Resource actionConfig) {
    this.actionConfig = actionConfig;
  }

  /**
   * Setter method for property <tt>actionConfigPath</tt>.
   * 
   * @param actionConfigPath value to be assigned to property actionConfigPath
   */
  public void setActionConfigPath(String actionConfigPath) {
    this.actionConfigPath = actionConfigPath;
  }

  /**
   * Setter method for property <tt>actionExecutionContextFactory</tt>.
   * 
   * @param actionExecutionContextFactory value to be assigned to property
   *        actionExecutionContextFactory
   */
  public void setActionExecutionContextFactory(
      ActionExecutionContextFactory actionExecutionContextFactory) {
    this.actionExecutionContextFactory = actionExecutionContextFactory;
  }

}
