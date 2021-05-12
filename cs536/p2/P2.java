// Yizhou Liu, liu773@wisc.edu
import java.util.*;
import java.io.*;
import java_cup.runtime.*;  // defines Symbol

/**
 * This program is to be used to test the C-- scanner.
 * This version is set up to test all tokens, but you are required to test 
 * other aspects of the scanner (e.g., input that causes errors, character 
 * numbers, values associated with tokens)
 */
public class P2 {
    public static void main(String[] args) throws IOException {
                                           // exception may be thrown by yylex
        // test all tokens
        testAllTokens();
        CharNum.num = 1;

        testReserves();
        CharNum.num=1;

        testIdentifiers();
        CharNum.num=1;

        testIntLiterals();
        CharNum.num=1;

        testLegalStrings();
        CharNum.num=1;

        testIllegalStrings();
        CharNum.num=1;

        testSymbols();
        CharNum.num=1;

        testComments();
        CharNum.num=1;

        testUntermStrLit();
        CharNum.num=1;

        testHelloWorld();
        CharNum.num=1;
    
        // ADD CALLS TO OTHER TEST METHODS HERE
    }

    /**
     * testAllTokens
     *
     * Open and read from file allTokens.txt
     * For each token read, write the corresponding string to allTokens.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testAllTokens() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("allTokens.in");
            outFile = new PrintWriter(new FileWriter("allTokens.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File allTokens.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("allTokens.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testHelloWorld
     *
     * Open and read from file HelloWorld.txt
     * For each token read, write the corresponding string to HelloWorld.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     * 
     * It also tests the expected char num and line num
     */
    private static void testHelloWorld() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("helloWorld.in");
            outFile = new PrintWriter(new FileWriter("helloWorld.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File helloWorld.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("helloWorld.out cannot be opened.");
            System.exit(-1);
        }

        List<String> expList=new ArrayList<>(Arrays.asList("1,1","1,8","1,18","1,19","1,20","3,5","3,12","3,25","4,5","6,1"));
        
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();
        int i=0;

        while (token.sym != sym.EOF) {  
            TokenVal t=((TokenVal)token.value);
            String expectedNum = expList.get(i);
            String[] expNums=expectedNum.split(",");
            int expLine=Integer.parseInt(expNums[0]);
            int expChar=Integer.parseInt(expNums[1]);
            int lineNum=t.linenum;
            int charNum=t.charnum;
            if (lineNum != expLine|| charNum != expChar) {
                System.out.println("Error: The line number or char number don't match!");
            }
            i++;
            switch (token.sym) {
            case sym.BOOL:
                outFile.println("bool"); 
                break;
            case sym.INT:
                outFile.println("int");
                break;
            case sym.VOID:
                outFile.println("void");
                break;
            case sym.TRUE:
                outFile.println("true"); 
                break;
            case sym.FALSE:
                outFile.println("false"); 
                break;
            case sym.STRUCT:
                outFile.println("struct"); 
                break;
            case sym.CIN:
                outFile.println("cin"); 
                break;
            case sym.COUT:
                outFile.println("cout");
                break;				
            case sym.IF:
                outFile.println("if");
                break;
            case sym.ELSE:
                outFile.println("else");
                break;
            case sym.WHILE:
                outFile.println("while");
                break;
            case sym.RETURN:
                outFile.println("return");
                break;
            case sym.ID:
                outFile.println(((IdTokenVal)token.value).idVal);
                break;
            case sym.INTLITERAL:  
                outFile.println(((IntLitTokenVal)token.value).intVal);
                break;
            case sym.STRINGLITERAL: 
                outFile.println(((StrLitTokenVal)token.value).strVal);
                break;    
            case sym.LCURLY:
                outFile.println("{");
                break;
            case sym.RCURLY:
                outFile.println("}");
                break;
            case sym.LPAREN:
                outFile.println("(");
                break;
            case sym.RPAREN:
                outFile.println(")");
                break;
            case sym.SEMICOLON:
                outFile.println(";");
                break;
            case sym.COMMA:
                outFile.println(",");
                break;
            case sym.DOT:
                outFile.println(".");
                break;
            case sym.WRITE:
                outFile.println("<<");
                break;
            case sym.READ:
                outFile.println(">>");
                break;				
            case sym.PLUSPLUS:
                outFile.println("++");
                break;
            case sym.MINUSMINUS:
                outFile.println("--");
                break;	
            case sym.PLUS:
                outFile.println("+");
                break;
            case sym.MINUS:
                outFile.println("-");
                break;
            case sym.TIMES:
                outFile.println("*");
                break;
            case sym.DIVIDE:
                outFile.println("/");
                break;
            case sym.NOT:
                outFile.println("!");
                break;
            case sym.AND:
                outFile.println("&&");
                break;
            case sym.OR:
                outFile.println("||");
                break;
            case sym.EQUALS:
                outFile.println("==");
                break;
            case sym.NOTEQUALS:
                outFile.println("!=");
                break;
            case sym.LESS:
                outFile.println("<");
                break;
            case sym.GREATER:
                outFile.println(">");
                break;
            case sym.LESSEQ:
                outFile.println("<=");
                break;
            case sym.GREATEREQ:
                outFile.println(">=");
                break;
			case sym.ASSIGN:
                outFile.println("=");
                break;
			default:
				outFile.println("UNKNOWN TOKEN");
            } // end switch

            token = scanner.next_token();
        } // end while
        outFile.close();
    }

