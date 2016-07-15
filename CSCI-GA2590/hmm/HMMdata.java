package hmm;

import java.util.HashMap;

import hmm.math.MyMath;

class HMMdata {
	double[][] transition;				// transition matrix, size N+2 by N+2 (including 
															// start and end states
	double[][] emission;				// emission matrix, size N+2 by M
	
	// pos tags of Penn tree
	String[] tags = {
			"CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", 
			"MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP",
			"PRPS", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", 
			"VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WPS", 
			"WRB", "$", "#", "`", "\"", "(", ")", ",", ".", ":"
	};
	
	/*
	 * map tag(state) and word(symbol) to integer as index, 
	 */
	HashMap<String, Integer> tagMap = new HashMap<String, Integer>();
	HashMap<String, Integer> symbolMap = new HashMap<String, Integer>();
	
	void initTag() {
		for (int i = 0; i < tags.length; i++) {
			// 0 is for start state, tags.length+1 for end state
			tagMap.put(tags[i], i+1);
		}
	}
	
	void initSymbol(String[] symbols) {
		for (int i = 0; i < symbols.length; i++) {
			symbolMap.put(symbols[i], i);
		}
	}
	
	void initModel() {
		int N = tags.length;
		int M = symbolMap.size();
		transition = new double[N+2][N+2];
		
		// Initialize start state to other tags in transition
		for (int i = 1; i <= N; i++) {
			transition[0][i] = MyMath.random(1, 10);
		}
		
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N + 1; j++) {
				transition[i][j] = MyMath.random(1, 10);
			}
		}
		
		// normalize and transform to log probability
		for (int i = 0; i <= N; i++) {
			MyMath.normal(transition[i]);
		}
		
		// emission[0] is empty, so we don't need to consider offset
		emission = new double[N+1][M];
		// initialize emission matrix
		for (int i = 1; i <= N; i++) {
			for (int j = 0; j < M; j++) {
				emission[i][j] = MyMath.random(1, 10);
			}
			MyMath.normal(emission[i]);
		}
	}

}
