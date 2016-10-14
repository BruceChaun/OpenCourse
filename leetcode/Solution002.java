/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode l = new ListNode(0);
        ListNode head = l;
        boolean carrier = false;
        while (l1 != null || l2 != null) {
            int sum = 0;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            if (carrier) {
                sum += 1;
            }
            carrier = sum > 9;
            l.next = new ListNode(sum % 10);
            l = l.next;
        }
        if (carrier) {
            l.next = new ListNode(1);
        }
        return head.next;
    }
}
