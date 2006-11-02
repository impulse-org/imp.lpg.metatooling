package org.jikespg.uide;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.uide.preferences.SafariPreferencesService;
import org.eclipse.uide.runtime.SAFARIPluginBase;
import org.jikespg.uide.preferences.PreferenceConstants;
import org.jikespg.uide.preferences.PreferenceInitializer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class JikesPGPlugin extends SAFARIPluginBase {
    public static final String kPluginID= "org.jikespg.uide";

    /**
     * The unique instance of this plugin class
     */
    protected static JikesPGPlugin sPlugin;
    
    // SMS 8 Sep 2006
    protected static SafariPreferencesService preferencesService = null;

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
        // SMS 8 Sep 2006
        // Switching preferences store to preferences service
        //IPreferenceStore prefStore= getPreferenceStore();
        if (preferencesService == null) {
        	preferencesService = new SafariPreferencesService();
        	preferencesService.setLanguageName("jikespg");
        	(new PreferenceInitializer()).initializeDefaultPreferences();
        }
        
        // SMS 8 Sep 2006
        // Trying preferences service without preferences cache
        /*
        JikesPGPreferenceCache.builderEmitMessages= prefStore.getBoolean(PreferenceConstants.P_EMIT_MESSAGES);
        JikesPGPreferenceCache.useDefaultExecutable= prefStore.getBoolean(PreferenceConstants.P_USE_DEFAULT_EXEC);
        JikesPGPreferenceCache.jikesPGExecutableFile= prefStore.getString(PreferenceConstants.P_JIKESPG_EXEC_PATH);
        JikesPGPreferenceCache.useDefaultIncludeDir= prefStore.getBoolean(PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR);
        JikesPGPreferenceCache.jikesPGIncludeDirs= prefStore.getString(PreferenceConstants.P_JIKESPG_INCLUDE_DIRS);
        JikesPGPreferenceCache.rootExtensionList= new HashSet();
        JikesPGPreferenceCache.rootExtensionList.addAll(Arrays.asList(prefStore.getString(PreferenceConstants.P_EXTENSION_LIST).split(",")));
        JikesPGPreferenceCache.nonRootExtensionList= new HashSet();
        JikesPGPreferenceCache.nonRootExtensionList.addAll(Arrays.asList(prefStore.getString(PreferenceConstants.P_NON_ROOT_EXTENSION_LIST).split(",")));
        */
        
        // SMS 8 Sep 2006 updated 30 Oct 2006
        // Not sure why the field fEmitInfoMessages is used in preference to the preference cache
        // (ask Bob F. about that).  If it is going to continue to be used, then it should probably
        // be initialized from the new preferences store instead of the deprecated preferences
        // cache.  (The field would seem to represent a sort of separate, single-value cache.)
        // Presently it doesn't actually seem to be used anywhere, at least within the JikesPG
        // UIDE, suggesting that it could safely be removed, at least insofar as JikesPG is concerned.
        // Potential users of the field would have to get the value from the preferences store,
        // but since the value is a preference, that doesn't seem inappropriate.
        //fEmitInfoMessages= JikesPGPreferenceCache.builderEmitMessages;
        fEmitInfoMessages = preferencesService.getBooleanPreference(PreferenceConstants.P_EMIT_MESSAGES);
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
    
    // SMS 8 Sep 2006
    //private final static IPreferencesService preferencesService = Platform.getPreferencesService();
    public static SafariPreferencesService getPreferencesService() {
    	if (preferencesService == null) {
    		preferencesService = new SafariPreferencesService();
        	preferencesService.setLanguageName("jikespg");
           	(new PreferenceInitializer()).initializeDefaultPreferences();
    	}
    	return preferencesService;
    }
    
    
    // SMS 30 Oct 2006
    // Overwriting method in SAFAIPluginBase because at that level we don't have
    // a preferences service to query dynamically, only a field set from this level
    // at the time of preference initialization
    public void maybeWriteInfoMsg(String msg) {
        //if (!fEmitInfoMessages)
        if (!preferencesService.getBooleanPreference(PreferenceConstants.P_EMIT_MESSAGES))
            return;
        writeInfoMsg(msg);
    }

    
}
