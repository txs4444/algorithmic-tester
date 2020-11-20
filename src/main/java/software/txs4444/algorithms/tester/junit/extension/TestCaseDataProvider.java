package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.extension.ParameterContext;

import java.io.InputStream;

public interface TestCaseDataProvider {
    InputStream inputData(ParameterContext parameterContext);
    InputStream expectedOutputData(ParameterContext parameterContext);

    boolean isInputDataParameter(ParameterContext parameterContext);

    boolean isExpectedOutputDataParameter(ParameterContext parameterContext);
}
