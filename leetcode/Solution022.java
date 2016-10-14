public class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<String>();
        if (n >= 0)
            recursive(res, 0, n, "");
        return res;
    }
    
    private void recursive(List<String> res, int diff, int nLeftRemain, String cur) {
        if (nLeftRemain == 0) {
            String toAdd = cur;
            for (int i = 0; i < diff; i++) {
                toAdd += ")";
            }
            res.add(toAdd);
            return;
        }
        
        recursive(res, diff + 1, nLeftRemain - 1, cur + "(");
        if (diff > 0)
            recursive(res, diff - 1, nLeftRemain, cur + ")");
    }
}
