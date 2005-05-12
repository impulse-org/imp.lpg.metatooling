package org.jikespg.uide.builder;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jikespg.uide.views.JikesPGView;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlOutputParser {
	
	private static HashMap options;
	private static HashMap aliases;
	private static HashSet terminals;
	private static HashMap warnings;
	private static int maximumRuleSize;
	
	static void parse(IProject project, String xmlFileName) throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException {
		options = new HashMap();
		aliases = new HashMap();
		warnings = new HashMap();
		terminals = new HashSet();
		maximumRuleSize = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = parser.parse(xmlFileName);
		doc.getDocumentElement().normalize();
	    Element rootElement = doc.getDocumentElement();
	    NodeList nodes = rootElement.getChildNodes();
	    String file = rootElement.getAttribute("file");
		for(int i=0; i<nodes.getLength(); i++) {
	        Node node = nodes.item(i);
        	String name = node.getNodeName();
        	NamedNodeMap attributes = node.getAttributes();
        	if (name.equals("options")) parseOptions(node, attributes, project);
        	if (name.equals("info")) parseInfo(node, attributes, project);
        	if (name.equals("warning")) parseWarning(node, attributes, project, file);
        	if (name.equals("error")) parseError(node, attributes, project);
        	if (name.equals("aliases")) parseAliases(node, attributes, project);
        	if (name.equals("terminals")) parseTerminals(node, attributes, project);
	    }
		for(int i=0; i<nodes.getLength(); i++) {
	        Node node = nodes.item(i);
        	String name = node.getNodeName();
        	NamedNodeMap attributes = node.getAttributes();
        	if (name.equals("rules")) parseRules(node, attributes, project);
	    }
		JikesPGView.println("");
		JikesPGView.println("A scanner and parser were generated in package "+options.get("PACKAGE"));
		JikesPGView.println("See Main.java for an example how to use the parser.");

	}

	static void parseTerminals(Node node, NamedNodeMap attributes, IProject project) {
		try {
			String packageName = (String)options.get("PACKAGE");
			String input = (String)options.get("GRM-FILE");
			//System.out.println("Parser terminals for "+input);
			String className = new File(input).getName();
			className = className.substring(0, className.lastIndexOf('.'));
			String path = packageName.replace('.', '/');
			IResource resource = getProjectFile(project, getInputDirectory(project), path+"/"+"Token.java");
			resource.delete(true, null);
			File file = getFile(resource);;
			PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
			ps.println(getStub("Token.txt", packageName, className));
			ps.println("\tpublic static String tokenNames[] = new String[NUM_TOKENS+1];");
			ps.println("\tstatic {");
			NodeList nodes = node.getChildNodes();
		    for(int i=0; i<nodes.getLength(); i++) {
		        Node terminal = nodes.item(i);
		        NamedNodeMap optionAttributes = terminal.getAttributes();
	        	String name = getStringAttribute(optionAttributes, "name", null);
	        	if (name == null) continue;
	        	int value = getIntAttribute(optionAttributes, "value", 0);
	        	terminals.add(name);
				ps.println("\t\t\ttokenNames["+value+"] = \""+name+"\";");
			}
			ps.println("\t}");
			ps.println("}");
			ps.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void parseAliases(Node node, NamedNodeMap attributes, IProject project) {
		try {
			NodeList nodes = node.getChildNodes();
		    for(int i=0; i<nodes.getLength(); i++) {
		        Node alias = nodes.item(i);
		        NamedNodeMap optionAttributes = alias.getAttributes();
	        	String name = getStringAttribute(optionAttributes, "name", null);
	        	String value = getStringAttribute(optionAttributes, "value", null);
	        	if (value != null)
	        		aliases.put(value, name);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void parseError(Node node, NamedNodeMap attributes, IProject project) {
		try {
	        String msg = getStringAttribute(attributes, "kind", "<Unknown Error>");
	        if (msg.startsWith("Informative:"))
	        	return;
	        String file = getStringAttribute(attributes, "file", null);
	        if (file != null) msg += ": "+file;
	        String template = getStringAttribute(attributes, "template", "");
	        IResource resource = getProjectFile(project, getOutputDirectory(project), file);
	        int lineNumber = getIntAttribute(attributes, "start-line", 1);
	        int startOffset = getIntAttribute(attributes, "start-offset", 1);
	        int endOffset = getIntAttribute(attributes, "end-offset", 1);
	        reportError(IMarker.SEVERITY_ERROR, resource, msg, lineNumber, startOffset, endOffset);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }

	static void parseWarning(Node node, NamedNodeMap attributes, IProject project, String grammarFile) {
		try {
	        String msg = "";
	        String kind = getStringAttribute(attributes, "kind", "<Unknown Warning>");
	        int line = getIntAttribute(attributes, "line", 1);
	        int start = line-1+getIntAttribute(attributes, "start", 0);
	        int end = line-1+getIntAttribute(attributes, "end", 0);
	        if ("Undeclared symbols assumed terminals".equals(kind)) {
	        	msg = kind + ": ";
	        	NodeList nodes = node.getChildNodes();
	        	for(int i=0; i<nodes.getLength(); i++) {
	        		msg += getStringAttribute(nodes.item(i).getAttributes(), "name", "")+" ";
	        	}
	        }
	        else
	        if ("rrconflict".equals(kind)) {
	        	msg = "Reduce/reduce conflict on ";
	        	msg += getStringAttribute(attributes, "symbol", "<Unknown Symbol>") + " between rule ";
	        	msg += getStringAttribute(attributes, "rule1", "<Unknown Rule>") + " and rule ";
	        	msg += getStringAttribute(attributes, "rule2", "<Unknown Rule>");
	        }
	        else
	        if ("srconflict".equals(kind)) {
	        	msg = "Shift/reduce conflict on ";
	        	msg += getStringAttribute(attributes, "symbol", "<Unknown Symbol>");
	        	String ruleNumber = getStringAttribute(attributes, "rule", "-1");
	        	warnings.put(ruleNumber, msg);
	        	msg += " at rule " + ruleNumber;
	        }
	        else
	        	msg = kind;
	        String file = getStringAttribute(attributes, "file", grammarFile);
	        IResource resource = getProjectFile(project, getOutputDirectory(project), file);
	        reportError(IMarker.SEVERITY_WARNING, resource, msg, line, start, end);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static IResource getProjectFile(IProject project, String directory, String fileName) {
		try {
			String projectPath = project.getLocation().toOSString();
			String relativePath = fileName;
			if (fileName.length() > projectPath.length()) {
                if (!fileName.startsWith(projectPath))
                    throw new IllegalArgumentException("Illegal project: "+projectPath+" for "+fileName);
				relativePath = fileName.substring(projectPath.length());
            }
			IFile file = project.getFile(relativePath);
			if (!file.exists()) {
				fileName = directory + "/"+fileName;
				relativePath = fileName.substring(projectPath.length());
				file = project.getFile(relativePath);
			}
            return file;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static void parseOptions(Node node, NamedNodeMap attributes, IProject project) {
		try {
			String file = getStringAttribute(attributes, "file", "");
	        IResource resource = getProjectFile(project, getOutputDirectory(project), file);
		    NodeList nodes = node.getChildNodes();
		    for(int i=0; i<nodes.getLength(); i++) {
		        Node option = nodes.item(i);
		        NamedNodeMap optionAttributes = option.getAttributes();
	        	String name = getStringAttribute(optionAttributes, "name", "");
	        	String value = getStringAttribute(optionAttributes, "value", "");
	        	options.put(name, value);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    static String getOutputDirectory(IProject project) throws IOException {
        return new File(project.getLocation().toFile(), "/src").getAbsolutePath().replace('\\','/');
    }

    static String getInputDirectory(IProject project) throws IOException {
        return new File(project.getLocation().toFile(), "/src").getAbsolutePath().replace('\\','/');
    }

	static void parseInfo(Node node, NamedNodeMap attributes, IProject project) {
		try {
	        String msg = "";
	        String kind = getStringAttribute(attributes, "kind", "<Unknown Info>");
	        if ("lalr".equals(kind)) 
	        	msg = "This grammar is LALR(" + getIntAttribute(attributes, "level", 0) + ")";
	        if ("lr0".equals(kind)) 
	        	msg = "This grammar is LR(0)";
	        if ("slr1".equals(kind)) 
	        	msg = "This grammar is SLR(1)";
	        String file = getStringAttribute(attributes, "file", null);
	        IResource resource = getProjectFile(project, getOutputDirectory(project), file);
	        reportError(IMarker.SEVERITY_INFO, resource, msg, 1, 1, 1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String getStringAttribute(NamedNodeMap attributes, String name, String defaultValue) {
		try {
			return attributes.getNamedItem(name).getNodeValue();
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
	static int getIntAttribute(NamedNodeMap attributes, String name, int defaultValue) {
		try {
			Node node = attributes.getNamedItem(name);
			return Integer.parseInt(node.getNodeValue());
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	static void reportError(int kind, IResource resource, String msg, int lineNumber, int charStart, int charEnd) throws CoreException  {
		if (lineNumber == 0) lineNumber = 1;
		IMarker marker = resource.createMarker(IMarker.PROBLEM);
		marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		marker.setAttribute(IMarker.CHAR_START, charStart);
		marker.setAttribute(IMarker.CHAR_END, charEnd);
		marker.setAttribute(IMarker.MESSAGE, msg);
		marker.setAttribute(IMarker.PRIORITY,IMarker.PRIORITY_HIGH);
		marker.setAttribute(IMarker.SEVERITY, kind);
	}

	static void parseRules(Node node, NamedNodeMap attributes, IProject project) {
		try {
			String input = (String)options.get("GRM-FILE");
			System.out.println("Parser rules for "+input);
			String className = new File(input).getName();
			className = className.substring(0, className.lastIndexOf('.'));
			String packageName = (String)options.get("PACKAGE");
            
			createParserFile(project, node, packageName, className, packageName.replace('.', '/')+"/AbstractParser.java");
			createRulesFile(project, node, packageName, className, packageName.replace('.', '/')+"/Rule.java");
			createAbstractScannerFile(project, packageName, className, packageName.replace('.', '/')+"/AbstractScanner.java");
            createFile(project, packageName, className, "Model", ".java");
            createFile(project, packageName, className, "Scanner", ".java");
            createFile(project, packageName, className, "Parser", ".java");
            createFile(project, packageName, className, "CharStream", ".java");
            createFile(project, packageName, className, "LexStream", ".java");
			createFile(project, packageName, className, "LexerSymbols", ".java");
			createFile(project, packageName, className, "TokenKindMap", ".java");			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String escapeString(String keyword) {
		StringBuffer buf = new StringBuffer(keyword);
		for (int n=buf.length()-1; n>=0; n--) {
			switch (buf.charAt(n)) {
			case '"': 
			case '\\': 
				buf.insert(n, '\\'); 
				break;
			}
		}
		return buf.toString();
	}

	private static String escapeChar(char c) {
		switch (c) {
			case '\'': 
			case '\\': return "\\"+c;
			default: 
				return Character.toString(c);
		}
	}

	private static void createParserFile(IProject project, Node node, String packageName, String className, String parserFileName) throws IOException, CoreException, FileNotFoundException {
		IResource resource = getProjectFile(project, getInputDirectory(project), parserFileName);
		resource.delete(true, null);
		File file = getFile(resource);;
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
		ps.println(getStub("AbstractParser1.txt", packageName, className));
		NodeList rules = node.getChildNodes();
		generateParserRules(ps, rules, packageName, className);
		ps.println(getStub("AbstractParser2.txt", packageName, className));
		ps.close();
	}

	private static void generateParserRules(PrintStream ps, NodeList rules, String packageName, String className) {
        ps.println("\t\tif (DEBUG) System.out.println(\"Reduce: \"+Rule.rules[ruleNumber]);");
        ps.println("\t\tswitch (ruleNumber) {");
        for (int n=0; n<rules.getLength(); n++) {
            Node rule = rules.item(n);
            NamedNodeMap attributes = rule.getAttributes();
            int nr = getIntAttribute(attributes, "number", -1);
            if (nr < 1) continue;
            ps.println("\t\t\tcase "+nr+": ruleAction"+nr+"(); break;");
        }
        ps.println("\t\t}\n\t}");
        ps.println("\tprotected Ast createAstNode(Rule rule) {");
        ps.println("\t\tAst node = new Ast(rule.lhs);");
        ps.println("\t\tnode.rule = rule;");
        ps.println("\t\treturn node;");
        ps.println("\t}");
        for (int n=0; n<rules.getLength(); n++) {
            Node rule = rules.item(n);
            NamedNodeMap attributes = rule.getAttributes();
            int nr = getIntAttribute(attributes, "number", -1);
            if (nr < 1) continue;
            generateParserRuleBody(ps, rule, packageName, attributes, nr, className);
        }

    }

	private static void generateParserRuleBody(PrintStream ps, Node rule, String packageName, NamedNodeMap attributes, int nr, String className) {
		String lhs = getStringAttribute(attributes, "lhs", "<unknown lhs>");
		String astClass = lhs;
		NodeList rhss = rule.getChildNodes();
		for (int k=rhss.getLength()-1; k>=0; k--) {
			Node rhs = rhss.item(k);
			if (!rhs.getNodeName().equals("rhs")) rule.removeChild(rhs);
		}			
		rhss = rule.getChildNodes();
        ps.println();
		ps.println("\tvoid ruleAction"+nr+"() {");
		String base = packageName.substring(0, packageName.lastIndexOf('.'));
		String astName = base+".model.AstNodeWith"+rhss.getLength()+"Children";
		ps.println("\t\tAst ast = createAstNode(Rule.rule"+nr+");");
		for (int k=0; k<rhss.getLength(); k++) {
			ps.println("\t\tast.addChild(this,(Ast)getSym(Rule.rule"+nr+", "+(k+1)+
							"), prsStream.getTokenAt(lpgParser.getToken("+(k+1)+")));");
		}
		ps.println("\t\tsetSym1(ast);");
		ps.println("\t}");
	}

	private static void createRulesFile(IProject project, Node node, String packageName, String className, String rulesFileName) throws IOException, CoreException, FileNotFoundException {
		IResource resource = getProjectFile(project, getInputDirectory(project), rulesFileName);
		resource.delete(true, null);
		File file = getFile(resource);;
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
		ps.println(getStub("Rules.txt", packageName, className));
		NodeList rules = node.getChildNodes();
		generateRules(ps, rules, className);
		ps.close();
	}

	private static void generateRules(PrintStream ps, NodeList rules, String className) {
		for (int n=0; n<rules.getLength(); n++) {
			Node rule = rules.item(n);
			NamedNodeMap attributes = rule.getAttributes();
			int nr = getIntAttribute(attributes, "number", -1);
			if (nr < 1) continue;
			ps.println("\tpublic static Rule rule"+nr+" = new Rule("+nr+", "+
                                    getRuleDescriptor(rule, attributes)+");");
		}
		ps.println("\tpublic static Rule rules[] = {");
		for (int n=0; n<rules.getLength(); n++) {
			Node rule = rules.item(n);
			NamedNodeMap attributes = rule.getAttributes();
			int nr = getIntAttribute(attributes, "number", -1);
			if (nr < 1) continue;
			ps.println("\t\trule"+nr+", ");
		}		
		ps.println("\t};");
		ps.println("\tstatic {");
		ps.println("\t\tnonTerminals = new HashSet(100);");
		ps.println("\t\tfor (int n=0; n<rules.length; n++)");
		ps.println("\t\t\tnonTerminals.add(rules[n].getLeftHandSide());");
		ps.println("\t};");
		ps.println("");
		ps.println("}");
	}

    private static String getRuleDescriptor(Node rule, NamedNodeMap attributes) {
        String lhs = getStringAttribute(attributes, "lhs", "<unknown lhs>");
        NodeList rhss = rule.getChildNodes();
        for (int k=rhss.getLength()-1; k>=0; k--) {
            Node rhs = rhss.item(k);
            if (!rhs.getNodeName().equals("rhs")) rule.removeChild(rhs);
        }
        rhss = rule.getChildNodes();
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"");
        buffer.append(lhs);
        buffer.append("\"");
        buffer.append(", new String[] { ");
        for (int k=0; k<rhss.getLength(); k++) {
            Node rhs = rhss.item(k);
            buffer.append("\"");
            buffer.append(getStringAttribute(rhs.getAttributes(), "symbol", ""));
            buffer.append("\", ");
        }
        buffer.append("}");
        return buffer.toString();
    }

	private static void createAbstractScannerFile(IProject project, String packageName, String className, String scannerFileName) throws IOException, CoreException, FileNotFoundException {
		IResource resource = getProjectFile(project, getInputDirectory(project), scannerFileName);
		resource.delete(true, null);
		File file = getFile(resource);;
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
		ps.println(getStub("AbstractScanner.txt", packageName, className));
		HashSet uniqueStartingLetters = new HashSet();
		Iterator aliasIterator = aliases.values().iterator();
		Iterator variableIterator = aliases.keySet().iterator();
		ArrayList sortedKeywordNames = new ArrayList();
		ArrayList sortedKeywordValues = new ArrayList();
		while (aliasIterator.hasNext()) {
			String keyword = escapeString((String)aliasIterator.next());
			String variable = (String)variableIterator.next();
			int length = keyword.length();
			int index = sortedKeywordNames.size()-1;
			for (;index >= 0 && length <= ((String)sortedKeywordNames.get(index)).length(); index--)
				;
			index++;
			sortedKeywordNames.add(index, keyword);
			sortedKeywordValues.add(index, variable);
			uniqueStartingLetters.add(keyword.substring(0,1));
		}
		ps.println("\tpublic static char KEYWORDS[][] = {");
		int size = sortedKeywordNames.size();
		for (int n=size-1; n>=0; n--) {
			String keyword = (String)sortedKeywordNames.get(n);
			ps.print("\t\t/*"+(size-1-n)+" */\tnew char[]{ ");
			for (int k=0; k<keyword.length(); k++) {
				char c = keyword.charAt(k);
				switch (c) {
				case '\\': 
				case '\'': 
					ps.print("'\\"+keyword.charAt(k)+"',");
					break;
				default:
					ps.print("'"+keyword.charAt(k)+"',");
					break;
				}
			}
			ps.println(" } ,");
		}
		ps.println("\t};");
		ps.println("\tpublic static int KEYWORD_VALUES[] = {");
		for (int n=size-1; n>=0; n--) {
			ps.println("\t\t/*"+(size-1-n)+" */\t"+sortedKeywordValues.get(n)+",");
		}
		ps.println("\t};");
		ps.println("\tpublic static boolean isKeywordStart(char c) {");
		ps.println("\t\tswitch(c) {");
		Iterator uniqueIterator = uniqueStartingLetters.iterator();
		while (uniqueIterator.hasNext()) {
			String startingLetter = (String)uniqueIterator.next();
			ps.println("\t\tcase '"+escapeChar(startingLetter.charAt(0))+"':");
		}
		ps.println("\t\t\treturn true;");
		ps.println("\t\tdefault:");
		ps.println("\t\t\treturn Character.isJavaIdentifierStart(c);");
		ps.println("\t\t}");
		ps.println("\t}");
		ps.println("}");
		ps.close();
	}

	static File getFile(IResource resource) {
		File dir = new File(resource.getParent().getLocation().toOSString());
		dir.mkdirs();
		dir.mkdir();
		File file = new File(resource.getLocation().toOSString());
		return file;
	}
	
	private static void createFile(IProject project, String packageName, String className, 
				String fileName, String extension) throws IOException, CoreException, FileNotFoundException {
		String srcFileName = packageName.replace('.','/')+"/"+fileName+extension;
		IResource resource = getProjectFile(project, getInputDirectory(project), srcFileName);
		if (resource.exists())
			return;
		File file = getFile(resource);;
		System.out.println("Creating file "+file);
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
		ps.println(getStub(fileName+".txt", packageName, className));
		ps.close();
	}

	public static String getStub(String name, String packageName, String className) {
		try {
			DataInputStream is = new DataInputStream(XmlOutputParser.class.getResourceAsStream(name));
			byte bytes[] = new byte[is.available()];
			is.readFully(bytes);
			is.close();
			StringBuffer sb = new StringBuffer(new String(bytes));
            replace(sb, "$CLS$", className);
            replace(sb, "$LNG$", className.toLowerCase());
			replace(sb, "$PKG$", packageName);
			return sb.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return "// missing stub: "+name;
		}
	}
	
	static void replace(StringBuffer sb, String goal, String substitute) {
		for (int index = sb.indexOf(goal); index!=-1; index = sb.indexOf(goal))
			sb.replace(index, index+goal.length(), substitute);
	}
}

// TODO: wizard with checkboxes
// TODO: builder, outline, folding editor
// TODO: self-host lpg editor
// TODO: plugin.xml creation/fixing