public class Solution {
    public String intToRoman(int num) {
        int[] romans = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        int romanLen = romans.length;
        int remains = num;
        int[] nums = new int[romanLen];
        while (remains > 0) {
            for (int i = 0; i < romanLen; i++) {
                if (remains >= romans[i]) {
                    nums[i] = remains / romans[i];
                    remains %= romans[i];
                }
            }
        }
        String roman = "";
        for (int i = 0; i < romanLen; i++) {
            int n = nums[i];
            for (int j = 0; j < n; j++) {
                roman += romanChar(romans[i]);
            }
        }
        return roman;
    }
    
    private String romanChar(int n) {
        if (n == 1000) 
            return "M";
        
        if (n == 900) 
            return "CM";
            
        if (n == 500) 
            return "D";
        
        if (n == 400) 
            return "CD";
        
        if (n == 100) 
            return "C";
        
        if (n == 90) 
            return "XC";
        
        if (n == 50) 
            return "L";
        
        if (n == 40) 
            return "XL";
        
        if (n == 10) 
            return "X";
        
        if (n == 9) 
            return "IX";
        
        if (n == 5) 
            return "V";
        
        if (n == 4) 
            return "IV";
        
        if (n == 1) 
            return "I";
            
        return "";
    }
}
