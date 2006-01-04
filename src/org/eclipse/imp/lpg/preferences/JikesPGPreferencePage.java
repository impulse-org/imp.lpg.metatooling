package org.jikespg.uide.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
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
    private DirectoryFieldEditor fTemplateField;

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

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks
     * needed to manipulate various types of preferences. Each field editor knows how to
     * save and restore itself.
     */
    public void createFieldEditors() {
	final BooleanFieldEditor useDefaultExecField= new BooleanFieldEditorExtraListener(PreferenceConstants.P_USE_DEFAULT_EXEC, "Use default JikesPG executable", getFieldEditorParent(),
	    new IPropertyChangeListener() {
	    	public void propertyChange(PropertyChangeEvent event) {
	    	    if (event.getNewValue().equals(Boolean.TRUE)) {
	    		fExecField.setEnabled(false, JikesPGPreferencePage.this.getFieldEditorParent());
			fExecField.setStringValue(JikesPGBuilder.getDefaultExecutablePath());
	    	    } else
	    		fExecField.setEnabled(true, JikesPGPreferencePage.this.getFieldEditorParent());
	    	}
	    });
	fExecField= new FileFieldEditor(PreferenceConstants.P_JIKESPG_EXEC_PATH, "&JikesPG executable:", getFieldEditorParent());

	final BooleanFieldEditor useDefaultTemplateField= new BooleanFieldEditorExtraListener(PreferenceConstants.P_USE_DEFAULT_TEMPLATE_DIR, "Use default JikesPG template path", getFieldEditorParent(),
	    new IPropertyChangeListener() {
	    	public void propertyChange(PropertyChangeEvent event) {
	    	    if (event.getNewValue().equals(Boolean.TRUE)) {
	    		fTemplateField.setEnabled(false, JikesPGPreferencePage.this.getFieldEditorParent());
			fTemplateField.setStringValue(JikesPGBuilder.getDefaultTemplatePath());
	    	    } else
	    		fTemplateField.setEnabled(true, JikesPGPreferencePage.this.getFieldEditorParent());
	    	}
	    });
	fTemplateField= new DirectoryFieldEditor(PreferenceConstants.P_JIKESPG_TEMPLATE_DIR, "&JikesPG template directory:", getFieldEditorParent());

	addField(useDefaultExecField);
	addField(fExecField);
	addField(useDefaultTemplateField);
	addField(fTemplateField);
	addField(new BooleanFieldEditor(PreferenceConstants.P_EMIT_MESSAGES, "Emit diagnostic messages from the builder",
		getFieldEditorParent()));

//	addField(new RadioGroupFieldEditor(PreferenceConstants.P_CHOICE, "An example of a multiple-choice preference", 1,
//		new String[][] { { "&Choice 1", "choice1" }, { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
//	addField(new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));

	getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
	    public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.P_EMIT_MESSAGES))
		    JikesPGPreferenceCache.builderEmitMessages= ((Boolean) event.getNewValue()).booleanValue();
		else if (event.getProperty().equals(PreferenceConstants.P_JIKESPG_EXEC_PATH))
		    JikesPGPreferenceCache.jikesPGExecutableFile= (String) event.getNewValue();
		else if (event.getProperty().equals(PreferenceConstants.P_JIKESPG_TEMPLATE_DIR))
		    JikesPGPreferenceCache.jikesPGTemplateDir= (String) event.getNewValue();
		else if (event.getProperty().equals(PreferenceConstants.P_USE_DEFAULT_EXEC)) {
		    JikesPGPreferenceCache.useDefaultExecutable= ((Boolean) event.getNewValue()).booleanValue();
//		    if (event.getNewValue().equals(Boolean.TRUE)) {
//			execField.setEnabled(false, null);
//			execField.setStringValue(JikesPGBuilder.getDefaultExecutablePath());
//		    } else {
//			execField.setEnabled(true, execField.getParent());
//		    }
		} else if (event.getProperty().equals(PreferenceConstants.P_USE_DEFAULT_TEMPLATE_DIR)) {
		    JikesPGPreferenceCache.useDefaultTemplates= ((Boolean) event.getNewValue()).booleanValue();
//		    if (event.getNewValue().equals(Boolean.TRUE)) {
//			templateField.setEnabled(false, null);
//			templateField.setStringValue(JikesPGBuilder.getDefaultTemplatePath());
//		    } else {
//			templateField.setEnabled(true, null);
//		    }
		}
	    }
	});
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {}
}
