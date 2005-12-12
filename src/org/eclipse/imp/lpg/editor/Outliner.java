/*
 * Created on Oct 31, 2005
 */
package org.jikespg.uide.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.uide.core.ErrorHandler;
import org.eclipse.uide.defaults.DefaultOutliner;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.parser.IParseController;
import org.eclipse.uide.parser.ParseError;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.ASTNodeToken;
import org.jikespg.uide.parser.JikesPGParser.AliasSeg;
import org.jikespg.uide.parser.JikesPGParser.DefineSeg;
import org.jikespg.uide.parser.JikesPGParser.ExportSeg;
import org.jikespg.uide.parser.JikesPGParser.GlobalsSeg;
import org.jikespg.uide.parser.JikesPGParser.HeadersSeg;
import org.jikespg.uide.parser.JikesPGParser.IASTNodeToken;
import org.jikespg.uide.parser.JikesPGParser.IdentifierSeg;
import org.jikespg.uide.parser.JikesPGParser.Iname;
import org.jikespg.uide.parser.JikesPGParser.IncludeSeg;
import org.jikespg.uide.parser.JikesPGParser.Ioption_value;
import org.jikespg.uide.parser.JikesPGParser.Iproduces;
import org.jikespg.uide.parser.JikesPGParser.Isymbol_list;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.JikesPG_INPUT;
import org.jikespg.uide.parser.JikesPGParser.RulesSeg;
import org.jikespg.uide.parser.JikesPGParser.SYMBOLList;
import org.jikespg.uide.parser.JikesPGParser.StartSeg;
import org.jikespg.uide.parser.JikesPGParser.TerminalsSeg;
import org.jikespg.uide.parser.JikesPGParser.TitleSeg;
import org.jikespg.uide.parser.JikesPGParser.action_segment;
import org.jikespg.uide.parser.JikesPGParser.define_segment44;
import org.jikespg.uide.parser.JikesPGParser.export_segment51;
import org.jikespg.uide.parser.JikesPGParser.export_segment52;
import org.jikespg.uide.parser.JikesPGParser.globals_segment37;
import org.jikespg.uide.parser.JikesPGParser.globals_segment38;
import org.jikespg.uide.parser.JikesPGParser.include_segment39;
import org.jikespg.uide.parser.JikesPGParser.include_segment40;
import org.jikespg.uide.parser.JikesPGParser.name125;
import org.jikespg.uide.parser.JikesPGParser.name126;
import org.jikespg.uide.parser.JikesPGParser.name127;
import org.jikespg.uide.parser.JikesPGParser.name128;
import org.jikespg.uide.parser.JikesPGParser.name129;
import org.jikespg.uide.parser.JikesPGParser.name130;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.nonTermList;
import org.jikespg.uide.parser.JikesPGParser.option;
import org.jikespg.uide.parser.JikesPGParser.option_list;
import org.jikespg.uide.parser.JikesPGParser.option_spec;
import org.jikespg.uide.parser.JikesPGParser.option_value31;
import org.jikespg.uide.parser.JikesPGParser.option_value32;
import org.jikespg.uide.parser.JikesPGParser.options_segment;
import org.jikespg.uide.parser.JikesPGParser.produces109;
import org.jikespg.uide.parser.JikesPGParser.produces110;
import org.jikespg.uide.parser.JikesPGParser.produces111;
import org.jikespg.uide.parser.JikesPGParser.produces112;
import org.jikespg.uide.parser.JikesPGParser.rules_segment;
import org.jikespg.uide.parser.JikesPGParser.start_segment;
import org.jikespg.uide.parser.JikesPGParser.start_symbol100;
import org.jikespg.uide.parser.JikesPGParser.start_symbol99;
import org.jikespg.uide.parser.JikesPGParser.terminal_symbol77;
import org.jikespg.uide.parser.JikesPGParser.terminal_symbol78;
import org.jikespg.uide.parser.JikesPGParser.terminals_segment48;
import org.jikespg.uide.parser.JikesPGParser.terminals_segment49;
import org.jikespg.uide.parser.JikesPGParser.terminals_segment50;
import org.jikespg.uide.parser.JikesPGParser.title_segment35;
import org.jikespg.uide.parser.JikesPGParser.title_segment36;

import com.ibm.lpg.IToken;
import com.ibm.lpg.PrsStream;

