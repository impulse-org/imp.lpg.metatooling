package org.jikespg.uide.parser;

public class JikesPGParserprs implements lpg.javaruntime.ParseTable, JikesPGParsersym {

    public interface IsNullable {
        public final static byte isNullable[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,1,0,0,0,0,0,0,0,
            0,0,1,0,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,
            0,0,0,0,0,0,1,0,0,0,
            0,0,0,1,0,0,1,0,1,1,
            0,0,1,0,0,0,0,0,1,1,
            0,1,0,1,0,0,0
        };
    };
    public final static byte isNullable[] = IsNullable.isNullable;
    public final boolean isNullable(int index) { return isNullable[index] != 0; }

    public interface ProsthesesIndex {
        public final static byte prosthesesIndex[] = {0,
            7,35,42,38,36,59,52,56,57,50,
            43,31,34,37,39,40,45,48,51,53,
            61,63,2,3,4,5,6,8,9,10,
            11,12,13,14,15,16,17,18,19,20,
            21,22,23,24,25,26,27,28,29,30,
            32,33,41,44,46,47,49,54,55,58,
            60,62,64,65,1
        };
    };
    public final static byte prosthesesIndex[] = ProsthesesIndex.prosthesesIndex;
    public final int prosthesesIndex(int index) { return prosthesesIndex[index]; }

    public interface IsKeyword {
        public final static byte isKeyword[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0
        };
    };
    public final static byte isKeyword[] = IsKeyword.isKeyword;
    public final boolean isKeyword(int index) { return isKeyword[index] != 0; }

    public interface BaseCheck {
        public final static byte baseCheck[] = {0,
            2,0,2,3,3,3,3,3,3,3,
            3,3,3,3,3,3,3,3,3,3,
            3,3,3,3,3,0,2,2,1,3,
            2,0,2,4,1,3,1,2,3,3,
            3,3,3,3,1,1,1,1,1,1,
            1,1,1,1,2,2,1,1,1,1,
            1,1,1,2,1,2,1,1,2,1,
            2,2,2,1,2,1,2,4,0,1,
            1,1,2,1,3,1,2,3,1,1,
            1,1,1,1,1,2,2,0,2,3,
            4,5,1,3,1,1,1,1,2,1,
            2,1,1,2,0,1,1,1,1,1,
            1,2,2,0,2,1,1,1,1,2,
            3,1,3,0,2,2,0,2,0,1,
            0,2,-93,-16,-31,1,-18,-97,6,3,
            8,9,10,6,-101,8,9,10,-10,-102,
            6,3,8,9,10,6,-14,8,9,10,
            -27,-15,-25,7,3,-36,1,-67,3,21,
            -2,38,36,13,14,-11,20,15,16,12,
            19,-42,1,-57,1,-6,21,27,7,-60,
            1,29,44,-1,-44,1,40,3,15,16,
            39,20,13,14,-100,-3,-104,-5,4,-8,
            6,-9,6,19,8,9,23,24,43,12,
            -13,-12,11,-19,4,-20,-30,1,-17,4,
            25,22,11,48,11,-21,61,-22,3,-23,
            3,-24,3,-40,3,-26,-37,4,2,4,
            -32,1,-7,-29,49,46,45,50,35,34,
            -48,41,-4,42,60,37,-58,-76,33,32,
            31,30,22,28,-41,1,-28,17,4,-52,
            1,-53,1,4,3,18,-70,-33,26,2,
            64,-34,1,-35,-38,1,-43,4,2,47,
            -72,58,-39,1,51,-45,-71,2,62,-46,
            1,-47,1,7,54,-49,1,-50,1,53,
            -51,-54,1,56,4,-55,1,-56,1,-59,
            1,-61,-62,2,2,-63,-64,2,2,-65,
            -66,2,2,-73,-74,-75,-77,-78,-79,63,
            7,7,5,5,-84,-80,-81,-87,-82,59,
            5,5,17,5,-83,-86,-88,2,-92,5,
            2,-94,-96,2,2,18,-68,-69,-85,-89,
            -90,-91,-95,-98,-99,-103,-105,-106,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,55,0,0,0,0,52,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,57,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0
        };
    };
    public final static byte baseCheck[] = BaseCheck.baseCheck;
    public final int baseCheck(int index) { return baseCheck[index]; }
    public final static byte rhs[] = baseCheck;
    public final int rhs(int index) { return rhs[index]; };

