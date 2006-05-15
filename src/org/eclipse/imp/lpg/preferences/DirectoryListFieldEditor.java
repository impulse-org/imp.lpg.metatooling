/*
 * Created on May 15, 2006
 */
package org.jikespg.uide.preferences;

import java.io.File;

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
    protected boolean doCheckState() {
        String path= getTextControl().getText();

        if (path.length() == 0 && isEmptyStringAllowed())
    	return true;

        String[] pathElems= path.split(";");

        for(int i= 0; i < pathElems.length; i++) {
    	String pathElem= pathElems[i].trim();
    	File dir= new File(pathElem);

    	if (!dir.isDirectory())
    	    return false;
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