package gov.nasa.jpf.inspector.tests.acceptance.legacy.simpleassignments;

import java.util.Map;

@SuppressWarnings("ALL")
public class SimpleAssignments {
  public static void main(String[] args) {
    SimpleAssignments simpleAssignments = new SimpleAssignments();
    simpleAssignments.testIt();
  }
  public void testIt() {
    // TODO make this test work
    boolean sa_slot_boolean = false;
    char sa_slot_char = 'f';
    byte sa_slot_byte = Byte.MIN_VALUE;
    short sa_slot_short = Short.MIN_VALUE;
    int sa_slot_int = Integer.MIN_VALUE;
    long sa_slot_long = Long.MIN_VALUE;

    float sa_slot_float = Float.NEGATIVE_INFINITY;
    double sa_slot_double = Double.NaN;

    String sa_slot_string = "Alf rulez";
    Object sa_slot_Object = new Object();

    boolean sa_array_boolean[] = {
            true,
            false };
    char sa_array_char[] = {
            '\u0000',
            '\uFFFF',
            'a',
            'l',
            'f' };
    byte sa_array_byte[] = {
            0,
            1,
            127 };
    short sa_array_short[] = {
            Short.MIN_VALUE,
            0,
            Short.MAX_VALUE };
    int sa_array_int[] = {
            Integer.MIN_VALUE,
            0,
            Integer.MAX_VALUE };
    long sa_array_long[] = {
            Long.MIN_VALUE,
            0,
            Long.MAX_VALUE };
    float sa_array_float[] = {
            Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY,
            Float.MIN_VALUE,
            (float) (-Math.PI),
            0.0f,
            (float) (Math.PI),
            Float.MAX_VALUE,
            Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY,
            Float.NaN,
            Float.NaN };

    double sa_array_double[] = {
            Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            Double.MIN_VALUE,
            -Math.E,
            0.0,
            Math.E,
            Double.MAX_VALUE,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.NaN,
            Double.NaN };

    Object sa_array_Object[] = { new Object() };
    Object sa_array2_String_String[][] = {
            {
                    "Java",
                    "PathFinder" },
            { "JPF-Shell" } };


    System.out.println("sa_field_boolean=" + sa_field_boolean);
    System.out.println("sa_field_char=" + sa_field_char);
    System.out.println("sa_field_byte=" + sa_field_byte);
    System.out.println("sa_field_short=" + sa_field_short);
    System.out.println("sa_field_int=" + sa_field_int);
    System.out.println("sa_field_long=" + sa_field_long);
    System.out.println("sa_field_float=" + sa_field_float);
    System.out.println("sa_field_double=" + sa_field_double);
    System.out.println("sa_field_double2=" + sa_field_double2);
    System.out.println("sa_field_string1=" + sa_field_string1);
    System.out.println("sa_field_string2=" + sa_field_string2);
    System.out.println("sa_field_string3=" + sa_field_string3);
    System.out.println("sa_field_Object=" + sa_field_Object);
    System.out.println("sa_field_StringBuffer=" + sa_field_StringBuffer);
    System.out.println("sa_field_Map=" + sa_field_Map);

    System.out.println("sa_field_box_test_boolean=" + sa_field_box_test_boolean);
    System.out.println("sa_field_box_test_char=" + sa_field_box_test_char);
    System.out.println("sa_field_box_test_byte=" + sa_field_box_test_byte);
    System.out.println("sa_field_box_test_short=" + sa_field_box_test_short);
    System.out.println("sa_field_box_test_int=" + sa_field_box_test_int);
    System.out.println("sa_field_box_test_long=" + sa_field_box_test_long);
    System.out.println("sa_field_box_test_float=" + sa_field_box_test_float);
    System.out.println("sa_field_box_test_double=" + sa_field_box_test_double);

    System.out.println("sa_field_Long=" + sa_field_Long);

    System.out.println();
    System.out.println("SLOTS");
    System.out.println("sa_slot_boolean=" + sa_slot_boolean);
    System.out.println("sa_slot_char=" + sa_slot_char);
    System.out.println("sa_slot_byte=" + sa_slot_byte);
    System.out.println("sa_slot_short=" + sa_slot_short);
    System.out.println("sa_slot_int=" + sa_slot_int);
    System.out.println("sa_slot_long=" + sa_slot_long);
    System.out.println("sa_slot_float=" + sa_slot_float);
    System.out.println("sa_slot_double=" + sa_slot_double);
    System.out.println("sa_slot_string=" + sa_slot_string);
    System.out.println("sa_slot_Object=" + sa_slot_Object);

    // Important point here
    breakpoint();

    System.out.println("sa_field_boolean=" + sa_field_boolean);
    System.out.println("sa_field_char=" + sa_field_char);
    System.out.println("sa_field_byte=" + sa_field_byte);
    System.out.println("sa_field_short=" + sa_field_short);
    System.out.println("sa_field_int=" + sa_field_int);
    System.out.println("sa_field_long=" + sa_field_long);
    System.out.println("sa_field_float=" + sa_field_float);
    System.out.println("sa_field_double=" + sa_field_double);
    System.out.println("sa_field_string1=" + sa_field_string1);
    System.out.println("sa_field_string2=" + sa_field_string2);
    System.out.println("sa_field_string3=" + sa_field_string3);
    System.out.println("sa_field_Object=" + sa_field_Object);
    System.out.println("sa_field_StringBuffer=" + sa_field_StringBuffer);
    System.out.println("sa_field_Map=" + sa_field_Map);

    System.out.println("sa_field_box_test_boolean=" + sa_field_box_test_boolean);
    System.out.println("sa_field_box_test_char=" + sa_field_box_test_char);
    System.out.println("sa_field_box_test_byte=" + sa_field_box_test_byte);
    System.out.println("sa_field_box_test_short=" + sa_field_box_test_short);
    System.out.println("sa_field_box_test_int=" + sa_field_box_test_int);
    System.out.println("sa_field_box_test_long=" + sa_field_box_test_long);
    System.out.println("sa_field_box_test_float=" + sa_field_box_test_float);
    System.out.println("sa_field_box_test_double=" + sa_field_box_test_double);
    System.out.println("sa_field_box_test_double2=" + sa_field_box_test_double2);

    System.out.println();
    System.out.println("SLOTS");
    System.out.println("sa_slot_boolean=" + sa_slot_boolean);
    System.out.println("sa_slot_char=" + sa_slot_char);
    System.out.println("sa_slot_byte=" + sa_slot_byte);
    System.out.println("sa_slot_short=" + sa_slot_short);
    System.out.println("sa_slot_int=" + sa_slot_int);
    System.out.println("sa_slot_long=" + sa_slot_long);
    System.out.println("sa_slot_float=" + sa_slot_float);
    System.out.println("sa_slot_double=" + sa_slot_double);
    System.out.println("sa_slot_string=" + sa_slot_string);
    System.out.println("sa_slot_Object=" + sa_slot_Object);

    // important point here
    breakpoint();

    return;
  }

