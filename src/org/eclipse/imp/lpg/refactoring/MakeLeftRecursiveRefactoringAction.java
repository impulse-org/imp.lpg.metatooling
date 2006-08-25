package org.jikespg.uide.refactoring;

import org.eclipse.ui.texteditor.TextEditorAction;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.refactoring.RefactoringStarter;

public class MakeLeftRecursiveRefactoringAction extends TextEditorAction {
//    private final UniversalEditor fEditor;

    public MakeLeftRecursiveRefactoringAction(UniversalEditor editor) {
	super(MakeNonEmptyRefactoringAction.ResBundle, "makeLeftRecursive.", editor);
//	fEditor= editor;
    }

    public void run() {
	final MakeLeftRecursiveRefactoring refactoring= new MakeLeftRecursiveRefactoring((UniversalEditor) this.getTextEditor());

	if (refactoring != null)
		new RefactoringStarter().activate(refactoring, new MakeLeftRecursiveWizard(refactoring, "Make LeftRecursive"), this.getTextEditor().getSite().getShell(), "Make LeftRecursive", false);
    }
}
