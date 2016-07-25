package mem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FeatureBuilder {
	
	/*
	 * nextWindow() adds str into s[] to form a new window
	 */
	private static void nextWindow(String[] s, String str) {
		for (int i = 0; i < s.length - 1; i++) {
			s[i] = s[i + 1];
		}
		s[s.length-1] = str;
	}
	
	/*
	 * initWindow() is invoked to build a new sentence. "#" means the start 
	 * or end symbol of a sentence.
	 * 
	 * @return true if build() is needed after initialization, false otherwise.
	 */
	private static boolean initWindow(BufferedReader dataReader, 
		String[] words, String[] pos, String[] bio, int size) throws IOException {
		words[0] = "#";
		pos[0] = "#";
		bio[0] = "#";
		for (int i = 1; i < size; i++) {
			String line = dataReader.readLine();
			if (line != null) {
				if (!line.equals("")) {
					String[] s = line.split("\t");
					words[i] = s[0];
					pos[i] = s[1];
					bio[i] = s[2];
				} else {
					return true;
				}
			} else {
				words[i] = "#";
				pos[i] = "#";
				bio[i] = "#";
				return true;
			}
		}
		return false;
	}
	
	/*
	 * build() generates a vector of feature functions for each word. Features involves
	 * words, POS tags, and BIO tags of each word, with window size @size. Typically,
	 * @size is 5 or 3. Each time, build() process the word in the middle of the window.
	 */
	private static void build(String[] words, String[] pos, String[] bio, PrintWriter writer, int size) {
		String output = words[size/2];
		for (int i = 0; i < size; i++) {
			if (!words[i].equals("#")) {
				if (i == size / 2) {
					output += "\tpos0=" + pos[i];
							
					if (words[i].charAt(0) >= 'A' && words[i].charAt(0) <= 'Z') {
						output += "\tcapitalized=1";
					}
				} else {
					String sign = "";
					if (i > size/2) {
						sign = "+";
					}
					output += "\tpos" + sign + (i - size/2) + "=" + pos[i]
							+ "\tbio" + sign  + (i - size/2) + "=" + bio[i];
				}
			}
		}
		output += "\t" + bio[size/2];
		writer.println(output);
	}
	
	public static void main(String[] args) {
		String dataFileName = args[0];
		String outputFileName = args[1];
		try {
			BufferedReader dataReader = new BufferedReader (new FileReader (dataFileName));
			PrintWriter writer = new PrintWriter (new FileWriter (outputFileName));
			
			final int window_size = Integer.parseInt(args[2]);
			String[] words = new String[window_size];
			String[] pos = new String[window_size];
			String[] bio = new String[window_size];
			
			if (initWindow(dataReader, words, pos, bio, window_size)) {
				build(words, pos, bio, writer, window_size);
			}
			
			String line = "";
			while ((line = dataReader.readLine()) != null) {
				build(words, pos, bio, writer, window_size);
				if (line.equals("")) {
					// end of sentence
					nextWindow(words, "#");
					nextWindow(pos, "#");
					nextWindow(bio, "#");
					build(words, pos, bio, writer, window_size);
					writer.println();
					if (initWindow(dataReader, words, pos, bio, window_size)) {
						build(words, pos, bio, writer, window_size);
					}
				} else {
					String[] s = line.split("\t");
					nextWindow(words, s[0]);
					nextWindow(pos, s[1]);
					nextWindow(bio, s[2]);
				}
			}
			
			// process the rest
			build(words, pos, bio, writer, window_size);
			nextWindow(words, "#");
			nextWindow(pos, "#");
			nextWindow(bio, "#");
			build(words, pos, bio, writer, window_size);
			
			dataReader.close();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
