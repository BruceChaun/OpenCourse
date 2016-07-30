import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

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

	private final char SENTINEL = '$';

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding 
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        List<String> result = new ArrayList<String>();
        // Implement this function yourself

		if (text.charAt(text.length()-1) != SENTINEL) {
			text += SENTINEL;
		}
		int textLen = text.length();

		List<Map<String, Integer>> tree 
			= new ArrayList<Map<String, Integer>>();
		Map<String, Integer> node = new HashMap<String, Integer>();
		tree.add(node);

		for (int i = textLen - 1; i >= 0; i--) {
			String suffix = text.substring(i);
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
					cur.remove(child);
					cur.put(child.substring(0, preLen), curNode);

					cur = tree.get(curNode);

					if (preLen < child.length()) {
						cur.put(child.substring(preLen), tree.size());
						node = new HashMap<String, Integer>();
						tree.add(node);
					}
				}
			}
		}
		

		// use bfs to write into result
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(0);

		while (!q.isEmpty()) {
			int n = q.poll();
			
			for (Map.Entry<String, Integer> entry : tree.get(n).entrySet()) {
				result.add(entry.getKey());
				q.add(entry.getValue());
			}
		}

        return result;
    }

	/*
	 * match() compares the longest prefixes of two strings.
	 *
	 * @return an integer indicating how many characters are matched.
	 *
	 */
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

	/*
	 * find() finds a child of current node, which match() the 
	 * given suffix.
	 *
	 * @return the matched sub-string.
	 */
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

    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        print(edges);
    }
}
