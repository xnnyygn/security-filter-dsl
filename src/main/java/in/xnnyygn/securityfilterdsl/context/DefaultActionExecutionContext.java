package in.xnnyygn.securityfilterdsl.context;

import in.xnnyygn.securityfilterdsl.URLManager;
import in.xnnyygn.securityfilterdsl.core.AbstractAction;
import in.xnnyygn.securityfilterdsl.util.Function0;
import in.xnnyygn.securityfilterdsl.util.WebParamsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default implementation.
 * 
 * @author xnnyygn
 */
public class DefaultActionExecutionContext implements ActionExecutionContext {

  private static final Log log = LogFactory
      .getLog(DefaultActionExecutionContext.class);
  private static final Object OBJECT_NOT_FOUND = new Object() {
    public String toString() {
      // just for log
      return "OBJECT_NOT_FOUND";
    }
  };

  private URLManager urlManager;
  private String defaultErrorLocationCode;

  private HttpServletRequest request;
  private HttpServletResponse response;
  private ActionReferenceContext referenceContext;

  private boolean sentRedirect = false;
  private Map<String, Object> runtimeVariables = new HashMap<String, Object>();
  private LinkedList<AbstractAction> actionStack =
      new LinkedList<AbstractAction>();

  public boolean shouldRedirect() {
    return sentRedirect;
  }

  public void showError(String locationCode, String errorCode) {
    log.warn("raise error, code " + errorCode + ", execution description "
        + getExecutionDescription());
    Map<String, Collection<String>> parameters =
        new HashMap<String, Collection<String>>();
    parameters.put("errorCode", Arrays.asList(errorCode));
    if (defaultErrorLocationCode != null) {
      stayOrChangeLocation(locationCode, parameters, false);
    }
  }

  public void stayOrChangeLocation(String locationCode,
      Map<String, Collection<String>> parameters, boolean allowPost) {
    if (allowPost && "POST".equals(request.getMethod())) {
      log.debug("allow post request");
      return;
    }
    Map<String, Collection<String>> combinedParameters =
        WebParamsUtils.collectParameters(request);
    combinedParameters.putAll(parameters);
    if (log.isInfoEnabled()) {
      log.info(String.format(
          "target location code [%s], combined paramters %s", locationCode,
          combinedParameters));
    }
    String targetURL = urlManager.buildURL(locationCode, combinedParameters);
    String targetURI = parseURI(targetURL);
    String currentURI = request.getRequestURI();
    if (log.isDebugEnabled()) {
      log.debug(String.format(
          "target URL [%s], target URI [%s], current URI [%s]", targetURL,
          targetURI, currentURI));
    }
    // test if target URI equals to current URI
    if (!StringUtils.equals(targetURI, currentURI)) {
      sendRedirect(targetURL);
    }
  }

  public String findLocation(String locationCode) {
    return urlManager.buildURL(locationCode);
  }

  /**
   * Parse URI from URL.
   * 
   * @param url URL, should not be blank
   * @return URI
   * @see URL#getPath()
   */
  private String parseURI(String url) {
    try {
      return new URL(url).getPath();
    } catch (Exception e) {
      throw new ActionExecutionException("failed to parse URI from URL " + url,
          e);
    }
  }

  /**
   * Set redirect URL in response.
   * 
   * @param url target URL
   */
  private void sendRedirect(String url) {
    sentRedirect = true;
    try {
      response.sendRedirect(url);
    } catch (IOException e) {
      throw new ActionExecutionException("failed to redirect", e);
    }
  }



  /**
   * Return runtime variable if variable exists in {@link #runtimeVariables}, or
   * get and set runtime variable by calling function provided in parameters. If
   * some variable cannot be found, {@link #OBJECT_NOT_FOUND} will be put into
   * {@link #runtimeVariables} to prevent future calling.
   * 
   * @param key the key of runtime variable
   * @param f function which can be called to get runtime variable value
   * @return runtime variable value
   */
  @SuppressWarnings("unchecked")
  protected <T> T getRuntimeVariable(String key, Function0<T> f) {
    Object obj = runtimeVariables.get(key);
    if (obj == OBJECT_NOT_FOUND) return null;
    if (obj != null) return (T) obj;
    T value = f.apply();
    runtimeVariables.put(key, value != null ? value : OBJECT_NOT_FOUND);
    return value;
  }

  public String getRequestURI() {
    return request.getRequestURI();
  }

  public String getRequestMethod() {
    return request.getMethod();
  }

  public String getAttribute(String name) {
    return referenceContext.getAttribute(name);
  }

  public void setRuntimeVariable(String name, Object value) {
    runtimeVariables.put(name, value);
  }

  public String getExecutionDescription() {
    StringBuilder builder = new StringBuilder();
    builder.append("ATTRIBUTES: ").append(referenceContext.getAttributes());
    builder.append(" RUNTIME_VARIABLES: ").append(runtimeVariables);
    builder.append(" ACTION STACK: ").append(actionStack);
    return builder.toString();
  }

  public boolean execute() {
    try {
      doExecute();
      copyRuntimeVariablesToRequest(Arrays.asList("foo", "bar"));
      return true;
    } catch (ActionExecutionException e) {
      log.warn("failed to execute action", e);
      showError(defaultErrorLocationCode, "ACTION_EXECUTION_FAILED");
      return false;
    }
  }

  /**
   * Copy runtime variables into request attributes.
   */
  private void copyRuntimeVariablesToRequest(Collection<String> names) {
    for (String key : names) {
      Object value = runtimeVariables.get(key);
      if (value != OBJECT_NOT_FOUND) {
        request.setAttribute(key, value);
      }
    }
  }

  /**
   * Execute action.
   */
  private void doExecute() {
    // first action must be root
    actionStack.addFirst(referenceContext.getRoot());
    while (true) {
      // determine next action
      AbstractAction action = actionStack.getFirst().execute(this);
      // reach the end
      if (action == null) break;
      actionStack.addFirst(action);
    }
  }

  public void debug(String value) {
    // do nothing, just for debug and test
  }

  /**
   * Setter method for property <tt>defaultErrorLocationCode</tt>.
   * 
   * @param defaultErrorLocationCode value to be assigned to property
   *        defaultErrorLocationCode
   */
  public void setDefaultErrorLocationCode(String defaultErrorLocationCode) {
    this.defaultErrorLocationCode = defaultErrorLocationCode;
  }

  /**
   * Setter method for property <tt>referenceContext</tt>.
   * 
   * @param referenceContext value to be assigned to property referenceContext
   */
  public void setReferenceContext(ActionReferenceContext referenceContext) {
    this.referenceContext = referenceContext;
  }


  /**
   * Setter method for property <tt>request</tt>.
   * 
   * @param request value to be assigned to property request
   */
  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  /**
   * Setter method for property <tt>response</tt>.
   * 
   * @param response value to be assigned to property response
   */
  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }

  public void setUrlManager(URLManager urlManager) {
    this.urlManager = urlManager;
  }

}
