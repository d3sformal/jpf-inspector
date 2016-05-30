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
package paper;

import gov.nasa.jpf.vm.Verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * Extract from this example is used in the "Advanced Debugging with JPF-Inspector" paper.
 *
 * TODO - Recorded execution (with commands bellow) is stored in the "TestClass-record.txt" file.
 *
 * # Stop if an error is discovered (assertion violation, null dereference, uncaught exception, etc.) 
 * create breakpoint property violated
 * # Start checking
 * run
 * 
 * # Print value of variables when error is found
 * print
 * 
 * # Create dynamic assertion
 * assert pos=paper/TestClass.java:TODO this.charset == "UTF-8"
 * 
 * # Backtrack before load File call
 * back_step_over  6 TODO - step number
 * 
 * # Forward run, to check new assertion 
 * run
 * 
 * # After assertion violation found
 * set this.charset "UTF-8"
 * 
 * # Check if test fails
 * run
 * 
 * # Analyze the race -> see possible different thread scheduling
 * enable print scheduling choice_generators
 * 
 * # Go back to see whit thread can be scheduled
 * back_step_transition
 * 
 * # To see program counters (location where each thread currently  is)
 * thread_pc
 * 
 * # Plan the main thread
 * cg select 0
 * 
 * # Check if different thread scheduling can also lead to error
 * run
 * 
 * 
 * 
 */

class CacheManager {

  private final FileLoader loader;
  private final Map<File, String> cache;

  public CacheManager () throws InterruptedException {
    loader = new FileLoader(this);
    cache = Collections.synchronizedMap(new HashMap<File, String>());
  }

  public void terminate () throws Throwable {
    loader.terminate();
  }

  /**
   * Asynchronously loads file to cache.
   * 
   * @param fileName File to load into cache.
   * @param charset Encoding of the file.
   */
  synchronized void loadFile (String fileName, String charset) {
    if (!fileInCache(fileName)) {
      loader.setFileName(fileName);
      loader.setCharset(charset);
    }
  }

  /**
   * Checks if file is cached
   * 
   * @param fileName File to check.
   * @return True if file is loaded in the cache, false otherwise.
   */
  public synchronized boolean fileInCache (String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("The fileName parameter cannot be null.");
    }
    File file = new File(fileName);
    return cache.containsKey(file);
  }

  void addToCache (File file, String fileContent) {
    if (file == null) {
      throw new IllegalArgumentException("The file parameter cannot be null.");
    }
    cache.put(file, fileContent);
  }

  /**
   * @param fileName Cached file
   * @return Get string with file content or null if file not in cache.
   */
  public synchronized String getCachedFile (String fileName) {
    File file = new File(fileName);
    return cache.get(file);
  }
}

/**
 * Loads files to cache in background thread
 */
final class FileLoader extends Thread {
  private File file = null;
  private String charset = "US-ASCII";

  private boolean terminate = false;

  private final CacheManager cacheManager;

  public FileLoader (CacheManager cacheManager) throws InterruptedException {
    super("FileLoader");
    assert (cacheManager != null);

    this.cacheManager = cacheManager;

    // Start loading thread
    synchronized (this) {
      this.setDaemon(true);
      this.start();
      try {
        wait();
      } catch (InterruptedException e) {
        // Terminate loading thread and terminate
        this.interrupt();
        throw e;
      }
    }
  }

  public synchronized void setFileName (String fileName) {
    file = new File(fileName);
    // Wake up loader thread to load file in background
    notify();
  }

  public synchronized void setCharset (String charset) {
    this.charset = charset;
  }

  public synchronized void terminate () throws InterruptedException {
    terminate = true;
    notify(); // Wake up run method
    wait();
  }

  @Override
  public synchronized void run () {
    try {
      // Wake up constructor - when block in wait()
      this.notify();

      // Loading cycle
      while (true) {
        try {
          wait(); // Wait for file to process

          if (terminate) {
            break;
          }

          // Load file to buffer
          assert (file != null);
          int len = (int) file.length();
          int readed = 0;
          byte[] buf = new byte[len];
          InputStream fis = new FileInputStream(file);
          Verify.breakTransition("In JPF8, a reason is required.");
          while (readed < len) {
            int lastRead = fis.read(buf, readed, len - readed);
            if (lastRead == -1) {
              break;
            }
            readed += lastRead;
          }
          fis.close();

          // Convert charset
          // assert (charset != null);
          Charset cs = Charset.forName(charset);
          String fileContent = new String(buf, cs);

          // Store to CacheManager
          cacheManager.addToCache(file, fileContent);
        } catch (IOException e) {
          // Error while read the file - ignore file
          // } catch (FileNotFoundException e) {
          // // Ignore file
        }
      }
    } catch (InterruptedException e) {
      // Terminate the thread
    }

    this.notify(); // Notify terminate method
  }
}

public class TestClass {

  public static void main (String[] args) {

    CacheManager cm = null;
    try {
      cm = new CacheManager();
      cm.loadFile("Test1.ini", "UTF-8");

      // String content =
      // cm.getCachedFile("Test1.ini");

      String content = null;
      do {
        content = cm.getCachedFile("Test1.ini");
        Thread.sleep(100);
      } while (content == null);

      assert (content.equals("#TestData-UTF8-áéíóúý-end"));

    } catch (InterruptedException e) {
    }

    // Terminate the test
    try {
      if (cm != null) {
        cm.terminate();
      }
    } catch (Throwable t) {
      assert (false);
    }
  }
}
