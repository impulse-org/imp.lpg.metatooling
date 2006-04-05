package org.jikespg.uide.search;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public abstract class JikesPGSearchContentProvider implements IStructuredContentProvider {
    protected static final Object[] EMPTY_ARR= new Object[0];

    protected JikesPGSearchResult fResult;

    protected JikesPGSearchResultPage fPage;

    public JikesPGSearchContentProvider(JikesPGSearchResultPage page) {
        fPage= page;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        initialize((JikesPGSearchResult) newInput);
    }

    protected void initialize(JikesPGSearchResult result) {
        fResult= result;
    }

    public abstract void elementsChanged(Object[] updatedElements);

    public abstract void clear();

    public void dispose() {
        // nothing to do
    }

    protected JikesPGSearchResultPage getPage() {
        return fPage;
    }
}
