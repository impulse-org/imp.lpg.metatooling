--
-- The Java KeyWord Lexer
--
%options package=$PACKAGE_NAME$
%options template=$TEMPLATE$

$Include
    UIDE/KWLexerLowerCaseMap.gi
$End

$Export

    -- List all the keywords the kwlexer will export to the lexer and parser
    boolean
    double
    else
    false
    if
    int
    return
    true
    void
    while
$End

$Terminals
    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z
$End

$Start
    Keyword
$End

$Rules

    -- The Goal for the parser is a single Keyword

    Keyword ::= b o o l e a n
        /.$BeginAction
            $setResult($_boolean);
          $EndAction
        ./

    Keyword ::= d o u b l e
        /.$BeginAction
            $setResult($_double);
          $EndAction
        ./
        
    Keyword ::= e l s e
        /.$BeginAction
            $setResult($_else);
          $EndAction
        ./

    Keyword ::= f a l s e
        /.$BeginAction
            $setResult($_false);
          $EndAction
        ./

    Keyword ::= i f
        /.$BeginAction
            $setResult($_if);
          $EndAction
        ./

    Keyword ::= i n t
        /.$BeginAction
            $setResult($_int);
          $EndAction
        ./

    Keyword ::= v o i d
        /.$BeginAction
            $setResult($_void);
          $EndAction
        ./

    Keyword ::= r e t u r n
        /.$BeginAction
            $setResult($_return);
          $EndAction
        ./

    Keyword ::= t r u e
        /.$BeginAction
            $setResult($_true);
          $EndAction
        ./

    Keyword ::= w h i l e
        /.$BeginAction
            $setResult($_while);
          $EndAction
        ./
$End
