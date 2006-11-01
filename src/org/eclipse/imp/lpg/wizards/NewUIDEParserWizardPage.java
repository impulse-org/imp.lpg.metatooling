package org.jikespg.uide.wizards;

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
import org.jikespg.uide.JikesPGPlugin;

/**
 * The "New" wizard page allows setting the container for the new file as well as the
 * file name. The page will only accept file name without the extension OR with the
 * extension that matches the expected one (g).
 */
public class NewUIDEParserWizardPage extends ExtensionPointWizardPage {
    GrammarOptions fGrammarOptions= new GrammarOptions();

    public NewUIDEParserWizardPage(ExtensionPointWizard wizard) {
	super(wizard, RuntimePlugin.UIDE_RUNTIME, "parser");
	setTitle("JikesPG Grammar");
	setDescription("This wizard creates a new JikesPG grammar file with a '.g' extension.");
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
            String lang= getField("language").getText();
            // RMF 10/10/2006 - fOmitExtensionIDName is true except for builder
            // extensions, so there *is* no "id" field for the parser extension!
//          getField("id").setText(lang + ".safari.parser");
            getField("language").fText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    // RMF 10/10/2006 - fOmitExtensionIDName is true except for builder
                    // extensions, so there *is* no "id" field for the parser extension!
//                  setIDIfEmpty();
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

    protected void setLanguageIfEmpty() {
        try {
            String pluginLang= ExtensionPointEnabler.determineLanguage(getProject()); // if a languageDesc exists
            if (pluginLang.length() == 0)
                return;

            WizardPageField field= getField("language");

            if (field.getText().length() == 0)
                field.setText(pluginLang);
            // SMS 16 Oct 2006:  added pluginLang to default ParseController class name
            getField("class").setText(fGrammarOptions.getPackageForLanguage(upperCaseFirst(pluginLang)) + "." + pluginLang + "ParseController");
        } catch (Exception e) {
            ErrorHandler.reportError("Cannot set language", e);
        }
    }

    // RMF 10/10/2006 - There *is* no "id" field/attribute; that only exists for builder extensions that actually need one.
//    protected void setIDIfEmpty() {
//        if (true) return;
//	try {
//	    WizardPageField langField= getField("language");
//            String language= langField.getText();
//
//            if (language.length() == 0)
//                return;
//            String langID= lowerCaseFirst(language);
//	    getField("id").setText(langID + ".safari.parser");
//	} catch (Exception e) {
//	    ErrorHandler.reportError("Cannot set ID", e);
//	}
//    }

    protected void setClassIfEmpty() {
        try {
            WizardPageField langField= getField("language");
            String language= langField.getText();

            if (language.length() == 0)
                return;
            String langPkg= lowerCaseFirst(language);
            String langClass= upperCaseFirst(language);

            // TODO Unify the following code with similar stuff in NewUIDEParserWizard.generateCodeStubs().
            // SMS 16 Oct 2006:  added language to default ParseController class name
            getField("class").setText(fGrammarOptions.getPackageForLanguage(language) + "." + langClass + "ParseController");
        } catch (Exception e) {
            ErrorHandler.reportError("Cannot set class", e);
        }
    }

    public GrammarOptions getOptions() {
	return fGrammarOptions;
    }
}
