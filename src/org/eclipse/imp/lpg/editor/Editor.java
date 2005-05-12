package org.jikespg.uide.editor;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2004  All Rights Reserved
 */


import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.uide.core.ErrorHandler;
import org.jikespg.uide.parser.Parser;

/**
 *  @author Chris Laffra
 */
public class Editor extends TextEditor {
	static Color COMMENT = new Color(Display.getCurrent(), new RGB(63, 127, 95));
	static Color STRING = new Color(Display.getCurrent(), new RGB(42, 0, 255));
	static Color KEYWORD = new Color(Display.getCurrent(), new RGB(127, 0, 85)); 
	public Editor() {
		super();
        try {
    		setSourceViewerConfiguration(new Configuration());
        }
        catch (Exception e) {
            ErrorHandler.reportError("Could not set JikesPG sourceviewer", e);
        }
	}
	protected void createActions() {
	   super.createActions();
       try {
    	   Action action = new ContentAssistAction(
    	   			ResourceBundle.getBundle("org.jikespg.uide.editor.messages"), 
    				"ContentAssistProposal.", this);
    	   action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
    	   setAction("ContentAssistProposal", action);
    	   markAsStateDependentAction("ContentAssistProposal", true);
       }
       catch (Exception e) {
           ErrorHandler.reportError("Could not initialize JikesPG editor actions", e);
       }
	}

	class Configuration extends SourceViewerConfiguration {
		public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
			PresentationReconciler reconciler = new PresentationReconciler();
			DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(new Scanner());
			reconciler.setRepairer(ddr, IDocument.DEFAULT_CONTENT_TYPE);
			reconciler.setDamager(ddr, IDocument.DEFAULT_CONTENT_TYPE);
			return reconciler;
		}
		public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		   ContentAssistant ca = new ContentAssistant();
		   IContentAssistProcessor processor = new CompletionProcessor();
		   ca.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		   ca.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		   return ca;
		}
		public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
			return new TextHover();
		}
	}
	
	class Scanner extends RuleBasedScanner {
		public Scanner() {
			WordRule rule = new WordRule(new IWordDetector() {
				public boolean isWordStart(char c) {
					return c == '%' || c == '-' || c==':' 
							|| Character.isJavaIdentifierStart(c);
				}

				public boolean isWordPart(char c) {
					return c == ':' || c == '=' 
						|| Character.isJavaIdentifierPart(c);
				}
			});
			for (int n = 0; n < Parser.KEYWORDS.length; n++)
				rule.addWord(Parser.KEYWORDS[n], new Token(new TextAttribute(
						Editor.KEYWORD, null, SWT.BOLD)));
			setRules(new IRule[] {
					rule,
					new SingleLineRule("--", null, new Token(new TextAttribute(
							Editor.COMMENT, null, SWT.BOLD))),
					new SingleLineRule("\"", "\"", new Token(new TextAttribute(
							Editor.STRING)), '\\'),
					new SingleLineRule("'", "'", new Token(new TextAttribute(
							Editor.STRING)), '\\'),
					new WhitespaceRule(new IWhitespaceDetector() {
						public boolean isWhitespace(char c) {
							return Character.isWhitespace(c);
						}
					}), });
		}
	}
	
	/**
	 * See plugin.xml and Editor.createActions how this content assist processor is activated
	 */ 
	class CompletionProcessor implements IContentAssistProcessor { 
		private final IContextInformation[] NO_CONTEXTS = new IContextInformation[0];
		private final char[] PROPOSAL_ACTIVATION_CHARS = new char[] { 's','f','p','n','m', };
		private ICompletionProposal[] NO_COMPLETIONS = new ICompletionProposal[0];
	
		public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
			try {
				IDocument document = viewer.getDocument();
				ArrayList result = new ArrayList();
				String prefix = lastWord(document, offset);
				String indent = lastIndent(document, offset);
				//AstRoot model = AstRoot.getModel(document, null);
				//model.getContentProposals(prefix, indent, offset, result);
				return (ICompletionProposal[]) result.toArray(new ICompletionProposal[result.size()]);
			}
			catch (Throwable e) {
				e.printStackTrace();
				return NO_COMPLETIONS;
			}
		}
		private String lastWord(IDocument doc, int offset) {
			try {
				for (int n = offset-1; n >= 0; n--)
					if (!Character.isJavaIdentifierPart(doc.getChar(n)))
						return doc.get(n + 1, offset-n-1);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			return "";
		}
		private String lastIndent(IDocument doc, int offset) {
			try {
				int start = offset-1; 
				while (start >= 0 && doc.getChar(start)!= '\n') start--;
				int end = start;
				while (end < offset && Character.isSpaceChar(doc.getChar(end))) end++;
				return doc.get(start+1, end-start-1);
			} catch (BadLocationException e) {
				//e.printStackTrace();
			}
			return "";
		}
		public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) { 
			return NO_CONTEXTS;
		}
		public char[] getCompletionProposalAutoActivationCharacters() {
			return PROPOSAL_ACTIVATION_CHARS;
		}
		public char[] getContextInformationAutoActivationCharacters() {
			return null;
		}
		public IContextInformationValidator getContextInformationValidator() {
			return null;
		}
		public String getErrorMessage() {
			return null;
		}
	}

	public class TextHover implements ITextHover {
		public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
			return new Region(offset, 0);
		}
		public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
			try {
				IDocument document = textViewer.getDocument();
				//AstRoot model = AstRoot.getModel(document, null);
				//AstElement element = (AstElement) model.getElementAt(hoverRegion.getOffset());
				//return element.getHoverHelp();
			}
			catch (Exception e) {				
			}
				return ""; 
		}
	}

}
