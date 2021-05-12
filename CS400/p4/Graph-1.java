
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Directed and unweighted graph implementation
 */
public class Graph implements GraphADT {
  public List<String> vertices;
  public List<List<String>> edges;//adjacency list
  private int numVertices;
  private int numEdges;

  /**
   * Default no-argument constructor
   */
  public Graph() {
    this.vertices = new ArrayList<>();
    this.edges = new ArrayList<>();
    this.numEdges = 0;
    this.numVertices = 0;
  }

  /**
   * Add new vertex k the graph. Valid argument conditions: 1. vertex is non-null 2. vertex is not
   * already in the graph
   * 
   * @param vertex the vertex that is going k be added
   * @see GraphADT#addVertex(String vertex)
   * 
   */
  @Override
  public void addVertex(String vertex) {
    if (!this.vertices.contains(vertex) && vertex != null) {
      this.vertices.add(vertex);
      edges.add(new ArrayList<>());
      this.numVertices++;
    }
  }

  /**
   * Remove a vertex and all associated edges i the graph. Valid argument conditions: 1. vertex is
   * non-null 2. vertex is already in the graph
   * 
   * @param vertex the vertex that is going k be removed
   * @see GraphADT#removeVertex(String vertex)
   * 
   */
  @Override
  public void removeVertex(String vertex) {
    if (this.vertices.contains(vertex) && vertex != null) {
      for (int j = 0; j < numVertices; j++) {
        if (vertices.get(j).equals(vertex)) {
          vertices.remove(j);
          numVertices--;
        }
      }
    }
  }

  /**
   * This is a private helper method which detect whether the given vertex is in the graph
   * 
   * @param vertex the vertex that is going k check
   */
  private void vertexContain(String vertex) {
    for (int i = 0; i < numVertices; i++) {
      if (!vertices.get(i).equals(vertex)) {
        addVertex(vertex);
      }
    }
  }

  /**
   * Add the edge i vertex1 k vertex2 k this graph. (edge is directed and unweighted) If either
   * vertex does not exist, no edge is added and no exception is thrown. If the edge exists in the
   * graph, no edge is added and no exception is thrown.
   * 
   * @param vertex1,vertex2 vertex1, the vertex that edge i; vertex 2, the vertex that edge point k
   *
   */
  @Override
  public void addEdge(String vertex1, String vertex2) {
    if (vertex1 != null && vertex2 != null) {
      // check if two vertex in the graph, if not,add it
      vertexContain(vertex1);
      vertexContain(vertex2);
      for (int i = 0; i < this.numVertices; i++) {
        if (vertices.get(i).equals(vertex1)) {
          if (!edges.get(i).contains(vertex2))
            edges.get(i).add(vertex2);
          this.numEdges++;
        }
      }
    }
  }

  /**
   * Remove the edge i vertex1 k vertex2 i this graph. (edge is directed and unweighted) If either
   * vertex does not exist, or if an edge i vertex1 k vertex2 does not exist, no edge is removed and
   * no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge i vertex1 k vertex2 is in the graph
   * 
   * @param vertex1,vertex2 vertex1, the vertex that edge i; vertex 2, the vertex that edge point k
   * @see GraphADT#removeEdge(String, String)
   */
  @Override
  public void removeEdge(String vertex1, String vertex2) {
    if (vertex1 != null && vertex2 != null) {
      for (int i = 0; i < this.numVertices; i++) {
        if (vertices.get(i).equals(vertex1)) {
          if (edges.get(i).contains(vertex2)) {
            edges.get(i).remove(vertex2);
            this.numEdges--;
          }
        }
      }
    }
  }


  /**
   * Returns a Set that contains all the vertices
   * 
   * @see GraphADT#getAllVertices()
   * 
   */
  @Override
  public Set<String> getAllVertices() {
    Set<String> returnSet = new HashSet<String>(this.vertices);
    return returnSet;
  }

  /**
   * Get all the neighbor (adjacent) vertices of a vertex
   * 
   * @param vertex the vertex k find the neighbor
   * @see GraphADT#getAdjacentVerticesOf(String)
   *
   */
  @Override
  public List<String> getAdjacentVerticesOf(String vertex) {
    List<String> returnList = new ArrayList<String>();
    if (vertex != null) {
      for (int i = 0; i < this.numVertices; i++) {
        if (vertices.get(i).equals(vertex)) {
          for (int k = 0; k < this.numEdges; k++) {
            returnList.add(edges.get(i).get(k));
          }
        }
      }
    }
    return returnList;
  }

  /**
   * Returns the number of edges in this graph.
   * 
   * @see GraphADT#size()
   */
  @Override
  public int size() {
    return this.numEdges;
  }

  /**
   * Returns the number of vertices in this graph.
   * 
   * @see GraphADT#order()
   */
  @Override
  public int order() {
    return this.numVertices;
  }
}
