
// Yizhou Liu, liu773@wisc.edu
import java.io.*;
import java.util.*;

import javax.print.DocFlavor.INPUT_STREAM;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a C-- program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Children
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//       StructDeclNode    IdNode, DeclListNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//       StructNode        IdNode
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PostIncStmtNode     ExpNode
//       PostDecStmtNode     ExpNode
//       ReadStmtNode        ExpNode
//       WriteStmtNode       ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       RepeatStmtNode      ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       DotAccessNode       ExpNode, IdNode
//       AssignNode          ExpNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of children, or
// internal nodes with a fixed number of children:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of children:
//        ProgramNode,     VarDeclNode,     FnDeclNode,     FormalDeclNode,
//        StructDeclNode,  FnBodyNode,      StructNode,     AssignStmtNode,
//        PostIncStmtNode, PostDecStmtNode, ReadStmtNode,   WriteStmtNode
//        IfStmtNode,      IfElseStmtNode,  WhileStmtNode,  RepeatStmtNode,
//        CallStmtNode
//        ReturnStmtNode,  DotAccessNode,   AssignExpNode,  CallExpNode,
//        UnaryExpNode,    BinaryExpNode,   UnaryMinusNode, NotNode,
//        PlusNode,        MinusNode,       TimesNode,      DivideNode,
//        AndNode,         OrNode,          EqualsNode,     NotEqualsNode,
//        LessNode,        GreaterNode,     LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode {
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

    abstract public void nameAnalysis(SymTable symTable);

    // this method can be used by the unparse methods to do indenting
    protected void addIndentation(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++)
            p.print(" ");
    }
}

// **********************************************************************
// ProgramNode, DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************

class ProgramNode extends ASTnode {
    public ProgramNode(DeclListNode L) {
        myDeclList = L;
    }

