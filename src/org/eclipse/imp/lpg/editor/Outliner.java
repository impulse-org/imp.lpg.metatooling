/*
 * Created on Oct 31, 2005
 */
package org.jikespg.uide.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.uide.core.ErrorHandler;
import org.eclipse.uide.defaults.DefaultOutliner;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.parser.IParseController;
import org.eclipse.uide.parser.ParseError;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.*;

import com.ibm.lpg.IToken;

public class Outliner extends DefaultOutliner {
    Stack fItemStack= new Stack();

    private final class OutlineVisitor extends JikesPGParser.AbstractVisitor {
	public void unimplementedVisitorFor(String s) {
	    System.out.println(s);
	}

	public void visitJikesPG(JikesPG n) {
//	    fItemStack.push(createTreeItem("JikesPG Spec"));
	    fItemStack.push(createTreeItem("Options"));
	    n.getoptions_segment().accept(this);
	    fItemStack.pop();
	    n.getJikesPG_INPUT().accept(this);
//	    fItemStack.pop();
	}
	public void visitoptions_segment(options_segment n) {
	    if (n.getoptions_segment() != null)
		n.getoptions_segment().accept(this);
	    n.getoption_spec().accept(this);
	}
	public void visitoption_spec(option_spec n) {
	    n.getoption_list().accept(this);
	}
	public void visitoption_list(option_list n) {
	    n.getoption_list().accept(this);
	    n.getoption().accept(this);
	}
	public void visitoption(option n) {
	    option_value value= (option_value) n.getoption_value();
	    createSubItem(symbolImage(n.getSYMBOL()) + " = " + symbolImage(value.getSYMBOL()));
	}
	private String symbolImage(TSYMBOL symbol) {
	    int symTokenIdx= symbol.getLeftToken();
	    IToken token= fController.getParser().getParseStream().getTokenAt(symTokenIdx);

	    return new String(fController.getLexer().getLexStream().getInputChars(), token.getStartOffset(), token.getEndOffset()-token.getStartOffset()+1);
	}

	public void visitJikesPG_INPUT(JikesPG_INPUT n) {
//	    fItemStack.push(createTreeItem("Grammar"));
	    n.getJikesPG_item().accept(this);
//	    fItemStack.pop();
	}
	public void visitAliasSeg(AliasSeg n) {
	    Ialias_segment as= n.getalias_segment();
	    createSubItem("alias");
	}
	public void visitDefineSeg(DefineSeg n) {
	    Idefine_segment ds= n.getdefine_segment();
	    createSubItem("define");
	}
	public void visitHeadersSeg(HeadersSeg n) {
	    Iheaders_segment hs= n.getheaders_segment();
	    createSubItem("headers");
	}
	public void visitIdentifierSeg(IdentifierSeg n) {
	    Iidentifier_segment is= n.getidentifier_segment();
	    createSubItem("identifiers");
	}
	public void visitTerminalsSeg(TerminalsSeg n) {
	    Iterminals_segment ts= n.getterminals_segment();
	    createSubItem("terminals");
	}
	public void visitTitleSeg(TitleSeg n) {
	    Ititle_segment ts= n.gettitle_segment();
	    createSubItem("title");
	}
	public void visitRulesSeg(RulesSeg n) {
	    fItemStack.push(createTreeItem("Rules"));
	    n.getrules_segment().accept(this);
	    fItemStack.pop();
	}
	public void visitrules_segment99(rules_segment99 n) {
	    n.getrules_segment().accept(this);
	    n.getrules().accept(this);
	}
	public void visitrules100(rules100 n) {
	    createSubItem(symbolImage(n.getSYMBOL()));
	}
    }

    private Tree fTree;

    private IParseController fController;

    private static final String MESSAGE= "This is the default outliner. Add your own using the UIDE wizard and see class 'org.eclipse.uide.defaults.DefaultOutliner'";

    public TreeItem createTreeItem(String label) {
	TreeItem treeItem= new TreeItem(fTree, SWT.NONE);
	treeItem.setText(label);
	treeItem.setImage(JavaPluginImages.DESC_MISC_PUBLIC.createImage());
	return treeItem;
    }

    public TreeItem createSubItem(String label) {
	TreeItem treeItem= new TreeItem((TreeItem) fItemStack.peek(), SWT.NONE);
	treeItem.setText(label);
	treeItem.setImage(JavaPluginImages.DESC_MISC_PUBLIC.createImage());
	return treeItem;
    }

    public void createOutlinePresentation(IParseController controller, int offset) {
	fController= controller;
	try {
	    if (controller != null && fTree != null) {
		fTree.setRedraw(false);
		fTree.removeAll();
		fItemStack.clear();
		if (controller.hasErrors()) {
		    createDebugContents(controller);
		} else {
		    JikesPG jpg= (JikesPG) controller.getCurrentAst();

		    jpg.accept(new OutlineVisitor());
		}
		fTree.setSelection(new TreeItem[] { fTree.getItem(new Point(0, 0)) });
	    }
	    //selectTreeItemAtTextOffset(offset);
	} catch (Throwable e) {
	    ErrorHandler.reportError("Could not generate outline", e);
	} finally {
	    if (fTree != null)
		fTree.setRedraw(true);
	}
    }

    private void createDebugContents(IParseController controller) {
	createTreeItem(MESSAGE);
	List errors= controller.getErrors();
	createTreeItem("Found " + errors.size() + " syntax error(s) in input: ");
	for(Iterator error= errors.iterator(); error.hasNext(); ) {
	    ParseError pe= (ParseError) error.next();
	    createTreeItem("  " + pe.description);
	}
	int count= controller.getParser().getParseStream().getSize();
	if (count > 1) {
	createTreeItem("Tokens:");
	for(int n= 1; n < 100 && n < count; n++) {
	    IToken token= controller.getParser().getParseStream().getTokenAt(n);
	    String label= n + ": "
		    + controller.getParser().getParseStream().orderedTerminalSymbols()[token.getKind()] + " = "
		    + token.getValue(controller.getLexer().getLexStream().getInputChars());
	    createTreeItem(label);
	}
	if (count >= 100)
	    createTreeItem("rest of outline truncated...");
	}
    }

    public void setEditor(UniversalEditor editor) {}

    public void setTree(Tree tree) {
	this.fTree= tree;
    }
}
