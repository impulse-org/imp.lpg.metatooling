package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.Stack;
import lpg.lpgjavaruntime.IToken;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.uide.core.ErrorHandler;
import org.eclipse.uide.defaults.DefaultOutliner;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.parser.IParseController;

//import jikesPG.safari.parser.JikesPGParser;
import org.jikespg.uide.IJikesPGResources;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.parser.JikesPGParser.*;

public class Outliner extends DefaultOutliner
{
	// The stack of ancestor items at the current point in
	// the traversal of the outline tree ("who's your daddy?")
    private Stack fItemStack = new Stack();

	/*
	 * A visitor for ASTs.  Its purpose is to create outline-tree items
	 * for AST node types that are to appear in the outline view.
	 */
    private final class OutlineVisitor extends AbstractVisitor {

		public void unimplementedVisitor(String s) {
			// Sometimes useful for debugging
			//System.out.println(s);
		}

		private StringBuffer fRHSLabel;
		
		// SMS 27 Jun 2006
		// This creates a "Options" item regardless of whether
		// there actually are options (so try not doing that)
	    public boolean visit(JikesPG n) {
	        //fItemStack.push(createTopItem("Options", n));
	        return true;
	    }
	    public void endVisit(JikesPG n) {
	        //fItemStack.pop();
	    }


	    // SMS 27 Jun 2006
	    // Options_segments occur only where there are options
	    // lines in the source, so use these as the basis of
	    // creating "Options" items.  These will be created
	    // one per line rather than one per file (but then,
	    // that's what the file actually says).
	    public boolean visit(option_specList n) {
	   		fItemStack.push(createTopItem("Options", n));
	    	return true;
	    }
	    public void endVisit(option_specList n) {
	        fItemStack.pop();
	    }
	    
	    
		public boolean visit(option n) {
			
		    Ioption_value value= n.getoption_value();

	            if (value != null) {
	                if (value instanceof option_value0)
	                    createSubItem(symbolImage(n.getSYMBOL()) + " = " + symbolImage(((option_value0) value).getSYMBOL()), n);
	                else if (value instanceof option_value1)
	                    createSubItem(symbolImage(n.getSYMBOL()) + " = " + symbolListImage(((option_value1) value).getsymbol_list()), n);
	            } else
			createSubItem(symbolImage(n.getSYMBOL()), n);
	            return true;
		}

		
	    public boolean visit(import_segment n) {
	        fItemStack.push(createTopItem("Import", n));
	        return false;
	    }
	    public void endVisit(import_segment n) {
	        fItemStack.pop();
	    }
	    public boolean visit(NoticeSeg n) {
	        fItemStack.push(createTopItem("Notice", n));
	        return false;
	    }
	    public void endVisit(NoticeSeg n) {
	        fItemStack.pop();
	    }
		public boolean visit(AliasSeg n) {
		    fItemStack.push(createTopItem("Alias", n));
		    return true;
		}
		public void endVisit(AliasSeg n) {
		    fItemStack.pop();
		}
		public boolean visit(DefineSeg n) {
		    fItemStack.push(createTopItem("Define", n));
		    return true;
		}
		public void endVisit(DefineSeg n) {
		    fItemStack.pop();
		}
	        public boolean visit(defineSpec n) {
	            createSubItem(symbolImage(n.getmacro_name_symbol()), (ASTNode) n.getmacro_name_symbol());
	            return true;
	        }
	        public boolean visit(EofSeg n) {
	            fItemStack.push(createTopItem("EOF", n));
	            return true;
	        }
	        public void endVisit(EofSeg n) {
	            fItemStack.pop();
	        }
	        public boolean visit(ExportSeg n) {
	            fItemStack.push(createTopItem("Export", n));
	            return true;
	        }
	        public void endVisit(ExportSeg n) {
	            fItemStack.pop();
	        }
	        public boolean visit(terminal_symbol1 n) {
	            createSubItem(n.getMACRO_NAME().toString(), (ASTNode) n.getMACRO_NAME());
	            return false;
	        }
	        public boolean visit(GlobalsSeg n) {
	            fItemStack.push(createTopItem("Globals", n));
	            return true;
	        }
	        public void endVisit(GlobalsSeg n) {
	            fItemStack.pop();
	        }
		public boolean visit(HeadersSeg n) {
		    fItemStack.push(createTopItem("Headers", n));
		    return true;
		}
		public void endVisit(HeadersSeg n) {
		    fItemStack.pop();
		}
		
