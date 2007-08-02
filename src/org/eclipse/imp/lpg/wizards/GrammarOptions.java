/*
 * Created on Feb 28, 2006
 */
package org.eclipse.imp.lpg.uide.wizards;

public class GrammarOptions {
    String fLanguageName;

    String fProjectName= "";

    String fPackageName= "";

    String fTemplateKind;

    String fTargetLanguage;
    
    String fClassNamePrefix;  // Typically language name with upper case initial
    
    boolean fHasKeywords;

    boolean fRequiresBacktracking;

    boolean fAutoGenerateASTs;
    
    boolean fGenGrammarFiles;


    public boolean getAutoGenerateASTs() {
        return fAutoGenerateASTs;
    }

    public void setAutoGenerateASTs(boolean autoGenerateASTs) {
        fAutoGenerateASTs= autoGenerateASTs;
    }

    
    public boolean getGenGrammarFiles() {
        return fGenGrammarFiles;
    }

    public void setGenGrammarFiles(boolean genGrammarFiles) {
    	fGenGrammarFiles= genGrammarFiles;
    }
    
    
    public boolean getHasKeywords() {
        return fHasKeywords;
    }

    public void setHasKeywords(boolean hasKeywords) {
        fHasKeywords= hasKeywords;
    }

    public String getLanguageName() {
        return fLanguageName;
    }

    public void setLanguageName(String languageName) {
        fLanguageName= languageName;
    }

    public String getPackageName() {
        return fPackageName;
    }

    public void setPackageName(String packageName) {
        fPackageName= packageName;
    }

    public boolean getRequiresBacktracking() {
        return fRequiresBacktracking;
    }

    public void setRequiresBacktracking(boolean requiresBacktracking) {
        fRequiresBacktracking= requiresBacktracking;
    }

    public String getTargetLanguage() {
        return fTargetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        fTargetLanguage= targetLanguage;
    }

    public String getTemplateKind() {
        return fTemplateKind;
    }

    public void setTemplateKind(String templateKind) {
        fTemplateKind= templateKind;
    }

    public String getProjectName() {
        return fProjectName;
    }

    public void setProjectName(String projectName) {
        fProjectName= projectName;
    }

    
    public String getClassNamePrefix() {
    	return fClassNamePrefix;
    }
    
    
    public void setClassNamePrefix(String prefix) {
    	fClassNamePrefix = prefix;
    }
    
    
    public String getPackageForLanguage(String language) {
	// RMF 2/23/2007 - Disabled the following test, so that the package name tracks
	// changes in the language name, rather than getting set once when the first
	// language name is passed in.
//        if (getPackageName().length() == 0) {
            StringBuffer buffer= new StringBuffer(/*"org."*/);
            for(int n= 0; n < language.length(); n++) {
        	char c= Character.toLowerCase(language.charAt(n));
    
        	if (Character.isJavaIdentifierPart(c))
        	    buffer.append(c);
            }
            buffer.append(".safari.parser");
            setPackageName(buffer.toString());
//        }
        return getPackageName();
    }
    
    
    protected String upperCaseFirst(String language) {
    	return Character.toUpperCase(language.charAt(0)) + language.substring(1);
    }
    
    
    public String getDefaultQualifiedNameForParseController(String language) {
        String langClass= upperCaseFirst(language);
        return getPackageForLanguage(language) + "." + langClass + "ParseController";
    }
    
    public String getDefaultQualifiedNameForNodeLocator(String language) {
        String langClass= upperCaseFirst(language);
        return getPackageForLanguage(language) + "." + langClass + "ASTNodeLocator";
    }
    
    
    public String getDefaultSimpleNameForParseController(String language) {
        String langClass= upperCaseFirst(language);
        return langClass + "ParseController";
    }
    
    public String getDefaultSimpleNameForNodeLocator(String language) {
        String langClass= upperCaseFirst(language);
        return langClass + "ASTNodeLocator";
    }
    
}