    public interface BaseAction {
        public final static char baseAction[] = {
            23,23,25,25,26,26,26,26,26,26,
            26,26,26,26,26,26,26,26,26,26,
            26,26,26,26,26,26,24,24,49,50,
            50,12,51,51,51,52,52,27,27,13,
            13,13,13,13,13,14,5,5,5,5,
            5,5,5,28,29,29,15,16,16,53,
            31,30,32,33,33,34,34,35,36,37,
            54,54,17,17,55,55,56,56,18,57,
            57,38,39,39,19,19,40,40,20,7,
            7,7,7,7,7,41,41,42,58,58,
            59,59,59,10,10,2,2,2,2,8,
            9,9,6,6,6,60,60,4,43,61,
            61,44,44,21,62,62,3,3,45,46,
            46,22,63,63,48,48,64,47,47,1,
            1,11,11,252,213,178,24,4,252,110,
            68,103,215,394,110,252,103,215,396,4,
            252,110,257,103,215,397,110,143,103,215,
            398,157,4,126,307,316,162,21,85,257,
            121,7,320,326,37,342,39,86,54,277,
            30,82,108,17,247,6,116,122,200,307,
            122,4,194,176,43,162,16,192,316,55,
            277,205,87,38,342,112,160,252,85,116,
            206,111,155,110,83,104,215,181,216,305,
            29,234,155,304,155,95,234,134,25,227,
            65,273,129,254,237,331,4,118,4,63,
            4,62,4,60,261,61,234,184,142,317,
            53,192,23,140,149,27,261,302,287,328,
            290,235,285,73,313,109,322,263,278,292,
            332,336,338,130,340,81,18,255,70,96,
            81,11,162,10,66,64,76,274,177,3,
            297,135,259,22,260,259,20,177,142,354,
            145,276,311,259,19,31,191,143,355,123,
            259,15,259,14,125,356,259,13,259,12,
            56,262,259,9,368,142,259,8,259,7,
            259,5,177,177,358,359,177,177,366,367,
            177,177,369,375,143,143,241,280,137,137,
            389,88,85,44,43,282,137,137,284,137,
            99,42,41,71,40,137,74,286,143,170,
            39,148,177,177,155,160,77,220,272,130,
            289,75,292,294,287,296,297,300,301,437,
            437,437,437,437,437,437,437,437,437,437,
            437,437,390,437,437,437,437,391,437,437,
            437,437,437,437,437,437,437,437,437,437,
            437,437,437,437,382,437,0
        };
    };
    public final static char baseAction[] = BaseAction.baseAction;
    public final int baseAction(int index) { return baseAction[index]; }
    public final static char lhs[] = baseAction;
    public final int lhs(int index) { return lhs[index]; };

    public interface TermCheck {
        public final static byte termCheck[] = {0,
            0,1,2,0,1,2,0,7,8,9,
            10,11,12,13,14,15,16,17,18,19,
            20,21,22,23,24,25,26,27,28,29,
            30,31,32,33,34,35,0,1,0,1,
            2,35,0,7,8,9,10,11,12,13,
            14,15,16,17,18,19,20,21,22,23,
            24,25,26,27,28,29,30,31,32,33,
            34,35,0,0,0,2,3,4,5,6,
            0,9,10,11,0,1,14,7,8,17,
            18,19,20,21,22,23,24,25,26,27,
            28,29,30,31,32,33,34,0,1,2,
            36,0,1,39,7,0,9,10,11,8,
            13,0,1,2,13,0,1,2,7,0,
            9,10,11,0,1,14,0,1,2,0,
            7,12,0,1,2,9,10,11,0,13,
            14,9,10,11,0,13,0,1,2,0,
            0,0,1,2,0,9,10,11,7,0,
            14,2,3,4,5,6,0,0,1,3,
            4,5,6,0,7,37,3,4,5,6,
            0,0,1,3,4,5,6,0,7,40,
            3,4,5,6,0,0,1,3,4,5,
            6,0,0,1,3,4,5,6,0,0,
            1,3,4,5,6,0,0,1,3,4,
            5,6,0,0,0,3,4,5,6,0,
            0,8,3,4,5,6,0,1,2,15,
            16,0,1,7,0,15,16,38,0,0,
            0,0,0,0,13,7,0,8,8,8,
            8,0,1,0,1,0,1,0,1,0,
            1,0,1,0,1,0,0,2,0,1,
            36,0,1,0,1,0,0,2,12,0,
            0,0,0,0,0,0,0,0,12,0,
            0,12,12,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0
        };
    };
    public final static byte termCheck[] = TermCheck.termCheck;
    public final int termCheck(int index) { return termCheck[index]; }

