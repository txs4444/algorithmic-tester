package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.opentest4j.AssertionFailedError;

import java.io.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;

class SingleCaseAlgorithmicTesterExtensionTest {
    private static final String INPUT_FILE_CONTENT = "input test content\n" +
            "any text\n" +
            "in few lines";
    private static final String OUTPUT_FILE_CONTENT = "output test content\n" +
            "any text\n" +
            "in few lines";

    @Test
    @DisplayName("Should pass by providing correct input data and output data equal to output stream content")
    public void shouldRunSuccessfullyWellDefinedTestMethod() {
        // given

        // when
        EngineExecutionResults results = execute(PositiveTestCase.class);

        // then
        results.testEvents().assertStatistics(stats -> stats.started(1).succeeded(1));
    }

    @Test
    @DisplayName("Should fail with output stream content not matching output data which is verified by extension")
    public void shouldFailWhenWrongOutputStreamContentIsWritten() {
        // given

        // when
        EngineExecutionResults results = execute(NegativeTestCase.class);

        // then
        results.testEvents()
                .assertStatistics(stats -> stats.started(1).failed(1));
        results.testEvents()
                .assertThatEvents()
                .haveExactly(1, event(finishedWithFailure(instanceOf(AssertionFailedError.class))));
    }

    private EngineExecutionResults execute(Class<?> testClass) {
        return EngineTestKit.engine("junit-jupiter")
                .selectors(selectClass(testClass))
                .execute();
    }

    static class PositiveTestCase {
        @Test
        @ExtendWith(SingleCaseAlgorithmicTesterExtension.class)
        void correctTestMethod(
                @InputData(file = "extension/inputExtensionTestContent") InputStream inputStream,
                @OutputData(file = "extension/outputExtensionTestContent") OutputStream outputStream
        ) {
            // given
            // input data provider by extension
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String inputProvidedByExtension = reader.lines().collect(Collectors.joining("\n"));

            // when
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.print(OUTPUT_FILE_CONTENT);
            printWriter.flush();


            // then
            assertThat(inputProvidedByExtension)
                    .isEqualTo(INPUT_FILE_CONTENT);
        }
    }

    static class NegativeTestCase {
        @Test
        @ExtendWith(SingleCaseAlgorithmicTesterExtension.class)
        void incorrectOutputStreamContent(
                @OutputData(file = "extension/outputExtensionTestContent") OutputStream outputStream
        ) {
            // given

            // when
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println("Any content not matching file's content");
            printWriter.flush();

            // then
            // output stream is verified against OutputData's file content by extension
        }
    }
}