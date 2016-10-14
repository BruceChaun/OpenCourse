public class Solution {
    public int[] searchRange(int[] nums, int target) {
        int n = nums.length;
        int i = 0, j = n - 1;
        int[] res = new int[]{-1, -1};
        
        // find the first occurrence
        int first = -1;
        while (i <= j) {
            int mid = (j - i) / 2 + i;
            if (nums[mid] < target) i = mid + 1;
            else j = mid;
            
            if (i == j) {
                if (nums[i] == target)
                    first = i;
                break;
            }
        }
        
        if (first == -1)
            return res;
            
        // find the last occurrence
        i = first + 1;
        j = n - 1;
        int last = first;
        while (i <= j) {
            if (i == j) {
                if (nums[i] == target)
                    last = i;
                break;
            }
                
            int mid = (j - i) / 2 + i + 1;
            if (nums[mid] > target) j = mid - 1;
            else i = mid;
        }
        
        res[0] = first;
        res[1] = last;
        
        return res;
    }
}
