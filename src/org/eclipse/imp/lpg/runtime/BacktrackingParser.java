package org.jikes.lpg.runtime;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.uide.parser.Ast;


public class BacktrackingParser extends Stacks implements LpgParser
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

    public BacktrackingParser(TokenStream tokStream, ParseTable prs, RuleAction ra) throws BadParseSymFileException
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

    
    IntTuple action = new IntTuple(1 << 20);

    //
    // Process reductions and continue...
    //
    int process_reductions(int act)
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
    public Ast parse(IProgressMonitor monitor) throws BadParseException
    {
        int error_token = BacktrackParse();

        allocateOtherStacks(); // Allocate Parse Location stacks.

        if (error_token != 0) { // an error was detected?
        	tokStream.reset();
            int curtok = tokStream.getToken();
            stateStackTop = -1;
            int act = START_STATE;
            try {
	        	for (int i = 0; (monitor==null || !monitor.isCanceled()) && i < action.size(); i++)
	            {
	                stateStack[++stateStackTop] = act;
	                locationStack[stateStackTop] = curtok;
	
	                act = action.get(i);
	                if (act <= NUM_RULES)  // a reduce action?
	                {
	                	//System.out.println("bt: reduce "+act);
	                }
	                else                   // a shift or shift-reduce action
	                {
	                    curtok = tokStream.getToken();
	                	//System.out.println("bt: shift "+curtok);
	                	if (curtok == error_token)
	                		break;
	                }
	            }
            }
            catch (ArrayIndexOutOfBoundsException e) {
            	//e.printStackTrace();
            }
            throw new BadParseException(error_token);
        }
        else
        {
            tokStream.reset();
            int curtok = tokStream.getToken();

            stateStackTop = -1;
            int act = START_STATE;

            for (int i = 0; !monitor.isCanceled() && i < action.size(); i++)
            {
                stateStack[++stateStackTop] = act;
                locationStack[stateStackTop] = curtok;

                act = action.get(i);
                if (act <= NUM_RULES)  // a reduce action?
                {
                	//System.out.println("bt: reduce "+act);
                    stateStackTop--; // make reduction look like shift-reduction
                    act = process_reductions(act);
                }
                else                   // a shift or shift-reduce action
                {
                    curtok = tokStream.getToken();
                    if (act > ERROR_ACTION) // a shift-reduce action?
                        act = process_reductions(act - ERROR_ACTION);
                }
            }
        }

        return (Ast) parseStack[0];
    }

    //
    // Process reductions and continue...
    //
    int process_backtrack_reductions(int act)
    {
        do
        {
            stateStackTop -= (prs.rhs(act) - 1);
            act = prs.ntAction(stateStack[stateStackTop], prs.lhs(act));
        } while(act <= NUM_RULES);

        return act;
    }

    //
    // Parse the input until either the parse completes successfully or
    // an error is encountered. This function returns an integer that
    // represents the last action that was executed by the parser. If
    // the parse was succesful, then the tuple "action" contains the
    // successful sequence of actions that was executed.
    //
    int BacktrackParse()
    {
        int error_token = 0;
        int maxStackTop = 0;

        //
        // Get the first token.
        //
        tokStream.reset();
        int curtok = tokStream.getToken();

        //
        // Allocate configuration stack.
        //
        ConfigurationStack configuration_stack = new ConfigurationStack(prs);

        //
        // Reset the action. Allocate and initialize the state stack.
        // Keep parsing until we successfully reach the end of file or
        // an error is encountered. The list of actions executed will
        // be stored in the "action" tuple.
        //
        action.reset();
        int current_kind = tokStream.getKind(curtok);
        reallocateStateStack();
        stateStackTop = 0;
        stateStack[stateStackTop] = START_STATE;
        int act = tAction(stateStack[stateStackTop], current_kind);
        for (;;)
        {
            if (act <= NUM_RULES)
            {
                action.add(act); // save this reduce action
                stateStackTop--;
                act = process_backtrack_reductions(act);
            }
            else if (act > ERROR_ACTION)
            {
                action.add(act); // save this shift-reduce action
                curtok = tokStream.getToken();
                current_kind = tokStream.getKind(curtok);
                act = process_backtrack_reductions(act - ERROR_ACTION);
            }
            else if (act < ACCEPT_ACTION)
            {
                action.add(act); // save this shift action
                curtok = tokStream.getToken();
                current_kind = tokStream.getKind(curtok);
            }
            else if (act == ERROR_ACTION)
            {
                error_token = (error_token > curtok ? error_token : curtok);
                if (current_kind != EOFT_SYMBOL)
                {
                    ConfigurationElement configuration = configuration_stack.pop();
                    if (configuration == null)
                         act = ERROR_ACTION;
                    else
                    {
                        stateStackTop = configuration.stack_top;
                        configuration.retrieveStack(stateStack);
                        action.reset(configuration.action_length);
                        act = configuration.act;
                        curtok = configuration.curtok;
                        current_kind = tokStream.getKind(curtok);
                        tokStream.reset(tokStream.getNext(curtok));
                        continue;
                    }
                }
                break;
            }
            else if (act > ACCEPT_ACTION)
            {
                if (configuration_stack.findConfiguration(stateStack, stateStackTop, curtok))
                    act = ERROR_ACTION;
                else
                {
                    configuration_stack.push(stateStack, stateStackTop, act + 1, curtok, action.size());
                    act = prs.baseAction(act);
                    maxStackTop = stateStackTop > maxStackTop ? stateStackTop : maxStackTop;
                }
                continue;
            }
            else break; // assert(act == ACCEPT_ACTION);
            try
            {
                stateStack[++stateStackTop] = act;
            }
            catch(IndexOutOfBoundsException e)
            {
                reallocateStateStack();
                stateStack[stateStackTop] = act;
            }

            act = tAction(act, current_kind);
        }

        //System.out.println("****Number of configurations: " + configuration_stack.configurationSize());
        //System.out.println("****Number of elements in stack tree: " + configuration_stack.numStateElements());
        //System.out.println("****Number of elements in stacks: " + configuration_stack.stacksSize());
        //System.out.println("****Number of actions: " + action.size());
        //System.out.println("****Max Stack Size = " + maxStackTop);
        //System.out.flush();
        return (act == ERROR_ACTION ? error_token : 0);
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
