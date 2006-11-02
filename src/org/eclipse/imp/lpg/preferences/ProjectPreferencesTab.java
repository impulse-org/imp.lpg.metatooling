package org.jikespg.uide.preferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.preferences.ProjectSelectionDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.uide.preferences.ISafariPreferencesService;
import org.eclipse.uide.preferences.SafariPreferencesService;
import org.eclipse.uide.preferences.SafariPreferencesTab;
import org.eclipse.uide.preferences.SafariPreferencesUtilities;
import org.eclipse.uide.preferences.fields.SafariBooleanFieldEditor;
import org.eclipse.uide.preferences.fields.SafariDirectoryListFieldEditor;
import org.eclipse.uide.preferences.fields.SafariFileFieldEditor;
import org.eclipse.uide.preferences.fields.SafariStringFieldEditor;
import org.jikespg.uide.builder.JikesPGBuilder;
import org.osgi.service.prefs.Preferences;


public class ProjectPreferencesTab extends SafariPreferencesTab {
	// Example:  declare preference fields
	SafariBooleanFieldEditor useDefaultExecField = null;
	SafariFileFieldEditor execField = null;
	SafariBooleanFieldEditor useDefaultGenIncludePathField = null;
	SafariDirectoryListFieldEditor includeDirectoriesField = null;
	SafariStringFieldEditor extensionsToProcessField = null;
	SafariStringFieldEditor extensionsToIncludeField = null;
	SafariBooleanFieldEditor emitDiagnosticMessagesField = null;
	SafariBooleanFieldEditor generateListingsField = null;
	
	StringFieldEditor selectedProjectName = null;
	List detailsLinks = new ArrayList();
	
	PreferencePage prefPage = null;

	IJavaProject javaProject = null;

	
	private final SafariPreferencesUtilities prefUtils;
	private ISafariPreferencesService prefService;
	
	
	public ProjectPreferencesTab(ISafariPreferencesService prefService) {
		this.prefService = prefService;
		prefUtils = new SafariPreferencesUtilities(prefService);
	}

	public Composite createProjectPreferencesTab(PreferencePage page, final TabFolder tabFolder) {
		
		prefPage = page;


		/*
		 * Prepare the body of the tab
		 */
	
		GridLayout layout = null;
		
		final Composite composite= new Composite(tabFolder, SWT.NONE);
        composite.setFont(tabFolder.getFont());
        final GridData gd= new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.widthHint= 0;
        gd.heightHint= SWT.DEFAULT;
        gd.horizontalSpan= 1;
        composite.setLayoutData(gd);
		
		layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		// The "tab" on the tab folder
		final TabItem tab= new TabItem(tabFolder, SWT.NONE);
		tab.setText("Project");
		tab.setControl(composite);	
		
		
		
		/*
		 * Add the elements relating to preferences fields and their associated "details" links.
		 * Note:  we save the details links so that we can enable and disable them later according to
		 * whether a project is selected.  Details links are not saved on the other tabs.
		 */	
		
		/*
		 * FYI:  The parameters to makeNewStringField following the "composite" parameter
		 *	boolean isEnabled, isEditable
		 *	boolean hasSpecialValue, String specialValue,
		 *	boolean emptyValueAllowed, String emptyValue,
		 *	boolean isRemovable
		 */

		
		useDefaultExecField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_USE_DEFAULT_EXEC, "Use default generator executable?",
				composite, false, true, true, false, false, false, true);
		Link useDefaultExecFieldDetails = prefUtils.createDetailsLink(composite, useDefaultExecField, useDefaultExecField.getChangeControl().getParent(), "Details ...");
		detailsLinks.add(useDefaultExecFieldDetails);
	
		execField = prefUtils.makeNewFileField(
				prefPage, this,
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_JIKESPG_EXEC_PATH, "Generator executable",
				composite, false, false, true, JikesPGBuilder.getDefaultExecutablePath(), false, "", true);
		Link execFieldDetails = prefUtils.createDetailsLink(composite, execField, execField.getTextControl().getParent(), "Details ...");
		detailsLinks.add(execFieldDetails);

