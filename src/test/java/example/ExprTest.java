package example;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * http://www.antlr.org/wiki/display/ANTLR3/Expression+evaluator
 */
public class ExprTest {

    @Test
    public void success() throws Exception {
        CharStream cs = new ANTLRStringStream("1 + (2+3) * 4");
        ExprLexer lexer = new ExprLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream();
        tokens.setTokenSource(lexer);
        ExprParser parser = new ExprParser(tokens);
        int result = parser.expr();
        assertThat(parser.failed(), is(false));
        assertThat(parser.getNumberOfSyntaxErrors(), is(equalTo(0)));
        assertThat(result, is(equalTo(21)));
    }

    @Test
    public void increment() throws Exception {
        CharStream cs = new ANTLRStringStream("1++");
        ExprLexer lexer = new ExprLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream();
        tokens.setTokenSource(lexer);
        ExprParser parser = new ExprParser(tokens);
        parser.expr();
        assertThat(parser.failed(), is(false));
        assertThat(parser.getNumberOfSyntaxErrors(), is(equalTo(2)));
    }

}
