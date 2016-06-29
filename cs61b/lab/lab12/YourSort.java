/* YourSort.java */

public class YourSort {

  public static void sort(int[] A) {
    // Place your Part III code here.
	int Asize = A.length;
	if (Asize <= 40) {
		Sort.selectionSort(A);
	} else {
		Sort.quicksort(A);
	}
  }
}
