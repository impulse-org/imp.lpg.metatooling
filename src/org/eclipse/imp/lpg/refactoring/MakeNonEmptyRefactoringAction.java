package org.jikespg.uide.refactoring;

import java.util.ResourceBundle;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.refactoring.RefactoringStarter;

public class MakeNonEmptyRefactoringAction extends TextEditorAction {
//    private final UniversalEditor fEditor;

    static ResourceBundle ResBundle= ResourceBundle.getBundle("org.jikespg.uide.refactoring.RefactoringMessages");

    public MakeNonEmptyRefactoringAction(UniversalEditor editor) {
	super(ResBundle, "makeNonEmpty.", editor);
//	fEditor= editor;
    }

    public void run() {
	final MakeNonEmptyRefactoring refactoring= new MakeNonEmptyRefactoring((UniversalEditor) this.getTextEditor());

	if (refactoring != null)
		new RefactoringStarter().activate(refactoring, new MakeNonEmptyWizard(refactoring, "Make Non-Empty"), this.getTextEditor().getSite().getShell(), "Make Non-Empty", false);
    }
}
