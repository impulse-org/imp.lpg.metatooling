/*
 * Created on Mar 24, 2006
 */
package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lpg.lpgjavaruntime.IToken;
import org.eclipse.uide.core.ILanguageService;
import org.eclipse.uide.editor.ISourceFormatter;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.*;

public class JikesPGFormatter implements ILanguageService, ISourceFormatter {
    private int fIndentSize= 6;
    private String fIndentString;

    private boolean fIndentProducesToWidestNonTerm= false;

    public void formatterStarts(String initialIndentation) {
        // Should pick up preferences here
        fIndentSize= 4;
        StringBuffer buff= new StringBuffer(fIndentSize);
        for(int i=0; i < fIndentSize; i++)
            buff.append(' ');
        fIndentString= buff.toString();
        fIndentProducesToWidestNonTerm= false;
    }

    public String format(IParseController parseController, String content, boolean isLineStart, String indentation, int[] positions) {
        final StringBuffer fBuff= new StringBuffer();
        final Set fAdjunctTokens= new HashSet();
        final ASTNode[] fAdjunctNode= new ASTNode[1];
        final List/*<IToken>*/ fFollowingAdjuncts= new ArrayList();
        JikesPG root= (JikesPG) parseController.getCurrentAst();

        root.accept(new JikesPGParser.AbstractVisitor() {
            private int prodCount;
            private int prodIndent;
            public void unimplementedVisitor(String s) {
                System.out.println("Unhandled node type: " + s);
            }
            public boolean preVisit(ASTNode n) {
                IToken left= n.getLeftIToken();
                IToken[] precAdjuncts= left.getPrecedingAdjuncts();

                for(int i= 0; i < precAdjuncts.length; i++) {
                    IToken adjunct= precAdjuncts[i];
                    if (!fAdjunctTokens.contains(adjunct)) {
                        fBuff.append(adjunct);
                        fBuff.append('\n');
                    }
                    fAdjunctTokens.add(adjunct);
                }

                if (fFollowingAdjuncts.size() == 0) {
                    IToken right= n.getRightIToken();
                    IToken[] follAdjuncts= right.getFollowingAdjuncts();

                    for(int i= 0; i < follAdjuncts.length; i++) {
                	IToken adjunct= follAdjuncts[i];

                	if (!fAdjunctTokens.contains(adjunct)) {
                	    fFollowingAdjuncts.add(adjunct);
                	    fAdjunctTokens.add(adjunct);
                	}
                    }
                    fAdjunctNode[0]= n;
                }
                return true;
            }
            public void postVisit(ASTNode n) {
        	if (n == fAdjunctNode[0]) {
        	    for(Iterator iter= fFollowingAdjuncts.iterator(); iter.hasNext(); ) {
        		IToken adjunct= (IToken) iter.next();
		    
        		fBuff.append(adjunct);
        		fBuff.append('\n');
        	    }
        	    fFollowingAdjuncts.clear();
        	}
            }
            public boolean visit(option_spec n) {
                fBuff.append("%options ");
                return true;
            }
            public boolean visit(optionList n) {
        	for(int i=0; i < n.size(); i++) {
        	    if (i > 0) fBuff.append(", ");
        	    final option opt= n.getoptionAt(i);
		    opt.accept(this);
        	}
        	return false;
            }
            public void endVisit(option_spec n) {
                fBuff.append('\n');
            }
            public boolean visit(option n) {
                fBuff.append(n.getSYMBOL());
                return true;
            }
            public boolean visit(option_value0 n) {
                fBuff.append("=" + n.getSYMBOL());
                return false;
            }
            public boolean visit(option_value1 n) {
                fBuff.append('(');
                SYMBOLList symList= n.getsymbol_list();
                for(int i=0; i < symList.size(); i++) {
                    if (i > 0) fBuff.append(',');
                    fBuff.append(symList.getSYMBOLAt(i));
                }
                fBuff.append(')');
                return false;
            }
            public boolean visit(NoticeSeg n) {
                fBuff.append("$Notice\n");
                return true;
            }
            public void endVisit(NoticeSeg n) {
                fBuff.append("$End\n\n");
            }
            public boolean visit(GlobalsSeg n) {
                fBuff.append("$Globals\n");
                return true;
            }
            public void endVisit(GlobalsSeg n) {
                fBuff.append("$End\n\n");
            }
            public boolean visit(HeadersSeg n) {
                fBuff.append("$Headers\n");
                return true;
            }
            public void endVisit(HeadersSeg n) {
                fBuff.append("$End\n\n");
            }
            public boolean visit(IdentifierSeg n) {
                fBuff.append("$Identifier\n");
                return true;
            }
            public void endVisit(IdentifierSeg n) {
                fBuff.append("$End\n\n");
            }
            public boolean visit(EofSeg n) {
                fBuff.append("$EOF\n");
                return true;
            }
            public void endVisit(EofSeg n) {
                fBuff.append("$End\n\n");
            }
            public boolean visit(terminal_symbol0 n) {
                fBuff.append(fIndentString);
                fBuff.append(n.getSYMBOL());
                fBuff.append('\n');
                return false;
            }
            public boolean visit(DefineSeg n) {
                fBuff.append("$Define\n");
                return true;
            }
            public void endVisit(DefineSeg n) {
                fBuff.append("$End\n\n");
            }
            public void endVisit(defineSpec n) {
                fBuff.append(fIndentString);
                fBuff.append(n.getmacro_name_symbol());
                fBuff.append(' ');
                fBuff.append(n.getmacro_segment());
                fBuff.append('\n');
            }
            public boolean visit(TerminalsSeg n) {
                fBuff.append("$Terminals\n");
                return true;
            }
            public void endVisit(TerminalsSeg n) {
                fBuff.append("$End\n\n");
            }
            public boolean visit(terminal n) {
                fBuff.append(fIndentString + n.getterminal_symbol());
                if (n.getoptTerminalAlias() != null)
                    fBuff.append(" ::= " + n.getoptTerminalAlias().getname());
                fBuff.append('\n');
                return false;
            }
            public boolean visit(StartSeg n) {
                fBuff.append("$Start\n");
                return true;
            }
            public void endVisit(StartSeg n) {
                fBuff.append("$End\n\n");
            }
            public boolean visit(start_symbol0 n) {
                fBuff.append(fIndentString);
                fBuff.append(n.getSYMBOL());
                fBuff.append('\n');
                return false;
            }
            public boolean visit(start_symbol1 n) {
                fBuff.append(n.getMACRO_NAME());
                return false;
            }
            public boolean visit(RulesSeg n) {
                fBuff.append("$Rules\n");
                if (fIndentProducesToWidestNonTerm) {
                    rules_segment rulesSegment= n.getrules_segment();
                    nonTermList nonTermList= rulesSegment.getnonTermList();
                    int maxLHSSymWid= 0;
                    for(int i=0; i < nonTermList.size(); i++) {
                        int lhsSymWid= nonTermList.getElementAt(i).getLeftIToken().toString().length();
                        if (lhsSymWid > maxLHSSymWid) maxLHSSymWid= lhsSymWid;
                    }
                    prodIndent= fIndentSize + maxLHSSymWid + 1;
                }
                return true;
            }
            public void endVisit(RulesSeg n) {
                fBuff.append("$End\n");
            }
            public boolean visit(nonTerm n) {
                fBuff.append(fIndentString);
                fBuff.append(n.getSYMBOL());
                if (n.getclassName() != null)
                    fBuff.append(n.getclassName());
                if (n.getarrayElement() != null)
                    fBuff.append(n.getarrayElement());
                if (fIndentProducesToWidestNonTerm) {
                    for(int i=n.getSYMBOL().toString().length() + fIndentSize + 1; i <= prodIndent; i++)
                        fBuff.append(' ');
                } else
                    fBuff.append(' ');
                fBuff.append(n.getproduces());
                prodCount= 0;
                if (!fIndentProducesToWidestNonTerm)
                    prodIndent= fIndentSize + n.getSYMBOL().toString().length() + 1;
                return true;
            }
            public void endVisit(nonTerm n) {
                fBuff.append('\n');
            }
            public boolean visit(rhs n) {
                if (prodCount > 0) {
                    fBuff.append('\n');
                    for(int i=0; i < prodIndent; i++)
                        fBuff.append(' ');
                    fBuff.append("|  ");
                }
                prodCount++;
                return true;
            }
            public boolean visit(action_segment n) {
                fBuff.append(n.getBLOCK());
                fBuff.append('\n');
                return false;
            }
            public boolean visit(symWithAttrs0 n) {
                fBuff.append(' ');
                fBuff.append(n.getEMPTY_KEY());
                return false;
            }
            public boolean visit(symWithAttrs1 n) {
                fBuff.append(' ');
                fBuff.append(n.getSYMBOL());
                return false;
            }
            public boolean visit(symWithAttrs2 n) {
                fBuff.append(' ');
                fBuff.append(n.getSYMBOL());
                fBuff.append(n.getMACRO_NAME());
                return false;
            }
        });

	return fBuff.toString();
    }

    public void formatterStops() {
	// TODO Auto-generated method stub
    }
}
