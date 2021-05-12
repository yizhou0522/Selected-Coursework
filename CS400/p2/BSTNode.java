

// Students may use and edit this class as they choose
// Students may add or remove or edit fields, methods, constructors for this class
// Students may inherit from an use this class in any way internally in other classes.
// Students are not required to use this class.
// BUT, IF YOUR CODE USES THIS CLASS, BE SURE TO SUBMIT THIS CLASS
//
// RECOMMENDED: do not use public or private visibility modifiers
// package level access means that all classes in the package can access directly.
//
// Classes that use this type: <TODO, list which if any classes use this type>
/**
 * This class represents a BSTNode instance
 * 
 * @author user
 *
 * @param <K> the generic type
 * @param <V> the generic type
 */
class BSTNode<K, V> {

  K key;
  V value;
  BSTNode<K, V> left;
  BSTNode<K, V> right;
  int balanceFactor;
  int height;


  /**
   * This constructor initializes the variables
   * 
   * @param key the given key value
   * @param value the given value
   * @param leftChild the given left node
   * @param rightChild the given right node
   */
  BSTNode(K key, V value, BSTNode<K, V> leftChild, BSTNode<K, V> rightChild) {
    this.key = key;
    this.value = value;
    this.left = leftChild;
    this.right = rightChild;
    this.height = 0;
    this.balanceFactor = 0;
  }

  /**
   * default constructor
   * 
   * @param key the given value
   * @param value the given value
   */

  BSTNode(K key, V value) {
    this(key, value, null, null);
  }


}
