public class Solution {
    public int lengthOfLongestSubstring(String s) {
        int chars = 256;
        int n = s.length();
        if (n < 1)
            return 0;
        int[] indices = new int[chars];
        for (int i=0; i<chars; i++) {
            indices[i] = -1;
        }
        indices[s.charAt(0)] = 0;
        int len = 1;
        int max_len = 1;
        
        for (int i=1; i<n; i++) {
            int pos = s.charAt(i);
            int prev_index = indices[pos];
            if (prev_index < 0 || i - prev_index > len)
                len++;
            else {
                if (len > max_len)
                    max_len = len;
                len = i - prev_index;
            }
            indices[pos] = i;
        }
        if (len > max_len)
            max_len = len;
        return max_len;
    }
}
