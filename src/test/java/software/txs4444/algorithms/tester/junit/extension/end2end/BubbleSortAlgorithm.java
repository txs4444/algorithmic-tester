package software.txs4444.algorithms.tester.junit.extension.end2end;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BubbleSortAlgorithm {
    public void solve(InputStream inputStream, OutputStream outputStream) throws IOException {
        // parse input data from inputStream
        Scanner scanner = new Scanner(inputStream);
        int n = scanner.nextInt();
        ArrayList<Integer> list = readListOfIntegers(scanner, n);

        // implementation of algorithm
        sort(list);

        // print out results
        printoutResult(list, outputStream);
    }

    private void printoutResult(ArrayList<Integer> list, OutputStream outputStream) throws IOException {
        String resultAsString = list.stream()
                .map(Object::toString).
                        collect(Collectors.joining(" "));
        OutputStreamWriter output = new OutputStreamWriter(outputStream);
        output.write(resultAsString);
        output.flush();
    }

    private ArrayList<Integer> readListOfIntegers(Scanner scanner, int n) {
        ArrayList<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(i, scanner.nextInt());
        }
        return list;
    }

    private void sort(ArrayList<Integer> list) {
        int n = list.size();
        for (int endIndex = 0; endIndex < n; endIndex++) {
            for (int j = 1; j < n - endIndex; j++) {
                int leftElementIndex = j - 1;
                int rightElementIndex = j;
                if (list.get(leftElementIndex) > list.get(rightElementIndex)) {
                    swap(leftElementIndex, rightElementIndex, list);
                }
            }
        }
    }

    private void swap(int i, int j, ArrayList<Integer> list) {
        int tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }
}
