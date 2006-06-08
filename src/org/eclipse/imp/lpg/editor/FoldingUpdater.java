package org.jikespg.uide.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lpg.lpgjavaruntime.LexStream;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.uide.editor.IFoldingUpdater;
import org.eclipse.uide.parser.IParseController;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.*;

public class FoldingUpdater implements IFoldingUpdater {
    private Annotation[] fOldAnnotations;

    public void updateFoldingStructure(IParseController parseController, ProjectionAnnotationModel annotationModel) {
	final List/*Annotation*/ annotations= new ArrayList();
	final HashMap newAnnotations= new HashMap();
	ASTNode ast= (ASTNode) parseController.getCurrentAst();

	if (ast == null)
	    return;

	ast.accept(new JikesPGParser.AbstractVisitor() {
	    public void unimplementedVisitor(String s) { }
	    private void makeAnnotation(ASTNode n) {
		ProjectionAnnotation annotation= new ProjectionAnnotation();
		int start= n.getLeftIToken().getStartOffset();
		int len= n.getRightIToken().getEndOffset() - start + 1;

		newAnnotations.put(annotation, new Position(start, len));
		annotations.add(annotation);
	    }
	    private void makeAnnotation(int start, int len) {
		ProjectionAnnotation annotation= new ProjectionAnnotation();

		newAnnotations.put(annotation, new Position(start, len));
		annotations.add(annotation);
	    }
	    public boolean visit(options_segment n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(AliasSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(DefineSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(ExportSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(GlobalsSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(HeadersSeg n) {
		makeAnnotation(n);
	        return false;
	    }
	    public boolean visit(IdentifierSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(ImportSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(IncludeSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(NoticeSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(RulesSeg n) {
		makeAnnotation(n);
		return true;
	    }
	    public boolean visit(rhs n) {
		final action_segment optAction= n.getopt_action_segment();

		if (optAction != null) {
		    // Make the action block and any surrounding whitespace foldable.
		    final LexStream lexStream= optAction.getIToken().getPrsStream().getLexStream();
		    int start= optAction.getLeftIToken().getStartOffset();
		    int len= optAction.getRightIToken().getEndOffset() - start + 3;

		    while (Character.isWhitespace(lexStream.getCharValue(start-1))) {
			start--;
			len++;
		    }
		    while (Character.isWhitespace(lexStream.getCharValue(start + len - 1)))
			len++;
		    len--;
		    makeAnnotation(start, len);
		}
		return false;
	    }
	    public boolean visit(TerminalsSeg n) {
		makeAnnotation(n);
		return false;
	    }
	    public boolean visit(TypesSeg n) {
		makeAnnotation(n);
		return false;
	    }
	});

//	dumpAnnotations(annotations, newAnnotations);

	if (fOldAnnotations == null || annotations.size() != fOldAnnotations.length)
	    annotationModel.modifyAnnotations(fOldAnnotations, newAnnotations, null);
	fOldAnnotations= (Annotation[]) annotations.toArray(new Annotation[annotations.size()]);
    }

    private void dumpAnnotations(final List annotations, final HashMap newAnnotations) {
	for(int i= 0; i < annotations.size(); i++) {
	    Annotation a= (Annotation) annotations.get(i);
	    Position p= (Position) newAnnotations.get(a);

	    System.out.println("Annotation @ " + p.offset + ":" + p.length);
	}
    }
}
