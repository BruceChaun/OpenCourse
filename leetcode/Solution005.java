public class Solution {
    public String longestPalindrome(String s) {
        int n = s.length();
        boolean[][] table = new boolean[n][n];
        int head = 0, tail = 0;
        for (int i = 0; i < n; i++) {
            table[i][i] = true;
        }
        
        for (int i = 0; i < n - 1; i++) {
            if (s.charAt(i) == s.charAt(i+1)) {
                table[i][i+1] = true;
                head = i;
                tail = i+1;
            }
        }
        
        for (int step = 2; step < n; step++) {
            for (int i = 0; i < n - step; i++) {
                int j = i + step;
                if (table[i+1][j-1] && s.charAt(i) == s.charAt(j)) {
                    table[i][j] = true;
                    if (j - i > tail - head) {
                        head = i;
                        tail = j;
                    }
                }
            }
        }
        
        return s.substring(head, tail+1);
    }
}
