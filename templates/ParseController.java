package $PACKAGE_NAME$;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.imp.language.ILanguageSyntaxProperties;
import org.eclipse.imp.model.ISourceProject;
import org.eclipse.imp.parser.ILexer;
import org.eclipse.imp.parser.IMessageHandler;
import org.eclipse.imp.parser.IParseController;
import org.eclipse.imp.parser.IParser;
import org.eclipse.imp.parser.ISourcePositionLocator;
import org.eclipse.imp.parser.MessageHandlerAdapter;
import org.eclipse.imp.parser.SimpleLPGParseController;

import $AST_PKG_NODE$;

/**
 * @author Stan Sutton (suttons@us.ibm.com) (for the following modifications)
 * @since May 1,  2007	Addition of marker types
 * @since May 10, 2007	Conversion IProject -> ISourceProject
 * @since May 31, 2007  Adapted to extend SimpleLPGParseController
 */
public class $CLASS_NAME_PREFIX$ParseController
    extends SimpleLPGParseController
    implements IParseController
{
	private $CLASS_NAME_PREFIX$Parser parser;

	private $CLASS_NAME_PREFIX$Lexer lexer;

        public $CLASS_NAME_PREFIX$ParseController() { }

	/**
	 * @param filePath
	 *            Absolute path of file
	 * @param project
	 *            Project that contains the file
	 * @param handler
	 *            A message handler to receive error messages (or any others)
	 *            from the parser
	 */
	public void initialize(IPath filePath, ISourceProject project,
			IMessageHandler handler) {
		super.initialize(filePath, project, handler);
	}

	public IParser getParser() {
		return parser;
	}

	public ILexer getLexer() {
		return lexer;
	}
	

	public ISourcePositionLocator getNodeLocator() {
		return new $CLASS_NAME_PREFIX$ASTNodeLocator();
	}

        public ILanguageSyntaxProperties getSyntaxProperties() {
            return null;
        }

        /**
	 * setFilePath() should be called before calling this method.
	 */
	public Object parse(String contents, boolean scanOnly,
			IProgressMonitor monitor) {
		PMMonitor my_monitor = new PMMonitor(monitor);
		char[] contentsArray = contents.toCharArray();

		if (lexer == null) {
		  lexer = new $CLASS_NAME_PREFIX$Lexer();
		}
		lexer.reset(contentsArray, fFilePath.toPortableString());
		
		if (parser == null) {
			parser = new $CLASS_NAME_PREFIX$Parser(lexer.getLexStream());
		}
		parser.reset(lexer.getLexStream());
		parser.getParseStream().setMessageHandler(new MessageHandlerAdapter(handler));

		lexer.lexer(my_monitor, parser.getParseStream()); // Lex the stream to
															// produce the token
															// stream
		if (my_monitor.isCancelled())
			return fCurrentAst; // TODO fCurrentAst might (probably will) be
								// inconsistent wrt the lex stream now

		fCurrentAst = parser.parser(my_monitor, 0);

		// SMS 18 Dec 2007:  functionDeclarationList is a LEG-specific type
		// not suitable for other languages; try replacing with ASTNode
//		if (fCurrentAst instanceof functionDeclarationList) {
//			parser.resolve((ASTNode) fCurrentAst);
//		}
		if (fCurrentAst instanceof ASTNode) {
			parser.resolve((ASTNode) fCurrentAst);
		}
		
		cacheKeywordsOnce();

		Object result = fCurrentAst;
		return result;
	}
}
