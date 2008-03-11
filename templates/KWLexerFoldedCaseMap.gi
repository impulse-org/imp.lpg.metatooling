%Terminals
    DollarSign ::= '$'
    Percent ::= '%'
    _
    
    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z
%End

%Headers
    /.
        //
        // Each upper case letter is mapped into its corresponding
        // lower case counterpart. For example, if an 'A' appears
        // in the input, it is mapped into $prefix$a$suffix$ just
        // like 'a'.
        //
        final static int tokenKind[] = new int[128];
        static
        {
            tokenKind['$'] = $prefix$DollarSign$suffix$;
            tokenKind['%'] = $prefix$Percent$suffix$;
            tokenKind['_'] = $prefix$_$suffix$;

            tokenKind['a'] = $prefix$a$suffix$;
            tokenKind['b'] = $prefix$b$suffix$;
            tokenKind['c'] = $prefix$c$suffix$;
            tokenKind['d'] = $prefix$d$suffix$;
            tokenKind['e'] = $prefix$e$suffix$;
            tokenKind['f'] = $prefix$f$suffix$;
            tokenKind['g'] = $prefix$g$suffix$;
            tokenKind['h'] = $prefix$h$suffix$;
            tokenKind['i'] = $prefix$i$suffix$;
            tokenKind['j'] = $prefix$j$suffix$;
            tokenKind['k'] = $prefix$k$suffix$;
            tokenKind['l'] = $prefix$l$suffix$;
            tokenKind['m'] = $prefix$m$suffix$;
            tokenKind['n'] = $prefix$n$suffix$;
            tokenKind['o'] = $prefix$o$suffix$;
            tokenKind['p'] = $prefix$p$suffix$;
            tokenKind['q'] = $prefix$q$suffix$;
            tokenKind['r'] = $prefix$r$suffix$;
            tokenKind['s'] = $prefix$s$suffix$;
            tokenKind['t'] = $prefix$t$suffix$;
            tokenKind['u'] = $prefix$u$suffix$;
            tokenKind['v'] = $prefix$v$suffix$;
            tokenKind['w'] = $prefix$w$suffix$;
            tokenKind['x'] = $prefix$x$suffix$;
            tokenKind['y'] = $prefix$y$suffix$;
            tokenKind['z'] = $prefix$z$suffix$;

            tokenKind['A'] = $prefix$a$suffix$;
            tokenKind['B'] = $prefix$b$suffix$;
            tokenKind['C'] = $prefix$c$suffix$;
            tokenKind['D'] = $prefix$d$suffix$;
            tokenKind['E'] = $prefix$e$suffix$;
            tokenKind['F'] = $prefix$f$suffix$;
            tokenKind['G'] = $prefix$g$suffix$;
            tokenKind['H'] = $prefix$h$suffix$;
            tokenKind['I'] = $prefix$i$suffix$;
            tokenKind['J'] = $prefix$j$suffix$;
            tokenKind['K'] = $prefix$k$suffix$;
            tokenKind['L'] = $prefix$l$suffix$;
            tokenKind['M'] = $prefix$m$suffix$;
            tokenKind['N'] = $prefix$n$suffix$;
            tokenKind['O'] = $prefix$o$suffix$;
            tokenKind['P'] = $prefix$p$suffix$;
            tokenKind['Q'] = $prefix$q$suffix$;
            tokenKind['R'] = $prefix$r$suffix$;
            tokenKind['S'] = $prefix$s$suffix$;
            tokenKind['T'] = $prefix$t$suffix$;
            tokenKind['U'] = $prefix$u$suffix$;
            tokenKind['V'] = $prefix$v$suffix$;
            tokenKind['W'] = $prefix$w$suffix$;
            tokenKind['X'] = $prefix$x$suffix$;
            tokenKind['Y'] = $prefix$y$suffix$;
            tokenKind['Z'] = $prefix$z$suffix$;
        };
    
        final int getKind(char c)
        {
            return (c < 128 ? tokenKind[c] : 0);
        }
    ./
%End
