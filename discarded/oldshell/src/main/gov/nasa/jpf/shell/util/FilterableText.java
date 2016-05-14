package gov.nasa.jpf.shell.util;

/**
 * Holds some text and attaches it to an owner.
 *
 */
public class FilterableText {

	private final String text;
  private final Object owner;

  public FilterableText(Object owner, String txt){
    this.text = txt;
    this.owner = owner;
  }


  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  public Object getOwner() {
    return owner;
  }
}
