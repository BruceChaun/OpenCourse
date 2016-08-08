import java.io.*;
import java.util.*;

/*
 * Build suffix array and then transform it into suffix tree
 */
public class SuffixTree {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    private SuffixTreeNode root;
    private int size = 0;  // number of all the nodes created

    public static void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        SuffixArray sa = new SuffixArray();
        int[] suffixArray = sa.buildSuffixArray(text);
        int[] lcpArray = sa.buildLCPArray(text, suffixArray);

        SA2ST(text, suffixArray, lcpArray);
        validate(text);

        boolean[] visited = new boolean[size + 1];
        String[] suffix = new String[text.length()];
        dfs(root, visited, suffix, text, "", text.length());

        System.out.println();
        for (int i = 0; i < text.length(); i++) {
            System.out.println(i + "\t" + suffix[i] + "\t" + text.substring(i).equals(suffix[i]));
        }
    }

    private void SA2ST(String text, int[] suffixArray, int[] lcpArray) {
        char[] chars = text.toCharArray();
        int len = text.length();

        root = new SuffixTreeNode(0, -1, -1, null);
        // record the starting node in the next iteration
        SuffixTreeNode curNode = root;
        // record the number of characters from root to curNode
        int dist = 0;
        // number of internal node
        int nInternal = 0;

        // add first suffix in suffix array
        int pos = suffixArray[0];
        createNewNode(root, chars[pos], pos, len, pos + 1);

        for (int i = 1; i < len; i++) {
            pos = suffixArray[i];
            int lcp = lcpArray[i - 1];

            while (lcp < dist) {
                dist -= curNode.edgeLength();
                curNode = curNode.parent;
            }

            if (dist == lcp) {
                int start = pos + lcp;
                createNewNode(curNode, chars[start], start, len, pos + 1);
            } else {
                nInternal--;
                int start = suffixArray[i - 1] + dist;
                SuffixTreeNode splitNode = curNode.children.get(chars[start]);
                start = splitNode.start;
                int offset = lcp - dist;

                SuffixTreeNode internal = 
                    splitEdge(splitNode, start + offset, chars, nInternal);
                createNewNode(internal, chars[pos + lcp], pos + lcp, len, pos + 1);

                curNode = internal;
                dist += curNode.edgeLength();
            }
        }
    }

    /*
     * createNewNode() creates a new node under @parent. 
     *
     * @params parent, to which node the new node should be added
     * key, linking parent and new node
     * start, the position the edge starts
     * textLen, the length of the whole text
     * nodeID, ID of the new node
     */
    private void createNewNode(SuffixTreeNode parent, char key, int start, 
            int textLen, int nodeID) {
        SuffixTreeNode node = new SuffixTreeNode(nodeID, start, textLen - 1, parent);
        parent.children.put(key, node);
        size++;
    }

    /*
     * splitEdge() splits the edge into two parts.
     *
     * @params node is the node to be splited,
     * splitPoint is the point where to split the edge,
     * text is the char array of the input string,
     * nInternal records how many internal nodes have been created.
     */
    private SuffixTreeNode splitEdge(SuffixTreeNode node, int splitPoint, 
            char[] text, int nInternal) {
        SuffixTreeNode mid = 
            new SuffixTreeNode(nInternal, node.start, splitPoint - 1, node.parent);
        mid.children.put(text[splitPoint], node);
        node.parent.children.put(text[node.start], mid);
        node.parent = mid;
        node.start = splitPoint;

        size++;
        return mid;
    }

    /*
     * validate() checks all the edges in the suffix tree.
     */
    private void validate(String text) {
        int len = text.length();

        List<String> result = new ArrayList<String>();
        Queue<SuffixTreeNode> q = new LinkedList<SuffixTreeNode>();
        q.add(root);
        while (!q.isEmpty()) {
            SuffixTreeNode n = q.poll();

            for (Map.Entry<Character, SuffixTreeNode> entry : n.children.entrySet()) {
                SuffixTreeNode node = entry.getValue();
                result.add(text.substring(node.start, node.end+1));
                q.add(node);
                System.out.println(n.node + "\t--\t" + node.node);
            }
        }

        for (String s : result) {
            System.out.println(s);
        }
    }

    /*
     * dfs() walks along all the paths from root to leaf, cancatenates and stores
     * all the substrings along the route.
     */
    private void dfs(SuffixTreeNode node, boolean[] visited, String[] suffix, 
            String text, String s, int len) {
        int id = node.node;
        if (id < 0)
            id = len - id;
        visited[id] = true;

        if (node.children.size() == 0) {
            suffix[id - 1] = s;
            return;
        }

        for (Map.Entry<Character, SuffixTreeNode> entry : node.children.entrySet()) {
            SuffixTreeNode child = entry.getValue();
            id = child.node;
            if (id < 0)
                id = len - id;
            if (!visited[id]) {
                dfs(child, visited, suffix, text, 
                        s + text.substring(child.start, child.end+1), len);
            }
        }
    }
}
