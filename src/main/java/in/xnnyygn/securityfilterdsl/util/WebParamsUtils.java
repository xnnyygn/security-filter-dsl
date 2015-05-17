package in.xnnyygn.securityfilterdsl.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities for web parameters.
 * 
 * @author xnnyygn
 */
public class WebParamsUtils {

  /**
   * Collect simple parameters from request.
   * 
   * @param request request, should not be null
   * @return parameter map, never be null
   * @see HttpServletRequest#getParameterNames()
   * @see HttpServletRequest#getParameter(String)
   */
  public static Map<String, String> collectSimpleParameters(HttpServletRequest request) {
    Map<String, String> params = new HashMap<String, String>();
    Enumeration<?> nameEnumeration = request.getParameterNames();
    while (nameEnumeration.hasMoreElements()) {
      String name = (String) nameEnumeration.nextElement();
      params.put(name, request.getParameter(name));
    }
    return params;
  }

  public static Map<String, Collection<String>> collectParameters(HttpServletRequest request) {
    Map<String, Collection<String>> params = new HashMap<String, Collection<String>>();
    Enumeration<?> nameEnumeration = request.getParameterNames();
    while (nameEnumeration.hasMoreElements()) {
      String name = (String) nameEnumeration.nextElement();
      params.put(name, Arrays.asList(request.getParameterValues(name)));
    }
    return params;
  }

}
