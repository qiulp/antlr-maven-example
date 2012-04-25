// $ANTLR 3.4 example/Java.g 2012-04-25 22:26:38
package example;

import org.antlr.runtime.*;

import java.util.HashMap;

/**
 * A Java 1.5 grammar for ANTLR v3 derived from the spec
 * <p/>
 * This is a very close representation of the spec; the changes
 * are comestic (remove left recursion) and also fixes (the spec
 * isn't exactly perfect).  I have run this on the 1.4.2 source
 * and some nasty looking enums from 1.5, but have not really
 * tested for 1.5 compatibility.
 * <p/>
 * I built this with: java -Xmx100M org.antlr.Tool java.g
 * and got two errors that are ok (for now):
 * java.g:691:9: Decision can match input such as
 * "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 * using multiple alternatives: 3, 4
 * As a result, alternative(s) 4 were disabled for that input
 * java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 * '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 * '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 * '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 * using multiple alternatives: 1, 2
 * As a result, alternative(s) 2 were disabled for that input
 * <p/>
 * You can turn enum on/off as a keyword :)
 * <p/>
 * Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 * <p/>
 * Primary author: Terence Parr, July 2006
 * <p/>
 * Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 * October 25, 2006;
 * fixed normalInterfaceDeclaration: now uses typeParameters instead
 * of typeParameter (according to JLS, 3rd edition)
 * fixed castExpression: no longer allows expression next to type
 * (according to semantics in JLS, in contrast with syntax in JLS)
 * <p/>
 * Version 1.0.2 -- Terence Parr, Nov 27, 2006
 * java spec I built this from had some bizarre for-loop control.
 * Looked weird and so I looked elsewhere...Yep, it's messed up.
 * simplified.
 * <p/>
 * Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 * Factored out an annotationName rule and used it in the annotation rule.
 * Not sure why, but typeName wasn't recognizing references to inner
 * annotations (e.g. @InterfaceName.InnerAnnotation())
 * Factored out the elementValue section of an annotation reference.  Created
 * elementValuePair and elementValuePairs rules, then used them in the
 * annotation rule.  Allows it to recognize annotation references with
 * multiple, comma separated attributes.
 * Updated elementValueArrayInitializer so that it allows multiple elements.
 * (It was only allowing 0 or 1 element).
 * Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 * doesn't appear to indicate this is legal, but it does work as of at least
 * JDK 1.5.0_06.
 * Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 * Because annotationConstantRest already references variableDeclarator which
 * has the Identifier portion in it, the parser would fail on constants in
 * annotation definitions because it expected two identifiers.
 * Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 * Wouldn't handle an inner interface that has a trailing ';'.
 * Swapped the expression and type rule reference order in castExpression to
 * make it check for genericized casts first.  It was failing to recognize a
 * statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 * 'Class<Byte' in the cast expression as a less than expression, then failing
 * on the '>'.
 * Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 * Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 * just 'arguments'.  The case it couldn't handle was a call to an explicit
 * generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 * may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 * <p/>
 * Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 * <p/>
 * Fixed formalParameterDecls, localVariableDeclaration, forInit,
 * and forVarControl to use variableModifier* not 'final'? (annotation)?
 * <p/>
 * Version 1.0.5 -- Terence, June 21, 2007
 * --a[i].foo didn't work. Fixed unaryExpression
 * <p/>
 * Version 1.0.6 -- John Ridgway, March 17, 2008
 * Made "assert" a switchable keyword like "enum".
 * Fixed compilationUnit to disallow "annotation importDeclaration ...".
 * Changed "Identifier ('.' Identifier)*" to "qualifiedName" in more
 * places.
 * Changed modifier* and/or variableModifier* to classOrInterfaceModifiers,
 * modifiers or variableModifiers, as appropriate.
 * Renamed "bound" to "typeBound" to better match language in the JLS.
 * Added "memberDeclaration" which rewrites to methodDeclaration or
 * fieldDeclaration and pulled type into memberDeclaration.  So we parse
 * type and then move on to decide whether we're dealing with a field
 * or a method.
 * Modified "constructorDeclaration" to use "constructorBody" instead of
 * "methodBody".  constructorBody starts with explicitConstructorInvocation,
 * then goes on to blockStatement*.  Pulling explicitConstructorInvocation
 * out of expressions allowed me to simplify "primary".
 * Changed variableDeclarator to simplify it.
 * Changed type to use classOrInterfaceType, thus simplifying it; of course
 * I then had to add classOrInterfaceType, but it is used in several
 * places.
 * Fixed annotations, old version allowed "@X(y,z)", which is illegal.
 * Added optional comma to end of "elementValueArrayInitializer"; as per JLS.
 * Changed annotationTypeElementRest to use normalClassDeclaration and
 * normalInterfaceDeclaration rather than classDeclaration and
 * interfaceDeclaration, thus getting rid of a couple of grammar ambiguities.
 * Split localVariableDeclaration into localVariableDeclarationStatement
 * (includes the terminating semi-colon) and localVariableDeclaration.
 * This allowed me to use localVariableDeclaration in "forInit" clauses,
 * simplifying them.
 * Changed switchBlockStatementGroup to use multiple labels.  This adds an
 * ambiguity, but if one uses appropriately greedy parsing it yields the
 * parse that is closest to the meaning of the switch statement.
 * Renamed "forVarControl" to "enhancedForControl" -- JLS language.
 * Added semantic predicates to test for shift operations rather than other
 * things.  Thus, for instance, the string "< <" will never be treated
 * as a left-shift operator.
 * In "creator" we rule out "nonWildcardTypeArguments" on arrayCreation,
 * which are illegal.
 * Moved "nonWildcardTypeArguments into innerCreator.
 * Removed 'super' superSuffix from explicitGenericInvocation, since that
 * is only used in explicitConstructorInvocation at the beginning of a
 * constructorBody.  (This is part of the simplification of expressions
 * mentioned earlier.)
 * Simplified primary (got rid of those things that are only used in
 * explicitConstructorInvocation).
 * Lexer -- removed "Exponent?" from FloatingPointLiteral choice 4, since it
 * led to an ambiguity.
 * <p/>
 * This grammar successfully parses every .java file in the JDK 1.5 source
 * tree (excluding those whose file names include '-', which are not
 * valid Java compilation units).
 * <p/>
 * June 26, 2008
 * <p/>
 * conditionalExpression had wrong precedence x?y:z.
 * <p/>
 * Known remaining problems:
 * "Letter" and "JavaIDDigit" are wrong.  The actual specification of
 * "Letter" should be "a character for which the method
 * Character.isJavaIdentifierStart(int) returns true."  A "Java
 * letter-or-digit is a character for which the method
 * Character.isJavaIdentifierPart(int) returns true."
 */
@SuppressWarnings({"all", "warnings", "unchecked"})
public class JavaParser extends Parser {
    public static final String[] tokenNames = new String[]{
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSERT", "COMMENT", "CharacterLiteral", "DecimalLiteral", "ENUM", "EscapeSequence", "Exponent", "FloatTypeSuffix", "FloatingPointLiteral", "HexDigit", "HexLiteral", "Identifier", "IntegerTypeSuffix", "JavaIDDigit", "LINE_COMMENT", "Letter", "OctalEscape", "OctalLiteral", "StringLiteral", "UnicodeEscape", "WS", "'!'", "'!='", "'%'", "'%='", "'&&'", "'&'", "'&='", "'('", "')'", "'*'", "'*='", "'+'", "'++'", "'+='", "','", "'-'", "'--'", "'-='", "'.'", "'...'", "'/'", "'/='", "':'", "';'", "'<'", "'='", "'=='", "'>'", "'?'", "'@'", "'['", "']'", "'^'", "'^='", "'abstract'", "'boolean'", "'break'", "'byte'", "'case'", "'catch'", "'char'", "'class'", "'continue'", "'default'", "'do'", "'double'", "'else'", "'extends'", "'false'", "'final'", "'finally'", "'float'", "'for'", "'if'", "'implements'", "'import'", "'instanceof'", "'int'", "'interface'", "'long'", "'native'", "'new'", "'null'", "'package'", "'private'", "'protected'", "'public'", "'return'", "'short'", "'static'", "'strictfp'", "'super'", "'switch'", "'synchronized'", "'this'", "'throw'", "'throws'", "'transient'", "'true'", "'try'", "'void'", "'volatile'", "'while'", "'{'", "'|'", "'|='", "'||'", "'}'", "'~'"
    };

