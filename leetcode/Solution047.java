public class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        List<Integer> ans = new ArrayList<Integer>();
        Arrays.sort(nums);
        P(nums, res, ans, new boolean[nums.length]);
        return res;
    }
    
    private void P(int[] nums, List<List<Integer>> res, List<Integer> ans, boolean[] used) {
        int n = nums.length;
        if (ans.size() == n) {
            res.add(new ArrayList<Integer>(ans));
            return;
        }
        
        for (int i = 0; i < n; i++) {
            if (!used[i] && // nums[i] not used
            // if successive two numbers are equal, if we choose the latter one to add first, 
            // the same case has occurred, thus dup
            (i == 0 || nums[i] != nums[i - 1] || used[i-1]) 
            ) {
                ans.add(nums[i]);
                used[i] = true;
                P(nums, res, ans, used);
                ans.remove(ans.size() - 1);
                used[i] = false;
            }
        }
    }
}
