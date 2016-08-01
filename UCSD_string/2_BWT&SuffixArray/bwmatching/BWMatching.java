import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BWMatching {
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

    // Preprocess the Burrows-Wheeler Transform bwt of some text
    // and compute as a result:
    //   * starts - for each character C in bwt, starts[C] is the first position
    //       of this character in the sorted array of
    //       all characters of the text.
    //   * occ_count_before - for each character C in bwt and each position P in bwt,
    //       occ_count_before[C][P] is the number of occurrences of character C in bwt
    //       from position 0 to position P inclusive.
    private void PreprocessBWT(String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before) {
        // Implement this function yourself

        countSort(bwt.toCharArray(), starts, occ_counts_before);
    }

    private void countSort(char[] a, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before) {
        int len = a.length;
        int bucketSize = 128; // ASCII
        int[] bucket = new int[bucketSize];

        for (int i = 0; i < len; i++) {
            bucket[a[i]]++;
        }

        int cnt = 0;
        for (int i = 0; i < bucketSize; i++) {
            int size_i = bucket[i];
            bucket[i] = cnt;
            cnt += size_i;
        }

        for (int i = 0; i < len; i++) {
            char c = a[i];
            int loc = bucket[c];
            if (!starts.containsKey(c)) {
                starts.put(c, loc);
            }
        }

        // init map
        for (Map.Entry<Character, Integer> entry : starts.entrySet()) {
            char key = entry.getKey();
            int[] counts = new int[len+1];
            occ_counts_before.put(key, counts);
        }

        for (int i = 1; i <= len; i++) {
            char c = a[i-1];

            for (Map.Entry<Character, int[]> entry : 
                    occ_counts_before.entrySet()) {
                char key = entry.getKey();
                int[] counts = entry.getValue();
                counts[i] = counts[i-1];
                if (key == c) {
                    counts[i]++;
                }
                occ_counts_before.put(key, counts);
            }
        }
    }

    // Compute the number of occurrences of string pattern in the text
    // given only Burrows-Wheeler Transform bwt of the text and additional
    // information we get from the preprocessing stage - starts and occ_counts_before.
    int CountOccurrences(String pattern, String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before) {
        // Implement this function yourself

        char[] lastCol = bwt.toCharArray();
        int top = 0;
        int bottom = bwt.length() - 1;
        while (top <= bottom) {
            int len = pattern.length();
            if (len == 0)
                return bottom - top + 1;

            char symbol = pattern.charAt(len - 1);
            pattern = pattern.substring(0, len - 1);

            int firstOccur = starts.get(symbol);
            int[] counts = occ_counts_before.get(symbol);
            top = firstOccur + counts[top];
            bottom = firstOccur + counts[bottom+1] - 1;
        }
        return 0;
    }

    static public void main(String[] args) throws IOException {
        new BWMatching().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        // Start of each character in the sorted list of characters of bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, Integer> starts = new HashMap<Character, Integer>();
        // Occurrence counts for each character and each position in bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
        // Preprocess the BWT once to get starts and occ_count_before.
        // For each pattern, we will then use these precomputed values and
        // spend only O(|pattern|) to find all occurrences of the pattern
        // in the text instead of O(|pattern| + |text|).
        PreprocessBWT(bwt, starts, occ_counts_before);
        int patternCount = scanner.nextInt();
        String[] patterns = new String[patternCount];
        int[] result = new int[patternCount];
        for (int i = 0; i < patternCount; ++i) {
            patterns[i] = scanner.next();
            result[i] = CountOccurrences(patterns[i], bwt, starts, occ_counts_before);
        }
        print(result);
    }
}
