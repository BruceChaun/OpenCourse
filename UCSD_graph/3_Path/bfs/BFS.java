import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class BFS {
	private static final int INF = Integer.MAX_VALUE;

    private static int distance(ArrayList<Integer>[] adj, int s, int t) {
        //write your code here
		int nNode = adj.length;

		int[] dist = new int[nNode];
		for (int i = 0; i< nNode; i++) {
			dist[i] = INF;
		}
	
		Queue<Integer> q = new LinkedList<Integer>(); 
		dist[s] = 0;
		q.add(s);

		while (!q.isEmpty()) {
			int node = q.poll();
			int nNeibors = adj[node].size();
			for (int i = 0; i < nNeibors; i++) {
				int neibor = adj[node].get(i);
				if (dist[neibor] == INF) {
					dist[neibor] = dist[node] + 1;
					q.add(neibor);
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
        System.out.println(distance(adj, x, y));
    }
}

