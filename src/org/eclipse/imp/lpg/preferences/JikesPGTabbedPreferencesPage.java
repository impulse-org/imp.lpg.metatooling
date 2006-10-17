package org.jikespg.uide.preferences;

//import jsdiv.JsdivPlugin;
//import jsdiv.preferences.SWTUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.uide.preferences.ISafariPreferencesService;
import org.eclipse.uide.preferences.SafariPreferencesUtilities;
import org.jikespg.uide.JikesPGPlugin;	//$ plugin name (based on language name?)

/**
 * CVS Preference Page
 * 
 * Allows the configuration of CVS specific options.
 * The currently supported options are:
 *  - Allow loading of CVS administration directory (CVSROOT)
 * 
 * There are currently a couple of deficiencies:
 *  1. The Repository view is not refreshed when the show CVSROOT option is changed
 *  2. There is no help associated with the page
 */
public class JikesPGTabbedPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {
	

	//ApplicablePreferencesTab applicableTab = null;
	ProjectPreferencesTab projectTab = null;
	InstancePreferencesTab instanceTab = null;
	ConfigurationPreferencesTab configurationTab = null;
	DefaultPreferencesTab defaultTab = null;

	//Composite applicableComposite = null;
	Composite projectComposite = null;
	Composite instanceComposite = null;
	Composite configurationComposite = null;
	Composite defaultComposite = null;
													//$ plugin name (based on language name?)
	private ISafariPreferencesService prefService = JikesPGPlugin.getPreferencesService();
	private SafariPreferencesUtilities prefUtils = new SafariPreferencesUtilities(prefService);
	

	
	
	public JikesPGTabbedPreferencesPage() {
		this.noDefaultAndApplyButton();
	}

	protected Control createContents(Composite parent) {
		
		// create a tab folder for the page
		final TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
        final GridData gd= new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.widthHint= 0;
        gd.heightHint= SWT.DEFAULT;
        gd.horizontalSpan= 1;
        tabFolder.setLayoutData(gd);

//		applicableTab = new ApplicablePreferencesTab(prefService);
//		applicableComposite = applicableTab.createApplicablePreferencesTab(tabFolder);
		projectTab = new ProjectPreferencesTab(prefService);
		projectComposite = projectTab.createProjectPreferencesTab(this, tabFolder);
		instanceTab = new InstancePreferencesTab(prefService);
		instanceComposite = instanceTab.createInstancePreferencesTab(this, 	tabFolder);
		configurationTab = new ConfigurationPreferencesTab(prefService);
		configurationComposite = configurationTab.createConfigurationPreferencesTab(this, tabFolder);
		defaultTab = new DefaultPreferencesTab(prefService);
		defaultComposite = defaultTab.createDefaultPreferencesTab(this, tabFolder);

		Dialog.applyDialogFont(parent);

		return tabFolder;
	}

	
	public void performApply()
	{
		defaultTab.performApply();
		configurationTab.performApply();
		instanceTab.performApply();
		projectTab.performApply();
	}
	
    public boolean performCancel() {
    	projectTab.performCancel();
        return super.performCancel();		// just returns true
    }

	public void performDefaults()
	{
		//if (applicableComposite.isVisible()) return;
		if (projectComposite.isVisible()) projectTab.performDefaults();
		else if (instanceComposite.isVisible()) instanceTab.performDefaults();
		else if (configurationComposite.isVisible()) configurationTab.performDefaults();
		else if (defaultComposite.isVisible()) defaultTab.performDefaults();
	}

	
	// Save the prevailing preferences
	// (called from other controls, e.g., when varoius buttons are pushed)
	public boolean performOk()
	{
		defaultTab.performOk();
		configurationTab.performOk();
		instanceTab.performOk();
		projectTab.performOk();

		return true;
	}
	

		
	public void init(IWorkbench workbench) {
	}




}
