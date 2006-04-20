/*
 * Created on Nov 1, 2005
 */
package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lpg.lpgjavaruntime.IToken;
import lpg.lpgjavaruntime.PrsStream;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.uide.editor.IContentProposer;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.IJikesPG_item;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.JikesPG_itemList;
import org.jikespg.uide.parser.JikesPGParser.RulesSeg;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.nonTermList;
import org.jikespg.uide.parser.JikesPGParser.rules_segment;

public class ContentProposer implements IContentProposer {

    public ICompletionProposal[] getContentProposals(IParseController controller, final int offset) {
	PrsStream parseStream= controller.getParser().getParseStream();
	int thisTokIdx= parseStream.getTokenIndexAtCharacter(offset);
        if (thisTokIdx < 0) thisTokIdx= - thisTokIdx;
	IToken prevTok= parseStream.getTokenAt(thisTokIdx - 1);
	JikesPG root= (JikesPG) controller.getCurrentAst();

	IASTNodeLocator locator= controller.getNodeLocator();
	ASTNode thisNode= (ASTNode) locator.findNode(root, offset);
        final String prefix= thisNode.getLeftIToken().toString();

        List/*<nonTerm>*/ nonTerms= getNonTerminals(root);
        final List/*<ICompletionProposal>*/ proposals= new ArrayList();

        for(Iterator iter= nonTerms.iterator(); iter.hasNext(); ) {
            nonTerm nt= (nonTerm) iter.next();
            String ntRawName= nt.getSYMBOL().toString();
            int idx= ntRawName.indexOf('$');
            final String ntName= (idx >= 0) ? ntRawName.substring(0, idx) : ntRawName;

            if (ntName.startsWith(prefix)) {
                proposals.add(new ICompletionProposal() {
                    public void apply(IDocument document) {
                        try {
                            document.replace(offset, 0, ntName.substring(prefix.length()));
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                    public Point getSelection(IDocument document) {
                        int newOffset= offset + ntName.length() - prefix.length();
                        return new Point(newOffset, 0);
                    }
                    public String getAdditionalProposalInfo() {
                        return null;
                    }
                    public String getDisplayString() {
                        return ntName;
                    }
                    public Image getImage() {
                        return null;
                    }
                    public IContextInformation getContextInformation() {
                        return null;
                    }
                });
            }
        }
        return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
    }

    private List/*<nonTerm>*/ getNonTerminals(JikesPG root) {
        List/*<nonTerm>*/ result= new ArrayList();

        RulesSeg rules= (RulesSeg) findItemOfType(root, RulesSeg.class);
        rules_segment rulesSeg= rules.getrules_segment();
        nonTermList nonTermList= rulesSeg.getnonTermList();

        result.addAll(nonTermList.getArrayList());
        return result;
    }

    private ASTNode findItemOfType(JikesPG root, Class ofType) {
        JikesPG_itemList itemList= root.getJikesPG_INPUT();

        for(int i=0; i < itemList.size(); i++) {
            IJikesPG_item item= itemList.getJikesPG_itemAt(i);

            if (ofType.isInstance(item))
                return (ASTNode) item;
        }
        return null;
    }
}
