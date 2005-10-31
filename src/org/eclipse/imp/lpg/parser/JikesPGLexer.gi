%options fp=JikesPGLexer,prefix=Char_
%options single-productions
%options package=org.jikespg.uide.parser
%options template=uide/UIDELexerTemplate.gi
%options export_terminals=("JikesPGsym.java", "TK_")
%options verbose

$Title
    /.package $package;./
$End

$Globals
    /.import java.util.*;
    import org.eclipse.uide.parser.ILexer;./
$End

$Define
    $package_declaration /.package $package;./
    $action_class /.$file_prefix./
    $additional_interfaces /., ILexer./
    $prs_stream_class /.PrsStream./
    $eof_token /.$_EOF_TOKEN./

    $kw_lexer_class /.Object./
$End

$Include
    uide/LexerVeryBasicMap.gi
$End

$Export
    DROPSYMBOLS_KEY DROPRULES_KEY NOTICE_KEY DEFINE_KEY TERMINALS_KEY KEYWORDS_KEY EOL_KEY
    EOF_KEY ERROR_KEY IDENTIFIER_KEY ALIAS_KEY TITLE_KEY GLOBALS_KEY
    EMPTY_KEY START_KEY TYPES_KEY RULES_KEY NAMES_KEY END_KEY
    HEADERS_KEY TRAILERS_KEY EXPORT_KEY IMPORT_KEY INCLUDE_KEY
    MACRO_NAME SYMBOL BLOCK EQUIVALENCE PRIORITY_EQUIVALENCE
    ARROW PRIORITY_ARROW OR_MARKER
    EQUAL OPTIONS_KEY COMMA SINGLE_LINE_COMMENT

    EOF_TOKEN ERROR_SYMBOL
$End

$Terminals
    CtlCharNotWS

    LF   CR   HT   FF

    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z
    _

    A    B    C    D    E    F    G    H    I    J    K    L    M
    N    O    P    Q    R    S    T    U    V    W    X    Y    Z

    0    1    2    3    4    5    6    7    8    9

    AfterASCII   ::= '\u0080..\ufffe'
    Space        ::= ' '
    LF           ::= NewLine
    CR           ::= Return
    HT           ::= HorizontalTab
    FF           ::= FormFeed
    DoubleQuote  ::= '"'
    SingleQuote  ::= "'"
    Percent      ::= '%'
    VerticalBar  ::= '|'
    Exclamation  ::= '!'
    AtSign       ::= '@'
    BackQuote    ::= '`'
    Tilde        ::= '~'
    Sharp        ::= '#'
    DollarSign   ::= '$'
    Ampersand    ::= '&'
    Caret        ::= '^'
    Colon        ::= ':'
    SemiColon    ::= ';'
    BackSlash    ::= '\'
    LeftBrace    ::= '{'
    RightBrace   ::= '}'
    LeftBracket  ::= '['
    RightBracket ::= ']'
    QuestionMark ::= '?'
    Comma        ::= ','
    Dot          ::= '.'
    LessThan     ::= '<'
    GreaterThan  ::= '>'
    Plus         ::= '+'
    Minus        ::= '-'
    Slash        ::= '/'
    Star         ::= '*'
    LeftParen    ::= '('
    RightParen   ::= ')'
    Equal        ::= '='
$End

$Eof
    EOF
$End

$Headers
    /.
		public int[] keywords = {
			$_DROPSYMBOLS_KEY, $_DROPRULES_KEY, $_NOTICE_KEY, $_DEFINE_KEY,
			$_TERMINALS_KEY, $_KEYWORDS_KEY, $_IDENTIFIER_KEY, $_ALIAS_KEY,
			$_EMPTY_KEY, $_START_KEY, $_TYPES_KEY, $_RULES_KEY, $_NAMES_KEY, $_END_KEY,
			$_HEADERS_KEY, $_TRAILERS_KEY, $_EXPORT_KEY, $_IMPORT_KEY, $_INCLUDE_KEY,
			$_OPTIONS_KEY, $_TITLE_KEY, $_GLOBALS_KEY
		};
		public boolean isKeyword(IToken token) { return true; }

		public char[][] getKeywords() { return null; }

		public boolean isKeywordStart(char c) { return false; }
     ./

$Start
    Token
$End

