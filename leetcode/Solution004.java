public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        int len = len1+len2;
        int low1 = 0, high1 = len1 - 1, low2 = 0, high2 = len2 - 1;
        
        if (len1 == 0) {
            if (len2 == 0)
                return 0;
            return median(nums2, 0, len2-1);
        } else {
            if (len2 == 0)
                return median(nums1, 0, len1-1);
        }
        
        int k = (len-1) / 2;
        if (len % 2 == 0) {
            return (kthSmallest(nums1, 0, len1-1, nums2, 0, len2-1, k+1) 
            + kthSmallest(nums1, 0, len1-1, nums2, 0, len2-1, k+2)) / 2.0;
        } else {
            return kthSmallest(nums1, 0, len1-1, nums2, 0, len2-1, k+1);
        }
    }
    
    private double median(int[] array, int low, int high) {
        int mid = (low+high)/2;
        if ((high-low)%2 == 0) {
            return array[mid];
        }
        return (array[mid] + array[mid+1]) / 2.0;
    }
    
    private int max(int a, int b) {
        return a > b ? a : b;
    }
    
    private int min(int a, int b) {
        return a < b ? a : b;
    }
    
    private int kthSmallest(int[] a1, int l1, int h1, int[] a2, int l2, int h2, int k) {
        if (h1-l1>h2-l2)
            return kthSmallest(a2, l2, h2, a1, l1, h1, k); 
        if (h1 < l1)
            return a2[k-1];
        if (k == 1) {
            return min(a1[l1], a2[l2]);
        }
        int m1 = min(h1-l1+1, k/2);
        int m2 = k - m1;
        if (a1[m1-1+l1] == a2[m2-1+l2]) {
            return a1[m1+l1-1];
        }
        else if (a1[m1-1+l1] < a2[m2-1+l2]) {
            return kthSmallest(a1, m1+l1, h1, a2, l2, m2+l2-1, k-m1);
        } else {
            return kthSmallest(a1, l1, m1+l1-1, a2, m2+l2, h2, k-m2);
        }
    }
}
