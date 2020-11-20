package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgorithmicTesterExtensionTest {
    private static final Pattern CONTENT_NUMBER_PATTER = Pattern.compile("0\\d");

    @TestTemplate
    @ExtendWith(AlgorithmicTesterExtension.class)
    @AlgorithmTestCases(directory = "extension/multipleTestCases")
    public void shouldRunMethodTwoTimesForTwoDifferentInputFiles(@TestCaseInput InputStream inputStream, @TestCaseOutput OutputStream outputStream) throws IOException {
        // given
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String inputContent = reader.lines().collect(Collectors.joining("\n"));
        Matcher matcher = CONTENT_NUMBER_PATTER.matcher(inputContent);
        matcher.find();
        String contentNumber = matcher.group();

        // when
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write("output " + contentNumber);
        writer.newLine();
        writer.write("for a");
        writer.newLine();
        writer.write("few lines");
        writer.flush();

        // then
        String expectedInputContent = new StringBuilder()
                .append("input content ").append(contentNumber).append("\n")
                .append("for\n")
                .append("a few lines")
                .toString();
        assertThat(inputContent).isEqualTo(expectedInputContent);
        // Extension does one more assertion against outputStream content
    }

}
