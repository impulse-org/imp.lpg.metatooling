/*
 * Created on Oct 28, 2005
 */
package org.jikespg.uide.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.uide.editor.ITokenColorer;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGLexer;
import org.jikespg.uide.parser.JikesPGParser;

import com.ibm.lpg.IToken;

public class TokenColorer implements ITokenColorer {
    static TextAttribute COMMENT;

    static TextAttribute SYMBOL;

    static TextAttribute KEYWORD;

    static TextAttribute LITERAL;

    static TextAttribute EMPTY;


    public TokenColorer() {
	Display display= Display.getCurrent();

	COMMENT= new TextAttribute(display.getSystemColor(SWT.COLOR_DARK_CYAN), null, SWT.ITALIC);
	SYMBOL= new TextAttribute(display.getSystemColor(SWT.COLOR_DARK_RED), null, SWT.NORMAL);
	KEYWORD= new TextAttribute(display.getSystemColor(SWT.COLOR_BLUE), null, SWT.BOLD);
	EMPTY= new TextAttribute(display.getSystemColor(SWT.COLOR_DARK_GRAY), null, SWT.BOLD);
	LITERAL= new TextAttribute(display.getSystemColor(SWT.COLOR_DARK_GRAY), null, SWT.ITALIC);
    }

    public TextAttribute getColoring(IParseController controller, IToken token) {
	if (token.getKind() == JikesPGLexer.TK_EMPTY_KEY)
	    return EMPTY;
	else if (controller.isKeyword(token.getKind()))
	    return KEYWORD;
	else if (token.getKind() == JikesPGLexer.TK_SYMBOL) {
	    char ch= controller.getLexer().getLexStream().getInputChars()[token.getStartOffset()];
	    if (ch == '\'' || ch == '"')
		return LITERAL;
	    return SYMBOL;
	} else if (token.getKind() == JikesPGLexer.TK_SINGLE_LINE_COMMENT)
	    return COMMENT;

	return null;
    }
}
