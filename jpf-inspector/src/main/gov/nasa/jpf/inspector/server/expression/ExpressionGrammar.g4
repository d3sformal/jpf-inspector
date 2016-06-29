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

grammar ExpressionGrammar;

options {
    language=Java;
}

@header{ 
    package gov.nasa.jpf.inspector.server.expression.generated;
    
    import gov.nasa.jpf.inspector.server.expression.*;
    import gov.nasa.jpf.inspector.server.expression.expressions.*;
    import gov.nasa.jpf.inspector.server.programstate.relop.*;
    import gov.nasa.jpf.inspector.server.breakpoints.*;
    import gov.nasa.jpf.inspector.utils.parser.*;
    import gov.nasa.jpf.inspector.utils.*;
    import gov.nasa.jpf.inspector.utils.expressions.*;
    import gov.nasa.jpf.inspector.interfaces.*;
    import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
    
}

allKeyWordsIDFLike
    : TOKEN_AND
    | TOKEN_ALL
    | TOKEN_ANY
    | TOKEN_ARRAY
    | TOKEN_ASSERT
    | TOKEN_B
    | TOKEN_BEGIN
    | TOKEN_BOTH
    | TOKEN_CHOICE_GENERATOR
    | TOKEN_CLASS
    | TOKEN_CONDITION
    | TOKEN_D
    | TOKEN_DATA
    | TOKEN_E
    | TOKEN_F
    | TOKEN_END
    | TOKEN_EXCEPTION_THROWN
    | TOKEN_FIELD_ACCESS
    | TOKEN_FIELD_READ
    | TOKEN_FIELD_WRITE
    | TOKEN_GARBAGE_COLLECTION
    | TOKEN_HIT_COUNT
    | TOKEN_IN
    | TOKEN_INFINITY
    | TOKEN_INSTRUCTION
    | TOKEN_INSTRUCTION_TYPE
    | TOKEN_INVOKE 
    | TOKEN_L
    | TOKEN_LOCAL_ACCESS
    | TOKEN_LOCAL_READ
    | TOKEN_LOCAL_WRITE
    | TOKEN_LOCK
    | TOKEN_METHOD
    | TOKEN_NAN
    | TOKEN_NEGATIVE_INFINITY1
    | TOKEN_NONE
    | TOKEN_NOT
    | TOKEN_NOTIFY
    | TOKEN_METHOD_INVOKE
    | TOKEN_OBJECT_CREATED
    | TOKEN_OBJECT_RELEASED
    | TOKEN_OR
    | TOKEN_POSITION
    | TOKEN_POSITIVE_INFINITY1    
    | TOKEN_PROPERTY_VIOLATED
    | TOKEN_RETURN
    | TOKEN_SCHEDULING
    | TOKEN_STACK_FRAME
    | TOKEN_STATE_ADVANCED
    | TOKEN_STEP_IN
    | TOKEN_STEP_OUT
    | TOKEN_STEP_OVER
    | TOKEN_SYNC_BLOCK
    | TOKEN_THREAD
    | TOKEN_THREAD_SCHEDULED
    | TOKEN_X
    ;

// fieldName cannot be a java keyword
javaKeyWords
    : TOKEN_FALSE
    | TOKEN_NULL
    | TOKEN_TRUE
    ;
    
//Non IDF keywords
allKeywordsOther
    : TOKEN_HASH_FIELD
    | TOKEN_HASH_HEAP
    | TOKEN_HASH_STACK_FRAME
    | TOKEN_HASH_STACK_SLOT
    | TOKEN_HASH_THREAD
    | TOKEN_HASH_THIS
    | TOKEN_HASH_STATIC
    | TOKEN_HASH_SUPER
    | TOKEN_NEGATIVE_INFINITY2
    | TOKEN_NOT_A_NUMBER
    | TOKEN_POSITIVE_INFINITY2
    | SIGN_ASTERISK
    | SIGN_BACK_SHLASH
    | SIGN_DOLAR
    | SIGN_DOT
    | SIGN_DOUBLE_QUOTE
    | SIGN_EQUALS
    | SIGN_MINUS
    | SIGN_PLUS
    | SIGN_SINGLE_QUOTE
    | ESCAPE_SEQ_B
    | ESCAPE_SEQ_T
    | ESCAPE_SEQ_N
    | ESCAPE_SEQ_F
    | ESCAPE_SEQ_R
    | ESCAPE_SEQ_DOUBLE_QUOTE
    | ESCAPE_SEQ_SINGLE_QUOTE
    | ESCAPE_SEQ_BACKSLASH
    | ESCAPE_SEQ_OCTAL
    | ESCAPE_SEQ_UNICODE
    | RELOP_EQUAL
    | RELOP_NOT_EQUAL
    | RELOP_LESS_THAN
    | RELOP_LESS_OR_EQUAL_THAN
    | RELOP_GREATER_THAN
    | RELOP_GREATER_OR_EQUAL_THAN
    ;

className returns [ClassName cn]
    : classNameText         { $cn = new ClassName($classNameText.text); }
    ;
fileName returns [String text]
    : fileNameText          { $text = $fileNameText.text; }
    ;
classAndFieldName returns [FieldName fn]
   : className WS? ':' WS? fieldName[$className.cn] { $fn = $fieldName.fn; }
   ;
fieldName [ClassName cn] returns [FieldName fn]
    : idf                   { $fn = new FieldName($idf.text, cn); }
    ;
methodName [ClassName cn] returns [MethodName mn]
    : idf                   { $mn = new MethodName($idf.text, cn); }
    ;
    

cmdBreakpointsCreateParams [ExpressionFactory expFactory] returns [ExpressionBoolean bp]
    : a=cmdBreakpointsCreateParams1[expFactory]  {$bp = $a.bp; } EOF 
    ;

