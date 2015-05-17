package in.xnnyygn.securityfilterdsl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class SimpleURLManagerTest {

  private SimpleURLManager manager;

  @Before
  public void setUp() {
    manager = new SimpleURLManager();
    Map<String, String> locationMap = new HashMap<String, String>();
    locationMap.put("fooLink", "http://foo.com");
    locationMap.put("barLink", "http://bar.com");
    manager.setLocationMap(locationMap);
  }

  @Test
  public void testBuildURL() {
    assertEquals("http://foo.com", manager.buildURL("fooLink"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildURLArgumentIsNull() {
    manager.buildURL(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildURLLocationNotFound() {
    manager.buildURL("noSuchLocationLink");
  }

  @Test
  public void testBuildURLSingleParameter() {
    Map<String, Collection<String>> parameters =
        new HashMap<String, Collection<String>>();
    parameters.put("a", Arrays.asList("b"));
    assertEquals("http://bar.com?a=b", manager.buildURL("barLink", parameters));
  }


  @Test
  public void testBuildURLMoreParameters() {
    // use tree map to ensure iteration order
    Map<String, Collection<String>> parameters =
        new TreeMap<String, Collection<String>>();
    parameters.put("a", Arrays.asList("b"));
    parameters.put("c", Arrays.asList("d"));
    assertEquals("http://bar.com?a=b&c=d",
        manager.buildURL("barLink", parameters));
  }

  @Test
  public void testBuildURLMultipleParameterValues() {
    // use tree map to ensure iteration order
    Map<String, Collection<String>> parameters =
        new TreeMap<String, Collection<String>>();
    parameters.put("a", Arrays.asList("b"));
    parameters.put("c", Arrays.asList("d", "e", "f"));
    assertEquals("http://bar.com?a=b&c=d&c=e&c=f",
        manager.buildURL("barLink", parameters));
  }
}
