// $ANTLR 3.5.2 D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g 2016-05-07 07:52:58
 
    package gov.nasa.jpf.inspector.server.expression.parser;
    
    import gov.nasa.jpf.inspector.server.expression.*;
    import gov.nasa.jpf.inspector.server.expression.expressions.*;
    import gov.nasa.jpf.inspector.server.programstate.relop.*;
    import gov.nasa.jpf.inspector.server.breakpoints.*;
    import gov.nasa.jpf.inspector.utils.parser.*;
    import gov.nasa.jpf.inspector.utils.*;
    import gov.nasa.jpf.inspector.interfaces.*;
    import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
    


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class ExpressionGrammarParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "CHAR", "DOUBLE_TEXT_LIMITTED", 
		"ESCAPE_SEQ_B", "ESCAPE_SEQ_BACKSLASH", "ESCAPE_SEQ_DOUBLE_QUOTE", "ESCAPE_SEQ_F", 
		"ESCAPE_SEQ_N", "ESCAPE_SEQ_OCTAL", "ESCAPE_SEQ_R", "ESCAPE_SEQ_SINGLE_QUOTE", 
		"ESCAPE_SEQ_T", "ESCAPE_SEQ_UNICODE", "FLOAT_TEXT", "HEX", "IDF_TEXT_INTERNAL", 
		"INT", "INT_TEXT", "LONG_TEXT", "RELOP_EQUAL", "RELOP_GREATER_OR_EQUAL_THAN", 
		"RELOP_GREATER_THAN", "RELOP_LESS_OR_EQUAL_THAN", "RELOP_LESS_THAN", "RELOP_NOT_EQUAL", 
		"SIGN_ASTERISK", "SIGN_BACK_SHLASH", "SIGN_DOLAR", "SIGN_DOT", "SIGN_DOUBLE_QUOTE", 
		"SIGN_EQUALS", "SIGN_MINUS", "SIGN_PLUS", "SIGN_SINGLE_QUOTE", "STRING", 
		"TOKEN_AND", "TOKEN_ANY", "TOKEN_ARRAY", "TOKEN_ASSERT", "TOKEN_B", "TOKEN_BEGIN", 
		"TOKEN_BOTH", "TOKEN_CHOICE_GENERATOR", "TOKEN_CONDITION", "TOKEN_D", 
		"TOKEN_DATA", "TOKEN_E", "TOKEN_END", "TOKEN_EXCEPTION_THROWN", "TOKEN_F", 
		"TOKEN_FALSE", "TOKEN_FIELD_ACCESS", "TOKEN_FIELD_READ", "TOKEN_FIELD_WRITE", 
		"TOKEN_GARBAGE_COLLECTION", "TOKEN_HASH_FIELD", "TOKEN_HASH_HEAP", "TOKEN_HASH_OUTER_CLASS", 
		"TOKEN_HASH_STACK_FRAME", "TOKEN_HASH_STACK_SLOT", "TOKEN_HASH_STATIC", 
		"TOKEN_HASH_SUPER", "TOKEN_HASH_THIS", "TOKEN_HASH_THREAD", "TOKEN_HIT_COUNT", 
		"TOKEN_IN", "TOKEN_INFINITY", "TOKEN_INSTRUCTION", "TOKEN_INSTRUCTION_TYPE", 
		"TOKEN_INVOKE", "TOKEN_L", "TOKEN_LOCK", "TOKEN_METHOD_INVOKE", "TOKEN_NAN", 
		"TOKEN_NEGATIVE_INFINITY1", "TOKEN_NEGATIVE_INFINITY2", "TOKEN_NONE", 
		"TOKEN_NOTIFY", "TOKEN_NOT_A_NUMBER", "TOKEN_NULL", "TOKEN_OBJECT_CREATED", 
		"TOKEN_OBJECT_RELEASED", "TOKEN_OR", "TOKEN_OUT", "TOKEN_POSITION", "TOKEN_POSITIVE_INFINITY1", 
		"TOKEN_POSITIVE_INFINITY2", "TOKEN_PROPERTY_VIOLATED", "TOKEN_RETURN", 
		"TOKEN_SCHEDULING", "TOKEN_SPECIFIC_INSTRUCTION", "TOKEN_STACK_FRAME", 
		"TOKEN_STATE_ADVANCED", "TOKEN_STEP_IN", "TOKEN_STEP_OUT", "TOKEN_STEP_OVER", 
		"TOKEN_SYNC_BLOCK", "TOKEN_THREAD", "TOKEN_THREAD_SCHEDULED", "TOKEN_TRUE", 
		"TOKEN_X", "WS", "'('", "')'", "'/'", "':'", "'['", "']'"
	};
	public static final int EOF=-1;
	public static final int T__105=105;
	public static final int T__106=106;
	public static final int T__107=107;
	public static final int T__108=108;
	public static final int T__109=109;
	public static final int T__110=110;
	public static final int CHAR=4;
	public static final int DOUBLE_TEXT_LIMITTED=5;
	public static final int ESCAPE_SEQ_B=6;
	public static final int ESCAPE_SEQ_BACKSLASH=7;
	public static final int ESCAPE_SEQ_DOUBLE_QUOTE=8;
	public static final int ESCAPE_SEQ_F=9;
	public static final int ESCAPE_SEQ_N=10;
	public static final int ESCAPE_SEQ_OCTAL=11;
	public static final int ESCAPE_SEQ_R=12;
	public static final int ESCAPE_SEQ_SINGLE_QUOTE=13;
	public static final int ESCAPE_SEQ_T=14;
	public static final int ESCAPE_SEQ_UNICODE=15;
	public static final int FLOAT_TEXT=16;
	public static final int HEX=17;
	public static final int IDF_TEXT_INTERNAL=18;
	public static final int INT=19;
	public static final int INT_TEXT=20;
	public static final int LONG_TEXT=21;
	public static final int RELOP_EQUAL=22;
	public static final int RELOP_GREATER_OR_EQUAL_THAN=23;
	public static final int RELOP_GREATER_THAN=24;
	public static final int RELOP_LESS_OR_EQUAL_THAN=25;
	public static final int RELOP_LESS_THAN=26;
	public static final int RELOP_NOT_EQUAL=27;
	public static final int SIGN_ASTERISK=28;
	public static final int SIGN_BACK_SHLASH=29;
	public static final int SIGN_DOLAR=30;
	public static final int SIGN_DOT=31;
	public static final int SIGN_DOUBLE_QUOTE=32;
	public static final int SIGN_EQUALS=33;
	public static final int SIGN_MINUS=34;
	public static final int SIGN_PLUS=35;
	public static final int SIGN_SINGLE_QUOTE=36;
	public static final int STRING=37;
	public static final int TOKEN_AND=38;
	public static final int TOKEN_ANY=39;
	public static final int TOKEN_ARRAY=40;
	public static final int TOKEN_ASSERT=41;
	public static final int TOKEN_B=42;
	public static final int TOKEN_BEGIN=43;
	public static final int TOKEN_BOTH=44;
	public static final int TOKEN_CHOICE_GENERATOR=45;
	public static final int TOKEN_CONDITION=46;
	public static final int TOKEN_D=47;
	public static final int TOKEN_DATA=48;
	public static final int TOKEN_E=49;
	public static final int TOKEN_END=50;
	public static final int TOKEN_EXCEPTION_THROWN=51;
	public static final int TOKEN_F=52;
	public static final int TOKEN_FALSE=53;
	public static final int TOKEN_FIELD_ACCESS=54;
	public static final int TOKEN_FIELD_READ=55;
	public static final int TOKEN_FIELD_WRITE=56;
	public static final int TOKEN_GARBAGE_COLLECTION=57;
	public static final int TOKEN_HASH_FIELD=58;
	public static final int TOKEN_HASH_HEAP=59;
	public static final int TOKEN_HASH_OUTER_CLASS=60;
	public static final int TOKEN_HASH_STACK_FRAME=61;
	public static final int TOKEN_HASH_STACK_SLOT=62;
	public static final int TOKEN_HASH_STATIC=63;
	public static final int TOKEN_HASH_SUPER=64;
	public static final int TOKEN_HASH_THIS=65;
	public static final int TOKEN_HASH_THREAD=66;
	public static final int TOKEN_HIT_COUNT=67;
	public static final int TOKEN_IN=68;
	public static final int TOKEN_INFINITY=69;
	public static final int TOKEN_INSTRUCTION=70;
	public static final int TOKEN_INSTRUCTION_TYPE=71;
	public static final int TOKEN_INVOKE=72;
	public static final int TOKEN_L=73;
	public static final int TOKEN_LOCK=74;
	public static final int TOKEN_METHOD_INVOKE=75;
	public static final int TOKEN_NAN=76;
	public static final int TOKEN_NEGATIVE_INFINITY1=77;
	public static final int TOKEN_NEGATIVE_INFINITY2=78;
	public static final int TOKEN_NONE=79;
	public static final int TOKEN_NOTIFY=80;
	public static final int TOKEN_NOT_A_NUMBER=81;
	public static final int TOKEN_NULL=82;
	public static final int TOKEN_OBJECT_CREATED=83;
	public static final int TOKEN_OBJECT_RELEASED=84;
	public static final int TOKEN_OR=85;
	public static final int TOKEN_OUT=86;
	public static final int TOKEN_POSITION=87;
	public static final int TOKEN_POSITIVE_INFINITY1=88;
	public static final int TOKEN_POSITIVE_INFINITY2=89;
	public static final int TOKEN_PROPERTY_VIOLATED=90;
	public static final int TOKEN_RETURN=91;
	public static final int TOKEN_SCHEDULING=92;
	public static final int TOKEN_SPECIFIC_INSTRUCTION=93;
	public static final int TOKEN_STACK_FRAME=94;
	public static final int TOKEN_STATE_ADVANCED=95;
	public static final int TOKEN_STEP_IN=96;
	public static final int TOKEN_STEP_OUT=97;
	public static final int TOKEN_STEP_OVER=98;
	public static final int TOKEN_SYNC_BLOCK=99;
	public static final int TOKEN_THREAD=100;
	public static final int TOKEN_THREAD_SCHEDULED=101;
	public static final int TOKEN_TRUE=102;
	public static final int TOKEN_X=103;
	public static final int WS=104;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public ExpressionGrammarParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public ExpressionGrammarParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return ExpressionGrammarParser.tokenNames; }
	@Override public String getGrammarFileName() { return "D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g"; }


	    public void displayRecognitionError(String[] tokenNames,
	                                        RecognitionException e) {
	        String hdr = getErrorHeader(e);
	        String msg = getErrorMessage(e, tokenNames); 
	        throw new RecognitionRuntimeException(hdr + " " + msg, e);
	    }



	// $ANTLR start "allKeyWordsIDFLike"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:146:1: allKeyWordsIDFLike : ( TOKEN_AND | TOKEN_ANY | TOKEN_ARRAY | TOKEN_ASSERT | TOKEN_BEGIN | TOKEN_BOTH | TOKEN_CHOICE_GENERATOR | TOKEN_CONDITION | TOKEN_D | TOKEN_DATA | TOKEN_E | TOKEN_F | TOKEN_END | TOKEN_EXCEPTION_THROWN | TOKEN_FIELD_ACCESS | TOKEN_FIELD_READ | TOKEN_FIELD_WRITE | TOKEN_GARBAGE_COLLECTION | TOKEN_HIT_COUNT | TOKEN_IN | TOKEN_INFINITY | TOKEN_INSTRUCTION | TOKEN_INSTRUCTION_TYPE | TOKEN_INVOKE | TOKEN_L | TOKEN_LOCK | TOKEN_NAN | TOKEN_NEGATIVE_INFINITY1 | TOKEN_NONE | TOKEN_NOTIFY | TOKEN_METHOD_INVOKE | TOKEN_OBJECT_CREATED | TOKEN_OBJECT_RELEASED | TOKEN_OR | TOKEN_POSITION | TOKEN_POSITIVE_INFINITY1 | TOKEN_PROPERTY_VIOLATED | TOKEN_RETURN | TOKEN_SCHEDULING | TOKEN_STACK_FRAME | TOKEN_STATE_ADVANCED | TOKEN_STEP_IN | TOKEN_STEP_OUT | TOKEN_STEP_OVER | TOKEN_SYNC_BLOCK | TOKEN_THREAD | TOKEN_THREAD_SCHEDULED | TOKEN_X );
	public final void allKeyWordsIDFLike() throws RecognitionException {
		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:147:5: ( TOKEN_AND | TOKEN_ANY | TOKEN_ARRAY | TOKEN_ASSERT | TOKEN_BEGIN | TOKEN_BOTH | TOKEN_CHOICE_GENERATOR | TOKEN_CONDITION | TOKEN_D | TOKEN_DATA | TOKEN_E | TOKEN_F | TOKEN_END | TOKEN_EXCEPTION_THROWN | TOKEN_FIELD_ACCESS | TOKEN_FIELD_READ | TOKEN_FIELD_WRITE | TOKEN_GARBAGE_COLLECTION | TOKEN_HIT_COUNT | TOKEN_IN | TOKEN_INFINITY | TOKEN_INSTRUCTION | TOKEN_INSTRUCTION_TYPE | TOKEN_INVOKE | TOKEN_L | TOKEN_LOCK | TOKEN_NAN | TOKEN_NEGATIVE_INFINITY1 | TOKEN_NONE | TOKEN_NOTIFY | TOKEN_METHOD_INVOKE | TOKEN_OBJECT_CREATED | TOKEN_OBJECT_RELEASED | TOKEN_OR | TOKEN_POSITION | TOKEN_POSITIVE_INFINITY1 | TOKEN_PROPERTY_VIOLATED | TOKEN_RETURN | TOKEN_SCHEDULING | TOKEN_STACK_FRAME | TOKEN_STATE_ADVANCED | TOKEN_STEP_IN | TOKEN_STEP_OUT | TOKEN_STEP_OVER | TOKEN_SYNC_BLOCK | TOKEN_THREAD | TOKEN_THREAD_SCHEDULED | TOKEN_X )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:
			{
			if ( (input.LA(1) >= TOKEN_AND && input.LA(1) <= TOKEN_ASSERT)||(input.LA(1) >= TOKEN_BEGIN && input.LA(1) <= TOKEN_F)||(input.LA(1) >= TOKEN_FIELD_ACCESS && input.LA(1) <= TOKEN_GARBAGE_COLLECTION)||(input.LA(1) >= TOKEN_HIT_COUNT && input.LA(1) <= TOKEN_NEGATIVE_INFINITY1)||(input.LA(1) >= TOKEN_NONE && input.LA(1) <= TOKEN_NOTIFY)||(input.LA(1) >= TOKEN_OBJECT_CREATED && input.LA(1) <= TOKEN_OR)||(input.LA(1) >= TOKEN_POSITION && input.LA(1) <= TOKEN_POSITIVE_INFINITY1)||(input.LA(1) >= TOKEN_PROPERTY_VIOLATED && input.LA(1) <= TOKEN_SCHEDULING)||(input.LA(1) >= TOKEN_STACK_FRAME && input.LA(1) <= TOKEN_THREAD_SCHEDULED)||input.LA(1)==TOKEN_X ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "allKeyWordsIDFLike"


	public static class javaKeyWords_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "javaKeyWords"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:198:1: javaKeyWords : ( TOKEN_FALSE | TOKEN_NULL | TOKEN_TRUE );
	public final ExpressionGrammarParser.javaKeyWords_return javaKeyWords() throws RecognitionException {
		ExpressionGrammarParser.javaKeyWords_return retval = new ExpressionGrammarParser.javaKeyWords_return();
		retval.start = input.LT(1);

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:199:5: ( TOKEN_FALSE | TOKEN_NULL | TOKEN_TRUE )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:
			{
			if ( input.LA(1)==TOKEN_FALSE||input.LA(1)==TOKEN_NULL||input.LA(1)==TOKEN_TRUE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "javaKeyWords"



	// $ANTLR start "allKeywordsOther"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:205:1: allKeywordsOther : ( TOKEN_HASH_FIELD | TOKEN_HASH_HEAP | TOKEN_HASH_STACK_FRAME | TOKEN_HASH_STACK_SLOT | TOKEN_HASH_THREAD | TOKEN_HASH_THIS | TOKEN_HASH_STATIC | TOKEN_HASH_SUPER | TOKEN_NEGATIVE_INFINITY2 | TOKEN_NOT_A_NUMBER | TOKEN_POSITIVE_INFINITY2 | SIGN_ASTERISK | SIGN_BACK_SHLASH | SIGN_DOLAR | SIGN_DOT | SIGN_DOUBLE_QUOTE | SIGN_EQUALS | SIGN_MINUS | SIGN_PLUS | SIGN_SINGLE_QUOTE | ESCAPE_SEQ_B | ESCAPE_SEQ_T | ESCAPE_SEQ_N | ESCAPE_SEQ_F | ESCAPE_SEQ_R | ESCAPE_SEQ_DOUBLE_QUOTE | ESCAPE_SEQ_SINGLE_QUOTE | ESCAPE_SEQ_BACKSLASH | ESCAPE_SEQ_OCTAL | ESCAPE_SEQ_UNICODE | RELOP_EQUAL | RELOP_NOT_EQUAL | RELOP_LESS_THAN | RELOP_LESS_OR_EQUAL_THAN | RELOP_GREATER_THAN | RELOP_GREATER_OR_EQUAL_THAN );
	public final void allKeywordsOther() throws RecognitionException {
		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:206:5: ( TOKEN_HASH_FIELD | TOKEN_HASH_HEAP | TOKEN_HASH_STACK_FRAME | TOKEN_HASH_STACK_SLOT | TOKEN_HASH_THREAD | TOKEN_HASH_THIS | TOKEN_HASH_STATIC | TOKEN_HASH_SUPER | TOKEN_NEGATIVE_INFINITY2 | TOKEN_NOT_A_NUMBER | TOKEN_POSITIVE_INFINITY2 | SIGN_ASTERISK | SIGN_BACK_SHLASH | SIGN_DOLAR | SIGN_DOT | SIGN_DOUBLE_QUOTE | SIGN_EQUALS | SIGN_MINUS | SIGN_PLUS | SIGN_SINGLE_QUOTE | ESCAPE_SEQ_B | ESCAPE_SEQ_T | ESCAPE_SEQ_N | ESCAPE_SEQ_F | ESCAPE_SEQ_R | ESCAPE_SEQ_DOUBLE_QUOTE | ESCAPE_SEQ_SINGLE_QUOTE | ESCAPE_SEQ_BACKSLASH | ESCAPE_SEQ_OCTAL | ESCAPE_SEQ_UNICODE | RELOP_EQUAL | RELOP_NOT_EQUAL | RELOP_LESS_THAN | RELOP_LESS_OR_EQUAL_THAN | RELOP_GREATER_THAN | RELOP_GREATER_OR_EQUAL_THAN )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:
			{
			if ( (input.LA(1) >= ESCAPE_SEQ_B && input.LA(1) <= ESCAPE_SEQ_UNICODE)||(input.LA(1) >= RELOP_EQUAL && input.LA(1) <= SIGN_SINGLE_QUOTE)||(input.LA(1) >= TOKEN_HASH_FIELD && input.LA(1) <= TOKEN_HASH_HEAP)||(input.LA(1) >= TOKEN_HASH_STACK_FRAME && input.LA(1) <= TOKEN_HASH_THREAD)||input.LA(1)==TOKEN_NEGATIVE_INFINITY2||input.LA(1)==TOKEN_NOT_A_NUMBER||input.LA(1)==TOKEN_POSITIVE_INFINITY2 ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "allKeywordsOther"



	// $ANTLR start "className"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:245:1: className returns [ClassName cn] : classNameText ;
	public final ClassName className() throws RecognitionException {
		ClassName cn = null;


		ParserRuleReturnScope classNameText1 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:246:5: ( classNameText )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:246:7: classNameText
			{
			pushFollow(FOLLOW_classNameText_in_className3289);
			classNameText1=classNameText();
			state._fsp--;

			 cn = new ClassName((classNameText1!=null?input.toString(classNameText1.start,classNameText1.stop):null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return cn;
	}
	// $ANTLR end "className"



	// $ANTLR start "fileName"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:249:1: fileName returns [String text] : fileNameText ;
	public final String fileName() throws RecognitionException {
		String text = null;


		ParserRuleReturnScope fileNameText2 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:250:5: ( fileNameText )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:250:7: fileNameText
			{
			pushFollow(FOLLOW_fileNameText_in_fileName3321);
			fileNameText2=fileNameText();
			state._fsp--;

			 text = (fileNameText2!=null?input.toString(fileNameText2.start,fileNameText2.stop):null); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return text;
	}
	// $ANTLR end "fileName"



	// $ANTLR start "fieldName"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:253:1: fieldName[ClassName cn] returns [FieldName fn] : idf ;
	public final FieldName fieldName(ClassName cn) throws RecognitionException {
		FieldName fn = null;


		ParserRuleReturnScope idf3 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:254:5: ( idf )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:254:7: idf
			{
			pushFollow(FOLLOW_idf_in_fieldName3356);
			idf3=idf();
			state._fsp--;

			 fn = new FieldName((idf3!=null?input.toString(idf3.start,idf3.stop):null), cn); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return fn;
	}
	// $ANTLR end "fieldName"



	// $ANTLR start "methodName"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:257:1: methodName[ClassName cn] returns [MethodName mn] : idf ;
	public final MethodName methodName(ClassName cn) throws RecognitionException {
		MethodName mn = null;


		ParserRuleReturnScope idf4 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:258:5: ( idf )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:258:7: idf
			{
			pushFollow(FOLLOW_idf_in_methodName3400);
			idf4=idf();
			state._fsp--;

			 mn = new MethodName((idf4!=null?input.toString(idf4.start,idf4.stop):null), cn); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return mn;
	}
	// $ANTLR end "methodName"



	// $ANTLR start "cmdBreakpointsCreateParams"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:263:1: cmdBreakpointsCreateParams[ExpressionFactory expFactory] returns [ExpressionBoolean bp] : a= cmdBreakpointsCreateParams1[expFactory] EOF ;
	public final ExpressionBoolean cmdBreakpointsCreateParams(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionBoolean bp = null;


		ExpressionBoolean a =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:264:5: (a= cmdBreakpointsCreateParams1[expFactory] EOF )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:264:7: a= cmdBreakpointsCreateParams1[expFactory] EOF
			{
			pushFollow(FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams3451);
			a=cmdBreakpointsCreateParams1(expFactory);
			state._fsp--;

			bp = a; 
			match(input,EOF,FOLLOW_EOF_in_cmdBreakpointsCreateParams3457); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bp;
	}
	// $ANTLR end "cmdBreakpointsCreateParams"



	// $ANTLR start "cmdBreakpointsCreateParams1"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:267:1: cmdBreakpointsCreateParams1[ExpressionFactory expFactory] returns [ExpressionBoolean bp] : (a= cmdBreakpoinstCreateParamsAtomNotTerminateIDF[expFactory] ( ( TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )? |a= cmdBreakpoinstCreateParamsAtomTerminateIDF[expFactory] ( ( WS TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( WS TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )? );
	public final ExpressionBoolean cmdBreakpointsCreateParams1(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionBoolean bp = null;


		ExpressionBoolean a =null;
		ExpressionBoolean b =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:268:5: (a= cmdBreakpoinstCreateParamsAtomNotTerminateIDF[expFactory] ( ( TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )? |a= cmdBreakpoinstCreateParamsAtomTerminateIDF[expFactory] ( ( WS TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( WS TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )? )
			int alt3=2;
			switch ( input.LA(1) ) {
			case WS:
				{
				switch ( input.LA(2) ) {
				case TOKEN_GARBAGE_COLLECTION:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_15 = input.LA(4);
						if ( (LA3_15==SIGN_EQUALS) ) {
							alt3=1;
						}
						else if ( ((LA3_15 >= RELOP_EQUAL && LA3_15 <= RELOP_NOT_EQUAL)||LA3_15==SIGN_DOT||LA3_15==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 15, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_CHOICE_GENERATOR:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_16 = input.LA(4);
						if ( (LA3_16==SIGN_EQUALS) ) {
							alt3=1;
						}
						else if ( ((LA3_16 >= RELOP_EQUAL && LA3_16 <= RELOP_NOT_EQUAL)||LA3_16==SIGN_DOT||LA3_16==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 16, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_INSTRUCTION_TYPE:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_17 = input.LA(4);
						if ( (LA3_17==SIGN_EQUALS) ) {
							alt3=1;
						}
						else if ( ((LA3_17 >= RELOP_EQUAL && LA3_17 <= RELOP_NOT_EQUAL)||LA3_17==SIGN_DOT||LA3_17==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 17, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_POSITION:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_18 = input.LA(4);
						if ( (LA3_18==SIGN_EQUALS) ) {
							alt3=1;
						}
						else if ( ((LA3_18 >= RELOP_EQUAL && LA3_18 <= RELOP_NOT_EQUAL)||LA3_18==SIGN_DOT||LA3_18==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 18, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_PROPERTY_VIOLATED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_19 = input.LA(4);
						if ( (LA3_19==EOF||LA3_19==TOKEN_AND||LA3_19==TOKEN_OR||LA3_19==106) ) {
							alt3=1;
						}
						else if ( ((LA3_19 >= RELOP_EQUAL && LA3_19 <= RELOP_NOT_EQUAL)||LA3_19==SIGN_DOT||LA3_19==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 19, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
					case TOKEN_AND:
					case TOKEN_OR:
					case 106:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 6, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_THREAD_SCHEDULED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_20 = input.LA(4);
						if ( (LA3_20==SIGN_EQUALS) ) {
							alt3=1;
						}
						else if ( ((LA3_20 >= RELOP_EQUAL && LA3_20 <= RELOP_NOT_EQUAL)||LA3_20==SIGN_DOT||LA3_20==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 20, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_STATE_ADVANCED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_21 = input.LA(4);
						if ( (LA3_21==EOF||LA3_21==TOKEN_AND||LA3_21==TOKEN_OR||LA3_21==106) ) {
							alt3=1;
						}
						else if ( ((LA3_21 >= RELOP_EQUAL && LA3_21 <= RELOP_NOT_EQUAL)||LA3_21==SIGN_DOT||LA3_21==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 21, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
					case TOKEN_AND:
					case TOKEN_OR:
					case 106:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_STEP_IN:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_22 = input.LA(4);
						if ( (LA3_22==EOF||LA3_22==TOKEN_AND||LA3_22==TOKEN_OR||LA3_22==106) ) {
							alt3=1;
						}
						else if ( ((LA3_22 >= RELOP_EQUAL && LA3_22 <= RELOP_NOT_EQUAL)||LA3_22==SIGN_DOT||LA3_22==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 22, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
					case TOKEN_AND:
					case TOKEN_OR:
					case 106:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 9, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_STEP_OVER:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_23 = input.LA(4);
						if ( (LA3_23==EOF||LA3_23==TOKEN_AND||LA3_23==TOKEN_OR||LA3_23==106) ) {
							alt3=1;
						}
						else if ( ((LA3_23 >= RELOP_EQUAL && LA3_23 <= RELOP_NOT_EQUAL)||LA3_23==SIGN_DOT||LA3_23==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 23, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
					case TOKEN_AND:
					case TOKEN_OR:
					case 106:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_SPECIFIC_INSTRUCTION:
				case 105:
					{
					alt3=1;
					}
					break;
				case TOKEN_STEP_OUT:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_24 = input.LA(4);
						if ( (LA3_24==TOKEN_THREAD) ) {
							alt3=1;
						}
						else if ( ((LA3_24 >= RELOP_EQUAL && LA3_24 <= RELOP_NOT_EQUAL)||LA3_24==SIGN_DOT||LA3_24==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 24, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case TOKEN_THREAD:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 12, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_ASSERT:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA3_25 = input.LA(4);
						if ( (LA3_25==105) ) {
							alt3=1;
						}
						else if ( ((LA3_25 >= RELOP_EQUAL && LA3_25 <= RELOP_NOT_EQUAL)||LA3_25==SIGN_DOT||LA3_25==109) ) {
							alt3=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 25, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 105:
						{
						alt3=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt3=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 13, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case CHAR:
				case DOUBLE_TEXT_LIMITTED:
				case FLOAT_TEXT:
				case HEX:
				case IDF_TEXT_INTERNAL:
				case INT:
				case INT_TEXT:
				case LONG_TEXT:
				case STRING:
				case TOKEN_AND:
				case TOKEN_ANY:
				case TOKEN_ARRAY:
				case TOKEN_BEGIN:
				case TOKEN_BOTH:
				case TOKEN_CONDITION:
				case TOKEN_D:
				case TOKEN_DATA:
				case TOKEN_E:
				case TOKEN_END:
				case TOKEN_EXCEPTION_THROWN:
				case TOKEN_F:
				case TOKEN_FALSE:
				case TOKEN_FIELD_ACCESS:
				case TOKEN_FIELD_READ:
				case TOKEN_FIELD_WRITE:
				case TOKEN_HASH_FIELD:
				case TOKEN_HASH_HEAP:
				case TOKEN_HASH_OUTER_CLASS:
				case TOKEN_HASH_STACK_FRAME:
				case TOKEN_HASH_STACK_SLOT:
				case TOKEN_HASH_STATIC:
				case TOKEN_HASH_SUPER:
				case TOKEN_HASH_THIS:
				case TOKEN_HASH_THREAD:
				case TOKEN_HIT_COUNT:
				case TOKEN_IN:
				case TOKEN_INFINITY:
				case TOKEN_INSTRUCTION:
				case TOKEN_INVOKE:
				case TOKEN_L:
				case TOKEN_LOCK:
				case TOKEN_METHOD_INVOKE:
				case TOKEN_NAN:
				case TOKEN_NEGATIVE_INFINITY1:
				case TOKEN_NEGATIVE_INFINITY2:
				case TOKEN_NONE:
				case TOKEN_NOTIFY:
				case TOKEN_NOT_A_NUMBER:
				case TOKEN_NULL:
				case TOKEN_OBJECT_CREATED:
				case TOKEN_OBJECT_RELEASED:
				case TOKEN_OR:
				case TOKEN_POSITIVE_INFINITY1:
				case TOKEN_POSITIVE_INFINITY2:
				case TOKEN_RETURN:
				case TOKEN_SCHEDULING:
				case TOKEN_STACK_FRAME:
				case TOKEN_SYNC_BLOCK:
				case TOKEN_THREAD:
				case TOKEN_TRUE:
				case TOKEN_X:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_GARBAGE_COLLECTION:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_15 = input.LA(3);
					if ( (LA3_15==SIGN_EQUALS) ) {
						alt3=1;
					}
					else if ( ((LA3_15 >= RELOP_EQUAL && LA3_15 <= RELOP_NOT_EQUAL)||LA3_15==SIGN_DOT||LA3_15==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 15, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_CHOICE_GENERATOR:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_16 = input.LA(3);
					if ( (LA3_16==SIGN_EQUALS) ) {
						alt3=1;
					}
					else if ( ((LA3_16 >= RELOP_EQUAL && LA3_16 <= RELOP_NOT_EQUAL)||LA3_16==SIGN_DOT||LA3_16==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 16, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_INSTRUCTION_TYPE:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_17 = input.LA(3);
					if ( (LA3_17==SIGN_EQUALS) ) {
						alt3=1;
					}
					else if ( ((LA3_17 >= RELOP_EQUAL && LA3_17 <= RELOP_NOT_EQUAL)||LA3_17==SIGN_DOT||LA3_17==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 17, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_POSITION:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_18 = input.LA(3);
					if ( (LA3_18==SIGN_EQUALS) ) {
						alt3=1;
					}
					else if ( ((LA3_18 >= RELOP_EQUAL && LA3_18 <= RELOP_NOT_EQUAL)||LA3_18==SIGN_DOT||LA3_18==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 18, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_PROPERTY_VIOLATED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_19 = input.LA(3);
					if ( (LA3_19==EOF||LA3_19==TOKEN_AND||LA3_19==TOKEN_OR||LA3_19==106) ) {
						alt3=1;
					}
					else if ( ((LA3_19 >= RELOP_EQUAL && LA3_19 <= RELOP_NOT_EQUAL)||LA3_19==SIGN_DOT||LA3_19==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 19, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
				case TOKEN_AND:
				case TOKEN_OR:
				case 106:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_THREAD_SCHEDULED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_20 = input.LA(3);
					if ( (LA3_20==SIGN_EQUALS) ) {
						alt3=1;
					}
					else if ( ((LA3_20 >= RELOP_EQUAL && LA3_20 <= RELOP_NOT_EQUAL)||LA3_20==SIGN_DOT||LA3_20==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 20, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_STATE_ADVANCED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_21 = input.LA(3);
					if ( (LA3_21==EOF||LA3_21==TOKEN_AND||LA3_21==TOKEN_OR||LA3_21==106) ) {
						alt3=1;
					}
					else if ( ((LA3_21 >= RELOP_EQUAL && LA3_21 <= RELOP_NOT_EQUAL)||LA3_21==SIGN_DOT||LA3_21==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 21, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
				case TOKEN_AND:
				case TOKEN_OR:
				case 106:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_STEP_IN:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_22 = input.LA(3);
					if ( (LA3_22==EOF||LA3_22==TOKEN_AND||LA3_22==TOKEN_OR||LA3_22==106) ) {
						alt3=1;
					}
					else if ( ((LA3_22 >= RELOP_EQUAL && LA3_22 <= RELOP_NOT_EQUAL)||LA3_22==SIGN_DOT||LA3_22==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 22, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
				case TOKEN_AND:
				case TOKEN_OR:
				case 106:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_STEP_OVER:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_23 = input.LA(3);
					if ( (LA3_23==EOF||LA3_23==TOKEN_AND||LA3_23==TOKEN_OR||LA3_23==106) ) {
						alt3=1;
					}
					else if ( ((LA3_23 >= RELOP_EQUAL && LA3_23 <= RELOP_NOT_EQUAL)||LA3_23==SIGN_DOT||LA3_23==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 23, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
				case TOKEN_AND:
				case TOKEN_OR:
				case 106:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_SPECIFIC_INSTRUCTION:
			case 105:
				{
				alt3=1;
				}
				break;
			case TOKEN_STEP_OUT:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_24 = input.LA(3);
					if ( (LA3_24==TOKEN_THREAD) ) {
						alt3=1;
					}
					else if ( ((LA3_24 >= RELOP_EQUAL && LA3_24 <= RELOP_NOT_EQUAL)||LA3_24==SIGN_DOT||LA3_24==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 24, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case TOKEN_THREAD:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_ASSERT:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA3_25 = input.LA(3);
					if ( (LA3_25==105) ) {
						alt3=1;
					}
					else if ( ((LA3_25 >= RELOP_EQUAL && LA3_25 <= RELOP_NOT_EQUAL)||LA3_25==SIGN_DOT||LA3_25==109) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 25, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 105:
					{
					alt3=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt3=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case CHAR:
			case DOUBLE_TEXT_LIMITTED:
			case FLOAT_TEXT:
			case HEX:
			case IDF_TEXT_INTERNAL:
			case INT:
			case INT_TEXT:
			case LONG_TEXT:
			case STRING:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FALSE:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_HEAP:
			case TOKEN_HASH_OUTER_CLASS:
			case TOKEN_HASH_STACK_FRAME:
			case TOKEN_HASH_STACK_SLOT:
			case TOKEN_HASH_STATIC:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
			case TOKEN_HASH_THREAD:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NEGATIVE_INFINITY2:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_NOT_A_NUMBER:
			case TOKEN_NULL:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_POSITIVE_INFINITY2:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_TRUE:
			case TOKEN_X:
				{
				alt3=2;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:268:7: a= cmdBreakpoinstCreateParamsAtomNotTerminateIDF[expFactory] ( ( TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )?
					{
					pushFollow(FOLLOW_cmdBreakpoinstCreateParamsAtomNotTerminateIDF_in_cmdBreakpointsCreateParams13483);
					a=cmdBreakpoinstCreateParamsAtomNotTerminateIDF(expFactory);
					state._fsp--;

					bp = a; 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:269:12: ( ( TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )?
					int alt1=3;
					int LA1_0 = input.LA(1);
					if ( (LA1_0==TOKEN_AND) ) {
						alt1=1;
					}
					else if ( (LA1_0==TOKEN_OR) ) {
						alt1=2;
					}
					switch (alt1) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:269:14: ( TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] )
							{
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:269:14: ( TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] )
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:269:15: TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory]
							{
							match(input,TOKEN_AND,FOLLOW_TOKEN_AND_in_cmdBreakpointsCreateParams13502); 
							pushFollow(FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13506);
							b=cmdBreakpointsCreateParams1(expFactory);
							state._fsp--;

							 bp = expFactory.getBreakpointOperatorAnd(a, b); 
							}

							}
							break;
						case 2 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:270:14: ( TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] )
							{
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:270:14: ( TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] )
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:270:15: TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory]
							{
							match(input,TOKEN_OR,FOLLOW_TOKEN_OR_in_cmdBreakpointsCreateParams13529); 
							pushFollow(FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13534);
							b=cmdBreakpointsCreateParams1(expFactory);
							state._fsp--;

							 bp = expFactory.getBreakpointOperatorOr (a, b); 
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:272:7: a= cmdBreakpoinstCreateParamsAtomTerminateIDF[expFactory] ( ( WS TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( WS TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )?
					{
					pushFollow(FOLLOW_cmdBreakpoinstCreateParamsAtomTerminateIDF_in_cmdBreakpointsCreateParams13563);
					a=cmdBreakpoinstCreateParamsAtomTerminateIDF(expFactory);
					state._fsp--;

					bp = a; 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:273:12: ( ( WS TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] ) | ( WS TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] ) )?
					int alt2=3;
					int LA2_0 = input.LA(1);
					if ( (LA2_0==WS) ) {
						int LA2_1 = input.LA(2);
						if ( (LA2_1==TOKEN_AND) ) {
							alt2=1;
						}
						else if ( (LA2_1==TOKEN_OR) ) {
							alt2=2;
						}
					}
					switch (alt2) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:273:14: ( WS TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] )
							{
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:273:14: ( WS TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory] )
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:273:15: WS TOKEN_AND b= cmdBreakpointsCreateParams1[expFactory]
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpointsCreateParams13582); 
							match(input,TOKEN_AND,FOLLOW_TOKEN_AND_in_cmdBreakpointsCreateParams13584); 
							pushFollow(FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13588);
							b=cmdBreakpointsCreateParams1(expFactory);
							state._fsp--;

							 bp = expFactory.getBreakpointOperatorAnd(a, b); 
							}

							}
							break;
						case 2 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:274:14: ( WS TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] )
							{
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:274:14: ( WS TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory] )
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:274:15: WS TOKEN_OR b= cmdBreakpointsCreateParams1[expFactory]
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpointsCreateParams13611); 
							match(input,TOKEN_OR,FOLLOW_TOKEN_OR_in_cmdBreakpointsCreateParams13613); 
							pushFollow(FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13618);
							b=cmdBreakpointsCreateParams1(expFactory);
							state._fsp--;

							 bp = expFactory.getBreakpointOperatorOr (a, b); 
							}

							}
							break;

					}

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bp;
	}
	// $ANTLR end "cmdBreakpointsCreateParams1"



	// $ANTLR start "cmdBreakpointsCreateParamsAtom"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:279:1: cmdBreakpointsCreateParamsAtom[ExpressionFactory expFactory] returns [ExpressionBoolean bp] : (a= cmdBreakpoinstCreateParamsAtomNotTerminateIDF[expFactory] EOF |a= cmdBreakpoinstCreateParamsAtomTerminateIDF[expFactory] EOF );
	public final ExpressionBoolean cmdBreakpointsCreateParamsAtom(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionBoolean bp = null;


		ExpressionBoolean a =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:280:5: (a= cmdBreakpoinstCreateParamsAtomNotTerminateIDF[expFactory] EOF |a= cmdBreakpoinstCreateParamsAtomTerminateIDF[expFactory] EOF )
			int alt4=2;
			switch ( input.LA(1) ) {
			case WS:
				{
				switch ( input.LA(2) ) {
				case TOKEN_GARBAGE_COLLECTION:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_15 = input.LA(4);
						if ( (LA4_15==SIGN_EQUALS) ) {
							alt4=1;
						}
						else if ( ((LA4_15 >= RELOP_EQUAL && LA4_15 <= RELOP_NOT_EQUAL)||LA4_15==SIGN_DOT||LA4_15==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 15, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_CHOICE_GENERATOR:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_16 = input.LA(4);
						if ( (LA4_16==SIGN_EQUALS) ) {
							alt4=1;
						}
						else if ( ((LA4_16 >= RELOP_EQUAL && LA4_16 <= RELOP_NOT_EQUAL)||LA4_16==SIGN_DOT||LA4_16==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 16, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_INSTRUCTION_TYPE:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_17 = input.LA(4);
						if ( (LA4_17==SIGN_EQUALS) ) {
							alt4=1;
						}
						else if ( ((LA4_17 >= RELOP_EQUAL && LA4_17 <= RELOP_NOT_EQUAL)||LA4_17==SIGN_DOT||LA4_17==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 17, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_POSITION:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_18 = input.LA(4);
						if ( (LA4_18==SIGN_EQUALS) ) {
							alt4=1;
						}
						else if ( ((LA4_18 >= RELOP_EQUAL && LA4_18 <= RELOP_NOT_EQUAL)||LA4_18==SIGN_DOT||LA4_18==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 18, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_PROPERTY_VIOLATED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_19 = input.LA(4);
						if ( (LA4_19==EOF) ) {
							alt4=1;
						}
						else if ( ((LA4_19 >= RELOP_EQUAL && LA4_19 <= RELOP_NOT_EQUAL)||LA4_19==SIGN_DOT||LA4_19==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 19, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 6, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_THREAD_SCHEDULED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_20 = input.LA(4);
						if ( (LA4_20==SIGN_EQUALS) ) {
							alt4=1;
						}
						else if ( ((LA4_20 >= RELOP_EQUAL && LA4_20 <= RELOP_NOT_EQUAL)||LA4_20==SIGN_DOT||LA4_20==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 20, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_STATE_ADVANCED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_21 = input.LA(4);
						if ( (LA4_21==EOF) ) {
							alt4=1;
						}
						else if ( ((LA4_21 >= RELOP_EQUAL && LA4_21 <= RELOP_NOT_EQUAL)||LA4_21==SIGN_DOT||LA4_21==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 21, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_STEP_IN:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_22 = input.LA(4);
						if ( (LA4_22==EOF) ) {
							alt4=1;
						}
						else if ( ((LA4_22 >= RELOP_EQUAL && LA4_22 <= RELOP_NOT_EQUAL)||LA4_22==SIGN_DOT||LA4_22==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 22, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 9, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_STEP_OVER:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_23 = input.LA(4);
						if ( (LA4_23==EOF) ) {
							alt4=1;
						}
						else if ( ((LA4_23 >= RELOP_EQUAL && LA4_23 <= RELOP_NOT_EQUAL)||LA4_23==SIGN_DOT||LA4_23==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 23, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case EOF:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_SPECIFIC_INSTRUCTION:
				case 105:
					{
					alt4=1;
					}
					break;
				case TOKEN_STEP_OUT:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_24 = input.LA(4);
						if ( (LA4_24==TOKEN_THREAD) ) {
							alt4=1;
						}
						else if ( ((LA4_24 >= RELOP_EQUAL && LA4_24 <= RELOP_NOT_EQUAL)||LA4_24==SIGN_DOT||LA4_24==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 24, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case TOKEN_THREAD:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 12, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_ASSERT:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA4_25 = input.LA(4);
						if ( (LA4_25==105) ) {
							alt4=1;
						}
						else if ( ((LA4_25 >= RELOP_EQUAL && LA4_25 <= RELOP_NOT_EQUAL)||LA4_25==SIGN_DOT||LA4_25==109) ) {
							alt4=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 4, 25, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 105:
						{
						alt4=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt4=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 13, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case CHAR:
				case DOUBLE_TEXT_LIMITTED:
				case FLOAT_TEXT:
				case HEX:
				case IDF_TEXT_INTERNAL:
				case INT:
				case INT_TEXT:
				case LONG_TEXT:
				case STRING:
				case TOKEN_AND:
				case TOKEN_ANY:
				case TOKEN_ARRAY:
				case TOKEN_BEGIN:
				case TOKEN_BOTH:
				case TOKEN_CONDITION:
				case TOKEN_D:
				case TOKEN_DATA:
				case TOKEN_E:
				case TOKEN_END:
				case TOKEN_EXCEPTION_THROWN:
				case TOKEN_F:
				case TOKEN_FALSE:
				case TOKEN_FIELD_ACCESS:
				case TOKEN_FIELD_READ:
				case TOKEN_FIELD_WRITE:
				case TOKEN_HASH_FIELD:
				case TOKEN_HASH_HEAP:
				case TOKEN_HASH_OUTER_CLASS:
				case TOKEN_HASH_STACK_FRAME:
				case TOKEN_HASH_STACK_SLOT:
				case TOKEN_HASH_STATIC:
				case TOKEN_HASH_SUPER:
				case TOKEN_HASH_THIS:
				case TOKEN_HASH_THREAD:
				case TOKEN_HIT_COUNT:
				case TOKEN_IN:
				case TOKEN_INFINITY:
				case TOKEN_INSTRUCTION:
				case TOKEN_INVOKE:
				case TOKEN_L:
				case TOKEN_LOCK:
				case TOKEN_METHOD_INVOKE:
				case TOKEN_NAN:
				case TOKEN_NEGATIVE_INFINITY1:
				case TOKEN_NEGATIVE_INFINITY2:
				case TOKEN_NONE:
				case TOKEN_NOTIFY:
				case TOKEN_NOT_A_NUMBER:
				case TOKEN_NULL:
				case TOKEN_OBJECT_CREATED:
				case TOKEN_OBJECT_RELEASED:
				case TOKEN_OR:
				case TOKEN_POSITIVE_INFINITY1:
				case TOKEN_POSITIVE_INFINITY2:
				case TOKEN_RETURN:
				case TOKEN_SCHEDULING:
				case TOKEN_STACK_FRAME:
				case TOKEN_SYNC_BLOCK:
				case TOKEN_THREAD:
				case TOKEN_TRUE:
				case TOKEN_X:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_GARBAGE_COLLECTION:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_15 = input.LA(3);
					if ( (LA4_15==SIGN_EQUALS) ) {
						alt4=1;
					}
					else if ( ((LA4_15 >= RELOP_EQUAL && LA4_15 <= RELOP_NOT_EQUAL)||LA4_15==SIGN_DOT||LA4_15==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 15, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_CHOICE_GENERATOR:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_16 = input.LA(3);
					if ( (LA4_16==SIGN_EQUALS) ) {
						alt4=1;
					}
					else if ( ((LA4_16 >= RELOP_EQUAL && LA4_16 <= RELOP_NOT_EQUAL)||LA4_16==SIGN_DOT||LA4_16==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 16, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_INSTRUCTION_TYPE:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_17 = input.LA(3);
					if ( (LA4_17==SIGN_EQUALS) ) {
						alt4=1;
					}
					else if ( ((LA4_17 >= RELOP_EQUAL && LA4_17 <= RELOP_NOT_EQUAL)||LA4_17==SIGN_DOT||LA4_17==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 17, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_POSITION:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_18 = input.LA(3);
					if ( (LA4_18==SIGN_EQUALS) ) {
						alt4=1;
					}
					else if ( ((LA4_18 >= RELOP_EQUAL && LA4_18 <= RELOP_NOT_EQUAL)||LA4_18==SIGN_DOT||LA4_18==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 18, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_PROPERTY_VIOLATED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_19 = input.LA(3);
					if ( (LA4_19==EOF) ) {
						alt4=1;
					}
					else if ( ((LA4_19 >= RELOP_EQUAL && LA4_19 <= RELOP_NOT_EQUAL)||LA4_19==SIGN_DOT||LA4_19==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 19, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_THREAD_SCHEDULED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_20 = input.LA(3);
					if ( (LA4_20==SIGN_EQUALS) ) {
						alt4=1;
					}
					else if ( ((LA4_20 >= RELOP_EQUAL && LA4_20 <= RELOP_NOT_EQUAL)||LA4_20==SIGN_DOT||LA4_20==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 20, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_STATE_ADVANCED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_21 = input.LA(3);
					if ( (LA4_21==EOF) ) {
						alt4=1;
					}
					else if ( ((LA4_21 >= RELOP_EQUAL && LA4_21 <= RELOP_NOT_EQUAL)||LA4_21==SIGN_DOT||LA4_21==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 21, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_STEP_IN:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_22 = input.LA(3);
					if ( (LA4_22==EOF) ) {
						alt4=1;
					}
					else if ( ((LA4_22 >= RELOP_EQUAL && LA4_22 <= RELOP_NOT_EQUAL)||LA4_22==SIGN_DOT||LA4_22==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 22, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_STEP_OVER:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_23 = input.LA(3);
					if ( (LA4_23==EOF) ) {
						alt4=1;
					}
					else if ( ((LA4_23 >= RELOP_EQUAL && LA4_23 <= RELOP_NOT_EQUAL)||LA4_23==SIGN_DOT||LA4_23==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 23, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_SPECIFIC_INSTRUCTION:
			case 105:
				{
				alt4=1;
				}
				break;
			case TOKEN_STEP_OUT:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_24 = input.LA(3);
					if ( (LA4_24==TOKEN_THREAD) ) {
						alt4=1;
					}
					else if ( ((LA4_24 >= RELOP_EQUAL && LA4_24 <= RELOP_NOT_EQUAL)||LA4_24==SIGN_DOT||LA4_24==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 24, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case TOKEN_THREAD:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_ASSERT:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA4_25 = input.LA(3);
					if ( (LA4_25==105) ) {
						alt4=1;
					}
					else if ( ((LA4_25 >= RELOP_EQUAL && LA4_25 <= RELOP_NOT_EQUAL)||LA4_25==SIGN_DOT||LA4_25==109) ) {
						alt4=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 25, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 105:
					{
					alt4=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt4=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case CHAR:
			case DOUBLE_TEXT_LIMITTED:
			case FLOAT_TEXT:
			case HEX:
			case IDF_TEXT_INTERNAL:
			case INT:
			case INT_TEXT:
			case LONG_TEXT:
			case STRING:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FALSE:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_HEAP:
			case TOKEN_HASH_OUTER_CLASS:
			case TOKEN_HASH_STACK_FRAME:
			case TOKEN_HASH_STACK_SLOT:
			case TOKEN_HASH_STATIC:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
			case TOKEN_HASH_THREAD:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NEGATIVE_INFINITY2:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_NOT_A_NUMBER:
			case TOKEN_NULL:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_POSITIVE_INFINITY2:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_TRUE:
			case TOKEN_X:
				{
				alt4=2;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}
			switch (alt4) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:280:7: a= cmdBreakpoinstCreateParamsAtomNotTerminateIDF[expFactory] EOF
					{
					pushFollow(FOLLOW_cmdBreakpoinstCreateParamsAtomNotTerminateIDF_in_cmdBreakpointsCreateParamsAtom3663);
					a=cmdBreakpoinstCreateParamsAtomNotTerminateIDF(expFactory);
					state._fsp--;

					 bp = a; 
					match(input,EOF,FOLLOW_EOF_in_cmdBreakpointsCreateParamsAtom3706); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:281:7: a= cmdBreakpoinstCreateParamsAtomTerminateIDF[expFactory] EOF
					{
					pushFollow(FOLLOW_cmdBreakpoinstCreateParamsAtomTerminateIDF_in_cmdBreakpointsCreateParamsAtom3716);
					a=cmdBreakpoinstCreateParamsAtomTerminateIDF(expFactory);
					state._fsp--;

					 bp = a; 
					match(input,EOF,FOLLOW_EOF_in_cmdBreakpointsCreateParamsAtom3762); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bp;
	}
	// $ANTLR end "cmdBreakpointsCreateParamsAtom"



	// $ANTLR start "cmdBreakpoinstCreateParamsAtomNotTerminateIDF"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:284:1: cmdBreakpoinstCreateParamsAtomNotTerminateIDF[ExpressionFactory expFactory] returns [ExpressionBoolean bp] : ( ( WS )? TOKEN_GARBAGE_COLLECTION ( WS )? '=' ( WS )? cmdGarbageCollectionSpec ( WS )? | ( WS )? TOKEN_CHOICE_GENERATOR ( WS )? '=' ( WS )? cmdChoiceGeneratorType ( WS )? | ( WS )? TOKEN_INSTRUCTION_TYPE ( WS )? '=' ( WS )? cmdInstructionTypes ( WS )? | ( WS )? TOKEN_POSITION ( WS )? '=' ( WS )? fileName ( WS )? ':' ( WS )? intValue ( WS )? | ( WS )? TOKEN_PROPERTY_VIOLATED ( WS )? | ( WS )? TOKEN_THREAD_SCHEDULED ( WS )? '=' ( WS )? cmdThreadScheduledDirection ( WS )? ( ':' ( WS )? intValue ( WS )? )? | ( WS )? TOKEN_STATE_ADVANCED ( WS )? | ( WS )? TOKEN_STEP_IN ( WS )? | ( WS )? TOKEN_STEP_OVER ( WS )? | ( WS )? '(' a= cmdBreakpointsCreateParams1[expFactory] ')' ( WS )? | ( WS )? TOKEN_STEP_OUT ( WS )? TOKEN_THREAD ( WS )? '=' ( WS )? tid= intValue ( WS )? TOKEN_STACK_FRAME ( WS )? '=' ( WS )? sfDepth= intValue ( WS )? | ( WS )? TOKEN_SPECIFIC_INSTRUCTION ( WS )? TOKEN_THREAD ( WS )? '=' ( WS )? tid= intValue ( WS )? TOKEN_INSTRUCTION ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? methodName[$className.cn] ( WS )? ':' ( WS )? instIndex= intValue ( WS )? TOKEN_HIT_COUNT ( WS )? '=' ( WS )? hc= intValue ( WS )? | ( WS )? TOKEN_ASSERT ( WS )? '(' fileName ( WS )? ':' ( WS )? intValue ( WS )? ')' ( WS )? '(' cmdBreakpointsCreateParams1[expFactory] ')' );
	public final ExpressionBoolean cmdBreakpoinstCreateParamsAtomNotTerminateIDF(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionBoolean bp = null;


		ExpressionBoolean a =null;
		Integer tid =null;
		Integer sfDepth =null;
		Integer instIndex =null;
		Integer hc =null;
		BreakPointModes cmdGarbageCollectionSpec5 =null;
		BreakPointModes cmdChoiceGeneratorType6 =null;
		InstructionTypes cmdInstructionTypes7 =null;
		String fileName8 =null;
		Integer intValue9 =null;
		BreakPointModes cmdThreadScheduledDirection10 =null;
		Integer intValue11 =null;
		ClassName className12 =null;
		MethodName methodName13 =null;
		String fileName14 =null;
		Integer intValue15 =null;
		ExpressionBoolean cmdBreakpointsCreateParams116 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:5: ( ( WS )? TOKEN_GARBAGE_COLLECTION ( WS )? '=' ( WS )? cmdGarbageCollectionSpec ( WS )? | ( WS )? TOKEN_CHOICE_GENERATOR ( WS )? '=' ( WS )? cmdChoiceGeneratorType ( WS )? | ( WS )? TOKEN_INSTRUCTION_TYPE ( WS )? '=' ( WS )? cmdInstructionTypes ( WS )? | ( WS )? TOKEN_POSITION ( WS )? '=' ( WS )? fileName ( WS )? ':' ( WS )? intValue ( WS )? | ( WS )? TOKEN_PROPERTY_VIOLATED ( WS )? | ( WS )? TOKEN_THREAD_SCHEDULED ( WS )? '=' ( WS )? cmdThreadScheduledDirection ( WS )? ( ':' ( WS )? intValue ( WS )? )? | ( WS )? TOKEN_STATE_ADVANCED ( WS )? | ( WS )? TOKEN_STEP_IN ( WS )? | ( WS )? TOKEN_STEP_OVER ( WS )? | ( WS )? '(' a= cmdBreakpointsCreateParams1[expFactory] ')' ( WS )? | ( WS )? TOKEN_STEP_OUT ( WS )? TOKEN_THREAD ( WS )? '=' ( WS )? tid= intValue ( WS )? TOKEN_STACK_FRAME ( WS )? '=' ( WS )? sfDepth= intValue ( WS )? | ( WS )? TOKEN_SPECIFIC_INSTRUCTION ( WS )? TOKEN_THREAD ( WS )? '=' ( WS )? tid= intValue ( WS )? TOKEN_INSTRUCTION ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? methodName[$className.cn] ( WS )? ':' ( WS )? instIndex= intValue ( WS )? TOKEN_HIT_COUNT ( WS )? '=' ( WS )? hc= intValue ( WS )? | ( WS )? TOKEN_ASSERT ( WS )? '(' fileName ( WS )? ':' ( WS )? intValue ( WS )? ')' ( WS )? '(' cmdBreakpointsCreateParams1[expFactory] ')' )
			int alt69=13;
			switch ( input.LA(1) ) {
			case WS:
				{
				switch ( input.LA(2) ) {
				case TOKEN_GARBAGE_COLLECTION:
					{
					alt69=1;
					}
					break;
				case TOKEN_CHOICE_GENERATOR:
					{
					alt69=2;
					}
					break;
				case TOKEN_INSTRUCTION_TYPE:
					{
					alt69=3;
					}
					break;
				case TOKEN_POSITION:
					{
					alt69=4;
					}
					break;
				case TOKEN_PROPERTY_VIOLATED:
					{
					alt69=5;
					}
					break;
				case TOKEN_THREAD_SCHEDULED:
					{
					alt69=6;
					}
					break;
				case TOKEN_STATE_ADVANCED:
					{
					alt69=7;
					}
					break;
				case TOKEN_STEP_IN:
					{
					alt69=8;
					}
					break;
				case TOKEN_STEP_OVER:
					{
					alt69=9;
					}
					break;
				case 105:
					{
					alt69=10;
					}
					break;
				case TOKEN_STEP_OUT:
					{
					alt69=11;
					}
					break;
				case TOKEN_SPECIFIC_INSTRUCTION:
					{
					alt69=12;
					}
					break;
				case TOKEN_ASSERT:
					{
					alt69=13;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 69, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_GARBAGE_COLLECTION:
				{
				alt69=1;
				}
				break;
			case TOKEN_CHOICE_GENERATOR:
				{
				alt69=2;
				}
				break;
			case TOKEN_INSTRUCTION_TYPE:
				{
				alt69=3;
				}
				break;
			case TOKEN_POSITION:
				{
				alt69=4;
				}
				break;
			case TOKEN_PROPERTY_VIOLATED:
				{
				alt69=5;
				}
				break;
			case TOKEN_THREAD_SCHEDULED:
				{
				alt69=6;
				}
				break;
			case TOKEN_STATE_ADVANCED:
				{
				alt69=7;
				}
				break;
			case TOKEN_STEP_IN:
				{
				alt69=8;
				}
				break;
			case TOKEN_STEP_OVER:
				{
				alt69=9;
				}
				break;
			case 105:
				{
				alt69=10;
				}
				break;
			case TOKEN_STEP_OUT:
				{
				alt69=11;
				}
				break;
			case TOKEN_SPECIFIC_INSTRUCTION:
				{
				alt69=12;
				}
				break;
			case TOKEN_ASSERT:
				{
				alt69=13;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 69, 0, input);
				throw nvae;
			}
			switch (alt69) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:7: ( WS )? TOKEN_GARBAGE_COLLECTION ( WS )? '=' ( WS )? cmdGarbageCollectionSpec ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:7: ( WS )?
					int alt5=2;
					int LA5_0 = input.LA(1);
					if ( (LA5_0==WS) ) {
						alt5=1;
					}
					switch (alt5) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3785); 
							}
							break;

					}

					match(input,TOKEN_GARBAGE_COLLECTION,FOLLOW_TOKEN_GARBAGE_COLLECTION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3788); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:38: ( WS )?
					int alt6=2;
					int LA6_0 = input.LA(1);
					if ( (LA6_0==WS) ) {
						alt6=1;
					}
					switch (alt6) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3792); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3795); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:46: ( WS )?
					int alt7=2;
					int LA7_0 = input.LA(1);
					if ( (LA7_0==WS) ) {
						alt7=1;
					}
					switch (alt7) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:46: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3797); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdGarbageCollectionSpec_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3800);
					cmdGarbageCollectionSpec5=cmdGarbageCollectionSpec();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:75: ( WS )?
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0==WS) ) {
						alt8=1;
					}
					switch (alt8) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:285:75: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3802); 
							}
							break;

					}

					 bp = expFactory.getBreakpointGarbageCollection(cmdGarbageCollectionSpec5); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:7: ( WS )? TOKEN_CHOICE_GENERATOR ( WS )? '=' ( WS )? cmdChoiceGeneratorType ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:7: ( WS )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( (LA9_0==WS) ) {
						alt9=1;
					}
					switch (alt9) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3841); 
							}
							break;

					}

					match(input,TOKEN_CHOICE_GENERATOR,FOLLOW_TOKEN_CHOICE_GENERATOR_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3844); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:38: ( WS )?
					int alt10=2;
					int LA10_0 = input.LA(1);
					if ( (LA10_0==WS) ) {
						alt10=1;
					}
					switch (alt10) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3850); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3853); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:46: ( WS )?
					int alt11=2;
					int LA11_0 = input.LA(1);
					if ( (LA11_0==WS) ) {
						alt11=1;
					}
					switch (alt11) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:46: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3855); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdChoiceGeneratorType_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3858);
					cmdChoiceGeneratorType6=cmdChoiceGeneratorType();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:73: ( WS )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==WS) ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:286:73: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3860); 
							}
							break;

					}

					 bp = expFactory.getBreakpointChoiceGenerator(cmdChoiceGeneratorType6); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:7: ( WS )? TOKEN_INSTRUCTION_TYPE ( WS )? '=' ( WS )? cmdInstructionTypes ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:7: ( WS )?
					int alt13=2;
					int LA13_0 = input.LA(1);
					if ( (LA13_0==WS) ) {
						alt13=1;
					}
					switch (alt13) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3901); 
							}
							break;

					}

					match(input,TOKEN_INSTRUCTION_TYPE,FOLLOW_TOKEN_INSTRUCTION_TYPE_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3904); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:38: ( WS )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==WS) ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3910); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3913); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:46: ( WS )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0==WS) ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:46: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3915); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdInstructionTypes_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3918);
					cmdInstructionTypes7=cmdInstructionTypes();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:70: ( WS )?
					int alt16=2;
					int LA16_0 = input.LA(1);
					if ( (LA16_0==WS) ) {
						alt16=1;
					}
					switch (alt16) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:287:70: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3920); 
							}
							break;

					}

					 bp = expFactory.getBreakpointInstructionType(cmdInstructionTypes7); 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:7: ( WS )? TOKEN_POSITION ( WS )? '=' ( WS )? fileName ( WS )? ':' ( WS )? intValue ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:7: ( WS )?
					int alt17=2;
					int LA17_0 = input.LA(1);
					if ( (LA17_0==WS) ) {
						alt17=1;
					}
					switch (alt17) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3964); 
							}
							break;

					}

					match(input,TOKEN_POSITION,FOLLOW_TOKEN_POSITION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3967); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:38: ( WS )?
					int alt18=2;
					int LA18_0 = input.LA(1);
					if ( (LA18_0==WS) ) {
						alt18=1;
					}
					switch (alt18) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3981); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3984); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:46: ( WS )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==WS) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:46: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3986); 
							}
							break;

					}

					pushFollow(FOLLOW_fileName_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3989);
					fileName8=fileName();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:59: ( WS )?
					int alt20=2;
					int LA20_0 = input.LA(1);
					if ( (LA20_0==WS) ) {
						alt20=1;
					}
					switch (alt20) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:59: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3991); 
							}
							break;

					}

					match(input,108,FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3994); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:67: ( WS )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==WS) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:67: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3996); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3999);
					intValue9=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:80: ( WS )?
					int alt22=2;
					int LA22_0 = input.LA(1);
					if ( (LA22_0==WS) ) {
						alt22=1;
					}
					switch (alt22) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:288:80: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4001); 
							}
							break;

					}

					 bp = expFactory.getExpBreakpointPosition(fileName8, intValue9); 
					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:289:7: ( WS )? TOKEN_PROPERTY_VIOLATED ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:289:7: ( WS )?
					int alt23=2;
					int LA23_0 = input.LA(1);
					if ( (LA23_0==WS) ) {
						alt23=1;
					}
					switch (alt23) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:289:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4035); 
							}
							break;

					}

					match(input,TOKEN_PROPERTY_VIOLATED,FOLLOW_TOKEN_PROPERTY_VIOLATED_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4038); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:289:38: ( WS )?
					int alt24=2;
					int LA24_0 = input.LA(1);
					if ( (LA24_0==WS) ) {
						alt24=1;
					}
					switch (alt24) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:289:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4043); 
							}
							break;

					}

					 bp = expFactory.getBreakpoointPropertyViolated(); 
					}
					break;
				case 6 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:7: ( WS )? TOKEN_THREAD_SCHEDULED ( WS )? '=' ( WS )? cmdThreadScheduledDirection ( WS )? ( ':' ( WS )? intValue ( WS )? )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:7: ( WS )?
					int alt25=2;
					int LA25_0 = input.LA(1);
					if ( (LA25_0==WS) ) {
						alt25=1;
					}
					switch (alt25) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4119); 
							}
							break;

					}

					match(input,TOKEN_THREAD_SCHEDULED,FOLLOW_TOKEN_THREAD_SCHEDULED_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4122); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:38: ( WS )?
					int alt26=2;
					int LA26_0 = input.LA(1);
					if ( (LA26_0==WS) ) {
						alt26=1;
					}
					switch (alt26) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4128); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4131); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:46: ( WS )?
					int alt27=2;
					int LA27_0 = input.LA(1);
					if ( (LA27_0==WS) ) {
						alt27=1;
					}
					switch (alt27) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:46: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4133); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdThreadScheduledDirection_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4136);
					cmdThreadScheduledDirection10=cmdThreadScheduledDirection();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:78: ( WS )?
					int alt28=2;
					int LA28_0 = input.LA(1);
					if ( (LA28_0==WS) ) {
						alt28=1;
					}
					switch (alt28) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:78: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4138); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:82: ( ':' ( WS )? intValue ( WS )? )?
					int alt31=2;
					int LA31_0 = input.LA(1);
					if ( (LA31_0==108) ) {
						alt31=1;
					}
					switch (alt31) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:83: ':' ( WS )? intValue ( WS )?
							{
							match(input,108,FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4142); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:87: ( WS )?
							int alt29=2;
							int LA29_0 = input.LA(1);
							if ( (LA29_0==WS) ) {
								alt29=1;
							}
							switch (alt29) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:87: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4144); 
									}
									break;

							}

							pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4147);
							intValue11=intValue();
							state._fsp--;

							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:100: ( WS )?
							int alt30=2;
							int LA30_0 = input.LA(1);
							if ( (LA30_0==WS) ) {
								alt30=1;
							}
							switch (alt30) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:290:100: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4149); 
									}
									break;

							}

							}
							break;

					}

					 bp = expFactory.getBreakpointThreadScheduled(cmdThreadScheduledDirection10, (intValue11!=null? intValue11 : null)); 
					}
					break;
				case 7 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:291:7: ( WS )? TOKEN_STATE_ADVANCED ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:291:7: ( WS )?
					int alt32=2;
					int LA32_0 = input.LA(1);
					if ( (LA32_0==WS) ) {
						alt32=1;
					}
					switch (alt32) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:291:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4163); 
							}
							break;

					}

					match(input,TOKEN_STATE_ADVANCED,FOLLOW_TOKEN_STATE_ADVANCED_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4166); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:291:38: ( WS )?
					int alt33=2;
					int LA33_0 = input.LA(1);
					if ( (LA33_0==WS) ) {
						alt33=1;
					}
					switch (alt33) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:291:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4174); 
							}
							break;

					}

					 bp = expFactory.getBreakpointStateAdvanced(); 
					}
					break;
				case 8 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:292:7: ( WS )? TOKEN_STEP_IN ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:292:7: ( WS )?
					int alt34=2;
					int LA34_0 = input.LA(1);
					if ( (LA34_0==WS) ) {
						alt34=1;
					}
					switch (alt34) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:292:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4250); 
							}
							break;

					}

					match(input,TOKEN_STEP_IN,FOLLOW_TOKEN_STEP_IN_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4253); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:292:38: ( WS )?
					int alt35=2;
					int LA35_0 = input.LA(1);
					if ( (LA35_0==WS) ) {
						alt35=1;
					}
					switch (alt35) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:292:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4268); 
							}
							break;

					}

					 bp = expFactory.getBreakpointSingleStep(StepType.ST_STEP_IN); 
					}
					break;
				case 9 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:293:7: ( WS )? TOKEN_STEP_OVER ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:293:7: ( WS )?
					int alt36=2;
					int LA36_0 = input.LA(1);
					if ( (LA36_0==WS) ) {
						alt36=1;
					}
					switch (alt36) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:293:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4344); 
							}
							break;

					}

					match(input,TOKEN_STEP_OVER,FOLLOW_TOKEN_STEP_OVER_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4347); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:293:38: ( WS )?
					int alt37=2;
					int LA37_0 = input.LA(1);
					if ( (LA37_0==WS) ) {
						alt37=1;
					}
					switch (alt37) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:293:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4360); 
							}
							break;

					}

					 bp = expFactory.getBreakpointSingleStep(StepType.ST_LINE); 
					}
					break;
				case 10 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:294:7: ( WS )? '(' a= cmdBreakpointsCreateParams1[expFactory] ')' ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:294:7: ( WS )?
					int alt38=2;
					int LA38_0 = input.LA(1);
					if ( (LA38_0==WS) ) {
						alt38=1;
					}
					switch (alt38) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:294:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4436); 
							}
							break;

					}

					match(input,105,FOLLOW_105_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4439); 
					pushFollow(FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4443);
					a=cmdBreakpointsCreateParams1(expFactory);
					state._fsp--;

					match(input,106,FOLLOW_106_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4446); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:294:61: ( WS )?
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==WS) ) {
						alt39=1;
					}
					switch (alt39) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:294:61: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4448); 
							}
							break;

					}

					 bp = a; 
					}
					break;
				case 11 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:7: ( WS )? TOKEN_STEP_OUT ( WS )? TOKEN_THREAD ( WS )? '=' ( WS )? tid= intValue ( WS )? TOKEN_STACK_FRAME ( WS )? '=' ( WS )? sfDepth= intValue ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:7: ( WS )?
					int alt40=2;
					int LA40_0 = input.LA(1);
					if ( (LA40_0==WS) ) {
						alt40=1;
					}
					switch (alt40) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4501); 
							}
							break;

					}

					match(input,TOKEN_STEP_OUT,FOLLOW_TOKEN_STEP_OUT_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4504); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:38: ( WS )?
					int alt41=2;
					int LA41_0 = input.LA(1);
					if ( (LA41_0==WS) ) {
						alt41=1;
					}
					switch (alt41) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4518); 
							}
							break;

					}

					match(input,TOKEN_THREAD,FOLLOW_TOKEN_THREAD_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4521); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:55: ( WS )?
					int alt42=2;
					int LA42_0 = input.LA(1);
					if ( (LA42_0==WS) ) {
						alt42=1;
					}
					switch (alt42) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:55: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4523); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4526); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:63: ( WS )?
					int alt43=2;
					int LA43_0 = input.LA(1);
					if ( (LA43_0==WS) ) {
						alt43=1;
					}
					switch (alt43) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:63: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4528); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4533);
					tid=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:80: ( WS )?
					int alt44=2;
					int LA44_0 = input.LA(1);
					if ( (LA44_0==WS) ) {
						alt44=1;
					}
					switch (alt44) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:80: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4535); 
							}
							break;

					}

					match(input,TOKEN_STACK_FRAME,FOLLOW_TOKEN_STACK_FRAME_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4538); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:102: ( WS )?
					int alt45=2;
					int LA45_0 = input.LA(1);
					if ( (LA45_0==WS) ) {
						alt45=1;
					}
					switch (alt45) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:102: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4540); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4543); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:110: ( WS )?
					int alt46=2;
					int LA46_0 = input.LA(1);
					if ( (LA46_0==WS) ) {
						alt46=1;
					}
					switch (alt46) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:110: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4545); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4550);
					sfDepth=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:131: ( WS )?
					int alt47=2;
					int LA47_0 = input.LA(1);
					if ( (LA47_0==WS) ) {
						alt47=1;
					}
					switch (alt47) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:295:131: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4552); 
							}
							break;

					}

					 bp = expFactory.getBreakpointStepOut( tid, sfDepth); 
					}
					break;
				case 12 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:7: ( WS )? TOKEN_SPECIFIC_INSTRUCTION ( WS )? TOKEN_THREAD ( WS )? '=' ( WS )? tid= intValue ( WS )? TOKEN_INSTRUCTION ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? methodName[$className.cn] ( WS )? ':' ( WS )? instIndex= intValue ( WS )? TOKEN_HIT_COUNT ( WS )? '=' ( WS )? hc= intValue ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:7: ( WS )?
					int alt48=2;
					int LA48_0 = input.LA(1);
					if ( (LA48_0==WS) ) {
						alt48=1;
					}
					switch (alt48) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4670); 
							}
							break;

					}

					match(input,TOKEN_SPECIFIC_INSTRUCTION,FOLLOW_TOKEN_SPECIFIC_INSTRUCTION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4673); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:38: ( WS )?
					int alt49=2;
					int LA49_0 = input.LA(1);
					if ( (LA49_0==WS) ) {
						alt49=1;
					}
					switch (alt49) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4675); 
							}
							break;

					}

					match(input,TOKEN_THREAD,FOLLOW_TOKEN_THREAD_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4678); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:55: ( WS )?
					int alt50=2;
					int LA50_0 = input.LA(1);
					if ( (LA50_0==WS) ) {
						alt50=1;
					}
					switch (alt50) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:55: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4680); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4683); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:63: ( WS )?
					int alt51=2;
					int LA51_0 = input.LA(1);
					if ( (LA51_0==WS) ) {
						alt51=1;
					}
					switch (alt51) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:63: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4685); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4690);
					tid=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:80: ( WS )?
					int alt52=2;
					int LA52_0 = input.LA(1);
					if ( (LA52_0==WS) ) {
						alt52=1;
					}
					switch (alt52) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:80: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4692); 
							}
							break;

					}

					match(input,TOKEN_INSTRUCTION,FOLLOW_TOKEN_INSTRUCTION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4695); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:102: ( WS )?
					int alt53=2;
					int LA53_0 = input.LA(1);
					if ( (LA53_0==WS) ) {
						alt53=1;
					}
					switch (alt53) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:102: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4697); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4700); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:110: ( WS )?
					int alt54=2;
					int LA54_0 = input.LA(1);
					if ( (LA54_0==WS) ) {
						alt54=1;
					}
					switch (alt54) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:110: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4702); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4705);
					className12=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:124: ( WS )?
					int alt55=2;
					int LA55_0 = input.LA(1);
					if ( (LA55_0==WS) ) {
						alt55=1;
					}
					switch (alt55) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:124: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4707); 
							}
							break;

					}

					match(input,108,FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4710); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:132: ( WS )?
					int alt56=2;
					int LA56_0 = input.LA(1);
					if ( (LA56_0==WS) ) {
						alt56=1;
					}
					switch (alt56) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:132: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4712); 
							}
							break;

					}

					pushFollow(FOLLOW_methodName_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4715);
					methodName13=methodName(className12);
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:162: ( WS )?
					int alt57=2;
					int LA57_0 = input.LA(1);
					if ( (LA57_0==WS) ) {
						alt57=1;
					}
					switch (alt57) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:162: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4718); 
							}
							break;

					}

					match(input,108,FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4721); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:170: ( WS )?
					int alt58=2;
					int LA58_0 = input.LA(1);
					if ( (LA58_0==WS) ) {
						alt58=1;
					}
					switch (alt58) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:170: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4723); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4728);
					instIndex=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:193: ( WS )?
					int alt59=2;
					int LA59_0 = input.LA(1);
					if ( (LA59_0==WS) ) {
						alt59=1;
					}
					switch (alt59) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:193: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4730); 
							}
							break;

					}

					match(input,TOKEN_HIT_COUNT,FOLLOW_TOKEN_HIT_COUNT_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4733); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:213: ( WS )?
					int alt60=2;
					int LA60_0 = input.LA(1);
					if ( (LA60_0==WS) ) {
						alt60=1;
					}
					switch (alt60) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:213: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4735); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4738); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:221: ( WS )?
					int alt61=2;
					int LA61_0 = input.LA(1);
					if ( (LA61_0==WS) ) {
						alt61=1;
					}
					switch (alt61) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:221: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4740); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4745);
					hc=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:237: ( WS )?
					int alt62=2;
					int LA62_0 = input.LA(1);
					if ( (LA62_0==WS) ) {
						alt62=1;
					}
					switch (alt62) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:296:237: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4747); 
							}
							break;

					}

					 bp = expFactory.getBreakpointInstruction(tid, methodName13, instIndex, hc); 
					}
					break;
				case 13 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:7: ( WS )? TOKEN_ASSERT ( WS )? '(' fileName ( WS )? ':' ( WS )? intValue ( WS )? ')' ( WS )? '(' cmdBreakpointsCreateParams1[expFactory] ')'
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:7: ( WS )?
					int alt63=2;
					int LA63_0 = input.LA(1);
					if ( (LA63_0==WS) ) {
						alt63=1;
					}
					switch (alt63) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4759); 
							}
							break;

					}

					match(input,TOKEN_ASSERT,FOLLOW_TOKEN_ASSERT_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4762); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:38: ( WS )?
					int alt64=2;
					int LA64_0 = input.LA(1);
					if ( (LA64_0==WS) ) {
						alt64=1;
					}
					switch (alt64) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:38: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4778); 
							}
							break;

					}

					match(input,105,FOLLOW_105_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4781); 
					pushFollow(FOLLOW_fileName_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4783);
					fileName14=fileName();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:55: ( WS )?
					int alt65=2;
					int LA65_0 = input.LA(1);
					if ( (LA65_0==WS) ) {
						alt65=1;
					}
					switch (alt65) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:55: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4785); 
							}
							break;

					}

					match(input,108,FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4788); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:63: ( WS )?
					int alt66=2;
					int LA66_0 = input.LA(1);
					if ( (LA66_0==WS) ) {
						alt66=1;
					}
					switch (alt66) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:63: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4790); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4793);
					intValue15=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:76: ( WS )?
					int alt67=2;
					int LA67_0 = input.LA(1);
					if ( (LA67_0==WS) ) {
						alt67=1;
					}
					switch (alt67) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:76: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4795); 
							}
							break;

					}

					match(input,106,FOLLOW_106_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4798); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:84: ( WS )?
					int alt68=2;
					int LA68_0 = input.LA(1);
					if ( (LA68_0==WS) ) {
						alt68=1;
					}
					switch (alt68) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:297:84: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4800); 
							}
							break;

					}

					match(input,105,FOLLOW_105_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4803); 
					pushFollow(FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4805);
					cmdBreakpointsCreateParams116=cmdBreakpointsCreateParams1(expFactory);
					state._fsp--;

					match(input,106,FOLLOW_106_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4808); 
					 bp = expFactory.getBreakpointAssert(expFactory.getExpBreakpointPosition(fileName14, intValue15), cmdBreakpointsCreateParams116); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bp;
	}
	// $ANTLR end "cmdBreakpoinstCreateParamsAtomNotTerminateIDF"



	// $ANTLR start "cmdBreakpoinstCreateParamsAtomTerminateIDF"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:300:1: cmdBreakpoinstCreateParamsAtomTerminateIDF[ExpressionFactory expFactory] returns [ExpressionBoolean bp] : ( ( WS )? fieldAccess ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? fieldName[$className.cn] ( WS )? | ( WS )? TOKEN_METHOD_INVOKE ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? methodName[$className.cn] ( WS )? | ( WS )? TOKEN_OBJECT_CREATED ( WS )? '=' ( WS )? className ( WS )? | ( WS )? TOKEN_OBJECT_RELEASED ( WS )? '=' ( WS )? className ( WS )? | ( WS )? TOKEN_EXCEPTION_THROWN ( WS )? '=' ( WS )? className ( WS )? |l= cmdStateExpression1Value[expFactory] relOp[expFactory] r= cmdStateExpression1Value[expFactory] );
	public final ExpressionBoolean cmdBreakpoinstCreateParamsAtomTerminateIDF(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionBoolean bp = null;


		ExpressionStateRootNode<?> l =null;
		ExpressionStateRootNode<?> r =null;
		ClassName className17 =null;
		BreakPointModes fieldAccess18 =null;
		FieldName fieldName19 =null;
		ClassName className20 =null;
		MethodName methodName21 =null;
		ClassName className22 =null;
		ClassName className23 =null;
		ClassName className24 =null;
		RelationOperator relOp25 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:5: ( ( WS )? fieldAccess ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? fieldName[$className.cn] ( WS )? | ( WS )? TOKEN_METHOD_INVOKE ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? methodName[$className.cn] ( WS )? | ( WS )? TOKEN_OBJECT_CREATED ( WS )? '=' ( WS )? className ( WS )? | ( WS )? TOKEN_OBJECT_RELEASED ( WS )? '=' ( WS )? className ( WS )? | ( WS )? TOKEN_EXCEPTION_THROWN ( WS )? '=' ( WS )? className ( WS )? |l= cmdStateExpression1Value[expFactory] relOp[expFactory] r= cmdStateExpression1Value[expFactory] )
			int alt94=6;
			switch ( input.LA(1) ) {
			case WS:
				{
				switch ( input.LA(2) ) {
				case TOKEN_FIELD_ACCESS:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA94_10 = input.LA(4);
						if ( (LA94_10==SIGN_EQUALS) ) {
							alt94=1;
						}
						else if ( ((LA94_10 >= RELOP_EQUAL && LA94_10 <= RELOP_NOT_EQUAL)||LA94_10==SIGN_DOT||LA94_10==109) ) {
							alt94=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 94, 10, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt94=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt94=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_FIELD_READ:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA94_10 = input.LA(4);
						if ( (LA94_10==SIGN_EQUALS) ) {
							alt94=1;
						}
						else if ( ((LA94_10 >= RELOP_EQUAL && LA94_10 <= RELOP_NOT_EQUAL)||LA94_10==SIGN_DOT||LA94_10==109) ) {
							alt94=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 94, 10, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt94=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt94=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_FIELD_WRITE:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA94_10 = input.LA(4);
						if ( (LA94_10==SIGN_EQUALS) ) {
							alt94=1;
						}
						else if ( ((LA94_10 >= RELOP_EQUAL && LA94_10 <= RELOP_NOT_EQUAL)||LA94_10==SIGN_DOT||LA94_10==109) ) {
							alt94=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 94, 10, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt94=1;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt94=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_METHOD_INVOKE:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA94_12 = input.LA(4);
						if ( (LA94_12==SIGN_EQUALS) ) {
							alt94=2;
						}
						else if ( ((LA94_12 >= RELOP_EQUAL && LA94_12 <= RELOP_NOT_EQUAL)||LA94_12==SIGN_DOT||LA94_12==109) ) {
							alt94=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 94, 12, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt94=2;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt94=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_OBJECT_CREATED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA94_14 = input.LA(4);
						if ( (LA94_14==SIGN_EQUALS) ) {
							alt94=3;
						}
						else if ( ((LA94_14 >= RELOP_EQUAL && LA94_14 <= RELOP_NOT_EQUAL)||LA94_14==SIGN_DOT||LA94_14==109) ) {
							alt94=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 94, 14, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt94=3;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt94=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 6, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_OBJECT_RELEASED:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA94_16 = input.LA(4);
						if ( (LA94_16==SIGN_EQUALS) ) {
							alt94=4;
						}
						else if ( ((LA94_16 >= RELOP_EQUAL && LA94_16 <= RELOP_NOT_EQUAL)||LA94_16==SIGN_DOT||LA94_16==109) ) {
							alt94=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 94, 16, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt94=4;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt94=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_EXCEPTION_THROWN:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA94_18 = input.LA(4);
						if ( (LA94_18==SIGN_EQUALS) ) {
							alt94=5;
						}
						else if ( ((LA94_18 >= RELOP_EQUAL && LA94_18 <= RELOP_NOT_EQUAL)||LA94_18==SIGN_DOT||LA94_18==109) ) {
							alt94=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 94, 18, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SIGN_EQUALS:
						{
						alt94=5;
						}
						break;
					case RELOP_EQUAL:
					case RELOP_GREATER_OR_EQUAL_THAN:
					case RELOP_GREATER_THAN:
					case RELOP_LESS_OR_EQUAL_THAN:
					case RELOP_LESS_THAN:
					case RELOP_NOT_EQUAL:
					case SIGN_DOT:
					case 109:
						{
						alt94=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case CHAR:
				case DOUBLE_TEXT_LIMITTED:
				case FLOAT_TEXT:
				case HEX:
				case IDF_TEXT_INTERNAL:
				case INT:
				case INT_TEXT:
				case LONG_TEXT:
				case STRING:
				case TOKEN_AND:
				case TOKEN_ANY:
				case TOKEN_ARRAY:
				case TOKEN_ASSERT:
				case TOKEN_BEGIN:
				case TOKEN_BOTH:
				case TOKEN_CHOICE_GENERATOR:
				case TOKEN_CONDITION:
				case TOKEN_D:
				case TOKEN_DATA:
				case TOKEN_E:
				case TOKEN_END:
				case TOKEN_F:
				case TOKEN_FALSE:
				case TOKEN_GARBAGE_COLLECTION:
				case TOKEN_HASH_FIELD:
				case TOKEN_HASH_HEAP:
				case TOKEN_HASH_OUTER_CLASS:
				case TOKEN_HASH_STACK_FRAME:
				case TOKEN_HASH_STACK_SLOT:
				case TOKEN_HASH_STATIC:
				case TOKEN_HASH_SUPER:
				case TOKEN_HASH_THIS:
				case TOKEN_HASH_THREAD:
				case TOKEN_HIT_COUNT:
				case TOKEN_IN:
				case TOKEN_INFINITY:
				case TOKEN_INSTRUCTION:
				case TOKEN_INSTRUCTION_TYPE:
				case TOKEN_INVOKE:
				case TOKEN_L:
				case TOKEN_LOCK:
				case TOKEN_NAN:
				case TOKEN_NEGATIVE_INFINITY1:
				case TOKEN_NEGATIVE_INFINITY2:
				case TOKEN_NONE:
				case TOKEN_NOTIFY:
				case TOKEN_NOT_A_NUMBER:
				case TOKEN_NULL:
				case TOKEN_OR:
				case TOKEN_POSITION:
				case TOKEN_POSITIVE_INFINITY1:
				case TOKEN_POSITIVE_INFINITY2:
				case TOKEN_PROPERTY_VIOLATED:
				case TOKEN_RETURN:
				case TOKEN_SCHEDULING:
				case TOKEN_STACK_FRAME:
				case TOKEN_STATE_ADVANCED:
				case TOKEN_STEP_IN:
				case TOKEN_STEP_OUT:
				case TOKEN_STEP_OVER:
				case TOKEN_SYNC_BLOCK:
				case TOKEN_THREAD:
				case TOKEN_THREAD_SCHEDULED:
				case TOKEN_TRUE:
				case TOKEN_X:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_FIELD_ACCESS:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA94_10 = input.LA(3);
					if ( (LA94_10==SIGN_EQUALS) ) {
						alt94=1;
					}
					else if ( ((LA94_10 >= RELOP_EQUAL && LA94_10 <= RELOP_NOT_EQUAL)||LA94_10==SIGN_DOT||LA94_10==109) ) {
						alt94=6;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt94=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_FIELD_READ:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA94_10 = input.LA(3);
					if ( (LA94_10==SIGN_EQUALS) ) {
						alt94=1;
					}
					else if ( ((LA94_10 >= RELOP_EQUAL && LA94_10 <= RELOP_NOT_EQUAL)||LA94_10==SIGN_DOT||LA94_10==109) ) {
						alt94=6;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt94=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_FIELD_WRITE:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA94_10 = input.LA(3);
					if ( (LA94_10==SIGN_EQUALS) ) {
						alt94=1;
					}
					else if ( ((LA94_10 >= RELOP_EQUAL && LA94_10 <= RELOP_NOT_EQUAL)||LA94_10==SIGN_DOT||LA94_10==109) ) {
						alt94=6;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt94=1;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_METHOD_INVOKE:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA94_12 = input.LA(3);
					if ( (LA94_12==SIGN_EQUALS) ) {
						alt94=2;
					}
					else if ( ((LA94_12 >= RELOP_EQUAL && LA94_12 <= RELOP_NOT_EQUAL)||LA94_12==SIGN_DOT||LA94_12==109) ) {
						alt94=6;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 12, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt94=2;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_OBJECT_CREATED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA94_14 = input.LA(3);
					if ( (LA94_14==SIGN_EQUALS) ) {
						alt94=3;
					}
					else if ( ((LA94_14 >= RELOP_EQUAL && LA94_14 <= RELOP_NOT_EQUAL)||LA94_14==SIGN_DOT||LA94_14==109) ) {
						alt94=6;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 14, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt94=3;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_OBJECT_RELEASED:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA94_16 = input.LA(3);
					if ( (LA94_16==SIGN_EQUALS) ) {
						alt94=4;
					}
					else if ( ((LA94_16 >= RELOP_EQUAL && LA94_16 <= RELOP_NOT_EQUAL)||LA94_16==SIGN_DOT||LA94_16==109) ) {
						alt94=6;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 16, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt94=4;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_EXCEPTION_THROWN:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA94_18 = input.LA(3);
					if ( (LA94_18==SIGN_EQUALS) ) {
						alt94=5;
					}
					else if ( ((LA94_18 >= RELOP_EQUAL && LA94_18 <= RELOP_NOT_EQUAL)||LA94_18==SIGN_DOT||LA94_18==109) ) {
						alt94=6;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 94, 18, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case SIGN_EQUALS:
					{
					alt94=5;
					}
					break;
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case 109:
					{
					alt94=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 94, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case CHAR:
			case DOUBLE_TEXT_LIMITTED:
			case FLOAT_TEXT:
			case HEX:
			case IDF_TEXT_INTERNAL:
			case INT:
			case INT_TEXT:
			case LONG_TEXT:
			case STRING:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_F:
			case TOKEN_FALSE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_HEAP:
			case TOKEN_HASH_OUTER_CLASS:
			case TOKEN_HASH_STACK_FRAME:
			case TOKEN_HASH_STACK_SLOT:
			case TOKEN_HASH_STATIC:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
			case TOKEN_HASH_THREAD:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NEGATIVE_INFINITY2:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_NOT_A_NUMBER:
			case TOKEN_NULL:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_POSITIVE_INFINITY2:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_TRUE:
			case TOKEN_X:
				{
				alt94=6;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 94, 0, input);
				throw nvae;
			}
			switch (alt94) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:7: ( WS )? fieldAccess ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? fieldName[$className.cn] ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:7: ( WS )?
					int alt70=2;
					int LA70_0 = input.LA(1);
					if ( (LA70_0==WS) ) {
						alt70=1;
					}
					switch (alt70) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4939); 
							}
							break;

					}

					pushFollow(FOLLOW_fieldAccess_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4942);
					fieldAccess18=fieldAccess();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:36: ( WS )?
					int alt71=2;
					int LA71_0 = input.LA(1);
					if ( (LA71_0==WS) ) {
						alt71=1;
					}
					switch (alt71) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:36: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4957); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4960); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:44: ( WS )?
					int alt72=2;
					int LA72_0 = input.LA(1);
					if ( (LA72_0==WS) ) {
						alt72=1;
					}
					switch (alt72) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:44: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4962); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4965);
					className17=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:58: ( WS )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==WS) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:58: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4967); 
							}
							break;

					}

					match(input,108,FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4970); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:66: ( WS )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==WS) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:66: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4972); 
							}
							break;

					}

					pushFollow(FOLLOW_fieldName_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4975);
					fieldName19=fieldName(className17);
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:95: ( WS )?
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==WS) ) {
						int LA75_1 = input.LA(2);
						if ( (LA75_1==EOF||LA75_1==WS||LA75_1==106) ) {
							alt75=1;
						}
					}
					switch (alt75) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:301:95: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4978); 
							}
							break;

					}

					 bp = expFactory.getBreakpointFieldAccess(fieldAccess18, fieldName19); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:7: ( WS )? TOKEN_METHOD_INVOKE ( WS )? '=' ( WS )? className ( WS )? ':' ( WS )? methodName[$className.cn] ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:7: ( WS )?
					int alt76=2;
					int LA76_0 = input.LA(1);
					if ( (LA76_0==WS) ) {
						alt76=1;
					}
					switch (alt76) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4995); 
							}
							break;

					}

					match(input,TOKEN_METHOD_INVOKE,FOLLOW_TOKEN_METHOD_INVOKE_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4998); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:36: ( WS )?
					int alt77=2;
					int LA77_0 = input.LA(1);
					if ( (LA77_0==WS) ) {
						alt77=1;
					}
					switch (alt77) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:36: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5005); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5008); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:44: ( WS )?
					int alt78=2;
					int LA78_0 = input.LA(1);
					if ( (LA78_0==WS) ) {
						alt78=1;
					}
					switch (alt78) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:44: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5010); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5013);
					className20=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:58: ( WS )?
					int alt79=2;
					int LA79_0 = input.LA(1);
					if ( (LA79_0==WS) ) {
						alt79=1;
					}
					switch (alt79) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:58: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5015); 
							}
							break;

					}

					match(input,108,FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5018); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:66: ( WS )?
					int alt80=2;
					int LA80_0 = input.LA(1);
					if ( (LA80_0==WS) ) {
						alt80=1;
					}
					switch (alt80) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:66: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5020); 
							}
							break;

					}

					pushFollow(FOLLOW_methodName_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5023);
					methodName21=methodName(className20);
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:96: ( WS )?
					int alt81=2;
					int LA81_0 = input.LA(1);
					if ( (LA81_0==WS) ) {
						int LA81_1 = input.LA(2);
						if ( (LA81_1==EOF||LA81_1==WS||LA81_1==106) ) {
							alt81=1;
						}
					}
					switch (alt81) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:302:96: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5026); 
							}
							break;

					}

					 bp = expFactory.getBreakpointMethodInvoke(BreakPointModes.BP_MODE_METHOD_INVOKE, methodName21); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:7: ( WS )? TOKEN_OBJECT_CREATED ( WS )? '=' ( WS )? className ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:7: ( WS )?
					int alt82=2;
					int LA82_0 = input.LA(1);
					if ( (LA82_0==WS) ) {
						alt82=1;
					}
					switch (alt82) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5042); 
							}
							break;

					}

					match(input,TOKEN_OBJECT_CREATED,FOLLOW_TOKEN_OBJECT_CREATED_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5045); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:36: ( WS )?
					int alt83=2;
					int LA83_0 = input.LA(1);
					if ( (LA83_0==WS) ) {
						alt83=1;
					}
					switch (alt83) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:36: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5051); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5054); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:44: ( WS )?
					int alt84=2;
					int LA84_0 = input.LA(1);
					if ( (LA84_0==WS) ) {
						alt84=1;
					}
					switch (alt84) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:44: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5056); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5059);
					className22=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:58: ( WS )?
					int alt85=2;
					int LA85_0 = input.LA(1);
					if ( (LA85_0==WS) ) {
						int LA85_1 = input.LA(2);
						if ( (LA85_1==EOF||LA85_1==WS||LA85_1==106) ) {
							alt85=1;
						}
					}
					switch (alt85) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:303:58: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5061); 
							}
							break;

					}

					 bp = expFactory.getBreakpointObjectCreated(className22); 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:7: ( WS )? TOKEN_OBJECT_RELEASED ( WS )? '=' ( WS )? className ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:7: ( WS )?
					int alt86=2;
					int LA86_0 = input.LA(1);
					if ( (LA86_0==WS) ) {
						alt86=1;
					}
					switch (alt86) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5115); 
							}
							break;

					}

					match(input,TOKEN_OBJECT_RELEASED,FOLLOW_TOKEN_OBJECT_RELEASED_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5118); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:36: ( WS )?
					int alt87=2;
					int LA87_0 = input.LA(1);
					if ( (LA87_0==WS) ) {
						alt87=1;
					}
					switch (alt87) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:36: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5123); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5126); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:44: ( WS )?
					int alt88=2;
					int LA88_0 = input.LA(1);
					if ( (LA88_0==WS) ) {
						alt88=1;
					}
					switch (alt88) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:44: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5128); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5131);
					className23=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:58: ( WS )?
					int alt89=2;
					int LA89_0 = input.LA(1);
					if ( (LA89_0==WS) ) {
						int LA89_1 = input.LA(2);
						if ( (LA89_1==EOF||LA89_1==WS||LA89_1==106) ) {
							alt89=1;
						}
					}
					switch (alt89) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:304:58: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5133); 
							}
							break;

					}

					 bp = expFactory.getBreakpointObjectReleased(className23); 
					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:7: ( WS )? TOKEN_EXCEPTION_THROWN ( WS )? '=' ( WS )? className ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:7: ( WS )?
					int alt90=2;
					int LA90_0 = input.LA(1);
					if ( (LA90_0==WS) ) {
						alt90=1;
					}
					switch (alt90) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5187); 
							}
							break;

					}

					match(input,TOKEN_EXCEPTION_THROWN,FOLLOW_TOKEN_EXCEPTION_THROWN_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5190); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:36: ( WS )?
					int alt91=2;
					int LA91_0 = input.LA(1);
					if ( (LA91_0==WS) ) {
						alt91=1;
					}
					switch (alt91) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:36: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5194); 
							}
							break;

					}

					match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5197); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:44: ( WS )?
					int alt92=2;
					int LA92_0 = input.LA(1);
					if ( (LA92_0==WS) ) {
						alt92=1;
					}
					switch (alt92) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:44: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5199); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5202);
					className24=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:58: ( WS )?
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( (LA93_0==WS) ) {
						int LA93_1 = input.LA(2);
						if ( (LA93_1==EOF||LA93_1==WS||LA93_1==106) ) {
							alt93=1;
						}
					}
					switch (alt93) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:305:58: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5204); 
							}
							break;

					}

					 bp = expFactory.getBreakpointExpecptionThrown(className24); 
					}
					break;
				case 6 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:306:7: l= cmdStateExpression1Value[expFactory] relOp[expFactory] r= cmdStateExpression1Value[expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpression1Value_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5260);
					l=cmdStateExpression1Value(expFactory);
					state._fsp--;

					pushFollow(FOLLOW_relOp_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5264);
					relOp25=relOp(expFactory);
					state._fsp--;

					pushFollow(FOLLOW_cmdStateExpression1Value_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5269);
					r=cmdStateExpression1Value(expFactory);
					state._fsp--;

					 bp = expFactory.getBreakpointCompare(l, relOp25, r); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bp;
	}
	// $ANTLR end "cmdBreakpoinstCreateParamsAtomTerminateIDF"



	// $ANTLR start "allKeyWords"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:309:1: allKeyWords returns [String text] : (a= allKeyWordsIDFLike |a= allKeywordsOther |a= javaKeyWords );
	public final String allKeyWords() throws RecognitionException {
		String text = null;


		ParserRuleReturnScope a =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:310:5: (a= allKeyWordsIDFLike |a= allKeywordsOther |a= javaKeyWords )
			int alt95=3;
			switch ( input.LA(1) ) {
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_X:
				{
				alt95=1;
				}
				break;
			case ESCAPE_SEQ_B:
			case ESCAPE_SEQ_BACKSLASH:
			case ESCAPE_SEQ_DOUBLE_QUOTE:
			case ESCAPE_SEQ_F:
			case ESCAPE_SEQ_N:
			case ESCAPE_SEQ_OCTAL:
			case ESCAPE_SEQ_R:
			case ESCAPE_SEQ_SINGLE_QUOTE:
			case ESCAPE_SEQ_T:
			case ESCAPE_SEQ_UNICODE:
			case RELOP_EQUAL:
			case RELOP_GREATER_OR_EQUAL_THAN:
			case RELOP_GREATER_THAN:
			case RELOP_LESS_OR_EQUAL_THAN:
			case RELOP_LESS_THAN:
			case RELOP_NOT_EQUAL:
			case SIGN_ASTERISK:
			case SIGN_BACK_SHLASH:
			case SIGN_DOLAR:
			case SIGN_DOT:
			case SIGN_DOUBLE_QUOTE:
			case SIGN_EQUALS:
			case SIGN_MINUS:
			case SIGN_PLUS:
			case SIGN_SINGLE_QUOTE:
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_HEAP:
			case TOKEN_HASH_STACK_FRAME:
			case TOKEN_HASH_STACK_SLOT:
			case TOKEN_HASH_STATIC:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
			case TOKEN_HASH_THREAD:
			case TOKEN_NEGATIVE_INFINITY2:
			case TOKEN_NOT_A_NUMBER:
			case TOKEN_POSITIVE_INFINITY2:
				{
				alt95=2;
				}
				break;
			case TOKEN_FALSE:
			case TOKEN_NULL:
			case TOKEN_TRUE:
				{
				alt95=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 95, 0, input);
				throw nvae;
			}
			switch (alt95) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:310:7: a= allKeyWordsIDFLike
					{
					pushFollow(FOLLOW_allKeyWordsIDFLike_in_allKeyWords5296);
					allKeyWordsIDFLike();
					state._fsp--;

					 text = (a!=null?input.toString(a.start,a.stop):null); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:311:7: a= allKeywordsOther
					{
					pushFollow(FOLLOW_allKeywordsOther_in_allKeyWords5325);
					allKeywordsOther();
					state._fsp--;

					 text = (a!=null?input.toString(a.start,a.stop):null); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:312:7: a= javaKeyWords
					{
					pushFollow(FOLLOW_javaKeyWords_in_allKeyWords5356);
					a=javaKeyWords();
					state._fsp--;

					 text = (a!=null?input.toString(a.start,a.stop):null); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return text;
	}
	// $ANTLR end "allKeyWords"



	// $ANTLR start "fieldAccess"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:315:1: fieldAccess returns [BreakPointModes bpMode] : ( TOKEN_FIELD_ACCESS | TOKEN_FIELD_READ | TOKEN_FIELD_WRITE );
	public final BreakPointModes fieldAccess() throws RecognitionException {
		BreakPointModes bpMode = null;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:316:5: ( TOKEN_FIELD_ACCESS | TOKEN_FIELD_READ | TOKEN_FIELD_WRITE )
			int alt96=3;
			switch ( input.LA(1) ) {
			case TOKEN_FIELD_ACCESS:
				{
				alt96=1;
				}
				break;
			case TOKEN_FIELD_READ:
				{
				alt96=2;
				}
				break;
			case TOKEN_FIELD_WRITE:
				{
				alt96=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 96, 0, input);
				throw nvae;
			}
			switch (alt96) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:316:7: TOKEN_FIELD_ACCESS
					{
					match(input,TOKEN_FIELD_ACCESS,FOLLOW_TOKEN_FIELD_ACCESS_in_fieldAccess5403); 
					 bpMode = BreakPointModes.BP_MODE_FIELD_ACCESS; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:317:7: TOKEN_FIELD_READ
					{
					match(input,TOKEN_FIELD_READ,FOLLOW_TOKEN_FIELD_READ_in_fieldAccess5413); 
					 bpMode = BreakPointModes.BP_MODE_FIELD_ACCESS_READ; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:318:7: TOKEN_FIELD_WRITE
					{
					match(input,TOKEN_FIELD_WRITE,FOLLOW_TOKEN_FIELD_WRITE_in_fieldAccess5425); 
					 bpMode = BreakPointModes.BP_MODE_FIELD_ACCESS_WRITE; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bpMode;
	}
	// $ANTLR end "fieldAccess"



	// $ANTLR start "cmdGarbageCollectionSpec"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:321:1: cmdGarbageCollectionSpec returns [BreakPointModes bpMode] : ( TOKEN_BEGIN | TOKEN_END | TOKEN_BOTH );
	public final BreakPointModes cmdGarbageCollectionSpec() throws RecognitionException {
		BreakPointModes bpMode = null;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:322:5: ( TOKEN_BEGIN | TOKEN_END | TOKEN_BOTH )
			int alt97=3;
			switch ( input.LA(1) ) {
			case TOKEN_BEGIN:
				{
				alt97=1;
				}
				break;
			case TOKEN_END:
				{
				alt97=2;
				}
				break;
			case TOKEN_BOTH:
				{
				alt97=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 97, 0, input);
				throw nvae;
			}
			switch (alt97) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:322:7: TOKEN_BEGIN
					{
					match(input,TOKEN_BEGIN,FOLLOW_TOKEN_BEGIN_in_cmdGarbageCollectionSpec5449); 
					 bpMode = BreakPointModes.BP_MODE_GC_BEGIN; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:323:7: TOKEN_END
					{
					match(input,TOKEN_END,FOLLOW_TOKEN_END_in_cmdGarbageCollectionSpec5459); 
					 bpMode = BreakPointModes.BP_MODE_GC_END; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:324:7: TOKEN_BOTH
					{
					match(input,TOKEN_BOTH,FOLLOW_TOKEN_BOTH_in_cmdGarbageCollectionSpec5471); 
					 bpMode = BreakPointModes.BP_MODE_GC_BOTH; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bpMode;
	}
	// $ANTLR end "cmdGarbageCollectionSpec"



	// $ANTLR start "cmdChoiceGeneratorType"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:327:1: cmdChoiceGeneratorType returns [BreakPointModes bpMode] : ( TOKEN_DATA | TOKEN_SCHEDULING | TOKEN_BOTH );
	public final BreakPointModes cmdChoiceGeneratorType() throws RecognitionException {
		BreakPointModes bpMode = null;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:328:5: ( TOKEN_DATA | TOKEN_SCHEDULING | TOKEN_BOTH )
			int alt98=3;
			switch ( input.LA(1) ) {
			case TOKEN_DATA:
				{
				alt98=1;
				}
				break;
			case TOKEN_SCHEDULING:
				{
				alt98=2;
				}
				break;
			case TOKEN_BOTH:
				{
				alt98=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 98, 0, input);
				throw nvae;
			}
			switch (alt98) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:328:7: TOKEN_DATA
					{
					match(input,TOKEN_DATA,FOLLOW_TOKEN_DATA_in_cmdChoiceGeneratorType5495); 
					 bpMode = BreakPointModes.BP_MODE_CHOICE_DATA; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:329:7: TOKEN_SCHEDULING
					{
					match(input,TOKEN_SCHEDULING,FOLLOW_TOKEN_SCHEDULING_in_cmdChoiceGeneratorType5511); 
					 bpMode = BreakPointModes.BP_MODE_CHOICE_SCHEDULING; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:330:7: TOKEN_BOTH
					{
					match(input,TOKEN_BOTH,FOLLOW_TOKEN_BOTH_in_cmdChoiceGeneratorType5521); 
					 bpMode = BreakPointModes.BP_MODE_CHOICE_BOTH; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bpMode;
	}
	// $ANTLR end "cmdChoiceGeneratorType"



	// $ANTLR start "cmdInstructionTypes"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:333:1: cmdInstructionTypes returns [InstructionTypes instructionType] : ( TOKEN_ANY | TOKEN_NONE | TOKEN_INVOKE | TOKEN_RETURN | TOKEN_FIELD_ACCESS | TOKEN_FIELD_READ | TOKEN_FIELD_WRITE | TOKEN_CONDITION | TOKEN_LOCK | TOKEN_ARRAY );
	public final InstructionTypes cmdInstructionTypes() throws RecognitionException {
		InstructionTypes instructionType = null;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:334:5: ( TOKEN_ANY | TOKEN_NONE | TOKEN_INVOKE | TOKEN_RETURN | TOKEN_FIELD_ACCESS | TOKEN_FIELD_READ | TOKEN_FIELD_WRITE | TOKEN_CONDITION | TOKEN_LOCK | TOKEN_ARRAY )
			int alt99=10;
			switch ( input.LA(1) ) {
			case TOKEN_ANY:
				{
				alt99=1;
				}
				break;
			case TOKEN_NONE:
				{
				alt99=2;
				}
				break;
			case TOKEN_INVOKE:
				{
				alt99=3;
				}
				break;
			case TOKEN_RETURN:
				{
				alt99=4;
				}
				break;
			case TOKEN_FIELD_ACCESS:
				{
				alt99=5;
				}
				break;
			case TOKEN_FIELD_READ:
				{
				alt99=6;
				}
				break;
			case TOKEN_FIELD_WRITE:
				{
				alt99=7;
				}
				break;
			case TOKEN_CONDITION:
				{
				alt99=8;
				}
				break;
			case TOKEN_LOCK:
				{
				alt99=9;
				}
				break;
			case TOKEN_ARRAY:
				{
				alt99=10;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 99, 0, input);
				throw nvae;
			}
			switch (alt99) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:334:7: TOKEN_ANY
					{
					match(input,TOKEN_ANY,FOLLOW_TOKEN_ANY_in_cmdInstructionTypes5550); 
					 instructionType = InstructionTypes.IT_ANY; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:335:7: TOKEN_NONE
					{
					match(input,TOKEN_NONE,FOLLOW_TOKEN_NONE_in_cmdInstructionTypes5574); 
					 instructionType = InstructionTypes.IT_NONE; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:336:7: TOKEN_INVOKE
					{
					match(input,TOKEN_INVOKE,FOLLOW_TOKEN_INVOKE_in_cmdInstructionTypes5597); 
					 instructionType = InstructionTypes.IT_INVOKE; 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:337:7: TOKEN_RETURN
					{
					match(input,TOKEN_RETURN,FOLLOW_TOKEN_RETURN_in_cmdInstructionTypes5618); 
					 instructionType = InstructionTypes.IT_RETURN; 
					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:338:7: TOKEN_FIELD_ACCESS
					{
					match(input,TOKEN_FIELD_ACCESS,FOLLOW_TOKEN_FIELD_ACCESS_in_cmdInstructionTypes5639); 
					 instructionType = InstructionTypes.IT_FIELD_ACCESS; 
					}
					break;
				case 6 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:339:7: TOKEN_FIELD_READ
					{
					match(input,TOKEN_FIELD_READ,FOLLOW_TOKEN_FIELD_READ_in_cmdInstructionTypes5654); 
					 instructionType = InstructionTypes.IT_FIELD_READ; 
					}
					break;
				case 7 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:340:7: TOKEN_FIELD_WRITE
					{
					match(input,TOKEN_FIELD_WRITE,FOLLOW_TOKEN_FIELD_WRITE_in_cmdInstructionTypes5671); 
					 instructionType = InstructionTypes.IT_FIELD_WRITE; 
					}
					break;
				case 8 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:341:7: TOKEN_CONDITION
					{
					match(input,TOKEN_CONDITION,FOLLOW_TOKEN_CONDITION_in_cmdInstructionTypes5687); 
					 instructionType = InstructionTypes.IT_IFCOND; 
					}
					break;
				case 9 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:342:7: TOKEN_LOCK
					{
					match(input,TOKEN_LOCK,FOLLOW_TOKEN_LOCK_in_cmdInstructionTypes5705); 
					 instructionType = InstructionTypes.IT_LOCK; 
					}
					break;
				case 10 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:343:7: TOKEN_ARRAY
					{
					match(input,TOKEN_ARRAY,FOLLOW_TOKEN_ARRAY_in_cmdInstructionTypes5728); 
					 instructionType = InstructionTypes.IT_ARRAY; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return instructionType;
	}
	// $ANTLR end "cmdInstructionTypes"



	// $ANTLR start "cmdThreadScheduledDirection"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:346:1: cmdThreadScheduledDirection returns [BreakPointModes bpMode] : ( TOKEN_IN | TOKEN_OUT | TOKEN_BOTH );
	public final BreakPointModes cmdThreadScheduledDirection() throws RecognitionException {
		BreakPointModes bpMode = null;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:347:5: ( TOKEN_IN | TOKEN_OUT | TOKEN_BOTH )
			int alt100=3;
			switch ( input.LA(1) ) {
			case TOKEN_IN:
				{
				alt100=1;
				}
				break;
			case TOKEN_OUT:
				{
				alt100=2;
				}
				break;
			case TOKEN_BOTH:
				{
				alt100=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 100, 0, input);
				throw nvae;
			}
			switch (alt100) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:347:7: TOKEN_IN
					{
					match(input,TOKEN_IN,FOLLOW_TOKEN_IN_in_cmdThreadScheduledDirection5763); 
					 bpMode = BreakPointModes.BP_MODE_THREAD_SCHEDULED_IN; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:348:7: TOKEN_OUT
					{
					match(input,TOKEN_OUT,FOLLOW_TOKEN_OUT_in_cmdThreadScheduledDirection5788); 
					 bpMode = BreakPointModes.BP_MODE_THREAD_SCHEDULED_OUT; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:349:7: TOKEN_BOTH
					{
					match(input,TOKEN_BOTH,FOLLOW_TOKEN_BOTH_in_cmdThreadScheduledDirection5812); 
					 bpMode = BreakPointModes.BP_MODE_THREAD_SCHEDULED_BOTH; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return bpMode;
	}
	// $ANTLR end "cmdThreadScheduledDirection"



	// $ANTLR start "cmdStateAssignment"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:352:1: cmdStateAssignment[ExpressionFactory expFactory] returns [ExpressionStateAssignment expr] : lval= cmdStateExpression1[$expFactory] SIGN_EQUALS rval= cmdStateExpression1[$expFactory] EOF ;
	public final ExpressionStateAssignment cmdStateAssignment(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateAssignment expr = null;


		ExpressionStateRootNode<?> lval =null;
		ExpressionStateRootNode<?> rval =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:353:5: (lval= cmdStateExpression1[$expFactory] SIGN_EQUALS rval= cmdStateExpression1[$expFactory] EOF )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:353:7: lval= cmdStateExpression1[$expFactory] SIGN_EQUALS rval= cmdStateExpression1[$expFactory] EOF
			{
			pushFollow(FOLLOW_cmdStateExpression1_in_cmdStateAssignment5852);
			lval=cmdStateExpression1(expFactory);
			state._fsp--;

			match(input,SIGN_EQUALS,FOLLOW_SIGN_EQUALS_in_cmdStateAssignment5855); 
			pushFollow(FOLLOW_cmdStateExpression1_in_cmdStateAssignment5859);
			rval=cmdStateExpression1(expFactory);
			state._fsp--;

			 expr = expFactory.getStateAssignment(lval, rval); 
			match(input,EOF,FOLLOW_EOF_in_cmdStateAssignment5864); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateAssignment"



	// $ANTLR start "cmdStateExpression"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:356:1: cmdStateExpression[ExpressionFactory expFactory] returns [ExpressionStateRootNode<?> expr] : cmdStateExpression1[$expFactory] EOF ;
	public final ExpressionStateRootNode<?> cmdStateExpression(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateRootNode<?> expr = null;


		ExpressionStateRootNode<?> cmdStateExpression126 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:357:5: ( cmdStateExpression1[$expFactory] EOF )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:357:7: cmdStateExpression1[$expFactory] EOF
			{
			pushFollow(FOLLOW_cmdStateExpression1_in_cmdStateExpression5892);
			cmdStateExpression126=cmdStateExpression1(expFactory);
			state._fsp--;

			 expr = cmdStateExpression126; 
			match(input,EOF,FOLLOW_EOF_in_cmdStateExpression5897); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpression"



	// $ANTLR start "cmdStateExpression1"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:360:1: cmdStateExpression1[ExpressionFactory expFactory] returns [ExpressionStateRootNode<?> expr] : ( ( WS )? cmdStateExpressionThread[$expFactory] | ( WS )? cmdStateExpressionHeap[$expFactory] | ( WS )? cmdStateConstValue[$expFactory] ( WS )? );
	public final ExpressionStateRootNode<?> cmdStateExpression1(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateRootNode<?> expr = null;


		ExpressionStateThread cmdStateExpressionThread27 =null;
		ExpressionStateRootNode<ExpressionStateValue> cmdStateExpressionHeap28 =null;
		ExpressionStateRootNode<ExpressionStateValue> cmdStateConstValue29 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:361:5: ( ( WS )? cmdStateExpressionThread[$expFactory] | ( WS )? cmdStateExpressionHeap[$expFactory] | ( WS )? cmdStateConstValue[$expFactory] ( WS )? )
			int alt105=3;
			switch ( input.LA(1) ) {
			case WS:
				{
				switch ( input.LA(2) ) {
				case EOF:
				case IDF_TEXT_INTERNAL:
				case SIGN_EQUALS:
				case TOKEN_AND:
				case TOKEN_ANY:
				case TOKEN_ARRAY:
				case TOKEN_ASSERT:
				case TOKEN_BEGIN:
				case TOKEN_BOTH:
				case TOKEN_CHOICE_GENERATOR:
				case TOKEN_CONDITION:
				case TOKEN_D:
				case TOKEN_DATA:
				case TOKEN_E:
				case TOKEN_END:
				case TOKEN_EXCEPTION_THROWN:
				case TOKEN_F:
				case TOKEN_FIELD_ACCESS:
				case TOKEN_FIELD_READ:
				case TOKEN_FIELD_WRITE:
				case TOKEN_GARBAGE_COLLECTION:
				case TOKEN_HASH_FIELD:
				case TOKEN_HASH_OUTER_CLASS:
				case TOKEN_HASH_STACK_FRAME:
				case TOKEN_HASH_STACK_SLOT:
				case TOKEN_HASH_SUPER:
				case TOKEN_HASH_THIS:
				case TOKEN_HASH_THREAD:
				case TOKEN_HIT_COUNT:
				case TOKEN_IN:
				case TOKEN_INFINITY:
				case TOKEN_INSTRUCTION:
				case TOKEN_INSTRUCTION_TYPE:
				case TOKEN_INVOKE:
				case TOKEN_L:
				case TOKEN_LOCK:
				case TOKEN_METHOD_INVOKE:
				case TOKEN_NAN:
				case TOKEN_NEGATIVE_INFINITY1:
				case TOKEN_NONE:
				case TOKEN_NOTIFY:
				case TOKEN_OBJECT_CREATED:
				case TOKEN_OBJECT_RELEASED:
				case TOKEN_OR:
				case TOKEN_POSITION:
				case TOKEN_POSITIVE_INFINITY1:
				case TOKEN_PROPERTY_VIOLATED:
				case TOKEN_RETURN:
				case TOKEN_SCHEDULING:
				case TOKEN_STACK_FRAME:
				case TOKEN_STATE_ADVANCED:
				case TOKEN_STEP_IN:
				case TOKEN_STEP_OUT:
				case TOKEN_STEP_OVER:
				case TOKEN_SYNC_BLOCK:
				case TOKEN_THREAD:
				case TOKEN_THREAD_SCHEDULED:
				case TOKEN_X:
					{
					alt105=1;
					}
					break;
				case TOKEN_HASH_STATIC:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA105_6 = input.LA(4);
						if ( (LA105_6==109) ) {
							switch ( input.LA(5) ) {
							case WS:
								{
								int LA105_8 = input.LA(6);
								if ( (LA105_8==HEX||(LA105_8 >= INT && LA105_8 <= INT_TEXT)) ) {
									alt105=1;
								}
								else if ( (LA105_8==IDF_TEXT_INTERNAL||LA105_8==SIGN_ASTERISK||(LA105_8 >= SIGN_DOLAR && LA105_8 <= SIGN_DOT)||(LA105_8 >= TOKEN_AND && LA105_8 <= TOKEN_ASSERT)||(LA105_8 >= TOKEN_BEGIN && LA105_8 <= TOKEN_GARBAGE_COLLECTION)||(LA105_8 >= TOKEN_HIT_COUNT && LA105_8 <= TOKEN_NEGATIVE_INFINITY1)||(LA105_8 >= TOKEN_NONE && LA105_8 <= TOKEN_NOTIFY)||(LA105_8 >= TOKEN_NULL && LA105_8 <= TOKEN_OR)||(LA105_8 >= TOKEN_POSITION && LA105_8 <= TOKEN_POSITIVE_INFINITY1)||(LA105_8 >= TOKEN_PROPERTY_VIOLATED && LA105_8 <= TOKEN_SCHEDULING)||(LA105_8 >= TOKEN_STACK_FRAME && LA105_8 <= TOKEN_X)) ) {
									alt105=2;
								}

								else {
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++) {
											input.consume();
										}
										NoViableAltException nvae =
											new NoViableAltException("", 105, 8, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

								}
								break;
							case HEX:
							case INT:
							case INT_TEXT:
								{
								alt105=1;
								}
								break;
							case IDF_TEXT_INTERNAL:
							case SIGN_ASTERISK:
							case SIGN_DOLAR:
							case SIGN_DOT:
							case TOKEN_AND:
							case TOKEN_ANY:
							case TOKEN_ARRAY:
							case TOKEN_ASSERT:
							case TOKEN_BEGIN:
							case TOKEN_BOTH:
							case TOKEN_CHOICE_GENERATOR:
							case TOKEN_CONDITION:
							case TOKEN_D:
							case TOKEN_DATA:
							case TOKEN_E:
							case TOKEN_END:
							case TOKEN_EXCEPTION_THROWN:
							case TOKEN_F:
							case TOKEN_FALSE:
							case TOKEN_FIELD_ACCESS:
							case TOKEN_FIELD_READ:
							case TOKEN_FIELD_WRITE:
							case TOKEN_GARBAGE_COLLECTION:
							case TOKEN_HIT_COUNT:
							case TOKEN_IN:
							case TOKEN_INFINITY:
							case TOKEN_INSTRUCTION:
							case TOKEN_INSTRUCTION_TYPE:
							case TOKEN_INVOKE:
							case TOKEN_L:
							case TOKEN_LOCK:
							case TOKEN_METHOD_INVOKE:
							case TOKEN_NAN:
							case TOKEN_NEGATIVE_INFINITY1:
							case TOKEN_NONE:
							case TOKEN_NOTIFY:
							case TOKEN_NULL:
							case TOKEN_OBJECT_CREATED:
							case TOKEN_OBJECT_RELEASED:
							case TOKEN_OR:
							case TOKEN_POSITION:
							case TOKEN_POSITIVE_INFINITY1:
							case TOKEN_PROPERTY_VIOLATED:
							case TOKEN_RETURN:
							case TOKEN_SCHEDULING:
							case TOKEN_STACK_FRAME:
							case TOKEN_STATE_ADVANCED:
							case TOKEN_STEP_IN:
							case TOKEN_STEP_OUT:
							case TOKEN_STEP_OVER:
							case TOKEN_SYNC_BLOCK:
							case TOKEN_THREAD:
							case TOKEN_THREAD_SCHEDULED:
							case TOKEN_TRUE:
							case TOKEN_X:
								{
								alt105=2;
								}
								break;
							default:
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 105, 7, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}
						}
						else if ( (LA105_6==EOF||LA105_6==SIGN_DOT||LA105_6==SIGN_EQUALS) ) {
							alt105=1;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 105, 6, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 109:
						{
						switch ( input.LA(4) ) {
						case WS:
							{
							int LA105_8 = input.LA(5);
							if ( (LA105_8==HEX||(LA105_8 >= INT && LA105_8 <= INT_TEXT)) ) {
								alt105=1;
							}
							else if ( (LA105_8==IDF_TEXT_INTERNAL||LA105_8==SIGN_ASTERISK||(LA105_8 >= SIGN_DOLAR && LA105_8 <= SIGN_DOT)||(LA105_8 >= TOKEN_AND && LA105_8 <= TOKEN_ASSERT)||(LA105_8 >= TOKEN_BEGIN && LA105_8 <= TOKEN_GARBAGE_COLLECTION)||(LA105_8 >= TOKEN_HIT_COUNT && LA105_8 <= TOKEN_NEGATIVE_INFINITY1)||(LA105_8 >= TOKEN_NONE && LA105_8 <= TOKEN_NOTIFY)||(LA105_8 >= TOKEN_NULL && LA105_8 <= TOKEN_OR)||(LA105_8 >= TOKEN_POSITION && LA105_8 <= TOKEN_POSITIVE_INFINITY1)||(LA105_8 >= TOKEN_PROPERTY_VIOLATED && LA105_8 <= TOKEN_SCHEDULING)||(LA105_8 >= TOKEN_STACK_FRAME && LA105_8 <= TOKEN_X)) ) {
								alt105=2;
							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 105, 8, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

							}
							break;
						case HEX:
						case INT:
						case INT_TEXT:
							{
							alt105=1;
							}
							break;
						case IDF_TEXT_INTERNAL:
						case SIGN_ASTERISK:
						case SIGN_DOLAR:
						case SIGN_DOT:
						case TOKEN_AND:
						case TOKEN_ANY:
						case TOKEN_ARRAY:
						case TOKEN_ASSERT:
						case TOKEN_BEGIN:
						case TOKEN_BOTH:
						case TOKEN_CHOICE_GENERATOR:
						case TOKEN_CONDITION:
						case TOKEN_D:
						case TOKEN_DATA:
						case TOKEN_E:
						case TOKEN_END:
						case TOKEN_EXCEPTION_THROWN:
						case TOKEN_F:
						case TOKEN_FALSE:
						case TOKEN_FIELD_ACCESS:
						case TOKEN_FIELD_READ:
						case TOKEN_FIELD_WRITE:
						case TOKEN_GARBAGE_COLLECTION:
						case TOKEN_HIT_COUNT:
						case TOKEN_IN:
						case TOKEN_INFINITY:
						case TOKEN_INSTRUCTION:
						case TOKEN_INSTRUCTION_TYPE:
						case TOKEN_INVOKE:
						case TOKEN_L:
						case TOKEN_LOCK:
						case TOKEN_METHOD_INVOKE:
						case TOKEN_NAN:
						case TOKEN_NEGATIVE_INFINITY1:
						case TOKEN_NONE:
						case TOKEN_NOTIFY:
						case TOKEN_NULL:
						case TOKEN_OBJECT_CREATED:
						case TOKEN_OBJECT_RELEASED:
						case TOKEN_OR:
						case TOKEN_POSITION:
						case TOKEN_POSITIVE_INFINITY1:
						case TOKEN_PROPERTY_VIOLATED:
						case TOKEN_RETURN:
						case TOKEN_SCHEDULING:
						case TOKEN_STACK_FRAME:
						case TOKEN_STATE_ADVANCED:
						case TOKEN_STEP_IN:
						case TOKEN_STEP_OUT:
						case TOKEN_STEP_OVER:
						case TOKEN_SYNC_BLOCK:
						case TOKEN_THREAD:
						case TOKEN_THREAD_SCHEDULED:
						case TOKEN_TRUE:
						case TOKEN_X:
							{
							alt105=2;
							}
							break;
						default:
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 105, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
						}
						break;
					case EOF:
					case SIGN_DOT:
					case SIGN_EQUALS:
						{
						alt105=1;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 105, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_HASH_HEAP:
					{
					alt105=2;
					}
					break;
				case CHAR:
				case DOUBLE_TEXT_LIMITTED:
				case FLOAT_TEXT:
				case HEX:
				case INT:
				case INT_TEXT:
				case LONG_TEXT:
				case STRING:
				case TOKEN_FALSE:
				case TOKEN_NEGATIVE_INFINITY2:
				case TOKEN_NOT_A_NUMBER:
				case TOKEN_NULL:
				case TOKEN_POSITIVE_INFINITY2:
				case TOKEN_TRUE:
					{
					alt105=3;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 105, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case EOF:
			case IDF_TEXT_INTERNAL:
			case SIGN_EQUALS:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_OUTER_CLASS:
			case TOKEN_HASH_STACK_FRAME:
			case TOKEN_HASH_STACK_SLOT:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
			case TOKEN_HASH_THREAD:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_X:
				{
				alt105=1;
				}
				break;
			case TOKEN_HASH_STATIC:
				{
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA105_6 = input.LA(3);
					if ( (LA105_6==109) ) {
						switch ( input.LA(4) ) {
						case WS:
							{
							int LA105_8 = input.LA(5);
							if ( (LA105_8==HEX||(LA105_8 >= INT && LA105_8 <= INT_TEXT)) ) {
								alt105=1;
							}
							else if ( (LA105_8==IDF_TEXT_INTERNAL||LA105_8==SIGN_ASTERISK||(LA105_8 >= SIGN_DOLAR && LA105_8 <= SIGN_DOT)||(LA105_8 >= TOKEN_AND && LA105_8 <= TOKEN_ASSERT)||(LA105_8 >= TOKEN_BEGIN && LA105_8 <= TOKEN_GARBAGE_COLLECTION)||(LA105_8 >= TOKEN_HIT_COUNT && LA105_8 <= TOKEN_NEGATIVE_INFINITY1)||(LA105_8 >= TOKEN_NONE && LA105_8 <= TOKEN_NOTIFY)||(LA105_8 >= TOKEN_NULL && LA105_8 <= TOKEN_OR)||(LA105_8 >= TOKEN_POSITION && LA105_8 <= TOKEN_POSITIVE_INFINITY1)||(LA105_8 >= TOKEN_PROPERTY_VIOLATED && LA105_8 <= TOKEN_SCHEDULING)||(LA105_8 >= TOKEN_STACK_FRAME && LA105_8 <= TOKEN_X)) ) {
								alt105=2;
							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 105, 8, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

							}
							break;
						case HEX:
						case INT:
						case INT_TEXT:
							{
							alt105=1;
							}
							break;
						case IDF_TEXT_INTERNAL:
						case SIGN_ASTERISK:
						case SIGN_DOLAR:
						case SIGN_DOT:
						case TOKEN_AND:
						case TOKEN_ANY:
						case TOKEN_ARRAY:
						case TOKEN_ASSERT:
						case TOKEN_BEGIN:
						case TOKEN_BOTH:
						case TOKEN_CHOICE_GENERATOR:
						case TOKEN_CONDITION:
						case TOKEN_D:
						case TOKEN_DATA:
						case TOKEN_E:
						case TOKEN_END:
						case TOKEN_EXCEPTION_THROWN:
						case TOKEN_F:
						case TOKEN_FALSE:
						case TOKEN_FIELD_ACCESS:
						case TOKEN_FIELD_READ:
						case TOKEN_FIELD_WRITE:
						case TOKEN_GARBAGE_COLLECTION:
						case TOKEN_HIT_COUNT:
						case TOKEN_IN:
						case TOKEN_INFINITY:
						case TOKEN_INSTRUCTION:
						case TOKEN_INSTRUCTION_TYPE:
						case TOKEN_INVOKE:
						case TOKEN_L:
						case TOKEN_LOCK:
						case TOKEN_METHOD_INVOKE:
						case TOKEN_NAN:
						case TOKEN_NEGATIVE_INFINITY1:
						case TOKEN_NONE:
						case TOKEN_NOTIFY:
						case TOKEN_NULL:
						case TOKEN_OBJECT_CREATED:
						case TOKEN_OBJECT_RELEASED:
						case TOKEN_OR:
						case TOKEN_POSITION:
						case TOKEN_POSITIVE_INFINITY1:
						case TOKEN_PROPERTY_VIOLATED:
						case TOKEN_RETURN:
						case TOKEN_SCHEDULING:
						case TOKEN_STACK_FRAME:
						case TOKEN_STATE_ADVANCED:
						case TOKEN_STEP_IN:
						case TOKEN_STEP_OUT:
						case TOKEN_STEP_OVER:
						case TOKEN_SYNC_BLOCK:
						case TOKEN_THREAD:
						case TOKEN_THREAD_SCHEDULED:
						case TOKEN_TRUE:
						case TOKEN_X:
							{
							alt105=2;
							}
							break;
						default:
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 105, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}
					else if ( (LA105_6==EOF||LA105_6==SIGN_DOT||LA105_6==SIGN_EQUALS) ) {
						alt105=1;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 105, 6, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 109:
					{
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA105_8 = input.LA(4);
						if ( (LA105_8==HEX||(LA105_8 >= INT && LA105_8 <= INT_TEXT)) ) {
							alt105=1;
						}
						else if ( (LA105_8==IDF_TEXT_INTERNAL||LA105_8==SIGN_ASTERISK||(LA105_8 >= SIGN_DOLAR && LA105_8 <= SIGN_DOT)||(LA105_8 >= TOKEN_AND && LA105_8 <= TOKEN_ASSERT)||(LA105_8 >= TOKEN_BEGIN && LA105_8 <= TOKEN_GARBAGE_COLLECTION)||(LA105_8 >= TOKEN_HIT_COUNT && LA105_8 <= TOKEN_NEGATIVE_INFINITY1)||(LA105_8 >= TOKEN_NONE && LA105_8 <= TOKEN_NOTIFY)||(LA105_8 >= TOKEN_NULL && LA105_8 <= TOKEN_OR)||(LA105_8 >= TOKEN_POSITION && LA105_8 <= TOKEN_POSITIVE_INFINITY1)||(LA105_8 >= TOKEN_PROPERTY_VIOLATED && LA105_8 <= TOKEN_SCHEDULING)||(LA105_8 >= TOKEN_STACK_FRAME && LA105_8 <= TOKEN_X)) ) {
							alt105=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 105, 8, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case HEX:
					case INT:
					case INT_TEXT:
						{
						alt105=1;
						}
						break;
					case IDF_TEXT_INTERNAL:
					case SIGN_ASTERISK:
					case SIGN_DOLAR:
					case SIGN_DOT:
					case TOKEN_AND:
					case TOKEN_ANY:
					case TOKEN_ARRAY:
					case TOKEN_ASSERT:
					case TOKEN_BEGIN:
					case TOKEN_BOTH:
					case TOKEN_CHOICE_GENERATOR:
					case TOKEN_CONDITION:
					case TOKEN_D:
					case TOKEN_DATA:
					case TOKEN_E:
					case TOKEN_END:
					case TOKEN_EXCEPTION_THROWN:
					case TOKEN_F:
					case TOKEN_FALSE:
					case TOKEN_FIELD_ACCESS:
					case TOKEN_FIELD_READ:
					case TOKEN_FIELD_WRITE:
					case TOKEN_GARBAGE_COLLECTION:
					case TOKEN_HIT_COUNT:
					case TOKEN_IN:
					case TOKEN_INFINITY:
					case TOKEN_INSTRUCTION:
					case TOKEN_INSTRUCTION_TYPE:
					case TOKEN_INVOKE:
					case TOKEN_L:
					case TOKEN_LOCK:
					case TOKEN_METHOD_INVOKE:
					case TOKEN_NAN:
					case TOKEN_NEGATIVE_INFINITY1:
					case TOKEN_NONE:
					case TOKEN_NOTIFY:
					case TOKEN_NULL:
					case TOKEN_OBJECT_CREATED:
					case TOKEN_OBJECT_RELEASED:
					case TOKEN_OR:
					case TOKEN_POSITION:
					case TOKEN_POSITIVE_INFINITY1:
					case TOKEN_PROPERTY_VIOLATED:
					case TOKEN_RETURN:
					case TOKEN_SCHEDULING:
					case TOKEN_STACK_FRAME:
					case TOKEN_STATE_ADVANCED:
					case TOKEN_STEP_IN:
					case TOKEN_STEP_OUT:
					case TOKEN_STEP_OVER:
					case TOKEN_SYNC_BLOCK:
					case TOKEN_THREAD:
					case TOKEN_THREAD_SCHEDULED:
					case TOKEN_TRUE:
					case TOKEN_X:
						{
						alt105=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 105, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case EOF:
				case SIGN_DOT:
				case SIGN_EQUALS:
					{
					alt105=1;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 105, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case TOKEN_HASH_HEAP:
				{
				alt105=2;
				}
				break;
			case CHAR:
			case DOUBLE_TEXT_LIMITTED:
			case FLOAT_TEXT:
			case HEX:
			case INT:
			case INT_TEXT:
			case LONG_TEXT:
			case STRING:
			case TOKEN_FALSE:
			case TOKEN_NEGATIVE_INFINITY2:
			case TOKEN_NOT_A_NUMBER:
			case TOKEN_NULL:
			case TOKEN_POSITIVE_INFINITY2:
			case TOKEN_TRUE:
				{
				alt105=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 105, 0, input);
				throw nvae;
			}
			switch (alt105) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:361:7: ( WS )? cmdStateExpressionThread[$expFactory]
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:361:7: ( WS )?
					int alt101=2;
					int LA101_0 = input.LA(1);
					if ( (LA101_0==WS) ) {
						alt101=1;
					}
					switch (alt101) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:361:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression15923); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionThread_in_cmdStateExpression15926);
					cmdStateExpressionThread27=cmdStateExpressionThread(expFactory);
					state._fsp--;

					 expr = cmdStateExpressionThread27; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:362:7: ( WS )? cmdStateExpressionHeap[$expFactory]
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:362:7: ( WS )?
					int alt102=2;
					int LA102_0 = input.LA(1);
					if ( (LA102_0==WS) ) {
						alt102=1;
					}
					switch (alt102) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:362:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression15944); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionHeap_in_cmdStateExpression15947);
					cmdStateExpressionHeap28=cmdStateExpressionHeap(expFactory);
					state._fsp--;

					 expr = cmdStateExpressionHeap28; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:363:7: ( WS )? cmdStateConstValue[$expFactory] ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:363:7: ( WS )?
					int alt103=2;
					int LA103_0 = input.LA(1);
					if ( (LA103_0==WS) ) {
						alt103=1;
					}
					switch (alt103) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:363:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression15967); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateConstValue_in_cmdStateExpression15970);
					cmdStateConstValue29=cmdStateConstValue(expFactory);
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:363:43: ( WS )?
					int alt104=2;
					int LA104_0 = input.LA(1);
					if ( (LA104_0==WS) ) {
						alt104=1;
					}
					switch (alt104) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:363:43: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression15973); 
							}
							break;

					}

					 expr = cmdStateConstValue29; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpression1"



	// $ANTLR start "cmdStateExpression1Value"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:370:1: cmdStateExpression1Value[ExpressionFactory expFactory] returns [ExpressionStateRootNode<?> expr] : ( ( WS )? cmdStateExpressionThreadValue[$expFactory] | ( WS )? cmdStateExpressionHeapValue[$expFactory] | ( WS )? cmdStateConstValue[$expFactory] ( WS )? );
	public final ExpressionStateRootNode<?> cmdStateExpression1Value(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateRootNode<?> expr = null;


		ExpressionStateThread cmdStateExpressionThreadValue30 =null;
		ExpressionStateRootNode<ExpressionStateValue> cmdStateExpressionHeapValue31 =null;
		ExpressionStateRootNode<ExpressionStateValue> cmdStateConstValue32 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:371:5: ( ( WS )? cmdStateExpressionThreadValue[$expFactory] | ( WS )? cmdStateExpressionHeapValue[$expFactory] | ( WS )? cmdStateConstValue[$expFactory] ( WS )? )
			int alt110=3;
			switch ( input.LA(1) ) {
			case WS:
				{
				switch ( input.LA(2) ) {
				case IDF_TEXT_INTERNAL:
				case TOKEN_AND:
				case TOKEN_ANY:
				case TOKEN_ARRAY:
				case TOKEN_ASSERT:
				case TOKEN_BEGIN:
				case TOKEN_BOTH:
				case TOKEN_CHOICE_GENERATOR:
				case TOKEN_CONDITION:
				case TOKEN_D:
				case TOKEN_DATA:
				case TOKEN_E:
				case TOKEN_END:
				case TOKEN_EXCEPTION_THROWN:
				case TOKEN_F:
				case TOKEN_FIELD_ACCESS:
				case TOKEN_FIELD_READ:
				case TOKEN_FIELD_WRITE:
				case TOKEN_GARBAGE_COLLECTION:
				case TOKEN_HASH_FIELD:
				case TOKEN_HASH_OUTER_CLASS:
				case TOKEN_HASH_STACK_FRAME:
				case TOKEN_HASH_STACK_SLOT:
				case TOKEN_HASH_STATIC:
				case TOKEN_HASH_SUPER:
				case TOKEN_HASH_THIS:
				case TOKEN_HASH_THREAD:
				case TOKEN_HIT_COUNT:
				case TOKEN_IN:
				case TOKEN_INFINITY:
				case TOKEN_INSTRUCTION:
				case TOKEN_INSTRUCTION_TYPE:
				case TOKEN_INVOKE:
				case TOKEN_L:
				case TOKEN_LOCK:
				case TOKEN_METHOD_INVOKE:
				case TOKEN_NAN:
				case TOKEN_NEGATIVE_INFINITY1:
				case TOKEN_NONE:
				case TOKEN_NOTIFY:
				case TOKEN_OBJECT_CREATED:
				case TOKEN_OBJECT_RELEASED:
				case TOKEN_OR:
				case TOKEN_POSITION:
				case TOKEN_POSITIVE_INFINITY1:
				case TOKEN_PROPERTY_VIOLATED:
				case TOKEN_RETURN:
				case TOKEN_SCHEDULING:
				case TOKEN_STACK_FRAME:
				case TOKEN_STATE_ADVANCED:
				case TOKEN_STEP_IN:
				case TOKEN_STEP_OUT:
				case TOKEN_STEP_OVER:
				case TOKEN_SYNC_BLOCK:
				case TOKEN_THREAD:
				case TOKEN_THREAD_SCHEDULED:
				case TOKEN_X:
					{
					alt110=1;
					}
					break;
				case TOKEN_HASH_HEAP:
					{
					alt110=2;
					}
					break;
				case CHAR:
				case DOUBLE_TEXT_LIMITTED:
				case FLOAT_TEXT:
				case HEX:
				case INT:
				case INT_TEXT:
				case LONG_TEXT:
				case STRING:
				case TOKEN_FALSE:
				case TOKEN_NEGATIVE_INFINITY2:
				case TOKEN_NOT_A_NUMBER:
				case TOKEN_NULL:
				case TOKEN_POSITIVE_INFINITY2:
				case TOKEN_TRUE:
					{
					alt110=3;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 110, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case IDF_TEXT_INTERNAL:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_OUTER_CLASS:
			case TOKEN_HASH_STACK_FRAME:
			case TOKEN_HASH_STACK_SLOT:
			case TOKEN_HASH_STATIC:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
			case TOKEN_HASH_THREAD:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_X:
				{
				alt110=1;
				}
				break;
			case TOKEN_HASH_HEAP:
				{
				alt110=2;
				}
				break;
			case CHAR:
			case DOUBLE_TEXT_LIMITTED:
			case FLOAT_TEXT:
			case HEX:
			case INT:
			case INT_TEXT:
			case LONG_TEXT:
			case STRING:
			case TOKEN_FALSE:
			case TOKEN_NEGATIVE_INFINITY2:
			case TOKEN_NOT_A_NUMBER:
			case TOKEN_NULL:
			case TOKEN_POSITIVE_INFINITY2:
			case TOKEN_TRUE:
				{
				alt110=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 110, 0, input);
				throw nvae;
			}
			switch (alt110) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:371:7: ( WS )? cmdStateExpressionThreadValue[$expFactory]
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:371:7: ( WS )?
					int alt106=2;
					int LA106_0 = input.LA(1);
					if ( (LA106_0==WS) ) {
						alt106=1;
					}
					switch (alt106) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:371:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression1Value6019); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionThreadValue_in_cmdStateExpression1Value6022);
					cmdStateExpressionThreadValue30=cmdStateExpressionThreadValue(expFactory);
					state._fsp--;

					 expr = cmdStateExpressionThreadValue30; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:372:7: ( WS )? cmdStateExpressionHeapValue[$expFactory]
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:372:7: ( WS )?
					int alt107=2;
					int LA107_0 = input.LA(1);
					if ( (LA107_0==WS) ) {
						alt107=1;
					}
					switch (alt107) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:372:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression1Value6040); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionHeapValue_in_cmdStateExpression1Value6043);
					cmdStateExpressionHeapValue31=cmdStateExpressionHeapValue(expFactory);
					state._fsp--;

					 expr = cmdStateExpressionHeapValue31; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:373:7: ( WS )? cmdStateConstValue[$expFactory] ( WS )?
					{
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:373:7: ( WS )?
					int alt108=2;
					int LA108_0 = input.LA(1);
					if ( (LA108_0==WS) ) {
						alt108=1;
					}
					switch (alt108) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:373:7: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression1Value6063); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateConstValue_in_cmdStateExpression1Value6066);
					cmdStateConstValue32=cmdStateConstValue(expFactory);
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:373:44: ( WS )?
					int alt109=2;
					int LA109_0 = input.LA(1);
					if ( (LA109_0==WS) ) {
						int LA109_1 = input.LA(2);
						if ( (LA109_1==EOF||(LA109_1 >= RELOP_EQUAL && LA109_1 <= RELOP_NOT_EQUAL)||LA109_1==WS||LA109_1==106) ) {
							alt109=1;
						}
					}
					switch (alt109) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:373:44: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpression1Value6070); 
							}
							break;

					}

					 expr = cmdStateConstValue32; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpression1Value"



	// $ANTLR start "cmdStateExpressionThread"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:376:1: cmdStateExpressionThread[ExpressionFactory expFactory] returns [ExpressionStateThread expr] : ( TOKEN_HASH_THREAD ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame[$expFactory] )? |a= cmdStateExpressionStackFrame[$expFactory] );
	public final ExpressionStateThread cmdStateExpressionThread(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateThread expr = null;


		ExpressionStateStackFrame a =null;
		Integer intValue33 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:5: ( TOKEN_HASH_THREAD ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame[$expFactory] )? |a= cmdStateExpressionStackFrame[$expFactory] )
			int alt118=2;
			int LA118_0 = input.LA(1);
			if ( (LA118_0==TOKEN_HASH_THREAD) ) {
				alt118=1;
			}
			else if ( (LA118_0==EOF||LA118_0==IDF_TEXT_INTERNAL||LA118_0==SIGN_EQUALS||(LA118_0 >= TOKEN_AND && LA118_0 <= TOKEN_ASSERT)||(LA118_0 >= TOKEN_BEGIN && LA118_0 <= TOKEN_F)||(LA118_0 >= TOKEN_FIELD_ACCESS && LA118_0 <= TOKEN_HASH_FIELD)||(LA118_0 >= TOKEN_HASH_OUTER_CLASS && LA118_0 <= TOKEN_HASH_THIS)||(LA118_0 >= TOKEN_HIT_COUNT && LA118_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA118_0 >= TOKEN_NONE && LA118_0 <= TOKEN_NOTIFY)||(LA118_0 >= TOKEN_OBJECT_CREATED && LA118_0 <= TOKEN_OR)||(LA118_0 >= TOKEN_POSITION && LA118_0 <= TOKEN_POSITIVE_INFINITY1)||(LA118_0 >= TOKEN_PROPERTY_VIOLATED && LA118_0 <= TOKEN_SCHEDULING)||(LA118_0 >= TOKEN_STACK_FRAME && LA118_0 <= TOKEN_THREAD_SCHEDULED)||LA118_0==TOKEN_X) ) {
				alt118=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 118, 0, input);
				throw nvae;
			}

			switch (alt118) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:7: TOKEN_HASH_THREAD ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame[$expFactory] )?
					{
					match(input,TOKEN_HASH_THREAD,FOLLOW_TOKEN_HASH_THREAD_in_cmdStateExpressionThread6110); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:25: ( WS )?
					int alt111=2;
					int LA111_0 = input.LA(1);
					if ( (LA111_0==WS) ) {
						alt111=1;
					}
					switch (alt111) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:25: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionThread6112); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:29: ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )?
					int alt115=2;
					int LA115_0 = input.LA(1);
					if ( (LA115_0==109) ) {
						alt115=1;
					}
					switch (alt115) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:30: '[' ( WS )? intValue ( WS )? ']' ( WS )?
							{
							match(input,109,FOLLOW_109_in_cmdStateExpressionThread6116); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:34: ( WS )?
							int alt112=2;
							int LA112_0 = input.LA(1);
							if ( (LA112_0==WS) ) {
								alt112=1;
							}
							switch (alt112) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:34: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionThread6118); 
									}
									break;

							}

							pushFollow(FOLLOW_intValue_in_cmdStateExpressionThread6121);
							intValue33=intValue();
							state._fsp--;

							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:47: ( WS )?
							int alt113=2;
							int LA113_0 = input.LA(1);
							if ( (LA113_0==WS) ) {
								alt113=1;
							}
							switch (alt113) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:47: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionThread6123); 
									}
									break;

							}

							match(input,110,FOLLOW_110_in_cmdStateExpressionThread6126); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:55: ( WS )?
							int alt114=2;
							int LA114_0 = input.LA(1);
							if ( (LA114_0==WS) ) {
								alt114=1;
							}
							switch (alt114) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:55: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionThread6128); 
									}
									break;

							}

							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:61: ( '.' ( WS )? a= cmdStateExpressionStackFrame[$expFactory] )?
					int alt117=2;
					int LA117_0 = input.LA(1);
					if ( (LA117_0==SIGN_DOT) ) {
						alt117=1;
					}
					switch (alt117) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:62: '.' ( WS )? a= cmdStateExpressionStackFrame[$expFactory]
							{
							match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionThread6134); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:66: ( WS )?
							int alt116=2;
							int LA116_0 = input.LA(1);
							if ( (LA116_0==WS) ) {
								alt116=1;
							}
							switch (alt116) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:377:66: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionThread6136); 
									}
									break;

							}

							pushFollow(FOLLOW_cmdStateExpressionStackFrame_in_cmdStateExpressionThread6141);
							a=cmdStateExpressionStackFrame(expFactory);
							state._fsp--;

							}
							break;

					}

					 expr = expFactory.getStateThread(intValue33, a); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:378:70: a= cmdStateExpressionStackFrame[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionStackFrame_in_cmdStateExpressionThread6220);
					a=cmdStateExpressionStackFrame(expFactory);
					state._fsp--;

					 expr = expFactory.getStateThread(null,            a); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionThread"



	// $ANTLR start "cmdStateExpressionThreadValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:381:1: cmdStateExpressionThreadValue[ExpressionFactory expFactory] returns [ExpressionStateThread expr] : ( TOKEN_HASH_THREAD ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrameValue[$expFactory] ) |a= cmdStateExpressionStackFrameValue[$expFactory] );
	public final ExpressionStateThread cmdStateExpressionThreadValue(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateThread expr = null;


		ExpressionStateStackFrame a =null;
		Integer intValue34 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:5: ( TOKEN_HASH_THREAD ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrameValue[$expFactory] ) |a= cmdStateExpressionStackFrameValue[$expFactory] )
			int alt125=2;
			int LA125_0 = input.LA(1);
			if ( (LA125_0==TOKEN_HASH_THREAD) ) {
				alt125=1;
			}
			else if ( (LA125_0==IDF_TEXT_INTERNAL||(LA125_0 >= TOKEN_AND && LA125_0 <= TOKEN_ASSERT)||(LA125_0 >= TOKEN_BEGIN && LA125_0 <= TOKEN_F)||(LA125_0 >= TOKEN_FIELD_ACCESS && LA125_0 <= TOKEN_HASH_FIELD)||(LA125_0 >= TOKEN_HASH_OUTER_CLASS && LA125_0 <= TOKEN_HASH_THIS)||(LA125_0 >= TOKEN_HIT_COUNT && LA125_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA125_0 >= TOKEN_NONE && LA125_0 <= TOKEN_NOTIFY)||(LA125_0 >= TOKEN_OBJECT_CREATED && LA125_0 <= TOKEN_OR)||(LA125_0 >= TOKEN_POSITION && LA125_0 <= TOKEN_POSITIVE_INFINITY1)||(LA125_0 >= TOKEN_PROPERTY_VIOLATED && LA125_0 <= TOKEN_SCHEDULING)||(LA125_0 >= TOKEN_STACK_FRAME && LA125_0 <= TOKEN_THREAD_SCHEDULED)||LA125_0==TOKEN_X) ) {
				alt125=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 125, 0, input);
				throw nvae;
			}

			switch (alt125) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:7: TOKEN_HASH_THREAD ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrameValue[$expFactory] )
					{
					match(input,TOKEN_HASH_THREAD,FOLLOW_TOKEN_HASH_THREAD_in_cmdStateExpressionThreadValue6249); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:25: ( WS )?
					int alt119=2;
					int LA119_0 = input.LA(1);
					if ( (LA119_0==WS) ) {
						alt119=1;
					}
					switch (alt119) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:25: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionThreadValue6251); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:29: ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )?
					int alt123=2;
					int LA123_0 = input.LA(1);
					if ( (LA123_0==109) ) {
						alt123=1;
					}
					switch (alt123) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:30: '[' ( WS )? intValue ( WS )? ']' ( WS )?
							{
							match(input,109,FOLLOW_109_in_cmdStateExpressionThreadValue6255); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:34: ( WS )?
							int alt120=2;
							int LA120_0 = input.LA(1);
							if ( (LA120_0==WS) ) {
								alt120=1;
							}
							switch (alt120) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:34: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionThreadValue6257); 
									}
									break;

							}

							pushFollow(FOLLOW_intValue_in_cmdStateExpressionThreadValue6260);
							intValue34=intValue();
							state._fsp--;

							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:47: ( WS )?
							int alt121=2;
							int LA121_0 = input.LA(1);
							if ( (LA121_0==WS) ) {
								alt121=1;
							}
							switch (alt121) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:47: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionThreadValue6262); 
									}
									break;

							}

							match(input,110,FOLLOW_110_in_cmdStateExpressionThreadValue6265); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:55: ( WS )?
							int alt122=2;
							int LA122_0 = input.LA(1);
							if ( (LA122_0==WS) ) {
								alt122=1;
							}
							switch (alt122) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:55: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionThreadValue6267); 
									}
									break;

							}

							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:61: ( '.' ( WS )? a= cmdStateExpressionStackFrameValue[$expFactory] )
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:62: '.' ( WS )? a= cmdStateExpressionStackFrameValue[$expFactory]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionThreadValue6273); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:66: ( WS )?
					int alt124=2;
					int LA124_0 = input.LA(1);
					if ( (LA124_0==WS) ) {
						alt124=1;
					}
					switch (alt124) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:382:66: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionThreadValue6275); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionStackFrameValue_in_cmdStateExpressionThreadValue6280);
					a=cmdStateExpressionStackFrameValue(expFactory);
					state._fsp--;

					}

					 expr = expFactory.getStateThread(intValue34, a); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:383:70: a= cmdStateExpressionStackFrameValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionStackFrameValue_in_cmdStateExpressionThreadValue6359);
					a=cmdStateExpressionStackFrameValue(expFactory);
					state._fsp--;

					 expr = expFactory.getStateThread(null,            a); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionThreadValue"



	// $ANTLR start "cmdStateExpressionStackFrame"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:386:1: cmdStateExpressionStackFrame[ExpressionFactory expFactory] returns [ExpressionStateStackFrame expr] : ( TOKEN_HASH_STACK_FRAME ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] )? |b= cmdStateExpressionStackFrame1[$expFactory, null] |);
	public final ExpressionStateStackFrame cmdStateExpressionStackFrame(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateStackFrame expr = null;


		ExpressionStateStackFrame a =null;
		ExpressionStateStackFrame b =null;
		Integer intValue35 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:5: ( TOKEN_HASH_STACK_FRAME ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] )? |b= cmdStateExpressionStackFrame1[$expFactory, null] |)
			int alt133=3;
			switch ( input.LA(1) ) {
			case TOKEN_HASH_STACK_FRAME:
				{
				alt133=1;
				}
				break;
			case IDF_TEXT_INTERNAL:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_OUTER_CLASS:
			case TOKEN_HASH_STACK_SLOT:
			case TOKEN_HASH_STATIC:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_X:
				{
				alt133=2;
				}
				break;
			case EOF:
			case SIGN_EQUALS:
				{
				alt133=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 133, 0, input);
				throw nvae;
			}
			switch (alt133) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:7: TOKEN_HASH_STACK_FRAME ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] )?
					{
					match(input,TOKEN_HASH_STACK_FRAME,FOLLOW_TOKEN_HASH_STACK_FRAME_in_cmdStateExpressionStackFrame6388); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:30: ( WS )?
					int alt126=2;
					int LA126_0 = input.LA(1);
					if ( (LA126_0==WS) ) {
						alt126=1;
					}
					switch (alt126) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:30: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrame6390); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:34: ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )?
					int alt130=2;
					int LA130_0 = input.LA(1);
					if ( (LA130_0==109) ) {
						alt130=1;
					}
					switch (alt130) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:35: '[' ( WS )? intValue ( WS )? ']' ( WS )?
							{
							match(input,109,FOLLOW_109_in_cmdStateExpressionStackFrame6394); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:39: ( WS )?
							int alt127=2;
							int LA127_0 = input.LA(1);
							if ( (LA127_0==WS) ) {
								alt127=1;
							}
							switch (alt127) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:39: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrame6396); 
									}
									break;

							}

							pushFollow(FOLLOW_intValue_in_cmdStateExpressionStackFrame6399);
							intValue35=intValue();
							state._fsp--;

							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:52: ( WS )?
							int alt128=2;
							int LA128_0 = input.LA(1);
							if ( (LA128_0==WS) ) {
								alt128=1;
							}
							switch (alt128) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:52: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrame6401); 
									}
									break;

							}

							match(input,110,FOLLOW_110_in_cmdStateExpressionStackFrame6404); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:60: ( WS )?
							int alt129=2;
							int LA129_0 = input.LA(1);
							if ( (LA129_0==WS) ) {
								alt129=1;
							}
							switch (alt129) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:60: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrame6406); 
									}
									break;

							}

							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:66: ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] )?
					int alt132=2;
					int LA132_0 = input.LA(1);
					if ( (LA132_0==SIGN_DOT) ) {
						alt132=1;
					}
					switch (alt132) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:67: '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value]
							{
							match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionStackFrame6412); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:71: ( WS )?
							int alt131=2;
							int LA131_0 = input.LA(1);
							if ( (LA131_0==WS) ) {
								alt131=1;
							}
							switch (alt131) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:387:71: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrame6414); 
									}
									break;

							}

							pushFollow(FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrame6419);
							a=cmdStateExpressionStackFrame1(expFactory, intValue35);
							state._fsp--;

							}
							break;

					}

					 expr = ( a!=null ? a :  expFactory.getStateStackFrame(intValue35, null) ); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:388:75: b= cmdStateExpressionStackFrame1[$expFactory, null]
					{
					pushFollow(FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrame6503);
					b=cmdStateExpressionStackFrame1(expFactory, null);
					state._fsp--;

					 expr = b; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:389:140: 
					{
					 expr = expFactory.getStateStackFrame(null, null); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionStackFrame"



	// $ANTLR start "cmdStateExpressionStackFrameValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:392:1: cmdStateExpressionStackFrameValue[ExpressionFactory expFactory] returns [ExpressionStateStackFrame expr] : ( TOKEN_HASH_STACK_FRAME ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] ) |b= cmdStateExpressionStackFrame1[$expFactory, null] );
	public final ExpressionStateStackFrame cmdStateExpressionStackFrameValue(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateStackFrame expr = null;


		ExpressionStateStackFrame a =null;
		ExpressionStateStackFrame b =null;
		Integer intValue36 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:5: ( TOKEN_HASH_STACK_FRAME ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] ) |b= cmdStateExpressionStackFrame1[$expFactory, null] )
			int alt140=2;
			int LA140_0 = input.LA(1);
			if ( (LA140_0==TOKEN_HASH_STACK_FRAME) ) {
				alt140=1;
			}
			else if ( (LA140_0==IDF_TEXT_INTERNAL||(LA140_0 >= TOKEN_AND && LA140_0 <= TOKEN_ASSERT)||(LA140_0 >= TOKEN_BEGIN && LA140_0 <= TOKEN_F)||(LA140_0 >= TOKEN_FIELD_ACCESS && LA140_0 <= TOKEN_HASH_FIELD)||LA140_0==TOKEN_HASH_OUTER_CLASS||(LA140_0 >= TOKEN_HASH_STACK_SLOT && LA140_0 <= TOKEN_HASH_THIS)||(LA140_0 >= TOKEN_HIT_COUNT && LA140_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA140_0 >= TOKEN_NONE && LA140_0 <= TOKEN_NOTIFY)||(LA140_0 >= TOKEN_OBJECT_CREATED && LA140_0 <= TOKEN_OR)||(LA140_0 >= TOKEN_POSITION && LA140_0 <= TOKEN_POSITIVE_INFINITY1)||(LA140_0 >= TOKEN_PROPERTY_VIOLATED && LA140_0 <= TOKEN_SCHEDULING)||(LA140_0 >= TOKEN_STACK_FRAME && LA140_0 <= TOKEN_THREAD_SCHEDULED)||LA140_0==TOKEN_X) ) {
				alt140=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 140, 0, input);
				throw nvae;
			}

			switch (alt140) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:7: TOKEN_HASH_STACK_FRAME ( WS )? ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )? ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] )
					{
					match(input,TOKEN_HASH_STACK_FRAME,FOLLOW_TOKEN_HASH_STACK_FRAME_in_cmdStateExpressionStackFrameValue6684); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:30: ( WS )?
					int alt134=2;
					int LA134_0 = input.LA(1);
					if ( (LA134_0==WS) ) {
						alt134=1;
					}
					switch (alt134) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:30: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrameValue6686); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:34: ( '[' ( WS )? intValue ( WS )? ']' ( WS )? )?
					int alt138=2;
					int LA138_0 = input.LA(1);
					if ( (LA138_0==109) ) {
						alt138=1;
					}
					switch (alt138) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:35: '[' ( WS )? intValue ( WS )? ']' ( WS )?
							{
							match(input,109,FOLLOW_109_in_cmdStateExpressionStackFrameValue6690); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:39: ( WS )?
							int alt135=2;
							int LA135_0 = input.LA(1);
							if ( (LA135_0==WS) ) {
								alt135=1;
							}
							switch (alt135) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:39: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrameValue6692); 
									}
									break;

							}

							pushFollow(FOLLOW_intValue_in_cmdStateExpressionStackFrameValue6695);
							intValue36=intValue();
							state._fsp--;

							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:52: ( WS )?
							int alt136=2;
							int LA136_0 = input.LA(1);
							if ( (LA136_0==WS) ) {
								alt136=1;
							}
							switch (alt136) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:52: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrameValue6697); 
									}
									break;

							}

							match(input,110,FOLLOW_110_in_cmdStateExpressionStackFrameValue6700); 
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:60: ( WS )?
							int alt137=2;
							int LA137_0 = input.LA(1);
							if ( (LA137_0==WS) ) {
								alt137=1;
							}
							switch (alt137) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:60: WS
									{
									match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrameValue6702); 
									}
									break;

							}

							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:66: ( '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value] )
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:67: '.' ( WS )? a= cmdStateExpressionStackFrame1[$expFactory, $intValue.value]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionStackFrameValue6708); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:71: ( WS )?
					int alt139=2;
					int LA139_0 = input.LA(1);
					if ( (LA139_0==WS) ) {
						alt139=1;
					}
					switch (alt139) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:393:71: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionStackFrameValue6710); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrameValue6715);
					a=cmdStateExpressionStackFrame1(expFactory, intValue36);
					state._fsp--;

					}

					 expr = ( a!=null ? a :  expFactory.getStateStackFrame(intValue36, null) ); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:394:75: b= cmdStateExpressionStackFrame1[$expFactory, null]
					{
					pushFollow(FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrameValue6799);
					b=cmdStateExpressionStackFrame1(expFactory, null);
					state._fsp--;

					 expr = b; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionStackFrameValue"



	// $ANTLR start "cmdStateExpressionStackFrame1"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:397:1: cmdStateExpressionStackFrame1[ExpressionFactory expFactory, Integer sfIndex] returns [ExpressionStateStackFrame expr] : (a= cmdStateExpressionValueStackFrameSlot[$expFactory] |b= cmdStateExpressionValueName[$expFactory] |c= cmdStateExpressionValueAfterStackFrame[$expFactory] |d= cmdStateExpressionValueStatic[$expFactory] );
	public final ExpressionStateStackFrame cmdStateExpressionStackFrame1(ExpressionFactory expFactory, Integer sfIndex) throws RecognitionException {
		ExpressionStateStackFrame expr = null;


		ExpressionStateStackFrameSlot a =null;
		ExpressionStateValueName b =null;
		ExpressionStateValueThis c =null;
		ExpressionStateValueStatic d =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:398:5: (a= cmdStateExpressionValueStackFrameSlot[$expFactory] |b= cmdStateExpressionValueName[$expFactory] |c= cmdStateExpressionValueAfterStackFrame[$expFactory] |d= cmdStateExpressionValueStatic[$expFactory] )
			int alt141=4;
			switch ( input.LA(1) ) {
			case TOKEN_HASH_STACK_SLOT:
				{
				alt141=1;
				}
				break;
			case IDF_TEXT_INTERNAL:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_X:
				{
				alt141=2;
				}
				break;
			case TOKEN_HASH_FIELD:
			case TOKEN_HASH_OUTER_CLASS:
			case TOKEN_HASH_SUPER:
			case TOKEN_HASH_THIS:
				{
				alt141=3;
				}
				break;
			case TOKEN_HASH_STATIC:
				{
				alt141=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 141, 0, input);
				throw nvae;
			}
			switch (alt141) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:398:7: a= cmdStateExpressionValueStackFrameSlot[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueStackFrameSlot_in_cmdStateExpressionStackFrame16841);
					a=cmdStateExpressionValueStackFrameSlot(expFactory);
					state._fsp--;

					 expr = expFactory.getStateStackFrame(sfIndex,   a); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:399:7: b= cmdStateExpressionValueName[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueName_in_cmdStateExpressionStackFrame16859);
					b=cmdStateExpressionValueName(expFactory);
					state._fsp--;

					 expr = expFactory.getStateStackFrameName(sfIndex,   b); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:400:7: c= cmdStateExpressionValueAfterStackFrame[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueAfterStackFrame_in_cmdStateExpressionStackFrame16887);
					c=cmdStateExpressionValueAfterStackFrame(expFactory);
					state._fsp--;

					 expr = expFactory.getStateStackFrameThis(sfIndex,   c); 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:401:7: d= cmdStateExpressionValueStatic[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueStatic_in_cmdStateExpressionStackFrame16904);
					d=cmdStateExpressionValueStatic(expFactory);
					state._fsp--;

					 expr = expFactory.getStateStackFrameStatic(sfIndex, d); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionStackFrame1"



	// $ANTLR start "cmdStateExpressionValueAfterStackFrame"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:405:1: cmdStateExpressionValueAfterStackFrame[ExpressionFactory expFactory] returns [ExpressionStateValueThis expr] : (a= cmdStateExpressionValueFieldIndex[$expFactory] |c= cmdStateExpressionValueOuterClass[$expFactory] |d= cmdStateExpressionValueSuper[$expFactory] |e= cmdStateExpressionValueThis[$expFactory] );
	public final ExpressionStateValueThis cmdStateExpressionValueAfterStackFrame(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValueThis expr = null;


		ExpressionStateValueFieldIndex a =null;
		ExpressionStateValueOuterClass c =null;
		ExpressionStateValueSuper d =null;
		ExpressionStateValueThis e =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:406:5: (a= cmdStateExpressionValueFieldIndex[$expFactory] |c= cmdStateExpressionValueOuterClass[$expFactory] |d= cmdStateExpressionValueSuper[$expFactory] |e= cmdStateExpressionValueThis[$expFactory] )
			int alt142=4;
			switch ( input.LA(1) ) {
			case TOKEN_HASH_FIELD:
				{
				alt142=1;
				}
				break;
			case TOKEN_HASH_OUTER_CLASS:
				{
				alt142=2;
				}
				break;
			case TOKEN_HASH_SUPER:
				{
				alt142=3;
				}
				break;
			case TOKEN_HASH_THIS:
				{
				alt142=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 142, 0, input);
				throw nvae;
			}
			switch (alt142) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:406:7: a= cmdStateExpressionValueFieldIndex[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueFieldIndex_in_cmdStateExpressionValueAfterStackFrame6946);
					a=cmdStateExpressionValueFieldIndex(expFactory);
					state._fsp--;

					 expr = expFactory.getStateValueThis(a); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:407:7: c= cmdStateExpressionValueOuterClass[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueOuterClass_in_cmdStateExpressionValueAfterStackFrame6982);
					c=cmdStateExpressionValueOuterClass(expFactory);
					state._fsp--;

					 expr = expFactory.getStateValueThis(c); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:408:7: d= cmdStateExpressionValueSuper[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueSuper_in_cmdStateExpressionValueAfterStackFrame7018);
					d=cmdStateExpressionValueSuper(expFactory);
					state._fsp--;

					 expr = expFactory.getStateValueThis(d); 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:409:7: e= cmdStateExpressionValueThis[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueThis_in_cmdStateExpressionValueAfterStackFrame7059);
					e=cmdStateExpressionValueThis(expFactory);
					state._fsp--;

					 expr = e; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueAfterStackFrame"



	// $ANTLR start "cmdStateExpressionValueFieldIndex"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:413:1: cmdStateExpressionValueFieldIndex[ExpressionFactory expFactory] returns [ExpressionStateValueFieldIndex expr] : TOKEN_HASH_FIELD ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateValueFieldIndex cmdStateExpressionValueFieldIndex(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValueFieldIndex expr = null;


		ExpressionStateValue a =null;
		Integer intValue37 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:5: ( TOKEN_HASH_FIELD ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:7: TOKEN_HASH_FIELD ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			match(input,TOKEN_HASH_FIELD,FOLLOW_TOKEN_HASH_FIELD_in_cmdStateExpressionValueFieldIndex7115); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:24: ( WS )?
			int alt143=2;
			int LA143_0 = input.LA(1);
			if ( (LA143_0==WS) ) {
				alt143=1;
			}
			switch (alt143) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:24: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7117); 
					}
					break;

			}

			match(input,109,FOLLOW_109_in_cmdStateExpressionValueFieldIndex7120); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:32: ( WS )?
			int alt144=2;
			int LA144_0 = input.LA(1);
			if ( (LA144_0==WS) ) {
				alt144=1;
			}
			switch (alt144) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:32: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7122); 
					}
					break;

			}

			pushFollow(FOLLOW_intValue_in_cmdStateExpressionValueFieldIndex7125);
			intValue37=intValue();
			state._fsp--;

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:45: ( WS )?
			int alt145=2;
			int LA145_0 = input.LA(1);
			if ( (LA145_0==WS) ) {
				alt145=1;
			}
			switch (alt145) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:45: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7127); 
					}
					break;

			}

			match(input,110,FOLLOW_110_in_cmdStateExpressionValueFieldIndex7130); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:53: ( WS )?
			int alt146=2;
			int LA146_0 = input.LA(1);
			if ( (LA146_0==WS) ) {
				int LA146_1 = input.LA(2);
				if ( (LA146_1==EOF||(LA146_1 >= RELOP_EQUAL && LA146_1 <= RELOP_NOT_EQUAL)||LA146_1==SIGN_DOT||LA146_1==SIGN_EQUALS||LA146_1==WS||LA146_1==106||LA146_1==109) ) {
					alt146=1;
				}
			}
			switch (alt146) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:53: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7132); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:58: (a= cmdStateExpressionValue[$expFactory] )?
			int alt147=2;
			int LA147_0 = input.LA(1);
			if ( (LA147_0==SIGN_DOT||LA147_0==109) ) {
				alt147=1;
			}
			switch (alt147) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:414:58: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueFieldIndex7137);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateValueFieldIndex(intValue37, a); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueFieldIndex"



	// $ANTLR start "cmdStateExpressionValueName"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:417:1: cmdStateExpressionValueName[ExpressionFactory expFactory] returns [ExpressionStateValueName expr] : idf_fieldName ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateValueName cmdStateExpressionValueName(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValueName expr = null;


		ExpressionStateValue a =null;
		ParserRuleReturnScope idf_fieldName38 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:418:5: ( idf_fieldName ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:418:7: idf_fieldName ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			pushFollow(FOLLOW_idf_fieldName_in_cmdStateExpressionValueName7171);
			idf_fieldName38=idf_fieldName();
			state._fsp--;

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:418:21: ( WS )?
			int alt148=2;
			int LA148_0 = input.LA(1);
			if ( (LA148_0==WS) ) {
				int LA148_1 = input.LA(2);
				if ( (LA148_1==EOF||(LA148_1 >= RELOP_EQUAL && LA148_1 <= RELOP_NOT_EQUAL)||LA148_1==SIGN_DOT||LA148_1==SIGN_EQUALS||LA148_1==WS||LA148_1==106||LA148_1==109) ) {
					alt148=1;
				}
			}
			switch (alt148) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:418:21: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueName7173); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:418:26: (a= cmdStateExpressionValue[$expFactory] )?
			int alt149=2;
			int LA149_0 = input.LA(1);
			if ( (LA149_0==SIGN_DOT||LA149_0==109) ) {
				alt149=1;
			}
			switch (alt149) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:418:26: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueName7178);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateValueName((idf_fieldName38!=null?input.toString(idf_fieldName38.start,idf_fieldName38.stop):null), a); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueName"



	// $ANTLR start "cmdStateExpressionValueOuterClass"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:421:1: cmdStateExpressionValueOuterClass[ExpressionFactory expFactory] returns [ExpressionStateValueOuterClass expr] : TOKEN_HASH_OUTER_CLASS ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateValueOuterClass cmdStateExpressionValueOuterClass(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValueOuterClass expr = null;


		ExpressionStateValue a =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:422:5: ( TOKEN_HASH_OUTER_CLASS ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:422:7: TOKEN_HASH_OUTER_CLASS ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			match(input,TOKEN_HASH_OUTER_CLASS,FOLLOW_TOKEN_HASH_OUTER_CLASS_in_cmdStateExpressionValueOuterClass7254); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:422:30: ( WS )?
			int alt150=2;
			int LA150_0 = input.LA(1);
			if ( (LA150_0==WS) ) {
				int LA150_1 = input.LA(2);
				if ( (LA150_1==EOF||(LA150_1 >= RELOP_EQUAL && LA150_1 <= RELOP_NOT_EQUAL)||LA150_1==SIGN_DOT||LA150_1==SIGN_EQUALS||LA150_1==WS||LA150_1==106||LA150_1==109) ) {
					alt150=1;
				}
			}
			switch (alt150) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:422:30: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueOuterClass7256); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:422:35: (a= cmdStateExpressionValue[$expFactory] )?
			int alt151=2;
			int LA151_0 = input.LA(1);
			if ( (LA151_0==SIGN_DOT||LA151_0==109) ) {
				alt151=1;
			}
			switch (alt151) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:422:35: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueOuterClass7261);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateValueOuterClass(a); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueOuterClass"



	// $ANTLR start "cmdStateExpressionValueStatic"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:425:1: cmdStateExpressionValueStatic[ExpressionFactory expFactory] returns [ExpressionStateValueStatic expr] : ( TOKEN_HASH_STATIC ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? | TOKEN_HASH_STATIC ( WS )? (a= cmdStateExpressionClass[$expFactory] )? );
	public final ExpressionStateValueStatic cmdStateExpressionValueStatic(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValueStatic expr = null;


		ExpressionStateValue a =null;
		Integer intValue39 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:5: ( TOKEN_HASH_STATIC ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? | TOKEN_HASH_STATIC ( WS )? (a= cmdStateExpressionClass[$expFactory] )? )
			int alt159=2;
			int LA159_0 = input.LA(1);
			if ( (LA159_0==TOKEN_HASH_STATIC) ) {
				switch ( input.LA(2) ) {
				case WS:
					{
					int LA159_2 = input.LA(3);
					if ( (LA159_2==EOF||(LA159_2 >= RELOP_EQUAL && LA159_2 <= RELOP_NOT_EQUAL)||LA159_2==SIGN_DOT||LA159_2==SIGN_EQUALS||LA159_2==TOKEN_AND||LA159_2==TOKEN_OR||LA159_2==WS||LA159_2==106) ) {
						alt159=2;
					}
					else if ( (LA159_2==109) ) {
						alt159=1;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 159, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 109:
					{
					alt159=1;
					}
					break;
				case EOF:
				case RELOP_EQUAL:
				case RELOP_GREATER_OR_EQUAL_THAN:
				case RELOP_GREATER_THAN:
				case RELOP_LESS_OR_EQUAL_THAN:
				case RELOP_LESS_THAN:
				case RELOP_NOT_EQUAL:
				case SIGN_DOT:
				case SIGN_EQUALS:
				case 106:
					{
					alt159=2;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 159, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 159, 0, input);
				throw nvae;
			}

			switch (alt159) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:7: TOKEN_HASH_STATIC ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
					{
					match(input,TOKEN_HASH_STATIC,FOLLOW_TOKEN_HASH_STATIC_in_cmdStateExpressionValueStatic7318); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:25: ( WS )?
					int alt152=2;
					int LA152_0 = input.LA(1);
					if ( (LA152_0==WS) ) {
						alt152=1;
					}
					switch (alt152) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:25: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStatic7320); 
							}
							break;

					}

					match(input,109,FOLLOW_109_in_cmdStateExpressionValueStatic7323); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:33: ( WS )?
					int alt153=2;
					int LA153_0 = input.LA(1);
					if ( (LA153_0==WS) ) {
						alt153=1;
					}
					switch (alt153) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:33: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStatic7325); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdStateExpressionValueStatic7328);
					intValue39=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:46: ( WS )?
					int alt154=2;
					int LA154_0 = input.LA(1);
					if ( (LA154_0==WS) ) {
						alt154=1;
					}
					switch (alt154) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:46: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStatic7330); 
							}
							break;

					}

					match(input,110,FOLLOW_110_in_cmdStateExpressionValueStatic7333); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:54: ( WS )?
					int alt155=2;
					int LA155_0 = input.LA(1);
					if ( (LA155_0==WS) ) {
						int LA155_1 = input.LA(2);
						if ( (LA155_1==EOF||(LA155_1 >= RELOP_EQUAL && LA155_1 <= RELOP_NOT_EQUAL)||LA155_1==SIGN_DOT||LA155_1==SIGN_EQUALS||LA155_1==WS||LA155_1==106||LA155_1==109) ) {
							alt155=1;
						}
					}
					switch (alt155) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:54: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStatic7335); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:59: (a= cmdStateExpressionValue[$expFactory] )?
					int alt156=2;
					int LA156_0 = input.LA(1);
					if ( (LA156_0==SIGN_DOT||LA156_0==109) ) {
						alt156=1;
					}
					switch (alt156) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:426:59: a= cmdStateExpressionValue[$expFactory]
							{
							pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueStatic7340);
							a=cmdStateExpressionValue(expFactory);
							state._fsp--;

							}
							break;

					}

					 expr = expFactory.getStateValueStaticArea(intValue39,  a); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:427:7: TOKEN_HASH_STATIC ( WS )? (a= cmdStateExpressionClass[$expFactory] )?
					{
					match(input,TOKEN_HASH_STATIC,FOLLOW_TOKEN_HASH_STATIC_in_cmdStateExpressionValueStatic7355); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:427:54: ( WS )?
					int alt157=2;
					int LA157_0 = input.LA(1);
					if ( (LA157_0==WS) ) {
						int LA157_1 = input.LA(2);
						if ( (LA157_1==EOF||(LA157_1 >= RELOP_EQUAL && LA157_1 <= RELOP_NOT_EQUAL)||LA157_1==SIGN_DOT||LA157_1==SIGN_EQUALS||LA157_1==WS||LA157_1==106) ) {
							alt157=1;
						}
					}
					switch (alt157) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:427:54: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStatic7386); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:427:59: (a= cmdStateExpressionClass[$expFactory] )?
					int alt158=2;
					int LA158_0 = input.LA(1);
					if ( (LA158_0==SIGN_DOT) ) {
						alt158=1;
					}
					switch (alt158) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:427:59: a= cmdStateExpressionClass[$expFactory]
							{
							pushFollow(FOLLOW_cmdStateExpressionClass_in_cmdStateExpressionValueStatic7391);
							a=cmdStateExpressionClass(expFactory);
							state._fsp--;

							}
							break;

					}

					 expr = expFactory.getStateValueStaticArea(null,             a); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueStatic"



	// $ANTLR start "cmdStateExpressionValueStackFrameSlot"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:430:1: cmdStateExpressionValueStackFrameSlot[ExpressionFactory expFactory] returns [ExpressionStateStackFrameSlot expr] : TOKEN_HASH_STACK_SLOT ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateStackFrameSlot cmdStateExpressionValueStackFrameSlot(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateStackFrameSlot expr = null;


		ExpressionStateValue a =null;
		Integer intValue40 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:5: ( TOKEN_HASH_STACK_SLOT ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:7: TOKEN_HASH_STACK_SLOT ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			match(input,TOKEN_HASH_STACK_SLOT,FOLLOW_TOKEN_HASH_STACK_SLOT_in_cmdStateExpressionValueStackFrameSlot7425); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:29: ( WS )?
			int alt160=2;
			int LA160_0 = input.LA(1);
			if ( (LA160_0==WS) ) {
				alt160=1;
			}
			switch (alt160) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:29: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7427); 
					}
					break;

			}

			match(input,109,FOLLOW_109_in_cmdStateExpressionValueStackFrameSlot7430); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:37: ( WS )?
			int alt161=2;
			int LA161_0 = input.LA(1);
			if ( (LA161_0==WS) ) {
				alt161=1;
			}
			switch (alt161) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:37: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7432); 
					}
					break;

			}

			pushFollow(FOLLOW_intValue_in_cmdStateExpressionValueStackFrameSlot7435);
			intValue40=intValue();
			state._fsp--;

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:50: ( WS )?
			int alt162=2;
			int LA162_0 = input.LA(1);
			if ( (LA162_0==WS) ) {
				alt162=1;
			}
			switch (alt162) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:50: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7437); 
					}
					break;

			}

			match(input,110,FOLLOW_110_in_cmdStateExpressionValueStackFrameSlot7440); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:58: ( WS )?
			int alt163=2;
			int LA163_0 = input.LA(1);
			if ( (LA163_0==WS) ) {
				int LA163_1 = input.LA(2);
				if ( (LA163_1==EOF||(LA163_1 >= RELOP_EQUAL && LA163_1 <= RELOP_NOT_EQUAL)||LA163_1==SIGN_DOT||LA163_1==SIGN_EQUALS||LA163_1==WS||LA163_1==106||LA163_1==109) ) {
					alt163=1;
				}
			}
			switch (alt163) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:58: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7442); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:64: (a= cmdStateExpressionValue[$expFactory] )?
			int alt164=2;
			int LA164_0 = input.LA(1);
			if ( (LA164_0==SIGN_DOT||LA164_0==109) ) {
				alt164=1;
			}
			switch (alt164) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:431:64: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueStackFrameSlot7448);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateValueStackFrameSlot(intValue40, a); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueStackFrameSlot"



	// $ANTLR start "cmdStateExpressionValueSuper"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:434:1: cmdStateExpressionValueSuper[ExpressionFactory expFactory] returns [ExpressionStateValueSuper expr] : TOKEN_HASH_SUPER ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateValueSuper cmdStateExpressionValueSuper(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValueSuper expr = null;


		ExpressionStateValue a =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:435:5: ( TOKEN_HASH_SUPER ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:435:7: TOKEN_HASH_SUPER ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			match(input,TOKEN_HASH_SUPER,FOLLOW_TOKEN_HASH_SUPER_in_cmdStateExpressionValueSuper7476); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:435:24: ( WS )?
			int alt165=2;
			int LA165_0 = input.LA(1);
			if ( (LA165_0==WS) ) {
				int LA165_1 = input.LA(2);
				if ( (LA165_1==EOF||(LA165_1 >= RELOP_EQUAL && LA165_1 <= RELOP_NOT_EQUAL)||LA165_1==SIGN_DOT||LA165_1==SIGN_EQUALS||LA165_1==WS||LA165_1==106||LA165_1==109) ) {
					alt165=1;
				}
			}
			switch (alt165) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:435:24: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueSuper7478); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:435:30: (a= cmdStateExpressionValue[$expFactory] )?
			int alt166=2;
			int LA166_0 = input.LA(1);
			if ( (LA166_0==SIGN_DOT||LA166_0==109) ) {
				alt166=1;
			}
			switch (alt166) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:435:30: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueSuper7484);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateValueSuper(a); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueSuper"



	// $ANTLR start "cmdStateExpressionValueThis"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:438:1: cmdStateExpressionValueThis[ExpressionFactory expFactory] returns [ExpressionStateValueThis expr] : TOKEN_HASH_THIS ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateValueThis cmdStateExpressionValueThis(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValueThis expr = null;


		ExpressionStateValue a =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:439:5: ( TOKEN_HASH_THIS ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:439:7: TOKEN_HASH_THIS ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			match(input,TOKEN_HASH_THIS,FOLLOW_TOKEN_HASH_THIS_in_cmdStateExpressionValueThis7512); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:439:23: ( WS )?
			int alt167=2;
			int LA167_0 = input.LA(1);
			if ( (LA167_0==WS) ) {
				int LA167_1 = input.LA(2);
				if ( (LA167_1==EOF||(LA167_1 >= RELOP_EQUAL && LA167_1 <= RELOP_NOT_EQUAL)||LA167_1==SIGN_DOT||LA167_1==SIGN_EQUALS||LA167_1==WS||LA167_1==106||LA167_1==109) ) {
					alt167=1;
				}
			}
			switch (alt167) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:439:23: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueThis7514); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:439:28: (a= cmdStateExpressionValue[$expFactory] )?
			int alt168=2;
			int LA168_0 = input.LA(1);
			if ( (LA168_0==SIGN_DOT||LA168_0==109) ) {
				alt168=1;
			}
			switch (alt168) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:439:28: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueThis7519);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateValueThis(a); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueThis"



	// $ANTLR start "cmdStateExpressionValueArray"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:441:1: cmdStateExpressionValueArray[ExpressionFactory expFactory] returns [ExpressionStateValue expr] : '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateValue cmdStateExpressionValueArray(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValue expr = null;


		ExpressionStateValue a =null;
		Integer intValue41 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:5: ( '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:7: '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			match(input,109,FOLLOW_109_in_cmdStateExpressionValueArray7547); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:11: ( WS )?
			int alt169=2;
			int LA169_0 = input.LA(1);
			if ( (LA169_0==WS) ) {
				alt169=1;
			}
			switch (alt169) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:11: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueArray7549); 
					}
					break;

			}

			pushFollow(FOLLOW_intValue_in_cmdStateExpressionValueArray7552);
			intValue41=intValue();
			state._fsp--;

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:24: ( WS )?
			int alt170=2;
			int LA170_0 = input.LA(1);
			if ( (LA170_0==WS) ) {
				alt170=1;
			}
			switch (alt170) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:24: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueArray7554); 
					}
					break;

			}

			match(input,110,FOLLOW_110_in_cmdStateExpressionValueArray7557); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:32: ( WS )?
			int alt171=2;
			int LA171_0 = input.LA(1);
			if ( (LA171_0==WS) ) {
				int LA171_1 = input.LA(2);
				if ( (LA171_1==EOF||(LA171_1 >= RELOP_EQUAL && LA171_1 <= RELOP_NOT_EQUAL)||LA171_1==SIGN_DOT||LA171_1==SIGN_EQUALS||LA171_1==WS||LA171_1==106||LA171_1==109) ) {
					alt171=1;
				}
			}
			switch (alt171) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:32: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionValueArray7559); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:37: (a= cmdStateExpressionValue[$expFactory] )?
			int alt172=2;
			int LA172_0 = input.LA(1);
			if ( (LA172_0==SIGN_DOT||LA172_0==109) ) {
				alt172=1;
			}
			switch (alt172) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:442:37: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueArray7564);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateValueArrayIndex(a, intValue41); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValueArray"



	// $ANTLR start "cmdStateExpressionValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:445:1: cmdStateExpressionValue[ExpressionFactory expFactory] returns [ExpressionStateValue expr] : (a= cmdStateExpressionClass[$expFactory] |b= cmdStateExpressionValueArray[$expFactory] );
	public final ExpressionStateValue cmdStateExpressionValue(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValue expr = null;


		ExpressionStateValue a =null;
		ExpressionStateValue b =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:446:5: (a= cmdStateExpressionClass[$expFactory] |b= cmdStateExpressionValueArray[$expFactory] )
			int alt173=2;
			int LA173_0 = input.LA(1);
			if ( (LA173_0==SIGN_DOT) ) {
				alt173=1;
			}
			else if ( (LA173_0==109) ) {
				alt173=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 173, 0, input);
				throw nvae;
			}

			switch (alt173) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:446:7: a= cmdStateExpressionClass[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionClass_in_cmdStateExpressionValue7614);
					a=cmdStateExpressionClass(expFactory);
					state._fsp--;

					 expr = a; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:447:7: b= cmdStateExpressionValueArray[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValueArray_in_cmdStateExpressionValue7633);
					b=cmdStateExpressionValueArray(expFactory);
					state._fsp--;

					 expr = b; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionValue"



	// $ANTLR start "cmdStateExpressionClass"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:451:1: cmdStateExpressionClass[ExpressionFactory expFactory] returns [ExpressionStateValue expr] : ( '.' ( WS )? a= cmdStateExpressionValueFieldIndex[$expFactory] | '.' ( WS )? b= cmdStateExpressionValueName[$expFactory] | '.' ( WS )? c= cmdStateExpressionValueOuterClass[$expFactory] | '.' ( WS )? d= cmdStateExpressionValueSuper[$expFactory] | '.' ( WS )? e= cmdStateExpressionValueThis[$expFactory] | '.' ( WS )? f= cmdStateExpressionValueStatic[$expFactory] );
	public final ExpressionStateValue cmdStateExpressionClass(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateValue expr = null;


		ExpressionStateValueFieldIndex a =null;
		ExpressionStateValueName b =null;
		ExpressionStateValueOuterClass c =null;
		ExpressionStateValueSuper d =null;
		ExpressionStateValueThis e =null;
		ExpressionStateValueStatic f =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:452:5: ( '.' ( WS )? a= cmdStateExpressionValueFieldIndex[$expFactory] | '.' ( WS )? b= cmdStateExpressionValueName[$expFactory] | '.' ( WS )? c= cmdStateExpressionValueOuterClass[$expFactory] | '.' ( WS )? d= cmdStateExpressionValueSuper[$expFactory] | '.' ( WS )? e= cmdStateExpressionValueThis[$expFactory] | '.' ( WS )? f= cmdStateExpressionValueStatic[$expFactory] )
			int alt180=6;
			int LA180_0 = input.LA(1);
			if ( (LA180_0==SIGN_DOT) ) {
				switch ( input.LA(2) ) {
				case WS:
					{
					switch ( input.LA(3) ) {
					case TOKEN_HASH_FIELD:
						{
						alt180=1;
						}
						break;
					case IDF_TEXT_INTERNAL:
					case TOKEN_AND:
					case TOKEN_ANY:
					case TOKEN_ARRAY:
					case TOKEN_ASSERT:
					case TOKEN_BEGIN:
					case TOKEN_BOTH:
					case TOKEN_CHOICE_GENERATOR:
					case TOKEN_CONDITION:
					case TOKEN_D:
					case TOKEN_DATA:
					case TOKEN_E:
					case TOKEN_END:
					case TOKEN_EXCEPTION_THROWN:
					case TOKEN_F:
					case TOKEN_FIELD_ACCESS:
					case TOKEN_FIELD_READ:
					case TOKEN_FIELD_WRITE:
					case TOKEN_GARBAGE_COLLECTION:
					case TOKEN_HIT_COUNT:
					case TOKEN_IN:
					case TOKEN_INFINITY:
					case TOKEN_INSTRUCTION:
					case TOKEN_INSTRUCTION_TYPE:
					case TOKEN_INVOKE:
					case TOKEN_L:
					case TOKEN_LOCK:
					case TOKEN_METHOD_INVOKE:
					case TOKEN_NAN:
					case TOKEN_NEGATIVE_INFINITY1:
					case TOKEN_NONE:
					case TOKEN_NOTIFY:
					case TOKEN_OBJECT_CREATED:
					case TOKEN_OBJECT_RELEASED:
					case TOKEN_OR:
					case TOKEN_POSITION:
					case TOKEN_POSITIVE_INFINITY1:
					case TOKEN_PROPERTY_VIOLATED:
					case TOKEN_RETURN:
					case TOKEN_SCHEDULING:
					case TOKEN_STACK_FRAME:
					case TOKEN_STATE_ADVANCED:
					case TOKEN_STEP_IN:
					case TOKEN_STEP_OUT:
					case TOKEN_STEP_OVER:
					case TOKEN_SYNC_BLOCK:
					case TOKEN_THREAD:
					case TOKEN_THREAD_SCHEDULED:
					case TOKEN_X:
						{
						alt180=2;
						}
						break;
					case TOKEN_HASH_OUTER_CLASS:
						{
						alt180=3;
						}
						break;
					case TOKEN_HASH_SUPER:
						{
						alt180=4;
						}
						break;
					case TOKEN_HASH_THIS:
						{
						alt180=5;
						}
						break;
					case TOKEN_HASH_STATIC:
						{
						alt180=6;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 180, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case TOKEN_HASH_FIELD:
					{
					alt180=1;
					}
					break;
				case IDF_TEXT_INTERNAL:
				case TOKEN_AND:
				case TOKEN_ANY:
				case TOKEN_ARRAY:
				case TOKEN_ASSERT:
				case TOKEN_BEGIN:
				case TOKEN_BOTH:
				case TOKEN_CHOICE_GENERATOR:
				case TOKEN_CONDITION:
				case TOKEN_D:
				case TOKEN_DATA:
				case TOKEN_E:
				case TOKEN_END:
				case TOKEN_EXCEPTION_THROWN:
				case TOKEN_F:
				case TOKEN_FIELD_ACCESS:
				case TOKEN_FIELD_READ:
				case TOKEN_FIELD_WRITE:
				case TOKEN_GARBAGE_COLLECTION:
				case TOKEN_HIT_COUNT:
				case TOKEN_IN:
				case TOKEN_INFINITY:
				case TOKEN_INSTRUCTION:
				case TOKEN_INSTRUCTION_TYPE:
				case TOKEN_INVOKE:
				case TOKEN_L:
				case TOKEN_LOCK:
				case TOKEN_METHOD_INVOKE:
				case TOKEN_NAN:
				case TOKEN_NEGATIVE_INFINITY1:
				case TOKEN_NONE:
				case TOKEN_NOTIFY:
				case TOKEN_OBJECT_CREATED:
				case TOKEN_OBJECT_RELEASED:
				case TOKEN_OR:
				case TOKEN_POSITION:
				case TOKEN_POSITIVE_INFINITY1:
				case TOKEN_PROPERTY_VIOLATED:
				case TOKEN_RETURN:
				case TOKEN_SCHEDULING:
				case TOKEN_STACK_FRAME:
				case TOKEN_STATE_ADVANCED:
				case TOKEN_STEP_IN:
				case TOKEN_STEP_OUT:
				case TOKEN_STEP_OVER:
				case TOKEN_SYNC_BLOCK:
				case TOKEN_THREAD:
				case TOKEN_THREAD_SCHEDULED:
				case TOKEN_X:
					{
					alt180=2;
					}
					break;
				case TOKEN_HASH_OUTER_CLASS:
					{
					alt180=3;
					}
					break;
				case TOKEN_HASH_SUPER:
					{
					alt180=4;
					}
					break;
				case TOKEN_HASH_THIS:
					{
					alt180=5;
					}
					break;
				case TOKEN_HASH_STATIC:
					{
					alt180=6;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 180, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 180, 0, input);
				throw nvae;
			}

			switch (alt180) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:452:7: '.' ( WS )? a= cmdStateExpressionValueFieldIndex[$expFactory]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7661); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:452:11: ( WS )?
					int alt174=2;
					int LA174_0 = input.LA(1);
					if ( (LA174_0==WS) ) {
						alt174=1;
					}
					switch (alt174) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:452:11: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionClass7663); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionValueFieldIndex_in_cmdStateExpressionClass7668);
					a=cmdStateExpressionValueFieldIndex(expFactory);
					state._fsp--;

					 expr = a; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:453:7: '.' ( WS )? b= cmdStateExpressionValueName[$expFactory]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7702); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:453:11: ( WS )?
					int alt175=2;
					int LA175_0 = input.LA(1);
					if ( (LA175_0==WS) ) {
						alt175=1;
					}
					switch (alt175) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:453:11: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionClass7704); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionValueName_in_cmdStateExpressionClass7709);
					b=cmdStateExpressionValueName(expFactory);
					state._fsp--;

					 expr = b; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:454:7: '.' ( WS )? c= cmdStateExpressionValueOuterClass[$expFactory]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7749); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:454:11: ( WS )?
					int alt176=2;
					int LA176_0 = input.LA(1);
					if ( (LA176_0==WS) ) {
						alt176=1;
					}
					switch (alt176) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:454:11: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionClass7751); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionValueOuterClass_in_cmdStateExpressionClass7756);
					c=cmdStateExpressionValueOuterClass(expFactory);
					state._fsp--;

					 expr = c; 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:455:7: '.' ( WS )? d= cmdStateExpressionValueSuper[$expFactory]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7790); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:455:11: ( WS )?
					int alt177=2;
					int LA177_0 = input.LA(1);
					if ( (LA177_0==WS) ) {
						alt177=1;
					}
					switch (alt177) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:455:11: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionClass7792); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionValueSuper_in_cmdStateExpressionClass7797);
					d=cmdStateExpressionValueSuper(expFactory);
					state._fsp--;

					 expr = d; 
					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:456:7: '.' ( WS )? e= cmdStateExpressionValueThis[$expFactory]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7836); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:456:11: ( WS )?
					int alt178=2;
					int LA178_0 = input.LA(1);
					if ( (LA178_0==WS) ) {
						alt178=1;
					}
					switch (alt178) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:456:11: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionClass7838); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionValueThis_in_cmdStateExpressionClass7843);
					e=cmdStateExpressionValueThis(expFactory);
					state._fsp--;

					 expr = e; 
					}
					break;
				case 6 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:457:7: '.' ( WS )? f= cmdStateExpressionValueStatic[$expFactory]
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7883); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:457:11: ( WS )?
					int alt179=2;
					int LA179_0 = input.LA(1);
					if ( (LA179_0==WS) ) {
						alt179=1;
					}
					switch (alt179) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:457:11: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionClass7885); 
							}
							break;

					}

					pushFollow(FOLLOW_cmdStateExpressionValueStatic_in_cmdStateExpressionClass7890);
					f=cmdStateExpressionValueStatic(expFactory);
					state._fsp--;

					 expr = f; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionClass"



	// $ANTLR start "cmdStateExpressionHeap"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:460:1: cmdStateExpressionHeap[ExpressionFactory expFactory] returns [ExpressionStateRootNode<ExpressionStateValue> expr] : ( TOKEN_HASH_HEAP ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? | TOKEN_HASH_HEAP ( WS )? '[' ( WS )? className ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? | TOKEN_HASH_STATIC ( WS )? '[' ( WS )? className ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? );
	public final ExpressionStateRootNode<ExpressionStateValue> cmdStateExpressionHeap(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateRootNode<ExpressionStateValue> expr = null;


		ExpressionStateValue a =null;
		Integer intValue42 =null;
		ClassName className43 =null;
		ClassName className44 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:5: ( TOKEN_HASH_HEAP ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? | TOKEN_HASH_HEAP ( WS )? '[' ( WS )? className ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? | TOKEN_HASH_STATIC ( WS )? '[' ( WS )? className ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			int alt196=3;
			int LA196_0 = input.LA(1);
			if ( (LA196_0==TOKEN_HASH_HEAP) ) {
				int LA196_1 = input.LA(2);
				if ( (LA196_1==WS) ) {
					int LA196_3 = input.LA(3);
					if ( (LA196_3==109) ) {
						switch ( input.LA(4) ) {
						case WS:
							{
							int LA196_5 = input.LA(5);
							if ( (LA196_5==HEX||(LA196_5 >= INT && LA196_5 <= INT_TEXT)) ) {
								alt196=1;
							}
							else if ( (LA196_5==IDF_TEXT_INTERNAL||LA196_5==SIGN_ASTERISK||(LA196_5 >= SIGN_DOLAR && LA196_5 <= SIGN_DOT)||(LA196_5 >= TOKEN_AND && LA196_5 <= TOKEN_ASSERT)||(LA196_5 >= TOKEN_BEGIN && LA196_5 <= TOKEN_GARBAGE_COLLECTION)||(LA196_5 >= TOKEN_HIT_COUNT && LA196_5 <= TOKEN_NEGATIVE_INFINITY1)||(LA196_5 >= TOKEN_NONE && LA196_5 <= TOKEN_NOTIFY)||(LA196_5 >= TOKEN_NULL && LA196_5 <= TOKEN_OR)||(LA196_5 >= TOKEN_POSITION && LA196_5 <= TOKEN_POSITIVE_INFINITY1)||(LA196_5 >= TOKEN_PROPERTY_VIOLATED && LA196_5 <= TOKEN_SCHEDULING)||(LA196_5 >= TOKEN_STACK_FRAME && LA196_5 <= TOKEN_X)) ) {
								alt196=2;
							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 196, 5, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

							}
							break;
						case HEX:
						case INT:
						case INT_TEXT:
							{
							alt196=1;
							}
							break;
						case IDF_TEXT_INTERNAL:
						case SIGN_ASTERISK:
						case SIGN_DOLAR:
						case SIGN_DOT:
						case TOKEN_AND:
						case TOKEN_ANY:
						case TOKEN_ARRAY:
						case TOKEN_ASSERT:
						case TOKEN_BEGIN:
						case TOKEN_BOTH:
						case TOKEN_CHOICE_GENERATOR:
						case TOKEN_CONDITION:
						case TOKEN_D:
						case TOKEN_DATA:
						case TOKEN_E:
						case TOKEN_END:
						case TOKEN_EXCEPTION_THROWN:
						case TOKEN_F:
						case TOKEN_FALSE:
						case TOKEN_FIELD_ACCESS:
						case TOKEN_FIELD_READ:
						case TOKEN_FIELD_WRITE:
						case TOKEN_GARBAGE_COLLECTION:
						case TOKEN_HIT_COUNT:
						case TOKEN_IN:
						case TOKEN_INFINITY:
						case TOKEN_INSTRUCTION:
						case TOKEN_INSTRUCTION_TYPE:
						case TOKEN_INVOKE:
						case TOKEN_L:
						case TOKEN_LOCK:
						case TOKEN_METHOD_INVOKE:
						case TOKEN_NAN:
						case TOKEN_NEGATIVE_INFINITY1:
						case TOKEN_NONE:
						case TOKEN_NOTIFY:
						case TOKEN_NULL:
						case TOKEN_OBJECT_CREATED:
						case TOKEN_OBJECT_RELEASED:
						case TOKEN_OR:
						case TOKEN_POSITION:
						case TOKEN_POSITIVE_INFINITY1:
						case TOKEN_PROPERTY_VIOLATED:
						case TOKEN_RETURN:
						case TOKEN_SCHEDULING:
						case TOKEN_STACK_FRAME:
						case TOKEN_STATE_ADVANCED:
						case TOKEN_STEP_IN:
						case TOKEN_STEP_OUT:
						case TOKEN_STEP_OVER:
						case TOKEN_SYNC_BLOCK:
						case TOKEN_THREAD:
						case TOKEN_THREAD_SCHEDULED:
						case TOKEN_TRUE:
						case TOKEN_X:
							{
							alt196=2;
							}
							break;
						default:
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 196, 4, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 196, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA196_1==109) ) {
					switch ( input.LA(3) ) {
					case WS:
						{
						int LA196_5 = input.LA(4);
						if ( (LA196_5==HEX||(LA196_5 >= INT && LA196_5 <= INT_TEXT)) ) {
							alt196=1;
						}
						else if ( (LA196_5==IDF_TEXT_INTERNAL||LA196_5==SIGN_ASTERISK||(LA196_5 >= SIGN_DOLAR && LA196_5 <= SIGN_DOT)||(LA196_5 >= TOKEN_AND && LA196_5 <= TOKEN_ASSERT)||(LA196_5 >= TOKEN_BEGIN && LA196_5 <= TOKEN_GARBAGE_COLLECTION)||(LA196_5 >= TOKEN_HIT_COUNT && LA196_5 <= TOKEN_NEGATIVE_INFINITY1)||(LA196_5 >= TOKEN_NONE && LA196_5 <= TOKEN_NOTIFY)||(LA196_5 >= TOKEN_NULL && LA196_5 <= TOKEN_OR)||(LA196_5 >= TOKEN_POSITION && LA196_5 <= TOKEN_POSITIVE_INFINITY1)||(LA196_5 >= TOKEN_PROPERTY_VIOLATED && LA196_5 <= TOKEN_SCHEDULING)||(LA196_5 >= TOKEN_STACK_FRAME && LA196_5 <= TOKEN_X)) ) {
							alt196=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 196, 5, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case HEX:
					case INT:
					case INT_TEXT:
						{
						alt196=1;
						}
						break;
					case IDF_TEXT_INTERNAL:
					case SIGN_ASTERISK:
					case SIGN_DOLAR:
					case SIGN_DOT:
					case TOKEN_AND:
					case TOKEN_ANY:
					case TOKEN_ARRAY:
					case TOKEN_ASSERT:
					case TOKEN_BEGIN:
					case TOKEN_BOTH:
					case TOKEN_CHOICE_GENERATOR:
					case TOKEN_CONDITION:
					case TOKEN_D:
					case TOKEN_DATA:
					case TOKEN_E:
					case TOKEN_END:
					case TOKEN_EXCEPTION_THROWN:
					case TOKEN_F:
					case TOKEN_FALSE:
					case TOKEN_FIELD_ACCESS:
					case TOKEN_FIELD_READ:
					case TOKEN_FIELD_WRITE:
					case TOKEN_GARBAGE_COLLECTION:
					case TOKEN_HIT_COUNT:
					case TOKEN_IN:
					case TOKEN_INFINITY:
					case TOKEN_INSTRUCTION:
					case TOKEN_INSTRUCTION_TYPE:
					case TOKEN_INVOKE:
					case TOKEN_L:
					case TOKEN_LOCK:
					case TOKEN_METHOD_INVOKE:
					case TOKEN_NAN:
					case TOKEN_NEGATIVE_INFINITY1:
					case TOKEN_NONE:
					case TOKEN_NOTIFY:
					case TOKEN_NULL:
					case TOKEN_OBJECT_CREATED:
					case TOKEN_OBJECT_RELEASED:
					case TOKEN_OR:
					case TOKEN_POSITION:
					case TOKEN_POSITIVE_INFINITY1:
					case TOKEN_PROPERTY_VIOLATED:
					case TOKEN_RETURN:
					case TOKEN_SCHEDULING:
					case TOKEN_STACK_FRAME:
					case TOKEN_STATE_ADVANCED:
					case TOKEN_STEP_IN:
					case TOKEN_STEP_OUT:
					case TOKEN_STEP_OVER:
					case TOKEN_SYNC_BLOCK:
					case TOKEN_THREAD:
					case TOKEN_THREAD_SCHEDULED:
					case TOKEN_TRUE:
					case TOKEN_X:
						{
						alt196=2;
						}
						break;
					default:
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 196, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 196, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA196_0==TOKEN_HASH_STATIC) ) {
				alt196=3;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 196, 0, input);
				throw nvae;
			}

			switch (alt196) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:7: TOKEN_HASH_HEAP ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
					{
					match(input,TOKEN_HASH_HEAP,FOLLOW_TOKEN_HASH_HEAP_in_cmdStateExpressionHeap7943); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:25: ( WS )?
					int alt181=2;
					int LA181_0 = input.LA(1);
					if ( (LA181_0==WS) ) {
						alt181=1;
					}
					switch (alt181) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:25: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap7947); 
							}
							break;

					}

					match(input,109,FOLLOW_109_in_cmdStateExpressionHeap7950); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:33: ( WS )?
					int alt182=2;
					int LA182_0 = input.LA(1);
					if ( (LA182_0==WS) ) {
						alt182=1;
					}
					switch (alt182) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:33: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap7952); 
							}
							break;

					}

					pushFollow(FOLLOW_intValue_in_cmdStateExpressionHeap7955);
					intValue42=intValue();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:47: ( WS )?
					int alt183=2;
					int LA183_0 = input.LA(1);
					if ( (LA183_0==WS) ) {
						alt183=1;
					}
					switch (alt183) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:47: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap7958); 
							}
							break;

					}

					match(input,110,FOLLOW_110_in_cmdStateExpressionHeap7961); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:55: ( WS )?
					int alt184=2;
					int LA184_0 = input.LA(1);
					if ( (LA184_0==WS) ) {
						alt184=1;
					}
					switch (alt184) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:55: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap7963); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:60: (a= cmdStateExpressionValue[$expFactory] )?
					int alt185=2;
					int LA185_0 = input.LA(1);
					if ( (LA185_0==SIGN_DOT||LA185_0==109) ) {
						alt185=1;
					}
					switch (alt185) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:461:60: a= cmdStateExpressionValue[$expFactory]
							{
							pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeap7968);
							a=cmdStateExpressionValue(expFactory);
							state._fsp--;

							}
							break;

					}

					 expr = expFactory.getStateHeap(intValue42, a); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:7: TOKEN_HASH_HEAP ( WS )? '[' ( WS )? className ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
					{
					match(input,TOKEN_HASH_HEAP,FOLLOW_TOKEN_HASH_HEAP_in_cmdStateExpressionHeap7992); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:25: ( WS )?
					int alt186=2;
					int LA186_0 = input.LA(1);
					if ( (LA186_0==WS) ) {
						alt186=1;
					}
					switch (alt186) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:25: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap7996); 
							}
							break;

					}

					match(input,109,FOLLOW_109_in_cmdStateExpressionHeap7999); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:33: ( WS )?
					int alt187=2;
					int LA187_0 = input.LA(1);
					if ( (LA187_0==WS) ) {
						alt187=1;
					}
					switch (alt187) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:33: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap8001); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdStateExpressionHeap8004);
					className43=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:47: ( WS )?
					int alt188=2;
					int LA188_0 = input.LA(1);
					if ( (LA188_0==WS) ) {
						alt188=1;
					}
					switch (alt188) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:47: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap8006); 
							}
							break;

					}

					match(input,110,FOLLOW_110_in_cmdStateExpressionHeap8009); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:55: ( WS )?
					int alt189=2;
					int LA189_0 = input.LA(1);
					if ( (LA189_0==WS) ) {
						alt189=1;
					}
					switch (alt189) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:55: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap8011); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:60: (a= cmdStateExpressionValue[$expFactory] )?
					int alt190=2;
					int LA190_0 = input.LA(1);
					if ( (LA190_0==SIGN_DOT||LA190_0==109) ) {
						alt190=1;
					}
					switch (alt190) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:462:60: a= cmdStateExpressionValue[$expFactory]
							{
							pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeap8016);
							a=cmdStateExpressionValue(expFactory);
							state._fsp--;

							}
							break;

					}

					 expr = expFactory.getStateHeap(className43,   a); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:7: TOKEN_HASH_STATIC ( WS )? '[' ( WS )? className ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
					{
					match(input,TOKEN_HASH_STATIC,FOLLOW_TOKEN_HASH_STATIC_in_cmdStateExpressionHeap8040); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:25: ( WS )?
					int alt191=2;
					int LA191_0 = input.LA(1);
					if ( (LA191_0==WS) ) {
						alt191=1;
					}
					switch (alt191) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:25: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap8042); 
							}
							break;

					}

					match(input,109,FOLLOW_109_in_cmdStateExpressionHeap8045); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:33: ( WS )?
					int alt192=2;
					int LA192_0 = input.LA(1);
					if ( (LA192_0==WS) ) {
						alt192=1;
					}
					switch (alt192) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:33: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap8047); 
							}
							break;

					}

					pushFollow(FOLLOW_className_in_cmdStateExpressionHeap8050);
					className44=className();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:47: ( WS )?
					int alt193=2;
					int LA193_0 = input.LA(1);
					if ( (LA193_0==WS) ) {
						alt193=1;
					}
					switch (alt193) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:47: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap8052); 
							}
							break;

					}

					match(input,110,FOLLOW_110_in_cmdStateExpressionHeap8055); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:55: ( WS )?
					int alt194=2;
					int LA194_0 = input.LA(1);
					if ( (LA194_0==WS) ) {
						alt194=1;
					}
					switch (alt194) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:55: WS
							{
							match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeap8057); 
							}
							break;

					}

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:60: (a= cmdStateExpressionValue[$expFactory] )?
					int alt195=2;
					int LA195_0 = input.LA(1);
					if ( (LA195_0==SIGN_DOT||LA195_0==109) ) {
						alt195=1;
					}
					switch (alt195) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:463:60: a= cmdStateExpressionValue[$expFactory]
							{
							pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeap8062);
							a=cmdStateExpressionValue(expFactory);
							state._fsp--;

							}
							break;

					}

					 expr = expFactory.getStateStaticArea(className44,   a); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionHeap"



	// $ANTLR start "cmdStateExpressionHeapValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:466:1: cmdStateExpressionHeapValue[ExpressionFactory expFactory] returns [ExpressionStateRootNode<ExpressionStateValue> expr] : TOKEN_HASH_HEAP ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? ;
	public final ExpressionStateRootNode<ExpressionStateValue> cmdStateExpressionHeapValue(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateRootNode<ExpressionStateValue> expr = null;


		ExpressionStateValue a =null;
		Integer intValue45 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:5: ( TOKEN_HASH_HEAP ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )? )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:7: TOKEN_HASH_HEAP ( WS )? '[' ( WS )? intValue ( WS )? ']' ( WS )? (a= cmdStateExpressionValue[$expFactory] )?
			{
			match(input,TOKEN_HASH_HEAP,FOLLOW_TOKEN_HASH_HEAP_in_cmdStateExpressionHeapValue8101); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:25: ( WS )?
			int alt197=2;
			int LA197_0 = input.LA(1);
			if ( (LA197_0==WS) ) {
				alt197=1;
			}
			switch (alt197) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:25: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeapValue8105); 
					}
					break;

			}

			match(input,109,FOLLOW_109_in_cmdStateExpressionHeapValue8108); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:33: ( WS )?
			int alt198=2;
			int LA198_0 = input.LA(1);
			if ( (LA198_0==WS) ) {
				alt198=1;
			}
			switch (alt198) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:33: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeapValue8110); 
					}
					break;

			}

			pushFollow(FOLLOW_intValue_in_cmdStateExpressionHeapValue8113);
			intValue45=intValue();
			state._fsp--;

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:47: ( WS )?
			int alt199=2;
			int LA199_0 = input.LA(1);
			if ( (LA199_0==WS) ) {
				alt199=1;
			}
			switch (alt199) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:47: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeapValue8116); 
					}
					break;

			}

			match(input,110,FOLLOW_110_in_cmdStateExpressionHeapValue8119); 
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:55: ( WS )?
			int alt200=2;
			int LA200_0 = input.LA(1);
			if ( (LA200_0==WS) ) {
				int LA200_1 = input.LA(2);
				if ( (LA200_1==EOF||(LA200_1 >= RELOP_EQUAL && LA200_1 <= RELOP_NOT_EQUAL)||LA200_1==SIGN_DOT||LA200_1==WS||LA200_1==106||LA200_1==109) ) {
					alt200=1;
				}
			}
			switch (alt200) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:55: WS
					{
					match(input,WS,FOLLOW_WS_in_cmdStateExpressionHeapValue8121); 
					}
					break;

			}

			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:60: (a= cmdStateExpressionValue[$expFactory] )?
			int alt201=2;
			int LA201_0 = input.LA(1);
			if ( (LA201_0==SIGN_DOT||LA201_0==109) ) {
				alt201=1;
			}
			switch (alt201) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:467:60: a= cmdStateExpressionValue[$expFactory]
					{
					pushFollow(FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeapValue8126);
					a=cmdStateExpressionValue(expFactory);
					state._fsp--;

					}
					break;

			}

			 expr = expFactory.getStateHeap(intValue45, a); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateExpressionHeapValue"



	// $ANTLR start "cmdStateConstValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:470:1: cmdStateConstValue[ExpressionFactory expFactory] returns [ExpressionStateRootNode<ExpressionStateValue> expr] : ( booleanValue | charValue | intValue | longValue | floatValue | doubleValueLimited | stringValue | nullValue );
	public final ExpressionStateRootNode<ExpressionStateValue> cmdStateConstValue(ExpressionFactory expFactory) throws RecognitionException {
		ExpressionStateRootNode<ExpressionStateValue> expr = null;


		boolean booleanValue46 =false;
		char charValue47 =0;
		Integer intValue48 =null;
		Long longValue49 =null;
		float floatValue50 =0.0f;
		double doubleValueLimited51 =0.0;
		String stringValue52 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:471:5: ( booleanValue | charValue | intValue | longValue | floatValue | doubleValueLimited | stringValue | nullValue )
			int alt202=8;
			switch ( input.LA(1) ) {
			case TOKEN_FALSE:
			case TOKEN_TRUE:
				{
				alt202=1;
				}
				break;
			case CHAR:
				{
				alt202=2;
				}
				break;
			case HEX:
			case INT:
			case INT_TEXT:
				{
				alt202=3;
				}
				break;
			case LONG_TEXT:
				{
				alt202=4;
				}
				break;
			case FLOAT_TEXT:
				{
				alt202=5;
				}
				break;
			case DOUBLE_TEXT_LIMITTED:
			case TOKEN_NEGATIVE_INFINITY2:
			case TOKEN_NOT_A_NUMBER:
			case TOKEN_POSITIVE_INFINITY2:
				{
				alt202=6;
				}
				break;
			case STRING:
				{
				alt202=7;
				}
				break;
			case TOKEN_NULL:
				{
				alt202=8;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 202, 0, input);
				throw nvae;
			}
			switch (alt202) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:471:7: booleanValue
					{
					pushFollow(FOLLOW_booleanValue_in_cmdStateConstValue8166);
					booleanValue46=booleanValue();
					state._fsp--;

					 expr = expFactory.getConstValueBoolean(booleanValue46); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:472:7: charValue
					{
					pushFollow(FOLLOW_charValue_in_cmdStateConstValue8184);
					charValue47=charValue();
					state._fsp--;

					 expr = expFactory.getConstValueChar   (charValue47); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:473:7: intValue
					{
					pushFollow(FOLLOW_intValue_in_cmdStateConstValue8205);
					intValue48=intValue();
					state._fsp--;

					 expr = expFactory.getConstValueInteger(intValue48); 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:474:7: longValue
					{
					pushFollow(FOLLOW_longValue_in_cmdStateConstValue8227);
					longValue49=longValue();
					state._fsp--;

					 expr = expFactory.getConstValueLong   (longValue49); 
					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:475:7: floatValue
					{
					pushFollow(FOLLOW_floatValue_in_cmdStateConstValue8248);
					floatValue50=floatValue();
					state._fsp--;

					 expr = expFactory.getConstValueFloat  (floatValue50); 
					}
					break;
				case 6 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:476:7: doubleValueLimited
					{
					pushFollow(FOLLOW_doubleValueLimited_in_cmdStateConstValue8268);
					doubleValueLimited51=doubleValueLimited();
					state._fsp--;

					 expr = expFactory.getConstValueDouble (doubleValueLimited51); 
					}
					break;
				case 7 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:477:7: stringValue
					{
					pushFollow(FOLLOW_stringValue_in_cmdStateConstValue8280);
					stringValue52=stringValue();
					state._fsp--;

					 expr = expFactory.getConstValueString (stringValue52); 
					}
					break;
				case 8 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:478:7: nullValue
					{
					pushFollow(FOLLOW_nullValue_in_cmdStateConstValue8299);
					nullValue();
					state._fsp--;

					 expr = expFactory.getConstValueNull   (); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "cmdStateConstValue"



	// $ANTLR start "relOp"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:481:1: relOp[ExpressionFactory expFactory] returns [RelationOperator relop] : ( RELOP_EQUAL | RELOP_NOT_EQUAL | RELOP_LESS_THAN | RELOP_LESS_OR_EQUAL_THAN | RELOP_GREATER_THAN | RELOP_GREATER_OR_EQUAL_THAN );
	public final RelationOperator relOp(ExpressionFactory expFactory) throws RecognitionException {
		RelationOperator relop = null;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:482:5: ( RELOP_EQUAL | RELOP_NOT_EQUAL | RELOP_LESS_THAN | RELOP_LESS_OR_EQUAL_THAN | RELOP_GREATER_THAN | RELOP_GREATER_OR_EQUAL_THAN )
			int alt203=6;
			switch ( input.LA(1) ) {
			case RELOP_EQUAL:
				{
				alt203=1;
				}
				break;
			case RELOP_NOT_EQUAL:
				{
				alt203=2;
				}
				break;
			case RELOP_LESS_THAN:
				{
				alt203=3;
				}
				break;
			case RELOP_LESS_OR_EQUAL_THAN:
				{
				alt203=4;
				}
				break;
			case RELOP_GREATER_THAN:
				{
				alt203=5;
				}
				break;
			case RELOP_GREATER_OR_EQUAL_THAN:
				{
				alt203=6;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 203, 0, input);
				throw nvae;
			}
			switch (alt203) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:482:7: RELOP_EQUAL
					{
					match(input,RELOP_EQUAL,FOLLOW_RELOP_EQUAL_in_relOp8336); 
					 relop = expFactory.getRelopFactory().getRelOpEqual(); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:483:7: RELOP_NOT_EQUAL
					{
					match(input,RELOP_NOT_EQUAL,FOLLOW_RELOP_NOT_EQUAL_in_relOp8368); 
					 relop = expFactory.getRelopFactory().getRelOpNotEqual(); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:484:7: RELOP_LESS_THAN
					{
					match(input,RELOP_LESS_THAN,FOLLOW_RELOP_LESS_THAN_in_relOp8396); 
					 relop = expFactory.getRelopFactory().getRelOpLessThan(); 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:485:7: RELOP_LESS_OR_EQUAL_THAN
					{
					match(input,RELOP_LESS_OR_EQUAL_THAN,FOLLOW_RELOP_LESS_OR_EQUAL_THAN_in_relOp8424); 
					 relop = expFactory.getRelopFactory().getRelOpLessEqual(); 
					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:486:7: RELOP_GREATER_THAN
					{
					match(input,RELOP_GREATER_THAN,FOLLOW_RELOP_GREATER_THAN_in_relOp8443); 
					 relop = expFactory.getRelopFactory().getRelOpGreaterThan(); 
					}
					break;
				case 6 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:487:7: RELOP_GREATER_OR_EQUAL_THAN
					{
					match(input,RELOP_GREATER_OR_EQUAL_THAN,FOLLOW_RELOP_GREATER_OR_EQUAL_THAN_in_relOp8468); 
					 relop = expFactory.getRelopFactory().getRelOpGreaterEqual(); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return relop;
	}
	// $ANTLR end "relOp"



	// $ANTLR start "booleanValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:492:1: booleanValue returns [boolean value] : ( TOKEN_TRUE | TOKEN_FALSE );
	public final boolean booleanValue() throws RecognitionException {
		boolean value = false;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:493:5: ( TOKEN_TRUE | TOKEN_FALSE )
			int alt204=2;
			int LA204_0 = input.LA(1);
			if ( (LA204_0==TOKEN_TRUE) ) {
				alt204=1;
			}
			else if ( (LA204_0==TOKEN_FALSE) ) {
				alt204=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 204, 0, input);
				throw nvae;
			}

			switch (alt204) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:493:7: TOKEN_TRUE
					{
					match(input,TOKEN_TRUE,FOLLOW_TOKEN_TRUE_in_booleanValue8523); 
					 value = true; 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:494:7: TOKEN_FALSE
					{
					match(input,TOKEN_FALSE,FOLLOW_TOKEN_FALSE_in_booleanValue8542); 
					 value = false; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "booleanValue"



	// $ANTLR start "nullValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:497:1: nullValue returns [Object value] : TOKEN_NULL ;
	public final Object nullValue() throws RecognitionException {
		Object value = null;


		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:498:5: ( TOKEN_NULL )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:498:7: TOKEN_NULL
			{
			match(input,TOKEN_NULL,FOLLOW_TOKEN_NULL_in_nullValue8573); 
			 value = null; 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "nullValue"



	// $ANTLR start "charValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:501:1: charValue returns [char value] : CHAR ;
	public final char charValue() throws RecognitionException {
		char value = 0;


		Token CHAR53=null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:502:5: ( CHAR )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:502:7: CHAR
			{
			CHAR53=(Token)match(input,CHAR,FOLLOW_CHAR_in_charValue8604); 
			 value = ExpressionStateValueConstChar.convertToCharWrapped((CHAR53!=null?CHAR53.getText():null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "charValue"



	// $ANTLR start "stringValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:505:1: stringValue returns [String value] : STRING ;
	public final String stringValue() throws RecognitionException {
		String value = null;


		Token STRING54=null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:506:5: ( STRING )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:506:7: STRING
			{
			STRING54=(Token)match(input,STRING,FOLLOW_STRING_in_stringValue8643); 
			 value = ExpressionStateValueConstString.convertToStringWrapped((STRING54!=null?STRING54.getText():null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "stringValue"



	// $ANTLR start "intValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:509:1: intValue returns [Integer value] : ( INT_TEXT | INT | HEX );
	public final Integer intValue() throws RecognitionException {
		Integer value = null;


		Token INT_TEXT55=null;
		Token INT56=null;
		Token HEX57=null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:510:5: ( INT_TEXT | INT | HEX )
			int alt205=3;
			switch ( input.LA(1) ) {
			case INT_TEXT:
				{
				alt205=1;
				}
				break;
			case INT:
				{
				alt205=2;
				}
				break;
			case HEX:
				{
				alt205=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 205, 0, input);
				throw nvae;
			}
			switch (alt205) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:510:7: INT_TEXT
					{
					INT_TEXT55=(Token)match(input,INT_TEXT,FOLLOW_INT_TEXT_in_intValue8678); 
					 value = ExpressionStateValueConstInteger.convertToIntegerWrapped((INT_TEXT55!=null?INT_TEXT55.getText():null)); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:511:7: INT
					{
					INT56=(Token)match(input,INT,FOLLOW_INT_in_intValue8693); 
					 value = ExpressionStateValueConstInteger.convertToIntegerWrapped((INT56!=null?INT56.getText():null)); 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:512:7: HEX
					{
					HEX57=(Token)match(input,HEX,FOLLOW_HEX_in_intValue8713); 
					 value = ExpressionStateValueConstInteger.convertToIntegerWrapped((HEX57!=null?HEX57.getText():null)); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "intValue"



	// $ANTLR start "longValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:514:1: longValue returns [Long value] : LONG_TEXT ;
	public final Long longValue() throws RecognitionException {
		Long value = null;


		Token LONG_TEXT58=null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:514:37: ( LONG_TEXT )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:514:39: LONG_TEXT
			{
			LONG_TEXT58=(Token)match(input,LONG_TEXT,FOLLOW_LONG_TEXT_in_longValue8746); 
			 value = ExpressionStateValueConstLong.convertToLongWrapped((LONG_TEXT58!=null?LONG_TEXT58.getText():null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "longValue"



	// $ANTLR start "doubleValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:515:1: doubleValue returns [double value] : doubleText ;
	public final double doubleValue() throws RecognitionException {
		double value = 0.0;


		ParserRuleReturnScope doubleText59 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:515:37: ( doubleText )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:515:39: doubleText
			{
			pushFollow(FOLLOW_doubleText_in_doubleValue8765);
			doubleText59=doubleText();
			state._fsp--;

			 value = Double.valueOf((doubleText59!=null?input.toString(doubleText59.start,doubleText59.stop):null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "doubleValue"



	// $ANTLR start "floatValue"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:516:1: floatValue returns [float value] : FLOAT_TEXT ;
	public final float floatValue() throws RecognitionException {
		float value = 0.0f;


		Token FLOAT_TEXT60=null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:516:37: ( FLOAT_TEXT )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:516:39: FLOAT_TEXT
			{
			FLOAT_TEXT60=(Token)match(input,FLOAT_TEXT,FOLLOW_FLOAT_TEXT_in_floatValue8785); 
			 value = Float.valueOf((FLOAT_TEXT60!=null?FLOAT_TEXT60.getText():null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "floatValue"



	// $ANTLR start "doubleValueLimited"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:520:1: doubleValueLimited returns [double value] : ( DOUBLE_TEXT_LIMITTED | TOKEN_NOT_A_NUMBER | TOKEN_NEGATIVE_INFINITY2 | TOKEN_POSITIVE_INFINITY2 );
	public final double doubleValueLimited() throws RecognitionException {
		double value = 0.0;


		Token DOUBLE_TEXT_LIMITTED61=null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:521:5: ( DOUBLE_TEXT_LIMITTED | TOKEN_NOT_A_NUMBER | TOKEN_NEGATIVE_INFINITY2 | TOKEN_POSITIVE_INFINITY2 )
			int alt206=4;
			switch ( input.LA(1) ) {
			case DOUBLE_TEXT_LIMITTED:
				{
				alt206=1;
				}
				break;
			case TOKEN_NOT_A_NUMBER:
				{
				alt206=2;
				}
				break;
			case TOKEN_NEGATIVE_INFINITY2:
				{
				alt206=3;
				}
				break;
			case TOKEN_POSITIVE_INFINITY2:
				{
				alt206=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 206, 0, input);
				throw nvae;
			}
			switch (alt206) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:521:7: DOUBLE_TEXT_LIMITTED
					{
					DOUBLE_TEXT_LIMITTED61=(Token)match(input,DOUBLE_TEXT_LIMITTED,FOLLOW_DOUBLE_TEXT_LIMITTED_in_doubleValueLimited8816); 
					 value = Double.valueOf((DOUBLE_TEXT_LIMITTED61!=null?DOUBLE_TEXT_LIMITTED61.getText():null)); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:522:7: TOKEN_NOT_A_NUMBER
					{
					match(input,TOKEN_NOT_A_NUMBER,FOLLOW_TOKEN_NOT_A_NUMBER_in_doubleValueLimited8839); 
					 value = Double.NaN; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:523:7: TOKEN_NEGATIVE_INFINITY2
					{
					match(input,TOKEN_NEGATIVE_INFINITY2,FOLLOW_TOKEN_NEGATIVE_INFINITY2_in_doubleValueLimited8864); 
					 value = Double.NEGATIVE_INFINITY; 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:524:7: TOKEN_POSITIVE_INFINITY2
					{
					match(input,TOKEN_POSITIVE_INFINITY2,FOLLOW_TOKEN_POSITIVE_INFINITY2_in_doubleValueLimited8883); 
					 value = Double.POSITIVE_INFINITY; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "doubleValueLimited"



	// $ANTLR start "doubleValueExtended"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:529:1: doubleValueExtended returns [double value] : ( doubleText | TOKEN_NOT_A_NUMBER | TOKEN_NEGATIVE_INFINITY1 | TOKEN_NEGATIVE_INFINITY2 | TOKEN_POSITIVE_INFINITY1 | TOKEN_POSITIVE_INFINITY2 );
	public final double doubleValueExtended() throws RecognitionException {
		double value = 0.0;


		ParserRuleReturnScope doubleText62 =null;

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:530:5: ( doubleText | TOKEN_NOT_A_NUMBER | TOKEN_NEGATIVE_INFINITY1 | TOKEN_NEGATIVE_INFINITY2 | TOKEN_POSITIVE_INFINITY1 | TOKEN_POSITIVE_INFINITY2 )
			int alt207=6;
			switch ( input.LA(1) ) {
			case DOUBLE_TEXT_LIMITTED:
			case TOKEN_INFINITY:
			case TOKEN_NAN:
				{
				alt207=1;
				}
				break;
			case TOKEN_NOT_A_NUMBER:
				{
				alt207=2;
				}
				break;
			case TOKEN_NEGATIVE_INFINITY1:
				{
				alt207=3;
				}
				break;
			case TOKEN_NEGATIVE_INFINITY2:
				{
				alt207=4;
				}
				break;
			case TOKEN_POSITIVE_INFINITY1:
				{
				alt207=5;
				}
				break;
			case TOKEN_POSITIVE_INFINITY2:
				{
				alt207=6;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 207, 0, input);
				throw nvae;
			}
			switch (alt207) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:530:7: doubleText
					{
					pushFollow(FOLLOW_doubleText_in_doubleValueExtended8921);
					doubleText62=doubleText();
					state._fsp--;

					 value = Double.valueOf((doubleText62!=null?input.toString(doubleText62.start,doubleText62.stop):null)); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:531:7: TOKEN_NOT_A_NUMBER
					{
					match(input,TOKEN_NOT_A_NUMBER,FOLLOW_TOKEN_NOT_A_NUMBER_in_doubleValueExtended8954); 
					 value = Double.NaN; 
					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:532:7: TOKEN_NEGATIVE_INFINITY1
					{
					match(input,TOKEN_NEGATIVE_INFINITY1,FOLLOW_TOKEN_NEGATIVE_INFINITY1_in_doubleValueExtended8979); 
					 value = Double.NEGATIVE_INFINITY; 
					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:533:7: TOKEN_NEGATIVE_INFINITY2
					{
					match(input,TOKEN_NEGATIVE_INFINITY2,FOLLOW_TOKEN_NEGATIVE_INFINITY2_in_doubleValueExtended8998); 
					 value = Double.NEGATIVE_INFINITY; 
					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:534:7: TOKEN_POSITIVE_INFINITY1
					{
					match(input,TOKEN_POSITIVE_INFINITY1,FOLLOW_TOKEN_POSITIVE_INFINITY1_in_doubleValueExtended9017); 
					 value = Double.POSITIVE_INFINITY; 
					}
					break;
				case 6 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:535:7: TOKEN_POSITIVE_INFINITY2
					{
					match(input,TOKEN_POSITIVE_INFINITY2,FOLLOW_TOKEN_POSITIVE_INFINITY2_in_doubleValueExtended9036); 
					 value = Double.POSITIVE_INFINITY; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "doubleValueExtended"


	public static class doubleText_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "doubleText"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:538:1: doubleText : ( DOUBLE_TEXT_LIMITTED | TOKEN_NAN | TOKEN_INFINITY );
	public final ExpressionGrammarParser.doubleText_return doubleText() throws RecognitionException {
		ExpressionGrammarParser.doubleText_return retval = new ExpressionGrammarParser.doubleText_return();
		retval.start = input.LT(1);

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:539:5: ( DOUBLE_TEXT_LIMITTED | TOKEN_NAN | TOKEN_INFINITY )
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:
			{
			if ( input.LA(1)==DOUBLE_TEXT_LIMITTED||input.LA(1)==TOKEN_INFINITY||input.LA(1)==TOKEN_NAN ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "doubleText"


	public static class idf_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "idf"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:544:1: idf : ( idf_fieldName | javaKeyWords );
	public final ExpressionGrammarParser.idf_return idf() throws RecognitionException {
		ExpressionGrammarParser.idf_return retval = new ExpressionGrammarParser.idf_return();
		retval.start = input.LT(1);

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:545:5: ( idf_fieldName | javaKeyWords )
			int alt208=2;
			int LA208_0 = input.LA(1);
			if ( (LA208_0==IDF_TEXT_INTERNAL||(LA208_0 >= TOKEN_AND && LA208_0 <= TOKEN_ASSERT)||(LA208_0 >= TOKEN_BEGIN && LA208_0 <= TOKEN_F)||(LA208_0 >= TOKEN_FIELD_ACCESS && LA208_0 <= TOKEN_GARBAGE_COLLECTION)||(LA208_0 >= TOKEN_HIT_COUNT && LA208_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA208_0 >= TOKEN_NONE && LA208_0 <= TOKEN_NOTIFY)||(LA208_0 >= TOKEN_OBJECT_CREATED && LA208_0 <= TOKEN_OR)||(LA208_0 >= TOKEN_POSITION && LA208_0 <= TOKEN_POSITIVE_INFINITY1)||(LA208_0 >= TOKEN_PROPERTY_VIOLATED && LA208_0 <= TOKEN_SCHEDULING)||(LA208_0 >= TOKEN_STACK_FRAME && LA208_0 <= TOKEN_THREAD_SCHEDULED)||LA208_0==TOKEN_X) ) {
				alt208=1;
			}
			else if ( (LA208_0==TOKEN_FALSE||LA208_0==TOKEN_NULL||LA208_0==TOKEN_TRUE) ) {
				alt208=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 208, 0, input);
				throw nvae;
			}

			switch (alt208) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:545:7: idf_fieldName
					{
					pushFollow(FOLLOW_idf_fieldName_in_idf9098);
					idf_fieldName();
					state._fsp--;

					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:546:7: javaKeyWords
					{
					pushFollow(FOLLOW_javaKeyWords_in_idf9106);
					javaKeyWords();
					state._fsp--;

					}
					break;

			}
			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "idf"


	public static class idf_fieldName_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "idf_fieldName"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:549:1: idf_fieldName : ( IDF_TEXT_INTERNAL | allKeyWordsIDFLike );
	public final ExpressionGrammarParser.idf_fieldName_return idf_fieldName() throws RecognitionException {
		ExpressionGrammarParser.idf_fieldName_return retval = new ExpressionGrammarParser.idf_fieldName_return();
		retval.start = input.LT(1);

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:550:5: ( IDF_TEXT_INTERNAL | allKeyWordsIDFLike )
			int alt209=2;
			int LA209_0 = input.LA(1);
			if ( (LA209_0==IDF_TEXT_INTERNAL) ) {
				alt209=1;
			}
			else if ( ((LA209_0 >= TOKEN_AND && LA209_0 <= TOKEN_ASSERT)||(LA209_0 >= TOKEN_BEGIN && LA209_0 <= TOKEN_F)||(LA209_0 >= TOKEN_FIELD_ACCESS && LA209_0 <= TOKEN_GARBAGE_COLLECTION)||(LA209_0 >= TOKEN_HIT_COUNT && LA209_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA209_0 >= TOKEN_NONE && LA209_0 <= TOKEN_NOTIFY)||(LA209_0 >= TOKEN_OBJECT_CREATED && LA209_0 <= TOKEN_OR)||(LA209_0 >= TOKEN_POSITION && LA209_0 <= TOKEN_POSITIVE_INFINITY1)||(LA209_0 >= TOKEN_PROPERTY_VIOLATED && LA209_0 <= TOKEN_SCHEDULING)||(LA209_0 >= TOKEN_STACK_FRAME && LA209_0 <= TOKEN_THREAD_SCHEDULED)||LA209_0==TOKEN_X) ) {
				alt209=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 209, 0, input);
				throw nvae;
			}

			switch (alt209) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:550:7: IDF_TEXT_INTERNAL
					{
					match(input,IDF_TEXT_INTERNAL,FOLLOW_IDF_TEXT_INTERNAL_in_idf_fieldName9134); 
					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:551:7: allKeyWordsIDFLike
					{
					pushFollow(FOLLOW_allKeyWordsIDFLike_in_idf_fieldName9142);
					allKeyWordsIDFLike();
					state._fsp--;

					}
					break;

			}
			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "idf_fieldName"


	public static class classNameText_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "classNameText"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:555:1: classNameText : ( idf ( ( '$' | '.' | SIGN_ASTERISK ) ( classNameText )? )? | '$' ( classNameText )? | '.' ( classNameText )? | SIGN_ASTERISK ( classNameText )? );
	public final ExpressionGrammarParser.classNameText_return classNameText() throws RecognitionException {
		ExpressionGrammarParser.classNameText_return retval = new ExpressionGrammarParser.classNameText_return();
		retval.start = input.LT(1);

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:556:5: ( idf ( ( '$' | '.' | SIGN_ASTERISK ) ( classNameText )? )? | '$' ( classNameText )? | '.' ( classNameText )? | SIGN_ASTERISK ( classNameText )? )
			int alt215=4;
			switch ( input.LA(1) ) {
			case IDF_TEXT_INTERNAL:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FALSE:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_NULL:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_TRUE:
			case TOKEN_X:
				{
				alt215=1;
				}
				break;
			case SIGN_DOLAR:
				{
				alt215=2;
				}
				break;
			case SIGN_DOT:
				{
				alt215=3;
				}
				break;
			case SIGN_ASTERISK:
				{
				alt215=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 215, 0, input);
				throw nvae;
			}
			switch (alt215) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:556:7: idf ( ( '$' | '.' | SIGN_ASTERISK ) ( classNameText )? )?
					{
					pushFollow(FOLLOW_idf_in_classNameText9160);
					idf();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:556:11: ( ( '$' | '.' | SIGN_ASTERISK ) ( classNameText )? )?
					int alt211=2;
					int LA211_0 = input.LA(1);
					if ( (LA211_0==SIGN_ASTERISK||(LA211_0 >= SIGN_DOLAR && LA211_0 <= SIGN_DOT)) ) {
						alt211=1;
					}
					switch (alt211) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:556:12: ( '$' | '.' | SIGN_ASTERISK ) ( classNameText )?
							{
							if ( input.LA(1)==SIGN_ASTERISK||(input.LA(1) >= SIGN_DOLAR && input.LA(1) <= SIGN_DOT) ) {
								input.consume();
								state.errorRecovery=false;
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:556:42: ( classNameText )?
							int alt210=2;
							int LA210_0 = input.LA(1);
							if ( (LA210_0==IDF_TEXT_INTERNAL||LA210_0==SIGN_ASTERISK||(LA210_0 >= SIGN_DOLAR && LA210_0 <= SIGN_DOT)||(LA210_0 >= TOKEN_AND && LA210_0 <= TOKEN_ASSERT)||(LA210_0 >= TOKEN_BEGIN && LA210_0 <= TOKEN_GARBAGE_COLLECTION)||(LA210_0 >= TOKEN_HIT_COUNT && LA210_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA210_0 >= TOKEN_NONE && LA210_0 <= TOKEN_NOTIFY)||(LA210_0 >= TOKEN_NULL && LA210_0 <= TOKEN_OR)||(LA210_0 >= TOKEN_POSITION && LA210_0 <= TOKEN_POSITIVE_INFINITY1)||(LA210_0 >= TOKEN_PROPERTY_VIOLATED && LA210_0 <= TOKEN_SCHEDULING)||(LA210_0 >= TOKEN_STACK_FRAME && LA210_0 <= TOKEN_X)) ) {
								alt210=1;
							}
							switch (alt210) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:556:42: classNameText
									{
									pushFollow(FOLLOW_classNameText_in_classNameText9177);
									classNameText();
									state._fsp--;

									}
									break;

							}

							}
							break;

					}

					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:557:7: '$' ( classNameText )?
					{
					match(input,SIGN_DOLAR,FOLLOW_SIGN_DOLAR_in_classNameText9188); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:557:15: ( classNameText )?
					int alt212=2;
					int LA212_0 = input.LA(1);
					if ( (LA212_0==IDF_TEXT_INTERNAL||LA212_0==SIGN_ASTERISK||(LA212_0 >= SIGN_DOLAR && LA212_0 <= SIGN_DOT)||(LA212_0 >= TOKEN_AND && LA212_0 <= TOKEN_ASSERT)||(LA212_0 >= TOKEN_BEGIN && LA212_0 <= TOKEN_GARBAGE_COLLECTION)||(LA212_0 >= TOKEN_HIT_COUNT && LA212_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA212_0 >= TOKEN_NONE && LA212_0 <= TOKEN_NOTIFY)||(LA212_0 >= TOKEN_NULL && LA212_0 <= TOKEN_OR)||(LA212_0 >= TOKEN_POSITION && LA212_0 <= TOKEN_POSITIVE_INFINITY1)||(LA212_0 >= TOKEN_PROPERTY_VIOLATED && LA212_0 <= TOKEN_SCHEDULING)||(LA212_0 >= TOKEN_STACK_FRAME && LA212_0 <= TOKEN_X)) ) {
						alt212=1;
					}
					switch (alt212) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:557:15: classNameText
							{
							pushFollow(FOLLOW_classNameText_in_classNameText9194);
							classNameText();
							state._fsp--;

							}
							break;

					}

					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:558:7: '.' ( classNameText )?
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_classNameText9203); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:558:15: ( classNameText )?
					int alt213=2;
					int LA213_0 = input.LA(1);
					if ( (LA213_0==IDF_TEXT_INTERNAL||LA213_0==SIGN_ASTERISK||(LA213_0 >= SIGN_DOLAR && LA213_0 <= SIGN_DOT)||(LA213_0 >= TOKEN_AND && LA213_0 <= TOKEN_ASSERT)||(LA213_0 >= TOKEN_BEGIN && LA213_0 <= TOKEN_GARBAGE_COLLECTION)||(LA213_0 >= TOKEN_HIT_COUNT && LA213_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA213_0 >= TOKEN_NONE && LA213_0 <= TOKEN_NOTIFY)||(LA213_0 >= TOKEN_NULL && LA213_0 <= TOKEN_OR)||(LA213_0 >= TOKEN_POSITION && LA213_0 <= TOKEN_POSITIVE_INFINITY1)||(LA213_0 >= TOKEN_PROPERTY_VIOLATED && LA213_0 <= TOKEN_SCHEDULING)||(LA213_0 >= TOKEN_STACK_FRAME && LA213_0 <= TOKEN_X)) ) {
						alt213=1;
					}
					switch (alt213) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:558:15: classNameText
							{
							pushFollow(FOLLOW_classNameText_in_classNameText9209);
							classNameText();
							state._fsp--;

							}
							break;

					}

					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:559:7: SIGN_ASTERISK ( classNameText )?
					{
					match(input,SIGN_ASTERISK,FOLLOW_SIGN_ASTERISK_in_classNameText9219); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:559:25: ( classNameText )?
					int alt214=2;
					int LA214_0 = input.LA(1);
					if ( (LA214_0==IDF_TEXT_INTERNAL||LA214_0==SIGN_ASTERISK||(LA214_0 >= SIGN_DOLAR && LA214_0 <= SIGN_DOT)||(LA214_0 >= TOKEN_AND && LA214_0 <= TOKEN_ASSERT)||(LA214_0 >= TOKEN_BEGIN && LA214_0 <= TOKEN_GARBAGE_COLLECTION)||(LA214_0 >= TOKEN_HIT_COUNT && LA214_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA214_0 >= TOKEN_NONE && LA214_0 <= TOKEN_NOTIFY)||(LA214_0 >= TOKEN_NULL && LA214_0 <= TOKEN_OR)||(LA214_0 >= TOKEN_POSITION && LA214_0 <= TOKEN_POSITIVE_INFINITY1)||(LA214_0 >= TOKEN_PROPERTY_VIOLATED && LA214_0 <= TOKEN_SCHEDULING)||(LA214_0 >= TOKEN_STACK_FRAME && LA214_0 <= TOKEN_X)) ) {
						alt214=1;
					}
					switch (alt214) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:559:25: classNameText
							{
							pushFollow(FOLLOW_classNameText_in_classNameText9225);
							classNameText();
							state._fsp--;

							}
							break;

					}

					}
					break;

			}
			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "classNameText"


	public static class fileNameText_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "fileNameText"
	// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:563:1: fileNameText : ( idf ( ( '.' | '\\\\' | '/' | ':' | SIGN_ASTERISK ) ( fileNameText )? )? | '.' ( classNameText )? | '\\\\' ( classNameText )? | '/' ( classNameText )? | SIGN_ASTERISK ( classNameText )? );
	public final ExpressionGrammarParser.fileNameText_return fileNameText() throws RecognitionException {
		ExpressionGrammarParser.fileNameText_return retval = new ExpressionGrammarParser.fileNameText_return();
		retval.start = input.LT(1);

		try {
			// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:564:5: ( idf ( ( '.' | '\\\\' | '/' | ':' | SIGN_ASTERISK ) ( fileNameText )? )? | '.' ( classNameText )? | '\\\\' ( classNameText )? | '/' ( classNameText )? | SIGN_ASTERISK ( classNameText )? )
			int alt222=5;
			switch ( input.LA(1) ) {
			case IDF_TEXT_INTERNAL:
			case TOKEN_AND:
			case TOKEN_ANY:
			case TOKEN_ARRAY:
			case TOKEN_ASSERT:
			case TOKEN_BEGIN:
			case TOKEN_BOTH:
			case TOKEN_CHOICE_GENERATOR:
			case TOKEN_CONDITION:
			case TOKEN_D:
			case TOKEN_DATA:
			case TOKEN_E:
			case TOKEN_END:
			case TOKEN_EXCEPTION_THROWN:
			case TOKEN_F:
			case TOKEN_FALSE:
			case TOKEN_FIELD_ACCESS:
			case TOKEN_FIELD_READ:
			case TOKEN_FIELD_WRITE:
			case TOKEN_GARBAGE_COLLECTION:
			case TOKEN_HIT_COUNT:
			case TOKEN_IN:
			case TOKEN_INFINITY:
			case TOKEN_INSTRUCTION:
			case TOKEN_INSTRUCTION_TYPE:
			case TOKEN_INVOKE:
			case TOKEN_L:
			case TOKEN_LOCK:
			case TOKEN_METHOD_INVOKE:
			case TOKEN_NAN:
			case TOKEN_NEGATIVE_INFINITY1:
			case TOKEN_NONE:
			case TOKEN_NOTIFY:
			case TOKEN_NULL:
			case TOKEN_OBJECT_CREATED:
			case TOKEN_OBJECT_RELEASED:
			case TOKEN_OR:
			case TOKEN_POSITION:
			case TOKEN_POSITIVE_INFINITY1:
			case TOKEN_PROPERTY_VIOLATED:
			case TOKEN_RETURN:
			case TOKEN_SCHEDULING:
			case TOKEN_STACK_FRAME:
			case TOKEN_STATE_ADVANCED:
			case TOKEN_STEP_IN:
			case TOKEN_STEP_OUT:
			case TOKEN_STEP_OVER:
			case TOKEN_SYNC_BLOCK:
			case TOKEN_THREAD:
			case TOKEN_THREAD_SCHEDULED:
			case TOKEN_TRUE:
			case TOKEN_X:
				{
				alt222=1;
				}
				break;
			case SIGN_DOT:
				{
				alt222=2;
				}
				break;
			case SIGN_BACK_SHLASH:
				{
				alt222=3;
				}
				break;
			case 107:
				{
				alt222=4;
				}
				break;
			case SIGN_ASTERISK:
				{
				alt222=5;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 222, 0, input);
				throw nvae;
			}
			switch (alt222) {
				case 1 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:564:7: idf ( ( '.' | '\\\\' | '/' | ':' | SIGN_ASTERISK ) ( fileNameText )? )?
					{
					pushFollow(FOLLOW_idf_in_fileNameText9247);
					idf();
					state._fsp--;

					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:564:11: ( ( '.' | '\\\\' | '/' | ':' | SIGN_ASTERISK ) ( fileNameText )? )?
					int alt217=2;
					int LA217_0 = input.LA(1);
					if ( (LA217_0==108) ) {
						int LA217_1 = input.LA(2);
						if ( (LA217_1==IDF_TEXT_INTERNAL||(LA217_1 >= SIGN_ASTERISK && LA217_1 <= SIGN_BACK_SHLASH)||LA217_1==SIGN_DOT||(LA217_1 >= TOKEN_AND && LA217_1 <= TOKEN_ASSERT)||(LA217_1 >= TOKEN_BEGIN && LA217_1 <= TOKEN_GARBAGE_COLLECTION)||(LA217_1 >= TOKEN_HIT_COUNT && LA217_1 <= TOKEN_NEGATIVE_INFINITY1)||(LA217_1 >= TOKEN_NONE && LA217_1 <= TOKEN_NOTIFY)||(LA217_1 >= TOKEN_NULL && LA217_1 <= TOKEN_OR)||(LA217_1 >= TOKEN_POSITION && LA217_1 <= TOKEN_POSITIVE_INFINITY1)||(LA217_1 >= TOKEN_PROPERTY_VIOLATED && LA217_1 <= TOKEN_SCHEDULING)||(LA217_1 >= TOKEN_STACK_FRAME && LA217_1 <= TOKEN_X)||(LA217_1 >= 107 && LA217_1 <= 108)) ) {
							alt217=1;
						}
						else if ( (LA217_1==WS) ) {
							int LA217_4 = input.LA(3);
							if ( (LA217_4==108) ) {
								alt217=1;
							}
						}
					}
					else if ( ((LA217_0 >= SIGN_ASTERISK && LA217_0 <= SIGN_BACK_SHLASH)||LA217_0==SIGN_DOT||LA217_0==107) ) {
						alt217=1;
					}
					switch (alt217) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:564:13: ( '.' | '\\\\' | '/' | ':' | SIGN_ASTERISK ) ( fileNameText )?
							{
							if ( (input.LA(1) >= SIGN_ASTERISK && input.LA(1) <= SIGN_BACK_SHLASH)||input.LA(1)==SIGN_DOT||(input.LA(1) >= 107 && input.LA(1) <= 108) ) {
								input.consume();
								state.errorRecovery=false;
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:564:55: ( fileNameText )?
							int alt216=2;
							int LA216_0 = input.LA(1);
							if ( (LA216_0==IDF_TEXT_INTERNAL||(LA216_0 >= SIGN_ASTERISK && LA216_0 <= SIGN_BACK_SHLASH)||LA216_0==SIGN_DOT||(LA216_0 >= TOKEN_AND && LA216_0 <= TOKEN_ASSERT)||(LA216_0 >= TOKEN_BEGIN && LA216_0 <= TOKEN_GARBAGE_COLLECTION)||(LA216_0 >= TOKEN_HIT_COUNT && LA216_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA216_0 >= TOKEN_NONE && LA216_0 <= TOKEN_NOTIFY)||(LA216_0 >= TOKEN_NULL && LA216_0 <= TOKEN_OR)||(LA216_0 >= TOKEN_POSITION && LA216_0 <= TOKEN_POSITIVE_INFINITY1)||(LA216_0 >= TOKEN_PROPERTY_VIOLATED && LA216_0 <= TOKEN_SCHEDULING)||(LA216_0 >= TOKEN_STACK_FRAME && LA216_0 <= TOKEN_X)||LA216_0==107) ) {
								alt216=1;
							}
							switch (alt216) {
								case 1 :
									// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:564:55: fileNameText
									{
									pushFollow(FOLLOW_fileNameText_in_fileNameText9272);
									fileNameText();
									state._fsp--;

									}
									break;

							}

							}
							break;

					}

					}
					break;
				case 2 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:565:7: '.' ( classNameText )?
					{
					match(input,SIGN_DOT,FOLLOW_SIGN_DOT_in_fileNameText9283); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:565:11: ( classNameText )?
					int alt218=2;
					int LA218_0 = input.LA(1);
					if ( (LA218_0==IDF_TEXT_INTERNAL||LA218_0==SIGN_ASTERISK||(LA218_0 >= SIGN_DOLAR && LA218_0 <= SIGN_DOT)||(LA218_0 >= TOKEN_AND && LA218_0 <= TOKEN_ASSERT)||(LA218_0 >= TOKEN_BEGIN && LA218_0 <= TOKEN_GARBAGE_COLLECTION)||(LA218_0 >= TOKEN_HIT_COUNT && LA218_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA218_0 >= TOKEN_NONE && LA218_0 <= TOKEN_NOTIFY)||(LA218_0 >= TOKEN_NULL && LA218_0 <= TOKEN_OR)||(LA218_0 >= TOKEN_POSITION && LA218_0 <= TOKEN_POSITIVE_INFINITY1)||(LA218_0 >= TOKEN_PROPERTY_VIOLATED && LA218_0 <= TOKEN_SCHEDULING)||(LA218_0 >= TOKEN_STACK_FRAME && LA218_0 <= TOKEN_X)) ) {
						alt218=1;
					}
					switch (alt218) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:565:11: classNameText
							{
							pushFollow(FOLLOW_classNameText_in_fileNameText9285);
							classNameText();
							state._fsp--;

							}
							break;

					}

					}
					break;
				case 3 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:566:7: '\\\\' ( classNameText )?
					{
					match(input,SIGN_BACK_SHLASH,FOLLOW_SIGN_BACK_SHLASH_in_fileNameText9294); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:566:12: ( classNameText )?
					int alt219=2;
					int LA219_0 = input.LA(1);
					if ( (LA219_0==IDF_TEXT_INTERNAL||LA219_0==SIGN_ASTERISK||(LA219_0 >= SIGN_DOLAR && LA219_0 <= SIGN_DOT)||(LA219_0 >= TOKEN_AND && LA219_0 <= TOKEN_ASSERT)||(LA219_0 >= TOKEN_BEGIN && LA219_0 <= TOKEN_GARBAGE_COLLECTION)||(LA219_0 >= TOKEN_HIT_COUNT && LA219_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA219_0 >= TOKEN_NONE && LA219_0 <= TOKEN_NOTIFY)||(LA219_0 >= TOKEN_NULL && LA219_0 <= TOKEN_OR)||(LA219_0 >= TOKEN_POSITION && LA219_0 <= TOKEN_POSITIVE_INFINITY1)||(LA219_0 >= TOKEN_PROPERTY_VIOLATED && LA219_0 <= TOKEN_SCHEDULING)||(LA219_0 >= TOKEN_STACK_FRAME && LA219_0 <= TOKEN_X)) ) {
						alt219=1;
					}
					switch (alt219) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:566:12: classNameText
							{
							pushFollow(FOLLOW_classNameText_in_fileNameText9296);
							classNameText();
							state._fsp--;

							}
							break;

					}

					}
					break;
				case 4 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:567:7: '/' ( classNameText )?
					{
					match(input,107,FOLLOW_107_in_fileNameText9305); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:567:11: ( classNameText )?
					int alt220=2;
					int LA220_0 = input.LA(1);
					if ( (LA220_0==IDF_TEXT_INTERNAL||LA220_0==SIGN_ASTERISK||(LA220_0 >= SIGN_DOLAR && LA220_0 <= SIGN_DOT)||(LA220_0 >= TOKEN_AND && LA220_0 <= TOKEN_ASSERT)||(LA220_0 >= TOKEN_BEGIN && LA220_0 <= TOKEN_GARBAGE_COLLECTION)||(LA220_0 >= TOKEN_HIT_COUNT && LA220_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA220_0 >= TOKEN_NONE && LA220_0 <= TOKEN_NOTIFY)||(LA220_0 >= TOKEN_NULL && LA220_0 <= TOKEN_OR)||(LA220_0 >= TOKEN_POSITION && LA220_0 <= TOKEN_POSITIVE_INFINITY1)||(LA220_0 >= TOKEN_PROPERTY_VIOLATED && LA220_0 <= TOKEN_SCHEDULING)||(LA220_0 >= TOKEN_STACK_FRAME && LA220_0 <= TOKEN_X)) ) {
						alt220=1;
					}
					switch (alt220) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:567:11: classNameText
							{
							pushFollow(FOLLOW_classNameText_in_fileNameText9307);
							classNameText();
							state._fsp--;

							}
							break;

					}

					}
					break;
				case 5 :
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:568:7: SIGN_ASTERISK ( classNameText )?
					{
					match(input,SIGN_ASTERISK,FOLLOW_SIGN_ASTERISK_in_fileNameText9316); 
					// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:568:22: ( classNameText )?
					int alt221=2;
					int LA221_0 = input.LA(1);
					if ( (LA221_0==IDF_TEXT_INTERNAL||LA221_0==SIGN_ASTERISK||(LA221_0 >= SIGN_DOLAR && LA221_0 <= SIGN_DOT)||(LA221_0 >= TOKEN_AND && LA221_0 <= TOKEN_ASSERT)||(LA221_0 >= TOKEN_BEGIN && LA221_0 <= TOKEN_GARBAGE_COLLECTION)||(LA221_0 >= TOKEN_HIT_COUNT && LA221_0 <= TOKEN_NEGATIVE_INFINITY1)||(LA221_0 >= TOKEN_NONE && LA221_0 <= TOKEN_NOTIFY)||(LA221_0 >= TOKEN_NULL && LA221_0 <= TOKEN_OR)||(LA221_0 >= TOKEN_POSITION && LA221_0 <= TOKEN_POSITIVE_INFINITY1)||(LA221_0 >= TOKEN_PROPERTY_VIOLATED && LA221_0 <= TOKEN_SCHEDULING)||(LA221_0 >= TOKEN_STACK_FRAME && LA221_0 <= TOKEN_X)) ) {
						alt221=1;
					}
					switch (alt221) {
						case 1 :
							// D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\server\\expression\\parser/ExpressionGrammar.g:568:22: classNameText
							{
							pushFollow(FOLLOW_classNameText_in_fileNameText9319);
							classNameText();
							state._fsp--;

							}
							break;

					}

					}
					break;

			}
			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "fileNameText"

	// Delegated rules



	public static final BitSet FOLLOW_classNameText_in_className3289 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fileNameText_in_fileName3321 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_idf_in_fieldName3356 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_idf_in_methodName3400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams3451 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_cmdBreakpointsCreateParams3457 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdBreakpoinstCreateParamsAtomNotTerminateIDF_in_cmdBreakpointsCreateParams13483 = new BitSet(new long[]{0x0000004000000002L,0x0000000000200000L});
	public static final BitSet FOLLOW_TOKEN_AND_in_cmdBreakpointsCreateParams13502 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000003FFFFBFFFFFL});
	public static final BitSet FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13506 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_OR_in_cmdBreakpointsCreateParams13529 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000003FFFFBFFFFFL});
	public static final BitSet FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdBreakpoinstCreateParamsAtomTerminateIDF_in_cmdBreakpointsCreateParams13563 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpointsCreateParams13582 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_TOKEN_AND_in_cmdBreakpointsCreateParams13584 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000003FFFFBFFFFFL});
	public static final BitSet FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpointsCreateParams13611 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_TOKEN_OR_in_cmdBreakpointsCreateParams13613 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000003FFFFBFFFFFL});
	public static final BitSet FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpointsCreateParams13618 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdBreakpoinstCreateParamsAtomNotTerminateIDF_in_cmdBreakpointsCreateParamsAtom3663 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_cmdBreakpointsCreateParamsAtom3706 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdBreakpoinstCreateParamsAtomTerminateIDF_in_cmdBreakpointsCreateParamsAtom3716 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_cmdBreakpointsCreateParamsAtom3762 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3785 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_TOKEN_GARBAGE_COLLECTION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3788 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3792 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3795 = new BitSet(new long[]{0x0004180000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3797 = new BitSet(new long[]{0x0004180000000000L});
	public static final BitSet FOLLOW_cmdGarbageCollectionSpec_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3800 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3802 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3841 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_TOKEN_CHOICE_GENERATOR_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3844 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3850 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3853 = new BitSet(new long[]{0x0001100000000000L,0x0000010010000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3855 = new BitSet(new long[]{0x0001100000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_cmdChoiceGeneratorType_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3858 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3860 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3901 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_TOKEN_INSTRUCTION_TYPE_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3904 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3910 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3913 = new BitSet(new long[]{0x01C0418000000000L,0x0000010008008500L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3915 = new BitSet(new long[]{0x01C0418000000000L,0x0000000008008500L});
	public static final BitSet FOLLOW_cmdInstructionTypes_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3918 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3920 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3964 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_TOKEN_POSITION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3967 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3981 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3984 = new BitSet(new long[]{0x03FFFBC0B0040000L,0x000009FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3986 = new BitSet(new long[]{0x03FFFBC0B0040000L,0x000008FFDDBDBFF8L});
	public static final BitSet FOLLOW_fileName_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3989 = new BitSet(new long[]{0x0000000000000000L,0x0000110000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3991 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3994 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3996 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF3999 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4001 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4035 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_TOKEN_PROPERTY_VIOLATED_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4038 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4043 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4119 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_TOKEN_THREAD_SCHEDULED_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4122 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4128 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4131 = new BitSet(new long[]{0x0000100000000000L,0x0000010000400010L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4133 = new BitSet(new long[]{0x0000100000000000L,0x0000000000400010L});
	public static final BitSet FOLLOW_cmdThreadScheduledDirection_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4136 = new BitSet(new long[]{0x0000000000000002L,0x0000110000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4138 = new BitSet(new long[]{0x0000000000000002L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4142 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4144 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4147 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4163 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_TOKEN_STATE_ADVANCED_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4166 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4174 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4250 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_TOKEN_STEP_IN_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4253 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4268 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4344 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_TOKEN_STEP_OVER_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4347 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4360 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4436 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4439 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000003FFFFBFFFFFL});
	public static final BitSet FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4443 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
	public static final BitSet FOLLOW_106_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4446 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4501 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_TOKEN_STEP_OUT_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4504 = new BitSet(new long[]{0x0000000000000000L,0x0000011000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4518 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_TOKEN_THREAD_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4521 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4523 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4526 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4528 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4533 = new BitSet(new long[]{0x0000000000000000L,0x0000010040000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4535 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_TOKEN_STACK_FRAME_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4538 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4540 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4543 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4545 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4550 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4552 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4670 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_TOKEN_SPECIFIC_INSTRUCTION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4673 = new BitSet(new long[]{0x0000000000000000L,0x0000011000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4675 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_TOKEN_THREAD_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4678 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4680 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4683 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4685 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4690 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000040L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4692 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_TOKEN_INSTRUCTION_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4695 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4697 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4700 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4702 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4705 = new BitSet(new long[]{0x0000000000000000L,0x0000110000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4707 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4710 = new BitSet(new long[]{0x03FFFBC000040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4712 = new BitSet(new long[]{0x03FFFBC000040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_methodName_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4715 = new BitSet(new long[]{0x0000000000000000L,0x0000110000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4718 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4721 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4723 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4728 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000008L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_TOKEN_HIT_COUNT_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4733 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4735 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4738 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4740 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4745 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4747 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4759 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_TOKEN_ASSERT_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4762 = new BitSet(new long[]{0x0000000000000000L,0x0000030000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4778 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4781 = new BitSet(new long[]{0x03FFFBC0B0040000L,0x000008FFDDBDBFF8L});
	public static final BitSet FOLLOW_fileName_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4783 = new BitSet(new long[]{0x0000000000000000L,0x0000110000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4785 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4788 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4790 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4793 = new BitSet(new long[]{0x0000000000000000L,0x0000050000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4795 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
	public static final BitSet FOLLOW_106_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4798 = new BitSet(new long[]{0x0000000000000000L,0x0000030000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4800 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4803 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000003FFFFBFFFFFL});
	public static final BitSet FOLLOW_cmdBreakpointsCreateParams1_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4805 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
	public static final BitSet FOLLOW_106_in_cmdBreakpoinstCreateParamsAtomNotTerminateIDF4808 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4939 = new BitSet(new long[]{0x01C0000000000000L});
	public static final BitSet FOLLOW_fieldAccess_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4942 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4957 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4960 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4962 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4965 = new BitSet(new long[]{0x0000000000000000L,0x0000110000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4967 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4970 = new BitSet(new long[]{0x03FFFBC000040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4972 = new BitSet(new long[]{0x03FFFBC000040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_fieldName_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4975 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4995 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_TOKEN_METHOD_INVOKE_in_cmdBreakpoinstCreateParamsAtomTerminateIDF4998 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5005 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5008 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5010 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5013 = new BitSet(new long[]{0x0000000000000000L,0x0000110000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5015 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5018 = new BitSet(new long[]{0x03FFFBC000040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5020 = new BitSet(new long[]{0x03FFFBC000040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_methodName_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5023 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5042 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_TOKEN_OBJECT_CREATED_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5045 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5051 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5054 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5056 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5059 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5115 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_TOKEN_OBJECT_RELEASED_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5118 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5123 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5126 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5128 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5131 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5187 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_TOKEN_EXCEPTION_THROWN_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5190 = new BitSet(new long[]{0x0000000200000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5194 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5197 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5199 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5202 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5204 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpression1Value_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5260 = new BitSet(new long[]{0x000000000FC00000L});
	public static final BitSet FOLLOW_relOp_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5264 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000001FFDFBFFFFFL});
	public static final BitSet FOLLOW_cmdStateExpression1Value_in_cmdBreakpoinstCreateParamsAtomTerminateIDF5269 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_allKeyWordsIDFLike_in_allKeyWords5296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_allKeywordsOther_in_allKeyWords5325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_javaKeyWords_in_allKeyWords5356 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_FIELD_ACCESS_in_fieldAccess5403 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_FIELD_READ_in_fieldAccess5413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_FIELD_WRITE_in_fieldAccess5425 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_BEGIN_in_cmdGarbageCollectionSpec5449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_END_in_cmdGarbageCollectionSpec5459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_BOTH_in_cmdGarbageCollectionSpec5471 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_DATA_in_cmdChoiceGeneratorType5495 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_SCHEDULING_in_cmdChoiceGeneratorType5511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_BOTH_in_cmdChoiceGeneratorType5521 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_ANY_in_cmdInstructionTypes5550 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_NONE_in_cmdInstructionTypes5574 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_INVOKE_in_cmdInstructionTypes5597 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_RETURN_in_cmdInstructionTypes5618 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_FIELD_ACCESS_in_cmdInstructionTypes5639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_FIELD_READ_in_cmdInstructionTypes5654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_FIELD_WRITE_in_cmdInstructionTypes5671 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_CONDITION_in_cmdInstructionTypes5687 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_LOCK_in_cmdInstructionTypes5705 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_ARRAY_in_cmdInstructionTypes5728 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_IN_in_cmdThreadScheduledDirection5763 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_OUT_in_cmdThreadScheduledDirection5788 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_BOTH_in_cmdThreadScheduledDirection5812 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpression1_in_cmdStateAssignment5852 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SIGN_EQUALS_in_cmdStateAssignment5855 = new BitSet(new long[]{0xFFFFFBE0003F0030L,0x000001FFDFBFFFFFL});
	public static final BitSet FOLLOW_cmdStateExpression1_in_cmdStateAssignment5859 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_cmdStateAssignment5864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpression1_in_cmdStateExpression5892 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_cmdStateExpression5897 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression15923 = new BitSet(new long[]{0xF7DFFBC000040000L,0x000000BFDDB9BFFFL});
	public static final BitSet FOLLOW_cmdStateExpressionThread_in_cmdStateExpression15926 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression15944 = new BitSet(new long[]{0x8800000000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionHeap_in_cmdStateExpression15947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression15967 = new BitSet(new long[]{0x00200020003B0030L,0x0000004002064000L});
	public static final BitSet FOLLOW_cmdStateConstValue_in_cmdStateExpression15970 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression15973 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression1Value6019 = new BitSet(new long[]{0xF7DFFBC000040000L,0x000000BFDDB9BFFFL});
	public static final BitSet FOLLOW_cmdStateExpressionThreadValue_in_cmdStateExpression1Value6022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression1Value6040 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionHeapValue_in_cmdStateExpression1Value6043 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression1Value6063 = new BitSet(new long[]{0x00200020003B0030L,0x0000004002064000L});
	public static final BitSet FOLLOW_cmdStateConstValue_in_cmdStateExpression1Value6066 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpression1Value6070 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_THREAD_in_cmdStateExpressionThread6110 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThread6112 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionThread6116 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThread6118 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionThread6121 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThread6123 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionThread6126 = new BitSet(new long[]{0x0000000080000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThread6128 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionThread6134 = new BitSet(new long[]{0xF7DFFBC000040000L,0x000001BFDDB9BFFBL});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThread6136 = new BitSet(new long[]{0xF7DFFBC000040000L,0x000000BFDDB9BFFBL});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrame_in_cmdStateExpressionThread6141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrame_in_cmdStateExpressionThread6220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_THREAD_in_cmdStateExpressionThreadValue6249 = new BitSet(new long[]{0x0000000080000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThreadValue6251 = new BitSet(new long[]{0x0000000080000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionThreadValue6255 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThreadValue6257 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionThreadValue6260 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThreadValue6262 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionThreadValue6265 = new BitSet(new long[]{0x0000000080000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThreadValue6267 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionThreadValue6273 = new BitSet(new long[]{0xF7DFFBC000040000L,0x000001BFDDB9BFFBL});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionThreadValue6275 = new BitSet(new long[]{0xF7DFFBC000040000L,0x000000BFDDB9BFFBL});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrameValue_in_cmdStateExpressionThreadValue6280 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrameValue_in_cmdStateExpressionThreadValue6359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_STACK_FRAME_in_cmdStateExpressionStackFrame6388 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrame6390 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionStackFrame6394 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrame6396 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionStackFrame6399 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrame6401 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionStackFrame6404 = new BitSet(new long[]{0x0000000080000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrame6406 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionStackFrame6412 = new BitSet(new long[]{0xD7DFFBC000040000L,0x000001BFDDB9BFFBL});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrame6414 = new BitSet(new long[]{0xD7DFFBC000040000L,0x000000BFDDB9BFFBL});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrame6419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrame6503 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_STACK_FRAME_in_cmdStateExpressionStackFrameValue6684 = new BitSet(new long[]{0x0000000080000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrameValue6686 = new BitSet(new long[]{0x0000000080000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionStackFrameValue6690 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrameValue6692 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionStackFrameValue6695 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrameValue6697 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionStackFrameValue6700 = new BitSet(new long[]{0x0000000080000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrameValue6702 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionStackFrameValue6708 = new BitSet(new long[]{0xD7DFFBC000040000L,0x000001BFDDB9BFFBL});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionStackFrameValue6710 = new BitSet(new long[]{0xD7DFFBC000040000L,0x000000BFDDB9BFFBL});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrameValue6715 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionStackFrame1_in_cmdStateExpressionStackFrameValue6799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueStackFrameSlot_in_cmdStateExpressionStackFrame16841 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueName_in_cmdStateExpressionStackFrame16859 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueAfterStackFrame_in_cmdStateExpressionStackFrame16887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueStatic_in_cmdStateExpressionStackFrame16904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueFieldIndex_in_cmdStateExpressionValueAfterStackFrame6946 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueOuterClass_in_cmdStateExpressionValueAfterStackFrame6982 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueSuper_in_cmdStateExpressionValueAfterStackFrame7018 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueThis_in_cmdStateExpressionValueAfterStackFrame7059 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_FIELD_in_cmdStateExpressionValueFieldIndex7115 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7117 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionValueFieldIndex7120 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7122 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionValueFieldIndex7125 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7127 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionValueFieldIndex7130 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueFieldIndex7132 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueFieldIndex7137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_idf_fieldName_in_cmdStateExpressionValueName7171 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueName7173 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueName7178 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_OUTER_CLASS_in_cmdStateExpressionValueOuterClass7254 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueOuterClass7256 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueOuterClass7261 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_STATIC_in_cmdStateExpressionValueStatic7318 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStatic7320 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionValueStatic7323 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStatic7325 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionValueStatic7328 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStatic7330 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionValueStatic7333 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStatic7335 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueStatic7340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_STATIC_in_cmdStateExpressionValueStatic7355 = new BitSet(new long[]{0x0000000080000002L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStatic7386 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_cmdStateExpressionClass_in_cmdStateExpressionValueStatic7391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_STACK_SLOT_in_cmdStateExpressionValueStackFrameSlot7425 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7427 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionValueStackFrameSlot7430 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7432 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionValueStackFrameSlot7435 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7437 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionValueStackFrameSlot7440 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueStackFrameSlot7442 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueStackFrameSlot7448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_SUPER_in_cmdStateExpressionValueSuper7476 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueSuper7478 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueSuper7484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_THIS_in_cmdStateExpressionValueThis7512 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueThis7514 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueThis7519 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionValueArray7547 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueArray7549 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionValueArray7552 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueArray7554 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionValueArray7557 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionValueArray7559 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionValueArray7564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionClass_in_cmdStateExpressionValue7614 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueArray_in_cmdStateExpressionValue7633 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7661 = new BitSet(new long[]{0x0400000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionClass7663 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValueFieldIndex_in_cmdStateExpressionClass7668 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7702 = new BitSet(new long[]{0x03DFFBC000040000L,0x000001BFDDB9BFF8L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionClass7704 = new BitSet(new long[]{0x03DFFBC000040000L,0x000000BFDDB9BFF8L});
	public static final BitSet FOLLOW_cmdStateExpressionValueName_in_cmdStateExpressionClass7709 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7749 = new BitSet(new long[]{0x1000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionClass7751 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValueOuterClass_in_cmdStateExpressionClass7756 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7790 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000001L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionClass7792 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_cmdStateExpressionValueSuper_in_cmdStateExpressionClass7797 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7836 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000002L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionClass7838 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_cmdStateExpressionValueThis_in_cmdStateExpressionClass7843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_cmdStateExpressionClass7883 = new BitSet(new long[]{0x8000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionClass7885 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValueStatic_in_cmdStateExpressionClass7890 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_HEAP_in_cmdStateExpressionHeap7943 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap7947 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionHeap7950 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap7952 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionHeap7955 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap7958 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionHeap7961 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap7963 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeap7968 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_HEAP_in_cmdStateExpressionHeap7992 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap7996 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionHeap7999 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap8001 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdStateExpressionHeap8004 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap8006 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionHeap8009 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap8011 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeap8016 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_STATIC_in_cmdStateExpressionHeap8040 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap8042 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionHeap8045 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000001FFDDBDBFF8L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap8047 = new BitSet(new long[]{0x03FFFBC0D0040000L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_className_in_cmdStateExpressionHeap8050 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap8052 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionHeap8055 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeap8057 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeap8062 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_HASH_HEAP_in_cmdStateExpressionHeapValue8101 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeapValue8105 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_cmdStateExpressionHeapValue8108 = new BitSet(new long[]{0x00000000001A0000L,0x0000010000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeapValue8110 = new BitSet(new long[]{0x00000000001A0000L});
	public static final BitSet FOLLOW_intValue_in_cmdStateExpressionHeapValue8113 = new BitSet(new long[]{0x0000000000000000L,0x0000410000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeapValue8116 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_cmdStateExpressionHeapValue8119 = new BitSet(new long[]{0x0000000080000002L,0x0000210000000000L});
	public static final BitSet FOLLOW_WS_in_cmdStateExpressionHeapValue8121 = new BitSet(new long[]{0x0000000080000002L,0x0000200000000000L});
	public static final BitSet FOLLOW_cmdStateExpressionValue_in_cmdStateExpressionHeapValue8126 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_booleanValue_in_cmdStateConstValue8166 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_charValue_in_cmdStateConstValue8184 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_intValue_in_cmdStateConstValue8205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_longValue_in_cmdStateConstValue8227 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_floatValue_in_cmdStateConstValue8248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_doubleValueLimited_in_cmdStateConstValue8268 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringValue_in_cmdStateConstValue8280 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullValue_in_cmdStateConstValue8299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RELOP_EQUAL_in_relOp8336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RELOP_NOT_EQUAL_in_relOp8368 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RELOP_LESS_THAN_in_relOp8396 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RELOP_LESS_OR_EQUAL_THAN_in_relOp8424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RELOP_GREATER_THAN_in_relOp8443 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RELOP_GREATER_OR_EQUAL_THAN_in_relOp8468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_TRUE_in_booleanValue8523 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_FALSE_in_booleanValue8542 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_NULL_in_nullValue8573 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CHAR_in_charValue8604 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_stringValue8643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_TEXT_in_intValue8678 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_intValue8693 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HEX_in_intValue8713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LONG_TEXT_in_longValue8746 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_doubleText_in_doubleValue8765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FLOAT_TEXT_in_floatValue8785 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLE_TEXT_LIMITTED_in_doubleValueLimited8816 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_NOT_A_NUMBER_in_doubleValueLimited8839 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_NEGATIVE_INFINITY2_in_doubleValueLimited8864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_POSITIVE_INFINITY2_in_doubleValueLimited8883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_doubleText_in_doubleValueExtended8921 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_NOT_A_NUMBER_in_doubleValueExtended8954 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_NEGATIVE_INFINITY1_in_doubleValueExtended8979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_NEGATIVE_INFINITY2_in_doubleValueExtended8998 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_POSITIVE_INFINITY1_in_doubleValueExtended9017 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TOKEN_POSITIVE_INFINITY2_in_doubleValueExtended9036 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_idf_fieldName_in_idf9098 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_javaKeyWords_in_idf9106 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDF_TEXT_INTERNAL_in_idf_fieldName9134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_allKeyWordsIDFLike_in_idf_fieldName9142 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_idf_in_classNameText9160 = new BitSet(new long[]{0x00000000D0000002L});
	public static final BitSet FOLLOW_set_in_classNameText9163 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_classNameText9177 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOLAR_in_classNameText9188 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_classNameText9194 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_classNameText9203 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_classNameText9209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_ASTERISK_in_classNameText9219 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_classNameText9225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_idf_in_fileNameText9247 = new BitSet(new long[]{0x00000000B0000002L,0x0000180000000000L});
	public static final BitSet FOLLOW_set_in_fileNameText9251 = new BitSet(new long[]{0x03FFFBC0B0040002L,0x000008FFDDBDBFF8L});
	public static final BitSet FOLLOW_fileNameText_in_fileNameText9272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_DOT_in_fileNameText9283 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_fileNameText9285 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_BACK_SHLASH_in_fileNameText9294 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_fileNameText9296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_107_in_fileNameText9305 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_fileNameText9307 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_ASTERISK_in_fileNameText9316 = new BitSet(new long[]{0x03FFFBC0D0040002L,0x000000FFDDBDBFF8L});
	public static final BitSet FOLLOW_classNameText_in_fileNameText9319 = new BitSet(new long[]{0x0000000000000002L});
}
