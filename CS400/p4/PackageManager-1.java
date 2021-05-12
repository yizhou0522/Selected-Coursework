
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale.FilteringMode;
import java.util.Set;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Filename: PackageManager.java Project: p4 Authors: yizhou liu
 * <p>
 * PackageManager is used to process json package dependency files and provide function that make
 * that information available to other users.
 * <p>
 * Each package that depends upon other packages has its own entry in the json file.
 * <p>
 * Package dependencies are important when building software, as you must install packages in an
 * order such that each package is installed after all of the packages that it depends on have been
 * installed.
 * <p>
 * For example: package A depends upon package B, then package B must be installed before package A.
 * <p>
 * This program will read package information and provide information about the packages that must
 * be installed before any given package can be installed. all of the packages in
 * <p>
 * You may add a main method, but we will test all methods with our own Test classes.
 */
public class PackageManager {

    private Graph graph; // the graph of all packages
    public Package[] myPackage = null; //array of packages


    /**
     * Package Manager default no-argument constructor.
     */
    public PackageManager() {
        graph = new Graph();
    }

    /**
     * Takes in a file path for a json file and builds the package dependency graph from it.
     *
     * @param jsonFilepath the name of json data file with package dependency information
     * @throws FileNotFoundException if file path is incorrect
     * @throws IOException           if the give file cannot be read
     * @throws ParseException        if the given json cannot be parsed
     */
    public void constructGraph(String jsonFilepath)
        throws FileNotFoundException, IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(jsonFilepath)); //obj store the parsed
        // json
        JSONObject jo = (JSONObject) obj;
        JSONArray packages = (JSONArray) jo.get("packages"); //array of packages
        myPackage = new Package[packages.size()];
        for (int i = 0; i < packages.size(); i++) {
            //read JSON file and input info
            JSONObject jsonPackage = (JSONObject) packages.get(i);
            String packageName = (String) jsonPackage.get("name");
            JSONArray jsonDependencies = (JSONArray) jsonPackage.get("dependencies");
            String[] arr = new String[jsonDependencies.size()];
            for (int j = 0; j < arr.length; j++) {
                String dependency = (String) jsonDependencies.get(j);
                arr[j] = dependency;
            }
            Package currPackage = new Package(packageName, arr);
            myPackage[i] = currPackage;
        } // initialize myPackage array
        for (int i = 0; i < myPackage.length; i++) {
            graph.addVertex(myPackage[i].getName());
        } // first put all vertex in adjacency list and vertex array
        for (int i = 0; i < myPackage.length; i++) {
            for (int k = 0; k < myPackage[i].getDependencies().length; k++) {
                graph.addEdge(myPackage[i].getDependencies()[k],
                    myPackage[i].getName());// first depend
                // on last
            }
        }
    }


    /**
     * Helper method to get all packages in the graph.
     *
     * @return Set<String> of all the packages
     */
    public Set<String> getAllPackages() {
        return graph.getAllVertices();
    }



    /**
     * Given a package name, returns a list of packages in a valid installation order.
     * <p>
     * Valid installation order means that each package is listed before any packages that depend upon
     * that package. //Topological order
     *
     * @return List<String>, order in which the packages have to be installed
     * @throws CycleException           if you encounter a cycle in the graph while finding the installation
     *                                  order for a particular package. Tip: Cycles in some other part of the graph that do not
     *                                  affect the installation order for the specified package, should not throw this
     *                                  exception.
     * @throws PackageNotFoundException if the package passed does not exist in the dependency graph.
     */
    public List<String> getInstallationOrder(String pkg)
        throws CycleException, PackageNotFoundException {
        if (!graph.vertices.contains(pkg))
            throw new PackageNotFoundException();
        List<String> result = new ArrayList<>(); //the List to be returned
        Stack stack = new Stack();
        try {
            findDependcies1(pkg, result, stack);
        } catch (StackOverflowError e) {
            throw new CycleException();
        }
        while (!stack.isEmpty())
            result.add((String) stack.pop());
        result.add(pkg); //add packages to the result list
        result = removeDuplicate1(result);
        System.out.println(result);
        return result;
    }

    /**
     * Given two packages - one to be installed and the other installed, return a List of the packages
     * that need to be newly installed.
     * <p>
     * For example, refer to shared_dependecies.json - toInstall("A","B") If package A needs to be
     * installed and packageB is already installed, return the list ["A", "C"] since D will have been
     * installed when B was previously installed.
     *
     * @return List<String>, packages that need to be newly installed.
     * @throws CycleException           if you encounter a cycle in the graph while finding the dependencies of
     *                                  the given packages. If there is a cycle in some other part of the graph that doesn't
     *                                  affect the parsing of these dependencies, cycle exception should not be thrown.
     * @throws PackageNotFoundException if any of the packages passed do not exist in the dependency
     *                                  graph.
     */
    public List<String> toInstall(String newPkg, String installedPkg)
        throws CycleException, PackageNotFoundException {
        if (!graph.vertices.contains(installedPkg))
            throw new PackageNotFoundException();
        List<String> installed = new ArrayList<>(); //list containing installed packages
        List<String> result = new ArrayList<>();  //list containing packages to be installed
        result.add(newPkg);
        try {
            findDependcies(newPkg, result);
        } catch (StackOverflowError e) {
            throw new CycleException();
        }//use stackoverflowerror to detect cycle
        result = removeDuplicate1(result);//remove duplicates
        installed.add(installedPkg);
        try {
            findDependcies(installedPkg, installed);
        } catch (StackOverflowError e) {
            throw new CycleException();
        }
        installed = removeDuplicate1(installed);
        //loop indices
        int i = 0;
        int k = 0;
        while (i < result.size() && k < installed.size()) { //traverse
            while (k < installed.size()) {
                if (result.get(i).equals(installed.get(k))) {
                    result.remove(i);
                }
                k++;
            }
            i++;
            k = 0;
        } // delete the elements already in installed list from result list
        return result;

    }

    /**
     * helper method that remove the duplicate packages
     *
     * @param list list to clear out duplicates
     * @return list without duplicates
     */
    private List<String> removeDuplicate1(List<String> list) {
        Set<String> set = new LinkedHashSet<>();
        set.addAll(list);
        list.clear();
        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);
        // return the list
        return list;
    }

    /**
     * get all dependencies for a given package
     *
     * @param pkg    the package need to find dependency
     * @param answer
     */
    private void findDependcies(String pkg, List<String> answer) {//
        int i; //loop index
        for (i = 0; i < myPackage.length; i++) {
            if (myPackage[i].getName().equals(pkg))
                break; //find
        }
        if (i == myPackage.length) //reach end
            return;
        for (int k = 0; k < myPackage[i].getDependencies().length; k++) {
            //add dependencies
            answer.add(myPackage[i].getDependencies()[k]);
            findDependcies(myPackage[i].getDependencies()[k], answer);
        }
    }

    /**
     * helper method to find the dependency of a package
     *
     * @param pkg    the package for find dependency
     * @param answer the list containing the dependency
     * @param stack  the stack storing packages
     */
    private void findDependcies1(String pkg, List<String> answer, Stack stack) {// get all
        int i; //loop index
        for (i = 0; i < myPackage.length; i++) {
            if (myPackage[i].getName().equals(pkg))
                break;
        }
        if (i == myPackage.length)
            return;
        for (int k = 0; k < myPackage[i].getDependencies().length; k++) {
            //recursively find dependencies for the package
            stack.push(myPackage[i].getDependencies()[k]);
            findDependcies1(myPackage[i].getDependencies()[k], answer, stack);
        }

    }

    /**
     * helper method to detect cycle in the graph
     *
     * @param i        index
     * @param visited  array of visited packages
     * @param recStack recursive stack
     * @return true if there is cycle; false otherwise
     */
    private boolean isCyclicUtil(int i, boolean[] visited, boolean[] recStack) {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack[i])
            return true;
        if (visited[i])
            return false;
        //set to true
        visited[i] = true;
        recStack[i] = true;
        List<String> children = graph.edges.get(i); //list of packages
        for (String c : children) {
            int k; //loop index
            for (k = 0; k < myPackage.length; k++) {
                if (myPackage[k].getName().equals(c)) {
                    break;
                }
            }
            if (isCyclicUtil(k, visited, recStack)) //there is cycle
                return true;
        }
        recStack[i] = false;
        return false;
    }

    /**
     * detect if there is a cycle in the graph
     *
     * @return true if the graph contains a cycle, else false.
     */
    private boolean isCyclic() {// change to private
        // Mark all the vertices as not visited and
        // not part of recursion stack
        boolean[] visited = new boolean[graph.order()];
        boolean[] recStack = new boolean[graph.order()];
        // Call the recursive helper function to
        // detect cycle in different DFS trees
        for (int i = 0; i < graph.order(); i++)
            if (isCyclicUtil(i, visited, recStack))
                return true;
        return false;
    }

    /**
     * Return a valid global installation order of all the packages in the dependency graph.
     * <p>
     * assumes: no package has been installed and you are required to install all the packages
     * <p>
     * returns a valid installation order that will not violate any dependencies
     *
     * @return List<String>, order in which all the packages have to be installed
     * @throws CycleException if you encounter a cycle in the graph
     */
    public List<String> getInstallationOrderForAllPackages() throws CycleException {
        if (isCyclic())
            throw new CycleException();
        List<String> result = new ArrayList<>();
        result = topologicalSort(); //the installation order
        return result;
    }

    /**
     * helper method for topological sort
     *
     * @param v       index
     * @param visited if the vertice has been visited
     * @param stack   stores packages
     */
    private void topologicalSortUtil(int v, boolean visited[], Stack stack) {
        // Mark the current node as visited.
        visited[v] = true;
        Integer i;
        // Recur for all the vertices adjacent to this
        // vertex
        Iterator<String> it = graph.edges.get(v).iterator();
        int k = 0; //loop index
        while (it.hasNext()) {
            String record = it.next();
            for (k = 0; k < myPackage.length; k++) {
                if (myPackage[k].getName().equals(record))
                    break;
            }
            i = k;
            if (!visited[i])
                topologicalSortUtil(i, visited, stack); //sort recursively
        }
        // Push current vertex to stack which stores result
        String str = graph.vertices.get(v);
        stack.push(str);
    }

    /**
     * perform topological sort
     *
     * @return list of packages in topological order
     */
    private List<String> topologicalSort() {
        List<String> result = new ArrayList<>(); //result storing packages
        Stack stack = new Stack();
        // Mark all the vertices as not visited
        boolean visited[] = new boolean[graph.order()];
        for (int i = 0; i < graph.order(); i++)
            visited[i] = false;
        // Call the recursive helper function to store
        // Topological Sort starting from all vertices
        // one by one
        for (int i = 0; i < graph.order(); i++)
            if (visited[i] == false)
                topologicalSortUtil(i, visited, stack);
        while (stack.empty() == false)
            result.add((String) stack.pop());
        return result;
    }

    /**
     * Find and return the name of the package with the maximum number of dependencies.
     * <p>
     * Tip: it's not just the number of dependencies given in the json file. The number of
     * dependencies includes the dependencies of its dependencies. But, if a package is listed in
     * multiple places, it is only counted once.
     * <p>
     * Example: if A depends on B and C, and B depends on C, and C depends on D. Then, A has 3
     * dependencies - B,C and D.
     *
     * @return String, name of the package with most dependencies.
     * @throws CycleException if you encounter a cycle in the graph
     */
    public String getPackageWithMaxDependencies() throws CycleException {
        if (isCyclic())
            throw new CycleException();
        List<String> result = new ArrayList<>();
        result = topologicalSort();
        return result.get(result.size() - 1); //get the last in the topological order
    }

    /**
     * main method for PackageManager
     *
     * @param args arguments passed into main method
     */
    public static void main(String[] args) {
        System.out.println("PackageManager.main()");
    }

}
