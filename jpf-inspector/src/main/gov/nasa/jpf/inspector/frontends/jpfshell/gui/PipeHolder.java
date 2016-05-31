//
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

package gov.nasa.jpf.inspector.frontends.jpfshell.gui;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Creates and holds pipe until the {@link #closePipe} method is called.
 * 
 * @author Alf
 */
final class PipeHolder extends Thread {
  private PipedInputStream pInputStream = null;
  private PipedOutputStream pOutputStream = null;

  private boolean terminate = false;
  private boolean terminated = false; // If true run method has terminated

  public PipeHolder () {
    super("PipeHolder");

    synchronized (this) {
      start();
      try {
        wait();
      } catch (InterruptedException ignored) {
      }
    }
  }

  @Override
  public synchronized void run () {
    try {
      pInputStream = new PipedInputStream(512);
      pOutputStream = new PipedOutputStream(pInputStream);
    } catch (IOException e) {
      pOutputStream = null;
      pInputStream = null;
    }
    notifyAll();

    try {
      while (!terminate) {
        wait();
      }
    } catch (InterruptedException ignored) {
    }
    notifyAll(); // Wake up closePipe
    terminated = true;
  }
  public synchronized void closePipe () {
    terminate = true;
    if (!terminated) {
      notifyAll();
      try {
        wait();
      } catch (InterruptedException ignored) {
      }
    }
  }
  /**
   * @return Gets stream where to read data sent to pipe
   */
  public PipedInputStream getInputStream () {
    return pInputStream;
  }

  /**
   * @return Gets stream where to write data to pipe
   */
  public PipedOutputStream getOutpoutStream () {
    return pOutputStream;
  }
}