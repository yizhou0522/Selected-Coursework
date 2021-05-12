
/**
 * This class represents an AVL tree
 * 
 * @author user
 *
 * @param <K> generic type extends Comparable
 * @param <V> generic type
 */
public class AVL<K extends Comparable<K>, V> extends BST<K, V> {
  /**
   * insert a node properly and throw exception if necessary
   * 
   * @param the given key
   * @param the given value
   * @throws DuplicateKeyException if key is already in the tree
   * @throws IllegalNullKeyException if key is null
   */
  public void insert(K key, V value) throws DuplicateKeyException, IllegalNullKeyException {
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
   * @throws IllegalNullKeyException
   */
  private BSTNode<K, V> insert(BSTNode<K, V> root, K key, V value)
      throws DuplicateKeyException, IllegalNullKeyException {
    if (root == null)
      return (new BSTNode<>(key, value));
    if (key.compareTo(root.key) < 0)
      root.left = insert(root.left, key, value);
    else if (key.compareTo(root.key) > 0)
      root.right = insert(root.right, key, value);
    else if (key.compareTo(root.key) == 0)
      throw new DuplicateKeyException();
    else
      return root;// basic BST insertion
    root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
    int balance = getBalance(root);
    // Left Left Case
    if (balance > 1 && key.compareTo(root.left.key) < 0)
      return rightRotate(root);
    // Right Right Case
    if (balance < -1 && key.compareTo(root.right.key) > 0)
      return leftRotate(root);
    // Left Right Case
    if (balance > 1 && key.compareTo(root.left.key) > 0) {
      root.left = leftRotate(root.left);
      return rightRotate(root);
    }
    // Right Left Case
    if (balance < -1 && key.compareTo(root.right.key) < 0) {
      root.right = rightRotate(root.right);
      return leftRotate(root);
    }
    return root;
  }

  /**
   * get the balance of the given node
   * 
   * @param N is the given node
   * @return the integer of balance of given node
   */
  private int getBalance(BSTNode<K, V> N) {
    if (N == null)
      return 0;
    return getHeight(N.left) - getHeight(N.right);
  }

  /**
   * The private method of right rotate
   * 
   * @param y is the given node
   * @return x the node reference.
   */
  private BSTNode<K, V> rightRotate(BSTNode<K, V> y) {
    BSTNode<K, V> x = y.left;
    BSTNode<K, V> temp = x.right;
    x.right = y;
    y.left = temp;
    y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
    x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
    return x;
  }

  /**
   * The private method of left rotate
   * 
   * @param x is the given node
   * @return the node reference
   */
  public BSTNode<K, V> leftRotate(BSTNode<K, V> x) {
    BSTNode<K, V> y = x.right;
    BSTNode<K, V> temp = y.left;
    y.left = x;
    x.right = temp;
    x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
    y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
    return y;
  }

  /**
   * This is the private method of get height
   * 
   * @param root is the given node
   * @return integer value of the height of given node
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

  /**
   * remove the node given by the key
   * 
   * @throws KeyNotFoundException if key is not found in the tree
   * @return true if the remove is successful
   * 
   */
  public boolean remove(K key) throws IllegalNullKeyException, KeyNotFoundException {
    if (!contains(key)) {
      throw new KeyNotFoundException();
    } else {
      root = remove(root, key);
      numKeys--;
      return true;
    }
  }

  /**
   * This is the private helper method of remove
   * 
   * @param root is the given node
   * @param key is the given key
   * @return node reference
   */
  private BSTNode<K, V> remove(BSTNode<K, V> root, K key) {
    if (root == null)
      return root;
    if (key.compareTo(root.key) < 0)
      root.left = remove(root.left, key);
    else if (key.compareTo(root.key) > 0)
      root.right = remove(root.right, key);
    else {
      if ((root.left == null) || (root.right == null)) {
        BSTNode<K, V> temp = null;
        if (temp == root.left)
          temp = root.right;
        else
          temp = root.left;
        if (temp == null) {
          temp = root;
          root = null;
        } else
          root = temp;
      } else {
        BSTNode<K, V> temp = minBSTNode(root.right);
        root.key = temp.key;
        root.right = remove(root.right, temp.key);
      }
    }
    if (root == null)
      return root;
    // basic BST remove method
    root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
    int balance = getBalance(root);
    // Left Left Case
    if (balance > 1 && getBalance(root.left) >= 0)
      return rightRotate(root);
    // Left Right Case
    if (balance > 1 && getBalance(root.left) < 0) {
      root.left = leftRotate(root.left);
      return rightRotate(root);
    }
    // Right Right Case
    if (balance < -1 && getBalance(root.right) <= 0)
      return leftRotate(root);
    // Right Left Case
    if (balance < -1 && getBalance(root.right) > 0) {
      root.right = rightRotate(root.right);
      return leftRotate(root);
    }
    return root;
  }

  /**
   * This is the private helper method to find the inorder successor
   * 
   * @param node is the given node
   * @return node reference
   */
  private BSTNode<K, V> minBSTNode(BSTNode<K, V> node) {
    BSTNode<K, V> current = node;
    while (current.left != null)
      current = current.left;
    return current;
  }

}
