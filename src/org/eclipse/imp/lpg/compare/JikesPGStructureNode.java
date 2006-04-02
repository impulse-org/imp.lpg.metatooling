package org.jikespg.uide.compare;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DocumentRangeNode;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.jikespg.uide.parser.JikesPGParser.*;

public class JikesPGStructureNode extends DocumentRangeNode implements ITypedElement {
    private final static Object[] NO_CHILDREN= new Object[0];

    public final static int OPTIONS_TYPE= 0;

    public final static int BODY_TYPE= 1;

    public static final int ACTION_TYPE= 2;

    /**
     * A simple visitor that produces the immediate children of a given node.<br>
     * The implementation below actually omits certain uninteresting intermediate nodes
     * from the structure.
     */
    private static class GetChildrenVisitor extends AbstractVisitor implements Visitor {
        private Object[] fChildren= NO_CHILDREN;

        private final JikesPGStructureNode fStructureNode;

        public GetChildrenVisitor(JikesPGStructureNode node) {
            fStructureNode= node;
        }

        public void unimplementedVisitor(String s) { }

        public boolean visit(JikesPG n) {
            options_segment options= n.getoptions_segment();
            JikesPG_INPUT input= n.getJikesPG_INPUT();

            int count=1;
            for(JikesPG_INPUT in= input; in != null; in= in.getJikesPG_INPUT()) {
                count++;
            }
            fChildren= new Object[count];
            fChildren[0]= new JikesPGStructureNode(options, fStructureNode, OPTIONS_TYPE, "options");
            int i=1;
            for(JikesPG_INPUT in= input; in != null; in= in.getJikesPG_INPUT(), i++) {
                // HACK Shouldn't need the following cast, but IJikesPG_item doesn't derive from ASTNode...
                fChildren[i]= new JikesPGStructureNode((ASTNode) input.getJikesPG_item(), fStructureNode, BODY_TYPE, "item");
            }
            return false;
        }

        public boolean visit(GlobalsSeg n) {
            globals_segment1 globals= (globals_segment1) n.getglobals_segment();
            fChildren= new Object[] {
                    new JikesPGStructureNode(globals.getaction_segment(), fStructureNode, ACTION_TYPE, "action")
            };
            return false;
        }

        public boolean visit(DefineSeg n) {
            define_segment1 defSeg= (define_segment1) n.getdefine_segment();
            int count=1;
            for(define_segment1 ds= defSeg; ds != null && ds.getdefine_segment() instanceof define_segment1; ds= (define_segment1) ds.getdefine_segment()) {
                count++;
            }
            fChildren= new Object[count];
            fChildren[0]= defSeg;
            int i=1;
            for(define_segment1 ds= defSeg; ds != null && ds.getdefine_segment() instanceof define_segment1; ds= (define_segment1) ds.getdefine_segment(), i++) {
                fChildren[i]= new JikesPGStructureNode(ds, fStructureNode, ACTION_TYPE, "define");
            }
            return false;
        }

        public boolean visit(TerminalsSeg n) {
            terminalList termList= (terminalList) n.getterminals_segment();
            int count= termList.size();

            fChildren= new Object[count];
            for(int i=0; i < termList.size(); i++) {
                fChildren[i]= new JikesPGStructureNode(termList.getElementAt(i), fStructureNode, ACTION_TYPE, "terminal");
            }
            return false;
        }

        public boolean visit(RulesSeg n) {
            rules_segment rulesSegment= n.getrules_segment();
            nonTermList nonTermList= rulesSegment.getnonTermList();
            int count= nonTermList.size();

            fChildren= new Object[count];
            for(int i=0; i < nonTermList.size(); i++) {
                fChildren[i]= new JikesPGStructureNode(nonTermList.getElementAt(i), fStructureNode, ACTION_TYPE, "non-terminal");
            }
            return false;
        }

        public Object[] getChildren() {
            return fChildren;
        }
    }


    private ASTNode fASTNode;

    public JikesPGStructureNode(ASTNode root, IDocument document, int typeCode, String id) {
        super(typeCode, id, document, root.getLeftIToken().getStartOffset(), root.getRightIToken().getEndOffset() - root.getLeftIToken().getStartOffset());
        fASTNode= root;
    }

    public JikesPGStructureNode(ASTNode node, JikesPGStructureNode parent, int typeCode, String id) {
	this(node, parent.getDocument(), typeCode, id);
        fASTNode= node;
    }

    public ASTNode getASTNode() {
        return fASTNode;
    }

    public String getName() {
	return fASTNode.getClass().getName();
    }

    public Image getImage() {
	return null;
    }

    public String getType() {
	return "bar";
    }

    public Object[] getChildren() {
        GetChildrenVisitor v= new GetChildrenVisitor(this);

        fASTNode.accept(v);
        return v.getChildren();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JikesPGStructureNode))
            return false;

        JikesPGStructureNode other= (JikesPGStructureNode) obj;

        return this.fASTNode.equals(other.fASTNode);
    }
}