cmdBreakpointsCreateParams1 [ExpressionFactory expFactory] returns [ExpressionBoolean bp]
    : WS? TOKEN_NOT WS? a3=cmdBreakpointsCreateParams1[expFactory] { $bp = expFactory.getBreakpointNot($a3.bp); }
    | a=cmdBreakpointsCreateParamsAtomNotTerminateIDF[expFactory] {$bp = $a.bp; }
           ( (TOKEN_AND b=cmdBreakpointsCreateParams1[expFactory] { $bp = expFactory.getBreakpointOperatorAnd($a.bp, $b.bp); } ) |
             (TOKEN_OR  b=cmdBreakpointsCreateParams1[expFactory] { $bp = expFactory.getBreakpointOperatorOr ($a.bp, $b.bp); } )
           )?
    | a2=cmdBreakpointsCreateParamsAtomTerminateIDF[expFactory] {$bp = $a2.bp; }
           ( (WS TOKEN_AND b2=cmdBreakpointsCreateParams1[expFactory] { $bp = expFactory.getBreakpointOperatorAnd($a2.bp, $b2.bp); } ) |
             (WS TOKEN_OR  b2=cmdBreakpointsCreateParams1[expFactory] { $bp = expFactory.getBreakpointOperatorOr ($a2.bp, $b2.bp); } )
           )?
    ;


cmdBreakpointsCreateParamsAtom [ExpressionFactory expFactory] returns [ExpressionBoolean bp]
    : a=cmdBreakpointsCreateParamsAtomNotTerminateIDF[expFactory]                                       { $bp = $a.bp; } EOF
    | b=cmdBreakpointsCreateParamsAtomTerminateIDF[expFactory]                                          { $bp = $b.bp; } EOF
    ;

cmdBreakpointsCreateParamsAtomNotTerminateIDF [ExpressionFactory expFactory] returns [ExpressionBoolean bp]
    : WS? TOKEN_GARBAGE_COLLECTION   WS? '=' WS? cmdGarbageCollectionSpec WS?                             { $bp = expFactory.getBreakpointGarbageCollection($cmdGarbageCollectionSpec.bpMode); }
    | WS? TOKEN_CHOICE_GENERATOR     WS? '=' WS? cmdChoiceGeneratorType WS?                               { $bp = expFactory.getBreakpointChoiceGenerator($cmdChoiceGeneratorType.bpMode); }
    | WS? TOKEN_INSTRUCTION_TYPE     WS? '=' WS? cmdInstructionTypes WS?                                  { $bp = expFactory.getBreakpointInstructionType($cmdInstructionTypes.instructionType); }
    | WS? TOKEN_POSITION             WS? '=' WS? fileName WS? ':' WS? intValue WS?                        { $bp = expFactory.getExpBreakpointPosition($fileName.text, $intValue.value); }
    | WS? TOKEN_PROPERTY_VIOLATED    WS?                                                                  { $bp = expFactory.getBreakpoointPropertyViolated(); }
    | WS? TOKEN_THREAD_SCHEDULED     WS? '=' WS? cmdThreadScheduledDirection WS? (':' WS? intValue WS?)?
     { $bp = expFactory.getBreakpointThreadScheduled(
              $cmdThreadScheduledDirection.bpMode,
              ($intValue.ctx !=null? $intValue.value : null)
              ); }
    | WS? TOKEN_STATE_ADVANCED       WS?                                                                  { $bp = expFactory.getBreakpointStateAdvanced(); }
    | WS? TOKEN_STEP_IN              WS?                                                                  { $bp = expFactory.getBreakpointSingleStep(StepType.ST_STEP_IN); }
    | WS? TOKEN_STEP_OVER            WS?                                                                  { $bp = expFactory.getBreakpointSingleStep(StepType.ST_LINE); }
    | WS? '(' a=cmdBreakpointsCreateParams1[expFactory] ')' WS?                                           { $bp = $a.bp; }
    | WS? TOKEN_STEP_OUT             WS? TOKEN_THREAD WS? '=' WS? tid=intValue WS? TOKEN_STACK_FRAME WS? '=' WS? sfDepth=intValue WS?                                                                                                            { $bp = expFactory.getBreakpointStepOut( $tid.value, $sfDepth.value); }
    | WS? TOKEN_SPECIFIC_INSTRUCTION WS? TOKEN_THREAD WS? '=' WS? tid=intValue WS? TOKEN_INSTRUCTION WS? '=' WS? className WS? ':' WS? methodName[$className.cn] WS? ':' WS? instIndex=intValue WS? TOKEN_HIT_COUNT WS? '=' WS? hc=intValue WS?  { $bp = expFactory.getBreakpointInstruction($tid.value, $methodName.mn, $instIndex.value, $hc.value); }
    | WS? TOKEN_ASSERT               WS? '(' fileName WS? ':' WS? intValue WS? ')' WS? '(' cmdBreakpointsCreateParams1[expFactory] ')'                                                                                                           { $bp = expFactory.getBreakpointAssert(expFactory.getExpBreakpointPosition($fileName.text, $intValue.value), $cmdBreakpointsCreateParams1.bp); }
    | WS? idf WS? '(' (WS? exprs=comma_separated_expressions[expFactory])? WS? ')' WS?
    { $bp = expFactory.getCustomHitCondition($idf.text, $exprs.ctx != null ? $exprs.expressions : new Expressions() ); }
    ;
comma_separated_expressions [ExpressionFactory expFactory] returns [Expressions expressions]
  :  a=nonCommaText WS? ',' WS? exprs=comma_separated_expressions[expFactory]
  { $expressions = $exprs.expressions; $expressions.insert($a.text); }
  | b=nonCommaText { $expressions = new Expressions(); $expressions.insert($b.text); }
  ;
