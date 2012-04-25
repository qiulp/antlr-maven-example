package example;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * http://www.antlr.org/wiki/display/ANTLR3/FAQ+-+Getting+Started
 * http://www.antlr.org/download/examples-v3.tar.gz
 */
public class JavaTest {

    @Test
    public void success() throws Exception {
        CharStream cs = new ANTLRStringStream("class Foo { public static void main(String[] args) throws Exception { } }");
        JavaLexer lexer = new JavaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream();
        tokens.setTokenSource(lexer);
        JavaParser parser = new JavaParser(tokens);
        parser.compilationUnit();
        assertThat(parser.getNumberOfSyntaxErrors(), is(equalTo(0)));
    }

    @Test
    public void increment() throws Exception {
        CharStream cs = new ANTLRStringStream("class Foo { public satic void main(String[] args) throws Exception { } }");
        JavaLexer lexer = new JavaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream();
        tokens.setTokenSource(lexer);
        JavaParser parser = new JavaParser(tokens);
        parser.compilationUnit();
        assertThat(parser.getNumberOfSyntaxErrors(), is(equalTo(1)));
    }

}