    public static final int EOF = -1;
    public static final int T__25 = 25;
    public static final int T__26 = 26;
    public static final int T__27 = 27;
    public static final int T__28 = 28;
    public static final int T__29 = 29;
    public static final int T__30 = 30;
    public static final int T__31 = 31;
    public static final int T__32 = 32;
    public static final int T__33 = 33;
    public static final int T__34 = 34;
    public static final int T__35 = 35;
    public static final int T__36 = 36;
    public static final int T__37 = 37;
    public static final int T__38 = 38;
    public static final int T__39 = 39;
    public static final int T__40 = 40;
    public static final int T__41 = 41;
    public static final int T__42 = 42;
    public static final int T__43 = 43;
    public static final int T__44 = 44;
    public static final int T__45 = 45;
    public static final int T__46 = 46;
    public static final int T__47 = 47;
    public static final int T__48 = 48;
    public static final int T__49 = 49;
    public static final int T__50 = 50;
    public static final int T__51 = 51;
    public static final int T__52 = 52;
    public static final int T__53 = 53;
    public static final int T__54 = 54;
    public static final int T__55 = 55;
    public static final int T__56 = 56;
    public static final int T__57 = 57;
    public static final int T__58 = 58;
    public static final int T__59 = 59;
    public static final int T__60 = 60;
    public static final int T__61 = 61;
    public static final int T__62 = 62;
    public static final int T__63 = 63;
    public static final int T__64 = 64;
    public static final int T__65 = 65;
    public static final int T__66 = 66;
    public static final int T__67 = 67;
    public static final int T__68 = 68;
    public static final int T__69 = 69;
    public static final int T__70 = 70;
    public static final int T__71 = 71;
    public static final int T__72 = 72;
    public static final int T__73 = 73;
    public static final int T__74 = 74;
    public static final int T__75 = 75;
    public static final int T__76 = 76;
    public static final int T__77 = 77;
    public static final int T__78 = 78;
    public static final int T__79 = 79;
    public static final int T__80 = 80;
    public static final int T__81 = 81;
    public static final int T__82 = 82;
    public static final int T__83 = 83;
    public static final int T__84 = 84;
    public static final int T__85 = 85;
    public static final int T__86 = 86;
    public static final int T__87 = 87;
    public static final int T__88 = 88;
    public static final int T__89 = 89;
    public static final int T__90 = 90;
    public static final int T__91 = 91;
    public static final int T__92 = 92;
    public static final int T__93 = 93;
    public static final int T__94 = 94;
    public static final int T__95 = 95;
    public static final int T__96 = 96;
    public static final int T__97 = 97;
    public static final int T__98 = 98;
    public static final int T__99 = 99;
    public static final int T__100 = 100;
    public static final int T__101 = 101;
    public static final int T__102 = 102;
    public static final int T__103 = 103;
    public static final int T__104 = 104;
    public static final int T__105 = 105;
    public static final int T__106 = 106;
    public static final int T__107 = 107;
    public static final int T__108 = 108;
    public static final int T__109 = 109;
    public static final int T__110 = 110;
    public static final int T__111 = 111;
    public static final int T__112 = 112;
    public static final int T__113 = 113;
    public static final int ASSERT = 4;
    public static final int COMMENT = 5;
    public static final int CharacterLiteral = 6;
    public static final int DecimalLiteral = 7;
    public static final int ENUM = 8;
    public static final int EscapeSequence = 9;
    public static final int Exponent = 10;
    public static final int FloatTypeSuffix = 11;
    public static final int FloatingPointLiteral = 12;
    public static final int HexDigit = 13;
    public static final int HexLiteral = 14;
    public static final int Identifier = 15;
    public static final int IntegerTypeSuffix = 16;
    public static final int JavaIDDigit = 17;
    public static final int LINE_COMMENT = 18;
    public static final int Letter = 19;
    public static final int OctalEscape = 20;
    public static final int OctalLiteral = 21;
    public static final int StringLiteral = 22;
    public static final int UnicodeEscape = 23;
    public static final int WS = 24;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[]{};
    }

    // delegators


    public JavaParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public JavaParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        this.state.ruleMemo = new HashMap[407 + 1];


    }

    public String[] getTokenNames() {
        return JavaParser.tokenNames;
    }

    public String getGrammarFileName() {
        return "example/Java.g";
    }


    // $ANTLR start "compilationUnit"
    // example/Java.g:183:1: compilationUnit : ( annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* );
    public final void compilationUnit() throws RecognitionException {
        int compilationUnit_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 1)) {
                return;
            }

            // example/Java.g:184:5: ( annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* )
            int alt8 = 2;
            switch (input.LA(1)) {
                case 54: {
                    int LA8_1 = input.LA(2);

                    if ((synpred5_Java())) {
                        alt8 = 1;
                    } else if ((true)) {
                        alt8 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 8, 1, input);

                        throw nvae;

                    }
                }
                break;
                case EOF:
                case ENUM:
                case 48:
                case 59:
                case 66:
                case 74:
                case 80:
                case 83:
                case 88:
                case 89:
                case 90:
                case 91:
                case 94:
                case 95: {
                    alt8 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 8, 0, input);

                    throw nvae;

            }

            switch (alt8) {
                case 1:
                    // example/Java.g:184:9: annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
                {
                    pushFollow(FOLLOW_annotations_in_compilationUnit59);
                    annotations();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:185:9: ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
                    int alt4 = 2;
                    switch (input.LA(1)) {
                        case 88: {
                            alt4 = 1;
                        }
                        break;
                        case ENUM:
                        case 54:
                        case 59:
                        case 66:
                        case 74:
                        case 83:
                        case 89:
                        case 90:
                        case 91:
                        case 94:
                        case 95: {
                            alt4 = 2;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 4, 0, input);

                            throw nvae;

                    }

                    switch (alt4) {
                        case 1:
                            // example/Java.g:185:13: packageDeclaration ( importDeclaration )* ( typeDeclaration )*
                        {
                            pushFollow(FOLLOW_packageDeclaration_in_compilationUnit73);
                            packageDeclaration();

                            state._fsp--;
                            if (state.failed) return;

                            // example/Java.g:185:32: ( importDeclaration )*
                            loop1:
                            do {
                                int alt1 = 2;
                                switch (input.LA(1)) {
                                    case 80: {
                                        alt1 = 1;
                                    }
                                    break;

                                }

                                switch (alt1) {
                                    case 1:
                                        // example/Java.g:185:32: importDeclaration
                                    {
                                        pushFollow(FOLLOW_importDeclaration_in_compilationUnit75);
                                        importDeclaration();

                                        state._fsp--;
                                        if (state.failed) return;

                                    }
                                    break;

                                    default:
                                        break loop1;
                                }
                            } while (true);


                            // example/Java.g:185:51: ( typeDeclaration )*
                            loop2:
                            do {
                                int alt2 = 2;
                                switch (input.LA(1)) {
                                    case ENUM:
                                    case 48:
                                    case 54:
                                    case 59:
                                    case 66:
                                    case 74:
                                    case 83:
                                    case 89:
                                    case 90:
                                    case 91:
                                    case 94:
                                    case 95: {
                                        alt2 = 1;
                                    }
                                    break;

                                }

                                switch (alt2) {
                                    case 1:
                                        // example/Java.g:185:51: typeDeclaration
                                    {
                                        pushFollow(FOLLOW_typeDeclaration_in_compilationUnit78);
                                        typeDeclaration();

                                        state._fsp--;
                                        if (state.failed) return;

                                    }
                                    break;

                                    default:
                                        break loop2;
                                }
                            } while (true);


                        }
                        break;
                        case 2:
                            // example/Java.g:186:13: classOrInterfaceDeclaration ( typeDeclaration )*
                        {
                            pushFollow(FOLLOW_classOrInterfaceDeclaration_in_compilationUnit93);
                            classOrInterfaceDeclaration();

                            state._fsp--;
                            if (state.failed) return;

                            // example/Java.g:186:41: ( typeDeclaration )*
                            loop3:
                            do {
                                int alt3 = 2;
                                switch (input.LA(1)) {
                                    case ENUM:
                                    case 48:
                                    case 54:
                                    case 59:
                                    case 66:
                                    case 74:
                                    case 83:
                                    case 89:
                                    case 90:
                                    case 91:
                                    case 94:
                                    case 95: {
                                        alt3 = 1;
                                    }
                                    break;

                                }

                                switch (alt3) {
                                    case 1:
                                        // example/Java.g:186:41: typeDeclaration
                                    {
                                        pushFollow(FOLLOW_typeDeclaration_in_compilationUnit95);
                                        typeDeclaration();

                                        state._fsp--;
                                        if (state.failed) return;

                                    }
                                    break;

                                    default:
                                        break loop3;
                                }
                            } while (true);


                        }
                        break;

                    }


                }
                break;
                case 2:
                    // example/Java.g:188:9: ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )*
                {
                    // example/Java.g:188:9: ( packageDeclaration )?
                    int alt5 = 2;
                    switch (input.LA(1)) {
                        case 88: {
                            alt5 = 1;
                        }
                        break;
                    }

                    switch (alt5) {
                        case 1:
                            // example/Java.g:188:9: packageDeclaration
                        {
                            pushFollow(FOLLOW_packageDeclaration_in_compilationUnit116);
                            packageDeclaration();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    // example/Java.g:188:29: ( importDeclaration )*
                    loop6:
                    do {
                        int alt6 = 2;
                        switch (input.LA(1)) {
                            case 80: {
                                alt6 = 1;
                            }
                            break;

                        }

                        switch (alt6) {
                            case 1:
                                // example/Java.g:188:29: importDeclaration
                            {
                                pushFollow(FOLLOW_importDeclaration_in_compilationUnit119);
                                importDeclaration();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop6;
                        }
                    } while (true);


                    // example/Java.g:188:48: ( typeDeclaration )*
                    loop7:
                    do {
                        int alt7 = 2;
                        switch (input.LA(1)) {
                            case ENUM:
                            case 48:
                            case 54:
                            case 59:
                            case 66:
                            case 74:
                            case 83:
                            case 89:
                            case 90:
                            case 91:
                            case 94:
                            case 95: {
                                alt7 = 1;
                            }
                            break;

                        }

                        switch (alt7) {
                            case 1:
                                // example/Java.g:188:48: typeDeclaration
                            {
                                pushFollow(FOLLOW_typeDeclaration_in_compilationUnit122);
                                typeDeclaration();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop7;
                        }
                    } while (true);


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 1, compilationUnit_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "compilationUnit"


    // $ANTLR start "packageDeclaration"
    // example/Java.g:191:1: packageDeclaration : 'package' qualifiedName ';' ;
    public final void packageDeclaration() throws RecognitionException {
        int packageDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 2)) {
                return;
            }

            // example/Java.g:192:5: ( 'package' qualifiedName ';' )
            // example/Java.g:192:9: 'package' qualifiedName ';'
            {
                match(input, 88, FOLLOW_88_in_packageDeclaration142);
                if (state.failed) return;

                pushFollow(FOLLOW_qualifiedName_in_packageDeclaration144);
                qualifiedName();

                state._fsp--;
                if (state.failed) return;

                match(input, 48, FOLLOW_48_in_packageDeclaration146);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 2, packageDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "packageDeclaration"


    // $ANTLR start "importDeclaration"
    // example/Java.g:195:1: importDeclaration : 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';' ;
    public final void importDeclaration() throws RecognitionException {
        int importDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 3)) {
                return;
            }

            // example/Java.g:196:5: ( 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';' )
            // example/Java.g:196:9: 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';'
            {
                match(input, 80, FOLLOW_80_in_importDeclaration169);
                if (state.failed) return;

                // example/Java.g:196:18: ( 'static' )?
                int alt9 = 2;
                switch (input.LA(1)) {
                    case 94: {
                        alt9 = 1;
                    }
                    break;
                }

                switch (alt9) {
                    case 1:
                        // example/Java.g:196:18: 'static'
                    {
                        match(input, 94, FOLLOW_94_in_importDeclaration171);
                        if (state.failed) return;

                    }
                    break;

                }


                pushFollow(FOLLOW_qualifiedName_in_importDeclaration174);
                qualifiedName();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:196:42: ( '.' '*' )?
                int alt10 = 2;
                switch (input.LA(1)) {
                    case 43: {
                        alt10 = 1;
                    }
                    break;
                }

                switch (alt10) {
                    case 1:
                        // example/Java.g:196:43: '.' '*'
                    {
                        match(input, 43, FOLLOW_43_in_importDeclaration177);
                        if (state.failed) return;

                        match(input, 34, FOLLOW_34_in_importDeclaration179);
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, 48, FOLLOW_48_in_importDeclaration183);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 3, importDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "importDeclaration"


    // $ANTLR start "typeDeclaration"
    // example/Java.g:199:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );
    public final void typeDeclaration() throws RecognitionException {
        int typeDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 4)) {
                return;
            }

            // example/Java.g:200:5: ( classOrInterfaceDeclaration | ';' )
            int alt11 = 2;
            switch (input.LA(1)) {
                case ENUM:
                case 54:
                case 59:
                case 66:
                case 74:
                case 83:
                case 89:
                case 90:
                case 91:
                case 94:
                case 95: {
                    alt11 = 1;
                }
                break;
                case 48: {
                    alt11 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 11, 0, input);

                    throw nvae;

            }

            switch (alt11) {
                case 1:
                    // example/Java.g:200:9: classOrInterfaceDeclaration
                {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration206);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:201:9: ';'
                {
                    match(input, 48, FOLLOW_48_in_typeDeclaration216);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 4, typeDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeDeclaration"


    // $ANTLR start "classOrInterfaceDeclaration"
    // example/Java.g:204:1: classOrInterfaceDeclaration : classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration ) ;
    public final void classOrInterfaceDeclaration() throws RecognitionException {
        int classOrInterfaceDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 5)) {
                return;
            }

            // example/Java.g:205:5: ( classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration ) )
            // example/Java.g:205:9: classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration )
            {
                pushFollow(FOLLOW_classOrInterfaceModifiers_in_classOrInterfaceDeclaration239);
                classOrInterfaceModifiers();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:205:35: ( classDeclaration | interfaceDeclaration )
                int alt12 = 2;
                switch (input.LA(1)) {
                    case ENUM:
                    case 66: {
                        alt12 = 1;
                    }
                    break;
                    case 54:
                    case 83: {
                        alt12 = 2;
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 12, 0, input);

                        throw nvae;

                }

                switch (alt12) {
                    case 1:
                        // example/Java.g:205:36: classDeclaration
                    {
                        pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration242);
                        classDeclaration();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;
                    case 2:
                        // example/Java.g:205:55: interfaceDeclaration
                    {
                        pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration246);
                        interfaceDeclaration();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 5, classOrInterfaceDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classOrInterfaceDeclaration"


    // $ANTLR start "classOrInterfaceModifiers"
    // example/Java.g:208:1: classOrInterfaceModifiers : ( classOrInterfaceModifier )* ;
    public final void classOrInterfaceModifiers() throws RecognitionException {
        int classOrInterfaceModifiers_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 6)) {
                return;
            }

            // example/Java.g:209:5: ( ( classOrInterfaceModifier )* )
            // example/Java.g:209:9: ( classOrInterfaceModifier )*
            {
                // example/Java.g:209:9: ( classOrInterfaceModifier )*
                loop13:
                do {
                    int alt13 = 2;
                    switch (input.LA(1)) {
                        case 54: {
                            switch (input.LA(2)) {
                                case Identifier: {
                                    alt13 = 1;
                                }
                                break;

                            }

                        }
                        break;
                        case 59:
                        case 74:
                        case 89:
                        case 90:
                        case 91:
                        case 94:
                        case 95: {
                            alt13 = 1;
                        }
                        break;

                    }

                    switch (alt13) {
                        case 1:
                            // example/Java.g:209:9: classOrInterfaceModifier
                        {
                            pushFollow(FOLLOW_classOrInterfaceModifier_in_classOrInterfaceModifiers270);
                            classOrInterfaceModifier();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop13;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 6, classOrInterfaceModifiers_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classOrInterfaceModifiers"


    // $ANTLR start "classOrInterfaceModifier"
    // example/Java.g:212:1: classOrInterfaceModifier : ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'strictfp' );
    public final void classOrInterfaceModifier() throws RecognitionException {
        int classOrInterfaceModifier_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 7)) {
                return;
            }

            // example/Java.g:213:5: ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'strictfp' )
            int alt14 = 8;
            switch (input.LA(1)) {
                case 54: {
                    alt14 = 1;
                }
                break;
                case 91: {
                    alt14 = 2;
                }
                break;
                case 90: {
                    alt14 = 3;
                }
                break;
                case 89: {
                    alt14 = 4;
                }
                break;
                case 59: {
                    alt14 = 5;
                }
                break;
                case 94: {
                    alt14 = 6;
                }
                break;
                case 74: {
                    alt14 = 7;
                }
                break;
                case 95: {
                    alt14 = 8;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 14, 0, input);

                    throw nvae;

            }

            switch (alt14) {
                case 1:
                    // example/Java.g:213:9: annotation
                {
                    pushFollow(FOLLOW_annotation_in_classOrInterfaceModifier290);
                    annotation();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:214:9: 'public'
                {
                    match(input, 91, FOLLOW_91_in_classOrInterfaceModifier303);
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:215:9: 'protected'
                {
                    match(input, 90, FOLLOW_90_in_classOrInterfaceModifier318);
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:216:9: 'private'
                {
                    match(input, 89, FOLLOW_89_in_classOrInterfaceModifier330);
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:217:9: 'abstract'
                {
                    match(input, 59, FOLLOW_59_in_classOrInterfaceModifier344);
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:218:9: 'static'
                {
                    match(input, 94, FOLLOW_94_in_classOrInterfaceModifier357);
                    if (state.failed) return;

                }
                break;
                case 7:
                    // example/Java.g:219:9: 'final'
                {
                    match(input, 74, FOLLOW_74_in_classOrInterfaceModifier372);
                    if (state.failed) return;

                }
                break;
                case 8:
                    // example/Java.g:220:9: 'strictfp'
                {
                    match(input, 95, FOLLOW_95_in_classOrInterfaceModifier388);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 7, classOrInterfaceModifier_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classOrInterfaceModifier"


    // $ANTLR start "modifiers"
    // example/Java.g:223:1: modifiers : ( modifier )* ;
    public final void modifiers() throws RecognitionException {
        int modifiers_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 8)) {
                return;
            }

            // example/Java.g:224:5: ( ( modifier )* )
            // example/Java.g:224:9: ( modifier )*
            {
                // example/Java.g:224:9: ( modifier )*
                loop15:
                do {
                    int alt15 = 2;
                    switch (input.LA(1)) {
                        case 54: {
                            switch (input.LA(2)) {
                                case Identifier: {
                                    alt15 = 1;
                                }
                                break;

                            }

                        }
                        break;
                        case 59:
                        case 74:
                        case 85:
                        case 89:
                        case 90:
                        case 91:
                        case 94:
                        case 95:
                        case 98:
                        case 102:
                        case 106: {
                            alt15 = 1;
                        }
                        break;

                    }

                    switch (alt15) {
                        case 1:
                            // example/Java.g:224:9: modifier
                        {
                            pushFollow(FOLLOW_modifier_in_modifiers410);
                            modifier();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop15;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 8, modifiers_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "modifiers"


    // $ANTLR start "classDeclaration"
    // example/Java.g:227:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );
    public final void classDeclaration() throws RecognitionException {
        int classDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 9)) {
                return;
            }

            // example/Java.g:228:5: ( normalClassDeclaration | enumDeclaration )
            int alt16 = 2;
            switch (input.LA(1)) {
                case 66: {
                    alt16 = 1;
                }
                break;
                case ENUM: {
                    alt16 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 16, 0, input);

                    throw nvae;

            }

            switch (alt16) {
                case 1:
                    // example/Java.g:228:9: normalClassDeclaration
                {
                    pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration430);
                    normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:229:9: enumDeclaration
                {
                    pushFollow(FOLLOW_enumDeclaration_in_classDeclaration440);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 9, classDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classDeclaration"


    // $ANTLR start "normalClassDeclaration"
    // example/Java.g:232:1: normalClassDeclaration : 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody ;
    public final void normalClassDeclaration() throws RecognitionException {
        int normalClassDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 10)) {
                return;
            }

            // example/Java.g:233:5: ( 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody )
            // example/Java.g:233:9: 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody
            {
                match(input, 66, FOLLOW_66_in_normalClassDeclaration463);
                if (state.failed) return;

                match(input, Identifier, FOLLOW_Identifier_in_normalClassDeclaration465);
                if (state.failed) return;

                // example/Java.g:233:28: ( typeParameters )?
                int alt17 = 2;
                switch (input.LA(1)) {
                    case 49: {
                        alt17 = 1;
                    }
                    break;
                }

                switch (alt17) {
                    case 1:
                        // example/Java.g:233:28: typeParameters
                    {
                        pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration467);
                        typeParameters();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:234:9: ( 'extends' type )?
                int alt18 = 2;
                switch (input.LA(1)) {
                    case 72: {
                        alt18 = 1;
                    }
                    break;
                }

                switch (alt18) {
                    case 1:
                        // example/Java.g:234:10: 'extends' type
                    {
                        match(input, 72, FOLLOW_72_in_normalClassDeclaration479);
                        if (state.failed) return;

                        pushFollow(FOLLOW_type_in_normalClassDeclaration481);
                        type();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:235:9: ( 'implements' typeList )?
                int alt19 = 2;
                switch (input.LA(1)) {
                    case 79: {
                        alt19 = 1;
                    }
                    break;
                }

                switch (alt19) {
                    case 1:
                        // example/Java.g:235:10: 'implements' typeList
                    {
                        match(input, 79, FOLLOW_79_in_normalClassDeclaration494);
                        if (state.failed) return;

                        pushFollow(FOLLOW_typeList_in_normalClassDeclaration496);
                        typeList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                pushFollow(FOLLOW_classBody_in_normalClassDeclaration508);
                classBody();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 10, normalClassDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "normalClassDeclaration"


    // $ANTLR start "typeParameters"
    // example/Java.g:239:1: typeParameters : '<' typeParameter ( ',' typeParameter )* '>' ;
    public final void typeParameters() throws RecognitionException {
        int typeParameters_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 11)) {
                return;
            }

            // example/Java.g:240:5: ( '<' typeParameter ( ',' typeParameter )* '>' )
            // example/Java.g:240:9: '<' typeParameter ( ',' typeParameter )* '>'
            {
                match(input, 49, FOLLOW_49_in_typeParameters531);
                if (state.failed) return;

                pushFollow(FOLLOW_typeParameter_in_typeParameters533);
                typeParameter();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:240:27: ( ',' typeParameter )*
                loop20:
                do {
                    int alt20 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt20 = 1;
                        }
                        break;

                    }

                    switch (alt20) {
                        case 1:
                            // example/Java.g:240:28: ',' typeParameter
                        {
                            match(input, 39, FOLLOW_39_in_typeParameters536);
                            if (state.failed) return;

                            pushFollow(FOLLOW_typeParameter_in_typeParameters538);
                            typeParameter();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop20;
                    }
                } while (true);


                match(input, 52, FOLLOW_52_in_typeParameters542);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 11, typeParameters_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeParameters"


    // $ANTLR start "typeParameter"
    // example/Java.g:243:1: typeParameter : Identifier ( 'extends' typeBound )? ;
    public final void typeParameter() throws RecognitionException {
        int typeParameter_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 12)) {
                return;
            }

            // example/Java.g:244:5: ( Identifier ( 'extends' typeBound )? )
            // example/Java.g:244:9: Identifier ( 'extends' typeBound )?
            {
                match(input, Identifier, FOLLOW_Identifier_in_typeParameter561);
                if (state.failed) return;

                // example/Java.g:244:20: ( 'extends' typeBound )?
                int alt21 = 2;
                switch (input.LA(1)) {
                    case 72: {
                        alt21 = 1;
                    }
                    break;
                }

                switch (alt21) {
                    case 1:
                        // example/Java.g:244:21: 'extends' typeBound
                    {
                        match(input, 72, FOLLOW_72_in_typeParameter564);
                        if (state.failed) return;

                        pushFollow(FOLLOW_typeBound_in_typeParameter566);
                        typeBound();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 12, typeParameter_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeParameter"


    // $ANTLR start "typeBound"
    // example/Java.g:247:1: typeBound : type ( '&' type )* ;
    public final void typeBound() throws RecognitionException {
        int typeBound_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 13)) {
                return;
            }

            // example/Java.g:248:5: ( type ( '&' type )* )
            // example/Java.g:248:9: type ( '&' type )*
            {
                pushFollow(FOLLOW_type_in_typeBound595);
                type();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:248:14: ( '&' type )*
                loop22:
                do {
                    int alt22 = 2;
                    switch (input.LA(1)) {
                        case 30: {
                            alt22 = 1;
                        }
                        break;

                    }

                    switch (alt22) {
                        case 1:
                            // example/Java.g:248:15: '&' type
                        {
                            match(input, 30, FOLLOW_30_in_typeBound598);
                            if (state.failed) return;

                            pushFollow(FOLLOW_type_in_typeBound600);
                            type();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop22;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 13, typeBound_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeBound"


    // $ANTLR start "enumDeclaration"
    // example/Java.g:251:1: enumDeclaration : ENUM Identifier ( 'implements' typeList )? enumBody ;
    public final void enumDeclaration() throws RecognitionException {
        int enumDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 14)) {
                return;
            }

            // example/Java.g:252:5: ( ENUM Identifier ( 'implements' typeList )? enumBody )
            // example/Java.g:252:9: ENUM Identifier ( 'implements' typeList )? enumBody
            {
                match(input, ENUM, FOLLOW_ENUM_in_enumDeclaration621);
                if (state.failed) return;

                match(input, Identifier, FOLLOW_Identifier_in_enumDeclaration623);
                if (state.failed) return;

                // example/Java.g:252:25: ( 'implements' typeList )?
                int alt23 = 2;
                switch (input.LA(1)) {
                    case 79: {
                        alt23 = 1;
                    }
                    break;
                }

                switch (alt23) {
                    case 1:
                        // example/Java.g:252:26: 'implements' typeList
                    {
                        match(input, 79, FOLLOW_79_in_enumDeclaration626);
                        if (state.failed) return;

                        pushFollow(FOLLOW_typeList_in_enumDeclaration628);
                        typeList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                pushFollow(FOLLOW_enumBody_in_enumDeclaration632);
                enumBody();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 14, enumDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "enumDeclaration"


    // $ANTLR start "enumBody"
    // example/Java.g:255:1: enumBody : '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' ;
    public final void enumBody() throws RecognitionException {
        int enumBody_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 15)) {
                return;
            }

            // example/Java.g:256:5: ( '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' )
            // example/Java.g:256:9: '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}'
            {
                match(input, 108, FOLLOW_108_in_enumBody651);
                if (state.failed) return;

                // example/Java.g:256:13: ( enumConstants )?
                int alt24 = 2;
                switch (input.LA(1)) {
                    case Identifier:
                    case 54: {
                        alt24 = 1;
                    }
                    break;
                }

                switch (alt24) {
                    case 1:
                        // example/Java.g:256:13: enumConstants
                    {
                        pushFollow(FOLLOW_enumConstants_in_enumBody653);
                        enumConstants();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:256:28: ( ',' )?
                int alt25 = 2;
                switch (input.LA(1)) {
                    case 39: {
                        alt25 = 1;
                    }
                    break;
                }

                switch (alt25) {
                    case 1:
                        // example/Java.g:256:28: ','
                    {
                        match(input, 39, FOLLOW_39_in_enumBody656);
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:256:33: ( enumBodyDeclarations )?
                int alt26 = 2;
                switch (input.LA(1)) {
                    case 48: {
                        alt26 = 1;
                    }
                    break;
                }

                switch (alt26) {
                    case 1:
                        // example/Java.g:256:33: enumBodyDeclarations
                    {
                        pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody659);
                        enumBodyDeclarations();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, 112, FOLLOW_112_in_enumBody662);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 15, enumBody_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "enumBody"


    // $ANTLR start "enumConstants"
    // example/Java.g:259:1: enumConstants : enumConstant ( ',' enumConstant )* ;
    public final void enumConstants() throws RecognitionException {
        int enumConstants_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 16)) {
                return;
            }

            // example/Java.g:260:5: ( enumConstant ( ',' enumConstant )* )
            // example/Java.g:260:9: enumConstant ( ',' enumConstant )*
            {
                pushFollow(FOLLOW_enumConstant_in_enumConstants681);
                enumConstant();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:260:22: ( ',' enumConstant )*
                loop27:
                do {
                    int alt27 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            switch (input.LA(2)) {
                                case Identifier:
                                case 54: {
                                    alt27 = 1;
                                }
                                break;

                            }

                        }
                        break;

                    }

                    switch (alt27) {
                        case 1:
                            // example/Java.g:260:23: ',' enumConstant
                        {
                            match(input, 39, FOLLOW_39_in_enumConstants684);
                            if (state.failed) return;

                            pushFollow(FOLLOW_enumConstant_in_enumConstants686);
                            enumConstant();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop27;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 16, enumConstants_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "enumConstants"


    // $ANTLR start "enumConstant"
    // example/Java.g:263:1: enumConstant : ( annotations )? Identifier ( arguments )? ( classBody )? ;
    public final void enumConstant() throws RecognitionException {
        int enumConstant_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 17)) {
                return;
            }

            // example/Java.g:264:5: ( ( annotations )? Identifier ( arguments )? ( classBody )? )
            // example/Java.g:264:9: ( annotations )? Identifier ( arguments )? ( classBody )?
            {
                // example/Java.g:264:9: ( annotations )?
                int alt28 = 2;
                switch (input.LA(1)) {
                    case 54: {
                        alt28 = 1;
                    }
                    break;
                }

                switch (alt28) {
                    case 1:
                        // example/Java.g:264:9: annotations
                    {
                        pushFollow(FOLLOW_annotations_in_enumConstant711);
                        annotations();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, Identifier, FOLLOW_Identifier_in_enumConstant714);
                if (state.failed) return;

                // example/Java.g:264:33: ( arguments )?
                int alt29 = 2;
                switch (input.LA(1)) {
                    case 32: {
                        alt29 = 1;
                    }
                    break;
                }

                switch (alt29) {
                    case 1:
                        // example/Java.g:264:33: arguments
                    {
                        pushFollow(FOLLOW_arguments_in_enumConstant716);
                        arguments();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:264:44: ( classBody )?
                int alt30 = 2;
                switch (input.LA(1)) {
                    case 108: {
                        alt30 = 1;
                    }
                    break;
                }

                switch (alt30) {
                    case 1:
                        // example/Java.g:264:44: classBody
                    {
                        pushFollow(FOLLOW_classBody_in_enumConstant719);
                        classBody();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 17, enumConstant_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "enumConstant"


    // $ANTLR start "enumBodyDeclarations"
    // example/Java.g:267:1: enumBodyDeclarations : ';' ( classBodyDeclaration )* ;
    public final void enumBodyDeclarations() throws RecognitionException {
        int enumBodyDeclarations_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 18)) {
                return;
            }

            // example/Java.g:268:5: ( ';' ( classBodyDeclaration )* )
            // example/Java.g:268:9: ';' ( classBodyDeclaration )*
            {
                match(input, 48, FOLLOW_48_in_enumBodyDeclarations743);
                if (state.failed) return;

                // example/Java.g:268:13: ( classBodyDeclaration )*
                loop31:
                do {
                    int alt31 = 2;
                    switch (input.LA(1)) {
                        case ENUM:
                        case Identifier:
                        case 48:
                        case 49:
                        case 54:
                        case 59:
                        case 60:
                        case 62:
                        case 65:
                        case 66:
                        case 70:
                        case 74:
                        case 76:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 89:
                        case 90:
                        case 91:
                        case 93:
                        case 94:
                        case 95:
                        case 98:
                        case 102:
                        case 105:
                        case 106:
                        case 108: {
                            alt31 = 1;
                        }
                        break;

                    }

                    switch (alt31) {
                        case 1:
                            // example/Java.g:268:14: classBodyDeclaration
                        {
                            pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations746);
                            classBodyDeclaration();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop31;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 18, enumBodyDeclarations_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "enumBodyDeclarations"


    // $ANTLR start "interfaceDeclaration"
    // example/Java.g:271:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );
    public final void interfaceDeclaration() throws RecognitionException {
        int interfaceDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 19)) {
                return;
            }

            // example/Java.g:272:5: ( normalInterfaceDeclaration | annotationTypeDeclaration )
            int alt32 = 2;
            switch (input.LA(1)) {
                case 83: {
                    alt32 = 1;
                }
                break;
                case 54: {
                    alt32 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 32, 0, input);

                    throw nvae;

            }

            switch (alt32) {
                case 1:
                    // example/Java.g:272:9: normalInterfaceDeclaration
                {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration771);
                    normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:273:9: annotationTypeDeclaration
                {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration781);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 19, interfaceDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceDeclaration"


    // $ANTLR start "normalInterfaceDeclaration"
    // example/Java.g:276:1: normalInterfaceDeclaration : 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody ;
    public final void normalInterfaceDeclaration() throws RecognitionException {
        int normalInterfaceDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 20)) {
                return;
            }

            // example/Java.g:277:5: ( 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody )
            // example/Java.g:277:9: 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody
            {
                match(input, 83, FOLLOW_83_in_normalInterfaceDeclaration804);
                if (state.failed) return;

                match(input, Identifier, FOLLOW_Identifier_in_normalInterfaceDeclaration806);
                if (state.failed) return;

                // example/Java.g:277:32: ( typeParameters )?
                int alt33 = 2;
                switch (input.LA(1)) {
                    case 49: {
                        alt33 = 1;
                    }
                    break;
                }

                switch (alt33) {
                    case 1:
                        // example/Java.g:277:32: typeParameters
                    {
                        pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration808);
                        typeParameters();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:277:48: ( 'extends' typeList )?
                int alt34 = 2;
                switch (input.LA(1)) {
                    case 72: {
                        alt34 = 1;
                    }
                    break;
                }

                switch (alt34) {
                    case 1:
                        // example/Java.g:277:49: 'extends' typeList
                    {
                        match(input, 72, FOLLOW_72_in_normalInterfaceDeclaration812);
                        if (state.failed) return;

                        pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration814);
                        typeList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration818);
                interfaceBody();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 20, normalInterfaceDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "normalInterfaceDeclaration"


    // $ANTLR start "typeList"
    // example/Java.g:280:1: typeList : type ( ',' type )* ;
    public final void typeList() throws RecognitionException {
        int typeList_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 21)) {
                return;
            }

            // example/Java.g:281:5: ( type ( ',' type )* )
            // example/Java.g:281:9: type ( ',' type )*
            {
                pushFollow(FOLLOW_type_in_typeList841);
                type();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:281:14: ( ',' type )*
                loop35:
                do {
                    int alt35 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt35 = 1;
                        }
                        break;

                    }

                    switch (alt35) {
                        case 1:
                            // example/Java.g:281:15: ',' type
                        {
                            match(input, 39, FOLLOW_39_in_typeList844);
                            if (state.failed) return;

                            pushFollow(FOLLOW_type_in_typeList846);
                            type();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop35;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 21, typeList_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeList"


    // $ANTLR start "classBody"
    // example/Java.g:284:1: classBody : '{' ( classBodyDeclaration )* '}' ;
    public final void classBody() throws RecognitionException {
        int classBody_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 22)) {
                return;
            }

            // example/Java.g:285:5: ( '{' ( classBodyDeclaration )* '}' )
            // example/Java.g:285:9: '{' ( classBodyDeclaration )* '}'
            {
                match(input, 108, FOLLOW_108_in_classBody871);
                if (state.failed) return;

                // example/Java.g:285:13: ( classBodyDeclaration )*
                loop36:
                do {
                    int alt36 = 2;
                    switch (input.LA(1)) {
                        case ENUM:
                        case Identifier:
                        case 48:
                        case 49:
                        case 54:
                        case 59:
                        case 60:
                        case 62:
                        case 65:
                        case 66:
                        case 70:
                        case 74:
                        case 76:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 89:
                        case 90:
                        case 91:
                        case 93:
                        case 94:
                        case 95:
                        case 98:
                        case 102:
                        case 105:
                        case 106:
                        case 108: {
                            alt36 = 1;
                        }
                        break;

                    }

                    switch (alt36) {
                        case 1:
                            // example/Java.g:285:13: classBodyDeclaration
                        {
                            pushFollow(FOLLOW_classBodyDeclaration_in_classBody873);
                            classBodyDeclaration();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop36;
                    }
                } while (true);


                match(input, 112, FOLLOW_112_in_classBody876);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 22, classBody_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classBody"


    // $ANTLR start "interfaceBody"
    // example/Java.g:288:1: interfaceBody : '{' ( interfaceBodyDeclaration )* '}' ;
    public final void interfaceBody() throws RecognitionException {
        int interfaceBody_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 23)) {
                return;
            }

            // example/Java.g:289:5: ( '{' ( interfaceBodyDeclaration )* '}' )
            // example/Java.g:289:9: '{' ( interfaceBodyDeclaration )* '}'
            {
                match(input, 108, FOLLOW_108_in_interfaceBody899);
                if (state.failed) return;

                // example/Java.g:289:13: ( interfaceBodyDeclaration )*
                loop37:
                do {
                    int alt37 = 2;
                    switch (input.LA(1)) {
                        case ENUM:
                        case Identifier:
                        case 48:
                        case 49:
                        case 54:
                        case 59:
                        case 60:
                        case 62:
                        case 65:
                        case 66:
                        case 70:
                        case 74:
                        case 76:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 89:
                        case 90:
                        case 91:
                        case 93:
                        case 94:
                        case 95:
                        case 98:
                        case 102:
                        case 105:
                        case 106: {
                            alt37 = 1;
                        }
                        break;

                    }

                    switch (alt37) {
                        case 1:
                            // example/Java.g:289:13: interfaceBodyDeclaration
                        {
                            pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody901);
                            interfaceBodyDeclaration();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop37;
                    }
                } while (true);


                match(input, 112, FOLLOW_112_in_interfaceBody904);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 23, interfaceBody_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceBody"


    // $ANTLR start "classBodyDeclaration"
    // example/Java.g:292:1: classBodyDeclaration : ( ';' | ( 'static' )? block | modifiers memberDecl );
    public final void classBodyDeclaration() throws RecognitionException {
        int classBodyDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 24)) {
                return;
            }

            // example/Java.g:293:5: ( ';' | ( 'static' )? block | modifiers memberDecl )
            int alt39 = 3;
            switch (input.LA(1)) {
                case 48: {
                    alt39 = 1;
                }
                break;
                case 94: {
                    switch (input.LA(2)) {
                        case 108: {
                            alt39 = 2;
                        }
                        break;
                        case ENUM:
                        case Identifier:
                        case 49:
                        case 54:
                        case 59:
                        case 60:
                        case 62:
                        case 65:
                        case 66:
                        case 70:
                        case 74:
                        case 76:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 89:
                        case 90:
                        case 91:
                        case 93:
                        case 94:
                        case 95:
                        case 98:
                        case 102:
                        case 105:
                        case 106: {
                            alt39 = 3;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 39, 2, input);

                            throw nvae;

                    }

                }
                break;
                case 108: {
                    alt39 = 2;
                }
                break;
                case ENUM:
                case Identifier:
                case 49:
                case 54:
                case 59:
                case 60:
                case 62:
                case 65:
                case 66:
                case 70:
                case 74:
                case 76:
                case 82:
                case 83:
                case 84:
                case 85:
                case 89:
                case 90:
                case 91:
                case 93:
                case 95:
                case 98:
                case 102:
                case 105:
                case 106: {
                    alt39 = 3;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 39, 0, input);

                    throw nvae;

            }

            switch (alt39) {
                case 1:
                    // example/Java.g:293:9: ';'
                {
                    match(input, 48, FOLLOW_48_in_classBodyDeclaration923);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:294:9: ( 'static' )? block
                {
                    // example/Java.g:294:9: ( 'static' )?
                    int alt38 = 2;
                    switch (input.LA(1)) {
                        case 94: {
                            alt38 = 1;
                        }
                        break;
                    }

                    switch (alt38) {
                        case 1:
                            // example/Java.g:294:9: 'static'
                        {
                            match(input, 94, FOLLOW_94_in_classBodyDeclaration933);
                            if (state.failed) return;

                        }
                        break;

                    }


                    pushFollow(FOLLOW_block_in_classBodyDeclaration936);
                    block();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:295:9: modifiers memberDecl
                {
                    pushFollow(FOLLOW_modifiers_in_classBodyDeclaration946);
                    modifiers();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration948);
                    memberDecl();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 24, classBodyDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classBodyDeclaration"


    // $ANTLR start "memberDecl"
    // example/Java.g:298:1: memberDecl : ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void memberDecl() throws RecognitionException {
        int memberDecl_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 25)) {
                return;
            }

            // example/Java.g:299:5: ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt40 = 6;
            switch (input.LA(1)) {
                case 49: {
                    alt40 = 1;
                }
                break;
                case Identifier: {
                    switch (input.LA(2)) {
                        case Identifier:
                        case 43:
                        case 49:
                        case 55: {
                            alt40 = 2;
                        }
                        break;
                        case 32: {
                            alt40 = 4;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 40, 2, input);

                            throw nvae;

                    }

                }
                break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt40 = 2;
                }
                break;
                case 105: {
                    alt40 = 3;
                }
                break;
                case 54:
                case 83: {
                    alt40 = 5;
                }
                break;
                case ENUM:
                case 66: {
                    alt40 = 6;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 40, 0, input);

                    throw nvae;

            }

            switch (alt40) {
                case 1:
                    // example/Java.g:299:9: genericMethodOrConstructorDecl
                {
                    pushFollow(FOLLOW_genericMethodOrConstructorDecl_in_memberDecl971);
                    genericMethodOrConstructorDecl();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:300:9: memberDeclaration
                {
                    pushFollow(FOLLOW_memberDeclaration_in_memberDecl981);
                    memberDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:301:9: 'void' Identifier voidMethodDeclaratorRest
                {
                    match(input, 105, FOLLOW_105_in_memberDecl991);
                    if (state.failed) return;

                    match(input, Identifier, FOLLOW_Identifier_in_memberDecl993);
                    if (state.failed) return;

                    pushFollow(FOLLOW_voidMethodDeclaratorRest_in_memberDecl995);
                    voidMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:302:9: Identifier constructorDeclaratorRest
                {
                    match(input, Identifier, FOLLOW_Identifier_in_memberDecl1005);
                    if (state.failed) return;

                    pushFollow(FOLLOW_constructorDeclaratorRest_in_memberDecl1007);
                    constructorDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:303:9: interfaceDeclaration
                {
                    pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl1017);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:304:9: classDeclaration
                {
                    pushFollow(FOLLOW_classDeclaration_in_memberDecl1027);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 25, memberDecl_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "memberDecl"


    // $ANTLR start "memberDeclaration"
    // example/Java.g:307:1: memberDeclaration : type ( methodDeclaration | fieldDeclaration ) ;
    public final void memberDeclaration() throws RecognitionException {
        int memberDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 26)) {
                return;
            }

            // example/Java.g:308:5: ( type ( methodDeclaration | fieldDeclaration ) )
            // example/Java.g:308:9: type ( methodDeclaration | fieldDeclaration )
            {
                pushFollow(FOLLOW_type_in_memberDeclaration1050);
                type();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:308:14: ( methodDeclaration | fieldDeclaration )
                int alt41 = 2;
                switch (input.LA(1)) {
                    case Identifier: {
                        switch (input.LA(2)) {
                            case 32: {
                                alt41 = 1;
                            }
                            break;
                            case 39:
                            case 48:
                            case 50:
                            case 55: {
                                alt41 = 2;
                            }
                            break;
                            default:
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                NoViableAltException nvae =
                                        new NoViableAltException("", 41, 1, input);

                                throw nvae;

                        }

                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 41, 0, input);

                        throw nvae;

                }

                switch (alt41) {
                    case 1:
                        // example/Java.g:308:15: methodDeclaration
                    {
                        pushFollow(FOLLOW_methodDeclaration_in_memberDeclaration1053);
                        methodDeclaration();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;
                    case 2:
                        // example/Java.g:308:35: fieldDeclaration
                    {
                        pushFollow(FOLLOW_fieldDeclaration_in_memberDeclaration1057);
                        fieldDeclaration();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 26, memberDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "memberDeclaration"


    // $ANTLR start "genericMethodOrConstructorDecl"
    // example/Java.g:311:1: genericMethodOrConstructorDecl : typeParameters genericMethodOrConstructorRest ;
    public final void genericMethodOrConstructorDecl() throws RecognitionException {
        int genericMethodOrConstructorDecl_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 27)) {
                return;
            }

            // example/Java.g:312:5: ( typeParameters genericMethodOrConstructorRest )
            // example/Java.g:312:9: typeParameters genericMethodOrConstructorRest
            {
                pushFollow(FOLLOW_typeParameters_in_genericMethodOrConstructorDecl1077);
                typeParameters();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl1079);
                genericMethodOrConstructorRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 27, genericMethodOrConstructorDecl_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "genericMethodOrConstructorDecl"


    // $ANTLR start "genericMethodOrConstructorRest"
    // example/Java.g:315:1: genericMethodOrConstructorRest : ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest );
    public final void genericMethodOrConstructorRest() throws RecognitionException {
        int genericMethodOrConstructorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 28)) {
                return;
            }

            // example/Java.g:316:5: ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest )
            int alt43 = 2;
            switch (input.LA(1)) {
                case Identifier: {
                    switch (input.LA(2)) {
                        case Identifier:
                        case 43:
                        case 49:
                        case 55: {
                            alt43 = 1;
                        }
                        break;
                        case 32: {
                            alt43 = 2;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 43, 1, input);

                            throw nvae;

                    }

                }
                break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93:
                case 105: {
                    alt43 = 1;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 43, 0, input);

                    throw nvae;

            }

            switch (alt43) {
                case 1:
                    // example/Java.g:316:9: ( type | 'void' ) Identifier methodDeclaratorRest
                {
                    // example/Java.g:316:9: ( type | 'void' )
                    int alt42 = 2;
                    switch (input.LA(1)) {
                        case Identifier:
                        case 60:
                        case 62:
                        case 65:
                        case 70:
                        case 76:
                        case 82:
                        case 84:
                        case 93: {
                            alt42 = 1;
                        }
                        break;
                        case 105: {
                            alt42 = 2;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 42, 0, input);

                            throw nvae;

                    }

                    switch (alt42) {
                        case 1:
                            // example/Java.g:316:10: type
                        {
                            pushFollow(FOLLOW_type_in_genericMethodOrConstructorRest1103);
                            type();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;
                        case 2:
                            // example/Java.g:316:17: 'void'
                        {
                            match(input, 105, FOLLOW_105_in_genericMethodOrConstructorRest1107);
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, Identifier, FOLLOW_Identifier_in_genericMethodOrConstructorRest1110);
                    if (state.failed) return;

                    pushFollow(FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest1112);
                    methodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:317:9: Identifier constructorDeclaratorRest
                {
                    match(input, Identifier, FOLLOW_Identifier_in_genericMethodOrConstructorRest1122);
                    if (state.failed) return;

                    pushFollow(FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest1124);
                    constructorDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 28, genericMethodOrConstructorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "genericMethodOrConstructorRest"


    // $ANTLR start "methodDeclaration"
    // example/Java.g:320:1: methodDeclaration : Identifier methodDeclaratorRest ;
    public final void methodDeclaration() throws RecognitionException {
        int methodDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 29)) {
                return;
            }

            // example/Java.g:321:5: ( Identifier methodDeclaratorRest )
            // example/Java.g:321:9: Identifier methodDeclaratorRest
            {
                match(input, Identifier, FOLLOW_Identifier_in_methodDeclaration1143);
                if (state.failed) return;

                pushFollow(FOLLOW_methodDeclaratorRest_in_methodDeclaration1145);
                methodDeclaratorRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 29, methodDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "methodDeclaration"


    // $ANTLR start "fieldDeclaration"
    // example/Java.g:324:1: fieldDeclaration : variableDeclarators ';' ;
    public final void fieldDeclaration() throws RecognitionException {
        int fieldDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 30)) {
                return;
            }

            // example/Java.g:325:5: ( variableDeclarators ';' )
            // example/Java.g:325:9: variableDeclarators ';'
            {
                pushFollow(FOLLOW_variableDeclarators_in_fieldDeclaration1164);
                variableDeclarators();

                state._fsp--;
                if (state.failed) return;

                match(input, 48, FOLLOW_48_in_fieldDeclaration1166);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 30, fieldDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "fieldDeclaration"


    // $ANTLR start "interfaceBodyDeclaration"
    // example/Java.g:328:1: interfaceBodyDeclaration : ( modifiers interfaceMemberDecl | ';' );
    public final void interfaceBodyDeclaration() throws RecognitionException {
        int interfaceBodyDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 31)) {
                return;
            }

            // example/Java.g:329:5: ( modifiers interfaceMemberDecl | ';' )
            int alt44 = 2;
            switch (input.LA(1)) {
                case ENUM:
                case Identifier:
                case 49:
                case 54:
                case 59:
                case 60:
                case 62:
                case 65:
                case 66:
                case 70:
                case 74:
                case 76:
                case 82:
                case 83:
                case 84:
                case 85:
                case 89:
                case 90:
                case 91:
                case 93:
                case 94:
                case 95:
                case 98:
                case 102:
                case 105:
                case 106: {
                    alt44 = 1;
                }
                break;
                case 48: {
                    alt44 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 44, 0, input);

                    throw nvae;

            }

            switch (alt44) {
                case 1:
                    // example/Java.g:329:9: modifiers interfaceMemberDecl
                {
                    pushFollow(FOLLOW_modifiers_in_interfaceBodyDeclaration1193);
                    modifiers();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration1195);
                    interfaceMemberDecl();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:330:9: ';'
                {
                    match(input, 48, FOLLOW_48_in_interfaceBodyDeclaration1205);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 31, interfaceBodyDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceBodyDeclaration"


    // $ANTLR start "interfaceMemberDecl"
    // example/Java.g:333:1: interfaceMemberDecl : ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void interfaceMemberDecl() throws RecognitionException {
        int interfaceMemberDecl_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 32)) {
                return;
            }

            // example/Java.g:334:5: ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt45 = 5;
            switch (input.LA(1)) {
                case Identifier:
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt45 = 1;
                }
                break;
                case 49: {
                    alt45 = 2;
                }
                break;
                case 105: {
                    alt45 = 3;
                }
                break;
                case 54:
                case 83: {
                    alt45 = 4;
                }
                break;
                case ENUM:
                case 66: {
                    alt45 = 5;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 45, 0, input);

                    throw nvae;

            }

            switch (alt45) {
                case 1:
                    // example/Java.g:334:9: interfaceMethodOrFieldDecl
                {
                    pushFollow(FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl1224);
                    interfaceMethodOrFieldDecl();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:335:9: interfaceGenericMethodDecl
                {
                    pushFollow(FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl1234);
                    interfaceGenericMethodDecl();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:336:9: 'void' Identifier voidInterfaceMethodDeclaratorRest
                {
                    match(input, 105, FOLLOW_105_in_interfaceMemberDecl1244);
                    if (state.failed) return;

                    match(input, Identifier, FOLLOW_Identifier_in_interfaceMemberDecl1246);
                    if (state.failed) return;

                    pushFollow(FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl1248);
                    voidInterfaceMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:337:9: interfaceDeclaration
                {
                    pushFollow(FOLLOW_interfaceDeclaration_in_interfaceMemberDecl1258);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:338:9: classDeclaration
                {
                    pushFollow(FOLLOW_classDeclaration_in_interfaceMemberDecl1268);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 32, interfaceMemberDecl_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceMemberDecl"


    // $ANTLR start "interfaceMethodOrFieldDecl"
    // example/Java.g:341:1: interfaceMethodOrFieldDecl : type Identifier interfaceMethodOrFieldRest ;
    public final void interfaceMethodOrFieldDecl() throws RecognitionException {
        int interfaceMethodOrFieldDecl_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 33)) {
                return;
            }

            // example/Java.g:342:5: ( type Identifier interfaceMethodOrFieldRest )
            // example/Java.g:342:9: type Identifier interfaceMethodOrFieldRest
            {
                pushFollow(FOLLOW_type_in_interfaceMethodOrFieldDecl1291);
                type();

                state._fsp--;
                if (state.failed) return;

                match(input, Identifier, FOLLOW_Identifier_in_interfaceMethodOrFieldDecl1293);
                if (state.failed) return;

                pushFollow(FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl1295);
                interfaceMethodOrFieldRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 33, interfaceMethodOrFieldDecl_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceMethodOrFieldDecl"


    // $ANTLR start "interfaceMethodOrFieldRest"
    // example/Java.g:345:1: interfaceMethodOrFieldRest : ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest );
    public final void interfaceMethodOrFieldRest() throws RecognitionException {
        int interfaceMethodOrFieldRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 34)) {
                return;
            }

            // example/Java.g:346:5: ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest )
            int alt46 = 2;
            switch (input.LA(1)) {
                case 50:
                case 55: {
                    alt46 = 1;
                }
                break;
                case 32: {
                    alt46 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 46, 0, input);

                    throw nvae;

            }

            switch (alt46) {
                case 1:
                    // example/Java.g:346:9: constantDeclaratorsRest ';'
                {
                    pushFollow(FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest1318);
                    constantDeclaratorsRest();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 48, FOLLOW_48_in_interfaceMethodOrFieldRest1320);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:347:9: interfaceMethodDeclaratorRest
                {
                    pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest1330);
                    interfaceMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 34, interfaceMethodOrFieldRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceMethodOrFieldRest"


    // $ANTLR start "methodDeclaratorRest"
    // example/Java.g:350:1: methodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void methodDeclaratorRest() throws RecognitionException {
        int methodDeclaratorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 35)) {
                return;
            }

            // example/Java.g:351:5: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // example/Java.g:351:9: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
                pushFollow(FOLLOW_formalParameters_in_methodDeclaratorRest1353);
                formalParameters();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:351:26: ( '[' ']' )*
                loop47:
                do {
                    int alt47 = 2;
                    switch (input.LA(1)) {
                        case 55: {
                            alt47 = 1;
                        }
                        break;

                    }

                    switch (alt47) {
                        case 1:
                            // example/Java.g:351:27: '[' ']'
                        {
                            match(input, 55, FOLLOW_55_in_methodDeclaratorRest1356);
                            if (state.failed) return;

                            match(input, 56, FOLLOW_56_in_methodDeclaratorRest1358);
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop47;
                    }
                } while (true);


                // example/Java.g:352:9: ( 'throws' qualifiedNameList )?
                int alt48 = 2;
                switch (input.LA(1)) {
                    case 101: {
                        alt48 = 1;
                    }
                    break;
                }

                switch (alt48) {
                    case 1:
                        // example/Java.g:352:10: 'throws' qualifiedNameList
                    {
                        match(input, 101, FOLLOW_101_in_methodDeclaratorRest1371);
                        if (state.failed) return;

                        pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaratorRest1373);
                        qualifiedNameList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:353:9: ( methodBody | ';' )
                int alt49 = 2;
                switch (input.LA(1)) {
                    case 108: {
                        alt49 = 1;
                    }
                    break;
                    case 48: {
                        alt49 = 2;
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 49, 0, input);

                        throw nvae;

                }

                switch (alt49) {
                    case 1:
                        // example/Java.g:353:13: methodBody
                    {
                        pushFollow(FOLLOW_methodBody_in_methodDeclaratorRest1389);
                        methodBody();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;
                    case 2:
                        // example/Java.g:354:13: ';'
                    {
                        match(input, 48, FOLLOW_48_in_methodDeclaratorRest1403);
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 35, methodDeclaratorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "methodDeclaratorRest"


    // $ANTLR start "voidMethodDeclaratorRest"
    // example/Java.g:358:1: voidMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void voidMethodDeclaratorRest() throws RecognitionException {
        int voidMethodDeclaratorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 36)) {
                return;
            }

            // example/Java.g:359:5: ( formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // example/Java.g:359:9: formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
                pushFollow(FOLLOW_formalParameters_in_voidMethodDeclaratorRest1436);
                formalParameters();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:359:26: ( 'throws' qualifiedNameList )?
                int alt50 = 2;
                switch (input.LA(1)) {
                    case 101: {
                        alt50 = 1;
                    }
                    break;
                }

                switch (alt50) {
                    case 1:
                        // example/Java.g:359:27: 'throws' qualifiedNameList
                    {
                        match(input, 101, FOLLOW_101_in_voidMethodDeclaratorRest1439);
                        if (state.failed) return;

                        pushFollow(FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest1441);
                        qualifiedNameList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:360:9: ( methodBody | ';' )
                int alt51 = 2;
                switch (input.LA(1)) {
                    case 108: {
                        alt51 = 1;
                    }
                    break;
                    case 48: {
                        alt51 = 2;
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 51, 0, input);

                        throw nvae;

                }

                switch (alt51) {
                    case 1:
                        // example/Java.g:360:13: methodBody
                    {
                        pushFollow(FOLLOW_methodBody_in_voidMethodDeclaratorRest1457);
                        methodBody();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;
                    case 2:
                        // example/Java.g:361:13: ';'
                    {
                        match(input, 48, FOLLOW_48_in_voidMethodDeclaratorRest1471);
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 36, voidMethodDeclaratorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "voidMethodDeclaratorRest"


    // $ANTLR start "interfaceMethodDeclaratorRest"
    // example/Java.g:365:1: interfaceMethodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' ;
    public final void interfaceMethodDeclaratorRest() throws RecognitionException {
        int interfaceMethodDeclaratorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 37)) {
                return;
            }

            // example/Java.g:366:5: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' )
            // example/Java.g:366:9: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';'
            {
                pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest1504);
                formalParameters();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:366:26: ( '[' ']' )*
                loop52:
                do {
                    int alt52 = 2;
                    switch (input.LA(1)) {
                        case 55: {
                            alt52 = 1;
                        }
                        break;

                    }

                    switch (alt52) {
                        case 1:
                            // example/Java.g:366:27: '[' ']'
                        {
                            match(input, 55, FOLLOW_55_in_interfaceMethodDeclaratorRest1507);
                            if (state.failed) return;

                            match(input, 56, FOLLOW_56_in_interfaceMethodDeclaratorRest1509);
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop52;
                    }
                } while (true);


                // example/Java.g:366:37: ( 'throws' qualifiedNameList )?
                int alt53 = 2;
                switch (input.LA(1)) {
                    case 101: {
                        alt53 = 1;
                    }
                    break;
                }

                switch (alt53) {
                    case 1:
                        // example/Java.g:366:38: 'throws' qualifiedNameList
                    {
                        match(input, 101, FOLLOW_101_in_interfaceMethodDeclaratorRest1514);
                        if (state.failed) return;

                        pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest1516);
                        qualifiedNameList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, 48, FOLLOW_48_in_interfaceMethodDeclaratorRest1520);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 37, interfaceMethodDeclaratorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceMethodDeclaratorRest"


    // $ANTLR start "interfaceGenericMethodDecl"
    // example/Java.g:369:1: interfaceGenericMethodDecl : typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest ;
    public final void interfaceGenericMethodDecl() throws RecognitionException {
        int interfaceGenericMethodDecl_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 38)) {
                return;
            }

            // example/Java.g:370:5: ( typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest )
            // example/Java.g:370:9: typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest
            {
                pushFollow(FOLLOW_typeParameters_in_interfaceGenericMethodDecl1543);
                typeParameters();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:370:24: ( type | 'void' )
                int alt54 = 2;
                switch (input.LA(1)) {
                    case Identifier:
                    case 60:
                    case 62:
                    case 65:
                    case 70:
                    case 76:
                    case 82:
                    case 84:
                    case 93: {
                        alt54 = 1;
                    }
                    break;
                    case 105: {
                        alt54 = 2;
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 54, 0, input);

                        throw nvae;

                }

                switch (alt54) {
                    case 1:
                        // example/Java.g:370:25: type
                    {
                        pushFollow(FOLLOW_type_in_interfaceGenericMethodDecl1546);
                        type();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;
                    case 2:
                        // example/Java.g:370:32: 'void'
                    {
                        match(input, 105, FOLLOW_105_in_interfaceGenericMethodDecl1550);
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, Identifier, FOLLOW_Identifier_in_interfaceGenericMethodDecl1553);
                if (state.failed) return;

                pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl1563);
                interfaceMethodDeclaratorRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 38, interfaceGenericMethodDecl_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "interfaceGenericMethodDecl"


    // $ANTLR start "voidInterfaceMethodDeclaratorRest"
    // example/Java.g:374:1: voidInterfaceMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ';' ;
    public final void voidInterfaceMethodDeclaratorRest() throws RecognitionException {
        int voidInterfaceMethodDeclaratorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 39)) {
                return;
            }

            // example/Java.g:375:5: ( formalParameters ( 'throws' qualifiedNameList )? ';' )
            // example/Java.g:375:9: formalParameters ( 'throws' qualifiedNameList )? ';'
            {
                pushFollow(FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest1586);
                formalParameters();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:375:26: ( 'throws' qualifiedNameList )?
                int alt55 = 2;
                switch (input.LA(1)) {
                    case 101: {
                        alt55 = 1;
                    }
                    break;
                }

                switch (alt55) {
                    case 1:
                        // example/Java.g:375:27: 'throws' qualifiedNameList
                    {
                        match(input, 101, FOLLOW_101_in_voidInterfaceMethodDeclaratorRest1589);
                        if (state.failed) return;

                        pushFollow(FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest1591);
                        qualifiedNameList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, 48, FOLLOW_48_in_voidInterfaceMethodDeclaratorRest1595);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 39, voidInterfaceMethodDeclaratorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "voidInterfaceMethodDeclaratorRest"


    // $ANTLR start "constructorDeclaratorRest"
    // example/Java.g:378:1: constructorDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? constructorBody ;
    public final void constructorDeclaratorRest() throws RecognitionException {
        int constructorDeclaratorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 40)) {
                return;
            }

            // example/Java.g:379:5: ( formalParameters ( 'throws' qualifiedNameList )? constructorBody )
            // example/Java.g:379:9: formalParameters ( 'throws' qualifiedNameList )? constructorBody
            {
                pushFollow(FOLLOW_formalParameters_in_constructorDeclaratorRest1618);
                formalParameters();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:379:26: ( 'throws' qualifiedNameList )?
                int alt56 = 2;
                switch (input.LA(1)) {
                    case 101: {
                        alt56 = 1;
                    }
                    break;
                }

                switch (alt56) {
                    case 1:
                        // example/Java.g:379:27: 'throws' qualifiedNameList
                    {
                        match(input, 101, FOLLOW_101_in_constructorDeclaratorRest1621);
                        if (state.failed) return;

                        pushFollow(FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1623);
                        qualifiedNameList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                pushFollow(FOLLOW_constructorBody_in_constructorDeclaratorRest1627);
                constructorBody();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 40, constructorDeclaratorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "constructorDeclaratorRest"


    // $ANTLR start "constantDeclarator"
    // example/Java.g:382:1: constantDeclarator : Identifier constantDeclaratorRest ;
    public final void constantDeclarator() throws RecognitionException {
        int constantDeclarator_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 41)) {
                return;
            }

            // example/Java.g:383:5: ( Identifier constantDeclaratorRest )
            // example/Java.g:383:9: Identifier constantDeclaratorRest
            {
                match(input, Identifier, FOLLOW_Identifier_in_constantDeclarator1646);
                if (state.failed) return;

                pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclarator1648);
                constantDeclaratorRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 41, constantDeclarator_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "constantDeclarator"


    // $ANTLR start "variableDeclarators"
    // example/Java.g:386:1: variableDeclarators : variableDeclarator ( ',' variableDeclarator )* ;
    public final void variableDeclarators() throws RecognitionException {
        int variableDeclarators_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 42)) {
                return;
            }

            // example/Java.g:387:5: ( variableDeclarator ( ',' variableDeclarator )* )
            // example/Java.g:387:9: variableDeclarator ( ',' variableDeclarator )*
            {
                pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1671);
                variableDeclarator();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:387:28: ( ',' variableDeclarator )*
                loop57:
                do {
                    int alt57 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt57 = 1;
                        }
                        break;

                    }

                    switch (alt57) {
                        case 1:
                            // example/Java.g:387:29: ',' variableDeclarator
                        {
                            match(input, 39, FOLLOW_39_in_variableDeclarators1674);
                            if (state.failed) return;

                            pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1676);
                            variableDeclarator();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop57;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 42, variableDeclarators_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "variableDeclarators"


    // $ANTLR start "variableDeclarator"
    // example/Java.g:390:1: variableDeclarator : variableDeclaratorId ( '=' variableInitializer )? ;
    public final void variableDeclarator() throws RecognitionException {
        int variableDeclarator_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 43)) {
                return;
            }

            // example/Java.g:391:5: ( variableDeclaratorId ( '=' variableInitializer )? )
            // example/Java.g:391:9: variableDeclaratorId ( '=' variableInitializer )?
            {
                pushFollow(FOLLOW_variableDeclaratorId_in_variableDeclarator1697);
                variableDeclaratorId();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:391:30: ( '=' variableInitializer )?
                int alt58 = 2;
                switch (input.LA(1)) {
                    case 50: {
                        alt58 = 1;
                    }
                    break;
                }

                switch (alt58) {
                    case 1:
                        // example/Java.g:391:31: '=' variableInitializer
                    {
                        match(input, 50, FOLLOW_50_in_variableDeclarator1700);
                        if (state.failed) return;

                        pushFollow(FOLLOW_variableInitializer_in_variableDeclarator1702);
                        variableInitializer();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 43, variableDeclarator_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "variableDeclarator"


    // $ANTLR start "constantDeclaratorsRest"
    // example/Java.g:394:1: constantDeclaratorsRest : constantDeclaratorRest ( ',' constantDeclarator )* ;
    public final void constantDeclaratorsRest() throws RecognitionException {
        int constantDeclaratorsRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 44)) {
                return;
            }

            // example/Java.g:395:5: ( constantDeclaratorRest ( ',' constantDeclarator )* )
            // example/Java.g:395:9: constantDeclaratorRest ( ',' constantDeclarator )*
            {
                pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1727);
                constantDeclaratorRest();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:395:32: ( ',' constantDeclarator )*
                loop59:
                do {
                    int alt59 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt59 = 1;
                        }
                        break;

                    }

                    switch (alt59) {
                        case 1:
                            // example/Java.g:395:33: ',' constantDeclarator
                        {
                            match(input, 39, FOLLOW_39_in_constantDeclaratorsRest1730);
                            if (state.failed) return;

                            pushFollow(FOLLOW_constantDeclarator_in_constantDeclaratorsRest1732);
                            constantDeclarator();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop59;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 44, constantDeclaratorsRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "constantDeclaratorsRest"


    // $ANTLR start "constantDeclaratorRest"
    // example/Java.g:398:1: constantDeclaratorRest : ( '[' ']' )* '=' variableInitializer ;
    public final void constantDeclaratorRest() throws RecognitionException {
        int constantDeclaratorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 45)) {
                return;
            }

            // example/Java.g:399:5: ( ( '[' ']' )* '=' variableInitializer )
            // example/Java.g:399:9: ( '[' ']' )* '=' variableInitializer
            {
                // example/Java.g:399:9: ( '[' ']' )*
                loop60:
                do {
                    int alt60 = 2;
                    switch (input.LA(1)) {
                        case 55: {
                            alt60 = 1;
                        }
                        break;

                    }

                    switch (alt60) {
                        case 1:
                            // example/Java.g:399:10: '[' ']'
                        {
                            match(input, 55, FOLLOW_55_in_constantDeclaratorRest1754);
                            if (state.failed) return;

                            match(input, 56, FOLLOW_56_in_constantDeclaratorRest1756);
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop60;
                    }
                } while (true);


                match(input, 50, FOLLOW_50_in_constantDeclaratorRest1760);
                if (state.failed) return;

                pushFollow(FOLLOW_variableInitializer_in_constantDeclaratorRest1762);
                variableInitializer();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 45, constantDeclaratorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "constantDeclaratorRest"


    // $ANTLR start "variableDeclaratorId"
    // example/Java.g:402:1: variableDeclaratorId : Identifier ( '[' ']' )* ;
    public final void variableDeclaratorId() throws RecognitionException {
        int variableDeclaratorId_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 46)) {
                return;
            }

            // example/Java.g:403:5: ( Identifier ( '[' ']' )* )
            // example/Java.g:403:9: Identifier ( '[' ']' )*
            {
                match(input, Identifier, FOLLOW_Identifier_in_variableDeclaratorId1785);
                if (state.failed) return;

                // example/Java.g:403:20: ( '[' ']' )*
                loop61:
                do {
                    int alt61 = 2;
                    switch (input.LA(1)) {
                        case 55: {
                            alt61 = 1;
                        }
                        break;

                    }

                    switch (alt61) {
                        case 1:
                            // example/Java.g:403:21: '[' ']'
                        {
                            match(input, 55, FOLLOW_55_in_variableDeclaratorId1788);
                            if (state.failed) return;

                            match(input, 56, FOLLOW_56_in_variableDeclaratorId1790);
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop61;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 46, variableDeclaratorId_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "variableDeclaratorId"


    // $ANTLR start "variableInitializer"
    // example/Java.g:406:1: variableInitializer : ( arrayInitializer | expression );
    public final void variableInitializer() throws RecognitionException {
        int variableInitializer_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 47)) {
                return;
            }

            // example/Java.g:407:5: ( arrayInitializer | expression )
            int alt62 = 2;
            switch (input.LA(1)) {
                case 108: {
                    alt62 = 1;
                }
                break;
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case Identifier:
                case OctalLiteral:
                case StringLiteral:
                case 25:
                case 32:
                case 36:
                case 37:
                case 40:
                case 41:
                case 60:
                case 62:
                case 65:
                case 70:
                case 73:
                case 76:
                case 82:
                case 84:
                case 86:
                case 87:
                case 93:
                case 96:
                case 99:
                case 103:
                case 105:
                case 113: {
                    alt62 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 62, 0, input);

                    throw nvae;

            }

            switch (alt62) {
                case 1:
                    // example/Java.g:407:9: arrayInitializer
                {
                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer1811);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:408:9: expression
                {
                    pushFollow(FOLLOW_expression_in_variableInitializer1821);
                    expression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 47, variableInitializer_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "variableInitializer"


    // $ANTLR start "arrayInitializer"
    // example/Java.g:411:1: arrayInitializer : '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' ;
    public final void arrayInitializer() throws RecognitionException {
        int arrayInitializer_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 48)) {
                return;
            }

            // example/Java.g:412:5: ( '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' )
            // example/Java.g:412:9: '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}'
            {
                match(input, 108, FOLLOW_108_in_arrayInitializer1848);
                if (state.failed) return;

                // example/Java.g:412:13: ( variableInitializer ( ',' variableInitializer )* ( ',' )? )?
                int alt65 = 2;
                switch (input.LA(1)) {
                    case CharacterLiteral:
                    case DecimalLiteral:
                    case FloatingPointLiteral:
                    case HexLiteral:
                    case Identifier:
                    case OctalLiteral:
                    case StringLiteral:
                    case 25:
                    case 32:
                    case 36:
                    case 37:
                    case 40:
                    case 41:
                    case 60:
                    case 62:
                    case 65:
                    case 70:
                    case 73:
                    case 76:
                    case 82:
                    case 84:
                    case 86:
                    case 87:
                    case 93:
                    case 96:
                    case 99:
                    case 103:
                    case 105:
                    case 108:
                    case 113: {
                        alt65 = 1;
                    }
                    break;
                }

                switch (alt65) {
                    case 1:
                        // example/Java.g:412:14: variableInitializer ( ',' variableInitializer )* ( ',' )?
                    {
                        pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1851);
                        variableInitializer();

                        state._fsp--;
                        if (state.failed) return;

                        // example/Java.g:412:34: ( ',' variableInitializer )*
                        loop63:
                        do {
                            int alt63 = 2;
                            switch (input.LA(1)) {
                                case 39: {
                                    switch (input.LA(2)) {
                                        case CharacterLiteral:
                                        case DecimalLiteral:
                                        case FloatingPointLiteral:
                                        case HexLiteral:
                                        case Identifier:
                                        case OctalLiteral:
                                        case StringLiteral:
                                        case 25:
                                        case 32:
                                        case 36:
                                        case 37:
                                        case 40:
                                        case 41:
                                        case 60:
                                        case 62:
                                        case 65:
                                        case 70:
                                        case 73:
                                        case 76:
                                        case 82:
                                        case 84:
                                        case 86:
                                        case 87:
                                        case 93:
                                        case 96:
                                        case 99:
                                        case 103:
                                        case 105:
                                        case 108:
                                        case 113: {
                                            alt63 = 1;
                                        }
                                        break;

                                    }

                                }
                                break;

                            }

                            switch (alt63) {
                                case 1:
                                    // example/Java.g:412:35: ',' variableInitializer
                                {
                                    match(input, 39, FOLLOW_39_in_arrayInitializer1854);
                                    if (state.failed) return;

                                    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1856);
                                    variableInitializer();

                                    state._fsp--;
                                    if (state.failed) return;

                                }
                                break;

                                default:
                                    break loop63;
                            }
                        } while (true);


                        // example/Java.g:412:61: ( ',' )?
                        int alt64 = 2;
                        switch (input.LA(1)) {
                            case 39: {
                                alt64 = 1;
                            }
                            break;
                        }

                        switch (alt64) {
                            case 1:
                                // example/Java.g:412:62: ','
                            {
                                match(input, 39, FOLLOW_39_in_arrayInitializer1861);
                                if (state.failed) return;

                            }
                            break;

                        }


                    }
                    break;

                }


                match(input, 112, FOLLOW_112_in_arrayInitializer1868);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 48, arrayInitializer_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "arrayInitializer"


    // $ANTLR start "modifier"
    // example/Java.g:415:1: modifier : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' );
    public final void modifier() throws RecognitionException {
        int modifier_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 49)) {
                return;
            }

            // example/Java.g:416:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )
            int alt66 = 12;
            switch (input.LA(1)) {
                case 54: {
                    alt66 = 1;
                }
                break;
                case 91: {
                    alt66 = 2;
                }
                break;
                case 90: {
                    alt66 = 3;
                }
                break;
                case 89: {
                    alt66 = 4;
                }
                break;
                case 94: {
                    alt66 = 5;
                }
                break;
                case 59: {
                    alt66 = 6;
                }
                break;
                case 74: {
                    alt66 = 7;
                }
                break;
                case 85: {
                    alt66 = 8;
                }
                break;
                case 98: {
                    alt66 = 9;
                }
                break;
                case 102: {
                    alt66 = 10;
                }
                break;
                case 106: {
                    alt66 = 11;
                }
                break;
                case 95: {
                    alt66 = 12;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 66, 0, input);

                    throw nvae;

            }

            switch (alt66) {
                case 1:
                    // example/Java.g:416:9: annotation
                {
                    pushFollow(FOLLOW_annotation_in_modifier1887);
                    annotation();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:417:9: 'public'
                {
                    match(input, 91, FOLLOW_91_in_modifier1897);
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:418:9: 'protected'
                {
                    match(input, 90, FOLLOW_90_in_modifier1907);
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:419:9: 'private'
                {
                    match(input, 89, FOLLOW_89_in_modifier1917);
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:420:9: 'static'
                {
                    match(input, 94, FOLLOW_94_in_modifier1927);
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:421:9: 'abstract'
                {
                    match(input, 59, FOLLOW_59_in_modifier1937);
                    if (state.failed) return;

                }
                break;
                case 7:
                    // example/Java.g:422:9: 'final'
                {
                    match(input, 74, FOLLOW_74_in_modifier1947);
                    if (state.failed) return;

                }
                break;
                case 8:
                    // example/Java.g:423:9: 'native'
                {
                    match(input, 85, FOLLOW_85_in_modifier1957);
                    if (state.failed) return;

                }
                break;
                case 9:
                    // example/Java.g:424:9: 'synchronized'
                {
                    match(input, 98, FOLLOW_98_in_modifier1967);
                    if (state.failed) return;

                }
                break;
                case 10:
                    // example/Java.g:425:9: 'transient'
                {
                    match(input, 102, FOLLOW_102_in_modifier1977);
                    if (state.failed) return;

                }
                break;
                case 11:
                    // example/Java.g:426:9: 'volatile'
                {
                    match(input, 106, FOLLOW_106_in_modifier1987);
                    if (state.failed) return;

                }
                break;
                case 12:
                    // example/Java.g:427:9: 'strictfp'
                {
                    match(input, 95, FOLLOW_95_in_modifier1997);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 49, modifier_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "modifier"


    // $ANTLR start "packageOrTypeName"
    // example/Java.g:430:1: packageOrTypeName : qualifiedName ;
    public final void packageOrTypeName() throws RecognitionException {
        int packageOrTypeName_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 50)) {
                return;
            }

            // example/Java.g:431:5: ( qualifiedName )
            // example/Java.g:431:9: qualifiedName
            {
                pushFollow(FOLLOW_qualifiedName_in_packageOrTypeName2016);
                qualifiedName();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 50, packageOrTypeName_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "packageOrTypeName"


    // $ANTLR start "enumConstantName"
    // example/Java.g:434:1: enumConstantName : Identifier ;
    public final void enumConstantName() throws RecognitionException {
        int enumConstantName_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 51)) {
                return;
            }

            // example/Java.g:435:5: ( Identifier )
            // example/Java.g:435:9: Identifier
            {
                match(input, Identifier, FOLLOW_Identifier_in_enumConstantName2035);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 51, enumConstantName_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "enumConstantName"


    // $ANTLR start "typeName"
    // example/Java.g:438:1: typeName : qualifiedName ;
    public final void typeName() throws RecognitionException {
        int typeName_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 52)) {
                return;
            }

            // example/Java.g:439:5: ( qualifiedName )
            // example/Java.g:439:9: qualifiedName
            {
                pushFollow(FOLLOW_qualifiedName_in_typeName2054);
                qualifiedName();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 52, typeName_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeName"


    // $ANTLR start "type"
    // example/Java.g:442:1: type : ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* );
    public final void type() throws RecognitionException {
        int type_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 53)) {
                return;
            }

            // example/Java.g:443:2: ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* )
            int alt69 = 2;
            switch (input.LA(1)) {
                case Identifier: {
                    alt69 = 1;
                }
                break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt69 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 69, 0, input);

                    throw nvae;

            }

            switch (alt69) {
                case 1:
                    // example/Java.g:443:4: classOrInterfaceType ( '[' ']' )*
                {
                    pushFollow(FOLLOW_classOrInterfaceType_in_type2068);
                    classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:443:25: ( '[' ']' )*
                    loop67:
                    do {
                        int alt67 = 2;
                        switch (input.LA(1)) {
                            case 55: {
                                alt67 = 1;
                            }
                            break;

                        }

                        switch (alt67) {
                            case 1:
                                // example/Java.g:443:26: '[' ']'
                            {
                                match(input, 55, FOLLOW_55_in_type2071);
                                if (state.failed) return;

                                match(input, 56, FOLLOW_56_in_type2073);
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop67;
                        }
                    } while (true);


                }
                break;
                case 2:
                    // example/Java.g:444:4: primitiveType ( '[' ']' )*
                {
                    pushFollow(FOLLOW_primitiveType_in_type2080);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:444:18: ( '[' ']' )*
                    loop68:
                    do {
                        int alt68 = 2;
                        switch (input.LA(1)) {
                            case 55: {
                                alt68 = 1;
                            }
                            break;

                        }

                        switch (alt68) {
                            case 1:
                                // example/Java.g:444:19: '[' ']'
                            {
                                match(input, 55, FOLLOW_55_in_type2083);
                                if (state.failed) return;

                                match(input, 56, FOLLOW_56_in_type2085);
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop68;
                        }
                    } while (true);


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 53, type_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "type"


    // $ANTLR start "classOrInterfaceType"
    // example/Java.g:447:1: classOrInterfaceType : Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* ;
    public final void classOrInterfaceType() throws RecognitionException {
        int classOrInterfaceType_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 54)) {
                return;
            }

            // example/Java.g:448:2: ( Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* )
            // example/Java.g:448:4: Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )*
            {
                match(input, Identifier, FOLLOW_Identifier_in_classOrInterfaceType2098);
                if (state.failed) return;

                // example/Java.g:448:15: ( typeArguments )?
                int alt70 = 2;
                switch (input.LA(1)) {
                    case 49: {
                        switch (input.LA(2)) {
                            case Identifier:
                            case 53:
                            case 60:
                            case 62:
                            case 65:
                            case 70:
                            case 76:
                            case 82:
                            case 84:
                            case 93: {
                                alt70 = 1;
                            }
                            break;
                        }

                    }
                    break;
                }

                switch (alt70) {
                    case 1:
                        // example/Java.g:448:15: typeArguments
                    {
                        pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2100);
                        typeArguments();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:448:30: ( '.' Identifier ( typeArguments )? )*
                loop72:
                do {
                    int alt72 = 2;
                    switch (input.LA(1)) {
                        case 43: {
                            alt72 = 1;
                        }
                        break;

                    }

                    switch (alt72) {
                        case 1:
                            // example/Java.g:448:31: '.' Identifier ( typeArguments )?
                        {
                            match(input, 43, FOLLOW_43_in_classOrInterfaceType2104);
                            if (state.failed) return;

                            match(input, Identifier, FOLLOW_Identifier_in_classOrInterfaceType2106);
                            if (state.failed) return;

                            // example/Java.g:448:46: ( typeArguments )?
                            int alt71 = 2;
                            switch (input.LA(1)) {
                                case 49: {
                                    switch (input.LA(2)) {
                                        case Identifier:
                                        case 53:
                                        case 60:
                                        case 62:
                                        case 65:
                                        case 70:
                                        case 76:
                                        case 82:
                                        case 84:
                                        case 93: {
                                            alt71 = 1;
                                        }
                                        break;
                                    }

                                }
                                break;
                            }

                            switch (alt71) {
                                case 1:
                                    // example/Java.g:448:46: typeArguments
                                {
                                    pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2108);
                                    typeArguments();

                                    state._fsp--;
                                    if (state.failed) return;

                                }
                                break;

                            }


                        }
                        break;

                        default:
                            break loop72;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 54, classOrInterfaceType_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classOrInterfaceType"


    // $ANTLR start "primitiveType"
    // example/Java.g:451:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
    public final void primitiveType() throws RecognitionException {
        int primitiveType_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 55)) {
                return;
            }

            // example/Java.g:452:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
            // example/Java.g:
            {
                if (input.LA(1) == 60 || input.LA(1) == 62 || input.LA(1) == 65 || input.LA(1) == 70 || input.LA(1) == 76 || input.LA(1) == 82 || input.LA(1) == 84 || input.LA(1) == 93) {
                    input.consume();
                    state.errorRecovery = false;
                    state.failed = false;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 55, primitiveType_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "primitiveType"


    // $ANTLR start "variableModifier"
    // example/Java.g:462:1: variableModifier : ( 'final' | annotation );
    public final void variableModifier() throws RecognitionException {
        int variableModifier_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 56)) {
                return;
            }

            // example/Java.g:463:5: ( 'final' | annotation )
            int alt73 = 2;
            switch (input.LA(1)) {
                case 74: {
                    alt73 = 1;
                }
                break;
                case 54: {
                    alt73 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 73, 0, input);

                    throw nvae;

            }

            switch (alt73) {
                case 1:
                    // example/Java.g:463:9: 'final'
                {
                    match(input, 74, FOLLOW_74_in_variableModifier2217);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:464:9: annotation
                {
                    pushFollow(FOLLOW_annotation_in_variableModifier2227);
                    annotation();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 56, variableModifier_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "variableModifier"


    // $ANTLR start "typeArguments"
    // example/Java.g:467:1: typeArguments : '<' typeArgument ( ',' typeArgument )* '>' ;
    public final void typeArguments() throws RecognitionException {
        int typeArguments_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 57)) {
                return;
            }

            // example/Java.g:468:5: ( '<' typeArgument ( ',' typeArgument )* '>' )
            // example/Java.g:468:9: '<' typeArgument ( ',' typeArgument )* '>'
            {
                match(input, 49, FOLLOW_49_in_typeArguments2246);
                if (state.failed) return;

                pushFollow(FOLLOW_typeArgument_in_typeArguments2248);
                typeArgument();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:468:26: ( ',' typeArgument )*
                loop74:
                do {
                    int alt74 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt74 = 1;
                        }
                        break;

                    }

                    switch (alt74) {
                        case 1:
                            // example/Java.g:468:27: ',' typeArgument
                        {
                            match(input, 39, FOLLOW_39_in_typeArguments2251);
                            if (state.failed) return;

                            pushFollow(FOLLOW_typeArgument_in_typeArguments2253);
                            typeArgument();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop74;
                    }
                } while (true);


                match(input, 52, FOLLOW_52_in_typeArguments2257);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 57, typeArguments_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeArguments"


    // $ANTLR start "typeArgument"
    // example/Java.g:471:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );
    public final void typeArgument() throws RecognitionException {
        int typeArgument_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 58)) {
                return;
            }

            // example/Java.g:472:5: ( type | '?' ( ( 'extends' | 'super' ) type )? )
            int alt76 = 2;
            switch (input.LA(1)) {
                case Identifier:
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt76 = 1;
                }
                break;
                case 53: {
                    alt76 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 76, 0, input);

                    throw nvae;

            }

            switch (alt76) {
                case 1:
                    // example/Java.g:472:9: type
                {
                    pushFollow(FOLLOW_type_in_typeArgument2280);
                    type();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:473:9: '?' ( ( 'extends' | 'super' ) type )?
                {
                    match(input, 53, FOLLOW_53_in_typeArgument2290);
                    if (state.failed) return;

                    // example/Java.g:473:13: ( ( 'extends' | 'super' ) type )?
                    int alt75 = 2;
                    switch (input.LA(1)) {
                        case 72:
                        case 96: {
                            alt75 = 1;
                        }
                        break;
                    }

                    switch (alt75) {
                        case 1:
                            // example/Java.g:473:14: ( 'extends' | 'super' ) type
                        {
                            if (input.LA(1) == 72 || input.LA(1) == 96) {
                                input.consume();
                                state.errorRecovery = false;
                                state.failed = false;
                            } else {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                throw mse;
                            }


                            pushFollow(FOLLOW_type_in_typeArgument2301);
                            type();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 58, typeArgument_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "typeArgument"


    // $ANTLR start "qualifiedNameList"
    // example/Java.g:476:1: qualifiedNameList : qualifiedName ( ',' qualifiedName )* ;
    public final void qualifiedNameList() throws RecognitionException {
        int qualifiedNameList_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 59)) {
                return;
            }

            // example/Java.g:477:5: ( qualifiedName ( ',' qualifiedName )* )
            // example/Java.g:477:9: qualifiedName ( ',' qualifiedName )*
            {
                pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2326);
                qualifiedName();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:477:23: ( ',' qualifiedName )*
                loop77:
                do {
                    int alt77 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt77 = 1;
                        }
                        break;

                    }

                    switch (alt77) {
                        case 1:
                            // example/Java.g:477:24: ',' qualifiedName
                        {
                            match(input, 39, FOLLOW_39_in_qualifiedNameList2329);
                            if (state.failed) return;

                            pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2331);
                            qualifiedName();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop77;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 59, qualifiedNameList_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "qualifiedNameList"


    // $ANTLR start "formalParameters"
    // example/Java.g:480:1: formalParameters : '(' ( formalParameterDecls )? ')' ;
    public final void formalParameters() throws RecognitionException {
        int formalParameters_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 60)) {
                return;
            }

            // example/Java.g:481:5: ( '(' ( formalParameterDecls )? ')' )
            // example/Java.g:481:9: '(' ( formalParameterDecls )? ')'
            {
                match(input, 32, FOLLOW_32_in_formalParameters2352);
                if (state.failed) return;

                // example/Java.g:481:13: ( formalParameterDecls )?
                int alt78 = 2;
                switch (input.LA(1)) {
                    case Identifier:
                    case 54:
                    case 60:
                    case 62:
                    case 65:
                    case 70:
                    case 74:
                    case 76:
                    case 82:
                    case 84:
                    case 93: {
                        alt78 = 1;
                    }
                    break;
                }

                switch (alt78) {
                    case 1:
                        // example/Java.g:481:13: formalParameterDecls
                    {
                        pushFollow(FOLLOW_formalParameterDecls_in_formalParameters2354);
                        formalParameterDecls();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, 33, FOLLOW_33_in_formalParameters2357);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 60, formalParameters_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "formalParameters"


    // $ANTLR start "formalParameterDecls"
    // example/Java.g:484:1: formalParameterDecls : variableModifiers type formalParameterDeclsRest ;
    public final void formalParameterDecls() throws RecognitionException {
        int formalParameterDecls_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 61)) {
                return;
            }

            // example/Java.g:485:5: ( variableModifiers type formalParameterDeclsRest )
            // example/Java.g:485:9: variableModifiers type formalParameterDeclsRest
            {
                pushFollow(FOLLOW_variableModifiers_in_formalParameterDecls2380);
                variableModifiers();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_type_in_formalParameterDecls2382);
                type();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_formalParameterDeclsRest_in_formalParameterDecls2384);
                formalParameterDeclsRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 61, formalParameterDecls_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "formalParameterDecls"


    // $ANTLR start "formalParameterDeclsRest"
    // example/Java.g:488:1: formalParameterDeclsRest : ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId );
    public final void formalParameterDeclsRest() throws RecognitionException {
        int formalParameterDeclsRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 62)) {
                return;
            }

            // example/Java.g:489:5: ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId )
            int alt80 = 2;
            switch (input.LA(1)) {
                case Identifier: {
                    alt80 = 1;
                }
                break;
                case 44: {
                    alt80 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 80, 0, input);

                    throw nvae;

            }

            switch (alt80) {
                case 1:
                    // example/Java.g:489:9: variableDeclaratorId ( ',' formalParameterDecls )?
                {
                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2407);
                    variableDeclaratorId();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:489:30: ( ',' formalParameterDecls )?
                    int alt79 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt79 = 1;
                        }
                        break;
                    }

                    switch (alt79) {
                        case 1:
                            // example/Java.g:489:31: ',' formalParameterDecls
                        {
                            match(input, 39, FOLLOW_39_in_formalParameterDeclsRest2410);
                            if (state.failed) return;

                            pushFollow(FOLLOW_formalParameterDecls_in_formalParameterDeclsRest2412);
                            formalParameterDecls();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 2:
                    // example/Java.g:490:9: '...' variableDeclaratorId
                {
                    match(input, 44, FOLLOW_44_in_formalParameterDeclsRest2424);
                    if (state.failed) return;

                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2426);
                    variableDeclaratorId();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 62, formalParameterDeclsRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "formalParameterDeclsRest"


    // $ANTLR start "methodBody"
    // example/Java.g:493:1: methodBody : block ;
    public final void methodBody() throws RecognitionException {
        int methodBody_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 63)) {
                return;
            }

            // example/Java.g:494:5: ( block )
            // example/Java.g:494:9: block
            {
                pushFollow(FOLLOW_block_in_methodBody2449);
                block();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 63, methodBody_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "methodBody"


    // $ANTLR start "constructorBody"
    // example/Java.g:497:1: constructorBody : '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' ;
    public final void constructorBody() throws RecognitionException {
        int constructorBody_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 64)) {
                return;
            }

            // example/Java.g:498:5: ( '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' )
            // example/Java.g:498:9: '{' ( explicitConstructorInvocation )? ( blockStatement )* '}'
            {
                match(input, 108, FOLLOW_108_in_constructorBody2468);
                if (state.failed) return;

                // example/Java.g:498:13: ( explicitConstructorInvocation )?
                int alt81 = 2;
                switch (input.LA(1)) {
                    case 49: {
                        alt81 = 1;
                    }
                    break;
                    case 99: {
                        int LA81_2 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case 32: {
                        int LA81_3 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case 96: {
                        int LA81_4 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case DecimalLiteral:
                    case HexLiteral:
                    case OctalLiteral: {
                        int LA81_5 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case FloatingPointLiteral: {
                        int LA81_6 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case CharacterLiteral: {
                        int LA81_7 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case StringLiteral: {
                        int LA81_8 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case 73:
                    case 103: {
                        int LA81_9 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case 87: {
                        int LA81_10 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case 86: {
                        int LA81_11 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case Identifier: {
                        int LA81_12 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case 60:
                    case 62:
                    case 65:
                    case 70:
                    case 76:
                    case 82:
                    case 84:
                    case 93: {
                        int LA81_13 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                    case 105: {
                        int LA81_14 = input.LA(2);

                        if ((synpred113_Java())) {
                            alt81 = 1;
                        }
                    }
                    break;
                }

                switch (alt81) {
                    case 1:
                        // example/Java.g:498:13: explicitConstructorInvocation
                    {
                        pushFollow(FOLLOW_explicitConstructorInvocation_in_constructorBody2470);
                        explicitConstructorInvocation();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                // example/Java.g:498:44: ( blockStatement )*
                loop82:
                do {
                    int alt82 = 2;
                    switch (input.LA(1)) {
                        case ASSERT:
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case ENUM:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 48:
                        case 54:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 65:
                        case 66:
                        case 67:
                        case 69:
                        case 70:
                        case 73:
                        case 74:
                        case 76:
                        case 77:
                        case 78:
                        case 82:
                        case 83:
                        case 84:
                        case 86:
                        case 87:
                        case 89:
                        case 90:
                        case 91:
                        case 92:
                        case 93:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 100:
                        case 103:
                        case 104:
                        case 105:
                        case 107:
                        case 108:
                        case 113: {
                            alt82 = 1;
                        }
                        break;

                    }

                    switch (alt82) {
                        case 1:
                            // example/Java.g:498:44: blockStatement
                        {
                            pushFollow(FOLLOW_blockStatement_in_constructorBody2473);
                            blockStatement();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop82;
                    }
                } while (true);


                match(input, 112, FOLLOW_112_in_constructorBody2476);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 64, constructorBody_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "constructorBody"


    // $ANTLR start "explicitConstructorInvocation"
    // example/Java.g:501:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' );
    public final void explicitConstructorInvocation() throws RecognitionException {
        int explicitConstructorInvocation_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 65)) {
                return;
            }

            // example/Java.g:502:5: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' )
            int alt85 = 2;
            switch (input.LA(1)) {
                case 49: {
                    alt85 = 1;
                }
                break;
                case 99: {
                    int LA85_2 = input.LA(2);

                    if ((synpred117_Java())) {
                        alt85 = 1;
                    } else if ((true)) {
                        alt85 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 85, 2, input);

                        throw nvae;

                    }
                }
                break;
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case Identifier:
                case OctalLiteral:
                case StringLiteral:
                case 32:
                case 60:
                case 62:
                case 65:
                case 70:
                case 73:
                case 76:
                case 82:
                case 84:
                case 86:
                case 87:
                case 93:
                case 103:
                case 105: {
                    alt85 = 2;
                }
                break;
                case 96: {
                    int LA85_4 = input.LA(2);

                    if ((synpred117_Java())) {
                        alt85 = 1;
                    } else if ((true)) {
                        alt85 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 85, 4, input);

                        throw nvae;

                    }
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 85, 0, input);

                    throw nvae;

            }

            switch (alt85) {
                case 1:
                    // example/Java.g:502:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
                {
                    // example/Java.g:502:9: ( nonWildcardTypeArguments )?
                    int alt83 = 2;
                    switch (input.LA(1)) {
                        case 49: {
                            alt83 = 1;
                        }
                        break;
                    }

                    switch (alt83) {
                        case 1:
                            // example/Java.g:502:9: nonWildcardTypeArguments
                        {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2495);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    if (input.LA(1) == 96 || input.LA(1) == 99) {
                        input.consume();
                        state.errorRecovery = false;
                        state.failed = false;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        MismatchedSetException mse = new MismatchedSetException(null, input);
                        throw mse;
                    }


                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation2506);
                    arguments();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 48, FOLLOW_48_in_explicitConstructorInvocation2508);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:503:9: primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';'
                {
                    pushFollow(FOLLOW_primary_in_explicitConstructorInvocation2518);
                    primary();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 43, FOLLOW_43_in_explicitConstructorInvocation2520);
                    if (state.failed) return;

                    // example/Java.g:503:21: ( nonWildcardTypeArguments )?
                    int alt84 = 2;
                    switch (input.LA(1)) {
                        case 49: {
                            alt84 = 1;
                        }
                        break;
                    }

                    switch (alt84) {
                        case 1:
                            // example/Java.g:503:21: nonWildcardTypeArguments
                        {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2522);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 96, FOLLOW_96_in_explicitConstructorInvocation2525);
                    if (state.failed) return;

                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation2527);
                    arguments();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 48, FOLLOW_48_in_explicitConstructorInvocation2529);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 65, explicitConstructorInvocation_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "explicitConstructorInvocation"


    // $ANTLR start "qualifiedName"
    // example/Java.g:507:1: qualifiedName : Identifier ( '.' Identifier )* ;
    public final void qualifiedName() throws RecognitionException {
        int qualifiedName_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 66)) {
                return;
            }

            // example/Java.g:508:5: ( Identifier ( '.' Identifier )* )
            // example/Java.g:508:9: Identifier ( '.' Identifier )*
            {
                match(input, Identifier, FOLLOW_Identifier_in_qualifiedName2549);
                if (state.failed) return;

                // example/Java.g:508:20: ( '.' Identifier )*
                loop86:
                do {
                    int alt86 = 2;
                    switch (input.LA(1)) {
                        case 43: {
                            switch (input.LA(2)) {
                                case Identifier: {
                                    alt86 = 1;
                                }
                                break;

                            }

                        }
                        break;

                    }

                    switch (alt86) {
                        case 1:
                            // example/Java.g:508:21: '.' Identifier
                        {
                            match(input, 43, FOLLOW_43_in_qualifiedName2552);
                            if (state.failed) return;

                            match(input, Identifier, FOLLOW_Identifier_in_qualifiedName2554);
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop86;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 66, qualifiedName_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "qualifiedName"


    // $ANTLR start "literal"
    // example/Java.g:511:1: literal : ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' );
    public final void literal() throws RecognitionException {
        int literal_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 67)) {
                return;
            }

            // example/Java.g:512:5: ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' )
            int alt87 = 6;
            switch (input.LA(1)) {
                case DecimalLiteral:
                case HexLiteral:
                case OctalLiteral: {
                    alt87 = 1;
                }
                break;
                case FloatingPointLiteral: {
                    alt87 = 2;
                }
                break;
                case CharacterLiteral: {
                    alt87 = 3;
                }
                break;
                case StringLiteral: {
                    alt87 = 4;
                }
                break;
                case 73:
                case 103: {
                    alt87 = 5;
                }
                break;
                case 87: {
                    alt87 = 6;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 87, 0, input);

                    throw nvae;

            }

            switch (alt87) {
                case 1:
                    // example/Java.g:512:9: integerLiteral
                {
                    pushFollow(FOLLOW_integerLiteral_in_literal2580);
                    integerLiteral();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:513:9: FloatingPointLiteral
                {
                    match(input, FloatingPointLiteral, FOLLOW_FloatingPointLiteral_in_literal2590);
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:514:9: CharacterLiteral
                {
                    match(input, CharacterLiteral, FOLLOW_CharacterLiteral_in_literal2600);
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:515:9: StringLiteral
                {
                    match(input, StringLiteral, FOLLOW_StringLiteral_in_literal2610);
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:516:9: booleanLiteral
                {
                    pushFollow(FOLLOW_booleanLiteral_in_literal2620);
                    booleanLiteral();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:517:9: 'null'
                {
                    match(input, 87, FOLLOW_87_in_literal2630);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 67, literal_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "literal"


    // $ANTLR start "integerLiteral"
    // example/Java.g:520:1: integerLiteral : ( HexLiteral | OctalLiteral | DecimalLiteral );
    public final void integerLiteral() throws RecognitionException {
        int integerLiteral_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 68)) {
                return;
            }

            // example/Java.g:521:5: ( HexLiteral | OctalLiteral | DecimalLiteral )
            // example/Java.g:
            {
                if (input.LA(1) == DecimalLiteral || input.LA(1) == HexLiteral || input.LA(1) == OctalLiteral) {
                    input.consume();
                    state.errorRecovery = false;
                    state.failed = false;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 68, integerLiteral_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "integerLiteral"


    // $ANTLR start "booleanLiteral"
    // example/Java.g:526:1: booleanLiteral : ( 'true' | 'false' );
    public final void booleanLiteral() throws RecognitionException {
        int booleanLiteral_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 69)) {
                return;
            }

            // example/Java.g:527:5: ( 'true' | 'false' )
            // example/Java.g:
            {
                if (input.LA(1) == 73 || input.LA(1) == 103) {
                    input.consume();
                    state.errorRecovery = false;
                    state.failed = false;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 69, booleanLiteral_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "booleanLiteral"


    // $ANTLR start "annotations"
    // example/Java.g:533:1: annotations : ( annotation )+ ;
    public final void annotations() throws RecognitionException {
        int annotations_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 70)) {
                return;
            }

            // example/Java.g:534:5: ( ( annotation )+ )
            // example/Java.g:534:9: ( annotation )+
            {
                // example/Java.g:534:9: ( annotation )+
                int cnt88 = 0;
                loop88:
                do {
                    int alt88 = 2;
                    switch (input.LA(1)) {
                        case 54: {
                            switch (input.LA(2)) {
                                case Identifier: {
                                    int LA88_3 = input.LA(3);

                                    if ((synpred128_Java())) {
                                        alt88 = 1;
                                    }


                                }
                                break;

                            }

                        }
                        break;

                    }

                    switch (alt88) {
                        case 1:
                            // example/Java.g:534:9: annotation
                        {
                            pushFollow(FOLLOW_annotation_in_annotations2719);
                            annotation();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            if (cnt88 >= 1) break loop88;
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(88, input);
                            throw eee;
                    }
                    cnt88++;
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 70, annotations_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotations"


    // $ANTLR start "annotation"
    // example/Java.g:537:1: annotation : '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )? ;
    public final void annotation() throws RecognitionException {
        int annotation_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 71)) {
                return;
            }

            // example/Java.g:538:5: ( '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )? )
            // example/Java.g:538:9: '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )?
            {
                match(input, 54, FOLLOW_54_in_annotation2739);
                if (state.failed) return;

                pushFollow(FOLLOW_annotationName_in_annotation2741);
                annotationName();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:538:28: ( '(' ( elementValuePairs | elementValue )? ')' )?
                int alt90 = 2;
                switch (input.LA(1)) {
                    case 32: {
                        alt90 = 1;
                    }
                    break;
                }

                switch (alt90) {
                    case 1:
                        // example/Java.g:538:30: '(' ( elementValuePairs | elementValue )? ')'
                    {
                        match(input, 32, FOLLOW_32_in_annotation2745);
                        if (state.failed) return;

                        // example/Java.g:538:34: ( elementValuePairs | elementValue )?
                        int alt89 = 3;
                        switch (input.LA(1)) {
                            case Identifier: {
                                switch (input.LA(2)) {
                                    case 50: {
                                        alt89 = 1;
                                    }
                                    break;
                                    case 26:
                                    case 27:
                                    case 29:
                                    case 30:
                                    case 32:
                                    case 33:
                                    case 34:
                                    case 36:
                                    case 37:
                                    case 40:
                                    case 41:
                                    case 43:
                                    case 45:
                                    case 49:
                                    case 51:
                                    case 52:
                                    case 53:
                                    case 55:
                                    case 57:
                                    case 81:
                                    case 109:
                                    case 111: {
                                        alt89 = 2;
                                    }
                                    break;
                                }

                            }
                            break;
                            case CharacterLiteral:
                            case DecimalLiteral:
                            case FloatingPointLiteral:
                            case HexLiteral:
                            case OctalLiteral:
                            case StringLiteral:
                            case 25:
                            case 32:
                            case 36:
                            case 37:
                            case 40:
                            case 41:
                            case 54:
                            case 60:
                            case 62:
                            case 65:
                            case 70:
                            case 73:
                            case 76:
                            case 82:
                            case 84:
                            case 86:
                            case 87:
                            case 93:
                            case 96:
                            case 99:
                            case 103:
                            case 105:
                            case 108:
                            case 113: {
                                alt89 = 2;
                            }
                            break;
                        }

                        switch (alt89) {
                            case 1:
                                // example/Java.g:538:36: elementValuePairs
                            {
                                pushFollow(FOLLOW_elementValuePairs_in_annotation2749);
                                elementValuePairs();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;
                            case 2:
                                // example/Java.g:538:56: elementValue
                            {
                                pushFollow(FOLLOW_elementValue_in_annotation2753);
                                elementValue();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;

                        }


                        match(input, 33, FOLLOW_33_in_annotation2758);
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 71, annotation_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotation"


    // $ANTLR start "annotationName"
    // example/Java.g:541:1: annotationName : Identifier ( '.' Identifier )* ;
    public final void annotationName() throws RecognitionException {
        int annotationName_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 72)) {
                return;
            }

            // example/Java.g:542:5: ( Identifier ( '.' Identifier )* )
            // example/Java.g:542:7: Identifier ( '.' Identifier )*
            {
                match(input, Identifier, FOLLOW_Identifier_in_annotationName2782);
                if (state.failed) return;

                // example/Java.g:542:18: ( '.' Identifier )*
                loop91:
                do {
                    int alt91 = 2;
                    switch (input.LA(1)) {
                        case 43: {
                            alt91 = 1;
                        }
                        break;

                    }

                    switch (alt91) {
                        case 1:
                            // example/Java.g:542:19: '.' Identifier
                        {
                            match(input, 43, FOLLOW_43_in_annotationName2785);
                            if (state.failed) return;

                            match(input, Identifier, FOLLOW_Identifier_in_annotationName2787);
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop91;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 72, annotationName_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationName"


    // $ANTLR start "elementValuePairs"
    // example/Java.g:545:1: elementValuePairs : elementValuePair ( ',' elementValuePair )* ;
    public final void elementValuePairs() throws RecognitionException {
        int elementValuePairs_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 73)) {
                return;
            }

            // example/Java.g:546:5: ( elementValuePair ( ',' elementValuePair )* )
            // example/Java.g:546:9: elementValuePair ( ',' elementValuePair )*
            {
                pushFollow(FOLLOW_elementValuePair_in_elementValuePairs2808);
                elementValuePair();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:546:26: ( ',' elementValuePair )*
                loop92:
                do {
                    int alt92 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt92 = 1;
                        }
                        break;

                    }

                    switch (alt92) {
                        case 1:
                            // example/Java.g:546:27: ',' elementValuePair
                        {
                            match(input, 39, FOLLOW_39_in_elementValuePairs2811);
                            if (state.failed) return;

                            pushFollow(FOLLOW_elementValuePair_in_elementValuePairs2813);
                            elementValuePair();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop92;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 73, elementValuePairs_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "elementValuePairs"


    // $ANTLR start "elementValuePair"
    // example/Java.g:549:1: elementValuePair : Identifier '=' elementValue ;
    public final void elementValuePair() throws RecognitionException {
        int elementValuePair_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 74)) {
                return;
            }

            // example/Java.g:550:5: ( Identifier '=' elementValue )
            // example/Java.g:550:9: Identifier '=' elementValue
            {
                match(input, Identifier, FOLLOW_Identifier_in_elementValuePair2834);
                if (state.failed) return;

                match(input, 50, FOLLOW_50_in_elementValuePair2836);
                if (state.failed) return;

                pushFollow(FOLLOW_elementValue_in_elementValuePair2838);
                elementValue();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 74, elementValuePair_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "elementValuePair"


    // $ANTLR start "elementValue"
    // example/Java.g:553:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );
    public final void elementValue() throws RecognitionException {
        int elementValue_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 75)) {
                return;
            }

            // example/Java.g:554:5: ( conditionalExpression | annotation | elementValueArrayInitializer )
            int alt93 = 3;
            switch (input.LA(1)) {
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case Identifier:
                case OctalLiteral:
                case StringLiteral:
                case 25:
                case 32:
                case 36:
                case 37:
                case 40:
                case 41:
                case 60:
                case 62:
                case 65:
                case 70:
                case 73:
                case 76:
                case 82:
                case 84:
                case 86:
                case 87:
                case 93:
                case 96:
                case 99:
                case 103:
                case 105:
                case 113: {
                    alt93 = 1;
                }
                break;
                case 54: {
                    alt93 = 2;
                }
                break;
                case 108: {
                    alt93 = 3;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 93, 0, input);

                    throw nvae;

            }

            switch (alt93) {
                case 1:
                    // example/Java.g:554:9: conditionalExpression
                {
                    pushFollow(FOLLOW_conditionalExpression_in_elementValue2861);
                    conditionalExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:555:9: annotation
                {
                    pushFollow(FOLLOW_annotation_in_elementValue2871);
                    annotation();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:556:9: elementValueArrayInitializer
                {
                    pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue2881);
                    elementValueArrayInitializer();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 75, elementValue_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "elementValue"


    // $ANTLR start "elementValueArrayInitializer"
    // example/Java.g:559:1: elementValueArrayInitializer : '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' ;
    public final void elementValueArrayInitializer() throws RecognitionException {
        int elementValueArrayInitializer_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 76)) {
                return;
            }

            // example/Java.g:560:5: ( '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' )
            // example/Java.g:560:9: '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}'
            {
                match(input, 108, FOLLOW_108_in_elementValueArrayInitializer2904);
                if (state.failed) return;

                // example/Java.g:560:13: ( elementValue ( ',' elementValue )* )?
                int alt95 = 2;
                switch (input.LA(1)) {
                    case CharacterLiteral:
                    case DecimalLiteral:
                    case FloatingPointLiteral:
                    case HexLiteral:
                    case Identifier:
                    case OctalLiteral:
                    case StringLiteral:
                    case 25:
                    case 32:
                    case 36:
                    case 37:
                    case 40:
                    case 41:
                    case 54:
                    case 60:
                    case 62:
                    case 65:
                    case 70:
                    case 73:
                    case 76:
                    case 82:
                    case 84:
                    case 86:
                    case 87:
                    case 93:
                    case 96:
                    case 99:
                    case 103:
                    case 105:
                    case 108:
                    case 113: {
                        alt95 = 1;
                    }
                    break;
                }

                switch (alt95) {
                    case 1:
                        // example/Java.g:560:14: elementValue ( ',' elementValue )*
                    {
                        pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2907);
                        elementValue();

                        state._fsp--;
                        if (state.failed) return;

                        // example/Java.g:560:27: ( ',' elementValue )*
                        loop94:
                        do {
                            int alt94 = 2;
                            switch (input.LA(1)) {
                                case 39: {
                                    switch (input.LA(2)) {
                                        case CharacterLiteral:
                                        case DecimalLiteral:
                                        case FloatingPointLiteral:
                                        case HexLiteral:
                                        case Identifier:
                                        case OctalLiteral:
                                        case StringLiteral:
                                        case 25:
                                        case 32:
                                        case 36:
                                        case 37:
                                        case 40:
                                        case 41:
                                        case 54:
                                        case 60:
                                        case 62:
                                        case 65:
                                        case 70:
                                        case 73:
                                        case 76:
                                        case 82:
                                        case 84:
                                        case 86:
                                        case 87:
                                        case 93:
                                        case 96:
                                        case 99:
                                        case 103:
                                        case 105:
                                        case 108:
                                        case 113: {
                                            alt94 = 1;
                                        }
                                        break;

                                    }

                                }
                                break;

                            }

                            switch (alt94) {
                                case 1:
                                    // example/Java.g:560:28: ',' elementValue
                                {
                                    match(input, 39, FOLLOW_39_in_elementValueArrayInitializer2910);
                                    if (state.failed) return;

                                    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2912);
                                    elementValue();

                                    state._fsp--;
                                    if (state.failed) return;

                                }
                                break;

                                default:
                                    break loop94;
                            }
                        } while (true);


                    }
                    break;

                }


                // example/Java.g:560:49: ( ',' )?
                int alt96 = 2;
                switch (input.LA(1)) {
                    case 39: {
                        alt96 = 1;
                    }
                    break;
                }

                switch (alt96) {
                    case 1:
                        // example/Java.g:560:50: ','
                    {
                        match(input, 39, FOLLOW_39_in_elementValueArrayInitializer2919);
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, 112, FOLLOW_112_in_elementValueArrayInitializer2923);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 76, elementValueArrayInitializer_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "elementValueArrayInitializer"


    // $ANTLR start "annotationTypeDeclaration"
    // example/Java.g:563:1: annotationTypeDeclaration : '@' 'interface' Identifier annotationTypeBody ;
    public final void annotationTypeDeclaration() throws RecognitionException {
        int annotationTypeDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 77)) {
                return;
            }

            // example/Java.g:564:5: ( '@' 'interface' Identifier annotationTypeBody )
            // example/Java.g:564:9: '@' 'interface' Identifier annotationTypeBody
            {
                match(input, 54, FOLLOW_54_in_annotationTypeDeclaration2946);
                if (state.failed) return;

                match(input, 83, FOLLOW_83_in_annotationTypeDeclaration2948);
                if (state.failed) return;

                match(input, Identifier, FOLLOW_Identifier_in_annotationTypeDeclaration2950);
                if (state.failed) return;

                pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration2952);
                annotationTypeBody();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 77, annotationTypeDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationTypeDeclaration"


    // $ANTLR start "annotationTypeBody"
    // example/Java.g:567:1: annotationTypeBody : '{' ( annotationTypeElementDeclaration )* '}' ;
    public final void annotationTypeBody() throws RecognitionException {
        int annotationTypeBody_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 78)) {
                return;
            }

            // example/Java.g:568:5: ( '{' ( annotationTypeElementDeclaration )* '}' )
            // example/Java.g:568:9: '{' ( annotationTypeElementDeclaration )* '}'
            {
                match(input, 108, FOLLOW_108_in_annotationTypeBody2975);
                if (state.failed) return;

                // example/Java.g:568:13: ( annotationTypeElementDeclaration )*
                loop97:
                do {
                    int alt97 = 2;
                    switch (input.LA(1)) {
                        case ENUM:
                        case Identifier:
                        case 49:
                        case 54:
                        case 59:
                        case 60:
                        case 62:
                        case 65:
                        case 66:
                        case 70:
                        case 74:
                        case 76:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 89:
                        case 90:
                        case 91:
                        case 93:
                        case 94:
                        case 95:
                        case 98:
                        case 102:
                        case 105:
                        case 106: {
                            alt97 = 1;
                        }
                        break;

                    }

                    switch (alt97) {
                        case 1:
                            // example/Java.g:568:14: annotationTypeElementDeclaration
                        {
                            pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody2978);
                            annotationTypeElementDeclaration();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop97;
                    }
                } while (true);


                match(input, 112, FOLLOW_112_in_annotationTypeBody2982);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 78, annotationTypeBody_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationTypeBody"


    // $ANTLR start "annotationTypeElementDeclaration"
    // example/Java.g:571:1: annotationTypeElementDeclaration : modifiers annotationTypeElementRest ;
    public final void annotationTypeElementDeclaration() throws RecognitionException {
        int annotationTypeElementDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 79)) {
                return;
            }

            // example/Java.g:572:5: ( modifiers annotationTypeElementRest )
            // example/Java.g:572:9: modifiers annotationTypeElementRest
            {
                pushFollow(FOLLOW_modifiers_in_annotationTypeElementDeclaration3005);
                modifiers();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration3007);
                annotationTypeElementRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 79, annotationTypeElementDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationTypeElementDeclaration"


    // $ANTLR start "annotationTypeElementRest"
    // example/Java.g:575:1: annotationTypeElementRest : ( type annotationMethodOrConstantRest ';' | normalClassDeclaration ( ';' )? | normalInterfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? );
    public final void annotationTypeElementRest() throws RecognitionException {
        int annotationTypeElementRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 80)) {
                return;
            }

            // example/Java.g:576:5: ( type annotationMethodOrConstantRest ';' | normalClassDeclaration ( ';' )? | normalInterfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? )
            int alt102 = 5;
            switch (input.LA(1)) {
                case Identifier:
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt102 = 1;
                }
                break;
                case 66: {
                    alt102 = 2;
                }
                break;
                case 83: {
                    alt102 = 3;
                }
                break;
                case ENUM: {
                    alt102 = 4;
                }
                break;
                case 54: {
                    alt102 = 5;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 102, 0, input);

                    throw nvae;

            }

            switch (alt102) {
                case 1:
                    // example/Java.g:576:9: type annotationMethodOrConstantRest ';'
                {
                    pushFollow(FOLLOW_type_in_annotationTypeElementRest3030);
                    type();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest3032);
                    annotationMethodOrConstantRest();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 48, FOLLOW_48_in_annotationTypeElementRest3034);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:577:9: normalClassDeclaration ( ';' )?
                {
                    pushFollow(FOLLOW_normalClassDeclaration_in_annotationTypeElementRest3044);
                    normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:577:32: ( ';' )?
                    int alt98 = 2;
                    switch (input.LA(1)) {
                        case 48: {
                            alt98 = 1;
                        }
                        break;
                    }

                    switch (alt98) {
                        case 1:
                            // example/Java.g:577:32: ';'
                        {
                            match(input, 48, FOLLOW_48_in_annotationTypeElementRest3046);
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 3:
                    // example/Java.g:578:9: normalInterfaceDeclaration ( ';' )?
                {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementRest3057);
                    normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:578:36: ( ';' )?
                    int alt99 = 2;
                    switch (input.LA(1)) {
                        case 48: {
                            alt99 = 1;
                        }
                        break;
                    }

                    switch (alt99) {
                        case 1:
                            // example/Java.g:578:36: ';'
                        {
                            match(input, 48, FOLLOW_48_in_annotationTypeElementRest3059);
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 4:
                    // example/Java.g:579:9: enumDeclaration ( ';' )?
                {
                    pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementRest3070);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:579:25: ( ';' )?
                    int alt100 = 2;
                    switch (input.LA(1)) {
                        case 48: {
                            alt100 = 1;
                        }
                        break;
                    }

                    switch (alt100) {
                        case 1:
                            // example/Java.g:579:25: ';'
                        {
                            match(input, 48, FOLLOW_48_in_annotationTypeElementRest3072);
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 5:
                    // example/Java.g:580:9: annotationTypeDeclaration ( ';' )?
                {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest3083);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:580:35: ( ';' )?
                    int alt101 = 2;
                    switch (input.LA(1)) {
                        case 48: {
                            alt101 = 1;
                        }
                        break;
                    }

                    switch (alt101) {
                        case 1:
                            // example/Java.g:580:35: ';'
                        {
                            match(input, 48, FOLLOW_48_in_annotationTypeElementRest3085);
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 80, annotationTypeElementRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationTypeElementRest"


    // $ANTLR start "annotationMethodOrConstantRest"
    // example/Java.g:583:1: annotationMethodOrConstantRest : ( annotationMethodRest | annotationConstantRest );
    public final void annotationMethodOrConstantRest() throws RecognitionException {
        int annotationMethodOrConstantRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 81)) {
                return;
            }

            // example/Java.g:584:5: ( annotationMethodRest | annotationConstantRest )
            int alt103 = 2;
            switch (input.LA(1)) {
                case Identifier: {
                    switch (input.LA(2)) {
                        case 32: {
                            alt103 = 1;
                        }
                        break;
                        case 39:
                        case 48:
                        case 50:
                        case 55: {
                            alt103 = 2;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 103, 1, input);

                            throw nvae;

                    }

                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 103, 0, input);

                    throw nvae;

            }

            switch (alt103) {
                case 1:
                    // example/Java.g:584:9: annotationMethodRest
                {
                    pushFollow(FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest3109);
                    annotationMethodRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:585:9: annotationConstantRest
                {
                    pushFollow(FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest3119);
                    annotationConstantRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 81, annotationMethodOrConstantRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationMethodOrConstantRest"


    // $ANTLR start "annotationMethodRest"
    // example/Java.g:588:1: annotationMethodRest : Identifier '(' ')' ( defaultValue )? ;
    public final void annotationMethodRest() throws RecognitionException {
        int annotationMethodRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 82)) {
                return;
            }

            // example/Java.g:589:5: ( Identifier '(' ')' ( defaultValue )? )
            // example/Java.g:589:9: Identifier '(' ')' ( defaultValue )?
            {
                match(input, Identifier, FOLLOW_Identifier_in_annotationMethodRest3142);
                if (state.failed) return;

                match(input, 32, FOLLOW_32_in_annotationMethodRest3144);
                if (state.failed) return;

                match(input, 33, FOLLOW_33_in_annotationMethodRest3146);
                if (state.failed) return;

                // example/Java.g:589:28: ( defaultValue )?
                int alt104 = 2;
                switch (input.LA(1)) {
                    case 68: {
                        alt104 = 1;
                    }
                    break;
                }

                switch (alt104) {
                    case 1:
                        // example/Java.g:589:28: defaultValue
                    {
                        pushFollow(FOLLOW_defaultValue_in_annotationMethodRest3148);
                        defaultValue();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 82, annotationMethodRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationMethodRest"


    // $ANTLR start "annotationConstantRest"
    // example/Java.g:592:1: annotationConstantRest : variableDeclarators ;
    public final void annotationConstantRest() throws RecognitionException {
        int annotationConstantRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 83)) {
                return;
            }

            // example/Java.g:593:5: ( variableDeclarators )
            // example/Java.g:593:9: variableDeclarators
            {
                pushFollow(FOLLOW_variableDeclarators_in_annotationConstantRest3172);
                variableDeclarators();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 83, annotationConstantRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "annotationConstantRest"


    // $ANTLR start "defaultValue"
    // example/Java.g:596:1: defaultValue : 'default' elementValue ;
    public final void defaultValue() throws RecognitionException {
        int defaultValue_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 84)) {
                return;
            }

            // example/Java.g:597:5: ( 'default' elementValue )
            // example/Java.g:597:9: 'default' elementValue
            {
                match(input, 68, FOLLOW_68_in_defaultValue3195);
                if (state.failed) return;

                pushFollow(FOLLOW_elementValue_in_defaultValue3197);
                elementValue();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 84, defaultValue_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "defaultValue"


    // $ANTLR start "block"
    // example/Java.g:602:1: block : '{' ( blockStatement )* '}' ;
    public final void block() throws RecognitionException {
        int block_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 85)) {
                return;
            }

            // example/Java.g:603:5: ( '{' ( blockStatement )* '}' )
            // example/Java.g:603:9: '{' ( blockStatement )* '}'
            {
                match(input, 108, FOLLOW_108_in_block3218);
                if (state.failed) return;

                // example/Java.g:603:13: ( blockStatement )*
                loop105:
                do {
                    int alt105 = 2;
                    switch (input.LA(1)) {
                        case ASSERT:
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case ENUM:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 48:
                        case 54:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 65:
                        case 66:
                        case 67:
                        case 69:
                        case 70:
                        case 73:
                        case 74:
                        case 76:
                        case 77:
                        case 78:
                        case 82:
                        case 83:
                        case 84:
                        case 86:
                        case 87:
                        case 89:
                        case 90:
                        case 91:
                        case 92:
                        case 93:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 100:
                        case 103:
                        case 104:
                        case 105:
                        case 107:
                        case 108:
                        case 113: {
                            alt105 = 1;
                        }
                        break;

                    }

                    switch (alt105) {
                        case 1:
                            // example/Java.g:603:13: blockStatement
                        {
                            pushFollow(FOLLOW_blockStatement_in_block3220);
                            blockStatement();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop105;
                    }
                } while (true);


                match(input, 112, FOLLOW_112_in_block3223);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 85, block_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "block"


    // $ANTLR start "blockStatement"
    // example/Java.g:606:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );
    public final void blockStatement() throws RecognitionException {
        int blockStatement_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 86)) {
                return;
            }

            // example/Java.g:607:5: ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement )
            int alt106 = 3;
            switch (input.LA(1)) {
                case 74: {
                    int LA106_1 = input.LA(2);

                    if ((synpred151_Java())) {
                        alt106 = 1;
                    } else if ((synpred152_Java())) {
                        alt106 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 106, 1, input);

                        throw nvae;

                    }
                }
                break;
                case 54: {
                    int LA106_2 = input.LA(2);

                    if ((synpred151_Java())) {
                        alt106 = 1;
                    } else if ((synpred152_Java())) {
                        alt106 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 106, 2, input);

                        throw nvae;

                    }
                }
                break;
                case Identifier: {
                    int LA106_3 = input.LA(2);

                    if ((synpred151_Java())) {
                        alt106 = 1;
                    } else if ((true)) {
                        alt106 = 3;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 106, 3, input);

                        throw nvae;

                    }
                }
                break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    int LA106_4 = input.LA(2);

                    if ((synpred151_Java())) {
                        alt106 = 1;
                    } else if ((true)) {
                        alt106 = 3;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 106, 4, input);

                        throw nvae;

                    }
                }
                break;
                case ENUM:
                case 59:
                case 66:
                case 83:
                case 89:
                case 90:
                case 91:
                case 94:
                case 95: {
                    alt106 = 2;
                }
                break;
                case ASSERT:
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case OctalLiteral:
                case StringLiteral:
                case 25:
                case 32:
                case 36:
                case 37:
                case 40:
                case 41:
                case 48:
                case 61:
                case 67:
                case 69:
                case 73:
                case 77:
                case 78:
                case 86:
                case 87:
                case 92:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 103:
                case 104:
                case 105:
                case 107:
                case 108:
                case 113: {
                    alt106 = 3;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 106, 0, input);

                    throw nvae;

            }

            switch (alt106) {
                case 1:
                    // example/Java.g:607:9: localVariableDeclarationStatement
                {
                    pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement3246);
                    localVariableDeclarationStatement();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:608:9: classOrInterfaceDeclaration
                {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement3256);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:609:9: statement
                {
                    pushFollow(FOLLOW_statement_in_blockStatement3266);
                    statement();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 86, blockStatement_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "blockStatement"


    // $ANTLR start "localVariableDeclarationStatement"
    // example/Java.g:612:1: localVariableDeclarationStatement : localVariableDeclaration ';' ;
    public final void localVariableDeclarationStatement() throws RecognitionException {
        int localVariableDeclarationStatement_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 87)) {
                return;
            }

            // example/Java.g:613:5: ( localVariableDeclaration ';' )
            // example/Java.g:613:10: localVariableDeclaration ';'
            {
                pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3290);
                localVariableDeclaration();

                state._fsp--;
                if (state.failed) return;

                match(input, 48, FOLLOW_48_in_localVariableDeclarationStatement3292);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 87, localVariableDeclarationStatement_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "localVariableDeclarationStatement"


    // $ANTLR start "localVariableDeclaration"
    // example/Java.g:616:1: localVariableDeclaration : variableModifiers type variableDeclarators ;
    public final void localVariableDeclaration() throws RecognitionException {
        int localVariableDeclaration_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 88)) {
                return;
            }

            // example/Java.g:617:5: ( variableModifiers type variableDeclarators )
            // example/Java.g:617:9: variableModifiers type variableDeclarators
            {
                pushFollow(FOLLOW_variableModifiers_in_localVariableDeclaration3311);
                variableModifiers();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_type_in_localVariableDeclaration3313);
                type();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_variableDeclarators_in_localVariableDeclaration3315);
                variableDeclarators();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 88, localVariableDeclaration_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "localVariableDeclaration"


    // $ANTLR start "variableModifiers"
    // example/Java.g:620:1: variableModifiers : ( variableModifier )* ;
    public final void variableModifiers() throws RecognitionException {
        int variableModifiers_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 89)) {
                return;
            }

            // example/Java.g:621:5: ( ( variableModifier )* )
            // example/Java.g:621:9: ( variableModifier )*
            {
                // example/Java.g:621:9: ( variableModifier )*
                loop107:
                do {
                    int alt107 = 2;
                    switch (input.LA(1)) {
                        case 54:
                        case 74: {
                            alt107 = 1;
                        }
                        break;

                    }

                    switch (alt107) {
                        case 1:
                            // example/Java.g:621:9: variableModifier
                        {
                            pushFollow(FOLLOW_variableModifier_in_variableModifiers3338);
                            variableModifier();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop107;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 89, variableModifiers_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "variableModifiers"


    // $ANTLR start "statement"
    // example/Java.g:624:1: statement : ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement );
    public final void statement() throws RecognitionException {
        int statement_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 90)) {
                return;
            }

            // example/Java.g:625:5: ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement )
            int alt114 = 16;
            switch (input.LA(1)) {
                case 108: {
                    alt114 = 1;
                }
                break;
                case ASSERT: {
                    alt114 = 2;
                }
                break;
                case 78: {
                    alt114 = 3;
                }
                break;
                case 77: {
                    alt114 = 4;
                }
                break;
                case 107: {
                    alt114 = 5;
                }
                break;
                case 69: {
                    alt114 = 6;
                }
                break;
                case 104: {
                    alt114 = 7;
                }
                break;
                case 97: {
                    alt114 = 8;
                }
                break;
                case 98: {
                    alt114 = 9;
                }
                break;
                case 92: {
                    alt114 = 10;
                }
                break;
                case 100: {
                    alt114 = 11;
                }
                break;
                case 61: {
                    alt114 = 12;
                }
                break;
                case 67: {
                    alt114 = 13;
                }
                break;
                case 48: {
                    alt114 = 14;
                }
                break;
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case OctalLiteral:
                case StringLiteral:
                case 25:
                case 32:
                case 36:
                case 37:
                case 40:
                case 41:
                case 60:
                case 62:
                case 65:
                case 70:
                case 73:
                case 76:
                case 82:
                case 84:
                case 86:
                case 87:
                case 93:
                case 96:
                case 99:
                case 103:
                case 105:
                case 113: {
                    alt114 = 15;
                }
                break;
                case Identifier: {
                    switch (input.LA(2)) {
                        case 47: {
                            alt114 = 16;
                        }
                        break;
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 45:
                        case 46:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 55:
                        case 57:
                        case 58:
                        case 81:
                        case 109:
                        case 110:
                        case 111: {
                            alt114 = 15;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 114, 16, input);

                            throw nvae;

                    }

                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 114, 0, input);

                    throw nvae;

            }

            switch (alt114) {
                case 1:
                    // example/Java.g:625:7: block
                {
                    pushFollow(FOLLOW_block_in_statement3356);
                    block();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:626:9: ASSERT expression ( ':' expression )? ';'
                {
                    match(input, ASSERT, FOLLOW_ASSERT_in_statement3366);
                    if (state.failed) return;

                    pushFollow(FOLLOW_expression_in_statement3368);
                    expression();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:626:27: ( ':' expression )?
                    int alt108 = 2;
                    switch (input.LA(1)) {
                        case 47: {
                            alt108 = 1;
                        }
                        break;
                    }

                    switch (alt108) {
                        case 1:
                            // example/Java.g:626:28: ':' expression
                        {
                            match(input, 47, FOLLOW_47_in_statement3371);
                            if (state.failed) return;

                            pushFollow(FOLLOW_expression_in_statement3373);
                            expression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 48, FOLLOW_48_in_statement3377);
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:627:9: 'if' parExpression statement ( options {k=1; } : 'else' statement )?
                {
                    match(input, 78, FOLLOW_78_in_statement3387);
                    if (state.failed) return;

                    pushFollow(FOLLOW_parExpression_in_statement3389);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_statement_in_statement3391);
                    statement();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:627:38: ( options {k=1; } : 'else' statement )?
                    int alt109 = 2;
                    switch (input.LA(1)) {
                        case 71: {
                            int LA109_1 = input.LA(2);

                            if ((synpred157_Java())) {
                                alt109 = 1;
                            }
                        }
                        break;
                    }

                    switch (alt109) {
                        case 1:
                            // example/Java.g:627:54: 'else' statement
                        {
                            match(input, 71, FOLLOW_71_in_statement3401);
                            if (state.failed) return;

                            pushFollow(FOLLOW_statement_in_statement3403);
                            statement();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 4:
                    // example/Java.g:628:9: 'for' '(' forControl ')' statement
                {
                    match(input, 77, FOLLOW_77_in_statement3415);
                    if (state.failed) return;

                    match(input, 32, FOLLOW_32_in_statement3417);
                    if (state.failed) return;

                    pushFollow(FOLLOW_forControl_in_statement3419);
                    forControl();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 33, FOLLOW_33_in_statement3421);
                    if (state.failed) return;

                    pushFollow(FOLLOW_statement_in_statement3423);
                    statement();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:629:9: 'while' parExpression statement
                {
                    match(input, 107, FOLLOW_107_in_statement3433);
                    if (state.failed) return;

                    pushFollow(FOLLOW_parExpression_in_statement3435);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_statement_in_statement3437);
                    statement();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:630:9: 'do' statement 'while' parExpression ';'
                {
                    match(input, 69, FOLLOW_69_in_statement3447);
                    if (state.failed) return;

                    pushFollow(FOLLOW_statement_in_statement3449);
                    statement();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 107, FOLLOW_107_in_statement3451);
                    if (state.failed) return;

                    pushFollow(FOLLOW_parExpression_in_statement3453);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 48, FOLLOW_48_in_statement3455);
                    if (state.failed) return;

                }
                break;
                case 7:
                    // example/Java.g:631:9: 'try' block ( catches 'finally' block | catches | 'finally' block )
                {
                    match(input, 104, FOLLOW_104_in_statement3465);
                    if (state.failed) return;

                    pushFollow(FOLLOW_block_in_statement3467);
                    block();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:632:9: ( catches 'finally' block | catches | 'finally' block )
                    int alt110 = 3;
                    switch (input.LA(1)) {
                        case 64: {
                            int LA110_1 = input.LA(2);

                            if ((synpred162_Java())) {
                                alt110 = 1;
                            } else if ((synpred163_Java())) {
                                alt110 = 2;
                            } else {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                NoViableAltException nvae =
                                        new NoViableAltException("", 110, 1, input);

                                throw nvae;

                            }
                        }
                        break;
                        case 75: {
                            alt110 = 3;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 110, 0, input);

                            throw nvae;

                    }

                    switch (alt110) {
                        case 1:
                            // example/Java.g:632:11: catches 'finally' block
                        {
                            pushFollow(FOLLOW_catches_in_statement3479);
                            catches();

                            state._fsp--;
                            if (state.failed) return;

                            match(input, 75, FOLLOW_75_in_statement3481);
                            if (state.failed) return;

                            pushFollow(FOLLOW_block_in_statement3483);
                            block();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;
                        case 2:
                            // example/Java.g:633:11: catches
                        {
                            pushFollow(FOLLOW_catches_in_statement3495);
                            catches();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;
                        case 3:
                            // example/Java.g:634:13: 'finally' block
                        {
                            match(input, 75, FOLLOW_75_in_statement3509);
                            if (state.failed) return;

                            pushFollow(FOLLOW_block_in_statement3511);
                            block();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 8:
                    // example/Java.g:636:9: 'switch' parExpression '{' switchBlockStatementGroups '}'
                {
                    match(input, 97, FOLLOW_97_in_statement3531);
                    if (state.failed) return;

                    pushFollow(FOLLOW_parExpression_in_statement3533);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 108, FOLLOW_108_in_statement3535);
                    if (state.failed) return;

                    pushFollow(FOLLOW_switchBlockStatementGroups_in_statement3537);
                    switchBlockStatementGroups();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 112, FOLLOW_112_in_statement3539);
                    if (state.failed) return;

                }
                break;
                case 9:
                    // example/Java.g:637:9: 'synchronized' parExpression block
                {
                    match(input, 98, FOLLOW_98_in_statement3549);
                    if (state.failed) return;

                    pushFollow(FOLLOW_parExpression_in_statement3551);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_block_in_statement3553);
                    block();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 10:
                    // example/Java.g:638:9: 'return' ( expression )? ';'
                {
                    match(input, 92, FOLLOW_92_in_statement3563);
                    if (state.failed) return;

                    // example/Java.g:638:18: ( expression )?
                    int alt111 = 2;
                    switch (input.LA(1)) {
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 60:
                        case 62:
                        case 65:
                        case 70:
                        case 73:
                        case 76:
                        case 82:
                        case 84:
                        case 86:
                        case 87:
                        case 93:
                        case 96:
                        case 99:
                        case 103:
                        case 105:
                        case 113: {
                            alt111 = 1;
                        }
                        break;
                    }

                    switch (alt111) {
                        case 1:
                            // example/Java.g:638:18: expression
                        {
                            pushFollow(FOLLOW_expression_in_statement3565);
                            expression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 48, FOLLOW_48_in_statement3568);
                    if (state.failed) return;

                }
                break;
                case 11:
                    // example/Java.g:639:9: 'throw' expression ';'
                {
                    match(input, 100, FOLLOW_100_in_statement3578);
                    if (state.failed) return;

                    pushFollow(FOLLOW_expression_in_statement3580);
                    expression();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 48, FOLLOW_48_in_statement3582);
                    if (state.failed) return;

                }
                break;
                case 12:
                    // example/Java.g:640:9: 'break' ( Identifier )? ';'
                {
                    match(input, 61, FOLLOW_61_in_statement3592);
                    if (state.failed) return;

                    // example/Java.g:640:17: ( Identifier )?
                    int alt112 = 2;
                    switch (input.LA(1)) {
                        case Identifier: {
                            alt112 = 1;
                        }
                        break;
                    }

                    switch (alt112) {
                        case 1:
                            // example/Java.g:640:17: Identifier
                        {
                            match(input, Identifier, FOLLOW_Identifier_in_statement3594);
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 48, FOLLOW_48_in_statement3597);
                    if (state.failed) return;

                }
                break;
                case 13:
                    // example/Java.g:641:9: 'continue' ( Identifier )? ';'
                {
                    match(input, 67, FOLLOW_67_in_statement3607);
                    if (state.failed) return;

                    // example/Java.g:641:20: ( Identifier )?
                    int alt113 = 2;
                    switch (input.LA(1)) {
                        case Identifier: {
                            alt113 = 1;
                        }
                        break;
                    }

                    switch (alt113) {
                        case 1:
                            // example/Java.g:641:20: Identifier
                        {
                            match(input, Identifier, FOLLOW_Identifier_in_statement3609);
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 48, FOLLOW_48_in_statement3612);
                    if (state.failed) return;

                }
                break;
                case 14:
                    // example/Java.g:642:9: ';'
                {
                    match(input, 48, FOLLOW_48_in_statement3622);
                    if (state.failed) return;

                }
                break;
                case 15:
                    // example/Java.g:643:9: statementExpression ';'
                {
                    pushFollow(FOLLOW_statementExpression_in_statement3633);
                    statementExpression();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 48, FOLLOW_48_in_statement3635);
                    if (state.failed) return;

                }
                break;
                case 16:
                    // example/Java.g:644:9: Identifier ':' statement
                {
                    match(input, Identifier, FOLLOW_Identifier_in_statement3645);
                    if (state.failed) return;

                    match(input, 47, FOLLOW_47_in_statement3647);
                    if (state.failed) return;

                    pushFollow(FOLLOW_statement_in_statement3649);
                    statement();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 90, statement_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "statement"


    // $ANTLR start "catches"
    // example/Java.g:647:1: catches : catchClause ( catchClause )* ;
    public final void catches() throws RecognitionException {
        int catches_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 91)) {
                return;
            }

            // example/Java.g:648:5: ( catchClause ( catchClause )* )
            // example/Java.g:648:9: catchClause ( catchClause )*
            {
                pushFollow(FOLLOW_catchClause_in_catches3672);
                catchClause();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:648:21: ( catchClause )*
                loop115:
                do {
                    int alt115 = 2;
                    switch (input.LA(1)) {
                        case 64: {
                            alt115 = 1;
                        }
                        break;

                    }

                    switch (alt115) {
                        case 1:
                            // example/Java.g:648:22: catchClause
                        {
                            pushFollow(FOLLOW_catchClause_in_catches3675);
                            catchClause();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop115;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 91, catches_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "catches"


    // $ANTLR start "catchClause"
    // example/Java.g:651:1: catchClause : 'catch' '(' formalParameter ')' block ;
    public final void catchClause() throws RecognitionException {
        int catchClause_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 92)) {
                return;
            }

            // example/Java.g:652:5: ( 'catch' '(' formalParameter ')' block )
            // example/Java.g:652:9: 'catch' '(' formalParameter ')' block
            {
                match(input, 64, FOLLOW_64_in_catchClause3700);
                if (state.failed) return;

                match(input, 32, FOLLOW_32_in_catchClause3702);
                if (state.failed) return;

                pushFollow(FOLLOW_formalParameter_in_catchClause3704);
                formalParameter();

                state._fsp--;
                if (state.failed) return;

                match(input, 33, FOLLOW_33_in_catchClause3706);
                if (state.failed) return;

                pushFollow(FOLLOW_block_in_catchClause3708);
                block();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 92, catchClause_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "catchClause"


    // $ANTLR start "formalParameter"
    // example/Java.g:655:1: formalParameter : variableModifiers type variableDeclaratorId ;
    public final void formalParameter() throws RecognitionException {
        int formalParameter_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 93)) {
                return;
            }

            // example/Java.g:656:5: ( variableModifiers type variableDeclaratorId )
            // example/Java.g:656:9: variableModifiers type variableDeclaratorId
            {
                pushFollow(FOLLOW_variableModifiers_in_formalParameter3727);
                variableModifiers();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_type_in_formalParameter3729);
                type();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_variableDeclaratorId_in_formalParameter3731);
                variableDeclaratorId();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 93, formalParameter_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "formalParameter"


    // $ANTLR start "switchBlockStatementGroups"
    // example/Java.g:659:1: switchBlockStatementGroups : ( switchBlockStatementGroup )* ;
    public final void switchBlockStatementGroups() throws RecognitionException {
        int switchBlockStatementGroups_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 94)) {
                return;
            }

            // example/Java.g:660:5: ( ( switchBlockStatementGroup )* )
            // example/Java.g:660:9: ( switchBlockStatementGroup )*
            {
                // example/Java.g:660:9: ( switchBlockStatementGroup )*
                loop116:
                do {
                    int alt116 = 2;
                    switch (input.LA(1)) {
                        case 63:
                        case 68: {
                            alt116 = 1;
                        }
                        break;

                    }

                    switch (alt116) {
                        case 1:
                            // example/Java.g:660:10: switchBlockStatementGroup
                        {
                            pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3759);
                            switchBlockStatementGroup();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop116;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 94, switchBlockStatementGroups_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "switchBlockStatementGroups"


    // $ANTLR start "switchBlockStatementGroup"
    // example/Java.g:667:1: switchBlockStatementGroup : ( switchLabel )+ ( blockStatement )* ;
    public final void switchBlockStatementGroup() throws RecognitionException {
        int switchBlockStatementGroup_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 95)) {
                return;
            }

            // example/Java.g:668:5: ( ( switchLabel )+ ( blockStatement )* )
            // example/Java.g:668:9: ( switchLabel )+ ( blockStatement )*
            {
                // example/Java.g:668:9: ( switchLabel )+
                int cnt117 = 0;
                loop117:
                do {
                    int alt117 = 2;
                    switch (input.LA(1)) {
                        case 63: {
                            int LA117_2 = input.LA(2);

                            if ((synpred178_Java())) {
                                alt117 = 1;
                            }


                        }
                        break;
                        case 68: {
                            int LA117_3 = input.LA(2);

                            if ((synpred178_Java())) {
                                alt117 = 1;
                            }


                        }
                        break;

                    }

                    switch (alt117) {
                        case 1:
                            // example/Java.g:668:9: switchLabel
                        {
                            pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup3786);
                            switchLabel();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            if (cnt117 >= 1) break loop117;
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(117, input);
                            throw eee;
                    }
                    cnt117++;
                } while (true);


                // example/Java.g:668:22: ( blockStatement )*
                loop118:
                do {
                    int alt118 = 2;
                    switch (input.LA(1)) {
                        case ASSERT:
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case ENUM:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 48:
                        case 54:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 65:
                        case 66:
                        case 67:
                        case 69:
                        case 70:
                        case 73:
                        case 74:
                        case 76:
                        case 77:
                        case 78:
                        case 82:
                        case 83:
                        case 84:
                        case 86:
                        case 87:
                        case 89:
                        case 90:
                        case 91:
                        case 92:
                        case 93:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 100:
                        case 103:
                        case 104:
                        case 105:
                        case 107:
                        case 108:
                        case 113: {
                            alt118 = 1;
                        }
                        break;

                    }

                    switch (alt118) {
                        case 1:
                            // example/Java.g:668:22: blockStatement
                        {
                            pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup3789);
                            blockStatement();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop118;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 95, switchBlockStatementGroup_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "switchBlockStatementGroup"


    // $ANTLR start "switchLabel"
    // example/Java.g:671:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );
    public final void switchLabel() throws RecognitionException {
        int switchLabel_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 96)) {
                return;
            }

            // example/Java.g:672:5: ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' )
            int alt119 = 3;
            switch (input.LA(1)) {
                case 63: {
                    switch (input.LA(2)) {
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 60:
                        case 62:
                        case 65:
                        case 70:
                        case 73:
                        case 76:
                        case 82:
                        case 84:
                        case 86:
                        case 87:
                        case 93:
                        case 96:
                        case 99:
                        case 103:
                        case 105:
                        case 113: {
                            alt119 = 1;
                        }
                        break;
                        case Identifier: {
                            switch (input.LA(3)) {
                                case 26:
                                case 27:
                                case 28:
                                case 29:
                                case 30:
                                case 31:
                                case 32:
                                case 34:
                                case 35:
                                case 36:
                                case 37:
                                case 38:
                                case 40:
                                case 41:
                                case 42:
                                case 43:
                                case 45:
                                case 46:
                                case 49:
                                case 50:
                                case 51:
                                case 52:
                                case 53:
                                case 55:
                                case 57:
                                case 58:
                                case 81:
                                case 109:
                                case 110:
                                case 111: {
                                    alt119 = 1;
                                }
                                break;
                                case 47: {
                                    int LA119_5 = input.LA(4);

                                    if ((synpred180_Java())) {
                                        alt119 = 1;
                                    } else if ((synpred181_Java())) {
                                        alt119 = 2;
                                    } else {
                                        if (state.backtracking > 0) {
                                            state.failed = true;
                                            return;
                                        }
                                        NoViableAltException nvae =
                                                new NoViableAltException("", 119, 5, input);

                                        throw nvae;

                                    }
                                }
                                break;
                                default:
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return;
                                    }
                                    NoViableAltException nvae =
                                            new NoViableAltException("", 119, 4, input);

                                    throw nvae;

                            }

                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 119, 1, input);

                            throw nvae;

                    }

                }
                break;
                case 68: {
                    alt119 = 3;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 119, 0, input);

                    throw nvae;

            }

            switch (alt119) {
                case 1:
                    // example/Java.g:672:9: 'case' constantExpression ':'
                {
                    match(input, 63, FOLLOW_63_in_switchLabel3813);
                    if (state.failed) return;

                    pushFollow(FOLLOW_constantExpression_in_switchLabel3815);
                    constantExpression();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 47, FOLLOW_47_in_switchLabel3817);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:673:9: 'case' enumConstantName ':'
                {
                    match(input, 63, FOLLOW_63_in_switchLabel3827);
                    if (state.failed) return;

                    pushFollow(FOLLOW_enumConstantName_in_switchLabel3829);
                    enumConstantName();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 47, FOLLOW_47_in_switchLabel3831);
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:674:9: 'default' ':'
                {
                    match(input, 68, FOLLOW_68_in_switchLabel3841);
                    if (state.failed) return;

                    match(input, 47, FOLLOW_47_in_switchLabel3843);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 96, switchLabel_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "switchLabel"


    // $ANTLR start "forControl"
    // example/Java.g:677:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );
    public final void forControl() throws RecognitionException {
        int forControl_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 97)) {
                return;
            }

            // example/Java.g:679:5: ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? )
            int alt123 = 2;
            alt123 = dfa123.predict(input);
            switch (alt123) {
                case 1:
                    // example/Java.g:679:9: enhancedForControl
                {
                    pushFollow(FOLLOW_enhancedForControl_in_forControl3874);
                    enhancedForControl();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:680:9: ( forInit )? ';' ( expression )? ';' ( forUpdate )?
                {
                    // example/Java.g:680:9: ( forInit )?
                    int alt120 = 2;
                    switch (input.LA(1)) {
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 54:
                        case 60:
                        case 62:
                        case 65:
                        case 70:
                        case 73:
                        case 74:
                        case 76:
                        case 82:
                        case 84:
                        case 86:
                        case 87:
                        case 93:
                        case 96:
                        case 99:
                        case 103:
                        case 105:
                        case 113: {
                            alt120 = 1;
                        }
                        break;
                    }

                    switch (alt120) {
                        case 1:
                            // example/Java.g:680:9: forInit
                        {
                            pushFollow(FOLLOW_forInit_in_forControl3884);
                            forInit();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 48, FOLLOW_48_in_forControl3887);
                    if (state.failed) return;

                    // example/Java.g:680:22: ( expression )?
                    int alt121 = 2;
                    switch (input.LA(1)) {
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 60:
                        case 62:
                        case 65:
                        case 70:
                        case 73:
                        case 76:
                        case 82:
                        case 84:
                        case 86:
                        case 87:
                        case 93:
                        case 96:
                        case 99:
                        case 103:
                        case 105:
                        case 113: {
                            alt121 = 1;
                        }
                        break;
                    }

                    switch (alt121) {
                        case 1:
                            // example/Java.g:680:22: expression
                        {
                            pushFollow(FOLLOW_expression_in_forControl3889);
                            expression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 48, FOLLOW_48_in_forControl3892);
                    if (state.failed) return;

                    // example/Java.g:680:38: ( forUpdate )?
                    int alt122 = 2;
                    switch (input.LA(1)) {
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 60:
                        case 62:
                        case 65:
                        case 70:
                        case 73:
                        case 76:
                        case 82:
                        case 84:
                        case 86:
                        case 87:
                        case 93:
                        case 96:
                        case 99:
                        case 103:
                        case 105:
                        case 113: {
                            alt122 = 1;
                        }
                        break;
                    }

                    switch (alt122) {
                        case 1:
                            // example/Java.g:680:38: forUpdate
                        {
                            pushFollow(FOLLOW_forUpdate_in_forControl3894);
                            forUpdate();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 97, forControl_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "forControl"


    // $ANTLR start "forInit"
    // example/Java.g:683:1: forInit : ( localVariableDeclaration | expressionList );
    public final void forInit() throws RecognitionException {
        int forInit_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 98)) {
                return;
            }

            // example/Java.g:684:5: ( localVariableDeclaration | expressionList )
            int alt124 = 2;
            switch (input.LA(1)) {
                case 54:
                case 74: {
                    alt124 = 1;
                }
                break;
                case Identifier: {
                    int LA124_3 = input.LA(2);

                    if ((synpred186_Java())) {
                        alt124 = 1;
                    } else if ((true)) {
                        alt124 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 124, 3, input);

                        throw nvae;

                    }
                }
                break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    int LA124_4 = input.LA(2);

                    if ((synpred186_Java())) {
                        alt124 = 1;
                    } else if ((true)) {
                        alt124 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 124, 4, input);

                        throw nvae;

                    }
                }
                break;
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case OctalLiteral:
                case StringLiteral:
                case 25:
                case 32:
                case 36:
                case 37:
                case 40:
                case 41:
                case 73:
                case 86:
                case 87:
                case 96:
                case 99:
                case 103:
                case 105:
                case 113: {
                    alt124 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 124, 0, input);

                    throw nvae;

            }

            switch (alt124) {
                case 1:
                    // example/Java.g:684:9: localVariableDeclaration
                {
                    pushFollow(FOLLOW_localVariableDeclaration_in_forInit3914);
                    localVariableDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:685:9: expressionList
                {
                    pushFollow(FOLLOW_expressionList_in_forInit3924);
                    expressionList();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 98, forInit_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "forInit"


    // $ANTLR start "enhancedForControl"
    // example/Java.g:688:1: enhancedForControl : variableModifiers type Identifier ':' expression ;
    public final void enhancedForControl() throws RecognitionException {
        int enhancedForControl_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 99)) {
                return;
            }

            // example/Java.g:689:5: ( variableModifiers type Identifier ':' expression )
            // example/Java.g:689:9: variableModifiers type Identifier ':' expression
            {
                pushFollow(FOLLOW_variableModifiers_in_enhancedForControl3947);
                variableModifiers();

                state._fsp--;
                if (state.failed) return;

                pushFollow(FOLLOW_type_in_enhancedForControl3949);
                type();

                state._fsp--;
                if (state.failed) return;

                match(input, Identifier, FOLLOW_Identifier_in_enhancedForControl3951);
                if (state.failed) return;

                match(input, 47, FOLLOW_47_in_enhancedForControl3953);
                if (state.failed) return;

                pushFollow(FOLLOW_expression_in_enhancedForControl3955);
                expression();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 99, enhancedForControl_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "enhancedForControl"


    // $ANTLR start "forUpdate"
    // example/Java.g:692:1: forUpdate : expressionList ;
    public final void forUpdate() throws RecognitionException {
        int forUpdate_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 100)) {
                return;
            }

            // example/Java.g:693:5: ( expressionList )
            // example/Java.g:693:9: expressionList
            {
                pushFollow(FOLLOW_expressionList_in_forUpdate3974);
                expressionList();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 100, forUpdate_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "forUpdate"


    // $ANTLR start "parExpression"
    // example/Java.g:698:1: parExpression : '(' expression ')' ;
    public final void parExpression() throws RecognitionException {
        int parExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 101)) {
                return;
            }

            // example/Java.g:699:5: ( '(' expression ')' )
            // example/Java.g:699:9: '(' expression ')'
            {
                match(input, 32, FOLLOW_32_in_parExpression3995);
                if (state.failed) return;

                pushFollow(FOLLOW_expression_in_parExpression3997);
                expression();

                state._fsp--;
                if (state.failed) return;

                match(input, 33, FOLLOW_33_in_parExpression3999);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 101, parExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "parExpression"


    // $ANTLR start "expressionList"
    // example/Java.g:702:1: expressionList : expression ( ',' expression )* ;
    public final void expressionList() throws RecognitionException {
        int expressionList_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 102)) {
                return;
            }

            // example/Java.g:703:5: ( expression ( ',' expression )* )
            // example/Java.g:703:9: expression ( ',' expression )*
            {
                pushFollow(FOLLOW_expression_in_expressionList4022);
                expression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:703:20: ( ',' expression )*
                loop125:
                do {
                    int alt125 = 2;
                    switch (input.LA(1)) {
                        case 39: {
                            alt125 = 1;
                        }
                        break;

                    }

                    switch (alt125) {
                        case 1:
                            // example/Java.g:703:21: ',' expression
                        {
                            match(input, 39, FOLLOW_39_in_expressionList4025);
                            if (state.failed) return;

                            pushFollow(FOLLOW_expression_in_expressionList4027);
                            expression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop125;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 102, expressionList_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "expressionList"


    // $ANTLR start "statementExpression"
    // example/Java.g:706:1: statementExpression : expression ;
    public final void statementExpression() throws RecognitionException {
        int statementExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 103)) {
                return;
            }

            // example/Java.g:707:5: ( expression )
            // example/Java.g:707:9: expression
            {
                pushFollow(FOLLOW_expression_in_statementExpression4048);
                expression();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 103, statementExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "statementExpression"


    // $ANTLR start "constantExpression"
    // example/Java.g:710:1: constantExpression : expression ;
    public final void constantExpression() throws RecognitionException {
        int constantExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 104)) {
                return;
            }

            // example/Java.g:711:5: ( expression )
            // example/Java.g:711:9: expression
            {
                pushFollow(FOLLOW_expression_in_constantExpression4071);
                expression();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 104, constantExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "constantExpression"


    // $ANTLR start "expression"
    // example/Java.g:714:1: expression : conditionalExpression ( assignmentOperator expression )? ;
    public final void expression() throws RecognitionException {
        int expression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 105)) {
                return;
            }

            // example/Java.g:715:5: ( conditionalExpression ( assignmentOperator expression )? )
            // example/Java.g:715:9: conditionalExpression ( assignmentOperator expression )?
            {
                pushFollow(FOLLOW_conditionalExpression_in_expression4094);
                conditionalExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:715:31: ( assignmentOperator expression )?
                int alt126 = 2;
                switch (input.LA(1)) {
                    case 28:
                    case 31:
                    case 35:
                    case 38:
                    case 42:
                    case 46:
                    case 49:
                    case 50:
                    case 52:
                    case 58:
                    case 110: {
                        alt126 = 1;
                    }
                    break;
                }

                switch (alt126) {
                    case 1:
                        // example/Java.g:715:32: assignmentOperator expression
                    {
                        pushFollow(FOLLOW_assignmentOperator_in_expression4097);
                        assignmentOperator();

                        state._fsp--;
                        if (state.failed) return;

                        pushFollow(FOLLOW_expression_in_expression4099);
                        expression();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 105, expression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "expression"


    // $ANTLR start "assignmentOperator"
    // example/Java.g:718:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}?| ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}?| ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?);
    public final void assignmentOperator() throws RecognitionException {
        int assignmentOperator_StartIndex = input.index();

        Token t1 = null;
        Token t2 = null;
        Token t3 = null;
        Token t4 = null;

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 106)) {
                return;
            }

            // example/Java.g:719:5: ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}?| ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}?| ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?)
            int alt127 = 12;
            int LA127_0 = input.LA(1);

            if ((LA127_0 == 50)) {
                alt127 = 1;
            } else if ((LA127_0 == 38)) {
                alt127 = 2;
            } else if ((LA127_0 == 42)) {
                alt127 = 3;
            } else if ((LA127_0 == 35)) {
                alt127 = 4;
            } else if ((LA127_0 == 46)) {
                alt127 = 5;
            } else if ((LA127_0 == 31)) {
                alt127 = 6;
            } else if ((LA127_0 == 110)) {
                alt127 = 7;
            } else if ((LA127_0 == 58)) {
                alt127 = 8;
            } else if ((LA127_0 == 28)) {
                alt127 = 9;
            } else if ((LA127_0 == 49) && (synpred198_Java())) {
                alt127 = 10;
            } else if ((LA127_0 == 52)) {
                switch (input.LA(2)) {
                    case 52: {
                        int LA127_12 = input.LA(3);

                        if ((LA127_12 == 52) && (synpred199_Java())) {
                            alt127 = 11;
                        } else if ((LA127_12 == 50) && (synpred200_Java())) {
                            alt127 = 12;
                        } else {
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 127, 12, input);

                            throw nvae;

                        }
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 127, 11, input);

                        throw nvae;

                }

            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 127, 0, input);

                throw nvae;

            }
            switch (alt127) {
                case 1:
                    // example/Java.g:719:9: '='
                {
                    match(input, 50, FOLLOW_50_in_assignmentOperator4124);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:720:9: '+='
                {
                    match(input, 38, FOLLOW_38_in_assignmentOperator4134);
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:721:9: '-='
                {
                    match(input, 42, FOLLOW_42_in_assignmentOperator4144);
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:722:9: '*='
                {
                    match(input, 35, FOLLOW_35_in_assignmentOperator4154);
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:723:9: '/='
                {
                    match(input, 46, FOLLOW_46_in_assignmentOperator4164);
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:724:9: '&='
                {
                    match(input, 31, FOLLOW_31_in_assignmentOperator4174);
                    if (state.failed) return;

                }
                break;
                case 7:
                    // example/Java.g:725:9: '|='
                {
                    match(input, 110, FOLLOW_110_in_assignmentOperator4184);
                    if (state.failed) return;

                }
                break;
                case 8:
                    // example/Java.g:726:9: '^='
                {
                    match(input, 58, FOLLOW_58_in_assignmentOperator4194);
                    if (state.failed) return;

                }
                break;
                case 9:
                    // example/Java.g:727:9: '%='
                {
                    match(input, 28, FOLLOW_28_in_assignmentOperator4204);
                    if (state.failed) return;

                }
                break;
                case 10:
                    // example/Java.g:728:9: ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}?
                {
                    t1 = (Token) match(input, 49, FOLLOW_49_in_assignmentOperator4225);
                    if (state.failed) return;

                    t2 = (Token) match(input, 49, FOLLOW_49_in_assignmentOperator4229);
                    if (state.failed) return;

                    t3 = (Token) match(input, 50, FOLLOW_50_in_assignmentOperator4233);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                            t2.getLine() == t3.getLine() &&
                            t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() &&\n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && \n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                }
                break;
                case 11:
                    // example/Java.g:733:9: ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}?
                {
                    t1 = (Token) match(input, 52, FOLLOW_52_in_assignmentOperator4267);
                    if (state.failed) return;

                    t2 = (Token) match(input, 52, FOLLOW_52_in_assignmentOperator4271);
                    if (state.failed) return;

                    t3 = (Token) match(input, 52, FOLLOW_52_in_assignmentOperator4275);
                    if (state.failed) return;

                    t4 = (Token) match(input, 50, FOLLOW_50_in_assignmentOperator4279);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                            t2.getLine() == t3.getLine() &&
                            t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() &&
                            t3.getLine() == t4.getLine() &&
                            t3.getCharPositionInLine() + 1 == t4.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&\n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&\n          $t3.getLine() == $t4.getLine() && \n          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() ");
                    }

                }
                break;
                case 12:
                    // example/Java.g:740:9: ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?
                {
                    t1 = (Token) match(input, 52, FOLLOW_52_in_assignmentOperator4310);
                    if (state.failed) return;

                    t2 = (Token) match(input, 52, FOLLOW_52_in_assignmentOperator4314);
                    if (state.failed) return;

                    t3 = (Token) match(input, 50, FOLLOW_50_in_assignmentOperator4318);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                            t2.getLine() == t3.getLine() &&
                            t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && \n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 106, assignmentOperator_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "assignmentOperator"


    // $ANTLR start "conditionalExpression"
    // example/Java.g:747:1: conditionalExpression : conditionalOrExpression ( '?' conditionalExpression ':' conditionalExpression )? ;
    public final void conditionalExpression() throws RecognitionException {
        int conditionalExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 107)) {
                return;
            }

            // example/Java.g:748:5: ( conditionalOrExpression ( '?' conditionalExpression ':' conditionalExpression )? )
            // example/Java.g:748:9: conditionalOrExpression ( '?' conditionalExpression ':' conditionalExpression )?
            {
                pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression4347);
                conditionalOrExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:748:33: ( '?' conditionalExpression ':' conditionalExpression )?
                int alt128 = 2;
                switch (input.LA(1)) {
                    case 53: {
                        alt128 = 1;
                    }
                    break;
                }

                switch (alt128) {
                    case 1:
                        // example/Java.g:748:35: '?' conditionalExpression ':' conditionalExpression
                    {
                        match(input, 53, FOLLOW_53_in_conditionalExpression4351);
                        if (state.failed) return;

                        pushFollow(FOLLOW_conditionalExpression_in_conditionalExpression4353);
                        conditionalExpression();

                        state._fsp--;
                        if (state.failed) return;

                        match(input, 47, FOLLOW_47_in_conditionalExpression4355);
                        if (state.failed) return;

                        pushFollow(FOLLOW_conditionalExpression_in_conditionalExpression4357);
                        conditionalExpression();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 107, conditionalExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "conditionalExpression"


    // $ANTLR start "conditionalOrExpression"
    // example/Java.g:751:1: conditionalOrExpression : conditionalAndExpression ( '||' conditionalAndExpression )* ;
    public final void conditionalOrExpression() throws RecognitionException {
        int conditionalOrExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 108)) {
                return;
            }

            // example/Java.g:752:5: ( conditionalAndExpression ( '||' conditionalAndExpression )* )
            // example/Java.g:752:9: conditionalAndExpression ( '||' conditionalAndExpression )*
            {
                pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression4379);
                conditionalAndExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:752:34: ( '||' conditionalAndExpression )*
                loop129:
                do {
                    int alt129 = 2;
                    switch (input.LA(1)) {
                        case 111: {
                            alt129 = 1;
                        }
                        break;

                    }

                    switch (alt129) {
                        case 1:
                            // example/Java.g:752:36: '||' conditionalAndExpression
                        {
                            match(input, 111, FOLLOW_111_in_conditionalOrExpression4383);
                            if (state.failed) return;

                            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression4385);
                            conditionalAndExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop129;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 108, conditionalOrExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "conditionalOrExpression"


    // $ANTLR start "conditionalAndExpression"
    // example/Java.g:755:1: conditionalAndExpression : inclusiveOrExpression ( '&&' inclusiveOrExpression )* ;
    public final void conditionalAndExpression() throws RecognitionException {
        int conditionalAndExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 109)) {
                return;
            }

            // example/Java.g:756:5: ( inclusiveOrExpression ( '&&' inclusiveOrExpression )* )
            // example/Java.g:756:9: inclusiveOrExpression ( '&&' inclusiveOrExpression )*
            {
                pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4407);
                inclusiveOrExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:756:31: ( '&&' inclusiveOrExpression )*
                loop130:
                do {
                    int alt130 = 2;
                    switch (input.LA(1)) {
                        case 29: {
                            alt130 = 1;
                        }
                        break;

                    }

                    switch (alt130) {
                        case 1:
                            // example/Java.g:756:33: '&&' inclusiveOrExpression
                        {
                            match(input, 29, FOLLOW_29_in_conditionalAndExpression4411);
                            if (state.failed) return;

                            pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4413);
                            inclusiveOrExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop130;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 109, conditionalAndExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "conditionalAndExpression"


    // $ANTLR start "inclusiveOrExpression"
    // example/Java.g:759:1: inclusiveOrExpression : exclusiveOrExpression ( '|' exclusiveOrExpression )* ;
    public final void inclusiveOrExpression() throws RecognitionException {
        int inclusiveOrExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 110)) {
                return;
            }

            // example/Java.g:760:5: ( exclusiveOrExpression ( '|' exclusiveOrExpression )* )
            // example/Java.g:760:9: exclusiveOrExpression ( '|' exclusiveOrExpression )*
            {
                pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4435);
                exclusiveOrExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:760:31: ( '|' exclusiveOrExpression )*
                loop131:
                do {
                    int alt131 = 2;
                    switch (input.LA(1)) {
                        case 109: {
                            alt131 = 1;
                        }
                        break;

                    }

                    switch (alt131) {
                        case 1:
                            // example/Java.g:760:33: '|' exclusiveOrExpression
                        {
                            match(input, 109, FOLLOW_109_in_inclusiveOrExpression4439);
                            if (state.failed) return;

                            pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4441);
                            exclusiveOrExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop131;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 110, inclusiveOrExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "inclusiveOrExpression"


    // $ANTLR start "exclusiveOrExpression"
    // example/Java.g:763:1: exclusiveOrExpression : andExpression ( '^' andExpression )* ;
    public final void exclusiveOrExpression() throws RecognitionException {
        int exclusiveOrExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 111)) {
                return;
            }

            // example/Java.g:764:5: ( andExpression ( '^' andExpression )* )
            // example/Java.g:764:9: andExpression ( '^' andExpression )*
            {
                pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression4463);
                andExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:764:23: ( '^' andExpression )*
                loop132:
                do {
                    int alt132 = 2;
                    switch (input.LA(1)) {
                        case 57: {
                            alt132 = 1;
                        }
                        break;

                    }

                    switch (alt132) {
                        case 1:
                            // example/Java.g:764:25: '^' andExpression
                        {
                            match(input, 57, FOLLOW_57_in_exclusiveOrExpression4467);
                            if (state.failed) return;

                            pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression4469);
                            andExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop132;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 111, exclusiveOrExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "exclusiveOrExpression"


    // $ANTLR start "andExpression"
    // example/Java.g:767:1: andExpression : equalityExpression ( '&' equalityExpression )* ;
    public final void andExpression() throws RecognitionException {
        int andExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 112)) {
                return;
            }

            // example/Java.g:768:5: ( equalityExpression ( '&' equalityExpression )* )
            // example/Java.g:768:9: equalityExpression ( '&' equalityExpression )*
            {
                pushFollow(FOLLOW_equalityExpression_in_andExpression4491);
                equalityExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:768:28: ( '&' equalityExpression )*
                loop133:
                do {
                    int alt133 = 2;
                    switch (input.LA(1)) {
                        case 30: {
                            alt133 = 1;
                        }
                        break;

                    }

                    switch (alt133) {
                        case 1:
                            // example/Java.g:768:30: '&' equalityExpression
                        {
                            match(input, 30, FOLLOW_30_in_andExpression4495);
                            if (state.failed) return;

                            pushFollow(FOLLOW_equalityExpression_in_andExpression4497);
                            equalityExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop133;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 112, andExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "andExpression"


    // $ANTLR start "equalityExpression"
    // example/Java.g:771:1: equalityExpression : instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* ;
    public final void equalityExpression() throws RecognitionException {
        int equalityExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 113)) {
                return;
            }

            // example/Java.g:772:5: ( instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* )
            // example/Java.g:772:9: instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )*
            {
                pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression4519);
                instanceOfExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:772:30: ( ( '==' | '!=' ) instanceOfExpression )*
                loop134:
                do {
                    int alt134 = 2;
                    switch (input.LA(1)) {
                        case 26:
                        case 51: {
                            alt134 = 1;
                        }
                        break;

                    }

                    switch (alt134) {
                        case 1:
                            // example/Java.g:772:32: ( '==' | '!=' ) instanceOfExpression
                        {
                            if (input.LA(1) == 26 || input.LA(1) == 51) {
                                input.consume();
                                state.errorRecovery = false;
                                state.failed = false;
                            } else {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                throw mse;
                            }


                            pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression4531);
                            instanceOfExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop134;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 113, equalityExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "equalityExpression"


    // $ANTLR start "instanceOfExpression"
    // example/Java.g:775:1: instanceOfExpression : relationalExpression ( 'instanceof' type )? ;
    public final void instanceOfExpression() throws RecognitionException {
        int instanceOfExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 114)) {
                return;
            }

            // example/Java.g:776:5: ( relationalExpression ( 'instanceof' type )? )
            // example/Java.g:776:9: relationalExpression ( 'instanceof' type )?
            {
                pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4553);
                relationalExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:776:30: ( 'instanceof' type )?
                int alt135 = 2;
                switch (input.LA(1)) {
                    case 81: {
                        alt135 = 1;
                    }
                    break;
                }

                switch (alt135) {
                    case 1:
                        // example/Java.g:776:31: 'instanceof' type
                    {
                        match(input, 81, FOLLOW_81_in_instanceOfExpression4556);
                        if (state.failed) return;

                        pushFollow(FOLLOW_type_in_instanceOfExpression4558);
                        type();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 114, instanceOfExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "instanceOfExpression"


    // $ANTLR start "relationalExpression"
    // example/Java.g:779:1: relationalExpression : shiftExpression ( relationalOp shiftExpression )* ;
    public final void relationalExpression() throws RecognitionException {
        int relationalExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 115)) {
                return;
            }

            // example/Java.g:780:5: ( shiftExpression ( relationalOp shiftExpression )* )
            // example/Java.g:780:9: shiftExpression ( relationalOp shiftExpression )*
            {
                pushFollow(FOLLOW_shiftExpression_in_relationalExpression4579);
                shiftExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:780:25: ( relationalOp shiftExpression )*
                loop136:
                do {
                    int alt136 = 2;
                    switch (input.LA(1)) {
                        case 49: {
                            switch (input.LA(2)) {
                                case CharacterLiteral:
                                case DecimalLiteral:
                                case FloatingPointLiteral:
                                case HexLiteral:
                                case Identifier:
                                case OctalLiteral:
                                case StringLiteral:
                                case 25:
                                case 32:
                                case 36:
                                case 37:
                                case 40:
                                case 41:
                                case 50:
                                case 60:
                                case 62:
                                case 65:
                                case 70:
                                case 73:
                                case 76:
                                case 82:
                                case 84:
                                case 86:
                                case 87:
                                case 93:
                                case 96:
                                case 99:
                                case 103:
                                case 105:
                                case 113: {
                                    alt136 = 1;
                                }
                                break;

                            }

                        }
                        break;
                        case 52: {
                            switch (input.LA(2)) {
                                case CharacterLiteral:
                                case DecimalLiteral:
                                case FloatingPointLiteral:
                                case HexLiteral:
                                case Identifier:
                                case OctalLiteral:
                                case StringLiteral:
                                case 25:
                                case 32:
                                case 36:
                                case 37:
                                case 40:
                                case 41:
                                case 50:
                                case 60:
                                case 62:
                                case 65:
                                case 70:
                                case 73:
                                case 76:
                                case 82:
                                case 84:
                                case 86:
                                case 87:
                                case 93:
                                case 96:
                                case 99:
                                case 103:
                                case 105:
                                case 113: {
                                    alt136 = 1;
                                }
                                break;

                            }

                        }
                        break;

                    }

                    switch (alt136) {
                        case 1:
                            // example/Java.g:780:27: relationalOp shiftExpression
                        {
                            pushFollow(FOLLOW_relationalOp_in_relationalExpression4583);
                            relationalOp();

                            state._fsp--;
                            if (state.failed) return;

                            pushFollow(FOLLOW_shiftExpression_in_relationalExpression4585);
                            shiftExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop136;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 115, relationalExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "relationalExpression"


    // $ANTLR start "relationalOp"
    // example/Java.g:783:1: relationalOp : ( ( '<' '=' )=>t1= '<' t2= '=' {...}?| ( '>' '=' )=>t1= '>' t2= '=' {...}?| '<' | '>' );
    public final void relationalOp() throws RecognitionException {
        int relationalOp_StartIndex = input.index();

        Token t1 = null;
        Token t2 = null;

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 116)) {
                return;
            }

            // example/Java.g:784:5: ( ( '<' '=' )=>t1= '<' t2= '=' {...}?| ( '>' '=' )=>t1= '>' t2= '=' {...}?| '<' | '>' )
            int alt137 = 4;
            switch (input.LA(1)) {
                case 49: {
                    int LA137_1 = input.LA(2);

                    if ((LA137_1 == 50) && (synpred211_Java())) {
                        alt137 = 1;
                    } else if (((LA137_1 >= CharacterLiteral && LA137_1 <= DecimalLiteral) || LA137_1 == FloatingPointLiteral || (LA137_1 >= HexLiteral && LA137_1 <= Identifier) || (LA137_1 >= OctalLiteral && LA137_1 <= StringLiteral) || LA137_1 == 25 || LA137_1 == 32 || (LA137_1 >= 36 && LA137_1 <= 37) || (LA137_1 >= 40 && LA137_1 <= 41) || LA137_1 == 60 || LA137_1 == 62 || LA137_1 == 65 || LA137_1 == 70 || LA137_1 == 73 || LA137_1 == 76 || LA137_1 == 82 || LA137_1 == 84 || (LA137_1 >= 86 && LA137_1 <= 87) || LA137_1 == 93 || LA137_1 == 96 || LA137_1 == 99 || LA137_1 == 103 || LA137_1 == 105 || LA137_1 == 113)) {
                        alt137 = 3;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 137, 1, input);

                        throw nvae;

                    }
                }
                break;
                case 52: {
                    int LA137_2 = input.LA(2);

                    if ((LA137_2 == 50) && (synpred212_Java())) {
                        alt137 = 2;
                    } else if (((LA137_2 >= CharacterLiteral && LA137_2 <= DecimalLiteral) || LA137_2 == FloatingPointLiteral || (LA137_2 >= HexLiteral && LA137_2 <= Identifier) || (LA137_2 >= OctalLiteral && LA137_2 <= StringLiteral) || LA137_2 == 25 || LA137_2 == 32 || (LA137_2 >= 36 && LA137_2 <= 37) || (LA137_2 >= 40 && LA137_2 <= 41) || LA137_2 == 60 || LA137_2 == 62 || LA137_2 == 65 || LA137_2 == 70 || LA137_2 == 73 || LA137_2 == 76 || LA137_2 == 82 || LA137_2 == 84 || (LA137_2 >= 86 && LA137_2 <= 87) || LA137_2 == 93 || LA137_2 == 96 || LA137_2 == 99 || LA137_2 == 103 || LA137_2 == 105 || LA137_2 == 113)) {
                        alt137 = 4;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 137, 2, input);

                        throw nvae;

                    }
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 137, 0, input);

                    throw nvae;

            }

            switch (alt137) {
                case 1:
                    // example/Java.g:784:9: ( '<' '=' )=>t1= '<' t2= '=' {...}?
                {
                    t1 = (Token) match(input, 49, FOLLOW_49_in_relationalOp4620);
                    if (state.failed) return;

                    t2 = (Token) match(input, 50, FOLLOW_50_in_relationalOp4624);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "relationalOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                }
                break;
                case 2:
                    // example/Java.g:787:9: ( '>' '=' )=>t1= '>' t2= '=' {...}?
                {
                    t1 = (Token) match(input, 52, FOLLOW_52_in_relationalOp4654);
                    if (state.failed) return;

                    t2 = (Token) match(input, 50, FOLLOW_50_in_relationalOp4658);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "relationalOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                }
                break;
                case 3:
                    // example/Java.g:790:9: '<'
                {
                    match(input, 49, FOLLOW_49_in_relationalOp4679);
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:791:9: '>'
                {
                    match(input, 52, FOLLOW_52_in_relationalOp4690);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 116, relationalOp_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "relationalOp"


    // $ANTLR start "shiftExpression"
    // example/Java.g:794:1: shiftExpression : additiveExpression ( shiftOp additiveExpression )* ;
    public final void shiftExpression() throws RecognitionException {
        int shiftExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 117)) {
                return;
            }

            // example/Java.g:795:5: ( additiveExpression ( shiftOp additiveExpression )* )
            // example/Java.g:795:9: additiveExpression ( shiftOp additiveExpression )*
            {
                pushFollow(FOLLOW_additiveExpression_in_shiftExpression4710);
                additiveExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:795:28: ( shiftOp additiveExpression )*
                loop138:
                do {
                    int alt138 = 2;
                    switch (input.LA(1)) {
                        case 49: {
                            switch (input.LA(2)) {
                                case 49: {
                                    switch (input.LA(3)) {
                                        case CharacterLiteral:
                                        case DecimalLiteral:
                                        case FloatingPointLiteral:
                                        case HexLiteral:
                                        case Identifier:
                                        case OctalLiteral:
                                        case StringLiteral:
                                        case 25:
                                        case 32:
                                        case 36:
                                        case 37:
                                        case 40:
                                        case 41:
                                        case 60:
                                        case 62:
                                        case 65:
                                        case 70:
                                        case 73:
                                        case 76:
                                        case 82:
                                        case 84:
                                        case 86:
                                        case 87:
                                        case 93:
                                        case 96:
                                        case 99:
                                        case 103:
                                        case 105:
                                        case 113: {
                                            alt138 = 1;
                                        }
                                        break;

                                    }

                                }
                                break;

                            }

                        }
                        break;
                        case 52: {
                            switch (input.LA(2)) {
                                case 52: {
                                    switch (input.LA(3)) {
                                        case 52: {
                                            switch (input.LA(4)) {
                                                case CharacterLiteral:
                                                case DecimalLiteral:
                                                case FloatingPointLiteral:
                                                case HexLiteral:
                                                case Identifier:
                                                case OctalLiteral:
                                                case StringLiteral:
                                                case 25:
                                                case 32:
                                                case 36:
                                                case 37:
                                                case 40:
                                                case 41:
                                                case 60:
                                                case 62:
                                                case 65:
                                                case 70:
                                                case 73:
                                                case 76:
                                                case 82:
                                                case 84:
                                                case 86:
                                                case 87:
                                                case 93:
                                                case 96:
                                                case 99:
                                                case 103:
                                                case 105:
                                                case 113: {
                                                    alt138 = 1;
                                                }
                                                break;

                                            }

                                        }
                                        break;
                                        case CharacterLiteral:
                                        case DecimalLiteral:
                                        case FloatingPointLiteral:
                                        case HexLiteral:
                                        case Identifier:
                                        case OctalLiteral:
                                        case StringLiteral:
                                        case 25:
                                        case 32:
                                        case 36:
                                        case 37:
                                        case 40:
                                        case 41:
                                        case 60:
                                        case 62:
                                        case 65:
                                        case 70:
                                        case 73:
                                        case 76:
                                        case 82:
                                        case 84:
                                        case 86:
                                        case 87:
                                        case 93:
                                        case 96:
                                        case 99:
                                        case 103:
                                        case 105:
                                        case 113: {
                                            alt138 = 1;
                                        }
                                        break;

                                    }

                                }
                                break;

                            }

                        }
                        break;

                    }

                    switch (alt138) {
                        case 1:
                            // example/Java.g:795:30: shiftOp additiveExpression
                        {
                            pushFollow(FOLLOW_shiftOp_in_shiftExpression4714);
                            shiftOp();

                            state._fsp--;
                            if (state.failed) return;

                            pushFollow(FOLLOW_additiveExpression_in_shiftExpression4716);
                            additiveExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop138;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 117, shiftExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "shiftExpression"


    // $ANTLR start "shiftOp"
    // example/Java.g:798:1: shiftOp : ( ( '<' '<' )=>t1= '<' t2= '<' {...}?| ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}?| ( '>' '>' )=>t1= '>' t2= '>' {...}?);
    public final void shiftOp() throws RecognitionException {
        int shiftOp_StartIndex = input.index();

        Token t1 = null;
        Token t2 = null;
        Token t3 = null;

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 118)) {
                return;
            }

            // example/Java.g:799:5: ( ( '<' '<' )=>t1= '<' t2= '<' {...}?| ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}?| ( '>' '>' )=>t1= '>' t2= '>' {...}?)
            int alt139 = 3;
            int LA139_0 = input.LA(1);

            if ((LA139_0 == 49) && (synpred215_Java())) {
                alt139 = 1;
            } else if ((LA139_0 == 52)) {
                switch (input.LA(2)) {
                    case 52: {
                        int LA139_3 = input.LA(3);

                        if ((LA139_3 == 52) && (synpred216_Java())) {
                            alt139 = 2;
                        } else if ((LA139_3 == 36) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 40) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 37) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 41) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 113) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 25) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 32) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 99) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 96) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == DecimalLiteral || LA139_3 == HexLiteral || LA139_3 == OctalLiteral) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == FloatingPointLiteral) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == CharacterLiteral) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == StringLiteral) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 73 || LA139_3 == 103) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 87) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 86) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == Identifier) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 60 || LA139_3 == 62 || LA139_3 == 65 || LA139_3 == 70 || LA139_3 == 76 || LA139_3 == 82 || LA139_3 == 84 || LA139_3 == 93) && (synpred217_Java())) {
                            alt139 = 3;
                        } else if ((LA139_3 == 105) && (synpred217_Java())) {
                            alt139 = 3;
                        } else {
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 139, 3, input);

                            throw nvae;

                        }
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 139, 2, input);

                        throw nvae;

                }

            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 139, 0, input);

                throw nvae;

            }
            switch (alt139) {
                case 1:
                    // example/Java.g:799:9: ( '<' '<' )=>t1= '<' t2= '<' {...}?
                {
                    t1 = (Token) match(input, 49, FOLLOW_49_in_shiftOp4747);
                    if (state.failed) return;

                    t2 = (Token) match(input, 49, FOLLOW_49_in_shiftOp4751);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                }
                break;
                case 2:
                    // example/Java.g:802:9: ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}?
                {
                    t1 = (Token) match(input, 52, FOLLOW_52_in_shiftOp4783);
                    if (state.failed) return;

                    t2 = (Token) match(input, 52, FOLLOW_52_in_shiftOp4787);
                    if (state.failed) return;

                    t3 = (Token) match(input, 52, FOLLOW_52_in_shiftOp4791);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                            t2.getLine() == t3.getLine() &&
                            t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&\n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                }
                break;
                case 3:
                    // example/Java.g:807:9: ( '>' '>' )=>t1= '>' t2= '>' {...}?
                {
                    t1 = (Token) match(input, 52, FOLLOW_52_in_shiftOp4821);
                    if (state.failed) return;

                    t2 = (Token) match(input, 52, FOLLOW_52_in_shiftOp4825);
                    if (state.failed) return;

                    if (!((t1.getLine() == t2.getLine() &&
                            t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine()))) {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 118, shiftOp_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "shiftOp"


    // $ANTLR start "additiveExpression"
    // example/Java.g:813:1: additiveExpression : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* ;
    public final void additiveExpression() throws RecognitionException {
        int additiveExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 119)) {
                return;
            }

            // example/Java.g:814:5: ( multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* )
            // example/Java.g:814:9: multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
            {
                pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4855);
                multiplicativeExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:814:34: ( ( '+' | '-' ) multiplicativeExpression )*
                loop140:
                do {
                    int alt140 = 2;
                    switch (input.LA(1)) {
                        case 36:
                        case 40: {
                            alt140 = 1;
                        }
                        break;

                    }

                    switch (alt140) {
                        case 1:
                            // example/Java.g:814:36: ( '+' | '-' ) multiplicativeExpression
                        {
                            if (input.LA(1) == 36 || input.LA(1) == 40) {
                                input.consume();
                                state.errorRecovery = false;
                                state.failed = false;
                            } else {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                throw mse;
                            }


                            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4867);
                            multiplicativeExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop140;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 119, additiveExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "additiveExpression"


    // $ANTLR start "multiplicativeExpression"
    // example/Java.g:817:1: multiplicativeExpression : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* ;
    public final void multiplicativeExpression() throws RecognitionException {
        int multiplicativeExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 120)) {
                return;
            }

            // example/Java.g:818:5: ( unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* )
            // example/Java.g:818:9: unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
            {
                pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4889);
                unaryExpression();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:818:25: ( ( '*' | '/' | '%' ) unaryExpression )*
                loop141:
                do {
                    int alt141 = 2;
                    switch (input.LA(1)) {
                        case 27:
                        case 34:
                        case 45: {
                            alt141 = 1;
                        }
                        break;

                    }

                    switch (alt141) {
                        case 1:
                            // example/Java.g:818:27: ( '*' | '/' | '%' ) unaryExpression
                        {
                            if (input.LA(1) == 27 || input.LA(1) == 34 || input.LA(1) == 45) {
                                input.consume();
                                state.errorRecovery = false;
                                state.failed = false;
                            } else {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                throw mse;
                            }


                            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4907);
                            unaryExpression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                        default:
                            break loop141;
                    }
                } while (true);


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 120, multiplicativeExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "multiplicativeExpression"


    // $ANTLR start "unaryExpression"
    // example/Java.g:821:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus );
    public final void unaryExpression() throws RecognitionException {
        int unaryExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 121)) {
                return;
            }

            // example/Java.g:822:5: ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus )
            int alt142 = 5;
            switch (input.LA(1)) {
                case 36: {
                    alt142 = 1;
                }
                break;
                case 40: {
                    alt142 = 2;
                }
                break;
                case 37: {
                    alt142 = 3;
                }
                break;
                case 41: {
                    alt142 = 4;
                }
                break;
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case Identifier:
                case OctalLiteral:
                case StringLiteral:
                case 25:
                case 32:
                case 60:
                case 62:
                case 65:
                case 70:
                case 73:
                case 76:
                case 82:
                case 84:
                case 86:
                case 87:
                case 93:
                case 96:
                case 99:
                case 103:
                case 105:
                case 113: {
                    alt142 = 5;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 142, 0, input);

                    throw nvae;

            }

            switch (alt142) {
                case 1:
                    // example/Java.g:822:9: '+' unaryExpression
                {
                    match(input, 36, FOLLOW_36_in_unaryExpression4933);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression4935);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:823:9: '-' unaryExpression
                {
                    match(input, 40, FOLLOW_40_in_unaryExpression4945);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression4947);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:824:9: '++' unaryExpression
                {
                    match(input, 37, FOLLOW_37_in_unaryExpression4957);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression4959);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:825:9: '--' unaryExpression
                {
                    match(input, 41, FOLLOW_41_in_unaryExpression4969);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression4971);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:826:9: unaryExpressionNotPlusMinus
                {
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression4981);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 121, unaryExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "unaryExpression"


    // $ANTLR start "unaryExpressionNotPlusMinus"
    // example/Java.g:829:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );
    public final void unaryExpressionNotPlusMinus() throws RecognitionException {
        int unaryExpressionNotPlusMinus_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 122)) {
                return;
            }

            // example/Java.g:830:5: ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? )
            int alt145 = 4;
            switch (input.LA(1)) {
                case 113: {
                    alt145 = 1;
                }
                break;
                case 25: {
                    alt145 = 2;
                }
                break;
                case 32: {
                    int LA145_3 = input.LA(2);

                    if ((synpred229_Java())) {
                        alt145 = 3;
                    } else if ((true)) {
                        alt145 = 4;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 145, 3, input);

                        throw nvae;

                    }
                }
                break;
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case Identifier:
                case OctalLiteral:
                case StringLiteral:
                case 60:
                case 62:
                case 65:
                case 70:
                case 73:
                case 76:
                case 82:
                case 84:
                case 86:
                case 87:
                case 93:
                case 96:
                case 99:
                case 103:
                case 105: {
                    alt145 = 4;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 145, 0, input);

                    throw nvae;

            }

            switch (alt145) {
                case 1:
                    // example/Java.g:830:9: '~' unaryExpression
                {
                    match(input, 113, FOLLOW_113_in_unaryExpressionNotPlusMinus5000);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5002);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:831:9: '!' unaryExpression
                {
                    match(input, 25, FOLLOW_25_in_unaryExpressionNotPlusMinus5012);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5014);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:832:9: castExpression
                {
                    pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus5024);
                    castExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:833:9: primary ( selector )* ( '++' | '--' )?
                {
                    pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus5034);
                    primary();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:833:17: ( selector )*
                    loop143:
                    do {
                        int alt143 = 2;
                        switch (input.LA(1)) {
                            case 43:
                            case 55: {
                                alt143 = 1;
                            }
                            break;

                        }

                        switch (alt143) {
                            case 1:
                                // example/Java.g:833:17: selector
                            {
                                pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus5036);
                                selector();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop143;
                        }
                    } while (true);


                    // example/Java.g:833:27: ( '++' | '--' )?
                    int alt144 = 2;
                    switch (input.LA(1)) {
                        case 37:
                        case 41: {
                            alt144 = 1;
                        }
                        break;
                    }

                    switch (alt144) {
                        case 1:
                            // example/Java.g:
                        {
                            if (input.LA(1) == 37 || input.LA(1) == 41) {
                                input.consume();
                                state.errorRecovery = false;
                                state.failed = false;
                            } else {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                throw mse;
                            }


                        }
                        break;

                    }


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 122, unaryExpressionNotPlusMinus_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "unaryExpressionNotPlusMinus"


    // $ANTLR start "castExpression"
    // example/Java.g:836:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );
    public final void castExpression() throws RecognitionException {
        int castExpression_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 123)) {
                return;
            }

            // example/Java.g:837:5: ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus )
            int alt147 = 2;
            switch (input.LA(1)) {
                case 32: {
                    int LA147_1 = input.LA(2);

                    if ((synpred233_Java())) {
                        alt147 = 1;
                    } else if ((true)) {
                        alt147 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 147, 1, input);

                        throw nvae;

                    }
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 147, 0, input);

                    throw nvae;

            }

            switch (alt147) {
                case 1:
                    // example/Java.g:837:8: '(' primitiveType ')' unaryExpression
                {
                    match(input, 32, FOLLOW_32_in_castExpression5062);
                    if (state.failed) return;

                    pushFollow(FOLLOW_primitiveType_in_castExpression5064);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 33, FOLLOW_33_in_castExpression5066);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpression_in_castExpression5068);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:838:8: '(' ( type | expression ) ')' unaryExpressionNotPlusMinus
                {
                    match(input, 32, FOLLOW_32_in_castExpression5077);
                    if (state.failed) return;

                    // example/Java.g:838:12: ( type | expression )
                    int alt146 = 2;
                    alt146 = dfa146.predict(input);
                    switch (alt146) {
                        case 1:
                            // example/Java.g:838:13: type
                        {
                            pushFollow(FOLLOW_type_in_castExpression5080);
                            type();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;
                        case 2:
                            // example/Java.g:838:20: expression
                        {
                            pushFollow(FOLLOW_expression_in_castExpression5084);
                            expression();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                    match(input, 33, FOLLOW_33_in_castExpression5087);
                    if (state.failed) return;

                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression5089);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 123, castExpression_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "castExpression"


    // $ANTLR start "primary"
    // example/Java.g:841:1: primary : ( parExpression | 'this' ( '.' Identifier )* ( identifierSuffix )? | 'super' superSuffix | literal | 'new' creator | Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );
    public final void primary() throws RecognitionException {
        int primary_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 124)) {
                return;
            }

            // example/Java.g:842:5: ( parExpression | 'this' ( '.' Identifier )* ( identifierSuffix )? | 'super' superSuffix | literal | 'new' creator | Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' )
            int alt153 = 8;
            switch (input.LA(1)) {
                case 32: {
                    alt153 = 1;
                }
                break;
                case 99: {
                    alt153 = 2;
                }
                break;
                case 96: {
                    alt153 = 3;
                }
                break;
                case CharacterLiteral:
                case DecimalLiteral:
                case FloatingPointLiteral:
                case HexLiteral:
                case OctalLiteral:
                case StringLiteral:
                case 73:
                case 87:
                case 103: {
                    alt153 = 4;
                }
                break;
                case 86: {
                    alt153 = 5;
                }
                break;
                case Identifier: {
                    alt153 = 6;
                }
                break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt153 = 7;
                }
                break;
                case 105: {
                    alt153 = 8;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 153, 0, input);

                    throw nvae;

            }

            switch (alt153) {
                case 1:
                    // example/Java.g:842:9: parExpression
                {
                    pushFollow(FOLLOW_parExpression_in_primary5108);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:843:9: 'this' ( '.' Identifier )* ( identifierSuffix )?
                {
                    match(input, 99, FOLLOW_99_in_primary5118);
                    if (state.failed) return;

                    // example/Java.g:843:16: ( '.' Identifier )*
                    loop148:
                    do {
                        int alt148 = 2;
                        switch (input.LA(1)) {
                            case 43: {
                                switch (input.LA(2)) {
                                    case Identifier: {
                                        int LA148_3 = input.LA(3);

                                        if ((synpred236_Java())) {
                                            alt148 = 1;
                                        }


                                    }
                                    break;

                                }

                            }
                            break;

                        }

                        switch (alt148) {
                            case 1:
                                // example/Java.g:843:17: '.' Identifier
                            {
                                match(input, 43, FOLLOW_43_in_primary5121);
                                if (state.failed) return;

                                match(input, Identifier, FOLLOW_Identifier_in_primary5123);
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop148;
                        }
                    } while (true);


                    // example/Java.g:843:34: ( identifierSuffix )?
                    int alt149 = 2;
                    switch (input.LA(1)) {
                        case 55: {
                            int LA149_1 = input.LA(2);

                            if ((synpred237_Java())) {
                                alt149 = 1;
                            }
                        }
                        break;
                        case 32: {
                            alt149 = 1;
                        }
                        break;
                        case 43: {
                            int LA149_3 = input.LA(2);

                            if ((synpred237_Java())) {
                                alt149 = 1;
                            }
                        }
                        break;
                    }

                    switch (alt149) {
                        case 1:
                            // example/Java.g:843:34: identifierSuffix
                        {
                            pushFollow(FOLLOW_identifierSuffix_in_primary5127);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 3:
                    // example/Java.g:844:9: 'super' superSuffix
                {
                    match(input, 96, FOLLOW_96_in_primary5138);
                    if (state.failed) return;

                    pushFollow(FOLLOW_superSuffix_in_primary5140);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:845:9: literal
                {
                    pushFollow(FOLLOW_literal_in_primary5150);
                    literal();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:846:9: 'new' creator
                {
                    match(input, 86, FOLLOW_86_in_primary5160);
                    if (state.failed) return;

                    pushFollow(FOLLOW_creator_in_primary5162);
                    creator();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:847:9: Identifier ( '.' Identifier )* ( identifierSuffix )?
                {
                    match(input, Identifier, FOLLOW_Identifier_in_primary5172);
                    if (state.failed) return;

                    // example/Java.g:847:20: ( '.' Identifier )*
                    loop150:
                    do {
                        int alt150 = 2;
                        switch (input.LA(1)) {
                            case 43: {
                                switch (input.LA(2)) {
                                    case Identifier: {
                                        int LA150_3 = input.LA(3);

                                        if ((synpred242_Java())) {
                                            alt150 = 1;
                                        }


                                    }
                                    break;

                                }

                            }
                            break;

                        }

                        switch (alt150) {
                            case 1:
                                // example/Java.g:847:21: '.' Identifier
                            {
                                match(input, 43, FOLLOW_43_in_primary5175);
                                if (state.failed) return;

                                match(input, Identifier, FOLLOW_Identifier_in_primary5177);
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop150;
                        }
                    } while (true);


                    // example/Java.g:847:38: ( identifierSuffix )?
                    int alt151 = 2;
                    switch (input.LA(1)) {
                        case 55: {
                            int LA151_1 = input.LA(2);

                            if ((synpred243_Java())) {
                                alt151 = 1;
                            }
                        }
                        break;
                        case 32: {
                            alt151 = 1;
                        }
                        break;
                        case 43: {
                            int LA151_3 = input.LA(2);

                            if ((synpred243_Java())) {
                                alt151 = 1;
                            }
                        }
                        break;
                    }

                    switch (alt151) {
                        case 1:
                            // example/Java.g:847:38: identifierSuffix
                        {
                            pushFollow(FOLLOW_identifierSuffix_in_primary5181);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 7:
                    // example/Java.g:848:9: primitiveType ( '[' ']' )* '.' 'class'
                {
                    pushFollow(FOLLOW_primitiveType_in_primary5192);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:848:23: ( '[' ']' )*
                    loop152:
                    do {
                        int alt152 = 2;
                        switch (input.LA(1)) {
                            case 55: {
                                alt152 = 1;
                            }
                            break;

                        }

                        switch (alt152) {
                            case 1:
                                // example/Java.g:848:24: '[' ']'
                            {
                                match(input, 55, FOLLOW_55_in_primary5195);
                                if (state.failed) return;

                                match(input, 56, FOLLOW_56_in_primary5197);
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop152;
                        }
                    } while (true);


                    match(input, 43, FOLLOW_43_in_primary5201);
                    if (state.failed) return;

                    match(input, 66, FOLLOW_66_in_primary5203);
                    if (state.failed) return;

                }
                break;
                case 8:
                    // example/Java.g:849:9: 'void' '.' 'class'
                {
                    match(input, 105, FOLLOW_105_in_primary5213);
                    if (state.failed) return;

                    match(input, 43, FOLLOW_43_in_primary5215);
                    if (state.failed) return;

                    match(input, 66, FOLLOW_66_in_primary5217);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 124, primary_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "primary"


    // $ANTLR start "identifierSuffix"
    // example/Java.g:852:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator );
    public final void identifierSuffix() throws RecognitionException {
        int identifierSuffix_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 125)) {
                return;
            }

            // example/Java.g:853:5: ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator )
            int alt156 = 8;
            switch (input.LA(1)) {
                case 55: {
                    switch (input.LA(2)) {
                        case 56: {
                            alt156 = 1;
                        }
                        break;
                        case CharacterLiteral:
                        case DecimalLiteral:
                        case FloatingPointLiteral:
                        case HexLiteral:
                        case Identifier:
                        case OctalLiteral:
                        case StringLiteral:
                        case 25:
                        case 32:
                        case 36:
                        case 37:
                        case 40:
                        case 41:
                        case 60:
                        case 62:
                        case 65:
                        case 70:
                        case 73:
                        case 76:
                        case 82:
                        case 84:
                        case 86:
                        case 87:
                        case 93:
                        case 96:
                        case 99:
                        case 103:
                        case 105:
                        case 113: {
                            alt156 = 2;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 156, 1, input);

                            throw nvae;

                    }

                }
                break;
                case 32: {
                    alt156 = 3;
                }
                break;
                case 43: {
                    switch (input.LA(2)) {
                        case 66: {
                            alt156 = 4;
                        }
                        break;
                        case 99: {
                            alt156 = 6;
                        }
                        break;
                        case 96: {
                            alt156 = 7;
                        }
                        break;
                        case 86: {
                            alt156 = 8;
                        }
                        break;
                        case 49: {
                            alt156 = 5;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 156, 3, input);

                            throw nvae;

                    }

                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 156, 0, input);

                    throw nvae;

            }

            switch (alt156) {
                case 1:
                    // example/Java.g:853:9: ( '[' ']' )+ '.' 'class'
                {
                    // example/Java.g:853:9: ( '[' ']' )+
                    int cnt154 = 0;
                    loop154:
                    do {
                        int alt154 = 2;
                        switch (input.LA(1)) {
                            case 55: {
                                alt154 = 1;
                            }
                            break;

                        }

                        switch (alt154) {
                            case 1:
                                // example/Java.g:853:10: '[' ']'
                            {
                                match(input, 55, FOLLOW_55_in_identifierSuffix5237);
                                if (state.failed) return;

                                match(input, 56, FOLLOW_56_in_identifierSuffix5239);
                                if (state.failed) return;

                            }
                            break;

                            default:
                                if (cnt154 >= 1) break loop154;
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                EarlyExitException eee =
                                        new EarlyExitException(154, input);
                                throw eee;
                        }
                        cnt154++;
                    } while (true);


                    match(input, 43, FOLLOW_43_in_identifierSuffix5243);
                    if (state.failed) return;

                    match(input, 66, FOLLOW_66_in_identifierSuffix5245);
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:854:9: ( '[' expression ']' )+
                {
                    // example/Java.g:854:9: ( '[' expression ']' )+
                    int cnt155 = 0;
                    loop155:
                    do {
                        int alt155 = 2;
                        switch (input.LA(1)) {
                            case 55: {
                                int LA155_2 = input.LA(2);

                                if ((synpred249_Java())) {
                                    alt155 = 1;
                                }


                            }
                            break;

                        }

                        switch (alt155) {
                            case 1:
                                // example/Java.g:854:10: '[' expression ']'
                            {
                                match(input, 55, FOLLOW_55_in_identifierSuffix5256);
                                if (state.failed) return;

                                pushFollow(FOLLOW_expression_in_identifierSuffix5258);
                                expression();

                                state._fsp--;
                                if (state.failed) return;

                                match(input, 56, FOLLOW_56_in_identifierSuffix5260);
                                if (state.failed) return;

                            }
                            break;

                            default:
                                if (cnt155 >= 1) break loop155;
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                EarlyExitException eee =
                                        new EarlyExitException(155, input);
                                throw eee;
                        }
                        cnt155++;
                    } while (true);


                }
                break;
                case 3:
                    // example/Java.g:855:9: arguments
                {
                    pushFollow(FOLLOW_arguments_in_identifierSuffix5273);
                    arguments();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:856:9: '.' 'class'
                {
                    match(input, 43, FOLLOW_43_in_identifierSuffix5283);
                    if (state.failed) return;

                    match(input, 66, FOLLOW_66_in_identifierSuffix5285);
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:857:9: '.' explicitGenericInvocation
                {
                    match(input, 43, FOLLOW_43_in_identifierSuffix5295);
                    if (state.failed) return;

                    pushFollow(FOLLOW_explicitGenericInvocation_in_identifierSuffix5297);
                    explicitGenericInvocation();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 6:
                    // example/Java.g:858:9: '.' 'this'
                {
                    match(input, 43, FOLLOW_43_in_identifierSuffix5307);
                    if (state.failed) return;

                    match(input, 99, FOLLOW_99_in_identifierSuffix5309);
                    if (state.failed) return;

                }
                break;
                case 7:
                    // example/Java.g:859:9: '.' 'super' arguments
                {
                    match(input, 43, FOLLOW_43_in_identifierSuffix5319);
                    if (state.failed) return;

                    match(input, 96, FOLLOW_96_in_identifierSuffix5321);
                    if (state.failed) return;

                    pushFollow(FOLLOW_arguments_in_identifierSuffix5323);
                    arguments();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 8:
                    // example/Java.g:860:9: '.' 'new' innerCreator
                {
                    match(input, 43, FOLLOW_43_in_identifierSuffix5333);
                    if (state.failed) return;

                    match(input, 86, FOLLOW_86_in_identifierSuffix5335);
                    if (state.failed) return;

                    pushFollow(FOLLOW_innerCreator_in_identifierSuffix5337);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 125, identifierSuffix_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "identifierSuffix"


    // $ANTLR start "creator"
    // example/Java.g:863:1: creator : ( nonWildcardTypeArguments createdName classCreatorRest | createdName ( arrayCreatorRest | classCreatorRest ) );
    public final void creator() throws RecognitionException {
        int creator_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 126)) {
                return;
            }

            // example/Java.g:864:5: ( nonWildcardTypeArguments createdName classCreatorRest | createdName ( arrayCreatorRest | classCreatorRest ) )
            int alt158 = 2;
            switch (input.LA(1)) {
                case 49: {
                    alt158 = 1;
                }
                break;
                case Identifier:
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt158 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 158, 0, input);

                    throw nvae;

            }

            switch (alt158) {
                case 1:
                    // example/Java.g:864:9: nonWildcardTypeArguments createdName classCreatorRest
                {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator5356);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_createdName_in_creator5358);
                    createdName();

                    state._fsp--;
                    if (state.failed) return;

                    pushFollow(FOLLOW_classCreatorRest_in_creator5360);
                    classCreatorRest();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:865:9: createdName ( arrayCreatorRest | classCreatorRest )
                {
                    pushFollow(FOLLOW_createdName_in_creator5370);
                    createdName();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:865:21: ( arrayCreatorRest | classCreatorRest )
                    int alt157 = 2;
                    switch (input.LA(1)) {
                        case 55: {
                            alt157 = 1;
                        }
                        break;
                        case 32: {
                            alt157 = 2;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 157, 0, input);

                            throw nvae;

                    }

                    switch (alt157) {
                        case 1:
                            // example/Java.g:865:22: arrayCreatorRest
                        {
                            pushFollow(FOLLOW_arrayCreatorRest_in_creator5373);
                            arrayCreatorRest();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;
                        case 2:
                            // example/Java.g:865:41: classCreatorRest
                        {
                            pushFollow(FOLLOW_classCreatorRest_in_creator5377);
                            classCreatorRest();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 126, creator_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "creator"


    // $ANTLR start "createdName"
    // example/Java.g:868:1: createdName : ( classOrInterfaceType | primitiveType );
    public final void createdName() throws RecognitionException {
        int createdName_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 127)) {
                return;
            }

            // example/Java.g:869:5: ( classOrInterfaceType | primitiveType )
            int alt159 = 2;
            switch (input.LA(1)) {
                case Identifier: {
                    alt159 = 1;
                }
                break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93: {
                    alt159 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 159, 0, input);

                    throw nvae;

            }

            switch (alt159) {
                case 1:
                    // example/Java.g:869:9: classOrInterfaceType
                {
                    pushFollow(FOLLOW_classOrInterfaceType_in_createdName5397);
                    classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:870:9: primitiveType
                {
                    pushFollow(FOLLOW_primitiveType_in_createdName5407);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 127, createdName_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "createdName"


    // $ANTLR start "innerCreator"
    // example/Java.g:873:1: innerCreator : ( nonWildcardTypeArguments )? Identifier classCreatorRest ;
    public final void innerCreator() throws RecognitionException {
        int innerCreator_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 128)) {
                return;
            }

            // example/Java.g:874:5: ( ( nonWildcardTypeArguments )? Identifier classCreatorRest )
            // example/Java.g:874:9: ( nonWildcardTypeArguments )? Identifier classCreatorRest
            {
                // example/Java.g:874:9: ( nonWildcardTypeArguments )?
                int alt160 = 2;
                switch (input.LA(1)) {
                    case 49: {
                        alt160 = 1;
                    }
                    break;
                }

                switch (alt160) {
                    case 1:
                        // example/Java.g:874:9: nonWildcardTypeArguments
                    {
                        pushFollow(FOLLOW_nonWildcardTypeArguments_in_innerCreator5430);
                        nonWildcardTypeArguments();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, Identifier, FOLLOW_Identifier_in_innerCreator5433);
                if (state.failed) return;

                pushFollow(FOLLOW_classCreatorRest_in_innerCreator5435);
                classCreatorRest();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 128, innerCreator_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "innerCreator"


    // $ANTLR start "arrayCreatorRest"
    // example/Java.g:877:1: arrayCreatorRest : '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) ;
    public final void arrayCreatorRest() throws RecognitionException {
        int arrayCreatorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 129)) {
                return;
            }

            // example/Java.g:878:5: ( '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) )
            // example/Java.g:878:9: '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            {
                match(input, 55, FOLLOW_55_in_arrayCreatorRest5454);
                if (state.failed) return;

                // example/Java.g:879:9: ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
                int alt164 = 2;
                switch (input.LA(1)) {
                    case 56: {
                        alt164 = 1;
                    }
                    break;
                    case CharacterLiteral:
                    case DecimalLiteral:
                    case FloatingPointLiteral:
                    case HexLiteral:
                    case Identifier:
                    case OctalLiteral:
                    case StringLiteral:
                    case 25:
                    case 32:
                    case 36:
                    case 37:
                    case 40:
                    case 41:
                    case 60:
                    case 62:
                    case 65:
                    case 70:
                    case 73:
                    case 76:
                    case 82:
                    case 84:
                    case 86:
                    case 87:
                    case 93:
                    case 96:
                    case 99:
                    case 103:
                    case 105:
                    case 113: {
                        alt164 = 2;
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 164, 0, input);

                        throw nvae;

                }

                switch (alt164) {
                    case 1:
                        // example/Java.g:879:13: ']' ( '[' ']' )* arrayInitializer
                    {
                        match(input, 56, FOLLOW_56_in_arrayCreatorRest5468);
                        if (state.failed) return;

                        // example/Java.g:879:17: ( '[' ']' )*
                        loop161:
                        do {
                            int alt161 = 2;
                            switch (input.LA(1)) {
                                case 55: {
                                    alt161 = 1;
                                }
                                break;

                            }

                            switch (alt161) {
                                case 1:
                                    // example/Java.g:879:18: '[' ']'
                                {
                                    match(input, 55, FOLLOW_55_in_arrayCreatorRest5471);
                                    if (state.failed) return;

                                    match(input, 56, FOLLOW_56_in_arrayCreatorRest5473);
                                    if (state.failed) return;

                                }
                                break;

                                default:
                                    break loop161;
                            }
                        } while (true);


                        pushFollow(FOLLOW_arrayInitializer_in_arrayCreatorRest5477);
                        arrayInitializer();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;
                    case 2:
                        // example/Java.g:880:13: expression ']' ( '[' expression ']' )* ( '[' ']' )*
                    {
                        pushFollow(FOLLOW_expression_in_arrayCreatorRest5491);
                        expression();

                        state._fsp--;
                        if (state.failed) return;

                        match(input, 56, FOLLOW_56_in_arrayCreatorRest5493);
                        if (state.failed) return;

                        // example/Java.g:880:28: ( '[' expression ']' )*
                        loop162:
                        do {
                            int alt162 = 2;
                            switch (input.LA(1)) {
                                case 55: {
                                    int LA162_1 = input.LA(2);

                                    if ((synpred262_Java())) {
                                        alt162 = 1;
                                    }


                                }
                                break;

                            }

                            switch (alt162) {
                                case 1:
                                    // example/Java.g:880:29: '[' expression ']'
                                {
                                    match(input, 55, FOLLOW_55_in_arrayCreatorRest5496);
                                    if (state.failed) return;

                                    pushFollow(FOLLOW_expression_in_arrayCreatorRest5498);
                                    expression();

                                    state._fsp--;
                                    if (state.failed) return;

                                    match(input, 56, FOLLOW_56_in_arrayCreatorRest5500);
                                    if (state.failed) return;

                                }
                                break;

                                default:
                                    break loop162;
                            }
                        } while (true);


                        // example/Java.g:880:50: ( '[' ']' )*
                        loop163:
                        do {
                            int alt163 = 2;
                            switch (input.LA(1)) {
                                case 55: {
                                    switch (input.LA(2)) {
                                        case 56: {
                                            alt163 = 1;
                                        }
                                        break;

                                    }

                                }
                                break;

                            }

                            switch (alt163) {
                                case 1:
                                    // example/Java.g:880:51: '[' ']'
                                {
                                    match(input, 55, FOLLOW_55_in_arrayCreatorRest5505);
                                    if (state.failed) return;

                                    match(input, 56, FOLLOW_56_in_arrayCreatorRest5507);
                                    if (state.failed) return;

                                }
                                break;

                                default:
                                    break loop163;
                            }
                        } while (true);


                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 129, arrayCreatorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "arrayCreatorRest"


    // $ANTLR start "classCreatorRest"
    // example/Java.g:884:1: classCreatorRest : arguments ( classBody )? ;
    public final void classCreatorRest() throws RecognitionException {
        int classCreatorRest_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 130)) {
                return;
            }

            // example/Java.g:885:5: ( arguments ( classBody )? )
            // example/Java.g:885:9: arguments ( classBody )?
            {
                pushFollow(FOLLOW_arguments_in_classCreatorRest5538);
                arguments();

                state._fsp--;
                if (state.failed) return;

                // example/Java.g:885:19: ( classBody )?
                int alt165 = 2;
                switch (input.LA(1)) {
                    case 108: {
                        alt165 = 1;
                    }
                    break;
                }

                switch (alt165) {
                    case 1:
                        // example/Java.g:885:19: classBody
                    {
                        pushFollow(FOLLOW_classBody_in_classCreatorRest5540);
                        classBody();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 130, classCreatorRest_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "classCreatorRest"


    // $ANTLR start "explicitGenericInvocation"
    // example/Java.g:888:1: explicitGenericInvocation : nonWildcardTypeArguments Identifier arguments ;
    public final void explicitGenericInvocation() throws RecognitionException {
        int explicitGenericInvocation_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 131)) {
                return;
            }

            // example/Java.g:889:5: ( nonWildcardTypeArguments Identifier arguments )
            // example/Java.g:889:9: nonWildcardTypeArguments Identifier arguments
            {
                pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation5564);
                nonWildcardTypeArguments();

                state._fsp--;
                if (state.failed) return;

                match(input, Identifier, FOLLOW_Identifier_in_explicitGenericInvocation5566);
                if (state.failed) return;

                pushFollow(FOLLOW_arguments_in_explicitGenericInvocation5568);
                arguments();

                state._fsp--;
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 131, explicitGenericInvocation_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "explicitGenericInvocation"


    // $ANTLR start "nonWildcardTypeArguments"
    // example/Java.g:892:1: nonWildcardTypeArguments : '<' typeList '>' ;
    public final void nonWildcardTypeArguments() throws RecognitionException {
        int nonWildcardTypeArguments_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 132)) {
                return;
            }

            // example/Java.g:893:5: ( '<' typeList '>' )
            // example/Java.g:893:9: '<' typeList '>'
            {
                match(input, 49, FOLLOW_49_in_nonWildcardTypeArguments5591);
                if (state.failed) return;

                pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments5593);
                typeList();

                state._fsp--;
                if (state.failed) return;

                match(input, 52, FOLLOW_52_in_nonWildcardTypeArguments5595);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 132, nonWildcardTypeArguments_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "nonWildcardTypeArguments"


    // $ANTLR start "selector"
    // example/Java.g:896:1: selector : ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' );
    public final void selector() throws RecognitionException {
        int selector_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 133)) {
                return;
            }

            // example/Java.g:897:5: ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' )
            int alt167 = 5;
            switch (input.LA(1)) {
                case 43: {
                    switch (input.LA(2)) {
                        case Identifier: {
                            alt167 = 1;
                        }
                        break;
                        case 99: {
                            alt167 = 2;
                        }
                        break;
                        case 96: {
                            alt167 = 3;
                        }
                        break;
                        case 86: {
                            alt167 = 4;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 167, 1, input);

                            throw nvae;

                    }

                }
                break;
                case 55: {
                    alt167 = 5;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 167, 0, input);

                    throw nvae;

            }

            switch (alt167) {
                case 1:
                    // example/Java.g:897:9: '.' Identifier ( arguments )?
                {
                    match(input, 43, FOLLOW_43_in_selector5618);
                    if (state.failed) return;

                    match(input, Identifier, FOLLOW_Identifier_in_selector5620);
                    if (state.failed) return;

                    // example/Java.g:897:24: ( arguments )?
                    int alt166 = 2;
                    switch (input.LA(1)) {
                        case 32: {
                            alt166 = 1;
                        }
                        break;
                    }

                    switch (alt166) {
                        case 1:
                            // example/Java.g:897:24: arguments
                        {
                            pushFollow(FOLLOW_arguments_in_selector5622);
                            arguments();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;
                case 2:
                    // example/Java.g:898:9: '.' 'this'
                {
                    match(input, 43, FOLLOW_43_in_selector5633);
                    if (state.failed) return;

                    match(input, 99, FOLLOW_99_in_selector5635);
                    if (state.failed) return;

                }
                break;
                case 3:
                    // example/Java.g:899:9: '.' 'super' superSuffix
                {
                    match(input, 43, FOLLOW_43_in_selector5645);
                    if (state.failed) return;

                    match(input, 96, FOLLOW_96_in_selector5647);
                    if (state.failed) return;

                    pushFollow(FOLLOW_superSuffix_in_selector5649);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 4:
                    // example/Java.g:900:9: '.' 'new' innerCreator
                {
                    match(input, 43, FOLLOW_43_in_selector5659);
                    if (state.failed) return;

                    match(input, 86, FOLLOW_86_in_selector5661);
                    if (state.failed) return;

                    pushFollow(FOLLOW_innerCreator_in_selector5663);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 5:
                    // example/Java.g:901:9: '[' expression ']'
                {
                    match(input, 55, FOLLOW_55_in_selector5673);
                    if (state.failed) return;

                    pushFollow(FOLLOW_expression_in_selector5675);
                    expression();

                    state._fsp--;
                    if (state.failed) return;

                    match(input, 56, FOLLOW_56_in_selector5677);
                    if (state.failed) return;

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 133, selector_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "selector"


    // $ANTLR start "superSuffix"
    // example/Java.g:904:1: superSuffix : ( arguments | '.' Identifier ( arguments )? );
    public final void superSuffix() throws RecognitionException {
        int superSuffix_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 134)) {
                return;
            }

            // example/Java.g:905:5: ( arguments | '.' Identifier ( arguments )? )
            int alt169 = 2;
            switch (input.LA(1)) {
                case 32: {
                    alt169 = 1;
                }
                break;
                case 43: {
                    alt169 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 169, 0, input);

                    throw nvae;

            }

            switch (alt169) {
                case 1:
                    // example/Java.g:905:9: arguments
                {
                    pushFollow(FOLLOW_arguments_in_superSuffix5700);
                    arguments();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;
                case 2:
                    // example/Java.g:906:9: '.' Identifier ( arguments )?
                {
                    match(input, 43, FOLLOW_43_in_superSuffix5710);
                    if (state.failed) return;

                    match(input, Identifier, FOLLOW_Identifier_in_superSuffix5712);
                    if (state.failed) return;

                    // example/Java.g:906:24: ( arguments )?
                    int alt168 = 2;
                    switch (input.LA(1)) {
                        case 32: {
                            alt168 = 1;
                        }
                        break;
                    }

                    switch (alt168) {
                        case 1:
                            // example/Java.g:906:24: arguments
                        {
                            pushFollow(FOLLOW_arguments_in_superSuffix5714);
                            arguments();

                            state._fsp--;
                            if (state.failed) return;

                        }
                        break;

                    }


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 134, superSuffix_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "superSuffix"


    // $ANTLR start "arguments"
    // example/Java.g:909:1: arguments : '(' ( expressionList )? ')' ;
    public final void arguments() throws RecognitionException {
        int arguments_StartIndex = input.index();

        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 135)) {
                return;
            }

            // example/Java.g:910:5: ( '(' ( expressionList )? ')' )
            // example/Java.g:910:9: '(' ( expressionList )? ')'
            {
                match(input, 32, FOLLOW_32_in_arguments5734);
                if (state.failed) return;

                // example/Java.g:910:13: ( expressionList )?
                int alt170 = 2;
                switch (input.LA(1)) {
                    case CharacterLiteral:
                    case DecimalLiteral:
                    case FloatingPointLiteral:
                    case HexLiteral:
                    case Identifier:
                    case OctalLiteral:
                    case StringLiteral:
                    case 25:
                    case 32:
                    case 36:
                    case 37:
                    case 40:
                    case 41:
                    case 60:
                    case 62:
                    case 65:
                    case 70:
                    case 73:
                    case 76:
                    case 82:
                    case 84:
                    case 86:
                    case 87:
                    case 93:
                    case 96:
                    case 99:
                    case 103:
                    case 105:
                    case 113: {
                        alt170 = 1;
                    }
                    break;
                }

                switch (alt170) {
                    case 1:
                        // example/Java.g:910:13: expressionList
                    {
                        pushFollow(FOLLOW_expressionList_in_arguments5736);
                        expressionList();

                        state._fsp--;
                        if (state.failed) return;

                    }
                    break;

                }


                match(input, 33, FOLLOW_33_in_arguments5739);
                if (state.failed) return;

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
            if (state.backtracking > 0) {
                memoize(input, 135, arguments_StartIndex);
            }

        }
        return;
    }
    // $ANTLR end "arguments"

    // $ANTLR start synpred5_Java
    public final void synpred5_Java_fragment() throws RecognitionException {
        // example/Java.g:184:9: ( annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) )
        // example/Java.g:184:9: annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
        {
            pushFollow(FOLLOW_annotations_in_synpred5_Java59);
            annotations();

            state._fsp--;
            if (state.failed) return;

            // example/Java.g:185:9: ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
            int alt176 = 2;
            switch (input.LA(1)) {
                case 88: {
                    alt176 = 1;
                }
                break;
                case ENUM:
                case 54:
                case 59:
                case 66:
                case 74:
                case 83:
                case 89:
                case 90:
                case 91:
                case 94:
                case 95: {
                    alt176 = 2;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 176, 0, input);

                    throw nvae;

            }

            switch (alt176) {
                case 1:
                    // example/Java.g:185:13: packageDeclaration ( importDeclaration )* ( typeDeclaration )*
                {
                    pushFollow(FOLLOW_packageDeclaration_in_synpred5_Java73);
                    packageDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:185:32: ( importDeclaration )*
                    loop173:
                    do {
                        int alt173 = 2;
                        switch (input.LA(1)) {
                            case 80: {
                                alt173 = 1;
                            }
                            break;

                        }

                        switch (alt173) {
                            case 1:
                                // example/Java.g:185:32: importDeclaration
                            {
                                pushFollow(FOLLOW_importDeclaration_in_synpred5_Java75);
                                importDeclaration();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop173;
                        }
                    } while (true);


                    // example/Java.g:185:51: ( typeDeclaration )*
                    loop174:
                    do {
                        int alt174 = 2;
                        switch (input.LA(1)) {
                            case ENUM:
                            case 48:
                            case 54:
                            case 59:
                            case 66:
                            case 74:
                            case 83:
                            case 89:
                            case 90:
                            case 91:
                            case 94:
                            case 95: {
                                alt174 = 1;
                            }
                            break;

                        }

                        switch (alt174) {
                            case 1:
                                // example/Java.g:185:51: typeDeclaration
                            {
                                pushFollow(FOLLOW_typeDeclaration_in_synpred5_Java78);
                                typeDeclaration();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop174;
                        }
                    } while (true);


                }
                break;
                case 2:
                    // example/Java.g:186:13: classOrInterfaceDeclaration ( typeDeclaration )*
                {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred5_Java93);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return;

                    // example/Java.g:186:41: ( typeDeclaration )*
                    loop175:
                    do {
                        int alt175 = 2;
                        switch (input.LA(1)) {
                            case ENUM:
                            case 48:
                            case 54:
                            case 59:
                            case 66:
                            case 74:
                            case 83:
                            case 89:
                            case 90:
                            case 91:
                            case 94:
                            case 95: {
                                alt175 = 1;
                            }
                            break;

                        }

                        switch (alt175) {
                            case 1:
                                // example/Java.g:186:41: typeDeclaration
                            {
                                pushFollow(FOLLOW_typeDeclaration_in_synpred5_Java95);
                                typeDeclaration();

                                state._fsp--;
                                if (state.failed) return;

                            }
                            break;

                            default:
                                break loop175;
                        }
                    } while (true);


                }
                break;

            }


        }

    }
    // $ANTLR end synpred5_Java

    // $ANTLR start synpred113_Java
    public final void synpred113_Java_fragment() throws RecognitionException {
        // example/Java.g:498:13: ( explicitConstructorInvocation )
        // example/Java.g:498:13: explicitConstructorInvocation
        {
            pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred113_Java2470);
            explicitConstructorInvocation();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred113_Java

    // $ANTLR start synpred117_Java
    public final void synpred117_Java_fragment() throws RecognitionException {
        // example/Java.g:502:9: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' )
        // example/Java.g:502:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
        {
            // example/Java.g:502:9: ( nonWildcardTypeArguments )?
            int alt184 = 2;
            switch (input.LA(1)) {
                case 49: {
                    alt184 = 1;
                }
                break;
            }

            switch (alt184) {
                case 1:
                    // example/Java.g:502:9: nonWildcardTypeArguments
                {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred117_Java2495);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return;

                }
                break;

            }


            if (input.LA(1) == 96 || input.LA(1) == 99) {
                input.consume();
                state.errorRecovery = false;
                state.failed = false;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                MismatchedSetException mse = new MismatchedSetException(null, input);
                throw mse;
            }


            pushFollow(FOLLOW_arguments_in_synpred117_Java2506);
            arguments();

            state._fsp--;
            if (state.failed) return;

            match(input, 48, FOLLOW_48_in_synpred117_Java2508);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred117_Java

    // $ANTLR start synpred128_Java
    public final void synpred128_Java_fragment() throws RecognitionException {
        // example/Java.g:534:9: ( annotation )
        // example/Java.g:534:9: annotation
        {
            pushFollow(FOLLOW_annotation_in_synpred128_Java2719);
            annotation();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred128_Java

    // $ANTLR start synpred151_Java
    public final void synpred151_Java_fragment() throws RecognitionException {
        // example/Java.g:607:9: ( localVariableDeclarationStatement )
        // example/Java.g:607:9: localVariableDeclarationStatement
        {
            pushFollow(FOLLOW_localVariableDeclarationStatement_in_synpred151_Java3246);
            localVariableDeclarationStatement();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred151_Java

    // $ANTLR start synpred152_Java
    public final void synpred152_Java_fragment() throws RecognitionException {
        // example/Java.g:608:9: ( classOrInterfaceDeclaration )
        // example/Java.g:608:9: classOrInterfaceDeclaration
        {
            pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred152_Java3256);
            classOrInterfaceDeclaration();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred152_Java

    // $ANTLR start synpred157_Java
    public final void synpred157_Java_fragment() throws RecognitionException {
        // example/Java.g:627:54: ( 'else' statement )
        // example/Java.g:627:54: 'else' statement
        {
            match(input, 71, FOLLOW_71_in_synpred157_Java3401);
            if (state.failed) return;

            pushFollow(FOLLOW_statement_in_synpred157_Java3403);
            statement();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred157_Java

    // $ANTLR start synpred162_Java
    public final void synpred162_Java_fragment() throws RecognitionException {
        // example/Java.g:632:11: ( catches 'finally' block )
        // example/Java.g:632:11: catches 'finally' block
        {
            pushFollow(FOLLOW_catches_in_synpred162_Java3479);
            catches();

            state._fsp--;
            if (state.failed) return;

            match(input, 75, FOLLOW_75_in_synpred162_Java3481);
            if (state.failed) return;

            pushFollow(FOLLOW_block_in_synpred162_Java3483);
            block();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred162_Java

    // $ANTLR start synpred163_Java
    public final void synpred163_Java_fragment() throws RecognitionException {
        // example/Java.g:633:11: ( catches )
        // example/Java.g:633:11: catches
        {
            pushFollow(FOLLOW_catches_in_synpred163_Java3495);
            catches();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred163_Java

    // $ANTLR start synpred178_Java
    public final void synpred178_Java_fragment() throws RecognitionException {
        // example/Java.g:668:9: ( switchLabel )
        // example/Java.g:668:9: switchLabel
        {
            pushFollow(FOLLOW_switchLabel_in_synpred178_Java3786);
            switchLabel();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred178_Java

    // $ANTLR start synpred180_Java
    public final void synpred180_Java_fragment() throws RecognitionException {
        // example/Java.g:672:9: ( 'case' constantExpression ':' )
        // example/Java.g:672:9: 'case' constantExpression ':'
        {
            match(input, 63, FOLLOW_63_in_synpred180_Java3813);
            if (state.failed) return;

            pushFollow(FOLLOW_constantExpression_in_synpred180_Java3815);
            constantExpression();

            state._fsp--;
            if (state.failed) return;

            match(input, 47, FOLLOW_47_in_synpred180_Java3817);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred180_Java

    // $ANTLR start synpred181_Java
    public final void synpred181_Java_fragment() throws RecognitionException {
        // example/Java.g:673:9: ( 'case' enumConstantName ':' )
        // example/Java.g:673:9: 'case' enumConstantName ':'
        {
            match(input, 63, FOLLOW_63_in_synpred181_Java3827);
            if (state.failed) return;

            pushFollow(FOLLOW_enumConstantName_in_synpred181_Java3829);
            enumConstantName();

            state._fsp--;
            if (state.failed) return;

            match(input, 47, FOLLOW_47_in_synpred181_Java3831);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred181_Java

    // $ANTLR start synpred182_Java
    public final void synpred182_Java_fragment() throws RecognitionException {
        // example/Java.g:679:9: ( enhancedForControl )
        // example/Java.g:679:9: enhancedForControl
        {
            pushFollow(FOLLOW_enhancedForControl_in_synpred182_Java3874);
            enhancedForControl();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred182_Java

    // $ANTLR start synpred186_Java
    public final void synpred186_Java_fragment() throws RecognitionException {
        // example/Java.g:684:9: ( localVariableDeclaration )
        // example/Java.g:684:9: localVariableDeclaration
        {
            pushFollow(FOLLOW_localVariableDeclaration_in_synpred186_Java3914);
            localVariableDeclaration();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred186_Java

    // $ANTLR start synpred198_Java
    public final void synpred198_Java_fragment() throws RecognitionException {
        // example/Java.g:728:9: ( '<' '<' '=' )
        // example/Java.g:728:10: '<' '<' '='
        {
            match(input, 49, FOLLOW_49_in_synpred198_Java4215);
            if (state.failed) return;

            match(input, 49, FOLLOW_49_in_synpred198_Java4217);
            if (state.failed) return;

            match(input, 50, FOLLOW_50_in_synpred198_Java4219);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred198_Java

    // $ANTLR start synpred199_Java
    public final void synpred199_Java_fragment() throws RecognitionException {
        // example/Java.g:733:9: ( '>' '>' '>' '=' )
        // example/Java.g:733:10: '>' '>' '>' '='
        {
            match(input, 52, FOLLOW_52_in_synpred199_Java4255);
            if (state.failed) return;

            match(input, 52, FOLLOW_52_in_synpred199_Java4257);
            if (state.failed) return;

            match(input, 52, FOLLOW_52_in_synpred199_Java4259);
            if (state.failed) return;

            match(input, 50, FOLLOW_50_in_synpred199_Java4261);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred199_Java

    // $ANTLR start synpred200_Java
    public final void synpred200_Java_fragment() throws RecognitionException {
        // example/Java.g:740:9: ( '>' '>' '=' )
        // example/Java.g:740:10: '>' '>' '='
        {
            match(input, 52, FOLLOW_52_in_synpred200_Java4300);
            if (state.failed) return;

            match(input, 52, FOLLOW_52_in_synpred200_Java4302);
            if (state.failed) return;

            match(input, 50, FOLLOW_50_in_synpred200_Java4304);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred200_Java

    // $ANTLR start synpred211_Java
    public final void synpred211_Java_fragment() throws RecognitionException {
        // example/Java.g:784:9: ( '<' '=' )
        // example/Java.g:784:10: '<' '='
        {
            match(input, 49, FOLLOW_49_in_synpred211_Java4612);
            if (state.failed) return;

            match(input, 50, FOLLOW_50_in_synpred211_Java4614);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred211_Java

    // $ANTLR start synpred212_Java
    public final void synpred212_Java_fragment() throws RecognitionException {
        // example/Java.g:787:9: ( '>' '=' )
        // example/Java.g:787:10: '>' '='
        {
            match(input, 52, FOLLOW_52_in_synpred212_Java4646);
            if (state.failed) return;

            match(input, 50, FOLLOW_50_in_synpred212_Java4648);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred212_Java

    // $ANTLR start synpred215_Java
    public final void synpred215_Java_fragment() throws RecognitionException {
        // example/Java.g:799:9: ( '<' '<' )
        // example/Java.g:799:10: '<' '<'
        {
            match(input, 49, FOLLOW_49_in_synpred215_Java4739);
            if (state.failed) return;

            match(input, 49, FOLLOW_49_in_synpred215_Java4741);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred215_Java

    // $ANTLR start synpred216_Java
    public final void synpred216_Java_fragment() throws RecognitionException {
        // example/Java.g:802:9: ( '>' '>' '>' )
        // example/Java.g:802:10: '>' '>' '>'
        {
            match(input, 52, FOLLOW_52_in_synpred216_Java4773);
            if (state.failed) return;

            match(input, 52, FOLLOW_52_in_synpred216_Java4775);
            if (state.failed) return;

            match(input, 52, FOLLOW_52_in_synpred216_Java4777);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred216_Java

    // $ANTLR start synpred217_Java
    public final void synpred217_Java_fragment() throws RecognitionException {
        // example/Java.g:807:9: ( '>' '>' )
        // example/Java.g:807:10: '>' '>'
        {
            match(input, 52, FOLLOW_52_in_synpred217_Java4813);
            if (state.failed) return;

            match(input, 52, FOLLOW_52_in_synpred217_Java4815);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred217_Java

    // $ANTLR start synpred229_Java
    public final void synpred229_Java_fragment() throws RecognitionException {
        // example/Java.g:832:9: ( castExpression )
        // example/Java.g:832:9: castExpression
        {
            pushFollow(FOLLOW_castExpression_in_synpred229_Java5024);
            castExpression();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred229_Java

    // $ANTLR start synpred233_Java
    public final void synpred233_Java_fragment() throws RecognitionException {
        // example/Java.g:837:8: ( '(' primitiveType ')' unaryExpression )
        // example/Java.g:837:8: '(' primitiveType ')' unaryExpression
        {
            match(input, 32, FOLLOW_32_in_synpred233_Java5062);
            if (state.failed) return;

            pushFollow(FOLLOW_primitiveType_in_synpred233_Java5064);
            primitiveType();

            state._fsp--;
            if (state.failed) return;

            match(input, 33, FOLLOW_33_in_synpred233_Java5066);
            if (state.failed) return;

            pushFollow(FOLLOW_unaryExpression_in_synpred233_Java5068);
            unaryExpression();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred233_Java

    // $ANTLR start synpred234_Java
    public final void synpred234_Java_fragment() throws RecognitionException {
        // example/Java.g:838:13: ( type )
        // example/Java.g:838:13: type
        {
            pushFollow(FOLLOW_type_in_synpred234_Java5080);
            type();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred234_Java

    // $ANTLR start synpred236_Java
    public final void synpred236_Java_fragment() throws RecognitionException {
        // example/Java.g:843:17: ( '.' Identifier )
        // example/Java.g:843:17: '.' Identifier
        {
            match(input, 43, FOLLOW_43_in_synpred236_Java5121);
            if (state.failed) return;

            match(input, Identifier, FOLLOW_Identifier_in_synpred236_Java5123);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred236_Java

    // $ANTLR start synpred237_Java
    public final void synpred237_Java_fragment() throws RecognitionException {
        // example/Java.g:843:34: ( identifierSuffix )
        // example/Java.g:843:34: identifierSuffix
        {
            pushFollow(FOLLOW_identifierSuffix_in_synpred237_Java5127);
            identifierSuffix();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred237_Java

    // $ANTLR start synpred242_Java
    public final void synpred242_Java_fragment() throws RecognitionException {
        // example/Java.g:847:21: ( '.' Identifier )
        // example/Java.g:847:21: '.' Identifier
        {
            match(input, 43, FOLLOW_43_in_synpred242_Java5175);
            if (state.failed) return;

            match(input, Identifier, FOLLOW_Identifier_in_synpred242_Java5177);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred242_Java

    // $ANTLR start synpred243_Java
    public final void synpred243_Java_fragment() throws RecognitionException {
        // example/Java.g:847:38: ( identifierSuffix )
        // example/Java.g:847:38: identifierSuffix
        {
            pushFollow(FOLLOW_identifierSuffix_in_synpred243_Java5181);
            identifierSuffix();

            state._fsp--;
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred243_Java

    // $ANTLR start synpred249_Java
    public final void synpred249_Java_fragment() throws RecognitionException {
        // example/Java.g:854:10: ( '[' expression ']' )
        // example/Java.g:854:10: '[' expression ']'
        {
            match(input, 55, FOLLOW_55_in_synpred249_Java5256);
            if (state.failed) return;

            pushFollow(FOLLOW_expression_in_synpred249_Java5258);
            expression();

            state._fsp--;
            if (state.failed) return;

            match(input, 56, FOLLOW_56_in_synpred249_Java5260);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred249_Java

    // $ANTLR start synpred262_Java
    public final void synpred262_Java_fragment() throws RecognitionException {
        // example/Java.g:880:29: ( '[' expression ']' )
        // example/Java.g:880:29: '[' expression ']'
        {
            match(input, 55, FOLLOW_55_in_synpred262_Java5496);
            if (state.failed) return;

            pushFollow(FOLLOW_expression_in_synpred262_Java5498);
            expression();

            state._fsp--;
            if (state.failed) return;

            match(input, 56, FOLLOW_56_in_synpred262_Java5500);
            if (state.failed) return;

        }

    }
    // $ANTLR end synpred262_Java

    // Delegated rules

    public final boolean synpred157_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred157_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred211_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred211_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred249_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred249_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred243_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred243_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred5_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred229_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred229_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred178_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred178_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred215_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred215_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred113_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred113_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred151_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred151_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred117_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred117_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred162_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred162_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred217_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred217_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred186_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred186_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred212_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred212_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred163_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred163_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred152_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred152_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred242_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred242_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred199_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred199_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred216_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred216_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred236_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred236_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred262_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred262_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred198_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred198_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred233_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred233_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred180_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred180_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred128_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred128_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred200_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred200_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred234_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred234_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred182_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred182_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred181_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred181_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred237_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred237_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }


    protected DFA123 dfa123 = new DFA123(this);
    protected DFA146 dfa146 = new DFA146(this);
    static final String DFA123_eotS =
            "\u0087\uffff";
    static final String DFA123_eofS =
            "\u0087\uffff";
    static final String DFA123_minS =
            "\1\6\4\17\22\uffff\5\17\1\6\1\17\1\6\1\47\30\uffff\1\70\1\47\1\uffff" +
                    "\21\0\2\uffff\3\0\21\uffff\1\0\5\uffff\1\0\30\uffff\1\0\5\uffff";
    static final String DFA123_maxS =
            "\1\161\1\135\1\17\1\157\1\67\22\uffff\2\67\1\135\1\17\1\135\1\161" +
                    "\1\143\1\161\1\67\30\uffff\1\70\1\67\1\uffff\21\0\2\uffff\3\0\21" +
                    "\uffff\1\0\5\uffff\1\0\30\uffff\1\0\5\uffff";
    static final String DFA123_acceptS =
            "\5\uffff\1\2\166\uffff\1\1\12\uffff";
    static final String DFA123_specialS =
            "\73\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1" +
                    "\14\1\15\1\16\1\17\1\20\2\uffff\1\21\1\22\1\23\21\uffff\1\24\5\uffff" +
                    "\1\25\30\uffff\1\26\5\uffff}>";
    static final String[] DFA123_transitionS = {
            "\2\5\4\uffff\1\5\1\uffff\1\5\1\3\5\uffff\2\5\2\uffff\1\5\6\uffff" +
                    "\1\5\3\uffff\2\5\2\uffff\2\5\6\uffff\1\5\5\uffff\1\2\5\uffff" +
                    "\1\4\1\uffff\1\4\2\uffff\1\4\4\uffff\1\4\2\uffff\1\5\1\1\1\uffff" +
                    "\1\4\5\uffff\1\4\1\uffff\1\4\1\uffff\2\5\5\uffff\1\4\2\uffff" +
                    "\1\5\2\uffff\1\5\3\uffff\1\5\1\uffff\1\5\7\uffff\1\5",
            "\1\27\46\uffff\1\32\5\uffff\1\30\1\uffff\1\30\2\uffff\1\30" +
                    "\4\uffff\1\30\3\uffff\1\31\1\uffff\1\30\5\uffff\1\30\1\uffff" +
                    "\1\30\10\uffff\1\30",
            "\1\33",
            "\1\37\12\uffff\7\5\1\uffff\11\5\1\35\1\uffff\2\5\1\uffff\1" +
                    "\5\1\34\4\5\1\uffff\1\36\1\uffff\2\5\26\uffff\1\5\33\uffff\3" +
                    "\5",
            "\1\71\33\uffff\1\5\13\uffff\1\70",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\76\33\uffff\1\74\5\uffff\1\73\5\uffff\1\75",
            "\1\100\47\uffff\1\77",
            "\1\101\46\uffff\1\104\5\uffff\1\102\1\uffff\1\102\2\uffff\1" +
                    "\102\4\uffff\1\102\3\uffff\1\103\1\uffff\1\102\5\uffff\1\102" +
                    "\1\uffff\1\102\10\uffff\1\102",
            "\1\105",
            "\1\110\20\uffff\1\107\12\uffff\1\106\12\uffff\1\113\5\uffff" +
                    "\1\111\1\uffff\1\111\2\uffff\1\111\4\uffff\1\111\3\uffff\1\112" +
                    "\1\uffff\1\111\5\uffff\1\111\1\uffff\1\111\10\uffff\1\111",
            "\2\5\4\uffff\1\5\1\uffff\1\5\1\116\5\uffff\2\5\2\uffff\1\5" +
                    "\6\uffff\1\5\3\uffff\2\5\2\uffff\2\5\7\uffff\2\5\2\uffff\1\120" +
                    "\6\uffff\1\117\1\uffff\1\117\2\uffff\1\117\4\uffff\1\117\2\uffff" +
                    "\1\5\2\uffff\1\117\5\uffff\1\117\1\uffff\1\117\1\uffff\2\5\5" +
                    "\uffff\1\117\2\uffff\1\5\2\uffff\1\5\3\uffff\1\5\1\uffff\1\5" +
                    "\7\uffff\1\5",
            "\1\142\41\uffff\1\5\20\uffff\1\5\23\uffff\1\5\11\uffff\1\5" +
                    "\2\uffff\1\5",
            "\2\5\4\uffff\1\5\1\uffff\2\5\5\uffff\2\5\2\uffff\1\5\6\uffff" +
                    "\1\5\3\uffff\2\5\2\uffff\2\5\16\uffff\1\150\3\uffff\1\5\1\uffff" +
                    "\1\5\2\uffff\1\5\4\uffff\1\5\2\uffff\1\5\2\uffff\1\5\5\uffff" +
                    "\1\5\1\uffff\1\5\1\uffff\2\5\5\uffff\1\5\2\uffff\1\5\2\uffff" +
                    "\1\5\3\uffff\1\5\1\uffff\1\5\7\uffff\1\5",
            "\1\5\7\uffff\1\174\1\5\1\uffff\1\5\4\uffff\1\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0081",
            "\1\5\7\uffff\1\174\1\5\1\uffff\1\5\4\uffff\1\5",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA123_eot = DFA.unpackEncodedString(DFA123_eotS);
    static final short[] DFA123_eof = DFA.unpackEncodedString(DFA123_eofS);
    static final char[] DFA123_min = DFA.unpackEncodedStringToUnsignedChars(DFA123_minS);
    static final char[] DFA123_max = DFA.unpackEncodedStringToUnsignedChars(DFA123_maxS);
    static final short[] DFA123_accept = DFA.unpackEncodedString(DFA123_acceptS);
    static final short[] DFA123_special = DFA.unpackEncodedString(DFA123_specialS);
    static final short[][] DFA123_transition;

    static {
        int numStates = DFA123_transitionS.length;
        DFA123_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA123_transition[i] = DFA.unpackEncodedString(DFA123_transitionS[i]);
        }
    }

    class DFA123 extends DFA {

        public DFA123(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 123;
            this.eot = DFA123_eot;
            this.eof = DFA123_eof;
            this.min = DFA123_min;
            this.max = DFA123_max;
            this.accept = DFA123_accept;
            this.special = DFA123_special;
            this.transition = DFA123_transition;
        }

        public String getDescription() {
            return "677:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );";
        }

        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream) _input;
            int _s = s;
            switch (s) {
                case 0:
                    int LA123_59 = input.LA(1);


                    int index123_59 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_59);

                    if (s >= 0) return s;
                    break;
                case 1:
                    int LA123_60 = input.LA(1);


                    int index123_60 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_60);

                    if (s >= 0) return s;
                    break;
                case 2:
                    int LA123_61 = input.LA(1);


                    int index123_61 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_61);

                    if (s >= 0) return s;
                    break;
                case 3:
                    int LA123_62 = input.LA(1);


                    int index123_62 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_62);

                    if (s >= 0) return s;
                    break;
                case 4:
                    int LA123_63 = input.LA(1);


                    int index123_63 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_63);

                    if (s >= 0) return s;
                    break;
                case 5:
                    int LA123_64 = input.LA(1);


                    int index123_64 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_64);

                    if (s >= 0) return s;
                    break;
                case 6:
                    int LA123_65 = input.LA(1);


                    int index123_65 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_65);

                    if (s >= 0) return s;
                    break;
                case 7:
                    int LA123_66 = input.LA(1);


                    int index123_66 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_66);

                    if (s >= 0) return s;
                    break;
                case 8:
                    int LA123_67 = input.LA(1);


                    int index123_67 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_67);

                    if (s >= 0) return s;
                    break;
                case 9:
                    int LA123_68 = input.LA(1);


                    int index123_68 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_68);

                    if (s >= 0) return s;
                    break;
                case 10:
                    int LA123_69 = input.LA(1);


                    int index123_69 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_69);

                    if (s >= 0) return s;
                    break;
                case 11:
                    int LA123_70 = input.LA(1);


                    int index123_70 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_70);

                    if (s >= 0) return s;
                    break;
                case 12:
                    int LA123_71 = input.LA(1);


                    int index123_71 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_71);

                    if (s >= 0) return s;
                    break;
                case 13:
                    int LA123_72 = input.LA(1);


                    int index123_72 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_72);

                    if (s >= 0) return s;
                    break;
                case 14:
                    int LA123_73 = input.LA(1);


                    int index123_73 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_73);

                    if (s >= 0) return s;
                    break;
                case 15:
                    int LA123_74 = input.LA(1);


                    int index123_74 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_74);

                    if (s >= 0) return s;
                    break;
                case 16:
                    int LA123_75 = input.LA(1);


                    int index123_75 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_75);

                    if (s >= 0) return s;
                    break;
                case 17:
                    int LA123_78 = input.LA(1);


                    int index123_78 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_78);

                    if (s >= 0) return s;
                    break;
                case 18:
                    int LA123_79 = input.LA(1);


                    int index123_79 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_79);

                    if (s >= 0) return s;
                    break;
                case 19:
                    int LA123_80 = input.LA(1);


                    int index123_80 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_80);

                    if (s >= 0) return s;
                    break;
                case 20:
                    int LA123_98 = input.LA(1);


                    int index123_98 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_98);

                    if (s >= 0) return s;
                    break;
                case 21:
                    int LA123_104 = input.LA(1);


                    int index123_104 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_104);

                    if (s >= 0) return s;
                    break;
                case 22:
                    int LA123_129 = input.LA(1);


                    int index123_129 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred182_Java())) {
                        s = 124;
                    } else if ((true)) {
                        s = 5;
                    }


                    input.seek(index123_129);

                    if (s >= 0) return s;
                    break;
            }
            if (state.backtracking > 0) {
                state.failed = true;
                return -1;
            }

            NoViableAltException nvae =
                    new NoViableAltException(getDescription(), 123, _s, input);
            error(nvae);
            throw nvae;
        }

    }

    static final String DFA146_eotS =
            "\7\uffff";
    static final String DFA146_eofS =
            "\7\uffff";
    static final String DFA146_minS =
            "\1\6\1\0\1\41\2\uffff\1\70\1\41";
    static final String DFA146_maxS =
            "\1\161\1\0\1\67\2\uffff\1\70\1\67";
    static final String DFA146_acceptS =
            "\3\uffff\1\2\1\1\2\uffff";
    static final String DFA146_specialS =
            "\1\uffff\1\0\5\uffff}>";
    static final String[] DFA146_transitionS = {
            "\2\3\4\uffff\1\3\1\uffff\1\3\1\1\5\uffff\2\3\2\uffff\1\3\6\uffff" +
                    "\1\3\3\uffff\2\3\2\uffff\2\3\22\uffff\1\2\1\uffff\1\2\2\uffff" +
                    "\1\2\4\uffff\1\2\2\uffff\1\3\2\uffff\1\2\5\uffff\1\2\1\uffff" +
                    "\1\2\1\uffff\2\3\5\uffff\1\2\2\uffff\1\3\2\uffff\1\3\3\uffff" +
                    "\1\3\1\uffff\1\3\7\uffff\1\3",
            "\1\uffff",
            "\1\4\11\uffff\1\3\13\uffff\1\5",
            "",
            "",
            "\1\6",
            "\1\4\11\uffff\1\3\13\uffff\1\5"
    };

    static final short[] DFA146_eot = DFA.unpackEncodedString(DFA146_eotS);
    static final short[] DFA146_eof = DFA.unpackEncodedString(DFA146_eofS);
    static final char[] DFA146_min = DFA.unpackEncodedStringToUnsignedChars(DFA146_minS);
    static final char[] DFA146_max = DFA.unpackEncodedStringToUnsignedChars(DFA146_maxS);
    static final short[] DFA146_accept = DFA.unpackEncodedString(DFA146_acceptS);
    static final short[] DFA146_special = DFA.unpackEncodedString(DFA146_specialS);
    static final short[][] DFA146_transition;

    static {
        int numStates = DFA146_transitionS.length;
        DFA146_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA146_transition[i] = DFA.unpackEncodedString(DFA146_transitionS[i]);
        }
    }

    class DFA146 extends DFA {

        public DFA146(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 146;
            this.eot = DFA146_eot;
            this.eof = DFA146_eof;
            this.min = DFA146_min;
            this.max = DFA146_max;
            this.accept = DFA146_accept;
            this.special = DFA146_special;
            this.transition = DFA146_transition;
        }

        public String getDescription() {
            return "838:12: ( type | expression )";
        }

        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream) _input;
            int _s = s;
            switch (s) {
                case 0:
                    int LA146_1 = input.LA(1);


                    int index146_1 = input.index();
                    input.rewind();

                    s = -1;
                    if ((synpred234_Java())) {
                        s = 4;
                    } else if ((true)) {
                        s = 3;
                    }


                    input.seek(index146_1);

                    if (s >= 0) return s;
                    break;
            }
            if (state.backtracking > 0) {
                state.failed = true;
                return -1;
            }

            NoViableAltException nvae =
                    new NoViableAltException(getDescription(), 146, _s, input);
            error(nvae);
            throw nvae;
        }

    }


    public static final BitSet FOLLOW_annotations_in_compilationUnit59 = new BitSet(new long[]{0x0840000000000100L, 0x00000000CF080404L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit73 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE090404L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit75 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE090404L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit78 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE080404L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_compilationUnit93 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE080404L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit95 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE080404L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit116 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE090404L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit119 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE090404L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit122 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE080404L});
    public static final BitSet FOLLOW_88_in_packageDeclaration142 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration144 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_packageDeclaration146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_importDeclaration169 = new BitSet(new long[]{0x0000000000008000L, 0x0000000040000000L});
    public static final BitSet FOLLOW_94_in_importDeclaration171 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedName_in_importDeclaration174 = new BitSet(new long[]{0x0001080000000000L});
    public static final BitSet FOLLOW_43_in_importDeclaration177 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_importDeclaration179 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_importDeclaration183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_typeDeclaration216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceModifiers_in_classOrInterfaceDeclaration239 = new BitSet(new long[]{0x0040000000000100L, 0x0000000000080004L});
    public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceModifier_in_classOrInterfaceModifiers270 = new BitSet(new long[]{0x0840000000000002L, 0x00000000CE000400L});
    public static final BitSet FOLLOW_annotation_in_classOrInterfaceModifier290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_classOrInterfaceModifier303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_classOrInterfaceModifier318 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_classOrInterfaceModifier330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_classOrInterfaceModifier344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_classOrInterfaceModifier357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_classOrInterfaceModifier372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_classOrInterfaceModifier388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_modifiers410 = new BitSet(new long[]{0x0840000000000002L, 0x00000444CE200400L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_normalClassDeclaration463 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_normalClassDeclaration465 = new BitSet(new long[]{0x0002000000000000L, 0x0000100000008100L});
    public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration467 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000008100L});
    public static final BitSet FOLLOW_72_in_normalClassDeclaration479 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_normalClassDeclaration481 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000008000L});
    public static final BitSet FOLLOW_79_in_normalClassDeclaration494 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_normalClassDeclaration496 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_classBody_in_normalClassDeclaration508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_typeParameters531 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters533 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_39_in_typeParameters536 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters538 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_52_in_typeParameters542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_typeParameter561 = new BitSet(new long[]{0x0000000000000002L, 0x0000000000000100L});
    public static final BitSet FOLLOW_72_in_typeParameter564 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_typeBound_in_typeParameter566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeBound595 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_typeBound598 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_typeBound600 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_ENUM_in_enumDeclaration621 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_enumDeclaration623 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000008000L});
    public static final BitSet FOLLOW_79_in_enumDeclaration626 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_enumDeclaration628 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_enumBody_in_enumDeclaration632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_enumBody651 = new BitSet(new long[]{0x0041008000008000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_enumConstants_in_enumBody653 = new BitSet(new long[]{0x0001008000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_enumBody656 = new BitSet(new long[]{0x0001000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody659 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_enumBody662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants681 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_enumConstants684 = new BitSet(new long[]{0x0040000000008000L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants686 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_annotations_in_enumConstant711 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_enumConstant714 = new BitSet(new long[]{0x0000000100000002L, 0x0000100000000000L});
    public static final BitSet FOLLOW_arguments_in_enumConstant716 = new BitSet(new long[]{0x0000000000000002L, 0x0000100000000000L});
    public static final BitSet FOLLOW_classBody_in_enumConstant719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_enumBodyDeclarations743 = new BitSet(new long[]{0x5843000000008102L, 0x00001644EE3C1446L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations746 = new BitSet(new long[]{0x5843000000008102L, 0x00001644EE3C1446L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_normalInterfaceDeclaration804 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_normalInterfaceDeclaration806 = new BitSet(new long[]{0x0002000000000000L, 0x0000100000000100L});
    public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration808 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000100L});
    public static final BitSet FOLLOW_72_in_normalInterfaceDeclaration812 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration814 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList841 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_typeList844 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_typeList846 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_108_in_classBody871 = new BitSet(new long[]{0x5843000000008100L, 0x00011644EE3C1446L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_classBody873 = new BitSet(new long[]{0x5843000000008100L, 0x00011644EE3C1446L});
    public static final BitSet FOLLOW_112_in_classBody876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_interfaceBody899 = new BitSet(new long[]{0x5843000000008100L, 0x00010644EE3C1446L});
    public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody901 = new BitSet(new long[]{0x5843000000008100L, 0x00010644EE3C1446L});
    public static final BitSet FOLLOW_112_in_interfaceBody904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_classBodyDeclaration923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_classBodyDeclaration933 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_classBodyDeclaration936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_classBodyDeclaration946 = new BitSet(new long[]{0x5042000000008100L, 0x00000200201C1046L});
    public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration948 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_genericMethodOrConstructorDecl_in_memberDecl971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberDeclaration_in_memberDecl981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_memberDecl991 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl993 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_voidMethodDeclaratorRest_in_memberDecl995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl1005 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_memberDecl1007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl1017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_memberDecl1027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_memberDeclaration1050 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_methodDeclaration_in_memberDeclaration1053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_memberDeclaration1057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_genericMethodOrConstructorDecl1077 = new BitSet(new long[]{0x5000000000008000L, 0x0000020020141042L});
    public static final BitSet FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl1079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_genericMethodOrConstructorRest1103 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_105_in_genericMethodOrConstructorRest1107 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest1110 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest1112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest1122 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest1124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_methodDeclaration1143 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_methodDeclaration1145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarators_in_fieldDeclaration1164 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_fieldDeclaration1166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceBodyDeclaration1193 = new BitSet(new long[]{0x5042000000008100L, 0x00000200201C1046L});
    public static final BitSet FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration1195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_interfaceBodyDeclaration1205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl1224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl1234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_interfaceMemberDecl1244 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMemberDecl1246 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl1248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceMemberDecl1258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_interfaceMemberDecl1268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_interfaceMethodOrFieldDecl1291 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMethodOrFieldDecl1293 = new BitSet(new long[]{0x0084000100000000L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl1295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest1318 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_interfaceMethodOrFieldRest1320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest1330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaratorRest1353 = new BitSet(new long[]{0x0081000000000000L, 0x0000102000000000L});
    public static final BitSet FOLLOW_55_in_methodDeclaratorRest1356 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_methodDeclaratorRest1358 = new BitSet(new long[]{0x0081000000000000L, 0x0000102000000000L});
    public static final BitSet FOLLOW_101_in_methodDeclaratorRest1371 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaratorRest1373 = new BitSet(new long[]{0x0001000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_methodBody_in_methodDeclaratorRest1389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_methodDeclaratorRest1403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidMethodDeclaratorRest1436 = new BitSet(new long[]{0x0001000000000000L, 0x0000102000000000L});
    public static final BitSet FOLLOW_101_in_voidMethodDeclaratorRest1439 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest1441 = new BitSet(new long[]{0x0001000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_methodBody_in_voidMethodDeclaratorRest1457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_voidMethodDeclaratorRest1471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest1504 = new BitSet(new long[]{0x0081000000000000L, 0x0000002000000000L});
    public static final BitSet FOLLOW_55_in_interfaceMethodDeclaratorRest1507 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_interfaceMethodDeclaratorRest1509 = new BitSet(new long[]{0x0081000000000000L, 0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_interfaceMethodDeclaratorRest1514 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest1516 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_interfaceMethodDeclaratorRest1520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_interfaceGenericMethodDecl1543 = new BitSet(new long[]{0x5000000000008000L, 0x0000020020141042L});
    public static final BitSet FOLLOW_type_in_interfaceGenericMethodDecl1546 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_105_in_interfaceGenericMethodDecl1550 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_interfaceGenericMethodDecl1553 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl1563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest1586 = new BitSet(new long[]{0x0001000000000000L, 0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_voidInterfaceMethodDeclaratorRest1589 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest1591 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_voidInterfaceMethodDeclaratorRest1595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_constructorDeclaratorRest1618 = new BitSet(new long[]{0x0000000000000000L, 0x0000102000000000L});
    public static final BitSet FOLLOW_101_in_constructorDeclaratorRest1621 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1623 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_constructorBody_in_constructorDeclaratorRest1627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_constantDeclarator1646 = new BitSet(new long[]{0x0084000000000000L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclarator1648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1671 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_variableDeclarators1674 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1676 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_variableDeclarator1697 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_variableDeclarator1700 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator1702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1727 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_constantDeclaratorsRest1730 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_constantDeclarator_in_constantDeclaratorsRest1732 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_55_in_constantDeclaratorRest1754 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_constantDeclaratorRest1756 = new BitSet(new long[]{0x0084000000000000L});
    public static final BitSet FOLLOW_50_in_constantDeclaratorRest1760 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_constantDeclaratorRest1762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_variableDeclaratorId1785 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_variableDeclaratorId1788 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_variableDeclaratorId1790 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer1811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer1821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_arrayInitializer1848 = new BitSet(new long[]{0x500003310260D0C0L, 0x0003128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1851 = new BitSet(new long[]{0x0000008000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_arrayInitializer1854 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1856 = new BitSet(new long[]{0x0000008000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_arrayInitializer1861 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_arrayInitializer1868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_modifier1887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_modifier1897 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_modifier1907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_modifier1917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_modifier1927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_modifier1937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_modifier1947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_modifier1957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_modifier1967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_modifier1977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_modifier1987 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_modifier1997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_packageOrTypeName2016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_enumConstantName2035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_typeName2054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_type2068 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_type2071 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_type2073 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_type2080 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_type2083 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_type2085 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_Identifier_in_classOrInterfaceType2098 = new BitSet(new long[]{0x0002080000000002L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2100 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_classOrInterfaceType2104 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_classOrInterfaceType2106 = new BitSet(new long[]{0x0002080000000002L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2108 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_74_in_variableModifier2217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_variableModifier2227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_typeArguments2246 = new BitSet(new long[]{0x5020000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2248 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_39_in_typeArguments2251 = new BitSet(new long[]{0x5020000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2253 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_52_in_typeArguments2257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeArgument2280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_typeArgument2290 = new BitSet(new long[]{0x0000000000000002L, 0x0000000100000100L});
    public static final BitSet FOLLOW_set_in_typeArgument2293 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_typeArgument2301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2326 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_qualifiedNameList2329 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2331 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_32_in_formalParameters2352 = new BitSet(new long[]{0x5040000200008000L, 0x0000000020141442L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters2354 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_formalParameters2357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameterDecls2380 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_formalParameterDecls2382 = new BitSet(new long[]{0x0000100000008000L});
    public static final BitSet FOLLOW_formalParameterDeclsRest_in_formalParameterDecls2384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2407 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_formalParameterDeclsRest2410 = new BitSet(new long[]{0x5040000000008000L, 0x0000000020141442L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameterDeclsRest2412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_formalParameterDeclsRest2424 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_methodBody2449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_constructorBody2468 = new BitSet(new long[]{0x784303310260D1D0L, 0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_constructorBody2470 = new BitSet(new long[]{0x784103310260D1D0L, 0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_blockStatement_in_constructorBody2473 = new BitSet(new long[]{0x784103310260D1D0L, 0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_112_in_constructorBody2476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2495 = new BitSet(new long[]{0x0000000000000000L, 0x0000000900000000L});
    public static final BitSet FOLLOW_set_in_explicitConstructorInvocation2498 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation2506 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_explicitConstructorInvocation2508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_explicitConstructorInvocation2518 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_43_in_explicitConstructorInvocation2520 = new BitSet(new long[]{0x0002000000000000L, 0x0000000100000000L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2522 = new BitSet(new long[]{0x0000000000000000L, 0x0000000100000000L});
    public static final BitSet FOLLOW_96_in_explicitConstructorInvocation2525 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation2527 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_explicitConstructorInvocation2529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName2549 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_qualifiedName2552 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName2554 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_integerLiteral_in_literal2580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FloatingPointLiteral_in_literal2590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CharacterLiteral_in_literal2600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_literal2610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanLiteral_in_literal2620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_literal2630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_annotations2719 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_annotation2739 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_annotationName_in_annotation2741 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_32_in_annotation2745 = new BitSet(new long[]{0x504003330260D0C0L, 0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValuePairs_in_annotation2749 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_elementValue_in_annotation2753 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_annotation2758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationName2782 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_annotationName2785 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_annotationName2787 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs2808 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_elementValuePairs2811 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs2813 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_Identifier_in_elementValuePair2834 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_elementValuePair2836 = new BitSet(new long[]{0x504003310260D0C0L, 0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair2838 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_elementValue2861 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_elementValue2871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue2881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_elementValueArrayInitializer2904 = new BitSet(new long[]{0x504003B10260D0C0L, 0x0003128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2907 = new BitSet(new long[]{0x0000008000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_elementValueArrayInitializer2910 = new BitSet(new long[]{0x504003310260D0C0L, 0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2912 = new BitSet(new long[]{0x0000008000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_elementValueArrayInitializer2919 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_elementValueArrayInitializer2923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_annotationTypeDeclaration2946 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_annotationTypeDeclaration2948 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_annotationTypeDeclaration2950 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration2952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_annotationTypeBody2975 = new BitSet(new long[]{0x5840000000008100L, 0x00010444EE3C1446L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody2978 = new BitSet(new long[]{0x5840000000008100L, 0x00010444EE3C1446L});
    public static final BitSet FOLLOW_112_in_annotationTypeBody2982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationTypeElementDeclaration3005 = new BitSet(new long[]{0x5040000000008100L, 0x00000000201C1046L});
    public static final BitSet FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration3007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_annotationTypeElementRest3030 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest3032 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3034 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_annotationTypeElementRest3044 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementRest3057 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementRest3070 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest3083 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest3109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest3119 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationMethodRest3142 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_annotationMethodRest3144 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_annotationMethodRest3146 = new BitSet(new long[]{0x0000000000000002L, 0x0000000000000010L});
    public static final BitSet FOLLOW_defaultValue_in_annotationMethodRest3148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarators_in_annotationConstantRest3172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_defaultValue3195 = new BitSet(new long[]{0x504003310260D0C0L, 0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_defaultValue3197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_block3218 = new BitSet(new long[]{0x784103310260D1D0L, 0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_blockStatement_in_block3220 = new BitSet(new long[]{0x784103310260D1D0L, 0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_112_in_block3223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement3246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement3256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement3266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3290 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_localVariableDeclarationStatement3292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_localVariableDeclaration3311 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration3313 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclarators_in_localVariableDeclaration3315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_variableModifiers3338 = new BitSet(new long[]{0x0040000000000002L, 0x0000000000000400L});
    public static final BitSet FOLLOW_block_in_statement3356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement3366 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3368 = new BitSet(new long[]{0x0001800000000000L});
    public static final BitSet FOLLOW_47_in_statement3371 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3373 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_statement3387 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3389 = new BitSet(new long[]{0x700103310260D0D0L, 0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3391 = new BitSet(new long[]{0x0000000000000002L, 0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_statement3401 = new BitSet(new long[]{0x700103310260D0D0L, 0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_statement3415 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_statement3417 = new BitSet(new long[]{0x504103310260D0C0L, 0x0002028920D41642L});
    public static final BitSet FOLLOW_forControl_in_statement3419 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_statement3421 = new BitSet(new long[]{0x700103310260D0D0L, 0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_107_in_statement3433 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3435 = new BitSet(new long[]{0x700103310260D0D0L, 0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_statement3447 = new BitSet(new long[]{0x700103310260D0D0L, 0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3449 = new BitSet(new long[]{0x0000000000000000L, 0x0000080000000000L});
    public static final BitSet FOLLOW_107_in_statement3451 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3453 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_104_in_statement3465 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3467 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000801L});
    public static final BitSet FOLLOW_catches_in_statement3479 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_statement3481 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_statement3495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_statement3509 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_statement3531 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3533 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_108_in_statement3535 = new BitSet(new long[]{0x8000000000000000L, 0x0001000000000010L});
    public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement3537 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_statement3539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_statement3549 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3551 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_statement3563 = new BitSet(new long[]{0x500103310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3565 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_statement3578 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3580 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_statement3592 = new BitSet(new long[]{0x0001000000008000L});
    public static final BitSet FOLLOW_Identifier_in_statement3594 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_statement3607 = new BitSet(new long[]{0x0001000000008000L});
    public static final BitSet FOLLOW_Identifier_in_statement3609 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_statement3622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExpression_in_statement3633 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_statement3645 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_statement3647 = new BitSet(new long[]{0x700103310260D0D0L, 0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_catches3672 = new BitSet(new long[]{0x0000000000000002L, 0x0000000000000001L});
    public static final BitSet FOLLOW_catchClause_in_catches3675 = new BitSet(new long[]{0x0000000000000002L, 0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_catchClause3700 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_catchClause3702 = new BitSet(new long[]{0x5040000000008000L, 0x0000000020141442L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause3704 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_catchClause3706 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameter3727 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_formalParameter3729 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameter3731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3759 = new BitSet(new long[]{0x8000000000000002L, 0x0000000000000010L});
    public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup3786 = new BitSet(new long[]{0xF84103310260D1D2L, 0x00021B9FFEDC767EL});
    public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup3789 = new BitSet(new long[]{0x784103310260D1D2L, 0x00021B9FFEDC766EL});
    public static final BitSet FOLLOW_63_in_switchLabel3813 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_constantExpression_in_switchLabel3815 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_switchLabel3817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_switchLabel3827 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_enumConstantName_in_switchLabel3829 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_switchLabel3831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_switchLabel3841 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_switchLabel3843 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enhancedForControl_in_forControl3874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forInit_in_forControl3884 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_forControl3887 = new BitSet(new long[]{0x500103310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_forControl3889 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_forControl3892 = new BitSet(new long[]{0x500003310260D0C2L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_forUpdate_in_forControl3894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_forInit3914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forInit3924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_enhancedForControl3947 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_enhancedForControl3949 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_enhancedForControl3951 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_enhancedForControl3953 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_enhancedForControl3955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forUpdate3974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_parExpression3995 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_parExpression3997 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_parExpression3999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionList4022 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_expressionList4025 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_expressionList4027 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_expression_in_statementExpression4048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_constantExpression4071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_expression4094 = new BitSet(new long[]{0x0416444890000002L, 0x0000400000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_expression4097 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_expression4099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_assignmentOperator4134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_assignmentOperator4144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_assignmentOperator4154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_assignmentOperator4164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_assignmentOperator4174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_110_in_assignmentOperator4184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_assignmentOperator4194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_assignmentOperator4204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_assignmentOperator4225 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_assignmentOperator4229 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4267 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4271 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4275 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4310 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4314 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4318 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression4347 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_53_in_conditionalExpression4351 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_conditionalExpression_in_conditionalExpression4353 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_conditionalExpression4355 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_conditionalExpression_in_conditionalExpression4357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression4379 = new BitSet(new long[]{0x0000000000000002L, 0x0000800000000000L});
    public static final BitSet FOLLOW_111_in_conditionalOrExpression4383 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression4385 = new BitSet(new long[]{0x0000000000000002L, 0x0000800000000000L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4407 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_conditionalAndExpression4411 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4413 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4435 = new BitSet(new long[]{0x0000000000000002L, 0x0000200000000000L});
    public static final BitSet FOLLOW_109_in_inclusiveOrExpression4439 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4441 = new BitSet(new long[]{0x0000000000000002L, 0x0000200000000000L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression4463 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_57_in_exclusiveOrExpression4467 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression4469 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression4491 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_andExpression4495 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression4497 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression4519 = new BitSet(new long[]{0x0008000004000002L});
    public static final BitSet FOLLOW_set_in_equalityExpression4523 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression4531 = new BitSet(new long[]{0x0008000004000002L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4553 = new BitSet(new long[]{0x0000000000000002L, 0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_instanceOfExpression4556 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_instanceOfExpression4558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression4579 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression4583 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression4585 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_49_in_relationalOp4620 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_relationalOp4624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_relationalOp4654 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_relationalOp4658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_relationalOp4679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_relationalOp4690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression4710 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_shiftOp_in_shiftExpression4714 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression4716 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_49_in_shiftOp4747 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_shiftOp4751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_shiftOp4783 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_shiftOp4787 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_shiftOp4791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_shiftOp4821 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_shiftOp4825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4855 = new BitSet(new long[]{0x0000011000000002L});
    public static final BitSet FOLLOW_set_in_additiveExpression4859 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4867 = new BitSet(new long[]{0x0000011000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4889 = new BitSet(new long[]{0x0000200408000002L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression4893 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4907 = new BitSet(new long[]{0x0000200408000002L});
    public static final BitSet FOLLOW_36_in_unaryExpression4933 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression4935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_unaryExpression4945 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression4947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_unaryExpression4957 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression4959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_unaryExpression4969 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression4971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression4981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_113_in_unaryExpressionNotPlusMinus5000 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_unaryExpressionNotPlusMinus5012 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus5024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus5034 = new BitSet(new long[]{0x00800A2000000002L});
    public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus5036 = new BitSet(new long[]{0x00800A2000000002L});
    public static final BitSet FOLLOW_32_in_castExpression5062 = new BitSet(new long[]{0x5000000000000000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression5064 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_castExpression5066 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_castExpression5068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_castExpression5077 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_type_in_castExpression5080 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_expression_in_castExpression5084 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_castExpression5087 = new BitSet(new long[]{0x500000010260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression5089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary5108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_primary5118 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_43_in_primary5121 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_primary5123 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary5127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_primary5138 = new BitSet(new long[]{0x0000080100000000L});
    public static final BitSet FOLLOW_superSuffix_in_primary5140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primary5150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_primary5160 = new BitSet(new long[]{0x5002000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_creator_in_primary5162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_primary5172 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_43_in_primary5175 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_primary5177 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary5181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_primary5192 = new BitSet(new long[]{0x0080080000000000L});
    public static final BitSet FOLLOW_55_in_primary5195 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_primary5197 = new BitSet(new long[]{0x0080080000000000L});
    public static final BitSet FOLLOW_43_in_primary5201 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_primary5203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_primary5213 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_43_in_primary5215 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_primary5217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_identifierSuffix5237 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_identifierSuffix5239 = new BitSet(new long[]{0x0080080000000000L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5243 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_identifierSuffix5245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_identifierSuffix5256 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_identifierSuffix5258 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_identifierSuffix5260 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix5273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5283 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_identifierSuffix5285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5295 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_explicitGenericInvocation_in_identifierSuffix5297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5307 = new BitSet(new long[]{0x0000000000000000L, 0x0000000800000000L});
    public static final BitSet FOLLOW_99_in_identifierSuffix5309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5319 = new BitSet(new long[]{0x0000000000000000L, 0x0000000100000000L});
    public static final BitSet FOLLOW_96_in_identifierSuffix5321 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix5323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5333 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_identifierSuffix5335 = new BitSet(new long[]{0x0002000000008000L});
    public static final BitSet FOLLOW_innerCreator_in_identifierSuffix5337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator5356 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_createdName_in_creator5358 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator5360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createdName_in_creator5370 = new BitSet(new long[]{0x0080000100000000L});
    public static final BitSet FOLLOW_arrayCreatorRest_in_creator5373 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator5377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_createdName5397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_createdName5407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_innerCreator5430 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_innerCreator5433 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_classCreatorRest_in_innerCreator5435 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5454 = new BitSet(new long[]{0x510003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5468 = new BitSet(new long[]{0x0080000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5471 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5473 = new BitSet(new long[]{0x0080000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_arrayInitializer_in_arrayCreatorRest5477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest5491 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5493 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5496 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest5498 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5500 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5505 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5507 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_arguments_in_classCreatorRest5538 = new BitSet(new long[]{0x0000000000000002L, 0x0000100000000000L});
    public static final BitSet FOLLOW_classBody_in_classCreatorRest5540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation5564 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_explicitGenericInvocation5566 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_explicitGenericInvocation5568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_nonWildcardTypeArguments5591 = new BitSet(new long[]{0x5000000000008000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments5593 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_nonWildcardTypeArguments5595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5618 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_selector5620 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_arguments_in_selector5622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5633 = new BitSet(new long[]{0x0000000000000000L, 0x0000000800000000L});
    public static final BitSet FOLLOW_99_in_selector5635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5645 = new BitSet(new long[]{0x0000000000000000L, 0x0000000100000000L});
    public static final BitSet FOLLOW_96_in_selector5647 = new BitSet(new long[]{0x0000080100000000L});
    public static final BitSet FOLLOW_superSuffix_in_selector5649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5659 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_selector5661 = new BitSet(new long[]{0x0002000000008000L});
    public static final BitSet FOLLOW_innerCreator_in_selector5663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_selector5673 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_selector5675 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_selector5677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix5700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_superSuffix5710 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_superSuffix5712 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix5714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_arguments5734 = new BitSet(new long[]{0x500003330260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expressionList_in_arguments5736 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_arguments5739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_synpred5_Java59 = new BitSet(new long[]{0x0840000000000100L, 0x00000000CF080404L});
    public static final BitSet FOLLOW_packageDeclaration_in_synpred5_Java73 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE090404L});
    public static final BitSet FOLLOW_importDeclaration_in_synpred5_Java75 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE090404L});
    public static final BitSet FOLLOW_typeDeclaration_in_synpred5_Java78 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE080404L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred5_Java93 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE080404L});
    public static final BitSet FOLLOW_typeDeclaration_in_synpred5_Java95 = new BitSet(new long[]{0x0841000000000102L, 0x00000000CE080404L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred113_Java2470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred117_Java2495 = new BitSet(new long[]{0x0000000000000000L, 0x0000000900000000L});
    public static final BitSet FOLLOW_set_in_synpred117_Java2498 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_synpred117_Java2506 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_synpred117_Java2508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_synpred128_Java2719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_synpred151_Java3246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred152_Java3256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_synpred157_Java3401 = new BitSet(new long[]{0x700103310260D0D0L, 0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_synpred157_Java3403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred162_Java3479 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_synpred162_Java3481 = new BitSet(new long[]{0x0000000000000000L, 0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_synpred162_Java3483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred163_Java3495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchLabel_in_synpred178_Java3786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_synpred180_Java3813 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_constantExpression_in_synpred180_Java3815 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_synpred180_Java3817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_synpred181_Java3827 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_enumConstantName_in_synpred181_Java3829 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_synpred181_Java3831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enhancedForControl_in_synpred182_Java3874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_synpred186_Java3914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_synpred198_Java4215 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred198_Java4217 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred198_Java4219 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred199_Java4255 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred199_Java4257 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred199_Java4259 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred199_Java4261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred200_Java4300 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred200_Java4302 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred200_Java4304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_synpred211_Java4612 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred211_Java4614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred212_Java4646 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred212_Java4648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_synpred215_Java4739 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred215_Java4741 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred216_Java4773 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred216_Java4775 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred216_Java4777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred217_Java4813 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred217_Java4815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_synpred229_Java5024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_synpred233_Java5062 = new BitSet(new long[]{0x5000000000000000L, 0x0000000020141042L});
    public static final BitSet FOLLOW_primitiveType_in_synpred233_Java5064 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_synpred233_Java5066 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_synpred233_Java5068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_synpred234_Java5080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_synpred236_Java5121 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_synpred236_Java5123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred237_Java5127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_synpred242_Java5175 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_synpred242_Java5177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred243_Java5181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_synpred249_Java5256 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_synpred249_Java5258 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_synpred249_Java5260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_synpred262_Java5496 = new BitSet(new long[]{0x500003310260D0C0L, 0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_synpred262_Java5498 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_synpred262_Java5500 = new BitSet(new long[]{0x0000000000000002L});

}