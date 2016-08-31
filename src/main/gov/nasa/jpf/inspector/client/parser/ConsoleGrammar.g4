//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

grammar ConsoleGrammar;
options {
    language=Java;
}
@header{
package gov.nasa.jpf.inspector.client.parser.generated;
import gov.nasa.jpf.inspector.interfaces.*;
import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.CallbackKind;
import gov.nasa.jpf.inspector.client.*;
import gov.nasa.jpf.inspector.client.commands.*;
import gov.nasa.jpf.inspector.client.commands.CmdRun.CmdRunTypes;
import gov.nasa.jpf.inspector.common.BreakpointCreationExpression;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
import gov.nasa.jpf.inspector.utils.parser.RecognitionRuntimeException;
}

// *******************************
// Parser rules
// *******************************

// Identifies of keyword as text
anyWord returns [String text]
    : IDF                           { $text = $IDF.text; }
    | allKeyWords                   { $text = $allKeyWords.text; }
    ;
allKeyWords returns [String text]
    : a=TOKEN_HIT_COUNT             { $text = $a.text; }
    | allKeyWordsWithoutHitCountBP  { $text = $allKeyWordsWithoutHitCountBP.text; }
    ;
allKeyWordsWithoutHitCountBP returns [String text]
    : a=TOKEN_NAME                  { $text = $a.text; }
    | a=TOKEN_STATE                 { $text = $a.text; }
    | allKeyWordsWithoutCreateBPandHitCount { $text = $allKeyWordsWithoutCreateBPandHitCount.text; }
    ;
allKeyWordsWithoutCreateBPandHitCount returns [String text]
    : a=TOKEN_ALL { $text = $a.text; }
    | a=TOKEN_ASK { $text = $a.text; }
    | a=TOKEN_ASSERT { $text = $a.text; }
    | a=TOKEN_BREAK { $text = $a.text; }
    | a=TOKEN_BREAKPOINT { $text = $a.text; }
    | a=TOKEN_CHOICE_GENERATORS { $text = $a.text; }
    | a=TOKEN_CONTINUE { $text = $a.text; }
    | a=TOKEN_CLEAR { $text = $a.text; }
    | a=TOKEN_CREATE { $text = $a.text; }
    | a=TOKEN_DATA { $text = $a.text; }
    | a=TOKEN_DELETE { $text = $a.text; }
    | a=TOKEN_DISABLE { $text = $a.text; }
    | a=TOKEN_E { $text = $a.text; }
    | a=TOKEN_ENABLE { $text = $a.text; }
    | a=TOKEN_EXECUTE { $text = $a.text; }
    | a=TOKEN_LOG { $text = $a.text; }
    | a=TOKEN_PRINT { $text = $a.text; }
    | a=TOKEN_RECORD { $text = $a.text; }
    | a=TOKEN_RUN { $text = $a.text; }
    | a=TOKEN_RUNNING { $text = $a.text; }
    | a=TOKEN_SAVE { $text = $a.text; }
    | a=TOKEN_SET { $text = $a.text; }
    | a=TOKEN_SELECT { $text = $a.text; }
    | a=TOKEN_SCHEDULING { $text = $a.text; }
    | a=TOKEN_SHOW { $text = $a.text; }
    | a=TOKEN_STARTED { $text = $a.text; }
    | a=TOKEN_STOPPED { $text = $a.text; }
    | a=TOKEN_USED { $text = $a.text; }
    | a=TOKEN_TERMINATING { $text = $a.text; }
    | a=TOKEN_THREAD { $text = $a.text; }
    | a=TOKEN_THREAD_PC { $text = $a.text; }
    | a=TOKEN_X { $text = $a.text; }
    | a=TOKEN_STEP_INSTRUCTION { $text = $a.text; }
    | a=TOKEN_STEP_OVER { $text = $a.text; }
    | a=TOKEN_STEP_IN { $text = $a.text; }
    | a=TOKEN_STEP_OUT { $text = $a.text; }
    | a=TOKEN_STEP_TRANSITION { $text = $a.text; }
    | a=TOKEN_BACK_STEP_INSTRUCTION { $text = $a.text; }
    | a=TOKEN_BACK_STEP_OVER { $text = $a.text; }
    | a=TOKEN_BACK_STEP_IN { $text = $a.text; }
    | a=TOKEN_BACK_STEP_OUT { $text = $a.text; }
    | a=TOKEN_BACK_STEP_TRANSITION { $text = $a.text; }
    | a=TOKEN_BACK_FIELD_ACCESS { $text = $a.text; }
    | a=TOKEN_BACK_BREAKPOINT_HIT { $text = $a.text; }
    | a=TOKEN_CB_STATE_CHANGE { $text = $a.text; }
    | a=TOKEN_CB_GENERIC_ERROR { $text = $a.text; }
    | a=TOKEN_CB_GENERIC_INFO { $text = $a.text; }
    | a=TOKEN_CB_BREAKPOINT_HIT { $text = $a.text; }
    | a=TOKEN_CB_CG_NEW_CHOICE { $text = $a.text; }
    | a=TOKEN_CB_CG_CHOICE_TO_USE { $text = $a.text; }
    | a=TOKEN_CB_CG_USED_CHOICE { $text = $a.text; }
    | a=TOKEN_QUIT { $text = $a.text; }
    | a=TOKEN_HELLO { $text = $a.text; }
    | a=TOKEN_HELP { $text = $a.text; }
    | a=TOKEN_WAIT { $text = $a.text; }
    | a=TOKEN_FINISH { $text = $a.text; }
    | a=TOKEN_TERMINATE { $text = $a.text; }
    | a=TOKEN_BOTH { $text = $a.text; }
    | a=TOKEN_CHANGE { $text = $a.text; }
    | a=TOKEN_SET_ATTR { $text = $a.text; }
    | a=TOKEN_PRINT_ATTR { $text = $a.text; }
    ;

