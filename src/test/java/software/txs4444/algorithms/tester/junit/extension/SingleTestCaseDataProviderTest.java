package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SingleTestCaseDataProviderTest {
    private final SingleTestCaseDataProvider testedObject = new SingleTestCaseDataProvider();

    @Test
    @DisplayName("Should load input stream from file given by InputData annotation")
    public void shouldLoadInputStreamFromInputDataAnnotation() throws IOException, NoSuchMethodException {
        // given
        Method correctTestMethod = getCorrectTestMethod();
        Parameter inputParam = correctTestMethod.getParameters()[0];
        ParameterContext parameterContext = new TestParameterContext(inputParam);

        String inputResourceFile = "extension/inputExtensionTestContent";
        String expectedContent = readResourceFileContent(inputResourceFile);

        // when
        InputStream inputStream = testedObject.inputData(parameterContext);
        String content = read(inputStream);

        // then
        assertThat(content).isEqualTo(expectedContent);
    }

    @Test
    @DisplayName("Should recognize input data parameter when parameter is InputStream annotated with InputData ")
    public void shouldRecognizeInputDataParameter() throws NoSuchMethodException {
        // given
        Method correctTestMethod = getCorrectTestMethod();
        Parameter inputParam = correctTestMethod.getParameters()[0];
        ParameterContext parameterContext = new TestParameterContext(inputParam);

        // when
        boolean isInputDataParameter = testedObject.isInputDataParameter(parameterContext);

        // then
        assertThat(isInputDataParameter)
                .isTrue();
    }

    @Test
    @DisplayName("Should NOT recognize input data parameter when no annotation")
    public void shouldNotRecognizeInputDataParameterWhenNoAnnotation() throws NoSuchMethodException {
        // given
        Method incorrectTestMethod = getIncorrectTestMethodWithoutAnnotations();
        Parameter inputStreamWithoutAnnotation = incorrectTestMethod.getParameters()[0];
        TestParameterContext parameterContext = new TestParameterContext(inputStreamWithoutAnnotation);

        // when
        boolean isInputDataParameter = testedObject.isInputDataParameter(parameterContext);

        // then
        assertThat(isInputDataParameter)
                .isFalse();
    }

    @Test
    @DisplayName("Should NOT recognize input data parameter when it's type is not InputStream")
    public void shouldNotRecognizeInputDataParameterWhenWrongType() throws NoSuchMethodException {
        // given
        Method incorrectTestMethod = getIncorrectTestMethodWithWrongTypes();
        Parameter inputDataWithWrongType = incorrectTestMethod.getParameters()[0];
        TestParameterContext parameterContext = new TestParameterContext(inputDataWithWrongType);

        // when
        boolean isInputDataParameter = testedObject.isInputDataParameter(parameterContext);

        // then
        assertThat(isInputDataParameter)
                .isFalse();
    }

    @Test
    @DisplayName("Should load input stream from file given by OutputData annotation")
    public void shouldLoadInputStreamForOutputDataAnnotation() throws IOException, NoSuchMethodException {
        // given
        Method correctTestMethod = getCorrectTestMethod();
        Parameter outputParam = correctTestMethod.getParameters()[1];
        TestParameterContext parameterContext = new TestParameterContext(outputParam);

        String expectedOutputResourceFile = "extension/outputExtensionTestContent";
        String expectedContent = readResourceFileContent(expectedOutputResourceFile);

        // when
        InputStream expectedOutputData = testedObject.expectedOutputData(parameterContext);
        String content = read(expectedOutputData);

        // then
        assertThat(content).isEqualTo(expectedContent);
    }

    @Test
    @DisplayName("Should recognize output data parameter when parameter is OutputStream annotated with OutputData ")
    public void shouldRecognizeOutputDataParameter() throws NoSuchMethodException {
        // given
        Method correctTestMethod = getCorrectTestMethod();
        Parameter outputParam = correctTestMethod.getParameters()[1];
        ParameterContext parameterContext = new TestParameterContext(outputParam);

        // when
        boolean isExpectedOutputDataParameter = testedObject.isExpectedOutputDataParameter(parameterContext);

        // then
        assertThat(isExpectedOutputDataParameter)
                .isTrue();
    }

    @Test
    @DisplayName("Should NOT recognize output data parameter when no annotation")
    public void shouldNotRecognizeOutputDataParameterWhenNoAnnotation() throws NoSuchMethodException {
        // given
        Method incorrectTestMethod = getIncorrectTestMethodWithoutAnnotations();
        Parameter outputStreamWithoutAnnotation = incorrectTestMethod.getParameters()[1];
        TestParameterContext parameterContext = new TestParameterContext(outputStreamWithoutAnnotation);

        // when
        boolean isExpectedOutputDataParameter = testedObject.isExpectedOutputDataParameter(parameterContext);

        // then
        assertThat(isExpectedOutputDataParameter)
                .isFalse();
    }

    @Test
    @DisplayName("Should NOT recognize output data parameter when it's type is not OutputStream")
    public void shouldNotRecognizeOutputDataParameterWhenWrongType() throws NoSuchMethodException {
        // given
        Method incorrectTestMethod = getIncorrectTestMethodWithWrongTypes();
        Parameter outputDataWithWrongType = incorrectTestMethod.getParameters()[1];
        TestParameterContext parameterContext = new TestParameterContext(outputDataWithWrongType);

        // when
        boolean isExpectedOutputDataParameter = testedObject.isExpectedOutputDataParameter(parameterContext);

        // then
        assertThat(isExpectedOutputDataParameter)
                .isFalse();
    }

    private String read(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String content = reader.lines().collect(Collectors.joining("\n"));
        return content;
    }

    private String readResourceFileContent(String inputResourceFile) throws IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(inputResourceFile).getPath());
        BufferedReader expectedContentReader = Files.newBufferedReader(path);
        String expectedContent = expectedContentReader.lines().collect(Collectors.joining("\n"));
        return expectedContent;
    }

    private Method getIncorrectTestMethodWithWrongTypes() throws NoSuchMethodException {
        return TestMethods.class.getDeclaredMethod("incorrectTestMethodWithWrongTypes", File.class, File.class);
    }

    private Method getIncorrectTestMethodWithoutAnnotations() throws NoSuchMethodException {
        return TestMethods.class.getDeclaredMethod("incorrectTestMethodWithoutAnnotations", InputStream.class, OutputStream.class);
    }

    private Method getCorrectTestMethod() throws NoSuchMethodException {
        return TestMethods.class.getDeclaredMethod("correctTestMethod", InputStream.class, OutputStream.class);
    }

    private static class TestMethods {

        void incorrectTestMethodWithoutAnnotations(InputStream inputStream, OutputStream outputStream) {
            // nothing to do, just to provide incorrect method signature to test
        }

        void incorrectTestMethodWithWrongTypes(@InputData(file = "any") File input, @OutputData(file = "any") File outputStream) {
            // nothing to do, just to provide incorrect method signature to test
        }

        void correctTestMethod(
                @InputData(file = "extension/inputExtensionTestContent") InputStream inputStream,
                @OutputData(file = "extension/outputExtensionTestContent") OutputStream outputStream
        ) {
            // noting to do, just to provide correct method signature to test
        }
    }
}
