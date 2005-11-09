/*
 * Created on Oct 31, 2005
 */
package org.jikespg.uide.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.uide.core.ErrorHandler;
import org.eclipse.uide.defaults.DefaultOutliner;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.parser.IParseController;
import org.eclipse.uide.parser.ParseError;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.*;

import com.ibm.lpg.IToken;
import com.ibm.lpg.PrsStream;

public class Outliner extends DefaultOutliner {
    Stack fItemStack= new Stack();

    private final class OutlineVisitor extends JikesPGParser.AbstractVisitor {
	public void unimplementedVisitorFor(String s) {
	    System.out.println(s);
	}

	public void visitJikesPG(JikesPG n) {
	    fItemStack.push(createTopItem("Options", n));
	    if (n.getoptions_segment() != null)
		n.getoptions_segment().accept(this);
	    fItemStack.pop();
            if (n.getJikesPG_INPUT() != null)
                n.getJikesPG_INPUT().accept(this);
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
	    createSubItem(symbolImage(n.getSYMBOL()) + " = " + symbolImage(value.getSYMBOL()), n);
	}
	public void visitJikesPG_INPUT(JikesPG_INPUT n) {
//	    fItemStack.push(createTreeItem("Grammar"));
	    if (n.getJikesPG_INPUT() != null)
		n.getJikesPG_INPUT().accept(this);
	    n.getJikesPG_item().accept(this);
//	    fItemStack.pop();
	}
	public void visitAliasSeg(AliasSeg n) {
	    fItemStack.push(createTopItem("Alias", n));
	    n.getalias_segment().accept(this);
	    fItemStack.pop();
	}
	public void visitDefineSeg(DefineSeg n) {
	    fItemStack.push(createTopItem("Define", n));
	    n.getdefine_segment().accept(this);
	    fItemStack.pop();
	}
	public void visitHeadersSeg(HeadersSeg n) {
	    fItemStack.push(createTopItem("Headers", n));
	    n.getheaders_segment().accept(this);
	    fItemStack.pop();
	}
	public void visitIdentifierSeg(IdentifierSeg n) {
	    fItemStack.push(createTopItem("Identifiers", n));
	    n.getidentifier_segment().accept(this);
	    fItemStack.pop();
	}
	public void visitStartSeg(StartSeg n) {
	    n.getstart_segment().accept(this);
	}
	public void visitstart_segment(start_segment n) {
	    n.getstart_symbol().accept(this);
//	    createTreeItem(symbolImage());
	}
	public void visitstart_symbol96(start_symbol96 n) {
	    createTopItem("Start = " + symbolImage(n.getSYMBOL()), n);
	}
	public void visitTerminalsSeg(TerminalsSeg n) {
	    fItemStack.push(createTopItem("Terminals", n));
	    n.getterminals_segment().accept(this);
	    fItemStack.pop();
	}
	public void visitterminals_segment45(terminals_segment45 n) {
//	    createSubItem(symbolImage(n.getTERMINALS_KEY()), n);
	}
	public void visitterminals_segment46(terminals_segment46 n) {
	    n.getterminals_segment().accept(this);
	    n.getterminal_symbol().accept(this);
	}
	public void visitterminals_segment47(terminals_segment47 n) {
	    n.getterminals_segment().accept(this);
	    String label= nameImage(n.getname()) + " " + producesImage(n.getproduces()) + " " + symbolImage(n.getterminal_symbol());
            createSubItem(label, n);
	}
	public void visitterminal_symbol74(terminal_symbol74 n) {
	    createSubItem(symbolImage(n.getSYMBOL()), n);
	}
	public void visitterminal_symbol75(terminal_symbol75 n) {
	    createSubItem(symbolImage(n.getMACRO_NAME()), n);
	}
	public void visitTitleSeg(TitleSeg n) {
	    fItemStack.push(createTopItem("Title", n));
	    n.gettitle_segment().accept(this);
	    fItemStack.pop();
	}
	public void visittitle_segment32(title_segment32 n) {
	    createSubItem(symbolImage(n.getTITLE_KEY()), n);
	}
	public void visittitle_segment33(title_segment33 n) {
	    createSubItem(symbolImage(n.getTITLE_KEY().getTITLE_KEY()), n);
	}
	public void visitRulesSeg(RulesSeg n) {
	    fItemStack.push(createTopItem("Rules", n));
	    n.getrules_segment().accept(this);
	    fItemStack.pop();
	}
	public void visitrules_segment99(rules_segment99 n) {
	    n.getrules_segment().accept(this);
	    n.getrules().accept(this);
	}
	public void visitrules100(rules100 n) {
	    createSubItem(symbolImage(n.getSYMBOL()), n);
	}
    }

    private Tree fTree;

    private IParseController fController;

    private static final String MESSAGE= "This is the default outliner. Add your own using the UIDE wizard and see class 'org.eclipse.uide.defaults.DefaultOutliner'";

