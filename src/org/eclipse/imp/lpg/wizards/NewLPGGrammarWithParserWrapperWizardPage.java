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
public class NewLPGGrammarWithParserWrapperWizardPage extends NewLanguageSupportWizardPage
{

    protected static final String thisWizardName = "New LPG Grammar with Parser Wrapper Wizard";
    protected static final String thisWizardDescription =
    	"This wizard creates new LPG grammar and grammar-include files with '.g' and '.gi' extensions.\n" +
    	"It also creates IMP parser-wrapper and AST node-locator classes.  Grammar files are created in the\n" +
    	"same directory as the parser wrapper";
    
	
	
	public NewLPGGrammarWithParserWrapperWizardPage(GeneratedComponentWizard wizard, GeneratedComponentAttribute[] wizardAttributes) {
		super(wizard, /*RuntimePlugin.IMP_RUNTIME,*/ "lpgGrammarWithParserWrapper", true,
				wizardAttributes, thisWizardName, thisWizardDescription);
//		setTitle("LPG Grammar with ParserWrapper");
//		setDescription();
	
    }
	
    protected void createAdditionalControls(Composite parent) {
    	createTextField(parent, "GrammarWithParserWrapper", "class",
    		"The qualified name of the parser-wrapper class to be generated", 
    		"", "ClassBrowse", true);    	       	
    	GrammarAndParserPageHelper helper= new GrammarAndParserPageHelper(parent, null, fGrammarOptions, getShell());
		helper.createImplLanguageField();
		helper.createOptionsFields();
    }	


    public void createControl(Composite parent) {
		super.createControl(parent);
		setLanguageIfEmpty();
		try {
		    getField("class").setEnabled(true);
            //String lang= getField("language").getText();
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
		    ErrorHandler.reportError("NewLPGGrammarWithParserWrapperWizardPage.createControl(..):  exception:", e);
		}
    }

  
}
