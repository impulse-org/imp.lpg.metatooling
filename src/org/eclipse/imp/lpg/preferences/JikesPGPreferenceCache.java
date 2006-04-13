/*
 * Created on Oct 25, 2005
 */
package org.jikespg.uide.preferences;

import java.util.Set;

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
     * If true, the builder will use the "templates" folder from the "LPG" plugin
     * as the sole "-include-directory" option.
     */
    public static boolean useDefaultIncludeDir;

    /**
     * Semicolon-separated list of absolute paths of the directory containing included
     * grammar files (via template, include, or import). If useDefaultInclude is true,
     * this is automatically set to point at the "templates" folder in the "LPG" plugin.
     */
    public static String jikesPGIncludeDirs;

    /**
     * Set of Strings identifying the list of file-name extensions that will be processed by
     * the JikesPG generator.
     */
    public static Set/*<String>*/ extensionList;

    /**
     * If true, the generator will be instructed to create listing files when
     * processing grammar/lexer specification files.
     */
    public static boolean generateListing;

    private JikesPGPreferenceCache() {}
}
