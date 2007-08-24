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
public class NewLPGGrammarWithParserWrapperWizardPage extends NewLanguageSupportWizardPage
{

	public NewLPGGrammarWithParserWrapperWizardPage(ExtensionPointWizard wizard) {
		super(wizard, RuntimePlugin.IMP_RUNTIME, "lpgGrammarWithParserWrapper");
		setTitle("LPG Grammar with ParserWrapper");
		setDescription("This wizard creates new LPG grammar and grammar-include files with '.g' and '.gi' extensions.\n" +
				"It also creates SAFARI parser-wrapper and AST node-locator classes.  Grammar files are created in the\n" +
				"same directory as the parser wrapper");
	
    }
	
    protected void createAdditionalControls(Composite parent) {
    	GrammarAndParserPageHelper helper= new GrammarAndParserPageHelper(parent, null, fGrammarOptions, getShell());
		helper.createImplLanguageField();
		helper.createOptionsFields();
    }	


    public void createControl(Composite parent) {
		super.createControl(parent);
		setLanguageIfEmpty();
		try {
		    getField("class").setEnabled(true);
	            String lang= getField("language").getText();
	            getField("language").fText.addModifyListener(new ModifyListener() {
	            	// SMS 14 Jun 2007
	                public void modifyText(ModifyEvent e) {
	                    //setClassIfEmpty();
	                	setClass();
	                }
	            });
		    fProjectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// SMS 13 Jun 2007
			    //setLanguageIfEmpty();
				setLanguage();
			}
		    });
		} catch (Exception e) {
		    ErrorHandler.reportError("NewLPGGrammarWithParserWrapperWizardPage.createControl(..):  Internal error, extension point schema may have changed", e);
		}
    }

  
}
