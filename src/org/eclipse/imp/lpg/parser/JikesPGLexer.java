package org.jikespg.uide.parser;

import lpg.javaruntime.*;
import java.util.*;
import org.eclipse.uide.parser.ILexer;
public class JikesPGLexer extends LpgLexStream implements JikesPGParsersym, JikesPGLexersym, RuleAction, ILexer
{
    private static ParseTable prs = new JikesPGLexerprs();
    private LexParser lexParser = new LexParser(this, prs, this);

    public int getToken(int i) { return lexParser.getToken(i); }
    public int getRhsFirstTokenIndex(int i) { return lexParser.getFirstToken(i); }
    public int getRhsLastTokenIndex(int i) { return lexParser.getLastToken(i); }

    public int getLeftSpan() { return lexParser.getFirstToken(); }
    public int getRightSpan() { return lexParser.getLastToken(); }

    public JikesPGLexer(String filename, int tab) throws java.io.IOException 
    {
        super(filename, tab);
    }

    public JikesPGLexer(char[] input_chars, String filename, int tab)
    {
        super(input_chars, filename, tab);
    }

    public JikesPGLexer(char[] input_chars, String filename)
    {
        this(input_chars, filename, 1);
    }

    public JikesPGLexer() {}

    public String[] orderedExportedSymbols() { return JikesPGParsersym.orderedTerminalSymbols; }
    public LexStream getLexStream() { return (LexStream) this; }

    public void lexer(IPrsStream prsStream)
    {
        lexer(null, prsStream);
    }
    
    public void lexer(Monitor monitor, IPrsStream prsStream)
    {
        if (getInputChars() == null)
            throw new NullPointerException("LexStream was not initialized");

        setPrsStream(prsStream);

        prsStream.makeToken(0, 0, 0); // Token list must start with a bad token
            
        lexParser.parseCharacters(monitor);  // Lex the input characters
            
        int i = getStreamIndex();
        prsStream.makeToken(i, i, TK_EOF_TOKEN); // and end with the end of file token
        prsStream.setStreamLength(prsStream.getSize());
            
        return;
    }

    //
    // The Lexer contains an array of characters as the input stream to be parsed.
    // There are methods to retrieve and classify characters.
    // The lexparser "token" is implemented simply as the index of the next character in the array.
    // The Lexer extends the abstract class LpgLexStream with an implementation of the abstract
    // method getKind.  The template defines the Lexer class and the lexer() method.
    // A driver creates the action class, "Lexer", passing an Option object to the constructor.
    //
    JikesPGKWLexer kwLexer;
    boolean printTokens;
    private final static int ECLIPSE_TAB_VALUE = 4;

    public int [] getKeywordKinds() { return kwLexer.getKeywordKinds(); }

    public JikesPGLexer(String filename) throws java.io.IOException
    {
        this(filename, ECLIPSE_TAB_VALUE);
        this.kwLexer = new JikesPGKWLexer(getInputChars(), TK_MACRO_NAME);
    }

    public void initialize(char [] content, String filename)
    {
        super.initialize(content, filename);
        if (this.kwLexer == null)
             this.kwLexer = new JikesPGKWLexer(getInputChars(), TK_MACRO_NAME);
        else this.kwLexer.setInputChars(getInputChars());
    }
    
    final void makeToken(int kind)
    {
        int startOffset = getLeftSpan(),
            endOffset = getRightSpan();
        makeToken(startOffset, endOffset, kind);
        if (printTokens) printValue(startOffset, endOffset);
    }

    final void makeComment(int kind)
    {
        int startOffset = getLeftSpan(),
            endOffset = getRightSpan();
        super.getPrsStream().makeAdjunct(startOffset, endOffset, kind);
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
        makeToken(startOffset, endOffset, kwKind);
        if (printTokens) printValue(startOffset, endOffset);
    }
    
    final void printValue(int startOffset, int endOffset)
    {
        String s = new String(getInputChars(), startOffset, endOffset - startOffset + 1);
        System.out.print(s);
    }