    public interface TermAction {
        public final static char termAction[] = {0,
            115,395,616,437,563,564,437,395,395,395,
            395,395,395,395,395,395,395,395,395,395,
            395,395,395,395,395,395,395,395,395,395,
            395,395,395,395,395,395,115,395,437,556,
            557,436,26,395,395,395,395,395,395,395,
            395,395,395,395,395,395,395,395,395,395,
            395,395,395,395,395,395,395,395,395,395,
            395,395,1,437,437,379,542,543,544,545,
            139,248,250,147,437,264,252,577,554,171,
            256,173,196,246,236,234,239,144,172,167,
            231,263,159,232,186,222,220,139,526,527,
            393,115,580,471,577,134,791,784,777,554,
            528,139,343,482,549,437,495,494,577,131,
            819,812,798,139,388,805,437,483,484,137,
            577,392,437,526,527,485,486,489,32,488,
            487,529,530,531,141,528,437,343,482,2,
            437,139,563,564,437,351,350,346,577,437,
            347,383,542,543,544,545,437,139,575,542,
            543,544,545,124,577,387,542,543,544,545,
            84,139,298,542,543,544,545,139,577,218,
            531,531,531,531,139,437,298,530,530,530,
            530,139,437,518,529,529,529,529,139,437,
            470,346,346,346,346,139,437,271,347,347,
            347,347,139,437,437,350,350,350,350,139,
            69,554,351,351,351,351,139,495,494,278,
            357,437,395,577,28,278,357,365,139,128,
            98,67,437,437,549,577,437,554,554,554,
            496,437,573,437,569,97,376,437,377,437,
            511,437,472,73,377,79,100,517,72,512,
            178,437,570,437,473,113,101,551,217,78,
            102,437,437,437,437,437,437,437,217,437,
            437,217,217
        };
    };
    public final static char termAction[] = TermAction.termAction;
    public final int termAction(int index) { return termAction[index]; }

    public interface Asb {
        public final static char asb[] = {0,
            96,193,96,171,92,68,68,92,37,33,
            33,36,61,1,33,92,92,33,37,61,
            33,33,33,33,33,61,29,95,94,68,
            68,68,125,170,37,67,63,170,170,36,
            37,8,125,67,63,170,170,121,170,170,
            37,37,67,170,170,170,67,61,170,67,
            125,125,125,125,125,125,92,130,92,92,
            1,68,1,1,168,92,92,28,28,28,
            28,28,28,92,133,124,167,124,167,160,
            92,124,5,125,92,125,5,133,163,164,
            5,5,133,5,166,133
        };
    };
    public final static char asb[] = Asb.asb;
    public final int asb(int index) { return asb[index]; }

    public interface Asr {
        public final static byte asr[] = {0,
            2,9,10,11,13,1,0,17,18,19,
            20,21,22,23,24,25,26,27,28,29,
            30,31,32,33,34,35,7,13,9,10,
            14,11,1,2,0,1,17,18,19,20,
            7,9,10,14,21,22,23,11,24,25,
            26,27,28,29,30,31,32,33,34,35,
            8,0,3,4,5,6,2,17,18,19,
            20,7,9,10,14,21,22,23,11,24,
            25,26,27,28,29,30,31,32,33,34,
            35,1,0,37,36,17,18,19,20,9,
            10,14,21,22,23,11,24,25,26,27,
            28,40,29,30,31,32,33,34,35,0,
            16,15,0,2,3,4,5,6,0,1,
            38,0,17,18,19,20,7,9,10,14,
            21,22,23,11,24,25,26,27,28,29,
            30,31,32,33,34,35,1,12,0,39,
            36,0,2,13,8,12,1,16,15,7,
            17,18,19,14,10,9,21,22,23,11,
            24,25,26,27,28,31,32,30,33,34,
            29,20,35,0
        };
    };
    public final static byte asr[] = Asr.asr;
    public final int asr(int index) { return asr[index]; }

