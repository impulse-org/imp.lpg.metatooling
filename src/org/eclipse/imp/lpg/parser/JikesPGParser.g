%options la=6
%options package=org.jikespg.uide.parser
%options automatic_ast,ast_type=ASTNode,visitor=preorder
%options template=uide/btParserTemplate.gi
%options import_terminals=JikesPGLexer.gi

$Globals
    /.import org.eclipse.uide.parser.IParser;
import org.eclipse.uide.editor.IMessageHandler;
     ./
$End

$Define
    $ast_class /.Object./
    $additional_interfaces /., IParser./
$End

$Terminals
    EQUIVALENCE ::= '::='
    PRIORITY_EQUIVALENCE ::= '::=?'
    ARROW ::= '->'
    PRIORITY_ARROW ::= '->?'
    OR_MARKER ::= '|'
    EQUAL ::= '='
    COMMA ::= ','
    LEFT_PAREN ::= '('
    RIGHT_PAREN ::= ')'
$End

$Start
    JikesPG
$End

$Headers
    /.
/*
    private IMessageHandler msgHandler;

    public void setMessageHandler(IMessageHandler handler) {
        msgHandler= handler;
    }

    public void reportError(int errorCode, String locationInfo, int leftToken, int rightToken, String tokenText)
    {
        int len= getEndOffset(rightToken) - getStartOffset(leftToken) + 1;
        if (msgHandler != null)
          msgHandler.handleMessage(getStartOffset(leftToken), len, errorMsgText[errorCode]);
    }
*/
     ./
$End

$Rules
    JikesPG ::= options_segment JikesPG_INPUT

    JikesPG_INPUT$$JikesPG_item ::= $empty
                                |   JikesPG_INPUT JikesPG_item

    JikesPG_item$AliasSeg      ::= alias_segment      END_KEY_OPT
    JikesPG_item$DefineSeg     ::= define_segment     END_KEY_OPT
    JikesPG_item$EofSeg        ::= eof_segment        END_KEY_OPT
    JikesPG_item$EolSeg        ::= eol_segment        END_KEY_OPT
    JikesPG_item$ErrorSeg      ::= error_segment      END_KEY_OPT
    JikesPG_item$ExportSeg     ::= export_segment     END_KEY_OPT
    JikesPG_item$GlobalsSeg    ::= globals_segment    END_KEY_OPT
    JikesPG_item$HeadersSeg    ::= headers_segment    END_KEY_OPT
    JikesPG_item$ImportSeg     ::= import_segment     END_KEY_OPT
    JikesPG_item$IdentifierSeg ::= identifier_segment END_KEY_OPT
    JikesPG_item$IncludeSeg    ::= include_segment    END_KEY_OPT
    JikesPG_item$KeywordsSeg   ::= keywords_segment   END_KEY_OPT
    JikesPG_item$NamesSeg      ::= names_segment      END_KEY_OPT
    JikesPG_item$NoticeSeg     ::= notice_segment     END_KEY_OPT
    JikesPG_item$RulesSeg      ::= rules_segment      END_KEY_OPT
    JikesPG_item$StartSeg      ::= start_segment      END_KEY_OPT
    JikesPG_item$TerminalsSeg  ::= terminals_segment  END_KEY_OPT
    JikesPG_item$AstSeg        ::= ast_segment        END_KEY_OPT
    JikesPG_item$TrailersSeg   ::= trailers_segment   END_KEY_OPT
    JikesPG_item$TypesSeg      ::= types_segment      END_KEY_OPT

    options_segment ::= $empty | options_segment option_spec
    option_spec$option_spec ::= OPTIONS_KEY
    option_spec$option_spec ::= OPTIONS_KEY option_list
    option_list ::= option | option_list ',' option
    option ::= SYMBOL option_value
    option_value ::= $empty | '=' SYMBOL | '=' '(' symbol_list ')'

    symbol_list$$SYMBOL ::= SYMBOL
                          | symbol_list ',' SYMBOL

    ast_segment ::= AST_KEY
    ast_segment ::= AST_KEY action_segment

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

    terminals_segment$$terminal ::= TERMINALS_KEY 
    terminals_segment$$terminal ::= terminals_segment terminal
--  terminals_segment ::= terminals_segment terminal_symbol produces name

    terminal ::= terminal_symbol optTerminalAlias
    optTerminalAlias ::= $empty | produces name

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

    nonTerm$nonTerm ::= SYMBOL produces rhsList
    nonTerm$nonTerm ::= SYMBOL MACRO_NAME$className produces rhsList
    nonTerm$nonTerm ::= SYMBOL MACRO_NAME$className MACRO_NAME$arrayElement produces rhsList
    rhsList$$rhs ::= rhs | rhsList '|'$ rhs

--    rules$$Rule ::= SYMBOL produces rhs
--    rules$$Rule ::= SYMBOL MACRO_NAME$classname produces rhs
--    rules$$Rule ::= SYMBOL MACRO_NAME$className MACRO_NAME$arrayName produces rhs
--    rules$$Rule ::= rules '|'$ rhs

    produces ::= '::='
    produces ::= '::=?'
    produces ::= '->'
    produces ::= '->?'

--  rhs$RhsList$ ::= $empty
--  rhs$RhsList  ::= rhs SYMBOL
--  rhs$RhsList  ::= rhs EMPTY_KEY 
--  rhs$RhsList  ::= rhs action_segment

    rhs ::= symWithAttrsList opt_action_segment

    symWithAttrsList ::= symWithAttrs | symWithAttrsList symWithAttrs

    symWithAttrs ::= EMPTY_KEY
    symWithAttrs ::= SYMBOL
    symWithAttrs ::= SYMBOL MACRO_NAME

    opt_action_segment ::= $empty | action_segment

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
