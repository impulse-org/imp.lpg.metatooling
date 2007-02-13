package org.jikespg.uide.parser;

public class JikesPGKWLexerprs implements lpg.javaruntime.ParseTable, JikesPGKWLexersym {

    public interface IsNullable {
        public final static byte isNullable[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0
        };
    };
    public final static byte isNullable[] = IsNullable.isNullable;
    public final boolean isNullable(int index) { return isNullable[index] != 0; }

    public interface ProsthesesIndex {
        public final static byte prosthesesIndex[] = {0,
            2,1
        };
    };
    public final static byte prosthesesIndex[] = ProsthesesIndex.prosthesesIndex;
    public final int prosthesesIndex(int index) { return prosthesesIndex[index]; }

    public interface IsKeyword {
        public final static byte isKeyword[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0
        };
    };
    public final static byte isKeyword[] = IsKeyword.isKeyword;
    public final boolean isKeyword(int index) { return isKeyword[index] != 0; }

    public interface BaseCheck {
        public final static byte baseCheck[] = {0,
            6,4,7,24,10,12,6,4,6,4,
            4,7,8,8,11,7,8,9,6,7,
            7,10,8,6,6,9,6
        };
    };
    public final static byte baseCheck[] = BaseCheck.baseCheck;
    public final int baseCheck(int index) { return baseCheck[index]; }
    public final static byte rhs[] = baseCheck;
    public final int rhs(int index) { return rhs[index]; };

    public interface BaseAction {
        public final static char baseAction[] = {
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,19,30,
            28,1,55,14,26,48,31,56,24,62,
            58,13,41,43,64,66,67,68,69,71,
            70,76,78,18,82,83,84,88,8,86,
            46,89,91,87,99,100,93,104,105,107,
            112,114,115,117,119,120,121,124,106,122,
            128,133,134,135,136,137,138,139,145,147,
            146,151,155,148,158,161,159,163,149,170,
            169,164,174,176,178,175,182,184,186,49,
            188,189,191,194,196,190,201,202,204,205,
            206,208,209,213,216,217,218,219,222,224,
            226,228,230,232,235,237,241,242,238,244,
            246,249,255,252,247,259,261,263,267,269,
            270,251,271,275,276,278,283,277,285,281,
            288,290,291,295,297,296,301,303,304,171,
            171
        };
    };
    public final static char baseAction[] = BaseAction.baseAction;
    public final int baseAction(int index) { return baseAction[index]; }
    public final static char lhs[] = baseAction;
    public final int lhs(int index) { return lhs[index]; };

    public interface TermCheck {
        public final static byte termCheck[] = {0,
            0,1,2,3,4,5,6,0,8,9,
            10,4,0,0,1,3,4,0,0,19,
            20,9,22,0,12,0,1,0,3,16,
            0,14,9,10,4,12,18,25,8,14,
            0,1,0,3,2,0,6,0,0,7,
            2,3,7,26,0,0,1,0,11,5,
            15,0,1,0,7,0,0,0,0,0,
            0,8,7,3,5,0,8,0,11,13,
            5,0,0,0,1,0,0,0,0,12,
            0,3,0,11,13,8,11,11,0,0,
            10,2,4,0,0,0,0,15,5,3,
            6,0,1,0,0,1,0,4,0,0,
            0,0,6,0,1,6,6,0,7,24,
            12,4,0,0,0,0,0,0,0,4,
            4,9,5,10,0,0,0,0,0,11,
            0,17,6,8,0,5,2,0,0,2,
            0,13,0,0,6,21,4,7,0,0,
            23,2,4,0,0,0,3,0,1,16,
            5,0,8,0,3,0,3,0,0,0,
            0,4,2,0,1,0,1,9,9,14,
            0,0,1,0,0,0,3,0,0,9,
            5,3,0,6,10,0,0,0,0,7,
            5,0,1,0,6,0,3,0,3,0,
            14,0,1,16,0,8,0,0,2,10,
            0,0,2,0,7,0,0,2,0,15,
            0,0,9,12,0,7,2,6,0,1,
            0,11,0,17,2,5,0,1,0,0,
            0,2,4,3,0,0,0,0,3,2,
            0,7,0,1,0,1,10,0,1,0,
            0,2,2,13,0,0,0,2,4,3,
            0,1,0,0,0,2,0,5,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0
        };
    };
    public final static byte termCheck[] = TermCheck.termCheck;
    public final int termCheck(int index) { return termCheck[index]; }