    private String symbolImage(int symTokenIdx) {
	IToken token= fController.getParser().getParseStream().getTokenAt(symTokenIdx);

	return new String(fController.getLexer().getLexStream().getInputChars(), token.getStartOffset(), token.getEndOffset()-token.getStartOffset()+1);
    }

    public String symbolImage(Iterminal_symbol terminal_symbol) {
	if (terminal_symbol instanceof terminal_symbol74)
	    return symbolImage(((terminal_symbol74) terminal_symbol).getSYMBOL());
	else if (terminal_symbol instanceof terminal_symbol75)
	    return symbolImage(((terminal_symbol75) terminal_symbol).getMACRO_NAME());
	else
	    return "<???>";
    }

    public String producesImage(Iproduces produces) {
	if (produces instanceof produces103)
	    return symbolImage(((produces103) produces).getEQUIVALENCE());
	else if (produces instanceof produces104)
	    return symbolImage(((produces103) produces).getEQUIVALENCE());
	else if (produces instanceof produces105)
	    return symbolImage(((produces103) produces).getEQUIVALENCE());
	else if (produces instanceof produces106)
	    return symbolImage(((produces103) produces).getEQUIVALENCE());
	else
	    return "<???>";
    }

    public String nameImage(Iname name) {
	if (name instanceof name119)
	    return symbolImage(((name119) name).getSYMBOL());
	else if (name instanceof name120)
	    return symbolImage(((name120) name).getMACRO_NAME());
	else if (name instanceof name121)
	    return "$empty";
	else if (name instanceof name122)
	    return "$error";
	else if (name instanceof name123)
	    return "$eol";
	else if (name instanceof name124)
	    return symbolImage(((name124) name).getIDENTIFIER_KEY());
	else
	    return "<???>";
    }

    private String symbolImage(TSYMBOL symbol) {
	return symbolImage(symbol.getLeftToken());
    }

    public TreeItem createTopItem(String label) {
	return createTopItem(label, null);
    }

    public TreeItem createTopItem(String label, ASTNode n) {
	TreeItem treeItem= new TreeItem(fTree, SWT.NONE);
	treeItem.setText(label);
	treeItem.setImage(JavaPluginImages.DESC_MISC_PUBLIC.createImage());
	if (n != null)
	    treeItem.setData(n);
	return treeItem;
    }

    public TreeItem createSubItem(String label) {
        return createSubItem(label, null);
    }

    public TreeItem createSubItem(String label, ASTNode n) {
	TreeItem treeItem= new TreeItem((TreeItem) fItemStack.peek(), SWT.NONE);
	treeItem.setText(label);
        if (n != null)
            treeItem.setData(n);
	treeItem.setImage(JavaPluginImages.DESC_MISC_PUBLIC.createImage());
	return treeItem;
    }

    public void createOutlinePresentation(IParseController controller, int offset) {
	fController= controller;
	try {
	    if (controller != null && fTree != null) {
		if (controller.hasErrors() && false) {
		    createDebugContents(controller);
		} else {
		    JikesPG jpg= (JikesPG) controller.getCurrentAst();

		    if (jpg != null) {
			fTree.setRedraw(false);
			fTree.removeAll();
			fItemStack.clear();
			jpg.accept(new OutlineVisitor());
		    }
		}
//		fTree.setSelection(new TreeItem[] { fTree.getItem(new Point(0, 0)) });
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
	createTopItem(MESSAGE);
	List errors= controller.getErrors();
	createTopItem("Found " + errors.size() + " syntax error(s) in input: ");
	for(Iterator error= errors.iterator(); error.hasNext(); ) {
	    ParseError pe= (ParseError) error.next();
	    createTopItem("  " + pe.description);
	}
	int count= controller.getParser().getParseStream().getSize();
	if (count > 1) {
	createTopItem("Tokens:");
	for(int n= 1; n < 100 && n < count; n++) {
	    IToken token= controller.getParser().getParseStream().getTokenAt(n);
	    String label= n + ": "
		    + controller.getParser().getParseStream().orderedTerminalSymbols()[token.getKind()] + " = "
		    + token.getValue(controller.getLexer().getLexStream().getInputChars());
	    createTopItem(label);
	}
	if (count >= 100)
	    createTopItem("rest of outline truncated...");
	}
    }

    public void setEditor(UniversalEditor editor) {}

    public void setTree(Tree tree) {
	this.fTree= tree;
	this.fTree.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		TreeItem ti= (TreeItem) e.item;
		Object data= ti.getData();

		if (data instanceof ASTNode) {
		    ASTNode node= (ASTNode) ti.getData();

		    PrsStream s= fController.getParser().getParseStream();
		    IToken token = s.getTokenAt(node.getLeftToken());

		    IEditorPart activeEditor= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		    AbstractTextEditor textEditor= (AbstractTextEditor) activeEditor;

		    textEditor.selectAndReveal(token.getStartOffset(), token.getEndOffset()-token.getStartOffset()+1);
//		    textEditor.setFocus();
		}
	    }
	    public void widgetDefaultSelected(SelectionEvent e) { }
	});
    }
}
