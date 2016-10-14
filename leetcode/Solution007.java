public class Solution {
    public int reverse(int x) {
        boolean negative =false;
        if (x < 0) {
            x = -x;
            negative = true;
        }
        
        long reverse_x = 0L;
        while (x > 0) {
            int bit = x % 10;
            x /= 10;
            reverse_x = reverse_x * 10 + bit;
        }
        
        if (reverse_x > Integer.MAX_VALUE || reverse_x < Integer.MIN_VALUE)
            return 0;
    
        if (negative)
            reverse_x = -reverse_x;
        
        return (int) reverse_x;
    }
}
