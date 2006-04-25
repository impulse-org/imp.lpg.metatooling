package org.jikespg.uide.parser;

import java.util.ArrayList;
import java.util.List;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.DefineSeg;
import org.jikespg.uide.parser.JikesPGParser.IJikesPG_item;
import org.jikespg.uide.parser.JikesPGParser.Idefine_segment;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.JikesPG_itemList;
import org.jikespg.uide.parser.JikesPGParser.RulesSeg;
import org.jikespg.uide.parser.JikesPGParser.TerminalsSeg;
import org.jikespg.uide.parser.JikesPGParser.define_segment1;
import org.jikespg.uide.parser.JikesPGParser.nonTermList;
import org.jikespg.uide.parser.JikesPGParser.rules_segment;
import org.jikespg.uide.parser.JikesPGParser.terminalList;

public class ASTUtils {
    private ASTUtils() { }

    public static List/*<Imacro_name_symbol>*/ getMacros(JikesPG root) {
        List/*<Imacro_name_symbol>*/ result= new ArrayList();
    
        // DO NOT pick up macros from any imported file! They shouldn't be treated as defined in this scope!
        DefineSeg defineSeg= (DefineSeg) findItemOfType(root, DefineSeg.class);
        Idefine_segment defines= defineSeg.getdefine_segment();
    
        while (defines instanceof define_segment1) {
            define_segment1 define= (define_segment1) defines;
            result.add(define.getmacro_name_symbol());
            defines= ((define_segment1) defines).getdefine_segment();
        }
        return result;
    }

    public static List/*<nonTerm>*/ getNonTerminals(JikesPG root) {
        List/*<nonTerm>*/ result= new ArrayList();
    
        // TODO: pick up non-terminals from any imported file
        RulesSeg rules= (RulesSeg) findItemOfType(root, RulesSeg.class);
        rules_segment rulesSeg= rules.getrules_segment();
        nonTermList nonTermList= rulesSeg.getnonTermList();
    
        result.addAll(nonTermList.getArrayList());
        return result;
    }

    public static List/*<terminal>*/ getTerminals(JikesPG root) {
        List/*<terminal>*/ result= new ArrayList();
    
        // TODO: pick up terminals from any imported file
        TerminalsSeg terminalsSeg= (TerminalsSeg) findItemOfType(root, TerminalsSeg.class);
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
}
