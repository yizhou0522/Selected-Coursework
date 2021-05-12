
import java.util.ArrayList; // allowed for creating traversal lists
import java.util.LinkedList;
import java.util.List; // required for returning List<K>
import java.util.Queue;

/**
 * This class represents a BST class
 */
public class BST<K extends Comparable<K>, V> implements BSTADT<K, V> {

  protected BSTNode<K, V> root;// the reference of root node
  protected int numKeys = 0; // number of keys in BST

  /**
   * no-arg constructor, initialize the root node
   */
  public BST() {
    root = null;
  }

  @Override
  /**
   * @return a preorder list
   */
  public List<K> getPreOrderTraversal() {
    List<K> result = new ArrayList<>();
    if (root == null)
      return result;
    else {
      preOrder(root, result);
      return result;
    }
  }

  /**
   * preorder helper method
   * 
   * @param root is the given node
   * @param result is the recorded list
   */
  private void preOrder(BSTNode<K, V> root, List<K> result) {
    if (root != null) {
      result.add(root.key);
      preOrder(root.left, result);
      preOrder(root.right, result);
    }
  }

  /**
   * @return a post order list
   */
  @Override
  public List<K> getPostOrderTraversal() {
    List<K> result = new ArrayList<>();
    if (root == null)
      return result;
    else {
      postOrder(root, result);
      return result;
    }
  }

  /**
   * postOrder helper method
   * 
   * @param root is the given node
   * @param result is the recorded list
   */
  private void postOrder(BSTNode<K, V> root, List<K> result) {
    if (root != null) {
      postOrder(root.left, result);
      postOrder(root.right, result);
      result.add(root.key);
    }
  }

  /**
   * @return a levelorder list
   */
  @Override
  public List<K> getLevelOrderTraversal() {
    List<K> result = new ArrayList<>();
    int h = getHeight(root);
    int i;
    for (i = 1; i <= h; i++) {
      levelOrder(root, i, result);
    }
    return result;
  }

  /**
   * This is a private level order helper
   * 
   * @param root is the given node
   * @param level is the given level
   * @param result is the recorded list
   */
  private void levelOrder(BSTNode<K, V> root, int level, List<K> result) {
    if (root == null)
      return;
    if (level == 1)
      result.add(root.key);
    else if (level > 1) {
      levelOrder(root.left, level - 1, result);
      levelOrder(root.right, level - 1, result);
    }

  }

  /**
   * @return a in order list
   */
  @Override
  public List<K> getInOrderTraversal() {
    List<K> result = new ArrayList<>();
    if (root == null)
      return result;
    else {
      inOrder(root, result);
      return result;
    }
  }

  /**
   * This is a inorder private helper
   * 
   * @param root is the given node
   * @param result is the recorded list
   */
  private void inOrder(BSTNode<K, V> root, List<K> result) {
    if (root != null) {
      inOrder(root.left, result);
      result.add(root.key);
      // System.out.print(root.key + " ");
      inOrder(root.right, result);
    }
  }

