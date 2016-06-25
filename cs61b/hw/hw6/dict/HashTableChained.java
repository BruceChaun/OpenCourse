/* HashTableChained.java */

package dict;

import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
	protected int tableLength;
	protected int bucketSize;
	protected DList[] list;


  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
	tableLength = 0;
	bucketSize = sizeEstimate;
	list = new DList[bucketSize];
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
	this(101);
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
    int hash = (7 * code + 60) % 15485863 % bucketSize;
	if (hash < 0)
		hash += bucketSize;
	return hash;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    return tableLength;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    return tableLength == 0;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
	int hashKey = compFunction(key.hashCode());
	Entry e = new Entry();
	e.key = key;
	e.value = value;
	if (list[hashKey] == null)
		list[hashKey] = new DList();
	list[hashKey].insertBack(e);

	tableLength++;
    return e;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    // Replace the following line with your solution.
	int hashKey = compFunction(key.hashCode());
	if (list[hashKey] == null)
		return null;

	DListNode n = (DListNode) list[hashKey].front();
	while (n != null) {
		try {
			if (((Entry) n.item()).key().equals(key)) {
				return (Entry) n.item();
			}
			n = (DListNode) n.next();
		} catch (InvalidNodeException e) {
			System.err.println(e);
		}
	}
	return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    // Replace the following line with your solution.
	int hashKey = compFunction(key.hashCode());
	if (list[hashKey] == null)
		return null;

	DListNode n = (DListNode) list[hashKey].front();
	while (n != null) {
		try {
			if (((Entry) n.item()).key().equals(key)) {
				n.remove();
				tableLength--;
				return (Entry) n.item();
			}
			n = (DListNode) n.next();
		} catch (InvalidNodeException e) {
			System.err.println(e);
		}
	}
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    // Your solution here.
	list = new DList[bucketSize];
	tableLength = 0;
  }

  /*
   * result() prints the collison and distribution of hash table
   */
  public void result() {
	  int zeros = 0, collisons = 0;
	  for (int i = 0; i < this.size(); i++) {
		  if (list[i] != null) {
			  if (list[i].length() > 1)
				  collisons++;
			  System.out.println(i + "\t has " + list[i].length() + " entries.");
		  } else {
			  zeros++;
			  System.out.println(i + "\t has 0 entries.");
		  }
	  }
	  System.out.println("There are " + zeros + " empty buckets, " +
			  collisons + " buckets collided.");
  }

}
