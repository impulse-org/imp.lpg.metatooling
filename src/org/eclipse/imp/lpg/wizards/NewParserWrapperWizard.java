package org.jikespg.uide.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.INewWizard;
import org.eclipse.uide.wizards.ExtensionPointEnabler;
import org.eclipse.uide.wizards.ExtensionPointWizardPage;

/**
 * This wizard creates a JikesPG grammar, a "parser" language service, and a
 * stub IParseController implementation for use with the UIDE.
 */	
public class NewParserWrapperWizard extends NewLanguageSupportWizard	//ExtensionPointWizard 
	implements INewWizard
{
    
    public NewParserWrapperWizard() {
		super();
		setNeedsProgressMonitor(true);
    }

    public void addPages() {
    	addPages(new ExtensionPointWizardPage[] { new NewParserWrapperWizardPage(this) });
    }

    protected void collectCodeParms() {
    	NewParserWrapperWizardPage page= (NewParserWrapperWizardPage) pages[0];
        
        //fProject=page.getProject();
    	IProject project = null;
    	String projectName = page.getProjectNameFromField();
    	if (projectName != null) {
    		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    	}
    	if (project ==  null) {
    		project= page.getProject();
    	}
        if (project == null) {
        	throw new IllegalStateException(
        		"NewParserWrapperWizard.collectCodeParms():  project cannot be identified.");
        }
    	fProject = project;
        
        
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

        fControllerFileName = fGrammarOptions.getDefaultSimpleNameForParseController(fLanguageName) + ".java";	//fClassNamePrefix + "ParseController.java";
		fLocatorFileName = fGrammarOptions.getDefaultSimpleNameForNodeLocator(fLanguageName) + ".java";			// fClassNamePrefix + "ASTNodeLocator.java";
    }

    	
    protected void generateCodeStubs(IProgressMonitor monitor) throws CoreException {
		boolean hasKeywords= fGrammarOptions.getHasKeywords();
		boolean requiresBacktracking= fGrammarOptions.getRequiresBacktracking();
		boolean autoGenerateASTs= fGrammarOptions.getAutoGenerateASTs();
		String templateKind= fGrammarOptions.getTemplateKind();

	    String parseCtlrTemplateName= "SeparateParseController.java";
		String locatorTemplateName = "SeparateASTNodeLocator.java";

		IFile parseControllerFile = createParseController(fControllerFileName, parseCtlrTemplateName, hasKeywords, fProject, monitor);
		IFile nodeLocatorFile = createNodeLocator(fLocatorFileName, locatorTemplateName, fProject, monitor);

		
        ExtensionPointEnabler.enable(fProject, "org.eclipse.uide.runtime", "parser", new String[][] {
                { "extension:id", fProject.getName() + ".parserWrapper" },
                { "extension:name", fLanguageName + " Parser Wrapper" },
                { "parserWrapper:class", fPackageName + "." + fClassNamePrefix + "ParseController" },
                { "parserWrapper:language", fLanguageName }
        		}, 	
        		false,
        		getPluginDependencies(),
        		new NullProgressMonitor());
		
		editFile(monitor, parseControllerFile);
		editFile(monitor, nodeLocatorFile);
    }
    
    
    /**
     * Return the names of any existing files that would be clobbered by the
     * new files to be generated.
     * 
     * @return	An array of names of existing files that would be clobbered by
     * 			the new files to be generated
     */
    protected String[] getFilesThatCouldBeClobbered() {
    	String prefix = getFileNamePrefix();
    	List<String> fileNames = new ArrayList();
    	
	    fileNames.add(prefix + fControllerFileName);
	    fileNames.add(prefix + fLocatorFileName);
  	
    	String[] result = fileNames.toArray(new String[0]);
    	return result;
    }
    
}
