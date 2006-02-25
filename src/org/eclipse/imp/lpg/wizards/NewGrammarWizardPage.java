package org.jikespg.uide.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionValidator;
import org.eclipse.uide.wizards.WizardPageField;

public class NewGrammarWizardPage extends WizardPage {
    protected Text projectText;

    protected Text descriptionText;

    protected static String projectName= "";

    private String fLanguageName;

    private String fPackageName= "";

    private String fTemplateKind;

    private String fTargetLanguage;

    private boolean fHasKeywords;

    private boolean fRequiresBacktracking;

    private boolean fAutoGenerateASTs;

    public NewGrammarWizardPage(/* ImageDescriptor titleImage */) {
        super("New JikesPG Grammar", "New JikesPG Grammar", null);
    }

    public void createControl(Composite parent) {
        Composite container= new Composite(parent, SWT.NULL | SWT.BORDER);
        GridLayout layout= new GridLayout();

        layout.numColumns= 3;
        layout.verticalSpacing= 9;
        container.setLayout(layout);

        createProjectLabelText(container);

        Label langLabel= new Label(container, SWT.NULL);
        langLabel.setText("Language:");
        langLabel.setBackground(container.getBackground());
        langLabel.setToolTipText("Specify the name of the language for which you want to write a grammar");

        Text langText= new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData td= new GridData(GridData.FILL_HORIZONTAL);
        td.horizontalSpan= 2;
        langText.setLayoutData(td);
        langText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Text text= (Text) e.widget;
                fLanguageName= text.getText();
            }
        });

        createTemplateField(container);
        createLanguageField(container);
        createOptionsFields(container);

        descriptionText= new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        descriptionText.setBackground(container.getBackground());
        GridData gd= new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan= 3;
        gd.widthHint= 450;
        descriptionText.setLayoutData(gd);
        descriptionText.setEditable(false);
        descriptionText.setText("Create a new JikesPG Grammar");
        dialogChanged();
        setControl(container);
        projectText.setFocus();
    }

    private void createProjectLabelText(Composite container) {
        Label label= new Label(container, SWT.NULL);
        label.setText("Project:");
        label.setBackground(container.getBackground());
        label.setToolTipText("Select the project");
        projectText= new Text(container, SWT.BORDER | SWT.SINGLE);
        projectText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        IProject project= getProject();
        if (project != null)
            projectText.setText(project.getName());
        discoverSelectedProject();
        Button button= new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                ContainerSelectionDialog dialog= new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false, "Select a Project");
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
                    IProject selectedProject= ResourcesPlugin.getWorkspace().getRoot().getProject(result[0].toString());
                    if (result.length == 1) {
                        // projectText.setText(((Path) result[0]).toOSString());
                        projectText.setText(selectedProject.getName());
                    }
                }
            }
        });
        projectText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Text text= (Text) e.widget;
                setProjectName(text.getText());
                dialogChanged();
            }
        });
        projectText.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                // Text text = (Text)e.widget;
                descriptionText.setText("Select the plug-in project to add this extension point to");
            }
        });
    }

    private void discoverSelectedProject() {
        ISelectionService service= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
        ISelection selection= service.getSelection();
        IProject project= getProject(selection);
        if (project != null) {
            projectName= project.getName();
            projectText.setText(projectName);
        }
    }

    private IProject getProject(ISelection selection) {
        if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
            IStructuredSelection ssel= (IStructuredSelection) selection;
            if (ssel.size() > 1)
                return null;
            Object obj= ssel.getFirstElement();
            if (obj instanceof IPackageFragmentRoot)
                obj= ((IPackageFragmentRoot) obj).getResource();
            if (obj instanceof IResource) {
                return ((IResource) obj).getProject();
            }
            if (obj instanceof IJavaProject) {
                return ((IJavaProject) obj).getProject();
            }
        }
        return null;
    }

    private void setProjectName(String newProjectName) {
        if (newProjectName.startsWith("P\\"))
            projectName= newProjectName.substring(1);
        else if (newProjectName.startsWith("\\"))
            projectName= newProjectName;
        else
            projectName= "\\" + newProjectName;
    }

    public IProject getProject() {
        try {
            IProject project= ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            if (project.exists())
                return project;
        } catch (Exception e) {
        }
        return null;
    }

    void dialogChanged() {
        setErrorMessage(null);

        IProject project= getProject();
        if (project == null) {
            setErrorMessage("Please select a plug-in project to add this extension point to");
            setPageComplete(false);
            return;
        }
        boolean isPlugin= false;
        try {
            isPlugin= project.hasNature("org.eclipse.pde.PluginNature");
        } catch (CoreException e) {
        }
        if (!isPlugin) {
            setErrorMessage("\"" + projectName + "\" is not a plug-in project. Please select a plug-in project to add this extension point to");
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

    private void createTemplateField(Composite parent) {
        Label label= new Label(parent, SWT.NULL);
        label.setText("Template:");
        label.setToolTipText("Select the parser/lexer template to use");
        label.setBackground(parent.getBackground());

        final int defaultTemplate= 1; // UIDE
        Combo combo= new Combo(parent, SWT.READ_ONLY);

        combo.setFont(parent.getFont());
        combo.setItems(new String[] { "none", "UIDE" });
        combo.setLayoutData(new GridData(GridData.BEGINNING));
        combo.select(defaultTemplate);
        combo.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                fTemplateKind= e.text;
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        fTemplateKind= combo.getItem(defaultTemplate);

        new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void createLanguageField(Composite parent) {
        Label label= new Label(parent, SWT.NULL);
        label.setText("Implementation:");
        label.setToolTipText("Select the implementation language for the parser/lexer");
        label.setBackground(parent.getBackground());

        final int defaultLang= 0; // Java
        Combo combo= new Combo(parent, SWT.READ_ONLY);

        combo.setFont(parent.getFont());
        combo.setItems(new String[] { "java", "c++" });
        combo.setLayoutData(new GridData(GridData.BEGINNING));
        combo.select(defaultLang);
        combo.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                fTargetLanguage= e.text;
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
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

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

	final Button cbBacktrack= new Button(parent, SWT.CHECK);

	cbBacktrack.setText("Language requires Backtracking");
	cbBacktrack.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
	        fRequiresBacktracking= cbBacktrack.getSelection();
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

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
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

    public String getLanguage() {
        return fLanguageName;
    }

    public String getTemplateKind() {
        return fTemplateKind;
    }

    public boolean autoGenerateASTs() {
        return fAutoGenerateASTs;
    }

    public boolean hasKeywords() {
        return fHasKeywords;
    }

    public boolean requiresBacktracking() {
        return fRequiresBacktracking;
    }

    WizardPageField getUncompletedField() {
        // for(int n= 0; n < fields.size(); n++) {
        // WizardPageField field= (WizardPageField) fields.get(n);
        // if (field.required && field.value.length() == 0) {
        // return field;
        // }
        // }
        return null;
    }
}
