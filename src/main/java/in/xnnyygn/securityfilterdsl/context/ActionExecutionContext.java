package in.xnnyygn.securityfilterdsl.context;

import java.util.Collection;
import java.util.Map;

/**
 * Action execution context.
 * 
 * @author xnnyygn
 */
public interface ActionExecutionContext {

  void showError(String locationCode, String errorCode);

  void stayOrChangeLocation(String locationCode, Map<String, Collection<String>> parameters,
      boolean allowPost);

  boolean shouldRedirect();

  String getRequestURI();
  
  String findLocation(String locationCode);

  String getRequestMethod();

  String getAttribute(String name);

  void setRuntimeVariable(String name, Object value);

  String getExecutionDescription();

  boolean execute();

  void debug(String value);

}
