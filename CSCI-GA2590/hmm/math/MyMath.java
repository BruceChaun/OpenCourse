package hmm.math;

import hmm.HMM;

public class MyMath {
	/*
	 * random() generates float number between min and max
	 * 
	 * @param min is the lower bound of the random number, 
	 * and max is the upper bound.
	 * 
	 * returns the random number
	 */
	public static double random(double min, double max) {
		return Math.random() * (max - min) + min;
	}
	
	/*
	 * logprob() calculates the log probability base on e
	 * 
	 *  @param p is the input probability
	 *  
	 *  returns the log probability
	 */
	public static double logprob(double p) {
		return Math.log(p);
	}
	
	/*
	 * logAdd() add the two log probabilities
	 * 
	 * @param a and b are two log probabilities
	 * 
	 * returns the added log probability
	 */
	public static double logAdd(double a, double b) {
		double[] x = {a, b};
		return logAdd(x);
	}
	
	/*
	 * Same as the previous logAdd, the only difference is 
	 * it takes an array as input parameter, rather than 
	 * two log probabilities.
	 */
	public static double logAdd(double[] a) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		if (sum > 0 || sum < HMM.NINF)
			return HMM.NINF;
		return sum;
	}
	
	/*
	 * normal() first normalizes each element in array a,
	 * then convert to log probability
	 * 
	 * @param a is an array to be normalized
	 */
	public static void normal(double[] a) {
		double sum = 0;
		int len = a.length;
		for (int i =0; i < len; i++) {
			sum += a[i];
		}
		
		for (int i = 0; i < len; i++) {
			double logprob = a[i] / sum;
			a[i] = logprob(logprob);
		}
	}

}
