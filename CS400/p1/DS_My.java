

import javax.management.RuntimeErrorException;

/**
 * This class represents a DS_My object
 * 
 * @author user
 *
 */
public class DS_My implements DataStructureADT {

  // TODO may wish to define an inner class
  // for storing key and value as a pair
  // such a class and its members should be "private"

  // Private Fields of the class
  // TODO create field(s) here to store data pairs
  /**
   * An inner class used to construct a key-value pair
   * 
   * @author user
   *
   */
  private class KeyValue {
    private Comparable key;// the key
    private Object value;// the value

    /**
     * constructor for keyvalue class
     * 
     * @param k is the given string of k
     * @param v is the given string of v
     */
    private KeyValue(Comparable k, Object v) {
      this.key = k;
      this.value = v;
    }

    /**
     * get the value of key
     * 
     * @return the key string
     */

    private Comparable getKey() {
      return key;
    }

    /**
     * get the string of value
     * 
     * @return the value string
     */
    private Object getValue() {
      return value;
    }
  }

  private final int MAX_SIZE = 1000;// max number of elements in array
  private int size = 0;// keep track of size
  private KeyValue[] myArray;

  /**
   * constructor for DS_My, initialize myArray
   */
  public DS_My() {
    myArray = new KeyValue[MAX_SIZE];

  }

  @Override
  /**
   * insert a key value pair into the array
   */
  public void insert(Comparable k, Object v) {
   
    if (k == null)
      throw new IllegalArgumentException("Null key");
    for (int i = 0; i < size; i++) {
      if (k.compareTo(myArray[i].getKey()) == 0)
        throw new RuntimeException("Duplicate Key");
    }
    myArray[size++] = new KeyValue(k, v);

  }

  /**
   * remove a specific key value pair from the array
   */
  @Override
  public boolean remove(Comparable k) {
  
    if (k == null)
      throw new IllegalArgumentException("Null key");
    if (!contains(k))
      return false;
    int index = 0;
    for (int i = 0; i < size; i++)
      if (k.compareTo(myArray[i].getKey()) == 0) {
        index = i;
        break;
      }//find the index of key k
    while (index < size) {
      myArray[index] = myArray[index + 1];
      myArray[index + 1] = null;
      index++;
    }//remove the given key 
    size--;
    return true;
  }

  /**
   * check if a specific key-value pair is in the array
   */
  @Override
  public boolean contains(Comparable k) {
    for (int i = 0; i < size; i++)
      if (k.compareTo(myArray[i].getKey()) == 0)
        return true;
    return false;
  }

  /**
   * get a specific key-value pair from the array
   * 
   * @return the key-value object
   */
  @Override
  public Object get(Comparable k) {
    if (k == null)
      throw new IllegalArgumentException("Null key");
    if (contains(k))
      for (int i = 0; i < size; i++)
        if (k.compareTo(myArray[i].getKey()) == 0)
          return myArray[i].getValue();
    return null;
  }

  /**
   * get the value of size
   * 
   * @return the value of size
   */
  @Override
  public int size() {
    return size;
  }

}
