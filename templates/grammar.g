%options package=$PACKAGE_NAME$
%options template=$TEMPLATE$
%options import_terminals=$CLASS_NAME_PREFIX$Lexer.gi
$AUTO_GENERATE$
--
-- This is just a sample grammar and not a real grammar for $LANG_NAME$
--

$Globals
    /.import org.eclipse.uide.parser.IParser;
    import java.util.Hashtable;
    import java.util.Stack;
    ./
$End



$Define
    $ast_class /.Object./
    $additional_interfaces /., IParser./
$End

$Terminals
    --            
    -- Here, you may list terminals needed by this grammar.
    -- Furthermore, a terminal may be mapped into an alias
    -- that can also be used in a grammar rule. In addition,
    -- when an alias is specified here it instructs the
    -- generated parser to use the alias in question when
    -- referring to the symbol to which it is aliased. For
    -- example, consider the following definitions:
    --
         boolean
         double
         else
         elseif
         if
         int
         return
         void
         while
         
         IDENTIFIER 
         NUMBER
         COMMA ::= ','
         SEMICOLON ::= ';'
         PLUS ::= '+'
         MINUS ::= '-'
         ASSIGN ::= '='
         LEFTPAREN ::= '('
         RIGHTPAREN ::= ')'
         LEFTBRACE ::= '{'
         RIGHTBRACE ::= '}'
    --
    -- Here the terminals int, float, IDENTIFIER and NUMBER are
    -- defined without an alias; SEMICOLON is aliased to ';';
    -- PLUS is aliased to '+'... etc...
    --
    -- Note that the terminals that do not have aliases are declared
    -- above only for documentation purposes.
    --
$End

$Start
    compilationUnit
$End

$Recover
   MissingExpression
$End

$Rules
    -- In this section you list all the rules for your grammar.
    -- When reduced, each rule will produce one Ast node.
    -- 
    --  Here are some example rules:
    -- 
    compilationUnit ::= $empty
                      | compilationUnit functionDeclaration

    functionDeclaration ::= Type identifier ( parameters ) block 
    
    parameters ::= $empty
                 | parameterList

    parameterList$$declaration ::= declaration
                                 | parameterList ',' declaration
                                                            
    declaration ::= primitiveType identifier

    stmtList$$statement ::= $empty
                          | stmtList statement
    statement ::= declarationStmt
                | assignmentStmt
                | ifStmt
                | returnStmt
                | whileStmt
                | block
                | ;

    block ::= '{' stmtList '}'
    /.
        $action_type.SymbolTable symbolTable;
        public void setSymbolTable($action_type.SymbolTable symbolTable) { this.symbolTable = symbolTable; }
        public $action_type.SymbolTable getSymbolTable() { return symbolTable; }
    ./

    declarationStmt ::= declaration ;
                              
    Type ::= primitiveType
           | void

    primitiveType ::= boolean
                    | double
                    | int
                              
    assignmentStmt ::= identifier '=' expression ';'
                     | BadAssignment
    ifStmt ::= if ( expression ) statement elseifStmtList elseStmtOpt
    
    elseifStmtList ::= $empty
                     | elseifStmtList elseif ( expression ) statement

    elseStmtOpt ::= $empty
                  | else statement

    whileStmt ::= while ( expression ) statement

    returnStmt ::= return expression ;

    expression ::= expression '+' term
                 | expression '-' term
                 | term
    term ::= NUMBER
           | identifier
           | identifier ( expressions )
    
    expressions ::= $empty
                  | expressionList
    expressionList ::= expression
                     | expressionList ',' expression

    identifier ::= IDENTIFIER
    /.
        IAst decl;
        public void setDeclaration(IAst decl) { this.decl = decl; }
        public IAst getDeclaration() { return decl; }
    ./

    BadAssignment ::= identifier '=' MissingExpression 
$End

$Headers
    /.
        public class SymbolTable extends Hashtable {
            SymbolTable parent;
            SymbolTable(SymbolTable parent) { this.parent = parent; }
            IAst findDeclaration(String name) {
                IAst decl = (IAst) get(name);
                return (decl != null
                              ? decl
                              : parent != null ? parent.findDeclaration(name) : null);
            }
            public SymbolTable getParent() { return parent; }
        }
        
        Stack symbolTableStack = null;
        SymbolTable topLevelSymbolTable = null;
        public SymbolTable getTopLevelSymbolTable() { return topLevelSymbolTable; }

        public void resolve($ast_type root) {
            if (root != null) {
                symbolTableStack = new Stack();
                topLevelSymbolTable = new SymbolTable(null);
                symbolTableStack.push(topLevelSymbolTable);
                root.accept(new SymbolTableVisitor());
            }
        }
        
        /*
         * A visitor for ASTs.  Its purpose is to build a symbol table
         * for declared symbols and resolved identifier in expressions.
         */
        private final class SymbolTableVisitor extends AbstractVisitor {
            public void unimplementedVisitor(String s) { /* Useful for debugging: System.out.println(s); */ }
            
            public void emitError(IToken id, String message) {
                getMessageHandler().handleMessage(ParseErrorCodes.NO_MESSAGE_CODE,
                                                  getLexStream().getLocation(id.getStartOffset(), id.getEndOffset()),
                                                  getLexStream().getLocation(0, 0),
                                                  getFileName(),
                                                  new String [] { message });
            }
            
            public boolean visit(block n) {
                n.setSymbolTable((SymbolTable) symbolTableStack.push(new SymbolTable((SymbolTable) symbolTableStack.peek())));
                return true;
            }

            public void endVisit(block n) { symbolTableStack.pop(); }

            public boolean visit(functionDeclaration n) {
                IToken id = n.getidentifier().getIToken();
                SymbolTable symbol_table = (SymbolTable) symbolTableStack.peek();
                if (symbol_table.get(id.toString()) == null)
                     symbol_table.put(id.toString(), n);
                else emitError(id, "Illegal redeclaration of " + id.toString());
                return true;
            }
            
            public boolean visit(declaration n) {
                IToken id = n.getidentifier().getIToken();
                SymbolTable symbol_table = (SymbolTable) symbolTableStack.peek();
                if (symbol_table.get(id.toString()) == null)
                     symbol_table.put(id.toString(), n);
                else emitError(id, "Illegal redeclaration of " + id.toString());
                return true;
            }

            public boolean visit(identifier n) {
                IToken id = n.getIDENTIFIER();
                IAst decl = ((SymbolTable) symbolTableStack.peek()).findDeclaration(id.toString());
                if (decl == null)
                     emitError(id, "Undeclared variable " + id.toString());
                else n.setDeclaration(decl);
                return true;
            }
        } // End SymbolTableVisitor
    ./
$End
