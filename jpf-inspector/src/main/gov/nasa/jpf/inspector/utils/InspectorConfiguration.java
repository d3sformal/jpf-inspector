package gov.nasa.jpf.inspector.utils;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.ShellManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains loaded configuration relevant to JPF Inspector.
 *
 * While we want to avoid singleton classes accessed statically in general, in this case, since JPF Inspector should be
 * quite configurable, we would need to pass an instance of this class everywhere or at least to the panel, the client
 * and the server and then that's pretty much the same thing as static access, and would be more messy.
 */
public final class InspectorConfiguration {
  private static Logger logger = Debugging.getLogger();
  private static InspectorConfiguration instance;
  private Config config;

  private InspectorConfiguration(Config config) {
    this.config = config;
    ignoreClassesFeature = config.getBoolean("jpf-inspector.ignore-breakpoints-in-ignored-classes", true);
    ignoredClasses = config.getStringArray("jpf-inspector.ignored-classes", new String[] { "java.*", "javax.*" });
    for(int i = 0; i < ignoredClasses.length; i++) {
      ignoredClasses[i] = ignoredClasses[i].replace(".", "\\.");
      ignoredClasses[i] = ignoredClasses[i].replace("*", ".*");
      ignoredClasses[i] = "^" + ignoredClasses[i];
    }
  }
  public static InspectorConfiguration getInstance() {
    if (instance == null) {
      if (ShellManager.getManager() == null) throw new IllegalStateException("Shell manager but be instantiated first.");
      Config config = ShellManager.getManager().getConfig();
      if (config == null) throw new IllegalStateException("Shell manager does not have a loaded Config object yet.");
      instance = new InspectorConfiguration(config);
    }
    return instance;
  }

  private boolean ignoreClassesFeature;
  private String[] ignoredClasses;

  public boolean isClassIgnored(String className) {
    if (!ignoreClassesFeature) return false; // If false, then we ignore nothing.

    for (String ignoredClass : ignoredClasses) {
      if (logger.isLoggable(Level.FINE)) {
        logger.fine("Checking ignoring against " + ignoredClass);
      }
      if (className.matches(ignoredClass)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Indicates whether safe mode is active. In safe mode, program state inspection and stepping can only happen when the
   * JPF is paused.
   */
  public boolean isSafeModeActive() {
    return config.getBoolean("jpf-inspector.safe-mode", true);
  }

  /**
   * Indicates whether, after the "run" command is executed, the JPF Inspector console should retain visibility.
   */
  public boolean shouldRequestFocusOnVerify() {
    return config.getBoolean("jpf-inspector.request-focus-on-verify", true);
  }
}
