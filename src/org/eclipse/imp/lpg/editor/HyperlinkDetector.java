package org.jikespg.uide.editor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;

public class HyperlinkDetector implements IHyperlinkDetector {

    public HyperlinkDetector() { }

    public IHyperlink[] detectHyperlinks(final ITextViewer textViewer, final IRegion region, boolean canShowMultipleHyperlinks) {
        return new IHyperlink[] { new IHyperlink() {
            public IRegion getHyperlinkRegion() {
                return new Region(region.getOffset() - 2, region.getLength() + 4);
            }

            public String getTypeLabel() {
                return null;
            }

            public String getHyperlinkText() {
                return "hah!";
            }

            public void open() {
                textViewer.setSelectedRange(0, 5);
            }
        } };
    }
}
