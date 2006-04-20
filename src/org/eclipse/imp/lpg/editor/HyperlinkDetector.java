package org.jikespg.uide.editor;

import lpg.lpgjavaruntime.IToken;
import lpg.lpgjavaruntime.PrsStream;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.uide.core.ILanguageService;
import org.eclipse.uide.editor.ISourceHyperlinkDetector;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.IASTNodeToken;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;

public class HyperlinkDetector implements ISourceHyperlinkDetector, ILanguageService {
    public HyperlinkDetector() { }

    public IHyperlink[] detectHyperlinks(final ITextViewer textViewer, final IRegion region, IParseController parseController) {
        int offset= region.getOffset();
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
            final nonTerm def= HoverHelper.findDefOf((IASTNodeToken) node, (JikesPG) ast);

            if (def != null) {
                final int srcStart= node.getLeftIToken().getStartOffset();
                final int srcLength= node.getRightIToken().getEndOffset() - srcStart + 1;
                final int targetStart= def.getLeftIToken().getStartOffset();
                final int targetLength= def.getSYMBOL().toString().length();

                return new IHyperlink[] {
                        new IHyperlink() {
                            public IRegion getHyperlinkRegion() {
                                return new Region(srcStart, srcLength);
                            }
                            public String getTypeLabel() {
                                return "non-terminal";
                            }
                            public String getHyperlinkText() {
                                return def.getSYMBOL().toString();
                            }
                            public void open() {
                                textViewer.setSelectedRange(targetStart, targetLength);
                                textViewer.revealRange(targetStart, targetLength);
                            }
                        }
                };
            }
        }
        return null;
    }
}
