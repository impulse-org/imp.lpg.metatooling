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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.imp.core.ErrorHandler;
import org.eclipse.imp.wizards.ExtensionPointEnabler;
import org.eclipse.imp.wizards.GeneratedComponentAttribute;
import org.eclipse.imp.wizards.GeneratedComponentWizard;
import org.eclipse.imp.wizards.GeneratedComponentWizardPage;
import org.eclipse.imp.wizards.WizardPageField;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModel;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.internal.core.ischema.IMetaAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class NewLanguageSupportWizardPage extends GeneratedComponentWizardPage //ExtensionPointWizardPage
{

    GrammarOptions fGrammarOptions= new GrammarOptions();
	
  
   public NewLanguageSupportWizardPage(
	    	GeneratedComponentWizard owner, /*String pluginID,*/ String componentID, boolean omitIDName,
	    	GeneratedComponentAttribute[] attributes, String wizardName, String wizardDescription)
    {
        super(owner, /*pluginID,*/ componentID, omitIDName, attributes, wizardName, wizardDescription);
    }
   
   
   
   /*
    * Set the wizard's implementation class field (i.e., the name of the parse controller
    * class), but only if the field is currently empty.
    * 
    * SMS 14 Jun 2007:  This method actually sets the class field in any case, and that
    * hasn't seemed to be a problem.  For consistency with its name, I've changed the
    * implementation here to only set the class if the field is empty and created a
    * second method, setClass(), to set the class in any case.
    */
   protected void setClassIfEmpty() {
       try {
           WizardPageField langField= getField("language");
           String language= langField.getText();
           if (language.length() == 0) {
               ErrorHandler.reportError("NewLPGGrammarWithParserWrapperWizardPage.setClassIfEmpty:  Cannot set class field (no language given)");
               return;
           }
           
           String parseControllerName = fGrammarOptions.getDefaultQualifiedNameForParseController(language);

           WizardPageField classField= getField("class");

           if (classField.getText().length() == 0)
               classField.setText(language);

           getField("class").setText(parseControllerName);
           
       } catch (Exception e) {
           ErrorHandler.reportError("NewUIDEParserOnlyWizard.setClassIfEmpty:  Cannot set class field (exception)", e);
       }
   }
   
   
   
   
   /*
    * Set the wizard's implementation class field (i.e., the name of the parse controller
    * class).
    */
   protected void setClass() {
       try {
           WizardPageField langField= getField("language");
           String language= langField.getText();
           if (language.length() == 0) {
               ErrorHandler.reportError("NewLPGGrammarWithParserWrapperWizardPage.setClassIfEmpty:  Cannot set class field (no language given)");
               return;
           }
           
           String parseControllerName = fGrammarOptions.getDefaultQualifiedNameForParseController(language);
           getField("class").setText(parseControllerName);
           
       } catch (Exception e) {
           ErrorHandler.reportError("NewUIDEParserOnlyWizard.setClassIfEmpty:  Cannot set class field (exception)", e);
       }
   }

	   
    /*
     * Set the wizard's language field from the value recorded in the plugin,
     * if any.
     */
    protected void setLanguageIfEmpty() {
        try {
            String pluginLang= determineLanguage(); // if a languageDesc exists
            
            if (pluginLang.length() == 0) {
                ErrorHandler.reportError("NewLanguageSupportWizardPage.setLanguageIfEmpty:  Cannot set language field (no language extension found)");	
                return;
            }

            WizardPageField field= getField("Language");

            // Field might be null if we haven't defined the language yet
            // (which here we might very well not have)
            if (field != null && field.getText().length() == 0)
                field.setText(pluginLang);

        } catch (Exception e) {
            ErrorHandler.reportError("NewLanguageSupportWizardPage.setLanguageIfEmpty:  Cannot set language field (exception)", e);	
        }
    }

    
    // SMS 13 Jun 2007
    protected void setLanguage() {
        try {
            String pluginLang= determineLanguage(); // if a languageDesc exists
            
            if (pluginLang.length() == 0) {
                ErrorHandler.reportError("NewLanguageSupportWizardPage.setLanguage:  Cannot set language field (no language extension found)");	
                return;
            }

            WizardPageField field= getField("Language");
            field.setText(pluginLang);

        } catch (Exception e) {
            ErrorHandler.reportError("NewLanguageSupportWizardPage.setLanguage:  Cannot set language field (exception)", e);	
        }
    }
	
    
    public String determineLanguage() {
    	try {
        	// SMS 9 Oct 2007
            IProject project = null;
            if (fProject != null)
            	project = fProject;
            else
            	project = getProjectBasedOnNameField();
        	
        	IPluginModel pluginModel= ExtensionPointEnabler.getPluginModel(project);
    	    if (pluginModel != null) {
    	    	// SMS  26 Jul 2007:  Load the extensions model in detail from the
    	    	// plugin.xml file using our customized method; loading the model
    	    	// in detail is necessary to make the children of an extension available
    	    	// (as needed below)
    	    	ExtensionPointEnabler.loadImpExtensionsModel(pluginModel, project);
    	    	
	    		IPluginExtension[] extensions= pluginModel.getExtensions().getExtensions();
	    		for(int n= 0; n < extensions.length; n++) {
	    		    IPluginExtension extension= extensions[n];
                    if (!extension.getPoint().equals("org.eclipse.imp.runtime.languageDescription"))
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
    	    } else if (project != null)
    	    	System.out.println("Not a plugin project: " + project.getName());
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return "";
    }
    
    

    public GrammarOptions getOptions() {
    	return fGrammarOptions;
    }
	
	
	
}
