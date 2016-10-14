public class Solution {
    private int nth(int x, int n) {
        for (int i = 0; i < n - 1; i++)
            x /= 10;
        return x % 10;
    }
    
    public boolean isPalindrome(int x) {
        if (x < 0)
            return false;
        if (x == 0)
            return true;
        int len = 0;
        int y = x;
        while (y > 0) {
            len++;
            y /= 10;
        }
        if (len == 1)
            return true;
        int i = 1, j = len;
        while (i < j) {
            if (nth(x, i) != nth(x, j))
                return false;
            i++;
            j--;
        }
        return true;
    }
}
