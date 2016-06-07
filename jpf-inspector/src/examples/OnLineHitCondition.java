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

    // Hits whenever the next instruction to be executed has the line number 5.
    return state.getVM().getInstruction().getLineNumber() == 5;
  }
}
