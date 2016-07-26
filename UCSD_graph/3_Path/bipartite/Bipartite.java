import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {
    private static int bipartite(ArrayList<Integer>[] adj) {
        //write your code here
		int nNode = adj.length;

		// 0 for uncolored, 1 and 2 for two parties
		int[] colors = new int[nNode];
		boolean flag = false;

		Queue<Integer> q = new LinkedList<Integer>();
		int s = 0;
		q.add(s);
		colors[s] = 1;

		while (!q.isEmpty() && !flag) {
			int node = q.poll();
			int nNeibors = adj[node].size();
			for (int i = 0; i < nNeibors; i++) {
				int neibor = adj[node].get(i);
				if (colors[neibor] == 0) {
					colors[neibor] = 3 - colors[node];
					q.add(neibor);
				} else if (colors[neibor] == colors[node]) {
					flag = true;
				}
			}
		}

        return flag ? 0 : 1;
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
        System.out.println(bipartite(adj));
    }
}

