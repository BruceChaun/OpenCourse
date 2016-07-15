package hmm;

import hmm.math.MyMath;

public class HMM {
	private static HMMdata data;
	private static final double eps = 1E-5;
	private static final int max_iter = 10000;
	public static final double NINF = Double.NEGATIVE_INFINITY;
	
	public static void train(String[] seq) {
		/*
		 * import tag and symbol mapper
		 * initialize model parameters like transition and emission
		 */
		data.initTag();
		data.initSymbol(seq);
		int M = data.symbolMap.size();		// #vocabulary
		int N = data.tagMap.size();				// #tag
		data.initModel();
		
		boolean converge = false;
		double prev_logprob = NINF;
		int iter = 0;
		while (!converge && iter < max_iter) {
			/* 
			 * sum of all the sequences
			 * batch-learning, update model parameters after 
			 * learning all the sequences
			 */
			double[][] A_numer = initArray(N+2, N+2);
			double[][] B_numer = initArray(N+1, M);
			double[] A_denom = initArray(N+2);
			double[] B_denom = initArray(N+1);
			
			double logprob = 0;
			for (int n = 0; n < seq.length; n++) {
				String[] s = seq[n].split(" ");
				int T = s.length;					// #words in sentence
				if (T == 0)
					continue;
				
				/* 
				 * use forward and backford algorithm to generate
				 * alpha and beta
				 */
				double[][] alpha = forward(s);
				double[][] beta = backward(s);
				
				/*
				 *  calculate the sentence likelihood
				 *  P(O|\lambda) = \alpha(T, q_F) = \sum \alpha(T, i) * a(i, q_F)
				 */
				double p = NINF;
				for (int i = 1; i <= N; i++) {
					p = MyMath.logAdd(p, alpha[T-1][i] + data.transition[i][N+1]);
				}
				logprob += p;
				
				// store each sequence, then add to global sum
				double[][] A_numer2 = initArray(N+2, N+2);
				double[][] B_numer2 = initArray(N+1, M);
				double[] A_denom2 = initArray(N+2);
				double[] B_denom2 = initArray(N+1);
				
				// from start state to the state of the first word
				for (int i = 1; i <= N; i++) {
					A_numer2[0][i] = MyMath.logAdd(A_numer2[0][i], 
							data.transition[0][i] + 
							data.emission[i][data.symbolMap.get(s[0])] +
							beta[1][i]);
					A_denom2[0] = MyMath.logAdd(A_denom2[0], A_numer2[0][i]);
				}
				
				// from the state of last word to end state
				for (int i = 1; i <= N; i++) {
					A_numer2[i][N+1] = MyMath.logAdd(A_numer2[i][N+1], 
							alpha[T-1][i] + data.transition[i][N+1]);
					A_denom2[N+1] = MyMath.logAdd(A_denom2[N+1], A_numer2[i][N+1]);
				}
				
				for (int t = 0; t < T; t++) {
					String word = s[t];
					int wordIndex = data.symbolMap.get(word);
					String nextWord = "";
					if (t < T - 1) {
						nextWord = s[t+1];
					}
					
					for (int i = 1; i <= N; i++) {
						if (t < T - 1) {
							for (int j = 1; j <= N; j++) {
								A_numer2[i][j] = MyMath.logAdd(A_numer2[i][j], 
										alpha[t][i] + 
										data.transition[i][j] + 
										data.emission[j][data.symbolMap.get(nextWord)] +
										beta[t+1][j]);
							}
							A_denom2[i] = MyMath.logAdd(A_denom2[i], 
									alpha[t][i] + beta[t][i]);
						} else {
							B_denom2[i] = MyMath.logAdd(A_denom2[i], 
									alpha[t][i] + beta[t][i]);
						}
						
						B_numer2[i][wordIndex] = MyMath.logAdd(B_numer2[i][wordIndex], 
								alpha[t][i] + beta[t][i]);
					}
					
				}
				
				// add to global values
				for (int i = 0; i <= N; i++) {
					A_numer[i][N+1] = MyMath.logAdd(A_numer[i][N+1],
							A_numer2[i][N+1] - p);
					
					for (int j = 1; j <= N+1; j++) {
						if (i > 0 || j < N+1) {
							A_numer[i][j] = MyMath.logAdd(A_numer[i][j], 
								A_numer2[i][j] - p);
						}
					}
					A_denom[i] = MyMath.logAdd(A_denom[i], 
							A_denom2[i] - p);
					
					if (i > 0) {
						for (int j = 0; j < M; j++) {
							B_numer[i][j] = MyMath.logAdd(B_numer[i][j], 
									B_numer2[i][j] - p);
						}
						B_denom[i] = MyMath.logAdd(B_denom[i], 
								B_denom2[i] - p);
					}
				}
			}
			
			// update transition and emission matrices
			for (int i = 0; i <= N; i++) {
				for (int j = 1; j <= N+1; j++) {
					if (i > 0 || j < N+1)
						data.transition[i][j] = A_numer[i][j] - A_denom[i];
				}
				if (i > 0) {
					for (int j = 0; j < M; j++) {
						data.emission[i][j] = B_numer[i][j] - B_denom[i];
					}
				}
			}
			
			// judge convergence
			if (Math.abs(logprob - prev_logprob) < eps) {
				converge = true;
			}
			
			iter++;
			prev_logprob = logprob;
		}
	}
	
