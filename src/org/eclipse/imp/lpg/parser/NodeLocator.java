package org.jikespg.uide.parser;

import org.eclipse.uide.parser.IASTNodeLocator;
import org.jikespg.uide.parser.JikesPGParser.*;

import com.ibm.lpg.IToken;
import com.ibm.lpg.PrsStream;

public class NodeLocator implements IASTNodeLocator {
    private final ParseController fController;

    public NodeLocator(ParseController controller) {
        fController= controller;
    }

    public Object findNode(Object ast, int offset) {
        ASTNode root= (ASTNode) ast;

        return root.accept(new LocatingVisitor(offset));
    }

    public Object findNode(Object ast, int startOffset, int endOffset) {
        ASTNode root= (ASTNode) ast;

        return root.accept(new LocatingVisitor(startOffset, endOffset));
    }

    private class LocatingVisitor extends JikesPGParser.AbstractResultVisitor {
        private final int fStartOffset;
        private final int fEndOffset;
        private final PrsStream fParseStream= fController.getParser().getParseStream();

        public LocatingVisitor(int offset) {
            this(offset, offset+1);
        }

        public LocatingVisitor(int startOffset, int endOffset) {
            fStartOffset= startOffset;
            fEndOffset= endOffset;
        }

        public Object unimplementedVisitor(String s) {
            System.out.println(s);
            return null;
        }

        public Object visit(JikesPG n) {
            if (n.getJikesPG_INPUT() != null)
                return n.getJikesPG_INPUT().accept(this);
            return null;
        }

        public Object visit(JikesPG_INPUT n) {
            Object o= null;
            if (n.getJikesPG_INPUT() != null)
                o= n.getJikesPG_INPUT().accept(this);
            if (o == null)
                o= n.getJikesPG_item().accept(this);
            return o;
        }

        public Object visit(HeadersSeg n) {
            return n.getheaders_segment().accept(this);
        }

        public Object visit(headers_segment0 n) {
            return null; // just the HEADERS_KEY
        }

        public Object visit(headers_segment1 n) {
            Object o= n.getheaders_segment().accept(this);
            if (o == null)
                o= n.getaction_segment().accept(this);
            return o;
        }

        public Object visit(TerminalsSeg n) {
            return n.getterminals_segment().accept(this);
        }

        public Object visit(terminals_segment1 n) {
            return null; // this is just for the TERMINALS_KEY
        }

        public Object visit(terminals_segment2 n) {
            Object o= n.getterminals_segment().accept(this);
            if (o == null)
                o= n.getterminal_symbol().accept(this);
            return o;
        }

        public Object visit(terminal_symbol1 n) {
            IToken symTok= fParseStream.getIToken(n.getLeftToken());
            if (fStartOffset >= symTok.getStartOffset() && fEndOffset <= symTok.getEndOffset())
                return n;
            return null;
        }

        public Object visit(RulesSeg n) {
            return n.getrules_segment().accept(this);
        }

        public Object visit(rules_segment n) {
            Object ret= null;
            if (n.getaction_segment_list() != null) {
                ret= n.getaction_segment_list().accept(this);
            }
            if (ret == null)
                ret= n.getnonTermList().accept(this);
            return ret;
        }
        public Object visit(nonTermList n) {
            for(int i= 0; i < n.size(); i++) {
                Object ret= n.getnonTermAt(i).accept(this);
                if (ret != null)
                    return ret;
            }
            return null;
        }
        public Object visit(nonTerm n) {
            Object ret= null;
            ret= n.getSYMBOL().accept(this);
            if (ret == null && n.getoptMacro() != null)
                ret= n.getoptMacro().accept(this);
            if (ret == null)
                ret= n.getproduces().accept(this);
            if (ret == null)
                ret= n.getrhsList().accept(this);
            return ret;
        }
        public Object visit(rhsSymbol n) {
            Object o= null;
            if (n.getrhs() != null)
                o= n.getrhs().accept(this);
            if (o == null)
                o= n.getSYMBOL().accept(this);
            return o;
        }
        public Object visit(rhsSymbolMacro n) {
            Object o= n.getSYMBOL().accept(this);
            if (o == null)
                o= n.getMACRO_NAME().accept(this);
            if (o == null)
                o= n.getrhs().accept(this);
            return o;
        }
        public Object visit(ASTNodeToken n) {
            int left= n.getLeftToken();
            int right= n.getRightToken();

            IToken lt= fParseStream.getTokenAt(left);
            IToken rt= fParseStream.getTokenAt(right);

            return (fStartOffset >= lt.getStartOffset() && fEndOffset <= rt.getEndOffset()+1) ? n : null;
        }
    }
}
