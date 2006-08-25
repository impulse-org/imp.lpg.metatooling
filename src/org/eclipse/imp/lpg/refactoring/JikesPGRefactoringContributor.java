package org.jikespg.uide.refactoring;

import org.eclipse.jface.action.IAction;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.editor.UniversalEditor.IRefactoringContributor;

public class JikesPGRefactoringContributor implements IRefactoringContributor {
    public JikesPGRefactoringContributor() { }

    public IAction[] getEditorRefactoringActions(UniversalEditor editor) {
	return new IAction[] {
		new MakeNonEmptyRefactoringAction(editor),
		new MakeEmptyRefactoringAction(editor),
		new MakeLeftRecursiveRefactoringAction(editor),
		new InlineNonTerminalRefactoringAction(editor)
	};
    }
}
