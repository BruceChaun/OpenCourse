import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Toposort {
    private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
		int nNode = adj.length;
        int used[] = new int[nNode];
        ArrayList<Integer> order = new ArrayList<Integer>();
        //write your code here
		for (int i = 0; i < nNode; i++) {
			if (used[i] == 0)
				dfs(adj, used, order, i);
		}

        return order;
    }

    private static void dfs(ArrayList<Integer>[] adj, int[] used, ArrayList<Integer> order, int s) {
      //write your code here
	  used[s] = 1;
	  int nNode = adj[s].size();
	  for (int i = 0; i < nNode; i++) {
		  int node = adj[s].get(i);
		  if (used[node] == 0) {
			  dfs(adj, used, order, node);
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
        ArrayList<Integer> order = toposort(adj);
        for (int x : order) {
            System.out.print((x + 1) + " ");
        }
    }
}

