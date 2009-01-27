%Terminals
    DollarSign ::= '$'
    _
    
    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z

    A    B    C    D    E    F    G    H    I    J    K    L    M
    N    O    P    Q    R    S    T    U    V    W    X    Y    Z
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

            tokenKind['A'] = $prefix$A$suffix$;
            tokenKind['B'] = $prefix$B$suffix$;
            tokenKind['C'] = $prefix$C$suffix$;
            tokenKind['D'] = $prefix$D$suffix$;
            tokenKind['E'] = $prefix$E$suffix$;
            tokenKind['F'] = $prefix$F$suffix$;
            tokenKind['G'] = $prefix$G$suffix$;
            tokenKind['H'] = $prefix$H$suffix$;
            tokenKind['I'] = $prefix$I$suffix$;
            tokenKind['J'] = $prefix$J$suffix$;
            tokenKind['K'] = $prefix$K$suffix$;
            tokenKind['L'] = $prefix$L$suffix$;
            tokenKind['M'] = $prefix$M$suffix$;
            tokenKind['N'] = $prefix$N$suffix$;
            tokenKind['O'] = $prefix$O$suffix$;
            tokenKind['P'] = $prefix$P$suffix$;
            tokenKind['Q'] = $prefix$Q$suffix$;
            tokenKind['R'] = $prefix$R$suffix$;
            tokenKind['S'] = $prefix$S$suffix$;
            tokenKind['T'] = $prefix$T$suffix$;
            tokenKind['U'] = $prefix$U$suffix$;
            tokenKind['V'] = $prefix$V$suffix$;
            tokenKind['W'] = $prefix$W$suffix$;
            tokenKind['X'] = $prefix$X$suffix$;
            tokenKind['Y'] = $prefix$Y$suffix$;
            tokenKind['Z'] = $prefix$Z$suffix$;
        };
    
        final int getKind(char c)
        {
            return (c < 128 ? tokenKind[c] : 0);
        }
    ./
%End