		prefUtils.createToggleFieldListener(useDefaultExecField, execField, false);
		execField.setEnabled(prefService.getProject() != null && !useDefaultExecField.getBooleanValue(), execField.getTextControl().getParent());
		execField.getTextControl().setEditable(prefService.getProject() != null && !useDefaultExecField.getBooleanValue());
		
		SafariPreferencesUtilities.fillGridPlace(composite, 2);	
		
		
		useDefaultGenIncludePathField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, "Use default generator include path?",
				composite, false, true, true, false, false, false, true);
		Link useDefaultGenIncludePathFieldDetails = prefUtils.createDetailsLink(composite, useDefaultGenIncludePathField, useDefaultGenIncludePathField.getChangeControl().getParent(), "Details ...");
		detailsLinks.add(useDefaultGenIncludePathFieldDetails);
	
		includeDirectoriesField = prefUtils.makeNewDirectoryListField(
				prefPage, this,
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_JIKESPG_INCLUDE_DIRS, "Include directories:",
				composite, false, false, true, JikesPGBuilder.getDefaultExecutablePath(), false, "", true);
		Link includeDirectoriesFieldDetails = prefUtils.createDetailsLink(composite, includeDirectoriesField, includeDirectoriesField.getTextControl().getParent(), "Details ...");
		detailsLinks.add(includeDirectoriesFieldDetails);

		prefUtils.createToggleFieldListener(useDefaultGenIncludePathField, includeDirectoriesField, false);
		includeDirectoriesField.setEnabled(prefService.getProject() != null && !useDefaultGenIncludePathField.getBooleanValue(), includeDirectoriesField.getTextControl().getParent());
		includeDirectoriesField.getTextControl().setEditable(prefService.getProject() != null && !useDefaultGenIncludePathField.getBooleanValue());		
		
		SafariPreferencesUtilities.fillGridPlace(composite, 2);	

		
		extensionsToProcessField = prefUtils.makeNewStringField(
				prefPage, this,
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_EXTENSION_LIST, "File-name extensions to process:",
				composite, false, false, true, JikesPGBuilder.getDefaultExecutablePath(), false, "", true);
		Link extensionsToProcessFieldDetails = prefUtils.createDetailsLink(composite, extensionsToProcessField, extensionsToProcessField.getTextControl().getParent(), "Details ...");
		detailsLinks.add(extensionsToProcessFieldDetails);
		
		extensionsToIncludeField = prefUtils.makeNewStringField(
				prefPage, this,
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_NON_ROOT_EXTENSION_LIST, "File-name extensions for include files:",
				composite, false, false, true, "gi", false, "", true);
		Link extensionsToIncludeFieldDetails = prefUtils.createDetailsLink(composite, extensionsToIncludeField, extensionsToIncludeField.getTextControl().getParent(), "Details ...");
		detailsLinks.add(extensionsToIncludeFieldDetails);
		
		emitDiagnosticMessagesField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_EMIT_MESSAGES, "Emit diagnostic messages during build?",
				composite, false, false, true, false, false, false, true);
		Link emitDiagnosticMessagesFieldDetails = prefUtils.createDetailsLink(composite, emitDiagnosticMessagesField, emitDiagnosticMessagesField.getChangeControl().getParent(), "Details ...");
		detailsLinks.add(emitDiagnosticMessagesFieldDetails);
		
		generateListingsField = prefUtils.makeNewBooleanField(
				prefService, ISafariPreferencesService.PROJECT_LEVEL, PreferenceConstants.P_GEN_LISTINGS, "Generate listing files?",
				composite, false, false, true, false, false, false, true);
		Link generateListingsFieldDetails = prefUtils.createDetailsLink(composite, generateListingsField, generateListingsField.getChangeControl().getParent(), "Details ...");
		detailsLinks.add(generateListingsFieldDetails);

		
		SafariPreferencesUtilities.fillGridPlace(composite, 2);
		
		
		// The makeNew***Field methods should check for the special case
		// represented by this tab (project level with no project selected)
		// and disable the new fields accordingly
		
		// But we still need to disable the details links since no
		// project is selected
		for (int i = 0; i < detailsLinks.size(); i++) {
			((Link)detailsLinks.get(i)).setEnabled(false);
		}	
		
		SafariPreferencesUtilities.fillGridPlace(composite, 2);

		/*
		 * Put in the elements related to selecting a project
		 */
		
		// To hold the text selection (label + field) and button
		Group groupHolder = new Group(composite, SWT.SHADOW_ETCHED_IN);
		groupHolder.setText("Project selection");
		groupHolder.setLayout(new GridLayout(2, false));
		groupHolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		
		// To hold the text selection label + field
		Composite projectFieldHolder = new Composite(groupHolder, SWT.EMBEDDED);
		//layout = new GridLayout();
		//projectFieldHolder.setLayout(layout);
		projectFieldHolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		
		selectedProjectName = 
			new StringFieldEditor("SelectedProjectName", "Selected project:  ", projectFieldHolder);
		selectedProjectName.setStringValue("none selected");
		// Clear these here in case there are any saved from a previous interaction with the page
		// (assuming that we should start each  new page with no project selected)
		prefService.clearPreferencesAtLevel(ISafariPreferencesService.PROJECT_LEVEL);
		// Set the project name field to be non-editable
		selectedProjectName.getTextControl(projectFieldHolder).setEditable(false);
		// Set the attribute fields to be non-editable, since without a project selected
		// it makes no sense for them to be able to take values

		createSelectProjectButton(groupHolder, composite, "Select Project");
		addProjectSelectionListener(projectFieldHolder);
				
		SafariPreferencesUtilities.fillGridPlace(composite, 3);
		
		/*
		 * Put explanatory notes toward the bottom
		 * (not sure whether WRAP is helpful here; can manually
		 * wrap text in labels with '\n')
		 */
		
		final Composite bottom = new Composite(composite, SWT.BOTTOM | SWT.WRAP);
        layout = new GridLayout();
        bottom.setLayout(layout);
        bottom.setLayoutData(new GridData(SWT.BOTTOM));
        
        Label bar = new Label(bottom, SWT.WRAP);
        GridData data = new GridData();
        data.verticalAlignment = SWT.WRAP;
        bar.setLayoutData(data);
        bar.setText("Preferences are shown here only when a project is selected.\n\n" +
        			"Preferences shown with a white background are set on this level.\n\n" +
        			"Preferences shown with a colored background are inherited from a\nhigher level.");
        
		SafariPreferencesUtilities.fillGridPlace(bottom, 1);
	  
		 
        buttons = prefUtils.createDefaultAndApplyButtons(composite, this);
        // SMS 31 Oct 2006
        //Button defaultsButton = (Button) buttons[0];
        //Button applyButton = (Button) buttons[1];
		
		return composite;
	}

	
	private void addProjectSelectionListener(Composite composite)
	{
		prefService.addProjectSelectionListener(new SafariProjectSelectionListener(composite));
	}
	


	private class SafariProjectSelectionListener implements SafariPreferencesService.IProjectSelectionListener
	{
		Composite composite = null;
		boolean haveCurrentListeners = false;
		IEclipsePreferences.IPreferenceChangeListener currentListener = null;
		
		
		 // @param	composite is the project field holder, but it isn't used here

		SafariProjectSelectionListener(Composite composite) {
			this.composite = composite;
		}
			
		/**
		 * Notification that a project was selected for inclusion in the preferences hierarchy.
		 * The given event must not be <code>null</code>.
		 * 
		 * @param event an event specifying the details about the new node
		 * @see IEclipsePreferences.NodeChangeEvent
		 * @see IEclipsePreferences#addNodeChangeListener(IEclipsePreferences.INodeChangeListener)
		 * @see IEclipsePreferences#removeNodeChangeListener(IEclipsePreferences.INodeChangeListener)
		 */
		public void selection(ISafariPreferencesService.ProjectSelectionEvent event) {
//			System.err.println("SafariProjectSelectionListener.selection():  composite = " + composite.toString());

			Preferences oldeNode = event.getPrevious();
			Preferences newNode = event.getNew();
			
			if (oldeNode == null && newNode == null) {
				// This is what happens for some reason when you clear the project selection,
				// but there shouldn't be anything to do (I don't think), because newNode == null
				// implies that the preferences should be cleared, disabled, etc., and oldeNode
				// ==  null implies that they should already be cleared, disabled, etc.
				// So, it should be okay to return from here ...
				return;
			}
			//System.err.println("SafariProjectSelectionListener.selection:  old node = " + (oldeNode == null ? "null" : "not null"));
			//System.err.println("SafariProjectSelectionListener.selection:  new node = " + (newNode == null ? "null" : "not null"));
			
			
			// If oldeNode is not null, we want to remove any preference-change listeners from it
			if (oldeNode != null && oldeNode instanceof IEclipsePreferences && haveCurrentListeners) {
				removeProjectPreferenceChangeListeners();
				haveCurrentListeners = false;
			} else {	
				//System.out.println("JsdivProjectPreferencesPage.SafariProjectSelectionListener.selection():  " +
				//	"\n\tnode is null, not of IEclipsePreferences type, or currentListener is null; not possible to add preference-change listener");
			}

			
			// Declare these for various manipulations of fields and controls to follow
			Composite useDefaultExecFieldHolder	= null;
			Composite execFieldHolder = null;
			Composite useDefaultGenIncludePathFieldHolder = null;
			Composite includeDirectoriesFieldHolder = null;
			Composite extensionsToProcessFieldHolder = null;
			Composite extensionsToIncludeFieldHolder = null;
			Composite emitDiagnosticMessagesFieldHolder = null;
			Composite generateListingsFieldHolder = null;

			
			// If we have a new project preferences node, then do various things
			// to set up the project's preferences
			if (newNode != null && newNode instanceof IEclipsePreferences) {
				// Set project name on tab
				selectedProjectName.setStringValue(newNode.name());
				
				// If the containing composite is not disposed, then set the fields'
				// values and make them enabled and editable (as appropriate to the
				// type of field)

				// Not entirely sure why the composite could or should be disposed if we're here,
				// but it happens sometimes when selecting a project for the second time, i.e.,
				// after having selected a project once, clicked "OK", and then brought up the
				// dialog and selected a project again.  PERHAPS there is a race condition, such
				// that sometimes the project-selection dialog is still overlaying the preferences
				// tab at the time that the listeners try to update the tab.  If the project-selection
				// dialog were still up then the preferences tab would be disposed.
				
				if (!composite.isDisposed()) {
					
					// Note:  Where there are toggles between fields, it is a good idea to set the
					// properties of the dependent field here according to the values they should have
					// based on the independent field.  There should be listeners to take care of 
					// that sort of adjustment once the tab is established, but when properties are
					// first initialized here, the properties may not always be set correctly through
					// the toggle.  I'm not entirely sure why that happens, except that there may be
					// a race condition between the setting of the dependent values by the listener
					// and the setting of those values here.  If the values are set by the listener
					// first (which might be surprising, but may be possible) then they will be
					// overwritten by values set here--so the values set here should be consistent
					// with what the listener would set.
					
					useDefaultExecFieldHolder = useDefaultExecField.getChangeControl().getParent();
					prefUtils.setField(useDefaultExecField, useDefaultExecFieldHolder);
					useDefaultExecField.getChangeControl(useDefaultExecFieldHolder).setEnabled(true);
					
					execFieldHolder = execField.getTextControl().getParent();
					prefUtils.setField(execField, execFieldHolder);
					//execField.getTextControl(execFieldHolder).setEnabled(!useDefaultExecField.getBooleanValue());
					execField.setEnabled(!useDefaultExecField.getBooleanValue(), execFieldHolder);
					execField.getTextControl(execFieldHolder).setEditable(!useDefaultExecField.getBooleanValue());
		
					useDefaultGenIncludePathFieldHolder = useDefaultGenIncludePathField.getChangeControl().getParent();
					prefUtils.setField(useDefaultGenIncludePathField, useDefaultGenIncludePathFieldHolder);
					useDefaultGenIncludePathField.getChangeControl(useDefaultGenIncludePathFieldHolder).setEnabled(true);
					
					includeDirectoriesFieldHolder = includeDirectoriesField.getTextControl().getParent();
					prefUtils.setField(includeDirectoriesField, includeDirectoriesFieldHolder);
					includeDirectoriesField.setEnabled(!useDefaultGenIncludePathField.getBooleanValue(), includeDirectoriesFieldHolder);
					includeDirectoriesField.getTextControl(includeDirectoriesFieldHolder).setEditable(!useDefaultGenIncludePathField.getBooleanValue());

					extensionsToProcessFieldHolder = extensionsToProcessField.getTextControl().getParent();
					prefUtils.setField(extensionsToProcessField, extensionsToProcessFieldHolder);
					extensionsToProcessField.getTextControl(extensionsToProcessFieldHolder).setEditable(true);
					extensionsToProcessField.getTextControl(extensionsToProcessFieldHolder).setEnabled(true);
					
					extensionsToIncludeFieldHolder = extensionsToIncludeField.getTextControl().getParent();
					prefUtils.setField(extensionsToIncludeField, extensionsToIncludeFieldHolder);
					extensionsToIncludeField.getTextControl(extensionsToIncludeFieldHolder).setEditable(true);
					extensionsToIncludeField.getTextControl(extensionsToIncludeFieldHolder).setEnabled(true);
					
					emitDiagnosticMessagesFieldHolder = emitDiagnosticMessagesField.getChangeControl().getParent();
					prefUtils.setField(emitDiagnosticMessagesField, emitDiagnosticMessagesFieldHolder);
					emitDiagnosticMessagesField.getChangeControl(emitDiagnosticMessagesFieldHolder).setEnabled(true);
					
					generateListingsFieldHolder = generateListingsField.getChangeControl().getParent();
					prefUtils.setField(generateListingsField, generateListingsFieldHolder);
					generateListingsField.getChangeControl(generateListingsFieldHolder).setEnabled(true);
				}
				

				// Add property change listeners
				// Example
				if (useDefaultExecFieldHolder != null) addProjectPreferenceChangeListeners(
						useDefaultExecField, PreferenceConstants.P_USE_DEFAULT_EXEC, useDefaultExecFieldHolder);
				if (execFieldHolder != null) addProjectPreferenceChangeListeners(execField, PreferenceConstants.P_JIKESPG_EXEC_PATH, execFieldHolder);
				if (useDefaultGenIncludePathFieldHolder != null) addProjectPreferenceChangeListeners(
						useDefaultGenIncludePathField, PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, useDefaultGenIncludePathFieldHolder);
				if (includeDirectoriesFieldHolder != null) addProjectPreferenceChangeListeners(
						includeDirectoriesField, PreferenceConstants.P_JIKESPG_INCLUDE_DIRS	, includeDirectoriesFieldHolder);
				if (extensionsToProcessFieldHolder != null) addProjectPreferenceChangeListeners(
						extensionsToProcessField, PreferenceConstants.P_EXTENSION_LIST, extensionsToProcessFieldHolder);
				if (extensionsToIncludeFieldHolder != null) addProjectPreferenceChangeListeners(
						extensionsToIncludeField, PreferenceConstants.P_NON_ROOT_EXTENSION_LIST, extensionsToIncludeFieldHolder);
				if (emitDiagnosticMessagesFieldHolder != null) addProjectPreferenceChangeListeners(
						emitDiagnosticMessagesField, PreferenceConstants.P_EMIT_MESSAGES, emitDiagnosticMessagesFieldHolder);
				if (generateListingsFieldHolder != null) addProjectPreferenceChangeListeners(
						generateListingsField, PreferenceConstants.P_GEN_LISTINGS, generateListingsFieldHolder);

				haveCurrentListeners = true;
			}
			
			// Or if we don't have a new project preferences node ...
			if (newNode == null || !(newNode instanceof IEclipsePreferences)) {
				// May happen when the preferences page is first brought up, or
				// if we allow the project to be deselected
				
				// Unset project name in the tab
				selectedProjectName.setStringValue("none selected");
				
				// Clear the preferences from the store
				prefService.clearPreferencesAtLevel(ISafariPreferencesService.PROJECT_LEVEL);
				
				// Disable fields and make them non-editable
				// Example:
				if (!composite.isDisposed()) {

						useDefaultExecField.getChangeControl(useDefaultExecFieldHolder).setEnabled(false);

						execField.getTextControl(execFieldHolder).setEnabled(false);
						execField.getTextControl(execFieldHolder).setEditable(false);

						useDefaultGenIncludePathField.getChangeControl(useDefaultGenIncludePathFieldHolder).setEnabled(false);

						includeDirectoriesField.getTextControl(includeDirectoriesFieldHolder).setEnabled(false);
						includeDirectoriesField.getTextControl(includeDirectoriesFieldHolder).setEditable(false);

						extensionsToProcessField.getTextControl(extensionsToProcessFieldHolder).setEditable(false);
						extensionsToProcessField.getTextControl(extensionsToProcessFieldHolder).setEnabled(false);

						extensionsToIncludeField.getTextControl(extensionsToIncludeFieldHolder).setEditable(false);
						extensionsToIncludeField.getTextControl(extensionsToIncludeFieldHolder).setEnabled(false);

						emitDiagnosticMessagesField.getChangeControl(emitDiagnosticMessagesFieldHolder).setEnabled(false);
						
						generateListingsField.getChangeControl(generateListingsFieldHolder).setEnabled(false);
				}
				
				// Remove listeners
				removeProjectPreferenceChangeListeners();
				haveCurrentListeners = false;
			}
		};
	}
	

	protected List  currentListeners = new ArrayList();
	protected List 	currentListenerNodes = new ArrayList();
	
	
	private void addProjectPreferenceChangeListeners(SafariStringFieldEditor field, String key, Composite composite)
	{
		IEclipsePreferences[] nodes = prefService.getNodesForLevels();
		for (int i = ISafariPreferencesService.PROJECT_INDEX; i < nodes.length; i++) {
			if (nodes[i] != null) {
				// SMS 31 Oct 2006
				//SafariProjectPreferenceChangeListener listener = new SafariProjectPreferenceChangeListener(field, key, composite);
				SafariPreferencesUtilities.SafariStringPreferenceChangeListener listener = 
					prefUtils.new SafariStringPreferenceChangeListener(field, key, composite);
				nodes[i].addPreferenceChangeListener(listener);
				currentListeners.add(listener);
				currentListenerNodes.add(nodes[i]);
			} else {
				//System.err.println("JsdivProjectPreferencesPage.addPropetyChangeListeners(..):  no listener added at level = " + i + "; node at that level is null");
			}
		}	
	}
	
	
	private void addProjectPreferenceChangeListeners(SafariBooleanFieldEditor field, String key, Composite composite)
	{
		IEclipsePreferences[] nodes = prefService.getNodesForLevels();
		for (int i = ISafariPreferencesService.PROJECT_INDEX; i < nodes.length; i++) {
			if (nodes[i] != null) {
				SafariPreferencesUtilities.SafariBooleanPreferenceChangeListener listener = 
					prefUtils.new SafariBooleanPreferenceChangeListener(field, key, composite);
				nodes[i].addPreferenceChangeListener(listener);
				currentListeners.add(listener);
				currentListenerNodes.add(nodes[i]);
			} else {
				//System.err.println("JsdivProjectPreferencesPage.addPropetyChangeListeners(..):  no listener added at level = " + i + "; node at that level is null");
			}
		}	
	}

	private void removeProjectPreferenceChangeListeners()
	{
		// Remove all listeners from their respective nodes
		for (int i = 0; i < currentListeners.size(); i++) {
			((IEclipsePreferences) currentListenerNodes.get(i)).removePreferenceChangeListener(
					((IEclipsePreferences.IPreferenceChangeListener)currentListeners.get(i)));
		}
		// Clear the lists
		currentListeners = new ArrayList();
		currentListenerNodes = new ArrayList();
	}
	
