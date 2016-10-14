public class Solution {
    public int romanToInt(String s) {
        //s = s.toUpperCase();
        int len = s.length();
        if (len == 0)
            return 0;
        
        int prev_n = roman(s.charAt(0));
        int num = prev_n;
        for (int i = 1; i < len; i++) {
            int v = roman(s.charAt(i));
            if (v > prev_n) {
                num -= prev_n;
                num += v - prev_n;
            } else {
                num += v;
            }
            prev_n = v;
        }
        return num;
    }
    
    private int roman(char c) {
        switch (c) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }
}
