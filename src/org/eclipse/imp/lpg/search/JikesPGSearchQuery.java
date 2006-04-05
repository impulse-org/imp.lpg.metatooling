package org.jikespg.uide.search;

import java.util.Iterator;

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
    private String fEntityName;

    private boolean fIsNonTerm;

    private JikesPGSearchResult fResult;

    private JikesPGSearchScope fScope;

    public JikesPGSearchQuery(String entityName, boolean isNonTerm, JikesPGSearchScope scope) {
        fEntityName= entityName;
        fIsNonTerm= isNonTerm;
        fScope= scope;
        fResult= new JikesPGSearchResult(this);
    }

    public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
        for(Iterator iter= fScope.getProjects().iterator(); iter.hasNext(); ) {
            IProject p= (IProject) iter.next();

            try {
                p.accept(new IResourceVisitor() {
                    public boolean visit(IResource resource) throws CoreException {
                        if (resource instanceof IFile) {
                            IFile file= (IFile) resource;
                            String exten= file.getFileExtension();

                            if (exten != null && exten.equals("g") || exten.equals("gi")) {
                                String contents= StreamUtils.readStreamContents(file.getContents(), ResourcesPlugin.getEncoding());
                                int idx= contents.indexOf(fEntityName);

                                if (idx >= 0) {
                                    Match m= new Match(file, idx, fEntityName.length());
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
        return "JikesPG grammar search";
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
