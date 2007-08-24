/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
package org.eclipse.imp.lpg.wizards;

import org.eclipse.imp.core.ErrorHandler;
import org.eclipse.imp.runtime.RuntimePlugin;
import org.eclipse.imp.wizards.ExtensionPointWizard;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * The "New" wizard page allows setting the container for the new file as well as the
 * file name. The page will only accept file name without the extension OR with the
 * extension that matches the expected one (g).
 */
public class NewLPGGrammarWizardPage extends NewLanguageSupportWizardPage
{

    public NewLPGGrammarWizardPage(ExtensionPointWizard wizard) {
		super(wizard, RuntimePlugin.IMP_RUNTIME, "lpgGrammar");
		setTitle("LPG Grammar");
		setDescription("This wizard creates new LPG grammar and grammar-include files with '.g' and '.gi' extensions.");
    }
	
    protected void createAdditionalControls(Composite parent) {
    	GrammarAndParserPageHelper helper= new GrammarAndParserPageHelper(parent, null, fGrammarOptions, getShell());
		helper.createImplLanguageField();
		helper.createOptionsFields();
    }	


    public void createControl(Composite parent) {
		super.createControl(parent);
		// Possible (or desirable?) here to set language even if not empty?
		setLanguageIfEmpty();
		try {
	
			// Don't worry about changing the Package field if the language
			// changes (so long as the project hasn't changed), since the language
			// name and the target package for the grammar can vary independently
	        
		    fProjectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// SMS 13 Jun 2007
			    //setLanguageIfEmpty();
				// Need a new language with a new project	
				setLanguage();
				// Clear target package for grammar; it must change whent
				// the project changes, but we don't presume a default value
				getField("Package").setText("");
			}
		    });
		} catch (Exception e) {
		    ErrorHandler.reportError("NewLanguageSupportWizardPage.createControl(..):  Internal error, extension point schema may have changed", e);
		}
    }

}
