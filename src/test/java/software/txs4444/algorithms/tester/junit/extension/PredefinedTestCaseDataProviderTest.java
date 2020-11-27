package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PredefinedTestCaseDataProviderTest {
    public static final TestCase ANY_TEST_CASE = TestCase.of(null, null, null);
    private final PredefinedTestCaseDataProvider testedObject = PredefinedTestCaseDataProvider.of(ANY_TEST_CASE);

    @Test
    @DisplayName("Should recognize input data parameter when parameter is InputStream annotated with TestCaseInput")
    public void shouldRecognizeInputParameter() throws NoSuchMethodException {
        // given
        Method correctMethod = getCorrectlyDefinedTestMethod();
        Parameter inputParameter = correctMethod.getParameters()[0];
        ParameterContext parameterContext = new TestParameterContext(inputParameter);

        // when
        boolean isInputParameter = testedObject.isInputDataParameter(parameterContext);

        // then
        assertTrue(isInputParameter);
    }

    @Test
    @DisplayName("Should recognize output data parameter when parameter is OutputStream annotated with TestCaseOutput")
    public void shouldRecognizeOutputParameter() throws NoSuchMethodException {
        // given
        Method correctMethod = getCorrectlyDefinedTestMethod();
        Parameter outputParameter = correctMethod.getParameters()[1];
        ParameterContext parameterContext = new TestParameterContext(outputParameter);

        // when
        boolean isOutputParameter = testedObject.isExpectedOutputDataParameter(parameterContext);

        // then
        assertTrue(isOutputParameter);
    }

    @Test
    @DisplayName("Should NOT recognize input data parameter when parameter is not annotated with TestCaseInput")
    public void shouldNotRecognizeInputParameterWhenNoAnnotation() throws NoSuchMethodException {
        // given
        Method correctMethod = getIncorrectlyDefinedTestMethodWithoutAnnotations();
        Parameter inputParameter = correctMethod.getParameters()[0];
        ParameterContext parameterContext = new TestParameterContext(inputParameter);

        // when
        boolean isInputParameter = testedObject.isInputDataParameter(parameterContext);

        // then
        assertFalse(isInputParameter);
    }

    @Test
    @DisplayName("Should NOT recognize output data parameter when parameter is not annotated with TestCaseOutput")
    public void shouldNotRecognizeOutputParameterWhenNoAnnotation() throws NoSuchMethodException {
        // given
        Method correctMethod = getIncorrectlyDefinedTestMethodWithoutAnnotations();
        Parameter outputParameter = correctMethod.getParameters()[1];
        ParameterContext parameterContext = new TestParameterContext(outputParameter);

        // when
        boolean isOutputParameter = testedObject.isExpectedOutputDataParameter(parameterContext);

        // then
        assertFalse(isOutputParameter);
    }

    @Test
    @DisplayName("Should NOT recognize input data parameter when parameter is not InputStream")
    public void shouldNotRecognizeInputParameterWhenWrongType() throws NoSuchMethodException {
        // given
        Method correctMethod = getIncorrectlyDefinedTestMethodWithWrongTypes();
        Parameter inputParameter = correctMethod.getParameters()[0];
        ParameterContext parameterContext = new TestParameterContext(inputParameter);

        // when
        boolean isInputParameter = testedObject.isInputDataParameter(parameterContext);

        // then
        assertFalse(isInputParameter);
    }

    @Test
    @DisplayName("Should NOT recognize output data parameter when parameter is not OutputStream")
    public void shouldNotRecognizeOutputParameterWhenWrongType() throws NoSuchMethodException {
        // given
        Method correctMethod = getIncorrectlyDefinedTestMethodWithWrongTypes();
        Parameter outputParameter = correctMethod.getParameters()[1];
        ParameterContext parameterContext = new TestParameterContext(outputParameter);

        // when
        boolean isOutputParameter = testedObject.isExpectedOutputDataParameter(parameterContext);

        // then
        assertFalse(isOutputParameter);
    }

    private Method getCorrectlyDefinedTestMethod() throws NoSuchMethodException {
        return this.getClass().getDeclaredMethod("correctlyDefinedTestMethod", InputStream.class, OutputStream.class);
    }

    private void correctlyDefinedTestMethod(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {

    }

    private Method getIncorrectlyDefinedTestMethodWithoutAnnotations() throws NoSuchMethodException {
        return this.getClass().getDeclaredMethod("incorrectlyDefinedTestMethodWithoutAnnotation", InputStream.class, OutputStream.class);
    }

    private void incorrectlyDefinedTestMethodWithoutAnnotation(InputStream input, OutputStream output) {

    }

    private Method getIncorrectlyDefinedTestMethodWithWrongTypes() throws NoSuchMethodException {
        return this.getClass().getDeclaredMethod("incorrectlyDefinedTestMethodWithWrongTypes", Integer.class, Integer.class);
    }

    private void incorrectlyDefinedTestMethodWithWrongTypes(@TestCaseInput Integer input, @TestCaseOutput Integer output) {

    }
}