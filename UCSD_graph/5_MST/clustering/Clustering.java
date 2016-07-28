import java.util.Scanner;
import java.util.*;

public class Clustering {
	private static class DistComparator implements Comparator<int[]> {
		private int[] x;
		private int[] y;

		public DistComparator(int[] x, int[] y) {
			this.x = x;
			this.y = y;
		}

		public int compare(int[] e1, int[] e2) {
			int p1 = e1[0];
			int p2 = e1[1];
			int dx = x[p1] - x[p2];
			int dy = y[p1] - y[p2];
			int len1 = dx * dx + dy * dy;
			p1 = e2[0];
			p2 = e2[1];
			dx = x[p1] - x[p2];
			dy = y[p1] - y[p2];
			int len2 = dx * dx + dy * dy;
			return len1 - len2;
		}
	}

    private static double clustering(int[] x, int[] y, int k) {
        //write your code here
		int nNode = x.length;
		int nEdge = nNode * nNode; // including e(i, i)
		double[][] adj  = new double[nNode][nNode];
		for (int i = 0; i < nNode; i++) {
			for (int j = i; j < nNode; j++) {
				double dx = 0.0 + x[i] - x[j];
				double dy = 0.0 + y[i] - y[j];
				adj[i][j] = Math.sqrt(dx*dx+dy*dy);
				adj[j][i] = adj[i][j];
			}
		}

		DistComparator dc = new DistComparator(x, y);
		PriorityQueue<int[]> pq 
			= new PriorityQueue<int[]>(nEdge, dc);

		for (int i = 0; i < nNode; i++) {
			for (int j = i+1; j < nNode; j++) {
				int[] e = {i, j};
				pq.offer(e);
			}
		}

		DisjointSet ds = new DisjointSet(nNode);
		double d = -1.0;
		while (ds.size() >= k) {
			int[] edge = pq.poll();
			int p1 = edge[0];
			int p2 = edge[1];

			if (ds.find(p1) != ds.find(p2)) {
				ds.union(p1, p2);
				double dx = 0.0 + x[p1] - x[p2];
				double dy = 0.0 + y[p1] - y[p2];
				d = Math.sqrt(dx*dx+dy*dy);
			}
		}

        return d;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }
        int k = scanner.nextInt();
        System.out.println(clustering(x, y, k));
    }
}

