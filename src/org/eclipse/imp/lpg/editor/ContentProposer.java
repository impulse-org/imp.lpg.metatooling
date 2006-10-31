/*
 * Created on Nov 1, 2005
 */
package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.Collection;
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
import org.eclipse.uide.editor.SourceProposal;
import org.eclipse.uide.parser.IASTNodeLocator;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.ASTUtils;
import org.jikespg.uide.parser.JikesPGParser.*;

public class ContentProposer implements IContentProposer {

    public ICompletionProposal[] getContentProposals(IParseController controller, final int offset) {
	PrsStream parseStream= controller.getParser().getParseStream();
	int thisTokIdx= parseStream.getTokenIndexAtCharacter(offset);
        if (thisTokIdx < 0) thisTokIdx= - thisTokIdx;
	JikesPG root= (JikesPG) controller.getCurrentAst();

        if (root == null)
            return new ICompletionProposal[0];

        IASTNodeLocator locator= controller.getNodeLocator();
	ASTNode thisNode= (ASTNode) locator.findNode(root, offset);
        final String prefixToken= thisNode.getLeftIToken().toString();
        final String prefix= prefixToken.substring(0, offset - thisNode.getLeftIToken().getStartOffset());

        final List/*<ICompletionProposal>*/ proposals= new ArrayList();

        if (thisNode.getParent() instanceof option) {
	    option opt= (option) thisNode.getParent();

	    if (thisNode == opt.getSYMBOL()) {
		proposals.addAll(computeOptionKeyProposals(prefix, offset));
	    }
        } else if (prefix.startsWith("$"))
            proposals.addAll(computeMacroCompletions(prefix, offset, root));
        else {
            proposals.addAll(computeNonTerminalCompletions(prefix, offset, root));
            proposals.addAll(computeTerminalCompletions(prefix, offset, root));
        }

        return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
    }

    private final static String[] OPTION_KEYS= {
	"action", "ast_directory", "ast_type", "attributes",
	"automatic_ast", "backtrack", "byte", "conflicts",
	"dat-directory", "dat-file", "dcl-file", "debug",
	"def-file", "edit", "error-maps", "escape=character", 
	"extends-parsetable", "export-terminals", "factory", "file-prefix",
	"filter", "first", "follow", "goto-default",
	"grm-file", "imp-file", "import-terminals", "include-directory",
	"lalr-level", "list", "margin", "max_cases",
	"names", "nt-check", "or-marker", "out_directory",
	"package", "parent_saved", "parsetable-interfaces", "prefix=string",
	"priority", "programming_language", "prs-file", "quiet",
	"read-reduce", "remap-terminals", "scopes", "serialize",
	"shift-default", "single-productions", "slr", "soft-keywords",
	"states", "suffix", "sym-file", "tab-file",
	"table", "template", "trace", "variables",
	"verbose", "visitor", "visitor-type", "warnings",
    	"xref"
    };

    private Collection<SourceProposal> computeOptionKeyProposals(String prefix, int offset) {
	Collection<SourceProposal> result= new ArrayList<SourceProposal>();
	for(int i= 0; i < OPTION_KEYS.length; i++) {
	    String key= OPTION_KEYS[i];
	    if (key.startsWith(prefix))
		result.add(new SourceProposal(key, prefix, offset));
	}
	return result;
    }

    private List/*<ICompletionProposal>*/ computeMacroCompletions(String prefix, int offset, JikesPG root) {
        List/*<ICompletionProposal>*/ result= new ArrayList();
        List/*<Imacro_name_symbol>*/ macros= ASTUtils.getMacros(root);

        for(Iterator iter= macros.iterator(); iter.hasNext(); ) {
            Imacro_name_symbol macro= (Imacro_name_symbol) iter.next();
            String macroName= macro.toString();

            if (macroName.startsWith(prefix)) {
                result.add(new SourceProposal(macroName, prefix, offset));
            }
        }
        return result;
    }

    private List/*<ICompletionProposal>*/ computeNonTerminalCompletions(final String prefix, final int offset, JikesPG root) {
        List/*<ICompletionProposal>*/ result= new ArrayList();
        List/*<nonTerm>*/ nonTerms= ASTUtils.getNonTerminals(root);

        for(Iterator iter= nonTerms.iterator(); iter.hasNext(); ) {
            nonTerm nt= (nonTerm) iter.next();
            String ntRawName= nt.getSYMBOL().toString();
            int idx= ntRawName.indexOf('$');
            final String ntName= (idx >= 0) ? ntRawName.substring(0, idx) : ntRawName;

            if (ntName.startsWith(prefix)) {
                result.add(new SourceProposal(ntName, prefix, offset));
            }
        }
        return result;
    }

    private List/*<ICompletionProposal>*/ computeTerminalCompletions(final String prefix, final int offset, JikesPG root) {
        List/*<ICompletionProposal>*/ result= new ArrayList();
        List/*<terminal>*/ terms= ASTUtils.getTerminals(root);

        for(Iterator iter= terms.iterator(); iter.hasNext(); ) {
            terminal t= (terminal) iter.next();
            String termRawName= t.getterminal_symbol().toString();
            int idx= termRawName.indexOf('$');
            final String termName= (idx >= 0) ? termRawName.substring(0, idx) : termRawName;

            if (termName.startsWith(prefix)) {
                result.add(new SourceProposal(termName, prefix, offset));
            }
        }
        return result;
    }
}
