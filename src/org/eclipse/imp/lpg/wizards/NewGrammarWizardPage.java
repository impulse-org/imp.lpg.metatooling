package org.jikespg.uide.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionValidator;
import org.eclipse.uide.wizards.WizardPageField;

public class NewGrammarWizardPage extends WizardPage {
    protected Text fProjectText;

    protected Text fDescriptionText;

    protected final GrammarOptions fGrammarOptions= new GrammarOptions();

    protected GrammarPageHelper fHelper;

    public NewGrammarWizardPage(/* ImageDescriptor titleImage */) {
	super("New JikesPG Grammar", "New JikesPG Grammar", null);
    }

    public void createControl(Composite parent) {
	Composite container= new Composite(parent, SWT.NULL | SWT.BORDER);
	GridLayout layout= new GridLayout();

	layout.numColumns= 3;
	layout.verticalSpacing= 9;
	container.setLayout(layout);

	fHelper= new GrammarPageHelper(container, null, fGrammarOptions, getShell());

	createProjectLabelText(container);
	createFolderFields(container);
	createLanguageField(container);

	fHelper.createFields();

	fDescriptionText= new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
	fDescriptionText.setBackground(container.getBackground());
	GridData gd= new GridData(GridData.FILL_BOTH);
	gd.horizontalSpan= 3;
	gd.widthHint= 450;
	fDescriptionText.setLayoutData(gd);
	fDescriptionText.setEditable(false);
	fDescriptionText.setText("Create a new JikesPG Grammar");
	dialogChanged();
	setControl(container);
	fProjectText.setFocus();
    }

    private void createProjectLabelText(Composite container) {
	Label label= new Label(container, SWT.NULL);
	label.setText("Project:");
	label.setBackground(container.getBackground());
	label.setToolTipText("Select the project");
	fProjectText= new Text(container, SWT.BORDER | SWT.SINGLE);
	fProjectText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	IProject project= getProject();
	if (project != null)
	    fProjectText.setText(project.getName());
	fHelper.discoverSelectedProject();
	Button button= new Button(container, SWT.PUSH);
	button.setText("Browse...");
	button.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
		ContainerSelectionDialog dialog= new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace()
			.getRoot(), false, "Select a Project");
		dialog.setValidator(new ISelectionValidator() {
		    public String isValid(Object selection) {
			IProject project= ResourcesPlugin.getWorkspace().getRoot().getProject(selection.toString());
			if (project.exists())
			    return null;
			return "The selected element \"" + selection + "\" is not an existing project.";
		    }
		});
		if (dialog.open() == ContainerSelectionDialog.OK) {
		    Object[] result= dialog.getResult();
		    if (result.length == 1) {
			IProject selectedProject= ResourcesPlugin.getWorkspace().getRoot().getProject(result[0].toString());
			// fProjectText.setText(((Path) result[0]).toOSString());
			fProjectText.setText(selectedProject.getName());
		    }
		}
	    }
	});
	fProjectText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		Text text= (Text) e.widget;
		fGrammarOptions.setProjectName(text.getText());
		dialogChanged();
	    }
	});
	fProjectText.addFocusListener(new FocusAdapter() {
	    public void focusGained(FocusEvent e) {
		// Text text = (Text)e.widget;
		fDescriptionText.setText("Select the plug-in project to add this extension point to");
	    }
	});
    }

    private void createFolderFields(Composite container) {
	Label label= new Label(container, SWT.NULL);

	label.setText("Folder:");
	label.setBackground(container.getBackground());
	label.setToolTipText("Select the folder that will contain the grammar and parser");

	final Text folderText= new Text(container, SWT.BORDER | SWT.SINGLE);

	folderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	folderText.setText(fGrammarOptions.getPackageName());
	fHelper.discoverSelectedProject();

	Button button= new Button(container, SWT.PUSH);

	button.setText("Browse...");
	button.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
		ContainerSelectionDialog dialog= new ContainerSelectionDialog(getShell(),
			ResourcesPlugin.getWorkspace().getRoot(), false, "Select a Folder");
		dialog.setValidator(new ISelectionValidator() {
		    public String isValid(Object selection) {
			IPath path= new Path(selection.toString());
			if (path.segmentCount() > 1) {
			    IContainer container= ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
			    if (container != null && container.exists())
				return null;
			}
			return "The selected element \"" + selection + "\" is not an existing or valid destination folder.";
		    }
		});
		if (dialog.open() == ContainerSelectionDialog.OK) {
		    Object[] result= dialog.getResult();
		    IFolder selectedFolder= ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(result[0].toString()));
		    if (result.length == 1) {
			// folderText.setText(((Path) result[0]).toOSString());
			folderText.setText(selectedFolder.getProjectRelativePath().toString());
		    }
		}
	    }
	});
	folderText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		Text text= (Text) e.widget;
		fGrammarOptions.setPackageName(text.getText());
		dialogChanged();
	    }
	});
	folderText.addFocusListener(new FocusAdapter() {
	    public void focusGained(FocusEvent e) {
		// Text text = (Text)e.widget;
		fDescriptionText.setText("Select the folder to which to add this grammar");
	    }
	});
    }

    private void createLanguageField(Composite container) {
	Label langLabel= new Label(container, SWT.NULL);
	langLabel.setText("Language:");
	langLabel.setBackground(container.getBackground());
	langLabel.setToolTipText("Specify the name of the language for which you want to write a grammar");

	Text langText= new Text(container, SWT.BORDER | SWT.SINGLE);
	GridData td= new GridData(GridData.FILL_HORIZONTAL);
	td.horizontalSpan= 1;
	langText.setLayoutData(td);
	langText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		Text text= (Text) e.widget;
		fGrammarOptions.setLanguageName(text.getText());
	    }
	});
	new Label(container, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    void dialogChanged() {
	setErrorMessage(null);

	IProject project= getProject();
	if (project == null) {
	    setErrorMessage("Please select a project to which to add this grammar");
	    setPageComplete(false);
	    return;
	}
	WizardPageField field= getUncompletedField();
	if (field != null) {
	    setErrorMessage("Please provide a value for the required attribute \"" + field.label + "\"");
	    setPageComplete(false);
	    return;
	}
	setPageComplete(true);
    }

    public IProject getProject() {
	try {
	    IProject project= ResourcesPlugin.getWorkspace().getRoot().getProject(fGrammarOptions.getProjectName());

	    if (project.exists())
		return project;
	} catch (Exception e) {
	}
	return null;
    }

    //    public void setLanguage() {
    //        try {
    //            String language= "";
    //            WizardPageField field= getField("language");
    //            if (field.getText().length() == 0)
    //                field.setText(language);
    //            getField("class").setText(getPackage(language) + ".Parser");
    //        } catch (Exception e) {
    //            ErrorHandler.reportError("Cannot set language", e);
    //        }
    //    }

    public GrammarOptions getOptions() {
	return fGrammarOptions;
    }

    WizardPageField getUncompletedField() {
	// for(int n= 0; n < fFields.size(); n++) {
	//     WizardPageField field= (WizardPageField) fFields.get(n);
	//     if (field.required && field.value.length() == 0) {
	//         return field;
	//     }
	// }
	return null;
    }
}
