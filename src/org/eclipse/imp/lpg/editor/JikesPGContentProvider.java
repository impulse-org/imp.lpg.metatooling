package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;

public class JikesPGContentProvider implements ITreeContentProvider {
    protected static final Object[] NO_CHILDREN= new Object[0];
    protected boolean fProvideMembers;
    protected boolean fProvideWorkingCopy;

    /**
     * Creates a new content provider. The content provider does not
     * provide members of compilation units or class files.
     */
    public JikesPGContentProvider() {
	this(false);
    }

    /**
     *@deprecated Use {@link #StandardJavaElementContentProvider(boolean)} instead.
     * Since 3.0 compilation unit children are always provided as working copies. The Java Model
     * does not support the 'original' mode anymore.
     */
    public JikesPGContentProvider(boolean provideMembers, boolean provideWorkingCopy) {
	this(provideMembers);
    }

    /**
     * Creates a new <code>StandardJavaElementContentProvider</code>.
     *
     * @param provideMembers if <code>true</code> members below compilation units 
     * and class files are provided. 
     */
    public JikesPGContentProvider(boolean provideMembers) {
	fProvideMembers= provideMembers;
	fProvideWorkingCopy= provideMembers;
    }

    /**
     * Returns whether members are provided when asking
     * for a compilation units or class file for its children.
     * 
     * @return <code>true</code> if the content provider provides members; 
     * otherwise <code>false</code> is returned
     */
    public boolean getProvideMembers() {
	return fProvideMembers;
    }

    /**
     * Sets whether the content provider is supposed to return members
     * when asking a compilation unit or class file for its children.
     * 
     * @param b if <code>true</code> then members are provided. 
     * If <code>false</code> compilation units and class files are the
     * leaves provided by this content provider.
     */
    public void setProvideMembers(boolean b) {
	//hello
	fProvideMembers= b;
    }

    /**
     * @deprecated Since 3.0 compilation unit children are always provided as working copies. The Java model
     * does not support the 'original' mode anymore. 
     */
    public boolean getProvideWorkingCopy() {
	return fProvideWorkingCopy;
    }

    /**
     * @deprecated Since 3.0 compilation unit children are always provided from the working copy. The Java model
     * offers a unified world and does not support the 'original' mode anymore. 
     */
    public void setProvideWorkingCopy(boolean b) {
	fProvideWorkingCopy= b;
    }

    /* (non-Javadoc)
     * @see IWorkingCopyProvider#providesWorkingCopies()
     */
    public boolean providesWorkingCopies() {
	return getProvideWorkingCopy();
    }

    /* (non-Javadoc)
     * Method declared on IStructuredContentProvider.
     */
    public Object[] getElements(Object parent) {
	return getChildren(parent);
    }

    /* (non-Javadoc)
     * Method declared on IContentProvider.
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

    /* (non-Javadoc)
     * Method declared on IContentProvider.
     */
    public void dispose() {}

    /* (non-Javadoc)
     * Method declared on ITreeContentProvider.
     */
    public Object[] getChildren(Object element) {
	ASTNode node= (ASTNode) element;

	return NO_CHILDREN; // return new GetChildrenVisitor().getChildren(node).toArray();
    }

    /* (non-Javadoc)
     * @see ITreeContentProvider
     */
    public boolean hasChildren(Object element) {
	Object[] children= getChildren(element);

	return (children != null) && children.length > 0;
    }

    /* (non-Javadoc)
     * Method declared on ITreeContentProvider.
     */
    public Object getParent(Object element) {
	ASTNode node= (ASTNode) element;

	return null; // node.getParent();
    }
}
