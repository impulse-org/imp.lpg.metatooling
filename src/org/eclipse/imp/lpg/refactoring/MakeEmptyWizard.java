package org.jikespg.uide.refactoring;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

public class MakeEmptyWizard extends RefactoringWizard {
    public MakeEmptyWizard(MakeEmptyRefactoring refactoring, String pageTitle) {
	super(refactoring, DIALOG_BASED_USER_INTERFACE | PREVIEW_EXPAND_FIRST_NODE);
	setDefaultPageTitle(pageTitle);
    }

    protected void addUserInputPages() {
	MakeEmptyInputPage page= new MakeEmptyInputPage("Make Empty");

	addPage(page);
    }

    public MakeEmptyRefactoring getMakeEmptyRefactoring() {
	return (MakeEmptyRefactoring) getRefactoring();
    }
}
