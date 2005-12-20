%options table=java,escape=$,la=3
%options fp=JikesPGParser,prefix=TK_
%options package=org.jikespg.uide.parser
%options automatic_ast,ast=ASTNode,visitor=deep
%options template=uide/UIDEbtParserTemplate.gi
%options import_terminals=JikesPGLexer.gi

$Title
    /.package $package;./
$End

$Globals
    /.import org.eclipse.uide.parser.IParser;./
$End

$Define
    $ast_class /.Object./
    $action_class /.$file_prefix./
    $additional_interfaces /., IParser./
$End

$Terminals
    DROPSYMBOLS_KEY DROPRULES_KEY NOTICE_KEY DEFINE_KEY TERMINALS_KEY KEYWORDS_KEY EOL_KEY
    EOF_KEY ERROR_KEY IDENTIFIER_KEY ALIAS_KEY TITLE_KEY GLOBALS_KEY
    EMPTY_KEY START_KEY TYPES_KEY RULES_KEY NAMES_KEY END_KEY
    HEADERS_KEY TRAILERS_KEY EXPORT_KEY IMPORT_KEY INCLUDE_KEY
    MACRO_NAME SYMBOL BLOCK EQUIVALENCE PRIORITY_EQUIVALENCE
    ARROW PRIORITY_ARROW OR_MARKER
    EQUAL OPTIONS_KEY

    EOF_TOKEN ERROR_SYMBOL

    EQUAL ::= '='
    EQUIVALENCE ::= '::='
    PRIORITY_EQUIVALENCE ::= '::=?'
    ARROW ::= '->'
    COMMA ::= ','
    PRIORITY_ARROW ::= '->?'
    OR_MARKER ::= '|'
    LEFT_PAREN ::= '('
    RIGHT_PAREN ::= ')'
$End

$EOF
    EOF_TOKEN
$End

$Start
    JikesPG
$End

