import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CircuitDesign {
    private final InputReader reader;
    private final OutputWriter writer;

    public CircuitDesign(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CircuitDesign(reader, writer).run();
        writer.writer.flush();
    }

    class Clause {
        int firstVar;
        int secondVar;
    }

    class TwoSatisfiability {
        int numVars;
        Clause[] clauses;

        TwoSatisfiability(int n, int m) {
            numVars = n;
            clauses = new Clause[m];
            for (int i = 0; i < m; ++i) {
                clauses[i] = new Clause();
            }
        }

        boolean isSatisfiable(int[] result) {
            List<Integer>[] adj = convert2graph();
            List<List<Integer>> scc = SCC(adj);
            if (!check(scc)) 
                return false;

            List<List<Integer>> topoOrder = topoSort(adj, scc);
            assign(scc, result);
            return true;
        }

        private int literal2index(int literal) {
            if (literal > 0)
                return literal;
            return -literal + numVars;
        }

        private int index2literal(int index) {
            if (index <= numVars)
                return index;
            return numVars - index;
        }

        private List<Integer>[] convert2graph() {
            List<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[numVars * 2 + 1];
            for (int i = 1; i < adj.length; i++)
                adj[i] = new ArrayList<Integer>();

            int nClause = clauses.length;

            for (int i = 0; i < nClause; i++) {
                int node1 = literal2index(clauses[i].firstVar);
                int node2 = literal2index(clauses[i].secondVar);
                if (clauses[i].firstVar > 0) {
                    adj[literal2index(-clauses[i].firstVar)].add(node2);
                } else {
                    adj[-clauses[i].firstVar].add(node2);
                }
                if (clauses[i].secondVar > 0) {
                    adj[literal2index(-clauses[i].secondVar)].add(node1);
                } else {
                    adj[-clauses[i].secondVar].add(node1);
                }
            }

            return adj;
        }

        private List<List<Integer>> SCC(List<Integer>[] adj) {
            int adjSize = adj.length;
            boolean[] visited = new boolean[adjSize];
            int[] dfn = new int[adjSize];
            int[] low = new int[adjSize];
            boolean[] inStack = new boolean[adjSize];
            List<List<Integer>> scc = new ArrayList<List<Integer>>();
            Stack stack = new Stack();
            for (int i = 1; i < adjSize; i++) {
                if (!visited[i]) {
                    int index = scc.size() + 1;
                    tarjan(adj, i, visited, dfn, low, index, inStack, stack, scc);
                }
            }
            return scc;
        }

        private void tarjan(List<Integer>[] adj, int s, boolean[] visited, 
                int[] dfn, int[] low, int index, boolean[] inStack, 
                Stack stack, List<List<Integer>> scc) {
            dfn[s] = index;
            low[s] = index;
            index++;
            visited[s] = true;
            inStack[s] = true;
            stack.push(s);
            int nNeibor = adj[s].size();
            
            for (int i = 0; i < nNeibor; i++) {
                int neibor = adj[s].get(i);
                if (!visited[neibor]) {
                    tarjan(adj, neibor, visited, dfn, low, index, inStack, stack, scc);
                    if (low[s] > low[neibor]) 
                        low[s] = low[neibor];
                } else if (inStack[neibor]) {
                    if (low[s] > dfn[neibor])
                        low[s] = dfn[neibor];
                }
            }

            if (dfn[s] == low[s]) {
                List<Integer> component = new ArrayList<Integer>();
                int v;
                do {
                    Integer i = (Integer) stack.pop();
                    v = i.intValue();
                    inStack[v] = false;
                    component.add(v);
                } while (s != v);
                scc.add(component);
            }
        }

        private boolean check(List<List<Integer>> scc) {
            int sccSize = scc.size();
            for (int i = 0; i < sccSize; i++) {
                boolean[] listed = new boolean[numVars * 2 + 1];
                for (int node : scc.get(i)) {
                    if (listed[literal2index(-index2literal(node))])
                        return false;
                    listed[node] = true;
                }
            }
            return true;
        }

        private List<List<Integer>> topoSort(List<Integer>[] adj, List<List<Integer>> scc) {
            List<List<Integer>> topoOrder = new ArrayList<List<Integer>>();
            int sccSize = scc.size();
            for (int i = 0; i < sccSize; i++) {
                List<Integer> order = new ArrayList<Integer>();
                boolean[] visited = new boolean[adj.length];
                dfs(adj, scc.get(i).get(0), visited, order);
                topoOrder.add(order);
            }
            return topoOrder;
        }

        private void dfs(List<Integer>[] adj, int s, boolean[] visited, List<Integer> order) {
            visited[s] = true;
            for (int neibor : adj[s]) {
                if (!visited[neibor]) {
                    dfs(adj, neibor, visited, order);
                }
            }
            order.add(s);
        }

        private void assign(List<List<Integer>> scc, int[] result) {
            int[] values = new int[numVars * 2 + 1];
            for (int i = 0; i < values.length; i++) 
                values[i] = -1; // unassigned

            for (List<Integer> component : scc) {
                for (int index : component) {
                    if (values[index] == -1) {
                        values[index] = 1;
                        int complement = literal2index(-index2literal(index));
                        values[complement] = 0;
                    }
                }
            }

            for (int i = 0; i < result.length; i++) {
                result[i] = values[i+1];
            }
        }

        /*
         * This is the original code, ignore it.
         */
        boolean isSatisfiable0(int[] result) {
            // This solution tries all possible 2^n variable assignments.
            // It is too slow to pass the problem.
            // Implement a more efficient algorithm here.
            for (int mask = 0; mask < (1 << numVars); ++mask) {
                for (int i = 0; i < numVars; ++i) {
                    result[i] = (mask >> i) & 1;
                }

                boolean formulaIsSatisfied = true;

                for (Clause clause: clauses) {
                    boolean clauseIsSatisfied = false;
                    if ((result[Math.abs(clause.firstVar) - 1] == 1) == (clause.firstVar < 0)) {
                        clauseIsSatisfied = true;
                    }
                    if ((result[Math.abs(clause.secondVar) - 1] == 1) == (clause.secondVar < 0)) {
                        clauseIsSatisfied = true;
                    }
                    if (!clauseIsSatisfied) {
                        formulaIsSatisfied = false;
                        break;
                    }
                }

                if (formulaIsSatisfied) {
                    return true;
                }
            }
            return false;
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        TwoSatisfiability twoSat = new TwoSatisfiability(n, m);
        for (int i = 0; i < m; ++i) {
            twoSat.clauses[i].firstVar = reader.nextInt();
            twoSat.clauses[i].secondVar = reader.nextInt();
        }

        int result[] = new int[n];
        if (twoSat.isSatisfiable(result)) {
            writer.printf("SATISFIABLE\n");
            for (int i = 1; i <= n; ++i) {
                if (result[i-1] == 1) {
                    writer.printf("%d", i);
                } else {
                    writer.printf("%d", -i);
                }
                if (i < n) {
                    writer.printf(" ");
                } else {
                    writer.printf("\n");
                }
            }
        } else {
            writer.printf("UNSATISFIABLE\n");
        }
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
