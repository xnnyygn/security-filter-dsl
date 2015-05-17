package in.xnnyygn.securityfilterdsl;

import java.util.Collection;
import java.util.Map;

/**
 * Manager for URL.
 * 
 * @author xnnyygn
 */
public interface URLManager {

  String buildURL(String locationCode);
  
  String buildURL(String locationCode, Map<String, Collection<String>> parameters);
  
}
