package org.jikespg.uide.parser;

import lpg.javaruntime.*;

public class JikesPGKWLexer extends JikesPGKWLexerprs implements JikesPGParsersym
{
    private char[] inputChars;
    private final int keywordKind[] = new int[27 + 1];

    public int[] getKeywordKinds() { return keywordKind; }

    public int lexer(int curtok, int lasttok)
    {
        int current_kind = getKind(inputChars[curtok]),
            act;

        for (act = tAction(START_STATE, current_kind);
             act > NUM_RULES && act < ACCEPT_ACTION;
             act = tAction(act, current_kind))
        {
            curtok++;
            current_kind = (curtok > lasttok
                                   ? Char_EOF
                                   : getKind(inputChars[curtok]));
        }

        if (act > ERROR_ACTION)
        {
            curtok++;
            act -= ERROR_ACTION;
        }

        return keywordKind[act == ERROR_ACTION  || curtok <= lasttok ? 0 : act];
    }

    public void setInputChars(char[] inputChars) { this.inputChars = inputChars; }


    //
    // Each upper case letter is mapped into is corresponding
    // lower case counterpart. For example, if an 'A' appears
    // in the input, it is mapped into Char_a just like 'a'.
    //
    final static int tokenKind[] = new int[128];
    static
    {
        tokenKind['$'] = Char_DollarSign;
        tokenKind['_'] = Char__;

        tokenKind['a'] = Char_a;
        tokenKind['b'] = Char_b;
        tokenKind['c'] = Char_c;
        tokenKind['d'] = Char_d;
        tokenKind['e'] = Char_e;
        tokenKind['f'] = Char_f;
        tokenKind['g'] = Char_g;
        tokenKind['h'] = Char_h;
        tokenKind['i'] = Char_i;
        tokenKind['j'] = Char_j;
        tokenKind['k'] = Char_k;
        tokenKind['l'] = Char_l;
        tokenKind['m'] = Char_m;
        tokenKind['n'] = Char_n;
        tokenKind['o'] = Char_o;
        tokenKind['p'] = Char_p;
        tokenKind['q'] = Char_q;
        tokenKind['r'] = Char_r;
        tokenKind['s'] = Char_s;
        tokenKind['t'] = Char_t;
        tokenKind['u'] = Char_u;
        tokenKind['v'] = Char_v;
        tokenKind['w'] = Char_w;
        tokenKind['x'] = Char_x;
        tokenKind['y'] = Char_y;
        tokenKind['z'] = Char_z;

        tokenKind['A'] = Char_a;
        tokenKind['B'] = Char_b;
        tokenKind['C'] = Char_c;
        tokenKind['D'] = Char_d;
        tokenKind['E'] = Char_e;
        tokenKind['F'] = Char_f;
        tokenKind['G'] = Char_g;
        tokenKind['H'] = Char_h;
        tokenKind['I'] = Char_i;
        tokenKind['J'] = Char_j;
        tokenKind['K'] = Char_k;
        tokenKind['L'] = Char_l;
        tokenKind['M'] = Char_m;
        tokenKind['N'] = Char_n;
        tokenKind['O'] = Char_o;
        tokenKind['P'] = Char_p;
        tokenKind['Q'] = Char_q;
        tokenKind['R'] = Char_r;
        tokenKind['S'] = Char_s;
        tokenKind['T'] = Char_t;
        tokenKind['U'] = Char_u;
        tokenKind['V'] = Char_v;
        tokenKind['W'] = Char_w;
        tokenKind['X'] = Char_x;
        tokenKind['Y'] = Char_y;
        tokenKind['Z'] = Char_z;
    };

    final int getKind(char c)
    {
        return (c < 128 ? tokenKind[c] : 0);
    }


