package org.jikespg.uide.refactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.rhs;
import org.jikespg.uide.parser.JikesPGParser.rhsList;
import org.jikespg.uide.parser.JikesPGParser.symWithAttrsList;

public class MakeNonEmptyRefactoring extends Refactoring {
    private final IFile fGrammarFile;
    private final ASTNode fNode;

    public MakeNonEmptyRefactoring(UniversalEditor editor) {
	super();

	IEditorInput input= editor.getEditorInput();

	if (input instanceof IFileEditorInput) {
	    IFileEditorInput fileInput= (IFileEditorInput) input;

	    fGrammarFile= fileInput.getFile();
	    fNode= findNode(editor);
	} else {
	    fGrammarFile= null;
	    fNode= null;
	}
    }

    private ASTNode findNode(UniversalEditor editor) {
	Point sel= editor.getSelection();
	IParseController parseController= editor.getParseController();
	ASTNode root= (ASTNode) parseController.getCurrentAst();
	IASTNodeLocator locator= parseController.getNodeLocator();

	return (ASTNode) locator.findNode(root, sel.x);
    }

    public String getName() {
	return "Make Non-Empty";
    }

    public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	if (!(fNode instanceof nonTerm))
	    return RefactoringStatus.createFatalErrorStatus("Make Non-Empty is only valid for non-terminals");

	nonTerm nt= (nonTerm) fNode;
	rhsList rhSides= nt.getrhsList();

	if (rhSides.size() != 2)
	    return RefactoringStatus.createFatalErrorStatus("Make Non-Empty is only valid for non-terminals with 2 productions");

	rhs rhs1= (rhs) rhSides.getElementAt(0);
	rhs rhs2= (rhs) rhSides.getElementAt(1);

	symWithAttrsList rhs1Syms= rhs1.getsymWithAttrsList();
	symWithAttrsList rhs2Syms= rhs2.getsymWithAttrsList();

	if (rhs2Syms.size() == 1 && rhs2Syms.getElementAt(0).toString().equals("$empty")) {
	    rhs tmp= rhs1;
	    symWithAttrsList tmpList= rhs1Syms;

	    rhs1= rhs2;
	    rhs2= tmp;
	    rhs1Syms= rhs2Syms;
	    rhs2Syms= tmpList;
	} else if (rhs1Syms.size() != 1 || !rhs1Syms.getElementAt(0).toString().equals("$empty")) {
	    return RefactoringStatus.createFatalErrorStatus("Non-terminal must have the form 'a ::= $empty | a b'");
	}

	if (!rhs2Syms.getElementAt(0).toString().equals(nt.getSYMBOL().toString()))
	    return RefactoringStatus.createFatalErrorStatus("Non-terminal must have the form 'a ::= $empty | a b'");

	return new RefactoringStatus();
    }

    public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	return new RefactoringStatus();
    }

    public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	// TODO Replace hand-written transform code with usage of SAFARI AST rewriter.
	nonTerm nt= (nonTerm) fNode;
	rhsList rhSides= nt.getrhsList();
	rhs rhs1= (rhs) rhSides.getElementAt(0);
	rhs rhs2= (rhs) rhSides.getElementAt(1);

	symWithAttrsList rhs1Syms= rhs1.getsymWithAttrsList();
	symWithAttrsList rhs2Syms= rhs2.getsymWithAttrsList();
	int N= rhs2Syms.size();

	if (rhs2Syms.size() == 1) {
	    rhs tmp= rhs1;
	    symWithAttrsList tmpList= rhs1Syms;

	    rhs1= rhs2;
	    rhs2= tmp;
	    rhs1Syms= rhs2Syms;
	    rhs2Syms= tmpList;
	}

	ASTNode symToReplace= rhs1Syms.getElementAt(0);
	StringBuffer buff= new StringBuffer();

	for(int i=1; i < N; i++) {
	    if (i > 1) buff.append(' ');
	    buff.append(rhs2Syms.getElementAt(i));
	}

	TextFileChange tfc= new TextFileChange("Make Non-Empty", fGrammarFile);

	tfc.setEdit(new MultiTextEdit());
	tfc.addEdit(new ReplaceEdit(symToReplace.getLeftIToken().getStartOffset(), symToReplace.getRightIToken().getEndOffset() - symToReplace.getLeftIToken().getStartOffset() + 1, buff.toString()));
	return tfc;
    }
}
