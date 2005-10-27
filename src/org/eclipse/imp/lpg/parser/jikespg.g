%options table=java,esc=$,la=3,em,scopes,fp=jikespg_,prefix=TK_

$Define ----------------------------------------------------------------

    --
    -- This macro generates a header for an action function consisting
    -- of the rule in question (commented) and a location directive.
    --
    $Location
    /.

        //
        // Rule $rule_number$:  $rule_text
        //
        #line $next_line "$input_file$"./

    --
    -- This macro is used to initialize the rule_action array
    -- to an unnamed function. A name is generated using the
    -- number of the rule in question.
    --
    $SetAction
    /:
            rule_action[$rule_number$] = &jikespg_act::Act$rule_number$;:/

    --
    -- This macro is used to initialize the rule_action array
    -- to the NoAction function.
    --
    $SetNoAction
    /:
            rule_action[$rule_number$] = &jikespg_act::NoAction;:/

    --
    -- This macro is used to declare an action function
    -- in the action class.
    --
    $DclVariableNameAction
        /!
            void MakeVariableName (void);
        !/
    
    --
    -- This macro is used to initialize the rule_action array
    -- with the MakeVariableName function.
    --
    $SetVariableNameAction
    /:
            rule_action[$rule_number$] = &jikespg_act::MakeVariableName ;:/

    --
    -- This macro is used for a rule that shares the VariableNameAction
    --
    $VariableNameAction
    /.$DclVariableNameAction $SetVariableNameAction $Location$./

    --
    -- This macro is used for a rule that shares the VariableNameAction
    --
    $ShareVariableNameAction
    /.$SetVariableNameAction

        //
        // Rule $rule_number$:  $rule_text
        //
        // void MakeVariableName(void);
        //./

    --
    -- Default name for a semantic action function.
    --
    $DefaultName /.Act$rule_number$./
    $DefaultHeader /.void jikespg_act::$DefaultName$()./

    --
    -- This macro is used to declare an action function
    -- in the action class.
    --
    $DclAction
        /!
            void $DefaultName$(void);
        !/
    
    $Action /.$DclAction $SetAction $Location$./
    
    --
    -- This macro generates a header for a rule that invokes the
    -- no_function routine.
    --
    $NoAction
    /.$SetNoAction

        //
        // Rule $rule_number$:  $rule_text
        //
        // void NoAction(void);
        //./


$End

$Terminals

    DROPSYMBOLS_KEY DROPRULES_KEY NOTICE_KEY DEFINE_KEY TERMINALS_KEY KEYWORDS_KEY EOL_KEY
    EOF_KEY ERROR_KEY IDENTIFIER_KEY ALIAS_KEY
    EMPTY_KEY START_KEY TYPES_KEY RULES_KEY NAMES_KEY END_KEY
    HEADERS_KEY TRAILERS_KEY EXPORT_KEY IMPORT_KEY INCLUDE_KEY
    MACRO_NAME SYMBOL BLOCK EQUIVALENCE PRIORITY_EQUIVALENCE
    ARROW PRIORITY_ARROW OR_MARKER

    EOF ERROR_SYMBOL

$End

$Alias

    '::='  ::= EQUIVALENCE
    '::=?' ::= PRIORITY_EQUIVALENCE
    '->'   ::= ARROW
    '->?'  ::= PRIORITY_ARROW
    '|'    ::= OR_MARKER
    $EOF   ::= EOF
    $ERROR ::= ERROR_SYMBOL

$End

$Names

    produces ::= '::='
    OR_MARKER ::= '|'

$End

$Start

    JikesPG_INPUT

$End

