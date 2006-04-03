package org.jikespg.uide;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.uide.runtime.SAFARIPluginBase;
import org.jikespg.uide.preferences.JikesPGPreferenceCache;
import org.jikespg.uide.preferences.PreferenceConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class JikesPGPlugin extends SAFARIPluginBase {
    public static final String kPluginID= "org.jikespg.uide";

    /**
     * The unique instance of this plugin class
     */
    protected static JikesPGPlugin sPlugin;

    public static JikesPGPlugin getInstance() {
        return sPlugin;
    }

    public JikesPGPlugin() {
        super();
        sPlugin= this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);

        // Initialize the JikesPGPreferences fields with the preference store data.
        IPreferenceStore prefStore= getPreferenceStore();

        JikesPGPreferenceCache.builderEmitMessages= prefStore.getBoolean(PreferenceConstants.P_EMIT_MESSAGES);
        JikesPGPreferenceCache.useDefaultExecutable= prefStore.getBoolean(PreferenceConstants.P_USE_DEFAULT_EXEC);
        JikesPGPreferenceCache.jikesPGExecutableFile= prefStore.getString(PreferenceConstants.P_JIKESPG_EXEC_PATH);
        JikesPGPreferenceCache.useDefaultTemplates= prefStore.getBoolean(PreferenceConstants.P_USE_DEFAULT_TEMPLATE_DIR);
        JikesPGPreferenceCache.jikesPGTemplateDir= prefStore.getString(PreferenceConstants.P_JIKESPG_TEMPLATE_DIR);

        fEmitInfoMessages= JikesPGPreferenceCache.builderEmitMessages;
    }

    public static final IPath ICONS_PATH= new Path("icons/"); //$NON-NLS-1$

    protected void initializeImageRegistry(ImageRegistry reg) {
        IPath path= ICONS_PATH.append("default.gif");//$NON-NLS-1$
        ImageDescriptor imageDescriptor= createImageDescriptor(getInstance().getBundle(), path);
        reg.put(IJikesPGResources.DEFAULT_AST, imageDescriptor);
    }

    public static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path) {
        URL url= Platform.find(bundle, path);
        if (url != null) {
            return ImageDescriptor.createFromURL(url);
        }
        return null;
    }

    public String getID() {
        return kPluginID;
    }
}
