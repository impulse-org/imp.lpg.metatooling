package org.jikespg.uide.wizards;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.uide.wizards.ExtensionPointWizard;
import org.eclipse.uide.wizards.ExtensionPointWizardPage;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.builder.JikesPGBuilder;

/**
 * This wizard creates a JikesPG grammar, a "parser" language service, and a
 * stub IParseController implementation for use with the UIDE.
 */
public class NewUIDEParserWizard extends ExtensionPointWizard implements INewWizard {
    //	private NewUIDEParserWizardPage page;
    //	private ISelection selection;

    private IProject fProject;
    private GrammarOptions fGrammarOptions;

    public NewUIDEParserWizard() {
	super();
	setNeedsProgressMonitor(true);
    }

    public void addPages() {
	addPages(new ExtensionPointWizardPage[] { new NewUIDEParserWizardPage(this), });
    }

    protected void collectCodeParms() {
        NewUIDEParserWizardPage page= (NewUIDEParserWizardPage) pages[0];
        
        fProject= page.getProject();
        fGrammarOptions= page.fGrammarOptions;
        fGrammarOptions.setLanguageName(page.getValue("language"));
        fGrammarOptions.setProjectName(fProject.getName());
        String className= page.getValue("class");
        fGrammarOptions.setPackageName(className.substring(0, className.lastIndexOf('.')));
    }

    protected void generateCodeStubs(IProgressMonitor monitor) throws CoreException {
	boolean hasKeywords= fGrammarOptions.getHasKeywords();
	boolean requiresBacktracking= fGrammarOptions.getRequiresBacktracking();
	boolean autoGenerateASTs= fGrammarOptions.getAutoGenerateASTs();
	String templateKind= fGrammarOptions.getTemplateKind();
	String packageName= fGrammarOptions.getPackageName();
        String packageFolder= packageName.replace('.', File.separatorChar);
	String languageName= fGrammarOptions.getLanguageName();
	String parserTemplateName= templateKind + (requiresBacktracking ? "/bt" : "/dt") + "ParserTemplate.gi";
	String lexerTemplateName= templateKind + "/LexerTemplate.gi";
	String kwLexerTemplateName= templateKind + "/KeywordTemplate.gi";
        String parseCtlrTemplateName= "ParseController.tmpl";
        String langClassName= Character.toUpperCase(languageName.charAt(0)) + languageName.substring(1);
	String grammarFileName= langClassName + "Parser.g";
	String lexerFileName= langClassName + "Lexer.gi";
	String kwlexerFileName= langClassName + "KWLexer.gi";
	String controllerFileName= langClassName + "ParseController.java";

	IFile grammarFile= createGrammar(packageName, languageName, packageFolder, grammarFileName, parserTemplateName, autoGenerateASTs, fProject,
		monitor);

	createLexer(packageName, languageName, lexerFileName, packageFolder, lexerTemplateName, hasKeywords, fProject, monitor);
	createKWLexer(packageName, languageName, kwlexerFileName, packageFolder, kwLexerTemplateName, hasKeywords, fProject, monitor);
	createParseController(packageName, languageName, controllerFileName, packageFolder, parseCtlrTemplateName, hasKeywords, fProject,
		monitor);

	editFile(monitor, grammarFile);
	enableBuilders(monitor, fProject, new String[] { JikesPGBuilder.BUILDER_ID });
    }

    static final String astDirectory= "Ast";

    static final String astNode= "ASTNode";

    static final String sAutoGenTemplate= "%options automatic_ast=toplevel,visitor,ast_directory=./" + astDirectory
	    + ",ast_type=" + astNode;

    static final String sKeywordTemplate= "%options filter=kwTemplate.gi";

    private IFile createGrammar(String packageName, String languageName, String packageFolder, String fileName, String templateName,
	    boolean autoGenerateASTs, IProject project, IProgressMonitor monitor) throws CoreException {

	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE$", packageName },
		{ "$AUTO_GENERATE$", autoGenerateASTs ? sAutoGenTemplate : "" },
		{ "$TEMPLATE$", templateName } };

	return createFileFromTemplate(fileName, "grammar.tmpl", packageFolder, replacements, project, monitor);
    }

    private IFile createLexer(String packageName, String languageName, String fileName, String packageFolder, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE$", packageName },
		{ "$TEMPLATE$", templateName },
		{ "$KEYWORD_FILTER$",
			hasKeywords ? ("%options filter=" + languageName + "KWLexer.gi") : "" },
		{ "$KEYWORD_LEXER$", hasKeywords ? ("$" + languageName + "KWLexer") : "Object" },
		{ "$LEXER_MAP$", (hasKeywords ? "LexerBasicMap" : "LexerVeryBasicMap") } };

	return createFileFromTemplate(fileName, "lexer.tmpl", packageFolder, replacements, project, monitor);
    }

    private IFile createKWLexer(String packageName, String languageName, String fileName, String packageFolder, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE$", packageName },
		{ "$TEMPLATE$", templateName }
	};
	return createFileFromTemplate(fileName, "kwlexer.tmpl", packageFolder, replacements, project, monitor);
    }

    private IFile createParseController(String packageName, String languageName, String fileName, String packageFolder,
	    String templateName, boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
                { "$LANG_NAME$", languageName },
                { "$PACKAGE$", packageName },
		{ "$AST_NODE$", packageName + "." + astDirectory + "." + astNode },
		{ "$PARSER_TYPE$", languageName + "Parser" },
		{ "$LEXER_TYPE$", languageName + "Lexer" }
	};

	return createFileFromTemplate(fileName, "ParseController.tmpl", packageFolder, replacements, project, monitor);
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
