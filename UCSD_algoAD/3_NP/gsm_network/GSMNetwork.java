import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

public class GSMNetwork {
    private final InputReader reader;
    private final OutputWriter writer;
    private final int COLORSIZE = 3;

    public GSMNetwork(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new GSMNetwork(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class ConvertGSMNetworkProblemToSat {
        int numVertices;
        Edge[] edges;

        ConvertGSMNetworkProblemToSat (int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }

            // add exact one of x[i][j]
            for (int i = 1; i <= n; i++) {
                int xi0 = index(i, 0);
                int xi1 = index(i, 1);
                int xi2 = index(i, 2);
                clauses.add(new int[]{xi0, xi1, xi2});
                clauses.add(new int[]{-xi0, -xi1});
                clauses.add(new int[]{-xi1, -xi2});
                clauses.add(new int[]{-xi2, -xi0});
            }
        }

        List<int[]> clauses = new ArrayList<int[]>();

        void printEquisatisfiableSatFormula() {
            // This solution prints a simple satisfiable formula
            // and passes about half of the tests.
            // Change this function to solve the problem.
            /*writer.printf("3 2\n");
            writer.printf("1 2 0\n");
            writer.printf("-1 -2 0\n");
            writer.printf("1 -2 0\n");*/

            boolean hasSol = false;
            int fields =(int)  Math.pow(2, COLORSIZE * numVertices);
            for (int i = 1; i <= fields; i++) {
                if (verify(i)) {
                    hasSol = true;
                    break;
                }
            }

            if (hasSol) {
                writer.printf("SATISFIABLE\n");
                writer.printf("1 -1 0\n");
            } else {
                writer.printf("UNSATISFIABLE\n");
                writer.printf("1 0\n");
                writer.printf("-1 0\n");
            }
        }

        boolean verify(int input) {
            boolean[][] x = new boolean[numVertices][COLORSIZE];
            boolean result = true;

            // convert decimal input value to boolean x
            for (int i = 1; i <= numVertices * COLORSIZE; i++) {
                if (input == 0)
                    break;

                if ((input & 1) == 1) {
                    int[] node_color = reverseIndex(i);
                    x[node_color[0]-1][node_color[1]] = true;
                }
                input = input >> 1;
            }

            int nClause = clauses.size();
            for (int i = 0; i < nClause; i++) {
                boolean clauseRes = false;
                int[] clause = clauses.get(i);
                for (int j = 0; j < clause.length; j++) {
                    int index = clause[j];
                    if (index > 0) {
                        int[] node_color = reverseIndex(index);
                        clauseRes = clauseRes || x[node_color[0]-1][node_color[1]];
                    } else {
                        int[] node_color = reverseIndex(-index);
                        clauseRes = clauseRes || (!x[node_color[0]-1][node_color[1]]);
                    }
                }
                result = result && clauseRes;
            }
            return result;
        }

        int index(int i, int j) {
            return COLORSIZE * (i - 1) + j + 1;
        }

        int[] reverseIndex(int index) {
            index--;
            return new int[]{index / COLORSIZE + 1, index % COLORSIZE};
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertGSMNetworkProblemToSat  converter = new ConvertGSMNetworkProblemToSat (n, m);
        for (int i = 0; i < m; ++i) {
            int from = reader.nextInt();
            int to = reader.nextInt();
            converter.edges[i].from = from;
            converter.edges[i].to = to;

            // add (!from | !to)
            for (int j = 0; j < COLORSIZE; j++) {
                int fromIndex = converter.index(from, j);
                int toIndex = converter.index(to, j);
                //converter.clauses.add(new int[]{fromIndx, toIndex});
                converter.clauses.add(new int[]{-fromIndex, -toIndex});
            }
        }

        converter.printEquisatisfiableSatFormula();
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
