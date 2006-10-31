package org.jikespg.uide.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

    public static List<Imacro_name_symbol> getMacros(JikesPG root) {
        List<Imacro_name_symbol> result= new ArrayList<Imacro_name_symbol>();
    
        // DO NOT pick up macros from any imported file! They shouldn't be treated as defined in this scope!
        List<ASTNode> defineSegs= findItemOfType(root, DefineSeg.class);
        for(Iterator iter= defineSegs.iterator(); iter.hasNext(); ) {
	    DefineSeg defineSeg= (DefineSeg) iter.next();
	    
	    if (defineSeg == null) continue;

	    defineSpecList defines= defineSeg.getdefine_segment();

	    for(int i= 0; i < defines.size(); i++) {
		defineSpec define= defines.getdefineSpecAt(i);
		result.add(define.getmacro_name_symbol());
	    }
	}
        return result;
    }

    public static List<nonTerm> getNonTerminals(JikesPG root) {
        List<nonTerm> result= new ArrayList<nonTerm>();
    
        // TODO: pick up non-terminals from any imported file
        List<ASTNode> rulesSegs= findItemOfType(root, RulesSeg.class);

        if (rulesSegs.size() == 0) return Collections.emptyList();

        for(Iterator iter= rulesSegs.iterator(); iter.hasNext(); ) {
	    RulesSeg rulesSeg= (RulesSeg) iter.next();
	    
	    rules_segment rules_seg= rulesSeg.getrules_segment();
	    nonTermList nonTermList= rules_seg.getnonTermList();
	    
	    result.addAll(nonTermList.getArrayList());
	}
        return result;
    }

    public static List<terminal> getTerminals(JikesPG root) {
        List<terminal> result= new ArrayList();
    
        // TODO: pick up terminals from any imported file
        List<ASTNode> terminalsSegs= findItemOfType(root, TerminalsSeg.class);

        for(Iterator iter= terminalsSegs.iterator(); iter.hasNext();) {
	    TerminalsSeg terminalsSeg= (TerminalsSeg) iter.next();

	    if (terminalsSeg != null) {
	        terminalList terminals= terminalsSeg.getterminals_segment();
	        
	        result.addAll(terminals.getArrayList());
	    }
	}
        return result;
    }

    public static List<ASTNode> findItemOfType(JikesPG root, Class ofType) {
        JikesPG_itemList itemList= root.getJikesPG_INPUT();
        List<ASTNode> result= new ArrayList<ASTNode>();

        for(int i=0; i < itemList.size(); i++) {
            IJikesPG_item item= itemList.getJikesPG_itemAt(i);
    
            if (ofType.isInstance(item))
                result.add((ASTNode) item);
        }
        return result;
    }

    public static ASTNode findDefOf(IASTNodeToken s, JikesPG root) {
        // This would use the auto-generated bindings if they were implemented already...
        String id= HoverHelper.stripName(s.toString());

        List<nonTerm> nonTermList= getNonTerminals(root);
        List<terminal> termList= getTerminals(root);
        List<Imacro_name_symbol> macros= getMacros(root);

        for(int j=0; j < nonTermList.size(); j++) {
            nonTerm nonTerm= nonTermList.get(j);
            String nonTermName= HoverHelper.stripName(nonTerm.getSYMBOL().toString());

            if (nonTermName.equals(id))
        	return nonTerm;
        }
        for(int j=0; j < termList.size(); j++) {
            terminal term= termList.get(j);
            String termName= HoverHelper.stripName(term.getterminal_symbol().toString());

            if (termName.equals(id))
        	return term;
        }
        for(int j=0; j < macros.size(); j++) {
            Imacro_name_symbol macro= macros.get(j);
            String macroName= macro.toString();

            if (macroName.equals(s.toString()))
        	return (ASTNode) macro;
        }
        return null;
    }

    public static List<ASTNode> findRefsOf(final nonTerm nonTerm) {
	final List<ASTNode> result= new ArrayList<ASTNode>();
	JikesPG root= getRoot(nonTerm);

	List<nonTerm> nonTerms= getNonTerminals(root);

	// Indexed search would be nice here...
	for(int i=0; i < nonTerms.size(); i++) {
	    nonTerm nt= nonTerms.get(i);

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
