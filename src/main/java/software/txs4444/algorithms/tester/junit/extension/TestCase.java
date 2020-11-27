package software.txs4444.algorithms.tester.junit.extension;

import lombok.Value;

import java.io.InputStream;

@Value(staticConstructor = "of")
class TestCase {
    String name;
    InputStream input;
    InputStream expectedOutput;
}