$Rules
    digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

    lower ::= a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z

    upper ::= A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z

    letter ::= lower | upper

    any    ::= letter | digit | special | white

    special ::= specialNoDotOrSlash | '.' | '/'

    specialNoDotOrSlash ::= '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' |
                            '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*'

    whiteChar ::= Space | LF | CR | HT | FF

    white ::= whiteChar | white whiteChar

    notEOL ::= letter | digit | special | Space | HT | FF

    notEOLs ::= notEOL | notEOLs notEOL

    slc   ::= '-' '-'
            | slc notEOL

    Token ::= white /.$BeginJava skipToken(); $EndJava./
            | slc   /.$BeginJava makeComment($_SINGLE_LINE_COMMENT); $EndJava./

    Equivalence ::= : : =
    Arrow       ::= - >

    Dots  ::= '.' | Dots '.'

    InsideBlock ::= $empty
                  | InsideBlock Dots specialNoDotOrSlash
                  | InsideBlock '/'
                  | InsideBlock specialNoDotOrSlash

    Block ::= '/' '.' InsideBlock Dots '/'

    Symbol ::= delimitedSymbol | normalSymbol

    delimitedSymbol ::= "'" notEOLs "'"
                      | '"' notEOLs '"'
                      | '<' notEOLs '>'

    normalSymbol ::= letter | normalSymbol symbolChar

    symbolChar ::= letter | digit | '_'

    Token ::= '%' O P T I O N S         /.$BeginJava makeToken($_OPTIONS_KEY);$EndJava./

    Token ::= '$' A L I A S             /.$BeginJava makeToken($_ALIAS_KEY);$EndJava./
    Token ::= '$' D E F I N E           /.$BeginJava makeToken($_DEFINE_KEY);$EndJava./
    Token ::= '$' D R O P S Y M B O L S /.$BeginJava makeToken($_DROPSYMBOLS_KEY);$EndJava./
    Token ::= '$' D R O P R U L E S     /.$BeginJava makeToken($_DROPRULES_KEY);$EndJava./
    Token ::= '$' E M P T Y             /.$BeginJava makeToken($_EMPTY_KEY);$EndJava./
    Token ::= '$' E N D                 /.$BeginJava makeToken($_END_KEY);$EndJava./
    Token ::= '$' E O L                 /.$BeginJava makeToken($_EOL_KEY);$EndJava./
    Token ::= '$' E O F                 /.$BeginJava makeToken($_EOF_KEY);$EndJava./
    Token ::= '$' E R R O R             /.$BeginJava makeToken($_ERROR_KEY);$EndJava./
    Token ::= '$' E X P O R T           /.$BeginJava makeToken($_EXPORT_KEY);$EndJava./
    Token ::= '$' G L O B A L S         /.$BeginJava makeToken($_GLOBALS_KEY);$EndJava./
    Token ::= '$' H E A D E R S         /.$BeginJava makeToken($_HEADERS_KEY);$EndJava./
    Token ::= '$' I D E N T I F I E R   /.$BeginJava makeToken($_IDENTIFIER_KEY);$EndJava./
    Token ::= '$' I M P O R T           /.$BeginJava makeToken($_IMPORT_KEY);$EndJava./
    Token ::= '$' I N C L U D E         /.$BeginJava makeToken($_INCLUDE_KEY);$EndJava./
    Token ::= '$' K E Y W O R D S       /.$BeginJava makeToken($_KEYWORDS_KEY);$EndJava./
    Token ::= '$' N A M E S             /.$BeginJava makeToken($_NAMES_KEY);$EndJava./
    Token ::= '$' N O T I C E           /.$BeginJava makeToken($_NOTICE_KEY);$EndJava./
    Token ::= '$' R U L E S             /.$BeginJava makeToken($_RULES_KEY);$EndJava./
    Token ::= '$' S T A R T             /.$BeginJava makeToken($_START_KEY);$EndJava./
    Token ::= '$' T E R M I N A L S     /.$BeginJava makeToken($_TERMINALS_KEY);$EndJava./
    Token ::= '$' T R A I L E R S       /.$BeginJava makeToken($_TRAILERS_KEY);$EndJava./
    Token ::= '$' T Y P E S             /.$BeginJava makeToken($_TYPES_KEY);$EndJava./
    Token ::= '$' T I T L E             /.$BeginJava makeToken($_TITLE_KEY);$EndJava./

    Token ::= '$' Symbol        /.$BeginJava makeToken($_MACRO_NAME);$EndJava./
    Token ::= Symbol            /.$BeginJava makeToken($_SYMBOL);$EndJava./
    Token ::= Block             /.$BeginJava makeToken($_BLOCK);$EndJava./
    Token ::= Equivalence       /.$BeginJava makeToken($_EQUIVALENCE);$EndJava./
    Token ::= Equivalence ?     /.$BeginJava makeToken($_PRIORITY_EQUIVALENCE);$EndJava./
    Token ::= Arrow             /.$BeginJava makeToken($_ARROW);$EndJava./
    Token ::= Arrow ?           /.$BeginJava makeToken($_PRIORITY_ARROW);$EndJava./
    Token ::= '|'               /.$BeginJava makeToken($_OR_MARKER);$EndJava./
    Token ::= '='               /.$BeginJava makeToken($_EQUAL);$EndJava./
    Token ::= ','               /.$BeginJava makeToken($_COMMA);$EndJava./
$End