    public void nameAnalysis(SymTable symTable) {
        myDeclList.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }

    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }

    public void nameAnalysis(SymTable symTable) {
        try {
            for (DeclNode elt : myDecls) {
                elt.nameAnalysis(symTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException");
            System.exit(-1);
        }
    }

    public void nameAnalysis(SymTable symTable, SymTable globalSymTable) {
        try {
            for (DeclNode elt : myDecls) {
                ((VarDeclNode) elt).nameAnalysis(symTable, globalSymTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException");
            System.exit(-1);
        }

    }

    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode) it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException");
            System.exit(-1);
        }
    }

    private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    public void nameAnalysis(SymTable symTable) {
        try {
            for (FormalDeclNode elt : myFormals) {
                elt.nameAnalysis(symTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException");
            System.exit(-1);
        }
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) { // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    public List<String> getType() {
        List<String> res = new LinkedList<String>();
        try {
            for (FormalDeclNode elt : myFormals) {
                res.add(elt.toString());
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException");
            System.exit(-1);
        }
        return res;
    }

    private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    public void nameAnalysis(SymTable symTable) {
        myDeclList.nameAnalysis(symTable);
        myStmtList.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }

    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    public void nameAnalysis(SymTable symTable) {

        try {
            for (StmtNode elt : myStmts) {
                elt.nameAnalysis(symTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException");
            System.exit(-1);
        }

    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }

    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public void nameAnalysis(SymTable symTable) {

        try {
            for (ExpNode elt : myExps) {
                elt.nameAnalysis(symTable);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException");
            System.exit(-1);
        }
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) { // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************

abstract class DeclNode extends ASTnode {
}

/**
 * Node for variable declaration
 */
class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    public void nameAnalysis(SymTable symTable) {
        nameAnalysis(symTable, symTable);
    }

    public void nameAnalysis(SymTable symTable, SymTable globalSymTable) {

        boolean isVoid;
        TSym globalSym;
        String idName;
        String typeName;

        idName = myId.getName();
        typeName = myType.toString();

        //check if type is int or bool
        if (myType instanceof IntNode || myType instanceof BoolNode) {
            try {
                TSym varSym = new VarTSym(typeName);
                //add declartion if satify requirements
                globalSymTable.addDecl(idName, varSym);
            } catch (DuplicateSymException e) {
                ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgument Exception occurs!");
            } catch (EmptySymTableException e) {
                System.err.println("EmptySymTableException in VarDeclNode.nameAnalysis()");
            }
        }
        //check if type is void; if void, prepare to print error msg
        if (myType instanceof VoidNode) {
            try {
                isVoid = true;
                ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Non-function declared void");
                if (globalSymTable.lookupLocal(idName) != null) {
                    ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
                }
            } catch (EmptySymTableException e) {
                System.err.println("EmptySymTableException in VarDeclNode.nameAnalysis()");
            }
        }
        //check if type is struct
        if (myType instanceof StructNode) {
            try {
                // find declared struct
                globalSym = symTable.lookupGlobal(typeName);
                if (globalSym == null) {
                    ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Invalid name of struct type");
                    return;
                }
                //check if varName has been used in the program
                if (symTable.lookupLocal(idName) != null) {
                    ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
                    return;
                }
                TSym structSym = new StructTSym(typeName, ((StructTSym) globalSym).getTable());
                globalSymTable.addDecl(idName, structSym);
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgument Exception occurs!");
            } catch (Exception e) {
                System.err.println("EmptySymTableException in VarDeclNode.nameAnalysis()");
            }
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.println(";");
    }

    private TypeNode myType;
    private IdNode myId;
    private int mySize;

    public static int NOT_STRUCT = -1;
}

class FnDeclNode extends DeclNode {
    public FnDeclNode(TypeNode type, IdNode id, FormalsListNode formalList, FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public void nameAnalysis(SymTable symTable) {
        String idName = myId.getName();
        try {
            //check if func name has been used in the same scope
            if (symTable.lookupLocal(idName) != null) {
                ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), " Multiply declared identifier");
            }
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }

        try {
            //add func decl with given name and param list to the symtable
            TSym fnSymTable = new FnTSym(myType.toString(), myFormalsList.getType());
            symTable.addDecl(idName, fnSymTable);
        } catch (DuplicateSymException e) {
            ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgument Exception occurs!");
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
        // add scope in hashtable
        symTable.addScope();
        myFormalsList.nameAnalysis(symTable);
        myBody.nameAnalysis(symTable);
        try {
            //remove it after nameAnalysis method
            symTable.removeScope();
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent + 4);
        p.println("}\n");
    }

    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    public void nameAnalysis(SymTable symTable) {
        if (myType.toString() == "void") {
            ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Non-function declared void");
        }
        try {
            //check if name has been used in the same scope
            if (symTable.lookupLocal(myId.getName()) != null) {
                ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
            }
        } catch (EmptySymTableException e) {
            System.out.println("EmptySymTableException occurs!");
        }
        // add declaration to symtable for new formals if requirements met
        try {
            symTable.addDecl(myId.getName(), new VarTSym(myType.toString()));
        } catch (DuplicateSymException e) {
            ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException occurs!");
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
    }

    public String toString() {
        return myType.toString();
    }

    private TypeNode myType;
    private IdNode myId;
}

class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    public void nameAnalysis(SymTable symTable) {
        String idName = myId.getName();
        try {
            if (symTable.lookupLocal(idName) != null) {
                ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), " Multiply declared identifier");
            }
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
        try {
            //create two sym tables: one for struct type, the other for struct name
            // follow this logic in other nameAnalysis
            SymTable globalSymTable = new SymTable();
            myDeclList.nameAnalysis(symTable, globalSymTable);
            StructTSym globalSym = new StructTSym(globalSymTable);
            symTable.addDecl(myId.getName(), globalSym);
        } catch (DuplicateSymException e) {
            ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgument Exception occurs!");
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }

    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("struct ");
        myId.unparse(p, 0);
        p.println("{");
        myDeclList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("};\n");

    }

    private IdNode myId;
    private DeclListNode myDeclList;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************

abstract class TypeNode extends ASTnode {
    abstract public String toString();
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("int");
    }

    public String toString() {
        return "int";
    }
}

class BoolNode extends TypeNode {
    public BoolNode() {
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("bool");
    }

    public String toString() {
        return "bool";
    }
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("void");
    }

    public String toString() {
        return "void";
    }
}

class StructNode extends TypeNode {
    public StructNode(IdNode id) {
        myId = id;
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
        myId.unparse(p, 0);
    }

    public String toString() {
        return myId.getName();
    }

    private IdNode myId;
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    public void nameAnalysis(SymTable symTable) {
        myAssign.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }

    private AssignNode myAssign;
}

class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("++;");
    }

    private ExpNode myExp;
}

class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("--;");
    }

    private ExpNode myExp;
}

class ReadStmtNode extends StmtNode {
    public ReadStmtNode(ExpNode e) {
        myExp = e;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cin >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    // 1 child (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}

class WriteStmtNode extends StmtNode {
    public WriteStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cout << ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
        symTable.addScope();
        myDeclList.nameAnalysis(symTable);
        myStmtList.nameAnalysis(symTable);
        try {
            symTable.removeScope();
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent + 4);
        myStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1, StmtListNode slist1, DeclListNode dlist2,
            StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
        symTable.addScope();
        myThenDeclList.nameAnalysis(symTable);
        myThenStmtList.nameAnalysis(symTable);
        try {
            symTable.removeScope();
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
        symTable.addScope();
        myElseDeclList.nameAnalysis(symTable);
        myElseStmtList.nameAnalysis(symTable);
        try {
            symTable.removeScope();
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent + 4);
        myThenStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
        addIndentation(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent + 4);
        myElseStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
        symTable.addScope();
        myDeclList.nameAnalysis(symTable);
        myStmtList.nameAnalysis(symTable);
        try {
            symTable.removeScope();
        } catch (EmptySymTableException e) {
            System.err.println("EmptySymTableException occurs!");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent + 4);
        myStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class RepeatStmtNode extends StmtNode {
    public RepeatStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
        symTable.addScope();
        myDeclList.nameAnalysis(symTable);
        myStmtList.nameAnalysis(symTable);
        try {
            symTable.removeScope();
        } catch (EmptySymTableException e) {
            System.err.println(" EmptySymTableException occurs!");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("repeat (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent + 4);
        myStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    public void nameAnalysis(SymTable symTable) {
        myCall.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }

    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }

    private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
}

class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }

    private int myLineNum;
    private int myCharNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void nameAnalysis(SymTable symTable) {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }

    private int myLineNum;
    private int myCharNum;
}

class IdNode extends ExpNode {
    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
        mySym = null;
    }

    public void nameAnalysis(SymTable symTable) {
        try {
            TSym sym = symTable.lookupGlobal(myStrVal);
            if (sym == null) {
                ErrMsg.fatal(myLineNum, myCharNum, "Undeclared identifier");
            } else {
                mySym = sym;
            }
        } catch (Exception e) {
            System.err.println("Exception occurs!");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
        if (mySym != null) {
            p.print("(" + mySym.toString() + ")");
        }
    }

    public String getName() {
        return this.myStrVal;
    }

    public int getLineNum() {
        return this.myLineNum;
    }

    public int getCharNum() {
        return this.myCharNum;
    }

    public TSym getSym() {
        return this.mySym;
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
    private TSym mySym;
}

class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;
        myId = id;
    }

    public void nameAnalysis(SymTable symTable) {
        String idName = myId.getName();
        myLoc.nameAnalysis(symTable);
        // check if it belongs to dotaccessexp node
        if (myLoc instanceof DotAccessExpNode) {
            IdNode node = ((DotAccessExpNode) myLoc).getIdNode();
            TSym locSym = node.getSym();
            try {
                //throw each error for each case respectively
                if ((locSym instanceof StructTSym)==false) {
                    ErrMsg.fatal(node.getLineNum(), node.getCharNum(), "Dot-access of nonstruct type");
                    return;
                }
                TSym locTable = ((StructTSym) locSym).getTable().lookupGlobal(myId.getName());
                if (locTable == null) {
                    ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Invalid struct field name");
                } else {
                    myId.nameAnalysis(((StructTSym) locSym).getTable());
                }
            } catch (EmptySymTableException e) {
                System.err.println("EmptySymTableException occurs! ");
            }

        }
        //check if it belongs to idnode
        if (myLoc instanceof IdNode) {
            try {
                //throw each error for each case respectively
                IdNode idLoc=(IdNode) myLoc;
                TSym locSym = idLoc.getSym();
                if ((locSym instanceof StructTSym)==false) {
                    ErrMsg.fatal(idLoc.getLineNum(), idLoc.getCharNum(),
                            "Dot-access of nonstruct type");
                    return;
                }
                SymTable structTable = ((StructTSym) locSym).getTable();
                TSym structSym = structTable.lookupGlobal(idName);
                if (structSym == null) {
                    ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Invalid struct field name");
                } else {
                    myId.nameAnalysis(structTable);
                }
            } catch (EmptySymTableException e) {
                System.err.println("EmptySymTableException occurs! ");
            }
        }

    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myLoc.unparse(p, 0);
        p.print(").");
        myId.unparse(p, 0);
    }

    public IdNode getIdNode() {
        return myId;
    }

    private ExpNode myLoc;
    private IdNode myId;
}

class AssignNode extends ExpNode {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }

    public void nameAnalysis(SymTable symTable) {
        myLhs.nameAnalysis(symTable);
        myExp.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        if (indent != -1)
            p.print("(");
        myLhs.unparse(p, 0);
        p.print(" = ");
        myExp.unparse(p, 0);
        if (indent != -1)
            p.print(")");
    }

    private ExpNode myLhs;
    private ExpNode myExp;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    public void nameAnalysis(SymTable symTable) {
        myId.nameAnalysis(symTable);
        myExpList.nameAnalysis(symTable);
    }

    public void unparse(PrintWriter p, int indent) {
        myId.unparse(p, 0);
        p.print("(");
        if (myExpList != null) {
            myExpList.unparse(p, 0);
        }
        p.print(")");
    }

    private IdNode myId;
    private ExpListNode myExpList; // possibly null
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp.nameAnalysis(symTable);
    }

    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }

    public void nameAnalysis(SymTable symTable) {
        myExp1.nameAnalysis(symTable);
        myExp2.nameAnalysis(symTable);
    }

    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(-");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(!");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" + ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" - ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" * ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" / ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" && ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" || ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" == ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" != ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" < ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" > ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" <= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" >= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}