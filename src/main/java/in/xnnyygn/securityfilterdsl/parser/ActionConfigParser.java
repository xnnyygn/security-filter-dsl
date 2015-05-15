package in.xnnyygn.securityfilterdsl.parser;

import in.xnnyygn.securityfilterdsl.action.DebugAction;
import in.xnnyygn.securityfilterdsl.action.ShowAction;
import in.xnnyygn.securityfilterdsl.action.ShowErrorAction;
import in.xnnyygn.securityfilterdsl.condition.RequestMethodCondition;
import in.xnnyygn.securityfilterdsl.condition.RequestURIEndCondition;
import in.xnnyygn.securityfilterdsl.condition.RequestUriCondition;
import in.xnnyygn.securityfilterdsl.context.ActionReferenceContext;
import in.xnnyygn.securityfilterdsl.context.DefaultActionReferenceContext;
import in.xnnyygn.securityfilterdsl.core.AbstractAction;
import in.xnnyygn.securityfilterdsl.core.AttributeCondition;
import in.xnnyygn.securityfilterdsl.core.Condition;
import in.xnnyygn.securityfilterdsl.core.ConditionPattern;
import in.xnnyygn.securityfilterdsl.core.NamedAction;
import in.xnnyygn.securityfilterdsl.core.PassAction;
import in.xnnyygn.securityfilterdsl.pattern.LiteralPattern;
import in.xnnyygn.securityfilterdsl.pattern.RegexPattern;
import in.xnnyygn.securityfilterdsl.pattern.VariablePattern;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Parser for action configuration.
 * 
 * @author xnnyygn
 */
public class ActionConfigParser {

    static class Context {

        private Condition lastCondition;
        private String    labelName;

        public Condition getLastCondition() {
            if (lastCondition == null) {
                throw new IllegalStateException("condition required before branch and fallback");
            }
            return lastCondition;
        }

        public void setLastCondition(Condition lastCondition) {
            this.lastCondition = lastCondition;
        }

        public boolean hasLabelName() {
            return labelName != null;
        }

        public String removeLabelName() {
            String labelName = new String(this.labelName);
            this.labelName = null;
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

    }

    /**
     * Parser config.
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public ActionReferenceContext parse(InputStream in) throws IOException {
        return parse(new InputStreamReader(in));
    }

    public ActionReferenceContext parse(Reader reader) throws IOException {
        ActionReferenceContext referenceContext = new DefaultActionReferenceContext();
        Context parserContext = new Context();
        for (Object lineObj : IOUtils.readLines(reader)) {
            String line = (String) lineObj;
            // blank or comment line
            if (StringUtils.isBlank(line) || line.startsWith("#")) {
                continue;
            }
            parseLine(line, parserContext, referenceContext);
        }
        return referenceContext;
    }

    private void parseLine(String line, Context parserContext,
                           ActionReferenceContext referenceContext) {
        String[] actionNotations = line.split("=>");
        int notationCount = actionNotations.length;

        if (notationCount > 2 || notationCount == 0) {
            throw new IllegalStateException("unexpected action notations count, " + line);
        }

        if (notationCount == 2) {
            // branch - action
            ConditionPattern pattern = parseConditionPattern(actionNotations[0]);
            AbstractAction action = buildAction(actionNotations[1], referenceContext);
            parserContext.getLastCondition().addBranchAction(pattern, action);
            return;
        }

        // notation count == 1
        List<String> pieces = splitAndTrim(line);
        if (pieces.isEmpty()) {
            throw new IllegalStateException("action type required, " + line);
        }

        String actionType = pieces.get(0);
        if ("MATCH".equals(actionType)) {
            // condition
            Condition condition = (Condition) buildAction(pieces, referenceContext);
            parserContext.setLastCondition(condition);
            if (parserContext.hasLabelName()) {
                referenceContext.addAction(parserContext.removeLabelName(), condition);
            }
        } else if ("DEFAULT".equals(actionType)) {
            // fallback action
            parserContext.getLastCondition().setFallbackAction(
                buildAction(pieces.subList(1, pieces.size()), referenceContext));
        } else if ("CALL".equals(actionType) && parserContext.hasLabelName()) {
            // action
            referenceContext.addAction(parserContext.removeLabelName(),
                buildAction(pieces, referenceContext));
        } else if ("LABEL".equals(actionType)) {
            // label
            parserContext.setLabelName(pieces.get(1));
        } else if ("SET".equals(actionType)) {
            // set attribute
            referenceContext.setAttribute(pieces.get(1), pieces.get(2));
        } else {
            throw new IllegalStateException("unexpected action type in line, " + actionType);
        }
    }

    private AbstractAction buildAction(String actionNotation, ActionReferenceContext context) {
        return buildAction(splitAndTrim(actionNotation), context);
    }

    private List<String> splitAndTrim(String notation) {
        List<String> pieces = new ArrayList<String>();
        for (String item : notation.split("[,\\s]")) {
            if (item.length() > 0) {
                pieces.add(item);
            }
        }
        return pieces;
    }

    private AbstractAction buildAction(List<String> pieces, ActionReferenceContext referenceContext) {
        if (pieces.size() < 2) {
            throw new IllegalStateException("action notation must has 2 parts at least");
        }

        String actionType = pieces.get(0);
        if ("CALL".equals(actionType)) {
            // action
            // syntax: action_type, action_name, value
            return doBuildAction(pieces);
        }

        if ("MATCH".equals(actionType)) {
            // condition
            // syntax: action_type, action_name
            return buildCondition(pieces.get(1), pieces.subList(2, pieces.size()));
        }

        if ("GOTO".equals(actionType)) {
            // syntax: action_type, name
            return new NamedAction(referenceContext, pieces.get(1));
        }

        throw new IllegalArgumentException("unknown action type " + actionType);
    }

    protected AbstractAction doBuildAction(List<String> pieces) {
        String actionName = pieces.get(1);
        if ("SHOW".equals(actionName))
            return ShowAction.create(pieces.subList(2, pieces.size()));
        if ("SHOW_ERROR".equals(actionName))
            return new ShowErrorAction(pieces.get(2), pieces.get(3));
        if ("PASS".equals(actionName))
            return new PassAction();
        if ("DEBUG".equals(actionName))
            return new DebugAction(pieces.get(2));

        throw new IllegalArgumentException("unknown action name " + actionName);
    }

    private Condition buildCondition(String conditionName, List<String> arguments) {
        if ("REQUEST_URI".equals(conditionName))
            return new RequestUriCondition();
        if ("REQUEST_URI_END".equals(conditionName))
            return new RequestURIEndCondition();
        if ("REQUEST_METHOD".equals(conditionName))
            return new RequestMethodCondition();

        return new AttributeCondition(conditionName);
    }

    private ConditionPattern parseConditionPattern(String actionNotation) {
        String[] pieces = actionNotation.split("\\s");
        if (pieces.length != 2) {
            throw new IllegalStateException("unexpected condition key format, " + actionNotation);
        }
        if (!"CASE".equals(pieces[0])) {
            throw new IllegalStateException("condition branch must starts with CASE "
                                            + actionNotation);
        }
        String pattern = pieces[1];
        if (pattern.startsWith("^") && pattern.endsWith("$")) {
            return new RegexPattern(pattern);
        }
        if (pattern.startsWith("`") && pattern.endsWith("`") && pattern.length() > 2) {
            return new VariablePattern(pattern.substring(1, pattern.length() - 1));
        }
        return new LiteralPattern(pattern);
    }

}
