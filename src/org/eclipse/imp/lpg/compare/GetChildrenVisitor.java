package org.jikespg.uide.compare;

import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.*;

/**
 * A simple visitor that produces the immediate children of a given AST node, each
 * wrapped by a JikesPGStructureNode.<br>
 * The implementation below actually omits certain uninteresting intermediate nodes
 * from the structure.
 */
public class GetChildrenVisitor extends AbstractVisitor implements Visitor {
    private final static Object[] NO_CHILDREN= new Object[0];

    Object[] fChildren= NO_CHILDREN;

    private final JikesPGStructureNode fStructureNode;

    public GetChildrenVisitor(JikesPGStructureNode node) {
        fStructureNode= node;
    }

    public void unimplementedVisitor(String s) { }

    public boolean visit(JikesPG n) {
        options_segment options= n.getoptions_segment();
        JikesPG_itemList itemList= n.getJikesPG_INPUT();
        int count= 1 + itemList.size();

        fChildren= new Object[count];
        fChildren[0]= new JikesPGStructureNode(options, fStructureNode, JikesPGStructureNode.OPTION, "options");

        for(int i= 0; i < itemList.size(); i++) {
            IJikesPG_item item= itemList.getJikesPG_itemAt(i);

            fChildren[i+1]= new JikesPGStructureNode((ASTNode) item, fStructureNode, JikesPGStructureNode.BODY, item.getLeftIToken().toString());
        }
        return false;
    }

    public boolean visit(GlobalsSeg n) {
        globals_segment1 globals= (globals_segment1) n.getglobals_segment();
        fChildren= new Object[] {
                new JikesPGStructureNode(globals.getaction_segment(), fStructureNode, JikesPGStructureNode.GLOBAL, "global")
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
        int i=0;
        for(define_segment1 ds= defSeg; ds != null; ds= (define_segment1) ds.getdefine_segment(), i++) {
            fChildren[i]= new JikesPGStructureNode(ds, fStructureNode, JikesPGStructureNode.DEFINE, ds.getmacro_name_symbol().toString());
            if (!(ds.getdefine_segment() instanceof define_segment1))
                break;
        }
        return false;
    }

    public boolean visit(TerminalsSeg n) {
        terminalList termList= (terminalList) n.getterminals_segment();
        int N= termList.size();

        fChildren= new Object[N];
        for(int i=0; i < N; i++) {
            terminal term= (terminal) termList.getElementAt(i);
            fChildren[N-i-1]= new JikesPGStructureNode(term, fStructureNode, JikesPGStructureNode.TERMINAL, term.getterminal_symbol().toString());
        }
        return false;
    }

    public boolean visit(RulesSeg n) {
        rules_segment rulesSegment= n.getrules_segment();
        nonTermList nonTermList= rulesSegment.getnonTermList();
        int N= nonTermList.size();

        fChildren= new Object[N];
        for(int i=0; i < N; i++) {
            nonTerm nt= (nonTerm) nonTermList.getElementAt(i);
            fChildren[N-i-1]= new JikesPGStructureNode(nt, fStructureNode, JikesPGStructureNode.NONTERMINAL, nt.getSYMBOL().toString());
        }
        return false;
    }

    public Object[] getChildren() {
        return fChildren;
    }
}