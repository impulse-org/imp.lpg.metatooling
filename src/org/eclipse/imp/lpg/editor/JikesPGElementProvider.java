package org.jikespg.uide.editor;

import lpg.lpgjavaruntime.IToken;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uide.editor.UniversalEditor;
import org.eclipse.uide.parser.IParseController;

public class JikesPGElementProvider implements IInformationProvider, IInformationProviderExtension {
    private UniversalEditor fEditor;
    private boolean fUseCodeResolve;

    public JikesPGElementProvider(IEditorPart editor) {
	fUseCodeResolve= false;
	if (editor instanceof UniversalEditor)
	    fEditor= (UniversalEditor) editor;
    }

    public JikesPGElementProvider(IEditorPart editor, boolean useCodeResolve) {
	this(editor);
	fUseCodeResolve= useCodeResolve;
    }

    /*
     * @see IInformationProvider#getSubject(ITextViewer, int)
     */
    public IRegion getSubject(ITextViewer textViewer, int offset) {
	if (textViewer != null && fEditor != null) {
	    IToken token= fEditor.getParseController().getParser().getParseStream().getTokenAtCharacter(offset);

	    if (token != null)
		return new Region(token.getStartOffset(), token.getEndOffset() - token.getStartOffset() + 1);
	    return new Region(offset, 0);
	}
	return null;
    }

    /*
     * @see IInformationProvider#getInformation(ITextViewer, IRegion)
     */
    public String getInformation(ITextViewer textViewer, IRegion subject) {
	return getInformation2(textViewer, subject).toString();
    }

    /*
     * @see IInformationProviderExtension#getElement(ITextViewer, IRegion)
     */
    public Object getInformation2(ITextViewer textViewer, IRegion subject) {
	if (fEditor == null)
	    return null;
	IParseController parseController= fEditor.getParseController();
	Object astRoot= parseController.getCurrentAst();
	Object astNode= parseController.getNodeLocator().findNode(astRoot, fEditor.getSelection().x);

	return astNode;
    }
}
