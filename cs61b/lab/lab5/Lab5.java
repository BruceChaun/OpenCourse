/*
 * experimental code for lab5
 * class X and Y are used
 */

public class Lab5 {
	public static void main(String[] args) {
		part1_a();
		part1_b();
		part1_c();

		part2();

		part3();

		part4();
	}

	public static void part1_a() {
		X[] xa = new X[5];
		Y[] ya;

		/*
		 * Y[] ya = xa;
		 *
		 * compile-time error
		 */

		/*
		 * need cast
		 * if from parent to child
		 * to pass compile-time
		 *
		 * but run time-error
		 *
		 * Y[] ya = (Y[]) xa;
		 */

		ya = new Y[5];

		/*
		 * both are groovy
		 */
		xa = ya;
		xa = (X[]) ya;
	}

	public static void part1_b() {
		Y[] ya = new Y[5];

		for (int i = 0; i < 5; i++) {
			Y y = new Y();
			y.a = i;
			y.b = i * i;
			ya[i] = y;
		}

		X[] xa = new X[5];

		/*
		 * both are groovy
		 * no run-time error
		 */
		xa = ya;
		xa = (X[]) ya;

		for (int i = 0; i < 5; i++) {
			System.out.println(ya[i].a);
		}

		/*
		 * we can assign back from xa to ya
		 */
		ya = (Y[]) xa;
		for (int i = 0; i < 5; i++) {
			System.out.println(ya[i].a + "\t" + ya[i].b);
		}
	}

	public static void part1_c() {
		X[] xa = new X[5];

		for (int i = 0; i < 5; i++) {
			X x = new X();
			x.a = i;
			xa[i] = x;
		}

		/* 
		 * Y[] ya = xa;			// compile-time error
		 * Y[] ya = (Y[]) xa;	// run-time error
		 *
		 * cannot assign from parent to child
		 */
	}

	public static void part2() {
		Y2 y2 = new Y2();

		/*
		 * (a)
		 *
		 * no compile-time or run-time error
		 * if subclass inherits and implements
		 * a method of superclass and implements 
		 * an interface with the same name
		 * and prototype.
		 *
		 * (b)
		 *
		 * compile-time error 
		 * if the interface has a different
		 * return value.
		 *
		 * (c)
		 *
		 * That different parameter type
		 * does not matter, it will choose
		 * the method with the proper
		 * parameter type.
		 *
		 * (d)
		 *
		 * For the case of the same return type, 
		 * number of parameters and type, but 
		 * with different names, call Y2.method, no error.
		 */
		y2.print("which");
		y2.print("");
	}

	public static void part3() {
		/*
		 * (a)
		 *
		 * Y2 will choose I.c
		 * no matter if values are the same
		 *
		 * (b)
		 * view the code in Y2.java
		 * use the constant name directly will cause error,
		 *
		 * (c)
		 *
		 * should explicitly declare whose constant name is.
		 */
	}

	public static void part4() {
		/*
		 * (a)
		 * Java calls the subclass method.
		 *
		 * (b)
		 * Java calls the superclass method.
		 *
		 * (c)
		 * cast to superclass
		 */
		Y2 y2 = new Y2();
		((X) y2).print("1");

		X x = new Y();
		x.a = 1;
		((Y) x).test();
	}
}
