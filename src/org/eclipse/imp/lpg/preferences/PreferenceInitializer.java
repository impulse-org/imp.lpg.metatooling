package org.jikespg.uide.preferences;


import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.uide.preferences.ISafariPreferencesService;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.builder.JikesPGBuilder;
import org.osgi.framework.Bundle;

/**
 * Initializes JikesPG preference default values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    /*
     * (non-Javadoc)
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
    	
	    // SMS 8 Sep 2006
	    // Use the service instead of the store
	    ///*
    	// Commented this back in so that the original preferences
    	// page would have some values to display
		IPreferenceStore store= JikesPGPlugin.getInstance().getPreferenceStore();
	
		store.setDefault(PreferenceConstants.P_EMIT_MESSAGES, true);
		store.setDefault(PreferenceConstants.P_GEN_LISTINGS, false);
		store.setDefault(PreferenceConstants.P_USE_DEFAULT_EXEC, true);
		store.setDefault(PreferenceConstants.P_JIKESPG_EXEC_PATH, JikesPGBuilder.getDefaultExecutablePath());
		store.setDefault(PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, true);
		store.setDefault(PreferenceConstants.P_JIKESPG_INCLUDE_DIRS, JikesPGBuilder.getDefaultIncludePath());
		store.setDefault(PreferenceConstants.P_EXTENSION_LIST, "g,lpg,gra");
		store.setDefault(PreferenceConstants.P_NON_ROOT_EXTENSION_LIST, "gi");
	    //*/
    
		ISafariPreferencesService service = JikesPGPlugin.getPreferencesService();
		
		// Examples:
		service.setBooleanPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_EMIT_MESSAGES, true);
		service.setBooleanPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_GEN_LISTINGS, false);
		service.setBooleanPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_USE_DEFAULT_EXEC, true);
		service.setStringPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_JIKESPG_EXEC_PATH, JikesPGBuilder.getDefaultExecutablePath());
		service.setBooleanPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_USE_DEFAULT_INCLUDE_DIR, true);		
		service.setStringPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_JIKESPG_INCLUDE_DIRS, JikesPGBuilder.getDefaultIncludePath());
		service.setStringPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_EXTENSION_LIST, "g,lpg,gra");
		service.setStringPreference(ISafariPreferencesService.DEFAULT_LEVEL, PreferenceConstants.P_NON_ROOT_EXTENSION_LIST, "gi");
    	
    
    }
    
    
    // SMS 28 Sep 2006
    // The following included in case of emergency (i.e., if, for some reason, JikesGPBuilder is not accessible,
    // then preference values can be initialized using the following routines, copied from that class)
    
    public static final String LPG_PLUGIN_ID= "lpg";
    
    public static String getDefaultExecutablePath() {
    	Bundle bundle= Platform.getBundle(LPG_PLUGIN_ID);
    	String os= Platform.getOS();
    	String plat= Platform.getOSArch();
    	Path path= new Path("bin/lpg-" + os + "_" + plat + (os.equals("win32") ? ".exe" : ""));
    	URL execURL= Platform.find(bundle, path);

    	if (execURL == null) {
    	    String errMsg= "Unable to find JikesPG executable at " + path + " in bundle " + bundle.getSymbolicName();

    	    JikesPGPlugin.getInstance().writeErrorMsg(errMsg);
    	    throw new IllegalArgumentException(errMsg);
    	} else {
    	    // N.B.: The jikespg executable will normally be inside a jar file,
    	    //       so use asLocalURL() to extract to a local file if needed.
    	    URL url;

    	    try {
    		url= Platform.asLocalURL(execURL);
    	    } catch (IOException e) {
    		JikesPGPlugin.getInstance().writeErrorMsg("Unable to locate default JikesPG executable." + e.getMessage());
    		return "???";
    	    }

    	    String jikesPGExecPath= url.getFile();

    	    if (os.equals("win32")) // remove leading slash from URL that shows up on Win32(?)
    		jikesPGExecPath= jikesPGExecPath.substring(1);

    	    JikesPGPlugin.getInstance().maybeWriteInfoMsg("JikesPG executable apparently at '" + jikesPGExecPath + "'.");
    	    return jikesPGExecPath;
    	}
    }
    
    
    public static String getDefaultIncludePath() {
    	Bundle bundle= Platform.getBundle(LPG_PLUGIN_ID);

    	try {
    	    // Use getEntry() rather than getResource(), since the "templates" folder is
    	    // no longer inside the plugin jar (which is now expanded upon installation).
    	    String tmplPath= Platform.asLocalURL(bundle.getEntry("templates")).getFile();

    	    if (Platform.getOS().equals("win32"))
    		tmplPath= tmplPath.substring(1);
    	    return tmplPath;
    	} catch(IOException e) {
    	    return null;
    	}
        }
    
}
