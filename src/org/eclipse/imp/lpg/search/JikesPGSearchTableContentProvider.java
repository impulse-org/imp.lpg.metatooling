package org.jikespg.uide.search;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.TableViewer;

public class JikesPGSearchTableContentProvider extends JikesPGSearchContentProvider {
    public JikesPGSearchTableContentProvider(JikesPGSearchResultPage page) {
        super(page);
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof JikesPGSearchResult) {
            Set filteredElements= new HashSet();
            Object[] rawElements= ((JikesPGSearchResult) inputElement).getElements();

            for(int i= 0; i < rawElements.length; i++) {
                // if (getPage().getDisplayedMatchCount(rawElements[i]) > 0)
                filteredElements.add(rawElements[i]);
            }
            return filteredElements.toArray();
        }
        return EMPTY_ARR;
    }

    public void elementsChanged(Object[] updatedElements) {
        if (fResult == null)
            return;
        int addCount= 0;
        int removeCount= 0;
        TableViewer viewer= (TableViewer) getPage().getViewer();
        Set updated= new HashSet();
        Set added= new HashSet();
        Set removed= new HashSet();
        for(int i= 0; i < updatedElements.length; i++) {
            if (getPage().getDisplayedMatchCount(updatedElements[i]) > 0) {
                if (viewer.testFindItem(updatedElements[i]) != null)
                    updated.add(updatedElements[i]);
                else
                    added.add(updatedElements[i]);
                addCount++;
            } else {
                removed.add(updatedElements[i]);
                removeCount++;
            }
        }

        viewer.add(added.toArray());
        viewer.update(updated.toArray(), null /*new String[] { SearchLabelProvider.PROPERTY_MATCH_COUNT } */);
        viewer.remove(removed.toArray());
    }

    public void clear() {
        getPage().getViewer().refresh();
    }
}
