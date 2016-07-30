import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	public int next [];

	Node ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
	}
}

public class TrieMatching implements Runnable {
	int letterToIndex (char letter)
	{
		switch (letter)
		{
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			case 'T': return 3;
			default: assert (false); return Node.NA;
		}
	}

	private final char SENTINEL = '$';

	List <Integer> solve (String text, int n, List <String> patterns) {
		List <Integer> result = new ArrayList <Integer> ();

		// write your code here
		int patLen = patterns.size();
		String[] patterns_ext = new String[patLen];

		for (int i = 0; i < patLen; i++) {
			patterns_ext[i] = patterns.get(i) + SENTINEL;
		}
		List<Map<Character, Integer>> trie = buildTrie(patterns_ext);

		int k = 0;
		while (k < text.length()) {
			int pos = prefixTrieMatching(text.substring(k), trie);
			if (pos >= 0) {
				result.add(k + pos);
			}
			k++;
		}

		return result;
	}

	List<Map<Character, Integer>> buildTrie(String[] patterns) {
		List<Map<Character, Integer>> trie = new ArrayList<Map<Character, Integer>>();

		Map<Character, Integer> root 
			= new HashMap<Character, Integer>();
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
					trie.add(node);
				}
				cur = trie.get(cur.get(cj));
			}
		}

		return trie;
	}

	int prefixTrieMatching(String text, List<Map<Character, Integer>> trie) {
		int pos = 0;
		int i = 0;
		char symbol = text.charAt(i);
		Map<Character, Integer> v = trie.get(0);

		while (true) {
			if (v.containsKey(SENTINEL)) {
				// v is a leaf, end of path
				break;
			} else {
				if (v.containsKey(symbol)) {
					int next = v.get(symbol);
					if (i < text.length() - 1)
						symbol = text.charAt(++i);
					else 
						symbol = '\0';
					v = trie.get(next);
				} else {
					pos = -1;
					break;
				}
			}
		}

		return pos;
	}

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String text = in.readLine ();
			int n = Integer.parseInt (in.readLine ());
			List <String> patterns = new ArrayList <String> ();
			for (int i = 0; i < n; i++) {
				patterns.add (in.readLine ());
			}

			List <Integer> ans = solve (text, n, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatching ()).start ();
	}
}
