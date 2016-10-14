public class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        P(nums, 0, result);
        return result;
    }
    
    private void P(int[] nums, int start, List<List<Integer>> result) {
        int n = nums.length;
        if (start == n) {
            List<Integer> ans = new ArrayList<Integer>();
            for (int x : nums) {
                ans.add(x);
            }
            result.add(ans);
            return;
        }
        
        for (int i = start; i < n; i++) {
            swap(nums, start, i);
            P(nums, start + 1, result);
            swap(nums, start, i);
        }
    }
    
    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
