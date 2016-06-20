package gov.nasa.jpf.inspector.tests.acceptance.legacy.usecases.uc3;//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

/**
 * This class is intended for manual testing of the print command.
 * 
 * @author Alf
 */
@SuppressWarnings("ALL")
public class Print02 {

  public static void main (String[] args) {
    // See Predecessor3.InnerPred3.callMePred3I3() method
    System.out.println("create breakpoint pos=Print02.java:181");

    Predecessor3 p = new Predecessor3();
    p.callMePred1(10000);

  }
}

@SuppressWarnings("ALL")
class Predecessor1 {

  int pred1_i1 = 101;
  public int pred1_i2 = 102;
  private final int pred1_i3 = 103;
  protected int pred1_i4 = 104;

  static int pred1_i11 = 111;
  public static int pred1_i12 = 112;
  private static int pred1_i13 = 113;
  protected static int pred1_i14 = 114;

  public int a = 1;

  class InnerPred1 {
    public int a = 1001;

    int pred1i1_i1 = 1101;
    public int pred1i1_i2 = 1102;
    private final int pred1i1_i3 = 1103;
    protected int pred1i1_i4 = 1104;

    // Not possible
    // static int pred1i1_i11 = 1111;
    // static public int pred1i1_i12 = 1112;
    // static private int pred1i1_i13 = 1113;
    // static protected int pred1i1_i14 = 1114;

    public void callMePred1I1 (int a) {
      System.out.println("# Predecessor1.InnerPred1.callMePred1I1(" + a + ")");
      callMePred2I2(10004);
    }

    public void callMePred2I2 (int a) {
      throw new RuntimeException("unreachable");
    }
  }

  public void callMePred1 (int a) {
    System.out.println("# Predecessor1.callMePred1(" + a + ")");
    callMePred2(a + 1);
  }

  public void callMePred2 (int a) {
    throw new RuntimeException("unreachable");
  }

}

@SuppressWarnings("ALL")
class Predecessor2 extends Predecessor1 {

  public int a = 2;

  int pred2_i1 = 201;
  public int pred2_i2 = 202;
  private final int pred2_i3 = 203;
  protected int pred2_i4 = 204;

  static int pred2_i11 = 211;
  public static int pred2_i12 = 212;
  private static int pred2_i13 = 213;
  protected static int pred2_i14 = 214;

  class InnerPred2 extends InnerPred1 {
    public int a = 1002;

    int pred2i2_i1 = 1201;
    public int pred2i2_i2 = 1202;
    private final int pred2i2_i3 = 1203;
    protected int pred2i2_i4 = 1204;

    @Override
    public void callMePred2I2 (int a) {
      System.out.println("# Predecessor2.InnerPred2.callMePred2I2(" + a + ")");
      callMePred3I3(a + 1);
    }

    public long callMePred3I3 (int a) {
      throw new RuntimeException("unreachable");
    }
  }

  @Override
  public void callMePred2 (int a) {
    System.out.println("# Predecessor2.callMePred2(" + a + ")");
    callMePred3(a + 1);
  }

  public void callMePred3 (int a) {
    throw new RuntimeException("unreachable");
  }

}

@SuppressWarnings("ALL")
class Predecessor3 extends Predecessor2 {
  public int a = 3;

  int pred3_i1 = 301;
  public int pred3_i2 = 302;
  private final int pred3_i3 = 303;
  protected int pred3_i4 = 304;

  static int pred3_i11 = 311;
  public static int pred3_i12 = 312;
  private static int pred3_i13 = 313;
  protected static int pred3_i14 = 314;

  public class InnerPred3 extends InnerPred2 {
    public int a = 1003;

    int pred3i3_i1 = 1301;
    public int pred3i3_i2 = 1302;
    private final int pred3i3_i3 = 1303;
    protected int pred3i3_i4 = 1304;

    // int Predecessor3 = 10;

    @Override
    public long callMePred3I3 (int a) {
      int b = 10;

      System.out.println("# Predecessor3.InnerPred2.callMePred3I3(" + a + ")");
      System.out.println("print");
      System.out.print("# The \"print\" command should show all fields defined in InnerPred3, InnerPred2, InnterPred1 ");
      System.out.println("as well as this$0 for acces to outer class and a parameter a and variable b");
      System.out.println("print this$0");
      System.out.println("# The \"print this$0\" command should show all fields (even static) defined in Predecessor3, Predecessor2, Predecessor1");
      System.out.println("print this.Precedessor3");
      System.out.println("# The same result as \"print this$0\"");

      System.out.println("# In the following commands check values of the parameter a - the deeper in the call stack the lower value of the parameter a");
      System.out.println("print #stackFram[0]");
      System.out.println("print #stackFram[1]");
      System.out.println("print #stackFram[2]");
      System.out.print("# The commands should show all fields defined in InnerPred3, InnerPred2, InnterPred1 ");
      System.out.println("as well as this$0 for acces to outer class and a parameter a.");
      System.out.println("print #stackFram[3]");
      System.out.println("# The the variable inner should be also dumped.");
      System.out.println("print #stackFram[4]");
      System.out.println("print #stackFram[5]");
      System.out.println("# The commands should show all fields defined in Predecessor3, Predecessor2, Predecessor1 and a parameter a.");

      return -10;
    }
  }

  @Override
  public void callMePred3 (int a) {
    System.out.println("# Predecessor2.callMePred2(" + a + ")");
    InnerPred3 inner = new InnerPred3();

    inner.callMePred1I1(a + 1);
  }

}
