import java.util.ArrayList;
import java.util.Scanner;

public class Acyclicity {
	private static int[] pre;
	private static int[] post;
	private static int clock = 1;

	private static int acyclic(ArrayList<Integer>[] adj) {
		//write your code here
		int nNode = adj.length;
		pre = new int[nNode];
		for (int i = 0; i < nNode; i++) {
			pre[i] = -1;
		}

		post = new int[nNode];
		for (int i = 0; i < nNode; i++) {
			post[i] = -1;
		}

		boolean cycle = false;
		for (int i = 0; i < nNode; i++) {
			if (pre[i] < 0) {
				cycle = explore(i, adj) || cycle;
			}
		}

		/* for (int i = 0; i < nNode; i++) {
			System.out.println("pre/post: " + pre[i] + "/" + post[i]);
		}*/

		return cycle ? 1 : 0;
	}

	private static boolean explore(int x, ArrayList<Integer>[] adj) {
		//System.out.println("exploring node " + x + ", clock is " + clock);
		pre[x] = clock++;
		boolean cycle = false;
		int nNeibors = adj[x].size();

		for (int i = 0; i < nNeibors; i++) {
			int node = adj[x].get(i);
			if (pre[node] < 0) {
				cycle = explore(node, adj);
			} else if (post[node] < 0 && post[x] < 0) {
				cycle = true;
			}
		}
		post[x] = clock++;
		return cycle;
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
		System.out.println(acyclic(adj));
	}
}

