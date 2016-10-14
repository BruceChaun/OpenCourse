public class Solution {
    public int myAtoi(String str) {
        long num = 0L;
        str = str.trim();
        int n = str.length();
        if (n <= 0) {
            return 0;
        }
        boolean negative = false;
        int validLen = 0;
        int i = 0;
        char c0 = str.charAt(0);
        
        if (c0 == '-') {
            negative = true;
            i++;
        } else if (c0 == '+') {
            i++;
        } else if (c0 < '0' || c0 > '9') {
            return 0;
        }
        
        if (i >= n) 
            return 0;
        while(str.charAt(i) == '0') {
            i++;
            if (i >= n)
                return 0;
        }
        
        for (; i < n; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9')
                break;
                
            num = num * 10 + (c - '0');
            validLen++;
        }
        if (negative)
            num = -num;
        if (num > Integer.MAX_VALUE)
            return negative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (num < Integer.MIN_VALUE)
            return negative ? Integer.MIN_VALUE : Integer.MA_VALUE;
        if (validLen > 10)
            return negative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        return (int) num;
    }
}
