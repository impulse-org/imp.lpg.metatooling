package org.jikespg.uide.refactoring;

import org.eclipse.ui.texteditor.TextEditorAction;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.refactoring.RefactoringStarter;

public class MakeEmptyRefactoringAction extends TextEditorAction {
//    private final UniversalEditor fEditor;

    public MakeEmptyRefactoringAction(UniversalEditor editor) {
	super(MakeNonEmptyRefactoringAction.ResBundle, "makeEmpty.", editor);
//	fEditor= editor;
    }

    public void run() {
	final MakeEmptyRefactoring refactoring= new MakeEmptyRefactoring((UniversalEditor) this.getTextEditor());

	if (refactoring != null)
		new RefactoringStarter().activate(refactoring, new MakeEmptyWizard(refactoring, "Make Empty"), this.getTextEditor().getSite().getShell(), "Make Empty", false);
    }
}
