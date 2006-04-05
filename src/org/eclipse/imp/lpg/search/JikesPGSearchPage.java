package org.jikespg.uide.search;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class JikesPGSearchPage extends DialogPage implements ISearchPage {
    private class TypeSelectionListener implements SelectionListener {
        public void widgetSelected(SelectionEvent e) {
            Widget w= (Widget) e.getSource();

            if (w == fNonTermTypeButton)
                fNonTerm= true;
            else
                fNonTerm= false;
        }
        public void widgetDefaultSelected(SelectionEvent e) { }
    }

    private ISearchPageContainer fContainer;
    private Text fEntityName;
    private boolean fNonTerm;
    private Button fNonTermTypeButton;
    private Button fTermTypeButton;

    public JikesPGSearchPage() {
        super("JikesPG Search", null);
    }

    public JikesPGSearchPage(String title) {
        this(title, null);
    }

    public JikesPGSearchPage(String title, ImageDescriptor image) {
        super(title, image);
    }

    public boolean performAction() {
        IProject project= ResourcesPlugin.getWorkspace().getRoot().getProject("JikesPGTest");
        boolean isWorkspaceScope= fContainer.getSelectedScope() == ISearchPageContainer.WORKSPACE_SCOPE;
        JikesPGSearchScope scope= isWorkspaceScope ? JikesPGSearchScope.createWorkspaceScope() :
            JikesPGSearchScope.createProjectScope(project);
        JikesPGSearchQuery query= new JikesPGSearchQuery(fEntityName.getText(), fNonTerm, scope);

        NewSearchUI.activateSearchResultView();
        NewSearchUI.runQueryInBackground(query);
        return true;
    }

    public void setContainer(ISearchPageContainer container) {
        fContainer= container;
    }

    public void createControl(Composite parent) {
        Composite result= new Composite(parent, SWT.NONE);

        GridLayout layout= new GridLayout(2, false);
        layout.horizontalSpacing= 10;
        result.setLayout(layout);

        Label label= new Label(result, SWT.LEFT);
        label.setText("Grammar entity:"); 
        label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1));

        Group typeGroup= new Group(parent, SWT.NONE);
        typeGroup.setText("Entity type"); 
        typeGroup.setLayout(new GridLayout(2, true));

        Label nameLabel= new Label(typeGroup, SWT.LEFT);
        nameLabel.setText("Name regexp:");

        fEntityName= new Text(typeGroup, SWT.LEFT | SWT.BORDER);
        fEntityName.setText("");

        fNonTermTypeButton= new Button(typeGroup, SWT.RADIO);
        fNonTermTypeButton.setText("Non-terminal");
        fNonTermTypeButton.setSelection(true);
        fNonTermTypeButton.addSelectionListener(new TypeSelectionListener());

        fTermTypeButton= new Button(typeGroup, SWT.RADIO);
        fTermTypeButton.setText("Terminal");
        fTermTypeButton.addSelectionListener(new TypeSelectionListener());

        setControl(result);
    }
}
