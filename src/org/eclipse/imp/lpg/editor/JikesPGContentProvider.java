package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.uide.core.ILanguageService;
import org.eclipse.uide.editor.OutlineInformationControl;
import org.eclipse.uide.editor.OutlineInformationControl.OutlineContentProvider;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.ASTNodeToken;
import org.jikespg.uide.parser.JikesPGParser.AbstractVisitor;
import org.jikespg.uide.parser.JikesPGParser.action_segment;
import org.jikespg.uide.parser.JikesPGParser.optTerminalAlias;
import org.jikespg.uide.parser.JikesPGParser.option_value0;
import org.jikespg.uide.parser.JikesPGParser.rhsList;

public class JikesPGContentProvider extends OutlineContentProvider implements ILanguageService {
    protected boolean fProvideMembers;
    protected boolean fProvideWorkingCopy;

    public JikesPGContentProvider() {
	this(false, null);
    }

    /**
     * Creates a new <code>JikesPGContentProvider</code>.
     *
     * @param provideMembers if <code>true</code> members below compilation units 
     * and class files are provided. 
     */
    public JikesPGContentProvider(boolean provideMembers, OutlineInformationControl oic) {
	super(oic, true);
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
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	super.inputChanged(viewer, oldInput, newInput);
    }

    /* (non-Javadoc)
     * Method declared on ITreeContentProvider.
     */
    public Object[] getChildren(Object element) {
	ASTNode node= (ASTNode) element;
	final ArrayList children= node.getChildren();

	for(Iterator iter= children.iterator(); iter.hasNext(); ) {
	    ASTNode child= (ASTNode) iter.next();

	    // Filter out certain "useless" AST node types from the child list.
	    // This prunes out entities and all their sub-children from the outline.
	    if (child instanceof ASTNodeToken || child instanceof action_segment ||
		child instanceof option_value0 || child instanceof optTerminalAlias ||
		child instanceof rhsList)
		iter.remove();
	}
	return children.toArray();
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

	return node.getParent();
    }
}
