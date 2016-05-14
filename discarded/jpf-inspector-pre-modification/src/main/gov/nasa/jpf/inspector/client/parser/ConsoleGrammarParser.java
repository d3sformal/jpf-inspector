// $ANTLR 3.3 Nov 30, 2010 12:45:30 D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g 2016-05-07 07:47:10
 
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


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class ConsoleGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "TOKEN_ALL", "TOKEN_ASK", "TOKEN_ASSERT", "TOKEN_ASSERTIONS", "TOKEN_BREAK", "TOKEN_BREAKPOINT", "TOKEN_CHOICE_GENERATORS", "TOKEN_CONTINUE", "TOKEN_CLEAR", "TOKEN_CREATE", "TOKEN_DATA", "TOKEN_DELETE", "TOKEN_DISABLE", "TOKEN_E", "TOKEN_ENABLE", "TOKEN_EXECUTE", "TOKEN_HIT_COUNT", "TOKEN_LOG", "TOKEN_NAME", "TOKEN_PRINT", "TOKEN_RECORD", "TOKEN_RUN", "TOKEN_RUNNING", "TOKEN_SAVE", "TOKEN_SELECT", "TOKEN_SET", "TOKEN_SCHEDULING", "TOKEN_SHOW", "TOKEN_STARTED", "TOKEN_STATE", "TOKEN_STOPPED", "TOKEN_USED", "TOKEN_TERMINATING", "TOKEN_THREAD", "TOKEN_THREAD_PC", "TOKEN_X", "TOKEN_STEP_INSTRUCTION", "TOKEN_STEP_OVER", "TOKEN_STEP_IN", "TOKEN_STEP_OUT", "TOKEN_STEP_TRANSITION", "TOKEN_BACK_STEP_INSTRUCTION", "TOKEN_BACK_STEP_OVER", "TOKEN_BACK_STEP_IN", "TOKEN_BACK_STEP_OUT", "TOKEN_BACK_STEP_TRANSITION", "TOKEN_CB_STATE_CHANGE", "TOKEN_CB_GENERIC_ERROR", "TOKEN_CB_GENERIC_INFO", "TOKEN_CB_BREAKPOINT_HIT", "TOKEN_CB_CG_NEW_CHOICE", "TOKEN_CB_CG_CHOICE_TO_USE", "TOKEN_CB_CG_USED_CHOICE", "IDF", "WS", "INT", "SIGN_EQUAL", "LESS", "HIGH", "SIGN_PLUS", "SIGN_MINUS", "HEX", "SIGN_DOLLAR", "SIGN_DOT", "SIGN_HASH", "SIGN_COLON", "SIGN_LSBRA", "SIGN_RSBRA", "SIGN_ASTERISK", "SIGN_SLASH", "SIGN_BACKSLASH", "SPECIAL_CHAR"
    };
    public static final int EOF=-1;
    public static final int TOKEN_ALL=4;
    public static final int TOKEN_ASK=5;
    public static final int TOKEN_ASSERT=6;
    public static final int TOKEN_ASSERTIONS=7;
    public static final int TOKEN_BREAK=8;
    public static final int TOKEN_BREAKPOINT=9;
    public static final int TOKEN_CHOICE_GENERATORS=10;
    public static final int TOKEN_CONTINUE=11;
    public static final int TOKEN_CLEAR=12;
    public static final int TOKEN_CREATE=13;
    public static final int TOKEN_DATA=14;
    public static final int TOKEN_DELETE=15;
    public static final int TOKEN_DISABLE=16;
    public static final int TOKEN_E=17;
    public static final int TOKEN_ENABLE=18;
    public static final int TOKEN_EXECUTE=19;
    public static final int TOKEN_HIT_COUNT=20;
    public static final int TOKEN_LOG=21;
    public static final int TOKEN_NAME=22;
    public static final int TOKEN_PRINT=23;
    public static final int TOKEN_RECORD=24;
    public static final int TOKEN_RUN=25;
    public static final int TOKEN_RUNNING=26;
    public static final int TOKEN_SAVE=27;
    public static final int TOKEN_SELECT=28;
    public static final int TOKEN_SET=29;
    public static final int TOKEN_SCHEDULING=30;
    public static final int TOKEN_SHOW=31;
    public static final int TOKEN_STARTED=32;
    public static final int TOKEN_STATE=33;
    public static final int TOKEN_STOPPED=34;
    public static final int TOKEN_USED=35;
    public static final int TOKEN_TERMINATING=36;
    public static final int TOKEN_THREAD=37;
    public static final int TOKEN_THREAD_PC=38;
    public static final int TOKEN_X=39;
    public static final int TOKEN_STEP_INSTRUCTION=40;
    public static final int TOKEN_STEP_OVER=41;
    public static final int TOKEN_STEP_IN=42;
    public static final int TOKEN_STEP_OUT=43;
    public static final int TOKEN_STEP_TRANSITION=44;
    public static final int TOKEN_BACK_STEP_INSTRUCTION=45;
    public static final int TOKEN_BACK_STEP_OVER=46;
    public static final int TOKEN_BACK_STEP_IN=47;
    public static final int TOKEN_BACK_STEP_OUT=48;
    public static final int TOKEN_BACK_STEP_TRANSITION=49;
    public static final int TOKEN_CB_STATE_CHANGE=50;
    public static final int TOKEN_CB_GENERIC_ERROR=51;
    public static final int TOKEN_CB_GENERIC_INFO=52;
    public static final int TOKEN_CB_BREAKPOINT_HIT=53;
    public static final int TOKEN_CB_CG_NEW_CHOICE=54;
    public static final int TOKEN_CB_CG_CHOICE_TO_USE=55;
    public static final int TOKEN_CB_CG_USED_CHOICE=56;
    public static final int IDF=57;
    public static final int WS=58;
    public static final int INT=59;
    public static final int SIGN_EQUAL=60;
    public static final int LESS=61;
    public static final int HIGH=62;
    public static final int SIGN_PLUS=63;
    public static final int SIGN_MINUS=64;
    public static final int HEX=65;
    public static final int SIGN_DOLLAR=66;
    public static final int SIGN_DOT=67;
    public static final int SIGN_HASH=68;
    public static final int SIGN_COLON=69;
    public static final int SIGN_LSBRA=70;
    public static final int SIGN_RSBRA=71;
    public static final int SIGN_ASTERISK=72;
    public static final int SIGN_SLASH=73;
    public static final int SIGN_BACKSLASH=74;
    public static final int SPECIAL_CHAR=75;

    // delegates
    // delegators


        public ConsoleGrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ConsoleGrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return ConsoleGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g"; }



        public void displayRecognitionError(String[] tokenNames,
                                            RecognitionException e) {
            String hdr = getErrorHeader(e);
            String msg = getErrorMessage(e, tokenNames); 
            throw new RecognitionRuntimeException(hdr + " " + msg, e);
        }




    // $ANTLR start "anyWord"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:114:1: anyWord returns [String text] : ( IDF | allKeyWords );
    public final String anyWord() throws RecognitionException {
        String text = null;

        Token IDF1=null;
        String allKeyWords2 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:115:5: ( IDF | allKeyWords )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==IDF) ) {
                alt1=1;
            }
            else if ( ((LA1_0>=TOKEN_ALL && LA1_0<=TOKEN_CB_CG_USED_CHOICE)) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:115:7: IDF
                    {
                    IDF1=(Token)match(input,IDF,FOLLOW_IDF_in_anyWord639); 
                     text = (IDF1!=null?IDF1.getText():null); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:116:7: allKeyWords
                    {
                    pushFollow(FOLLOW_allKeyWords_in_anyWord675);
                    allKeyWords2=allKeyWords();

                    state._fsp--;

                     text = allKeyWords2; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return text;
    }
    // $ANTLR end "anyWord"


    // $ANTLR start "allKeyWords"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:119:1: allKeyWords returns [String text] : (a= TOKEN_HIT_COUNT | allKeyWordsWithoutHitCountBP );
    public final String allKeyWords() throws RecognitionException {
        String text = null;

        Token a=null;
        String allKeyWordsWithoutHitCountBP3 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:120:5: (a= TOKEN_HIT_COUNT | allKeyWordsWithoutHitCountBP )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==TOKEN_HIT_COUNT) ) {
                alt2=1;
            }
            else if ( ((LA2_0>=TOKEN_ALL && LA2_0<=TOKEN_EXECUTE)||(LA2_0>=TOKEN_LOG && LA2_0<=TOKEN_CB_CG_USED_CHOICE)) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:120:7: a= TOKEN_HIT_COUNT
                    {
                    a=(Token)match(input,TOKEN_HIT_COUNT,FOLLOW_TOKEN_HIT_COUNT_in_allKeyWords722); 
                     text = (a!=null?a.getText():null); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:121:7: allKeyWordsWithoutHitCountBP
                    {
                    pushFollow(FOLLOW_allKeyWordsWithoutHitCountBP_in_allKeyWords744);
                    allKeyWordsWithoutHitCountBP3=allKeyWordsWithoutHitCountBP();

                    state._fsp--;

                     text = allKeyWordsWithoutHitCountBP3; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return text;
    }
    // $ANTLR end "allKeyWords"


    // $ANTLR start "allKeyWordsWithoutHitCountBP"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:124:1: allKeyWordsWithoutHitCountBP returns [String text] : (a= TOKEN_NAME | a= TOKEN_STATE | allKeyWordsWithoutCreateBPandHitCount );
    public final String allKeyWordsWithoutHitCountBP() throws RecognitionException {
        String text = null;

        Token a=null;
        String allKeyWordsWithoutCreateBPandHitCount4 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:125:5: (a= TOKEN_NAME | a= TOKEN_STATE | allKeyWordsWithoutCreateBPandHitCount )
            int alt3=3;
            switch ( input.LA(1) ) {
            case TOKEN_NAME:
                {
                alt3=1;
                }
                break;
            case TOKEN_STATE:
                {
                alt3=2;
                }
                break;
            case TOKEN_ALL:
            case TOKEN_ASK:
            case TOKEN_ASSERT:
            case TOKEN_ASSERTIONS:
            case TOKEN_BREAK:
            case TOKEN_BREAKPOINT:
            case TOKEN_CHOICE_GENERATORS:
            case TOKEN_CONTINUE:
            case TOKEN_CLEAR:
            case TOKEN_CREATE:
            case TOKEN_DATA:
            case TOKEN_DELETE:
            case TOKEN_DISABLE:
            case TOKEN_E:
            case TOKEN_ENABLE:
            case TOKEN_EXECUTE:
            case TOKEN_LOG:
            case TOKEN_PRINT:
            case TOKEN_RECORD:
            case TOKEN_RUN:
            case TOKEN_RUNNING:
            case TOKEN_SAVE:
            case TOKEN_SELECT:
            case TOKEN_SET:
            case TOKEN_SCHEDULING:
            case TOKEN_SHOW:
            case TOKEN_STARTED:
            case TOKEN_STOPPED:
            case TOKEN_USED:
            case TOKEN_TERMINATING:
            case TOKEN_THREAD:
            case TOKEN_THREAD_PC:
            case TOKEN_X:
            case TOKEN_STEP_INSTRUCTION:
            case TOKEN_STEP_OVER:
            case TOKEN_STEP_IN:
            case TOKEN_STEP_OUT:
            case TOKEN_STEP_TRANSITION:
            case TOKEN_BACK_STEP_INSTRUCTION:
            case TOKEN_BACK_STEP_OVER:
            case TOKEN_BACK_STEP_IN:
            case TOKEN_BACK_STEP_OUT:
            case TOKEN_BACK_STEP_TRANSITION:
            case TOKEN_CB_STATE_CHANGE:
            case TOKEN_CB_GENERIC_ERROR:
            case TOKEN_CB_GENERIC_INFO:
            case TOKEN_CB_BREAKPOINT_HIT:
            case TOKEN_CB_CG_NEW_CHOICE:
            case TOKEN_CB_CG_CHOICE_TO_USE:
            case TOKEN_CB_CG_USED_CHOICE:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:125:7: a= TOKEN_NAME
                    {
                    a=(Token)match(input,TOKEN_NAME,FOLLOW_TOKEN_NAME_in_allKeyWordsWithoutHitCountBP770); 
                     text = (a!=null?a.getText():null); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:126:7: a= TOKEN_STATE
                    {
                    a=(Token)match(input,TOKEN_STATE,FOLLOW_TOKEN_STATE_in_allKeyWordsWithoutHitCountBP799); 
                     text = (a!=null?a.getText():null); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:127:7: allKeyWordsWithoutCreateBPandHitCount
                    {
                    pushFollow(FOLLOW_allKeyWordsWithoutCreateBPandHitCount_in_allKeyWordsWithoutHitCountBP825);
                    allKeyWordsWithoutCreateBPandHitCount4=allKeyWordsWithoutCreateBPandHitCount();

                    state._fsp--;

                     text = allKeyWordsWithoutCreateBPandHitCount4; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return text;
    }
    // $ANTLR end "allKeyWordsWithoutHitCountBP"


    // $ANTLR start "allKeyWordsWithoutCreateBPandHitCount"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:130:1: allKeyWordsWithoutCreateBPandHitCount returns [String text] : ( TOKEN_ALL | TOKEN_ASK | TOKEN_ASSERT | TOKEN_ASSERTIONS | TOKEN_BREAK | TOKEN_BREAKPOINT | TOKEN_CHOICE_GENERATORS | TOKEN_CONTINUE | TOKEN_CLEAR | TOKEN_CREATE | TOKEN_DATA | TOKEN_DELETE | TOKEN_DISABLE | TOKEN_E | TOKEN_ENABLE | TOKEN_EXECUTE | TOKEN_LOG | TOKEN_PRINT | TOKEN_RECORD | TOKEN_RUN | TOKEN_RUNNING | TOKEN_SAVE | TOKEN_SET | TOKEN_SELECT | TOKEN_SCHEDULING | TOKEN_SHOW | TOKEN_STARTED | TOKEN_STOPPED | TOKEN_USED | TOKEN_TERMINATING | TOKEN_THREAD | TOKEN_THREAD_PC | TOKEN_X | TOKEN_STEP_INSTRUCTION | TOKEN_STEP_OVER | TOKEN_STEP_IN | TOKEN_STEP_OUT | TOKEN_STEP_TRANSITION | TOKEN_BACK_STEP_INSTRUCTION | TOKEN_BACK_STEP_OVER | TOKEN_BACK_STEP_IN | TOKEN_BACK_STEP_OUT | TOKEN_BACK_STEP_TRANSITION | TOKEN_CB_STATE_CHANGE | TOKEN_CB_GENERIC_ERROR | TOKEN_CB_GENERIC_INFO | TOKEN_CB_BREAKPOINT_HIT | TOKEN_CB_CG_NEW_CHOICE | TOKEN_CB_CG_CHOICE_TO_USE | TOKEN_CB_CG_USED_CHOICE );
    public final String allKeyWordsWithoutCreateBPandHitCount() throws RecognitionException {
        String text = null;

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:131:5: ( TOKEN_ALL | TOKEN_ASK | TOKEN_ASSERT | TOKEN_ASSERTIONS | TOKEN_BREAK | TOKEN_BREAKPOINT | TOKEN_CHOICE_GENERATORS | TOKEN_CONTINUE | TOKEN_CLEAR | TOKEN_CREATE | TOKEN_DATA | TOKEN_DELETE | TOKEN_DISABLE | TOKEN_E | TOKEN_ENABLE | TOKEN_EXECUTE | TOKEN_LOG | TOKEN_PRINT | TOKEN_RECORD | TOKEN_RUN | TOKEN_RUNNING | TOKEN_SAVE | TOKEN_SET | TOKEN_SELECT | TOKEN_SCHEDULING | TOKEN_SHOW | TOKEN_STARTED | TOKEN_STOPPED | TOKEN_USED | TOKEN_TERMINATING | TOKEN_THREAD | TOKEN_THREAD_PC | TOKEN_X | TOKEN_STEP_INSTRUCTION | TOKEN_STEP_OVER | TOKEN_STEP_IN | TOKEN_STEP_OUT | TOKEN_STEP_TRANSITION | TOKEN_BACK_STEP_INSTRUCTION | TOKEN_BACK_STEP_OVER | TOKEN_BACK_STEP_IN | TOKEN_BACK_STEP_OUT | TOKEN_BACK_STEP_TRANSITION | TOKEN_CB_STATE_CHANGE | TOKEN_CB_GENERIC_ERROR | TOKEN_CB_GENERIC_INFO | TOKEN_CB_BREAKPOINT_HIT | TOKEN_CB_CG_NEW_CHOICE | TOKEN_CB_CG_CHOICE_TO_USE | TOKEN_CB_CG_USED_CHOICE )
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:
            {
            if ( (input.LA(1)>=TOKEN_ALL && input.LA(1)<=TOKEN_EXECUTE)||input.LA(1)==TOKEN_LOG||(input.LA(1)>=TOKEN_PRINT && input.LA(1)<=TOKEN_STARTED)||(input.LA(1)>=TOKEN_STOPPED && input.LA(1)<=TOKEN_CB_CG_USED_CHOICE) ) {
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
        }
        return text;
    }
    // $ANTLR end "allKeyWordsWithoutCreateBPandHitCount"


    // $ANTLR start "clientCommandWithCB"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:184:1: clientCommandWithCB returns [ClientCommand value] : ( clientCommands EOF | ( WS )? cmdCallback ( WS )? );
    public final ClientCommand clientCommandWithCB() throws RecognitionException {
        ClientCommand value = null;

        ClientCommand clientCommands5 = null;

        ClientCommand cmdCallback6 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:185:5: ( clientCommands EOF | ( WS )? cmdCallback ( WS )? )
            int alt6=2;
            switch ( input.LA(1) ) {
            case WS:
                {
                int LA6_1 = input.LA(2);

                if ( ((LA6_1>=TOKEN_ASSERT && LA6_1<=TOKEN_BREAK)||(LA6_1>=TOKEN_CHOICE_GENERATORS && LA6_1<=TOKEN_CONTINUE)||LA6_1==TOKEN_CREATE||(LA6_1>=TOKEN_DELETE && LA6_1<=TOKEN_DISABLE)||LA6_1==TOKEN_ENABLE||(LA6_1>=TOKEN_PRINT && LA6_1<=TOKEN_RUN)||LA6_1==TOKEN_SET||LA6_1==TOKEN_SHOW||LA6_1==TOKEN_USED||(LA6_1>=TOKEN_THREAD && LA6_1<=TOKEN_THREAD_PC)||(LA6_1>=TOKEN_STEP_INSTRUCTION && LA6_1<=TOKEN_BACK_STEP_TRANSITION)) ) {
                    alt6=1;
                }
                else if ( ((LA6_1>=TOKEN_CB_STATE_CHANGE && LA6_1<=TOKEN_CB_CG_USED_CHOICE)) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }
                }
                break;
            case TOKEN_ASSERT:
            case TOKEN_ASSERTIONS:
            case TOKEN_BREAK:
            case TOKEN_CHOICE_GENERATORS:
            case TOKEN_CONTINUE:
            case TOKEN_CREATE:
            case TOKEN_DELETE:
            case TOKEN_DISABLE:
            case TOKEN_ENABLE:
            case TOKEN_PRINT:
            case TOKEN_RECORD:
            case TOKEN_RUN:
            case TOKEN_SET:
            case TOKEN_SHOW:
            case TOKEN_USED:
            case TOKEN_THREAD:
            case TOKEN_THREAD_PC:
            case TOKEN_STEP_INSTRUCTION:
            case TOKEN_STEP_OVER:
            case TOKEN_STEP_IN:
            case TOKEN_STEP_OUT:
            case TOKEN_STEP_TRANSITION:
            case TOKEN_BACK_STEP_INSTRUCTION:
            case TOKEN_BACK_STEP_OVER:
            case TOKEN_BACK_STEP_IN:
            case TOKEN_BACK_STEP_OUT:
            case TOKEN_BACK_STEP_TRANSITION:
                {
                alt6=1;
                }
                break;
            case TOKEN_CB_STATE_CHANGE:
            case TOKEN_CB_GENERIC_ERROR:
            case TOKEN_CB_GENERIC_INFO:
            case TOKEN_CB_BREAKPOINT_HIT:
            case TOKEN_CB_CG_NEW_CHOICE:
            case TOKEN_CB_CG_CHOICE_TO_USE:
            case TOKEN_CB_CG_USED_CHOICE:
                {
                alt6=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:185:7: clientCommands EOF
                    {
                    pushFollow(FOLLOW_clientCommands_in_clientCommandWithCB1262);
                    clientCommands5=clientCommands();

                    state._fsp--;

                     value = clientCommands5; 
                    match(input,EOF,FOLLOW_EOF_in_clientCommandWithCB1266); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:186:7: ( WS )? cmdCallback ( WS )?
                    {
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:186:7: ( WS )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==WS) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:186:7: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommandWithCB1274); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_cmdCallback_in_clientCommandWithCB1277);
                    cmdCallback6=cmdCallback();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:186:23: ( WS )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==WS) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:186:23: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommandWithCB1279); 

                            }
                            break;

                    }

                     value = cmdCallback6; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "clientCommandWithCB"


    // $ANTLR start "clientCommands"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:189:1: clientCommands returns [ClientCommand value] : ( WS )? clientCommands1 EOF ;
    public final ClientCommand clientCommands() throws RecognitionException {
        ClientCommand value = null;

        ClientCommand clientCommands17 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:190:5: ( ( WS )? clientCommands1 EOF )
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:190:7: ( WS )? clientCommands1 EOF
            {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:190:7: ( WS )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==WS) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:190:7: WS
                    {
                    match(input,WS,FOLLOW_WS_in_clientCommands1303); 

                    }
                    break;

            }

            pushFollow(FOLLOW_clientCommands1_in_clientCommands1306);
            clientCommands17=clientCommands1();

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_clientCommands1308); 
             value = clientCommands17; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "clientCommands"


    // $ANTLR start "clientCommands1"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:193:1: clientCommands1 returns [ClientCommand value] : ( TOKEN_RUN ( WS )? | TOKEN_CONTINUE ( WS )? | TOKEN_BREAK ( WS )? | cmdBreakpoints ( WS )? | cmdSingleSteps ( WS )? | cmdProgramState ( WS )? | cmdChoiceGenerators ( WS )? | cmdRecord ( WS )? | cmdAssertions ( WS )? );
    public final ClientCommand clientCommands1() throws RecognitionException {
        ClientCommand value = null;

        ClientCommand cmdBreakpoints8 = null;

        CmdSingleStepping cmdSingleSteps9 = null;

        ClientCommand cmdProgramState10 = null;

        ClientCommand cmdChoiceGenerators11 = null;

        ClientCommand cmdRecord12 = null;

        ClientCommand cmdAssertions13 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:194:5: ( TOKEN_RUN ( WS )? | TOKEN_CONTINUE ( WS )? | TOKEN_BREAK ( WS )? | cmdBreakpoints ( WS )? | cmdSingleSteps ( WS )? | cmdProgramState ( WS )? | cmdChoiceGenerators ( WS )? | cmdRecord ( WS )? | cmdAssertions ( WS )? )
            int alt17=9;
            switch ( input.LA(1) ) {
            case TOKEN_RUN:
                {
                alt17=1;
                }
                break;
            case TOKEN_CONTINUE:
                {
                alt17=2;
                }
                break;
            case TOKEN_BREAK:
                {
                alt17=3;
                }
                break;
            case TOKEN_CREATE:
            case TOKEN_DELETE:
            case TOKEN_SHOW:
                {
                alt17=4;
                }
                break;
            case TOKEN_STEP_INSTRUCTION:
            case TOKEN_STEP_OVER:
            case TOKEN_STEP_IN:
            case TOKEN_STEP_OUT:
            case TOKEN_STEP_TRANSITION:
            case TOKEN_BACK_STEP_INSTRUCTION:
            case TOKEN_BACK_STEP_OVER:
            case TOKEN_BACK_STEP_IN:
            case TOKEN_BACK_STEP_OUT:
            case TOKEN_BACK_STEP_TRANSITION:
                {
                alt17=5;
                }
                break;
            case TOKEN_PRINT:
            case TOKEN_SET:
            case TOKEN_THREAD:
            case TOKEN_THREAD_PC:
                {
                alt17=6;
                }
                break;
            case TOKEN_CHOICE_GENERATORS:
            case TOKEN_DISABLE:
            case TOKEN_ENABLE:
            case TOKEN_USED:
                {
                alt17=7;
                }
                break;
            case TOKEN_RECORD:
                {
                alt17=8;
                }
                break;
            case TOKEN_ASSERT:
            case TOKEN_ASSERTIONS:
                {
                alt17=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:194:7: TOKEN_RUN ( WS )?
                    {
                    match(input,TOKEN_RUN,FOLLOW_TOKEN_RUN_in_clientCommands11331); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:194:30: ( WS )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==WS) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:194:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11346); 

                            }
                            break;

                    }

                     value = new CmdRun(CmdRunTypes.RUN); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:195:7: TOKEN_CONTINUE ( WS )?
                    {
                    match(input,TOKEN_CONTINUE,FOLLOW_TOKEN_CONTINUE_in_clientCommands11357); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:195:30: ( WS )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==WS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:195:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11367); 

                            }
                            break;

                    }

                     value = new CmdRun(CmdRunTypes.RUN); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:196:7: TOKEN_BREAK ( WS )?
                    {
                    match(input,TOKEN_BREAK,FOLLOW_TOKEN_BREAK_in_clientCommands11378); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:196:30: ( WS )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==WS) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:196:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11391); 

                            }
                            break;

                    }

                     value = new CmdRun(CmdRunTypes.STOP); 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:197:7: cmdBreakpoints ( WS )?
                    {
                    pushFollow(FOLLOW_cmdBreakpoints_in_clientCommands11402);
                    cmdBreakpoints8=cmdBreakpoints();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:197:30: ( WS )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==WS) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:197:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11412); 

                            }
                            break;

                    }

                     value = cmdBreakpoints8; 

                    }
                    break;
                case 5 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:198:7: cmdSingleSteps ( WS )?
                    {
                    pushFollow(FOLLOW_cmdSingleSteps_in_clientCommands11423);
                    cmdSingleSteps9=cmdSingleSteps();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:198:30: ( WS )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==WS) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:198:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11433); 

                            }
                            break;

                    }

                     value = cmdSingleSteps9; 

                    }
                    break;
                case 6 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:199:7: cmdProgramState ( WS )?
                    {
                    pushFollow(FOLLOW_cmdProgramState_in_clientCommands11444);
                    cmdProgramState10=cmdProgramState();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:199:30: ( WS )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==WS) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:199:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11453); 

                            }
                            break;

                    }

                     value = cmdProgramState10; 

                    }
                    break;
                case 7 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:200:7: cmdChoiceGenerators ( WS )?
                    {
                    pushFollow(FOLLOW_cmdChoiceGenerators_in_clientCommands11464);
                    cmdChoiceGenerators11=cmdChoiceGenerators();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:200:30: ( WS )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==WS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:200:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11469); 

                            }
                            break;

                    }

                     value = cmdChoiceGenerators11; 

                    }
                    break;
                case 8 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:201:7: cmdRecord ( WS )?
                    {
                    pushFollow(FOLLOW_cmdRecord_in_clientCommands11480);
                    cmdRecord12=cmdRecord();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:201:30: ( WS )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==WS) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:201:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11495); 

                            }
                            break;

                    }

                     value = cmdRecord12; 

                    }
                    break;
                case 9 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:202:7: cmdAssertions ( WS )?
                    {
                    pushFollow(FOLLOW_cmdAssertions_in_clientCommands11506);
                    cmdAssertions13=cmdAssertions();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:202:30: ( WS )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==WS) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:202:30: WS
                            {
                            match(input,WS,FOLLOW_WS_in_clientCommands11517); 

                            }
                            break;

                    }

                     value = cmdAssertions13; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "clientCommands1"


    // $ANTLR start "cmdBreakpoints"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:205:1: cmdBreakpoints returns [ClientCommand value] : ( TOKEN_SHOW ( WS )? TOKEN_BREAKPOINT | TOKEN_DELETE ( WS )? TOKEN_BREAKPOINT ( WS )? INT | TOKEN_CREATE ( WS )? TOKEN_BREAKPOINT ( ( WS )? cmdCreateBP[bpCreate] )* WS bpExpression );
    public final ClientCommand cmdBreakpoints() throws RecognitionException {
        ClientCommand value = null;

        Token INT14=null;
        String bpExpression15 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:206:5: ( TOKEN_SHOW ( WS )? TOKEN_BREAKPOINT | TOKEN_DELETE ( WS )? TOKEN_BREAKPOINT ( WS )? INT | TOKEN_CREATE ( WS )? TOKEN_BREAKPOINT ( ( WS )? cmdCreateBP[bpCreate] )* WS bpExpression )
            int alt24=3;
            switch ( input.LA(1) ) {
            case TOKEN_SHOW:
                {
                alt24=1;
                }
                break;
            case TOKEN_DELETE:
                {
                alt24=2;
                }
                break;
            case TOKEN_CREATE:
                {
                alt24=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:206:7: TOKEN_SHOW ( WS )? TOKEN_BREAKPOINT
                    {
                    match(input,TOKEN_SHOW,FOLLOW_TOKEN_SHOW_in_cmdBreakpoints1542); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:206:20: ( WS )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==WS) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:206:20: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdBreakpoints1546); 

                            }
                            break;

                    }

                    match(input,TOKEN_BREAKPOINT,FOLLOW_TOKEN_BREAKPOINT_in_cmdBreakpoints1549); 
                     value = new CmdBreakpointShow(); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:207:7: TOKEN_DELETE ( WS )? TOKEN_BREAKPOINT ( WS )? INT
                    {
                    match(input,TOKEN_DELETE,FOLLOW_TOKEN_DELETE_in_cmdBreakpoints1571); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:207:20: ( WS )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==WS) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:207:20: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdBreakpoints1573); 

                            }
                            break;

                    }

                    match(input,TOKEN_BREAKPOINT,FOLLOW_TOKEN_BREAKPOINT_in_cmdBreakpoints1576); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:207:41: ( WS )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==WS) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:207:41: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdBreakpoints1578); 

                            }
                            break;

                    }

                    INT14=(Token)match(input,INT,FOLLOW_INT_in_cmdBreakpoints1581); 
                     value = new CmdBreakpointDelete((INT14!=null?INT14.getText():null)); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:208:7: TOKEN_CREATE ( WS )? TOKEN_BREAKPOINT ( ( WS )? cmdCreateBP[bpCreate] )* WS bpExpression
                    {
                    match(input,TOKEN_CREATE,FOLLOW_TOKEN_CREATE_in_cmdBreakpoints1595); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:208:20: ( WS )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==WS) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:208:20: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdBreakpoints1597); 

                            }
                            break;

                    }

                    match(input,TOKEN_BREAKPOINT,FOLLOW_TOKEN_BREAKPOINT_in_cmdBreakpoints1600); 
                     ConsoleBreakpointCreate  bpCreate = new ConsoleBreakpointCreate(); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:208:124: ( ( WS )? cmdCreateBP[bpCreate] )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==WS) ) {
                            int LA23_1 = input.LA(2);

                            if ( (LA23_1==TOKEN_HIT_COUNT||LA23_1==TOKEN_NAME||LA23_1==TOKEN_STATE||LA23_1==INT||(LA23_1>=SIGN_PLUS && LA23_1<=HEX)) ) {
                                alt23=1;
                            }


                        }
                        else if ( (LA23_0==TOKEN_HIT_COUNT||LA23_0==TOKEN_NAME||LA23_0==TOKEN_STATE||LA23_0==INT||(LA23_0>=SIGN_PLUS && LA23_0<=HEX)) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:208:126: ( WS )? cmdCreateBP[bpCreate]
                    	    {
                    	    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:208:126: ( WS )?
                    	    int alt22=2;
                    	    int LA22_0 = input.LA(1);

                    	    if ( (LA22_0==WS) ) {
                    	        alt22=1;
                    	    }
                    	    switch (alt22) {
                    	        case 1 :
                    	            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:208:126: WS
                    	            {
                    	            match(input,WS,FOLLOW_WS_in_cmdBreakpoints1618); 

                    	            }
                    	            break;

                    	    }

                    	    pushFollow(FOLLOW_cmdCreateBP_in_cmdBreakpoints1621);
                    	    cmdCreateBP(bpCreate);

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop23;
                        }
                    } while (true);

                    match(input,WS,FOLLOW_WS_in_cmdBreakpoints1626); 
                    pushFollow(FOLLOW_bpExpression_in_cmdBreakpoints1628);
                    bpExpression15=bpExpression();

                    state._fsp--;

                     bpCreate.setBPExpression(bpExpression15); value = new CmdBreakpointCreate(bpCreate); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "cmdBreakpoints"


    // $ANTLR start "cmdCreateBP"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:211:1: cmdCreateBP[ConsoleBreakpointCreate bpCreate] : ( TOKEN_NAME ( WS )? SIGN_EQUAL ( WS )? anyWord | TOKEN_STATE ( WS )? SIGN_EQUAL ( WS )? cmdBreakpointsStates | (lower= intValue ( WS )? signLess= LESS ( WS )? )? TOKEN_HIT_COUNT ( ( WS )? signHigh= LESS ( WS )? upper= intValue )? );
    public final void cmdCreateBP(ConsoleBreakpointCreate bpCreate) throws RecognitionException {
        Token signLess=null;
        Token signHigh=null;
        Integer lower = null;

        Integer upper = null;

        String anyWord16 = null;

        BreakPointStates cmdBreakpointsStates17 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:212:5: ( TOKEN_NAME ( WS )? SIGN_EQUAL ( WS )? anyWord | TOKEN_STATE ( WS )? SIGN_EQUAL ( WS )? cmdBreakpointsStates | (lower= intValue ( WS )? signLess= LESS ( WS )? )? TOKEN_HIT_COUNT ( ( WS )? signHigh= LESS ( WS )? upper= intValue )? )
            int alt35=3;
            switch ( input.LA(1) ) {
            case TOKEN_NAME:
                {
                alt35=1;
                }
                break;
            case TOKEN_STATE:
                {
                alt35=2;
                }
                break;
            case TOKEN_HIT_COUNT:
            case INT:
            case SIGN_PLUS:
            case SIGN_MINUS:
            case HEX:
                {
                alt35=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }

            switch (alt35) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:212:7: TOKEN_NAME ( WS )? SIGN_EQUAL ( WS )? anyWord
                    {
                    match(input,TOKEN_NAME,FOLLOW_TOKEN_NAME_in_cmdCreateBP1649); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:212:19: ( WS )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==WS) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:212:19: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdCreateBP1652); 

                            }
                            break;

                    }

                    match(input,SIGN_EQUAL,FOLLOW_SIGN_EQUAL_in_cmdCreateBP1655); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:212:34: ( WS )?
                    int alt26=2;
                    int LA26_0 = input.LA(1);

                    if ( (LA26_0==WS) ) {
                        alt26=1;
                    }
                    switch (alt26) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:212:34: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdCreateBP1657); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_anyWord_in_cmdCreateBP1660);
                    anyWord16=anyWord();

                    state._fsp--;

                     bpCreate.setName(anyWord16); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:213:7: TOKEN_STATE ( WS )? SIGN_EQUAL ( WS )? cmdBreakpointsStates
                    {
                    match(input,TOKEN_STATE,FOLLOW_TOKEN_STATE_in_cmdCreateBP1670); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:213:19: ( WS )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==WS) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:213:19: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdCreateBP1672); 

                            }
                            break;

                    }

                    match(input,SIGN_EQUAL,FOLLOW_SIGN_EQUAL_in_cmdCreateBP1675); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:213:34: ( WS )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==WS) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:213:34: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdCreateBP1677); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_cmdBreakpointsStates_in_cmdCreateBP1680);
                    cmdBreakpointsStates17=cmdBreakpointsStates();

                    state._fsp--;

                     bpCreate.setState(cmdBreakpointsStates17); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:7: (lower= intValue ( WS )? signLess= LESS ( WS )? )? TOKEN_HIT_COUNT ( ( WS )? signHigh= LESS ( WS )? upper= intValue )?
                    {
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:7: (lower= intValue ( WS )? signLess= LESS ( WS )? )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==INT||(LA31_0>=SIGN_PLUS && LA31_0<=HEX)) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:8: lower= intValue ( WS )? signLess= LESS ( WS )?
                            {
                            pushFollow(FOLLOW_intValue_in_cmdCreateBP1693);
                            lower=intValue();

                            state._fsp--;

                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:24: ( WS )?
                            int alt29=2;
                            int LA29_0 = input.LA(1);

                            if ( (LA29_0==WS) ) {
                                alt29=1;
                            }
                            switch (alt29) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:24: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdCreateBP1696); 

                                    }
                                    break;

                            }

                            signLess=(Token)match(input,LESS,FOLLOW_LESS_in_cmdCreateBP1701); 
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:42: ( WS )?
                            int alt30=2;
                            int LA30_0 = input.LA(1);

                            if ( (LA30_0==WS) ) {
                                alt30=1;
                            }
                            switch (alt30) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:42: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdCreateBP1703); 

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    match(input,TOKEN_HIT_COUNT,FOLLOW_TOKEN_HIT_COUNT_in_cmdCreateBP1708); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:64: ( ( WS )? signHigh= LESS ( WS )? upper= intValue )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==WS) ) {
                        int LA34_1 = input.LA(2);

                        if ( (LA34_1==LESS) ) {
                            alt34=1;
                        }
                    }
                    else if ( (LA34_0==LESS) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:65: ( WS )? signHigh= LESS ( WS )? upper= intValue
                            {
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:65: ( WS )?
                            int alt32=2;
                            int LA32_0 = input.LA(1);

                            if ( (LA32_0==WS) ) {
                                alt32=1;
                            }
                            switch (alt32) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:65: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdCreateBP1711); 

                                    }
                                    break;

                            }

                            signHigh=(Token)match(input,LESS,FOLLOW_LESS_in_cmdCreateBP1716); 
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:83: ( WS )?
                            int alt33=2;
                            int LA33_0 = input.LA(1);

                            if ( (LA33_0==WS) ) {
                                alt33=1;
                            }
                            switch (alt33) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:214:83: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdCreateBP1718); 

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_intValue_in_cmdCreateBP1723);
                            upper=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     bpCreate.setBounds( lower, (signLess!=null?(signLess!=null?signLess.getText():null):null), (signHigh!=null?(signHigh!=null?signHigh.getText():null):null), upper); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "cmdCreateBP"


    // $ANTLR start "cmdBreakpointsStates"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:217:1: cmdBreakpointsStates returns [BreakPointStates bpState] : ( TOKEN_DISABLE | TOKEN_LOG | TOKEN_ENABLE );
    public final BreakPointStates cmdBreakpointsStates() throws RecognitionException {
        BreakPointStates bpState = null;

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:218:5: ( TOKEN_DISABLE | TOKEN_LOG | TOKEN_ENABLE )
            int alt36=3;
            switch ( input.LA(1) ) {
            case TOKEN_DISABLE:
                {
                alt36=1;
                }
                break;
            case TOKEN_LOG:
                {
                alt36=2;
                }
                break;
            case TOKEN_ENABLE:
                {
                alt36=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }

            switch (alt36) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:218:7: TOKEN_DISABLE
                    {
                    match(input,TOKEN_DISABLE,FOLLOW_TOKEN_DISABLE_in_cmdBreakpointsStates1748); 
                     bpState = BreakPointStates.BP_STATE_DISABLED; 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:219:7: TOKEN_LOG
                    {
                    match(input,TOKEN_LOG,FOLLOW_TOKEN_LOG_in_cmdBreakpointsStates1766); 
                     bpState = BreakPointStates.BP_STATE_LOGGING; 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:220:7: TOKEN_ENABLE
                    {
                    match(input,TOKEN_ENABLE,FOLLOW_TOKEN_ENABLE_in_cmdBreakpointsStates1788); 
                     bpState = BreakPointStates.BP_STATE_ENABLED; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return bpState;
    }
    // $ANTLR end "cmdBreakpointsStates"


    // $ANTLR start "bpExpression"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:226:1: bpExpression returns [String expr] : ( allKeyWordsWithoutCreateBPandHitCount (b01= allTextWS )? | allNonKeywordsRulesBase (b02= allTextWS )? | IDF (b03= allTextWS )? );
    public final String bpExpression() throws RecognitionException {
        String expr = null;

        Token IDF20=null;
        ConsoleGrammarParser.allTextWS_return b01 = null;

        ConsoleGrammarParser.allTextWS_return b02 = null;

        ConsoleGrammarParser.allTextWS_return b03 = null;

        String allKeyWordsWithoutCreateBPandHitCount18 = null;

        ConsoleGrammarParser.allNonKeywordsRulesBase_return allNonKeywordsRulesBase19 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:227:5: ( allKeyWordsWithoutCreateBPandHitCount (b01= allTextWS )? | allNonKeywordsRulesBase (b02= allTextWS )? | IDF (b03= allTextWS )? )
            int alt40=3;
            switch ( input.LA(1) ) {
            case TOKEN_ALL:
            case TOKEN_ASK:
            case TOKEN_ASSERT:
            case TOKEN_ASSERTIONS:
            case TOKEN_BREAK:
            case TOKEN_BREAKPOINT:
            case TOKEN_CHOICE_GENERATORS:
            case TOKEN_CONTINUE:
            case TOKEN_CLEAR:
            case TOKEN_CREATE:
            case TOKEN_DATA:
            case TOKEN_DELETE:
            case TOKEN_DISABLE:
            case TOKEN_E:
            case TOKEN_ENABLE:
            case TOKEN_EXECUTE:
            case TOKEN_LOG:
            case TOKEN_PRINT:
            case TOKEN_RECORD:
            case TOKEN_RUN:
            case TOKEN_RUNNING:
            case TOKEN_SAVE:
            case TOKEN_SELECT:
            case TOKEN_SET:
            case TOKEN_SCHEDULING:
            case TOKEN_SHOW:
            case TOKEN_STARTED:
            case TOKEN_STOPPED:
            case TOKEN_USED:
            case TOKEN_TERMINATING:
            case TOKEN_THREAD:
            case TOKEN_THREAD_PC:
            case TOKEN_X:
            case TOKEN_STEP_INSTRUCTION:
            case TOKEN_STEP_OVER:
            case TOKEN_STEP_IN:
            case TOKEN_STEP_OUT:
            case TOKEN_STEP_TRANSITION:
            case TOKEN_BACK_STEP_INSTRUCTION:
            case TOKEN_BACK_STEP_OVER:
            case TOKEN_BACK_STEP_IN:
            case TOKEN_BACK_STEP_OUT:
            case TOKEN_BACK_STEP_TRANSITION:
            case TOKEN_CB_STATE_CHANGE:
            case TOKEN_CB_GENERIC_ERROR:
            case TOKEN_CB_GENERIC_INFO:
            case TOKEN_CB_BREAKPOINT_HIT:
            case TOKEN_CB_CG_NEW_CHOICE:
            case TOKEN_CB_CG_CHOICE_TO_USE:
            case TOKEN_CB_CG_USED_CHOICE:
                {
                alt40=1;
                }
                break;
            case SIGN_EQUAL:
            case SIGN_DOLLAR:
            case SIGN_DOT:
            case SIGN_HASH:
            case SIGN_COLON:
            case SIGN_LSBRA:
            case SIGN_RSBRA:
            case SIGN_ASTERISK:
            case SIGN_SLASH:
            case SIGN_BACKSLASH:
            case SPECIAL_CHAR:
                {
                alt40=2;
                }
                break;
            case IDF:
                {
                alt40=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }

            switch (alt40) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:227:7: allKeyWordsWithoutCreateBPandHitCount (b01= allTextWS )?
                    {
                    pushFollow(FOLLOW_allKeyWordsWithoutCreateBPandHitCount_in_bpExpression1823);
                    allKeyWordsWithoutCreateBPandHitCount18=allKeyWordsWithoutCreateBPandHitCount();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:227:52: (b01= allTextWS )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==WS) ) {
                        int LA37_1 = input.LA(2);

                        if ( ((LA37_1>=TOKEN_ALL && LA37_1<=IDF)||(LA37_1>=INT && LA37_1<=SPECIAL_CHAR)) ) {
                            alt37=1;
                        }
                    }
                    else if ( ((LA37_0>=TOKEN_ALL && LA37_0<=IDF)||(LA37_0>=INT && LA37_0<=SPECIAL_CHAR)) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:227:52: b01= allTextWS
                            {
                            pushFollow(FOLLOW_allTextWS_in_bpExpression1831);
                            b01=allTextWS();

                            state._fsp--;


                            }
                            break;

                    }

                     expr = allKeyWordsWithoutCreateBPandHitCount18 +  ((b01!=null?input.toString(b01.start,b01.stop):null)!=null?(b01!=null?input.toString(b01.start,b01.stop):null):""); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:228:7: allNonKeywordsRulesBase (b02= allTextWS )?
                    {
                    pushFollow(FOLLOW_allNonKeywordsRulesBase_in_bpExpression1849);
                    allNonKeywordsRulesBase19=allNonKeywordsRulesBase();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:228:52: (b02= allTextWS )?
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( (LA38_0==WS) ) {
                        int LA38_1 = input.LA(2);

                        if ( ((LA38_1>=TOKEN_ALL && LA38_1<=IDF)||(LA38_1>=INT && LA38_1<=SPECIAL_CHAR)) ) {
                            alt38=1;
                        }
                    }
                    else if ( ((LA38_0>=TOKEN_ALL && LA38_0<=IDF)||(LA38_0>=INT && LA38_0<=SPECIAL_CHAR)) ) {
                        alt38=1;
                    }
                    switch (alt38) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:228:52: b02= allTextWS
                            {
                            pushFollow(FOLLOW_allTextWS_in_bpExpression1871);
                            b02=allTextWS();

                            state._fsp--;


                            }
                            break;

                    }

                     expr = (allNonKeywordsRulesBase19!=null?input.toString(allNonKeywordsRulesBase19.start,allNonKeywordsRulesBase19.stop):null) +                ((b02!=null?input.toString(b02.start,b02.stop):null)!=null?(b02!=null?input.toString(b02.start,b02.stop):null):""); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:229:7: IDF (b03= allTextWS )?
                    {
                    IDF20=(Token)match(input,IDF,FOLLOW_IDF_in_bpExpression1889); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:229:52: (b03= allTextWS )?
                    int alt39=2;
                    int LA39_0 = input.LA(1);

                    if ( (LA39_0==WS) ) {
                        int LA39_1 = input.LA(2);

                        if ( ((LA39_1>=TOKEN_ALL && LA39_1<=IDF)||(LA39_1>=INT && LA39_1<=SPECIAL_CHAR)) ) {
                            alt39=1;
                        }
                    }
                    else if ( ((LA39_0>=TOKEN_ALL && LA39_0<=IDF)||(LA39_0>=INT && LA39_0<=SPECIAL_CHAR)) ) {
                        alt39=1;
                    }
                    switch (alt39) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:229:52: b03= allTextWS
                            {
                            pushFollow(FOLLOW_allTextWS_in_bpExpression1931);
                            b03=allTextWS();

                            state._fsp--;


                            }
                            break;

                    }

                     expr = (IDF20!=null?IDF20.getText():null) +                                    ((b03!=null?input.toString(b03.start,b03.stop):null)!=null?(b03!=null?input.toString(b03.start,b03.stop):null):""); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end "bpExpression"


    // $ANTLR start "cmdSingleSteps"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:233:1: cmdSingleSteps returns [CmdSingleStepping value] : ( TOKEN_STEP_INSTRUCTION ( intValue )? | TOKEN_STEP_OVER ( intValue )? | TOKEN_STEP_IN ( intValue )? | TOKEN_STEP_OUT ( intValue )? | TOKEN_STEP_TRANSITION ( ( WS )? c= cgType )? ( intValue )? | TOKEN_BACK_STEP_INSTRUCTION ( intValue )? | TOKEN_BACK_STEP_OVER ( intValue )? | TOKEN_BACK_STEP_IN ( intValue )? | TOKEN_BACK_STEP_OUT ( intValue )? | TOKEN_BACK_STEP_TRANSITION ( ( WS )? c= cgType )? ( intValue )? );
    public final CmdSingleStepping cmdSingleSteps() throws RecognitionException {
        CmdSingleStepping value = null;

        CmdChoiceGeneratorsTracking.CGTypeSpec c = null;

        Integer intValue21 = null;

        Integer intValue22 = null;

        Integer intValue23 = null;

        Integer intValue24 = null;

        Integer intValue25 = null;

        Integer intValue26 = null;

        Integer intValue27 = null;

        Integer intValue28 = null;

        Integer intValue29 = null;

        Integer intValue30 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:234:5: ( TOKEN_STEP_INSTRUCTION ( intValue )? | TOKEN_STEP_OVER ( intValue )? | TOKEN_STEP_IN ( intValue )? | TOKEN_STEP_OUT ( intValue )? | TOKEN_STEP_TRANSITION ( ( WS )? c= cgType )? ( intValue )? | TOKEN_BACK_STEP_INSTRUCTION ( intValue )? | TOKEN_BACK_STEP_OVER ( intValue )? | TOKEN_BACK_STEP_IN ( intValue )? | TOKEN_BACK_STEP_OUT ( intValue )? | TOKEN_BACK_STEP_TRANSITION ( ( WS )? c= cgType )? ( intValue )? )
            int alt55=10;
            switch ( input.LA(1) ) {
            case TOKEN_STEP_INSTRUCTION:
                {
                alt55=1;
                }
                break;
            case TOKEN_STEP_OVER:
                {
                alt55=2;
                }
                break;
            case TOKEN_STEP_IN:
                {
                alt55=3;
                }
                break;
            case TOKEN_STEP_OUT:
                {
                alt55=4;
                }
                break;
            case TOKEN_STEP_TRANSITION:
                {
                alt55=5;
                }
                break;
            case TOKEN_BACK_STEP_INSTRUCTION:
                {
                alt55=6;
                }
                break;
            case TOKEN_BACK_STEP_OVER:
                {
                alt55=7;
                }
                break;
            case TOKEN_BACK_STEP_IN:
                {
                alt55=8;
                }
                break;
            case TOKEN_BACK_STEP_OUT:
                {
                alt55=9;
                }
                break;
            case TOKEN_BACK_STEP_TRANSITION:
                {
                alt55=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;
            }

            switch (alt55) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:234:7: TOKEN_STEP_INSTRUCTION ( intValue )?
                    {
                    match(input,TOKEN_STEP_INSTRUCTION,FOLLOW_TOKEN_STEP_INSTRUCTION_in_cmdSingleSteps1963); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:234:51: ( intValue )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==INT||(LA41_0>=SIGN_PLUS && LA41_0<=HEX)) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:234:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps1986);
                            intValue21=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(true, StepType.ST_INSTRUCTION,   intValue21); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:235:7: TOKEN_STEP_OVER ( intValue )?
                    {
                    match(input,TOKEN_STEP_OVER,FOLLOW_TOKEN_STEP_OVER_in_cmdSingleSteps1998); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:235:51: ( intValue )?
                    int alt42=2;
                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==INT||(LA42_0>=SIGN_PLUS && LA42_0<=HEX)) ) {
                        alt42=1;
                    }
                    switch (alt42) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:235:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2028);
                            intValue22=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(true, StepType.ST_LINE,          intValue22); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:236:7: TOKEN_STEP_IN ( intValue )?
                    {
                    match(input,TOKEN_STEP_IN,FOLLOW_TOKEN_STEP_IN_in_cmdSingleSteps2040); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:236:51: ( intValue )?
                    int alt43=2;
                    int LA43_0 = input.LA(1);

                    if ( (LA43_0==INT||(LA43_0>=SIGN_PLUS && LA43_0<=HEX)) ) {
                        alt43=1;
                    }
                    switch (alt43) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:236:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2072);
                            intValue23=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(true, StepType.ST_STEP_IN,       intValue23); 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:237:7: TOKEN_STEP_OUT ( intValue )?
                    {
                    match(input,TOKEN_STEP_OUT,FOLLOW_TOKEN_STEP_OUT_in_cmdSingleSteps2084); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:237:51: ( intValue )?
                    int alt44=2;
                    int LA44_0 = input.LA(1);

                    if ( (LA44_0==INT||(LA44_0>=SIGN_PLUS && LA44_0<=HEX)) ) {
                        alt44=1;
                    }
                    switch (alt44) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:237:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2115);
                            intValue24=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(true, StepType.ST_STEP_OUT,      intValue24); 

                    }
                    break;
                case 5 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:238:7: TOKEN_STEP_TRANSITION ( ( WS )? c= cgType )? ( intValue )?
                    {
                    match(input,TOKEN_STEP_TRANSITION,FOLLOW_TOKEN_STEP_TRANSITION_in_cmdSingleSteps2127); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:238:29: ( ( WS )? c= cgType )?
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==WS) ) {
                        int LA46_1 = input.LA(2);

                        if ( (LA46_1==TOKEN_ALL||LA46_1==TOKEN_DATA||LA46_1==TOKEN_SCHEDULING) ) {
                            alt46=1;
                        }
                    }
                    else if ( (LA46_0==TOKEN_ALL||LA46_0==TOKEN_DATA||LA46_0==TOKEN_SCHEDULING) ) {
                        alt46=1;
                    }
                    switch (alt46) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:238:30: ( WS )? c= cgType
                            {
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:238:30: ( WS )?
                            int alt45=2;
                            int LA45_0 = input.LA(1);

                            if ( (LA45_0==WS) ) {
                                alt45=1;
                            }
                            switch (alt45) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:238:30: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdSingleSteps2130); 

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_cgType_in_cmdSingleSteps2135);
                            c=cgType();

                            state._fsp--;


                            }
                            break;

                    }

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:238:51: ( intValue )?
                    int alt47=2;
                    int LA47_0 = input.LA(1);

                    if ( (LA47_0==INT||(LA47_0>=SIGN_PLUS && LA47_0<=HEX)) ) {
                        alt47=1;
                    }
                    switch (alt47) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:238:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2145);
                            intValue25=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = CmdSingleStepping.createCmdSingleSteppingTransition(true, c, intValue25); 

                    }
                    break;
                case 6 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:239:7: TOKEN_BACK_STEP_INSTRUCTION ( intValue )?
                    {
                    match(input,TOKEN_BACK_STEP_INSTRUCTION,FOLLOW_TOKEN_BACK_STEP_INSTRUCTION_in_cmdSingleSteps2157); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:239:51: ( intValue )?
                    int alt48=2;
                    int LA48_0 = input.LA(1);

                    if ( (LA48_0==INT||(LA48_0>=SIGN_PLUS && LA48_0<=HEX)) ) {
                        alt48=1;
                    }
                    switch (alt48) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:239:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2175);
                            intValue26=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(false, StepType.ST_INSTRUCTION,  intValue26); 

                    }
                    break;
                case 7 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:240:7: TOKEN_BACK_STEP_OVER ( intValue )?
                    {
                    match(input,TOKEN_BACK_STEP_OVER,FOLLOW_TOKEN_BACK_STEP_OVER_in_cmdSingleSteps2187); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:240:51: ( intValue )?
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( (LA49_0==INT||(LA49_0>=SIGN_PLUS && LA49_0<=HEX)) ) {
                        alt49=1;
                    }
                    switch (alt49) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:240:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2212);
                            intValue27=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(false, StepType.ST_LINE,         intValue27); 

                    }
                    break;
                case 8 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:241:7: TOKEN_BACK_STEP_IN ( intValue )?
                    {
                    match(input,TOKEN_BACK_STEP_IN,FOLLOW_TOKEN_BACK_STEP_IN_in_cmdSingleSteps2224); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:241:51: ( intValue )?
                    int alt50=2;
                    int LA50_0 = input.LA(1);

                    if ( (LA50_0==INT||(LA50_0>=SIGN_PLUS && LA50_0<=HEX)) ) {
                        alt50=1;
                    }
                    switch (alt50) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:241:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2251);
                            intValue28=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(false, StepType.ST_STEP_IN,      intValue28); 

                    }
                    break;
                case 9 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:242:7: TOKEN_BACK_STEP_OUT ( intValue )?
                    {
                    match(input,TOKEN_BACK_STEP_OUT,FOLLOW_TOKEN_BACK_STEP_OUT_in_cmdSingleSteps2263); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:242:51: ( intValue )?
                    int alt51=2;
                    int LA51_0 = input.LA(1);

                    if ( (LA51_0==INT||(LA51_0>=SIGN_PLUS && LA51_0<=HEX)) ) {
                        alt51=1;
                    }
                    switch (alt51) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:242:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2289);
                            intValue29=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdSingleStepping(false, StepType.ST_STEP_OUT,     intValue29); 

                    }
                    break;
                case 10 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:243:7: TOKEN_BACK_STEP_TRANSITION ( ( WS )? c= cgType )? ( intValue )?
                    {
                    match(input,TOKEN_BACK_STEP_TRANSITION,FOLLOW_TOKEN_BACK_STEP_TRANSITION_in_cmdSingleSteps2301); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:243:34: ( ( WS )? c= cgType )?
                    int alt53=2;
                    int LA53_0 = input.LA(1);

                    if ( (LA53_0==WS) ) {
                        int LA53_1 = input.LA(2);

                        if ( (LA53_1==TOKEN_ALL||LA53_1==TOKEN_DATA||LA53_1==TOKEN_SCHEDULING) ) {
                            alt53=1;
                        }
                    }
                    else if ( (LA53_0==TOKEN_ALL||LA53_0==TOKEN_DATA||LA53_0==TOKEN_SCHEDULING) ) {
                        alt53=1;
                    }
                    switch (alt53) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:243:35: ( WS )? c= cgType
                            {
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:243:35: ( WS )?
                            int alt52=2;
                            int LA52_0 = input.LA(1);

                            if ( (LA52_0==WS) ) {
                                alt52=1;
                            }
                            switch (alt52) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:243:35: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdSingleSteps2304); 

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_cgType_in_cmdSingleSteps2309);
                            c=cgType();

                            state._fsp--;


                            }
                            break;

                    }

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:243:51: ( intValue )?
                    int alt54=2;
                    int LA54_0 = input.LA(1);

                    if ( (LA54_0==INT||(LA54_0>=SIGN_PLUS && LA54_0<=HEX)) ) {
                        alt54=1;
                    }
                    switch (alt54) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:243:51: intValue
                            {
                            pushFollow(FOLLOW_intValue_in_cmdSingleSteps2314);
                            intValue30=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = CmdSingleStepping.createCmdSingleSteppingTransition(false, c, intValue30); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "cmdSingleSteps"


    // $ANTLR start "cmdProgramState"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:247:1: cmdProgramState returns [ClientCommand value] : ( TOKEN_THREAD ( ( WS )? intValue )? | TOKEN_PRINT ( ( WS )? allText )? | TOKEN_THREAD_PC ( ( WS )? intValue )? | TOKEN_SET ( WS )? allText );
    public final ClientCommand cmdProgramState() throws RecognitionException {
        ClientCommand value = null;

        Integer intValue31 = null;

        ConsoleGrammarParser.allText_return allText32 = null;

        Integer intValue33 = null;

        ConsoleGrammarParser.allText_return allText34 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:248:3: ( TOKEN_THREAD ( ( WS )? intValue )? | TOKEN_PRINT ( ( WS )? allText )? | TOKEN_THREAD_PC ( ( WS )? intValue )? | TOKEN_SET ( WS )? allText )
            int alt63=4;
            switch ( input.LA(1) ) {
            case TOKEN_THREAD:
                {
                alt63=1;
                }
                break;
            case TOKEN_PRINT:
                {
                alt63=2;
                }
                break;
            case TOKEN_THREAD_PC:
                {
                alt63=3;
                }
                break;
            case TOKEN_SET:
                {
                alt63=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }

            switch (alt63) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:248:5: TOKEN_THREAD ( ( WS )? intValue )?
                    {
                    match(input,TOKEN_THREAD,FOLLOW_TOKEN_THREAD_in_cmdProgramState2341); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:248:21: ( ( WS )? intValue )?
                    int alt57=2;
                    int LA57_0 = input.LA(1);

                    if ( (LA57_0==WS) ) {
                        int LA57_1 = input.LA(2);

                        if ( (LA57_1==INT||(LA57_1>=SIGN_PLUS && LA57_1<=HEX)) ) {
                            alt57=1;
                        }
                    }
                    else if ( (LA57_0==INT||(LA57_0>=SIGN_PLUS && LA57_0<=HEX)) ) {
                        alt57=1;
                    }
                    switch (alt57) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:248:22: ( WS )? intValue
                            {
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:248:22: ( WS )?
                            int alt56=2;
                            int LA56_0 = input.LA(1);

                            if ( (LA56_0==WS) ) {
                                alt56=1;
                            }
                            switch (alt56) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:248:22: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdProgramState2347); 

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_intValue_in_cmdProgramState2350);
                            intValue31=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdStatusThreads(intValue31); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:249:5: TOKEN_PRINT ( ( WS )? allText )?
                    {
                    match(input,TOKEN_PRINT,FOLLOW_TOKEN_PRINT_in_cmdProgramState2362); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:249:21: ( ( WS )? allText )?
                    int alt59=2;
                    int LA59_0 = input.LA(1);

                    if ( (LA59_0==WS) ) {
                        int LA59_1 = input.LA(2);

                        if ( ((LA59_1>=TOKEN_ALL && LA59_1<=IDF)||(LA59_1>=INT && LA59_1<=SPECIAL_CHAR)) ) {
                            alt59=1;
                        }
                    }
                    else if ( ((LA59_0>=TOKEN_ALL && LA59_0<=IDF)||(LA59_0>=INT && LA59_0<=SPECIAL_CHAR)) ) {
                        alt59=1;
                    }
                    switch (alt59) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:249:22: ( WS )? allText
                            {
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:249:22: ( WS )?
                            int alt58=2;
                            int LA58_0 = input.LA(1);

                            if ( (LA58_0==WS) ) {
                                alt58=1;
                            }
                            switch (alt58) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:249:22: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdProgramState2369); 

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_allText_in_cmdProgramState2372);
                            allText32=allText();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdPrint((allText32!=null?input.toString(allText32.start,allText32.stop):null)!=null?(allText32!=null?input.toString(allText32.start,allText32.stop):null):""); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:250:5: TOKEN_THREAD_PC ( ( WS )? intValue )?
                    {
                    match(input,TOKEN_THREAD_PC,FOLLOW_TOKEN_THREAD_PC_in_cmdProgramState2385); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:250:21: ( ( WS )? intValue )?
                    int alt61=2;
                    int LA61_0 = input.LA(1);

                    if ( (LA61_0==WS) ) {
                        int LA61_1 = input.LA(2);

                        if ( (LA61_1==INT||(LA61_1>=SIGN_PLUS && LA61_1<=HEX)) ) {
                            alt61=1;
                        }
                    }
                    else if ( (LA61_0==INT||(LA61_0>=SIGN_PLUS && LA61_0<=HEX)) ) {
                        alt61=1;
                    }
                    switch (alt61) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:250:22: ( WS )? intValue
                            {
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:250:22: ( WS )?
                            int alt60=2;
                            int LA60_0 = input.LA(1);

                            if ( (LA60_0==WS) ) {
                                alt60=1;
                            }
                            switch (alt60) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:250:22: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdProgramState2388); 

                                    }
                                    break;

                            }

                            pushFollow(FOLLOW_intValue_in_cmdProgramState2391);
                            intValue33=intValue();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdThreadsPC(intValue33); 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:251:5: TOKEN_SET ( WS )? allText
                    {
                    match(input,TOKEN_SET,FOLLOW_TOKEN_SET_in_cmdProgramState2403); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:251:22: ( WS )?
                    int alt62=2;
                    int LA62_0 = input.LA(1);

                    if ( (LA62_0==WS) ) {
                        alt62=1;
                    }
                    switch (alt62) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:251:22: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdProgramState2412); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_allText_in_cmdProgramState2415);
                    allText34=allText();

                    state._fsp--;

                     value = new CmdSet((allText34!=null?input.toString(allText34.start,allText34.stop):null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "cmdProgramState"


    // $ANTLR start "cmdChoiceGenerators"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:255:1: cmdChoiceGenerators returns [ClientCommand value] : ( TOKEN_USED ( WS )? TOKEN_CHOICE_GENERATORS | a= mode ( WS )? (b= cgMode ( WS )? )? (c= cgType ( WS )? )? TOKEN_CHOICE_GENERATORS | TOKEN_CHOICE_GENERATORS ( WS )? TOKEN_SELECT cgChoice );
    public final ClientCommand cmdChoiceGenerators() throws RecognitionException {
        ClientCommand value = null;

        boolean a = false;

        ChoiceGeneratorsInterface.CGMode b = null;

        CmdChoiceGeneratorsTracking.CGTypeSpec c = null;

        int cgChoice35 = 0;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:256:5: ( TOKEN_USED ( WS )? TOKEN_CHOICE_GENERATORS | a= mode ( WS )? (b= cgMode ( WS )? )? (c= cgType ( WS )? )? TOKEN_CHOICE_GENERATORS | TOKEN_CHOICE_GENERATORS ( WS )? TOKEN_SELECT cgChoice )
            int alt71=3;
            switch ( input.LA(1) ) {
            case TOKEN_USED:
                {
                alt71=1;
                }
                break;
            case TOKEN_DISABLE:
            case TOKEN_ENABLE:
                {
                alt71=2;
                }
                break;
            case TOKEN_CHOICE_GENERATORS:
                {
                alt71=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }

            switch (alt71) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:256:7: TOKEN_USED ( WS )? TOKEN_CHOICE_GENERATORS
                    {
                    match(input,TOKEN_USED,FOLLOW_TOKEN_USED_in_cmdChoiceGenerators2444); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:256:18: ( WS )?
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( (LA64_0==WS) ) {
                        alt64=1;
                    }
                    switch (alt64) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:256:18: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdChoiceGenerators2446); 

                            }
                            break;

                    }

                    match(input,TOKEN_CHOICE_GENERATORS,FOLLOW_TOKEN_CHOICE_GENERATORS_in_cmdChoiceGenerators2449); 
                     value = new CmdUsedChoiceGenerators(); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:7: a= mode ( WS )? (b= cgMode ( WS )? )? (c= cgType ( WS )? )? TOKEN_CHOICE_GENERATORS
                    {
                    pushFollow(FOLLOW_mode_in_cmdChoiceGenerators2461);
                    a=mode();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:15: ( WS )?
                    int alt65=2;
                    int LA65_0 = input.LA(1);

                    if ( (LA65_0==WS) ) {
                        alt65=1;
                    }
                    switch (alt65) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:15: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdChoiceGenerators2464); 

                            }
                            break;

                    }

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:19: (b= cgMode ( WS )? )?
                    int alt67=2;
                    int LA67_0 = input.LA(1);

                    if ( (LA67_0==TOKEN_ASK||LA67_0==TOKEN_PRINT) ) {
                        alt67=1;
                    }
                    switch (alt67) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:20: b= cgMode ( WS )?
                            {
                            pushFollow(FOLLOW_cgMode_in_cmdChoiceGenerators2470);
                            b=cgMode();

                            state._fsp--;

                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:29: ( WS )?
                            int alt66=2;
                            int LA66_0 = input.LA(1);

                            if ( (LA66_0==WS) ) {
                                alt66=1;
                            }
                            switch (alt66) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:29: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdChoiceGenerators2472); 

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:35: (c= cgType ( WS )? )?
                    int alt69=2;
                    int LA69_0 = input.LA(1);

                    if ( (LA69_0==TOKEN_ALL||LA69_0==TOKEN_DATA||LA69_0==TOKEN_SCHEDULING) ) {
                        alt69=1;
                    }
                    switch (alt69) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:36: c= cgType ( WS )?
                            {
                            pushFollow(FOLLOW_cgType_in_cmdChoiceGenerators2480);
                            c=cgType();

                            state._fsp--;

                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:45: ( WS )?
                            int alt68=2;
                            int LA68_0 = input.LA(1);

                            if ( (LA68_0==WS) ) {
                                alt68=1;
                            }
                            switch (alt68) {
                                case 1 :
                                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:257:45: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_cmdChoiceGenerators2482); 

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    match(input,TOKEN_CHOICE_GENERATORS,FOLLOW_TOKEN_CHOICE_GENERATORS_in_cmdChoiceGenerators2487); 
                     value = new CmdChoiceGeneratorsTracking( (c!=null ? c : CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_ALL), (b != null ? b : ChoiceGeneratorsInterface.CGMode.CG_MODE_PRINT), a); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:258:7: TOKEN_CHOICE_GENERATORS ( WS )? TOKEN_SELECT cgChoice
                    {
                    match(input,TOKEN_CHOICE_GENERATORS,FOLLOW_TOKEN_CHOICE_GENERATORS_in_cmdChoiceGenerators2498); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:258:31: ( WS )?
                    int alt70=2;
                    int LA70_0 = input.LA(1);

                    if ( (LA70_0==WS) ) {
                        alt70=1;
                    }
                    switch (alt70) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:258:31: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdChoiceGenerators2500); 

                            }
                            break;

                    }

                    match(input,TOKEN_SELECT,FOLLOW_TOKEN_SELECT_in_cmdChoiceGenerators2503); 
                    pushFollow(FOLLOW_cgChoice_in_cmdChoiceGenerators2505);
                    cgChoice35=cgChoice();

                    state._fsp--;

                     value = new CmdChoiceSelect(cgChoice35); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "cmdChoiceGenerators"


    // $ANTLR start "cgType"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:261:1: cgType returns [CmdChoiceGeneratorsTracking.CGTypeSpec cgsType] : ( TOKEN_SCHEDULING | TOKEN_DATA | TOKEN_ALL );
    public final CmdChoiceGeneratorsTracking.CGTypeSpec cgType() throws RecognitionException {
        CmdChoiceGeneratorsTracking.CGTypeSpec cgsType = null;

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:262:5: ( TOKEN_SCHEDULING | TOKEN_DATA | TOKEN_ALL )
            int alt72=3;
            switch ( input.LA(1) ) {
            case TOKEN_SCHEDULING:
                {
                alt72=1;
                }
                break;
            case TOKEN_DATA:
                {
                alt72=2;
                }
                break;
            case TOKEN_ALL:
                {
                alt72=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }

            switch (alt72) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:262:7: TOKEN_SCHEDULING
                    {
                    match(input,TOKEN_SCHEDULING,FOLLOW_TOKEN_SCHEDULING_in_cgType2528); 
                     cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_SCHEDULING; 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:263:7: TOKEN_DATA
                    {
                    match(input,TOKEN_DATA,FOLLOW_TOKEN_DATA_in_cgType2540); 
                     cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_DATA; 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:264:7: TOKEN_ALL
                    {
                    match(input,TOKEN_ALL,FOLLOW_TOKEN_ALL_in_cgType2558); 
                     cgsType = CmdChoiceGeneratorsTracking.CGTypeSpec.CGS_ALL; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return cgsType;
    }
    // $ANTLR end "cgType"


    // $ANTLR start "cgMode"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:267:1: cgMode returns [ChoiceGeneratorsInterface.CGMode cg_mode] : ( TOKEN_ASK | TOKEN_PRINT );
    public final ChoiceGeneratorsInterface.CGMode cgMode() throws RecognitionException {
        ChoiceGeneratorsInterface.CGMode cg_mode = null;

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:268:5: ( TOKEN_ASK | TOKEN_PRINT )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==TOKEN_ASK) ) {
                alt73=1;
            }
            else if ( (LA73_0==TOKEN_PRINT) ) {
                alt73=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }
            switch (alt73) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:268:7: TOKEN_ASK
                    {
                    match(input,TOKEN_ASK,FOLLOW_TOKEN_ASK_in_cgMode2591); 
                     cg_mode =  ChoiceGeneratorsInterface.CGMode.CG_MODE_ASK; 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:269:7: TOKEN_PRINT
                    {
                    match(input,TOKEN_PRINT,FOLLOW_TOKEN_PRINT_in_cgMode2606); 
                     cg_mode =  ChoiceGeneratorsInterface.CGMode.CG_MODE_PRINT; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return cg_mode;
    }
    // $ANTLR end "cgMode"


    // $ANTLR start "mode"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:272:1: mode returns [boolean mode] : ( TOKEN_ENABLE | TOKEN_DISABLE );
    public final boolean mode() throws RecognitionException {
        boolean mode = false;

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:273:5: ( TOKEN_ENABLE | TOKEN_DISABLE )
            int alt74=2;
            int LA74_0 = input.LA(1);

            if ( (LA74_0==TOKEN_ENABLE) ) {
                alt74=1;
            }
            else if ( (LA74_0==TOKEN_DISABLE) ) {
                alt74=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 74, 0, input);

                throw nvae;
            }
            switch (alt74) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:273:7: TOKEN_ENABLE
                    {
                    match(input,TOKEN_ENABLE,FOLLOW_TOKEN_ENABLE_in_mode2632); 
                     mode = true; 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:274:7: TOKEN_DISABLE
                    {
                    match(input,TOKEN_DISABLE,FOLLOW_TOKEN_DISABLE_in_mode2644); 
                     mode = false; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return mode;
    }
    // $ANTLR end "mode"


    // $ANTLR start "cgChoice"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:277:1: cgChoice returns [int choice] : ( | ( WS )? intValue | ( WS )? '*' );
    public final int cgChoice() throws RecognitionException {
        int choice = 0;

        Integer intValue36 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:278:5: ( | ( WS )? intValue | ( WS )? '*' )
            int alt77=3;
            switch ( input.LA(1) ) {
            case WS:
                {
                switch ( input.LA(2) ) {
                case EOF:
                    {
                    alt77=1;
                    }
                    break;
                case INT:
                case SIGN_PLUS:
                case SIGN_MINUS:
                case HEX:
                    {
                    alt77=2;
                    }
                    break;
                case SIGN_ASTERISK:
                    {
                    alt77=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 77, 1, input);

                    throw nvae;
                }

                }
                break;
            case EOF:
                {
                alt77=1;
                }
                break;
            case INT:
            case SIGN_PLUS:
            case SIGN_MINUS:
            case HEX:
                {
                alt77=2;
                }
                break;
            case SIGN_ASTERISK:
                {
                alt77=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 77, 0, input);

                throw nvae;
            }

            switch (alt77) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:278:23: 
                    {
                     choice = CmdChoiceSelect.USE_CURENT_CHOICE; 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:279:7: ( WS )? intValue
                    {
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:279:7: ( WS )?
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( (LA75_0==WS) ) {
                        alt75=1;
                    }
                    switch (alt75) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:279:7: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cgChoice2692); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_intValue_in_cgChoice2695);
                    intValue36=intValue();

                    state._fsp--;

                     choice = intValue36; 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:280:7: ( WS )? '*'
                    {
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:280:7: ( WS )?
                    int alt76=2;
                    int LA76_0 = input.LA(1);

                    if ( (LA76_0==WS) ) {
                        alt76=1;
                    }
                    switch (alt76) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:280:7: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cgChoice2708); 

                            }
                            break;

                    }

                    match(input,SIGN_ASTERISK,FOLLOW_SIGN_ASTERISK_in_cgChoice2711); 
                     choice = ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return choice;
    }
    // $ANTLR end "cgChoice"


    // $ANTLR start "cmdCallback"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:283:1: cmdCallback returns [ClientCommand value] : ( TOKEN_CB_STATE_CHANGE | TOKEN_CB_STATE_CHANGE ( WS )? TOKEN_STATE ( WS )? '=' ( WS )? inspectorState | TOKEN_CB_GENERIC_ERROR | TOKEN_CB_GENERIC_INFO | TOKEN_CB_BREAKPOINT_HIT | TOKEN_CB_CG_NEW_CHOICE | TOKEN_CB_CG_CHOICE_TO_USE | TOKEN_CB_CG_USED_CHOICE );
    public final ClientCommand cmdCallback() throws RecognitionException {
        ClientCommand value = null;

        InspectorStates inspectorState37 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:284:5: ( TOKEN_CB_STATE_CHANGE | TOKEN_CB_STATE_CHANGE ( WS )? TOKEN_STATE ( WS )? '=' ( WS )? inspectorState | TOKEN_CB_GENERIC_ERROR | TOKEN_CB_GENERIC_INFO | TOKEN_CB_BREAKPOINT_HIT | TOKEN_CB_CG_NEW_CHOICE | TOKEN_CB_CG_CHOICE_TO_USE | TOKEN_CB_CG_USED_CHOICE )
            int alt81=8;
            alt81 = dfa81.predict(input);
            switch (alt81) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:284:7: TOKEN_CB_STATE_CHANGE
                    {
                    match(input,TOKEN_CB_STATE_CHANGE,FOLLOW_TOKEN_CB_STATE_CHANGE_in_cmdCallback2742); 
                     value = new CmdCallback(CB_METHODS.CB_STATE_CHANGE); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:285:7: TOKEN_CB_STATE_CHANGE ( WS )? TOKEN_STATE ( WS )? '=' ( WS )? inspectorState
                    {
                    match(input,TOKEN_CB_STATE_CHANGE,FOLLOW_TOKEN_CB_STATE_CHANGE_in_cmdCallback2799); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:285:29: ( WS )?
                    int alt78=2;
                    int LA78_0 = input.LA(1);

                    if ( (LA78_0==WS) ) {
                        alt78=1;
                    }
                    switch (alt78) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:285:29: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdCallback2801); 

                            }
                            break;

                    }

                    match(input,TOKEN_STATE,FOLLOW_TOKEN_STATE_in_cmdCallback2804); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:285:45: ( WS )?
                    int alt79=2;
                    int LA79_0 = input.LA(1);

                    if ( (LA79_0==WS) ) {
                        alt79=1;
                    }
                    switch (alt79) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:285:45: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdCallback2806); 

                            }
                            break;

                    }

                    match(input,SIGN_EQUAL,FOLLOW_SIGN_EQUAL_in_cmdCallback2809); 
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:285:53: ( WS )?
                    int alt80=2;
                    int LA80_0 = input.LA(1);

                    if ( (LA80_0==WS) ) {
                        alt80=1;
                    }
                    switch (alt80) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:285:53: WS
                            {
                            match(input,WS,FOLLOW_WS_in_cmdCallback2811); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_inspectorState_in_cmdCallback2814);
                    inspectorState37=inspectorState();

                    state._fsp--;

                     value = new CmdCallback(inspectorState37); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:286:7: TOKEN_CB_GENERIC_ERROR
                    {
                    match(input,TOKEN_CB_GENERIC_ERROR,FOLLOW_TOKEN_CB_GENERIC_ERROR_in_cmdCallback2828); 
                     value = new CmdCallback(CB_METHODS.CB_GENERIC_ERROR); 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:287:7: TOKEN_CB_GENERIC_INFO
                    {
                    match(input,TOKEN_CB_GENERIC_INFO,FOLLOW_TOKEN_CB_GENERIC_INFO_in_cmdCallback2884); 
                     value = new CmdCallback(CB_METHODS.CB_GENERIC_INFO); 

                    }
                    break;
                case 5 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:288:7: TOKEN_CB_BREAKPOINT_HIT
                    {
                    match(input,TOKEN_CB_BREAKPOINT_HIT,FOLLOW_TOKEN_CB_BREAKPOINT_HIT_in_cmdCallback2942); 
                     value = new CmdCallback(CB_METHODS.CB_BREAKPOINT_HIT); 

                    }
                    break;
                case 6 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:289:7: TOKEN_CB_CG_NEW_CHOICE
                    {
                    match(input,TOKEN_CB_CG_NEW_CHOICE,FOLLOW_TOKEN_CB_CG_NEW_CHOICE_in_cmdCallback2997); 
                     value = new CmdCallback(CB_METHODS.CB_CG_NEW_CHOICE); 

                    }
                    break;
                case 7 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:290:7: TOKEN_CB_CG_CHOICE_TO_USE
                    {
                    match(input,TOKEN_CB_CG_CHOICE_TO_USE,FOLLOW_TOKEN_CB_CG_CHOICE_TO_USE_in_cmdCallback3053); 
                     value = new CmdCallback(CB_METHODS.CB_CG_CHOICE_TO_USE); 

                    }
                    break;
                case 8 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:291:7: TOKEN_CB_CG_USED_CHOICE
                    {
                    match(input,TOKEN_CB_CG_USED_CHOICE,FOLLOW_TOKEN_CB_CG_USED_CHOICE_in_cmdCallback3106); 
                     value = new CmdCallback(CB_METHODS.CB_CG_USED_CHOICE); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "cmdCallback"


    // $ANTLR start "inspectorState"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:294:1: inspectorState returns [InspectorStates state] : ( TOKEN_RUNNING | TOKEN_STARTED | TOKEN_STOPPED | TOKEN_TERMINATING );
    public final InspectorStates inspectorState() throws RecognitionException {
        InspectorStates state = null;

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:295:5: ( TOKEN_RUNNING | TOKEN_STARTED | TOKEN_STOPPED | TOKEN_TERMINATING )
            int alt82=4;
            switch ( input.LA(1) ) {
            case TOKEN_RUNNING:
                {
                alt82=1;
                }
                break;
            case TOKEN_STARTED:
                {
                alt82=2;
                }
                break;
            case TOKEN_STOPPED:
                {
                alt82=3;
                }
                break;
            case TOKEN_TERMINATING:
                {
                alt82=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 82, 0, input);

                throw nvae;
            }

            switch (alt82) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:295:7: TOKEN_RUNNING
                    {
                    match(input,TOKEN_RUNNING,FOLLOW_TOKEN_RUNNING_in_inspectorState3174); 
                     state = InspectorStates.JPF_RUNNING; 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:296:7: TOKEN_STARTED
                    {
                    match(input,TOKEN_STARTED,FOLLOW_TOKEN_STARTED_in_inspectorState3192); 
                     state = InspectorStates.JPF_STARTED; 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:297:7: TOKEN_STOPPED
                    {
                    match(input,TOKEN_STOPPED,FOLLOW_TOKEN_STOPPED_in_inspectorState3210); 
                     state = InspectorStates.JPF_STOPPED; 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:298:7: TOKEN_TERMINATING
                    {
                    match(input,TOKEN_TERMINATING,FOLLOW_TOKEN_TERMINATING_in_inspectorState3228); 
                     state = InspectorStates.JPF_TERMINATING; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return state;
    }
    // $ANTLR end "inspectorState"


    // $ANTLR start "cmdRecord"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:301:1: cmdRecord returns [ClientCommand value] : ( TOKEN_RECORD WS TOKEN_CLEAR | TOKEN_RECORD WS TOKEN_PRINT | TOKEN_RECORD WS TOKEN_SAVE WS allText | TOKEN_RECORD WS TOKEN_EXECUTE WS allText );
    public final ClientCommand cmdRecord() throws RecognitionException {
        ClientCommand value = null;

        ConsoleGrammarParser.allText_return allText38 = null;

        ConsoleGrammarParser.allText_return allText39 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:302:5: ( TOKEN_RECORD WS TOKEN_CLEAR | TOKEN_RECORD WS TOKEN_PRINT | TOKEN_RECORD WS TOKEN_SAVE WS allText | TOKEN_RECORD WS TOKEN_EXECUTE WS allText )
            int alt83=4;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==TOKEN_RECORD) ) {
                int LA83_1 = input.LA(2);

                if ( (LA83_1==WS) ) {
                    switch ( input.LA(3) ) {
                    case TOKEN_CLEAR:
                        {
                        alt83=1;
                        }
                        break;
                    case TOKEN_PRINT:
                        {
                        alt83=2;
                        }
                        break;
                    case TOKEN_SAVE:
                        {
                        alt83=3;
                        }
                        break;
                    case TOKEN_EXECUTE:
                        {
                        alt83=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 83, 2, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 83, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;
            }
            switch (alt83) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:302:7: TOKEN_RECORD WS TOKEN_CLEAR
                    {
                    match(input,TOKEN_RECORD,FOLLOW_TOKEN_RECORD_in_cmdRecord3259); 
                    match(input,WS,FOLLOW_WS_in_cmdRecord3261); 
                    match(input,TOKEN_CLEAR,FOLLOW_TOKEN_CLEAR_in_cmdRecord3263); 
                     value = new CmdRecordClear(); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:303:7: TOKEN_RECORD WS TOKEN_PRINT
                    {
                    match(input,TOKEN_RECORD,FOLLOW_TOKEN_RECORD_in_cmdRecord3290); 
                    match(input,WS,FOLLOW_WS_in_cmdRecord3292); 
                    match(input,TOKEN_PRINT,FOLLOW_TOKEN_PRINT_in_cmdRecord3294); 
                     value = new CmdRecordPrint(); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:304:7: TOKEN_RECORD WS TOKEN_SAVE WS allText
                    {
                    match(input,TOKEN_RECORD,FOLLOW_TOKEN_RECORD_in_cmdRecord3321); 
                    match(input,WS,FOLLOW_WS_in_cmdRecord3323); 
                    match(input,TOKEN_SAVE,FOLLOW_TOKEN_SAVE_in_cmdRecord3325); 
                    match(input,WS,FOLLOW_WS_in_cmdRecord3330); 
                    pushFollow(FOLLOW_allText_in_cmdRecord3332);
                    allText38=allText();

                    state._fsp--;

                     value = new CmdRecordSave((allText38!=null?allText38.expr:null)); 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:305:7: TOKEN_RECORD WS TOKEN_EXECUTE WS allText
                    {
                    match(input,TOKEN_RECORD,FOLLOW_TOKEN_RECORD_in_cmdRecord3346); 
                    match(input,WS,FOLLOW_WS_in_cmdRecord3348); 
                    match(input,TOKEN_EXECUTE,FOLLOW_TOKEN_EXECUTE_in_cmdRecord3350); 
                    match(input,WS,FOLLOW_WS_in_cmdRecord3352); 
                    pushFollow(FOLLOW_allText_in_cmdRecord3354);
                    allText39=allText();

                    state._fsp--;

                     value = new CmdRecordExecute((allText39!=null?allText39.expr:null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "cmdRecord"


    // $ANTLR start "cmdAssertions"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:308:1: cmdAssertions returns [ClientCommand value] : ( TOKEN_ASSERTIONS WS mode ( WS b= allText )? | TOKEN_ASSERT WS a= allTextNoWS WS b= allText );
    public final ClientCommand cmdAssertions() throws RecognitionException {
        ClientCommand value = null;

        ConsoleGrammarParser.allText_return b = null;

        ConsoleGrammarParser.allTextNoWS_return a = null;

        boolean mode40 = false;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:309:5: ( TOKEN_ASSERTIONS WS mode ( WS b= allText )? | TOKEN_ASSERT WS a= allTextNoWS WS b= allText )
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( (LA85_0==TOKEN_ASSERTIONS) ) {
                alt85=1;
            }
            else if ( (LA85_0==TOKEN_ASSERT) ) {
                alt85=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 85, 0, input);

                throw nvae;
            }
            switch (alt85) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:309:7: TOKEN_ASSERTIONS WS mode ( WS b= allText )?
                    {
                    match(input,TOKEN_ASSERTIONS,FOLLOW_TOKEN_ASSERTIONS_in_cmdAssertions3381); 
                    match(input,WS,FOLLOW_WS_in_cmdAssertions3383); 
                    pushFollow(FOLLOW_mode_in_cmdAssertions3385);
                    mode40=mode();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:309:41: ( WS b= allText )?
                    int alt84=2;
                    int LA84_0 = input.LA(1);

                    if ( (LA84_0==WS) ) {
                        int LA84_1 = input.LA(2);

                        if ( ((LA84_1>=TOKEN_ALL && LA84_1<=IDF)||(LA84_1>=INT && LA84_1<=SPECIAL_CHAR)) ) {
                            alt84=1;
                        }
                    }
                    switch (alt84) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:309:42: WS b= allText
                            {
                            match(input,WS,FOLLOW_WS_in_cmdAssertions3397); 
                            pushFollow(FOLLOW_allText_in_cmdAssertions3401);
                            b=allText();

                            state._fsp--;


                            }
                            break;

                    }

                     value = new CmdAssertions(mode40, (b!=null?b.expr:null)); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:310:7: TOKEN_ASSERT WS a= allTextNoWS WS b= allText
                    {
                    match(input,TOKEN_ASSERT,FOLLOW_TOKEN_ASSERT_in_cmdAssertions3419); 
                    match(input,WS,FOLLOW_WS_in_cmdAssertions3425); 
                    pushFollow(FOLLOW_allTextNoWS_in_cmdAssertions3429);
                    a=allTextNoWS();

                    state._fsp--;

                    match(input,WS,FOLLOW_WS_in_cmdAssertions3432); 
                    pushFollow(FOLLOW_allText_in_cmdAssertions3436);
                    b=allText();

                    state._fsp--;

                     value = new CmdAssertionsBreakpoint((a!=null?a.expr:null) /* fileName:line */, (b!=null?b.expr:null) /* state expression like  var_i != 10 */); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "cmdAssertions"

    public static class allText_return extends ParserRuleReturnScope {
        public String expr;
    };

    // $ANTLR start "allText"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:313:1: allText returns [String expr] : ( anyWord (b02= allTextWS )? | allNonKeywordsRules (b03= allTextWS )? );
    public final ConsoleGrammarParser.allText_return allText() throws RecognitionException {
        ConsoleGrammarParser.allText_return retval = new ConsoleGrammarParser.allText_return();
        retval.start = input.LT(1);

        ConsoleGrammarParser.allTextWS_return b02 = null;

        ConsoleGrammarParser.allTextWS_return b03 = null;

        String anyWord41 = null;

        ConsoleGrammarParser.allNonKeywordsRules_return allNonKeywordsRules42 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:314:5: ( anyWord (b02= allTextWS )? | allNonKeywordsRules (b03= allTextWS )? )
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( ((LA88_0>=TOKEN_ALL && LA88_0<=IDF)) ) {
                alt88=1;
            }
            else if ( ((LA88_0>=INT && LA88_0<=SPECIAL_CHAR)) ) {
                alt88=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 88, 0, input);

                throw nvae;
            }
            switch (alt88) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:314:7: anyWord (b02= allTextWS )?
                    {
                    pushFollow(FOLLOW_anyWord_in_allText3467);
                    anyWord41=anyWord();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:314:52: (b02= allTextWS )?
                    int alt86=2;
                    int LA86_0 = input.LA(1);

                    if ( (LA86_0==WS) ) {
                        int LA86_1 = input.LA(2);

                        if ( ((LA86_1>=TOKEN_ALL && LA86_1<=IDF)||(LA86_1>=INT && LA86_1<=SPECIAL_CHAR)) ) {
                            alt86=1;
                        }
                    }
                    else if ( ((LA86_0>=TOKEN_ALL && LA86_0<=IDF)||(LA86_0>=INT && LA86_0<=SPECIAL_CHAR)) ) {
                        alt86=1;
                    }
                    switch (alt86) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:314:52: b02= allTextWS
                            {
                            pushFollow(FOLLOW_allTextWS_in_allText3505);
                            b02=allTextWS();

                            state._fsp--;


                            }
                            break;

                    }

                     retval.expr = anyWord41 +                            ((b02!=null?input.toString(b02.start,b02.stop):null)!=null?(b02!=null?input.toString(b02.start,b02.stop):null):""); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:315:7: allNonKeywordsRules (b03= allTextWS )?
                    {
                    pushFollow(FOLLOW_allNonKeywordsRules_in_allText3520);
                    allNonKeywordsRules42=allNonKeywordsRules();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:315:52: (b03= allTextWS )?
                    int alt87=2;
                    int LA87_0 = input.LA(1);

                    if ( (LA87_0==WS) ) {
                        int LA87_1 = input.LA(2);

                        if ( ((LA87_1>=TOKEN_ALL && LA87_1<=IDF)||(LA87_1>=INT && LA87_1<=SPECIAL_CHAR)) ) {
                            alt87=1;
                        }
                    }
                    else if ( ((LA87_0>=TOKEN_ALL && LA87_0<=IDF)||(LA87_0>=INT && LA87_0<=SPECIAL_CHAR)) ) {
                        alt87=1;
                    }
                    switch (alt87) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:315:52: b03= allTextWS
                            {
                            pushFollow(FOLLOW_allTextWS_in_allText3546);
                            b03=allTextWS();

                            state._fsp--;


                            }
                            break;

                    }

                     retval.expr = (allNonKeywordsRules42!=null?input.toString(allNonKeywordsRules42.start,allNonKeywordsRules42.stop):null) +                ((b03!=null?input.toString(b03.start,b03.stop):null)!=null?(b03!=null?input.toString(b03.start,b03.stop):null):""); 

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
        }
        return retval;
    }
    // $ANTLR end "allText"

    public static class allTextNoWS_return extends ParserRuleReturnScope {
        public String expr;
    };

    // $ANTLR start "allTextNoWS"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:318:1: allTextNoWS returns [String expr] : ( anyWord (b02= allTextNoWS )? | allNonKeywordsRules (b03= allTextNoWS )? );
    public final ConsoleGrammarParser.allTextNoWS_return allTextNoWS() throws RecognitionException {
        ConsoleGrammarParser.allTextNoWS_return retval = new ConsoleGrammarParser.allTextNoWS_return();
        retval.start = input.LT(1);

        ConsoleGrammarParser.allTextNoWS_return b02 = null;

        ConsoleGrammarParser.allTextNoWS_return b03 = null;

        String anyWord43 = null;

        ConsoleGrammarParser.allNonKeywordsRules_return allNonKeywordsRules44 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:319:5: ( anyWord (b02= allTextNoWS )? | allNonKeywordsRules (b03= allTextNoWS )? )
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( ((LA91_0>=TOKEN_ALL && LA91_0<=IDF)) ) {
                alt91=1;
            }
            else if ( ((LA91_0>=INT && LA91_0<=SPECIAL_CHAR)) ) {
                alt91=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 91, 0, input);

                throw nvae;
            }
            switch (alt91) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:319:7: anyWord (b02= allTextNoWS )?
                    {
                    pushFollow(FOLLOW_anyWord_in_allTextNoWS3574);
                    anyWord43=anyWord();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:319:52: (b02= allTextNoWS )?
                    int alt89=2;
                    int LA89_0 = input.LA(1);

                    if ( ((LA89_0>=TOKEN_ALL && LA89_0<=IDF)||(LA89_0>=INT && LA89_0<=SPECIAL_CHAR)) ) {
                        alt89=1;
                    }
                    switch (alt89) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:319:52: b02= allTextNoWS
                            {
                            pushFollow(FOLLOW_allTextNoWS_in_allTextNoWS3612);
                            b02=allTextNoWS();

                            state._fsp--;


                            }
                            break;

                    }

                     retval.expr = anyWord43 +                            ((b02!=null?input.toString(b02.start,b02.stop):null)!=null?(b02!=null?input.toString(b02.start,b02.stop):null):""); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:320:7: allNonKeywordsRules (b03= allTextNoWS )?
                    {
                    pushFollow(FOLLOW_allNonKeywordsRules_in_allTextNoWS3627);
                    allNonKeywordsRules44=allNonKeywordsRules();

                    state._fsp--;

                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:320:52: (b03= allTextNoWS )?
                    int alt90=2;
                    int LA90_0 = input.LA(1);

                    if ( ((LA90_0>=TOKEN_ALL && LA90_0<=IDF)||(LA90_0>=INT && LA90_0<=SPECIAL_CHAR)) ) {
                        alt90=1;
                    }
                    switch (alt90) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:320:52: b03= allTextNoWS
                            {
                            pushFollow(FOLLOW_allTextNoWS_in_allTextNoWS3653);
                            b03=allTextNoWS();

                            state._fsp--;


                            }
                            break;

                    }

                     retval.expr = (allNonKeywordsRules44!=null?input.toString(allNonKeywordsRules44.start,allNonKeywordsRules44.stop):null) +                ((b03!=null?input.toString(b03.start,b03.stop):null)!=null?(b03!=null?input.toString(b03.start,b03.stop):null):""); 

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
        }
        return retval;
    }
    // $ANTLR end "allTextNoWS"

    public static class allTextWS_return extends ParserRuleReturnScope {
        public String expr;
    };

    // $ANTLR start "allTextWS"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:323:1: allTextWS returns [String expr] : ( (a= WS )? ) allText ;
    public final ConsoleGrammarParser.allTextWS_return allTextWS() throws RecognitionException {
        ConsoleGrammarParser.allTextWS_return retval = new ConsoleGrammarParser.allTextWS_return();
        retval.start = input.LT(1);

        Token a=null;
        ConsoleGrammarParser.allText_return allText45 = null;


        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:324:5: ( ( (a= WS )? ) allText )
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:324:7: ( (a= WS )? ) allText
            {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:324:7: ( (a= WS )? )
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:324:8: (a= WS )?
            {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:324:9: (a= WS )?
            int alt92=2;
            int LA92_0 = input.LA(1);

            if ( (LA92_0==WS) ) {
                alt92=1;
            }
            switch (alt92) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:324:9: a= WS
                    {
                    a=(Token)match(input,WS,FOLLOW_WS_in_allTextWS3684); 

                    }
                    break;

            }


            }

            pushFollow(FOLLOW_allText_in_allTextWS3688);
            allText45=allText();

            state._fsp--;

             retval.expr = ((a!=null?a.getText():null)!=null?(a!=null?a.getText():null):"") + (allText45!=null?allText45.expr:null); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "allTextWS"

    public static class allNonKeywordsRules_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "allNonKeywordsRules"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:328:1: allNonKeywordsRules : ( allNonKeywordsRulesBase | LESS | HIGH | SIGN_PLUS | SIGN_MINUS | INT | HEX );
    public final ConsoleGrammarParser.allNonKeywordsRules_return allNonKeywordsRules() throws RecognitionException {
        ConsoleGrammarParser.allNonKeywordsRules_return retval = new ConsoleGrammarParser.allNonKeywordsRules_return();
        retval.start = input.LT(1);

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:329:5: ( allNonKeywordsRulesBase | LESS | HIGH | SIGN_PLUS | SIGN_MINUS | INT | HEX )
            int alt93=7;
            switch ( input.LA(1) ) {
            case SIGN_EQUAL:
            case SIGN_DOLLAR:
            case SIGN_DOT:
            case SIGN_HASH:
            case SIGN_COLON:
            case SIGN_LSBRA:
            case SIGN_RSBRA:
            case SIGN_ASTERISK:
            case SIGN_SLASH:
            case SIGN_BACKSLASH:
            case SPECIAL_CHAR:
                {
                alt93=1;
                }
                break;
            case LESS:
                {
                alt93=2;
                }
                break;
            case HIGH:
                {
                alt93=3;
                }
                break;
            case SIGN_PLUS:
                {
                alt93=4;
                }
                break;
            case SIGN_MINUS:
                {
                alt93=5;
                }
                break;
            case INT:
                {
                alt93=6;
                }
                break;
            case HEX:
                {
                alt93=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 93, 0, input);

                throw nvae;
            }

            switch (alt93) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:329:7: allNonKeywordsRulesBase
                    {
                    pushFollow(FOLLOW_allNonKeywordsRulesBase_in_allNonKeywordsRules3711);
                    allNonKeywordsRulesBase();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:330:7: LESS
                    {
                    match(input,LESS,FOLLOW_LESS_in_allNonKeywordsRules3719); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:331:7: HIGH
                    {
                    match(input,HIGH,FOLLOW_HIGH_in_allNonKeywordsRules3727); 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:332:7: SIGN_PLUS
                    {
                    match(input,SIGN_PLUS,FOLLOW_SIGN_PLUS_in_allNonKeywordsRules3735); 

                    }
                    break;
                case 5 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:333:7: SIGN_MINUS
                    {
                    match(input,SIGN_MINUS,FOLLOW_SIGN_MINUS_in_allNonKeywordsRules3743); 

                    }
                    break;
                case 6 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:334:7: INT
                    {
                    match(input,INT,FOLLOW_INT_in_allNonKeywordsRules3751); 

                    }
                    break;
                case 7 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:335:7: HEX
                    {
                    match(input,HEX,FOLLOW_HEX_in_allNonKeywordsRules3759); 

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
        }
        return retval;
    }
    // $ANTLR end "allNonKeywordsRules"

    public static class allNonKeywordsRulesBase_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "allNonKeywordsRulesBase"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:338:1: allNonKeywordsRulesBase : ( signs | specialChar );
    public final ConsoleGrammarParser.allNonKeywordsRulesBase_return allNonKeywordsRulesBase() throws RecognitionException {
        ConsoleGrammarParser.allNonKeywordsRulesBase_return retval = new ConsoleGrammarParser.allNonKeywordsRulesBase_return();
        retval.start = input.LT(1);

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:339:5: ( signs | specialChar )
            int alt94=2;
            int LA94_0 = input.LA(1);

            if ( (LA94_0==SIGN_EQUAL||(LA94_0>=SIGN_DOLLAR && LA94_0<=SIGN_BACKSLASH)) ) {
                alt94=1;
            }
            else if ( (LA94_0==SPECIAL_CHAR) ) {
                alt94=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 94, 0, input);

                throw nvae;
            }
            switch (alt94) {
                case 1 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:339:7: signs
                    {
                    pushFollow(FOLLOW_signs_in_allNonKeywordsRulesBase3776);
                    signs();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:340:7: specialChar
                    {
                    pushFollow(FOLLOW_specialChar_in_allNonKeywordsRulesBase3784);
                    specialChar();

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
        }
        return retval;
    }
    // $ANTLR end "allNonKeywordsRulesBase"


    // $ANTLR start "signs"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:346:1: signs : ( SIGN_DOLLAR | SIGN_DOT | SIGN_EQUAL | SIGN_HASH | SIGN_COLON | SIGN_LSBRA | SIGN_RSBRA | SIGN_ASTERISK | SIGN_SLASH | SIGN_BACKSLASH );
    public final void signs() throws RecognitionException {
        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:346:7: ( SIGN_DOLLAR | SIGN_DOT | SIGN_EQUAL | SIGN_HASH | SIGN_COLON | SIGN_LSBRA | SIGN_RSBRA | SIGN_ASTERISK | SIGN_SLASH | SIGN_BACKSLASH )
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:
            {
            if ( input.LA(1)==SIGN_EQUAL||(input.LA(1)>=SIGN_DOLLAR && input.LA(1)<=SIGN_BACKSLASH) ) {
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
        }
        return ;
    }
    // $ANTLR end "signs"


    // $ANTLR start "intValue"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:372:1: intValue returns [Integer value] : ( ( SIGN_PLUS )? INT | SIGN_MINUS INT | ( SIGN_PLUS )? HEX | SIGN_MINUS HEX );
    public final Integer intValue() throws RecognitionException {
        Integer value = null;

        Token INT46=null;
        Token INT47=null;
        Token HEX48=null;
        Token HEX49=null;

        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:373:5: ( ( SIGN_PLUS )? INT | SIGN_MINUS INT | ( SIGN_PLUS )? HEX | SIGN_MINUS HEX )
            int alt97=4;
            switch ( input.LA(1) ) {
            case SIGN_PLUS:
                {
                int LA97_1 = input.LA(2);

                if ( (LA97_1==INT) ) {
                    alt97=1;
                }
                else if ( (LA97_1==HEX) ) {
                    alt97=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 97, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
                {
                alt97=1;
                }
                break;
            case SIGN_MINUS:
                {
                int LA97_3 = input.LA(2);

                if ( (LA97_3==INT) ) {
                    alt97=2;
                }
                else if ( (LA97_3==HEX) ) {
                    alt97=4;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 97, 3, input);

                    throw nvae;
                }
                }
                break;
            case HEX:
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
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:373:7: ( SIGN_PLUS )? INT
                    {
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:373:7: ( SIGN_PLUS )?
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( (LA95_0==SIGN_PLUS) ) {
                        alt95=1;
                    }
                    switch (alt95) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:373:7: SIGN_PLUS
                            {
                            match(input,SIGN_PLUS,FOLLOW_SIGN_PLUS_in_intValue4067); 

                            }
                            break;

                    }

                    INT46=(Token)match(input,INT,FOLLOW_INT_in_intValue4070); 
                     value =  Integer.valueOf((INT46!=null?INT46.getText():null)); 

                    }
                    break;
                case 2 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:374:7: SIGN_MINUS INT
                    {
                    match(input,SIGN_MINUS,FOLLOW_SIGN_MINUS_in_intValue4080); 
                    INT47=(Token)match(input,INT,FOLLOW_INT_in_intValue4082); 
                     value = -Integer.valueOf((INT47!=null?INT47.getText():null)); 

                    }
                    break;
                case 3 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:375:7: ( SIGN_PLUS )? HEX
                    {
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:375:7: ( SIGN_PLUS )?
                    int alt96=2;
                    int LA96_0 = input.LA(1);

                    if ( (LA96_0==SIGN_PLUS) ) {
                        alt96=1;
                    }
                    switch (alt96) {
                        case 1 :
                            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:375:7: SIGN_PLUS
                            {
                            match(input,SIGN_PLUS,FOLLOW_SIGN_PLUS_in_intValue4092); 

                            }
                            break;

                    }

                    HEX48=(Token)match(input,HEX,FOLLOW_HEX_in_intValue4095); 
                     value =  Integer.valueOf(((HEX48!=null?HEX48.getText():null)).substring(2), 16); 

                    }
                    break;
                case 4 :
                    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:376:7: SIGN_MINUS HEX
                    {
                    match(input,SIGN_MINUS,FOLLOW_SIGN_MINUS_in_intValue4105); 
                    HEX49=(Token)match(input,HEX,FOLLOW_HEX_in_intValue4107); 
                     value = -Integer.valueOf(((HEX49!=null?HEX49.getText():null)).substring(2), 16); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "intValue"


    // $ANTLR start "specialChar"
    // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:383:1: specialChar : SPECIAL_CHAR ;
    public final void specialChar() throws RecognitionException {
        try {
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:383:13: ( SPECIAL_CHAR )
            // D:\\gsoc\\jpf-inspector\\src\\main\\gov\\nasa\\jpf\\inspector\\client\\parser/ConsoleGrammar.g:383:15: SPECIAL_CHAR
            {
            match(input,SPECIAL_CHAR,FOLLOW_SPECIAL_CHAR_in_specialChar4155); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "specialChar"

    // Delegated rules


    protected DFA81 dfa81 = new DFA81(this);
    static final String DFA81_eotS =
        "\13\uffff";
    static final String DFA81_eofS =
        "\1\uffff\1\11\6\uffff\1\11\2\uffff";
    static final String DFA81_minS =
        "\1\62\1\41\6\uffff\1\41\2\uffff";
    static final String DFA81_maxS =
        "\1\70\1\72\6\uffff\1\41\2\uffff";
    static final String DFA81_acceptS =
        "\2\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\uffff\1\1\1\2";
    static final String DFA81_specialS =
        "\13\uffff}>";
    static final String[] DFA81_transitionS = {
            "\1\1\1\2\1\3\1\4\1\5\1\6\1\7",
            "\1\12\30\uffff\1\10",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\12",
            "",
            ""
    };

    static final short[] DFA81_eot = DFA.unpackEncodedString(DFA81_eotS);
    static final short[] DFA81_eof = DFA.unpackEncodedString(DFA81_eofS);
    static final char[] DFA81_min = DFA.unpackEncodedStringToUnsignedChars(DFA81_minS);
    static final char[] DFA81_max = DFA.unpackEncodedStringToUnsignedChars(DFA81_maxS);
    static final short[] DFA81_accept = DFA.unpackEncodedString(DFA81_acceptS);
    static final short[] DFA81_special = DFA.unpackEncodedString(DFA81_specialS);
    static final short[][] DFA81_transition;

    static {
        int numStates = DFA81_transitionS.length;
        DFA81_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA81_transition[i] = DFA.unpackEncodedString(DFA81_transitionS[i]);
        }
    }

    class DFA81 extends DFA {

        public DFA81(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 81;
            this.eot = DFA81_eot;
            this.eof = DFA81_eof;
            this.min = DFA81_min;
            this.max = DFA81_max;
            this.accept = DFA81_accept;
            this.special = DFA81_special;
            this.transition = DFA81_transition;
        }
        public String getDescription() {
            return "283:1: cmdCallback returns [ClientCommand value] : ( TOKEN_CB_STATE_CHANGE | TOKEN_CB_STATE_CHANGE ( WS )? TOKEN_STATE ( WS )? '=' ( WS )? inspectorState | TOKEN_CB_GENERIC_ERROR | TOKEN_CB_GENERIC_INFO | TOKEN_CB_BREAKPOINT_HIT | TOKEN_CB_CG_NEW_CHOICE | TOKEN_CB_CG_CHOICE_TO_USE | TOKEN_CB_CG_USED_CHOICE );";
        }
    }
 

    public static final BitSet FOLLOW_IDF_in_anyWord639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allKeyWords_in_anyWord675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_HIT_COUNT_in_allKeyWords722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allKeyWordsWithoutHitCountBP_in_allKeyWords744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_NAME_in_allKeyWordsWithoutHitCountBP770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STATE_in_allKeyWordsWithoutHitCountBP799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allKeyWordsWithoutCreateBPandHitCount_in_allKeyWordsWithoutHitCountBP825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_allKeyWordsWithoutCreateBPandHitCount0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clientCommands_in_clientCommandWithCB1262 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_clientCommandWithCB1266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommandWithCB1274 = new BitSet(new long[]{0x01FC000000000000L});
    public static final BitSet FOLLOW_cmdCallback_in_clientCommandWithCB1277 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommandWithCB1279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands1303 = new BitSet(new long[]{0x0003FF68A385ADC0L});
    public static final BitSet FOLLOW_clientCommands1_in_clientCommands1306 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_clientCommands1308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_RUN_in_clientCommands11331 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CONTINUE_in_clientCommands11357 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_BREAK_in_clientCommands11378 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cmdBreakpoints_in_clientCommands11402 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cmdSingleSteps_in_clientCommands11423 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cmdProgramState_in_clientCommands11444 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cmdChoiceGenerators_in_clientCommands11464 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cmdRecord_in_clientCommands11480 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cmdAssertions_in_clientCommands11506 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_clientCommands11517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_SHOW_in_cmdBreakpoints1542 = new BitSet(new long[]{0x0400000000000200L});
    public static final BitSet FOLLOW_WS_in_cmdBreakpoints1546 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_TOKEN_BREAKPOINT_in_cmdBreakpoints1549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_DELETE_in_cmdBreakpoints1571 = new BitSet(new long[]{0x0400000000000200L});
    public static final BitSet FOLLOW_WS_in_cmdBreakpoints1573 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_TOKEN_BREAKPOINT_in_cmdBreakpoints1576 = new BitSet(new long[]{0x0C00000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdBreakpoints1578 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_INT_in_cmdBreakpoints1581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CREATE_in_cmdBreakpoints1595 = new BitSet(new long[]{0x0400000000000200L});
    public static final BitSet FOLLOW_WS_in_cmdBreakpoints1597 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_TOKEN_BREAKPOINT_in_cmdBreakpoints1600 = new BitSet(new long[]{0x8C00000200500000L,0x0000000000000003L});
    public static final BitSet FOLLOW_WS_in_cmdBreakpoints1618 = new BitSet(new long[]{0x8C00000200500000L,0x0000000000000003L});
    public static final BitSet FOLLOW_cmdCreateBP_in_cmdBreakpoints1621 = new BitSet(new long[]{0x8C00000200500000L,0x0000000000000003L});
    public static final BitSet FOLLOW_WS_in_cmdBreakpoints1626 = new BitSet(new long[]{0x13FFFFFDFFAFFFF0L,0x0000000000000FFCL});
    public static final BitSet FOLLOW_bpExpression_in_cmdBreakpoints1628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_NAME_in_cmdCreateBP1649 = new BitSet(new long[]{0x1400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1652 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_SIGN_EQUAL_in_cmdCreateBP1655 = new BitSet(new long[]{0x07FFFFFFFFFFFFF0L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1657 = new BitSet(new long[]{0x07FFFFFFFFFFFFF0L});
    public static final BitSet FOLLOW_anyWord_in_cmdCreateBP1660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STATE_in_cmdCreateBP1670 = new BitSet(new long[]{0x1400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1672 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_SIGN_EQUAL_in_cmdCreateBP1675 = new BitSet(new long[]{0x0400000000250000L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1677 = new BitSet(new long[]{0x0400000000250000L});
    public static final BitSet FOLLOW_cmdBreakpointsStates_in_cmdCreateBP1680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_intValue_in_cmdCreateBP1693 = new BitSet(new long[]{0x2400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1696 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_LESS_in_cmdCreateBP1701 = new BitSet(new long[]{0x0400000000100000L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1703 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_TOKEN_HIT_COUNT_in_cmdCreateBP1708 = new BitSet(new long[]{0x2400000000000002L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1711 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_LESS_in_cmdCreateBP1716 = new BitSet(new long[]{0x8C00000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_WS_in_cmdCreateBP1718 = new BitSet(new long[]{0x8800000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdCreateBP1723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_DISABLE_in_cmdBreakpointsStates1748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_LOG_in_cmdBreakpointsStates1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_ENABLE_in_cmdBreakpointsStates1788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allKeyWordsWithoutCreateBPandHitCount_in_bpExpression1823 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextWS_in_bpExpression1831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allNonKeywordsRulesBase_in_bpExpression1849 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextWS_in_bpExpression1871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDF_in_bpExpression1889 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextWS_in_bpExpression1931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STEP_INSTRUCTION_in_cmdSingleSteps1963 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps1986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STEP_OVER_in_cmdSingleSteps1998 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STEP_IN_in_cmdSingleSteps2040 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STEP_OUT_in_cmdSingleSteps2084 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STEP_TRANSITION_in_cmdSingleSteps2127 = new BitSet(new long[]{0x8C00000040004012L,0x0000000000000003L});
    public static final BitSet FOLLOW_WS_in_cmdSingleSteps2130 = new BitSet(new long[]{0x0400000040004010L});
    public static final BitSet FOLLOW_cgType_in_cmdSingleSteps2135 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_BACK_STEP_INSTRUCTION_in_cmdSingleSteps2157 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_BACK_STEP_OVER_in_cmdSingleSteps2187 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_BACK_STEP_IN_in_cmdSingleSteps2224 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_BACK_STEP_OUT_in_cmdSingleSteps2263 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_BACK_STEP_TRANSITION_in_cmdSingleSteps2301 = new BitSet(new long[]{0x8C00000040004012L,0x0000000000000003L});
    public static final BitSet FOLLOW_WS_in_cmdSingleSteps2304 = new BitSet(new long[]{0x0400000040004010L});
    public static final BitSet FOLLOW_cgType_in_cmdSingleSteps2309 = new BitSet(new long[]{0x8800000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdSingleSteps2314 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_THREAD_in_cmdProgramState2341 = new BitSet(new long[]{0x8C00000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_WS_in_cmdProgramState2347 = new BitSet(new long[]{0x8800000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdProgramState2350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_PRINT_in_cmdProgramState2362 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_WS_in_cmdProgramState2369 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allText_in_cmdProgramState2372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_THREAD_PC_in_cmdProgramState2385 = new BitSet(new long[]{0x8C00000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_WS_in_cmdProgramState2388 = new BitSet(new long[]{0x8800000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cmdProgramState2391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_SET_in_cmdProgramState2403 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_WS_in_cmdProgramState2412 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allText_in_cmdProgramState2415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_USED_in_cmdChoiceGenerators2444 = new BitSet(new long[]{0x0400000000000400L});
    public static final BitSet FOLLOW_WS_in_cmdChoiceGenerators2446 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_TOKEN_CHOICE_GENERATORS_in_cmdChoiceGenerators2449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mode_in_cmdChoiceGenerators2461 = new BitSet(new long[]{0x0400000040804430L});
    public static final BitSet FOLLOW_WS_in_cmdChoiceGenerators2464 = new BitSet(new long[]{0x0400000040804430L});
    public static final BitSet FOLLOW_cgMode_in_cmdChoiceGenerators2470 = new BitSet(new long[]{0x0400000040004410L});
    public static final BitSet FOLLOW_WS_in_cmdChoiceGenerators2472 = new BitSet(new long[]{0x0400000040004410L});
    public static final BitSet FOLLOW_cgType_in_cmdChoiceGenerators2480 = new BitSet(new long[]{0x0400000000000400L});
    public static final BitSet FOLLOW_WS_in_cmdChoiceGenerators2482 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_TOKEN_CHOICE_GENERATORS_in_cmdChoiceGenerators2487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CHOICE_GENERATORS_in_cmdChoiceGenerators2498 = new BitSet(new long[]{0x0400000010000000L});
    public static final BitSet FOLLOW_WS_in_cmdChoiceGenerators2500 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_TOKEN_SELECT_in_cmdChoiceGenerators2503 = new BitSet(new long[]{0x8C00000000000000L,0x0000000000000103L});
    public static final BitSet FOLLOW_cgChoice_in_cmdChoiceGenerators2505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_SCHEDULING_in_cgType2528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_DATA_in_cgType2540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_ALL_in_cgType2558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_ASK_in_cgMode2591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_PRINT_in_cgMode2606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_ENABLE_in_mode2632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_DISABLE_in_mode2644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_cgChoice2692 = new BitSet(new long[]{0x8800000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_intValue_in_cgChoice2695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_cgChoice2708 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_SIGN_ASTERISK_in_cgChoice2711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_STATE_CHANGE_in_cmdCallback2742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_STATE_CHANGE_in_cmdCallback2799 = new BitSet(new long[]{0x0400000200000000L});
    public static final BitSet FOLLOW_WS_in_cmdCallback2801 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_TOKEN_STATE_in_cmdCallback2804 = new BitSet(new long[]{0x1400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdCallback2806 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_SIGN_EQUAL_in_cmdCallback2809 = new BitSet(new long[]{0x0400001504000000L});
    public static final BitSet FOLLOW_WS_in_cmdCallback2811 = new BitSet(new long[]{0x0400001504000000L});
    public static final BitSet FOLLOW_inspectorState_in_cmdCallback2814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_GENERIC_ERROR_in_cmdCallback2828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_GENERIC_INFO_in_cmdCallback2884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_BREAKPOINT_HIT_in_cmdCallback2942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_CG_NEW_CHOICE_in_cmdCallback2997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_CG_CHOICE_TO_USE_in_cmdCallback3053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_CB_CG_USED_CHOICE_in_cmdCallback3106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_RUNNING_in_inspectorState3174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STARTED_in_inspectorState3192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_STOPPED_in_inspectorState3210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_TERMINATING_in_inspectorState3228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_RECORD_in_cmdRecord3259 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdRecord3261 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_TOKEN_CLEAR_in_cmdRecord3263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_RECORD_in_cmdRecord3290 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdRecord3292 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_TOKEN_PRINT_in_cmdRecord3294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_RECORD_in_cmdRecord3321 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdRecord3323 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_TOKEN_SAVE_in_cmdRecord3325 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdRecord3330 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allText_in_cmdRecord3332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_RECORD_in_cmdRecord3346 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdRecord3348 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_TOKEN_EXECUTE_in_cmdRecord3350 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdRecord3352 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allText_in_cmdRecord3354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_ASSERTIONS_in_cmdAssertions3381 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdAssertions3383 = new BitSet(new long[]{0x0000000000050000L});
    public static final BitSet FOLLOW_mode_in_cmdAssertions3385 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_WS_in_cmdAssertions3397 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allText_in_cmdAssertions3401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_ASSERT_in_cmdAssertions3419 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdAssertions3425 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextNoWS_in_cmdAssertions3429 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_WS_in_cmdAssertions3432 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allText_in_cmdAssertions3436 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_anyWord_in_allText3467 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextWS_in_allText3505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allNonKeywordsRules_in_allText3520 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextWS_in_allText3546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_anyWord_in_allTextNoWS3574 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextNoWS_in_allTextNoWS3612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allNonKeywordsRules_in_allTextNoWS3627 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF2L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allTextNoWS_in_allTextNoWS3653 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_allTextWS3684 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x0000000000000FFFL});
    public static final BitSet FOLLOW_allText_in_allTextWS3688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_allNonKeywordsRulesBase_in_allNonKeywordsRules3711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LESS_in_allNonKeywordsRules3719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HIGH_in_allNonKeywordsRules3727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_PLUS_in_allNonKeywordsRules3735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_MINUS_in_allNonKeywordsRules3743 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_allNonKeywordsRules3751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HEX_in_allNonKeywordsRules3759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signs_in_allNonKeywordsRulesBase3776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_specialChar_in_allNonKeywordsRulesBase3784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_signs0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_PLUS_in_intValue4067 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_INT_in_intValue4070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_MINUS_in_intValue4080 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_INT_in_intValue4082 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_PLUS_in_intValue4092 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_HEX_in_intValue4095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_MINUS_in_intValue4105 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_HEX_in_intValue4107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SPECIAL_CHAR_in_specialChar4155 = new BitSet(new long[]{0x0000000000000002L});

}