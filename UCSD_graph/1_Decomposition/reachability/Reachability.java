import java.util.ArrayList;
import java.util.Scanner;

public class Reachability {
	private static boolean[] visited;

    private static int reach(ArrayList<Integer>[] adj, int x, int y) {
        //write your code here
		
		int nNode = adj.length;
		visited = new boolean[nNode];

		// mark all the nodes as unvisited
		for (int i = 0; i < nNode; i++) {
			visited[i] = false;
		}

		// start to explore at node x
		explore(x, adj);
        return visited[y] ? 1 : 0;
    }

	private static void explore(int x, ArrayList<Integer>[] adj) {
		visited[x] = true;
		int nNeibors = adj[x].size();

		for (int i = 0; i < nNeibors; i++) {
			int node = adj[x].get(i);
			if (!visited[node]) {
				explore(node, adj);
			}
		}
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
            adj[y - 1].add(x - 1);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(reach(adj, x, y));
    }
}

