package in.xnnyygn.securityfilterdsl.context;

import java.util.List;
import java.util.Map;

/**
 * Action execution context.
 * 
 * @author xnnyygn
 */
public interface ActionExecutionContext {

  void showError(String locationCode, String errorCode);

  void stayOrChangeLocation(String locationCode, Map<String, List<String>> parameters,
      boolean allowPost);

  boolean shouldRedirect();

  String getRequestURI();

  String getRequestMethod();

  String findLocation(String locationCode);

  String validOrderExists();

  String getAttribute(String name);

  void setRuntimeVariable(String name, Object value);

  String getExecutionDescription();

  boolean execute();

  void debug(String value);

}