// Can parse all Client commands as well as text representation of Callbacks - used for Record&Replay approach
clientCommandWithCB returns [ClientCommand value]
    : clientCommands { $value = $clientCommands.value; } EOF
    | WS? cmdCallback WS? { $value = $cmdCallback.value; }
    ;
clientCommands returns [ClientCommand value]
    : WS? clientCommands1 EOF { $value = $clientCommands1.value; }
    ;
clientCommands1 returns [ClientCommand value]

    : left=clientCommands1 WS? SIGN_AMPERSAND_AMPERSAND WS? right=clientCommands1 WS?{ $value = new CmdThen($left.value, $right.value); }
       // the "then" command (&&); recursive, thus must be here and not in a sub-nonterminal

    | cmdBreakpoints         WS? { $value = $cmdBreakpoints.value; }
    | cmdSingleSteps         WS? { $value = $cmdSingleSteps.value; }
    | cmdInformational        WS? { $value = $cmdInformational.value; }
    | cmdChoiceGenerators    WS? { $value = $cmdChoiceGenerators.value; }
    | cmdRecord              WS? { $value = $cmdRecord.value; }
    | cmdAssertions          WS? { $value = $cmdAssertions.value; }
    | cmdExecution           WS? { $value = $cmdExecution.value; }
    | cmdCustom              WS? { $value = $cmdCustom.value; }  // Must be the last item because this is a catch-all.
    ;
cmdExecution returns [ClientCommand value]
  : TOKEN_RUN              WS? { $value = new CmdRun(CmdRunTypes.RUN, "run"); }
  | TOKEN_CONTINUE         WS? { $value = new CmdRun(CmdRunTypes.RUN, "continue"); }
  | TOKEN_BREAK            WS? { $value = new CmdRun(CmdRunTypes.STOP, "break"); }
  | TOKEN_QUIT             WS? { $value = new CmdQuit(); }
  | TOKEN_TERMINATE   { $value = new CmdTerminate(); }
  | TOKEN_WAIT   { $value = new CmdWait(); }
  | TOKEN_FINISH   { $value = new CmdFinish(); }
  | TOKEN_SET        WS? allText     { $value = new CmdSet($allText.text); }
  | TOKEN_SET_ATTR   WS? allText     { $value = new CmdSetAttr($allText.text); }
  ;
