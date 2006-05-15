package org.jikespg.uide.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Bogus BooleanFieldEditor extension to work around single-listener restriction.
 * @author rfuhrer
 */
public class BooleanFieldEditorExtraListener extends BooleanFieldEditor {
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