

/**
 * I choose number 5 strategy as my method to implement this hashtable. Hashtable is an array and I
 * choose linkedlist to implement the bucket
 * 
 * @author user
 *
 * @param <K> the generic type
 * @param <V> the generic type
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

  private int capacity;// the number of table
  private double loadFactorThreshold;// the threshold
  private int numOfKeys;// the number of keys
  private ListNode[] table;// the hashtable array

  /**
   * Inner class of listnode
   * 
   * @author user
   *
   * @param <K> the generic type
   * @param <V> the generic type
   */
  static private class ListNode<K extends Comparable<K>, V> {
    K key;
    V value;
    ListNode next;
  }

  /**
   * Default constructor
   */
  public HashTable() {
    this(11, 0.75);
    table = new ListNode[capacity];
  }

  /**
   * Construtor
   * 
   * @param initialCapacity
   * @param loadFactorThreshold
   */
  public HashTable(int initialCapacity, double loadFactorThreshold) {
    this.capacity = initialCapacity;
    this.loadFactorThreshold = loadFactorThreshold;
    table = new ListNode[capacity];
  }

  /**
   * private helper method for hash
   * 
   * @param key is the given key
   * 
   * @return the integer of hashcode
   */
  private int hash(K key) {
    return (Math.abs(key.hashCode())) % capacity;
  }

  /**
   * private helper method for resize
   */
  private void resize() {
    ListNode[] newtable = new ListNode[table.length * 2 + 1];
    for (int i = 0; i < table.length; i++) {
      ListNode list = table[i]; // For traversing linked list number i.
      while (list != null) {//traversing the list
        ListNode next = list.next;
        int hash = (Math.abs(list.key.hashCode())) % newtable.length;
        list.next = newtable[hash];
        newtable[hash] = list;
        list = next;
      }
    }
    table = newtable;
    capacity = newtable.length;
  }

  /**
   * the insert method of hashtable
   * 
   * @param the given key
   * @param the given value
   */
  @Override
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    int hashIndex = hash(key);
    ListNode list = table[hashIndex];
    while (list != null) {
      if (list.key.equals(key))
        throw new DuplicateKeyException();
      list = list.next;
    }
    if (numOfKeys >= loadFactorThreshold * table.length)//rehash
      resize();
    ListNode newNode = new ListNode();//add new nodes at the beginning of each list
    newNode.key = key;
    newNode.value = value;
    newNode.next = table[hashIndex];
    table[hashIndex] = newNode;
    numOfKeys++; // Count the newly added key.

  }

  /**
   * remove the given key
   * 
   * @return true if the key can be found, otherwise false.
   * @param key the given key
   * @throw illegalnullkeyexception if key is null
   */
  @Override
  public boolean remove(K key) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    int hashIndex = hash(key);
    if (table[hashIndex] == null) {
      return false;
    }
    if (table[hashIndex].key.equals(key)) {
      table[hashIndex] = table[hashIndex].next;
      numOfKeys--;
      return true;//find the keys and delete
    }
    ListNode prev = table[hashIndex];
    ListNode curr = prev.next;
    while (curr != null && !curr.key.equals(key)) {
      curr = curr.next;
      prev = curr;
    }
    if (curr != null) {
      prev.next = curr.next;
      numOfKeys--;
      return true;//find the keys and delete
    }
    return false;// key does not found in table
  }

  /**
   * @return the value key
   */
  @Override
  public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    if (key == null)
      throw new IllegalNullKeyException();
    int hashIndex = hash(key);
    ListNode list = table[hashIndex];
    while (list != null) {
      if (list.key.equals(key))
        return (V) list.value;
      list = list.next;
    }
    throw new KeyNotFoundException();//does not find the key 
  }

  /**
   * @return the number of keys
   */
  @Override
  public int numKeys() {
    return numOfKeys;
  }

  /**
   * @return the value of threshold
   */
  @Override
  public double getLoadFactorThreshold() {
    return loadFactorThreshold;
  }

  /**
   * return the value of load factor
   */
  @Override
  public double getLoadFactor() {
    return ((double) (numOfKeys)) / capacity;
  }

  /**
   * return the int value of capacity
   */
  @Override
  public int getCapacity() {
    return capacity;
  }

  /**
   * return the int of collision resolution
   */
  @Override
  public int getCollisionResolution() {
    return 5;
  }
}
