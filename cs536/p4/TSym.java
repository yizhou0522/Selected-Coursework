// Yizhou Liu, liu773@wisc.edu
import java.util.*;

public class TSym {
    private String type;

    public TSym(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }

}
/**
 * variable node
 */
class VarTSym extends TSym {
    private String type;

    public VarTSym(String type) {
        super(null);
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
/**
 * Function node
 */
class FnTSym extends TSym {
    private String returnType;
    private List<String> formalType;

    public FnTSym(String returnType, List<String> formalType) {
        super(null);
        this.returnType = returnType;
        this.formalType = formalType;
    }

    public String toString() {
        return String.join(",",formalType)+"->"+returnType;
    }
}

/**
 * Struct node
 */
class StructTSym extends TSym {
    private String type;
    private SymTable table;

    public StructTSym(String type, SymTable table) {
        super(null);
        this.type = type;
        this.table = table;
    }

    public StructTSym(SymTable table) {
        super(null);
        this.table = table;
    }

    public String toString() {
        return type;
    }

    public SymTable getTable() {
        return table;
    }
}