public class Outliner extends DefaultOutliner {
    Stack fItemStack= new Stack();

    private final class OutlineVisitor extends JikesPGParser.AbstractVisitor {
	public void unimplementedVisitorFor(String s) {
	    System.out.println(s);
	}

	public void visit(JikesPG n) {
	    fItemStack.push(createTopItem("Options", n));
	    if (n.getoptions_segment() != null)
		n.getoptions_segment().accept(this);
	    fItemStack.pop();
            if (n.getJikesPG_INPUT() != null)
                n.getJikesPG_INPUT().accept(this);
	}
	public void visit(options_segment n) {
	    if (n.getoptions_segment() != null)
		n.getoptions_segment().accept(this);
	    n.getoption_spec().accept(this);
	}
	public void visit(option_spec n) {
	    n.getoption_list().accept(this);
	}
	public void visit(option_list n) {
	    n.getoption_list().accept(this);
	    n.getoption().accept(this);
	}
	public void visit(option n) {
	    Ioption_value value= n.getoption_value();

            if (value != null) {
                if (value instanceof option_value31)
                    createSubItem(symbolImage(n.getSYMBOL()) + " = " + symbolImage(((option_value31) value).getSYMBOL()), n);
                else if (value instanceof option_value32)
                    createSubItem(symbolImage(n.getSYMBOL()) + " = " + symbolListImage(((option_value32) value).getsymbol_list()), n);
            } else
		createSubItem(symbolImage(n.getSYMBOL()), n);
	}
	public void visit(JikesPG_INPUT n) {
//	    fItemStack.push(createTreeItem("Grammar"));
	    if (n.getJikesPG_INPUT() != null)
		n.getJikesPG_INPUT().accept(this);
	    n.getJikesPG_item().accept(this);
//	    fItemStack.pop();
	}
	public void visit(AliasSeg n) {
	    fItemStack.push(createTopItem("Alias", n));
	    n.getalias_segment().accept(this);
	    fItemStack.pop();
	}
	public void visit(DefineSeg n) {
	    fItemStack.push(createTopItem("Define", n));
	    n.getdefine_segment().accept(this);
	    fItemStack.pop();
	}
        public void visit(define_segment44 n) {
            n.getmacro_segment();
            createSubItem(symbolImage(n.getmacro_name_symbol()), (ASTNode) n.getmacro_name_symbol());
        }
        public void visit(ExportSeg n) {
            fItemStack.push(createTopItem("Export", n));
            n.getexport_segment().accept(this);
            fItemStack.pop();
        }
        public void visit(export_segment52 n) {
            if (n.getexport_segment() != null)
                n.getexport_segment().accept(this);
            createSubItem(symbolImage(n.getterminal_symbol()), (ASTNode) n.getterminal_symbol());
        }
        public void visit(GlobalsSeg n) {
            fItemStack.push(createTopItem("Globals", n));
            if (n.getglobals_segment() != null)
                n.getglobals_segment().accept(this);
            fItemStack.pop();
        }
        public void visit(globals_segment38 n) {
            if (n.getglobals_segment() != null)
                n.getglobals_segment().accept(this);
            createSubItem(blockImage(n.getaction_segment()), n.getaction_segment());
        }
	public void visit(HeadersSeg n) {
	    fItemStack.push(createTopItem("Headers", n));
	    n.getheaders_segment().accept(this);
	    fItemStack.pop();
	}
        public void visit(IncludeSeg n) {
            n.getinclude_segment().accept(this);
        }
        public void visit(include_segment40 n) {
            createTopItem("Include " + symbolImage(n.getSYMBOL()), n.getSYMBOL());
        }
	public void visit(IdentifierSeg n) {
	    fItemStack.push(createTopItem("Identifiers", n));
	    n.getidentifier_segment().accept(this);
	    fItemStack.pop();
	}
	public void visit(StartSeg n) {
	    n.getstart_segment().accept(this);
	}
	public void visit(start_segment n) {
	    n.getstart_symbol().accept(this);
//	    createTreeItem(symbolImage());
	}
        public void visit(start_symbol99 n) {
            createTopItem("Start = " + symbolImage(n), n);
        }
	public void visit(start_symbol100 n) {
            createTopItem("Start = " + symbolImage(n), n);
	}
	public void visit(TerminalsSeg n) {
	    fItemStack.push(createTopItem("Terminals", n));
	    n.getterminals_segment().accept(this);
	    fItemStack.pop();
	}
	public void visit(terminals_segment48 n) {
//	    createSubItem(symbolImage(n.getTERMINALS_KEY()), n);
	}
	public void visit(terminals_segment49 n) {
	    n.getterminals_segment().accept(this);
	    n.getterminal_symbol().accept(this);
	}
	public void visit(terminals_segment50 n) {
	    n.getterminals_segment().accept(this);
	    String label= nameImage(n.getname()) + " " + producesImage(n.getproduces()) + " " + symbolImage(n.getterminal_symbol());
            createSubItem(label, (ASTNode) n.getterminal_symbol());
	}
	public void visit(terminal_symbol77 n) {
	    createSubItem(symbolImage(n), n);
	}
	public void visit(terminal_symbol78 n) {
	    createSubItem(symbolImage(n.getLeftToken()), n);
	}
	public void visit(TitleSeg n) {
	    fItemStack.push(createTopItem("Title", n));
	    n.gettitle_segment().accept(this);
	    fItemStack.pop();
	}
	public void visit(title_segment35 n) {
//	    createSubItem(symbolImage(n.getTITLE_KEY()), n);
	}
	public void visit(title_segment36 n) {
//	    createSubItem(symbolImage(n.getTITLE_KEY().getLeftToken()), n);
            createSubItem(blockImage(((action_segment) n.getaction_segment()).getLeftToken()), n);
	}
	public void visit(RulesSeg n) {
	    fItemStack.push(createTopItem("Rules", n));
	    n.getrules_segment().accept(this);
	    fItemStack.pop();
	}
        public void visit(rules_segment n) {
            if (n.getaction_segment_list() != null)
                n.getaction_segment_list().accept(this);
            n.getnonTermList().accept(this);
        }
        public void visit(nonTermList n) {
            for(int i=0; i < n.size(); i++)
                n.getnonTermAt(i).accept(this);
        }
        public void visit(nonTerm n) {
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

//    public String symbolImage(Iterminal_symbol terminal_symbol) {
//	if (terminal_symbol instanceof terminal_symbol74)
//	    return symbolImage(((terminal_symbol74) terminal_symbol).getLeftToken());
//	else if (terminal_symbol instanceof terminal_symbol75)
//	    return symbolImage(((terminal_symbol75) terminal_symbol).getLeftToken());
//	else
//	    return "<???>";
//    }

    public String producesImage(Iproduces produces) {
	if (produces instanceof produces109)
	    return symbolImage(((produces109) produces).getLeftToken());
	else if (produces instanceof produces110)
	    return symbolImage(((produces110) produces).getLeftToken());
	else if (produces instanceof produces111)
	    return symbolImage(((produces111) produces).getLeftToken());
	else if (produces instanceof produces112)
	    return symbolImage(((produces112) produces).getLeftToken());
	else
	    return "<???>";
    }

    public String nameImage(Iname name) {
	if (name instanceof name125)
	    return symbolImage(((name125) name).getLeftToken());
	else if (name instanceof name126)
	    return symbolImage(((name126) name).getLeftToken());
	else if (name instanceof name127)
	    return "$empty";
	else if (name instanceof name128)
	    return "$error";
	else if (name instanceof name129)
	    return "$eol";
	else if (name instanceof name130)
	    return symbolImage(((name130) name).getLeftToken());
	else
	    return "<???>";
    }

    private String symbolImage(IASTNodeToken symbol) {
	return symbolImage(symbol.getLeftToken());
    }

    private String symbolListImage(Isymbol_list symbols) {
        SYMBOLList symbolList= (SYMBOLList) symbols;
        StringBuffer buff= new StringBuffer();
        buff.append('(');
        for(int i=0; i < symbolList.size(); i++) {
            if (i > 0) buff.append(',');
            buff.append(symbolImage(symbolList.getSYMBOLAt(i)));
        }
        buff.append(')');
        return buff.toString();
    }

    private String blockImage(ASTNodeToken block) {
        return blockImage(block.getLeftToken());
    }

    private String blockImage(int blockToken) {
        String rep= fController.getParser().getParseStream().getTokenText(blockToken);

        return rep;
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
