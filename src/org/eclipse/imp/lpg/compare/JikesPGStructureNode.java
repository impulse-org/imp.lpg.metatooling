package org.jikespg.uide.compare;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DocumentRangeNode;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.AbstractVisitor;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.Visitor;

public class JikesPGStructureNode extends DocumentRangeNode implements ITypedElement {
    private final static Object[] NO_CHILDREN= new Object[0];

    private static class GetChildrenVisitor extends AbstractVisitor implements Visitor {
        private Object[] fChildren= NO_CHILDREN;

        private final JikesPGStructureNode fStructureNode;

        public GetChildrenVisitor(JikesPGStructureNode node) {
            fStructureNode= node;
        }

        public void unimplementedVisitor(String s) { }

        public boolean visit(JikesPG n) {
            fChildren= new Object[] {
                    new JikesPGStructureNode(n.getoptions_segment(), fStructureNode, 0, "hi", 0, 1),
                    new JikesPGStructureNode(n.getJikesPG_INPUT(), fStructureNode, 0, "bye", 0, 1) };
            return false;
        }

        public Object[] getChildren() {
            return fChildren;
        }
    }


    private ASTNode fASTNode;

    public JikesPGStructureNode(ASTNode root, IDocument document, int typeCode, String id, int start, int length) {
        super(typeCode, id, document, start, length);
        fASTNode= root;
    }

    public JikesPGStructureNode(ASTNode node, JikesPGStructureNode parent, int typeCode, String id, int start, int length) {
	super(typeCode, id, parent.getDocument(), start, length);
        fASTNode= node;
    }

    public ASTNode getASTNode() {
        return fASTNode;
    }

    public String getName() {
	return "foo";
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

        return this.fASTNode == other.fASTNode;
    }
}