$Rules
    JikesPG ::= options_segment JikesPG_INPUT

    JikesPG_INPUT ::= $empty | JikesPG_INPUT JikesPG_item END_KEY_OPT

    JikesPG_item$AliasSeg      ::= alias_segment
    JikesPG_item$DefineSeg     ::= define_segment
    JikesPG_item$EofSeg        ::= eof_segment
    JikesPG_item$EolSeg        ::= eol_segment
    JikesPG_item$ErrorSeg      ::= error_segment
    JikesPG_item$ExportSeg     ::= export_segment
    JikesPG_item$GlobalsSeg    ::= globals_segment
    JikesPG_item$HeadersSeg    ::= headers_segment
    JikesPG_item$ImportSeg     ::= import_segment
    JikesPG_item$IdentifierSeg ::= identifier_segment
    JikesPG_item$IncludeSeg    ::= include_segment
    JikesPG_item$KeywordsSeg   ::= keywords_segment
    JikesPG_item$NamesSeg      ::= names_segment
    JikesPG_item$NoticeSeg     ::= notice_segment
    JikesPG_item$RulesSeg      ::= rules_segment
    JikesPG_item$StartSeg      ::= start_segment
    JikesPG_item$TerminalsSeg  ::= terminals_segment
    JikesPG_item$TitleSeg      ::= title_segment
    JikesPG_item$TrailersSeg   ::= trailers_segment
    JikesPG_item$TypesSeg      ::= types_segment

    options_segment ::= $empty | options_segment option_spec
    option_spec ::= OPTIONS_KEY option_list
    option_list ::= option | option_list ',' option
    option ::= SYMBOL option_value
    option_value ::= $empty | '=' SYMBOL | '=' '(' symbol_list ')'

    symbol_list$$SYMBOL ::= SYMBOL | symbol_list ',' SYMBOL

    title_segment ::= TITLE_KEY
    title_segment ::= TITLE_KEY action_segment

    globals_segment ::= GLOBALS_KEY
    globals_segment ::= globals_segment action_segment

    include_segment ::= INCLUDE_KEY
    include_segment ::= INCLUDE_KEY SYMBOL

    notice_segment ::= NOTICE_KEY
    notice_segment ::= notice_segment action_segment

    define_segment ::= DEFINE_KEY
    define_segment ::= define_segment macro_name_symbol macro_segment 

    macro_name_symbol ::= MACRO_NAME
    macro_name_symbol ::= SYMBOL -- warning: escape prefix missing...
    macro_segment ::= BLOCK 

    terminals_segment ::= TERMINALS_KEY 
    terminals_segment ::= terminals_segment terminal_symbol
    terminals_segment ::= terminals_segment terminal_symbol produces name

    export_segment ::= EXPORT_KEY 
    export_segment ::= export_segment terminal_symbol

    import_segment ::= IMPORT_KEY 
    import_segment ::= IMPORT_KEY SYMBOL drop_command_list

    drop_command ::= drop_symbols
    drop_command ::= drop_rules

    drop_symbols ::= DROPSYMBOLS_KEY
    drop_symbols ::= drop_symbols SYMBOL

    drop_rules ::= DROPRULES_KEY
    drop_rules ::= drop_rules drop_rule

    drop_rule ::= SYMBOL produces rhs
    drop_rule ::= SYMBOL MACRO_NAME produces rhs
    drop_rule ::= drop_rule '|' rhs

    drop_command_list ::= $empty

    drop_command_list ::= drop_command_list drop_command

    keywords_segment ::= KEYWORDS_KEY
    keywords_segment ::= keywords_segment terminal_symbol
    keywords_segment ::= keywords_segment terminal_symbol produces name

    error_segment ::= ERROR_KEY
    error_segment ::= ERROR_KEY terminal_symbol

    identifier_segment ::= IDENTIFIER_KEY
    identifier_segment ::= IDENTIFIER_KEY terminal_symbol

    eol_segment ::= EOL_KEY
    eol_segment ::= EOL_KEY terminal_symbol

    eof_segment ::= EOF_KEY
    eof_segment ::= EOF_KEY terminal_symbol

    terminal_symbol ::= SYMBOL
    terminal_symbol ::= MACRO_NAME -- warning: escape prefix used in symbol

    alias_segment ::= ALIAS_KEY
    alias_segment ::= alias_segment ERROR_KEY produces alias_rhs 
    alias_segment ::= alias_segment EOL_KEY produces alias_rhs 
    alias_segment ::= alias_segment EOF_KEY produces alias_rhs 
    alias_segment ::= alias_segment IDENTIFIER_KEY produces alias_rhs 
    alias_segment ::= alias_segment SYMBOL produces alias_rhs 
    alias_segment ::= alias_segment alias_lhs_macro_name produces alias_rhs 

    alias_lhs_macro_name ::= MACRO_NAME -- warning: escape prefix used in symbol

    alias_rhs ::= SYMBOL 
    alias_rhs ::= MACRO_NAME -- warning: escape prefix used in symbol
    alias_rhs ::= ERROR_KEY 
    alias_rhs ::= EOL_KEY 
    alias_rhs ::= EOF_KEY 
    alias_rhs ::= EMPTY_KEY 
    alias_rhs ::= IDENTIFIER_KEY

    start_segment ::= START_KEY start_symbol

    headers_segment ::= HEADERS_KEY
    headers_segment ::= headers_segment action_segment

    trailers_segment ::= TRAILERS_KEY
    trailers_segment ::= trailers_segment action_segment

    start_symbol ::= SYMBOL 
    start_symbol ::= MACRO_NAME

    rules_segment ::= RULES_KEY$ action_segment_list nonTermList

    nonTermList$$nonTerm ::= $empty | nonTermList nonTerm

    nonTerm ::= SYMBOL optMacro produces rhsList
    rhsList ::= rhs | rhsList '|'$ rhs
    optMacro ::= MACRO_NAME | $empty

--    rules$Rule ::= SYMBOL produces rhs
--    rules$Rule ::= SYMBOL MACRO_NAME produces rhs
--    rules$Rule ::= rules '|'$ rhs

    produces ::= '::='
    produces ::= '::=?'
    produces ::= '->'
    produces ::= '->?'

    rhs ::= $empty
    rhs$rhsSymbol ::= rhs SYMBOL
    rhs$rhsSymbolMacro ::= rhs SYMBOL MACRO_NAME
    rhs$emptyRHS ::= rhs EMPTY_KEY 
    rhs$rhsAction ::= rhs action_segment

    action_segment ::= BLOCK 

    types_segment ::= TYPES_KEY
    types_segment ::= types_segment type_declarations

    type_declarations ::= SYMBOL produces SYMBOL
                        | type_declarations '|' SYMBOL

    names_segment ::= NAMES_KEY
    names_segment ::= names_segment name produces name 

    name ::= SYMBOL
    name ::= MACRO_NAME -- warning: escape prefix used in symbol
    name ::= EMPTY_KEY 
    name ::= ERROR_KEY 
    name ::= EOL_KEY 
    name ::= IDENTIFIER_KEY

    END_KEY_OPT ::= $empty
    END_KEY_OPT ::= END_KEY 

    action_segment_list ::= $empty
    action_segment_list ::= action_segment_list action_segment 
$End
