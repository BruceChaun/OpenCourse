import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixArrayLong {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        // write your code here

        // compute the order and equivalent classes of the 
        // single characters
        int len = text.length();
        int[] order = countSort(text);
        int[] classes = initialClass(text, order);
        int l = 1;
        while (l < len) {
            order = doubleSort(text, l, order, classes);
            classes = updateClass(classes, order, l);
            l *= 2;
        }
        return order;
    }

    /*
     * countSort() sorts all characters in a String, and stores 
     * the order in an array.
     */
    private int[] countSort(String s) {
        int len = s.length();
        char[] chars = s.toCharArray();
        int[] order = new int[len];
        final int BUCKETSIZE = 128;
        int[] bucket = new int[BUCKETSIZE];

        for (int i = 0; i < len; i++) {
            bucket[chars[i]]++;
        }

        int prevSum = 0;
        for (int i = 0; i < BUCKETSIZE; i++) {
            int tmp = bucket[i];
            bucket[i] = prevSum;
            prevSum += tmp;
        }

        for (int i = 0; i < len; i++) {
            order[bucket[chars[i]]++] = i;
        }

        return order;
    }

    /*
     * initialClass() computes the equivalent class of all single 
     * characters in a string. In this case, two characters are 
     * in the same class if they are the same.
     */
    private int[] initialClass(String s, int[] order) {
        int len = s.length();
        char[] chars = s.toCharArray();
        int[] classes = new int[len];
        classes[0] = 0;

        for (int i = 1; i < len; i++) {
            int cur = order[i], prev = order[i-1];
            if (chars[cur] == chars[prev])
                classes[cur] = classes[prev];
            else
                classes[cur] = classes[prev] + 1;
        }

        return classes;
    }

    /*
     * doubleSort() doubles the size of the current sorted @order, and 
     * sorts substrings with size 2 * @len. Since the second half has 
     * been sorted from the previous procedure, use countSort method to 
     * sort the first half.
     */
    private int[] doubleSort(String s, int len, int[] order, int[] classes) {
        int size = s.length();
        int[] newOrder = new int[size];
        int[] count = new int[size];

        for (int i = 0; i < size; i++) {
            count[classes[i]]++;
        }

        int prevSum = 0;
        for (int i = 0; i < size; i++) {
            int tmp = count[i];
            count[i] = prevSum;
            prevSum += tmp;
        }

        for (int i = 0; i < size; i++) {
            int start = (order[i] - len + size) % size;
            int cls = classes[start];
            newOrder[count[cls]++] = start;
        }

        return newOrder;
    }

    /*
     * updateClass() updates the equivalent class after substring are 
     * augmented and sorted. Compare the classes of two parts in a 
     * substring with another, if they are both equal, then they are 
     * in the same class; otherwise increment class number.
     */ 
    private int[] updateClass(int[] classes, int[] order, int len) {
        int size = order.length;
        int[] newClasses = new int[size];
        newClasses[order[0]] = 0;
        for (int i = 1; i < size; i++) {
            int cur1 = order[i];
            int prev1 = order[i-1];
            int cur2 = (cur1 + len) % size;
            int prev2 = (prev1 + len) % size;
            if (classes[cur1] == classes[prev1] && classes[cur2] == classes[prev2]) {
                newClasses[cur1] = newClasses[prev1];
            } else {
                newClasses[cur1] = newClasses[prev1] + 1;
            }
        }

        return newClasses;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
