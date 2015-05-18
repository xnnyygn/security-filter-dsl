package in.xnnyygn.securityfilterdsl;

import in.xnnyygn.securityfilterdsl.context.DefaultActionExecutionContext;
import in.xnnyygn.securityfilterdsl.context.DefaultActionReferenceContext;
import in.xnnyygn.securityfilterdsl.parser.ActionConfigParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Test.
 * 
 * @author xnnyygn
 */
public class DefaultActionExecutionContextTest {

  private static final String DEFAULT_ERROR_LINK = "defaultErrorLink";
  private DefaultActionExecutionContext context;
  private Mockery mockContext;
  private MockHttpServletRequest mockHttpServletRequest;
  private MockHttpServletResponse mockHttpServletResponse;
  private URLManager mockURLManager;

  @Before
  public void setUp() throws Exception {
    mockContext = new JUnit4Mockery();
    mockHttpServletRequest = new MockHttpServletRequest();
    mockHttpServletResponse = new MockHttpServletResponse();

    context = new DefaultActionExecutionContext();
    context.setRequest(mockHttpServletRequest);
    context.setResponse(mockHttpServletResponse);
    mockURLManager = mockContext.mock(URLManager.class);
    context.setUrlManager(mockURLManager);
    context.setDefaultErrorLocationCode(DEFAULT_ERROR_LINK);
  }

  @Test
  public void testShowError() throws MalformedURLException {
    context.setReferenceContext(new DefaultActionReferenceContext());
    mockHttpServletRequest.setRequestURI("/bar.htm");

    mockContext.checking(new Expectations() {
      {
        allowing(mockURLManager).exists(with(any(String.class)));
        will(returnValue(false));
      }
    });
    context.showError("errorLink", "SOME_ERROR_CODE");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testExecute() throws FileNotFoundException, IOException {
    context.setReferenceContext(new ActionConfigParser().parse(new FileInputStream(
        "src/test/resources/action-config/rule-show-error-3.txt")));
    mockHttpServletRequest.setRequestURI("/bar.htm");
    mockContext.checking(new Expectations() {
      {
        oneOf(mockURLManager).exists("errorLink");
        will(returnValue(true));
        oneOf(mockURLManager).buildURL(with("errorLink"), with(any(Map.class)));
        will(returnValue("http://error.com/error.htm"));
      }
    });
    context.execute();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testExecuteFailed() throws IOException {
    context.setReferenceContext(new ActionConfigParser().parse(new FileInputStream(
        "src/test/resources/action-config/rule-execute-failed.txt")));
    mockContext.checking(new Expectations() {
      {
        oneOf(mockURLManager).buildURL(with("fooLink"), with(any(Map.class)));
        will(throwException(new IllegalStateException("no such link fooLink")));

        allowing(mockURLManager).exists(DEFAULT_ERROR_LINK);
        will(returnValue(false));
      }
    });
    context.execute();
  }
}
