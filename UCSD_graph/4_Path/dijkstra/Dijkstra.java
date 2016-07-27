import java.util.*;

public class Dijkstra {
	private static class DistComparator implements Comparator<Integer> {
		private static int[] dist;

		public DistComparator(int[] dist) {
			this.dist = dist;
		}

		public int compare(Integer i, Integer j) {
			return dist[i] - dist[j];
		}
	}

	private static final int INF = Integer.MAX_VALUE;

	private static int distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
		int nNode = adj.length;
		int[] dist = new int[nNode];

		// initialize distance array
		for (int i = 0; i < nNode; i++) {
			dist[i] = INF;
		}
		dist[s] = 0;

		PriorityQueue<Integer> pq 
			= new PriorityQueue<Integer>(nNode, new DistComparator(dist));

		// build priority queue, i.e., minimum heap
		for (int i = 0; i < nNode; i++) {
			pq.offer(i);
		}

		while (!pq.isEmpty()) {
			int node = pq.poll();
			int nNeibors = adj[node].size();
			for (int i = 0; i < nNeibors; i++) {
				int neibor = adj[node].get(i);
				int weight = cost[node].get(i);

				/*
				 * If dist[node] is INF, it means node
				 * is unreachable from src node
				 */
				if (dist[node] < INF) {
					int d = dist[node] + weight;
					if (d < dist[neibor]) {
						// relax
						dist[neibor] = d;

						// change priority
						if (pq.contains(neibor)) {
							pq.remove(neibor);
							pq.offer(neibor);
						}
					}
				}
			}
		}

		return dist[t] == INF ? -1 : dist[t];
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
		int x = scanner.nextInt() - 1;
		int y = scanner.nextInt() - 1;
		System.out.println(distance(adj, cost, x, y));
	}
}