nonCommaText
 : (~',') nonCommaText
 | (~',')
 ;


cmdBreakpointsCreateParamsAtomTerminateIDF [ExpressionFactory expFactory] returns [ExpressionBoolean bp]
    : WS? fieldAccess              WS? '=' WS? className WS? ':' WS? fieldName[$className.cn] WS?
    { $bp = expFactory.getBreakpointFieldAccess($fieldAccess.bpMode, $fieldName.fn); }
    | WS? TOKEN_METHOD_INVOKE      WS? '=' WS? className WS? ':' WS? methodName[$className.cn] WS?
    { $bp = expFactory.getBreakpointMethodInvoke(BreakPointModes.BP_MODE_METHOD_INVOKE, $methodName.mn); }
    | WS? TOKEN_OBJECT_CREATED     WS? '=' WS? className WS?
    { $bp = expFactory.getBreakpointObjectCreated($className.cn); }
    | WS? TOKEN_OBJECT_RELEASED    WS? '=' WS? className WS?
    { $bp = expFactory.getBreakpointObjectReleased($className.cn); }
    | WS? TOKEN_EXCEPTION_THROWN   WS? '=' WS? className WS?
    { $bp = expFactory.getBreakpointExpecptionThrown($className.cn); }
    | WS? TOKEN_CLASS WS? '=' WS? className WS?
    { $bp = expFactory.getBreakpointClass($className.cn); }
    | WS? TOKEN_METHOD WS? '=' WS? methodName[new ClassName("*")] WS?
    { $bp = expFactory.getBreakpointMethod($methodName.mn); }
    | WS? TOKEN_METHOD WS? '=' WS? className WS? ':' WS? methodName[$className.cn] WS?
    { $bp = expFactory.getBreakpointMethod($methodName.mn); }
    | WS? TOKEN_LOCAL_ACCESS WS? '=' WS? idf WS? { $bp = expFactory.getBreakpointLocalAccess($idf.text); }
    | WS? TOKEN_LOCAL_READ WS? '=' WS? idf WS? { $bp = expFactory.getBreakpointLocalRead($idf.text); }
    | WS? TOKEN_LOCAL_WRITE WS? '=' WS? idf WS? { $bp = expFactory.getBreakpointLocalWrite($idf.text); }
    | l=cmdStateExpression1Value[expFactory]  relOp[expFactory] r=cmdStateExpression1Value[expFactory]
    { $bp = expFactory.getBreakpointCompare($l.expr, $relOp.relop, $r.expr); }
    ;

allKeyWords returns [String text]
    : a=allKeyWordsIDFLike                  { $text = $a.text; }
    | b=allKeywordsOther                    { $text = $b.text; }
    | c=javaKeyWords                        { $text = $c.text; }
    ;

fieldAccess returns [BreakPointModes bpMode] 
    : TOKEN_FIELD_ACCESS { $bpMode = BreakPointModes.BP_MODE_FIELD_ACCESS; }
    | TOKEN_FIELD_READ   { $bpMode = BreakPointModes.BP_MODE_FIELD_ACCESS_READ; }
    | TOKEN_FIELD_WRITE  { $bpMode = BreakPointModes.BP_MODE_FIELD_ACCESS_WRITE; }
    ;

cmdGarbageCollectionSpec returns [BreakPointModes bpMode]
    : TOKEN_BEGIN { $bpMode  = BreakPointModes.BP_MODE_GC_BEGIN; }
    | TOKEN_END   { $bpMode  = BreakPointModes.BP_MODE_GC_END; }
    | TOKEN_BOTH  { $bpMode  = BreakPointModes.BP_MODE_GC_BOTH; }
    | TOKEN_ALL       { $bpMode = BreakPointModes.BP_MODE_CHOICE_BOTH; }
    ;

cmdChoiceGeneratorType returns [BreakPointModes bpMode]
    : TOKEN_DATA       { $bpMode = BreakPointModes.BP_MODE_CHOICE_DATA; }
    | TOKEN_SCHEDULING { $bpMode = BreakPointModes.BP_MODE_CHOICE_SCHEDULING; }
    | TOKEN_BOTH       { $bpMode = BreakPointModes.BP_MODE_CHOICE_BOTH; }
    | TOKEN_ALL       { $bpMode = BreakPointModes.BP_MODE_CHOICE_BOTH; }
    ;

cmdInstructionTypes returns [InstructionType instructionType]
    : TOKEN_ANY               { $instructionType = InstructionType.ANY; }
    | TOKEN_NONE              { $instructionType = InstructionType.NONE; }
    | TOKEN_INVOKE            { $instructionType = InstructionType.INVOKE; }
    | TOKEN_RETURN            { $instructionType = InstructionType.RETURN; }
    | TOKEN_FIELD_ACCESS      { $instructionType = InstructionType.FIELD_ACCESS; }
    | TOKEN_FIELD_READ        { $instructionType = InstructionType.FIELD_READ; }
    | TOKEN_FIELD_WRITE       { $instructionType = InstructionType.FIELD_WRITE; }
    | TOKEN_LOCAL_ACCESS      { $instructionType = InstructionType.LOCAL_ACCESS; }
    | TOKEN_LOCAL_READ        { $instructionType = InstructionType.LOCAL_READ; }
    | TOKEN_LOCAL_WRITE       { $instructionType = InstructionType.LOCAL_WRITE; }
    | TOKEN_CONDITION         { $instructionType = InstructionType.IFCOND; }
    | TOKEN_LOCK              { $instructionType = InstructionType.LOCK; }
    | TOKEN_ARRAY             { $instructionType = InstructionType.ARRAY; }
    ;

