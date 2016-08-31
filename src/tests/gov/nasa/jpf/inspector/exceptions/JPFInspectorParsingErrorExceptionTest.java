package gov.nasa.jpf.inspector.exceptions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link JPFInspectorParsingErrorException}.
 */
public class JPFInspectorParsingErrorExceptionTest {
  @Test
  public void testExpressError() {
    JPFInspectorParsingErrorException ex;

    // "Position 0 - char 0 should be marked");
    ex = new JPFInspectorParsingErrorException(null, "01234567890123456789", 0);
    Assert.assertEquals("01234...\n" +
                                "^", ex.expressError(8));
    Assert.assertEquals("0123456...\n" +
                                "^", ex.expressError(10));
    Assert.assertEquals("01234567890123456789\n" +
                                "^", ex.expressError(20));
    Assert.assertEquals("01234567890123456789\n" +
                                "^", ex.expressError(30));

    // "Position 2 - char 2 should be marked");
    ex = new JPFInspectorParsingErrorException(null, "01234567890123456789", 2);
    Assert.assertEquals("01234...\n" +
                                "  ^", ex.expressError(8));
    Assert.assertEquals("0123456...\n" +
                                "  ^", ex.expressError(10));
    Assert.assertEquals("01234567890123456789\n" +
                                "  ^", ex.expressError(20));
    Assert.assertEquals("01234567890123456789\n" +
                                "  ^", ex.expressError(30));

    // "Position 40 - char 2 (in 21) should be marked");
    ex = new JPFInspectorParsingErrorException(null, "01020304050607080910111213141516171819202122232425262728293031323334353637383940", 40);
    Assert.assertEquals("0102030405\n" +
                                "0607080910\n" +
                                "1112131415\n" +
                                "1617181920\n" +
                                "2122232...\n" +
                                "^", ex.expressError(10));
    Assert.assertEquals("01020304050607080910\n" +
                                "11121314151617181920\n" +
                                "21222324252627282...\n" +
                                "^", ex.expressError(20));
    Assert.assertEquals("0102030405060708091011121314151617181920\n" +
                                "2122232425262728293031323334353637383940\n" +
                                "^", ex.expressError(40));
    Assert.assertEquals("010203040506070809101\n" +
                                "112131415161718192021\n" +
                                "222...             ^", ex.expressError(21));

    // "Position -1 - no ^ should be shown and all input should be printed");
    ex = new JPFInspectorParsingErrorException(null, "01020304050607080910111213141516171819202122232425262728293031323334353637383940", -1);
    Assert.assertEquals("01020304050607080910111213141516171819202122232425262728293031323334353637383940", ex.expressError(10));

    ex = new JPFInspectorParsingErrorException(null, null, 5);
    Assert.assertEquals("", ex.expressError(50));
  }
}
