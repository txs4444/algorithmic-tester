package software.txs4444.algorithms.tester.junit.extension;

import lombok.Value;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.extension.*;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;


public class AlgorithmicTesterExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        Method testMethod = getTestMethod(context);
        AlgorithmTestCases algorithmTestCasesAnnotation = testMethod.getAnnotation(AlgorithmTestCases.class);
        int parameterCount = testMethod.getParameterCount();
        Set<Class<?>> parameterTypes = Set.of(testMethod.getParameterTypes());
        return algorithmTestCasesAnnotation != null
                && parameterCount == 2
                && parameterTypes.contains(InputStream.class)
                && parameterTypes.contains(OutputStream.class);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        Method testMethod = getTestMethod(context);
        AlgorithmTestCases algorithmTestCasesAnnotation = testMethod.getAnnotation(AlgorithmTestCases.class);
        return loadTestCases(algorithmTestCasesAnnotation.directory())
                .map(this::createTestTemplateContextForInputData);
    }

    private TestTemplateInvocationContext createTestTemplateContextForInputData(TestCase testCase) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return testCase.getName();
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(
                        new SingleCaseAlgorithmicTesterExtension(
                                new TestCaseDataProvider() {
                                    @Override
                                    public InputStream inputData(ParameterContext parameterContext) {
                                        return testCase.getInput();
                                    }

                                    @Override
                                    public InputStream expectedOutputData(ParameterContext parameterContext) {
                                        return testCase.getExpectedOutput();
                                    }

                                    @Override
                                    public boolean isInputDataParameter(ParameterContext parameterContext) {
                                        return parameterContext.isAnnotated(TestCaseInput.class);
                                    }

                                    @Override
                                    public boolean isExpectedOutputDataParameter(ParameterContext parameterContext) {
                                        return parameterContext.isAnnotated(TestCaseOutput.class);
                                    }
                                }
                        )
                );
            }
        };
    }

    private Stream<TestCase> loadTestCases(String directory) {
        SortedSet<File> inputFiles = findFiles(directory, "input[0-9]+");
        SortedSet<File> outputFiles = findFiles(directory, "output[0-9]+");
        if (inputFiles.size() != outputFiles.size()) {
            throw new IllegalStateException("Number of input files differ from output files");
        }
        Iterator<File> inputFilesIterator = inputFiles.iterator();
        Iterator<File> outputFilesIterator = outputFiles.iterator();
        List<TestCase> testCases = new ArrayList<>();
        while (inputFilesIterator.hasNext() && outputFilesIterator.hasNext()) {
            File inputFile = inputFilesIterator.next();
            String inputFileSuffixIndex = inputFile.getName().substring(5);
            File outputFile = outputFilesIterator.next();
            String outputFileSuffixIndex = outputFile.getName().substring(6);
            if (!inputFileSuffixIndex.equals(outputFileSuffixIndex)) {
                throw new IllegalStateException("No corresponding input-output for input" + inputFileSuffixIndex);
            }
            try {
                testCases.add(TestCase.of(inputFileSuffixIndex, new FileInputStream(inputFile), new FileInputStream(outputFile)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Could not load input/output for test case: " + outputFileSuffixIndex, e);
            }
        }
        return testCases.stream();
    }

    private SortedSet<File> findFiles(String directory, String inputFilesPattern) {
        URL directoryUrl = getClass().getClassLoader().getResource(directory);
        if (Objects.isNull(directoryUrl)) {
            throw new AlgorithmTestCaseDataFilesException(String.format("Could not open data directory: %s", directory));
        }
        URI uri = null;
        try {
            uri = directoryUrl.toURI();
        } catch (URISyntaxException e) {
            throw new AlgorithmTestCaseDataFilesException(String.format("Could not open data directory: %s", directoryUrl), e);
        }
        File resourceDir = new File(uri);
        File[] inputFiles = resourceDir.listFiles((FileFilter) new RegexFileFilter(inputFilesPattern));
        SortedSet<File> sortedInputFiles = Sets.newTreeSet(inputFiles);
        return sortedInputFiles;
    }

    private Method getTestMethod(ExtensionContext context) {
        return context.getTestMethod().orElseThrow(() -> new IllegalStateException("No test method"));
    }


    @Value(staticConstructor = "of")
    static class TestCase {
        String name;
        InputStream input;
        InputStream expectedOutput;
    }
}
