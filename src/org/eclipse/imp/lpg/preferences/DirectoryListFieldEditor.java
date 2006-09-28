/*
 * Created on May 15, 2006
 */
package org.jikespg.uide.preferences;

import java.io.File;
import java.util.Stack;

import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;

public class DirectoryListFieldEditor extends StringButtonFieldEditor {
    /**
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public DirectoryListFieldEditor(String name, String labelText, Composite parent) {
        init(name, labelText);
        setErrorMessage("Contents must be a semicolon-separated list of existing directory paths.");
        setChangeButtonText("Browse...");
        setValidateStrategy(VALIDATE_ON_FOCUS_LOST);
        createControl(parent);
    }
    protected String changePressed() {
        Text text= getTextControl();
        String curText= text.getText();
        Point sel= text.getSelection();
        boolean replace= (text.getSelectionCount() > 0); // any non-empty selection?
        boolean replaceAll= text.getSelectionCount() == text.getCharCount(); // all selected?
        File f = new File(getTextControl().getText());

        if (!f.exists())
    	f = null;
        File d = getDirectory(f);
        if (d == null)
    	return null;

        final String dirPath= d.getAbsolutePath();

        if (replaceAll)
    	return dirPath;

        int prevSep= curText.lastIndexOf(';', sel.x);

        if (prevSep < 0)
    	prevSep= 0;

        String leadingPart= (prevSep > 0) ? curText.substring(0, prevSep) + ';' : "";

        if (replace) {
    	// Replace the selected entries with the browse result
    	int followSep= curText.indexOf(';', sel.y);

    	if (followSep < 0)
    	    followSep= curText.length();
    	return leadingPart + dirPath + curText.substring(followSep);
        }

        if (sel.x == text.getCharCount()) // caret at end of field? => append new entry
    	return curText + ';' + dirPath;

        // insertion point in middle of text; insert new entry before this entry
        return leadingPart + dirPath + (prevSep > 0 ? "" : ";") + curText.substring(prevSep);
    }
    
    
    // SMS 28 Sep 2006
    // Modified doCheckState() to tolerate the use of quotes (needed to
    // accommodate path entries with spaces).
    // Now checks that any single or double quotes in the list are properly
    // balanced and that any list segments between quotes represent valid
    // directory paths.
    
    protected boolean doCheckState() {
    	
        String path= getTextControl().getText();
        if (path.length() == 0 && isEmptyStringAllowed()) {
        	return true;
        }
        
        // Check for balanced quotes
        final String singleQuote = "'";
        final String doubleQuote = "\"";
        Stack stack = new Stack();
        for (int i = 0; i < path.length(); i++) {
        	if (path.charAt(i) == '\'') {
        		if (!stack.empty() && singleQuote.equals(stack.peek()))
        			stack.pop();
        		else
        			stack.push(singleQuote);
        	}
        	if (path.charAt(i) == '"') {
        		if (!stack.empty() && doubleQuote.equals(stack.peek()))
        			stack.pop();
        		else
        			stack.push(doubleQuote);
        	}
        }
        if (stack.size() != 0)
        	return false;

        
        // Now validate list segments between quotes
        path = path.replace("\"", "'");
        String[] splits = path.split("'");       
        boolean splitsVerified = true;
        for (int i = 0; i < splits.length; i++) {
        	splitsVerified = splitsVerified && doCheckState(splits[i]);
        	if (!splitsVerified) return false;
        }
        return true;
    }
    
    
    protected boolean doCheckState(String path)
    {	// This is the real work of the original doCheckState()
        if (path.length() == 0) {
        	return true;
        }
        
        String[] pathElems= path.split(";");

        for(int i= 0; i < pathElems.length; i++) {
	    	String pathElem= pathElems[i].trim();
	    	File dir= new File(pathElem);
	
	    	if (!dir.isDirectory()) {
	    		setErrorMessage("Path list contains a name that is not a directory name");
	    		return false;
	    	}
        }
        
        return true;
    }

    
    /**
     * Helper that opens the directory chooser dialog.
     * @param startingDirectory The directory the dialog will open in.
     * @return File File or <code>null</code>.
     * 
     */
    private File getDirectory(File startingDirectory) {
        DirectoryDialog fileDialog = new DirectoryDialog(getShell(), SWT.OPEN);
        if (startingDirectory != null)
    	fileDialog.setFilterPath(startingDirectory.getPath());
        String dir = fileDialog.open();
        if (dir != null) {
    	dir = dir.trim();
    	if (dir.length() > 0)
    	    return new File(dir);
        }
        return null;
    }
}