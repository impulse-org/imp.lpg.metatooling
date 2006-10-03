package org.jikespg.uide.refactoring;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.uide.core.ILanguageService;
import org.jikespg.uide.parser.GetChildrenVisitor;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.terminal;

import com.ibm.watson.safari.xform.pattern.matching.MatchResult;
import com.ibm.watson.safari.xform.pattern.matching.Matcher;
import com.ibm.watson.safari.xform.pattern.parser.ASTAdapterBase;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

public class JikesPGASTAdapter extends ASTAdapterBase implements ILanguageService {
    @Override
    protected Object getTargetType(Object astNode) {
        // The "targetType" concept probably won't make sense in JikesPG grammars
        // until we have a more principled treatment of macro variables.
        return super.getTargetType(astNode);
    }

    protected String getName(Object astNode) {
	ASTNode node= (ASTNode) astNode;
        if (node instanceof nonTerm)
            return ((nonTerm) node).getSYMBOL().toString();
        if (node instanceof terminal)
            return ((terminal) node).getterminal_symbol().toString();
        throw new IllegalArgumentException("AST node type " + node.getClass().getName() + " has no name!");
    }

    public Object[] getChildren(Object astNode) {
	GetChildrenVisitor v= new GetChildrenVisitor();
	((ASTNode) astNode).accept(v);
        return v.getChildren();
    }

    public boolean isInstanceOfType(Object astNode, String typeName) {
        try {
            return Class.forName("org.jikespg.uide.parser.JikesPGParser$" + typeName).isInstance(astNode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Set findAllMatches(final Matcher matcher, Object astRoot) {
        final Set/*<MatchContext>*/ result= new HashSet();

        ASTNode root= (ASTNode) astRoot;

        root.accept(new JikesPGParser.AbstractVisitor() {
            public boolean preVisit(ASTNode n) {
                MatchResult m= matcher.match(n);

                if (m != null)
                    result.add(m);
                return true; // keep looking for all matches
            }

	    public void unimplementedVisitor(String s) { }
        });
        return result;
    }

    public MatchResult findNextMatch(final Matcher matcher, Object astRoot, final int matchStartPos) {
        final MatchResult[] result= new MatchResult[1];
        ASTNode root= (ASTNode) astRoot;

        root.accept(new JikesPGParser.AbstractVisitor() {
            public boolean preVisit(ASTNode n) {
                if (result[0] != null)
                    return false; // already have a match
                else {
                    int nodePos= n.getLeftIToken().getStartOffset();

                    if (matchStartPos < nodePos) {
                	MatchResult m= matcher.match(n);

                	if (m != null) {
                	    result[0]= m;
                	    return false; // got a match
                	}
                    }
                }
                return true; // no match; keep looking
            }

	    public void unimplementedVisitor(String s) { }
        });
        return result[0];
    }

    public String getFile(Object astNode) {
	ASTNode node= (ASTNode) astNode;

	return node.getLeftIToken().getPrsStream().getFileName();
    }

    public int getOffset(Object astNode) {
	ASTNode node= (ASTNode) astNode;

	return node.getLeftIToken().getStartOffset();
    }

    public int getLength(Object astNode) {
	ASTNode node= (ASTNode) astNode;

	return node.getRightIToken().getEndOffset() - node.getLeftIToken().getStartOffset() + 1;
    }

    public Object construct(String qualName, Object[] children) throws IllegalArgumentException {
        return super.construct(qualName, children);
    }

    public Object construct(String qualName, Object[] children, Map attribs) throws IllegalArgumentException {
        return super.construct(qualName, children, attribs);
    }
}
