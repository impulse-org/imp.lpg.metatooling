/*
 * Created on Feb 28, 2006
 */
package org.jikespg.uide.wizards;

public class GrammarOptions {
    String fLanguageName;

    String fProjectName= "";

    String fPackageName= "";

    String fTemplateKind;

    String fTargetLanguage;

    boolean fHasKeywords;

    boolean fRequiresBacktracking;

    boolean fAutoGenerateASTs;

    public boolean getAutoGenerateASTs() {
        return fAutoGenerateASTs;
    }

    public void setAutoGenerateASTs(boolean autoGenerateASTs) {
        fAutoGenerateASTs= autoGenerateASTs;
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

    public String getPackageForLanguage(String language) {
        if (getPackageName().length() == 0) {
            StringBuffer buffer= new StringBuffer("org.");
            for(int n= 0; n < language.length(); n++) {
        	char c= Character.toLowerCase(language.charAt(n));
    
        	if (Character.isJavaIdentifierPart(c))
        	    buffer.append(c);
            }
            buffer.append(".parser");
            setPackageName(buffer.toString());
        }
        return getPackageName();
    }
}