package org.jikespg.uide.refactoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.ASTUtils;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.IASTNodeToken;
import org.jikespg.uide.parser.JikesPGParser.IsymWithAttrs;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.rhs;
import org.jikespg.uide.parser.JikesPGParser.rhsList;
import org.jikespg.uide.parser.JikesPGParser.symWithAttrsList;

public class InlineNonTerminalRefactoring extends Refactoring {
    private final IFile fGrammarFile;
    private final ASTNode fNode;
    private nonTerm fNonTerm;
    private symWithAttrsList fRHS;
    private boolean fInlineAll;
    private boolean fDeleteOrig;

    public InlineNonTerminalRefactoring(UniversalEditor editor) {
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
	fInlineAll= false;
	fDeleteOrig= false;
    }

    public boolean getDeleteOrig() {
        return fDeleteOrig;
    }

    public void setDeleteOrig(boolean deleteOrig) {
        fDeleteOrig= deleteOrig;
    }

    private ASTNode findNode(UniversalEditor editor) {
	Point sel= editor.getSelection();
	IParseController parseController= editor.getParseController();
	ASTNode root= (ASTNode) parseController.getCurrentAst();
	IASTNodeLocator locator= parseController.getNodeLocator();

	return (ASTNode) locator.findNode(root, sel.x);
    }

    public String getName() {
	return "Inline Non-Terminal";
    }

    public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	// Check parameters retrieved from editor context
	if (fNode instanceof nonTerm) {
	    fNonTerm= (nonTerm) fNode;
	    fInlineAll= true;
	} else if (fNode instanceof IsymWithAttrs) {
	    ASTNode def= ASTUtils.findDefOf(((IASTNodeToken) fNode), ASTUtils.getRoot(fNode));

	    if (!(def instanceof nonTerm))
		return RefactoringStatus.createFatalErrorStatus("Inline Non-Terminal is only valid for non-terminal symbols");
	    fNonTerm= (nonTerm) def;
	} else if (fNode instanceof IASTNodeToken) {
	    if (fNode.getParent() instanceof nonTerm) {
		fNonTerm= (nonTerm) fNode.getParent();
		fInlineAll= true;
	    } else
		return RefactoringStatus.createFatalErrorStatus("Inline Non-Terminal is only valid for non-terminal symbols");
	} else
	    return RefactoringStatus.createFatalErrorStatus("Inline Non-Terminal is only valid for non-terminal symbols");

	rhsList rules= fNonTerm.getrhsList();

	if (rules.size() != 1)
	    return RefactoringStatus.createFatalErrorStatus("Inline Non-Terminal is only valid for non-terminals with a single production");

	final rhs rule= (rhs) rules.getElementAt(0);

	if (rule.getopt_action_segment() != null)
	    return RefactoringStatus.createFatalErrorStatus("Non-terminal to be inlined cannot have an action block");

	fRHS= rule.getsymWithAttrsList();
	return new RefactoringStatus();
    }

    public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	return new RefactoringStatus();
    }

    public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
	// TODO Replace hand-written transform code with usage of SAFARI AST rewriter.
	List/*<IsymWithAttrs>*/ refs;

	if (fInlineAll)
	    refs= ASTUtils.findRefsOf(fNonTerm);
	else
	    refs= Collections.singletonList(fNode);

	StringBuffer buff= new StringBuffer();
	int N= fRHS.size();

	for(int i=0; i < N; i++) {
	    // TODO Don't gratuitously throw out the action code...
	    // TODO Check to see whether this sym has any annotations, and if so... do something about them... whatever that means
	    if (i > 0) buff.append(' ');
	    buff.append(fRHS.getElementAt(i).toString());
	}

	String nonTermString= buff.toString();
	TextFileChange tfc= new TextFileChange("Inline Non-Terminal", fGrammarFile);

	tfc.setEdit(new MultiTextEdit());

	for(Iterator iter= refs.iterator(); iter.hasNext(); ) {
	    IsymWithAttrs sym= (IsymWithAttrs) iter.next();
	    int startOffset= sym.getLeftIToken().getStartOffset();
	    int endOffset= sym.getLeftIToken().getEndOffset();

	    tfc.addEdit(new ReplaceEdit(startOffset, endOffset - startOffset + 1, nonTermString));
	}

	if (fDeleteOrig) {
	    final int startOffset= fNonTerm.getLeftIToken().getStartOffset();
	    final int endOffset= fNonTerm.getRightIToken().getEndOffset();
	    tfc.addEdit(new DeleteEdit(startOffset, endOffset - startOffset + 1));
	}
	return tfc;
    }

    public boolean getInlineAll() {
        return fInlineAll;
    }

    public void setInlineAll(boolean inlineAll) {
        fInlineAll= inlineAll;
    }
}
