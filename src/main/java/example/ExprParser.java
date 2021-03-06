// $ANTLR 3.4 example/Expr.g 2012-04-25 22:11:22
package example;

import org.antlr.runtime.*;

import java.util.HashMap;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class ExprParser extends Parser {
    public static final String[] tokenNames = new String[]{
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ID", "INT", "NEWLINE", "WS", "'('", "')'", "'*'", "'+'", "'-'", "'='"
    };

    public static final int EOF = -1;
    public static final int T__8 = 8;
    public static final int T__9 = 9;
    public static final int T__10 = 10;
    public static final int T__11 = 11;
    public static final int T__12 = 12;
    public static final int T__13 = 13;
    public static final int ID = 4;
    public static final int INT = 5;
    public static final int NEWLINE = 6;
    public static final int WS = 7;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[]{};
    }

    // delegators


    public ExprParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public ExprParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() {
        return ExprParser.tokenNames;
    }

    public String getGrammarFileName() {
        return "example/Expr.g";
    }


    /**
     * Map variable name to Integer object holding value
     */
    HashMap memory = new HashMap();


    // $ANTLR start "prog"
    // example/Expr.g:16:1: prog : ( stat )+ ;
    public final void prog() throws RecognitionException {
        try {
            // example/Expr.g:16:5: ( ( stat )+ )
            // example/Expr.g:16:9: ( stat )+
            {
                // example/Expr.g:16:9: ( stat )+
                int cnt1 = 0;
                loop1:
                do {
                    int alt1 = 2;
                    switch (input.LA(1)) {
                        case ID:
                        case INT:
                        case NEWLINE:
                        case 8: {
                            alt1 = 1;
                        }
                        break;

                    }

                    switch (alt1) {
                        case 1:
                            // example/Expr.g:16:9: stat
                        {
                            pushFollow(FOLLOW_stat_in_prog31);
                            stat();

                            state._fsp--;


                        }
                        break;

                        default:
                            if (cnt1 >= 1) break loop1;
                            EarlyExitException eee =
                                    new EarlyExitException(1, input);
                            throw eee;
                    }
                    cnt1++;
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return;
    }
    // $ANTLR end "prog"


    // $ANTLR start "stat"
    // example/Expr.g:18:1: stat : ( expr NEWLINE | ID '=' expr NEWLINE | NEWLINE );
    public final void stat() throws RecognitionException {
        Token ID2 = null;
        int expr1 = 0;

        int expr3 = 0;


        try {
            // example/Expr.g:18:5: ( expr NEWLINE | ID '=' expr NEWLINE | NEWLINE )
            int alt2 = 3;
            switch (input.LA(1)) {
                case INT:
                case 8: {
                    alt2 = 1;
                }
                break;
                case ID: {
                    switch (input.LA(2)) {
                        case 13: {
                            alt2 = 2;
                        }
                        break;
                        case NEWLINE:
                        case 10:
                        case 11:
                        case 12: {
                            alt2 = 1;
                        }
                        break;
                        default:
                            NoViableAltException nvae =
                                    new NoViableAltException("", 2, 2, input);

                            throw nvae;

                    }

                }
                break;
                case NEWLINE: {
                    alt2 = 3;
                }
                break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 2, 0, input);

                    throw nvae;

            }

            switch (alt2) {
                case 1:
                    // example/Expr.g:18:9: expr NEWLINE
                {
                    pushFollow(FOLLOW_expr_in_stat42);
                    expr1 = expr();

                    state._fsp--;


                    match(input, NEWLINE, FOLLOW_NEWLINE_in_stat44);

                    System.out.println(expr1);

                }
                break;
                case 2:
                    // example/Expr.g:19:9: ID '=' expr NEWLINE
                {
                    ID2 = (Token) match(input, ID, FOLLOW_ID_in_stat56);

                    match(input, 13, FOLLOW_13_in_stat58);

                    pushFollow(FOLLOW_expr_in_stat60);
                    expr3 = expr();

                    state._fsp--;


                    match(input, NEWLINE, FOLLOW_NEWLINE_in_stat62);

                    memory.put((ID2 != null ? ID2.getText() : null), new Integer(expr3));

                }
                break;
                case 3:
                    // example/Expr.g:21:9: NEWLINE
                {
                    match(input, NEWLINE, FOLLOW_NEWLINE_in_stat82);

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return;
    }
    // $ANTLR end "stat"


    // $ANTLR start "expr"
    // example/Expr.g:24:1: expr returns [int value] : e= multExpr ( '+' e= multExpr | '-' e= multExpr )* ;
    public final int expr() throws RecognitionException {
        int value = 0;


        int e = 0;


        try {
            // example/Expr.g:25:5: (e= multExpr ( '+' e= multExpr | '-' e= multExpr )* )
            // example/Expr.g:25:9: e= multExpr ( '+' e= multExpr | '-' e= multExpr )*
            {
                pushFollow(FOLLOW_multExpr_in_expr107);
                e = multExpr();

                state._fsp--;


                value = e;

                // example/Expr.g:26:9: ( '+' e= multExpr | '-' e= multExpr )*
                loop3:
                do {
                    int alt3 = 3;
                    switch (input.LA(1)) {
                        case 11: {
                            alt3 = 1;
                        }
                        break;
                        case 12: {
                            alt3 = 2;
                        }
                        break;

                    }

                    switch (alt3) {
                        case 1:
                            // example/Expr.g:26:13: '+' e= multExpr
                        {
                            match(input, 11, FOLLOW_11_in_expr123);

                            pushFollow(FOLLOW_multExpr_in_expr127);
                            e = multExpr();

                            state._fsp--;


                            value += e;

                        }
                        break;
                        case 2:
                            // example/Expr.g:27:13: '-' e= multExpr
                        {
                            match(input, 12, FOLLOW_12_in_expr143);

                            pushFollow(FOLLOW_multExpr_in_expr147);
                            e = multExpr();

                            state._fsp--;


                            value -= e;

                        }
                        break;

                        default:
                            break loop3;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "expr"


    // $ANTLR start "multExpr"
    // example/Expr.g:31:1: multExpr returns [int value] : e= atom ( '*' e= atom )* ;
    public final int multExpr() throws RecognitionException {
        int value = 0;


        int e = 0;


        try {
            // example/Expr.g:32:5: (e= atom ( '*' e= atom )* )
            // example/Expr.g:32:9: e= atom ( '*' e= atom )*
            {
                pushFollow(FOLLOW_atom_in_multExpr185);
                e = atom();

                state._fsp--;


                value = e;

                // example/Expr.g:32:37: ( '*' e= atom )*
                loop4:
                do {
                    int alt4 = 2;
                    switch (input.LA(1)) {
                        case 10: {
                            alt4 = 1;
                        }
                        break;

                    }

                    switch (alt4) {
                        case 1:
                            // example/Expr.g:32:38: '*' e= atom
                        {
                            match(input, 10, FOLLOW_10_in_multExpr190);

                            pushFollow(FOLLOW_atom_in_multExpr194);
                            e = atom();

                            state._fsp--;


                            value *= e;

                        }
                        break;

                        default:
                            break loop4;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "multExpr"


    // $ANTLR start "atom"
    // example/Expr.g:35:1: atom returns [int value] : ( INT | ID | '(' expr ')' );
    public final int atom() throws RecognitionException {
        int value = 0;


        Token INT4 = null;
        Token ID5 = null;
        int expr6 = 0;


        try {
            // example/Expr.g:36:5: ( INT | ID | '(' expr ')' )
            int alt5 = 3;
            switch (input.LA(1)) {
                case INT: {
                    alt5 = 1;
                }
                break;
                case ID: {
                    alt5 = 2;
                }
                break;
                case 8: {
                    alt5 = 3;
                }
                break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 5, 0, input);

                    throw nvae;

            }

            switch (alt5) {
                case 1:
                    // example/Expr.g:36:9: INT
                {
                    INT4 = (Token) match(input, INT, FOLLOW_INT_in_atom221);

                    value = Integer.parseInt((INT4 != null ? INT4.getText() : null));

                }
                break;
                case 2:
                    // example/Expr.g:37:9: ID
                {
                    ID5 = (Token) match(input, ID, FOLLOW_ID_in_atom233);


                    Integer v = (Integer) memory.get((ID5 != null ? ID5.getText() : null));
                    if (v != null) value = v.intValue();
                    else System.err.println("undefined variable " + (ID5 != null ? ID5.getText() : null));


                }
                break;
                case 3:
                    // example/Expr.g:43:9: '(' expr ')'
                {
                    match(input, 8, FOLLOW_8_in_atom253);

                    pushFollow(FOLLOW_expr_in_atom255);
                    expr6 = expr();

                    state._fsp--;


                    match(input, 9, FOLLOW_9_in_atom257);

                    value = expr6;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "atom"

    // Delegated rules


    public static final BitSet FOLLOW_stat_in_prog31 = new BitSet(new long[]{0x0000000000000172L});
    public static final BitSet FOLLOW_expr_in_stat42 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NEWLINE_in_stat44 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_stat56 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_stat58 = new BitSet(new long[]{0x0000000000000130L});
    public static final BitSet FOLLOW_expr_in_stat60 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NEWLINE_in_stat62 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEWLINE_in_stat82 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multExpr_in_expr107 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_11_in_expr123 = new BitSet(new long[]{0x0000000000000130L});
    public static final BitSet FOLLOW_multExpr_in_expr127 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_12_in_expr143 = new BitSet(new long[]{0x0000000000000130L});
    public static final BitSet FOLLOW_multExpr_in_expr147 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_atom_in_multExpr185 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_10_in_multExpr190 = new BitSet(new long[]{0x0000000000000130L});
    public static final BitSet FOLLOW_atom_in_multExpr194 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_INT_in_atom221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_8_in_atom253 = new BitSet(new long[]{0x0000000000000130L});
    public static final BitSet FOLLOW_expr_in_atom255 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_9_in_atom257 = new BitSet(new long[]{0x0000000000000002L});

}