    public JikesPGKWLexer(char[] inputChars, int identifierKind)
    {
        this.inputChars = inputChars;
        keywordKind[0] = identifierKind;

        //
        // Rule 1:  Keyword ::= $ a l i a s
        //
        keywordKind[1] = (TK_ALIAS_KEY);
      
    
        //
        // Rule 2:  Keyword ::= $ a s t
        //
        keywordKind[2] = (TK_AST_KEY);
      
    
        //
        // Rule 3:  Keyword ::= $ d e f i n e
        //
        keywordKind[3] = (TK_DEFINE_KEY);
      
    
        //
        // Rule 4:  Keyword ::= $ d i s j o i n t p r e d e c e s s o r s e t s
        //
        keywordKind[4] = (TK_DISJOINTPREDECESSORSETS_KEY);
      
    
        //
        // Rule 5:  Keyword ::= $ d r o p r u l e s
        //
        keywordKind[5] = (TK_DROPRULES_KEY);
      
    
        //
        // Rule 6:  Keyword ::= $ d r o p s y m b o l s
        //
        keywordKind[6] = (TK_DROPSYMBOLS_KEY);
      
    
        //
        // Rule 7:  Keyword ::= $ e m p t y
        //
        keywordKind[7] = (TK_EMPTY_KEY);
      
    
        //
        // Rule 8:  Keyword ::= $ e n d
        //
        keywordKind[8] = (TK_END_KEY);
      
    
        //
        // Rule 9:  Keyword ::= $ e r r o r
        //
        keywordKind[9] = (TK_ERROR_KEY);
      
    
        //
        // Rule 10:  Keyword ::= $ e o l
        //
        keywordKind[10] = (TK_EOL_KEY);
      
    
        //
        // Rule 11:  Keyword ::= $ e o f
        //
        keywordKind[11] = (TK_EOF_KEY);
      
    
        //
        // Rule 12:  Keyword ::= $ e x p o r t
        //
        keywordKind[12] = (TK_EXPORT_KEY);
      
    
        //
        // Rule 13:  Keyword ::= $ g l o b a l s
        //
        keywordKind[13] = (TK_GLOBALS_KEY);
      
    
        //
        // Rule 14:  Keyword ::= $ h e a d e r s
        //
        keywordKind[14] = (TK_HEADERS_KEY);
      
    
        //
        // Rule 15:  Keyword ::= $ i d e n t i f i e r
        //
        keywordKind[15] = (TK_IDENTIFIER_KEY);
      
    
        //
        // Rule 16:  Keyword ::= $ i m p o r t
        //
        keywordKind[16] = (TK_IMPORT_KEY);
      
    
        //
        // Rule 17:  Keyword ::= $ i n c l u d e
        //
        keywordKind[17] = (TK_INCLUDE_KEY);
      
    
        //
        // Rule 18:  Keyword ::= $ k e y w o r d s
        //
        keywordKind[18] = (TK_KEYWORDS_KEY);
      
    
        //
        // Rule 19:  Keyword ::= $ n a m e s
        //
        keywordKind[19] = (TK_NAMES_KEY);
      
    
        //
        // Rule 20:  Keyword ::= $ n o t i c e
        //
        keywordKind[20] = (TK_NOTICE_KEY);
      
    
        //
        // Rule 21:  Keyword ::= $ o p t i o n
        //
        keywordKind[21] = (TK_OPTIONS_KEY);
      
    
        //
        // Rule 22:  Keyword ::= $ t e r m i n a l s
        //
        keywordKind[22] = (TK_TERMINALS_KEY);
      
    
        //
        // Rule 23:  Keyword ::= $ r e c o v e r
        //
        keywordKind[23] = (TK_RECOVER_KEY);
      
    
        //
        // Rule 24:  Keyword ::= $ r u l e s
        //
        keywordKind[24] = (TK_RULES_KEY);
      
    
        //
        // Rule 25:  Keyword ::= $ s t a r t
        //
        keywordKind[25] = (TK_START_KEY);
      
    
        //
        // Rule 26:  Keyword ::= $ t r a i l e r s
        //
        keywordKind[26] = (TK_TRAILERS_KEY);
      
    
        //
        // Rule 27:  Keyword ::= $ t y p e s
        //
        keywordKind[27] = (TK_TYPES_KEY);
      
    

        for (int i = 0; i < keywordKind.length; i++)
        {
            if (keywordKind[i] == 0)
                keywordKind[i] = identifierKind;
        }
    }
}

