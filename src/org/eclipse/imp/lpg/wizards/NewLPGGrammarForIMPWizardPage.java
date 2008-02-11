/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
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
import org.eclipse.ui.dialogs.ISelectionValidator;

/**
 * The "New" wizard page allows setting the container for the new file as well as the
 * file name. The page will only accept file name without the extension OR with the
 * extension that matches the expected one (g).
 */
public class NewLPGGrammarForIMPWizardPage extends NewLanguageSupportWizardPage
{
    protected static final String thisWizardName = "New LPG Grammar Wizard";
    protected static final String thisWizardDescription =
    	"This wizard creates new LPG grammar and grammar-include files with '.g' and '.gi' extensions.";

    public NewLPGGrammarForIMPWizardPage(GeneratedComponentWizard wizard, GeneratedComponentAttribute[] wizardAttributes) {
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
		// Possible (or desirable?) here to set language even if not empty?
		setLanguageIfEmpty();
		
		adjustLanguageByProject(fProject, getField("Language"));


		try {
	
			// Don't worry about changing the Package field if the language
			// changes (so long as the project hasn't changed), since the language
			// name and the target package for the grammar can vary independently
	        
		    fProjectText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					// SMS 13 Jun 2007
				    //setLanguageIfEmpty();
					// Need a new language with a new project	
					//setLanguage();
					
					adjustLanguageByProject(fProject, getField("Language"));
						
					// Clear target package for grammar; it must change whent
					// the project changes, but we don't presume a default value
					getField("Package").setText("");
				}
		    });
		} catch (Exception e) {
		    ErrorHandler.reportError("NewLPGGrammarWizardPage.createControl(..):  Internal error, extension point schema may have changed", e);
		}
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
    
}
