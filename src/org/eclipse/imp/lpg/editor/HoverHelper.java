package org.jikespg.uide.editor;

import org.eclipse.uide.editor.IHoverHelper;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.Irules;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.TSYMBOL;
import org.jikespg.uide.parser.JikesPGParser.rules100;

import com.ibm.lpg.IToken;
import com.ibm.lpg.PrsStream;

public class HoverHelper implements IHoverHelper {
    public String getHoverHelpAt(IParseController parseController, int offset) {
        PrsStream ps= parseController.getParser().getParseStream();
        IToken token= ps.getTokenAtCharacter(offset);

        if (token == null) return null;

        ASTNode ast= (ASTNode) parseController.getCurrentAst();

        if (ast == null) return null;

        IASTNodeLocator nodeLocator= parseController.getNodeLocator();

        ASTNode node= (ASTNode) nodeLocator.findNode(ast, offset);

        if (node == null)
            return null;

        if (node instanceof TSYMBOL) {
            ASTNode def= (ASTNode) findDefOf((TSYMBOL) node, (JikesPG) ast);

            if (def != null)
                return getSubstring(parseController, ps.getIToken(def.getLeftToken()).getStartOffset(), ps.getIToken(def.getRightToken()).getEndOffset());
        }
        return getSubstring(parseController, token);
    }

    private String getSubstring(IParseController parseController, int start, int end) {
        return new String(parseController.getLexer().getLexStream().getInputChars(), start, end-start+1);
    }

    private String getSubstring(IParseController parseController, IToken token) {
        return getSubstring(parseController, token.getStartOffset(), token.getEndOffset());
    }

    private Irules findDefOf(TSYMBOL s, JikesPG root) {
        return null;
    }
}
