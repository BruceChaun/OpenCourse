import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KnuthMorrisPratt {
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

    // Find all the occurrences of the pattern in the text and return
    // a list of all positions in the text (starting from 0) where
    // the pattern starts in the text.
    public List<Integer> findPattern(String pattern, String text) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        // Implement this function yourself

        String t = pattern + "$" + text;
        int[] s = prefix(t);
        
        int pLen = pattern.length();
        int tLen = t.length();
        for (int i = pLen+1; i < tLen; i++) {
            if (s[i] == pLen) {
                result.add(i - pLen * 2);
            }
        }

        return result;
    }

    private int[] prefix(String text) {
        int len = text.length();
        char[] p = text.toCharArray();
        int[] s = new int[len];

        s[0] = 0;
        int border = 0;
        for (int i = 1; i < len; i++) {
            while (border > 0 && p[border] != p[i])
                border = s[border-1];
            
            if (p[i] == p[border]) 
                border++;
            else
                border = 0;

            s[i] = border;
        }

        return s;
    }

    static public void main(String[] args) throws IOException {
        new KnuthMorrisPratt().run();
    }

    public void print(List<Integer> x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String pattern = scanner.next();
        String text = scanner.next();
        List<Integer> positions = findPattern(pattern, text);
        print(positions);
    }
}