cmdInformational returns [ClientCommand value]
  : TOKEN_THREAD    (WS? intValue)?   { $value = new CmdStatusThreads($intValue.ctx != null ? $intValue.value : null); }
  | TOKEN_PRINT     (WS? allText)?    { $value = new CmdPrint($allText.text != null ? $allText.text : ""); }
  | TOKEN_PRINT_ATTR (WS? allText)?   { $value = new CmdPrintAttr($allText.text != null ? $allText.text : ""); }
  | TOKEN_THREAD_PC (WS? intValue)?   { $value = new CmdThreadsPC($intValue.ctx != null ? $intValue.value : null); }
  | TOKEN_HELLO  { $value = new CmdHello(); }
  | TOKEN_HELP   { $value = new CmdHelp(); }
  ;

cmdCustom returns [ClientCommand value]
  : name=IDF { $value = new CmdCustomCommand($name.text, ""); }
  | name=IDF WS arguments=allText { $value = new CmdCustomCommand($name.text, $arguments.text); }
  ;

cmdBreakpoints returns [ClientCommand value]
    : TOKEN_SHOW   WS? TOKEN_BREAKPOINT             { $value = new CmdBreakpointShow(); }
    | TOKEN_DELETE WS? TOKEN_BREAKPOINT WS? INT     { $value = new CmdBreakpointDelete($INT.text); }
    | TOKEN_CREATE WS? TOKEN_BREAKPOINT
    { BreakpointCreationExpression  bpCreate = new BreakpointCreationExpression(); } ( WS? cmdCreateBP[bpCreate])* WS bpExpression
     { bpCreate.setBPExpression($bpExpression.expr); $value = new CmdBreakpointCreate(bpCreate); }
    | cmdChangeBreakpoint                           { $value = $cmdChangeBreakpoint.value; }
    ;

cmdChangeBreakpoint returns [ClientCommand value]
  : TOKEN_ENABLE WS? TOKEN_BREAKPOINT WS? INT { $value = CmdBreakpointChange.createEnableBreakpointCommand($INT.text); }
  | TOKEN_DISABLE WS? TOKEN_BREAKPOINT WS? INT { $value = CmdBreakpointChange.createDisableBreakpointCommand($INT.text); }
  | TOKEN_CHANGE WS? TOKEN_BREAKPOINT WS? INT WS? TOKEN_STATE WS? SIGN_EQUAL WS? cmdBreakpointState
  { $value = CmdBreakpointChange.createChangeBreakpointCommand($INT.text, $cmdBreakpointState.bpState); }
  ;

cmdCreateBP [BreakpointCreationExpression bpCreate]
    : TOKEN_NAME  WS? SIGN_EQUAL WS? anyWord { $bpCreate.setName($anyWord.text); }
    | TOKEN_STATE WS? SIGN_EQUAL WS? cmdBreakpointState { $bpCreate.setState($cmdBreakpointState.bpState); }
    | (lower=intValue  WS? signLess=LESS WS?)? TOKEN_HIT_COUNT (WS? signHigh=LESS WS? upper=intValue)?
     {
        $bpCreate.setBounds(
            $lower.ctx != null ? $lower.value : null,
            $signLess != null ? $signLess.text : null,
            $signHigh != null ? $signHigh.text : null,
            $upper.ctx != null ? $upper.value : null
            );
     }
    ;

