import java.util.Scanner;

public class ConnectingPoints {
	private static final double INF = Double.MAX_VALUE;

    private static double minimumDistance(int[] x, int[] y) {
        double result = 0.;
        //write your code here
		//
		// Since any two nodes can be viewed as connected, 
		// the graoh is dense, |E| = O(|V|^2). So use Prim 
		// alogorithm with array.
		int nNode = x.length;
		double[] cost = new double[nNode]; // distance from src node
		boolean[] added = new boolean[nNode]; // is added to MST?
		for (int i = 0; i < nNode; i++) {
			cost[i] = INF;
			added[i] = false;
		}
		cost[0] = 0; // 0 as src

		int num = 0; // #nodes added to MST, stop when |V|-1
		while (num++ < nNode) {
			int node = min(cost, added);
			result += cost[node];
			for (int i = 0; i < nNode; i++) {
				if (!added[i]) {
					double dx = 0.0 + x[i] - x[node];
					double dy = 0.0 + y[i] - y[node];
					double dist = Math.sqrt(dx*dx+dy*dy);
					if (dist < cost[i]) {
						cost[i] = dist;
					}
				}
			}
		}

        return result;
    }

	private static int min(double[] a, boolean[] added) {
		int min_i = -1;
		double min_cost = INF;
		for (int i = 0; i < a.length; i++) {
			if (!added[i] && min_cost > a[i]) {
				min_i = i;
				min_cost = a[i];
			}
		}
		added[min_i] = true;
		return min_i;
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
        System.out.println(minimumDistance(x, y));
    }
}

