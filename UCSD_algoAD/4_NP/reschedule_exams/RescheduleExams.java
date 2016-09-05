import java.util.Collection;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class RescheduleExams {

    class Edge {
        int u, v;
        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    private int color2int(char color) {
        if (color == 'R')
            return 0;
        if (color == 'G')
            return 1;
        return 2;
    }

    private char int2color(int i) {
        i = (i + 3) % 3;
        if (i == 0)
            return 'R';
        if (i == 1)
            return 'G';
        return 'B';
    }

    private void change(char[] colors, int i, boolean incr) {
        int index = color2int(colors[i]);
        if (incr)
            colors[i] = int2color(index + 1);
        else
            colors[i] = int2color(index - 1);
    }

    private boolean checkBall(List<Integer>[] adj, char[] colors, 
            int r, boolean incr, boolean[] changed) {
        int n = adj.length;

        // check satisfiability
        boolean ok = true;
        for (int i = 0; i < n; i++) {
            for (int j : adj[i]) {
                if (colors[i] == colors[j]) {
                    // both nodes have changed, so can't change any more
                    if (changed[i] && changed[j])
                        return false;
                    
                    // change either
                    if (!changed[j]) {
                        change(colors, j, incr);
                        changed[j] = true;
                    } else {
                        change(colors, i, incr);
                        changed[i] = true;
                    }

                    ok = false;
                    break;
                }
            }
            if (!ok)
                break;
        }

        if (ok)
            return true;

        if (r == 0)
            return false;

        if (checkBall(adj, colors, r - 1, incr, changed))
            return true;
        else
            return false;
    }

    char[] assignNewColors(int n, Edge[] edges, char[] colors) {
        // Insert your code here.
        /*if (n % 3 == 0) {
            char[] newColors = colors.clone();
            for (int i = 0; i < n; i++) {
                newColors[i] = "RGB".charAt(i % 3);
            }
            return newColors;
        } else {
            return null;
        }*/
        // DEEP COPY!!!
        char[] ori_colors = new char[n];
        for (int i = 0; i < n; i++)
            ori_colors[i] = colors[i];

        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }

        int nEdge = edges.length;
        for (int i = 0; i < nEdge; i++) {
            int u = edges[i].u - 1;
            int v = edges[i].v - 1;
            adj[u].add(v);
            adj[v].add(u);
        }

        for (int i = 0; i < n; i++) {
            colors[i] = int2color(color2int(ori_colors[i]) + 1);
        }
        if (checkBall(adj, colors, n / 2, true, new boolean[n]))
            return colors;

        for (int i = 0; i < n; i++) {
            colors[i] = int2color(color2int(ori_colors[i]) - 1);
        }
        if (checkBall(adj, colors, n / 2, false, new boolean[n]))
            return colors;

        return null;
    } 

    void run() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        scanner.nextLine();
        
        String colorsLine = scanner.nextLine();
        char[] colors = colorsLine.toCharArray();

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges[i] = new Edge(u, v);
        }

        char[] newColors = assignNewColors(n, edges, colors);

        if (newColors == null) {
            writer.println("Impossible");
        } else {
            writer.println(new String(newColors));
        }

        writer.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new RescheduleExams().run();
    }
}
