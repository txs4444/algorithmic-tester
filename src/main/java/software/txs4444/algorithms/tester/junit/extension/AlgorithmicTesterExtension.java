package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgorithmicTesterExtension implements ParameterResolver, AfterTestExecutionCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmicTesterExtension.class);
    private static final ExtensionContext.Namespace TEST_DATA_FILE_LOADER_NAMESPACE = ExtensionContext.Namespace.create(AlgorithmicTesterExtension.class);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(InputData.class) || parameterContext.isAnnotated(OutputData.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext.isAnnotated(InputData.class)) {
            InputStream inputDataStream = getInputStreamOfInputData(parameterContext);
            getThisExtensionsStore(extensionContext).put(Resource.INPUT_DATA_STREAM, inputDataStream);
            return inputDataStream;
        }
        if (parameterContext.isAnnotated(OutputData.class)) {
            InputStream outputStreamData = getInputStreamOfOutputData(parameterContext);
            getThisExtensionsStore(extensionContext).put(Resource.OUTPUT_DATA_STREAM, outputStreamData);
            ByteArrayOutputStream containerForAlgorithmsOutput = new ByteArrayOutputStream();
            getThisExtensionsStore(extensionContext).put(Resource.ALGORITHMS_OUTPUT_RESULT_STREAM, containerForAlgorithmsOutput);
            return containerForAlgorithmsOutput;
        }
        return null;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (isOutputStreamProvided(context)) {
            ByteArrayOutputStream algorithmsOutputResultStream = (ByteArrayOutputStream) getThisExtensionsStore(context).get(Resource.ALGORITHMS_OUTPUT_RESULT_STREAM);
            String expectedAlgorithmsResult = getExpectedAlgorithmsResult(context);
            assertThat(algorithmsOutputResultStream.toString()).isEqualTo(expectedAlgorithmsResult);
        }
        closeResources(context);
    }

    private boolean isOutputStreamProvided(ExtensionContext context) {
        return getThisExtensionsStore(context).get(Resource.OUTPUT_DATA_STREAM) != null;
    }

    private void closeResources(ExtensionContext context) {
        for(Resource resourceType : Resource.values()) {
            Closeable resourceToClose = getThisExtensionsStore(context).get(resourceType, Closeable.class);
            if (resourceToClose != null) {
                try {
                    resourceToClose.close();
                } catch (IOException e) {
                    LOGGER.warn("Could not close resource {}", resourceType, e);
                }
            }
        }
    }

    private String getExpectedAlgorithmsResult(ExtensionContext context) {
        InputStream expectedOutputDataStream = (InputStream) getThisExtensionsStore(context).get(Resource.OUTPUT_DATA_STREAM);
        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(expectedOutputDataStream));
        return inputStreamReader.lines().collect(Collectors.joining("\n"));
    }

    private InputStream getInputStreamOfOutputData(ParameterContext parameterContext) {
        Optional<OutputData> annotation = parameterContext.findAnnotation(OutputData.class);
        OutputData outputData = annotation.get();
        String filePath = outputData.file();
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    private InputStream getInputStreamOfInputData(ParameterContext parameterContext) {
        Optional<InputData> annotation = parameterContext.findAnnotation(InputData.class);
        InputData inputData = annotation.get();
        String filePath = inputData.file();
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    private static ExtensionContext.Store getThisExtensionsStore(ExtensionContext context) {
        return context.getStore(TEST_DATA_FILE_LOADER_NAMESPACE);
    }

    private enum Resource {INPUT_DATA_STREAM, ALGORITHMS_OUTPUT_RESULT_STREAM, OUTPUT_DATA_STREAM}
}
