package org.jikespg.uide.parser;

import org.eclipse.uide.parser.IASTNodeLocator;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.HeadersSeg;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.JikesPG_INPUT;
import org.jikespg.uide.parser.JikesPGParser.RulesSeg;
import org.jikespg.uide.parser.JikesPGParser.TSYMBOL;
import org.jikespg.uide.parser.JikesPGParser.TerminalsSeg;
import org.jikespg.uide.parser.JikesPGParser.headers_segment92;
import org.jikespg.uide.parser.JikesPGParser.headers_segment93;
import org.jikespg.uide.parser.JikesPGParser.rhsSymbol;
import org.jikespg.uide.parser.JikesPGParser.rules100;
import org.jikespg.uide.parser.JikesPGParser.rules_segment98;
import org.jikespg.uide.parser.JikesPGParser.rules_segment99;
import org.jikespg.uide.parser.JikesPGParser.terminal_symbol74;
import org.jikespg.uide.parser.JikesPGParser.terminals_segment45;
import org.jikespg.uide.parser.JikesPGParser.terminals_segment46;

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

        return null;
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

        public Object unimplementedVisitorFor(String s) {
            System.out.println(s);
            return null;
        }

        public Object visitJikesPG(JikesPG n) {
            if (n.getJikesPG_INPUT() != null)
                return n.getJikesPG_INPUT().accept(this);
            return null;
        }

        public Object visitJikesPG_INPUT(JikesPG_INPUT n) {
            Object o= null;
            if (n.getJikesPG_INPUT() != null)
                o= n.getJikesPG_INPUT().accept(this);
            if (o == null)
                o= n.getJikesPG_item().accept(this);
            return o;
        }

        public Object visitHeadersSeg(HeadersSeg n) {
            return n.getheaders_segment().accept(this);
        }

        public Object visitheaders_segment92(headers_segment92 n) {
            return null; // just the HEADERS_KEY
        }

        public Object visitheaders_segment93(headers_segment93 n) {
            Object o= n.getheaders_segment().accept(this);
            if (o == null)
                o= n.getaction_segment().accept(this);
            return o;
        }

        public Object visitTerminalsSeg(TerminalsSeg n) {
            return n.getterminals_segment().accept(this);
        }

        public Object visitterminals_segment45(terminals_segment45 n) {
            return null; // this is just for the TERMINALS_KEY
        }

        public Object visitterminals_segment46(terminals_segment46 n) {
            Object o= n.getterminals_segment().accept(this);
            if (o == null)
                o= n.getterminal_symbol().accept(this);
            return o;
        }

        public Object visitterminal_symbol74(terminal_symbol74 n) {
            IToken symTok= fParseStream.getIToken(n.getSYMBOL());
            if (fStartOffset >= symTok.getStartOffset() && fEndOffset <= symTok.getEndOffset())
                return n;
            return null;
        }

        public Object visitRulesSeg(RulesSeg n) {
            return n.getrules_segment().accept(this);
        }

        public Object visitrules_segment98(rules_segment98 n) {
            if (n.getaction_segment_list() != null)
                return n.getaction_segment_list().accept(this);
            return null;
        }

        public Object visitrules_segment99(rules_segment99 n) {
            Object o= n.getrules_segment().accept(this);
            if (o == null)
                o= n.getrules().accept(this);
            return o;
        }
        public Object visitrules100(rules100 n) {
            Object o= n.getSYMBOL().accept(this);
            if (o == null)
                o= n.getrhs().accept(this);
            return o;
        }
        public Object visitrhsSymbol(rhsSymbol n) {
            Object o= null;
            if (n.getrhs() != null)
                o= n.getrhs().accept(this);
            if (o == null)
                o= n.getSYMBOL().accept(this);
            return o;
        }
        public Object visitTSYMBOL(TSYMBOL n) {
            int left= n.getLeftToken();
            int right= n.getRightToken();

            IToken lt= fParseStream.getTokenAt(left);
            IToken rt= fParseStream.getTokenAt(right);

            return (fStartOffset >= lt.getStartOffset() && fEndOffset <= rt.getEndOffset()) ? n : null;
        }
    }
}