		// SMS 27 Jun 2006
		// Trailers were omitted originally but they can occur
		public boolean visit(TrailersSeg n) {
		    fItemStack.push(createTopItem("Trailers", n));
		    return true;
		}
		public void endVisit(TrailersSeg n) {
		    fItemStack.pop();
		}
		
	        public void endVisit(include_segment n) {
	            createTopItem("Include " + n.getSYMBOL(), n);
	        }
		public boolean visit(IdentifierSeg n) {
		    fItemStack.push(createTopItem("Identifiers", n));
		    return true;
		}
		public void endVisit(IdentifierSeg n) {
		    fItemStack.pop();
		}
		public boolean visit(KeywordsSeg n) {
		    fItemStack.push(createTopItem("Keywords", n));
		    return true;
		}
		public void endVisit(KeywordsSeg n) {
		    fItemStack.pop();
		}
	        public void endVisit(start_symbol0 n) {
	            createTopItem("Start = " + symbolImage(n), n);
	        }
		public void endVisit(start_symbol1 n) {
	            createTopItem("Start = " + symbolImage(n), n);
		}
		public boolean visit(TerminalsSeg n) {
		    fItemStack.push(createTopItem("Terminals", n));
		    return true;
		}
		public void endVisit(TerminalsSeg n) {
		    fItemStack.pop();
		}
		public void endVisit(terminal n) {
		    Iterminal_symbol symbol= n.getterminal_symbol();
		    optTerminalAlias alias= n.getoptTerminalAlias();
		    String label;
		    if (alias != null) {
			Iproduces prod= alias.getproduces();
			Iname name= alias.getname();
			label= nameImage(name) + " " + producesImage(prod) + " " + symbolImage(symbol);
		    } else
			label= symbolImage(symbol);
	            createSubItem(label, (ASTNode) symbol);
		}
		public boolean visit(AstSeg n) {
		    fItemStack.push(createTopItem("Ast", n));
		    return true;
		}
		public void endVisit(AstSeg n) {
		    fItemStack.pop();
		}
		public boolean visit(RulesSeg n) {
		    fItemStack.push(createTopItem("Rules", n));
		    return true;
		}
		public void endVisit(RulesSeg n) {
		    fItemStack.pop();
		}
		public boolean visit(nonTerm n) {
		    if (n.getrhsList().size() > 1)
			fItemStack.push(createSubItem(symbolImage(n.getSYMBOL()), n));
		    return true;
		}
	        public void endVisit(nonTerm n) {
		    if (n.getrhsList().size() > 1)
			fItemStack.pop();
	        }
	        public boolean visit(rhs n) {
	            fRHSLabel = new StringBuffer();
	            final nonTerm parentNonTerm= (nonTerm) n.getParent().getParent();
		    if (parentNonTerm.getrhsList().size() == 1) {
			fRHSLabel.append(parentNonTerm.getSYMBOL());
			fRHSLabel.append(" ::= ");
		    }
	            return true;
	        }
	        public void endVisit(rhs n) {
	            createSubItem(fRHSLabel.toString(), n);
	        }
	        public void endVisit(symWithAttrs0 n) {
	            fRHSLabel.append(' ');
	            fRHSLabel.append(n.getIToken().toString());
	        }
	        public void endVisit(symWithAttrs1 n) {
	            fRHSLabel.append(' ');
	            fRHSLabel.append(n.getIToken().toString());
	        }
	        public void endVisit(symWithAttrs2 n) {
	            fRHSLabel.append(' ');
	            fRHSLabel.append(n.getSYMBOL().getIToken().toString());
	        }
	        public boolean visit(TypesSeg n) {
	            fItemStack.push(createTopItem("Types", n));
	            return true;
	        }
	        public void endVisit(TypesSeg n) {
	            fItemStack.pop();
	        }
//	        public boolean visit(types_segment1 n) {
//	            return true;
//	        }
	        public boolean visit(type_declarations n) {
	            fItemStack.push(createSubItem(symbolImage(n.getSYMBOL()), n));
	            return true;
	        }
	        public void endVisit(type_declarations n) {
	            fItemStack.pop();
	        }

    }	// End OutlineVisitor



    // FIXME Should dispose() this image at some point, no?
    private static final Image sTreeItemImage= JikesPGPlugin.getInstance().getImageRegistry().get(IJikesPGResources.OUTLINE_ITEM);

    public String producesImage(Iproduces produces) {
	if (produces instanceof produces0)
	    return ((produces0) produces).getLeftIToken().toString();
	else if (produces instanceof produces1)
	    return ((produces1) produces).getLeftIToken().toString();
	else if (produces instanceof produces2)
	    return ((produces2) produces).getLeftIToken().toString();
	else if (produces instanceof produces3)
	    return ((produces3) produces).getLeftIToken().toString();
	else
	    return "<???>";
    }