    public interface Nasb {
        public final static byte nasb[] = {0,
            37,36,42,85,45,31,79,47,49,17,
            21,49,51,7,17,3,58,5,49,53,
            60,62,64,66,13,71,10,36,81,55,
            35,76,74,35,92,16,73,35,35,68,
            91,23,74,16,74,35,35,83,35,35,
            92,91,16,35,35,35,27,87,35,33,
            74,74,74,74,74,74,19,36,36,94,
            25,96,25,25,98,89,100,102,102,102,
            102,102,102,104,36,74,106,108,36,36,
            36,74,1,74,36,74,1,36,36,39,
            1,1,36,1,36,36
        };
    };
    public final static byte nasb[] = Nasb.nasb;
    public final int nasb(int index) { return nasb[index]; }

    public interface Nasr {
        public final static byte nasr[] = {0,
            9,0,38,0,36,0,7,40,0,14,
            27,0,16,29,0,1,3,0,12,0,
            43,0,1,20,7,0,16,1,15,0,
            48,0,14,13,1,0,24,0,60,6,
            0,25,49,0,50,0,46,0,11,0,
            41,0,34,0,1,64,0,37,0,33,
            0,32,0,31,0,30,0,58,4,0,
            28,0,62,2,0,1,22,0,47,0,
            51,0,54,0,26,0,53,0,56,0,
            1,4,0,63,0,59,0,17,0,55,
            0,5,0,52,0,18,0,57,0
        };
    };
    public final static byte nasr[] = Nasr.nasr;
    public final int nasr(int index) { return nasr[index]; }

    public interface TerminalIndex {
        public final static byte terminalIndex[] = {0,
            41,40,1,2,3,4,18,42,19,20,
            25,5,17,21,15,16,11,12,13,14,
            22,23,24,26,27,28,29,30,32,33,
            34,35,36,37,38,7,6,8,9,31,
            39,43
        };
    };
    public final static byte terminalIndex[] = TerminalIndex.terminalIndex;
    public final int terminalIndex(int index) { return terminalIndex[index]; }

    public interface NonterminalIndex {
        public final static byte nonterminalIndex[] = {0,
            0,67,74,70,68,87,82,85,86,80,
            0,64,66,69,71,72,76,79,81,83,
            88,89,0,0,0,44,45,46,47,48,
            49,50,51,52,0,53,54,55,56,57,
            58,0,59,60,0,61,0,0,62,63,
            0,65,73,75,77,78,0,0,84,0,
            0,0,90,91,0
        };
    };
    public final static byte nonterminalIndex[] = NonterminalIndex.nonterminalIndex;
    public final int nonterminalIndex(int index) { return nonterminalIndex[index]; }
    public final static int scopePrefix[] = null;
    public final int scopePrefix(int index) { return 0;}

    public final static int scopeSuffix[] = null;
    public final int scopeSuffix(int index) { return 0;}

    public final static int scopeLhs[] = null;
    public final int scopeLhs(int index) { return 0;}

    public final static int scopeLa[] = null;
    public final int scopeLa(int index) { return 0;}

    public final static int scopeStateSet[] = null;
    public final int scopeStateSet(int index) { return 0;}

    public final static int scopeRhs[] = null;
    public final int scopeRhs(int index) { return 0;}

    public final static int scopeState[] = null;
    public final int scopeState(int index) { return 0;}

    public final static int inSymb[] = null;
    public final int inSymb(int index) { return 0;}