  /**
   * insert a node properly and throw exception if necessary
   * 
   * @param the given key
   * @param the given value
   */
  @Override
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    root = insert(root, key, value);
    numKeys++;
  }

  /**
   * This is the private insert helper
   * 
   * @param current is the given node
   * @param key is the given key
   * @param value is the given value
   * @return a node reference
   * @throws DuplicateKeyException if key is already in the tree
   */
  private BSTNode<K, V> insert(BSTNode<K, V> current, K key, V value) throws DuplicateKeyException {
    if (current == null) {
      current = new BSTNode<>(key, value);
      return current;
    }
    if (key.compareTo(current.key) == 0)
      throw new DuplicateKeyException();
    if (key.compareTo(current.key) < 0) {
      current.left = insert(current.left, key, value);
    } else if (key.compareTo(current.key) > 0) {
      current.right = insert(current.right, key, value);
    }
    return current;
  }

  /**
   * removes the element given by the user's key
   * 
   * @return true if the element is successfully removed, otherwise return false;
   */
  @Override
  public boolean remove(K key) throws IllegalNullKeyException, KeyNotFoundException {
    if (key == null)
      throw new IllegalNullKeyException();
    if (!contains(key))
      throw new KeyNotFoundException();
    root = remove(root, key);
    numKeys--;
    return true;
  }

  /**
   * This is the private remove method
   * 
   * @param root is the given node
   * @param key is the given key
   * @return the node reference
   */
  private BSTNode<K, V> remove(BSTNode<K, V> root, K key) {
    if (root == null)
      return root;
    if (key.compareTo(root.key) < 0)
      root.left = remove(root.left, key);
    else if (key.compareTo(root.key) > 0)
      root.right = remove(root.right, key);
    else {
      if (root.left == null)
        return root.right;
      else if (root.right == null)
        return root.left;
      root.key = minValue(root.right);
      // Delete the inorder successor
      root.right = remove(root.right, root.key);
    }

    return root;
  }

  /**
   * This is the private helper method minValue
   * 
   * @param root is the given node
   * @return the leftmost key in given node's subtree
   */
  private K minValue(BSTNode<K, V> root) {
    K minv = root.key;
    while (root.left != null) {
      minv = root.left.key;
      root = root.left;
    }
    return minv;
  }

  /**
   * Get the key in the tree
   * 
   * @return the value of the given tree
   */
  @Override
  public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    if (key == null)
      throw new IllegalNullKeyException();
    return get(root, key);
  }

  /**
   * This is the private method of get
   * 
   * @param root is the given node
   * @param key is the given key
   * @return value given by the key
   * @throws KeyNotFoundException if key does not exist in the tree
   */
  private V get(BSTNode<K, V> root, K key) throws KeyNotFoundException {
    boolean found = false;
    while ((root != null) && !found) {
      K kval = root.key;
      if (key.compareTo(kval) < 0)
        root = root.left;
      else if (key.compareTo(kval) > 0)
        root = root.right;
      else {
        return root.value;
      }
    }
    throw new KeyNotFoundException();
  }

  /**
   * This method checks if a given key is in the tree
   * 
   * @return true if the given key is in the tree, otherwise returns false
   */
  @Override
  public boolean contains(K key) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    return contains(root, key);
  }

  /**
   * This is a private helper method of contains
   * 
   * @param node is the given node
   * @param key is the given key
   * @return true if the given key is in the tree, otherwise returns false
   */
  private boolean contains(BSTNode<K, V> node, K key) {
    if (node == null)
      return false;
    if (node.key.compareTo(key) == 0)
      return true;
    if (key.compareTo(node.key) < 0)
      return contains(node.left, key);
    else
      return contains(node.right, key);
  }

  /**
   * @return the number of keys
   */
  @Override
  public int numKeys() {
    return numKeys;
  }

  /**
   * @return the key of root node
   */
  @Override
  public K getKeyAtRoot() {
    if (root == null)
      return null;
    else {
      return root.key;
    }
  }

  /**
   * 
   * @return the key of left child of the user given key
   */
  @Override
  public K getKeyOfLeftChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
    if (key == null)
      throw new IllegalNullKeyException();
    return getNode(root, key).left.key;
  }

  /**
   * This is the private getNode method
   * 
   * @param node is the given node
   * @param key is the given key
   * @return node found by the given parameters
   * @throws KeyNotFoundException if key does not exist in the tree
   */
  private BSTNode<K, V> getNode(BSTNode<K, V> node, K key) throws KeyNotFoundException {
    if (node == null)
      throw new KeyNotFoundException();
    if (node.key.compareTo(key) == 0)
      return node;
    if (key.compareTo(node.key) < 0)
      return getNode(node.left, key);
    else
      return getNode(node.right, key);

  }

  /**
   * 
   * @return the key of right child of the user given key
   */
  @Override
  public K getKeyOfRightChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
    if (key == null)
      throw new IllegalNullKeyException();
    return getNode(root, key).right.key;
  }

  /**
   * @return the value of height of this tree
   */
  @Override
  public int getHeight() {
    return getHeight(root);

  }

  /**
   * This is the private the getHeight method
   * 
   * @param root is the given node
   * @return the value of height of the tree
   */
  private int getHeight(BSTNode<K, V> root) {
    if (root == null)
      return 0;
    else {
      int left = getHeight(root.left);
      int right = getHeight(root.right);
      if (left > right)
        return (left + 1);
      else
        return (right + 1);
    }
  }

}
