%options fp=JikesPGLexer,trace=full,la=15
%options single-productions
%options package=org.jikespg.uide.parser
%options template=uide/LexerTemplate.gi
%options filter=JikesPGKWLexer.gi

$Globals
    /.import java.util.*;
    import org.eclipse.uide.parser.ILexer;./
$End

$Define
    $additional_interfaces /., ILexer./
    $kw_lexer_class /.$JikesPGKWLexer./
    $_IDENTIFIER /.$_MACRO_NAME./
$End

$Include
    uide/LexerBasicMap.gi
$End

$Export
    MACRO_NAME
    SYMBOL
    BLOCK
    EQUIVALENCE
    PRIORITY_EQUIVALENCE
    ARROW
    PRIORITY_ARROW
    OR_MARKER
    EQUAL
    OPTIONS_KEY
    COMMA
    SINGLE_LINE_COMMENT
    LEFT_PAREN
    RIGHT_PAREN
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

--$Headers
--    /.
--		public int[] keywords = {
--			$_DROPSYMBOLS_KEY, $_DROPRULES_KEY, $_NOTICE_KEY, $_DEFINE_KEY,
--			$_TERMINALS_KEY, $_KEYWORDS_KEY, $_IDENTIFIER_KEY, $_ALIAS_KEY,
--			$_EMPTY_KEY, $_START_KEY, $_TYPES_KEY, $_RULES_KEY, $_NAMES_KEY, $_END_KEY,
--			$_HEADERS_KEY, $_TRAILERS_KEY, $_EXPORT_KEY, $_IMPORT_KEY, $_INCLUDE_KEY,
--			$_OPTIONS_KEY, $_TITLE_KEY, $_GLOBALS_KEY
--		};
--		public boolean isKeyword(IToken token) { return true; }
--
--		public char[][] getKeywords() { return null; }
--
--		public boolean isKeywordStart(char c) { return false; }
--     ./

$Start
    Token
$End

$Rules
    Token ::= white /.$BeginJava skipToken(); $EndJava./
    Token ::= slc   /.$BeginJava makeComment($_SINGLE_LINE_COMMENT); $EndJava./

    Token ::= options Eol
    Token ::= options optionList Eol
    Token ::= options optionWhiteChar slc
    Token ::= options optionList optionWhiteChar slc
    Token ::= MacroSymbol       /.$BeginJava checkForKeyWord();$EndJava./
    Token ::= Symbol            /.$BeginJava makeToken($_SYMBOL);$EndJava./
    Token ::= Block             /.$BeginJava makeToken($_BLOCK);$EndJava./
    Token ::= Equivalence       /.$BeginJava makeToken($_EQUIVALENCE);$EndJava./
    Token ::= Equivalence ?     /.$BeginJava makeToken($_PRIORITY_EQUIVALENCE);$EndJava./
    Token ::= Arrow             /.$BeginJava makeToken($_ARROW);$EndJava./
    Token ::= Arrow ?           /.$BeginJava makeToken($_PRIORITY_ARROW);$EndJava./
    Token ::= '|'               /.$BeginJava makeToken($_OR_MARKER);$EndJava./

    digit -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

    aA -> a | A
    bB -> b | B
    cC -> c | C
    dD -> d | D
    eE -> e | E
    fF -> f | F
    gG -> g | G
    hH -> h | H
    iI -> i | I
    jJ -> j | J
    kK -> k | K
    lL -> l | L
    mM -> m | M
    nN -> n | N
    oO -> o | O
    pP -> p | P
    qQ -> q | Q
    rR -> r | R
    sS -> s | S
    tT -> t | T
    uU -> u | U
    vV -> v | V
    wW -> w | W
    xX -> x | X
    yY -> y | Y
    zZ -> z | Z

