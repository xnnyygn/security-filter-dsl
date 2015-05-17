package in.xnnyygn.securityfilterdsl;

import in.xnnyygn.securityfilterdsl.context.DefaultActionExecutionContext;
import in.xnnyygn.securityfilterdsl.context.DefaultActionReferenceContext;
import in.xnnyygn.securityfilterdsl.parser.ActionConfigParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

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

  private DefaultActionExecutionContext context;
  private Mockery mockContext;
  private MockHttpServletRequest mockHttpServletRequest;
  private MockHttpServletResponse mockHttpServletResponse;

  @Before
  public void setUp() throws Exception {
    mockContext = new JUnit4Mockery();
    mockHttpServletRequest = new MockHttpServletRequest();
    mockHttpServletResponse = new MockHttpServletResponse();

    context = new DefaultActionExecutionContext();
    context.setRequest(mockHttpServletRequest);
    context.setResponse(mockHttpServletResponse);
  }

  @Test
  public void testShowError() throws MalformedURLException {
    context.setReferenceContext(new DefaultActionReferenceContext());
    mockHttpServletRequest.setRequestURI("/bar.htm");

    mockContext.checking(new Expectations() {
      {
      }
    });
    context.showError("errorLink", "SOME_ERROR_CODE");
  }

  @Test
  public void testExecute() throws FileNotFoundException, IOException {
    context.setReferenceContext(new ActionConfigParser().parse(new FileInputStream(
        "src/test/resources/action-config/rule-show-error-3.txt")));
    mockHttpServletRequest.setRequestURI("/bar.htm");
    mockContext.checking(new Expectations() {
      {
      }
    });
    context.execute();
  }

  @Test
  public void testExecuteFailed() throws IOException {
    context.setReferenceContext(new ActionConfigParser().parse(new FileInputStream(
        "src/test/resources/action-config/rule-execute-failed.txt")));
    mockContext.checking(new Expectations() {
      {
      }
    });
    context.execute();
  }
}
