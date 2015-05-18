package in.xnnyygn.securityfilterdsl;

import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.context.ActionExecutionContextFactory;
import in.xnnyygn.securityfilterdsl.context.ActionReferenceContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * Test.
 * 
 * @author xnnyygn
 */
public class SolutionInterceptorTest {

  private static final String ACTION_CONFIG_PATH = "src/main/resources/security-filter-rule.txt";
  private SecurityInterceptor interceptor;
  private Mockery mockContext;
  private ActionExecutionContext mockActionExecutionContext;
  private ActionExecutionContextFactory actionExecutionFactory =
      new ActionExecutionContextFactory() {

        public ActionExecutionContext create(HttpServletRequest request,
            HttpServletResponse response, ActionReferenceContext referenceContext) {
          return mockActionExecutionContext;
        }
      };

  @Before
  public void setUp() throws Exception {
    interceptor = new SecurityInterceptor();
    interceptor.setActionConfigPath(ACTION_CONFIG_PATH);
    interceptor.setActionExecutionContextFactory(actionExecutionFactory);
    interceptor.afterPropertiesSet();

    mockContext = new JUnit4Mockery();
    mockActionExecutionContext = mockContext.mock(ActionExecutionContext.class);
  }

  @Test
  public void test() throws Exception {
    mockContext.checking(new Expectations() {
      {
        oneOf(mockActionExecutionContext).execute();
        will(returnValue(true));
        oneOf(mockActionExecutionContext).shouldRedirect();
        will(returnValue(false));
      }
    });
    interceptor.preHandle(null, null, null);
  }

}
