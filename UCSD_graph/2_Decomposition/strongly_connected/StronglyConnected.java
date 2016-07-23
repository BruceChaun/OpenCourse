import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class StronglyConnected {
	private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj) {
		//write your code here
		ArrayList<Integer>[] reverse_adj = reverse(adj);

		ArrayList<Integer> order = new ArrayList<Integer>();
		int nNode = adj.length;
		boolean[] visited = new boolean[nNode];

		for (int i = 0; i < nNode; i++) {
			if (!visited[i]) {
				dfs(reverse_adj, visited, order, i);
			}
		}

		int[] scc = new int[nNode];
		for (int i = 0; i < nNode; i++) {
			visited[i] = false;
			scc[i] = -1;
		}

		int nSCC = 0;
		for (int i = 0; i < nNode; i++) {
			int node = order.get(i);
			if (!visited[node]) {
				dfs(adj, visited, new ArrayList<Integer>(), node);
				nSCC++;
			}
		}

		return nSCC;
	}

	private static ArrayList<Integer>[] reverse(ArrayList<Integer>[] adj) {
		int nNode = adj.length;
		ArrayList<Integer>[] reverse_adj = 
			(ArrayList<Integer>[]) new ArrayList[nNode];

		for (int i = 0; i < nNode; i++) {
			reverse_adj[i] = new ArrayList<Integer>();
		}

		for (int i = 0; i < nNode; i++) {
			int nNeibors = adj[i].size();

			for (int j = 0; j < nNeibors; j++) {
				int node = adj[i].get(j);
				reverse_adj[node].add(i);
			}
		}

		return reverse_adj;
	}

	private static void dfs(ArrayList<Integer>[] adj, boolean[] visited, ArrayList<Integer> order, int s) {
		visited[s] = true;
		int nNode = adj[s].size();
		for (int i = 0; i < nNode; i++) {
			int node = adj[s].get(i);
			if (!visited[node]) {
				dfs(adj, visited, order, node);
			}
		}
		order.add(0, s);
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		for (int i = 0; i < n; i++) {
			adj[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < m; i++) {
			int x, y;
			x = scanner.nextInt();
			y = scanner.nextInt();
			adj[x - 1].add(y - 1);
		}
		System.out.println(numberOfStronglyConnectedComponents(adj));
	}
}

