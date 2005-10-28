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

    public TokenColorer() {
	Display display= Display.getCurrent();

	COMMENT= new TextAttribute(display.getSystemColor(SWT.COLOR_DARK_CYAN), null, SWT.ITALIC);

	SYMBOL= new TextAttribute(display.getSystemColor(SWT.COLOR_RED), null, SWT.NORMAL);

	KEYWORD= new TextAttribute(display.getSystemColor(SWT.COLOR_BLUE), null, SWT.BOLD);
    }

    public TextAttribute getColoring(IParseController controller, IToken token) {
	if (controller.isKeyword(token.getKind()))
	    return KEYWORD;
	else if (token.getKind() == JikesPGLexer.TK_SYMBOL)
	    return SYMBOL;
	return null;
    }
}
