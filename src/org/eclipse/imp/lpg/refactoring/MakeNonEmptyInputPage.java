package org.jikespg.uide.refactoring;

import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class MakeNonEmptyInputPage extends UserInputWizardPage {
    public MakeNonEmptyInputPage(String name) {
	super(name);
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
	Composite result= new Composite(parent, SWT.NONE);
	setControl(result);
	GridLayout layout= new GridLayout();
	layout.numColumns= 2;
	result.setLayout(layout);
	//		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaHelpContextIds.INTRODUCE_FACTORY_WIZARD_PAGE);		
    }

    private MakeNonEmptyRefactoring getMakeNonEmptyRefactoring() {
	return (MakeNonEmptyRefactoring) getRefactoring();
    }
}
