

import java.util.TreeMap;

/**
 * 
 * @author user
 *
 * @param <K> the generic type
 * @param <V> the generic type
 */
public class MyProfiler<K extends Comparable<K>, V> {

  HashTableADT<K, V> hashtable;// hashtable instance
  TreeMap<K, V> treemap;// java.util.treemap instance

  /**
   * The constructor of MyProfiler
   */
  public MyProfiler() {
    hashtable = new HashTable<>();
    treemap = new TreeMap<>();
  }

  /**
   * insert a given element
   * 
   * @param key is the given key
   * @param value is the given value
   * @throws IllegalNullKeyException if the key is null
   * @throws DuplicateKeyException if another key is already in the treemap or hashtable
   */
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
    hashtable.insert(key, value);
    treemap.put(key, value);
  }

  /**
   * get an element based on given key
   * 
   * @param key the given key
   * @throws IllegalNullKeyException if the key is null
   * @throws KeyNotFoundException if they key is not found
   */
  public void retrieve(K key) throws IllegalNullKeyException, KeyNotFoundException {
    hashtable.get(key);
    treemap.get(key);
  }

  /**
   * The main methods test the hot methods of util.treemap and hashtable
   * 
   * @param args
   */
  public static void main(String[] args) {
    try {
      int numElements = Integer.parseInt(args[0]);
      MyProfiler<Integer, Integer> profile = new MyProfiler<>();
      for (int i = 0; i < numElements; i++) {
        profile.insert(i, 1);
        profile.retrieve(i);
      }
      String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
      System.out.println(msg);
    } catch (Exception e) {
      System.out.println("Usage: java MyProfiler <number_of_elements>");
      System.exit(1);
    }
  }
}
