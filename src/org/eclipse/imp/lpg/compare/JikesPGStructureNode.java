package org.jikespg.uide.compare;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DocumentRangeNode;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;

public class JikesPGStructureNode extends DocumentRangeNode implements ITypedElement {

    public JikesPGStructureNode(int typeCode, String id, IDocument document, int start, int length) {
	super(typeCode, id, document, start, length);
    }

    public String getName() {
	return "foo";
    }

    public Image getImage() {
	return null;
    }

    public String getType() {
	return "bar";
    }
}
