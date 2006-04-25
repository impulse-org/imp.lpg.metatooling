package org.jikespg.uide.editor;

import java.util.List;
import lpg.lpgjavaruntime.IToken;
import lpg.lpgjavaruntime.PrsStream;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.uide.editor.IHoverHelper;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.ASTUtils;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.IASTNodeToken;
import org.jikespg.uide.parser.JikesPGParser.IJikesPG_item;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.JikesPG_itemList;
import org.jikespg.uide.parser.JikesPGParser.RulesSeg;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.nonTermList;
import org.jikespg.uide.parser.JikesPGParser.rules_segment;
import org.jikespg.uide.parser.JikesPGParser.terminal;

public class HoverHelper implements IHoverHelper {
    public String getHoverHelpAt(IParseController parseController, ISourceViewer srcViewer, int offset) {
        PrsStream ps= parseController.getParser().getParseStream();
        IToken token= ps.getTokenAtCharacter(offset);

        if (token == null) return null;

        ASTNode ast= (ASTNode) parseController.getCurrentAst();

        if (ast == null) return null;

        IASTNodeLocator nodeLocator= parseController.getNodeLocator();

        ASTNode node= (ASTNode) nodeLocator.findNode(ast, offset);

        if (node == null)
            return null;

        if (node instanceof IASTNodeToken) {
            ASTNode def= (ASTNode) findDefOf((IASTNodeToken) node, (JikesPG) ast);

            if (def != null)
                return getSubstring(parseController, def.getLeftIToken().getStartOffset(), def.getRightIToken().getEndOffset());
        }
        return getSubstring(parseController, token);
    }

    public static String getSubstring(IParseController parseController, int start, int end) {
        return new String(parseController.getLexer().getLexStream().getInputChars(), start, end-start+1);
    }

    public static String getSubstring(IParseController parseController, IToken token) {
        return getSubstring(parseController, token.getStartOffset(), token.getEndOffset());
    }

    public static ASTNode findDefOf(IASTNodeToken s, JikesPG root) {
	// This would use the auto-generated bindings if they were implemented already...
	String id= stripName(s.toString());

        List/*<nonTerm>*/ nonTermList= ASTUtils.getNonTerminals(root);
        List/*<terminal>*/ termList= ASTUtils.getTerminals(root);
	
        for(int j=0; j < nonTermList.size(); j++) {
            nonTerm nonTerm= (nonTerm) nonTermList.get(j);
            String nonTermName= stripName(nonTerm.getSYMBOL().toString());

            if (nonTermName.equals(id))
        	return nonTerm;
        }
        for(int j=0; j < termList.size(); j++) {
            terminal term= (terminal) termList.get(j);
            String termName= stripName(term.getterminal_symbol().toString());

            if (termName.equals(id))
        	return term;
        }
	return null;
    }

    public static String stripName(String rawId) {
	int idx= rawId.indexOf('$');

	return (idx >= 0) ? rawId.substring(0, idx) : rawId;
    }
}
