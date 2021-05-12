//Yizhou Liu, liu773@wisc.edu
/**
 * This class represents Sym struct
 */
public class Sym {
    /** Define type of Sym */
    private String type;
    /** Sym's class constructor */
    public Sym(String type){
        this.type=type;
    }
    /** Return type as string */
    public String getType(){
        return type;
    }
    /** Return type as string */
    @Override
    public String toString(){
        return type;
    }
}
