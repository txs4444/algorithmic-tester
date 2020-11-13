package software.txs4444.algorithms.tester;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BubbleSortAlgorithm {
    public void solve(InputStream inputStream, OutputStream outputStream) throws IOException {
        Scanner scanner = new Scanner(inputStream);
        int n = scanner.nextInt();
        ArrayList<Integer> list = readListOfIntegers(scanner, n);

        sort(n, list);

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

    private void sort(int n, ArrayList<Integer> list) {
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (list.get(j) > list.get(i)) {
                    swap(i, j, list);
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
