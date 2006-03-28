/*
 * Created on Mar 24, 2006
 */
package org.jikespg.uide.editor;

import org.eclipse.uide.core.ILanguageService;
import org.eclipse.uide.editor.ISourceFormatter;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.*;

public class JikesPGFormatter implements ILanguageService, ISourceFormatter {

    public void formatterStarts(String initialIndentation) {
	// TODO Auto-generated method stub
    }

    public String format(IParseController parseController, String content, boolean isLineStart, String indentation, int[] positions) {
        final StringBuffer buff= new StringBuffer();
        JikesPG root= (JikesPG) parseController.getCurrentAst();

        root.accept(new JikesPGParser.AbstractVisitor() {
            private int prodCount;
            private int prodIndent;
            public void unimplementedVisitor(String s) {
                System.out.println("Unhandled node type: " + s);
            }
            public boolean visit(option_spec n) {
                buff.append("%options ");
                return true;
            }
            public boolean visit(option_list n) {
//                if (n.getoption_list() != null)
//                    buff.append(',');
                return true;
            }
            public void endVisit(option_list n) {
                if (n.getoption_list() != null)
                    buff.append(',');
            }
            public void endVisit(option_spec n) {
                buff.append('\n');
            }
            public boolean visit(option n) {
                buff.append(n.getSYMBOL());
                return true;
            }
            public boolean visit(option_value0 n) {
                buff.append("=" + n.getSYMBOL());
                return false;
            }
            public boolean visit(option_value1 n) {
                buff.append('(');
                SYMBOLList symList= n.getsymbol_list();
                for(int i=0; i < symList.size(); i++) {
                    if (i > 0) buff.append(',');
                    buff.append(symList.getSYMBOLAt(i));
                }
                buff.append(')');
                return false;
            }
            public boolean visit(GlobalsSeg n) {
                buff.append("$Globals\n");
                return true;
            }
            public void endVisit(GlobalsSeg n) {
                buff.append("$End\n\n");
            }
            public boolean visit(globals_segment1 n) {
                buff.append("    ");
                return true;
            }
            public boolean visit(DefineSeg n) {
                buff.append("$Define\n");
                return true;
            }
            public void endVisit(DefineSeg n) {
                buff.append("$End\n\n");
            }
            public void endVisit(define_segment1 n) {
                buff.append("    ");
                buff.append(n.getmacro_name_symbol());
                buff.append(' ');
                buff.append(n.getmacro_segment());
                buff.append('\n');
            }
            public boolean visit(TerminalsSeg n) {
                buff.append("$Terminals\n");
                return true;
            }
            public void endVisit(TerminalsSeg n) {
                buff.append("$End\n\n");
            }
            public boolean visit(terminal n) {
                buff.append("    " + n.getterminal_symbol());
                if (n.getoptTerminalAlias() != null)
                    buff.append(" ::= " + n.getoptTerminalAlias().getname());
                buff.append('\n');
                return false;
            }
            public boolean visit(RulesSeg n) {
                buff.append("$Rules\n");
                rules_segment rulesSegment= n.getrules_segment();
                nonTermList nonTermList= rulesSegment.getnonTermList();
                int maxLHSSymWid= 0;
                for(int i=0; i < nonTermList.size(); i++) {
                    int lhsSymWid= nonTermList.getElementAt(i).getLeftIToken().toString().length();
                    if (lhsSymWid > maxLHSSymWid) maxLHSSymWid= lhsSymWid;
                }
                prodIndent= 4 + maxLHSSymWid + 1;
                return true;
            }
            public void endVisit(RulesSeg n) {
                buff.append("$End\n");
            }
            public boolean visit(nonTerm n) {
                buff.append("    ");
                buff.append(n.getSYMBOL());
                if (n.getclassName() != null)
                    buff.append(n.getclassName());
                if (n.getarrayElement() != null)
                    buff.append(n.getarrayElement());
                for(int i=n.getSYMBOL().toString().length() + 4 + 1; i <= prodIndent; i++)
                    buff.append(' ');
                buff.append(n.getproduces());
                prodCount= 0;
//                prodIndent= 4 + n.getSYMBOL().toString().length() + 1;
                return true;
            }
            public void endVisit(nonTerm n) {
                buff.append('\n');
            }
            public boolean visit(rhs n) {
                if (prodCount > 0) {
                    buff.append('\n');
                    for(int i=0; i < prodIndent; i++)
                        buff.append(' ');
                    buff.append("|  ");
                }
                prodCount++;
                return true;
            }
            public boolean visit(action_segment n) {
                buff.append(n.getBLOCK());
                buff.append('\n');
                return false;
            }
            public boolean visit(symWithAttrs0 n) {
                buff.append(' ');
                buff.append(n.getEMPTY_KEY());
                return false;
            }
            public boolean visit(symWithAttrs1 n) {
                buff.append(' ');
                buff.append(n.getSYMBOL());
                return false;
            }
            public boolean visit(symWithAttrs2 n) {
                buff.append(' ');
                buff.append(n.getSYMBOL());
                buff.append(n.getMACRO_NAME());
                return false;
            }
        });

	return buff.toString();
    }

    public void formatterStops() {
	// TODO Auto-generated method stub
    }
}
