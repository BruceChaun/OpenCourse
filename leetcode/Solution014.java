public class Solution {
    public String longestCommonPrefix(String[] strs) {
        int n = strs.length;
        if (n == 0)
            return "";
            
        int size = strs[0].length();
        for (int i = 1; i < n; i++) {
            int si = strs[i].length();
            if (si == 0)
                return "";
                
            if (size > si)
                size = si;
                
            while (!strs[0].substring(0, size).equals(strs[i].substring(0, size)))
                size--;
        }
        return strs[0].substring(0, size);
    }
}
