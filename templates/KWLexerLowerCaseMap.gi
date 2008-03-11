%Terminals
    DollarSign ::= '$'
    _
    
    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z
%End

%Headers
    /.
        final static int tokenKind[] = new int[128];
        static
        {
            tokenKind['$'] = $prefix$DollarSign$suffix$;
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
        };
    
        final int getKind(char c)
        {
            return (c < 128 ? tokenKind[c] : 0);
        }
    ./
%End
