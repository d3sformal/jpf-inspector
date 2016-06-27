package gov.nasa.jpf.inspector.frontends.swing.explorer;

public class Attachment {
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
    return new Attachment(name, AttachmentKind.NONE);
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

  public enum AttachmentKind {
    NONE,
    INSTANCE_FIELD,
    STATIC_FIELD,
    ARRAY_ELEMENT,
    STACK_FRAME,
    TOPMOST_STACK_FRAME,
    HEAP_ENTRY
  }
}
