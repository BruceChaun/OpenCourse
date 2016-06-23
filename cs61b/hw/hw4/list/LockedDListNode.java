/* LockedDListNode.java */

package list;

/*
 * A LockedDListNode is a node in a LockedDList,
 * inherits from DListNode.
 */

public class LockedDListNode extends DListNode {
	protected boolean locked;

	LockedDListNode(Object i, DListNode p, DListNode n) {
		super(i, p, n);
		locked = false;
	}
}
