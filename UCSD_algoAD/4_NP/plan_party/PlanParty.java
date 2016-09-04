import java.io.*;
import java.util.*;

class Vertex {
    Vertex() {
        this.weight = 0;
        this.children = new ArrayList<Integer>();
    }

    int weight;
    ArrayList<Integer> children;
}

class PlanParty {
    static Vertex[] ReadTree() throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int vertices_count = (int) tokenizer.nval;

        Vertex[] tree = new Vertex[vertices_count];

        for (int i = 0; i < vertices_count; ++i) {
            tree[i] = new Vertex();
            tokenizer.nextToken();
            tree[i].weight = (int) tokenizer.nval;
        }

        for (int i = 1; i < vertices_count; ++i) {
            tokenizer.nextToken();
            int from = (int) tokenizer.nval;
            tokenizer.nextToken();
            int to = (int) tokenizer.nval;
            tree[from - 1].children.add(to - 1);
            tree[to - 1].children.add(from - 1);
        }

        return tree;
    }
    static int n = 0;

    static int dfs(Vertex[] tree, int vertex, int parent, int[] fun) {
        Vertex cur = tree[vertex];
        if (cur.children.isEmpty()) {
            fun[vertex] = cur.weight;
            return fun[vertex];
        }

        int plan1 = cur.weight, plan2 = 0;
        
        for (int child : cur.children) {
            if (child != parent) {
                for (int grandchild : tree[child].children) {
                    if (grandchild != vertex)
                        plan1 += dfs(tree, grandchild, child, fun);
                }
            }
        }

        for (int child : cur.children) {
            if (child != parent) {
                plan2 += dfs(tree, child, vertex, fun);
            }
        }

        fun[vertex] = plan1 > plan2 ? plan1 : plan2;
        return fun[vertex];
    }

    static int MaxWeightIndependentTreeSubset(Vertex[] tree) {
        int size = tree.length;
        if (size == 0)
            return 0;

        int[] fun = new int[size];
        return dfs(tree, 0, -1, fun);
    }

    public static void main(String[] args) throws IOException {
      // This is to avoid stack overflow issues
      new Thread(null, new Runnable() {
                    public void run() {
                        try {
                            new PlanParty().run();
                        } catch(IOException e) {
                        }
                    }
                }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        Vertex[] tree = ReadTree();
        int weight = MaxWeightIndependentTreeSubset(tree);
        System.out.println(weight);
    }
}
