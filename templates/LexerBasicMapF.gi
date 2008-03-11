%Headers
    --
    -- Additional methods for the action class not provided in the template
    --
    /.
        //
        // The Lexer contains an array of characters as the input stream to be parsed.
        // There are methods to retrieve and classify characters.
        // The lexparser "token" is implemented simply as the index of the next character in the array.
        // The Lexer extends the abstract class LpgLexStream with an implementation of the abstract
        // method getKind.  The template defines the Lexer class and the lexer() method.
        // A driver creates the action class, "Lexer", passing an Option object to the constructor.
        //
        $kw_lexer_class kwLexer;
        boolean printTokens;
        private final static int ECLIPSE_TAB_VALUE = 4;

        public int [] getKeywordKinds() { return kwLexer.getKeywordKinds(); }

        public $action_type(String filename) throws java.io.IOException
        {
            this(filename, ECLIPSE_TAB_VALUE);
            this.kwLexer = new $kw_lexer_class(lexStream.getInputChars(), $_IDENTIFIER);
        }

        /**
         * @deprecated function replaced by {@link #reset(char [] content, String filename)}
         */
        public void initialize(char [] content, String filename)
        {
            reset(content, filename);
        }
        
        final void makeToken(int left_token, int right_token, int kind)
        {
            lexStream.makeToken(left_token, right_token, kind);
        }
        
        final void makeToken(int kind)
        {
            int startOffset = getLeftSpan(),
                endOffset = getRightSpan();
            lexStream.makeToken(startOffset, endOffset, kind);
            if (printTokens) printValue(startOffset, endOffset);
        }

        final void makeComment(int kind)
        {
            int startOffset = getLeftSpan(),
                endOffset = getRightSpan();
            lexStream.getPrsStream().makeAdjunct(startOffset, endOffset, kind);
        }

        final void skipToken()
        {
            if (printTokens) printValue(getLeftSpan(), getRightSpan());
        }
        
        final void checkForKeyWord()
        {
            int startOffset = getLeftSpan(),
                endOffset = getRightSpan(),
                kwKind = kwLexer.lexer(startOffset, endOffset);
            lexStream.makeToken(startOffset, endOffset, kwKind);
            if (printTokens) printValue(startOffset, endOffset);
        }
        
        // This flavor of checkForKeyWord is necessary when the default kind
        // (which is returned when the keyword filter doesn't match) is something
        // other than _IDENTIFIER.
        final void checkForKeyWord(int defaultKind)
        {
            int startOffset = getLeftSpan(),
                endOffset = getRightSpan(),
                kwKind = kwLexer.lexer(startOffset, endOffset);
            if (kwKind == $_IDENTIFIER)
                kwKind = defaultKind;
            lexStream.makeToken(startOffset, endOffset, kwKind);
            if (printTokens) printValue(startOffset, endOffset);
        }
        
        final void printValue(int startOffset, int endOffset)
        {
            String s = new String(lexStream.getInputChars(), startOffset, endOffset - startOffset + 1);
            System.out.print(s);
        }

        //
        //
        //
        static class $super_stream_class extends LpgLexStream
        {
        public final static int tokenKind[] =
        {
            $prefix$CtlCharNotWS$suffix$,    // 000    0x00
            $prefix$CtlCharNotWS$suffix$,    // 001    0x01
            $prefix$CtlCharNotWS$suffix$,    // 002    0x02
            $prefix$CtlCharNotWS$suffix$,    // 003    0x03
            $prefix$CtlCharNotWS$suffix$,    // 004    0x04
            $prefix$CtlCharNotWS$suffix$,    // 005    0x05
            $prefix$CtlCharNotWS$suffix$,    // 006    0x06
            $prefix$CtlCharNotWS$suffix$,    // 007    0x07
            $prefix$CtlCharNotWS$suffix$,    // 008    0x08
            $prefix$HT$suffix$,              // 009    0x09
            $prefix$LF$suffix$,              // 010    0x0A
            $prefix$CtlCharNotWS$suffix$,    // 011    0x0B
            $prefix$FF$suffix$,              // 012    0x0C
            $prefix$CR$suffix$,              // 013    0x0D
            $prefix$CtlCharNotWS$suffix$,    // 014    0x0E
            $prefix$CtlCharNotWS$suffix$,    // 015    0x0F
            $prefix$CtlCharNotWS$suffix$,    // 016    0x10
            $prefix$CtlCharNotWS$suffix$,    // 017    0x11
            $prefix$CtlCharNotWS$suffix$,    // 018    0x12
            $prefix$CtlCharNotWS$suffix$,    // 019    0x13
            $prefix$CtlCharNotWS$suffix$,    // 020    0x14
            $prefix$CtlCharNotWS$suffix$,    // 021    0x15
            $prefix$CtlCharNotWS$suffix$,    // 022    0x16
            $prefix$CtlCharNotWS$suffix$,    // 023    0x17
            $prefix$CtlCharNotWS$suffix$,    // 024    0x18
            $prefix$CtlCharNotWS$suffix$,    // 025    0x19
            $prefix$CtlCharNotWS$suffix$,    // 026    0x1A
            $prefix$CtlCharNotWS$suffix$,    // 027    0x1B
            $prefix$CtlCharNotWS$suffix$,    // 028    0x1C
            $prefix$CtlCharNotWS$suffix$,    // 029    0x1D
            $prefix$CtlCharNotWS$suffix$,    // 030    0x1E
            $prefix$CtlCharNotWS$suffix$,    // 031    0x1F
            $prefix$Space$suffix$,           // 032    0x20
            $prefix$Exclamation$suffix$,     // 033    0x21
            $prefix$DoubleQuote$suffix$,     // 034    0x22
            $prefix$Sharp$suffix$,           // 035    0x23
            $prefix$DollarSign$suffix$,      // 036    0x24
            $prefix$Percent$suffix$,         // 037    0x25
            $prefix$Ampersand$suffix$,       // 038    0x26
            $prefix$SingleQuote$suffix$,     // 039    0x27
            $prefix$LeftParen$suffix$,       // 040    0x28
            $prefix$RightParen$suffix$,      // 041    0x29
            $prefix$Star$suffix$,            // 042    0x2A
            $prefix$Plus$suffix$,            // 043    0x2B
            $prefix$Comma$suffix$,           // 044    0x2C
            $prefix$Minus$suffix$,           // 045    0x2D
            $prefix$Dot$suffix$,             // 046    0x2E
            $prefix$Slash$suffix$,           // 047    0x2F
            $prefix$0$suffix$,               // 048    0x30
            $prefix$1$suffix$,               // 049    0x31
            $prefix$2$suffix$,               // 050    0x32
            $prefix$3$suffix$,               // 051    0x33
            $prefix$4$suffix$,               // 052    0x34
            $prefix$5$suffix$,               // 053    0x35
            $prefix$6$suffix$,               // 054    0x36
            $prefix$7$suffix$,               // 055    0x37
            $prefix$8$suffix$,               // 056    0x38
            $prefix$9$suffix$,               // 057    0x39
            $prefix$Colon$suffix$,           // 058    0x3A
            $prefix$SemiColon$suffix$,       // 059    0x3B
            $prefix$LessThan$suffix$,        // 060    0x3C
            $prefix$Equal$suffix$,           // 061    0x3D
            $prefix$GreaterThan$suffix$,     // 062    0x3E
            $prefix$QuestionMark$suffix$,    // 063    0x3F
            $prefix$AtSign$suffix$,          // 064    0x40
            $prefix$A$suffix$,               // 065    0x41
            $prefix$B$suffix$,               // 066    0x42
            $prefix$C$suffix$,               // 067    0x43
            $prefix$D$suffix$,               // 068    0x44
            $prefix$E$suffix$,               // 069    0x45
            $prefix$F$suffix$,               // 070    0x46
            $prefix$G$suffix$,               // 071    0x47
            $prefix$H$suffix$,               // 072    0x48
            $prefix$I$suffix$,               // 073    0x49
            $prefix$J$suffix$,               // 074    0x4A
            $prefix$K$suffix$,               // 075    0x4B
            $prefix$L$suffix$,               // 076    0x4C
            $prefix$M$suffix$,               // 077    0x4D
            $prefix$N$suffix$,               // 078    0x4E
            $prefix$O$suffix$,               // 079    0x4F
            $prefix$P$suffix$,               // 080    0x50
            $prefix$Q$suffix$,               // 081    0x51
            $prefix$R$suffix$,               // 082    0x52
            $prefix$S$suffix$,               // 083    0x53
            $prefix$T$suffix$,               // 084    0x54
            $prefix$U$suffix$,               // 085    0x55
            $prefix$V$suffix$,               // 086    0x56
            $prefix$W$suffix$,               // 087    0x57
            $prefix$X$suffix$,               // 088    0x58
            $prefix$Y$suffix$,               // 089    0x59
            $prefix$Z$suffix$,               // 090    0x5A
            $prefix$LeftBracket$suffix$,     // 091    0x5B
            $prefix$BackSlash$suffix$,       // 092    0x5C
            $prefix$RightBracket$suffix$,    // 093    0x5D
            $prefix$Caret$suffix$,           // 094    0x5E
            $prefix$_$suffix$,               // 095    0x5F
            $prefix$BackQuote$suffix$,       // 096    0x60
            $prefix$a$suffix$,               // 097    0x61
            $prefix$b$suffix$,               // 098    0x62
            $prefix$c$suffix$,               // 099    0x63
            $prefix$d$suffix$,               // 100    0x64
            $prefix$e$suffix$,               // 101    0x65
            $prefix$f$suffix$,               // 102    0x66
            $prefix$g$suffix$,               // 103    0x67
            $prefix$h$suffix$,               // 104    0x68
            $prefix$i$suffix$,               // 105    0x69
            $prefix$j$suffix$,               // 106    0x6A
            $prefix$k$suffix$,               // 107    0x6B
            $prefix$l$suffix$,               // 108    0x6C
            $prefix$m$suffix$,               // 109    0x6D
            $prefix$n$suffix$,               // 110    0x6E
            $prefix$o$suffix$,               // 111    0x6F
            $prefix$p$suffix$,               // 112    0x70
            $prefix$q$suffix$,               // 113    0x71
            $prefix$r$suffix$,               // 114    0x72
            $prefix$s$suffix$,               // 115    0x73
            $prefix$t$suffix$,               // 116    0x74
            $prefix$u$suffix$,               // 117    0x75
            $prefix$v$suffix$,               // 118    0x76
            $prefix$w$suffix$,               // 119    0x77
            $prefix$x$suffix$,               // 120    0x78
            $prefix$y$suffix$,               // 121    0x79
            $prefix$z$suffix$,               // 122    0x7A
            $prefix$LeftBrace$suffix$,       // 123    0x7B
            $prefix$VerticalBar$suffix$,     // 124    0x7C
            $prefix$RightBrace$suffix$,      // 125    0x7D
            $prefix$Tilde$suffix$,           // 126    0x7E

            $prefix$AfterASCII$suffix$,      // for all chars in range 128..65534
            $prefix$EOF$suffix$              // for '\uffff' or 65535 
        };
                
        public final int getKind(int i)  // Classify character at ith location
        {
            int c = (i >= getStreamLength() ? '\uffff' : getCharValue(i));
            return (c < 128 // ASCII Character
                      ? tokenKind[c]
                      : c == '\uffff'
                           ? $prefix$EOF$suffix$
                           : $prefix$AfterASCII$suffix$);
        }

        public String[] orderedExportedSymbols() { return $exp_type.orderedTerminalSymbols; }

        public $super_stream_class(String filename, int tab) throws java.io.IOException
        {
            super(filename, tab);
        }

        public $super_stream_class(char[] input_chars, String filename, int tab)
        {
            super(input_chars, filename, tab);
        }
    
        public $super_stream_class(char[] input_chars, String filename)
        {
            super(input_chars, filename, 1);
        }
        }
    ./
%End
