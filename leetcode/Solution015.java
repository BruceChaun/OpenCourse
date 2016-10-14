public class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        java.util.Arrays.sort(nums);
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            if (i > 0 && nums[i-1] == nums[i])
        		continue;
        		
            int target = -nums[i];
            int head = i + 1;
            int tail = n - 1;
            
            while (head < tail) {
                int sum = nums[head] + nums[tail];
                if (sum == target) {
                    List<Integer> triple = new ArrayList<Integer>();
                    triple.add(nums[i]);
                    triple.add(nums[head]);
                    triple.add(nums[tail]);
                    result.add(triple);
                    
                    int newHead = head + 1;
                    int newTail = tail - 1;
                    while (newHead < n && nums[newHead] == nums[head])
                    	newHead++;
                    while (newTail > i && nums[newTail] == nums[tail])
                    	newTail--;
                    
                    head = newHead;
                    tail = newTail;
                } else if (sum < target) {
                    head++;
                } else {
                    tail--;
                }
            }
        }
        
        return result;
    }
}
