package gov.nasa.jpf.inspector.tests.cmd2text;

import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface;
import gov.nasa.jpf.inspector.server.expression.ExpressionParser;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.tests.infrastructure.Breakpoint;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTest;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestCallbacks;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestDriver;
import gov.nasa.jpf.util.TypeRef;

import java.io.PrintStream;

import org.junit.Test;

public class NormalizedExpressionParsingTest extends InspectorTest {

  private static final String breakpointFieldAccessExprs[] = {
    "   field_access =   MyClass : fw  ",
    "field=My*:x",
    "fa=field:DEBUG",
    "field_read=fw_fw3 : r42",
    "fr          =*:init5",
    "field_write =   *x*:field_write",
    "fw=field_access   : a12345 " };

  @Test
  public void testBreakpointFieldAccess () {
    testCommands(breakpointFieldAccessExprs);
  }

  private static final String breakpointGarbageCollectionExprs[] = {
    "   garbage_collection =   both  ",
    "gc=begin",
    "  gc     =end", };

  @Test
  public void testBreakpointGarbageCollection () {
    testCommands(breakpointGarbageCollectionExprs);
  }

  private static final String breakpointChoiceGeneratorExprs[] = {
    "   choice_generator =   both  ",
    "cg=data",
    "  cg     =sched", };

  @Test
  public void testBreakpointChoiceGenerator () {
    testCommands(breakpointChoiceGeneratorExprs);
  }

  private static final String breakpointInstructionTypeExprs[] = {
    "   instruction_type =   any  ",
    "it=none",
    "  inst_type     =invoke",
    "  it=inv",
    "  it=ret",
    "  it=return",
    "  it=field_access",
    "  it=field",
    "  it=fa",
    "  it=field_read",
    "  it=fr",
    "  it=field_write",
    "  it=fw",
    "  it=condition",
    "  it=cond",
    "  it=lock",
    "  it=array", };

  @Test
  public void testBreakpointInstructionType () {
    testCommands(breakpointInstructionTypeExprs);
  }

  private static final String breakpointMethodInvokeExprs[] = {
    "   method_invoke = Agent   : Alf009   ",
    "mi=mil.pentagon.target.Main:hit",
    "mi=pos:pos",
    "mi=mi:mi",
    "mi=field_access.fr:field_access",
    "mi=cg.both.cgdata.Alf:garbage_collection",
    "mi=cg.both.cgdata.Cg$Runner:garbage_collection", };

  private static final String breakpointOperatorsExprs[] = {
    "  pv and pv",
    "pv or pv",
    "it=cond and(mi=pos:pos)",
    "((it=cond)and(mi=pos:pos)) or pv", };

  @Test
  public void testBreakpointOperators () {
    testCommands(breakpointOperatorsExprs);
  }

  @Test
  public void testBreakpointMethodInvoke () {
    testCommands(breakpointMethodInvokeExprs);
  }

  private static final String breakpointPositionExprs[] = {
    "   position = Alf:009   ",
    "pos=gov/nasa/jpf/inspector/JPFInspector    :  13",
    "pos=gc:1",
    "pos=pos:1",
    "pos=field_write:2",
    "pos=it:3",
    "pos=inv:4",
    "pos=invit:5", };

  @Test
  public void testBreakpointPosition () {
    testCommands(breakpointPositionExprs);
  }

  private static final String breakpointPropertyViolatedExprs[] = {
    "   property_violated  ",
    "pv", };

  @Test
  public void testBreakpointPropertyViolated () {
    testCommands(breakpointPropertyViolatedExprs);
  }

  private static final String breakpointSpecificClassExprs[] = {
    "   object_created = cia.Bond007  ",
    " objc = objc.object_created.object_released.Class.print",
    " object_released = thread.this$MyClass",
    "objr=pv.property_violated.mi.array$none",
    "exception_thrown=java.lang.RuntimeException",
    "et=et.notify.block.me.Now" };

  @Test
  public void testBreakpointSpecificClass () {
    testCommands(breakpointSpecificClassExprs);
  }

  private static final String breakpointStateAdvancedExpr[] = {
    "state_advanced",
    "sa", };

