public class Solution {
    public double myPow(double x, int n) {
        if (x == 1.0)
            return 1.0;
            
        if (n == -1)
            return 1 / x;
            
        if (n == 0)
            return 1;
            
        if (n == 1)
            return x;
        
        if (n % 2 == 0) {
            double pow = myPow(x, n / 2);
            return pow * pow;
        }
        else {
            if (n > 0) {
                double pow = myPow(x, n / 2);
                return x * pow * pow;
            } else {
                double pow = myPow(x, n / 2);
                return pow * pow / x;
            }
        }
    }
}
