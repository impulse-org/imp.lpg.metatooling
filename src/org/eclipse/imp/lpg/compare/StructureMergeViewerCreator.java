package org.jikespg.uide;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.compare.structuremergeviewer.IStructureCreator;
import org.eclipse.compare.structuremergeviewer.StructureDiffViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.compare.JavaStructureCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.uide.parser.ILexer;
import org.eclipse.uide.parser.IParser;
import org.jikespg.uide.parser.JikesPGLexer;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;

public class StructureMergeViewerCreator implements IViewerCreator {
    static class ASTWrapper implements IStructureComparator {
	ASTNode node;
	public ASTWrapper(ASTNode n) {
	    node= n;
	}
	public Object[] getChildren() {
	    return node.getChildren();
	}
    }
    public Viewer createViewer(Composite parent, CompareConfiguration config) {
	return new StructureDiffViewer(parent, config) {
	    IStructureCreator fStructureCreator;
	    {
		fStructureCreator= new IStructureCreator() {
		    public String getName() {
			return "yo";
		    }
		    public IStructureComparator getStructure(Object input) {
			try {
			    if (input instanceof ResourceNode) {
				ResourceNode resNode= (ResourceNode) input;
				IResource res= resNode.getResource();
				if (res instanceof IFile) {
				    IFile file= (IFile) res;

				    ILexer lexer= new JikesPGLexer();
				    IParser parser= new JikesPGParser(lexer.getLexStream());
				    InputStream in= file.getContents();
				    StringBuffer buff= new StringBuffer();
				    byte ch[] = new byte[1];
				    while (in.read(ch) > 0)
					buff.append((char) ch[0]);
				    char[] contentsArray = buff.toString().toCharArray();

				    lexer.initialize(contentsArray, file.getLocation().toString());
				    parser.getParseStream().resetTokenStream();
				    lexer.lexer(null, parser.getParseStream()); // Lex the stream to produce the token stream

				    return new ASTWrapper((ASTNode) parser.parser(null, 0));
				}
			    }
			} catch (IOException io) {
			} catch (CoreException ce) {
			}
			return null;
		    }
		    public IStructureComparator locate(Object path, Object input) {
			return null;
		    }
		    public String getContents(Object node, boolean ignoreWhitespace) {
			return null;
		    }
		    public void save(IStructureComparator node, Object input) {
			// TODO Auto-generated method stub
		    }
		};
		setStructureCreator(fStructureCreator);
	    }
	};
    }
}