--  lower ::= a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z
--  upper ::= A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z

    letter -> AfterASCII | '_' | aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | nN | oO | pP | qQ | rR | sS | tT | uU | vV | wW | xX | yY | zZ

    anyNonWhiteChar -> letter | digit | special

    special -> specialNoDotOrSlash | '.' | '/'

    --    special -> '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '.' | '/' |
    --               '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
    --               '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '$'

    specialNoDotDollar -> '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '/' |
                          '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                          '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*'

    specialNoColonDollar -> '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '.' | '/' |
                            '%' | '&' | '^' | ';' | "'" | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*'

    specialNoEqualDollar -> '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '.' | '/' |
                            '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '#' | '*'

    specialNoQuestionDollar -> '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '.' | '/' |
                               '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                               '[' | ']' | ',' | '<' | '>' | '=' | '#' | '*'

    specialNoMinusRightAngleDollar -> '+' | '(' | ')' | '!' | '@' | '`' | '~' | '.' | '/' |
                                      '%' | '&' | '^' | ':' | ';' | '"' | '\' | '|' | '{' | '}' |
                                      '[' | ']' | '?' | ',' | '<' | "'" | '=' | '#' | '*' | '$'

    specialNoDollar -> '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '.' | '/' |
                       '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                       '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*'

    specialNoDotOrSlash -> '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' |
                           '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                           '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '$'

    specialNoDoubleQuote -> '+' | '-' | '(' | ')' | '!' | '@' | '`' | '~' | '.' | '/' |
                            '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '$'

    specialNoSingleQuote -> '+' | '-' | '(' | ')' | '!' | '@' | '`' | '~' | '.' | '/' |
                            '%' | '&' | '^' | ':' | ';' | '"' | '\' | '|' | '{' | '}' |
                            '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '$'

    specialNoRightAngle -> '+' | '-' | '(' | ')' | '!' | '@' | '`' | '~' | '.' | '/' |
                           '%' | '&' | '^' | ':' | ';' | '"' | '\' | '|' | '{' | '}' |
                           '[' | ']' | '?' | ',' | '<' | "'" | '=' | '#' | '*' | '$'

    specialNoMinusSingleQuoteDoublequoteLeftAngleCommaLparenRparen -> '+' | '!' | '@' | '`' | '~' | '.' | '/' |
                                                                      '%' | '&' | '^' | ':' | ';' | '\' | '|' | '{' | '}' |
                                                                      '[' | ']' | '?' | '>' | '=' | '#' | '*' | '$'

    specialNoColonMinusSingleQuoteDoublequoteLeftAngleOrSlashDollarPercent -> '+' | '(' | ')' | '!' | '@' | '`' | '~' | '.' |
                                                                              '&' | '^' | ';' | '\' | '{' | '}' |
                                                                              '[' | ']' | '?' | ',' | '>' | '=' | '#' | '*'

    Eol -> LF | CR

    whiteChar -> Space | Eol | HT | FF

    white ::= whiteChar
            | white whiteChar

    notEOL -> letter | digit | special | Space | HT | FF

    notEOLOrQuote -> letter | digit | specialNoSingleQuote | Space | HT | FF

    notEOLOrDoubleQuote -> letter | digit | specialNoDoubleQuote | Space | HT | FF

    notEOLOrRightAngle -> letter | digit | specialNoRightAngle | Space | HT | FF

    notEOLOrQuotes ::= notEOLOrQuote
                     | notEOLOrQuotes notEOLOrQuote

    notEOLOrDoubleQuotes ::= notEOLOrDoubleQuote
                           | notEOLOrDoubleQuotes notEOLOrDoubleQuote

    notEOLOrRightAngles ::= notEOLOrRightAngle
                          | notEOLOrRightAngles notEOLOrRightAngle

    slc ::= '-' '-'
          | slc notEOL

    Equivalence ::= ':' ':' '='
    Arrow       ::= '-' '>'

    Dots ::= '.'
           | Dots '.'

    InsideBlockChar -> letter | whiteChar | digit | specialNoDotOrSlash

    InsideBlock ::= $empty
                  | InsideBlock InsideBlockChar
                  | InsideBlock Dots InsideBlockChar
                  | InsideBlock '/'

    Block ::= '/' '.' InsideBlock Dots '/'

    Symbol -> delimitedSymbol
            | specialSymbol
            | normalSymbol

    delimitedSymbol ::= "'" notEOLOrQuotes "'"
                      | '"' notEOLOrDoubleQuotes '"'
                      | '<' letter notEOLOrRightAngles '>'

    MacroSymbol ::= '$'
                  | MacroSymbol letter
                  | MacroSymbol digit

    anyNonWhiteNoColonMinusSingleQuoteDoublequoteLeftAngleOrSlashDollarPercent -> letter | digit | specialNoColonMinusSingleQuoteDoublequoteLeftAngleOrSlashDollarPercent
    normalSymbol ::= anyNonWhiteNoColonMinusSingleQuoteDoublequoteLeftAngleOrSlashDollarPercent
                   | normalSymbol anyNonWhiteChar

    --
    -- Below, we write special rules to recognize initial 
    -- prefixes of these special metasymbols as valid symbols.
    --
    --    BLOCK            /.  ...
    --    EQUIVALENCE      ::=[?]
    --    ARROW            ->[?]
    --    COMMENT          -- ...
    --    OR_MARKER        |
    --    OPTIONS_KEY      %options
    --    bracketed symbol < ... >
    --

    letterNoOo -> AfterASCII | '_' | aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | nN | pP | qQ | rR | sS | tT | uU | vV | wW | xX | yY | zZ
    letterNoPp -> AfterASCII | '_' | aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | nN | oO | qQ | rR | sS | tT | uU | vV | wW | xX | yY | zZ
    letterNoTt -> AfterASCII | '_' | aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | nN | oO | pP | qQ | rR | sS | uU | vV | wW | xX | yY | zZ
    letterNoIi -> AfterASCII | '_' | aA | bB | cC | dD | eE | fF | gG | hH | jJ | kK | lL | mM | nN | oO | pP | qQ | rR | sS | tT | uU | vV | wW | xX | yY | zZ
    letterNoNn -> AfterASCII | '_' | aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | oO | pP | qQ | rR | sS | tT | uU | vV | wW | xX | yY | zZ
    letterNoSs -> AfterASCII | '_' | aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | nN | oO | pP | qQ | rR | tT | uU | vV | wW | xX | yY | zZ

    anyNonWhiteNoLetterDollar -> digit | specialNoDollar
    anyNonWhiteNoDotDollar -> letter | digit | specialNoDotDollar
    anyNonWhiteNoColonDollar -> letter | digit | specialNoColonDollar
    anyNonWhiteNoEqualDollar -> letter | digit | specialNoEqualDollar
    anyNonWhiteNoQuestionDollar -> letter | digit | specialNoQuestionDollar
    anyNonWhiteNoMinusRightAngleDollar -> letter | digit | specialNoMinusRightAngleDollar
    anyNonWhiteNoDollar -> letter | digit | specialNoDollar

    anyNonWhiteNoOoDollar -> letterNoOo | digit | specialNoDollar
    anyNonWhiteNoPpDollar -> letterNoPp | digit | specialNoDollar
    anyNonWhiteNoTtDollar -> letterNoTt | digit | specialNoDollar
    anyNonWhiteNoIiDollar -> letterNoIi | digit | specialNoDollar
    anyNonWhiteNoNnDollar -> letterNoNn | digit | specialNoDollar
    anyNonWhiteNoSsDollar -> letterNoSs | digit | specialNoDollar

    specialSymbol -> simpleSpecialSymbol
                   | complexSpecialSymbol

    simpleSpecialSymbol ::= '<'
                          | '/'
                          | ':'
                          | ':' ':'
                          | '-'
                          | '%'
                          | '%' oO
                          | '%' oO pP
                          | '%' oO pP tT
                          | '%' oO pP tT iI
                          | '%' oO pP tT iI oO
                          | '%' oO pP tT iI oO nN

    complexSpecialSymbol ::= '<' anyNonWhiteNoLetterDollar
                           | '/' anyNonWhiteNoDotDollar
                           | ':' anyNonWhiteNoColonDollar
                           | ':' ':' anyNonWhiteNoEqualDollar
                           | ':' ':' '=' anyNonWhiteNoQuestionDollar
                           | ':' ':' '=' '?' anyNonWhiteNoDollar
                           | '-' anyNonWhiteNoMinusRightAngleDollar
                           | '-' '>' anyNonWhiteNoQuestionDollar
                           | '-' '>' '?' anyNonWhiteNoDollar
                           | '|' anyNonWhiteNoDollar
                           | '%' anyNonWhiteNoOoDollar
                           | '%' oO anyNonWhiteNoPpDollar
                           | '%' oO pP anyNonWhiteNoTtDollar
                           | '%' oO pP tT anyNonWhiteNoIiDollar
                           | '%' oO pP tT iI anyNonWhiteNoOoDollar
                           | '%' oO pP tT iI oO anyNonWhiteNoNnDollar
                           | '%' oO pP tT iI oO nN anyNonWhiteNoSsDollar
                           | '%' oO pP tT iI oO nN sS anyNonWhiteNoDollar
                           | complexSpecialSymbol anyNonWhiteNoDollar

    number ::= digit
             | number digit

   --
   -- The following rules are used for processing options.
   --
   options ::= '%' oO pP tT iI oO nN sS$s optionWhiteChar optionWhite
          /.$BeginJava
                      makeToken(getLeftSpan(), getRhsLastTokenIndex($s), $_OPTIONS_KEY);
            $EndJava
          ./
 
    _opt -> $empty
         | '_'
         | '-'

   no ::= nN oO

   none ::= nN oO nN eE

   anyNoMinusSingleQuoteDoublequoteLeftAngleCommaLparenRparen -> letter
                                                               | digit
                                                               | specialNoMinusSingleQuoteDoublequoteLeftAngleCommaLparenRparen

   optionSymbol ::= anyNoMinusSingleQuoteDoublequoteLeftAngleCommaLparenRparen
                  | '-' anyNoMinusSingleQuoteDoublequoteLeftAngleCommaLparenRparen
                  | optionSymbol '-' anyNoMinusSingleQuoteDoublequoteLeftAngleCommaLparenRparen
                  | optionSymbol anyNoMinusSingleQuoteDoublequoteLeftAngleCommaLparenRparen

   Value ::= delimitedSymbol
           | '-'
           | optionSymbol
           | optionSymbol '-'

   optionWhiteChar -> Space | HT | FF
   optionWhite ::= $empty
                 | optionWhite optionWhiteChar

   optionList ::= option
                | optionList ',' option

   --
   -- action_block
   -- ast_directory
   -- ast_type
   -- automatic_ast
   -- attributes
   --
   option ::= action_block optionWhite '=' optionWhite '(' optionWhite filename optionWhite ',' optionWhite block_begin optionWhite ',' optionWhite block_end optionWhite ')' optionWhite
   action_block ::= aA cC tT iI oO nN _opt bB lL oO cC kK
                  | aA bB
   filename -> Value
   block_begin -> Value
   block_end -> Value

   option ::= ast_directory optionWhite '=' optionWhite Value optionWhite
   ast_directory ::= aA sS tT _opt dD iI rR eE cC tT oO rR yY
                   | aA dD 

   option ::= ast_type optionWhite '=' optionWhite Value optionWhite
   ast_type ::= aA sS tT _opt tT yY pP eE
              | aA tT 

   option ::= attributes optionWhite
            | no attributes optionWhite
   attributes ::= aA tT tT rR iI bB uU tT eE sS

   option ::= automatic_ast optionWhite
            | no automatic_ast optionWhite
   option ::= automatic_ast optionWhite '=' optionWhite automatic_ast_value optionWhite
   automatic_ast ::= aA uU tT oO mM aA tT iI cC _opt aA sS tT
                   | aA aA
   automatic_ast_value ::= none
                         | nN eE sS tT eE dD
                         | tT oO pP _opt lL eE vV eE lL

   --
   -- backtrack
   -- byte
   --
   option ::= backtrack optionWhite
            | no backtrack optionWhite
   backtrack ::= bB aA cC kK tT rR aA cC kK

   option ::= byte optionWhite
            | no byte optionWhite
   byte ::= bB yY tT eE
   

   --
   -- conflicts
   --
   option ::= conflicts optionWhite
            | no conflicts optionWhite
   conflicts ::= cC oO nN fF lL iI cC tT sS

   --
   -- dat_directory
   -- dat_file
   -- dcl_file
   -- def_file
   -- debug
   --
   option ::= dat_directory optionWhite '=' optionWhite Value optionWhite
   dat_directory ::= dD aA tT _opt dD iI rR eE cC tT oO rR yY 
                   | dD dD

   option ::= dat_file optionWhite '=' optionWhite Value optionWhite
   dat_file ::= dD aA tT _opt fF iI lL eE

   option ::= dcl_file optionWhite '=' optionWhite Value optionWhite
   dcl_file ::= dD cC lL _opt fF iI lL eE

   option ::= def_file optionWhite '=' optionWhite Value optionWhite
   def_file ::= dD eE fF _opt fF iI lL eE

   option ::= debug optionWhite
            | no debug optionWhite
   debug ::= dD eE bB uU gG
   

   --
   -- edit
   -- error_maps
   -- escape
   -- export_terminals
   -- extends_parsetable
   --
   option ::= edit optionWhite
            | no edit optionWhite
   edit ::= eE dD iI tT

   option ::= error_maps optionWhite
            | no error_maps optionWhite
   error_maps ::= eE rR rR oO rR _opt mM aA pP sS
                | eE mM

   option ::= escape optionWhite '=' optionWhite anyNonWhiteChar optionWhite
   escape ::= eE sS cC aA pP eE

   option ::= export_terminals optionWhite '=' optionWhite filename optionWhite
   option ::= export_terminals optionWhite '=' optionWhite '(' optionWhite filename optionWhite ')' optionWhite
   option ::= export_terminals optionWhite '=' optionWhite '(' optionWhite filename optionWhite ',' optionWhite export_prefix optionWhite ')' optionWhite
   option ::= export_terminals optionWhite '=' optionWhite '(' optionWhite filename optionWhite ',' optionWhite export_prefix optionWhite ',' optionWhite export_suffix optionWhite ')' optionWhite
   export_terminals ::= eE xX pP oO rR tT _opt tT eE rR mM iI nN aA lL sS
                      | eE tT 
   export_prefix -> Value
   export_suffix -> Value

   option ::= extends_parsetable optionWhite
            | no extends_parsetable optionWhite
   option ::= extends_parsetable optionWhite '=' optionWhite Value optionWhite
   extends_parsetable ::= eE xX tT eE nN dD sS _opt pP aA rR sS eE tT aA bB lL eE
                        | eE pP

   --
   -- factory
   -- file_prefix
   -- filter
   -- first
   -- follow
   --
   option ::= factory optionWhite '=' optionWhite Value optionWhite
   factory ::= fF aA cC tT oO rR yY

   option ::= file_prefix$fp optionWhite '='$eq optionWhite Value$val optionWhite
          /.$BeginJava
                      makeToken(getRhsFirstTokenIndex($fp), getRhsLastTokenIndex($fp), $_SYMBOL);
                      makeToken(getRhsFirstTokenIndex($eq), getRhsLastTokenIndex($eq), $_EQUAL);
                      makeToken(getRhsFirstTokenIndex($val), getRhsLastTokenIndex($val), $_SYMBOL);
            $EndJava
          ./
   file_prefix ::= fF iI lL eE _opt pP rR eE fF iI xX
                 | fF pP

   option ::= filter optionWhite '=' optionWhite Value optionWhite
   filter ::= fF iI lL tT eE rR

   option ::= first optionWhite
            | no first optionWhite
   first ::= fF iI rR sS tT

   option ::= follow optionWhite
            | no follow optionWhite
   follow ::= fF oO lL lL oO wW
   

   --
   -- goto_default
   --
   option ::= goto_default optionWhite
            | no goto_default optionWhite
   goto_default ::= gG oO tT oO _opt dD eE fF aA uU lL tT
                  | gG dD

   --
   -- Headers
   --
   option ::= headers optionWhite '=' optionWhite '(' optionWhite filename optionWhite ',' optionWhite block_begin optionWhite ',' optionWhite block_end optionWhite ')' optionWhite
   headers ::= hH eE aA dD eE rR sS

   --
   -- imp_file
   -- import_terminals
   -- include_directory/include_directories
   --
   option ::= imp_file optionWhite '=' optionWhite Value optionWhite
   imp_file ::= iI mM pP _opt fF iI lL eE
              | iI fF 

   option ::= import_terminals optionWhite '=' optionWhite Value optionWhite
   import_terminals ::= iI mM pP oO rR tT _opt tT eE rR mM iI nN aA lL sS
                      | iI tT

   option ::= include_directory optionWhite '=' optionWhite Value optionWhite
   include_directory ::= iI nN cC lL uU dD eE _opt dD iI rR eE cC tT oO rR yY
                       | iI nN cC lL uU dD eE _opt dD iI rR eE cC tT oO rR iI eE sS 
                       | iI dD

   --
   -- lalr_level
   -- list
   --
   option ::= lalr_level optionWhite
            | no lalr_level optionWhite
   option ::= lalr_level optionWhite '=' optionWhite number optionWhite
   lalr_level ::= lL aA lL rR _opt lL eE vV eE lL
                | lL aA lL rR
                | lL aA
                | lL lL

   option ::= list optionWhite
            | no list optionWhite
   list ::= lL iI sS tT 

   --
   -- margin
   -- max_cases
   --
   option ::= margin optionWhite '=' optionWhite number optionWhite
   margin ::= mM aA rR gG iI nN

   option ::= max_cases optionWhite '=' optionWhite number optionWhite
   max_cases ::= mM aA xX _opt cC aA sS eE sS
               | mM cC

   --
   -- names
   -- nt_check
   --
   option ::= nN aA mM eE sS optionWhite
            | no nN aA mM eE sS optionWhite
   option ::= nN aA mM eE sS optionWhite '=' optionWhite names_value optionWhite

   names_value ::= oO pP tT iI mM iI zZ eE dD
                 | mM aA xX iI mM uU mM
                 | mM iI nN iI mM uU mM
   

   option ::= nt_check optionWhite
            | no nt_check optionWhite
   nt_check ::= nN tT _opt cC hH eE cC kK
              | nN cC

   --
   -- or_marker
   --
   option ::= or_marker optionWhite '=' optionWhite anyNonWhiteChar optionWhite
   or_marker ::= oO rR _opt mM aA rR kK eE rR
               | oO mM 

   --
   -- package
   -- parsetable_interfaces
   -- prefix
   -- priority
   -- programming_language
   -- prs_file
   --
   option ::= package optionWhite '=' optionWhite Value optionWhite
   package ::= pP aA cC kK aA gG eE

   option ::= parsetable_interfaces optionWhite '=' optionWhite Value optionWhite
   parsetable_interfaces ::= pP aA rR sS eE tT aA bB lL eE _opt iI nN tT eE rR fF aA cC eE sS
                           | pP aA rR sS eE tT aA bB lL eE
                           | pP iI

   option ::= prefix optionWhite '=' optionWhite Value optionWhite
   prefix ::= pP rR eE fF iI xX

   option ::= priority optionWhite
            | no priority optionWhite
   priority ::= pP rR iI oO rR iI tT yY

   option ::= programming_language optionWhite '=' optionWhite programming_language_value optionWhite
   programming_language ::= pP rR oO gG rR aA mM mM iI nN gG _opt lL aA nN gG uU aA gG eE
                          | pP lL
   programming_language_value ::= none
                                | xX mM lL
                                | cC
                                | cC pP pP
                                | jJ aA vV aA
                                | pP lL xX
                                | pP lL xX aA sS mM
                                | mM lL
   option ::= prs_file optionWhite '=' optionWhite Value optionWhite
   prs_file ::= pP rR sS _opt fF iI lL eE
              | pP fF
   

   --
   -- quiet
   --
   option ::= quiet optionWhite
            | no quiet optionWhite
   quiet ::= qQ uU iI eE tT

   --
   -- read_reduce
   -- remap_terminals
   --
   option ::= read_reduce optionWhite
            | no read_reduce optionWhite
   read_reduce ::= rR eE aA dD _opt rR eE dD uU cC eE
                 | rR rR

   option ::= remap_terminals optionWhite
            | no remap_terminals optionWhite
   remap_terminals ::= rR eE mM aA pP _opt tT eE rR mM iI nN aA lL sS
                     | rR tT

   --
   -- scopes
   -- serialize
   -- shift_default
   -- single_productions
   -- slr
   -- soft_keywords
   -- states
   -- suffix
   -- sym_file
   --
   option ::= scopes optionWhite
            | no scopes optionWhite
   scopes ::= sS cC oO pP eE sS

   option ::= serialize optionWhite
            | no serialize optionWhite
   serialize ::= sS eE rR iI aA lL iI zZ eE

   option ::= shift_default optionWhite
            | no shift_default optionWhite
   shift_default ::= sS hH iI fF tT _opt dD eE fF aA uU lL tT
                   | sS dD

   option ::= single_productions optionWhite
            | no single_productions optionWhite
   single_productions ::= sS iI nN gG lL eE _opt pP rR oO dD uU cC tT iI oO nN sS
                        | sS pP

   option ::= slr optionWhite
            | no slr optionWhite
   slr ::= sS lL rR

   option ::= soft_keywords$sk optionWhite /.$BeginJava  makeToken(getRhsFirstTokenIndex($sk), getRhsLastTokenIndex($sk), $_SYMBOL); $EndJava ./
            | no$no soft_keywords$sk optionWhite /.$BeginJava  makeToken(getRhsFirstTokenIndex($no), getRhsLastTokenIndex($sk), $_SYMBOL); $EndJava ./
   soft_keywords ::= sS oO fF tT _opt kK eE yY wW oO rR dD sS 
                   | sS oO fF tT
                   | sS kK

   option ::= states optionWhite
            | no states optionWhite
   states ::= sS tT aA tT eE sS

   option ::= suffix optionWhite '=' optionWhite Value optionWhite
   suffix ::= sS uU fF fF iI xX 

   option ::= sym_file optionWhite '=' optionWhite Value optionWhite
   sym_file ::= sS yY mM _opt fF iI lL eE
              | sS fF 

   --
   -- tab_file
   -- table
   -- template
   -- trace
   --
   option ::= tab_file optionWhite '=' optionWhite Value optionWhite
   tab_file ::= tT aA bB _opt fF iI lL eE
              | tT fF

   option ::= template optionWhite '=' optionWhite Value optionWhite
   template ::= tT eE mM pP lL aA tT eE
   

   option ::= trailers optionWhite '=' optionWhite '(' optionWhite filename optionWhite ',' optionWhite block_begin optionWhite ',' optionWhite block_end optionWhite ')' optionWhite
   trailers ::= tT rR aA iI lL eE rR sS

   option ::= table optionWhite
            | no table optionWhite
   option ::= table optionWhite '=' optionWhite programming_language_value optionWhite
   table ::= tT aA bB lL eE 

   option ::= trace  optionWhite
            | no trace optionWhite
   option ::= trace optionWhite '=' optionWhite trace_value optionWhite
   trace ::= T rR aA cC eE

   trace_value ::= none
                 | cC oO nN fF lL iI cC tT sS
                 | fF uU lL lL

   --
   -- variables
   -- verbose
   -- visitor
   -- visitor_type
   --
   option ::= variables optionWhite
            | no variables optionWhite
   option ::= variables optionWhite '=' optionWhite variables_value optionWhite
   variables ::= vV aA rR iI aA bB lL eE sS
   variables_value ::= none
                     | bB oO tT hH
                     | tT eE rR mM iI nN aA lL sS
                     | nN oO nN _opt tT eE rR mM iI nN aA lL sS
                     | nN tT

   option ::= verbose optionWhite
            | no verbose optionWhite
   verbose ::= vV eE rR bB oO sS eE

   option ::= visitor optionWhite
            | no visitor optionWhite
   option ::= visitor optionWhite '=' optionWhite visitor_value optionWhite
   visitor ::= vV iI sS iI tT oO rR
   visitor_value ::= none
                   | dD eE fF aA uU lL tT
                   | pP rR eE oO rR dD eE rR

   option ::= visitor_type optionWhite '=' optionWhite Value optionWhite
   visitor_type ::= vV iI sS iI tT oO rR _opt tT yY pP eE
                  | vV tT

   --
   -- warnings
   --
   option ::= warnings optionWhite
            | no warnings optionWhite
   warnings ::= wW aA rR nN iI nN gG sS

   --
   -- xref
   --
   option ::= xreference optionWhite
            | no xreference optionWhite
   xreference ::= xX rR eE fF
                | xX rR eE fF eE rR eE nN cC eE
$End