cmdThreadScheduledDirection returns [BreakPointModes bpMode]
    : TOKEN_IN                { $bpMode = BreakPointModes.BP_MODE_THREAD_SCHEDULED_IN; }
    | TOKEN_OUT               { $bpMode = BreakPointModes.BP_MODE_THREAD_SCHEDULED_OUT; }
    | TOKEN_BOTH              { $bpMode = BreakPointModes.BP_MODE_THREAD_SCHEDULED_BOTH; }
    | TOKEN_ALL       { $bpMode = BreakPointModes.BP_MODE_CHOICE_BOTH; }
    ;

cmdStateAssignment [ExpressionFactory expFactory] returns [ExpressionStateAssignment expr]
    : lval=cmdStateExpression1[$expFactory] SIGN_EQUALS rval=cmdStateExpression1[$expFactory] { $expr = $expFactory.getStateAssignment($lval.expr, $rval.expr); } EOF 
    ;
    
cmdStateExpression [ExpressionFactory expFactory] returns [ExpressionStateRootNode expr]
    : cmdStateExpression1[$expFactory] { $expr = $cmdStateExpression1.expr; } EOF
    ;

cmdStateExpression1    [ExpressionFactory expFactory] returns [ExpressionStateRootNode expr]
    : WS? cmdStateExpressionThread[$expFactory]        { $expr = $cmdStateExpressionThread.expr; }
    | WS? cmdStateExpressionHeap[$expFactory]          { $expr = $cmdStateExpressionHeap.expr; }
    | WS? cmdStateConstValue[$expFactory] WS?          { $expr = $cmdStateConstValue.expr; }
    ;
    
// This version is able to parse text which represents only values 
// no #thread, #thread[0], #stackFrame[0]
// but #stackFrame[0].x is permitted
// The expression has to terminate in the cmdStateExpressionValue* rule
cmdStateExpression1Value    [ExpressionFactory expFactory] returns [ExpressionStateRootNode expr]
    : WS? cmdStateExpressionThreadValue[$expFactory]        { $expr = $cmdStateExpressionThreadValue.expr; }
    | WS? cmdStateExpressionHeapValue[$expFactory]          { $expr = $cmdStateExpressionHeapValue.expr; }
    | WS? cmdStateConstValue[$expFactory]  WS?               { $expr = $cmdStateConstValue.expr; }
    ;

cmdStateExpressionThread [ExpressionFactory expFactory] returns [ExpressionStateThread expr]
    : TOKEN_HASH_THREAD WS? ('[' WS? intValue WS? ']' WS?)? ('.' WS? a=cmdStateExpressionStackFrame[$expFactory])?
     { $expr = $expFactory.getStateThread($intValue.ctx != null ? $intValue.value : null, $a.ctx != null ? $a.expr : null); }
    |
      a=cmdStateExpressionStackFrame[$expFactory]
     { $expr = $expFactory.getStateThread(null,            $a.expr); }
    ;

cmdStateExpressionThreadValue [ExpressionFactory expFactory] returns [ExpressionStateThread expr]
    : TOKEN_HASH_THREAD WS? ('[' WS? intValue WS? ']' WS?)? ('.' WS? a=cmdStateExpressionStackFrameValue[$expFactory])
     { $expr = $expFactory.getStateThread($intValue.ctx != null ? $intValue.value : null, $a.ctx != null ? $a.expr : null); }
    |                                                                a=cmdStateExpressionStackFrameValue[$expFactory]
     { $expr = $expFactory.getStateThread(null,            $a.expr); }
    ;

cmdStateExpressionStackFrame [ExpressionFactory expFactory] returns [ExpressionStateStackFrame expr]
    : TOKEN_HASH_STACK_FRAME WS? ('[' WS? intValue WS? ']' WS?)?
     ('.' WS? a=cmdStateExpressionStackFrame1[$expFactory, $intValue.ctx!=null?$intValue.value:null])?
    { $expr = ( $a.ctx !=null ? $a.expr :  $expFactory.getStateStackFrame($intValue.ctx!=null?$intValue.value:null, null) ); }
    | b=cmdStateExpressionStackFrame1[$expFactory, null] { $expr = $b.expr; }
    | { $expr = $expFactory.getStateStackFrame(null, null); }
    ;

cmdStateExpressionStackFrameValue [ExpressionFactory expFactory] returns [ExpressionStateStackFrame expr]
    : TOKEN_HASH_STACK_FRAME WS? ('[' WS? intValue WS? ']' WS?)? ('.' WS? a=cmdStateExpressionStackFrame1[$expFactory, $intValue.ctx!=null?$intValue.value:null])  
     { $expr = ( $a.ctx!=null ? $a.expr :  $expFactory.getStateStackFrame($intValue.ctx!=null?$intValue.value:null, null) ); }
    |                                                                     b=cmdStateExpressionStackFrame1[$expFactory, null]               { $expr = $b.expr; }
    ;

cmdStateExpressionStackFrame1 [ExpressionFactory expFactory, Integer sfIndex] returns [ExpressionStateStackFrame expr]
    : a=cmdStateExpressionValueStackFrameSlot[$expFactory]      { $expr = $expFactory.getStateStackFrame($sfIndex,   $a.expr); }
    | b=cmdStateExpressionValueName[$expFactory]                { $expr = $expFactory.getStateStackFrameName($sfIndex,   $b.expr); }
    | c=cmdStateExpressionValueAfterStackFrame[$expFactory]     { $expr = $expFactory.getStateStackFrameThis($sfIndex,   $c.expr); }
    | d=cmdStateExpressionValueStatic[$expFactory]              { $expr = $expFactory.getStateStackFrameStatic($sfIndex, $d.expr); }
    ;

