package org.jikes.lpg.runtime;

public class DeterministicParser extends Stacks
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

    public DeterministicParser(TokenStream tokStream, ParseTable prs, RuleAction ra) throws BadParseSymFileException
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
        
        if (!tokStream.isValidForParser()) throw new BadParseSymFileException();
    }

    //
    // Process reductions and continue...
    //
    int processReductions(int act)
    {
        do
        {
            stateStackTop -= (prs.rhs(act) - 1);
            ra.ruleAction(act);
            act = prs.ntAction(stateStack[stateStackTop], prs.lhs(act));
        } while(act <= NUM_RULES);

        return act;
    }

    //
    //
    //
    public Object parse() throws BadParseException
    {
        tokStream.reset();
        int curtok = tokStream.getToken(),
            act = START_STATE,
            current_kind = tokStream.getKind(curtok);

        reallocateStacks(); // make initial allocatation
 
        //
        // Start parsing.
        //
        stateStackTop = -1;

        ProcessTerminals: for (;;)
        {
            try
            {
                stateStack[++stateStackTop] = act;
            }
            catch(IndexOutOfBoundsException e)
            {
                reallocateStacks();
                stateStack[stateStackTop] = act;
            }

            locationStack[stateStackTop] = curtok;

            act = tAction(act, current_kind);
 
            if (act <= NUM_RULES)
            {
                stateStackTop--; // make reduction look like a shift-reduce
                act = processReductions(act);
            }
            else if (act > ERROR_ACTION)
            {
                curtok = tokStream.getToken();
                current_kind = tokStream.getKind(curtok);

                act = processReductions(act - ERROR_ACTION);
            }
            else if (act < ACCEPT_ACTION)
            {
                curtok = tokStream.getToken();
                current_kind = tokStream.getKind(curtok);
            }
            else break ProcessTerminals;
        }

        if (act == ERROR_ACTION)
            throw new BadParseException(curtok);

        return parseStack[0];
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
