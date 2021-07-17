package software.txs4444.algorithms.tester.junit.extension.end2end.algorithmicTesterExtension;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import software.txs4444.algorithms.tester.junit.extension.AlgorithmTestCases;
import software.txs4444.algorithms.tester.junit.extension.AlgorithmicTesterExtension;
import software.txs4444.algorithms.tester.junit.extension.TestCaseInput;
import software.txs4444.algorithms.tester.junit.extension.TestCaseOutput;
import software.txs4444.algorithms.tester.junit.extension.end2end.BubbleSortAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@ExtendWith(AlgorithmicTesterExtension.class)
public class BubbleSortAlgorithmTest {
    private final BubbleSortAlgorithm testedObject = new BubbleSortAlgorithm();

    @TestTemplate
    @AlgorithmTestCases(directory = "end2end")
    public void shouldPassPredefinedTests(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) throws IOException {
        // given
        // Input data are loaded into inputStream from specified file

        // when
        testedObject.solve(input, output);

        // then
        // Extension is responsible for using your output and check it against content of the file specified by annotation
    }
}
