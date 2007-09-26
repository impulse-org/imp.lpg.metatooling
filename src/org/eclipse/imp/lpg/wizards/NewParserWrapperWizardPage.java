/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
package org.eclipse.imp.lpg.wizards;

import org.eclipse.imp.core.ErrorHandler;
import org.eclipse.imp.wizards.GeneratedComponentAttribute;
import org.eclipse.imp.wizards.GeneratedComponentWizard;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * The "New" wizard page allows setting the container for the new file as well as the
 * file name. The page will only accept file name without the extension OR with the
 * extension that matches the expected one (g).
 */
public class NewParserWrapperWizardPage extends NewLanguageSupportWizardPage
{
    protected static final String thisWizardName = "New IMP Parser Wrapper";
    protected static final String thisWizardDescription =
    	"This wizard creates new IMP parser wrapper and AST node locator.";
 

    public NewParserWrapperWizardPage(GeneratedComponentWizard wizard, GeneratedComponentAttribute[] wizardAttributes) {
//		super(wizard, RuntimePlugin.IMP_RUNTIME, "parserWrapper");
//		setTitle("");
//		setDescription("This wizard creates new parser wrapper and AST node locator.");
		super(wizard, /*RuntimePlugin.IMP_RUNTIME,*/ "lpgGrammar", true,
				wizardAttributes, thisWizardName, thisWizardDescription);
    	
    }	
	
    
    protected void createAdditionalControls(Composite parent) {
    	createTextField(parent, "ParserWrapper", "class",
    		"The qualified name of the parser-wrapper class to be generated", 
    		"", "ClassBrowse", true);    	       	
    }	
    
 
    public void createControl(Composite parent) {
		super.createControl(parent);
		setLanguageIfEmpty();
		try {
			// SMS 10 May 2007
			// setEnabled was called with "false"; I don't know why,
			// but I want an enabled field (and enabling it here
			// hasn't seemed to cause any problems)
		    getField("class").setEnabled(true);

            getField("language").fText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {	
                    setClass();
                }
            });
		    fProjectText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
				    setLanguage();
				}
		    });
		} catch (Exception e) {
		    ErrorHandler.reportError("NewParserWrapperWizardPage.createControl(..):  Internal error, extension point schema may have changed", e);
		}
    }

}