    public interface Name {
        public final static String name[] = {
            "",
            "::=",
            "::=?",
            "->",
            "->?",
            "|",
            "=",
            ",",
            "(",
            ")",
            "$empty",
            "ALIAS_KEY",
            "AST_KEY",
            "DEFINE_KEY",
            "DISJOINTPREDECESSORSETS_KEY",
            "DROPRULES_KEY",
            "DROPSYMBOLS_KEY",
            "EMPTY_KEY",
            "END_KEY",
            "ERROR_KEY",
            "EOL_KEY",
            "EOF_KEY",
            "EXPORT_KEY",
            "GLOBALS_KEY",
            "HEADERS_KEY",
            "IDENTIFIER_KEY",
            "IMPORT_KEY",
            "INCLUDE_KEY",
            "KEYWORDS_KEY",
            "NAMES_KEY",
            "NOTICE_KEY",
            "OPTIONS_KEY",
            "RECOVER_KEY",
            "TERMINALS_KEY",
            "RULES_KEY",
            "START_KEY",
            "TRAILERS_KEY",
            "TYPES_KEY",
            "EOF_TOKEN",
            "SINGLE_LINE_COMMENT",
            "MACRO_NAME",
            "SYMBOL",
            "BLOCK",
            "ERROR_TOKEN",
            "JikesPG_item",
            "alias_segment",
            "ast_segment",
            "define_segment",
            "eof_segment",
            "eol_segment",
            "error_segment",
            "export_segment",
            "globals_segment",
            "identifier_segment",
            "import_segment",
            "include_segment",
            "keywords_segment",
            "names_segment",
            "notice_segment",
            "start_segment",
            "terminals_segment",
            "types_segment",
            "option_spec",
            "option_list",
            "option",
            "symbol_list",
            "aliasSpec",
            "produces",
            "alias_rhs",
            "alias_lhs_macro_name",
            "action_segment",
            "defineSpec",
            "macro_name_symbol",
            "macro_segment",
            "terminal_symbol",
            "drop_command_list",
            "drop_command",
            "drop_symbols",
            "drop_rules",
            "drop_rule",
            "rhsList",
            "keywordSpec",
            "name",
            "nameSpec",
            "nonTerm",
            "rhs",
            "symWithAttrsList",
            "symWithAttrs",
            "terminal",
            "type_declarations",
            "barSymbolList",
            "symbol_pair"
        };
    };
    public final static String name[] = Name.name;
    public final String name(int index) { return name[index]; }

    public final static int
           ERROR_SYMBOL      = 42,
           SCOPE_UBOUND      = -1,
           SCOPE_SIZE        = 0,
           MAX_NAME_LENGTH   = 27;

    public final int getErrorSymbol() { return ERROR_SYMBOL; }
    public final int getScopeUbound() { return SCOPE_UBOUND; }
    public final int getScopeSize() { return SCOPE_SIZE; }
    public final int getMaxNameLength() { return MAX_NAME_LENGTH; }

    public final static int
           NUM_STATES        = 106,
           NT_OFFSET         = 42,
           LA_STATE_OFFSET   = 579,
           MAX_LA            = 3,
           NUM_RULES         = 142,
           NUM_NONTERMINALS  = 65,
           NUM_SYMBOLS       = 107,
           SEGMENT_SIZE      = 8192,
           START_STATE       = 204,
           IDENTIFIER_SYMBOL = 0,
           EOFT_SYMBOL       = 35,
           EOLT_SYMBOL       = 35,
           ACCEPT_ACTION     = 436,
           ERROR_ACTION      = 437;

    public final static boolean BACKTRACK = true;

    public final int getNumStates() { return NUM_STATES; }
    public final int getNtOffset() { return NT_OFFSET; }
    public final int getLaStateOffset() { return LA_STATE_OFFSET; }
    public final int getMaxLa() { return MAX_LA; }
    public final int getNumRules() { return NUM_RULES; }
    public final int getNumNonterminals() { return NUM_NONTERMINALS; }
    public final int getNumSymbols() { return NUM_SYMBOLS; }
    public final int getSegmentSize() { return SEGMENT_SIZE; }
    public final int getStartState() { return START_STATE; }
    public final int getStartSymbol() { return lhs[0]; }
    public final int getIdentifierSymbol() { return IDENTIFIER_SYMBOL; }
    public final int getEoftSymbol() { return EOFT_SYMBOL; }
    public final int getEoltSymbol() { return EOLT_SYMBOL; }
    public final int getAcceptAction() { return ACCEPT_ACTION; }
    public final int getErrorAction() { return ERROR_ACTION; }
    public final boolean isValidForParser() { return isValidForParser; }
    public final boolean getBacktrack() { return BACKTRACK; }

    public final int originalState(int state) {
        return -baseCheck[state];
    }
    public final int asi(int state) {
        return asb[originalState(state)];
    }
    public final int nasi(int state) {
        return nasb[originalState(state)];
    }
    public final int inSymbol(int state) {
        return inSymb[originalState(state)];
    }

    public final int ntAction(int state, int sym) {
        return baseAction[state + sym];
    }

    public final int tAction(int state, int sym) {
        int i = baseAction[state],
            k = i + sym;
        return termAction[termCheck[k] == sym ? k : i];
    }
    public final int lookAhead(int la_state, int sym) {
        int k = la_state + sym;
        return termAction[termCheck[k] == sym ? k : la_state];
    }
}
