package org.jikespg.uide.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class JikesPGSearchTreeContentProvider extends JikesPGSearchContentProvider implements ITreeContentProvider {
    private Map fChildrenMap;

    public JikesPGSearchTreeContentProvider(JikesPGSearchResultPage page) {
        super(page);
    }

    public void elementsChanged(Object[] updatedElements) {
        AbstractTreeViewer viewer= (AbstractTreeViewer) getPage().getViewer();
        if (fResult == null)
            return;
        Set toRemove= new HashSet();
        Set toUpdate= new HashSet();
        Map toAdd= new HashMap();
        for(int i= 0; i < updatedElements.length; i++) {
            if (getPage().getDisplayedMatchCount(updatedElements[i]) > 0)
                insert(toAdd, toUpdate, updatedElements[i]);
            else
                remove(toRemove, toUpdate, updatedElements[i]);
        }

        viewer.remove(toRemove.toArray());
        for(Iterator iter= toAdd.keySet().iterator(); iter.hasNext();) {
            Object parent= iter.next();
            HashSet children= (HashSet) toAdd.get(parent);
            viewer.add(parent, children.toArray());
        }
        for(Iterator elementsToUpdate= toUpdate.iterator(); elementsToUpdate.hasNext();) {
            viewer.refresh(elementsToUpdate.next());
        }
    }

    protected void insert(Map toAdd, Set toUpdate, Object child) {
        Object parent= getParent(child);
        while (parent != null) {
            if (insertChild(parent, child)) {
                if (toAdd != null)
                    insertInto(parent, child, toAdd);
            } else {
                if (toUpdate != null)
                    toUpdate.add(parent);
                return;
            }
            child= parent;
            parent= getParent(child);
        }
        if (insertChild(fResult, child)) {
            if (toAdd != null)
                insertInto(fResult, child, toAdd);
        }
    }

    private boolean insertChild(Object parent, Object child) {
        return insertInto(parent, child, fChildrenMap);
    }

    private boolean insertInto(Object parent, Object child, Map map) {
        Set children= (Set) map.get(parent);
        if (children == null) {
            children= new HashSet();
            map.put(parent, children);
        }
        return children.add(child);
    }

    protected void remove(Set toRemove, Set toUpdate, Object element) {
        // precondition here: fResult.getMatchCount(child) <= 0

        if (hasChildren(element)) {
            if (toUpdate != null)
                toUpdate.add(element);
        } else {
            if (getPage().getDisplayedMatchCount(element) == 0) {
                fChildrenMap.remove(element);
                Object parent= getParent(element);
                if (parent != null) {
                    if (removeFromSiblings(element, parent)) {
                        remove(toRemove, toUpdate, parent);
                    }
                } else {
                    if (removeFromSiblings(element, fResult)) {
                        if (toRemove != null)
                            toRemove.add(element);
                    }
                }
            } else {
                if (toUpdate != null) {
                    toUpdate.add(element);
                }
            }
        }
    }

    /**
     * @param element
     * @param parent
     * @return returns true if it really was a remove (i.e. element was a child of parent).
     */
    private boolean removeFromSiblings(Object element, Object parent) {
        Set siblings= (Set) fChildrenMap.get(parent);
        if (siblings != null) {
            return siblings.remove(element);
        } else {
            return false;
        }
    }

    public void clear() {
        initialize(fResult);
        getPage().getViewer().refresh();
    }

    public Object[] getChildren(Object parentElement) {
        // TODO do something
        return EMPTY_ARR;
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }
}
