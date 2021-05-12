// Files:            P1.java
// Author:           Yizhou Liu
// Email:            liu773@wisc.edu
/**
 * This class aims to test methods in Sym, SymTable classes and the expected
 * self-defined exceptions, i.e, EmptySymTableException and
 * DuplicateSymException
 */
public class P1My {
  /**
   * The main method serves to test all the possible cases in Sym, SymTable
   * classes
   */
  public static void main(String[] args) throws DuplicateSymException, EmptySymTableException {
    // test type
    Sym sym = new Sym("test");
    if (!sym.getType().equals("test")) {
      System.out.println("Error: sym returns the wrong type");
    }
    // test emptysymtable exception
    SymTable st = new SymTable();
    // System.out.println(st.);
    st.removeScope(); // now the list is empty, i.e., without hashmap
    try {
      st.addDecl("test", sym);
      System.out.println("Error: addDecl does not throw EmptySymTableException");
    } catch (EmptySymTableException e) {
    } catch (Exception e) {
      System.out.println("Error: addDecl throws wrong exception");
    }
    try {
      Sym res = st.lookupLocal("test");
      System.out.println("Error: lookupLocal does not throw EmptySymTableException");
    } catch (EmptySymTableException e) {
    } catch (Exception e) {
      System.out.println("Error: lookupLocal throws wrong exception");
    }
    try {
      Sym res = st.lookupGlobal("test");
      System.out.println("Error: lookupGlobal does not throw EmptySymTableException");
    } catch (EmptySymTableException e) {
    } catch (Exception e) {
      System.out.println("Error: lookupGlobal throws wrong exception");
    }
    try {
      st.removeScope();
      System.out.println("Error: removeScope does not throw EmptySymTableException");
    } catch (EmptySymTableException e) {
    } catch (Exception e) {
      System.out.println("Error: removeScope throws wrong exception");
    }
    // test illegalargument exception
    try {
      st.addDecl(null, sym);
      System.out.println("Error: addDecl does not throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    } catch (Exception e) {
      System.out.println("Error: addDecl throws wrong exception");
    }
    try {
      st.addDecl("test", null);
      System.out.println("Error: addDecl does not throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    } catch (Exception e) {
      System.out.println("Error: addDecl throws wrong exception");
    }
    // test duplicatesym exception
    try {
      st.addScope();
      st.addDecl("test1", sym);
      st.addDecl("test1", sym); // now st should contain one hashmap with name "test1",
      // while adding other one should fail
      System.out.println("Error: addDecl does not throw DuplicateSymException_1");
    } catch (DuplicateSymException e) {
    } catch (Exception e) {
      System.out.println("Error: addDecl throws wrong exception");
    }
    // Below is for non-exception cases
    // test addDecl, lookupLocal, lookupGlobal
    // case 1
    SymTable st1 = new SymTable();
    st1.addDecl("test1", sym);
    try {
      if (st1.lookupLocal("test1") != sym) {
        System.out.println("Error: lookupLocal does not work");
      }
    } catch (Exception e) {
      System.out.println("Error: lookupLocal throws wrong exception");
    }
    try {
      if (st1.lookupGlobal("test1") != sym) {
        System.out.println("Error: lookupGlobal does not work");
      }
    } catch (Exception e) {
      System.out.println("Error: lookupGlobal throws wrong exception");
    }
    // case 2
    SymTable st2 = new SymTable();
    st2.addDecl("test1", sym);
    st2.addScope();
    st2.addDecl("test2", sym);
    // now test2 should be the front
    try {
      if (st2.lookupLocal("test1") != null) {
        System.out.println("Error: lookupLocal does not work");
      }
    } catch (Exception e) {
      System.out.println("Error: lookupLocal throws wrong exception");
    }
    try {
      if (st2.lookupGlobal("test1") != sym) {
        System.out.println("Error: lookupGlobal does not work");
      }
    } catch (Exception e) {
      System.out.println("Error: lookupGlobal throws wrong exception");
    }
    try {
      if (st2.lookupGlobal("test3") != null) {
        System.out.println("Error: lookupGlobal does not work");
      }
    } catch (Exception e) {
      System.out.println("Error: lookupGlobal throws wrong exception");
    }
    // case 3
    SymTable st3 = new SymTable();
    try {
      st3.addDecl("test1", sym);
      st3.addScope();
      st3.addDecl("test2", sym);
      st3.removeScope();
      st3.addScope();
      st3.addDecl("test3", sym);
      // should remove test2
      // since the above code covers the lookup methods, lookup methods should
      // work as expected
      if (st3.lookupLocal("test2") != null) {
        System.out.println("Error: removeScope does not work");
      }
    } catch (Exception e) {
      System.out.println("Error: wrong exceptions are thrown");
    }

  }
}
