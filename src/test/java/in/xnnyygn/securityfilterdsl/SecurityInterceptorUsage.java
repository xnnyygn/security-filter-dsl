package in.xnnyygn.securityfilterdsl;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import in.xnnyygn.securityfilterdsl.context.SimpleActionExecutionContextFactory;

public class SecurityInterceptorUsage {

  public static void main(String[] args) throws Exception {
    SecurityInterceptor interceptor = new SecurityInterceptor();
    interceptor.setActionExecutionContextFactory(new SimpleActionExecutionContextFactory());
    // enable the powerful refreshable action reference context
    // but you have to stop the thread manually
    interceptor.setActionConfigPath("src/main/resources/security-filter-rule.txt");
    interceptor.afterPropertiesSet();

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/debug.htm");
    interceptor.preHandle(request, new MockHttpServletResponse(), null);
  }

}
