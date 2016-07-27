import java.util.ArrayList;
import java.util.Scanner;

public class NegativeCycle {
	private static final int INF = Integer.MAX_VALUE;

    private static int negativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        // write your code here
		int nNode = adj.length;
		int[] dist = new int[nNode];
		boolean flag = false;

		int i;
		for (i = 0; i < nNode; i++) {
			dist[i] = INF;
		}
		dist[0] = 0;// 0 as src

		for (i = 0; i < nNode; i++) {
			for (int node = 0; node < nNode; node++) {
				int nNeibors = adj[node].size();
				for (int j = 0; j < nNeibors; j++) {
					int neibor = adj[node].get(j);
					int weight = cost[node].get(j);

					if (dist[node] < INF) {
						int d = dist[node] + weight;
						if (d < dist[neibor]) {
							dist[neibor] = d;
							if (i == nNode - 1) {
								flag = true;
							}
						}
					}
				}
			}
		}
        return flag ? 1 : 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        System.out.println(negativeCycle(adj, cost));
    }
}

