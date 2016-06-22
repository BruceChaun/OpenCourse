/* DList.java */

/**
 *  A DList is a mutable doubly-linked list.  
 *
 *  Modified from previous lab
 */

public class DList {

	/**
	 *  head references the sentinel node.
	 *
	 *  DO NOT CHANGE THE FOLLOWING FIELD DECLARATIONS.
	 */

	protected DListNode head;
	protected DListNode tail;
	protected long size;

	/**
	 *  DList() constructor for an empty DList.
	 */
	public DList() {
		head = null;
		tail = head;
		size = 0;
	}

	/**
	 *  DList() constructor for a one-node DList.
	 */
	public DList(int[] a) {
		head = new DListNode(a);
		tail = head;
		size = 1;
	}

	/**
	 *  insertEnd() inserts an item at the end of a DList.
	 */
	public void insertEnd(int[] i) {
		// Your solution here.
		if (head == null) {
			head = new DListNode(i);
			tail = head;
			return;
		}

		DListNode node = new DListNode(i);
		tail.next = node;
		node.prev = tail;
		tail = node;
		size++;
	}

	/**
	 *  insertFront() inserts an item at the front of a DList.
	 */
	public void insertFront(int i) {
		// Your solution here.
	}

	/**
	 *  removeFront() removes the first item (and first non-sentinel node) from
	 *  a DList.  If the list is empty, do nothing.
	 */
	public void removeFront() {
		// Your solution here.

		if (size == 0)
			return;
	}

	/**
	 *  toString() returns a String representation of this DList.
	 *
	 *  DO NOT CHANGE THIS METHOD.
	 *
	 *  @return a String representation of this DList.
	 */
	public String toString() {
		String result = "[  ";
		DListNode current = head.next;
		while (current != head) {
			result = result + current.item + "  ";
			current = current.next;
		}
		return result + "]";
	}
}