/*	
	private class SafariProjectPreferenceChangeListener implements IEclipsePreferences.IPreferenceChangeListener
	{
		Composite composite = null;
		String key = null;
		SafariStringFieldEditor field = null;
		
		SafariProjectPreferenceChangeListener(SafariStringFieldEditor field, String key, Composite composite) {
			this.field = field;
			this.key = key;	
			this.composite = composite;
		}
		
		public void preferenceChange(PreferenceChangeEvent event)
		{
			if (event.getNewValue() == null && event.getOldValue() == null) return;
			if (event.getNewValue() != null && event.getNewValue().equals(event.getOldValue())) return;	
			
			String eventKey = event.getKey();
			if (!composite.isDisposed()) {
				if (eventKey.equals(key)) {
					//System.out.println("SafariPreferenceChangeListener (Project prefs):  Got event for key = " + eventKey);
					prefUtils.setField(field, composite);
					// Why set editable to false here????
					//field.getTextControl(composite).setEditable(false);
				}
			}
		}
	}
*/
	
	
	/**
	 * 
	 * @param	composite	the wiget that holds the button
	 * @param	fieldParent	the wiget that holds the field that will be
	 * 						set when the button is pressed
	 * 						(needed for posting a listener)
	 * @param	text		text that appears in the link
	 */
	private Button createSelectProjectButton(Composite composite, final Composite fieldParent, String text)
	{
		final Button button = new Button(composite, SWT.NONE);
		button.setText(text);
		
		final class CompositeLinkSelectionListener implements SelectionListener {
			ProjectSelectionButtonResponder responder = null;
			// param was Composite parent
			CompositeLinkSelectionListener(ProjectSelectionButtonResponder responder) {
				this.responder = responder;
			}
			
			public void widgetSelected(SelectionEvent e) {
				//doMakeProjectSelectionLinkActivated((Link) e.widget, fieldParent);
				responder.doProjectSelectionActivated((Button) e.widget, fieldParent);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				//doMakeProjectSelectionLinkActivated((Link) e.widget, fieldParent);
				responder.doProjectSelectionActivated((Button) e.widget, fieldParent);
			}
		}

		CompositeLinkSelectionListener linkSelectionListener =
			new CompositeLinkSelectionListener(new ProjectSelectionButtonResponder());
		
		button.addSelectionListener(linkSelectionListener);
		return button;
	}

	
	
	private class ProjectSelectionButtonResponder {
	
		public void doProjectSelectionActivated(Button button, Composite composite)
		{
			HashSet projectsWithSpecifics = new HashSet();
			try {
				IJavaProject[] projects = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
				for (int i= 0; i < projects.length; i++) {
					IJavaProject curr = projects[i];
					//if (hasProjectSpecificOptions(curr.getProject())) {
					//	projectsWithSpecifics.add(curr);
					//}
				}
			} catch (JavaModelException e) {
				System.err.println("ProjectPreferencesTab:  JavaModelException obtaining Java projects");
			}
			
			ProjectSelectionDialog dialog = new ProjectSelectionDialog(button.getShell(), projectsWithSpecifics);
			if (dialog.open() == Window.OK) {
				javaProject = (IJavaProject) dialog.getFirstResult();
			}
	
			if (javaProject != null) {
				IProject project = javaProject.getProject();
				if (project.exists())
					prefService.setProject(project);
			}
			
			// Don't want to set these fields unless there is a project from
			// which they can legitimately take values.  Recall that the fields
			// associated with the project level might be filled in by inheritance
			// even when there is no project.  Still, that should only be done
			// when there is a project assigned
			
			// Shouldn't need to set the fields here because they should be set
			// by a listener when the project is set in the preferences service
/*			
			if (prefService.getProject() != null) {
				prefUtils.setField(useDefaultExecField, useDefaultExecField.getChangeControl().getParent());
				prefUtils.setField(execField, execField.getTextControl().getParent());
				execField.setEnabled(!useDefaultExecField.getBooleanValue(), execField.getTextControl().getParent());
				
				prefUtils.setField(useDefaultGenIncludePathField, useDefaultGenIncludePathField.getChangeControl().getParent());
				prefUtils.setField(includeDirectoriesField, includeDirectoriesField.getTextControl().getParent());
				includeDirectoriesField.setEnabled(!useDefaultGenIncludePathField.getBooleanValue(), includeDirectoriesField.getTextControl().getParent());
				
				prefUtils.setField(extensionsToProcessField, extensionsToProcessField.getTextControl().getParent());
				prefUtils.setField(extensionsToIncludeField, extensionsToIncludeField.getTextControl().getParent());
				
				prefUtils.setField(emitDiagnosticMessagesField, emitDiagnosticMessagesField.getChangeControl().getParent());
				prefUtils.setField(generateListingsField, generateListingsField.getChangeControl().getParent());
			}
*/			
			// And enable the details links since now a project is selected
			for (int i = 0; i < detailsLinks.size(); i++) {
				((Link)detailsLinks.get(i)).setEnabled(true);
			}
			
			//return javaProject.getElementName();
		}	
	}
	
	/**
	 * 
	 * @param	composite	the wiget that holds the link
	 * @param	fieldParent	the wiget that holds the fields that will be
	 * 						set when the link is selected
	 * @param	text		Text that appears in the link
	 */
