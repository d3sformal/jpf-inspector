package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;

/**
 * Represents an array or an object without loaded elements or fields. Such an array or object is only intended
 * to be displayed to the user in its short form.
 */
public class PSEVariableShortForm extends PSEVariable {
  private static final long serialVersionUID = -2393412289253782599L;

  public PSEVariableShortForm(String varName, String varTypeName,
                              String varValue, boolean isStatic, String definedIn, int index) {
    super(varName, varTypeName, varValue, isStatic, definedIn, index);
  }

  @Override
  public <T> T visit(PSEVisitor<T> visitor) throws JPFInspectorException {
    throw new JPFInspectorGenericErrorException("Short-form variables may only be printed as part of a larger entry and not alone.");
  }
}
