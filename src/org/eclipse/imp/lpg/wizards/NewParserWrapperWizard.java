/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package org.eclipse.imp.lpg.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.imp.wizards.ExtensionEnabler;
import org.eclipse.imp.wizards.GeneratedComponentWizardPage;
import org.eclipse.ui.INewWizard;

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
    	fWizardAttributes = setupAttributes();
    	addPages(new GeneratedComponentWizardPage[] { new NewParserWrapperWizardPage(this, fWizardAttributes) });
    }

    protected void collectCodeParms() {
    	NewParserWrapperWizardPage page= (NewParserWrapperWizardPage) pages[0];

    	IProject project = null;
    	project = page.getProjectBasedOnNameField();
    	if (project ==  null) {
    		project= page.getSelectedProject();
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
		fLocatorFileName = fGrammarOptions.getDefaultSimpleNameForPositionLocator(fLanguageName) + ".java";			// fClassNamePrefix + "ASTNodeLocator.java";
    }

    	
    protected void generateCodeStubs(IProgressMonitor monitor) throws CoreException {
		boolean hasKeywords= fGrammarOptions.getHasKeywords();
		boolean requiresBacktracking= fGrammarOptions.getRequiresBacktracking();
		boolean autoGenerateASTs= fGrammarOptions.getAutoGenerateASTs();

	    String parseCtlrTemplateName= "SeparateParseController.java";
		String locatorTemplateName = "SeparateSourcePositionLocator.java";

		IFile parseControllerFile = createParseController(fControllerFileName, parseCtlrTemplateName, hasKeywords, fProject, monitor);
		IFile locatorFile = createSourcePositionLocator(fLocatorFileName, locatorTemplateName, fProject, monitor);
		
        ExtensionEnabler.enable(fProject, "org.eclipse.imp.runtime", "parser", new String[][] {
                { "extension:id", fProject.getName() + ".parser" },
                { "extension:name", fLanguageName + " Parser" },
                { "parser:class", fPackageName + "." + fClassNamePrefix + "ParseController" },
                { "parser:language", fLanguageName }
        		}, 	
        		false,
        		getPluginDependencies(),
        		new NullProgressMonitor());

		editFile(monitor, parseControllerFile);
		editFile(monitor, locatorFile);
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
