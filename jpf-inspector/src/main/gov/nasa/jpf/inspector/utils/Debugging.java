package gov.nasa.jpf.inspector.utils;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.shell.ShellManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains debugging utilities for JPF Inspector.
 * In particular, it encapsulates logging. Use only this class to get loggers inside JPF Inspector.
 */
public class Debugging {
  /**
   * Gets the logger that should be used inside JPF Inspector.
   *
   * It sets its default log level based on the "log.level" property in JPF configuration files. We must do this on our own because JPF logging utilities are not yet initialized by the time we need logging. This is because the JPF object itself is not yet created (it is only created in the shell when the Verify command is executed).
   *
   * This also means that advanced logging configuration (specifically, the log.error and log.warning etc.) string arrays won't work.
   *
   * @param config The initialized configuration object.
   * @return A logger for JPF Inspector.
   */
  public static Logger getLogger(Config config) {
    Logger logger = JPF.getLogger("inspector");
    try {
      logger.setLevel(Level.parse(config.getString("log.level", "WARNING").toUpperCase()));
    } catch (IllegalArgumentException ex) {
      logger.warning("The log level from configuration files (" + config.getString("log.level") + ") does not exist.");
    }
    return logger;
  }

  public static Logger getLogger() {
      return getLogger(ShellManager.getManager().getConfig());
  }

  /**
   * Gets the logger that should be used inside JPF Inspector if the Inspector is launched via a Shell handled by
   * the Shell Manager (e.g. a graphical Swing shell).
   * @return A logger for JPF Inspector.
   */
  public static Logger getSwingShellLogger() {
    return getLogger(ShellManager.getManager().getConfig());
  }

  public static void sleep(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException ignored) {
      // Don't care. This is just a debugging method.
    }
  }

  public static Logger getSimpleLogger() {
    return JPF.getLogger("inspector");
  }
}
