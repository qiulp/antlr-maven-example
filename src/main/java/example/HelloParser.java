// $ANTLR 3.4 example/Hello.g 2012-04-25 22:00:15
package example;

import org.antlr.runtime.*;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class HelloParser extends Parser {
    public static final String[] tokenNames = new String[]{
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMMA", "'Hello'", "'World'"
    };

    public static final int EOF = -1;
    public static final int T__5 = 5;
    public static final int T__6 = 6;
    public static final int COMMA = 4;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[]{};
    }

    // delegators


    public HelloParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public HelloParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() {
        return HelloParser.tokenNames;
    }

    public String getGrammarFileName() {
        return "example/Hello.g";
    }


    // $ANTLR start "greet"
    // example/Hello.g:7:1: greet : 'Hello' COMMA 'World' ;
    public final void greet() throws RecognitionException {
        try {
            // example/Hello.g:8:9: ( 'Hello' COMMA 'World' )
            // example/Hello.g:8:17: 'Hello' COMMA 'World'
            {
                match(input, 5, FOLLOW_5_in_greet39);

                match(input, COMMA, FOLLOW_COMMA_in_greet41);

                match(input, 6, FOLLOW_6_in_greet43);

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return;
    }
    // $ANTLR end "greet"

    // Delegated rules


    public static final BitSet FOLLOW_5_in_greet39 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_COMMA_in_greet41 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_6_in_greet43 = new BitSet(new long[]{0x0000000000000002L});

}