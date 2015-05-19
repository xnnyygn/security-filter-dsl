package in.xnnyygn.securityfilterdsl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation.
 * 
 * @author xnnyygn
 */
public class SimpleURLManager implements URLManager {

  /**
   * Location map. locationCode -> URI
   */
  private Map<String, String> locationMap = Collections.emptyMap();

  public String buildURL(String locationCode) {
    return buildURL(locationCode, new HashMap<String, Collection<String>>(0));
  }

  public String buildURL(String locationCode, Map<String, Collection<String>> parameters) {

    // check arguments
    if (locationCode == null || parameters == null) {
      throw new IllegalArgumentException("location code or parameters should not be null");
    }

    // check if URI exists
    String uri = locationMap.get(locationCode);
    if (uri == null) {
      throw new IllegalStateException("no such location, code " + locationCode);
    }

    StringBuilder builder = new StringBuilder(uri);
    if (!parameters.isEmpty()) {
      builder.append('?');
      for (String key : parameters.keySet()) {
        for (String value : parameters.get(key)) {
          builder.append(key).append('=').append(value).append('&');
        }
      }
      // remove redundant '&' at the end
      builder.deleteCharAt(builder.length() - 1);
    }
    return builder.toString();
  }

  /**
   * @see in.xnnyygn.securityfilterdsl.URLManager#exists(java.lang.String)
   */
  public boolean exists(String locationCode) {
    return locationMap.containsKey(locationCode);
  }

  public void setLocationMap(Map<String, String> locationMap) {
    this.locationMap = locationMap;
  }

}
