package org.jikespg.uide.search;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.Match;
import org.jikespg.uide.utils.StreamUtils;

public class JikesPGSearchQuery implements ISearchQuery {
    private String fEntityRegexp;

    private boolean fIsNonTerm;

    private JikesPGSearchResult fResult;

    private JikesPGSearchScope fScope;

    public JikesPGSearchQuery(String entityRegexp, boolean isNonTerm, JikesPGSearchScope scope) {
        fEntityRegexp= entityRegexp;
        fIsNonTerm= isNonTerm;
        fScope= scope;
        fResult= new JikesPGSearchResult(this);
    }

    public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
        final Pattern pat= Pattern.compile(fEntityRegexp);

        for(Iterator iter= fScope.getProjects().iterator(); iter.hasNext(); ) {
            IProject p= (IProject) iter.next();

            try {
                p.accept(new IResourceVisitor() {
                    public boolean visit(IResource resource) throws CoreException {
                        if (resource instanceof IFile) {
                            IFile file= (IFile) resource;
                            String exten= file.getFileExtension();

                            if (exten != null && (exten.equals("g") || exten.equals("gi"))) {
                                String contents= StreamUtils.readStreamContents(file.getContents(), ResourcesPlugin.getEncoding());
                                Matcher matcher= pat.matcher(contents);

                                while (matcher.find()) {
                                    Match m= new Match(file, matcher.start(), matcher.end() - matcher.start());

                                    fResult.addMatch(m);
                                }
                            }
                        }
                        return true;
                    }
                });
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return new Status(IStatus.OK, "org.jikespg.uide", 0, "Search complete", null);
    }

    public String getLabel() {
        return (fIsNonTerm ? "non-terminal '" : "terminal '") + fEntityRegexp + "'";
    }

    public boolean canRerun() {
        return false;
    }

    public boolean canRunInBackground() {
        return true;
    }

    public ISearchResult getSearchResult() {
        return fResult;
    }

    public JikesPGSearchScope getScope() {
        return fScope;
    }
}
