/*
 * Created on Feb 28, 2006
 */
package org.jikespg.uide.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

public class GrammarPageHelper {
    private final Composite fParent;

    private final IProject fProject;

    private final GrammarOptions fGrammarOptions;

    private Text fProjectText;

    private Shell fShell;

    public GrammarPageHelper(Composite parent, IProject project, GrammarOptions options, Shell shell) {
	fParent= parent;
	fProject= project;
	fGrammarOptions= options;
	fShell= shell;
    }

    public void createFields() {
	// createProjectLabelText(fParent);
	// createFolderFields(fParent);
	// createLanguageField(fParent);
	createTemplateField(fParent);
	createImplLanguageField(fParent);
	createOptionsFields(fParent);
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
		fGrammarOptions.setTemplateKind(e.text);
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	fGrammarOptions.setTemplateKind(combo.getItem(defaultTemplate));

	new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void createImplLanguageField(Composite parent) {
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
		fGrammarOptions.setTargetLanguage(e.text);
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	fGrammarOptions.setTargetLanguage(combo.getItem(defaultLang));
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
		fGrammarOptions.setHasKeywords(cbKeywords.getSelection());
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	final Button cbBacktrack= new Button(parent, SWT.CHECK);

	cbBacktrack.setText("Language requires Backtracking");
	cbBacktrack.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		fGrammarOptions.setRequiresBacktracking(cbBacktrack.getSelection());
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	new Label(parent, SWT.NULL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	final Button cbAutoASTs= new Button(parent, SWT.CHECK);

	cbAutoASTs.setText("JikesPG auto-generated AST classes");
	cbAutoASTs
		.setToolTipText("Check this if you want JikesPG to generate a set of AST classes for your grammar's non-terminals.");
	cbAutoASTs.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		fGrammarOptions.setAutoGenerateASTs(cbAutoASTs.getSelection());
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
    }

    public void discoverSelectedProject() {
	ISelectionService service= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
	ISelection selection= service.getSelection();
	IProject project= getProject(selection);
	if (project != null) {
	    fGrammarOptions.setProjectName(project.getName());
	    fProjectText.setText(project.getName());
	}
    }

    public static IProject getProject(ISelection selection) {
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
}