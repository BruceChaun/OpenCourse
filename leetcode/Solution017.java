public class Solution {
    private final String[] map = {
        "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
    };
    
    public List<String> letterCombinations(String digits) {
        int len = digits.length();
        List<String> res = new ArrayList<String>();
        if (len == 0)
            return res;
        String[] ss = new String[len];
        for (int i = 0; i < len; i++) {
            ss[i] = map[digits.charAt(i) - '0'];
        }
        
        dfs(ss, 0, res, "");
        return res;
    }
    
    private void dfs(String[] ss, int layer, List<String> res, String cur) {
        if (layer == ss.length) {
            res.add(cur);
            return;
        }
        
        for (int i = 0; i < ss[layer].length(); i++) {
            String s = ss[layer].substring(i, i+1);
            dfs(ss, layer + 1, res, cur + s);
        }
    }
}