// This is a special "this" adding rule after stackFrame
cmdStateExpressionValueAfterStackFrame [ExpressionFactory expFactory] returns [ExpressionStateValueThis expr]
    : a=cmdStateExpressionValueFieldIndex[$expFactory]                        { $expr = $expFactory.getStateValueThis($a.expr); }
    | c=cmdStateExpressionValueOuterClass[$expFactory]                        { $expr = $expFactory.getStateValueThis($c.expr); }
    | d=cmdStateExpressionValueSuper[$expFactory]                             { $expr = $expFactory.getStateValueThis($d.expr); }
    | e=cmdStateExpressionValueThis[$expFactory]                              { $expr = $e.expr; }
    ;


cmdStateExpressionValueFieldIndex [ExpressionFactory expFactory] returns [ExpressionStateValueFieldIndex expr]
    : TOKEN_HASH_FIELD WS? '[' WS? intValue WS? ']' WS? a=cmdStateExpressionValue[$expFactory]?      
      { $expr = $expFactory.getStateValueFieldIndex($intValue.ctx!=null?$intValue.value:null, $a.ctx != null ? $a.expr : null); }
    ;

cmdStateExpressionValueName [ExpressionFactory expFactory] returns [ExpressionStateValueName expr]
    : idf_fieldName WS? a=cmdStateExpressionValue[$expFactory]?
     { $expr = $expFactory.getStateValueName($idf_fieldName.text, $a.ctx != null ? $a.expr : null); }
    ;

cmdStateExpressionValueOuterClass [ExpressionFactory expFactory] returns [ExpressionStateValueOuterClass expr]
    : TOKEN_HASH_OUTER_CLASS WS? a=cmdStateExpressionValue[$expFactory]?
    { $expr = $expFactory.getStateValueOuterClass($a.ctx != null ? $a.expr : null); }
    ;

cmdStateExpressionValueStatic [ExpressionFactory expFactory] returns [ExpressionStateValueStatic expr]
    : TOKEN_HASH_STATIC WS? '[' WS? intValue WS? ']' WS? a=cmdStateExpressionValue[$expFactory]?
    { $expr = $expFactory.getStateValueStaticArea($intValue.value,  $a.ctx != null ? $a.expr : null); }
    | TOKEN_HASH_STATIC                              WS? b=cmdStateExpressionClass[$expFactory]?
     { $expr = $expFactory.getStateValueStaticArea(null,            $b.ctx != null ? $b.expr : null); }
    ;

cmdStateExpressionValueStackFrameSlot [ExpressionFactory expFactory] returns [ExpressionStateStackFrameSlot expr]
    : TOKEN_HASH_STACK_SLOT WS? '[' WS? intValue WS? ']' WS?  a=cmdStateExpressionValue[$expFactory]?
     { $expr = $expFactory.getStateValueStackFrameSlot($intValue.value, $a.ctx != null ? $a.expr : null); }
    ;

cmdStateExpressionValueSuper [ExpressionFactory expFactory] returns [ExpressionStateValueSuper expr]
    : TOKEN_HASH_SUPER WS?  a=cmdStateExpressionValue[$expFactory]?
      { $expr = $expFactory.getStateValueSuper($a.ctx != null ? $a.expr : null); }
    ;


cmdStateExpressionValueThis [ExpressionFactory expFactory] returns [ExpressionStateValueThis expr]
    : TOKEN_HASH_THIS WS? a=cmdStateExpressionValue[$expFactory]?
    { $expr = $expFactory.getStateValueThis($a.ctx != null ? $a.expr : null); }
    ;

cmdStateExpressionValueArray [ExpressionFactory expFactory] returns [ExpressionStateValue expr]
    : '[' WS? intValue WS? ']' WS? a=cmdStateExpressionValue[$expFactory]?
     { $expr = $expFactory.getStateValueArrayIndex($a.ctx != null ? $a.expr : null, $intValue.value); }
    ;

cmdStateExpressionValue [ExpressionFactory expFactory] returns [ExpressionStateValue expr]
    : a=cmdStateExpressionClass[$expFactory]       { $expr = $a.expr; }
    | b=cmdStateExpressionValueArray[$expFactory]  { $expr = $b.expr; }
    ;

// Represents whole class
cmdStateExpressionClass [ExpressionFactory expFactory] returns [ExpressionStateValue expr]
    : '.' WS? a=cmdStateExpressionValueFieldIndex[$expFactory]                        { $expr = $a.expr; }
    | '.' WS? b=cmdStateExpressionValueName[$expFactory]                              { $expr = $b.expr; }
    | '.' WS? c=cmdStateExpressionValueOuterClass[$expFactory]                        { $expr = $c.expr; }
    | '.' WS? d=cmdStateExpressionValueSuper[$expFactory]                             { $expr = $d.expr; }
    | '.' WS? e=cmdStateExpressionValueThis[$expFactory]                              { $expr = $e.expr; }
    | '.' WS? f=cmdStateExpressionValueStatic[$expFactory]                            { $expr = $f.expr; }
    ;

cmdStateExpressionHeap [ExpressionFactory expFactory] returns [ExpressionStateRootNode expr]
    : TOKEN_HASH_HEAP   WS? '[' WS? intValue  WS? ']' WS? a=cmdStateExpressionValue[$expFactory]?
    { $expr = $expFactory.getStateHeap($intValue.value, $a.ctx != null ? $a.expr : null); }
    | TOKEN_HASH_HEAP   WS? '[' WS? className WS? ']' WS?
    { $expr = $expFactory.getStateHeap($className.cn); }
    | TOKEN_HASH_STATIC WS? '[' WS? className WS? ']' WS?
       { $expr = $expFactory.getStateStaticArea($className.cn); }
    ;

