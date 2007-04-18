package org.jikespg.uide.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.safari.jikespg.builder.JikesPGBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uide.runtime.RuntimePlugin;
import org.eclipse.uide.wizards.ExtensionPointWizard;
import org.eclipse.uide.wizards.ExtensionPointWizardPage;
import org.jikespg.uide.JikesPGPlugin;

/**
 * This wizard creates a JikesPG grammar, a "parser" language service, and a
 * stub IParseController implementation for use with the UIDE.
 */
public class NewUIDEParserWizard extends ExtensionPointWizard implements INewWizard {
    //	private NewUIDEParserWizardPage page;
    //	private ISelection selection;

    private IProject fProject;
    private GrammarOptions fGrammarOptions;

    protected String fLanguageName;
    protected String fPackageName;
    protected String fPackageFolder;
    protected String fParserPackage;
    protected String fClassNamePrefix;

    
    protected String fGrammarFileName;
    protected String fLexerFileName;
    protected String fKwlexerFileName;
    protected String fControllerFileName;
    protected String fLocatorFileName;
    
    public NewUIDEParserWizard() {
	super();
	setNeedsProgressMonitor(true);
    }

    public void addPages() {
	addPages(new ExtensionPointWizardPage[] { new NewUIDEParserWizardPage(this), });
    }

    private final static List/*<String pluginID>*/ dependencies= new ArrayList();

    static {
	dependencies.add(RuntimePlugin.UIDE_RUNTIME);
	dependencies.add("org.eclipse.core.runtime");
	dependencies.add("org.eclipse.core.resources");
	dependencies.add("org.eclipse.uide.runtime");
	dependencies.add("lpg.runtime");
    }

    protected List getPluginDependencies() {
	return dependencies;
    }

    protected void collectCodeParms() {
        NewUIDEParserWizardPage page= (NewUIDEParserWizardPage) pages[0];
        
        fProject= page.getProject();
        fGrammarOptions= page.fGrammarOptions;
        fGrammarOptions.setLanguageName(page.getValue("language"));
        fGrammarOptions.setProjectName(fProject.getName());
        String qualifiedClassName = page.getValue("class");
        fGrammarOptions.setPackageName(qualifiedClassName.substring(0, qualifiedClassName.lastIndexOf('.')));

	    fPackageName= fGrammarOptions.getPackageName();
	    fLanguageName= fGrammarOptions.getLanguageName();
        fPackageFolder= fPackageName.replace('.', File.separatorChar);
        
        // SMS 13 Apr 2007 moving this here from generateCodeStubs
        // so that the information is available for advising the user
        // before stubs are generated
        fGrammarOptions.setClassNamePrefix(Character.toUpperCase(fLanguageName.charAt(0)) + fLanguageName.substring(1));
        fClassNamePrefix = fGrammarOptions.getClassNamePrefix();

        fGrammarFileName= fClassNamePrefix + "Parser.g";
	    fLexerFileName= fClassNamePrefix + "Lexer.gi";
	    fKwlexerFileName= fClassNamePrefix + "KWLexer.gi";
        fControllerFileName = fGrammarOptions.getDefaultSimpleNameForParseController(fLanguageName) + ".java";	//fClassNamePrefix + "ParseController.java";
		fLocatorFileName = fGrammarOptions.getDefaultSimpleNameForNodeLocator(fLanguageName) + ".java";			// fClassNamePrefix + "ASTNodeLocator.java";
    }

    	
    protected void generateCodeStubs(IProgressMonitor monitor) throws CoreException {
		boolean hasKeywords= fGrammarOptions.getHasKeywords();
		boolean requiresBacktracking= fGrammarOptions.getRequiresBacktracking();
		boolean autoGenerateASTs= fGrammarOptions.getAutoGenerateASTs();
		String templateKind= fGrammarOptions.getTemplateKind();

        String parserTemplateName= templateKind + (requiresBacktracking ? "/bt" : "/dt") + "ParserTemplate.gi";
	    String lexerTemplateName= templateKind + "/LexerTemplate.gi";
	    String kwLexerTemplateName= templateKind + "/KeywordTemplate.gi";
	    String parseCtlrTemplateName= "ParseController.java";
		String locatorTemplateName = "ASTNodeLocator.java";

        // SMS 13 Apr 2007:  commenting out as part of move to collectCodeParms()
//        String langClassName= Character.toUpperCase(fLanguageName.charAt(0)) + fLanguageName.substring(1);
//        fClassNamePrefix= langClassName;
//
//        String grammarFileName= langClassName + "Parser.g";
//	    String lexerFileName= langClassName + "Lexer.gi";
//	    String kwlexerFileName= langClassName + "KWLexer.gi";
//        String controllerFileName= langClassName + "ParseController.java";
    
	// TODO Need to add the base language plugin (if any) as a plugin dependency
//	final String baseLang= ExtensionPointEnabler.findServiceAttribute(RuntimePlugin.UIDE_RUNTIME + ".languageDescription", fLanguageName, "language", "derivedFrom", "");
//	final String baseLangServiceImpl= ExtensionPointEnabler.findServiceImplClass(RuntimePlugin.UIDE_RUNTIME + ".parser", baseLang, null);
//	subs.put("$BASE_CLASS$", baseLangServiceImpl);

		IFile grammarFile= createGrammar(fGrammarFileName, parserTemplateName, autoGenerateASTs, fProject, monitor);
	
		createLexer(fLexerFileName, lexerTemplateName, hasKeywords, fProject, monitor);
        if (hasKeywords) {
            createKWLexer(fKwlexerFileName, kwLexerTemplateName, hasKeywords, fProject, monitor);
        }
		createParseController(fControllerFileName, parseCtlrTemplateName, hasKeywords, fProject, monitor);
		createNodeLocator(fLocatorFileName, locatorTemplateName, fProject, monitor);
		
		editFile(monitor, grammarFile);
		enableBuilders(monitor, fProject, new String[] { JikesPGBuilder.BUILDER_ID });
    }

