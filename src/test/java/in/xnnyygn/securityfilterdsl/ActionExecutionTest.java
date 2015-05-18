package in.xnnyygn.securityfilterdsl;

import static org.junit.Assert.assertTrue;
import in.xnnyygn.securityfilterdsl.action.ShowErrorAction;
import in.xnnyygn.securityfilterdsl.context.ActionExecutionContext;
import in.xnnyygn.securityfilterdsl.context.ActionReferenceContext;
import in.xnnyygn.securityfilterdsl.core.AbstractAction;
import in.xnnyygn.securityfilterdsl.core.PassAction;
import in.xnnyygn.securityfilterdsl.parser.ActionConfigParser;

import java.io.FileInputStream;
import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for action execution.
 * 
 * @author xnnyygn
 */
public class ActionExecutionTest {

  private static final String ACTION_CONFIG_FOLDER = "src/test/resources/action-config/";
  private ActionConfigParser configParser;
  private Mockery mockContext = null;
  private ActionExecutionContext mockExecutionContext = null;

  @Before
  public void setUp() {
    configParser = new ActionConfigParser();
    mockContext = new JUnit4Mockery();
    mockExecutionContext = mockContext.mock(ActionExecutionContext.class);
  }

  @Test
  public void testPass() throws IOException {
    ActionReferenceContext referenceContext =
        configParser.parse(new FileInputStream(ACTION_CONFIG_FOLDER + "/rule-pass.txt"));
    assertTrue(referenceContext.getRoot() instanceof PassAction);
  }

  @Test
  public void testShowError() throws IOException {
    ActionReferenceContext referenceContext =
        configParser.parse(new FileInputStream(ACTION_CONFIG_FOLDER + "/rule-show-error.txt"));
    AbstractAction action = referenceContext.getRoot();
    assertTrue(action instanceof ShowErrorAction);

    mockContext.checking(new Expectations() {
      {
        oneOf(mockExecutionContext).showError("fooErrorLink", "SOME_ERROR_CODE");
      }
    });
    action.execute(mockExecutionContext);
  }

  @Test
  public void testConditionPatternLiteral() throws IOException {
    ActionReferenceContext referenceContext =
        configParser.parse(new FileInputStream(ACTION_CONFIG_FOLDER
            + "/rule-condition-patterns.txt"));
    AbstractAction action = referenceContext.get("PATTERN_LITERAL");

    mockContext.checking(new Expectations() {
      {
        oneOf(mockExecutionContext).getAttribute("X");
        will(returnValue("2"));
        oneOf(mockExecutionContext).setRuntimeVariable(with(any(String.class)),
            with(any(Object.class)));
        oneOf(mockExecutionContext).debug("X_2");
      }
    });
    executeActions(action);
  }

  private void executeActions(AbstractAction action) {
    while ((action = action.execute(mockExecutionContext)) != null);
  }

  @Test
  public void testConditionPatternRegex() throws IOException {
    ActionReferenceContext referenceContext =
        configParser.parse(new FileInputStream(ACTION_CONFIG_FOLDER
            + "/rule-condition-patterns.txt"));
    AbstractAction action = referenceContext.get("PATTERN_REGEX");

    mockContext.checking(new Expectations() {
      {
        oneOf(mockExecutionContext).getAttribute("Y");
        will(returnValue("111"));
        oneOf(mockExecutionContext).setRuntimeVariable(with(any(String.class)),
            with(any(Object.class)));
        oneOf(mockExecutionContext).debug(with("NUM_N"));
      }
    });
    executeActions(action);
  }

  @Test
  public void testConditionPatternVariable() throws IOException {
    final ActionReferenceContext referenceContext =
        configParser.parse(new FileInputStream(ACTION_CONFIG_FOLDER
            + "/rule-condition-patterns.txt"));
    AbstractAction action = referenceContext.get("PATTERN_VARIABLE");

    mockContext.checking(new Expectations() {
      {
        oneOf(mockExecutionContext).getAttribute("Z");
        will(returnValue("4"));
        oneOf(mockExecutionContext).getAttribute("Z1");
        will(returnValue(referenceContext.getAttribute("Z1")));
        oneOf(mockExecutionContext).setRuntimeVariable(with(any(String.class)),
            with(any(Object.class)));
        oneOf(mockExecutionContext).debug(with("Z_1"));
      }
    });
    executeActions(action);
  }

  @Test
  public void testRequestURICondition() throws IOException {
    final ActionReferenceContext referenceContext =
        configParser.parse(new FileInputStream(ACTION_CONFIG_FOLDER + "/rule-request-url.txt"));
    AbstractAction action = referenceContext.get("MATCH_URI");

    mockContext.checking(new Expectations() {
      {
        oneOf(mockExecutionContext).getRequestURI();
        will(returnValue("/error.htm"));
        oneOf(mockExecutionContext).setRuntimeVariable(with(any(String.class)),
            with(any(Object.class)));
        oneOf(mockExecutionContext).debug(with("URI_3"));
      }
    });
    executeActions(action);
  }

  @Test
  public void testRequestURIEndCondition() throws IOException {
    final ActionReferenceContext referenceContext =
        configParser.parse(new FileInputStream(ACTION_CONFIG_FOLDER + "/rule-request-url.txt"));
    AbstractAction action = referenceContext.get("MATCH_URI_END");

    mockContext.checking(new Expectations() {
      {
        oneOf(mockExecutionContext).getRequestURI();
        will(returnValue("/bar.htm"));
        oneOf(mockExecutionContext).findLocation("fooLink");
        will(returnValue(null));
        oneOf(mockExecutionContext).getAttribute("fooLink");
        will(returnValue(null));
        oneOf(mockExecutionContext).findLocation("barLink");
        will(returnValue("http://foo.com/bar.htm"));
        oneOf(mockExecutionContext).debug(with("URI_END_2"));
        oneOf(mockExecutionContext).setRuntimeVariable(with(any(String.class)),
            with(any(Object.class)));
      }
    });
    executeActions(action);
  }

  @Test
  public void testRequestMethod() throws IOException {
    ActionReferenceContext context =
        new ActionConfigParser().parse(new FileInputStream(ACTION_CONFIG_FOLDER
            + "/rule-request-method.txt"));
    mockContext.checking(new Expectations() {
      {
        oneOf(mockExecutionContext).getRequestMethod();
        will(returnValue("POST"));
        oneOf(mockExecutionContext).debug("2");
        oneOf(mockExecutionContext).setRuntimeVariable(with(any(String.class)),
            with(any(Object.class)));
      }
    });
    context.getRoot().execute(mockExecutionContext);
  }

}
