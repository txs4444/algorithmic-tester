package software.txs4444.algorithms.tester.junit.extension;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.extension.ParameterContext;

import java.io.InputStream;
import java.io.OutputStream;

@AllArgsConstructor(staticName = "of")
class PredefinedTestCaseDataProvider implements TestCaseDataProvider {
    private final TestCase testCaseData;

    @Override
    public InputStream inputData(ParameterContext parameterContext) {
        return testCaseData.getInput();
    }

    @Override
    public InputStream expectedOutputData(ParameterContext parameterContext) {
        return testCaseData.getExpectedOutput();
    }

    @Override
    public boolean isInputDataParameter(ParameterContext parameterContext) {
        return parameterContext.isAnnotated(TestCaseInput.class)
                && parameterContext.getParameter().getType().isAssignableFrom(InputStream.class);
    }

    @Override
    public boolean isExpectedOutputDataParameter(ParameterContext parameterContext) {
        return parameterContext.isAnnotated(TestCaseOutput.class)
                && parameterContext.getParameter().getType().isAssignableFrom(OutputStream.class);
    }
}
