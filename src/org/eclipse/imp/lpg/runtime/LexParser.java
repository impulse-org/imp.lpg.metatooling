package org.jikes.lpg.runtime;

public class LexParser
{
	private int START_STATE,
				NUM_RULES,
				LA_STATE_OFFSET,
				EOFT_SYMBOL,
				ACCEPT_ACTION,
				ERROR_ACTION;


    TokenStream tokStream;
    ParseTable prs;
    RuleAction ra;

    public LexParser(TokenStream tokStream, ParseTable prs, RuleAction ra)
    {
        this.tokStream = tokStream;
        this.prs = prs;
        this.ra = ra;

        START_STATE = prs.getStartState();
		NUM_RULES = prs.getNumRules();
		LA_STATE_OFFSET = prs.getLaStateOffset();
		EOFT_SYMBOL = prs.getEoftSymbol();
		ACCEPT_ACTION = prs.getAcceptAction();
		ERROR_ACTION = prs.getErrorAction();
        
    }

    int act, curtok, currentKind;

    //
    //
    //
    int processReductions(int act)
    {
        do
        {
            stateStackTop -= (prs.rhs(act) - 1);
            ra.ruleAction(act);
            // lexers will reset the stateStack whenever a token has been recognized
            if (stateStackTop == -1) return START_STATE;
            act = prs.ntAction(stack[stateStackTop], prs.lhs(act));
        } while(act <= NUM_RULES);

        return act;
    }

    //
    //
    //
    public void parseCharacters()
    {
        tokStream.reset();
        curtok = tokStream.getToken();
        act = START_STATE;
        currentKind = tokStream.getKind(curtok);

        //
        // Start parsing.
        //
        resetStateStack();

        ProcessTerminals: for (;;)
        {
            if (++stateStackTop >= stackLength)
                reallocateStacks();

            stack[stateStackTop] = act;

            locationStack[stateStackTop] = curtok;

            act = tAction(act, currentKind);

            if (act <= NUM_RULES)
            {
                stateStackTop--; // make reduction look like a shift-reduce
                act = processReductions(act);
            }
            else if (act > ERROR_ACTION)
            {
                curtok = tokStream.getToken();
                currentKind = tokStream.getKind(curtok);

                act = processReductions(act - ERROR_ACTION);
            }
            else if (act < ACCEPT_ACTION)
            {
                curtok = tokStream.getToken();
                currentKind = tokStream.getKind(curtok);
            }
            else if (act == ERROR_ACTION)
            {
                //
                // Report bad token and skip it
                //
                tokStream.reportError(curtok, "Lexical Scanning");
		stateStackTop = -1;
		act = START_STATE;
                curtok = tokStream.getToken();
		currentKind = tokStream.getKind(curtok);
            }
            else break ProcessTerminals;
        }

        return;
    }

// Stacks portion
    final static int STACK_INCREMENT = 1024;

    int stateStackTop,
        stackLength = 0,
        stack[],
        locationStack[];
	int parseStack[];

    //
    // This method is used in lexers (written as parsers) which recognize single tokens
    //
    public final void resetStateStack()
    {
    	stateStackTop = -1;
    }

    //
    // Given a rule of the form     A ::= x1 x2 ... xn     n > 0
    //
    // the function TOKEN(i) yields the symbol xi, if xi is a terminal
    // or ti, if xi is a nonterminal that produced a string of the form
    // xi => ti w.
    //
    public final int getToken(int i)
    {
        return locationStack[stateStackTop + (i - 1)];
    }

    //
    // Given a rule of the form     A ::= x1 x2 ... xn     n > 0
    //
    // The function SYM(i) yields the int subtree associated with symbol
    // xi. NOTE that if xi is a terminal, SYM(i) is undefined ! (However,
    // see token_action below.)
    //
    // setSYM1(int ast) is a function that allows us to assign an int
    // tree to SYM(1).
    //
    public final int getSym(int i) { return parseStack[stateStackTop + (i - 1)]; }
    public final void setSym1(int n) { parseStack[stateStackTop] = n; }

    void reallocateStacks()
    {
        int old_stack_length = (stack == null ? 0 : stackLength);
        stackLength += STACK_INCREMENT;

        if (old_stack_length == 0)
        {
            stack = new int[stackLength];
            locationStack = new int[stackLength];
            parseStack = new int[stackLength];
        }
        else
        {
            System.arraycopy(stack, 0, stack = new int[stackLength], 0, old_stack_length);
            System.arraycopy(locationStack, 0, locationStack = new int[stackLength], 0, old_stack_length);
            System.arraycopy(parseStack, 0, parseStack = new int[stackLength], 0, old_stack_length);
        }
        return;
    }

    public int tAction(int act, int sym)
    {
        act = prs.tAction(act, sym);
        if (act > LA_STATE_OFFSET)
        {
            int next_token = tokStream.peek();
            act = prs.lookAhead(act - LA_STATE_OFFSET, tokStream.getKind(next_token));
            while(act > LA_STATE_OFFSET)
            {
                next_token = tokStream.getNext(next_token);
                act = prs.lookAhead(act - LA_STATE_OFFSET, tokStream.getKind(next_token));
            }
        }
        return act;
    }

}