  @Test
  public void testBreakpointStateAdvanced () {
    testCommands(breakpointStateAdvancedExpr);
  }

  private static final String breakpointStepOutExpr[] = {
    "   step_out thread=0 stack_frame=2  ",
    "sout ti=0 sf=1", };

  @Test
  public void testBreakpointStepOut () {
    testCommands(breakpointStepOutExpr);
  }

  private static final String breakpointThreadScheduledExprs[] = {
    "   thread_scheduled = both : 0 ",
    "ts=in:1",
    "ts=out", };

  @Test
  public void testBreakpointThreadScheduled () {
    testCommands(breakpointThreadScheduledExprs);
  }

  private static final String breakpointSingleStepExprs[] = {
    " si ",
    "so",
    "step_in",
    "step_over " };

  @Test
  public void testBreakpointSingleStep () {
    testCommands(breakpointSingleStepExprs);
  }

  private static final String breakpointInstructionExprs[] = {
    " specific_instruction thread=1 instruction=" + Breakpoint.class.getName() + ":breakMe:1 hit_count=5 ",
    " specific_instruction thread = 1 instruction = " + Breakpoint.class.getName() + " : breakMe : 1 hit_count = 5 ",
    "specific_instruction ti=5 inst=" + Breakpoint.class.getName() + ":breakMe:1 hc=3", };

  @Test
  public void testBreakpointInstruction () {
    testCommands(breakpointInstructionExprs);
  }

  static public class NormalizedExpressionParsingTestDriver extends InspectorTestDriver {
    private static final boolean DEBUG = true;

    private final String[] exprs;
    private final PrintStream out;

    public NormalizedExpressionParsingTestDriver (JPFInspector inspector, InspectorTestCallbacks callbacks, InspectorTest test, String[] exprs) {
      super(inspector, callbacks, test);
      this.exprs = exprs;
      this.out = inspector.getDebugPrintStream();
    }

    @Override
    public void driveInspector () throws JPFInspectorException {

      try {
        inspector.createBreakPoint(Breakpoint.getBreakpoint());
      } catch (JPFInspectorException e) {
        fail(e);
      }
      callbacks.waitForCB_BreakpointHit();
      callbacks.nextCB_StateChange(InspectorStates.JPF_STOPPED);

      ExpressionParser parser = new ExpressionParser(inspector);

      for (int i = 0; i < exprs.length; i++) {
        try {
          String origExpr = exprs[i];

          if (DEBUG) {
            out.println("Parsing \"" + origExpr + "\"");
          }

          ExpressionBooleanInterface exprBool = parser.getBreakpointExpression(origExpr);

          if (exprBool == null) {
            fail("Initial boolean breakpoint expression has not been parsed successfully: " + origExpr);
          }

          if (DEBUG) {
            out.println("\t\t ... OK");
          }

          String normalizedExpression = exprBool.getNormalizedExpression();
          if (DEBUG) {
            out.println("\tNormalized command version \"" + normalizedExpression + "\"");
          }

          ExpressionBooleanInterface normalizedExprBool = parser.getBreakpointExpression(normalizedExpression);

          if (normalizedExprBool == null) {
            fail("Normalized boolean breakpoint expression has not been parsed successfully: " + origExpr);

          }
          if (DEBUG) {
            out.println("\t\t ... OK");
          }
        } catch (JPFInspectorException e) {
          fail(e);
        }
      }

      // Terminate the test
      callbacks.nextCB_Resume_and_StateChange(InspectorStates.JPF_RUNNING);
      callbacks.nextCB_StateChange(InspectorStates.JPF_TERMINATING);
      callbacks.finishCallback();
    }

  }

  protected void testCommands (String[] exprs) {
    if (runInspectorTest(
        new TypeRef("gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestCallbacks"),
        new TypeRef("gov.nasa.jpf.inspector.tests.cmd2text.NormalizedExpressionParsingTest$NormalizedExpressionParsingTestDriver"),
        new Class<?>[] { String[].class },
        new Object[] { exprs },
        2)) {
      // JPF Test
      Breakpoint.breakMe();
      return;
    }

    // Test is done in the test driver

  }

  public static void main (String[] testMethods) {
    runTestsOfThisClass(testMethods);
  }
}
