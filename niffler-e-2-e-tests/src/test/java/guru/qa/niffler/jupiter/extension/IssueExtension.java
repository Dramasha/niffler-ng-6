package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledBuIssue;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

public class IssueExtension implements ExecutionCondition {

    private final GhApiClient ghApiClient = new GhApiClient();

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                DisabledBuIssue.class
        ).or(() -> AnnotationSupport.findAnnotation(
                        context.getRequiredTestClass(),
                        DisabledBuIssue.class,
                        SearchOption.INCLUDE_ENCLOSING_CLASSES
                )
        ).map(
                byIssue -> "open".equals(ghApiClient.getIssueState(byIssue.value()))
                        ? ConditionEvaluationResult.disabled("Disabled by issue " + byIssue.value())
                        : ConditionEvaluationResult.enabled("Issue closed")
        ).orElseGet(
                () -> ConditionEvaluationResult.enabled("Annotation @DisabledBuIssue not found")
        );
    }
}
