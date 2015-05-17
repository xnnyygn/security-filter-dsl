package in.xnnyygn.securityfilterdsl.action;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.core.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Show action.
 * 
 * @author xnnyygn
 */
public class ShowAction extends Action {

  private static final Log log = LogFactory.getLog(ShowAction.class);
  private final String locationCode;
  private final boolean allowPost;
  private final Map<String, Collection<String>> parameters;

  public static ShowAction create(List<String> arguments) {
    String locationCode = arguments.get(0);
    Map<String, String> argumentPairs =
        parseArgumentPairs(arguments.subList(1, arguments.size()));
    boolean allowPost = "ALLOW_POST".equals(argumentPairs.get("options"));
    String parameters =
        StringUtils.defaultString(argumentPairs.get("parameters"));
    return new ShowAction(locationCode, allowPost, parseParameters(parameters));
  }

  private static Map<String, Collection<String>> parseParameters(
      String queryString) {
    Map<String, Collection<String>> parameters =
        new HashMap<String, Collection<String>>();
    for (String pair : StringUtils.split(queryString, '&')) {
      int equalIndex = pair.indexOf('=');
      if (equalIndex < 0) {
        log.warn("illegal parameter pair [" + pair + "]");
        continue;
      }
      String key = pair.substring(0, equalIndex);
      String value = pair.substring(equalIndex + 1);
      if (!parameters.containsKey(key)) {
        parameters.put(key, new ArrayList<String>());
      }
      parameters.get(key).add(value);
    }
    return parameters;
  }

  private static Map<String, String> parseArgumentPairs(List<String> arguments) {
    Map<String, String> argumentPairs = new HashMap<String, String>();
    for (String pair : arguments) {
      int colonIndex = pair.indexOf(':');
      if (colonIndex < 0) {
        log.warn("illegal action argument pair [" + pair + "]");
        continue;
      }
      String key = pair.substring(0, colonIndex);
      String value = pair.substring(colonIndex + 1);
      argumentPairs.put(key, value);
    }
    return argumentPairs;
  }

  public ShowAction(String locationCode, boolean allowPost,
      Map<String, Collection<String>> parameters) {
    super();
    this.locationCode = locationCode;
    this.allowPost = allowPost;
    this.parameters = parameters;
  }

  @Override
  protected void doExecute(ActionExecutionContext context) {
    context.stayOrChangeLocation(locationCode, parameters, allowPost);
  }

  @Override
  public String toString() {
    return "ShowAction [locationCode=" + locationCode + ", allowPost="
        + allowPost + ", parameters=" + parameters + "]";
  }

}