    /**
     * testReserves
     *
     * Open and read from file allReserves.txt
     * For each token read, write the corresponding string to allReserves.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testReserves() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("allReserves.in");
            outFile = new PrintWriter(new FileWriter("allReserves.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File allReserves.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("allReserves.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testidentifiers
     *
     * Open and read from file identifiers.txt
     * For each token read, write the corresponding string to identifiers.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testIdentifiers() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("identifiers.in");
            outFile = new PrintWriter(new FileWriter("identifiers.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File identifiers.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("identifiers.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testintLiterals
     *
     * Open and read from file intLiterals.txt
     * For each token read, write the corresponding string to intLiterals.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testIntLiterals() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("intLiterals.in");
            outFile = new PrintWriter(new FileWriter("intLiterals.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File IntLiterals.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("IntLiterals.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testlegalStrings
     *
     * Open and read from file legalStrings.txt
     * For each token read, write the corresponding string to legalStrings.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testLegalStrings() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("legalStrings.in");
            outFile = new PrintWriter(new FileWriter("legalStrings.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File legalStrings.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("legalStrings.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testillegalStrings
     *
     * Open and read from file illegalStrings.txt
     * For each token read, write the corresponding string to illegalStrings.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testIllegalStrings() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("illegalStrings.in");
            outFile = new PrintWriter(new FileWriter("illegalStrings.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File IllegalStrings.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("IllegalStrings.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testSymbols
     *
     * Open and read from file Symbols.txt
     * For each token read, write the corresponding string to Symbols.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testSymbols() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("symbols.in");
            outFile = new PrintWriter(new FileWriter("symbols.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File Symbols.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("Symbols.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testComments
     *
     * Open and read from file Comments.txt
     * For each token read, write the corresponding string to Comments.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testComments() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("comments.in");
            outFile = new PrintWriter(new FileWriter("comments.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File Comments.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("Comments.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }

    /**
     * testUntermStrLit
     *
     * Open and read from file UntermStrLit.txt
     * For each token read, write the corresponding string to UntermStrLit.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testUntermStrLit() throws IOException{
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("untermStrLit.in");
            outFile = new PrintWriter(new FileWriter("untermStrLit.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File untermStrLit.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("untermStrLit.out cannot be opened.");
            System.exit(-1);
        }
        
        scanner(inFile, outFile);
    }



    /**
     * 
     *  A utility method, modified with the given testAllTokens method
     */
    private static void scanner(FileReader inFile, PrintWriter outFile) throws IOException{
        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();
        while (token.sym != sym.EOF) {  
            switch (token.sym) {
            case sym.BOOL:
                outFile.println("bool"); 
                break;
            case sym.INT:
                outFile.println("int");
                break;
            case sym.VOID:
                outFile.println("void");
                break;
            case sym.TRUE:
                outFile.println("true"); 
                break;
            case sym.FALSE:
                outFile.println("false"); 
                break;
            case sym.STRUCT:
                outFile.println("struct"); 
                break;
            case sym.CIN:
                outFile.println("cin"); 
                break;
            case sym.COUT:
                outFile.println("cout");
                break;				
            case sym.IF:
                outFile.println("if");
                break;
            case sym.ELSE:
                outFile.println("else");
                break;
            case sym.WHILE:
                outFile.println("while");
                break;
            case sym.RETURN:
                outFile.println("return");
                break;
            case sym.ID:
                outFile.println(((IdTokenVal)token.value).idVal);
                break;
            case sym.INTLITERAL:  
                outFile.println(((IntLitTokenVal)token.value).intVal);
                break;
            case sym.STRINGLITERAL: 
                outFile.println(((StrLitTokenVal)token.value).strVal);
                break;    
            case sym.LCURLY:
                outFile.println("{");
                break;
            case sym.RCURLY:
                outFile.println("}");
                break;
            case sym.LPAREN:
                outFile.println("(");
                break;
            case sym.RPAREN:
                outFile.println(")");
                break;
            case sym.SEMICOLON:
                outFile.println(";");
                break;
            case sym.COMMA:
                outFile.println(",");
                break;
            case sym.DOT:
                outFile.println(".");
                break;
            case sym.WRITE:
                outFile.println("<<");
                break;
            case sym.READ:
                outFile.println(">>");
                break;				
            case sym.PLUSPLUS:
                outFile.println("++");
                break;
            case sym.MINUSMINUS:
                outFile.println("--");
                break;	
            case sym.PLUS:
                outFile.println("+");
                break;
            case sym.MINUS:
                outFile.println("-");
                break;
            case sym.TIMES:
                outFile.println("*");
                break;
            case sym.DIVIDE:
                outFile.println("/");
                break;
            case sym.NOT:
                outFile.println("!");
                break;
            case sym.AND:
                outFile.println("&&");
                break;
            case sym.OR:
                outFile.println("||");
                break;
            case sym.EQUALS:
                outFile.println("==");
                break;
            case sym.NOTEQUALS:
                outFile.println("!=");
                break;
            case sym.LESS:
                outFile.println("<");
                break;
            case sym.GREATER:
                outFile.println(">");
                break;
            case sym.LESSEQ:
                outFile.println("<=");
                break;
            case sym.GREATEREQ:
                outFile.println(">=");
                break;
			case sym.ASSIGN:
                outFile.println("=");
                break;
			default:
				outFile.println("UNKNOWN TOKEN");
            } // end switch

            token = scanner.next_token();
        } // end while
        outFile.close();
    }
}