/*	
	private Link createProjectSelectionLink(Composite composite, final Composite fieldParent, String text) {
		//compositeForLink = composite;	
		Link link= new Link(composite, SWT.NONE);
		link.setFont(composite.getFont());
		link.setText("<A>" + text + "</A>");  //$NON-NLS-1$//$NON-NLS-2$
		
		final class CompositeLinkSelectionListener implements SelectionListener {
//			Composite composite = null;
			CompositeLinkSelectionListener(Composite composite) {
//				this.composite = composite;
			}
			public void widgetSelected(SelectionEvent e) {
				doClearProjectSelectionLinkActivated((Link) e.widget, fieldParent);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				doClearProjectSelectionLinkActivated((Link) e.widget, fieldParent);
			}
		}
		CompositeLinkSelectionListener linkSelectionListener = new CompositeLinkSelectionListener(composite);
		
		link.addSelectionListener(linkSelectionListener);
		
		return link;
	}
*/
/*	
	final void doClearProjectSelectionLinkActivated(Link link, Composite composite)
	{
		prefService.setProject(null);
		// Recall:  setting the project to null clears the project-preferences scope
		// in the Safari preferences service (so there's no need to do that explicitly
		// here

		// Disable the details links since no project is selected now	
		for (int i = 0; i < detailsLinks.size(); i++) {
			((Link)detailsLinks.get(i)).setEnabled(false);
		}
	}	
*/	

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


	public boolean performCancel() {
		// Nullify the project in any case
		prefService.setProject(null);
		return true;
	}

	
	
	public void performDefaults() {
		if (prefService.getProject() == null) {
			// If no project set then there's no preferences
			// file from which to load anything	
			return;
		}
		prefService.clearPreferencesAtLevel(ISafariPreferencesService.PROJECT_LEVEL);
		
		// Example:  reload all fields for a valid project
		useDefaultExecField.loadWithInheritance();
		execField.loadWithInheritance();
		useDefaultGenIncludePathField.loadWithInheritance();
		includeDirectoriesField.loadWithInheritance();
		extensionsToProcessField.loadWithInheritance();
		extensionsToIncludeField.loadWithInheritance();
		emitDiagnosticMessagesField.loadWithInheritance();
		generateListingsField.loadWithInheritance();
	}	
	
	
	protected boolean performOk() {
		if (prefService.getProject() == null) {
			// Clear preferences because we're closing up dialog;
			// note that a project preferences node will exist, if only
			// in a leftover state, even when no project is selected
			prefService.clearPreferencesAtLevel(ISafariPreferencesService.PROJECT_LEVEL);
			return true;
		}
		// Example:  store all fields for a valid project
		useDefaultExecField.store();
		execField.store();
		useDefaultGenIncludePathField.store();
		includeDirectoriesField.store();
		extensionsToProcessField.store();
		extensionsToIncludeField.store();
		emitDiagnosticMessagesField.store();
		generateListingsField.store();
		
		// Nullify the project in any case
		prefService.setProject(null);
		return true;
	}
	
	
}
