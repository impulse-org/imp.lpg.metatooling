package $PACKAGE_NAME$;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lpg.runtime.IMessageHandler;
import lpg.runtime.IPrsStream;
import lpg.runtime.IToken;
import lpg.runtime.LexStream;
import lpg.runtime.LpgLexStream;
import lpg.runtime.Monitor;
import lpg.runtime.PrsStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.uide.model.ISourceProject;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.ILexer;
import org.eclipse.uide.parser.IParseController;
import org.eclipse.uide.parser.IParser;
import org.eclipse.uide.parser.ParseError;
import org.eclipse.uide.parser.SimpleLPGParseController;


/**
 * NOTE:  This version of the Parse Controller is for use when the Parse
 * Controller and corresponding Node Locator are generated separately from
 * a corresponding set of LPG grammar templates and possibly in the absence
 * of the lexer, parser, and AST-related types that would be generated from
 * those templates.  It is assumed that either a) the Controller will be
 * used with a suitable set of lexer, parser, and AST-related types
 * that are provided by some means other than LPG, or b) the Controller will
 * be used with a set of lexer, parser, and AST types that have been, or will
 * be, separately generated based on LPG.  In order to enable this version of
 * the Parse Controller to compile, dummy lexer, parser, and AST-related types
 * have been included as member types in the Controller.  These types are not
 * operational and are merely placeholders for types that would support a
 * functioning implementation.  Apart from the inclusion of these dummy types,
 * this representation of the Parse Controller is the same as that used
 * with LPG.
 * 	
 * @author Stan Sutton (suttons@us.ibm.com)
 * @since May 1,  2007	Addition of marker types
 * @since May 10, 2007	Conversion IProject -> ISourceProject
 * @since May 15, 2007	Addition of dummy types
 */
public class $CLASS_NAME_PREFIX$ParseController
	extends SimpleLPGParseController
	implements IParseController
{
    
    public class ASTNodeToken extends $AST_NODE$ implements IASTNodeToken {
        public ASTNodeToken(IToken token) { super(token); }
    }
    
    public interface Visitor {
        boolean preVisit($AST_NODE$ element);
        void postVisit($AST_NODE$ element);
        boolean visit(ASTNodeToken n);
        void endVisit(ASTNodeToken n);
    }
    
    public static abstract class AbstractVisitor implements Visitor {
        public abstract void unimplementedVisitor(String s);
        public boolean preVisit($AST_NODE$ element) { return true; }
        public void postVisit($AST_NODE$ element) {}
        public boolean visit(ASTNodeToken n) { unimplementedVisitor("visit(ASTNodeToken)"); return true; }
        public void endVisit(ASTNodeToken n) { unimplementedVisitor("endVisit(ASTNodeToken)"); }
    }
    
    public interface IASTNodeToken {
        public IToken getLeftIToken();
        public IToken getRightIToken();
        void accept(Visitor v);
    }
    
    public class $AST_NODE$ implements IASTNodeToken {
        public $AST_NODE$ (IToken token) { }
        public $AST_NODE$ (IToken leftIToken, IToken rightIToken) { }
        public IToken getLeftIToken() { return null; }
        public IToken getRightIToken() { return null; }
        public void accept(Visitor v) { }
    }

    public class $CLASS_NAME_PREFIX$Lexer extends LpgLexStream implements ILexer {
        public $CLASS_NAME_PREFIX$Lexer(String filename) throws java.io.IOException { }
        public LexStream getLexStream() { return (LexStream) this; }
        public void lexer(Monitor monitor, IPrsStream prsStream) { };
        public int[] getKeywordKinds() { return null; }
        public int getKind(int i) { return 0; }
        public String[] orderedExportedSymbols() { return null; }
    }
    
    public class $CLASS_NAME_PREFIX$Parser extends PrsStream implements IParser
    {
        public $CLASS_NAME_PREFIX$Parser(LexStream lexStream) {    }
        public PrsStream getParseStream() { return (PrsStream) this; }
        public Object parser(Monitor monitor, int error_repair_count) { return null; }
        public int getEOFTokenKind() { return 0; }
        public void setMessageHandler(IMessageHandler errMsg) { }
        public void resolve($AST_NODE$ root) {}
    }
	

    private $CLASS_NAME_PREFIX$Parser parser;
    private $CLASS_NAME_PREFIX$Lexer lexer;
    private $AST_NODE$ currentAst;

    private char keywords[][];
    private boolean isKeyword[];

    /**
     * @param filePath		Project-relative path of file
     * @param project		Project that contains the file
     * @param handler		A message handler to receive error messages (or any others)
     * 						from the parser
     */
    public void initialize(IPath filePath, ISourceProject project, IMessageHandler handler) {
    	super.initialize(filePath, project, handler)
;    	IPath fullFilePath = project.getRawProject().getLocation().append(filePath);
        createLexerAndParser(fullFilePath);

    	parser.setMessageHandler(handler);
    }

    public IParser getParser() { return parser; }
    public ILexer getLexer() { return lexer; }

    public IASTNodeLocator getNodeLocator() { return new $CLASS_NAME_PREFIX$ASTNodeLocator(); }

    public boolean hasErrors() { return currentAst == null; }
    public List getErrors() { return Collections.singletonList(new ParseError("parse error", null)); }
    
    public $CLASS_NAME_PREFIX$ParseController()
    {
    }

    private void createLexerAndParser(IPath filePath) {
        try {
            lexer = new $CLASS_NAME_PREFIX$Lexer(filePath.toOSString());
            parser = new $CLASS_NAME_PREFIX$Parser(lexer.getLexStream());
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    
    /**
     * setFilePath() should be called before calling this method.
     */
    public Object parse(String contents, boolean scanOnly, IProgressMonitor monitor)
    {
    	PMMonitor my_monitor = new PMMonitor(monitor);
    	char[] contentsArray = contents.toCharArray();

        lexer.initialize(contentsArray, fFilePath.toPortableString());
        parser.getParseStream().resetTokenStream();
        
        lexer.lexer(my_monitor, parser.getParseStream()); // Lex the stream to produce the token stream
        if (my_monitor.isCancelled())
            return currentAst; // TODO currentAst might (probably will) be inconsistent wrt the lex stream now

        currentAst = ($AST_NODE$) parser.parser(my_monitor, 0);
        parser.resolve(currentAst);

        cacheKeywordsOnce();

        return currentAst;
    }
    
}
	