package org.jikespg.uide.editor;

import lpg.lpgjavaruntime.IToken;
import lpg.lpgjavaruntime.PrsStream;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.uide.editor.IHoverHelper;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.ASTUtils;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.IASTNodeToken;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;

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
            ASTNode def= (ASTNode) ASTUtils.findDefOf((IASTNodeToken) node, (JikesPG) ast);

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

    public static String stripName(String rawId) {
	int idx= rawId.indexOf('$');

	return (idx >= 0) ? rawId.substring(0, idx) : rawId;
    }
}
