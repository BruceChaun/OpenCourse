/* LockedDList */

package list;

/*
 * A LockedDList inherits from DList. Removing a locked node has no effect.
 */

public class LockedDList extends DList {
	protected LockedDListNode newNode(Object item, DListNode prev, DListNode next) {
		return new LockedDListNode(item, prev, next);
	}

	public void lockNode(DListNode node) {
		((LockedDListNode) node).locked = true;
	}

	public void remove(DListNode node) {
		if (!((LockedDListNode) node).locked) {
			super.remove(node);
		}
	}

	private static void test(boolean b, String s) {
		if (b)
			System.out.println("Good.");
		else 
			System.out.println(s);
	}

	public static void main(String[] args) {
		System.out.println("\nTesting create empty DList...\n");
		LockedDList d = new LockedDList();
		test(d.toString().equals("[  ]"), "init empty DList fails.");

		System.out.println("\nTesting insertFront()...\n");
		d.insertFront(1);
		System.out.println(d);
		test(d.toString().equals("[  1  ]"), "add 1 at front fails.");

		System.out.println("\nTesting insertBack()...\n");
		d.insertBack(2);
		System.out.println(d);
		test(d.toString().equals("[  1  2  ]"), "add 1 at back fails.");

		System.out.println("\nTesting front()...\n");
		DListNode f = d.front();
		test(f.item == 1, "DList front = 1 fails.");

		System.out.println("\nTesting back()...\n");
		DListNode b = d.back();
		test(b.item == 2, "DList back = 2 fails.");

		System.out.println("\nTesting insertAfter()...\n");
		d.insertAfter(10, f);
		System.out.println(d);
		test(d.toString().equals("[  1  10  2  ]"), "insert 10 after 1 fails.");

		System.out.println("\nTesting insertBefore()...\n");
		d.insertBefore(5, b);
		System.out.println(d);
		test(d.toString().equals("[  1  10  5  2  ]"), "insert 5 before 2 fails.");

		System.out.println("\nTesting prev()...\n");
		DListNode p = d.prev(b);
		test(p.item == 5, "prev of 2 = 5 fails.");

		d.insertAfter(6, b);
		d.lockNode(b);
		d.remove(b);
		System.out.println(d);
		test(d.toString().equals("[  1  10  5  2  6  ]"), "lock 2 fails.");
	}
}
