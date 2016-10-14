import java.util.Arrays;

public class Solution {
    public int[] twoSum(int[] nums, int target) {
        int[] array = {-1, -1};
        int[] n = new int[nums.length]; 
        for (int i = 0; i < nums.length; i++) {
            n[i] = nums[i];
        }
        Arrays.sort(n);
        int i = 0, j = nums.length - 1;
        while (i < j) {
            if (n[i] + n[j] == target) {
                for (int k = 0; k < nums.length; k++) {
                    if (nums[k] == n[i] || nums[k] == n[j]) {
                        if (array[0] < 0) {
                            array[0] = k;
                        }
                        else if (array[1] < 0) {
                            array[1] = k;
                        }
                    }
                }
                return array;
            }
            else if (n[i] + n[j] > target) {
                j--;
            }
            else {
                i++;
            }
        }
        return new int[] {0, 0};
    }
}
