--
-- The Java Keyword Lexer
--
%Options fp=JikesPGKWLexer
%options single-productions
%options package=org.jikespg.uide.parser
%options template=uide/KeywordTemplate.gi

$Include
    uide\KWLexerMap.gi
$End

$Export

   ALIAS_KEY
   AST_KEY
   DEFINE_KEY
   DROPRULES_KEY
   DROPSYMBOLS_KEY
   EMPTY_KEY
   END_KEY
   ERROR_KEY
   EOL_KEY
   EOF_KEY 
   EXPORT_KEY
   GLOBALS_KEY
   HEADERS_KEY
   IDENTIFIER_KEY
   IMPORT_KEY
   INCLUDE_KEY
   KEYWORDS_KEY
   NAMES_KEY
   NOTICE_KEY
   TERMINALS_KEY
   RULES_KEY
   START_KEY
   TRAILERS_KEY
   TYPES_KEY

$End

$Start
    Keyword
$End

$Rules
    Keyword ::= '$' aA lL iI aA sS 
        /.$BeginAction
            $setResult($_ALIAS_KEY);
          $EndAction
        ./
    Keyword ::= '$' aA sS tT 
        /.$BeginAction
            $setResult($_AST_KEY);
          $EndAction
        ./
    Keyword ::= '$' dD eE fF iI nN eE 
        /.$BeginAction
            $setResult($_DEFINE_KEY);
          $EndAction
        ./
    Keyword ::= '$' dD rR oO pP rR uU lL eE sS 
        /.$BeginAction
            $setResult($_DROPRULES_KEY);
          $EndAction
        ./
    Keyword ::= '$' dD rR oO pP sS yY mM bB oO lL sS 
        /.$BeginAction
            $setResult($_DROPSYMBOLS_KEY);
          $EndAction
        ./
    Keyword ::= '$' eE mM pP tT yY 
        /.$BeginAction
            $setResult($_EMPTY_KEY);
          $EndAction
        ./
    Keyword ::= '$' eE nN dD 
        /.$BeginAction
            $setResult($_END_KEY);
          $EndAction
        ./
    Keyword ::= '$' eE rR rR oO rR
        /.$BeginAction
            $setResult($_ERROR_KEY);
          $EndAction
        ./
    Keyword ::= '$' eE oO lL
        /.$BeginAction
            $setResult($_EOL_KEY);
          $EndAction
        ./
    Keyword ::= '$' eE oO fF
        /.$BeginAction
            $setResult($_EOF_KEY);
          $EndAction
        ./
    Keyword ::= '$' eE xX pP oO rR tT 
        /.$BeginAction
            $setResult($_EXPORT_KEY);
          $EndAction
        ./
    Keyword ::= '$' gG lL oO bB aA lL sS 
        /.$BeginAction
            $setResult($_GLOBALS_KEY);
          $EndAction
        ./
    Keyword ::= '$' hH eE aA dD eE rR sS
        /.$BeginAction
            $setResult($_HEADERS_KEY);
          $EndAction
        ./
    Keyword ::= '$' iI dD eE nN tT iI fF iI eE rR 
        /.$BeginAction
            $setResult($_IDENTIFIER_KEY);
          $EndAction
        ./
    Keyword ::= '$' iI mM pP oO rR tT 
        /.$BeginAction
            $setResult($_IMPORT_KEY);
          $EndAction
        ./
    Keyword ::= '$' iI nN cC lL uU dD eE 
        /.$BeginAction
            $setResult($_INCLUDE_KEY);
          $EndAction
        ./
    Keyword ::= '$' kK eE yY wW oO rR dD sS 
        /.$BeginAction
            $setResult($_KEYWORDS_KEY);
          $EndAction
        ./
    Keyword ::= '$' nN aA mM eE sS 
        /.$BeginAction
            $setResult($_NAMES_KEY);
          $EndAction
        ./
    Keyword ::= '$' nN oO tT iI cC eE 
        /.$BeginAction
            $setResult($_NOTICE_KEY);
          $EndAction
        ./
    Keyword ::= '$' tT eE rR mM iI nN aA lL sS 
        /.$BeginAction
            $setResult($_TERMINALS_KEY);
          $EndAction
        ./
    Keyword ::= '$' rR uU lL eE sS 
        /.$BeginAction
            $setResult($_RULES_KEY);
          $EndAction
        ./
    Keyword ::= '$' sS tT aA rR tT 
        /.$BeginAction
            $setResult($_START_KEY);
          $EndAction
        ./
    Keyword ::= '$' tT rR aA iI lL eE rR sS 
        /.$BeginAction
            $setResult($_TRAILERS_KEY);
          $EndAction
        ./
    Keyword ::= '$' tT yY pP eE sS 
        /.$BeginAction
            $setResult($_TYPES_KEY);
          $EndAction
        ./

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
$End