$Rules

    /!
        #line $next_line "$input_file$"
        #ifndef jikespg_act_INCLUDED
        #define jikespg_act_INCLUDED

        #include "Stacks.h"
        #include <assert.h>

        class Control;
        class Parser;
        class jikespg_act : public Stacks
        {
        public:
            //
            //
            //
            jikespg_act(Control *control_,
                        LexStream *lex_stream_,
                        VariableLookupTable *variable_table_,
                        MacroLookupTable *macro_table_);

            //
            //
            //
            int identifier_index,
                eol_index,
                eof_index,
                error_index,
                start_index;

            Tuple<int> terminals,
                       keywords,
                       exports;

            class AliasDefinition
            {
            public:
                int lhs_index,
                    rhs_index;
            };
            Tuple<AliasDefinition> aliases;

            class NameDefinition
            {
            public:
                int lhs_index,
                    rhs_index;
            };
            Tuple<NameDefinition> names;

            Tuple<int> notice_blocks,
                       header_blocks,
                       initial_blocks,
                       trailer_blocks;

            class RuleDefinition
            {
            public:
              int lhs_index,
                  classname_index,
                  separator_index,
                  end_rhs_index;
            };
            Tuple<RuleDefinition> rules,
                                  dropped_rules;

            class TypeDefinition
            {
            public:
              int type_index,
                  separator_index,
                  symbol_index;
            };
            Tuple<TypeDefinition> types;

        protected:

            Control *control;
            Option *option;
            LexStream *lex_stream;
            VariableLookupTable *variable_table;
            MacroLookupTable *macro_table;

            TokenObject curtok;

            //
            //
            //
            enum
            {
                MACRO_EXPECTED_INSTEAD_OF_SYMBOL,
                SYMBOL_EXPECTED_INSTEAD_OF_MACRO,
                RESPECIFICATION_OF_ERROR_SYMBOL,
                RESPECIFICATION_OF_IDENTIFIER_SYMBOL,
                RESPECIFICATION_OF_EOL_SYMBOL,
                RESPECIFICATION_OF_EOF_SYMBOL,
                RESPECIFICATION_OF_START_SYMBOL,
                RECURSIVE_IMPORT
            };
            void ReportError(int, int, int);

            void SetIdentifierIndex(int index)
            {
                if (identifier_index == 0)
                     identifier_index = index;
                else ReportError(RESPECIFICATION_OF_IDENTIFIER_SYMBOL, index, index);
            }

            void SetEolIndex(int index)
            {
                if (eol_index == 0)
                     eol_index = index;
                else ReportError(RESPECIFICATION_OF_EOL_SYMBOL, index, index);
            }

            void SetEofIndex(int index)
            {
                if (eof_index == 0)
                     eof_index = index;
                else ReportError(RESPECIFICATION_OF_EOF_SYMBOL, index, index);
            }

            void SetErrorIndex(int index)
            {
                if (error_index == 0)
                     error_index = index;
                else ReportError(RESPECIFICATION_OF_ERROR_SYMBOL, index, index);
            }

            void SetStartIndex(int index)
            {
                if (start_index == 0)
                     start_index = index;
                else ReportError(RESPECIFICATION_OF_START_SYMBOL, index, index);
            }

            bool Compare(RuleDefinition &, RuleDefinition &);
            void Merge(Parser &);

            void (jikespg_act::*rule_action[$NUM_RULES + 1]) ();
        
            void BadAction(void) { assert(false); }
        
            void NoAction(void) {}
    !/

    /:
        #line $next_line "$input_file$"
        #include "jikespg_act.h"
        #include "control.h"

        jikespg_act::jikespg_act(Control *control_,
                                 LexStream *lex_stream_,
                                 VariableLookupTable *variable_table_,
                                 MacroLookupTable *macro_table_)
                    : control(control_),
                      lex_stream(lex_stream_),
                      variable_table(variable_table_),
                      macro_table(macro_table_),

                      option(control_ -> option),

                      identifier_index(0),
                      start_index(0),
                      eol_index(0),
                      eof_index(0),
                      error_index(0)
        {
            rule_action[0] = &jikespg_act::BadAction;
    :/

    /.
        #include "jikespg_act.h"
        #include "control.h"
        #include "parser.h"
        #include "scanner.h"
        #include "symbol.h"

        #line $next_line "$input_file$"

        //
        // Compare the right hand-side of two rules and return true if they are identical.
        //
        bool jikespg_act::Compare(RuleDefinition &rule1, RuleDefinition &rule2)
        {
            //
            // Copy all symbols in the right-hand side of rule 1 to list1
            //
            Tuple<int> list1;
            for (int i = lex_stream -> Next(rule1.separator_index);
                 i < rule1.end_rhs_index;
                 i++)
            {
                if (lex_stream -> Kind(i) == TK_SYMBOL)
                    list1.Next() = i;
            }

            //
            // Copy all symbols in the right-hand side of rule 2 to list2
            //
            Tuple<int> list2;
            for (int i = lex_stream -> Next(rule2.separator_index);
                 i < rule2.end_rhs_index;
                 i++)
            {
                if (lex_stream -> Kind(i) == TK_SYMBOL)
                    list2.Next() = i;
            }

            //
            // Compare symbols in list1 to symbols in list2 and return true if they are the same.
            //
            if (list1.Length() == list2.Length())
            {
                int i;
                for (i = 0; i < list1.Length(); i++)
                {
                    int j = list1[i],
                        k = list2[i];
                    if ((lex_stream -> NameStringLength(j) != lex_stream -> NameStringLength(k)) ||
                        strcmp(lex_stream -> NameString(j), lex_stream -> NameString(k)) != 0)
                        break;
                }
                return (i == list1.Length());
            }

            return false;
        }

        //
        // Merge the information from an imported grammar with the
        // information for this grammar.
        //
        void jikespg_act::Merge(Parser &import)
        {
            //
            // Process drop list before adding rules
            //
            RuleLookupTable symbol_table,
                            rule_table;
            for (int i = 0; i < dropped_rules.Length(); i++)
            {
                //
                // If the separator index is 0, it is an indication
                // that all rules with this lhs_index should be dropped.
                //
                if (dropped_rules[i].separator_index == 0)
                    (void) symbol_table.FindOrInsertName(lex_stream -> NameString(dropped_rules[i].lhs_index),
                                                         lex_stream -> NameStringLength(dropped_rules[i].lhs_index));
                else
                {
                    RuleSymbol *rule_symbol = rule_table.FindOrInsertName(lex_stream -> NameString(dropped_rules[i].lhs_index),
                                                                          lex_stream -> NameStringLength(dropped_rules[i].lhs_index));
                    rule_symbol -> AddRule(i);
                }
            }

            //
            // Process special symbols.
            //
            if (import.identifier_index != 0)
                SetIdentifierIndex(import.identifier_index);
            if (import.eol_index != 0)
                SetEolIndex(import.eol_index);
            if (import.eof_index != 0)
                SetEofIndex(import.eof_index);
            if (import.error_index != 0)
                SetErrorIndex(import.error_index);
            if (import.start_index != 0)
                SetStartIndex(import.start_index);

            for (int i = 0; i < import.terminals.Length(); i++)
                terminals.Next() = import.terminals[i];

            for (int i = 0; i < import.keywords.Length(); i++)
                keywords.Next() = import.keywords[i];

            for (int i = 0; i < import.exports.Length(); i++)
                exports.Next() = import.exports[i];

            for (int i = 0; i < import.aliases.Length(); i++)
            {
                //
                // Allow aliasing only for names that have not been dropped.
                //
                if (! symbol_table.FindName(lex_stream -> NameString(import.aliases[i].lhs_index),
                                            lex_stream -> NameStringLength(import.aliases[i].lhs_index)))
                {
                    aliases.Next() = import.aliases[i];
                }
            }

            for (int i = 0; i < import.names.Length(); i++)
            {
                //
                // Assign name to all symbols that have not been dropped.
                //
                if (! symbol_table.FindName(lex_stream -> NameString(import.names[i].lhs_index),
                                            lex_stream -> NameStringLength(import.names[i].lhs_index)))
                {
                    names.Next() = import.names[i];
                }
            }

            for (int i = 0; i < import.notice_blocks.Length(); i++)
                notice_blocks.Next() = import.notice_blocks[i];

            for (int i = 0; i < import.header_blocks.Length(); i++)
                header_blocks.Next() = import.header_blocks[i];

            for (int i = 0; i < import.initial_blocks.Length(); i++)
                initial_blocks.Next() = import.initial_blocks[i];

            for (int i = 0; i < import.trailer_blocks.Length(); i++)
                trailer_blocks.Next() = import.trailer_blocks[i];

            for (int i = 0; i < import.types.Length(); i++)
            {
                //
                // Add type declaration for all symbols that have not been dropped.
                //
                if (! symbol_table.FindName(lex_stream -> NameString(import.types[i].symbol_index),
                                            lex_stream -> NameStringLength(import.types[i].symbol_index)))
                    types.Next() = import.types[i];
            }

            //
            // Add all imported rules that have not been dropped.
            //
            for (int i = 0; i < import.rules.Length(); i++)
            {
                 if (! symbol_table.FindName(lex_stream -> NameString(import.rules[i].lhs_index),
                                             lex_stream -> NameStringLength(import.rules[i].lhs_index)))
                 {
                     RuleSymbol *rule_symbol = rule_table.FindName(lex_stream -> NameString(import.rules[i].lhs_index),
                                                                   lex_stream -> NameStringLength(import.rules[i].lhs_index));
                     if (! rule_symbol)
                         rules.Next() = import.rules[i];
                     else
                     {
                         Tuple<int> &dr = rule_symbol -> Rules();
                         int k;
                         for (k = 0; k < dr.Length(); k++)
                         {
                             int dropped_rule_no = dr[k];
                             if (Compare(dropped_rules[dropped_rule_no], import.rules[i]))
                                 break;
                         }

                         if (k == dr.Length()) // not a dropped rule
                         {
                             int current_rule = rules.Length(),
                                 preceding_rule = current_rule - 1;

                             rules.Next() = import.rules[i];

                             //
                             // If the first rule in a sequence of rules containing
                             // alternations is dropped, we have to replace the alternate
                             // symbol by a production symbol in the first alternate rule in 
                             // the sequence that was not dropped.
                             //
                             if (lex_stream -> Kind(rules[current_rule].separator_index) == TK_OR_MARKER &&
                                 (preceding_rule < 0 || rules[preceding_rule].lhs_index != rules[current_rule].lhs_index))
                             {
                                 class Token *separator = lex_stream -> GetTokenReference(rules[current_rule].separator_index);
                                 separator -> SetKind(lex_stream -> Kind(lex_stream -> Next(rules[current_rule].lhs_index)));
                             }
                         }
                     }
                 }
            }
        }

    ./

    JikesPG_INPUT ::= $empty
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT include_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT notice_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT define_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT terminals_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT export_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT import_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT keywords_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT eof_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT eol_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT error_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT identifier_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT start_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT alias_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT names_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT headers_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT trailers_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT rules_segment [END_KEY]
        /.$NoAction./

    JikesPG_INPUT ::= JikesPG_INPUT types_segment [END_KEY]
        /.$NoAction./

    include_segment ::= INCLUDE_KEY
        /.$NoAction./

    include_segment ::= INCLUDE_KEY SYMBOL
        /.$NoAction./

    notice_segment ::= NOTICE_KEY
        /.$NoAction./

    notice_segment ::= notice_segment action_segment
        /.$Action
        $DefaultHeader
        {
            notice_blocks.Next() = Token(2);
            return;
        }
        ./

    define_segment ::= DEFINE_KEY
        /.$NoAction./

    define_segment ::= define_segment macro_name_symbol macro_segment 
        /.$Action
        $DefaultHeader
        {
            MacroSymbol *macro_symbol = lex_stream -> GetMacroSymbol(Token(2));

            assert(macro_symbol);

            macro_symbol -> SetBlock(Token(3));

            return;
        }
        ./

    macro_name_symbol ::= MACRO_NAME
        /.$NoAction./

    macro_name_symbol ::= SYMBOL -- warning: escape prefix missing...
        /.$Action
        $DefaultHeader
        {
            int length = lex_stream -> NameStringLength(Token(1)) + 1;
            char *macro_name = new char[length + 1];
            macro_name[0] = option -> escape;
            strcpy(macro_name + 1, lex_stream -> NameString(Token(1)));

            MacroSymbol *macro_symbol = macro_table -> FindName(macro_name, length);
            if (macro_symbol == NULL)
            {
                macro_symbol = macro_table -> InsertName(macro_name, length);
                ReportError(MACRO_EXPECTED_INSTEAD_OF_SYMBOL, Token(1), Token(1));
            }

            lex_stream -> GetTokenReference(Token(1)) -> SetSymbol(macro_symbol);

            delete [] macro_name;

            return;
        }
        ./

    macro_segment ::= BLOCK 
        /.$NoAction./

    terminals_segment ::= TERMINALS_KEY 
        /.$NoAction./

    terminals_segment ::= terminals_segment terminal_symbol
        /.$Action
        $DefaultHeader
        {
            terminals.Next() = Token(2);

            return;
        }
        ./

    terminals_segment ::= terminals_segment terminal_symbol produces name
        /.$Action
        $DefaultHeader
        {
            terminals.Next() = Token(2);

            int index = names.NextIndex();
            names[index].lhs_index = Token(2);
            names[index].rhs_index = Token(4);

            index = aliases.NextIndex();
            aliases[index].lhs_index = Token(4);
            aliases[index].rhs_index = Token(2);

            return;
        }
        ./

    export_segment ::= EXPORT_KEY 
        /.$NoAction./

    export_segment ::= export_segment terminal_symbol
        /.$Action
        $DefaultHeader
        {
            exports.Next() = Token(2);
            return;
        }
        ./

    import_segment ::= IMPORT_KEY 
        /.$NoAction./

    import_segment ::= IMPORT_KEY SYMBOL {drop_command}
        /.$Action
        $DefaultHeader
        {
            int current_index = lex_stream -> Peek(),
                start_index = lex_stream -> NumTokens();

            Scanner scanner(option, lex_stream, variable_table, macro_table);
            scanner.Scan(Token(2));

            if (lex_stream -> NumBadTokens() > 0)
                exit(12);
            else // file found and scanned with no errors?
            {
                //
                // If the file is already in the process of being included, issue an error and stop.
                //
                InputFileSymbol *import_file = lex_stream -> FindOrInsertFile(option -> include_search_directory,
                                                                              lex_stream -> NameString(Token(2)));
                assert(import_file);

                if (import_file -> IsLocked())
                {
                    ReportError(RECURSIVE_IMPORT, Token(2), Token(2));
                    option -> report.Print(Control::syslis);
                    option -> report.Flush(stdout);

                    exit(12);
                }
                else
                {
                    //
                    // Lock the include_file to avoid looping...
                    //
                    import_file -> Lock();

                    Parser parser(control, lex_stream, variable_table, macro_table);
                    parser.Parse(start_index);

                    Merge(parser);

                    import_file -> Unlock();
                }
            }

            dropped_rules.Reset();
            lex_stream -> Reset(current_index);

            return;
        }
        ./

    drop_command ::= drop_symbols
        /.$NoAction./
    drop_command ::= drop_rules
        /.$NoAction./

    drop_symbols ::= DROPSYMBOLS_KEY
        /.$NoAction./
    drop_symbols ::= drop_symbols SYMBOL
        /.$Action
        $DefaultHeader
        {
            int index = dropped_rules.NextIndex();
            dropped_rules[index].lhs_index = Token(2);
            dropped_rules[index].separator_index = 0;
            dropped_rules[index].end_rhs_index = 0;

            return;
        }
        ./

    drop_rules ::= DROPRULES_KEY
        /.$NoAction./
    drop_rules ::= drop_rules drop_rule
        /.$NoAction./

    drop_rule ::= SYMBOL produces rhs
        /.$Action
        $DefaultHeader
        {
            int index = dropped_rules.NextIndex();
            dropped_rules[index].lhs_index = Token(1);
            dropped_rules[index].separator_index = Token(2);
            dropped_rules[index].end_rhs_index = curtok;

            return;
        }
        ./
    drop_rule ::= SYMBOL MACRO_NAME produces rhs
        /.$Action
        $DefaultHeader
        {
            int index = dropped_rules.NextIndex();
            dropped_rules[index].lhs_index = Token(1);
            dropped_rules[index].separator_index = Token(3);
            dropped_rules[index].end_rhs_index = curtok;

            return;
        }
        ./
    drop_rule ::= drop_rule '|' rhs
        /.$Action
        $DefaultHeader
        {
            int index = dropped_rules.NextIndex();
            dropped_rules[index].lhs_index = dropped_rules[index - 1].lhs_index;
            dropped_rules[index].separator_index = Token(2);
            dropped_rules[index].end_rhs_index = curtok;

            return;
        }
        ./


    {drop_command} ::= $empty
        /.$NoAction./

    {drop_command} ::= {drop_command} drop_command
        /.$NoAction./

    keywords_segment ::= KEYWORDS_KEY
        /.$NoAction./

    keywords_segment ::= keywords_segment terminal_symbol
        /.$Action
        $DefaultHeader
        {
            keywords.Next() = Token(2);

            return;
        }
        ./

    keywords_segment ::= keywords_segment terminal_symbol produces name
        /.$Action
        $DefaultHeader
        {
            keywords.Next() = Token(2);

            int index = names.NextIndex();
            names[index].lhs_index = Token(2);
            names[index].rhs_index = Token(4);

            index = aliases.NextIndex();
            aliases[index].lhs_index = Token(4);
            aliases[index].rhs_index = Token(2);

            return;
        }
        ./

    error_segment ::= ERROR_KEY
        /.$NoAction./

    error_segment ::= ERROR_KEY terminal_symbol
        /.$Action
        $DefaultHeader
        {
            SetErrorIndex(Token(2));
        }
        ./

    identifier_segment ::= IDENTIFIER_KEY
        /.$NoAction./

    identifier_segment ::= IDENTIFIER_KEY terminal_symbol
        /.$Action
        $DefaultHeader
        {
            SetIdentifierIndex(Token(2));
        }
        ./

    eol_segment ::= EOL_KEY
        /.$NoAction./

    eol_segment ::= EOL_KEY terminal_symbol
        /.$Action
        $DefaultHeader
        {
            SetEolIndex(Token(2));
        }
        ./

    eof_segment ::= EOF_KEY
        /.$NoAction./

    eof_segment ::= EOF_KEY terminal_symbol
        /.$Action
        $DefaultHeader
        {
            SetEofIndex(Token(2));
        }
        ./

    terminal_symbol ::= SYMBOL
        /.$NoAction./

    terminal_symbol ::= MACRO_NAME -- warning: escape prefix used in symbol
        /.$VariableNameAction
        void jikespg_act::MakeVariableName (void)
        {
            char *variable_name = lex_stream -> NameString(Token(1));
            int length = lex_stream -> NameStringLength(Token(1));

            VariableSymbol *variable_symbol = variable_table -> FindName(variable_name, length);
            if (variable_symbol == NULL)
            {
                variable_symbol = variable_table -> InsertName(variable_name, length);
                ReportError(SYMBOL_EXPECTED_INSTEAD_OF_MACRO, Token(1), Token(1));
            }

            lex_stream -> GetTokenReference(Token(1)) -> SetSymbol(variable_symbol);

            return;
        }
        ./

    alias_segment ::= ALIAS_KEY
        /.$NoAction./

    alias_segment ::= alias_segment ERROR_KEY produces alias_rhs 
        /.$Action
        $DefaultHeader
        {
            SetErrorIndex(Token(4));
        }
        ./

    alias_segment ::= alias_segment EOL_KEY produces alias_rhs 
        /.$Action
        $DefaultHeader
        {
            SetEolIndex(Token(4));
        }
        ./

    alias_segment ::= alias_segment EOF_KEY produces alias_rhs 
        /.$Action
        $DefaultHeader
        {
            SetEofIndex(Token(4));
        }
        ./

    alias_segment ::= alias_segment IDENTIFIER_KEY produces alias_rhs 
        /.$Action
        $DefaultHeader
        {
            SetIdentifierIndex(Token(4));
        }
        ./

    alias_segment ::= alias_segment SYMBOL produces alias_rhs 
        /.$Action
        $DefaultHeader
        {
            int index = aliases.NextIndex();
            aliases[index].lhs_index = Token(2);
            aliases[index].rhs_index = Token(4);

            return;
        }
        ./

    alias_segment ::= alias_segment alias_lhs_macro_name produces alias_rhs 
        /.$Action
        $DefaultHeader
        {
            int index = aliases.NextIndex();
            aliases[index].lhs_index = Token(2);
            aliases[index].rhs_index = Token(4);

            return;
        }
        ./

    alias_lhs_macro_name ::= MACRO_NAME -- warning: escape prefix used in symbol
        /.$SetVariableNameAction./

    alias_rhs ::= SYMBOL 
        /.$NoAction./

    alias_rhs ::= MACRO_NAME -- warning: escape prefix used in symbol
        /.$ShareVariableNameAction./

    alias_rhs ::= ERROR_KEY 
        /.$NoAction./

    alias_rhs ::= EOL_KEY 
        /.$NoAction./

    alias_rhs ::= EOF_KEY 
        /.$NoAction./

    alias_rhs ::= EMPTY_KEY 
        /.$NoAction./

    alias_rhs ::= IDENTIFIER_KEY
        /.$NoAction./

    start_segment ::= START_KEY start_symbol
        /.$Action
        $DefaultHeader
        {
            SetStartIndex(Token(2));
        }
        ./

    headers_segment ::= HEADERS_KEY
        /.$NoAction./

    headers_segment ::= headers_segment action_segment
        /.$Action
        $DefaultHeader
        {
            header_blocks.Next() = Token(2);
            return;
        }
        ./

    trailers_segment ::= TRAILERS_KEY
        /.$NoAction./

    trailers_segment ::= trailers_segment action_segment
        /.$Action
        $DefaultHeader
        {
            trailer_blocks.Next() = Token(2);
            return;
        }
        ./

    start_symbol ::= SYMBOL 
        /.$NoAction./

    start_symbol ::= MACRO_NAME
        /.$NoAction./

    rules_segment ::= RULES_KEY {action_segment}
        /.$NoAction./

    rules_segment ::= rules_segment rules
        /.$NoAction./

    rules ::= SYMBOL produces rhs
        /.$Action
        $DefaultHeader
        {
            int index = rules.NextIndex();
            rules[index].lhs_index = Token(1);
            rules[index].classname_index = 0;
            rules[index].separator_index = Token(2);
            rules[index].end_rhs_index = curtok;

            return;
        }
        ./

    rules ::= SYMBOL MACRO_NAME produces rhs
        /.$Action
        $DefaultHeader
        {
            int index = rules.NextIndex();
            rules[index].lhs_index = Token(1);
            rules[index].classname_index = Token(2);
            rules[index].separator_index = Token(3);
            rules[index].end_rhs_index = curtok;

            return;
        }
        ./

    rules ::= rules '|' rhs
        /.$Action
        $DefaultHeader
        {
            int index = rules.NextIndex();
            rules[index].lhs_index = rules[index - 1].lhs_index;
            rules[index].classname_index = rules[index - 1].classname_index;
            rules[index].separator_index = Token(2);
            rules[index].end_rhs_index = curtok;

            return;
        }
        ./

    produces ::= '::='
        /.$NoAction./

    produces ::= '::=?'
        /.$NoAction./

    produces ::= '->'
        /.$NoAction./

    produces ::= '->?'
        /.$NoAction./

    rhs ::= $empty
        /.$NoAction./

    rhs ::= rhs SYMBOL
        /.$NoAction./

    rhs ::= rhs SYMBOL MACRO_NAME
        /.$NoAction./

    rhs ::= rhs EMPTY_KEY 
        /.$NoAction./

    rhs ::= rhs action_segment
        /.$NoAction./

    action_segment ::= BLOCK 
        /.$NoAction./

    types_segment ::= TYPES_KEY
        /.$NoAction./

    types_segment ::= types_segment type_declarations
        /.$NoAction./

    type_declarations ::= SYMBOL produces SYMBOL
        /.$Action
        $DefaultHeader
        {
            int index = types.NextIndex();
            types[index].type_index = Token(1);
            types[index].separator_index = Token(2);
            types[index].symbol_index = Token(3);

            return;
        }
        ./
                        | type_declarations '|' SYMBOL
        /.$Action
        $DefaultHeader
        {
            int index = types.NextIndex();
            types[index].type_index = types[index - 1].type_index;
            types[index].separator_index = Token(2);
            types[index].symbol_index = Token(3);

            return;
        }
        ./

    names_segment ::= NAMES_KEY
        /.$NoAction./

    names_segment ::= names_segment name produces name 
        /.$Action
        $DefaultHeader
        {
            int index = names.NextIndex();
            names[index].lhs_index = Token(2);
            names[index].rhs_index = Token(4);

            return;
        }
        ./

    name ::= SYMBOL
        /.$NoAction./

    name ::= MACRO_NAME -- warning: escape prefix used in symbol
        /.$ShareVariableNameAction./

    name ::= EMPTY_KEY 
        /.$NoAction./

    name ::= ERROR_KEY 
        /.$NoAction./

    name ::= EOL_KEY 
        /.$NoAction./

    name ::= IDENTIFIER_KEY
        /.$NoAction./

    [END_KEY] ::= $empty
        /.$NoAction./

    [END_KEY] ::= END_KEY 
        /.$NoAction./

    {action_segment} ::= $empty
        /.$NoAction./

    {action_segment} ::= {action_segment} action_segment 
        /.$Action
        $DefaultHeader
        {
            initial_blocks.Next() = Token(2);
            return;
        }
        ./

    ----------------------------------------------------------------------

    /!
        #line $next_line "$input_file"
        
        };
        #endif
    !/
        
    /:

        #line $next_line "$input_file"

            return;
        }
    :/

    /.

        #line $next_line "$input_file"
        #include <iostream>

        using namespace std;

        //
        //
        //
        void jikespg_act::ReportError(int msg_code, int left_token_index, int right_token_index)
        {
            int left_line_no    = lex_stream -> Line(left_token_index),
                left_column_no  = lex_stream -> Column(left_token_index),
                right_line_no   = lex_stream -> EndLine(right_token_index),
                right_column_no = lex_stream -> EndColumn(right_token_index);

            option -> report.Put(lex_stream -> FileName(left_token_index));
            option -> report.PutChar(':');
            option -> report.Put(left_line_no);
            option -> report.PutChar(':');
            option -> report.Put(left_column_no);
            option -> report.PutChar(':');
            option -> report.Put(right_line_no);
            option -> report.PutChar(':');
            option -> report.Put(right_column_no);
            option -> report.Put(": ");

            switch(msg_code)
            {
                case MACRO_EXPECTED_INSTEAD_OF_SYMBOL:
                     option -> report.Put("A macro name was expected here instead of a grammar symbol name");
                     break;
                case SYMBOL_EXPECTED_INSTEAD_OF_MACRO:
                     option -> report.Put("A grammar symbol name was expected instead of a macro name");
                     break;
                case RESPECIFICATION_OF_ERROR_SYMBOL:
                     option -> report.Put("Illegal respecification of the error symbol");
                     break;
                case RESPECIFICATION_OF_IDENTIFIER_SYMBOL:
                     option -> report.Put("Illegal respecification of the identifier symbol");
                     break;
                case RESPECIFICATION_OF_EOL_SYMBOL:
                     option -> report.Put("Illegal respecification of the eol symbol");
                     break;
                case RESPECIFICATION_OF_EOF_SYMBOL:
                     option -> report.Put("Illegal respecification of the eof symbol");
                     break;
                case RESPECIFICATION_OF_START_SYMBOL:
                     option -> report.Put("Illegal respecification of the start symbol");
                     break;
                case RECURSIVE_IMPORT:
                     option -> report.Put("Attempt to recursively include file ");
                     option -> report.Put(lex_stream -> NameString(Token(2)));
                     break;
                default:
                     assert(false);
            }

            option -> report.PutChar('\n');

            return;
        }
    ./
$End
