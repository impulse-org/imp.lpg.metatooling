package org.jikespg.uide.preferences;

	
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.uide.preferences.ISafariPreferencesService;
import org.eclipse.uide.preferences.SafariPreferencesTab;
import org.eclipse.uide.preferences.SafariPreferencesUtilities;
import org.eclipse.uide.preferences.fields.SafariBooleanFieldEditor;
import org.eclipse.uide.preferences.fields.SafariDirectoryListFieldEditor;
import org.eclipse.uide.preferences.fields.SafariFileFieldEditor;
import org.eclipse.uide.preferences.fields.SafariStringFieldEditor;
import org.jikespg.uide.builder.JikesPGBuilder;


public class ConfigurationPreferencesTab  extends SafariPreferencesTab {

	// Example:  declare preference fields
	SafariBooleanFieldEditor useDefaultExecField = null;
	SafariFileFieldEditor execField = null;
	SafariBooleanFieldEditor useDefaultGenIncludePathField = null;
	SafariDirectoryListFieldEditor includeDirectoriesField = null;
	SafariStringFieldEditor extensionsToProcessField = null;
	SafariStringFieldEditor extensionsToIncludeField = null;
	SafariBooleanFieldEditor emitDiagnosticMessagesField = null;
	SafariBooleanFieldEditor generateListingsField = null;
	
	private final SafariPreferencesUtilities prefUtils;
	private ISafariPreferencesService prefService;
	
	private PreferencePage prefPage = null;
	
	
	public ConfigurationPreferencesTab(ISafariPreferencesService prefService) {
		this.prefService = prefService;
		prefUtils = new SafariPreferencesUtilities(prefService);
	}
	
	
	public Composite createConfigurationPreferencesTab(PreferencePage page, final TabFolder tabFolder) {
		
		prefPage = page;

        final Composite composite= new Composite(tabFolder, SWT.NONE);
        composite.setFont(tabFolder.getFont());
        final GridData gd= new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.widthHint= 0;
        gd.heightHint= SWT.DEFAULT;
        gd.horizontalSpan= 1;
        composite.setLayoutData(gd);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		composite.setLayout(gl);
		
		final TabItem tab= new TabItem(tabFolder, SWT.NONE);
		tab.setText("Configuration");
		tab.setControl(composite);
		
		
		/*
		 * Add the elements relating to preferences fields and their associated "details" links.
		 * Note that for the "Configuration" preferences page the characteristics of the preferences
		 * (e.g., removable, or special or empty values) are set individually and there are no general
		 * restrictions on them.
		 */	
		
		/*
		 * FYI:  The parameters to makeNew*Field following the "composite" parameter"
		 *	boolean isEnabled,	
		 *	boolean hasSpecialValue, String specialValue,
		 *	boolean emptyValueAllowed, String emptyValue,
		 *	boolean isRemovable
		 */
		
		//SafariPreferencesUtilities.fillGridPlace(composite, 2);
		
		useDefaultExecField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_USE_DEFAULT_EXEC, "Use default generator executable?",
				composite, true, true, true, false, false, false, true);
		Link useDefaultExecDetails = prefUtils.createDetailsLink(composite, useDefaultExecField, useDefaultExecField.getChangeControl().getParent(), "Details ...");

		execField = prefUtils.makeNewFileField(
				prefPage, this,
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_JIKESPG_EXEC_PATH, "Generator executable",
				composite, false, false, true, JikesPGBuilder.getDefaultExecutablePath(), false, "", true);
		Link execFieldDetails = prefUtils.createDetailsLink(composite, execField, execField.getTextControl().getParent(), "Details ...");

		prefUtils.createToggleFieldListener(useDefaultExecField, execField, false);
		execField.setEnabled(!useDefaultExecField.getBooleanValue(), execField.getTextControl().getParent());
		execField.getTextControl().setEditable(!useDefaultExecField.getBooleanValue());

		SafariPreferencesUtilities.fillGridPlace(composite, 2);	
		
