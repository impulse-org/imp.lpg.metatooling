/**
 * 
 */
package org.jikespg.uide.search;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class JikesPGSearchScope {
    Set fProjects= new HashSet();

    private JikesPGSearchScope() { }

    private JikesPGSearchScope(IProject p) {
        fProjects.add(p);
    }

    public void addProject(IProject project) {
        fProjects.add(project);
    }

    public Set/*<IProject>*/ getProjects() {
        return fProjects;
    }

    public static JikesPGSearchScope createWorkspaceScope() {
        JikesPGSearchScope scope= new JikesPGSearchScope();
        IProject[] projects= ResourcesPlugin.getWorkspace().getRoot().getProjects();

        for(int i= 0; i < projects.length; i++) {
            scope.addProject(projects[i]);
        }
        return scope;
    }

    public static JikesPGSearchScope createProjectScope(IProject project) {
        return new JikesPGSearchScope(project);
    }
}