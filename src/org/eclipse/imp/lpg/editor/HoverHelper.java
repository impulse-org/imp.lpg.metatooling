package org.jikespg.uide.editor;

import org.eclipse.uide.editor.IHoverHelper;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;

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

        if (node == null) return null;

        return new String(parseController.getLexer().getLexStream().getInputChars(), token.getStartOffset(), token.getEndOffset()-token.getStartOffset()+1);
    }
}
