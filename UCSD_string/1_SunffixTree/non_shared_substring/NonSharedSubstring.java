import java.io.*;
import java.math.*;
import java.util.*;

public class NonSharedSubstring implements Runnable {
    String solve (String p, String q) {
        // p is a substring of q, meaningless
        if (q.contains(p)) 
            return "";

        String text = p + "#" + q + "$";
        List<Map<String, Integer>> tree = buildSuffixTree(text);

        int treeSize = tree.size();

        // dfs to mark how many children contain '#' and their paths from root
        int[] num = new int[treeSize];
        String[] paths = new String[treeSize];
        paths[0] = "";
        boolean[] visited = new boolean[treeSize];
        dfs(tree, 0, visited, num, paths);

        // bfs to find the shortest (candidate) substrings
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(0);
        List<String> candidate = new ArrayList<String>();

        while (!queue.isEmpty()) {
            int n = queue.poll();
            String curPath = paths[n];
            Map<String, Integer> node = tree.get(n);
            int nKid = node.size();

            // all characters in string 1 are not in string 2
            if (n == 0 && nKid == num[n]) {
                String edge = node.entrySet().iterator().next().getKey();
                String firstChar = edge.substring(0, 1);
                return firstChar;
            }

            // look two layers down
            if (num[n] > 0) {
                for (Map.Entry<String, Integer> entry : node.entrySet()) {
                    String edge = entry.getKey();
                    int child = entry.getValue();

                    int nGrandchild = tree.get(child).size();
                    int pos = edge.indexOf('#');
                    if (nGrandchild == num[child] && 
                        (pos > 0 || num[child] > 0)) {
                        // all children in child only belong to string 1, 
                        // which forms a large leaf (conceptually)
                        String s = curPath + edge.substring(0, 1);
                        candidate.add(s);
                    } else if (num[child] > 0 || pos > 0)
                        queue.add(child); // still have substrings of string 1
                }
            }
        }

        // go through candidate to select the shortest one
        String result = "";
        for (String s : candidate) {
            if (result.equals("") || (s.length() < result.length()))
                result = s;
        }

        return result;
    }

    private void dfs(List<Map<String, Integer>> tree, int s, 
            boolean[] visited, int[] num, String[] paths) {
        visited[s] = true;
        Map<String, Integer> node = tree.get(s);
        String curPath = paths[s];

        int nKid = 0;
        for (Map.Entry<String, Integer> entry : node.entrySet()) {
            int child = entry.getValue();
            String edge = entry.getKey();

            paths[child] = curPath + edge;
            if (!visited[child]) {
                dfs(tree, child, visited, num, paths);
            }

            int nGrandchild = tree.get(child).size();
            if (edge.contains("#") || 
                    (nGrandchild > 0 && nGrandchild == num[child])) {
                nKid++;
                    }
        }
        num[s] = nKid;
    }

    private List<Map<String, Integer>> buildSuffixTree(String s) {
        int strLen = s.length();
        List<Map<String, Integer>> tree 
            = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> node = new HashMap<String, Integer>();
        tree.add(node);

        for (int i = strLen - 1; i >= 0; i--) {
            String suffix = s.substring(i);
            Map<String, Integer> cur = tree.get(0);

            while (true) {
                String child = find(cur, suffix);
                if (child.equals("")) {
                    cur.put(suffix, tree.size());
                    node = new HashMap<String, Integer>();
                    tree.add(node);
                    break;
                } else {
                    int preLen = match(child, suffix);

                    // shorten suffix
                    suffix = suffix.substring(preLen);
                    int curNode = cur.get(child);

                    // split child
                    if (preLen < child.length()) {
                        cur.remove(child);
                        cur.put(child.substring(0, preLen), curNode);
                        cur = tree.get(curNode);
                        cur.put(child.substring(preLen), tree.size());
                        node = new HashMap<String, Integer>();
                        tree.add(node);
                    } else {
                        cur = tree.get(curNode);
                    }
                }
            }
        }
        return tree;
    }

    private int match(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int len = len1 < len2 ? len1 : len2;
        int num = 0;

        for (int i = 0; i < len; i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                num ++;
            } else {
                break;
            }
        }
        return num;
    }

    private String find(Map<String, Integer> node, String suffix) {
        String s = "";
        for (Map.Entry<String, Integer> entry : node.entrySet()) {
            String key = entry.getKey();
            int size = match(key, suffix);
            if (size > 0) {
                s = key;
                break;
            }
        }
        return s;
    }

    public void run () {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
            String p = in.readLine ();
            String q = in.readLine ();

            String ans = solve (p, q);

            System.out.println (ans);
        }
        catch (Throwable e) {
            e.printStackTrace ();
            System.exit (1);
        }
    }

    public static void main (String [] args) {
        new Thread (new NonSharedSubstring ()).start ();
    }
}
