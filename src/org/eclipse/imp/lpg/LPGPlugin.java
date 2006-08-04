package org.jikespg.uide;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

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
        JikesPGPreferenceCache.useDefaultIncludeDir= prefStore.getBoolean(PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR);
        JikesPGPreferenceCache.jikesPGIncludeDirs= prefStore.getString(PreferenceConstants.P_JIKESPG_INCLUDE_DIRS);
        JikesPGPreferenceCache.rootExtensionList= new HashSet();
        JikesPGPreferenceCache.rootExtensionList.addAll(Arrays.asList(prefStore.getString(PreferenceConstants.P_EXTENSION_LIST).split(",")));
        JikesPGPreferenceCache.nonRootExtensionList= new HashSet();
        JikesPGPreferenceCache.nonRootExtensionList.addAll(Arrays.asList(prefStore.getString(PreferenceConstants.P_NON_ROOT_EXTENSION_LIST).split(",")));

        fEmitInfoMessages= JikesPGPreferenceCache.builderEmitMessages;
    }

    public static final IPath ICONS_PATH= new Path("icons/"); //$NON-NLS-1$

    protected void initializeImageRegistry(ImageRegistry reg) {
        IPath path= ICONS_PATH.append("default.gif");//$NON-NLS-1$
        ImageDescriptor imageDescriptor= createImageDescriptor(getInstance().getBundle(), path);
        reg.put(IJikesPGResources.DEFAULT_AST, imageDescriptor);

        path= ICONS_PATH.append("outline_item.gif");//$NON-NLS-1$
        imageDescriptor= createImageDescriptor(getInstance().getBundle(), path);
        reg.put(IJikesPGResources.OUTLINE_ITEM, imageDescriptor);

        path= ICONS_PATH.append("grammarfile.gif");//$NON-NLS-1$
        imageDescriptor= createImageDescriptor(getInstance().getBundle(), path);
        reg.put(IJikesPGResources.GRAMMAR_FILE, imageDescriptor);

        path= ICONS_PATH.append("grammarfile-warning.jpg");//$NON-NLS-1$
        imageDescriptor= createImageDescriptor(getInstance().getBundle(), path);
        reg.put(IJikesPGResources.GRAMMAR_FILE_WARNING, imageDescriptor);

        path= ICONS_PATH.append("grammarfile-error.jpg");//$NON-NLS-1$
        imageDescriptor= createImageDescriptor(getInstance().getBundle(), path);
        reg.put(IJikesPGResources.GRAMMAR_FILE_ERROR, imageDescriptor);
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
