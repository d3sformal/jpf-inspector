package gov.nasa.jpf.inspector.frontends.swing.explorer;

public class AttachmentInformation {
  public final String name;
  public final AttachmentKind kind;

  @Override
  public boolean equals(Object obj) {
    return obj != null &&
            obj instanceof  AttachmentInformation &&
            ((AttachmentInformation)obj).name.equals(this.name) &&
            ((AttachmentInformation)obj).kind.equals(this.kind);
  }

  private AttachmentInformation(String name, AttachmentKind kind) {
    this.name = name;
    this.kind = kind;
  }
  public static AttachmentInformation name(String name) {
    return new AttachmentInformation(name, AttachmentKind.NONE);
  }
  public static AttachmentInformation instanceField(String name) {
    return new AttachmentInformation(name, AttachmentKind.INSTANCE_FIELD);
  }

  public static AttachmentInformation stackFrame(int index) {
    return new AttachmentInformation(Integer.toString(index), AttachmentKind.STACK_FRAME);
  }
  public static AttachmentInformation topmostStackFrame() {
    return new AttachmentInformation("Topmost Stack Frame", AttachmentKind.TOPMOST_STACK_FRAME);
  }

  public AttachmentKind getKind() {
    return kind;
  }

  public String getName() {
    return name;
  }

  public enum AttachmentKind {
    NONE,
    INSTANCE_FIELD,
    STACK_FRAME,
    TOPMOST_STACK_FRAME
  }
}
