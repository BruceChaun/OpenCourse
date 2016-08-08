import java.util.*;

class SuffixArray {
    int[] buildSuffixArray(String text) {
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

    /*
     * Build LCP array in linear time
     */
    int[] buildLCPArray(String text, int[] order) {
        int len = text.length();
        char[] chars = text.toCharArray();
        int[] lcpArray = new int[len-1];
        int[] rank = invertSuffixArray(order);
        int lcp = 0;

        /*
         * compare the first two sorted suffixes and then augment.
         */
        int pos1 = order[0];
        for (int i = 0; i < len; i++) {
            int orderIndex = rank[pos1];
            if (orderIndex == len - 1)
                lcp = 0;
            else {
                int pos2 = order[orderIndex + 1];
                lcp = LCP(chars, pos1, pos2, lcp);
                lcpArray[orderIndex] = lcp;
            }
            pos1 = (pos1 + 1) % len;
        }

        return lcpArray;
    }

    /*
     * invertSuffixArray() is the inverted function of building suffix 
     * array, i.e., invertSuffixArray(suffixArray(i)) = i.
     */
    private int[] invertSuffixArray(int[] order) {
        int len = order.length;
        int[] rank = new int[len];
        for (int i = 0; i < len; i++) {
            rank[order[i]] = i;
        }
        return rank;
    }

    /*
     * LCP() computes two suffixes starting at @pos1 and @pos2 based on 
     * the previous computed lcp value @equal.
     *
     * NOTE: new lcp will always decrease by at most 1, compared with 
     * the old lcp.
     */
    private int LCP(char[] chars, int pos1, int pos2, int equal) {
        int len = chars.length;
        int lcp = equal;
        if (lcp > 0) 
            lcp--;

        while (pos1 + lcp < len && pos2 + lcp < len) {
            if (chars[pos1 + lcp] == chars[pos2 + lcp])
                lcp++;
            else
                break;
        }

        return lcp;
    }
}
