public class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        int n = candidates.length;
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        List<Integer> ans = new ArrayList<Integer>();
        S(candidates, target, 0, res, ans);
        return res;
    }
    
    private void S(int[] nums, int target, int start, List<List<Integer>> res, List<Integer> ans) {
        int n = nums.length;
        if (start > n - 1)
            return;
            
        if (target == nums[start]) {
            ans.add(nums[start]);
            res.add(new ArrayList<Integer>(ans));
            ans.remove(ans.size() - 1);
            return;
        }
        
        if (target < nums[start])
            return;
        
        // adding nums[i] and stay
        ans.add(nums[start]);
        S(nums, target - nums[start], start, res, ans);
        // adding nums[i] and go next
        // S(nums, target - nums[start], start + 1, res, ans);
        ans.remove(ans.size() - 1);
        // not adding nums[i]
        S(nums, target, start + 1, res, ans);
    }
}