cmdStateExpressionHeapValue [ExpressionFactory expFactory] returns [ExpressionStateRootNode expr]
    : TOKEN_HASH_HEAP   WS? '[' WS? intValue  WS? ']' WS? a=cmdStateExpressionValue[$expFactory]?
      { $expr = $expFactory.getStateHeap($intValue.value, $a.ctx != null ? $a.expr : null); }
    ;
 
cmdStateConstValue [ExpressionFactory expFactory] returns [ExpressionStateRootNode expr]
    : booleanValue         { $expr = expFactory.getConstValueBoolean($booleanValue.value); }
    | charValue            { $expr = expFactory.getConstValueChar   ($charValue.value); }
    | intValue             { $expr = expFactory.getConstValueInteger($intValue.value); }
    | longValue            { $expr = expFactory.getConstValueLong   ($longValue.value); }
    | floatValue           { $expr = expFactory.getConstValueFloat  ($floatValue.value); }
    | doubleValueLimited   { $expr = expFactory.getConstValueDouble ($doubleValueLimited.value); }
    | stringValue          { $expr = expFactory.getConstValueString ($stringValue.value); }
    | nullValue            { $expr = expFactory.getConstValueNull   (); }
    ;

relOp  [ExpressionFactory expFactory] returns [RelationOperator relop]
    : RELOP_EQUAL                       { $relop = expFactory.getRelopFactory().getRelOpEqual(); }
    | RELOP_NOT_EQUAL                   { $relop = expFactory.getRelopFactory().getRelOpNotEqual(); }
    | RELOP_LESS_THAN                   { $relop = expFactory.getRelopFactory().getRelOpLessThan(); }
    | RELOP_LESS_OR_EQUAL_THAN          { $relop = expFactory.getRelopFactory().getRelOpLessEqual(); }
    | RELOP_GREATER_THAN                { $relop = expFactory.getRelopFactory().getRelOpGreaterThan(); }
    | RELOP_GREATER_OR_EQUAL_THAN       { $relop = expFactory.getRelopFactory().getRelOpGreaterEqual(); }
    ;


booleanValue returns [boolean value]
    : TOKEN_TRUE          { $value = true; }
    | TOKEN_FALSE         { $value = false; }
    ;

nullValue returns [Object value]
    : TOKEN_NULL         { $value = null; }
    ;

charValue returns [char value]
    : CHAR                { $value = ExpressionStateValueConstChar.convertToCharWrapped($CHAR.text); } 
    ;

stringValue returns [String value]
    : STRING             { $value = ExpressionStateValueConstString.convertToStringWrapped($STRING.text); }
    ;

intValue returns [Integer value]
    : INT_TEXT      { $value = ExpressionStateValueConstInteger.convertToIntegerWrapped($INT_TEXT.text); }
    | INT           { $value = ExpressionStateValueConstInteger.convertToIntegerWrapped($INT.text); }
    | HEX           { $value = ExpressionStateValueConstInteger.convertToIntegerWrapped($HEX.text); }
    ;
longValue   returns [Long value]    : LONG_TEXT     { $value = ExpressionStateValueConstLong.convertToLongWrapped($LONG_TEXT.text); } ;
doubleValue returns [double value]  : doubleText    { $value = Double.valueOf($doubleText.text); } ;
floatValue  returns [float value]   : FLOAT_TEXT    { $value = Float.valueOf($FLOAT_TEXT.text); }
    ;
    
// Note TOKEN_NEGATIVE_INFINITY1 and TOKEN_POSITIVE_INFINITY1 are parsed as idfField_name and ("special semantic hack") resolves this
doubleValueLimited returns [double value]
    : DOUBLE_TEXT_LIMITTED              { $value = Double.valueOf($DOUBLE_TEXT_LIMITTED.text); }
    | TOKEN_NOT_A_NUMBER                { $value = Double.NaN; }
    | TOKEN_NEGATIVE_INFINITY2          { $value = Double.NEGATIVE_INFINITY; }
    | TOKEN_POSITIVE_INFINITY2          { $value = Double.POSITIVE_INFINITY; }
    ;

    

doubleValueExtended returns [double value]
    : doubleText                        { $value = Double.valueOf($doubleText.text); }
    | TOKEN_NOT_A_NUMBER                { $value = Double.NaN; }
    | TOKEN_NEGATIVE_INFINITY1          { $value = Double.NEGATIVE_INFINITY; }
    | TOKEN_NEGATIVE_INFINITY2          { $value = Double.NEGATIVE_INFINITY; }
    | TOKEN_POSITIVE_INFINITY1          { $value = Double.POSITIVE_INFINITY; }
    | TOKEN_POSITIVE_INFINITY2          { $value = Double.POSITIVE_INFINITY; }
    ;

doubleText
    : DOUBLE_TEXT_LIMITTED
    | TOKEN_NAN
    | TOKEN_INFINITY
    ;

idf 
    : idf_fieldName
    | javaKeyWords       
    ;
    
idf_fieldName
    : IDF_TEXT_INTERNAL
    | allKeyWordsIDFLike
    ;


classNameText
    : idf (( '$' | '.' | SIGN_ASTERISK ) classNameText?)?
    | '$'     classNameText?
    | '.'     classNameText? // Standard dot in file name
    | SIGN_ASTERISK     classNameText? // Regular expression extension
    ; 
 

fileNameText
    : idf ( ('.' | '\\' | '/' | ':' | SIGN_ASTERISK ) fileNameText?)?
    | '.' classNameText?
    | '\\' classNameText?
    | '/' classNameText?
    | SIGN_ASTERISK  classNameText? // Regular expression extension
    ;