    static final String astDirectory= "Ast";

    static final String astNode= "ASTNode";

    static final String sAutoGenTemplate= "%options parent_saved,automatic_ast=toplevel,visitor=preorder,ast_directory=./" + astDirectory
	    + ",ast_type=" + astNode;

    static final String sKeywordTemplate= "%options filter=kwTemplate.gi";

    private IFile createGrammar(String fileName, String templateName,
	    boolean autoGenerateASTs, IProject project, IProgressMonitor monitor) throws CoreException {

	Map subs= getStandardSubstitutions();

	subs.put("$AUTO_GENERATE$", autoGenerateASTs ? sAutoGenTemplate : "");
	subs.put("$TEMPLATE$", templateName);

	String grammarTemplateFileName = "grammar.g";
	return createFileFromTemplate(fileName, grammarTemplateFileName, fPackageFolder, subs, project, monitor);
    }

    private IFile createLexer(String fileName, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	Map subs= getStandardSubstitutions();

	subs.put("$TEMPLATE$", templateName);
	subs.put("$KEYWORD_FILTER$",
		hasKeywords ? ("%options filter=" + fClassNamePrefix + "KWLexer.gi") : "");
	subs.put("$KEYWORD_LEXER$", hasKeywords ? ("$" + fClassNamePrefix + "KWLexer") : "Object");
	subs.put("$LEXER_MAP$", (hasKeywords ? "LexerBasicMap" : "LexerVeryBasicMap"));

	String lexerTemplateName = "lexer.gi";
	return createFileFromTemplate(fileName, lexerTemplateName, fPackageFolder, subs, project, monitor);
    }

    private IFile createKWLexer(String fileName, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	Map subs= getStandardSubstitutions();
	subs.put("$TEMPLATE$", templateName);

	String kwLexerTemplateName = "kwlexer.gi";
	return createFileFromTemplate(fileName, kwLexerTemplateName, fPackageFolder, subs, project, monitor);
    }

    private IFile createParseController(
    	String fileName, String templateName, boolean hasKeywords, IProject project, IProgressMonitor monitor)
    throws CoreException {
		Map subs= getStandardSubstitutions();
	
		subs.put("$AST_PKG_NODE$", fPackageName + "." + astDirectory + "." + astNode);
		subs.put("$AST_NODE$", astNode);
		subs.put("$PARSER_TYPE$", fClassNamePrefix + "Parser");
		subs.put("$LEXER_TYPE$", fClassNamePrefix + "Lexer");
		
		String parseControllerTemplateName = "ParseController.java";
		return createFileFromTemplate(fileName, parseControllerTemplateName, fPackageFolder, subs, project, monitor);
    }

    
	// SMS 29 Sep 2006
    // All this node locator stuff, to provide one that has the same AST node type
    // as is generated for the parser by the other parts of this wizard (and unlike
    // the node type assumed in org.eclipse.uide.parser)
    
	
    private IFile createNodeLocator(
		String fileName, String templateName, IProject project, IProgressMonitor monitor) throws CoreException
    {
    	Map subs= getStandardSubstitutions();

    	subs.put("$AST_PKG_NODE$", fPackageName + "." + astDirectory + "." + astNode);
    	subs.put("$AST_NODE$", astNode);
    	//subs.put("$PARSER_TYPE$", fClassNamePrefix + "Parser");
    	//subs.put("$LEXER_TYPE$", fClassNamePrefix + "Lexer");

    	return createFileFromTemplate(fileName, templateName, fPackageFolder, subs, project, monitor);
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

    protected Map getStandardSubstitutions() {
        Map result= new HashMap();
        result.put("$LANG_NAME$", fLanguageName);
        result.put("$CLASS_NAME_PREFIX$", fClassNamePrefix);
        result.put("$PACKAGE_NAME$", fPackageName);
        return result;
    }
    
    
    
    /**
     * Return the names of any existing files that would be clobbered by the
     * new files to be generated.
     * 
     * @return	An array of names of existing files that would be clobbered by
     * 			the new files to be generated
     */
    protected String[] getFilesThatCouldBeClobbered() {
    	String prefix = fProject.getLocation().toString() + '/' + getProjectSourceLocation() + fPackageName.replace('.', '/') + '/';
		return new String[] {
    			prefix + fGrammarFileName,
    		    prefix + fLexerFileName,
    		    prefix + fKwlexerFileName,
    		    prefix + fControllerFileName
    	};
    }
    
}
