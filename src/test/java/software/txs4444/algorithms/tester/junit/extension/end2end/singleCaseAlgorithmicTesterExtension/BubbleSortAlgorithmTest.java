package software.txs4444.algorithms.tester.junit.extension.end2end.singleCaseAlgorithmicTesterExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.txs4444.algorithms.tester.junit.extension.InputData;
import software.txs4444.algorithms.tester.junit.extension.OutputData;
import software.txs4444.algorithms.tester.junit.extension.SingleCaseAlgorithmicTesterExtension;
import software.txs4444.algorithms.tester.junit.extension.end2end.BubbleSortAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@ExtendWith(SingleCaseAlgorithmicTesterExtension.class)
public class BubbleSortAlgorithmTest {
    private final BubbleSortAlgorithm testedObject = new BubbleSortAlgorithm();


    @Test
    public void testDataSet01(
            @InputData(file = "end2end/input01") InputStream inputStream,
            @OutputData(file = "end2end/output01") OutputStream outputStream
    ) throws IOException {
        // given
        // Input data are loaded into inputStream from specified file

        // when
        testedObject.solve(inputStream, outputStream);

        // then
        // Extension is responsible for using your output and check it against content of the file specified by annotation
    }

    @Test
    public void testDataSet02(
            @InputData(file = "end2end/input02") InputStream inputStream,
            @OutputData(file = "end2end/output02") OutputStream outputStream
    ) throws IOException {
        // given
        // Input data are loaded into inputStream from specified file

        // when
        testedObject.solve(inputStream, outputStream);

        // then
        // Extension is responsible for using your output and check it against content of the file specified by annotation
    }

}
