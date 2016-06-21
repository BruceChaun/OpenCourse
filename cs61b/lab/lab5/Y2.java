/*
 * experimental code for lab5
 */

public class Y2 extends X implements I {
	public void print(String s) {
		System.out.println("This is Y2.\t" + s);
	}

	public static void main(String[] args) {
		System.out.println(X.c);
	}
}
