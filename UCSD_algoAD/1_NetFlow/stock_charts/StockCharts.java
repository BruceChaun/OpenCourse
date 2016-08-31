import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.Queue;

public class StockCharts {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new StockCharts().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i)
            for (int j = 0; j < numPoints; ++j)
                stockData[i][j] = in.nextInt();
        return stockData;
    }

    private int minCharts(int[][] stockData) {
        int numStocks = stockData.length;
        int numData = stockData[0].length;

        int gSize = numStocks * 2 + 2;
        int[][] adj = new int[gSize][gSize];
        for (int i = 1; i <= numStocks; i++) {
            adj[0][i] = 1;
            adj[numStocks + i][gSize - 1] = 1;
        }

        // add arrow (i, j) if stock[i] is strictly smaller than stock[j]
        for (int i = 1; i <= numStocks; i++) {
            for (int j = 1; j <= numStocks; j++) {
                if (i != j && compare(stockData[i-1], stockData[j-1]))
                    adj[i][numStocks + j] = 1;
            }
        }

        int flow = 0;
        while (true) {
            int g = bfs(adj);
            if (g > 0) flow += g;
            else break;
        }

        return numStocks - flow;
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

        while (!q.isEmpty()) {
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

    /*
     * this is original codes, useless
     */
    private int minCharts0(int[][] stockData) {
        // Replace this incorrect greedy algorithm with an
        // algorithm that correctly finds the minimum number
        // of charts on which we can put all the stock data
        // without intersections of graphs on one chart.

        int numStocks = stockData.length;
        // Each chart is a list of indices of its stocks.
        List<List<Integer>> charts = new ArrayList<>();
        for (int i = 0; i < numStocks; ++i) {
            boolean added = false;
            for (List<Integer> chart : charts) {
                boolean canAdd = true;
                for (int index : chart)
                    if (!compare(stockData[i], stockData[index]) &&
                        !compare(stockData[index], stockData[i])) {
                        canAdd = false;
                        break;
                    }
                if (canAdd) {
                    added = true;
                    chart.add(i);
                    break;
                }
            }
            if (!added) {
                List<Integer> newChart = new ArrayList<Integer>();
                newChart.add(i);
                charts.add(newChart);
            }
        }
        return charts.size();
    }

    boolean compare(int[] stock1, int[] stock2) {
        for (int i = 0; i < stock1.length; ++i)
            if (stock1[i] >= stock2[i])
                return false;
        return true;
    }

    private void writeResponse(int result) {
        out.println(result);
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
