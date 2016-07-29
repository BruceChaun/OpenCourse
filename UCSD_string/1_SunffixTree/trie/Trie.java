import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {
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

    List<Map<Character, Integer>> buildTrie(String[] patterns) {
        List<Map<Character, Integer>> trie = new ArrayList<Map<Character, Integer>>();

        // write your code here

		// set root node
		Map<Character, Integer> root 
			= new HashMap<Character, Integer>();
		//root.put('\0', 0);
		trie.add(root);

		for (int i = 0; i < patterns.length; i++) {
			Map<Character, Integer> cur = trie.get(0);
			String p = patterns[i];
			int patLen = p.length();
			for (int j = 0; j < patLen; j++) {
				char cj = p.charAt(j);
				if (!cur.containsKey(cj)) {
					cur.put(cj, trie.size());
					Map<Character, Integer> node 
						= new HashMap<Character, Integer>();
					//node.put('\0', trie.size());
					trie.add(node);
				}
				cur = trie.get(cur.get(cj));
			}
		}

        return trie;
    }

    static public void main(String[] args) throws IOException {
        new Trie().run();
    }

    public void print(List<Map<Character, Integer>> trie) {
        for (int i = 0; i < trie.size(); ++i) {
            Map<Character, Integer> node = trie.get(i);
            for (Map.Entry<Character, Integer> entry : node.entrySet()) {
                System.out.println(i + "->" + entry.getValue() + ":" + entry.getKey());
            }
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        int patternsCount = scanner.nextInt();
        String[] patterns = new String[patternsCount];
        for (int i = 0; i < patternsCount; ++i) {
            patterns[i] = scanner.next();
        }
        List<Map<Character, Integer>> trie = buildTrie(patterns);
        print(trie);
    }
}