    //
    //
    //
    public final static int tokenKind[] =
    {
        Char_CtlCharNotWS,    // 000    0x00
        Char_CtlCharNotWS,    // 001    0x01
        Char_CtlCharNotWS,    // 002    0x02
        Char_CtlCharNotWS,    // 003    0x03
        Char_CtlCharNotWS,    // 004    0x04
        Char_CtlCharNotWS,    // 005    0x05
        Char_CtlCharNotWS,    // 006    0x06
        Char_CtlCharNotWS,    // 007    0x07
        Char_CtlCharNotWS,    // 008    0x08
        Char_HT,              // 009    0x09
        Char_LF,              // 010    0x0A
        Char_CtlCharNotWS,    // 011    0x0B
        Char_FF,              // 012    0x0C
        Char_CR,              // 013    0x0D
        Char_CtlCharNotWS,    // 014    0x0E
        Char_CtlCharNotWS,    // 015    0x0F
        Char_CtlCharNotWS,    // 016    0x10
        Char_CtlCharNotWS,    // 017    0x11
        Char_CtlCharNotWS,    // 018    0x12
        Char_CtlCharNotWS,    // 019    0x13
        Char_CtlCharNotWS,    // 020    0x14
        Char_CtlCharNotWS,    // 021    0x15
        Char_CtlCharNotWS,    // 022    0x16
        Char_CtlCharNotWS,    // 023    0x17
        Char_CtlCharNotWS,    // 024    0x18
        Char_CtlCharNotWS,    // 025    0x19
        Char_CtlCharNotWS,    // 026    0x1A
        Char_CtlCharNotWS,    // 027    0x1B
        Char_CtlCharNotWS,    // 028    0x1C
        Char_CtlCharNotWS,    // 029    0x1D
        Char_CtlCharNotWS,    // 030    0x1E
        Char_CtlCharNotWS,    // 031    0x1F
        Char_Space,           // 032    0x20
        Char_Exclamation,     // 033    0x21
        Char_DoubleQuote,     // 034    0x22
        Char_Sharp,           // 035    0x23
        Char_DollarSign,      // 036    0x24
        Char_Percent,         // 037    0x25
        Char_Ampersand,       // 038    0x26
        Char_SingleQuote,     // 039    0x27
        Char_LeftParen,       // 040    0x28
        Char_RightParen,      // 041    0x29
        Char_Star,            // 042    0x2A
        Char_Plus,            // 043    0x2B
        Char_Comma,           // 044    0x2C
        Char_Minus,           // 045    0x2D
        Char_Dot,             // 046    0x2E
        Char_Slash,           // 047    0x2F
        Char_0,               // 048    0x30
        Char_1,               // 049    0x31
        Char_2,               // 050    0x32
        Char_3,               // 051    0x33
        Char_4,               // 052    0x34
        Char_5,               // 053    0x35
        Char_6,               // 054    0x36
        Char_7,               // 055    0x37
        Char_8,               // 056    0x38
        Char_9,               // 057    0x39
        Char_Colon,           // 058    0x3A
        Char_SemiColon,       // 059    0x3B
        Char_LessThan,        // 060    0x3C
        Char_Equal,           // 061    0x3D
        Char_GreaterThan,     // 062    0x3E
        Char_QuestionMark,    // 063    0x3F
        Char_AtSign,          // 064    0x40
        Char_A,               // 065    0x41
        Char_B,               // 066    0x42
        Char_C,               // 067    0x43
        Char_D,               // 068    0x44
        Char_E,               // 069    0x45
        Char_F,               // 070    0x46
        Char_G,               // 071    0x47
        Char_H,               // 072    0x48
        Char_I,               // 073    0x49
        Char_J,               // 074    0x4A
        Char_K,               // 075    0x4B
        Char_L,               // 076    0x4C
        Char_M,               // 077    0x4D
        Char_N,               // 078    0x4E
        Char_O,               // 079    0x4F
        Char_P,               // 080    0x50
        Char_Q,               // 081    0x51
        Char_R,               // 082    0x52
        Char_S,               // 083    0x53
        Char_T,               // 084    0x54
        Char_U,               // 085    0x55
        Char_V,               // 086    0x56
        Char_W,               // 087    0x57
        Char_X,               // 088    0x58
        Char_Y,               // 089    0x59
        Char_Z,               // 090    0x5A
        Char_LeftBracket,     // 091    0x5B
        Char_BackSlash,       // 092    0x5C
        Char_RightBracket,    // 093    0x5D
        Char_Caret,           // 094    0x5E
        Char__,               // 095    0x5F
        Char_BackQuote,       // 096    0x60
        Char_a,               // 097    0x61
        Char_b,               // 098    0x62
        Char_c,               // 099    0x63
        Char_d,               // 100    0x64
        Char_e,               // 101    0x65
        Char_f,               // 102    0x66
        Char_g,               // 103    0x67
        Char_h,               // 104    0x68
        Char_i,               // 105    0x69
        Char_j,               // 106    0x6A
        Char_k,               // 107    0x6B
        Char_l,               // 108    0x6C
        Char_m,               // 109    0x6D
        Char_n,               // 110    0x6E
        Char_o,               // 111    0x6F
        Char_p,               // 112    0x70
        Char_q,               // 113    0x71
        Char_r,               // 114    0x72
        Char_s,               // 115    0x73
        Char_t,               // 116    0x74
        Char_u,               // 117    0x75
        Char_v,               // 118    0x76
        Char_w,               // 119    0x77
        Char_x,               // 120    0x78
        Char_y,               // 121    0x79
        Char_z,               // 122    0x7A
        Char_LeftBrace,       // 123    0x7B
        Char_VerticalBar,     // 124    0x7C
        Char_RightBrace,      // 125    0x7D
        Char_Tilde,           // 126    0x7E

        Char_AfterASCII,      // for all chars in range 128..65534
        Char_EOF              // for '\uffff' or 65535 
    };
            
