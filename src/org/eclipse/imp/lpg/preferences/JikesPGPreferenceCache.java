/*
 * Created on Oct 25, 2005
 */
package org.jikespg.uide.preferences;

/**
 * Caches the values of various JikesPG preferences from the preference store
 * for convenience and efficiency.<br>
 * May be removed at some point in lieu of direct access to the preference store.
 * @author rfuhrer
 */
public class JikesPGPreferenceCache {
    public static boolean builderEmitMessages;

    /**
     * If true, the builder will use the appropriate default JikesPG executable
     * from the "LPG" plugin's "bin" folder.
     */
    public static boolean useDefaultExecutable;

    /**
     * Absolute path of the JikesPG executable file. If useDefaultExecutable is true,
     * this is automatically set to point at the executable in the "LPG" plugin.
     */
    public static String jikesPGExecutableFile;

    /**
     * If true, the builder will specify the "templates" folder from the "LPG" plugin
     * as the "-include-directory" option.
     */
    public static boolean useDefaultTemplates;

    /**
     * Absolute path of the directory containing the JikesPG templates. If useDefaultTemplates
     * is true, this is automatically set to point at the "templates" folder in the "LPG" plugin.
     */
    public static String jikesPGTemplateDir;

    private JikesPGPreferenceCache() {}
}