	private static double[][] backward(String[] s) {
		// TODO Auto-generated method stub
		int T = s.length;
		int N = data.tagMap.size();
		double[][] beta = new double[T][N+1];
		
		// \beta(T, i) = a(i, q_F)
		for (int i = 1; i <= N; i++) {
			beta[T-1][i] = data.transition[i][N+1];
		}
		
		// for t = T-1 .. 1, \beta(t, i) = \sum a(i, j) * b(j ,0_{t+1}) * \beta(t+1, j)
		for (int t = T-2; t > 0; t--) {
			int wordIndex = data.symbolMap.get(s[t+1]);
			for (int i = 1; i <= N; i++) {
				beta[t][i] = 0;
				for (int j = 1; j <= N; j++) {
					beta[t][i] = MyMath.logAdd(beta[t][i], 
							data.transition[i][j] + data.emission[j][wordIndex] + beta[t+1][j]);
				}
			}
		}
		return beta;
	}

	/*
	 * forward algorithm
	 */
	private static double[][] forward(String[] s) {
		// TODO Auto-generated method stub
		int T = s.length;
		int N = data.tagMap.size();
		double[][] alpha = new double[T][N+1];
		
		int wordIndex = data.symbolMap.get(s[0]);
		// \alpha(1, i) = a(0, i) * b(i, o_1)
		for (int i = 1; i <= N; i++) {
			alpha[0][i] = data.transition[0][i] + data.emission[i][wordIndex];
		}
		
		// for t = 2 .. T, \alpha(t, i) = \sum \alpha(t-1, j) * a(j, i) * b(i, o_t) 
		for (int t = 1; t < T; t++) {
			wordIndex = data.symbolMap.get(s[t]);
			for (int i = 1; i <= N; i++) {
				alpha[t][i] = 0;
				for (int j = 1; j <= N; j++) {
					alpha[t][i] = MyMath.logAdd(alpha[t][i], 
						alpha[t-1][j] + data.transition[j][i] + data.emission[i][wordIndex]);
				}
			}
		}
		return alpha;
	}

	public static double[][] initArray(int row, int col) {
		double[][] a = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				a[i][j] = 0;
			}
		}
		return a;
	}
	
	public static double[] initArray(int size) {
		double[] a = new double[size];
		for (int i = 0; i < size; i++) {
				a[i] = 0;
		}
		return a;
	}
	
	public static void main(String[] args) {
		
	}

}
