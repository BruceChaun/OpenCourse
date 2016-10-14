public class Solution {
    public void nextPermutation(int[] nums) {
        int len = nums.length;
        int beginNum = 0;
        int ptr = len - 2;
        while (ptr >= 0 && nums[ptr] >= nums[ptr + 1])
            ptr--;
        
        if (ptr >= 0)
            beginNum = nums[ptr];
        else {
            int i = 0, j = len - 1;
            while (i < j) {
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
                i++;
                j--;
            }
            return;
        }
           
        int sortedLen = len - ptr;
        int[] sorted = new int[sortedLen];
        for (int i = 0; i < sortedLen; i++) {
            sorted[i] = nums[i + ptr];
        }
        Arrays.sort(sorted);
        
        int pos = 0;
        while (pos < sortedLen && sorted[pos] <= beginNum)
            pos++;
        
        nums[ptr] = sorted[pos];
        for (int i = 0; i < sortedLen; i++) {
            if (i < pos) {
                nums[ptr + 1 + i] = sorted[i];
            } else if (i > pos){
                nums[ptr + i] = sorted[i];
            }
        }
    }
}
