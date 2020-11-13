package software.txs4444.algorithms.tester;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.txs4444.algorithms.tester.junit.extension.InputData;
import software.txs4444.algorithms.tester.junit.extension.OutputData;
import software.txs4444.algorithms.tester.junit.extension.AlgorithmicTesterExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@ExtendWith(AlgorithmicTesterExtension.class)
public class BubbleSortAlgorithmTest {
    private final BubbleSortAlgorithm testedObject = new BubbleSortAlgorithm();


    @Test
    public void testDataSet01(
            @InputData(file = "sortingAlgorithm/input01") InputStream inputStream,
            @OutputData(file = "sortingAlgorithm/output01") OutputStream outputStream
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
            @InputData(file = "sortingAlgorithm/input02") InputStream inputStream,
            @OutputData(file = "sortingAlgorithm/output02") OutputStream outputStream
    ) throws IOException {
        // given
        // Input data are loaded into inputStream from specified file

        // when
        testedObject.solve(inputStream, outputStream);

        // then
        // Extension is responsible for using your output and check it against content of the file specified by annotation
    }

}
