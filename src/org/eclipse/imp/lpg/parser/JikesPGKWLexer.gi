--
-- The Java Keyword Lexer
--
%Options fp=JikesPGKWLexer
%options single-productions
%options package=org.jikespg.uide.parser
%options template=uide/KeywordTemplate.gi

$Include
    --
    -- Each upper case letter is mapped into is corresponding
    -- lower case counterpart. For example, if an 'A' appears
    -- in the input, it is mapped into Char_a just like 'a'.
    --
    uide\KWLexerFoldedCaseMap.gi
$End

$Export

   ALIAS_KEY
   AST_KEY
   DEFINE_KEY
   DISJOINTPREDECESSORSETS_KEY
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
   OPTIONS_KEY
   RECOVER_KEY
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
    Keyword ::= '$' a l i a s
        /.$BeginJava
            $setResult($_ALIAS_KEY);
          $EndJava
        ./
    Keyword ::= '$' a s t
        /.$BeginJava
            $setResult($_AST_KEY);
          $EndJava
        ./
    Keyword ::= '$' d e f i n e
        /.$BeginJava
            $setResult($_DEFINE_KEY);
          $EndJava
        ./
     Keyword ::= '$' d i s j o i n t p r e d e c e s s o r s e t s
        /.$BeginJava
            $setResult($_DISJOINTPREDECESSORSETS_KEY);
          $EndJava
        ./
    Keyword ::= '$' d r o p r u l e s
        /.$BeginJava
            $setResult($_DROPRULES_KEY);
          $EndJava
        ./
    Keyword ::= '$' d r o p s y m b o l s
        /.$BeginJava
            $setResult($_DROPSYMBOLS_KEY);
          $EndJava
        ./
    Keyword ::= '$' e m p t y
        /.$BeginJava
            $setResult($_EMPTY_KEY);
          $EndJava
        ./
    Keyword ::= '$' e n d
        /.$BeginJava
            $setResult($_END_KEY);
          $EndJava
        ./
    Keyword ::= '$' e r r o r
        /.$BeginJava
            $setResult($_ERROR_KEY);
          $EndJava
        ./
    Keyword ::= '$' e o l
        /.$BeginJava
            $setResult($_EOL_KEY);
          $EndJava
        ./
    Keyword ::= '$' e o f
        /.$BeginJava
            $setResult($_EOF_KEY);
          $EndJava
        ./
    Keyword ::= '$' e x p o r t
        /.$BeginJava
            $setResult($_EXPORT_KEY);
          $EndJava
        ./
    Keyword ::= '$' g l o b a l s
        /.$BeginJava
            $setResult($_GLOBALS_KEY);
          $EndJava
        ./
    Keyword ::= '$' h e a d e r s
        /.$BeginJava
            $setResult($_HEADERS_KEY);
          $EndJava
        ./
    Keyword ::= '$' i d e n t i f i e r
        /.$BeginJava
            $setResult($_IDENTIFIER_KEY);
          $EndJava
        ./
    Keyword ::= '$' i m p o r t
        /.$BeginJava
            $setResult($_IMPORT_KEY);
          $EndJava
        ./
    Keyword ::= '$' i n c l u d e
        /.$BeginJava
            $setResult($_INCLUDE_KEY);
          $EndJava
        ./
    Keyword ::= '$' k e y w o r d s
        /.$BeginJava
            $setResult($_KEYWORDS_KEY);
          $EndJava
        ./
    Keyword ::= '$' n a m e s
        /.$BeginJava
            $setResult($_NAMES_KEY);
          $EndJava
        ./
    Keyword ::= '$' n o t i c e
        /.$BeginJava
            $setResult($_NOTICE_KEY);
          $EndJava
        ./
    Keyword ::= '$' o p t i o n -- this rule is used here only to record this keyword!
        /.$BeginJava
            $setResult($_OPTIONS_KEY);
          $EndJava
        ./
    Keyword ::= '$' t e r m i n a l s
        /.$BeginJava
            $setResult($_TERMINALS_KEY);
          $EndJava
        ./
    Keyword ::= '$' r u l e s
        /.$BeginJava
            $setResult($_RULES_KEY);
          $EndJava
        ./
    Keyword ::= '$' s t a r t 
        /.$BeginJava
            $setResult($_START_KEY);
          $EndJava
        ./
    Keyword ::= '$' t r a i l e r s
        /.$BeginJava
            $setResult($_TRAILERS_KEY);
          $EndJava
        ./
    Keyword ::= '$' t y p e s
        /.$BeginJava
            $setResult($_TYPES_KEY);
          $EndJava
        ./
$End
