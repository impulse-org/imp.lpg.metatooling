package org.jikespg.uide.wizards;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.uide.core.ErrorHandler;
import org.eclipse.uide.runtime.RuntimePlugin;
import org.eclipse.uide.wizards.ExtensionPointWizard;

/**
 * The "New" wizard page allows setting the container for the new file as well as the
 * file name. The page will only accept file name without the extension OR with the
 * extension that matches the expected one (g).
 */
public class NewParserWrapperWizardPage extends NewLanguageSupportWizardPage
{

    public NewParserWrapperWizardPage(ExtensionPointWizard wizard) {
		super(wizard, RuntimePlugin.UIDE_RUNTIME, "parserWrapper");
		setTitle("Parser Wrapper");
		setDescription("This wizard creates new parser wrapper and AST node locator.");
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
