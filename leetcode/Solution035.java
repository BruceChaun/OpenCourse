public class Solution {
    public int searchInsert(int[] nums, int target) {
        int n = nums.length;
        if (n == 0)
            return 0;
            
        int i = 0, j = n - 1;
        int pos = -1;
        while (i < j) {
            int mid = (j - i) / 2 + i;
            if (nums[mid] > target)
                j = mid - 1;
            else if (nums[mid] < target)
                i = mid + 1;
            else {
                pos = mid;
                break;
            }
        }
        
        if (pos == -1) {
            if (nums[i] < target)
                pos = i + 1;
            else
                pos = i;
        }
        return pos;
    }
}