    public String nameImage(Iname name) {
	if (name instanceof name0)
	    return ((name0) name).getLeftIToken().toString();
	else if (name instanceof name1)
	    return ((name1) name).getLeftIToken().toString();
	else if (name instanceof name2)
	    return "$empty";
	else if (name instanceof name3)
	    return "$error";
	else if (name instanceof name4)
	    return "$eol";
	else if (name instanceof name5)
	    return ((name5) name).getLeftIToken().toString();
	else
	    return "<???>";
    }

    private String symbolImage(IASTNodeToken symbol) {
	return symbol.getLeftIToken().toString();
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
        return block.getLeftIToken().toString();
    }
    

    
    
    public TreeItem createTopItem(String label) {
		return createTopItem(label, null);
    }

    public TreeItem createTopItem(String label, ASTNode n) {
		TreeItem treeItem= new TreeItem(tree, SWT.NONE);
		treeItem.setText(label);
		//treeItem.setImage(JikesPGImages.OUTLINE_ITEM_IMAGE);
		treeItem.setImage(sTreeItemImage);
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
		//treeItem.setImage(JikesPGImages.OUTLINE_ITEM_IMAGE);
		treeItem.setImage(sTreeItemImage);
		return treeItem;
    }


   
    /*
     * To hold in a generic way any data that the method "significantChange(..)"
     * may require from one invocation to the next.
     */
    private Object[] previous = null;
    