//**************
// LEXER

WS      :   (' '|'\n'|'\r'|'\t')+ ;

TOKEN_ALL                       : 'all';
TOKEN_AND                       : 'and' ;
TOKEN_ANY                       : 'any' ;
TOKEN_ARRAY                     : 'array' ;
TOKEN_ASSERT                    : 'assert' ;
TOKEN_B                         : 'b' ;
TOKEN_BEGIN                     : 'begin' ;
TOKEN_BOTH                      : 'both' ;
TOKEN_CHOICE_GENERATOR          : 'choice_generator' | 'cg' ;
TOKEN_CONDITION                 : 'condition' | 'cond' ;
TOKEN_D                         : 'd' | 'D' ;
TOKEN_DATA                      : 'data' ;
TOKEN_E                         : 'e' | 'E' ;
TOKEN_F                         : 'f' | 'F' ;
TOKEN_END                       : 'end' ;
TOKEN_EXCEPTION_THROWN          : 'exception_thrown' | 'et' ;
TOKEN_FALSE                     : 'false' ;
TOKEN_FIELD_ACCESS              : 'field_access' | 'field' | 'fa' ;
TOKEN_FIELD_READ                : 'field_read' | 'fr' ;
TOKEN_FIELD_WRITE               : 'field_write' | 'fw' ;
TOKEN_GARBAGE_COLLECTION        : 'garbage_collection' | 'gc' ;
TOKEN_HASH_FIELD                : '#field' ;
TOKEN_HASH_HEAP                 : '#heap' ;
TOKEN_HASH_OUTER_CLASS          : '#outerClass' ;
TOKEN_HASH_STACK_FRAME          : '#stackFrame' ;
TOKEN_HASH_STACK_SLOT           : '#stackSlot' ;
TOKEN_HASH_THREAD               : '#thread' ;
TOKEN_HASH_THIS                 : '#this' ;
TOKEN_HASH_STATIC               : '#static' ;
TOKEN_HASH_SUPER                : '#super' ;
TOKEN_HIT_COUNT                 : 'hit_count' | 'hc' ;
TOKEN_IN                        : 'in' ;
TOKEN_INFINITY                  : 'Infinity' ;
TOKEN_INSTRUCTION               : 'instruction' | 'inst' ;
TOKEN_INSTRUCTION_TYPE          : 'instruction_type' | 'inst_type' | 'it' ;
TOKEN_INVOKE                    : 'invoke' | 'inv' ;
TOKEN_L                         : 'l' | 'L' ;
TOKEN_LOCK                      : 'lock' ;
TOKEN_NAN                       : 'NaN' ; // Not-a-number
TOKEN_NEGATIVE_INFINITY1        : 'negative_infinity' | 'neg_inf' ;
TOKEN_NEGATIVE_INFINITY2        : '-inf' ;
TOKEN_NONE                      : 'none' ;
TOKEN_NOT_A_NUMBER              : 'not-a-number' ;
TOKEN_NOTIFY                    : 'notify' ;
TOKEN_NULL                      : 'null' ;
TOKEN_METHOD_INVOKE             : 'method_invoke' | 'mi' ;
TOKEN_OBJECT_CREATED            : 'object_created' | 'objc' ;
TOKEN_OBJECT_RELEASED           : 'object_released' | 'objr' ;
TOKEN_OR                        : 'or' ;
TOKEN_OUT                       : 'out' ;
TOKEN_POSITION                  : 'position' | 'pos' ;
TOKEN_POSITIVE_INFINITY1        : 'positive_infinity' | 'pos_inf' ;
TOKEN_POSITIVE_INFINITY2        : '+inf' ;
TOKEN_PROPERTY_VIOLATED         : 'property_violated' | 'pv' ;
TOKEN_RETURN                    : 'return' | 'ret' ;
TOKEN_SCHEDULING                : 'scheduling' | 'sched' ;
TOKEN_SPECIFIC_INSTRUCTION      : 'specific_instruction' ;
TOKEN_STACK_FRAME               : 'stack_frame' | 'sf' ;
TOKEN_STATE_ADVANCED            : 'state_advanced' | 'sa' ;
TOKEN_STEP_IN                   : 'step_in' | 'si' ;
TOKEN_STEP_OUT                  : 'step_out' | 'sout' ;
TOKEN_STEP_OVER                 : 'step_over' | 'so' ;
TOKEN_SYNC_BLOCK                : 'sync_block' | 'sb' ;
TOKEN_THREAD                    : 'thread' | 'ti' ;
TOKEN_THREAD_SCHEDULED          : 'thread_scheduled' | 'ts' ;
TOKEN_TRUE                      : 'true' ;
TOKEN_X                         : 'x' | 'X' ;

TOKEN_LOCAL_ACCESS : 'local_access' | 'la';
TOKEN_LOCAL_READ : 'local_read' | 'lr';
TOKEN_LOCAL_WRITE : 'local_write' | 'lw';
TOKEN_METHOD : 'method';
TOKEN_CLASS : 'class';
TOKEN_NOT : 'not';


SIGN_ASTERISK                   : '*' ;
SIGN_BACK_SHLASH                : '\\' ;
SIGN_DOLAR                      : '$' ;
SIGN_DOT                        : '.' ;
SIGN_DOUBLE_QUOTE               : '"' ;
SIGN_EQUALS                     : '=' ;
SIGN_MINUS                      : '-' ;
SIGN_PLUS                       : '+' ;
SIGN_SINGLE_QUOTE               : '\'' ;


