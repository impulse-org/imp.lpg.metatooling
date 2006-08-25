package org.jikespg.uide.refactoring;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

public class MakeNonEmptyWizard extends RefactoringWizard {
    public MakeNonEmptyWizard(MakeNonEmptyRefactoring refactoring, String pageTitle) {
	super(refactoring, DIALOG_BASED_USER_INTERFACE | PREVIEW_EXPAND_FIRST_NODE);
	setDefaultPageTitle(pageTitle);
    }

    protected void addUserInputPages() {
	MakeNonEmptyInputPage page= new MakeNonEmptyInputPage("Make Non-Empty");

	addPage(page);
    }

    public MakeNonEmptyRefactoring getMakeNonEmptyRefactoring() {
	return (MakeNonEmptyRefactoring) getRefactoring();
    }
}
