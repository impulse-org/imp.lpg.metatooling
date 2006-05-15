package org.jikespg.uide.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.builder.JikesPGBuilder;

/**
 * Initializes JikesPG preference default values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    /*
     * (non-Javadoc)
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
	IPreferenceStore store= JikesPGPlugin.getInstance().getPreferenceStore();

	store.setDefault(PreferenceConstants.P_EMIT_MESSAGES, true);
	store.setDefault(PreferenceConstants.P_GEN_LISTINGS, false);
	store.setDefault(PreferenceConstants.P_USE_DEFAULT_EXEC, true);
	store.setDefault(PreferenceConstants.P_JIKESPG_EXEC_PATH, JikesPGBuilder.getDefaultExecutablePath());
	store.setDefault(PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, true);
	store.setDefault(PreferenceConstants.P_JIKESPG_INCLUDE_DIRS, JikesPGBuilder.getDefaultIncludePath());
	store.setDefault(PreferenceConstants.P_EXTENSION_LIST, "g,lpg,gra");
	store.setDefault(PreferenceConstants.P_NON_ROOT_EXTENSION_LIST, "gi");
    }
}
