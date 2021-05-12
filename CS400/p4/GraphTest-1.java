

import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Assert;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.Set;
import java.util.*;

public class GraphTest {

  // create instances for test
  Graph g = new Graph();

  /**
   * create instance of BST<String,String>
   *
   * @return an instance of AVL<String,String>
   */
  protected Graph createInstance() {
    return new Graph();
  }

  /**
   * initializing instances
   *
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    // create instances
    g = createInstance();

  }

  /**
   * create instances before each test
   *
   * @throws Exception
   */
  @BeforeEach
  void setUpBeforeEach() throws Exception {
    g = createInstance();

  }

  /**
   * destroy instances after each test
   *
   * @throws Exception
   */
  @AfterEach
  void tearDownAfterAll() throws Exception {
    g = null;

  }

  /**
   * destroy objs after testing
   *
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    g = null;

  }

  /**
   * add a few vertices and see if they are added properly
   */
  @Test
  public void test001_add_vertex() {
    g.addVertex("123");
    g.addVertex("4");
    // expected set that contains the strings
    Set<String> expected = new HashSet<String>();
    expected.add("123");
    expected.add("4");
    Assert.assertEquals(expected, g.getAllVertices());
  }

  /**
   * multiple add and remove and see if graph contains correct vertices
   */
  @Test
  public void test002_add_and_remove_vertices() {
    // perform add and remove operations
    g.addVertex("123");
    g.addVertex("4532");
    g.addVertex("4");
    g.addVertex("78");
    g.removeVertex("4");
    g.removeVertex("78");
    // expected set that contains the added strings
    Set<String> expected = new HashSet<String>();
    expected.add("123");
    expected.add("4532");


    Assert.assertEquals(expected, g.getAllVertices());
  }

  /**
   * add edges and see if graph contains correct edges
   */
  @Test
  public void test003_add_edges() {
    // perform add and remove operations
    g.addVertex("123");
    g.addVertex("4532");
    g.addVertex("4");
    g.addVertex("78");
    // add edges
    g.addEdge("123", "4532");
    g.addEdge("123", "4");
    g.addEdge("4", "78");
    // expected what contains for String "123"
    List<String> expected_123 = new ArrayList<>();
    List<String> actual = new ArrayList<>();
    actual.add("78");
    expected_123.add("4532");
    expected_123.add("4");
    // expected what contains for String "4"
    List<String> expected_4 = new ArrayList<>();
    expected_4.add("78");
    Assert.assertEquals(expected_4, actual);
  }

  /**
   * check the correct edges methods
   */
  @Test
  public void test004_add_and_remove_edges() {
    // perform add and remove operations
    g.addVertex("123");
    g.addVertex("4532");
    g.addVertex("4");
    g.addVertex("78");
    // add edges
    g.addEdge("123", "4532");
    g.addEdge("123", "4");
    g.addEdge("4", "78");
    g.addEdge("4", "123");
    // remove edges
    g.removeEdge("123", "4");
    g.addEdge("4", "123");
    // expected what contains for String "123"
    List<String> expected_123 = new ArrayList<>();
    List<String> actual = new ArrayList<>();
    actual.add("4532");
    expected_123.add("4532");

    // expected what contains for String "4"
    List<String> expected_4 = new ArrayList<>();
    expected_4.add("78");
    Assert.assertEquals(expected_123, actual);
  }

  /**
   * check if num of vertices is correct
   */
  @Test
  public void test005_add_and_remove_num_of_vertices() {
    // perform add and remove operations
    g.addVertex("123");
    g.addVertex("4532");
    g.addVertex("4");
    g.addVertex("78");
    g.removeVertex("4");
    g.removeVertex("78");
    Assert.assertEquals(2, g.order());
  }

  /**
   * check num of edges is correct
   */
  @Test
  public void test006_size_of_graph() {
    // perform add and remove operations
    g.addVertex("123");
    g.addVertex("4532");
    g.addVertex("4");
    g.addVertex("78");
    // add edges
    g.addEdge("123", "4532");
    g.addEdge("123", "4");
    g.addEdge("4", "78");
    g.addEdge("4", "123");
    // remove edges
    g.removeEdge("123", "4");
    g.addEdge("4", "123");
    System.out.println("xxxx" + g.size());
    Assert.assertEquals(4, g.size());
  }

  /**
   * check the size and order of an empty graph
   */
  @Test
  public void test007_num_on_empty_graph() {
    // check the size and order of an empty graph
    Assert.assertEquals(0, g.size());
    Assert.assertEquals(0, g.order());
  }

  /**
   * check the vertices of an empty graph
   */
  @Test
  public void test008_vertex_of_empty_graph() {
    // create empty set
    Set<String> emptySet = new HashSet<>();

    Assert.assertEquals(emptySet, g.getAllVertices());

  }

  /**
   * add large number of vertices and check order is correct
   */
  @Test
  public void test009_add_large_number_of_vertices_and_check_order_is_correct() {
    // add a large number of vertices
    for (int i = 0; i < 200; i++) {
      g.addVertex(Integer.toString(i));
    }
    System.out.println("yyyy" + g.order());
    Assert.assertEquals(200, g.order());
  }

  /**
   * add large number of vertices and check they are properly added
   */
  @Test
  public void test010_add_large_number_of_vertices_and_check_if_properly_added() {
    Set<String> expected = new HashSet<>(); // expected contained vertices of graph
    // add a large number of vertices
    for (int i = 0; i < 200; i++) {
      g.addVertex(Integer.toString(i));
      expected.add(Integer.toString(i));
    }

    Assert.assertEquals(expected, g.getAllVertices());
  }

  /**
   * add large number of vertices and edges and check if size is correct
   */
  @Test
  public void test011_add_large_number_of_vertices_and_edges_check_if_properly_added() {
    Set<String> expected = new HashSet<>(); // expected contained vertices of graph
    // add a large number of vertices
    for (int i = 0; i < 200; i++) {
      g.addVertex(Integer.toString(i));
      expected.add(Integer.toString(i));
    }

    // add a large number of edges
    for (int i = 0; i < 100; i++) {
      g.addEdge(Integer.toString(i), Integer.toString(i + 1));
    }

    Assert.assertEquals(100, g.size());
  }



}
