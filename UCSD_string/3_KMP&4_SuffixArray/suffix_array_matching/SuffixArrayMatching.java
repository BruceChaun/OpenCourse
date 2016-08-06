import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuffixArrayMatching {
    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
        }
    }


    public int[] computeSuffixArray(String text) {
        // write your code here

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

    public List<Integer> findOccurrences(String pattern, String text, int[] suffixArray) {
        List<Integer> result = new ArrayList<Integer>();

        // write your code here

        /*
         * binary search method, but in while statement, if min == max, 
         * it will jump out of the iteration. And if at position min, 
         * substring starting at suffixArray[min] will not compare with 
         * pattern. Therefore, we will miss the case and probably get 
         * the wrong answer.
         */
        int size = suffixArray.length;
        int patLen = pattern.length();
        int min = 0, max = size - 1;

        // find the first match, if any
        while (min < max) {
            int mid = (max - min) / 2 + min;
            int pos = suffixArray[mid];
            String substring = "";
            if (pos + patLen < size)
                substring = text.substring(pos, pos + patLen);
            else 
                substring = text.substring(pos);

            if (pattern.compareTo(substring) <= 0) 
                max = mid;
            else 
                min = mid + 1;

            if (min == max) {
                pos = suffixArray[min];
                if (pos + patLen < size)
                    substring = text.substring(pos, pos + patLen);
                else 
                    substring = text.substring(pos);

                if (!pattern.equals(substring)) 
                    min++;
            }
        }
        int start = min;

        // find the last match, if any
        max = size - 1;
        while (min < max) {
            int mid = (max - min) / 2 + min;
            int pos = suffixArray[mid];
            String substring = "";
            if (pos + patLen < size)
                substring = text.substring(pos, pos + patLen);
            else 
                substring = text.substring(pos);

            if (pattern.compareTo(substring) < 0) 
                max = mid;
            else 
                min = mid + 1;

            if (min == max) {
                pos = suffixArray[min];
                if (pos + patLen < size)
                    substring = text.substring(pos, pos + patLen);
                else 
                    substring = text.substring(pos);

                if (!pattern.equals(substring))
                    max--;
            }
        }
        int end = max;

        for (int i = start; i <= end; i++)
            result.add(suffixArray[i]);

        return result;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {
        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrences(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
    }
}
