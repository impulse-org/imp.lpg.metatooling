/*
 * Created on Mar 24, 2006
 */
package org.jikespg.uide.editor;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

public class JikesPGFormatter implements IFormattingStrategy {

    public void formatterStarts(String initialIndentation) {
	// TODO Auto-generated method stub
    }

    public String format(String content, boolean isLineStart, String indentation, int[] positions) {
	System.out.println("Hi Bob");
	return content;
    }

    public void formatterStops() {
	// TODO Auto-generated method stub
    }
}