    public final int getKind(int i)  // Classify character at ith location
    {
        int c = (i >= getStreamLength() ? '\uffff' : getCharValue(i));
        return (c < 128 // ASCII Character
                  ? tokenKind[c]
                  : c == '\uffff'
                       ? Char_EOF
                       : Char_AfterASCII);
    }

    public void ruleAction( int ruleNumber)
    {
        switch(ruleNumber)
        {
 
            //
            // Rule 1:  Token ::= white
            //
            case 1: { 
             skipToken();           break;
            }
 
            //
            // Rule 2:  Token ::= singleLineComment
            //
            case 2: { 
             makeComment(TK_SINGLE_LINE_COMMENT);           break;
            }
 
            //
            // Rule 4:  Token ::= MacroSymbol
            //
            case 4: { 
             checkForKeyWord();          break;
            }
 
            //
            // Rule 5:  Token ::= Symbol
            //
            case 5: { 
             makeToken(TK_SYMBOL);          break;
            }
 
            //
            // Rule 6:  Token ::= Block
            //
            case 6: { 
             makeToken(TK_BLOCK);          break;
            }
 
            //
            // Rule 7:  Token ::= Equivalence
            //
            case 7: { 
             makeToken(TK_EQUIVALENCE);          break;
            }
 
            //
            // Rule 8:  Token ::= Equivalence ?
            //
            case 8: { 
             makeToken(TK_PRIORITY_EQUIVALENCE);          break;
            }
 
            //
            // Rule 9:  Token ::= Arrow
            //
            case 9: { 
             makeToken(TK_ARROW);          break;
            }
 
            //
            // Rule 10:  Token ::= Arrow ?
            //
            case 10: { 
             makeToken(TK_PRIORITY_ARROW);          break;
            }
 
            //
            // Rule 11:  Token ::= |
            //
            case 11: { 
             makeToken(TK_OR_MARKER);          break;
            }
 
            //
            // Rule 750:  OptionLines ::= OptionLineList
            //
            case 750: { 
            
                  // What ever needs to happen after the options have been 
                  // scanned must happen here.
                  break;
            }
       
            //
            // Rule 759:  options ::= % oO pP tT iI oO nN sS
            //
            case 759: { 
            
                  makeToken(getLeftSpan(), getRightSpan(), TK_OPTIONS_KEY);
                  break;
            }
       
            //
            // Rule 760:  OptionComment ::= singleLineComment
            //
            case 760: { 
             makeComment(TK_SINGLE_LINE_COMMENT);           break;
            }
 
            //
            // Rule 784:  separator ::= ,$comma
            //
            case 784: { 
              makeToken(getLeftSpan(), getRightSpan(), TK_COMMA);           break;
            }
 
            //
            // Rule 785:  option ::= action_block$ab optionWhite =$eq optionWhite ($lp optionWhite filename$fn optionWhite ,$comma1 optionWhite block_begin$bb optionWhite ,$comma2 optionWhite block_end$be optionWhite )$rp optionWhite
            //
            case 785: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_LEFT_PAREN);
                  makeToken(getRhsFirstTokenIndex(7), getRhsLastTokenIndex(7), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(9), getRhsLastTokenIndex(9), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(11), getRhsLastTokenIndex(11), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(13), getRhsLastTokenIndex(13), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(15), getRhsLastTokenIndex(15), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(17), getRhsLastTokenIndex(17), TK_RIGHT_PAREN);
                  break;
            }
       
