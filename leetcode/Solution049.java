public class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<List<String>>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (String s : strs) {
            char[] cnt = new char[26];
            String count = "";
            for (int i = 0; i < s.length(); i++) {
                int index = s.charAt(i) - 'a';
                
                cnt[index] = (char) (cnt[index]+1);
            }
            for (int i = 0; i < 26; i++) {
                count += cnt[i];
            }
            List<String> group;
            if (map.containsKey(count)) {
                group = res.get(map.get(count));
                group.add(s);
            } else {
                group = new ArrayList<String>();
                group.add(s);
                map.put(count, res.size());
                res.add(group);
            }
        }
        return res;
    }
}
