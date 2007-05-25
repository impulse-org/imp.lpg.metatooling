package $PACKAGE_NAME$;

import java.io.IOException;
import java.util.ArrayList;
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
import org.eclipse.uide.parser.IParseControllerWithMarkerTypes;
import org.eclipse.uide.parser.IParser;
import org.eclipse.uide.parser.ParseError;


/**
 * Implementation of IParseControllerWithMarkerTypes, that is, a
 * subtype of IParseController that allows problem-marker types
 * to be associated to and read from a parse controller.
 * Note:  the list of marker types is static, so associations of parse
 * controllers to problem-marker types are by type rather than by instance
 * (which is probably typical of the usual compiler/parser model).
 * @see org.eclipse.uide.parser.IParseControllerWithMarkerTypes for
 * an explanation of why this is useful.
 * 
 * Now also incorporates ISourceProject in place of IProject.
 * 
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
public class $CLASS_NAME_PREFIX$ParseController implements IParseControllerWithMarkerTypes
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
	


	
    private ISourceProject project;
    private IPath filePath;
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
    	this.filePath= filePath;
    	this.project= project;
    	IPath fullFilePath = project.getRawProject().getLocation().append(filePath);
        createLexerAndParser(fullFilePath);

    	parser.setMessageHandler(handler);
    }
    
    public ISourceProject getProject() { return project; }
    public IPath getPath() { return filePath; }

    public IParser getParser() { return parser; }
    public ILexer getLexer() { return lexer; }
    public Object getCurrentAst() { return currentAst; }
    public char [][] getKeywords() { return keywords; }
    public boolean isKeyword(int kind) { return isKeyword[kind]; }
    public int getTokenIndexAtCharacter(int offset)
    {
        int index = parser.getParseStream().getTokenIndexAtCharacter(offset);
        return (index < 0 ? -index : index);
    }
    public IToken getTokenAtCharacter(int offset) {
    	return parser.getParseStream().getTokenAtCharacter(offset);
    }
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

    class MyMonitor implements Monitor
    {
        IProgressMonitor monitor;
        boolean wasCancelled= false;
        MyMonitor(IProgressMonitor monitor)
        {
            this.monitor = monitor;	
        }
        public boolean isCancelled() {
        	if (!wasCancelled)
        		wasCancelled = monitor.isCanceled();
        	return wasCancelled;
        }
    }
    
    /**
     * setFilePath() should be called before calling this method.
     */
    public Object parse(String contents, boolean scanOnly, IProgressMonitor monitor)
    {
    	MyMonitor my_monitor = new MyMonitor(monitor);
    	char[] contentsArray = contents.toCharArray();

        lexer.initialize(contentsArray, filePath.toPortableString());
        parser.getParseStream().resetTokenStream();
        
        lexer.lexer(my_monitor, parser.getParseStream()); // Lex the stream to produce the token stream
        if (my_monitor.isCancelled())
            return currentAst; // TODO currentAst might (probably will) be inconsistent wrt the lex stream now

        currentAst = ($AST_NODE$) parser.parser(my_monitor, 0);
        parser.resolve(currentAst);

        cacheKeywordsOnce();

        return currentAst;
    }

    private void cacheKeywordsOnce() {
        if (keywords == null) {
            String tokenKindNames[] = parser.getParseStream().orderedTerminalSymbols();
            this.isKeyword = new boolean[tokenKindNames.length];
            this.keywords = new char[tokenKindNames.length][];

            int [] keywordKinds = lexer.getKeywordKinds();
            for (int i = 1; i < keywordKinds.length; i++)
            {
                int index = parser.getParseStream().mapKind(keywordKinds[i]);

                isKeyword[index] = true;
                keywords[index] = parser.getParseStream().orderedTerminalSymbols()[index].toCharArray();
            }
        }
    }
    
    
    /*
     * For the management of associated problem-marker types
     */
    
    private static List problemMarkerTypes = new ArrayList();
    
    public List getProblemMarkerTypes() {
    	return problemMarkerTypes;
    }
    
    public void addProblemMarkerType(String problemMarkerType) {
    	problemMarkerTypes.add(problemMarkerType);
    }
    
	public void removeProblemMarkerType(String problemMarkerType) {
		problemMarkerTypes.remove(problemMarkerType);
	}
    
}
	