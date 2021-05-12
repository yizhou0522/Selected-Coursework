
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the methods within the DS_My class
 * 
 * @author user
 *
 * @param <T> the type of dataStructureInstance
 */
abstract class DataStructureADTTest<T extends DataStructureADT<String, String>> {

  private T dataStructureInstance;

  protected abstract T createInstance();

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    dataStructureInstance = createInstance();
  }

  @AfterEach
  void tearDown() throws Exception {
    dataStructureInstance = null;
  }

  /**
   * checks if an empty data structure has a size number of 0
   */
  @Test
  void test00_empty_dataStructureInstance_size() {

    if (dataStructureInstance.size() != 0)
      fail("data structure should be empty, with size=0, but size=" + dataStructureInstance.size());
  }

  /**
   * check if the size turns to one after one object is inserted
   */
  @Test
  void test01_after_insert_one_size_is_one() {

    // dataStructureInstance=createInstance();
    for (int i = 0; i < 50; i++) {
      dataStructureInstance.insert("" + i, "cs400");
    }
    for (int i = 0; i < 25; i++) {
      dataStructureInstance.remove("" + i);
    }
    if (dataStructureInstance.size() != 25)
      fail("data structure should have a size of 25, but size=" + dataStructureInstance.size());
  }

  /**
   * check if the size turns to 0 after one object is removed.
   */
  @Test
  void test02_after_insert_one_remove_one_size_is_0() {
    dataStructureInstance.insert(new String("1"), "cs400");
    dataStructureInstance.remove(new String("1"));
    if (dataStructureInstance.size() != 0)
      fail("data structure should have a size of 0, but size=" + dataStructureInstance.size());
  }

  /**
   * check if the right exception is thrown after a duplicate object is added
   */
  @Test
  void test03_duplicate_exception_is_thrown() {
    dataStructureInstance.insert(new String("1"), "cs400");
    dataStructureInstance.insert(new String("2"), "cs200");
    dataStructureInstance.insert(new String("3"), "cs300");
    try {
      dataStructureInstance.insert(new String("1"), "cs300");
      fail("data structure should throw a RunTimeException, but it does not.");
    } catch (RuntimeException e) {
    } catch (Exception e) {
      fail("data structure should throw a RunTimeException, but it does not.");
    }
  }

  /**
   * check if remove method returns false after being called to remove a key that does not present
   */
  @Test
  void test04_remove_returns_false_when_key_not_present() {

    dataStructureInstance.insert(new String("1"), "cs400");
    dataStructureInstance.insert(new String("2"), "cs200");
    dataStructureInstance.insert(new String("3"), "cs300");
    try {
      if (dataStructureInstance.remove(new String("4")) == true)
        fail("data structure should return false, but it returns true");
    } catch (Exception e) {
      fail("data structure should return false, but it throws exception");
    }
  }

  /**
   * check if remove method returns true after being called to remove an object
   */
  @Test
  void test05_insert_one_remove_it_returns_true() {

    dataStructureInstance.insert(new String("1"), "cs400");
    try {
      if (dataStructureInstance.remove(new String("1")) == false)
        fail("data structure should return true, but it returns false.");
    } catch (Exception e) {
      fail("data structure should return true, but it throws an exception");
    }
  }

  /**
   * test if data structure contains the key that has been removed
   */
  @Test
  void test06_contains_returns_false() {
    dataStructureInstance.insert(new String("1"), "cs400");
    dataStructureInstance.insert(new String("2"), "cs400");
    dataStructureInstance.remove("2");
    if (dataStructureInstance.contains("2") == true)
      fail("data structure should return false, but it returns true.");
  }

  /**
   * check to see if data stucture can store at most 500 elements
   */
  @Test
  void test07_insert_500_items() {
    for (int i = 0; i < 500; i++) {
      dataStructureInstance.insert("" + i, "cs400");
    }
    if (dataStructureInstance.size() != 500)
      fail("data structure cannot insert at most 500 elements");
  }

  // TODO: add tests to ensure that you can detect implementation that fail

  // Tip: consider different numbers of inserts and removes and how different combinations of insert
  // and removes


}
