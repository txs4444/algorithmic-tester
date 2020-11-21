package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.extension.ParameterContext;

import java.io.InputStream;
import java.util.Optional;

class SingleTestCaseDataProvider implements TestCaseDataProvider {
    @Override
    public InputStream inputData(ParameterContext parameterContext) {
        Optional<InputData> annotation = parameterContext.findAnnotation(InputData.class);
        InputData inputData = annotation.get();
        String filePath = inputData.file();
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    @Override
    public InputStream expectedOutputData(ParameterContext parameterContext) {
        Optional<OutputData> annotation = parameterContext.findAnnotation(OutputData.class);
        OutputData outputData = annotation.get();
        String filePath = outputData.file();
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    @Override
    public boolean isInputDataParameter(ParameterContext parameterContext) {
        return parameterContext.isAnnotated(InputData.class) && parameterContext.getParameter().getType().isAssignableFrom(InputStream.class);
    }

    @Override
    public boolean isExpectedOutputDataParameter(ParameterContext parameterContext) {
        return parameterContext.isAnnotated(OutputData.class);
    }
}