		useDefaultGenIncludePathField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, "Use default generator include path?",
				composite, true, true, true, false, false, false, true);
		Link useDefaultGenIncludePathFieldDetails = prefUtils.createDetailsLink(composite, useDefaultGenIncludePathField, useDefaultGenIncludePathField.getChangeControl().getParent(), "Details ...");
		
		includeDirectoriesField = prefUtils.makeNewDirectoryListField(
				prefPage, this,
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_JIKESPG_INCLUDE_DIRS, "Include directories:",
				composite, false, false, true, JikesPGBuilder.getDefaultExecutablePath(), false, "", true);
		Link includeDirectoriesFieldDetails = prefUtils.createDetailsLink(composite, includeDirectoriesField, includeDirectoriesField.getTextControl().getParent(), "Details ...");

		prefUtils.createToggleFieldListener(useDefaultGenIncludePathField, includeDirectoriesField, false);
		includeDirectoriesField.setEnabled(!useDefaultGenIncludePathField.getBooleanValue(), includeDirectoriesField.getTextControl().getParent());
		includeDirectoriesField.getTextControl().setEditable(!useDefaultGenIncludePathField.getBooleanValue());		
		
		SafariPreferencesUtilities.fillGridPlace(composite, 2);	
		
		extensionsToProcessField = prefUtils.makeNewStringField(
				prefPage, this,
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_EXTENSION_LIST, "File-name extensions to process:",
				composite, true, true, true, JikesPGBuilder.getDefaultExecutablePath(), false, "", true);
		Link extensionsToProcessFieldDetails = prefUtils.createDetailsLink(composite, extensionsToProcessField, extensionsToProcessField.getTextControl().getParent(), "Details ...");

		extensionsToIncludeField = prefUtils.makeNewStringField(
				prefPage, this,
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_NON_ROOT_EXTENSION_LIST, "File-name extensions for include files:",
				composite, true, true, true, "gi", false, "", true);
		Link extensionsToIncludeFieldDetails = prefUtils.createDetailsLink(composite, extensionsToIncludeField, extensionsToIncludeField.getTextControl().getParent(), "Details ...");

		emitDiagnosticMessagesField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_EMIT_MESSAGES, "Emit diagnostic messages during build?",
				composite, true, true, true, false, false, false, true);
		Link emitDiagnosticMessagesFieldDetails = prefUtils.createDetailsLink(composite, emitDiagnosticMessagesField, emitDiagnosticMessagesField.getChangeControl().getParent(), "Details ...");
		
		generateListingsField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.CONFIGURATION_LEVEL, PreferenceConstants.P_GEN_LISTINGS, "Generate listing files?",
				composite, true, true, true, false, false, false, true);
		Link generateListingsFieldDetails = prefUtils.createDetailsLink(composite, generateListingsField, generateListingsField.getChangeControl().getParent(), "Details ...");
		
			
		
		SafariPreferencesUtilities.fillGridPlace(composite, 2);
		
		// Put notes on bottom
		// (not sure whether WRAP is really helpful here)
		
		final Composite bottom = new Composite(composite, SWT.BOTTOM | SWT.WRAP);
        GridLayout layout = new GridLayout();
        bottom.setLayout(layout);
        bottom.setLayoutData(new GridData(SWT.BOTTOM));
        
        Label bar = new Label(bottom, SWT.WRAP);
        GridData data = new GridData();
        data.verticalAlignment = SWT.WRAP;
        bar.setLayoutData(data);
        bar.setText("Preferences shown with a white (or neutral) background are set on this level.\n\n" +
        			"Preferences shown with a colored background are inherited from a\nhigher level.");
        
		SafariPreferencesUtilities.fillGridPlace(bottom, 1);
		

        buttons = prefUtils.createDefaultAndApplyButtons(composite, this);
        Button defaultsButton = (Button) buttons[0];
        Button applyButton = (Button) buttons[1];
      
		
		return composite;
	}

	
	
	public void performApply() {
		// Example:  store each field
		useDefaultExecField.store();
		execField.store();
		useDefaultGenIncludePathField.store();
		includeDirectoriesField.store();
		extensionsToProcessField.store();
		extensionsToIncludeField.store();
		emitDiagnosticMessagesField.store();
		generateListingsField.store();
	}

	
	
	public void performDefaults() {
		// Clear all preferences at this level and reload them into the
		// preferences store through the initializer
		prefService.clearPreferencesAtLevel(ISafariPreferencesService.CONFIGURATION_LEVEL);

		// Example:  reload each preferences field
		useDefaultExecField.loadWithInheritance();
		execField.loadWithInheritance();
		useDefaultGenIncludePathField.loadWithInheritance();
		includeDirectoriesField.loadWithInheritance();
		extensionsToProcessField.loadWithInheritance();
		extensionsToIncludeField.loadWithInheritance();
		emitDiagnosticMessagesField.loadWithInheritance();
		generateListingsField.loadWithInheritance();
	}

	
	public boolean performOk() {
		// Example:  Store each field
		useDefaultExecField.store();
		execField.store();
		useDefaultGenIncludePathField.store();
		includeDirectoriesField.store();
		extensionsToProcessField.store();
		extensionsToIncludeField.store();
		emitDiagnosticMessagesField.store();
		generateListingsField.store();
		return true;
	}
	
}
