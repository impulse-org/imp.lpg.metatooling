package org.jikespg.uide.refactoring;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class InlineNonTerminalInputPage extends UserInputWizardPage {
    public InlineNonTerminalInputPage(String name) {
	super(name);
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
	Composite result= new Composite(parent, SWT.NONE);
	setControl(result);
	GridLayout layout= new GridLayout();
	layout.numColumns= 1;
	result.setLayout(layout);

	final Button deleteButton= new Button(result, SWT.CHECK);

	deleteButton.setText("Delete non-terminal after inlining");
	deleteButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	deleteButton.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		getInlineNonTerminalRefactoring().setDeleteOrig(deleteButton.getSelection());
	    }
	    public void widgetDefaultSelected(SelectionEvent e) { }
	});

	final Button inlineAllButton= new Button(result, SWT.CHECK);

	inlineAllButton.setText("Inline all occurrences of non-terminal");
	inlineAllButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	inlineAllButton.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		getInlineNonTerminalRefactoring().setInlineAll(inlineAllButton.getSelection());
	    }
	    public void widgetDefaultSelected(SelectionEvent e) { }
	});
	// PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaHelpContextIds.INTRODUCE_FACTORY_WIZARD_PAGE);
    }

    private InlineNonTerminalRefactoring getInlineNonTerminalRefactoring() {
	return (InlineNonTerminalRefactoring) getRefactoring();
    }
}