    /**
     * Report whether there has been a significant change in the AST
     * associated with the parse controller given in the current
     * invocation compared to the AST associated with the parse
     * controller given on the previous invocation (if any).
     * 
     * A significant change is considered to be one in which the AST
     * is not logically the same from one invocation to the next,
     * as indicated by a change in the parse controller (implying an
     * entirely new tree), in the number of tokens in the tree, or in
     * the text making up any individual token.
     * 
     * Replace or override this method if a different algorithm for
     * detecting significant changes is desired.
     * 
     * @param controller	A parse controller that is to be compared to
     * 						the previously given parse controller, especially
     * 						for changes in their respective ASTs
     * @return				True if the AST for the current controller is
     * 						effectively the same as the AST for the previous
     * 						controller or if both are null; false otherwise
     */
    public boolean significantChange(IParseController controller)
    {
    	boolean previousWasNull = previous == null;
    	boolean result = false;
    	
    	// Check for previous values being null (as in uninitialized)
    	if (previousWasNull) {
    		// create and initialize previous
    		previous = new Object[3];
    		for (int i = 0; i < previous.length; i++) {
    			previous[i] = null;
    		}
    		
    		// check for current and previous controllers both null	
    		if (controller == null) {
    			return false;
    		}
    	}
    	
    	// If here then had some previous values (although these
    	// could individually be null); is current controller null?
    	if (controller == null) {
    		for (int i = 0; i < previous.length; i++) {
    			if (previous[i] == null) continue;	// not changed
    			result = true;						// changed
    			previous[i] = null;					// null now
    		}
    		return result;
    	}
    	
    	// If here then had some previous values and have some current
    	// values; these need to be compared
    	// (for simplicity assume that current values are not null)
    	
    	// Get current values for comparison to previous
    	ArrayList tokens = controller.getParser().getParseStream().getTokens();
    	char[] chars = controller.getLexer().getLexStream().getInputChars();
    	
    	// Get previous values for comparison to current
    	IParseController previousController = (IParseController) previous[0];
    	ArrayList previousTokens = (ArrayList) previous[1];
    	char[] previousChars = (char[]) previous[2];
    	
    	// Update previous values to current values in any case (now that
    	// we've saved previous in local fields)
		previous[0] = controller;	
		previous[1] = tokens;
		previous[2] = chars;
    	
    	// Compare current and previous values; return true if different
		
		// Are the whole trees different?  (Assume so if controllers differ)
    	if (previousController != controller) return true;
    	
    	// Are the sizes of the trees different? 
    	if (previousTokens.size() != tokens.size()) {
    		return true;
    	}
    	
    	// Are any of the individual tokens different?
    	for (int i = 0; i < previousTokens.size()-1; i++) {
    		IToken previousToken = (IToken)previousTokens.get(i);
    		IToken token = (IToken)tokens.get(i);
    		if (previousToken.getKind() != token.getKind()) {
    			//System.out.println("Previous and current tokens differ at token # = " + i);
    			return true;
    		}
    		int previousStart = previousToken.getStartOffset();
    		int previousEnd = previousToken.getEndOffset();
    		int start = token.getStartOffset();
    		int end = token.getEndOffset();
    		if ((previousEnd - previousStart) != (end - start)) {
				System.out.println("Previous and current tokens have different extents at token # = " + i);
				return true;
    		}
    		for (int j = 0; j < (previousEnd - previousStart + 1); j++) {
    			if (previousChars[previousStart+j] != chars[start+j]) {
    				System.out.println("Previous and current tokens have different characters at token # = " + i +
    						", character # = " + j);
    				return true;
    			}
    		}
    	}
    	
    	// No significant differences found
    	return false;
    }
    
    
    /**
     * Create the outline tree representing the AST associated with a given
     * parse controller.  There are two issues of particular concern in the
     * design of this method:  observing the appropriate UI protocol for
     * redrawing the tree, and filtering out unwanted invocations that
     * reflect events that should probably not trigger a redrawing of the tree.
     * 
     * Regarding the protocol for redrawing the tree, it seems that calls to
     * tree.setRedraw(true) must be balanced by calls to tree.setRedraw(false).
     * This is because of logic in org.eclipse.swt.widgets.Control, the parent
     * class of org.eclipse.swt.widgets.TreeItem.  If there are excessive calls
     * to tree.setRedraw(true) then the outline view can become "stuck" on a
     * particular file; that is, when a new editor is opened or brought to
     * the foreground, the outline view continues to show the outline for a
     * previous file.  The implementation provided here generally assures,
     * insofar as possible, that the calls to tree.setRedraw(true) and to
     * tree.setRedraw(false) are balanced, even in the face of exceptions.
     * If the logic of this method is modified, care should be taken to assure
     * that this balance is maintained.
     * 
     * Regarding unwanted invocations, this method may be called even when
     * the AST represented has not changed and thus when redrawing of
     * the outline may be unnecessary.  Unnecessary redrawing of the outline
     * is probably a negligable inefficiency in most cases, but redrawing has
     * the effect of collapsing an expanded outline, which can be annoying
     * for users.  Folding of the source text in an editor is one case in which
     * this method is called when there is no change to the underlying AST.
     * The implementation provided here includes a test for "significantChange"
     * and avoids redrawing the outline when no significant change has occurred.
     * The provided version of significantChange(..) tracks changes to the
     * whole tree, the number of tokens, and the text associated with individual
     * tokens.  Users who want to use an alternative definition of "significant
     * change" should rewrite or override the provided method.
     * 
     * @param	controller	A parse controller from which an AST can be obtained
     * 						to serve as the basis of an outline presentation
     * 	
     * @param	offset		Currently ignored; kept for backward compatibility
     * 
     * @return	void		Has no return but has the effect of (re)drawing
     * 						an outline tree based on the AST obtained from the
     * 						given parse controller, if possible.
     * 
     * 						Has no effect under a variety of problematic
     * 						conditions:  given parse controller is null, outline
     * 						tree is null, parse controller has errors, or parse
     * 						controller's current AST is null.  Also has no
     * 						effect if there is no "significant change" from the
     * 						the AST on the previous invocation to the AST on this
     * 						invocation.
     * 
     * @exception			Throws none; traps all exceptions and reports an
     * 						error.
     */
    public void createOutlinePresentation(IParseController controller, int offset)
    {
    	if (controller == null || tree == null) return;
    	if (controller.hasErrors()) return;
    	if (!significantChange(controller)) return;
    	
    	boolean redrawSetFalse = false;
		try {
			ASTNode root= (ASTNode) controller.getCurrentAst();
			if (root == null) return;
						
			tree.setRedraw(false);
			redrawSetFalse = true;
			tree.removeAll();
			fItemStack.clear();
			root.accept(new OutlineVisitor());
			
		} catch (Throwable e) {
		    ErrorHandler.reportError("Exception generating outlinel", e);
		} finally {
		    if (redrawSetFalse) {
		    	tree.setRedraw(true);
			}
		}
    }
 
 

    public void setEditor(UniversalEditor editor) {
        System.out.println(editor.getTitle());
        super.setEditor(editor);
    }

    public void setTree(Tree tree) {
        super.setTree(tree);
	this.tree.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		TreeItem ti= (TreeItem) e.item;
		Object data= ti.getData();

		if (data instanceof ASTNode) {
		    ASTNode node= (ASTNode) ti.getData();
		    IToken token= node.getLeftIToken();

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
