import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BurrowsWheelerTransform {
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

    String BWT(String text) {
        StringBuilder result = new StringBuilder();

        // write your code here

        if (text.charAt(text.length()-1) != '$')
            text += '$';

        int len = text.length();
        PriorityQueue<String> pq = cycleAndSort(text);

        while (!pq.isEmpty()) {
            String s = pq.poll();
            result.append(s.charAt(len-1));
        }

        return result.toString();
    }

    private static PriorityQueue<String> cycleAndSort(String text) {
        int len = text.length();
        PriorityQueue<String> pq = new PriorityQueue<String>();
        String[] texts = new String[len];

        for (int i = 0; i < len; i++) {
            pq.offer(text);
            text = text.substring(len - 1) + text.substring(0, len - 1);
        }
        return pq;
    }

    static public void main(String[] args) throws IOException {
        new BurrowsWheelerTransform().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        System.out.println(BWT(text));
    }
}
