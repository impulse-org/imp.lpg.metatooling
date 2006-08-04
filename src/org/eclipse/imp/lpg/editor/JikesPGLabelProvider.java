/*
 * Created on Jul 7, 2006
 */
package org.jikespg.uide.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.uide.core.ILanguageService;
import org.eclipse.uide.utils.MarkerUtils;
import org.jikespg.uide.IJikesPGResources;
import org.jikespg.uide.JikesPGPlugin;
import org.jikespg.uide.parser.JikesPGParser.*;

public class JikesPGLabelProvider implements ILabelProvider, ILanguageService {
    private Set fListeners= new HashSet();

    private static ImageRegistry sImageRegistry= JikesPGPlugin.getInstance().getImageRegistry();

    private static Image DEFAULT_IMAGE= sImageRegistry.get(IJikesPGResources.DEFAULT_AST);

    private static Image GRAMMAR_FILE_IMAGE= sImageRegistry.get(IJikesPGResources.GRAMMAR_FILE);

    private static Image GRAMMAR_FILE_ERROR_IMAGE= sImageRegistry.get(IJikesPGResources.GRAMMAR_FILE_ERROR);

    private static Image GRAMMAR_FILE_WARNING_IMAGE= sImageRegistry.get(IJikesPGResources.GRAMMAR_FILE_WARNING);

    public Image getImage(Object element) {
	if (element instanceof IFile) {
	    IFile file= (IFile) element;
	    int sev= MarkerUtils.getMaxProblemMarkerSeverity(file, IResource.DEPTH_ONE);

	    switch(sev) {
	    case IMarker.SEVERITY_ERROR: return GRAMMAR_FILE_ERROR_IMAGE;
	    case IMarker.SEVERITY_WARNING: return GRAMMAR_FILE_WARNING_IMAGE;
	    default:
		return GRAMMAR_FILE_IMAGE;
	    }
	}
	ASTNode n= (ASTNode) element;

	return getImageFor(n);
    }

    public static Image getImageFor(ASTNode n) {
	return DEFAULT_IMAGE;
    }

    public String getText(Object element) {
	ASTNode n= (ASTNode) element;

	return getLabelFor(n);
    }

    public static String getLabelFor(ASTNode n) {
	if (n instanceof JikesPG)
	    return "grammar";
	if (n instanceof options_segment)
	    return "options";
	if (n instanceof AliasSeg)
	    return "aliases";
	if (n instanceof DefineSeg)
	    return "defines";
	if (n instanceof GlobalsSeg)
	    return "globals";
	if (n instanceof HeadersSeg)
	    return "headers";
	if (n instanceof ImportSeg)
	    return "imports";
	if (n instanceof IncludeSeg)
	    return "includes";
	if (n instanceof RulesSeg)
	    return "rules";
	if (n instanceof TerminalsSeg)
	    return "terminals";
	if (n instanceof JikesPG_itemList)
	    return "item list";

	if (n instanceof option_spec)
	    return "option spec";
	if (n instanceof option_list)
	    return "%option " + ((option_list) n).getoption().getSYMBOL();
	if (n instanceof nonTermList)
	    return "non-terminals";
	if (n instanceof option)
	    return ((option) n).getSYMBOL().toString();
	if (n instanceof define_segment1)
	    return /*"macro " +*/((define_segment1) n).getmacro_name_symbol().toString();
	if (n instanceof nonTerm)
	    return /*"non-terminal " +*/((nonTerm) n).getSYMBOL().toString();
	if (n instanceof terminal)
	    return /*"terminal " +*/((terminal) n).getterminal_symbol().toString();

	return "<???>";
    }

    public void addListener(ILabelProviderListener listener) {
	fListeners.add(listener);
    }

    public void dispose() {}

    public boolean isLabelProperty(Object element, String property) {
	return false;
    }

    public void removeListener(ILabelProviderListener listener) {
	fListeners.remove(listener);
    }
}
