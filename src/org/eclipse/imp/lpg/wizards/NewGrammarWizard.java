package org.jikespg.uide.wizards;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.uide.wizards.ExtensionPointWizard;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.builder.JikesPGBuilder;
import org.osgi.framework.Bundle;

/**
 * This wizard creates a set of JikesPG specification files for a new parser, lexer,
 * and (optionally) a keyword filter. Unlike NewUIDEParserWizard, this doesn't create
 * an Eclipse/UIDE extension and an IParseController implementation, just a naked
 * JikesPG parser/lexer to use in whatever context the user wants.
 * @author rfuhrer
 */
// TODO needs much refactoring to share code with the NewUIDEParserWizard.
public class NewGrammarWizard extends Wizard implements INewWizard {
    private final NewGrammarWizardPage fPage;

    public NewGrammarWizard() {
	super();
	fPage= new NewGrammarWizardPage();
    }

    public void addPages() {
	addPage(fPage);
    }

    public boolean performFinish() {
	IRunnableWithProgress op= new IRunnableWithProgress() {
	    public void run(IProgressMonitor monitor) throws InvocationTargetException {
		try {
		    doFinish(monitor);
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

    private void doFinish(IProgressMonitor monitor) {
	IProject project= fPage.getProject();
	GrammarOptions options= fPage.getOptions();
	boolean hasKeywords= options.getHasKeywords();
	boolean requiresBacktracking= options.getRequiresBacktracking();
	boolean autoGenerateASTs= options.getAutoGenerateASTs();
	String packageName= options.getPackageName();
	String languageName= options.getLanguageName();
	String templateKind= options.getTemplateKind();
	String parserTemplateName= templateKind + (requiresBacktracking ? "/bt" : "/dt") + "ParserTemplate.gi";
	String lexerTemplateName= templateKind + "/LexerTemplate.gi";
	String kwLexerTemplateName= templateKind + "/KeywordTemplate.gi";
	String grammarFileName= "src/" + languageName.toLowerCase() + "Parser.g";
	String lexerFileName= "src/" + languageName.toLowerCase() + "Lexer.gi";
	String kwlexerFileName= "src/" + languageName.toLowerCase() + "KWLexer.gi";

	try {
	    IFile grammarFile= createGrammarFile(packageName, languageName, grammarFileName, parserTemplateName, autoGenerateASTs, project, monitor);

	    createSampleLexerFile(packageName, languageName, lexerFileName, lexerTemplateName, hasKeywords, project, monitor);
	    createSampleKWLexerFile(packageName, languageName, kwlexerFileName, kwLexerTemplateName, hasKeywords, project, monitor);

	    editFile(monitor, grammarFile);
	    enableBuilders(monitor, project);
	} catch (CoreException e) {
	    e.printStackTrace();
	}
    }

    private void enableBuilders(IProgressMonitor monitor, final IProject project) {
	monitor.setTaskName("Enabling builders...");
	Job job= new WorkspaceJob("Enabling builders...") {
	    public IStatus runInWorkspace(IProgressMonitor monitor) {
		try {
		    addBuilder(project, JikesPGBuilder.BUILDER_ID);
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
    private void editFile(IProgressMonitor monitor, final IFile file) {
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

    static final String astDirectory= "Ast";
    static final String astNode= "ASTNode";
    static final String sAutoGenTemplate= "%options automatic_ast=toplevel,visitor,ast_directory=./" + astDirectory + ",ast_type=" + astNode;
    static final String sKeywordTemplate= "%options filter=kwTemplate.gi";

    private IFile createGrammarFile(String packageName, String languageName, String fileName, String templateName,
	    boolean autoGenerateASTs, IProject project, IProgressMonitor monitor) throws CoreException {

	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE_NAME$", packageName },
		{ "$AUTO_GENERATE$", autoGenerateASTs ? sAutoGenTemplate : "" },
		{ "$TEMPLATE$", templateName } };

	return createFileFromTemplate(fileName, "grammar.tmpl", replacements, project, monitor);
    }

    private IFile createSampleLexerFile(String packageName, String languageName, String fileName, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE_NAME$", packageName },
		{ "$TEMPLATE$", templateName },
		{ "$KEYWORD_FILTER$",
			hasKeywords ? ("%options filter=" + languageName + "KWLexer.gi") : "" },
		{ "$KEYWORD_LEXER$", hasKeywords ? ("$" + languageName + "KWLexer") : "Object" },
		{ "$LEXER_MAP$", (hasKeywords ? "LexerBasicMap" : "LexerVeryBasicMap") } };

	return createFileFromTemplate(fileName, "lexer.tmpl", replacements, project, monitor);
    }

    private IFile createSampleKWLexerFile(String packageName, String languageName, String fileName, String templateName,
	    boolean hasKeywords, IProject project, IProgressMonitor monitor) throws CoreException {
	String[][] replacements= new String[][] {
		{ "$LANG_NAME$", languageName },
		{ "$PACKAGE_NAME$", packageName },
		{ "$TEMPLATE$", templateName }
	};
	return createFileFromTemplate(fileName, "kwlexer.tmpl", replacements, project, monitor);
    }

    private void addBuilder(IProject project, String id) throws CoreException {
	IProjectDescription desc= project.getDescription();
	ICommand[] commands= desc.getBuildSpec();
	for(int i= 0; i < commands.length; ++i)
	    if (commands[i].getBuilderName().equals(id))
		return;
	// add builder to project
	ICommand command= desc.newCommand();
	command.setBuilderName(id);
	ICommand[] nc= new ICommand[commands.length + 1];
	// Add it before other builders.
	System.arraycopy(commands, 0, nc, 1, commands.length);
	nc[0]= command;
	desc.setBuildSpec(nc);
	project.setDescription(desc, null);
    }

    protected IFile createFileFromTemplate(String fileName, String templateName, String[][] replacements, IProject project, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Creating " + fileName, 2);
    
        final IFile file= project.getFile(new Path(fileName));
        StringBuffer buffer= new StringBuffer(new String(getTemplateFile(templateName)));
    
        for(int i= 0; i < replacements.length; i++) {
            ExtensionPointWizard.replace(buffer, replacements[i][0], replacements[i][1]);
        }
    
        if (file.exists()) {
            file.setContents(new ByteArrayInputStream(buffer.toString().getBytes()), true, true, monitor);
        } else {
            file.create(new ByteArrayInputStream(buffer.toString().getBytes()), true, monitor);
        }
        monitor.worked(1);
        return file;
    }

    protected byte[] getTemplateFile(String fileName) {
        try {
            Bundle bundle= Platform.getBundle(JikesPGPlugin.kPluginID);
            URL url= Platform.asLocalURL(Platform.find(bundle, new Path("/templates/" + fileName)));
            String path= url.getPath();
            FileInputStream fis= new FileInputStream(path);
            DataInputStream is= new DataInputStream(fis);
            byte bytes[]= new byte[fis.available()];
    
            is.readFully(bytes);
            is.close();
            fis.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return ("// missing template file: " + fileName).getBytes();
        }
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {}
}
