package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;

public class AlgorithmicTesterExtensionTest {

    @Test
    @DisplayName("Should run two tests for input/output 01 nad 02 each test should pass")
    public void shouldRunTestForEachInputAndOutputDefinedAndPassEachTime() {
        // given

        // when
        EngineExecutionResults testExecutionResult = execute(PositiveTestCase.class);

        // then
        testExecutionResult.testEvents()
                .assertStatistics(stats -> stats.started(2).succeeded(2));
    }

    @Test
    @DisplayName("Should fail when parameters are not annotated with TestDataInput and TestDataOutput")
    public void shouldFailWhenParametersNotAnnotated() {
        // given

        // when
        EngineExecutionResults testExecutionResult = executeMethod(NegativeTestCase.class, "failWithoutParametersAnnotation");

        // then
        testExecutionResult.testEvents()
                .assertStatistics(stats -> stats.started(2).failed(2))
                .assertEventsMatchLoosely(
                        event(test(), displayName("01"), finishedWithFailure(instanceOf(ParameterResolutionException.class))),
                        event(test(), displayName("02"), finishedWithFailure(instanceOf(ParameterResolutionException.class)))
                );
    }

    @Test
    @DisplayName("Should not run any test when directed directory has no files")
    public void shouldNotRunAnyTestWhenNoFilesInDirectory() {
        // given

        // when
        EngineExecutionResults testExecutionResult = executeMethod(NegativeTestCase.class, "notExecuteWithNotMatchedTestCases");

        // then
        testExecutionResult.testEvents()
                .assertStatistics(stats -> stats.started(0));
    }

    @Test
    @DisplayName("Should not run any test when directed directory contains not matching input and output files")
    public void shouldNotRunAnyTestWhenInputAndOutputFilesNumbersDoesNotMatch() {
        // given

        // when
        EngineExecutionResults testExecutionResult = executeMethod(NegativeTestCase.class, "notExecuteWithEmptyTestcaseDirectory");

        // then
        testExecutionResult.testEvents()
                .assertStatistics(stats -> stats.started(0));
    }

    @Test
    @DisplayName("Should not run any test when directed directory does not exist")
    public void shouldNotRunAnyTestWhenDirectoryNotExist() {
        // given

        // when
        EngineExecutionResults testExecutionResult = executeMethod(NegativeTestCase.class, "notExecuteWithNotExistingDirectory");

        // then
        testExecutionResult.testEvents()
                .assertStatistics(stats -> stats.started(0));
    }

    @Test
    @DisplayName("Should run two tests 01 succeeds, 02 fails")
    public void shouldRunToTestsButOnly01Pass() {
        // given

        // when
        EngineExecutionResults testExecutionResult = executeMethod(NegativeTestCase.class, "firstPassesSecondFails");

        // then
        testExecutionResult.testEvents()
                .assertStatistics(stats -> stats.started(2).succeeded(1).failed(1))
                .assertEventsMatchLoosely(
                        event(test(), displayName("01"), finishedSuccessfully()),
                        event(test(), displayName("02"), finishedWithFailure())
                );
    }

    @Test
    @DisplayName("Should treat each of class method as separate test with its own directory containing testing data")
    public void shouldRunSupportMultipleTestMethodInOneClassEachWithDifferentDataSource() {
        // given

        // when
        EngineExecutionResults testExecutionResult = execute(MultipleTestCasesDirectoryInOneClass.class);

        // then
        testExecutionResult.testEvents()
                .assertStatistics(stats -> stats.started(5));
    }

    private EngineExecutionResults execute(Class<?> testClass) {
        return EngineTestKit.engine("junit-jupiter")
                .selectors(selectClass(testClass))
                .execute();
    }

    private EngineExecutionResults executeMethod(Class<?> testClass, String methodName) {
        Method testMethod = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow();
        return EngineTestKit.engine("junit-jupiter")
                .selectors(selectMethod(testClass, testMethod))
                .execute();
    }

    static class PositiveTestCase {
        @TestTemplate
        @ExtendWith(AlgorithmicTesterExtension.class)
        @AlgorithmTestCases(directory = "extension/firstSetTestCasesDir")
        void correctTestMethod(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {
            // given
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
            String inputContent = inputReader.lines().collect(Collectors.joining("\n"));
            String outputContent = inputContent.replace("input", "output");

            // when
            PrintWriter outputWriter = new PrintWriter(output);
            outputWriter.print(outputContent);
            outputWriter.flush();

            // then
            // Extensions should verify output stream's content against expected output defined in output file associated with given input
        }
    }

    static class NegativeTestCase {
        @TestTemplate
        @ExtendWith(AlgorithmicTesterExtension.class)
        @AlgorithmTestCases(directory = "extension/firstSetTestCasesDir")
        void firstPassesSecondFails(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {
            // given

            // when
            PrintWriter outputWriter = new PrintWriter(output);
            outputWriter.println("output content 01");
            outputWriter.println("for");
            outputWriter.print("a few lines");
            outputWriter.flush();

            // then
            // Extensions should verify output stream's content against expected output defined in output file associated with given input
        }

        @TestTemplate
        @ExtendWith(AlgorithmicTesterExtension.class)
        @AlgorithmTestCases(directory = "extension/firstSetTestCasesDir")
        void failWithoutParametersAnnotation(InputStream input, OutputStream output) {
        }

        @TestTemplate
        @ExtendWith(AlgorithmicTesterExtension.class)
        @AlgorithmTestCases(directory = "extension/emptyTestCasesDir")
        void notExecuteWithEmptyTestcaseDirectory(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {
        }

        @TestTemplate
        @ExtendWith(AlgorithmicTesterExtension.class)
        @AlgorithmTestCases(directory = "extension/notMatchedTestCasesDir")
        void notExecuteWithNotMatchedTestCases(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {
        }

        @TestTemplate
        @ExtendWith(AlgorithmicTesterExtension.class)
        @AlgorithmTestCases(directory = "extension/noteExistingTestCasesDir")
        void notExecuteWithNotExistingDirectory(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {
        }
    }

    @ExtendWith(AlgorithmicTesterExtension.class)
    static class MultipleTestCasesDirectoryInOneClass {
        @TestTemplate
        @AlgorithmTestCases(directory = "extension/firstSetTestCasesDir")
        void runTestAgainstTestCasesFromFirstDirectory(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {

        }

        @TestTemplate
        @AlgorithmTestCases(directory = "extension/secondSetTestCasesDir")
        void runTestAgainstTestCasesFromSecondDirectory(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {

        }
    }
}
