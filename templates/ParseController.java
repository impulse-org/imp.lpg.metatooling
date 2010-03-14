package $PACKAGE_NAME$;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.imp.services.ILanguageSyntaxProperties;
import org.eclipse.imp.parser.SimpleLPGParseController;

import $PLUGIN_PACKAGE$.$PLUGIN_CLASS$;
import $AST_PKG_NODE$;

/**
 * The $LANG_NAME$ implementation of the IMP IParseController interface.
 */
public class $CLASS_NAME_PREFIX$ParseController extends SimpleLPGParseController {
    public $CLASS_NAME_PREFIX$ParseController() {
    	super($PLUGIN_CLASS$.kLanguageID);
    	fLexer = new $CLASS_NAME_PREFIX$Lexer();
    	fParser = new $CLASS_NAME_PREFIX$Parser();
    }

	public ILanguageSyntaxProperties getSyntaxProperties() {
	    return null;
	}

	public Object parse(String contents, IProgressMonitor monitor) {
		super.parse(contents, monitor);

		if (fCurrentAst instanceof ASTNode) {
			(($CLASS_NAME_PREFIX$Parser) fParser).resolve((ASTNode) fCurrentAst);
		}
		
		return fCurrentAst;
	}
}
