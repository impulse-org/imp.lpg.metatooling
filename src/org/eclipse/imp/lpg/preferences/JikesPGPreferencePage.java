package org.jikespg.uide.preferences;

import java.io.File;
import java.util.HashSet;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.builder.JikesPGBuilder;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into
 * JFace that allows us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that
 * belongs to the main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */
public class JikesPGPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private FileFieldEditor fExecField;
    private DirectoryListFieldEditor fTemplateField;

    public JikesPGPreferencePage() {
	super(GRID);
	setPreferenceStore(JikesPGPlugin.getInstance().getPreferenceStore());
	setDescription("Preferences for the JikesPG parser/lexer generator");
    }

    /**
     * Bogus BooleanFieldEditor extension to work around single-listener restriction.
     * @author rfuhrer
     */
    class BooleanFieldEditorExtraListener extends BooleanFieldEditor {
	private final IPropertyChangeListener fExtraListener;

	public BooleanFieldEditorExtraListener(String name, String foo, Composite parent, IPropertyChangeListener extraListener) {
	    super(name, foo, parent);
	    this.fExtraListener= extraListener;
	}
	
	protected void valueChanged(boolean oldValue, boolean newValue) {
	    super.valueChanged(oldValue, newValue);
	    if (oldValue != newValue && fExtraListener != null)
		fExtraListener.propertyChange(new PropertyChangeEvent(this,
			VALUE, new Boolean(oldValue), new Boolean(newValue)));
	}
    }

    class DirectoryListFieldEditor extends StringButtonFieldEditor {
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

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks
     * needed to manipulate various types of preferences. Each field editor knows how to
     * save and restore itself.
     */
    public void createFieldEditors() {
	final BooleanFieldEditor useDefaultExecField= new BooleanFieldEditorExtraListener(PreferenceConstants.P_USE_DEFAULT_EXEC, "Use default generator executable", getFieldEditorParent(),
	    new IPropertyChangeListener() {
	    	public void propertyChange(PropertyChangeEvent event) {
	    	    if (event.getNewValue().equals(Boolean.TRUE)) {
	    		fExecField.setEnabled(false, JikesPGPreferencePage.this.getFieldEditorParent());
			fExecField.setStringValue(JikesPGBuilder.getDefaultExecutablePath());
	    	    } else
	    		fExecField.setEnabled(true, JikesPGPreferencePage.this.getFieldEditorParent());
	    	}
	    });
	fExecField= new FileFieldEditor(PreferenceConstants.P_JIKESPG_EXEC_PATH, "Generator e&xecutable:", getFieldEditorParent());

	final BooleanFieldEditor useDefaultTemplateField= new BooleanFieldEditorExtraListener(PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, "Use default generator include path", getFieldEditorParent(),
	    new IPropertyChangeListener() {
	    	public void propertyChange(PropertyChangeEvent event) {
	    	    if (event.getNewValue().equals(Boolean.TRUE)) {
	    		fTemplateField.setEnabled(false, JikesPGPreferencePage.this.getFieldEditorParent());
			fTemplateField.setStringValue(JikesPGBuilder.getDefaultIncludePath());
	    	    } else
	    		fTemplateField.setEnabled(true, JikesPGPreferencePage.this.getFieldEditorParent());
	    	}
	    });
	fTemplateField= new DirectoryListFieldEditor(PreferenceConstants.P_JIKESPG_INCLUDE_DIRS, "&Include directories:", getFieldEditorParent());

	addField(useDefaultExecField);
	addField(fExecField);
	addField(useDefaultTemplateField);
	addField(fTemplateField);
	addField(new StringFieldEditor(PreferenceConstants.P_EXTENSION_LIST, "File-name &extensions to process:",
		getFieldEditorParent()));
	addField(new BooleanFieldEditor(PreferenceConstants.P_EMIT_MESSAGES, "Emit &diagnostic messages during build",
		getFieldEditorParent()));
	addField(new BooleanFieldEditor(PreferenceConstants.P_GEN_LISTINGS, "Generate &listing files",
		getFieldEditorParent()));

//	addField(new RadioGroupFieldEditor(PreferenceConstants.P_CHOICE, "An example of a multiple-choice preference", 1,
//		new String[][] { { "&Choice 1", "choice1" }, { "C&hoice 2", "choice2" } }, getFieldEditorParent()));

	if (JikesPGPreferenceCache.useDefaultExecutable)
	    fExecField.setEnabled(false, getFieldEditorParent());
	if (JikesPGPreferenceCache.useDefaultIncludeDir)
	    fTemplateField.setEnabled(false, getFieldEditorParent());

	getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
	    public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.P_EMIT_MESSAGES))
		    JikesPGPreferenceCache.builderEmitMessages= ((Boolean) event.getNewValue()).booleanValue();
		else if (event.getProperty().equals(PreferenceConstants.P_GEN_LISTINGS))
		    JikesPGPreferenceCache.generateListing= ((Boolean) event.getNewValue()).booleanValue();
		else if (event.getProperty().equals(PreferenceConstants.P_JIKESPG_EXEC_PATH))
		    JikesPGPreferenceCache.jikesPGExecutableFile= (String) event.getNewValue();
		else if (event.getProperty().equals(PreferenceConstants.P_JIKESPG_INCLUDE_DIRS))
		    JikesPGPreferenceCache.jikesPGIncludeDirs= (String) event.getNewValue();
		else if (event.getProperty().equals(PreferenceConstants.P_USE_DEFAULT_EXEC)) {
		    JikesPGPreferenceCache.useDefaultExecutable= ((Boolean) event.getNewValue()).booleanValue();
		    if (event.getNewValue().equals(Boolean.TRUE)) {
			fExecField.setEnabled(false, getFieldEditorParent());
			fExecField.setStringValue(JikesPGBuilder.getDefaultExecutablePath());
		    } else {
			fExecField.setEnabled(true, getFieldEditorParent());
		    }
		} else if (event.getProperty().equals(PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR)) {
		    JikesPGPreferenceCache.useDefaultIncludeDir= ((Boolean) event.getNewValue()).booleanValue();
		    if (event.getNewValue().equals(Boolean.TRUE)) {
			fTemplateField.setEnabled(false, getFieldEditorParent());
			fTemplateField.setStringValue(JikesPGBuilder.getDefaultIncludePath());
		    } else {
			fTemplateField.setEnabled(true, getFieldEditorParent());
		    }
		} else if (event.getProperty().equals(PreferenceConstants.P_EXTENSION_LIST)) {
		    JikesPGPreferenceCache.extensionList= new HashSet();
		    String[] extens= ((String) event.getNewValue()).split(",");
		    for(int i= 0; i < extens.length; i++) {
			JikesPGPreferenceCache.extensionList.add(extens[i]);
		    }
		}
	    }
	});
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {}
}
