package org.jikespg.uide.wizards;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.uide.wizards.ExtensionPointWizard;
import org.eclipse.uide.wizards.ExtensionPointWizardPage;
import org.jikespg.uide.builder.JikesPGBuilder;

/**
 * This wizard creates a JikesPG grammar, a "parser" language service, and a
 * stub IParseController implementation for use with the UIDE.
 */
public class NewUIDEParserWizard extends ExtensionPointWizard implements INewWizard {
    //	private NewUIDEParserWizardPage page;
    //	private ISelection selection;

    public NewUIDEParserWizard() {
	super();
	setNeedsProgressMonitor(true);
    }

    public void addPages() {
	addPages(new ExtensionPointWizardPage[] { new NewUIDEParserWizardPage(this), });
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We will create
     * an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {
	super.performFinish();
	IRunnableWithProgress op= new IRunnableWithProgress() {
	    public void run(IProgressMonitor monitor) throws InvocationTargetException {
		try {
		    doFinish(monitor);
		} catch (CoreException e) {
		    throw new InvocationTargetException(e);
		} finally {
		    monitor.done();
		}
	    }
	};
	try {
	    getContainer().run(true, false, op);
	} catch (InterruptedException e) {
	    return false;
	} catch (InvocationTargetException e) {
	    Throwable realException= e.getTargetException();
	    MessageDialog.openError(getShell(), "Error", realException.getMessage());
	    return false;
	}
	return true;
    }

    /**
     * The worker method. It will find the container, create the
     * file if missing or just replace its contents, and open
     * the editor on the newly created file.
     */
    private void doFinish(IProgressMonitor monitor) throws CoreException {
	NewUIDEParserWizardPage page= (NewUIDEParserWizardPage) pages[0];

	IProject project= page.getProject();
	String packageName= page.getPackage();
	String languageName= page.getLanguage();
	String templateName= page.getTemplateName();
	String grammarFileName= "src/" + languageName.toLowerCase() + "Parser.g";
	String lexerFileName= "src/" + languageName.toLowerCase() + "Lexer.gi";
	boolean hasKeywords= page.hasKeywords();
	boolean autoGenerateASTs= page.autoGenerateASTs();

	IFile file= createSampleGrammarFile(monitor, project, packageName, languageName,
		grammarFileName, templateName, hasKeywords, autoGenerateASTs);

	createSampleLexerFile(monitor, project, packageName, languageName, lexerFileName, templateName);

	editSampleGrammarFile(monitor, file);
	enableBuilders(monitor, file);
    }

    /**
     * @param monitor
     * @param file
     */
    private void enableBuilders(IProgressMonitor monitor, final IFile file) {
	monitor.setTaskName("Enabling builders...");
	Job job= new WorkspaceJob("Enabling builders...") {
	    public IStatus runInWorkspace(IProgressMonitor monitor) {
		try {
		    addBuilder(file.getProject(), JikesPGBuilder.BUILDER_ID);
		} catch (Throwable e) {
		    e.printStackTrace();
		}
		return Status.OK_STATUS;
	    }
	};
	job.schedule();
    }

    /**
     * @param monitor
     * @param file
     */
    private void editSampleGrammarFile(IProgressMonitor monitor, final IFile file) {
	monitor.setTaskName("Opening file for editing...");
	getShell().getDisplay().asyncExec(new Runnable() {
	    public void run() {
		IWorkbenchPage page= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
		    IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
		}
	    }
	});
	monitor.worked(1);
    }

    static final String sAutoGenTemplate= "%options ast=ASTNode,visitor";
    static final String sKeywordTemplate= "%options filter=kwTemplate.gi";

    /**
     * @param monitor
     * @param project
     * @param packageName
     * @param languageName
     * @param fileName
     * @param templateName name of a JikesPG template file to use
     * @return
     * @throws CoreException
     */
    private IFile createSampleGrammarFile(IProgressMonitor monitor, IProject project, String packageName, String languageName,
	    String fileName, String templateName, boolean hasKeywords, boolean autoGenerateASTs) throws CoreException {
	monitor.beginTask("Creating " + fileName, 2);

	final IFile file= project.getFile(new Path(fileName));
	StringBuffer buffer= new StringBuffer(new String(getSampleGrammar()));

	replace(buffer, "$LANG_NAME$", languageName);
	replace(buffer, "$PACKAGE$", packageName);
	replace(buffer, "$TEMPLATE$", templateName);
	replace(buffer, "$KEYWORD_TEMPLATE$", hasKeywords ? sKeywordTemplate : "");
	replace(buffer, "$AUTO_GENERATE$", autoGenerateASTs ? sAutoGenTemplate : "");

	if (file.exists()) {
	    file.setContents(new ByteArrayInputStream(buffer.toString().getBytes()), true, true, monitor);
	} else {
	    file.create(new ByteArrayInputStream(buffer.toString().getBytes()), true, monitor);
	}
	monitor.worked(1);
	return file;
    }

    private IFile createSampleLexerFile(IProgressMonitor monitor, IProject project, String packageName,
	    String languageName, String fileName, String templateName) throws CoreException {
	monitor.beginTask("Creating " + fileName, 2);

	final IFile file= project.getFile(new Path(fileName));
	StringBuffer buffer= new StringBuffer(new String(getSampleLexer()));

	replace(buffer, "$LANG_NAME$", languageName);
	replace(buffer, "$PACKAGE$", packageName);
	replace(buffer, "$TEMPLATE$", templateName);

	if (file.exists()) {
	    file.setContents(new ByteArrayInputStream(buffer.toString().getBytes()), true, true, monitor);
	} else {
	    file.create(new ByteArrayInputStream(buffer.toString().getBytes()), true, monitor);
	}
	monitor.worked(1);
	return file;
    }

    private void addBuilder(IProject project, String id) throws CoreException {
	IProjectDescription desc= project.getDescription();
	ICommand[] commands= desc.getBuildSpec();
	for(int i= 0; i < commands.length; ++i)
	    if (commands[i].getBuilderName().equals(id))
		return;
	//add builder to project
	ICommand command= desc.newCommand();
	command.setBuilderName(id);
	ICommand[] nc= new ICommand[commands.length + 1];
	// Add it before other builders.
	System.arraycopy(commands, 0, nc, 1, commands.length);
	nc[0]= command;
	desc.setBuildSpec(nc);
	project.setDescription(desc, null);
    }

    private byte[] getSampleGrammar() {
	return getSampleFile("sample_grammar.txt");
    }

    private byte[] getSampleLexer() {
	return getSampleFile("sample_lexer.txt");
    }

    private byte[] getSampleFile(String fileName) {
	try {
	    DataInputStream is= new DataInputStream(NewUIDEParserWizard.class.getResourceAsStream(fileName));
	    byte bytes[]= new byte[is.available()];
	    is.readFully(bytes);
	    is.close();
	    return bytes;
	} catch (Exception e) {
	    e.printStackTrace();
	    return ("// missing sample file: " + fileName).getBytes();
	}
    }

    static void replace(StringBuffer sb, String target, String substitute) {
	for(int index= sb.indexOf(target); index != -1; index= sb.indexOf(target))
	    sb.replace(index, index + target.length(), substitute);
    }

    /**
     * We will accept the selection in the workbench to see if
     * we can initialize from it.
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
	// this.selection = selection;
    }
}
