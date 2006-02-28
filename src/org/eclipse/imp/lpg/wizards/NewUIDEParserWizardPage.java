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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
public class NewUIDEParserWizardPage extends ExtensionPointWizardPage {
    private static final String UIDE= "org.eclipse.uide";

    private static final String JIKESPG= "org.jikespg.uide";

    GrammarOptions fGrammarOptions= new GrammarOptions();

    public NewUIDEParserWizardPage(ExtensionPointWizard wizard) {
	super(wizard, RuntimePlugin.UIDE_RUNTIME, "parser");
	setTitle("LPG Grammar for New Language");
	setDescription("This wizard creates a new grammar file with *.g extension.");
	requires.add(UIDE);
	requires.add(JIKESPG);
	requires.add("org.eclipse.core.runtime");
	requires.add("org.eclipse.core.resources");
    }

    protected void createAdditionalControls(Composite parent) {
	GrammarPageHelper helper= new GrammarPageHelper(parent, null, fGrammarOptions, getShell());

	helper.createFields();
    }

    public void createControl(Composite parent) {
	super.createControl(parent);
	setLanguageIfEmpty();
	try {
//	    getField("class").setEnabled(false);
	    projectText.addModifyListener(new ModifyListener() {
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
	    IPluginModel plugin= ExtensionPointEnabler.getPlugin(NewUIDEParserWizardPage.this);

	    if (plugin != null) {
		IPluginExtension[] extensions= plugin.getExtensions().getExtensions();

		for(int n= 0; n < extensions.length; n++) {
		    IPluginExtension extension= extensions[n];
		    IPluginObject[] children= extension.getChildren();

		    for(int k= 0; k < children.length; k++) {
			IPluginObject object= children[k];

			if (object.getName().equals("language")) {
			    return ((IPluginElement) object).getAttribute("language").getValue();
			}
		    }
		    System.out.println("No language extension in plugin '" + plugin.getBundleDescription().getName() + "'.");
		}
	    } else if (getProject() != null)
		System.out.println("Not a plugin project: " + getProject().getName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return "unknown";
    }

    protected void setLanguageIfEmpty() {
	try {
	    String language= determineLanguage();
	    WizardPageField field= getField("language");
	    if (field.getText().length() == 0)
		field.setText(language);
	    getField("class").setText(fGrammarOptions.getPackageForLanguage(language) + ".Parser");
	} catch (Exception e) {
	    ErrorHandler.reportError("Cannot set language", e);
	}
    }

    public GrammarOptions getOptions() {
	return fGrammarOptions;
    }
}
