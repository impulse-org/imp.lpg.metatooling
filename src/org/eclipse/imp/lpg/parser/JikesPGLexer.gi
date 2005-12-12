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
    LEFT_PAREN RIGHT_PAREN

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

    aA ::= a | A
    bB ::= b | B
    cC ::= c | C
    dD ::= d | D
    eE ::= e | E
    fF ::= f | F
    gG ::= g | G
    hH ::= h | H
    iI ::= i | I
    jJ ::= j | J
    kK ::= k | K
    lL ::= l | L
    mM ::= m | M
    nN ::= n | N
    oO ::= o | O
    pP ::= p | P
    qQ ::= q | Q
    rR ::= r | R
    sS ::= s | S
    tT ::= t | T
    uU ::= u | U
    vV ::= v | V
    wW ::= w | W
    xX ::= x | X
    yY ::= y | Y
    zZ ::= z | Z

--  lower ::= a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z

--  upper ::= A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z

    letter ::= aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | nN | oO | pP | qQ | rR | sS | tT | uU | vV | wW | xX | yY | zZ

    any    ::= letter | digit | special | white

    special ::= specialNoDotOrSlash | '.' | '/'

    specialNoDotOrSlash ::= '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' |
                            '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '_' | '$'

    specialNoDoubleQuote ::= '+' | '-' | '(' | ')' | '!' | '@' | '`' | '~' | '.' | '/' |
                            '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '_'

    specialNoSingleQuote ::= '+' | '-' | '(' | ')' | '!' | '@' | '`' | '~' | '.' | '/' |
                            '%' | '&' | '^' | ':' | ';' | '"' | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '_'

    specialNoRightAngle ::= '+' | '-' | '(' | ')' | '!' | '@' | '`' | '~' | '.' | '/' |
                            '%' | '&' | '^' | ':' | ';' | '"' | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | "'" | '=' | '#' | '*' | '_'

    whiteChar ::= Space | LF | CR | HT | FF

    white ::= whiteChar | white whiteChar

    notEOL ::= letter | digit | special | Space | HT | FF

    notEOLOrQuote ::= letter | digit | specialNoSingleQuote | Space | HT | FF

    notEOLOrDoubleQuote ::= letter | digit | specialNoDoubleQuote | Space | HT | FF

    notEOLOrRightAngle ::= letter | digit | specialNoRightAngle | Space | HT | FF

    notEOLs ::= notEOL | notEOLs notEOL

    notEOLOrQuotes ::= notEOLOrQuote | notEOLOrQuotes notEOLOrQuote

    notEOLOrDoubleQuotes ::= notEOLOrDoubleQuote | notEOLOrDoubleQuotes notEOLOrDoubleQuote

    notEOLOrRightAngles ::= notEOLOrRightAngle | notEOLOrRightAngles notEOLOrRightAngle

    slc   ::= '-' '-'
            | slc notEOL

    Token ::= white /.$BeginJava skipToken(); $EndJava./
            | slc   /.$BeginJava makeComment($_SINGLE_LINE_COMMENT); $EndJava./

    Equivalence ::= ':' ':' '='
    Arrow       ::= '-' '>'

    Dots  ::= '.' | Dots '.'

    letterWhiteDigit ::= letter | white | digit

    InsideBlock ::= $empty
    			  | InsideBlock letterWhiteDigit
                  | InsideBlock Dots specialNoDotOrSlash
                  | InsideBlock '/'
                  | InsideBlock specialNoDotOrSlash

    Block ::= '/' '.' InsideBlock Dots '/'

    Symbol ::= delimitedSymbol | normalSymbol | number

    delimitedSymbol ::= "'" notEOLOrQuotes "'"
                      | '"' notEOLOrDoubleQuotes '"'
                      | '<' notEOLOrRightAngles '>'

    normalSymbol ::= letter | normalSymbol symbolChar

    symbolChar ::= letter | digit | '_' | '.' | '-' | '/'

    number ::= digit | number digit

    Token ::= '%' oO pP tT iI oO nN sS           /.$BeginJava makeToken($_OPTIONS_KEY);$EndJava./

    Token ::= '$' aA lL iI aA sS                 /.$BeginJava makeToken($_ALIAS_KEY);$EndJava./
    Token ::= '$' dD eE fF iI nN eE              /.$BeginJava makeToken($_DEFINE_KEY);$EndJava./
    Token ::= '$' dD rR oO pP sS yY mM bB oO lL sS /.$BeginJava makeToken($_DROPSYMBOLS_KEY);$EndJava./
    Token ::= '$' dD rR oO pP rR uU lL eE sS     /.$BeginJava makeToken($_DROPRULES_KEY);$EndJava./
    Token ::= '$' eE mM pP tT yY                 /.$BeginJava makeToken($_EMPTY_KEY);$EndJava./
    Token ::= '$' eE nN dD                       /.$BeginJava makeToken($_END_KEY);$EndJava./
    Token ::= '$' eE oO fF                       /.$BeginJava makeToken($_EOF_KEY);$EndJava./
    Token ::= '$' eE oO lL                       /.$BeginJava makeToken($_EOL_KEY);$EndJava./
    Token ::= '$' eE rR rR oO rR                 /.$BeginJava makeToken($_ERROR_KEY);$EndJava./
    Token ::= '$' eE xX pP oO rR tT              /.$BeginJava makeToken($_EXPORT_KEY);$EndJava./
    Token ::= '$' gG lL oO bB aA lL sS           /.$BeginJava makeToken($_GLOBALS_KEY);$EndJava./
    Token ::= '$' hH eE aA dD eE rR sS           /.$BeginJava makeToken($_HEADERS_KEY);$EndJava./
    Token ::= '$' iI dD eE nN tT iI fF iI eE rR  /.$BeginJava makeToken($_IDENTIFIER_KEY);$EndJava./
    Token ::= '$' iI mM pP oO rR tT              /.$BeginJava makeToken($_IMPORT_KEY);$EndJava./
    Token ::= '$' iI nN cC lL uU dD eE           /.$BeginJava makeToken($_INCLUDE_KEY);$EndJava./
    Token ::= '$' kK eE yY wW oO rR dD sS        /.$BeginJava makeToken($_KEYWORDS_KEY);$EndJava./
    Token ::= '$' nN aA mM eE sS                 /.$BeginJava makeToken($_NAMES_KEY);$EndJava./
    Token ::= '$' nN oO tT iI cC eE              /.$BeginJava makeToken($_NOTICE_KEY);$EndJava./
    Token ::= '$' rR uU lL eE sS                 /.$BeginJava makeToken($_RULES_KEY);$EndJava./
    Token ::= '$' sS tT aA rR tT                 /.$BeginJava makeToken($_START_KEY);$EndJava./
    Token ::= '$' tT eE rR mM iI nN aA lL sS     /.$BeginJava makeToken($_TERMINALS_KEY);$EndJava./
    Token ::= '$' tT rR aA iI lL eE rR sS        /.$BeginJava makeToken($_TRAILERS_KEY);$EndJava./
    Token ::= '$' tT yY pP eE sS                 /.$BeginJava makeToken($_TYPES_KEY);$EndJava./
    Token ::= '$' tT iI tT lL eE                 /.$BeginJava makeToken($_TITLE_KEY);$EndJava./

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
    Token ::= '('               /.$BeginJava makeToken($_LEFT_PAREN); $EndJava./
    Token ::= ')'               /.$BeginJava makeToken($_RIGHT_PAREN); $EndJava./
$End
