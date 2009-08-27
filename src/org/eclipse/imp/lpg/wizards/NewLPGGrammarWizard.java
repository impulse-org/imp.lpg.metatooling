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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.imp.lpg.builder.LPGBuilder;
import org.eclipse.imp.wizards.ExtensionEnabler;
import org.eclipse.imp.wizards.GeneratedComponentWizardPage;
import org.eclipse.imp.wizards.WizardUtilities;
import org.eclipse.pde.core.plugin.IPluginModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;

/**
 * This wizard creates a JikesPG grammar, a "parser" language service, and a
 * stub IParseController implementation for use with the UIDE.
 */
public class NewLPGGrammarWizard extends NewLanguageSupportWizard	//ExtensionPointWizard 
	implements INewWizard
{
	
	public final static String PACKAGE_FIELD_NAME = "Package";
	
    public NewLPGGrammarWizard() {
		super();
		setNeedsProgressMonitor(true);
    }

    public void addPages() {
    	fWizardAttributes = setupAttributes();
    	addPages(new GeneratedComponentWizardPage[] { new NewLPGGrammarWizardPage(this, fWizardAttributes) });
    }


    protected void collectCodeParms() {

    	NewLPGGrammarWizardPage page= (NewLPGGrammarWizardPage) pages[0];
		IProject project = null;
    	project = page.getProjectBasedOnNameField();
    	if (project ==  null) {
    		project= page.getSelectedProject();
    	}
        if (project == null) {
        	throw new IllegalStateException(
        		"NewLPGGrammarWizard.collectCodeParms():  project cannot be identified.");
        }
    	fProject = project;
    	
        fGrammarOptions= page.fGrammarOptions;
        fGrammarOptions.setLanguageName(page.getValue("language"));
        fGrammarOptions.setProjectName(fProject.getName());
        
	    fLanguageName= fGrammarOptions.getLanguageName();
	    
	    fPackageName = page.getValue(PACKAGE_FIELD_NAME);
	    fPackageFolder = fPackageName.replace('.', '/');
	    fPackageFolder = getProjectSourceLocation(fProject) + fPackageFolder;
	    IPath packageFolderPath = new Path(fPackageFolder);
	    IResource packageFolderResource = fProject.findMember(packageFolderPath);


        // SMS 11 Jun 2007:  Added checks on selection of folder 
        if (packageFolderResource == null) {
        	// Selected package folder is not under project source folder
    		String message = "Selected package is not under project source folder; aborting generation.";
        	Shell parent = this.getShell();
        	MessageBox messageBox = new MessageBox(parent, (SWT.OK));
        	messageBox.setMessage(message);
        	messageBox.open();
        	throw new IllegalArgumentException(
        		"NewLPGGrammarWizard.collectCodeParms():  Selected package is not under project source location.");
        } else {
        	// presumably have a package name
        	fPackageFolder = fPackageFolder.substring(getProjectSourceLocation(fProject).length(), fPackageFolder.length());
        }
        
        
        // SMS 13 Apr 2007 moving this here from generateCodeStubs
        // so that the information is available for advising the user
        // before stubs are generated
        fGrammarOptions.setClassNamePrefix(Character.toUpperCase(fLanguageName.charAt(0)) + fLanguageName.substring(1));
        fClassNamePrefix = fGrammarOptions.getClassNamePrefix();

        fGrammarFileName= fClassNamePrefix + "Parser.g";
	    fLexerFileName= fClassNamePrefix + "Lexer.gi";
	    fKwlexerFileName= fClassNamePrefix + "KWLexer.gi";
    }

    	
    protected void generateCodeStubs(IProgressMonitor monitor) throws CoreException {
		boolean hasKeywords= fGrammarOptions.getHasKeywords();
		boolean requiresBacktracking= fGrammarOptions.getRequiresBacktracking();
		boolean autoGenerateASTs= fGrammarOptions.getAutoGenerateASTs();

        String parserTemplateName= (requiresBacktracking ? "bt" : "dt") + "ParserTemplate";
	    String lexerTemplateName= "LexerTemplate";
	    String kwLexerTemplateName= "KeywordTemplate";
		
	    // SMS 20 Sep 2007
	    // Need to update plug-in dependencies even though not creating an extension
	    // (previously we've relied on the enabling of extensions to do this)
	    //
	    // fProject should be set by this point
	    IPluginModel pluginModel = ExtensionEnabler.getPluginModelForProject(fProject);

	    if (pluginModel != null) {
	    	ExtensionEnabler.addRequiredPluginImports(pluginModel, fProject, getPluginDependencies());
	    } else {
	    	// RMF 8/27/2009 - Need to manipulate the project's build path to add the LPG runtime jar...
	    }
	    
		IFile lexerFile = createLexer(fLexerFileName, lexerTemplateName, hasKeywords, fProject, monitor);
		editFile(monitor, lexerFile);
        if (hasKeywords) {
            IFile kwLexerFile = createKWLexer(fKwlexerFileName, kwLexerTemplateName, hasKeywords, fProject, monitor);
            editFile(monitor, kwLexerFile);
        }
		IFile grammarFile= createGrammar(fGrammarFileName, parserTemplateName, autoGenerateASTs, fProject, monitor);
		editFile(monitor, grammarFile);

        // RMF 3/4/2009 - This is no longer necessary, now that all the templates (even for IMP use)
        // are in the lpg.generator plugin.
//		setIncludeDirPreference();
		WizardUtilities.enableBuilders(monitor, fProject, new String[] { LPGBuilder.BUILDER_ID });
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
    	
    	if (fGrammarOptions.getGenGrammarFiles()) {
			fileNames.add(prefix + fGrammarFileName);
		    fileNames.add(prefix + fLexerFileName);
		    fileNames.add(prefix + fKwlexerFileName);
    	}
  	
    	String[] result = fileNames.toArray(new String[0]);
    	return result;
    }
    
    
    
    /**
     * Post a dialog advising the user that finishing the wizard
     * could lead to the clobbering of previously auto-generated AST
     * classes.  If the user is OK  with that, continue by calling
     * oktoClobberFiles in the parent with the list of specific files
     * that might be clobbered and return the results of that.
     * 
     * Current implementation expects that the file names provided will
     * be the full absolute path names in the file system.
     * 
     * @param files		The names of files that would be clobbered by
     * 					files to be generated
     * @return			True if the user presses OK in response to
     * 					all dialogs involved in the check; false
     *                  if the user presses CANCEL for any of them
     *                  (presumably)
     */
    protected boolean okToClobberFiles(String[] files)
    {	
        // Safety check to warn the user about the possibility of clobbering existing
        // auto-generated AST classes (that may have been modified)
    	String astDir = getFileNamePrefix() + astDirectory;
    	final File file= new File(astDir);
    	
    	if (fGrammarOptions.getAutoGenerateASTs() && file.exists()) {
    		String message = "Any previously auto-generated AST classes will be overwritten; proceed?";
        	Shell parent = this.getShell();
        	MessageBox messageBox = new MessageBox(parent, (SWT.CANCEL | SWT.OK));
        	messageBox.setMessage(message);
        	int result = messageBox.open();
        	if (result == SWT.CANCEL)
        		return false;
    	}
    	
    	return super.okToClobberFiles(files);
    }
    
}
