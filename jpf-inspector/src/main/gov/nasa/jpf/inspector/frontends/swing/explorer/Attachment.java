package gov.nasa.jpf.inspector.frontends.swing.explorer;

/**
 * Represents a way in which an Explorer node is connected to its parent.
 *
 * This consists of its relationship ({@link AttachmentKind}) and name with respect
 * to the parent (for example, field name or array index).
 */
public final class Attachment {
  public final String name;
  public final AttachmentKind kind;

  @Override
  public boolean equals(Object obj) {
    return obj != null &&
            obj instanceof Attachment &&
            ((Attachment)obj).name.equals(this.name) &&
            ((Attachment)obj).kind.equals(this.kind);
  }

  private Attachment(String name, AttachmentKind kind) {
    this.name = name;
    this.kind = kind;
  }
  public static Attachment name(String name) {
    return new Attachment(name, AttachmentKind.UNSPECIFIED);
  }
  public static Attachment instanceField(String name) {
    return new Attachment(name, AttachmentKind.INSTANCE_FIELD);
  }

  public static Attachment stackFrame(int index) {
    return new Attachment(Integer.toString(index), AttachmentKind.STACK_FRAME);
  }
  public static Attachment topmostStackFrame() {
    return new Attachment("Topmost Stack Frame", AttachmentKind.TOPMOST_STACK_FRAME);
  }

  public AttachmentKind getKind() {
    return kind;
  }

  public String getName() {
    return name;
  }

  public static Attachment heapEntry(int objectRef) {
    return new Attachment("#" + objectRef, AttachmentKind.HEAP_ENTRY);
  }

  public static Attachment irrelevant() {
    return new Attachment("this does not matter", AttachmentKind.UNSPECIFIED);
  }

  public static Attachment stackSlot(String name) {
    return new Attachment(name, AttachmentKind.STACK_SLOT);
  }

  public static Attachment arrayElement(int index) {
    return new Attachment("["+ index + "]", AttachmentKind.ARRAY_ELEMENT);
  }

  public static Attachment staticAreaEntry(String typeName) {
    return new Attachment(typeName, AttachmentKind.STATIC_AREA_ENTRY);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + kind.hashCode();
    return result;
  }

  public static Attachment staticField(String name) {
    return new Attachment(name, AttachmentKind.STATIC_FIELD);
  }

  /**
   * Represents the relationship of an Explorer node to its parent.
   */
  public enum AttachmentKind {
    PARENTLESS,
    UNSPECIFIED,
    INSTANCE_FIELD,
    STATIC_FIELD,
    ARRAY_ELEMENT,
    STACK_FRAME,
    TOPMOST_STACK_FRAME,
    STACK_SLOT,
    STATIC_AREA_ENTRY,
    HEAP_ENTRY
  }
}
