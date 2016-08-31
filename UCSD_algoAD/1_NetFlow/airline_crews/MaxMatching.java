import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.Queue;

public class MaxMatching {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new MaxMatching().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        boolean[][] bipartiteGraph = readData();
        int[] matching = findMatching(bipartiteGraph);
        writeResponse(matching);
        out.close();
    }

    boolean[][] readData() throws IOException {
        int numLeft = in.nextInt();
        int numRight = in.nextInt();
        boolean[][] adjMatrix = new boolean[numLeft][numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                adjMatrix[i][j] = (in.nextInt() == 1);
        return adjMatrix;
    }

    private int[] findMatching(boolean[][] bipartiteGraph) {
        // Replace this code with an algorithm that finds the maximum
        // matching correctly in all cases.
        
        /*int numLeft = bipartiteGraph.length;
        int numRight = bipartiteGraph[0].length;

        int[] matching = new int[numLeft];
        Arrays.fill(matching, -1);
        boolean[] busyRight = new boolean[numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                if (bipartiteGraph[i][j] && matching[i] == -1 && !busyRight[j]) {
                    matching[i] = j;
                    busyRight[j] = true;
                }
                */

        /*
         * add nodes source and sink and convert bipartiteGraph
         * to network flow. Use adjacent matrix to store edges 
         * for convenience.
         */
        int nFlight = bipartiteGraph.length;
        int nCrew = bipartiteGraph[0].length;
        int gSize = nFlight + nCrew + 2;
        int[][] graph = new int[gSize][gSize];

        for (int i = 1; i <= nFlight; i++) 
            graph[0][i] = 1;
        
        for (int i = nFlight + 1; i < gSize - 1; i++)
            graph[i][gSize-1] = 1;

        for (int i = 0; i < nFlight; i++) {
            for (int j = 0; j < nCrew; j++) {
                if (bipartiteGraph[i][j]) 
                    graph[i+1][nFlight+1+j] = 1;
            }
        }

        while (bfs(graph) > 0) ;

        int[] matching = new int[nFlight];
        for (int i = 0; i < nFlight; ++i)
            matching[i] = -1;
        boolean[] picked = new boolean[nCrew];
        /*
         * compare augemented graph and original bipartiteGraph
         */
        for (int i = 0; i < nFlight; i++) {
            for (int j = 0; j < nCrew; j++) {
                if (bipartiteGraph[i][j] && !picked[j] 
                    && graph[i+1][nFlight+1+j] == 0) {
                        matching[i] = j;
                        picked[j] = true;
                        break;
                    }
            }
        }
        return matching;
    }

    private int bfs(int[][] adj) {
        int size = adj.length;
        boolean[] visited = new boolean[size];
        int[] prev = new int[size];
        prev[0] = -1;
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(0);
        visited[0] = true;

        boolean existsPath = false;

        while(!q.isEmpty()) {
            int node = q.poll();
            for (int i = 0; i < size; i++) {
                if (adj[node][i] > 0 && !visited[i]) {
                    prev[i] = node;
                    visited[i] = true;
                    if (i == size - 1) {
                        existsPath = true;
                        q.clear();
                        break;
                    } else {
                        q.add(i);
                    }
                }
            }
        }

        if (!existsPath)
            return 0;

        int node = size - 1;
        int min = Integer.MAX_VALUE;
        while (prev[node] != -1) {
            int c = adj[prev[node]][node];
            if (c < min) min = c;
            node = prev[node];
        }

        node = size - 1;
        while (prev[node] != -1) {
            adj[prev[node]][node] -= min;
            adj[node][prev[node]] += min;
            node = prev[node];
        }

        return min;
    }

    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i] + 1);
            }
        }
        out.println();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
