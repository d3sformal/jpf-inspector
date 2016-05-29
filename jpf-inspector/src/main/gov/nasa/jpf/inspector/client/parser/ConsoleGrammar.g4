//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  
grammar ConsoleGrammar;
options {
    language=Java;
}
@header{
package gov.nasa.jpf.inspector.client.parser;
import gov.nasa.jpf.inspector.interfaces.*;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks.CB_METHODS;
import gov.nasa.jpf.inspector.client.*;
import gov.nasa.jpf.inspector.client.commands.*;
import gov.nasa.jpf.inspector.client.commands.CmdRun.CmdRunTypes;
import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate.ConsoleBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
import gov.nasa.jpf.inspector.utils.parser.RecognitionRuntimeException;
}
/*@lexer::header {
package gov.nasa.jpf.inspector.client.parser;
}*//*
@members {

    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames); 
        throw new RecognitionRuntimeException(hdr + " " + msg, e);
    }

}*/

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
    : TOKEN_ALL
    | TOKEN_ASK
    | TOKEN_ASSERT
    | TOKEN_ASSERTIONS
    | TOKEN_BREAK
    | TOKEN_BREAKPOINT
    | TOKEN_CHOICE_GENERATORS
    | TOKEN_CONTINUE
    | TOKEN_CLEAR
    | TOKEN_CREATE
    | TOKEN_DATA
    | TOKEN_DELETE
    | TOKEN_DISABLE
    | TOKEN_E
    | TOKEN_ENABLE
    | TOKEN_EXECUTE
    | TOKEN_LOG
    | TOKEN_PRINT
    | TOKEN_RECORD
    | TOKEN_RUN
    | TOKEN_RUNNING
    | TOKEN_SAVE
    | TOKEN_SET
    | TOKEN_SELECT
    | TOKEN_SCHEDULING
    | TOKEN_SHOW
    | TOKEN_STARTED
    | TOKEN_STOPPED
    | TOKEN_USED
    | TOKEN_TERMINATING
    | TOKEN_THREAD
    | TOKEN_THREAD_PC
    | TOKEN_X
    | TOKEN_STEP_INSTRUCTION
    | TOKEN_STEP_OVER
    | TOKEN_STEP_IN
    | TOKEN_STEP_OUT
    | TOKEN_STEP_TRANSITION
    | TOKEN_BACK_STEP_INSTRUCTION
    | TOKEN_BACK_STEP_OVER
    | TOKEN_BACK_STEP_IN
    | TOKEN_BACK_STEP_OUT
    | TOKEN_BACK_STEP_TRANSITION
    | TOKEN_CB_STATE_CHANGE
    | TOKEN_CB_GENERIC_ERROR
    | TOKEN_CB_GENERIC_INFO
    | TOKEN_CB_BREAKPOINT_HIT
    | TOKEN_CB_CG_NEW_CHOICE
    | TOKEN_CB_CG_CHOICE_TO_USE
    | TOKEN_CB_CG_USED_CHOICE
    | TOKEN_QUIT
    | TOKEN_HELLO
    | TOKEN_HELP
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
    : TOKEN_RUN              WS? { $value = new CmdRun(CmdRunTypes.RUN, "run"); }
    | TOKEN_CONTINUE         WS? { $value = new CmdRun(CmdRunTypes.RUN, "continue"); }
    | TOKEN_BREAK            WS? { $value = new CmdRun(CmdRunTypes.STOP, "break"); }
    | cmdBreakpoints         WS? { $value = $cmdBreakpoints.value; }
    | cmdSingleSteps         WS? { $value = $cmdSingleSteps.value; }
    | cmdProgramState        WS? { $value = $cmdProgramState.value; }
    | cmdChoiceGenerators    WS? { $value = $cmdChoiceGenerators.value; }
    | cmdRecord              WS? { $value = $cmdRecord.value; }
    | cmdAssertions          WS? { $value = $cmdAssertions.value; }
    | cmdInformational       WS? { $value = $cmdInformational.value; }
    ;
 
cmdBreakpoints returns [ClientCommand value]
    : TOKEN_SHOW   WS? TOKEN_BREAKPOINT             { $value = new CmdBreakpointShow(); }
    | TOKEN_DELETE WS? TOKEN_BREAKPOINT WS? INT     { $value = new CmdBreakpointDelete($INT.text); }
    | TOKEN_CREATE WS? TOKEN_BREAKPOINT             { ConsoleBreakpointCreate  bpCreate = new ConsoleBreakpointCreate(); } ( WS? cmdCreateBP[bpCreate])* WS bpExpression { bpCreate.setBPExpression($bpExpression.expr); $value = new CmdBreakpointCreate(bpCreate); }
    ;
cmdCreateBP [ConsoleBreakpointCreate bpCreate]
    : TOKEN_NAME  WS? SIGN_EQUAL WS? anyWord { $bpCreate.setName($anyWord.text); }
    | TOKEN_STATE WS? SIGN_EQUAL WS? cmdBreakpointsStates { $bpCreate.setState($cmdBreakpointsStates.bpState); }
    | (lower=intValue  WS? signLess=LESS WS?)? TOKEN_HIT_COUNT (WS? signHigh=LESS WS? upper=intValue)? { $bpCreate.setBounds( $lower.value, ($signLess!=null?$signLess.text:null), ($signHigh!=null?$signHigh.text:null), $upper.value); }
    ;
