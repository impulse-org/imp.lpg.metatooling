package org.jikespg.uide.editor;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uide.runtime.RuntimePlugin;

public class JikesPGOutlineInformationControl extends AbstractInformationControl {
    private KeyAdapter fKeyAdapter;
    private OutlineContentProvider fOutlineContentProvider;
    private Object fInput= null;
    private OutlineSorter fOutlineSorter;
    private OutlineLabelProvider fInnerLabelProvider;
    protected Color fForegroundColor;
//    private boolean fShowOnlyMainType; // RMF what would this mean in general?
    private LexicalSortingAction fLexicalSortingAction;
    private Map fTypeHierarchies= new HashMap();

    private class OutlineTreeViewer extends TreeViewer {
	private boolean fIsFiltering= false;

	private OutlineTreeViewer(Tree tree) {
	    super(tree);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object[] getFilteredChildren(Object parent) {
	    Object[] result= getRawChildren(parent);
	    int unfilteredChildren= result.length;
	    ViewerFilter[] filters= getFilters();
	    if (filters != null) {
		for(int i= 0; i < filters.length; i++)
		    result= filters[i].filter(this, parent, result);
	    }
	    fIsFiltering= unfilteredChildren != result.length;
	    return result;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void internalExpandToLevel(Widget node, int level) {
	    if (!fIsFiltering && node instanceof Item) {
		Item i= (Item) node;
//		if (i.getData() instanceof IJavaElement) {
//		    IJavaElement je= (IJavaElement) i.getData();
//		    if (je.getElementType() == IJavaElement.IMPORT_CONTAINER || isInnerType(je)) {
//			setExpanded(i, false);
//			return;
//		    }
//		}
	    }
	    super.internalExpandToLevel(node, level);
	}

//	private boolean isInnerType(IJavaElement element) {
//	    if (element != null && element.getElementType() == IJavaElement.TYPE) {
//		IType type= (IType) element;
//		return type.isMember();
//	    }
//	    return false;
//	}
    }

    private class OutlineContentProvider extends JikesPGContentProvider {
	private boolean fShowInheritedMembers;

	/**
	 * Creates a new Outline content provider.
	 *
	 * @param showInheritedMembers <code>true</code> iff inherited members are shown
	 */
	private OutlineContentProvider(boolean showInheritedMembers) {
	    super(true);
	    fShowInheritedMembers= showInheritedMembers;
	}

	public boolean isShowingInheritedMembers() {
	    return fShowInheritedMembers;
	}

	public void toggleShowInheritedMembers() {
	    Tree tree= getTreeViewer().getTree();
	    tree.setRedraw(false);
	    fShowInheritedMembers= !fShowInheritedMembers;
	    getTreeViewer().refresh();
	    getTreeViewer().expandToLevel(2);
	    // reveal selection
	    Object selectedElement= getSelectedElement();
	    if (selectedElement != null)
		getTreeViewer().reveal(selectedElement);
	    tree.setRedraw(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getChildren(Object element) {
//	    if (fShowOnlyMainType) {
//		if (element instanceof ICompilationUnit) {
//		    element= getMainType((ICompilationUnit) element);
//		} else if (element instanceof IClassFile) {
//		    element= getMainType((IClassFile) element);
//		}
//		if (element == null)
//		    return NO_CHILDREN;
//	    }
//	    if (fShowInheritedMembers && element instanceof IType) {
//		IType type= (IType) element;
//		if (type.getDeclaringType() == null) {
//		    ITypeHierarchy th= getSuperTypeHierarchy(type);
//		    if (th != null) {
//			List children= new ArrayList();
//			IType[] superClasses= th.getAllSupertypes(type);
//			children.addAll(Arrays.asList(super.getChildren(type)));
//			for(int i= 0, scLength= superClasses.length; i < scLength; i++)
//			    children.addAll(Arrays.asList(super.getChildren(superClasses[i])));
//			return children.toArray();
//		    }
//		}
//	    }
	    return super.getChildren(element);
	}

	/**
	 * {@inheritDoc}
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	    super.inputChanged(viewer, oldInput, newInput);
	    fTypeHierarchies.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
	    super.dispose();
	    fTypeHierarchies.clear();
	}
    }

    private class OutlineSorter extends ViewerSorter {
	private static final int OTHER= 1;
//	private static final int TYPE= 2;
//	private static final int ANONYM= 3;
//	private JavaElementSorter fJavaElementSorter= new JavaElementSorter();

	/*
	 * @see org.eclipse.jface.viewers.ViewerSorter#sort(org.eclipse.jface.viewers.Viewer, java.lang.Object[])
	 */
	public void sort(Viewer viewer, Object[] elements) {
	    if (!fLexicalSortingAction.isChecked())
		return;
	    super.sort(viewer, elements);
	}

	/*
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public int compare(Viewer viewer, Object e1, Object e2) {
	    int cat1= category(e1);
	    int cat2= category(e2);
	    if (cat1 != cat2)
		return cat1 - cat2;
//	    if (cat1 == OTHER) { // method or field
//		if (fSortByDefiningTypeAction.isChecked()) {
//		    IType def1= (e1 instanceof IMethod) ? getDefiningType((IMethod) e1) : null;
//		    IType def2= (e2 instanceof IMethod) ? getDefiningType((IMethod) e2) : null;
//		    if (def1 != null) {
//			if (def2 != null) {
//			    if (!def2.equals(def1)) {
//				return compareInHierarchy(getSuperTypeHierarchy(def1), def1, def2);
//			    }
//			} else {
//			    return -1;
//			}
//		    } else {
//			if (def2 != null) {
//			    return 1;
//			}
//		    }
//		}
//	    } else if (cat1 == ANONYM) {
//		return 0;
//	    }
//	    if (fLexicalSortingAction.isChecked())
//		return fJavaElementSorter.compare(viewer, e1, e2); // use appearance preference page settings
//	    else
		return 0;
	}

	/*
	 * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
	 */
	public int category(Object element) {
//	    if (element instanceof IType) {
//		IType type= (IType) element;
//		if (type.getElementName().length() == 0) {
//		    return ANONYM;
//		}
//		return TYPE;
//	    }
	    return OTHER;
	}
    }

    private class LexicalSortingAction extends Action {
	private static final String STORE_LEXICAL_SORTING_CHECKED= "LexicalSortingAction.isChecked"; //$NON-NLS-1$
	private TreeViewer fOutlineViewer;

	private LexicalSortingAction(TreeViewer outlineViewer) {
	    super("Sort", IAction.AS_CHECK_BOX);
	    setToolTipText("Sort by name");
	    setDescription("Sort entries lexically by name");
	    RuntimePlugin.getImageDescriptor("alphab_sort_co.gif"); //$NON-NLS-1$
	    fOutlineViewer= outlineViewer;
	    boolean checked= getDialogSettings().getBoolean(STORE_LEXICAL_SORTING_CHECKED);
	    setChecked(checked);
//	    PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.LEXICAL_SORTING_BROWSING_ACTION);
	}

	public void run() {
	    valueChanged(isChecked(), true);
	}

	private void valueChanged(final boolean on, boolean store) {
	    setChecked(on);
	    BusyIndicator.showWhile(fOutlineViewer.getControl().getDisplay(), new Runnable() {
		public void run() {
		    fOutlineViewer.refresh(false);
		}
	    });
	    if (store)
		getDialogSettings().put(STORE_LEXICAL_SORTING_CHECKED, on);
	}
    }

    /**
     * Creates a new Java outline information control.
     *
     * @param parent
     * @param shellStyle
     * @param treeStyle
     * @param commandId
     */
    public JikesPGOutlineInformationControl(Shell parent, int shellStyle, int treeStyle, String commandId) {
	super(parent, shellStyle, treeStyle, commandId, true);
    }

    /**
     * {@inheritDoc}
     */
    protected Text createFilterText(Composite parent) {
	Text text= super.createFilterText(parent);
	text.addKeyListener(getKeyAdapter());
	return text;
    }

    /**
     * {@inheritDoc}
     */
    protected TreeViewer createTreeViewer(Composite parent, int style) {
	Tree tree= new Tree(parent, SWT.SINGLE | (style & ~SWT.MULTI));
	GridData gd= new GridData(GridData.FILL_BOTH);
	gd.heightHint= tree.getItemHeight() * 12;
	tree.setLayoutData(gd);
	final TreeViewer treeViewer= new OutlineTreeViewer(tree);
	// Hard-coded filters
	treeViewer.addFilter(new NamePatternFilter());
	treeViewer.addFilter(new GrammarElementFilter() { });
	fForegroundColor= parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
	fInnerLabelProvider= new OutlineLabelProvider(fOutlineContentProvider.fShowInheritedMembers, fForegroundColor);
	fInnerLabelProvider.addLabelDecorator(new ProblemsLabelDecorator());
	IDecoratorManager decoratorMgr= PlatformUI.getWorkbench().getDecoratorManager();
//	if (decoratorMgr.getEnabled("org.eclipse.jdt.ui.override.decorator")) //$NON-NLS-1$
//	    fInnerLabelProvider.addLabelDecorator(new OverrideIndicatorLabelDecorator(null));
	treeViewer.setLabelProvider(fInnerLabelProvider);
	fLexicalSortingAction= new LexicalSortingAction(treeViewer);
//	fSortByDefiningTypeAction= new SortByDefiningTypeAction(treeViewer);
//	fShowOnlyMainTypeAction= new ShowOnlyMainTypeAction(treeViewer);
	fOutlineContentProvider= new OutlineContentProvider(false);
	treeViewer.setContentProvider(fOutlineContentProvider);
	fOutlineSorter= new OutlineSorter();
	treeViewer.setSorter(fOutlineSorter);
	treeViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
	treeViewer.getTree().addKeyListener(getKeyAdapter());
	return treeViewer;
    }

    /**
     * {@inheritDoc}
     */
    protected String getStatusFieldText() {
	KeySequence[] sequences= new KeySequence[0]; // getInvokingCommandKeySequences(); // Disabled for now, since impl in jdt relied on deprecated API
	if (sequences == null || sequences.length == 0)
	    return ""; //$NON-NLS-1$
	String keySequence= sequences[0].format();
	if (fOutlineContentProvider.isShowingInheritedMembers())
	    return "Press Ctrl+O to hide inherited members";
	else
	    return "Press Ctrl+O to show inherited members";
    }

    /*
     * @see org.eclipse.jdt.internal.ui.text.AbstractInformationControl#getId()
     * @since 3.0
     */
    protected String getId() {
	return "org.eclipse.jdt.internal.ui.text.QuickOutline"; //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    public void setInput(Object information) {
	if (information == null || information instanceof String) {
	    inputChanged(null, null);
	    return;
	}
//	IJavaElement je= (IJavaElement) information;
//	ICompilationUnit cu= (ICompilationUnit) je.getAncestor(IJavaElement.COMPILATION_UNIT);
//	if (cu != null)
//	    fInput= cu;
//	else
//	    fInput= je.getAncestor(IJavaElement.CLASS_FILE);
	fInput= information;
	inputChanged(fInput, information);
    }

    private KeyAdapter getKeyAdapter() {
	if (fKeyAdapter == null) {
	    fKeyAdapter= new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    int accelerator= SWTKeySupport.convertEventToUnmodifiedAccelerator(e);
		    KeySequence keySequence= KeySequence.getInstance(SWTKeySupport.convertAcceleratorToKeyStroke(accelerator));
		    KeySequence[] sequences= new KeySequence[0]; // getInvokingCommandKeySequences(); // RMF 6/1/2006 - disabled since impl used deprecated API
		    if (sequences == null)
			return;
		    for(int i= 0; i < sequences.length; i++) {
			if (sequences[i].equals(keySequence)) {
			    e.doit= false;
			    toggleShowInheritedMembers();
			    return;
			}
		    }
		}
	    };
	}
	return fKeyAdapter;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleStatusFieldClicked() {
	toggleShowInheritedMembers();
    }

    protected void toggleShowInheritedMembers() {
//	long flags= AppearanceAwareLabelProvider.DEFAULT_TEXTFLAGS | JavaElementLabels.F_APP_TYPE_SIGNATURE;
//	if (!fOutlineContentProvider.isShowingInheritedMembers())
//	    flags|= JavaElementLabels.ALL_POST_QUALIFIED;
//	fInnerLabelProvider.setTextFlags(flags);
	fOutlineContentProvider.toggleShowInheritedMembers();
	updateStatusFieldText();
    }

    /*
     * @see org.eclipse.jdt.internal.ui.text.AbstractInformationControl#fillViewMenu(org.eclipse.jface.action.IMenuManager)
     */
    protected void fillViewMenu(IMenuManager viewMenu) {
	super.fillViewMenu(viewMenu);
//	viewMenu.add(fShowOnlyMainTypeAction); //$NON-NLS-1$
	viewMenu.add(new Separator("Sorters")); //$NON-NLS-1$
	viewMenu.add(fLexicalSortingAction);
//	viewMenu.add(fSortByDefiningTypeAction);
    }

    private IProgressMonitor getProgressMonitor() {
	IWorkbenchPage wbPage= RuntimePlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage();
	if (wbPage == null)
	    return null;
	IEditorPart editor= wbPage.getActiveEditor();
	if (editor == null)
	    return null;
	return editor.getEditorSite().getActionBars().getStatusLineManager().getProgressMonitor();
    }
}
