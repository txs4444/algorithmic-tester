package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AlgorithmicTesterExtension.class)
class TestDataFileLoaderTest {

    @Test
    public void shouldLoadInputData(@InputData(file = "extension/inputExtensionTestContent")InputStream inputStream) throws IOException {
        // given
        Path path = Paths.get(getClass().getClassLoader().getResource("extension/inputExtensionTestContent").getPath());
        BufferedReader expectedContentReader = Files.newBufferedReader(path);
        String expectedContent = expectedContentReader.lines().collect(Collectors.joining("\n"));

        // when
        BufferedReader providedContentReader = new BufferedReader(new InputStreamReader(inputStream));
        String providedContent = providedContentReader.lines().collect(Collectors.joining("\n"));

        // then
        assertThat(providedContent)
                .isEqualTo(expectedContent);
    }

    @Test
    public void shouldLoadExpectedOutputDataAndCompareItWithContentWrittenToInputStream(
            @OutputData(file = "extension/outputExtensionTestContent") OutputStream outputStream
    ) throws IOException {
        // given

        // when
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write("output test content");
        writer.newLine();
        writer.write("any text");
        writer.newLine();
        writer.write("in few lines");
        writer.flush();

        // then
    }

    @Disabled
    @Test
    public void shouldFailWhenExpectedResultDontMatchOutputStreamContent(
            @OutputData(file = "extension/outputExtensionTestContent") OutputStream outputStream
    ) throws IOException {
        // given

        // when
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write("not expected");
        writer.flush();

        // then
        // No way to test it since there is no way to catch exception thrown by AlgorithmicTesterExtension
        // TestExecutionExceptionHandler - Allow to handle exception thrown immediately by test method
        // LifecycleMethodExecutionExceptionHandler - Only for methods e.g. method annotated with @AfterEach, but not capable of handle exception
        // thrown by AfterEachCallback
    }

}