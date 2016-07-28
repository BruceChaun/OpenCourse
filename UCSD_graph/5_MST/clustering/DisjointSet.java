public class DisjointSet {
	private int[] array;
	private int size;

	public DisjointSet(int size) {
		this.size = size;
		array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = -1;
		}
	}
	
	public int size() {
		return size;
	}

	public int find(int x) {
		if (array[x] < 0) 
			return x;

		array[x] = find(array[x]);
		return array[x];
	}

	public void union(int x, int y) {
		int xRoot = find(x);
		int yRoot = find(y);

		if (xRoot < yRoot) {
			array[xRoot] += array[yRoot];
			array[yRoot] = xRoot;
			size--;
		} else if (yRoot < xRoot) {
			array[yRoot] += array[xRoot];
			array[xRoot] = yRoot;
			size--;
		}
	}
}
