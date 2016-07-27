import java.util.*;

public class ShortestPaths {

	private static void shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, long[] distance, int[] reachable, int[] shortest) {
		//write your code here
		int nNode = adj.length;
		distance[s] = 0;
		reachable[s] = 1;

		Queue<Integer> q = new LinkedList<Integer>();

		for (int i = 0; i < nNode; i++) {
			for (int node = 0; node < nNode; node++) {
				int nNeibors = adj[node].size();
				for (int j = 0; j < nNeibors; j++) {
					int neibor = adj[node].get(j);
					int weight = cost[node].get(j);

					//if (distance[node] < Long.MAX_VALUE) {
					if (reachable[node] == 1) {
						reachable[neibor] = 1;
						long d = distance[node] + weight;
						if (d < distance[neibor]) {
							distance[neibor] = d;
							if (i == nNode - 1) {
								q.add(neibor);
							}
						}
					}
				}
			}
		}

		BFS(adj, q, shortest);
	}

	private static void BFS(ArrayList<Integer>[] adj,
			Queue<Integer> q, int[] shortest) {
		while (!q.isEmpty()) {
			int node = q.poll();
			shortest[node] = 0;
			for (Integer neibor : adj[node]) {
				if (shortest[neibor] == 1) {
					q.add(neibor);
				}
			}
		}
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
		int s = scanner.nextInt() - 1;
		long distance[] = new long[n];
		int reachable[] = new int[n];
		int shortest[] = new int[n];
		for (int i = 0; i < n; i++) {
			distance[i] = Long.MAX_VALUE;
			reachable[i] = 0;
			shortest[i] = 1;
		}
		shortestPaths(adj, cost, s, distance, reachable, shortest);
		for (int i = 0; i < n; i++) {
			if (reachable[i] == 0) {
				System.out.println('*');
			} else if (shortest[i] == 0) {
				System.out.println('-');
			} else {
				System.out.println(distance[i]);
			}
		}
	}

}

