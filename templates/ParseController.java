package $PACKAGE_NAME$;
	
import java.io.IOException;

import lpg.runtime.IMessageHandler;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.uide.model.ISourceProject;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.ILexer;
import org.eclipse.uide.parser.IParseController;
import org.eclipse.uide.parser.IParser;
import org.eclipse.uide.parser.SimpleLPGParseController;

import $AST_PKG_NODE$;

/**
 * Implementation of IParseControllerWithMarkerTypes, that is, a
 * subtype of IParseController that allows problem-marker types
 * to be associated to and read from a parse controller.
 * @see org.eclipse.uide.parser.IParseControllerWithMarkerTypes for
 * an explanation of why this is useful.
 * 
 * Note:  the list of marker tpyes is static, so associations of parse
 * controllers to problem-marker types are by type rather than by instance
 * (which is probably typical of the usual compiler/parser model).
 * 
 * Now also incorporates ISourceProject in place of IProject.
 * 
 * @author Stan Sutton (suttons@us.ibm.com)
 * @since May 1,  2007	Addition of marker types
 * @since May 10, 2007	Conversion IProject -> ISourceProject
 * @since May 31, 2007  Adapted to extend SimpleLPGParseController
 */
public class $CLASS_NAME_PREFIX$ParseController
	extends SimpleLPGParseController
	implements IParseController
{
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
    public void initialize(IPath filePath, ISourceProject project, IMessageHandler handler) {
    	super.initialize(filePath, project, handler);
    	IPath fullFilePath = project.getRawProject().getLocation().append(filePath);
        createLexerAndParser(fullFilePath);

    	parser.setMessageHandler(handler);
    }


    public IParser getParser() { return parser; }
    public ILexer getLexer() { return lexer; }

    public IASTNodeLocator getNodeLocator() { return new $CLASS_NAME_PREFIX$ASTNodeLocator(); }	//return new AstLocator(); }
    
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
    	
    /**
     * setFilePath() should be called before calling this method.
     */
    public Object parse(String contents, boolean scanOnly, IProgressMonitor monitor)
    {
    	PMMonitor my_monitor = new PMMonitor(monitor);
    	char[] contentsArray = contents.toCharArray();

        lexer.initialize(contentsArray, fFilePath.toPortableString());
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

    
 
    
}
