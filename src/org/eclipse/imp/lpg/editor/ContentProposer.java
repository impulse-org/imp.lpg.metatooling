/*
 * Created on Nov 1, 2005
 */
package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.jikespg.uide.parser.JikesPGParser.TerminalsSeg;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.nonTermList;
import org.jikespg.uide.parser.JikesPGParser.rules_segment;
import org.jikespg.uide.parser.JikesPGParser.terminal;
import org.jikespg.uide.parser.JikesPGParser.terminalList;

public class ContentProposer implements IContentProposer {

    private static final class GrammarProposal implements ICompletionProposal {
        private final String fName;

        private final String fPrefix;

        private final int fOffset;

        private GrammarProposal(String name, String prefix, int offset) {
            super();
            fName= name;
            fPrefix= prefix;
            fOffset= offset;
        }

        public void apply(IDocument document) {
            try {
                document.replace(fOffset, 0, fName.substring(fPrefix.length()));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        public Point getSelection(IDocument document) {
            int newOffset= fOffset + fName.length() - fPrefix.length();
            return new Point(newOffset, 0);
        }

        public String getAdditionalProposalInfo() {
            return null;
        }

        public String getDisplayString() {
            return fName;
        }

        public Image getImage() {
            return null;
        }

        public IContextInformation getContextInformation() {
            return null;
        }
    }

    public ICompletionProposal[] getContentProposals(IParseController controller, final int offset) {
	PrsStream parseStream= controller.getParser().getParseStream();
	int thisTokIdx= parseStream.getTokenIndexAtCharacter(offset);
        if (thisTokIdx < 0) thisTokIdx= - thisTokIdx;
	JikesPG root= (JikesPG) controller.getCurrentAst();

        if (root == null)
            return new ICompletionProposal[0];

        IASTNodeLocator locator= controller.getNodeLocator();
	ASTNode thisNode= (ASTNode) locator.findNode(root, offset);
        final String prefix= thisNode.getLeftIToken().toString();

        final List/*<ICompletionProposal>*/ proposals= new ArrayList();

        proposals.addAll(computeNonTerminalCompletions(prefix, offset, root));
        proposals.addAll(computeTerminalCompletions(prefix, offset, root));

        return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
    }

    private List/*<ICompletionProposal>*/ computeNonTerminalCompletions(final String prefix, final int offset, JikesPG root) {
        List/*<ICompletionProposal>*/ result= new ArrayList();
        List/*<nonTerm>*/ nonTerms= getNonTerminals(root);

        for(Iterator iter= nonTerms.iterator(); iter.hasNext(); ) {
            nonTerm nt= (nonTerm) iter.next();
            String ntRawName= nt.getSYMBOL().toString();
            int idx= ntRawName.indexOf('$');
            final String ntName= (idx >= 0) ? ntRawName.substring(0, idx) : ntRawName;

            if (ntName.startsWith(prefix)) {
                result.add(new GrammarProposal(ntName, prefix, offset));
            }
        }
        return result;
    }

    private List/*<nonTerm>*/ getNonTerminals(JikesPG root) {
        List/*<nonTerm>*/ result= new ArrayList();

        // TODO: pick up non-terminals from any imported file
        RulesSeg rules= (RulesSeg) findItemOfType(root, RulesSeg.class);
        rules_segment rulesSeg= rules.getrules_segment();
        nonTermList nonTermList= rulesSeg.getnonTermList();

        result.addAll(nonTermList.getArrayList());
        return result;
    }

    private List/*<ICompletionProposal>*/ computeTerminalCompletions(final String prefix, final int offset, JikesPG root) {
        List/*<ICompletionProposal>*/ result= new ArrayList();
        List/*<terminal>*/ terms= getTerminals(root);

        for(Iterator iter= terms.iterator(); iter.hasNext(); ) {
            terminal t= (terminal) iter.next();
            String termRawName= t.getterminal_symbol().toString();
            int idx= termRawName.indexOf('$');
            final String termName= (idx >= 0) ? termRawName.substring(0, idx) : termRawName;

            if (termName.startsWith(prefix)) {
                result.add(new GrammarProposal(termName, prefix, offset));
            }
        }
        return result;
    }

    private List/*<terminal>*/ getTerminals(JikesPG root) {
        List/*<terminal>*/ result= new ArrayList();

        // TODO: pick up terminals from any imported file
        TerminalsSeg terminalsSeg= (TerminalsSeg) findItemOfType(root, TerminalsSeg.class);
        terminalList terminals= terminalsSeg.getterminals_segment();

        result.addAll(terminals.getArrayList());
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
