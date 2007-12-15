--
-- The Java KeyWord Lexer
--
%options package=$PACKAGE_NAME$
%options template=$TEMPLATE$.gi

%Include
    KWLexerLowerCaseMap.gi
%End

%Export

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
%End

%Terminals
    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z
%End

%Start
    Keyword
%End

%Rules

    -- The Goal for the parser is a single Keyword

    Keyword ::= b o o l e a n  /.$setResult($_boolean);./
    Keyword ::= d o u b l e    /.$setResult($_double);./
    Keyword ::= e l s e        /.$setResult($_else);./
    Keyword ::= f a l s e      /.$setResult($_false);./
    Keyword ::= i f            /.$setResult($_if);./
    Keyword ::= i n t          /.$setResult($_int);./
    Keyword ::= v o i d        /.$setResult($_void);./
    Keyword ::= r e t u r n    /.$setResult($_return);./
    Keyword ::= t r u e        /.$setResult($_true);./
    Keyword ::= w h i l e      /.$setResult($_while);./
%End
