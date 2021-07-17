# Algorithmic-tester

Algorithmic-tester is an extension for Jupiter which aims to help testing challenges on platfomrs such as HackerRank.

If you write an algorithm it takes an input and is expected to print some output.
Often examples are given. It could be nice to just copy them into input/output files and run test - it is exactly what Algorithmic-tester is for.

# Example of usage
Examples shown below are part of the test suit and can be found [here](src/test/java/software/txs4444/algorithms/tester/junit/extension/end2end)

## Run for multiple test cases
It runs solve against each test case defined in *end2end* directory. Each test case is defined as two *input* and *output* files 
with matching suffixes. Suffixes must be made of numbers. For example *input01* and *output01*.  

```java
@ExtendWith(AlgorithmicTesterExtension.class)
public class BubbleSortAlgorithmTest {
    private final BubbleSortAlgorithm testedObject = new BubbleSortAlgorithm();

    @TestTemplate
    @AlgorithmTestCases(directory = "end2end")
    public void shouldPassPredefinedTests(@TestCaseInput InputStream input, @TestCaseOutput OutputStream output) {
        // given
        // Input data are loaded into inputStream from specified file

        // when
        testedObject.solve(input, output);

        // then
        // Extension is responsible for using your output and check it against content of the file specified by annotation
    }
}
```
 
1) Use *AlgorithmicTesterExtension* extension
2) Mark method as [TestTemplate](https://junit.org/junit5/docs/current/user-guide/#writing-tests-test-templates)
3) Mark method with *AlgorithmTestCases* and indicate directory containing test cases
4) Test method must have two parameters of type InputStream and OutputStream and parameters must be marked as *TestCaseInput* and *TestCaseOutput* respectively

Test class can have multiple methods with different directories for each method.

## Run single test case
In some cases (i.g. debugging) one my want to run only a single test case. It can be done as follows:

```java
@ExtendWith(SingleCaseAlgorithmicTesterExtension.class)
public class BubbleSortAlgorithmTest {
    private final BubbleSortAlgorithm testedObject = new BubbleSortAlgorithm();
    
    @Test
    public void testDataSet01(
            @InputData(file = "end2end/input01") InputStream inputStream,
            @OutputData(file = "end2end/output01") OutputStream outputStream
    ) {
        // given
        // Input data are loaded into inputStream from specified file

        // when
        testedObject.solve(inputStream, outputStream);

        // then
        // Extension is responsible for using your output and check it against content of the file specified by annotation
    }
}
```

1) Use *SingleCaseAlgorithmicTesterExtension* extension
2) Mark as ordinary Jupiter's *Test* method 
3) Test method must have two parameters of type InputStream and OutputStream
4) Parameters must be marked with *InputData* and *OutputData* and each annotation must point to input and output file respectively. 

## Template to copy and past
It is nice to write an algorithm in IDE along with tests. However, it needs to be easy to copy it and past into platform such as HackerRank.

I follow a template to make copping simple and does not require any adjustment.

```java
import java.io.InputStream;

class Algorithm {
    public static void solve(InputStream input, OutputStream output) throws IOException {
        Scanner scanner = new Scanner(input);
        // parsing input using scanner and producing some date - it is algorithm specific and cannot be avoided
        result = algorithmMethod(data);
        OutputStreamWriter writer = OutputStreamWriter(output);
        // writing result into writer - also algorithm specific
        writer.flush();
    }
    
    private static RESULT_TYPE algorithmMethod(...) {
        // code your algorithm here
    }
}
```

Then it is as easy as copying and pasting those methods and modifying *main* method (which is usually provided).
```java
public static void main(String[] args) throws IOException {
        solve(System.in, System.out);
}
```