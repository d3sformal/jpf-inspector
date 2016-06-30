//
// Copyright (C) 2010-2011 Pavel Jančík
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
//

package gov.nasa.jpf.inspector.frontends.swing.terminal;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Creates and holds pipe until the {@link #closePipe} method is called.
 * 
 * @author Alf
 */
final class PipeHolder extends Thread {
  private static final int PIPE_SIZE = 512;
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
      pInputStream = new PipedInputStream(PIPE_SIZE);
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