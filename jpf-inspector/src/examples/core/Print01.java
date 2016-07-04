///
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
///

package core;/**
 * This class is intended for manual testing of the print command.
 * 
 * @author Alf
 */
@SuppressWarnings("ALL") public class Print01 {

  // Variables of various types
  public final boolean bool = true;
  public final byte b = 127;
  public final short s = Short.MIN_VALUE;
  public final int i = 0;
  public final long l1 = Long.MAX_VALUE;
  public final long l2 = Long.MIN_VALUE;

  public final float f1 = 0;
  public final float f2 = Float.MAX_VALUE;
  public final float f3 = Float.MIN_VALUE;
  public final float f4 = Float.NaN;
  public final float f5 = Float.POSITIVE_INFINITY;
  public final float f6 = Float.NEGATIVE_INFINITY;
  public final float f7 = 2.12345678e-30f;

  public final double d1 = 0;
  public final double d2 = Double.MAX_VALUE;
  public final double d3 = Double.MIN_VALUE;
  public final double d4 = Double.NaN;
  public final double d5 = Double.POSITIVE_INFINITY;
  public final double d6 = Double.NEGATIVE_INFINITY;
  public final double d7 = 2.12345678e-130;

  public final String s1 = null;
  public final String s2 = "";
  public final String s3 = " ";
  public final String s4 = " \n\t";
  public final String s5 = "Alfik\"+ ěščřžýáíé\" ";

  public boolean[] abool1 = null;
  public boolean[] abool2 = {
    true,
    true,
    false };
  public boolean[] abool3 = new boolean[4];

  public int[] ai1 = null;
  public int[] ai2 = {
    -1,
    0,
    1 };
  public int[] ai3 = new int[10];
  public int[] ai4 = new int[1024];

  /**
   * @param args
   */
  public static void main (String[] args) {
    Print01 p = new Print01();
    System.out.println("create breakpoint pos=Print01.java:78");
    System.out.print("print this");

    System.out.println("p.s5=" + p.s5);
  }

}
