package gov.nasa.jpf.inspector.tests.unit;

import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.inspector.utils.expressions.MethodName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.MethodInfo;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionNameMatching {
  @Test
  public void testTestingWorks() {
    Assert.assertTrue(true);
  }
  @Test
  public void testMethodName() {
    ClassInfo classInteger = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Integer");
    MethodInfo methodInteger = classInteger.getMethod("parseInt(String)", false);
    MethodName methodName = new MethodName("parseInt", new ClassName("java.lang.Integer"));
    Assert.assertEquals("parseInt", methodName.getMethodName());
    Assert.assertEquals("java.lang.Integer", methodName.getClassName());
    Assert.assertTrue(methodName.isSameMethod(methodInteger));
  }
}
