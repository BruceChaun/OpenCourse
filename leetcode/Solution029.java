public class Solution {
    public int divide(int dividend, int divisor) {
        if (dividend == 0)
            return 0;
            
        if (divisor == 0) {
            return Integer.MAX_VALUE;
        }
        
        if (dividend == Integer.MIN_VALUE && divisor == -1)
            return Integer.MAX_VALUE;
        
        if (dividend > 0 && divisor > 0)
            return div(-dividend, -divisor);
        
        else if (dividend > 0)
            return -div(-dividend, divisor);
            
        else if (divisor > 0)
            return -div(dividend, -divisor);
            
        else
            return div(dividend, divisor);
    }
    
    private int div(int dividend, int divisor) {
        if (dividend > divisor)
            return 0;
            
        int q = 0, local_q = 0;
        int d = divisor;
        while (dividend <= d && d < 0) {
            local_q++;
            d <<= 1;
        }
        local_q--;
        int r = dividend - (divisor << local_q);
        q += (1 << local_q) + div(r, divisor);
        return q;
    }
}
