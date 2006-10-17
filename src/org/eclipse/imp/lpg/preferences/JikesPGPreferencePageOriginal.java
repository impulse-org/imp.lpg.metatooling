package org.jikespg.uide.preferences;

import java.util.HashSet;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
public class JikesPGPreferencePageOriginal extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private FileFieldEditor fExecField;
    private DirectoryListFieldEditor fTemplateField;

    public JikesPGPreferencePageOriginal() {
	super(GRID);
	setPreferenceStore(JikesPGPlugin.getInstance().getPreferenceStore());
	setDescription("Preferences for the JikesPG parser/lexer generator");
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
	    		fExecField.setEnabled(false, JikesPGPreferencePageOriginal.this.getFieldEditorParent());
			fExecField.setStringValue(JikesPGBuilder.getDefaultExecutablePath());
	    	    } else
	    		fExecField.setEnabled(true, JikesPGPreferencePageOriginal.this.getFieldEditorParent());
	    	}
	    });
	fExecField= new FileFieldEditor(PreferenceConstants.P_JIKESPG_EXEC_PATH, "Generator e&xecutable:", getFieldEditorParent());

	final BooleanFieldEditor useDefaultTemplateField= new BooleanFieldEditorExtraListener(PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, "Use default generator include path", getFieldEditorParent(),
	    new IPropertyChangeListener() {
	    	public void propertyChange(PropertyChangeEvent event) {
	    	    if (event.getNewValue().equals(Boolean.TRUE)) {
	    		fTemplateField.setEnabled(false, JikesPGPreferencePageOriginal.this.getFieldEditorParent());
			fTemplateField.setStringValue(JikesPGBuilder.getDefaultIncludePath());
	    	    } else
	    		fTemplateField.setEnabled(true, JikesPGPreferencePageOriginal.this.getFieldEditorParent());
	    	}
	    });
	fTemplateField= new DirectoryListFieldEditor(PreferenceConstants.P_JIKESPG_INCLUDE_DIRS, "&Include directories:", getFieldEditorParent());

	addField(useDefaultExecField);
	addField(fExecField);
	addField(useDefaultTemplateField);
	addField(fTemplateField);
	addField(new StringFieldEditor(PreferenceConstants.P_EXTENSION_LIST, "File-name &extensions to process:",
		getFieldEditorParent()));
	addField(new StringFieldEditor(PreferenceConstants.P_NON_ROOT_EXTENSION_LIST, "File-name extensions for in&cluded files:",
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
		    JikesPGPreferenceCache.rootExtensionList= new HashSet();
		    String[] extens= ((String) event.getNewValue()).split(",");
		    for(int i= 0; i < extens.length; i++) {
			JikesPGPreferenceCache.rootExtensionList.add(extens[i]);
		    }
		} else if (event.getProperty().equals(PreferenceConstants.P_NON_ROOT_EXTENSION_LIST)) {
		    JikesPGPreferenceCache.nonRootExtensionList= new HashSet();
		    String[] extens= ((String) event.getNewValue()).split(",");
		    for(int i= 0; i < extens.length; i++) {
			JikesPGPreferenceCache.nonRootExtensionList.add(extens[i]);
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
