package org.jikespg.uide.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jikespg.uide.editor.HoverHelper;
import org.jikespg.uide.parser.JikesPGParser.*;

public class ASTUtils {
    private ASTUtils() { }

    public static JikesPG getRoot(ASTNode node) {
	while (node != null && !(node instanceof JikesPG))
	    node= node.parent;
	return (JikesPG) node;
    }

    public static List/*<Imacro_name_symbol>*/ getMacros(JikesPG root) {
        List/*<Imacro_name_symbol>*/ result= new ArrayList();
    
        // DO NOT pick up macros from any imported file! They shouldn't be treated as defined in this scope!
        DefineSeg defineSeg= (DefineSeg) findItemOfType(root, DefineSeg.class);

        if (defineSeg == null) return Collections.EMPTY_LIST;

        defineSpecList defines= defineSeg.getdefine_segment();

        for(int i= 0; i < defines.size(); i++) {
            defineSpec define= defines.getdefineSpecAt(i);
            result.add(define.getmacro_name_symbol());
        }
        return result;
    }

    public static List/*<nonTerm>*/ getNonTerminals(JikesPG root) {
        List/*<nonTerm>*/ result= new ArrayList();
    
        // TODO: pick up non-terminals from any imported file
        RulesSeg rules= (RulesSeg) findItemOfType(root, RulesSeg.class);

        if (rules == null) return Collections.EMPTY_LIST;

        rules_segment rulesSeg= rules.getrules_segment();
        nonTermList nonTermList= rulesSeg.getnonTermList();
    
        result.addAll(nonTermList.getArrayList());
        return result;
    }

    public static List/*<terminal>*/ getTerminals(JikesPG root) {
        List/*<terminal>*/ result= new ArrayList();
    
        // TODO: pick up terminals from any imported file
        TerminalsSeg terminalsSeg= (TerminalsSeg) findItemOfType(root, TerminalsSeg.class);

        if (terminalsSeg == null) return Collections.EMPTY_LIST;

        terminalList terminals= terminalsSeg.getterminals_segment();
    
        result.addAll(terminals.getArrayList());
        return result;
    }

    public static ASTNode findItemOfType(JikesPG root, Class ofType) {
        JikesPG_itemList itemList= root.getJikesPG_INPUT();
    
        for(int i=0; i < itemList.size(); i++) {
            IJikesPG_item item= itemList.getJikesPG_itemAt(i);
    
            if (ofType.isInstance(item))
                return (ASTNode) item;
        }
        return null;
    }

    public static ASTNode findDefOf(IASTNodeToken s, JikesPG root) {
        // This would use the auto-generated bindings if they were implemented already...
        String id= HoverHelper.stripName(s.toString());

        List/*<nonTerm>*/ nonTermList= getNonTerminals(root);
        List/*<terminal>*/ termList= getTerminals(root);
        List/*<Imacro_name_symbol>*/ macros= getMacros(root);

        for(int j=0; j < nonTermList.size(); j++) {
            nonTerm nonTerm= (nonTerm) nonTermList.get(j);
            String nonTermName= HoverHelper.stripName(nonTerm.getSYMBOL().toString());

            if (nonTermName.equals(id))
        	return nonTerm;
        }
        for(int j=0; j < termList.size(); j++) {
            terminal term= (terminal) termList.get(j);
            String termName= HoverHelper.stripName(term.getterminal_symbol().toString());

            if (termName.equals(id))
        	return term;
        }
        for(int j=0; j < macros.size(); j++) {
            Imacro_name_symbol macro= (Imacro_name_symbol) macros.get(j);
            String macroName= macro.toString();

            if (macroName.equals(s.toString()))
        	return (ASTNode) macro;
        }
        return null;
    }

    public static List/*symWithAttrs*/ findRefsOf(final nonTerm nonTerm) {
	final List result= new ArrayList();
	JikesPG root= getRoot(nonTerm);

	List/*<nonTerm>*/ nonTerms= getNonTerminals(root);

	// Indexed search would be nice here...
	for(int i=0; i < nonTerms.size(); i++) {
	    nonTerm nt= (nonTerm) nonTerms.get(i);

	    nt.accept(new AbstractVisitor() {
		public void unimplementedVisitor(String s) { }
		public boolean visit(symWithAttrs1 n) {
		    if (n.getSYMBOL().toString().equals(nonTerm.getSYMBOL().toString()))
			result.add(n);
		    return super.visit(n);
		}
		public boolean visit(symWithAttrs2 n) {
		    if (n.getSYMBOL().toString().equals(nonTerm.getSYMBOL().toString()))
			result.add(n);
		    return super.visit(n);
		}
	    });
	}
	return result;
    }
}
