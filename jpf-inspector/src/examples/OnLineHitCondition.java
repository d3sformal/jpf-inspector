import gov.nasa.jpf.inspector.common.pse.PSEVariablePrimitive;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.interfaces.CustomHitCondition;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

public class OnLineHitCondition implements CustomHitCondition {
  @Override
  public String getName() {
    return "position";
  }

  @Override
  public boolean isHit(InspectorState state, Object[] arguments) {
    if (state.getListenerMethod() != InspectorState.ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    Object arg1 = arguments[0];
    PSEVariablePrimitive programStateEntry = (PSEVariablePrimitive)arg1;
    Integer integer = (Integer) programStateEntry.getWrappedValue();

    // Hits whenever the next instruction to be executed has the line number at the first parameter.
    return state.getVM().getInstruction().getLineNumber() == integer;
  }

  @Override
  public String getDetails(InspectorState state, Object[] arguments) {
    return "These are details of the example custom hit condition.";
  }
}
