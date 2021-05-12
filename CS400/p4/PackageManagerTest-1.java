
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;
import java.util.*;

public class PackageManagerTest {
  // create instances for test
  PackageManager pm = new PackageManager();

  /**
   * create instance of BST<String,String>
   *
   * @return an instance of AVL<String,String>
   */
  protected PackageManager createInstance() {
    return new PackageManager();
  }

  /**
   * initializing instances
   *
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    // create instances
    pm = createInstance();

  }

  /**
   * create instances before each test
   *
   * @throws Exception
   */
  @BeforeEach
  void setUpBeforeEach() throws Exception {
    pm = createInstance();

  }

  /**
   * destroy instances after each test
   *
   * @throws Exception
   */
  @AfterEach
  void tearDownAfterAll() throws Exception {
    pm = null;

  }

  /**
   * destroy objs after testing
   *
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    pm = null;

  }

  /**
   * check if FileNotFoundException is thrown for PM constructor
   * 
   * @throws IOException
   * @throws ParseException for invalid json file
   */
  @Test
  public void test001_FileNotFoundException_for_PM_constructor()
      throws IOException, ParseException {
    boolean excpThrown = false; // check excp is thrown

    try {
      pm.constructGraph("13435"); // invalid path
    } catch (FileNotFoundException e) {
      excpThrown = true;
    }
    if (!excpThrown) {
      Assertions.fail("excp should throw when null key is removed!");
    }

  }

  /**
   * check if exception is thrown for a VALID json file
   * 
   * @throws IOException
   * @throws ParseException should not throw
   */
  @Test
  public void test002_FileNotFoundException_for_valid_json() throws IOException, ParseException {
    boolean excpThrown = false; // check excp is thrown

    try {
      pm.constructGraph("valid.json"); // valid path
    } catch (FileNotFoundException e) {
      excpThrown = true;
    } catch (Exception e) {
      excpThrown = true;
    }
    if (excpThrown) {
      Assertions.fail("excp should not throw for valid json file!");
    }

  }

  /**
   *
   * @throws FileNotFoundException
   * @throws IOException
   * @throws ParseException
   */
  @Test
  public void test003_get_all_packages() throws FileNotFoundException, IOException, ParseException {
    pm.constructGraph("valid.json");
    // expected content for what returns
    Set<String> expected = new HashSet<>();
    expected.add("A");
    expected.add("B");
    expected.add("C");
    expected.add("D");
    expected.add("E");
    System.out.println( pm.getAllPackages());
    Assert.assertEquals(expected, pm.getAllPackages());
  }

  /**
   * check the toInstall() method if return correct list of packages
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * @throws ParseException
   * @throws CycleException
   * @throws ParseException
   */
  @Test
  public void test004_toInstall() throws FileNotFoundException, IOException, ParseException,
      CycleException, ParseException, PackageNotFoundException {

    pm.constructGraph("shared_dependencies.json");
    // expected content for what returns
    List<String> expected = new ArrayList<>();
    expected.add("A");
    expected.add("C");

    Assert.assertEquals(expected, pm.toInstall("A", "B"));
  }

  /**
   * test the getInstallOrder() method
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * @throws CycleException
   * @throws PackageNotFoundException
   * @throws ParseException
   */
  @Test
  public void test005_get_install_order() throws FileNotFoundException, IOException, CycleException,
      PackageNotFoundException, ParseException {
    pm.constructGraph("valid.json");
    // expected content for what returns
    List<String> expected = new ArrayList<>();
    expected.add("D");
    expected.add("C");
    expected.add("B");
    expected.add("A");
    Assert.assertEquals(expected, pm.getInstallationOrder("A"));

  }

  /**
   * check the getInstallationOrderForAllPackages() method
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * @throws CycleException
   * @throws PackageNotFoundException
   * @throws ParseException
   */
  @Test
  public void test006_getInstallationOrderForAllPackages() throws FileNotFoundException,
      IOException, CycleException, PackageNotFoundException, ParseException {
    pm.constructGraph("valid.json");
    // expected content for what returns
    List<String> expected = new ArrayList<>();
    expected.add("D");
    expected.add("C");
    expected.add("B");
    expected.add("E");
    expected.add("A");
    System.out.println(pm.getInstallationOrderForAllPackages());
    Assert.assertEquals(expected, pm.getInstallationOrderForAllPackages());
  }


  /**
   * check getPackageWithMaxDependencies() method
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * @throws CycleException
   * @throws PackageNotFoundException
   * @throws ParseException
   */
  @Test
  public void test007_getPackageWithMaxDependencies() throws FileNotFoundException, IOException,
      CycleException, PackageNotFoundException, ParseException {
    pm.constructGraph("shared_dependencies.json");
    Assert.assertEquals("A", pm.getPackageWithMaxDependencies());
  }

  /**
   * check if getInstallationOrder() throws PackageNotFoundException
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * @throws CycleException
   * @throws PackageNotFoundException
   * @throws ParseException
   */
  @Test
  public void test008_getInstallationOrder_throws_PackageNotFoundException()
      throws FileNotFoundException, IOException, CycleException, PackageNotFoundException,
      ParseException {
    boolean excpThrown = false; // check excp is thrown
    try {
      pm.constructGraph("shared_dependencies.json"); // invalid path
      pm.getInstallationOrder("24543");
    } catch (PackageNotFoundException e) {
      excpThrown = true;
    }
    if (!excpThrown) {
      Assertions.fail("excp should throw when null key is removed!");
    }
  }

  /**
   * check if toInstall() throws PackageNotFoundException
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * @throws CycleException
   * @throws PackageNotFoundException
   * @throws ParseException
   */
  @Test
  public void test009_toInstall_throws_PackageNotFoundException() throws FileNotFoundException,
      IOException, CycleException, PackageNotFoundException, ParseException {
    boolean excpThrown = false; // check excp is thrown
    try {
      pm.constructGraph("shared_dependencies.json"); // invalid path
      pm.toInstall("435", "25443");
    } catch (PackageNotFoundException e) {
      excpThrown = true;
    }
    if (!excpThrown) {
      Assertions.fail("excp should throw when null key is removed!");
    }
  }

  /**
   * helper method to see content of an arrayList
   * 
   * @param arrayList passed in arrayList
   */
  public static void printArrayList(ArrayList<String> arrayList) {
    for (String s : arrayList) {
      System.out.println(s);
    }

  }

  /**
   * helper method to see content of a set
   * 
   * @param set passed in set
   */
  public static void printSet(HashSet<String> set) {
    for (String s : set) {
      System.out.println(s);
    }

  }



}


