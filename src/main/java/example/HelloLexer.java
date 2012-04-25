// $ANTLR 3.4 example/Hello.g 2012-04-25 22:00:15
package example;

import org.antlr.runtime.*;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class HelloLexer extends Lexer {
    public static final int EOF = -1;
    public static final int T__5 = 5;
    public static final int T__6 = 6;
    public static final int COMMA = 4;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[]{};
    }

    public HelloLexer() {
    }

    public HelloLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public HelloLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "example/Hello.g";
    }

    // $ANTLR start "T__5"
    public final void mT__5() throws RecognitionException {
        try {
            int _type = T__5;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // example/Hello.g:4:6: ( 'Hello' )
            // example/Hello.g:4:8: 'Hello'
            {
                match("Hello");


            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__5"

    // $ANTLR start "T__6"
    public final void mT__6() throws RecognitionException {
        try {
            int _type = T__6;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // example/Hello.g:5:6: ( 'World' )
            // example/Hello.g:5:8: 'World'
            {
                match("World");


            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__6"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // example/Hello.g:11:9: ( ',' )
            // example/Hello.g:11:17: ','
            {
                match(',');

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "COMMA"

    public void mTokens() throws RecognitionException {
        // example/Hello.g:1:8: ( T__5 | T__6 | COMMA )
        int alt1 = 3;
        switch (input.LA(1)) {
            case 'H': {
                alt1 = 1;
            }
            break;
            case 'W': {
                alt1 = 2;
            }
            break;
            case ',': {
                alt1 = 3;
            }
            break;
            default:
                NoViableAltException nvae =
                        new NoViableAltException("", 1, 0, input);

                throw nvae;

        }

        switch (alt1) {
            case 1:
                // example/Hello.g:1:10: T__5
            {
                mT__5();


            }
            break;
            case 2:
                // example/Hello.g:1:15: T__6
            {
                mT__6();


            }
            break;
            case 3:
                // example/Hello.g:1:20: COMMA
            {
                mCOMMA();


            }
            break;

        }

    }


}