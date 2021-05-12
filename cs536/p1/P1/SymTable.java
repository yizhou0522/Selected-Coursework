import java.util.*;
//Yizhou Liu, liu773@wisc.edu
/**This class represents SymTable that is used to store all the symbols */
public class SymTable {
    private LinkedList<HashMap<String,Sym>> list;
    //list used to store hashmap

    /** SymTable's constructor */
    public SymTable(){
        list=new LinkedList<>();
        list.addFirst(new HashMap<String,Sym>());
    }

    /** Add user-defined <name, sym> to hashmap */
    public void addDecl(String name, Sym sym) throws DuplicateSymException, EmptySymTableException{
        if (list.isEmpty()){
            throw new EmptySymTableException();
        }
        // If myString is null, then calling myString.equals(null) or myString.equals("") will fail with a NullPointerException. You cannot call any instance methods on a null variable.
        if(name==null||sym==null){
            throw new IllegalArgumentException();
        }
        if(list.getFirst().containsKey(name)){
            throw new DuplicateSymException();
        }
        list.getFirst().put(name,sym);
    }

    /** Add a new hashmap */
    public void addScope(){
        list.addFirst(new HashMap<String,Sym>());
    }

    /** Look for name in the first hashmap */
    public Sym lookupLocal(String name) throws EmptySymTableException{
        if (list.isEmpty()){
            throw new EmptySymTableException();
        }
        if(list.getFirst().containsKey(name)){
            return list.getFirst().get(name);
        }
        return null;
    }

    /** Look for name in all hashmaps */
    public Sym lookupGlobal(String name) throws EmptySymTableException{
        if (list.isEmpty()){
            throw new EmptySymTableException();
        }
        for (HashMap<String,Sym> map: list){
            if(map.containsKey(name)){
                return map.get(name);
            }
        }
        return null;
    }

    /** Remove hashmap from the list */
    public void removeScope() throws EmptySymTableException{
        // System.out.println(list.isEmpty());
        if (list.isEmpty()){
            throw new EmptySymTableException();
        }
        list.removeFirst();
    }

    /** Print the result as expected format */
    public void print(){
        System.out.print("\nSym Table\n");
        for (HashMap<String,Sym> map: list){
            System.out.println(map.toString());
        }
        System.out.println();
    }

}
