/**
 * 
 */
package org.jikespg.uide.search;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

class JikesPGSearchResult extends AbstractTextSearchResult implements IFileMatchAdapter, IEditorMatchAdapter {
    JikesPGSearchQuery fQuery;

    public JikesPGSearchResult(JikesPGSearchQuery q) {
        super();
        fQuery= q;

//      addBogusMatches();
    }

    private void addBogusMatches() {
        Set/*<IProject>*/ projects= fQuery.getScope().getProjects();
        IProject jikesPGTestProject= null;

        for(Iterator iter= projects.iterator(); iter.hasNext(); ) {
            IProject project= (IProject) iter.next();
            if (project.getName().equals("JikesPGTest"))
                jikesPGTestProject= project;
        }
        if (jikesPGTestProject == null) {
            System.err.println("Couldn't find test project");
        } else {
            IFile file= jikesPGTestProject.getFile("grammar.g");

            addMatch(new Match(file, 0, 10));
            addMatch(new Match(file, 100, 10));
        }
    }

    public IEditorMatchAdapter getEditorMatchAdapter() {
        return this;
    }

    public IFileMatchAdapter getFileMatchAdapter() {
        return this;
    }

    public String getLabel() {
        return fQuery.getLabel();
    }

    public String getTooltip() {
        return "heh?";
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public ISearchQuery getQuery() {
        return fQuery;
    }

    public Match[] computeContainedMatches(AbstractTextSearchResult result, IFile file) {
        return getMatches(file);
    }

    public IFile getFile(Object element) {
        return (IFile) element;
    }

    public boolean isShownInEditor(Match match, IEditorPart editor) {
        IEditorInput editorInput= editor.getEditorInput();

        if (editorInput instanceof IFileEditorInput)  {
            IFileEditorInput fileEditorInput= (IFileEditorInput) editorInput;
            IFile file= fileEditorInput.getFile();
            IFile matchFile= (IFile) match.getElement();

            return (file.equals(matchFile));
        }
        return false;
    }

    public Match[] computeContainedMatches(AbstractTextSearchResult result, IEditorPart editor) {
        IEditorInput editorInput= editor.getEditorInput();

        if (editorInput instanceof IFileEditorInput)  {
            IFileEditorInput fileEditorInput= (IFileEditorInput) editorInput;

            return computeContainedMatches(result, fileEditorInput.getFile());
        }
        return new Match[0];
    }
}