cmdBreakpointState returns [BreakpointState bpState]
    : TOKEN_DISABLE         { $bpState = BreakpointState.DISABLED; }
    | TOKEN_LOG             { $bpState = BreakpointState.LOGGING; }
    | TOKEN_ENABLE          { $bpState = BreakpointState.ENABLED; }
    ;

// We have to solve collision between bpExpression and "hitCountExpression" expression
// Note: Not precise, however due to structure of the bpExpressions it is enough
// Special handling of "name" "hitCount" "state" and  '+' '-' INT HEX

bpExpression returns [String expr]
    : allKeyWordsWithoutCreateBPandHitCount     b01=allTextWS?        { $expr = $allKeyWordsWithoutCreateBPandHitCount.text +  ($b01.text!=null?$b01.text:""); }
    | allNonKeywordsRulesBase                   b02=allTextWS?        { $expr = $allNonKeywordsRulesBase.text +                ($b02.text!=null?$b02.text:""); }
    | IDF                                       b03=allTextWS?        { $expr = $IDF.text +                                    ($b03.text!=null?$b03.text:""); }
    ;

cmdSingleSteps returns [CmdSingleStepping value]
    : TOKEN_STEP_INSTRUCTION                      (WS intValue)?
    { $value = new CmdSingleStepping(true, StepType.ST_INSTRUCTION,   $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_STEP_OVER                            (WS intValue)?
     { $value = new CmdSingleStepping(true, StepType.ST_LINE,          $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_STEP_IN                               (WS intValue)?
    { $value = new CmdSingleStepping(true, StepType.ST_STEP_IN,       $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_STEP_OUT                              (WS intValue)?
    { $value = new CmdSingleStepping(true, StepType.ST_STEP_OUT,      $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_STEP_TRANSITION (WS? c=cgType)?       (WS intValue)?
    { $value = CmdSingleStepping.createCmdSingleSteppingTransition(true, $c.ctx != null ? $c.cgsType : null, $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_BACK_STEP_INSTRUCTION                 (WS intValue)?
    { $value = new CmdSingleStepping(false, StepType.ST_INSTRUCTION,  $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_BACK_STEP_OVER                       (WS intValue)?
    { $value = new CmdSingleStepping(false, StepType.ST_LINE,         $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_BACK_STEP_IN                          (WS intValue)?
    { $value = new CmdSingleStepping(false, StepType.ST_STEP_IN,      $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_BACK_STEP_OUT                         (WS intValue)?
    { $value = new CmdSingleStepping(false, StepType.ST_STEP_OUT,     $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_BACK_STEP_TRANSITION (WS? c=cgType)? (WS intValue)?
    { $value = CmdSingleStepping.createCmdSingleSteppingTransition(false, $c.ctx != null ? $c.cgsType : null, $intValue.ctx != null ? $intValue.value : 1); }
    | TOKEN_BACK_BREAKPOINT_HIT
    { $value = CmdSingleStepping.createBackBreakpointHit(); }
    | TOKEN_BACK_FIELD_ACCESS (WS? field=allTextNoWS)
    { $value = CmdSingleStepping.createBackFieldAccess($field.expr); }
    ;





cmdChoiceGenerators returns [ClientCommand value]
    : TOKEN_USED WS? TOKEN_CHOICE_GENERATORS { $value = new CmdUsedChoiceGenerators(); }
    | a=enableOrDisable  WS? (b=cgMode WS?)? (c=cgType WS?)? TOKEN_CHOICE_GENERATORS
     {
      $value = new CmdChoiceGeneratorsTracking(
      ($c.ctx !=null ? $c.cgsType : CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_ALL),
      ($b.ctx != null ? $b.cg_mode : ChoiceGeneratorsInterface.CGMode.CG_MODE_PRINT),
      $a.value);
      }
    | TOKEN_CHOICE_GENERATORS WS? TOKEN_SELECT cgChoice { $value = new CmdChoiceSelect($cgChoice.choice); }
    | TOKEN_ENABLE WS? TOKEN_THREAD WS? intValue { $value = CmdEnableThread.createEnableThreadCommand($intValue.value); }
    | TOKEN_DISABLE WS? TOKEN_THREAD WS? intValue { $value = CmdEnableThread.createDisableThreadCommand($intValue.value); }
    | TOKEN_SHOW WS? TOKEN_CHOICE_GENERATORS { $value = new CmdShowChoiceGenerators(); }
    ;

cgType returns [CmdChoiceGeneratorsTracking.CGTypeSpec cgsType]
    : TOKEN_SCHEDULING   { $cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_SCHEDULING; }
    | TOKEN_DATA         { $cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_DATA; }
    | TOKEN_ALL          { $cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_ALL; }
    | TOKEN_BOTH          { $cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_ALL; }
    ;

cgMode returns [ChoiceGeneratorsInterface.CGMode cg_mode] 
    : TOKEN_ASK      { $cg_mode =  ChoiceGeneratorsInterface.CGMode.CG_MODE_ASK; }
    | TOKEN_PRINT    { $cg_mode =  ChoiceGeneratorsInterface.CGMode.CG_MODE_PRINT; }
    ;

enableOrDisable returns [boolean value]
    : TOKEN_ENABLE   { $value = true; }
    | TOKEN_DISABLE  { $value = false; }
    ;

cgChoice returns [int choice]
    :                 { $choice = CmdChoiceSelect.USE_CURRENT_CHOICE; }
    | WS? intValue    { $choice = $intValue.value; }
    | WS? '*'         { $choice = ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE; }
    ;

cmdCallback returns [ClientCommand value]
    : TOKEN_CB_STATE_CHANGE                                                { $value = new CmdCallback(CallbackKind.CB_STATE_CHANGE); }
    | TOKEN_CB_STATE_CHANGE WS? TOKEN_STATE WS? '=' WS? inspectorState     { $value = new CmdCallback($inspectorState.state); }
    | TOKEN_CB_GENERIC_ERROR                                               { $value = new CmdCallback(CallbackKind.CB_GENERIC_ERROR); }
    | TOKEN_CB_GENERIC_INFO                                                 { $value = new CmdCallback(CallbackKind.CB_GENERIC_INFO); }
    | TOKEN_CB_BREAKPOINT_HIT                                              { $value = new CmdCallback(CallbackKind.CB_BREAKPOINT_HIT); }
    | TOKEN_CB_CG_NEW_CHOICE                                               { $value = new CmdCallback(CallbackKind.CB_CG_NEW_CHOICE); }
    | TOKEN_CB_CG_CHOICE_TO_USE                                            { $value = new CmdCallback(CallbackKind.CB_CG_CHOICE_TO_USE); }
    | TOKEN_CB_CG_USED_CHOICE                                              { $value = new CmdCallback(CallbackKind.CB_CG_USED_CHOICE); }
    ;

inspectorState returns [InspectorStatusChange state]
    : TOKEN_RUNNING         { $state = InspectorStatusChange.JPF_RUNNING; }
    | TOKEN_STARTED         { $state = InspectorStatusChange.JPF_STARTED; }
    | TOKEN_STOPPED         { $state = InspectorStatusChange.JPF_STOPPED; }
    | TOKEN_TERMINATING     { $state = InspectorStatusChange.JPF_TERMINATING; }
    ;
    
cmdRecord returns [ClientCommand value]
    : TOKEN_RECORD WS TOKEN_CLEAR                  { $value = new CmdRecordClear(); }
    | TOKEN_RECORD WS TOKEN_PRINT                  { $value = new CmdRecordPrint(); }
    | TOKEN_RECORD WS TOKEN_SAVE    WS allText     { $value = new CmdRecordSave($allText.expr); }
    | TOKEN_RECORD WS TOKEN_EXECUTE WS allText     { $value = new CmdRecordExecute($allText.expr); }
    ;

cmdAssertions returns [ClientCommand value]
    : TOKEN_ASSERT     WS a=allTextNoWS  WS b=allText
       { $value = new CmdAssert($a.expr, $b.expr); }
    ;

// TOKEN_PRINT is defined cmdProgramStateText
signs : SIGN_DOLLAR | SIGN_DOT | SIGN_EQUAL | SIGN_HASH | SIGN_COLON | SIGN_LSBRA | SIGN_RSBRA | SIGN_ASTERISK | SIGN_SLASH | SIGN_BACKSLASH ;


intValue returns [Integer value]
    : SIGN_PLUS? INT { $value =  Integer.valueOf($INT.text); }
    | SIGN_MINUS INT { $value = -Integer.valueOf($INT.text); }
    | SIGN_PLUS? HEX { $value =  Integer.valueOf(($HEX.text).substring(2), 16); }
    | SIGN_MINUS HEX { $value = -Integer.valueOf(($HEX.text).substring(2), 16); }
    ;


allText returns [String expr]
    : anyWord                                   b02=allTextWS?     { $expr = $anyWord.text +                            ($b02.text!=null?$b02.text:""); }
    | allNonKeywordsRules                       b03=allTextWS?     { $expr = $allNonKeywordsRules.text +                ($b03.text!=null?$b03.text:""); }
    ;

allTextNoWS returns [String expr]
    : anyWord                                   b02=allTextNoWS?     { $expr = $anyWord.text +                            ($b02.text!=null?$b02.text:""); }
    | allNonKeywordsRules                       b03=allTextNoWS?     { $expr = $allNonKeywordsRules.text +                ($b03.text!=null?$b03.text:""); }
    ;

allTextWS returns [String expr]
    : (a=WS?) allText    { $expr = ($a.text!=null?$a.text:"") + $allText.expr; }
    ;

//Non "IDF" like rules
allNonKeywordsRules
    : allNonKeywordsRulesBase
    | LESS
    | HIGH
    | SIGN_PLUS
    | SIGN_MINUS
    | INT
    | HEX
    ;

allNonKeywordsRulesBase
    : signs
    | specialChar
    ;




specialChar : SPECIAL_CHAR ;

// *******************************
// Lexer rules
// *******************************
// (In ANTLR4, all lexer rules must be after all parser rules or a syntax error
// ("expecting ARG_ACTION while matching a rule") will trigger.

// Keywords and commands
TOKEN_ALL : 'all' ;
TOKEN_ASK : 'ask';
TOKEN_ASSERT : 'assert' ;
TOKEN_BOTH : 'both';
TOKEN_BREAK : 'break' ;
TOKEN_BREAKPOINT : 'breakpoint' | 'bp' ;
TOKEN_CHOICE_GENERATORS : 'choice_generators' | 'cg' ;
TOKEN_CHANGE : 'change';
TOKEN_CONTINUE : 'continue' | 'cont' ;
TOKEN_CLEAR : 'clear' ;
TOKEN_CREATE : 'create' | 'cr' ;
TOKEN_DATA : 'data' ;
TOKEN_DELETE : 'delete' | 'del' ;
TOKEN_DISABLE : 'disable' | 'dis' ;
TOKEN_E : 'e' | 'E' ;
TOKEN_ENABLE : 'enable' | 'en' ;
TOKEN_EXECUTE : 'execute' | 'ex' ;
TOKEN_HIT_COUNT : 'hit_count' | 'hc' ;
TOKEN_LOG : 'log' ;
TOKEN_NAME : 'name' ;
TOKEN_PRINT : 'print' ;
TOKEN_PRINT_ATTR : 'print_attr';
TOKEN_RECORD : 'record' | 'rec' ;
TOKEN_RUN : 'run' ;
TOKEN_RUNNING : 'running' ;
TOKEN_SAVE : 'save' ;
TOKEN_SELECT : 'select' | 'sel' ;
TOKEN_SET : 'set' ;
TOKEN_SET_ATTR : 'set_attr' | 'seta';
TOKEN_SCHEDULING : 'scheduling' | 'sched' ;
TOKEN_SHOW : 'show' | 'sw' ;
TOKEN_STARTED : 'started' ;
TOKEN_STATE : 'state' ;
TOKEN_STOPPED : 'stopped' ;
TOKEN_USED : 'used' ;
TOKEN_TERMINATING : 'terminating' ;
TOKEN_THREAD : 'thread' | 'ti'  | 't';
TOKEN_THREAD_PC : 'thread_pc' | 'thpc' ;
TOKEN_X : 'x' | 'X' ;

// Stepping-related keywords
TOKEN_STEP_INSTRUCTION : 'step_instruction' | 'sins' ;
TOKEN_STEP_OVER : 'step_over' | 'so' ;
TOKEN_STEP_IN : 'step_in' | 'si' ;
TOKEN_STEP_OUT : 'step_out' | 'sout' ;
TOKEN_STEP_TRANSITION : 'step_transition' | 'st' ;
TOKEN_BACK_STEP_INSTRUCTION : 'back_step_instruction' | 'bsins' ;
TOKEN_BACK_STEP_OVER : 'back_step_over' | 'bso' ;
TOKEN_BACK_STEP_IN : 'back_step_in' | 'bsi' ;
TOKEN_BACK_STEP_OUT : 'back_step_out' | 'bsout' ;
TOKEN_BACK_STEP_TRANSITION : 'back_step_transition' | 'bst' ;
TOKEN_BACK_BREAKPOINT_HIT : 'back_breakpoint_hit' | 'bbhit';
TOKEN_BACK_FIELD_ACCESS : 'back_field_access' | 'bfa';

// Callback-related keywords
TOKEN_CB_STATE_CHANGE : 'callback_state_change' | 'cb_state_changed' ;
TOKEN_CB_GENERIC_ERROR : 'callback_generic_error' | 'cb_gen_error' ;
TOKEN_CB_GENERIC_INFO : 'callback_generic_info' | 'cg_gen_info' ;
TOKEN_CB_BREAKPOINT_HIT : 'callback_breakpoint_hit' | 'cb_bp_hit' ;
TOKEN_CB_CG_NEW_CHOICE : 'callback_choice_generator_new_choice' | 'cb_cg_new_choice' ;
TOKEN_CB_CG_CHOICE_TO_USE : 'callback_choice_generator_choice_to_use' | 'cb_cg_choice_to_use' ;
TOKEN_CB_CG_USED_CHOICE : 'callback_choice_generator_used_choice' | 'cb_cg_used_choice' ;

// New commmands
TOKEN_HELLO : 'hello';
TOKEN_HELP : 'help';
TOKEN_WAIT : 'wait';
TOKEN_FINISH : 'finish';
TOKEN_TERMINATE : 'terminate';
TOKEN_QUIT : 'quit' | 'exit';

// End of keywords.


SIGN_AMPERSAND_AMPERSAND : '&&';
SIGN_EQUAL : '=' ;
SIGN_DOLLAR : '$' ;
SIGN_DOT : '.' ;
SIGN_HASH : '#' ;
SIGN_COLON : ':';
SIGN_LSBRA : '[' ;
SIGN_RSBRA : ']' ;
SIGN_ASTERISK : '*' ;
SIGN_SLASH : '/' ;
SIGN_BACKSLASH : '\\' ;
SIGN_PLUS : '+' ;
SIGN_MINUS : '-' ;

LESS : '<' | '<=' ;
HIGH : '>' | '>=' ;


IDF :   ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '0'..'9' | '_')* ;

HEX : '0' TOKEN_X ('0'..'9'|'A'..'F'|'a'..'f')+;
INT : ('0'..'9')+ ;


// Whitespace
WS     :   (' '|'\n'|'\r'|'\t')+ ;

// Any characters not matched before
SPECIAL_CHAR : . ;
