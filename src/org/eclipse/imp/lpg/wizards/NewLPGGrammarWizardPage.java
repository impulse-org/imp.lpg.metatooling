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

import org.eclipse.core.resources.IProject;
import org.eclipse.imp.core.ErrorHandler;
import org.eclipse.imp.ui.dialogs.filters.ViewerFilterForJavaProjects;
import org.eclipse.imp.ui.dialogs.validators.SelectionValidatorForJavaProjects;
import org.eclipse.imp.utils.ValidationUtils;
import org.eclipse.imp.wizards.GeneratedComponentAttribute;
import org.eclipse.imp.wizards.GeneratedComponentWizard;
import org.eclipse.imp.wizards.WizardPageField;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionValidator;

/**
 * The "New" wizard page allows setting the container for the new file as well as the
 * file name. The page will only accept file name without the extension OR with the
 * extension that matches the expected one (g).
 */
public class NewLPGGrammarWizardPage extends NewLanguageSupportWizardPage
{
    protected static final String thisWizardName = "New LPG Grammar Wizard";
    protected static final String thisWizardDescription =
    	"This wizard creates new LPG grammar and grammar-include files with '.g' and '.gi' extensions.";

    public NewLPGGrammarWizardPage(GeneratedComponentWizard wizard, GeneratedComponentAttribute[] wizardAttributes) {
		//super(wizard, RuntimePlugin.IMP_RUNTIME, "lpgGrammar");
		//setTitle("LPG Grammar");
		//setDescription("This wizard creates new LPG grammar and grammar-include files with '.g' and '.gi' extensions.");
		super(wizard, /*RuntimePlugin.IMP_RUNTIME,*/ "lpgGrammar", true,
				wizardAttributes, thisWizardName, thisWizardDescription);
    }
	
    protected void createAdditionalControls(Composite parent) {
    	createTextField(parent, "LPGGrammar", NewLPGGrammarForIMPWizard.PACKAGE_FIELD_NAME,
        		"The package in which the grammar templates are to be instantiated", 
        		"", "PackageBrowse", true);    
    	GrammarAndParserPageHelper helper= new GrammarAndParserPageHelper(parent, null, fGrammarOptions, getShell());
		helper.createImplLanguageField();
		helper.createOptionsFields();
    }	


    public void createControl(Composite parent) {
    	
		super.createControl(parent);
	    
		// SMS 11 Feb 2008
		// Don't worry about setting the language, based on the
		// project or otherwise, since in this case the name of
		// the language is totally open.
		
		// SMS 11 Feb 2008
		// Also, don't worry about listening for changes to the project,
		// since the project is not presumed to define a language
    }
    
    
	// SMS 28 Nov 2007
    protected void adjustLanguageByProject(IProject project, WizardPageField languageField)
    {
    	if (project != null) {
    		if (ValidationUtils.isIDEProject(project)) {
    			setLanguageIfEmpty();
    			languageField.setEnabled(false);
    		} else {
    			languageField.setText("");
    			languageField.setEnabled(true);
    		}
    	}	
    }

    
    protected ViewerFilter getViewerFilterForProjects() {
    	return new ViewerFilterForJavaProjects();
    }

	protected ISelectionValidator getSelectionValidatorForProjects() {
		return new SelectionValidatorForJavaProjects();
	}
    
	
	
	
	/*
	 * Overrides the corresponding method in IMPWizardPage.
	 * 
	 * Here we need to create the field but we do not want to initialize it with
	 * a language name and we want it to be enabled so that it can be edited
	 * (since the user has to provide a name).
	 * 
	 * Also, since this field is independent of any others on the page, there
	 * is no need for a listener to the project field and no need to be able to
	 * add listeners from other fields.
	 * 
	 * @see org.eclipse.imp.wizards.IMPWizardPage#createLanguageFieldForComponent(org.eclipse.swt.widgets.Composite, java.lang.String)
	 */
    protected void createLanguageFieldForComponent(Composite parent, String componentID) {
        WizardPageField languageField= new WizardPageField(
        	componentID, "language", "Language", "", 0, true, "Language for which to create " + componentID);

        fLanguageText= createLabelTextBrowse(parent, languageField, null);
        fLanguageText.setData(languageField);

        fLanguageText.setEnabled(true);
        
        fFields.add(languageField);    
    }
	
	
}
