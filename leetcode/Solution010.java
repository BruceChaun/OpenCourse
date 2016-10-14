public class Solution {
    public boolean isMatch(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        boolean[][] mat = new boolean[pLen+1][sLen+1];
        mat[0][0] = true;
        
        // any char followed by '*' can be ignored
        for (int i = 1; i < pLen; i+=2) {
            if (p.charAt(i) == '*')
                mat[i+1][0] = true;
            else 
                break;
        }
        
        for (int i = 1; i <= pLen; i++) {
            char c = p.charAt(i-1);
            if (c != '*') {
                for (int j = 1; j <= sLen; j++) {
                    mat[i][j] = c == s.charAt(j-1) || c == '.';
                    mat[i][j] = mat[i][j] && mat[i-1][j-1];
                }
            } else {
                for (int j = 1; j <= sLen; j++) {
                    char prev_c = p.charAt(i-2);
                    char cur_s = s.charAt(j-1);
                    mat[i][j] = prev_c == '.' || prev_c == cur_s;
                    mat[i][j] = mat[i][j] && mat[i][j-1] || mat[i-2][j];
                }
            }
        }
        
        return mat[pLen][sLen];
    }
}
