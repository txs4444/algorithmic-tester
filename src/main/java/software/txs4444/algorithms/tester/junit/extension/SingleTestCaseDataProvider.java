package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

class SingleTestCaseDataProvider implements TestCaseDataProvider {
    @Override
    public InputStream inputData(ParameterContext parameterContext) {
        Optional<InputData> annotation = parameterContext.findAnnotation(InputData.class);
        String executableName = parameterContext.getDeclaringExecutable().getName();
        InputData inputData = annotation.orElseThrow(() -> noAnnotationOnParamException(executableName, InputData.class));
        String filePath = inputData.file();
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    @Override
    public InputStream expectedOutputData(ParameterContext parameterContext) {
        Optional<OutputData> annotation = parameterContext.findAnnotation(OutputData.class);
        String executableName = parameterContext.getDeclaringExecutable().getName();
        OutputData outputData = annotation.orElseThrow(() -> noAnnotationOnParamException(executableName, OutputData.class));
        String filePath = outputData.file();
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    @Override
    public boolean isInputDataParameter(ParameterContext parameterContext) {
        return parameterContext.isAnnotated(InputData.class)
                && parameterContext.getParameter().getType().isAssignableFrom(InputStream.class);
    }

    @Override
    public boolean isExpectedOutputDataParameter(ParameterContext parameterContext) {
        return parameterContext.isAnnotated(OutputData.class)
                && parameterContext.getParameter().getType().isAssignableFrom(OutputStream.class);
    }

    private ParameterResolutionException noAnnotationOnParamException(String methodName, Class<?> expectedAnnotation) {
        String errorMsg = String.format(
                "Could not find annotation %s on method %s - it suggest wrong usage of %s, as check's should be used priory to parametere resolution",
                expectedAnnotation.getSimpleName(),
                methodName,
                this.getClass().getSimpleName()
            );
        return new ParameterResolutionException("");
    }
}
