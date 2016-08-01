import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    String inverseBWT(String bwt) {
        //StringBuilder result = new StringBuilder();

        // write your code here
        char[] lastCol = bwt.toCharArray();
        int len = bwt.length();
        char[] firstCol = new char[len];
        int[] last2first = new int[len];
        countSort(lastCol, firstCol, last2first);

        int ptr = 0;
        char[] result = new char[len];
        for (int i = 0; i < len; i++) {
            result[len - 1 - i] = firstCol[ptr];
            ptr = last2first[ptr];
        }

        return new String(result);
        //return result.toString();
    }

    private void countSort(char[] a, char[] sorted, int[] last2first) {
        int bucketSize = 5; // A, T, G, C, $ only
        int[] bucket = new int[bucketSize];
        int len = a.length;

        for (int i = 0; i < len; i++) {
            bucket[charIndex(a[i])]++;
        }

        // accumulate
        int cnt = 0;
        for (int i = 0; i < bucketSize; i++) {
            int size_i = bucket[i];
            bucket[i] = cnt;
            cnt += size_i;
        }

        // sort
        for (int i = 0; i < len; i++) {
            int loc = bucket[charIndex(a[i])];
            sorted[loc] = a[i];
            bucket[charIndex(a[i])]++;
            last2first[i] = loc;
        }
    }

    private int charIndex(char c) {
        switch (c) {
            case '$':
                return 0;
            case 'A':
                return 1;
            case 'C':
                return 2;
            case 'G':
                return 3;
            case 'T':
                return 4;
        }
        return -1;
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}