  private void breakpoint() {
  }

  public boolean sa_field_boolean = true;
  char sa_field_char = 'a';
  protected byte sa_field_byte = 9;
  private final short sa_field_short = 257;

  public final static int sa_field_int = 1 * 1024 * 1024; // Static and final
  static long sa_field_long = 1 * 1024 * 1024 * 1024 * 1024L;

  protected static float sa_field_float = 3.1415f;
  private static double sa_field_double = Math.E;
  private static double sa_field_double2 = Math.PI;

  private static String sa_field_string1 = "Alf";
  private static String sa_field_string2 = "BlaBlaBla";
  private static String sa_field_string3 = "Bin Aladin";
  private static Object sa_field_Object = new Object();
  private static StringBuffer sa_field_StringBuffer = new StringBuffer();
  private static Map<Integer, Integer> sa_field_Map = null;

  // Boxed fields
  public boolean sa_field_box_test_boolean = true;
  char sa_field_box_test_char = 'a';
  protected byte sa_field_box_test_byte = 9;
  private final short sa_field_box_test_short = 257;

  public final static int sa_field_box_test_int = 1 * 1024 * 1024; // Static and final
  static long sa_field_box_test_long = 1 * 1024 * 1024 * 1024 * 1024L;

  protected static float sa_field_box_test_float = 3.1415f;
  private static double sa_field_box_test_double = Math.E;
  private static double sa_field_box_test_double2 = Math.PI;

  public Boolean sa_field_Boolean = false;
  Character sa_field_Char = 'l';
  protected Byte sa_field_Byte = -9;
  private final Short sa_field_Short = -257;

  public final static Integer sa_field_Int = 2 * 1024 * 1024; // Static and final
  static Long sa_field_Long = 2L * 1024L * 1024L * 1024L * 1024L;

  protected static Float sa_field_Float = 4f;
  private static Double sa_field_Double = 0.1;
}
