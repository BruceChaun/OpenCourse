public class Solution {
    public String multiply(String num1, String num2) {
        int n1 = num1.length(), n2 = num2.length();
        char[] chars1 = num1.toCharArray();
        char[] chars2 = num2.toCharArray();
        char[] result = new char[n1 + n2];
        for (int i = 0; i < result.length; i++)
            result[i] = '0';
        
        for (int i = n2 - 1, ptr = result.length - 1; i >= 0; i--) {
            int n = chars2[i] - '0';
            int carrier = 0;
            int j = 0;
            for (j = 0; j < n1; j++) {
                int prod = (chars1[n1 - 1 - j] - '0') * n + carrier;
                prod += result[ptr - j] - '0';
                carrier = prod / 10;
                result[ptr - j] = (char) (prod % 10 + '0');
            }
            if (carrier > 0)
                result[ptr - j] = (char) (carrier + '0');
            
            ptr--;
        }
        
        String prod = new String(result);
        int cut = 0;
        while (prod.charAt(cut) == '0') {
            cut++;
            if (cut >= prod.length())
                return "0";
        }
        return prod.substring(cut);
        //return product(num1, num2);
    }
    
    // Assume s1.length <= s2.length
    private String product(String s1, String s2) {
        if (s1.equals("0") || s2.equals("0"))
            return "0";
            
        int n1 = s1.length(), n2 = s2.length();
        if (n1 > n2)
            return product(s2, s1);
        
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        if (n1 == 1) {
            String result = "";
            int num = chars1[0] - '0';
            int carrier = 0;
            for (int i = n2 - 1; i >= 0; i--) {
                int prod = (chars2[i] - '0') * num + carrier;
                result = (prod % 10) + result;
                carrier = prod / 10;
            }
            
            if (carrier > 0) {
                result = carrier + result;
            }
            
            
            return result;
        }
        
        int half1 = n2 / 2;
        int half2 = n2 - half1;
        /*
            x = a + b, y = c + d
            x * y = (a + b) * (c + d) = ac + ad + bc + bd
            ad + bc = (a + b) * (c + d) - ac - bd
        */
        if (n1 <= half2) {
            String low = product(s1, s2.substring(half1));
            String high = product(s1, s2.substring(0, half1));
            for (int i = 0; i < half2; i++)
                high += "0";
            
            return add(low, high);
        } else {
            String s11 = s1.substring(0, n1 - half2);   // a
            String s12 = s1.substring(n1 - half2);      // b
            String s21 = s2.substring(0, half1);        // c
            String s22 = s2.substring(half1);           // d
            
            String ac = product(s11, s21);
            String bd = product(s12, s22);
            String xy = product(add(s11, s12), add(s21, s22));
            String adbc = minus(xy, add(ac, bd));
            
            for (int i = 0; i < half2 * 2; i++) {
                ac += "0";
            }
            for (int i = 0; i < half2; i++) {
                adbc += "0";
            }
            return add(add(ac, adbc), bd);
        }
    }
    
    // Assume s1.length <= s2.length
    private String add(String s1, String s2) {
        int n1 = s1.length(), n2 = s2.length();
        if (n1 > n2)
            return add(s2, s1);
        
        String sum = "";
        String first = s1;
        for (int i = n1; i < n2; i++) {
            first = "0" + first;
        }
        
        char[] chars1 = first.toCharArray();
        char[] chars2 = s2.toCharArray();
        int carrier = 0;
        for (int i = n2 - 1; i >= 0; i--) {
            int num1 = chars1[i] - '0';
            int num2 = chars2[i] - '0';
            int localSum = num1 + num2 + carrier;
            sum = (localSum % 10) + sum;
            carrier = localSum / 10;
        }
        if (carrier > 0) {
            sum = carrier + sum;
        }
        
        return sum;
    }
    
    private String minus(String s1, String s2) {
        int n1 = s1.length(), n2 = s2.length();
        String s = s2;
        for (int i = n2; i < n1; i++) {
            s = "0" + s;
        }
        
        String diff = "";
        boolean carrier = false;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s.toCharArray();
        for (int i = n1 - 1; i >= 0; i--) {
            int num1 = chars1[i] - '0', num2 = chars2[i] - '0';
            if (carrier)
                num1--;
                
            if (num1 < num2) {
                carrier = true;
                num1 += 10;
            } else {
                carrier = false;
            }
            
            int localDiff = num1 - num2;
            diff = localDiff + diff;
        }
        
        int cutoff = 0;
        while (diff.charAt(cutoff) == '0') {
            cutoff++;
            if (cutoff >= diff.length())
                return "0";
        }
        
        return diff.substring(cutoff);
    }
}