            //
            // Rule 791:  option ::= ast_directory$ad optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 791: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 794:  option ::= ast_type$at optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 794: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 797:  option ::= attributes$a optionWhite
            //
            case 797: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 798:  option ::= no attributes$a optionWhite
            //
            case 798: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 800:  option ::= automatic_ast$a optionWhite
            //
            case 800: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 801:  option ::= no automatic_ast$a optionWhite
            //
            case 801: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 802:  option ::= automatic_ast$aa optionWhite =$eq optionWhite automatic_ast_value$val optionWhite
            //
            case 802: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 808:  option ::= backtrack$b optionWhite
            //
            case 808: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 809:  option ::= no backtrack$b optionWhite
            //
            case 809: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 811:  option ::= byte$b optionWhite
            //
            case 811: { 
              makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 812:  option ::= no byte$b optionWhite
            //
            case 812: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 814:  option ::= conflicts$c optionWhite
            //
            case 814: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 815:  option ::= no conflicts$c optionWhite
            //
            case 815: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 817:  option ::= dat_directory$dd optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 817: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 820:  option ::= dat_file$df optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 820: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 822:  option ::= dcl_file$df optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 822: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 824:  option ::= def_file$df optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 824: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 826:  option ::= debug$d optionWhite
            //
            case 826: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 827:  option ::= no debug$d optionWhite
            //
            case 827: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 829:  option ::= edit$e optionWhite
            //
            case 829: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 830:  option ::= no edit$e optionWhite
            //
            case 830: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 832:  option ::= error_maps$e optionWhite
            //
            case 832: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 833:  option ::= no error_maps$e optionWhite
            //
            case 833: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 836:  option ::= escape$e optionWhite =$eq optionWhite anyNonWhiteChar$val optionWhite
            //
            case 836: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 838:  option ::= export_terminals$et optionWhite =$eq optionWhite filename$fn optionWhite
            //
            case 838: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 839:  option ::= export_terminals$et optionWhite =$eq optionWhite ($lp optionWhite filename$fn optionWhite )$rp optionWhite
            //
            case 839: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_LEFT_PAREN);
                  makeToken(getRhsFirstTokenIndex(7), getRhsLastTokenIndex(7), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(9), getRhsLastTokenIndex(9), TK_RIGHT_PAREN);
                  break;
            }
       
            //
            // Rule 840:  option ::= export_terminals$et optionWhite =$eq optionWhite ($lp optionWhite filename$fn optionWhite ,$comma optionWhite export_prefix$ep optionWhite )$rp optionWhite
            //
            case 840: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_LEFT_PAREN);
                  makeToken(getRhsFirstTokenIndex(7), getRhsLastTokenIndex(7), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(9), getRhsLastTokenIndex(9), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(11), getRhsLastTokenIndex(11), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(13), getRhsLastTokenIndex(13), TK_RIGHT_PAREN);
                  break;
            }
       
            //
            // Rule 841:  option ::= export_terminals$et optionWhite =$eq optionWhite ($lp optionWhite filename$fn optionWhite ,$comma1 optionWhite export_prefix$ep optionWhite ,$comma2 optionWhite export_suffix$es optionWhite )$rp optionWhite
            //
            case 841: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_LEFT_PAREN);
                  makeToken(getRhsFirstTokenIndex(7), getRhsLastTokenIndex(7), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(9), getRhsLastTokenIndex(9), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(11), getRhsLastTokenIndex(11), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(13), getRhsLastTokenIndex(13), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(15), getRhsLastTokenIndex(15), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(17), getRhsLastTokenIndex(17), TK_RIGHT_PAREN);
                  break;
            }
       
            //
            // Rule 846:  option ::= extends_parsetable$e optionWhite
            //
            case 846: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 847:  option ::= no extends_parsetable$e optionWhite
            //
            case 847: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 848:  option ::= extends_parsetable$ep optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 848: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 851:  option ::= factory$f optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 851: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 853:  option ::= file_prefix$fp optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 853: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 856:  option ::= filter$f optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 856: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 858:  option ::= first$f optionWhite
            //
            case 858: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 859:  option ::= no first$f optionWhite
            //
            case 859: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 861:  option ::= follow$f optionWhite
            //
            case 861: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 862:  option ::= no follow$f optionWhite
            //
            case 862: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 864:  option ::= goto_default$g optionWhite
            //
            case 864: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 865:  option ::= no goto_default$g optionWhite
            //
            case 865: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 868:  option ::= headers$h optionWhite =$eq optionWhite ($lp optionWhite filename$fn optionWhite ,$comma1 optionWhite block_begin$bb optionWhite ,$comma2 optionWhite block_end$be optionWhite )$rp optionWhite
            //
            case 868: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_LEFT_PAREN);
                  makeToken(getRhsFirstTokenIndex(7), getRhsLastTokenIndex(7), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(9), getRhsLastTokenIndex(9), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(11), getRhsLastTokenIndex(11), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(13), getRhsLastTokenIndex(13), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(15), getRhsLastTokenIndex(15), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(17), getRhsLastTokenIndex(17), TK_RIGHT_PAREN);
                  break;
            }
       
            //
            // Rule 870:  option ::= imp_file$if optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 870: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 873:  option ::= import_terminals$it optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 873: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 876:  option ::= include_directory$id optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 876: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 880:  option ::= lalr_level$l optionWhite
            //
            case 880: { 
              makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 881:  option ::= no lalr_level$l optionWhite
            //
            case 881: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 882:  option ::= lalr_level$l optionWhite =$eq optionWhite number$val optionWhite
            //
            case 882: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 887:  option ::= list$l optionWhite
            //
            case 887: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 888:  option ::= no list$l optionWhite
            //
            case 888: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 890:  option ::= margin$m optionWhite =$eq optionWhite number$val optionWhite
            //
            case 890: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 892:  option ::= max_cases$mc optionWhite =$eq optionWhite number$val optionWhite
            //
            case 892: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 895:  option ::= names$n optionWhite
            //
            case 895: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 896:  option ::= no names$n optionWhite
            //
            case 896: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 897:  option ::= names$n optionWhite =$eq optionWhite names_value$val optionWhite
            //
            case 897: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 902:  option ::= nt_check$n optionWhite
            //
            case 902: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 903:  option ::= no nt_check$n optionWhite
            //
            case 903: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 906:  option ::= or_marker$om optionWhite =$eq optionWhite anyNonWhiteChar$val optionWhite
            //
            case 906: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 909:  option ::= out_directory$dd optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 909: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 912:  option ::= parent_saved$ps optionWhite
            //
            case 912: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 913:  option ::= no parent_saved$ps optionWhite
            //
            case 913: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 916:  option ::= package$p optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 916: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 918:  option ::= parsetable_interfaces$pi optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 918: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 922:  option ::= prefix$p optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 922: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 924:  option ::= priority$p optionWhite
            //
            case 924: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 925:  option ::= no priority$p optionWhite
            //
            case 925: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 927:  option ::= programming_language$pl optionWhite =$eq optionWhite programming_language_value$val optionWhite
            //
            case 927: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 938:  option ::= prs_file$pf optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 938: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 941:  option ::= quiet$q optionWhite
            //
            case 941: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 942:  option ::= no quiet$q optionWhite
            //
            case 942: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 944:  option ::= read_reduce$r optionWhite
            //
            case 944: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 945:  option ::= no read_reduce$r optionWhite
            //
            case 945: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 948:  option ::= remap_terminals$r optionWhite
            //
            case 948: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            }
 
            //
            // Rule 949:  option ::= no remap_terminals$r optionWhite
            //
            case 949: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            }
 
            //
            // Rule 952:  option ::= scopes$s optionWhite
            //
            case 952: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 953:  option ::= no scopes$s optionWhite
            //
            case 953: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 955:  option ::= serialize$s optionWhite
            //
            case 955: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 956:  option ::= no serialize$s optionWhite
            //
            case 956: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 958:  option ::= shift_default$s optionWhite
            //
            case 958: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 959:  option ::= no shift_default$s optionWhite
            //
            case 959: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 962:  option ::= single_productions$s optionWhite
            //
            case 962: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 963:  option ::= no single_productions$s optionWhite
            //
            case 963: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 966:  option ::= slr$s optionWhite
            //
            case 966: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 967:  option ::= no slr$s optionWhite
            //
            case 967: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 969:  option ::= soft_keywords$s optionWhite
            //
            case 969: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 970:  option ::= no soft_keywords$s optionWhite
            //
            case 970: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 974:  option ::= states$s optionWhite
            //
            case 974: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 975:  option ::= no states$s optionWhite
            //
            case 975: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 977:  option ::= suffix$s optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 977: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 979:  option ::= sym_file$sf optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 979: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 982:  option ::= tab_file$tf optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 982: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 985:  option ::= template$t optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 985: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 987:  option ::= trailers$t optionWhite =$eq optionWhite ($lp optionWhite filename$fn optionWhite ,$comma1 optionWhite block_begin$bb optionWhite ,$comma2 optionWhite block_end$be optionWhite )$rp optionWhite
            //
            case 987: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_LEFT_PAREN);
                  makeToken(getRhsFirstTokenIndex(7), getRhsLastTokenIndex(7), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(9), getRhsLastTokenIndex(9), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(11), getRhsLastTokenIndex(11), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(13), getRhsLastTokenIndex(13), TK_COMMA);
                  makeToken(getRhsFirstTokenIndex(15), getRhsLastTokenIndex(15), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(17), getRhsLastTokenIndex(17), TK_RIGHT_PAREN);
                  break;
            }
       
            //
            // Rule 989:  option ::= table$t optionWhite
            //
            case 989: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 990:  option ::= no table$t optionWhite
            //
            case 990: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 991:  option ::= table$t optionWhite =$eq optionWhite programming_language_value$val optionWhite
            //
            case 991: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 993:  option ::= trace$t optionWhite
            //
            case 993: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 994:  option ::= no trace$t optionWhite
            //
            case 994: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 995:  option ::= trace$t optionWhite =$eq optionWhite trace_value$val optionWhite
            //
            case 995: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 1000:  option ::= variables$v optionWhite
            //
            case 1000: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1001:  option ::= no variables$v optionWhite
            //
            case 1001: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1002:  option ::= variables$v optionWhite =$eq optionWhite variables_value$val optionWhite
            //
            case 1002: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 1009:  option ::= verbose$v optionWhite
            //
            case 1009: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1010:  option ::= no verbose$v optionWhite
            //
            case 1010: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1012:  option ::= visitor$v optionWhite
            //
            case 1012: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1013:  option ::= no visitor$v optionWhite
            //
            case 1013: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1014:  option ::= visitor$v optionWhite =$eq optionWhite visitor_value$val optionWhite
            //
            case 1014: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 1019:  option ::= visitor_type$vt optionWhite =$eq optionWhite Value$val optionWhite
            //
            case 1019: { 
            
                  makeToken(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(1), TK_SYMBOL);
                  makeToken(getRhsFirstTokenIndex(3), getRhsLastTokenIndex(3), TK_EQUAL);
                  makeToken(getRhsFirstTokenIndex(5), getRhsLastTokenIndex(5), TK_SYMBOL);
                  break;
            }
       
            //
            // Rule 1022:  option ::= warnings$w optionWhite
            //
            case 1022: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1023:  option ::= no warnings$w optionWhite
            //
            case 1023: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1025:  option ::= xreference$x optionWhite
            //
            case 1025: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(1), TK_SYMBOL);           break;
            } 
 
            //
            // Rule 1026:  option ::= no xreference$x optionWhite
            //
            case 1026: { 
              makeToken(getLeftSpan(), getRhsLastTokenIndex(2), TK_SYMBOL);           break;
            } 

    
            default:
                break;
        }
        return;
    }
}

