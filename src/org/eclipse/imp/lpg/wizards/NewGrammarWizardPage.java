package org.jikespg.uide.wizards;

import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModel;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.uide.core.ErrorHandler;
import org.eclipse.uide.wizards.ExtensionPointEnabler;
import org.eclipse.uide.wizards.ExtensionPointWizard;
import org.eclipse.uide.wizards.ExtensionPointWizardPage;

/**
 * The "New" wizard page allows setting the container for
 * the new file as well as the file name. The page
 * will only accept file name without the extension OR
 * with the extension that matches the expected one (g).
 */

public class NewGrammarWizardPage extends ExtensionPointWizardPage {
	
//	private Text packageText;
    private String packageName;
    private static final String UIDE = "org.eclipse.uide";
    private static final String JIKESPG = "org.jikespg.uide";
    
    public NewGrammarWizardPage(ExtensionPointWizard wizard) {
		super(wizard, "org.eclipse.uide", "parser");
        setTitle("LPG Grammar for New Language");
		setDescription("This wizard creates a new grammar file with *.g extension.");
        requires.add(UIDE);
        requires.add(JIKESPG);
        requires.add("org.eclipse.core.runtime");
        requires.add("org.eclipse.core.resources");
        	}
    
    public void createControl(Composite parent) {
        super.createControl(parent);
        setLanguage();
        try {
            getField("class").setEnabled(false);
            projectText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    setLanguage();
                }
            });
        }
        catch (Exception e) {
            ErrorHandler.reportError("Internal error, extension point schema may have changed", e);
        }
    }

    public String getLanguage() {
        try {
            IPluginModel plugin = ExtensionPointEnabler.getPlugin(NewGrammarWizardPage.this);
            if (plugin != null) {
                IPluginExtension[] extensions = plugin.getExtensions().getExtensions();
                for (int n = 0; n < extensions.length; n++) {
                    IPluginExtension extension = extensions[n];
                    IPluginObject[] children = extension.getChildren();
                    for (int k = 0; k < children.length; k++) {
                        IPluginObject object = children[k];
                        if (object.getName().equals("language")) {
                            return ((IPluginElement)object).getAttribute("language").getValue();
                        }
                    }
                    System.out.println("no language");
                }
            }
            else
                System.out.println("no plugin");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }
    
    public String getPackage(String language) {
        StringBuffer buffer = new StringBuffer("org.");
        for (int n=0; n<language.length(); n++) {
            char c = Character.toLowerCase(language.charAt(n));
            if (Character.isJavaIdentifierPart(c))
                buffer.append(c);
        }
        buffer.append(".parser");
        packageName = buffer.toString();
        return getPackage();
    }
    
    public String getPackage() {
        return packageName;
    }

    public void setLanguage() {
        try {
            String language = getLanguage();
            Field field = getField("language");
            if (field.getText().length() ==0)
                field.setText(language);
            getField("class").setText(getPackage(language)+".Parser");
        }
        catch (Exception e) {
            ErrorHandler.reportError("Cannot set language", e);
        }
    }
}