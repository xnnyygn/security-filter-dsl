# security filter dsl #

a security filter controlled by DSL

## Introduction ##

The main purpose of this DSL is to write a more human-readable and easy-maintainable security filter by DSL and to avoid nested conditions hell in code. It isn't a replacement of login filter as it does not provide any function for login. If your project requires the same complex logic check between some controllers, this is a suitable soluton for you. For others, you can learn something about DSL and feel free to use it or adapt it to your projects. :)

## Quick Start ##

security interceptor(filter)

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
    
action config(DSL)

	# DSL entrance
    LABEL ROOT
    MATCH REQUEST_URI
    CASE /debug.htm => CALL DEBUG hello

    # pass request to /foo.htm
    CASE ^/foo\.htm$ => CALL PASS

    CASE /some.htm => GOTO URI_SOME

    # redirect to barLink if any other request comes in
    DEFAULT CALL SHOW barLink

    ####################################

    LABEL URI_SOME
    MATCH REQUEST_METHOD
    CASE GET => CALL DEBUG some_get
    CASE POST => CALL DEBUG some_post
    DEFAULT CALL DEBUG some_other

log example

	DEBUG [main] condition.AbstractPatternMatchCondition - RequestUriCondition match value /debug.htm
	DEBUG [main] context.DefaultActionExecutionContext - debug hello


## Semantic Model ##

The model here is described in the form of Scala. If you want the pure Java model, please refer to the Java files under package `in.xnnyygn.securityfilterdsl.core`.

	sealed abstract class AbstractAction {
      def execute(context: ActionExecutionContext): AbstractAction
    }
     
    abstract class Condition extends AbstractAction
     
    abstract class Action extends AbstractAction {
      def execute(context: ActionExecutionContext) = {
        doExecute(context)
        null
      }
      def doExecute(context: ActionExecutionContext): Unit
    }

The abstract model of both `Action` and `Condition` is `AbstractAction`, which requires returning another `AbstractAction` instance. If the returned instance is `null`, the chain call will be terminated and it is actually what is done in `Action`. `Condition`, although it just inherits `AbstractAction`, evaluates and decides what the next `AbstractAction` would be and returns it.

## DSL Syntax ##

### Basic ###

1. Blank lines will be ignored
2. Lines beginning with `#` will be ignored, which means you can write comments just by starting lines with `#`
3. `LABEL ${labelName}` define `AbstractAction` with a name by adding label command before any AbstractAction DSL. You should not define two AbstractActions with the same name, the first one will be replaced.
4. `GOTO ${labelName}` jump to a specified AbstractAction by name
5. `CALL ${actionName} [${actionParameters}]` call some action with optional parameters, any action must be defined in parser and mapped to a subclass of `Action`
6. `SET ${variableName} ${variableValue}` define a context variable and set it to a specified value

The Syntax of `Condition`

    MATCH REQUEST_METHOD
    CASE GET => CALL PASS
    CASE POST => GOTO METHOD_POST
    DEFAULT GOTO OTHER

A condition must start by `MATCH`, the word following is the name of context variables. You can find defined condition variable such as `REQUEST_URI`, `REQUEST_METHOD` etc in parser. When encountering undefined condition variable, `AttributeCondition` will be created, which may be not what you want. Be careful of the spelling of the condition variable in DSL and parser.

The Simplest DSL

	LABEL ROOT
    CALL PASS
    
It just passes all requests. `ROOT` is the label name of entrance of the DSL and executed by the context firstly. `PASS` is the simplest action doing nothing and you can end any chain call by `PASS` action.

Three types of Patterns

1. Literal e.g `CASE /foo.htm`
2. Regular Expression e.g `CASE ^/foo\.htm$` this pattern must start with `^` and end with `$`
3. Variable with back-tick It's difficult to show it in markdown, but if you know something about Scala, wrapping a variable with back-tick could be the pattern in pattern match and the value of variable will be evaluated when matching.

### Actions ###

There are some basic actions in current codebase.

`PASS`

Mapped to `PassAction`, doing nothing, generally used at the end of DSL.

`SHOW ${locationCode} ${options} ${additionalParameters}`

Mapped to `ShowAction`. It requires a URL manager(`URLManager` in exeuction context) to determine the full URL by location code. At this time, the only option allowed is `ALLOW_POST`. The additional parameters written in query string form will be added to calculated URL when redirect request. See more information in source code of `ShowAction`.

`SHOW_ERROR ${errorLocationCode} ${errorCode}`

Mapped to `ShowErrorAction`. Compared with `ShowAction`, error code is the only parameter added to the target URL.

### Conditons ###

`REQUEST_URI`

Mapped to `HttpServletRequest#getRequestURI`

`REQUEST_METHOD`

Mapped to `HttpServletRequest#getRequestMethod`

`REQUEST_URI_END`

This is an interesting condition as it works in a very different way compared with normal conditons. The patterns will be calulated and check if current URI is at the end of the pattern, not checking the equality between two variables. See `RequestUriEndConditoin` for more information.

## Refreshable DSL ##

Another interesting part in this DSL is a refreshable reference context, in other word, you can modify your filter logic by changing DSL during runtime, which is a great benifit for complicated Java application.

Class `RefreshableActionReferenceContext` checks the rule file(DSL) in specified duration and if any change, by calculating the hash of file and comparing between current hash and previous hash, is made, the DSL will be replaced and new logic applied. 

## Others ##

If you are interested in the design part of this DSL, please click the link below, this is the introduction article on my blog. 

[http://blog.gssxgss.me/security-filter-dsl-1/](http://blog.gssxgss.me/security-filter-dsl-1/) (Written in Chinese)

