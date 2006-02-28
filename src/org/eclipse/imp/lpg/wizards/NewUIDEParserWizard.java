package org.jikespg.uide.wizards;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.uide.wizards.ExtensionPointWizard;
import org.eclipse.uide.wizards.ExtensionPointWizardPage;
import org.eclipse.uide.wizards.Wizards.GenericServiceWizard;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.builder.JikesPGBuilder;

/**
 * This wizard creates a JikesPG grammar, a "parser" language service, and a
 * stub IParseController implementation for use with the UIDE.
 */
public class NewUIDEParserWizard extends ExtensionPointWizard implements INewWizard {
    //	private NewUIDEParserWizardPage page;
    //	private ISelection selection;

    public NewUIDEParserWizard() {
	super();
	setNeedsProgressMonitor(true);
    }

    public void addPages() {
	addPages(new ExtensionPointWizardPage[] { new NewUIDEParserWizardPage(this), });
    }

    /**
     * The worker method. It will find the container, create the
     * file if missing or just replace its contents, and open
     * the editor on the newly created file.
     */
    protected void generateCodeStubs(IProgressMonitor monitor) throws CoreException {
	NewUIDEParserWizardPage page= (NewUIDEParserWizardPage) pages[0];

	IProject project= page.getProject();
	GrammarOptions options= page.getOptions();
	boolean hasKeywords= options.getHasKeywords();
	boolean requiresBacktracking= options.getRequiresBacktracking();
	boolean autoGenerateASTs= options.getAutoGenerateASTs();
	String templateKind= options.getTemplateKind();
	String packageName= options.getPackageName();
	String languageName= options.getLanguageName();
	String parserTemplateName= templateKind + (requiresBacktracking ? "/bt" : "/dt") + "ParserTemplate.gi";
	String lexerTemplateName= templateKind + "/LexerTemplate.gi";
	String kwLexerTemplateName= templateKind + "/KeywordTemplate.gi";
	String grammarFileName= "src/" + languageName.toLowerCase() + "Parser.g";
	String lexerFileName= "src/" + languageName.toLowerCase() + "Lexer.gi";
	String kwlexerFileName= "src/" + languageName.toLowerCase() + "KWLexer.gi";
	String controllerFileName= "src/ParseController.java";

	IFile grammarFile= createGrammarFile(packageName, languageName, grammarFileName, parserTemplateName, autoGenerateASTs, project,
		monitor);

	createSampleLexerFile(packageName, languageName, lexerFileName, lexerTemplateName, hasKeywords, project, monitor);
	createSampleKWLexerFile(packageName, languageName, kwlexerFileName, kwLexerTemplateName, hasKeywords, project, monitor);
	createSampleParseControllerFile(packageName, languageName, controllerFileName, kwLexerTemplateName, hasKeywords, project,
		monitor);

	editFile(monitor, grammarFile);
	enableBuilders(monitor, project, new String[] { JikesPGBuilder.BUILDER_ID });
    }

    static final String astDirectory= "Ast";

    static final String astNode= "ASTNode";

    static final String sAutoGenTemplate= "%options automacit_ast=toplevel,visitor,ast_directory=./" + astDirectory
	    + ",ast_type=" + astNode;

    static final String sKeywordTemplate= "%options filter=kwTemplate.gi";

    private IFile createGrammarFile(String packageName, String languageName, String fileName, String templateName,
	    boolean autoGenerateASTs, IProject project, IProgressMonitor monitor) throws CoreException {

	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE$", packageName },
		{ "$AUTO_GENERATE$", autoGenerateASTs ? sAutoGenTemplate : "" },
		{ "$TEMPLATE$", templateName } };

	return createFileFromTemplate(fileName, "grammar.tmpl", replacements, project, monitor);
    }

    private IFile createSampleLexerFile(String packageName, String languageName, String fileName, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE$", packageName },
		{ "$TEMPLATE$", templateName },
		{ "$KEYWORD_FILTER$",
			hasKeywords ? ("%options filter=" + languageName + "KWLexer.gi") : "" },
		{ "$KEYWORD_LEXER$", hasKeywords ? ("$" + languageName + "KWLexer") : "Object" },
		{ "$LEXER_MAP$", (hasKeywords ? "LexerBasicMap" : "LexerVeryBasicMap") } };

	return createFileFromTemplate(fileName, "lexer.tmpl", replacements, project, monitor);
    }

    private IFile createSampleKWLexerFile(String packageName, String languageName, String fileName, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE$", packageName },
		{ "$TEMPLATE$", templateName }
	};
	return createFileFromTemplate(fileName, "kwlexer.tmpl", replacements, project, monitor);
    }

    private IFile createSampleParseControllerFile(String packageName, String languageName, String fileName,
	    String templateName, boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
		{ "$AST_NODE$", packageName + "." + astDirectory + "." + astNode },
		{ "$PARSER_TYPE$", languageName + "Parser" },
		{ "$LEXER_TYPE$", languageName + "Lexer" }
	};

	return createFileFromTemplate(fileName, "ParseController.tmpl", replacements, project, monitor);
    }

    protected String getTemplateBundleID() {
        return JikesPGPlugin.kPluginID;
    }

    /**
     * We will accept the selection in the workbench to see if
     * we can initialize from it.
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    // this.selection = selection;
    }
}
