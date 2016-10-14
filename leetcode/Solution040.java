public class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        int n = candidates.length;
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        List<Integer> ans = new ArrayList<Integer>();
        S(candidates, target, 0, res, ans);
        return res;
    }
    
    private void S(int[] nums, int target, int start, List<List<Integer>> res, List<Integer> ans) {
        if (target == 0) {
            // ans.add(nums[start]);
            res.add(new ArrayList<Integer>(ans));
            // ans.remove(ans.size() - 1);
            return;
        }
        
        int n = nums.length;
        if (start > n - 1)
            return;
        
        for (int i = start; i < n; i++) {
            if (i > start && nums[i] == nums[i-1])
                continue;
                
            if (nums[i] > target)
                break;
                
            ans.add(nums[i]);
            S(nums, target - nums[i], i + 1, res, ans);
            ans.remove(ans.size() - 1);
        }
        /*
        // adding nums[i] and stay
        ans.add(nums[start]);
        
        S(nums, target - nums[start], start + 1, res, ans);
        
        while (start < n - 1 && nums[start] == nums[start+1])
            start++;
        // adding nums[i] and go next
        // S(nums, target - nums[start], start + 1, res, ans);
        ans.remove(ans.size() - 1);
        // not adding nums[i]
        S(nums, target, start + 1, res, ans);*/
    }
}
