package org.jikespg.uide.parser;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2004  All Rights Reserved
 */


/**
 * 
 * @author Chris Laffra
 *
 * LPG parser
 * 
 */
public class Parser {
	public final static String KEYWORDS[] = new String[] { 
			"$end", "::=", "%options", "%Options", "$Terminals", "$Alias",
			"$Header", "$End", "$Headers", "$Trailers", "$DefaultAction",
			"$BeginActions", "$EndActions", "$NullAction", "$NoAction",
			"$BeginAction", "$EndAction", "$setSym1", "$getSym", "$getToken",
			"$rule_number", "$rule_text", "$package_declaration",
			"$Rules", "$Define", "$import_classes", "$action_class", "$lex_stream_class",
			"$prs_stream_class", "$eof_token", "$_EOF_SYMBOL", "$prs_stream",
			"$lex_stream", "$Export", "$Eof", "package", "import"
	};
}
