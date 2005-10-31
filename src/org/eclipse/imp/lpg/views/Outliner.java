/*
 * Created on Oct 31, 2005
 */
package org.jikespg.uide.views;

import java.util.Iterator;
import java.util.List;

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
    private final class OutlineVisitor extends JikesPGParser.AbstractVisitor {
	public void unimplementedVisitorFor(String s) {}

	public void visitAliasSeg(AliasSeg n) {
	    Ialias_segment as= n.getalias_segment();
	    fCurrentItem= createTreeItem("alias");
	}
	public void visitDefineSeg(DefineSeg n) {
	    Idefine_segment ds= n.getdefine_segment();
	    fCurrentItem= createTreeItem("define");
	}
	public void visitHeadersSeg(HeadersSeg n) {
	    Iheaders_segment hs= n.getheaders_segment();
	    fCurrentItem= createTreeItem("headers");
	}
	public void visitIdentifierSeg(IdentifierSeg n) {
	    Iidentifier_segment is= n.getidentifier_segment();
	    fCurrentItem= createTreeItem("identifiers");
	}
	public void visitTerminalsSeg(TerminalsSeg n) {
	    Iterminals_segment ts= n.getterminals_segment();
	    fCurrentItem= createTreeItem("terminals");
	}
	public void visitTitleSeg(TitleSeg n) {
	    Ititle_segment ts= n.gettitle_segment();
	    fCurrentItem= createTreeItem("title");
	}
	public void visitRulesSeg(RulesSeg n) {
	    Irules_segment rs= n.getrules_segment();
	    fCurrentItem= createTreeItem("rules");
	}
    }

    private Tree fTree;

    private TreeItem fCurrentItem;

    private static final String MESSAGE= "This is the default outliner. Add your own using the UIDE wizard and see class 'org.eclipse.uide.defaults.DefaultOutliner'";

    public TreeItem createTreeItem(String label) {
	TreeItem treeItem= new TreeItem(fTree, SWT.NONE);
	treeItem.setText(label);
	return treeItem;
    }

    public void createOutlinePresentation(IParseController controller, int offset) {
	try {
	    if (controller != null && fTree != null) {
		fTree.setRedraw(false);
		fTree.removeAll();
		if (controller.hasErrors()) {
		    createTreeItem(MESSAGE);
		    List errors= controller.getErrors();
		    createTreeItem("Found " + errors.size() + " syntax error(s) in input: ");
		    for(Iterator error= errors.iterator(); error.hasNext();) {
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
		    return;
		} else {
		    JikesPG jpg= (JikesPG) controller.getCurrentAst();
		    IJikesPG_INPUT segSeq= jpg.getJikesPG_INPUT();

		    segSeq.accept(new OutlineVisitor());
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

    public void setEditor(UniversalEditor editor) {}

    public void setTree(Tree tree) {
	this.fTree= tree;
    }
}
