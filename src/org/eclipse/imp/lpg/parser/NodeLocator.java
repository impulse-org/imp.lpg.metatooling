package org.jikespg.uide.parser;

import lpg.lpgjavaruntime.IToken;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;

public class NodeLocator implements IASTNodeLocator {
    private ASTNode fResult= null;

    public NodeLocator(ParseController controller) {
    }

    public Object findNode(Object ast, int offset) {
        ASTNode root= (ASTNode) ast;

        if (root == null)
            return null;

        root.accept(new LocatingVisitor(offset));
        return fResult;
    }

    public Object findNode(Object ast, int startOffset, int endOffset) {
        ASTNode root= (ASTNode) ast;

        root.accept(new LocatingVisitor(startOffset, endOffset));
        return fResult;
    }

    private class LocatingVisitor extends JikesPGParser.AbstractVisitor {
        private final int fStartOffset;
        private final int fEndOffset;

        public LocatingVisitor(int offset) {
            this(offset, offset);
        }

        public LocatingVisitor(int startOffset, int endOffset) {
            fStartOffset= startOffset;
            fEndOffset= endOffset;
        }

        public ASTNode getResult() { return fResult; }

        public void unimplementedVisitor(String s) {
//            System.out.println(s);
        }

        public void postVisit(ASTNode n) {
            // Use postVisit() so that we find the innermost AST node that contains the given text
            // range (innermost node's postVisit() will be the first postVisit() to be called).
            if (fResult == null) {
        	IToken symTok= n.getLeftIToken();
        	// Consider position just to right of end of token part of the token
        	if (fStartOffset >= symTok.getStartOffset() && fEndOffset <= symTok.getEndOffset()+1)
        	    fResult= n;
            }
        }
    }
    
    
    // SMS 14 Jun 2006
    // Added to address change in IASTNodeLocator
    
    public int getStartOffset(Object node) {
    	ASTNode n = (ASTNode) node;
    	return n.getLeftIToken().getStartOffset();
    }
    
    
    public int getEndOffset(Object node) {
    	ASTNode n = (ASTNode) node;
    	return n.getRightIToken().getEndOffset();
    }
    
    
    public int getLength(Object  node) {
    	ASTNode n = (ASTNode) node;
    	return getEndOffset(n) - getStartOffset(n);
    }
    
}