cmdBreakpointsStates returns [BreakPointStates bpState]
    : TOKEN_DISABLE         { $bpState = BreakPointStates.BP_STATE_DISABLED; }
    | TOKEN_LOG             { $bpState = BreakPointStates.BP_STATE_LOGGING; }
    | TOKEN_ENABLE          { $bpState = BreakPointStates.BP_STATE_ENABLED; }
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
    : TOKEN_STEP_INSTRUCTION                      intValue?  { $value = new CmdSingleStepping(true, StepType.ST_INSTRUCTION,   $intValue.value); }
    | TOKEN_STEP_OVER                             intValue?  { $value = new CmdSingleStepping(true, StepType.ST_LINE,          $intValue.value); }
    | TOKEN_STEP_IN                               intValue?  { $value = new CmdSingleStepping(true, StepType.ST_STEP_IN,       $intValue.value); }
    | TOKEN_STEP_OUT                              intValue?  { $value = new CmdSingleStepping(true, StepType.ST_STEP_OUT,      $intValue.value); }
    | TOKEN_STEP_TRANSITION (WS? c=cgType)?       intValue?  { $value = CmdSingleStepping.createCmdSingleSteppingTransition(true, $c.cgsType, $intValue.value); }
    | TOKEN_BACK_STEP_INSTRUCTION                 intValue?  { $value = new CmdSingleStepping(false, StepType.ST_INSTRUCTION,  $intValue.value); }
    | TOKEN_BACK_STEP_OVER                        intValue?  { $value = new CmdSingleStepping(false, StepType.ST_LINE,         $intValue.value); }
    | TOKEN_BACK_STEP_IN                          intValue?  { $value = new CmdSingleStepping(false, StepType.ST_STEP_IN,      $intValue.value); }
    | TOKEN_BACK_STEP_OUT                         intValue?  { $value = new CmdSingleStepping(false, StepType.ST_STEP_OUT,     $intValue.value); }
    | TOKEN_BACK_STEP_TRANSITION (WS? c=cgType)?  intValue?  { $value = CmdSingleStepping.createCmdSingleSteppingTransition(false, $c.cgsType, $intValue.value); }
    ;
cmdInformational returns [ClientCommand value]
  : TOKEN_HELLO  { $value = new CmdHello(); }
  | TOKEN_HELP   { $value = new CmdHelp(); }
  | TOKEN_QUIT    { $value = new CmdQuit(); }
  ;

cmdProgramState returns [ClientCommand value]
  : TOKEN_THREAD    (WS? intValue)?   { $value = new CmdStatusThreads($intValue.value); }
  | TOKEN_PRINT     (WS? allText)?    { $value = new CmdPrint($allText.text!=null?$allText.text:""); }
  | TOKEN_THREAD_PC (WS? intValue)?   { $value = new CmdThreadsPC($intValue.value); }
  | TOKEN_SET        WS? allText      { $value = new CmdSet($allText.text); }
  ;
  

cmdChoiceGenerators returns [ClientCommand value]
    : TOKEN_USED WS? TOKEN_CHOICE_GENERATORS { $value = new CmdUsedChoiceGenerators(); }
    | a=enableOrDisable  WS? (b=cgMode WS?)? (c=cgType WS?)? TOKEN_CHOICE_GENERATORS  { $value = new CmdChoiceGeneratorsTracking( ($c.cgsType!=null ? $c.cgsType : CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_ALL), ($b.cg_mode != null ? $b.cg_mode : ChoiceGeneratorsInterface.CGMode.CG_MODE_PRINT), $a.value); }
    | TOKEN_CHOICE_GENERATORS WS? TOKEN_SELECT cgChoice { $value = new CmdChoiceSelect($cgChoice.choice); }
    ;

cgType returns [CmdChoiceGeneratorsTracking.CGTypeSpec cgsType]
    : TOKEN_SCHEDULING   { $cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_SCHEDULING; }
    | TOKEN_DATA         { $cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_DATA; }
    | TOKEN_ALL          { $cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_ALL; }
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
    :                 { $choice = CmdChoiceSelect.USE_CURENT_CHOICE; }
    | WS? intValue    { $choice = $intValue.value; }
    | WS? '*'         { $choice = ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE; }
    ;