    public interface TermAction {
        public final static char termAction[] = {0,
            171,41,32,33,35,34,38,171,43,36,
            42,83,171,171,46,61,60,171,171,40,
            39,62,37,171,63,171,49,171,48,45,
            171,78,54,56,51,55,31,59,52,47,
            171,66,171,64,67,171,65,171,171,68,
            125,126,181,170,171,171,53,171,50,44,
            182,171,57,171,58,171,171,171,171,171,
            171,69,70,74,75,171,73,171,72,71,
            76,171,171,171,81,171,171,171,171,77,
            171,85,171,80,79,82,84,86,171,171,
            179,88,87,171,171,171,171,89,173,91,
            90,171,92,171,171,94,171,93,171,171,
            171,171,95,171,99,97,98,171,101,100,
            96,102,171,171,171,171,171,171,171,106,
            107,103,108,104,171,171,171,171,171,109,
            171,105,111,112,171,196,195,171,171,198,
            171,117,171,171,115,110,116,114,171,171,
            113,190,118,171,171,171,120,171,122,119,
            121,171,123,171,124,171,180,171,171,171,
            171,127,172,171,129,171,130,128,131,178,
            171,171,191,171,171,171,132,171,171,192,
            187,135,171,134,133,171,171,171,171,136,
            183,171,174,171,139,171,194,171,140,171,
            137,171,188,138,171,141,171,171,185,142,
            171,171,184,171,145,171,171,197,171,143,
            171,171,146,144,171,147,189,148,171,150,
            171,154,171,149,193,151,171,152,171,171,
            171,176,153,186,171,171,171,171,156,177,
            171,155,171,157,171,159,158,171,161,171,
            171,162,163,160,171,171,171,166,164,165,
            171,167,171,171,171,175,171,168
        };
    };
    public final static char termAction[] = TermAction.termAction;
    public final int termAction(int index) { return termAction[index]; }
    public final int asb(int index) { return 0; }
    public final int asr(int index) { return 0; }
    public final int nasb(int index) { return 0; }
    public final int nasr(int index) { return 0; }
    public final int terminalIndex(int index) { return 0; }
    public final int nonterminalIndex(int index) { return 0; }
    public final int scopePrefix(int index) { return 0;}
    public final int scopeSuffix(int index) { return 0;}
    public final int scopeLhs(int index) { return 0;}
    public final int scopeLa(int index) { return 0;}
    public final int scopeStateSet(int index) { return 0;}
    public final int scopeRhs(int index) { return 0;}
    public final int scopeState(int index) { return 0;}
    public final int inSymb(int index) { return 0;}
    public final String name(int index) { return null; }
    public final int getErrorSymbol() { return 0; }
    public final int getScopeUbound() { return 0; }
    public final int getScopeSize() { return 0; }
    public final int getMaxNameLength() { return 0; }

    public final static int
           NUM_STATES        = 140,
           NT_OFFSET         = 29,
           LA_STATE_OFFSET   = 198,
           MAX_LA            = 0,
           NUM_RULES         = 27,
           NUM_NONTERMINALS  = 2,
           NUM_SYMBOLS       = 31,
           SEGMENT_SIZE      = 8192,
           START_STATE       = 28,
           IDENTIFIER_SYMBOL = 0,
           EOFT_SYMBOL       = 26,
           EOLT_SYMBOL       = 30,
           ACCEPT_ACTION     = 170,
           ERROR_ACTION      = 171;

    public final static boolean BACKTRACK = false;

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

    public final int originalState(int state) { return 0; }
    public final int asi(int state) { return 0; }
    public final int nasi(int state) { return 0; }
    public final int inSymbol(int state) { return 0; }

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
