package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleCaseAlgorithmicTesterExtension implements ParameterResolver, AfterTestExecutionCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleCaseAlgorithmicTesterExtension.class);
    private static final ExtensionContext.Namespace TEST_DATA_FILE_LOADER_NAMESPACE = ExtensionContext.Namespace.create(SingleCaseAlgorithmicTesterExtension.class);

    private final TestCaseDataProvider testCaseDataProvider;

    SingleCaseAlgorithmicTesterExtension() {
        this.testCaseDataProvider = new SingleTestCaseDataProvider();
    }

    SingleCaseAlgorithmicTesterExtension(TestCaseDataProvider testCaseDataProvider) {
        this.testCaseDataProvider = testCaseDataProvider;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (extensionContext.getTestMethod().isPresent()) {
            boolean isRecognizedParameter = testCaseDataProvider.isInputDataParameter(parameterContext)
                    || testCaseDataProvider.isExpectedOutputDataParameter(parameterContext);
            if (!isRecognizedParameter) {
                LOGGER.warn(
                        "Parameter {} of method {} is neither input nor output parameter",
                        parameterContext.getParameter().getName(),
                        extensionContext.getTestMethod().get().toGenericString()
                );
            }
            return isRecognizedParameter;
        }
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (testCaseDataProvider.isInputDataParameter(parameterContext)) {
            InputStream inputDataStream = testCaseDataProvider.inputData(parameterContext);
            getThisExtensionsStore(extensionContext).put(Resource.INPUT_DATA_STREAM, inputDataStream);
            return inputDataStream;
        }
        if (testCaseDataProvider.isExpectedOutputDataParameter(parameterContext)) {
            InputStream outputStreamData = testCaseDataProvider.expectedOutputData(parameterContext);
            getThisExtensionsStore(extensionContext).put(Resource.OUTPUT_DATA_STREAM, outputStreamData);
            ByteArrayOutputStream containerForAlgorithmsOutput = new ByteArrayOutputStream();
            getThisExtensionsStore(extensionContext).put(Resource.ALGORITHMS_OUTPUT_RESULT_STREAM, containerForAlgorithmsOutput);
            return containerForAlgorithmsOutput;
        }
        LOGGER.error("Parameter {} could not be resolved", parameterContext.getParameter().getName());
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

    private static ExtensionContext.Store getThisExtensionsStore(ExtensionContext context) {
        return context.getStore(TEST_DATA_FILE_LOADER_NAMESPACE);
    }

    private enum Resource {INPUT_DATA_STREAM, ALGORITHMS_OUTPUT_RESULT_STREAM, OUTPUT_DATA_STREAM}
}
