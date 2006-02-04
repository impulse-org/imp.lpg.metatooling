package org.jikespg.uide.editor;

import java.util.HashMap;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.uide.editor.IFoldingUpdater;
import org.eclipse.uide.parser.IParseController;

public class FoldingUpdater implements IFoldingUpdater {
    private Annotation[] fOldAnnotations;

    public void updateFoldingStructure(IParseController parseController, ProjectionAnnotationModel annotationModel) {
	System.out.println("Hello");

	final int N= 1;
	Annotation[] annotations= new Annotation[N];
	HashMap newAnnotations= new HashMap();

	for(int i= 0; i < N; i++) {
	    ProjectionAnnotation annotation= new ProjectionAnnotation();
	    newAnnotations.put(annotation, new Position(0, 50));
	    annotations[i]= annotation;
	}
	annotationModel.modifyAnnotations(fOldAnnotations, newAnnotations, null);
	fOldAnnotations= annotations;
    }
}
