package org.jikespg.uide;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.print.DocPrintJob;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.compare.structuremergeviewer.IStructureCreator;
import org.eclipse.compare.structuremergeviewer.StructureDiffViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.uide.parser.ILexer;
import org.eclipse.uide.parser.IParser;
import org.jikespg.uide.compare.JikesPGStructureNode;
import org.jikespg.uide.parser.JikesPGLexer;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.AbstractVisitor;
import org.jikespg.uide.parser.JikesPGParser.JikesPG;
import org.jikespg.uide.parser.JikesPGParser.Visitor;

public class StructureMergeViewerCreator implements IViewerCreator {
    private static class JikesPGStructureCreator implements IStructureCreator {
        private final CompareConfiguration fConfig;

        public JikesPGStructureCreator(CompareConfiguration config) {
            fConfig= config;
        }

        public String getName() {
            return fConfig.getRightLabel(null); // fFile.getName();
        }

        public IStructureComparator getStructure(Object input) {
            try {
                IResource res= null;
                IFile file= null;

                if (input instanceof ResourceNode) {
                    res= ((ResourceNode) input).getResource();
                } else if (input instanceof IResourceProvider) {
                    res= ((IResourceProvider) input).getResource();
                }
                if (res != null && res instanceof IFile)
                    file= (IFile) res;
                if (file != null) {
                    IEditorPart editor= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                    IDocumentProvider docProvider= ((ITextEditor) editor).getDocumentProvider();
                    // TODO This is totally bogus: how do we know we got invoked from a text editor???
                    // We need an IDocument, but how to get that???
                    IDocument doc= docProvider.getDocument(editor.getEditorInput());

                    return new JikesPGStructureNode(parseFile(file), doc, 0, "root");
                }
            } catch (IOException io) {
            } catch (CoreException ce) {
            }
            return null;
        }

        private ASTNode parseFile(IFile file) throws IOException, CoreException {
            return parseStream(file.getContents(), file.getLocation());
        }

        private ASTNode parseStream(InputStream stream, IPath path) throws IOException {
            char[] contentsArray= getStreamContents(stream);
            ILexer lexer= new JikesPGLexer();
            IParser parser= new JikesPGParser(lexer.getLexStream());

            lexer.initialize(contentsArray, path.toString());
            parser.getParseStream().resetTokenStream();
            lexer.lexer(null, parser.getParseStream()); // Lex the stream to produce the token stream
            return (ASTNode) parser.parser(null, 0);
        }

        private char[] getStreamContents(InputStream in) throws IOException {
            InputStreamReader reader= new InputStreamReader(in);
            StringBuffer buff= new StringBuffer();
            int ch;

            while ((ch = reader.read()) > 0)
                buff.append((char) ch);
            return buff.toString().toCharArray();
        }

        public IStructureComparator locate(Object path, Object input) {
            JikesPGStructureNode inputAST= (JikesPGStructureNode) input;

            return null;
        }

        public String getContents(Object node, boolean ignoreWhitespace) {
            JikesPGStructureNode wrapper= (JikesPGStructureNode) node;

            return wrapper.getASTNode().toString();
        }

        public void save(IStructureComparator node, Object input) {
            // TODO Auto-generated method stub
        }
    }

    private static final class JikesPGStructureDiffViewer extends StructureDiffViewer {
        private IStructureCreator fStructureCreator;

        private JikesPGStructureDiffViewer(Composite parent, CompareConfiguration config) {
            super(parent, config);
            fStructureCreator= new JikesPGStructureCreator(config);
            setStructureCreator(fStructureCreator);
        }
    }

    public Viewer createViewer(final Composite parent, final CompareConfiguration config) {
        return new JikesPGStructureDiffViewer(parent, config);
    }
}
