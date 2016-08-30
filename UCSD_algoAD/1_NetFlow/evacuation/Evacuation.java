import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Evacuation {
    private static FastScanner in;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();

        FlowGraph graph = readGraph();
        System.out.println(maxFlow(graph, 0, graph.size() - 1));
    }

    private static int maxFlow(FlowGraph graph, int from, int to) {
        int flow = 0;
        /* your code goes here */

        boolean flag = true;
        int nVertex = graph.size();
        while (flag) {
            boolean[] visited = new boolean[nVertex];
            int[] prev = new int[nVertex];
            prev[0] = -1;
            int g = BFS(graph, visited, prev);
            if (g > 0) flow += g;
            else flag = false;
        }

        return flow;
    }

    /*
     * BFS() breadthfirstsearches the graph from the first vertex, finds 
     * the shortest path and updates the flows of edges in the path if 
     * any, otherwise returns 0.
     */
    private static int BFS(FlowGraph graph, boolean[] visited, int[] prev) {
        boolean existsPath = false;
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(0);
        visited[0] = true;

        while (!q.isEmpty()) {
            int node = q.poll();
            //System.out.println("visiting " + node);
            List<Integer> list = graph.getIds(node);
            int nEdge = list.size();
            //System.out.println(node + " has " + nEdge + " linked lists");
            for (int i = 0; i < nEdge; i++) {
                int edgeNum = list.get(i);
                Edge e = graph.getEdge(edgeNum);
                //System.out.println(edgeNum + "\tto " + e.to + ", has capacity " + e.capacity);
                if (e.capacity > e.flow && !visited[e.to]) {
                    prev[e.to] = e.from;
                    visited[e.to] = true;
                    if (e.to == graph.size() - 1) {
                        existsPath = true;
                        q.clear();
                        break;
                    } else {
                        q.add(e.to);
                        //System.out.println("added " + e.to);
                    }
                }
            }
        }

        //System.out.println(existsPath);
        if (!existsPath)
            return 0;

        int node = graph.size() - 1;
        int minEdge = Integer.MAX_VALUE;
        while (prev[node] != -1) {
            int c = graph.getCapacity(prev[node], node);
            if (minEdge > c)
                minEdge = c;
            node = prev[node];
        }
        //System.out.println("min edge " + minEdge);

        node = graph.size() - 1;
        while (prev[node] != -1) {
            int edgeId = graph.getId(prev[node], node);
            if (edgeId == -1) {
                System.err.println("no such edge");
                break;
            }
            //System.out.println(edgeId + "\t" + prev[node] + " -> " + node);
            graph.addFlow(edgeId, minEdge);
            node = prev[node];
        }
        return minEdge;
    }

    static FlowGraph readGraph() throws IOException {
        int vertex_count = in.nextInt();
        int edge_count = in.nextInt();
        FlowGraph graph = new FlowGraph(vertex_count);

        for (int i = 0; i < edge_count; ++i) {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, capacity = in.nextInt();
            graph.addEdge(from, to, capacity);
        }

        /*System.out.println();
        int size = graph.size();
        for (int i = 0; i < size; i++) {
            List<Integer> list = graph.getIds(i);
            System.out.println("checking " + i);
            for (int id : list) {
                Edge e = graph.getEdge(id);
                //if (e.capacity > 0)
                System.out.println(id + "\t" + e.from + "\t->\t" + e.to + "\t" + e.capacity);
            }
        }
        System.out.println();*/
        return graph;
    }

    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    /* This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly. */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[])new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int capacity) {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. */
            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) {
            /* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
             * due to the described above scheme. On the other hand, when we have to get a "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through! */
            //System.out.println(id + "\t" + edges.get(id).flow);
            //System.out.println((id ^ 1) + "\t" + edges.get(id ^ 1).flow);
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
            //System.out.println(id + "\t" + edges.get(id).flow);
            //System.out.println((id ^ 1) + "\t" + edges.get(id ^ 1).flow);
        }

        /*
         * given edge (from, to), return the residual capacity
         */
        public int getCapacity(int from, int to) {
            List<Integer> list = getIds(from);
            for (int i = 0; i < list.size(); i++) {
                Edge e = getEdge(list.get(i));
                if (e.to == to)
                    return e.capacity - e.flow;
            }
            return 0;
        }

        /*
         * given edge (from, to), return its id
         */
        public int getId(int from, int to) {
            List<Integer> list = getIds(from);
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i);
                Edge e = getEdge(id);
                if (e.to == to)
                    return id;
            }
            return -1;
        }
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