ESCAPE_SEQ_B                    : '\\b' ;
ESCAPE_SEQ_T                    : '\\t' ;
ESCAPE_SEQ_N                    : '\\n' ;
ESCAPE_SEQ_F                    : '\\f' ;
ESCAPE_SEQ_R                    : '\\r' ;
ESCAPE_SEQ_DOUBLE_QUOTE         : '\\"' ;
ESCAPE_SEQ_SINGLE_QUOTE         : '\\\'' ;
ESCAPE_SEQ_BACKSLASH            : '\\\\' ;
ESCAPE_SEQ_OCTAL                : '\\' ((('0' .. '3')( '0' .. '7')('0' .. '7')) | (( '0' .. '7')('0' .. '7')) | ( '0' .. '7'));
ESCAPE_SEQ_UNICODE              : '\\u' ('0' .. '9' | 'a' .. 'f' | 'A' .. 'F') ('0' .. '9' | 'a' .. 'f' | 'A' .. 'F') ('0' .. '9' | 'a' .. 'f' | 'A' .. 'F') ('0' .. '9' | 'a' .. 'f' | 'A' .. 'F') ;

RELOP_EQUAL                     : '==' ;
RELOP_NOT_EQUAL                 : '!=' ;
RELOP_LESS_THAN                 : '<'  ;
RELOP_LESS_OR_EQUAL_THAN        : '<=' ;
RELOP_GREATER_THAN              : '>'  ;
RELOP_GREATER_OR_EQUAL_THAN     : '>=' ;

// **************************************************************************************
// **************************************************************************************
// **************************************************************************************
// Only for internal purposes don't use directly (use idf, intValue etc) instead following tokens
        
HEX : '0' TOKEN_X ('0'..'9'|'A'..'F'|'a'..'f')+;
INT : '0' .. '9'+ ;

IDF_TEXT_INTERNAL : ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0' .. '9')* ;


CHAR 
    : SIGN_SINGLE_QUOTE ( 
         (~'\\') |     // All non excape characters
         ( ESCAPE_SEQ_B | ESCAPE_SEQ_T | ESCAPE_SEQ_N | ESCAPE_SEQ_F | ESCAPE_SEQ_R | ESCAPE_SEQ_DOUBLE_QUOTE | ESCAPE_SEQ_SINGLE_QUOTE | ESCAPE_SEQ_BACKSLASH | ESCAPE_SEQ_OCTAL | ESCAPE_SEQ_UNICODE )
       ) SIGN_SINGLE_QUOTE ;

STRING
    : SIGN_DOUBLE_QUOTE (
         (~'\\') |     // All non excape characters
         ( ESCAPE_SEQ_B | ESCAPE_SEQ_T | ESCAPE_SEQ_N | ESCAPE_SEQ_F | ESCAPE_SEQ_R | ESCAPE_SEQ_DOUBLE_QUOTE | ESCAPE_SEQ_SINGLE_QUOTE | ESCAPE_SEQ_BACKSLASH | ESCAPE_SEQ_OCTAL | ESCAPE_SEQ_UNICODE )
    )* SIGN_DOUBLE_QUOTE ;



INT_TEXT 
    : SIGN_PLUS  INT 
    | SIGN_MINUS INT
    | SIGN_PLUS  HEX
    | SIGN_MINUS HEX
    ;

LONG_TEXT 
    : SIGN_PLUS  INT TOKEN_L
    | SIGN_MINUS INT TOKEN_L
    |            INT TOKEN_L
    | SIGN_PLUS  HEX TOKEN_L
    | SIGN_MINUS HEX TOKEN_L
    |            HEX TOKEN_L
    ;
// The NaN and Infiniti are considered to be Double + (hack if assigned to float value)
FLOAT_TEXT
    : (SIGN_PLUS | SIGN_MINUS)? INT SIGN_DOT INT? (TOKEN_F)
    | (SIGN_PLUS | SIGN_MINUS)?     SIGN_DOT INT  (TOKEN_F)
    | (SIGN_PLUS | SIGN_MINUS)? INT SIGN_DOT INT? TOKEN_E (SIGN_PLUS | SIGN_MINUS)? INT (TOKEN_F) 
    | (SIGN_PLUS | SIGN_MINUS)?     SIGN_DOT INT  TOKEN_E (SIGN_PLUS | SIGN_MINUS)? INT (TOKEN_F)
    | (SIGN_PLUS | SIGN_MINUS)? INT               TOKEN_E (SIGN_PLUS | SIGN_MINUS)? INT (TOKEN_F)
    | (SIGN_PLUS | SIGN_MINUS)? INT                                                     (TOKEN_F)
    ;

// Note NaN and Infinity are parsed as idfField_name ("special semantic hack") resolves this
DOUBLE_TEXT_LIMITTED
    : (SIGN_PLUS | SIGN_MINUS)? INT SIGN_DOT INT? (TOKEN_D)?
    | (SIGN_PLUS | SIGN_MINUS)?     SIGN_DOT INT  (TOKEN_D)?
    | (SIGN_PLUS | SIGN_MINUS)? INT SIGN_DOT INT? TOKEN_E (SIGN_PLUS | SIGN_MINUS)? INT (TOKEN_D)? 
    | (SIGN_PLUS | SIGN_MINUS)?     SIGN_DOT INT  TOKEN_E (SIGN_PLUS | SIGN_MINUS)? INT (TOKEN_D)?
    | (SIGN_PLUS | SIGN_MINUS)? INT               TOKEN_E (SIGN_PLUS | SIGN_MINUS)? INT (TOKEN_D)?
    | (SIGN_PLUS | SIGN_MINUS)? INT                                                     (TOKEN_D)
    | (SIGN_PLUS | SIGN_MINUS)  TOKEN_NAN
    | (SIGN_PLUS | SIGN_MINUS)  TOKEN_INFINITY
    ;
