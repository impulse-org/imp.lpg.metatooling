package org.jikespg.uide.wizards;

import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModel;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
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
public class NewGrammarWizardPage extends ExtensionPointWizardPage {
    private static final String UIDE= "org.eclipse.uide";

    private static final String JIKESPG= "org.jikespg.uide";

    private String fPackageName;

    private String fTemplateName;

    private String fTargetLanguage;

    private boolean fHasKeywords;

    private boolean fAutoGenerateASTs;

    public NewGrammarWizardPage(ExtensionPointWizard wizard) {
	super(wizard, RuntimePlugin.UIDE_RUNTIME, "parser");
	setTitle("LPG Grammar for New Language");
	setDescription("This wizard creates a new grammar file with *.g extension.");
	requires.add(UIDE);
	requires.add(JIKESPG);
	requires.add("org.eclipse.core.runtime");
	requires.add("org.eclipse.core.resources");
    }

    protected void createAdditionalControls(Composite parent) {
	createTemplateField(parent);
	createLanguageField(parent);
	createOptionsFields(parent);
    }

    private void createTemplateField(Composite parent) {
	Label label= new Label(parent, SWT.NULL);
	label.setText("Template:");
	label.setToolTipText("Select the parser/lexer template to use");
	label.setBackground(parent.getBackground());

	final int defaultTemplate= 3;  // UIDE
	Combo combo= new Combo(parent, SWT.READ_ONLY);

	combo.setFont(parent.getFont());
	combo.setItems(new String[] { "none", "simple", "backtracking", "UIDE" });
	combo.setLayoutData(new GridData(GridData.BEGINNING));
	combo.select(defaultTemplate);
	combo.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		fTemplateName= e.text;
	    }
	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	fTemplateName= combo.getItem(defaultTemplate);

	new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void createLanguageField(Composite parent) {
	Label label= new Label(parent, SWT.NULL);
	label.setText("Implementation:");
	label.setToolTipText("Select the implementation language for the parser/lexer");
	label.setBackground(parent.getBackground());

	final int defaultLang= 0;  // Java
	Combo combo= new Combo(parent, SWT.READ_ONLY);

	combo.setFont(parent.getFont());
	combo.setItems(new String[] { "java", "c++" });
	combo.setLayoutData(new GridData(GridData.BEGINNING));
	combo.select(defaultLang);
	combo.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		fTargetLanguage= e.text;
	    }
	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	fTargetLanguage= combo.getItem(defaultLang);
	combo.setEnabled(false);

	new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void createOptionsFields(Composite parent) {
	Label optionsLabel= new Label(parent, SWT.NULL);

	optionsLabel.setText("Options:");
	optionsLabel.setToolTipText("JikesPG Options");

	final Button cbKeywords= new Button(parent, SWT.CHECK);

	cbKeywords.setText("Language has keywords");
	cbKeywords.setToolTipText("Check this if your language has both keywords and identifiers.");
	cbKeywords.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		fHasKeywords= cbKeywords.getSelection();
	    }
	    public void widgetDefaultSelected(SelectionEvent e) { }
	});

	final Button cbAutoASTs= new Button(parent, SWT.CHECK);

	cbAutoASTs.setText("JikesPG auto-generated AST classes");
	cbAutoASTs.setToolTipText("Check this if you want JikesPG to generate a set of AST classes for your grammar's non-terminals.");
	cbAutoASTs.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		fAutoGenerateASTs= cbAutoASTs.getSelection();
	    }
	    public void widgetDefaultSelected(SelectionEvent e) { }
	});
    }

    public void createControl(Composite parent) {
	super.createControl(parent);
	setLanguage();
	try {
//	    getField("class").setEnabled(false);
	    projectText.addModifyListener(new ModifyListener() {
		public void modifyText(ModifyEvent e) {
		    setLanguage();
		}
	    });
	} catch (Exception e) {
	    ErrorHandler.reportError("Internal error, extension point schema may have changed", e);
	}
    }

    public String getLanguage() {
	try {
	    IPluginModel plugin= ExtensionPointEnabler.getPlugin(NewGrammarWizardPage.this);

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

    public String getPackage(String language) {
	StringBuffer buffer= new StringBuffer("org.");
	for(int n= 0; n < language.length(); n++) {
	    char c= Character.toLowerCase(language.charAt(n));
	    if (Character.isJavaIdentifierPart(c))
		buffer.append(c);
	}
	buffer.append(".parser");
	fPackageName= buffer.toString();
	return getPackage();
    }

    public String getPackage() {
	return fPackageName;
    }

    public void setLanguage() {
	try {
	    String language= getLanguage();
	    WizardPageField field= getField("language");
	    if (field.getText().length() == 0)
		field.setText(language);
	    getField("class").setText(getPackage(language) + ".Parser");
	} catch (Exception e) {
	    ErrorHandler.reportError("Cannot set language", e);
	}
    }

    public String getTemplateName() {
        return fTemplateName;
    }

    public boolean autoGenerateASTs() {
        return fAutoGenerateASTs;
    }

    public boolean hasKeywords() {
        return fHasKeywords;
    }
}