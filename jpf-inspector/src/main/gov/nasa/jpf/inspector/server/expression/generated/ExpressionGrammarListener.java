// Generated from D:\gsoc\jpf-inspector\src\main\gov\nasa\jpf\inspector\server\expression/ExpressionGrammar.g4 by ANTLR 4.5.3
 
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
    

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExpressionGrammarParser}.
 */
public interface ExpressionGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#allKeyWordsIDFLike}.
	 * @param ctx the parse tree
	 */
	void enterAllKeyWordsIDFLike(ExpressionGrammarParser.AllKeyWordsIDFLikeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#allKeyWordsIDFLike}.
	 * @param ctx the parse tree
	 */
	void exitAllKeyWordsIDFLike(ExpressionGrammarParser.AllKeyWordsIDFLikeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#javaKeyWords}.
	 * @param ctx the parse tree
	 */
	void enterJavaKeyWords(ExpressionGrammarParser.JavaKeyWordsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#javaKeyWords}.
	 * @param ctx the parse tree
	 */
	void exitJavaKeyWords(ExpressionGrammarParser.JavaKeyWordsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#allKeywordsOther}.
	 * @param ctx the parse tree
	 */
	void enterAllKeywordsOther(ExpressionGrammarParser.AllKeywordsOtherContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#allKeywordsOther}.
	 * @param ctx the parse tree
	 */
	void exitAllKeywordsOther(ExpressionGrammarParser.AllKeywordsOtherContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(ExpressionGrammarParser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(ExpressionGrammarParser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#fileName}.
	 * @param ctx the parse tree
	 */
	void enterFileName(ExpressionGrammarParser.FileNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#fileName}.
	 * @param ctx the parse tree
	 */
	void exitFileName(ExpressionGrammarParser.FileNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(ExpressionGrammarParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(ExpressionGrammarParser.FieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#methodName}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(ExpressionGrammarParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#methodName}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(ExpressionGrammarParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParams}.
	 * @param ctx the parse tree
	 */
	void enterCmdBreakpointsCreateParams(ExpressionGrammarParser.CmdBreakpointsCreateParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParams}.
	 * @param ctx the parse tree
	 */
	void exitCmdBreakpointsCreateParams(ExpressionGrammarParser.CmdBreakpointsCreateParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParams1}.
	 * @param ctx the parse tree
	 */
	void enterCmdBreakpointsCreateParams1(ExpressionGrammarParser.CmdBreakpointsCreateParams1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParams1}.
	 * @param ctx the parse tree
	 */
	void exitCmdBreakpointsCreateParams1(ExpressionGrammarParser.CmdBreakpointsCreateParams1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParamsAtom}.
	 * @param ctx the parse tree
	 */
	void enterCmdBreakpointsCreateParamsAtom(ExpressionGrammarParser.CmdBreakpointsCreateParamsAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParamsAtom}.
	 * @param ctx the parse tree
	 */
	void exitCmdBreakpointsCreateParamsAtom(ExpressionGrammarParser.CmdBreakpointsCreateParamsAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParamsAtomNotTerminateIDF}.
	 * @param ctx the parse tree
	 */
	void enterCmdBreakpointsCreateParamsAtomNotTerminateIDF(ExpressionGrammarParser.CmdBreakpointsCreateParamsAtomNotTerminateIDFContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParamsAtomNotTerminateIDF}.
	 * @param ctx the parse tree
	 */
	void exitCmdBreakpointsCreateParamsAtomNotTerminateIDF(ExpressionGrammarParser.CmdBreakpointsCreateParamsAtomNotTerminateIDFContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParamsAtomTerminateIDF}.
	 * @param ctx the parse tree
	 */
	void enterCmdBreakpointsCreateParamsAtomTerminateIDF(ExpressionGrammarParser.CmdBreakpointsCreateParamsAtomTerminateIDFContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdBreakpointsCreateParamsAtomTerminateIDF}.
	 * @param ctx the parse tree
	 */
	void exitCmdBreakpointsCreateParamsAtomTerminateIDF(ExpressionGrammarParser.CmdBreakpointsCreateParamsAtomTerminateIDFContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#allKeyWords}.
	 * @param ctx the parse tree
	 */
	void enterAllKeyWords(ExpressionGrammarParser.AllKeyWordsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#allKeyWords}.
	 * @param ctx the parse tree
	 */
	void exitAllKeyWords(ExpressionGrammarParser.AllKeyWordsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess(ExpressionGrammarParser.FieldAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess(ExpressionGrammarParser.FieldAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdGarbageCollectionSpec}.
	 * @param ctx the parse tree
	 */
	void enterCmdGarbageCollectionSpec(ExpressionGrammarParser.CmdGarbageCollectionSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdGarbageCollectionSpec}.
	 * @param ctx the parse tree
	 */
	void exitCmdGarbageCollectionSpec(ExpressionGrammarParser.CmdGarbageCollectionSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdChoiceGeneratorType}.
	 * @param ctx the parse tree
	 */
	void enterCmdChoiceGeneratorType(ExpressionGrammarParser.CmdChoiceGeneratorTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdChoiceGeneratorType}.
	 * @param ctx the parse tree
	 */
	void exitCmdChoiceGeneratorType(ExpressionGrammarParser.CmdChoiceGeneratorTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdInstructionTypes}.
	 * @param ctx the parse tree
	 */
	void enterCmdInstructionTypes(ExpressionGrammarParser.CmdInstructionTypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdInstructionTypes}.
	 * @param ctx the parse tree
	 */
	void exitCmdInstructionTypes(ExpressionGrammarParser.CmdInstructionTypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdThreadScheduledDirection}.
	 * @param ctx the parse tree
	 */
	void enterCmdThreadScheduledDirection(ExpressionGrammarParser.CmdThreadScheduledDirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdThreadScheduledDirection}.
	 * @param ctx the parse tree
	 */
	void exitCmdThreadScheduledDirection(ExpressionGrammarParser.CmdThreadScheduledDirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateAssignment}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateAssignment(ExpressionGrammarParser.CmdStateAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateAssignment}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateAssignment(ExpressionGrammarParser.CmdStateAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpression}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpression(ExpressionGrammarParser.CmdStateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpression}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpression(ExpressionGrammarParser.CmdStateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpression1}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpression1(ExpressionGrammarParser.CmdStateExpression1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpression1}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpression1(ExpressionGrammarParser.CmdStateExpression1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpression1Value}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpression1Value(ExpressionGrammarParser.CmdStateExpression1ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpression1Value}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpression1Value(ExpressionGrammarParser.CmdStateExpression1ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionThread}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionThread(ExpressionGrammarParser.CmdStateExpressionThreadContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionThread}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionThread(ExpressionGrammarParser.CmdStateExpressionThreadContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionThreadValue}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionThreadValue(ExpressionGrammarParser.CmdStateExpressionThreadValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionThreadValue}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionThreadValue(ExpressionGrammarParser.CmdStateExpressionThreadValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionStackFrame}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionStackFrame(ExpressionGrammarParser.CmdStateExpressionStackFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionStackFrame}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionStackFrame(ExpressionGrammarParser.CmdStateExpressionStackFrameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionStackFrameValue}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionStackFrameValue(ExpressionGrammarParser.CmdStateExpressionStackFrameValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionStackFrameValue}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionStackFrameValue(ExpressionGrammarParser.CmdStateExpressionStackFrameValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionStackFrame1}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionStackFrame1(ExpressionGrammarParser.CmdStateExpressionStackFrame1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionStackFrame1}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionStackFrame1(ExpressionGrammarParser.CmdStateExpressionStackFrame1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueAfterStackFrame}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueAfterStackFrame(ExpressionGrammarParser.CmdStateExpressionValueAfterStackFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueAfterStackFrame}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueAfterStackFrame(ExpressionGrammarParser.CmdStateExpressionValueAfterStackFrameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueFieldIndex}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueFieldIndex(ExpressionGrammarParser.CmdStateExpressionValueFieldIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueFieldIndex}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueFieldIndex(ExpressionGrammarParser.CmdStateExpressionValueFieldIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueName}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueName(ExpressionGrammarParser.CmdStateExpressionValueNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueName}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueName(ExpressionGrammarParser.CmdStateExpressionValueNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueOuterClass}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueOuterClass(ExpressionGrammarParser.CmdStateExpressionValueOuterClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueOuterClass}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueOuterClass(ExpressionGrammarParser.CmdStateExpressionValueOuterClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueStatic}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueStatic(ExpressionGrammarParser.CmdStateExpressionValueStaticContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueStatic}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueStatic(ExpressionGrammarParser.CmdStateExpressionValueStaticContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueStackFrameSlot}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueStackFrameSlot(ExpressionGrammarParser.CmdStateExpressionValueStackFrameSlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueStackFrameSlot}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueStackFrameSlot(ExpressionGrammarParser.CmdStateExpressionValueStackFrameSlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueSuper}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueSuper(ExpressionGrammarParser.CmdStateExpressionValueSuperContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueSuper}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueSuper(ExpressionGrammarParser.CmdStateExpressionValueSuperContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueThis}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueThis(ExpressionGrammarParser.CmdStateExpressionValueThisContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueThis}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueThis(ExpressionGrammarParser.CmdStateExpressionValueThisContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueArray}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValueArray(ExpressionGrammarParser.CmdStateExpressionValueArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValueArray}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValueArray(ExpressionGrammarParser.CmdStateExpressionValueArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValue}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionValue(ExpressionGrammarParser.CmdStateExpressionValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionValue}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionValue(ExpressionGrammarParser.CmdStateExpressionValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionClass}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionClass(ExpressionGrammarParser.CmdStateExpressionClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionClass}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionClass(ExpressionGrammarParser.CmdStateExpressionClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionHeap}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionHeap(ExpressionGrammarParser.CmdStateExpressionHeapContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionHeap}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionHeap(ExpressionGrammarParser.CmdStateExpressionHeapContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionHeapValue}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateExpressionHeapValue(ExpressionGrammarParser.CmdStateExpressionHeapValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateExpressionHeapValue}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateExpressionHeapValue(ExpressionGrammarParser.CmdStateExpressionHeapValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#cmdStateConstValue}.
	 * @param ctx the parse tree
	 */
	void enterCmdStateConstValue(ExpressionGrammarParser.CmdStateConstValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#cmdStateConstValue}.
	 * @param ctx the parse tree
	 */
	void exitCmdStateConstValue(ExpressionGrammarParser.CmdStateConstValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#relOp}.
	 * @param ctx the parse tree
	 */
	void enterRelOp(ExpressionGrammarParser.RelOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#relOp}.
	 * @param ctx the parse tree
	 */
	void exitRelOp(ExpressionGrammarParser.RelOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(ExpressionGrammarParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(ExpressionGrammarParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#nullValue}.
	 * @param ctx the parse tree
	 */
	void enterNullValue(ExpressionGrammarParser.NullValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#nullValue}.
	 * @param ctx the parse tree
	 */
	void exitNullValue(ExpressionGrammarParser.NullValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#charValue}.
	 * @param ctx the parse tree
	 */
	void enterCharValue(ExpressionGrammarParser.CharValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#charValue}.
	 * @param ctx the parse tree
	 */
	void exitCharValue(ExpressionGrammarParser.CharValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#stringValue}.
	 * @param ctx the parse tree
	 */
	void enterStringValue(ExpressionGrammarParser.StringValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#stringValue}.
	 * @param ctx the parse tree
	 */
	void exitStringValue(ExpressionGrammarParser.StringValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#intValue}.
	 * @param ctx the parse tree
	 */
	void enterIntValue(ExpressionGrammarParser.IntValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#intValue}.
	 * @param ctx the parse tree
	 */
	void exitIntValue(ExpressionGrammarParser.IntValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#longValue}.
	 * @param ctx the parse tree
	 */
	void enterLongValue(ExpressionGrammarParser.LongValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#longValue}.
	 * @param ctx the parse tree
	 */
	void exitLongValue(ExpressionGrammarParser.LongValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#doubleValue}.
	 * @param ctx the parse tree
	 */
	void enterDoubleValue(ExpressionGrammarParser.DoubleValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#doubleValue}.
	 * @param ctx the parse tree
	 */
	void exitDoubleValue(ExpressionGrammarParser.DoubleValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#floatValue}.
	 * @param ctx the parse tree
	 */
	void enterFloatValue(ExpressionGrammarParser.FloatValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#floatValue}.
	 * @param ctx the parse tree
	 */
	void exitFloatValue(ExpressionGrammarParser.FloatValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#doubleValueLimited}.
	 * @param ctx the parse tree
	 */
	void enterDoubleValueLimited(ExpressionGrammarParser.DoubleValueLimitedContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#doubleValueLimited}.
	 * @param ctx the parse tree
	 */
	void exitDoubleValueLimited(ExpressionGrammarParser.DoubleValueLimitedContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#doubleValueExtended}.
	 * @param ctx the parse tree
	 */
	void enterDoubleValueExtended(ExpressionGrammarParser.DoubleValueExtendedContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#doubleValueExtended}.
	 * @param ctx the parse tree
	 */
	void exitDoubleValueExtended(ExpressionGrammarParser.DoubleValueExtendedContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#doubleText}.
	 * @param ctx the parse tree
	 */
	void enterDoubleText(ExpressionGrammarParser.DoubleTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#doubleText}.
	 * @param ctx the parse tree
	 */
	void exitDoubleText(ExpressionGrammarParser.DoubleTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#idf}.
	 * @param ctx the parse tree
	 */
	void enterIdf(ExpressionGrammarParser.IdfContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#idf}.
	 * @param ctx the parse tree
	 */
	void exitIdf(ExpressionGrammarParser.IdfContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#idf_fieldName}.
	 * @param ctx the parse tree
	 */
	void enterIdf_fieldName(ExpressionGrammarParser.Idf_fieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#idf_fieldName}.
	 * @param ctx the parse tree
	 */
	void exitIdf_fieldName(ExpressionGrammarParser.Idf_fieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#classNameText}.
	 * @param ctx the parse tree
	 */
	void enterClassNameText(ExpressionGrammarParser.ClassNameTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#classNameText}.
	 * @param ctx the parse tree
	 */
	void exitClassNameText(ExpressionGrammarParser.ClassNameTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionGrammarParser#fileNameText}.
	 * @param ctx the parse tree
	 */
	void enterFileNameText(ExpressionGrammarParser.FileNameTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionGrammarParser#fileNameText}.
	 * @param ctx the parse tree
	 */
	void exitFileNameText(ExpressionGrammarParser.FileNameTextContext ctx);
}