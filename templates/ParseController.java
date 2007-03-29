package $PACKAGE_NAME$;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lpg.runtime.IToken;
import lpg.runtime.Monitor;
import lpg.runtime.IMessageHandler;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.ILexer;
import org.eclipse.uide.parser.IParseController;
import org.eclipse.uide.parser.IParser;
import org.eclipse.uide.parser.ParseError;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import $AST_PKG_NODE$;

public class $CLASS_NAME_PREFIX$ParseController implements IParseController
{
    private IProject project;
    private IPath filePath;
    private $PARSER_TYPE$ parser;
    private $LEXER_TYPE$ lexer;
    private $AST_NODE$ currentAst;

    private char keywords[][];
    private boolean isKeyword[];

    /**
     * @param filePath		Project-relative path of file
     * @param project		Project that contains the file
     * @param handler		A message handler to receive error messages (or any others)
     * 						from the parser
     */
    public void initialize(IPath filePath, IProject project, IMessageHandler handler) {
    	this.filePath= filePath;
    	this.project= project;
    	IPath fullFilePath = project.getLocation().append(filePath);
        createLexerAndParser(fullFilePath);

    	parser.setMessageHandler(handler);
    }

    public IProject getProject() { return project; }
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
    }											// SMS 3 Oct 2006:  trying new locator
    public IASTNodeLocator getNodeLocator() { return new $CLASS_NAME_PREFIX$ASTNodeLocator(); }	//return new AstLocator(); }

    public boolean hasErrors() { return currentAst == null; }
    public List getErrors() { return Collections.singletonList(new ParseError("parse error", null)); }
    
    public $CLASS_NAME_PREFIX$ParseController()
    {
    }

    private void createLexerAndParser(IPath filePath) {
        try {
            lexer = new $LEXER_TYPE$(filePath.toOSString()); // Create the lexer
            parser = new $PARSER_TYPE$(lexer.getLexStream() /*, project*/);  // Create the parser
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

        // SMS 28 Mar 2007
        // Commenting out to prevent clobbering of markers set by previous
        // builders in the same build phase.  This will also give behavior
        // that is more consistent with the handling of markers in the JDT.
//        IResource file = project.getFile(filePath);
//   	    try {
//        	file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
//        } catch(CoreException e) {
//        	System.err.println("$LANG_NAME$ParseController.parse:  caught CoreException while deleting problem markers; continuing to parse regardless");
//        }
        
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
}
