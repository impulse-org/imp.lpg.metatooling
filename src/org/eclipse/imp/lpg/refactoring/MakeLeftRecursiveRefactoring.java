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
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
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

public class MakeLeftRecursiveRefactoring extends Refactoring {
    private final IFile fGrammarFile;
    private final ASTNode fNode;

    public MakeLeftRecursiveRefactoring(UniversalEditor editor) {
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
	return "Make Left Recursive";
    }

    public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	if (!(fNode instanceof nonTerm) && !(fNode instanceof rhs))
	    return RefactoringStatus.createFatalErrorStatus("Make Left Recursive is only valid for non-terminals and recursive productions");

	if (fNode instanceof nonTerm) {
	    nonTerm nt= (nonTerm) fNode;
	    rhsList rhSides= nt.getrhsList();

	    for(int i=0; i < rhSides.size(); i++)
		checkProduction((rhs) rhSides.getElementAt(i), nt);
	} else {
	    rhs prod= (rhs) fNode;
	    checkProduction(prod, (nonTerm) prod.getParent());
	}

	return new RefactoringStatus();
    }

    private RefactoringStatus checkProduction(rhs prod, nonTerm nt) {
	symWithAttrsList rhsSyms= prod.getsymWithAttrsList();
	final int N= rhsSyms.size();

	for(int i= 0; i < N; i++) { // make sure the production is of the form a ::= b c ... a
	    if (rhsSyms.getElementAt(i).toString().equals(nt.getSYMBOL().toString()))
		return RefactoringStatus.createFatalErrorStatus("Non-terminal must have the form 'a ::= b c ... a'");
	}
	if (!rhsSyms.getElementAt(N-1).toString().equals(nt.getSYMBOL().toString()))
	    return RefactoringStatus.createFatalErrorStatus("Non-terminal must have the form 'a ::= b c ... a'");
	return new RefactoringStatus();
    }

    public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	return new RefactoringStatus();
    }

    private void rewriteProduction(rhs prod, nonTerm nt, TextFileChange tfc) {
	// TODO Replace hand-written transform code with usage of SAFARI AST rewriter.
	String ntSym= nt.getSYMBOL().toString();
	symWithAttrsList syms= prod.getsymWithAttrsList();
	int N= syms.size();
	ASTNode symToDelete= syms.getElementAt(N-1);
	int deleteStart= symToDelete.getLeftIToken().getStartOffset();
	int deleteEnd= symToDelete.getRightIToken().getEndOffset();

	if (!symToDelete.getLeftIToken().toString().equals(ntSym.toString()))
	    return;
	tfc.addEdit(new DeleteEdit(deleteStart, deleteEnd - deleteStart + 1));
	tfc.addEdit(new InsertEdit(syms.getElementAt(0).getLeftIToken().getStartOffset(), ntSym + " "));
    }

    public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	TextFileChange tfc= new TextFileChange("Make Left Recursive", fGrammarFile);
	nonTerm nt;

	tfc.setEdit(new MultiTextEdit());

	if (fNode instanceof nonTerm) {
	    nt= (nonTerm) fNode;
	    rhsList rhSides= nt.getrhsList();

	    for(int i=0; i < rhSides.size(); i++)
		rewriteProduction((rhs) rhSides.getElementAt(i), nt, tfc);
	} else {
	    rhs prod= (rhs) fNode;
	    nt= (nonTerm) prod.getParent();
	    rewriteProduction(prod, nt, tfc);
	}

	return tfc;
    }
}
