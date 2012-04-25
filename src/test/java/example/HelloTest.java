package example;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * http://d.hatena.ne.jp/momijiame/20120221/1329834078
 */
public class HelloTest {

    @Test
    public void success() throws Exception {
        CharStream cs = new ANTLRStringStream("Hello, World");
        HelloLexer lexer = new HelloLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream();
        tokens.setTokenSource(lexer);
        HelloParser parser = new HelloParser(tokens);
        parser.greet();
        assertThat(parser.getNumberOfSyntaxErrors(), is(equalTo(0)));
    }

    @Test
    public void hasErrors() throws Exception {
        CharStream cs = new ANTLRStringStream("Hello World!");
        HelloLexer lexer = new HelloLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream();
        tokens.setTokenSource(lexer);
        HelloParser parser = new HelloParser(tokens);
        parser.greet();
        assertThat(parser.getNumberOfSyntaxErrors(), is(equalTo(1)));
    }

}
