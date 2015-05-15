/**
 * Alipay.com Inc. Copyright (c) 2004-2015 All Rights Reserved.
 */
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
        allowing(mockActionExecutionContext).getRequestURI();
        will(returnValue("/foo.htm"));
        allowing(mockActionExecutionContext).getAttribute(with(any(String.class)));
        will(returnValue(null));
        allowing(mockActionExecutionContext).shouldRedirect();
        will(returnValue(true));
      }
    });
    interceptor.preHandle(null, null, null);
    Thread.sleep(5000);
    interceptor.preHandle(null, null, null);
  }

}
