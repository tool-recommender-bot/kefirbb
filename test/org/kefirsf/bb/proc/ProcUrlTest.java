package org.kefirsf.bb.proc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kefirsf.bb.UrlCollection;

import static org.junit.Assert.*;

/**
 * Tests for a URL processor pattern element.
 *
 * @author kefir
 */
@RunWith(Parameterized.class)
public class ProcUrlTest extends AbstractProcUrlTest {
    @Parameterized.Parameters(name = "{0}")
    public static String[] urlCollection() {
        return UrlCollection.VALID;
    }

    @Parameterized.Parameter
    public String url;

    @Test
    public void testParse() throws NestingException {
        Source source = prepareSource();

        Context context = new Context();
        context.setSource(source);

        ProcUrl norm = new ProcUrl("a", false, false, false);
        ProcUrl ghost = new ProcUrl("b", true, false, false);

        assertFalse(norm.parse(context, null));
        assertFalse(ghost.parse(context, null));

        source.setOffset(PREFIX.length());
        assertTrue(ghost.parse(context, null));
        assertEquals(PREFIX.length(), source.getOffset());
        assertTrue(norm.parse(context, null));
        assertEquals(source.length() - SUFFIX.length(), source.getOffset());
    }

    @Test
    public void testParseTerminator() throws NestingException {
        Context context = prepareContext();
        Source source = context.getSource();
        PatternConstant terminator = new PatternConstant(SUFFIX, false);

        ProcUrl norm = new ProcUrl("a", false, false, false);
        ProcUrl ghost = new ProcUrl("b", true, false, false);

        assertFalse(norm.parse(context, terminator));
        assertFalse(ghost.parse(context, terminator));

        source.setOffset(PREFIX.length());
        assertTrue(ghost.parse(context, terminator));
        assertEquals(PREFIX.length(), source.getOffset());
        assertTrue(norm.parse(context, terminator));
        assertEquals(source.length() - SUFFIX.length(), source.getOffset());
    }

    @Test
    public void testIsNextIn() {
        Context context = prepareContext();
        Source source = context.getSource();
        ProcUrl element = new ProcUrl("a", false, false, false);

        assertFalse(element.isNextIn(context));
        source.setOffset(PREFIX.length() - 1);
        assertFalse(element.isNextIn(context));
        source.setOffset(PREFIX.length());
        assertTrue(element.isNextIn(context));
        source.setOffset(source.length() - SUFFIX.length());
        assertFalse(element.isNextIn(context));
    }

    @Test
    public void testFindIn() {
        Source source = prepareSource();
        ProcUrl element = new ProcUrl("a", false, false, false);

        assertEquals(PREFIX.length(), element.findIn(source));
        source.setOffset(PREFIX.length() - 1);
        assertEquals(PREFIX.length(), element.findIn(source));
        source.setOffset(source.length() - SUFFIX.length());
        assertEquals(-1, element.findIn(source));
    }

    @Override
    protected String getValue() {
        return url;
    }
}
