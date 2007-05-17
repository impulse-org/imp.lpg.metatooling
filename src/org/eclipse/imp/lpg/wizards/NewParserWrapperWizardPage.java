package org.jikespg.uide.wizards;

import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModel;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.uide.core.ErrorHandler;
import org.eclipse.uide.runtime.RuntimePlugin;
import org.eclipse.uide.wizards.ExtensionPointEnabler;
import org.eclipse.uide.wizards.ExtensionPointWizard;
import org.eclipse.uide.wizards.ExtensionPointWizardPage;
import org.eclipse.uide.wizards.WizardPageField;

/**
 * The "New" wizard page allows setting the container for the new file as well as the
 * file name. The page will only accept file name without the extension OR with the
 * extension that matches the expected one (g).
 */
public class NewParserWrapperWizardPage extends ExtensionPointWizardPage {
    GrammarOptions fGrammarOptions= new GrammarOptions();

    public NewParserWrapperWizardPage(ExtensionPointWizard wizard) {
		super(wizard, RuntimePlugin.UIDE_RUNTIME, "parserWrapper");
		setTitle("Parser Wrapper");
		setDescription("This wizard creates new parser wrapper and AST node locator.");
    }	
	
    protected void createAdditionalControls(Composite parent) {
    	GrammarAndParserPageHelper helper= new GrammarAndParserPageHelper(parent, null, fGrammarOptions, getShell());
//		helper.createTemplateField();
//		helper.createImplLanguageField();
//		helper.createOptionsFields();
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
	        String lang= getField("language").getText();

            getField("language").fText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {	
                    setClassIfEmpty();
                }
            });
		    fProjectText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
				    setLanguageIfEmpty();
				}
		    });
		} catch (Exception e) {
		    ErrorHandler.reportError("Internal error, extension point schema may have changed", e);
		}
    }

    public String determineLanguage() {
	try {
		IPluginModel pluginModel= ExtensionPointEnabler.getPluginModel(getProject());

	    if (pluginModel != null) {
		IPluginExtension[] extensions= pluginModel.getExtensions().getExtensions();

		for(int n= 0; n < extensions.length; n++) {
		    IPluginExtension extension= extensions[n];

                    if (!extension.getPoint().equals("org.eclipse.uide.runtime.languageDescription"))
                        continue;

                    IPluginObject[] children= extension.getChildren();

		    for(int k= 0; k < children.length; k++) {
			IPluginObject object= children[k];

			if (object.getName().equals("language")) {
			    return ((IPluginElement) object).getAttribute("language").getValue();
			}
		    }
		    System.out.println("Unable to determine language for plugin '" + pluginModel.getBundleDescription().getName() + "': no languageDescription extension.");
		}
	    } else if (getProject() != null)
		System.out.println("Not a plugin project: " + getProject().getName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return "";
    }

    /*
     * Set the wizard's language field from the value recorded in the plugin,
     * if any.  Also set the class field.
     */
    protected void setLanguageIfEmpty() {
        try {
            String pluginLang= determineLanguage(); // if a languageDesc exists
            
            if (pluginLang.length() == 0) {
                ErrorHandler.reportError("	.setLanguageIfEmpty:  Cannot set language field (no language extension found)");	
                return;
            }

            WizardPageField field= getField("language");

            if (field.getText().length() == 0)
                field.setText(pluginLang);
            
            String parseControllerName = fGrammarOptions.getDefaultQualifiedNameForParseController(pluginLang);
            getField("class").setText(parseControllerName);
        } catch (Exception e) {
            ErrorHandler.reportError("NewLPGGrammarWizard.setLanguageIfEmpty:  Cannot set language field (exception)", e);	
        }
    }
    
    
    /*
     * Set the wizard's implementation class field (i.e., the name of the parse controller
     * class).
     */
    protected void setClassIfEmpty() {
        try {
            WizardPageField langField= getField("language");
            String language= langField.getText();
            if (language.length() == 0) {
                ErrorHandler.reportError("NewUIDEParserOnlyWizard.setClassIfEmpty:  Cannot set class field (no language given)");
                return;
            }
            
            String parseControllerName = fGrammarOptions.getDefaultQualifiedNameForParseController(language);
            getField("class").setText(parseControllerName);
            
        } catch (Exception e) {
            ErrorHandler.reportError("NewUIDEParserOnlyWizard.setClassIfEmpty:  Cannot set class field (exception)", e);
        }
    }

    public GrammarOptions getOptions() {
	return fGrammarOptions;
    }
    
    
    public void createFields() {

    }
  
}