cmdCallback returns [ClientCommand value]
    : TOKEN_CB_STATE_CHANGE                                                { $value = new CmdCallback(CB_METHODS.CB_STATE_CHANGE); }
    | TOKEN_CB_STATE_CHANGE WS? TOKEN_STATE WS? '=' WS? inspectorState     { $value = new CmdCallback($inspectorState.state); }
    | TOKEN_CB_GENERIC_ERROR                                               { $value = new CmdCallback(CB_METHODS.CB_GENERIC_ERROR); }
    | TOKEN_CB_GENERIC_INFO                                                 { $value = new CmdCallback(CB_METHODS.CB_GENERIC_INFO); }
    | TOKEN_CB_BREAKPOINT_HIT                                              { $value = new CmdCallback(CB_METHODS.CB_BREAKPOINT_HIT); }
    | TOKEN_CB_CG_NEW_CHOICE                                               { $value = new CmdCallback(CB_METHODS.CB_CG_NEW_CHOICE); }
    | TOKEN_CB_CG_CHOICE_TO_USE                                            { $value = new CmdCallback(CB_METHODS.CB_CG_CHOICE_TO_USE); }
    | TOKEN_CB_CG_USED_CHOICE                                              { $value = new CmdCallback(CB_METHODS.CB_CG_USED_CHOICE); }
    ;

inspectorState returns [InspectorStates state]
    : TOKEN_RUNNING         { $state = InspectorStates.JPF_RUNNING; }
    | TOKEN_STARTED         { $state = InspectorStates.JPF_STARTED; }
    | TOKEN_STOPPED         { $state = InspectorStates.JPF_STOPPED; }
    | TOKEN_TERMINATING     { $state = InspectorStates.JPF_TERMINATING; }
    ;
    
cmdRecord returns [ClientCommand value]
    : TOKEN_RECORD WS TOKEN_CLEAR                  { $value = new CmdRecordClear(); }
    | TOKEN_RECORD WS TOKEN_PRINT                  { $value = new CmdRecordPrint(); }
    | TOKEN_RECORD WS TOKEN_SAVE    WS allText     { $value = new CmdRecordSave($allText.expr); }
    | TOKEN_RECORD WS TOKEN_EXECUTE WS allText     { $value = new CmdRecordExecute($allText.expr); }
    ;

cmdAssertions returns [ClientCommand value]
    : TOKEN_ASSERTIONS WS enableOrDisable          (WS b=allText)?       { $value = new CmdAssertions($enableOrDisable.value, $b.expr); }
    | TOKEN_ASSERT     WS a=allTextNoWS  WS b=allText         { $value = new CmdAssertionsBreakpoint($a.expr /* fileName:line */, $b.expr /* state expression like  var_i != 10 */); }
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



// TOKEN_PRINT is defined cmdProgramStateText
signs : SIGN_DOLLAR | SIGN_DOT | SIGN_EQUAL | SIGN_HASH | SIGN_COLON | SIGN_LSBRA | SIGN_RSBRA | SIGN_ASTERISK | SIGN_SLASH | SIGN_BACKSLASH ;


intValue returns [Integer value]
    : SIGN_PLUS? INT { $value =  Integer.valueOf($INT.text); }
    | SIGN_MINUS INT { $value = -Integer.valueOf($INT.text); }
    | SIGN_PLUS? HEX { $value =  Integer.valueOf(($HEX.text).substring(2), 16); }
    | SIGN_MINUS HEX { $value = -Integer.valueOf(($HEX.text).substring(2), 16); }
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
TOKEN_ASSERTIONS : 'assertions' ;
TOKEN_BREAK : 'break' ;
TOKEN_BREAKPOINT : 'breakpoint' | 'bp' ;
TOKEN_CHOICE_GENERATORS : 'choice_generators' | 'cg' ;
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
TOKEN_RECORD : 'record' | 'rec' ;
TOKEN_RUN : 'run' ;
TOKEN_RUNNING : 'running' ;
TOKEN_SAVE : 'save' ;
TOKEN_SELECT : 'select' | 'sel' ;
TOKEN_SET : 'set' ;
TOKEN_SCHEDULING : 'scheduling' | 'sched' ;
TOKEN_SHOW : 'show' | 'sw' ;
TOKEN_STARTED : 'started' ;
TOKEN_STATE : 'state' ;
TOKEN_STOPPED : 'stopped' ;
TOKEN_USED : 'used' ;
TOKEN_TERMINATING : 'terminating' ;
TOKEN_THREAD : 'thread' | 'ti' ;
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

// Callback-related keywords
TOKEN_CB_STATE_CHANGE : 'callback_state_change' | 'cb_state_changed' ;
TOKEN_CB_GENERIC_ERROR : 'callback_generic_error' | 'cb_gen_error' ;
TOKEN_CB_GENERIC_INFO : 'callback_generic_info' | 'cg_gen_info' ;
TOKEN_CB_BREAKPOINT_HIT : 'callback_breakpoint_hit' | 'cb_bp_hit' ;
TOKEN_CB_CG_NEW_CHOICE : 'callback_choice_generator_new_choice' | 'cb_cg_new_choice' ;
TOKEN_CB_CG_CHOICE_TO_USE : 'callback_choice_generator_choice_to_use' | 'cb_cg_choice_to_use' ;
TOKEN_CB_CG_USED_CHOICE : 'callback_choice_generator_used_choice' | 'cb_cg_used_choice' ;

// Informational commmands
TOKEN_HELLO : 'hello';
TOKEN_HELP : 'help';
TOKEN_QUIT : 'quit' | 'exit';

// End of keywords.


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
