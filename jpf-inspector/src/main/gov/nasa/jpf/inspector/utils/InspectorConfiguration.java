package gov.nasa.jpf.inspector.utils;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.inspector.interfaces.CustomCommand;
import gov.nasa.jpf.inspector.interfaces.CustomHitCondition;
import gov.nasa.jpf.inspector.server.breakpoints.InternalBreakpointHolder;
import gov.nasa.jpf.shell.ShellManager;

import javax.swing.plaf.TreeUI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
  private Logger logger;
  private static InspectorConfiguration instance;
  private Map<String, Class<? extends CustomHitCondition>> customHitConditions;
  private Map<String, CustomCommand> customCommands;
  private Map<String, CommandAlias> aliases;
  private Config config;

  private InspectorConfiguration(Config config) {
    this.config = config;
    this.logger = Debugging.getLogger(config);

    // Set ignored classes
    ignoreClassesFeature = config.getBoolean("jpf-inspector.ignore_breakpoints_in_ignored_classes", true);
    ignoredClasses = config.getStringArray("jpf-inspector.ignored_classes", new String[] { "java.*", "javax.*" });
    for(int i = 0; i < ignoredClasses.length; i++) {
      ignoredClasses[i] = ignoredClasses[i].replace(".", "\\.");
      ignoredClasses[i] = ignoredClasses[i].replace("*", ".*");
      ignoredClasses[i] = "^" + ignoredClasses[i];
    }

    loadCustomHitConditions(config);

    loadCustomCommands(config);

    loadAliases(config);
  }

  private void loadAliases(Config config) {
    aliases = new HashMap<>();
    String[] aliasKeys = config.getKeysStartingWith("jpf-inspector.alias.");
    for (String key : aliasKeys) {
      String alias = key.substring("jpf-inspector.alias.".length()).trim();
      String value = config.getString(key);
      CommandAlias commandAlias = new CommandAlias(alias, value);
      aliases.put(alias, commandAlias);
    }
  }

  private void loadCustomHitConditions(Config config) {
    customHitConditions = new HashMap<>();
    Class<?>[] customHitConditionClasses = config.getClasses("jpf-inspector.custom_hit_conditions");
    for(Class<?> customClass : customHitConditionClasses) {
      Object exampleInstance = instantiateClass(customClass);
      if (exampleInstance == null) {
        continue;
      }
      if (!(exampleInstance instanceof CustomHitCondition)) {
        logger.warning("Class '" + customClass.getName() + "' does not implement the CustomHitCondition interface. It won't be usable in the Inspector.");
        continue;
      }
      CustomHitCondition customHitCondition = (CustomHitCondition) exampleInstance;
      String name = customHitCondition.getName();
      customHitConditions.put(name, customHitCondition.getClass());
    }
  }

  private void loadCustomCommands(Config config) {
    customCommands = new HashMap<>();
    Class<?>[] customCommandClasses = config.getClasses("jpf-inspector.custom_commands");
    for(Class<?> customClass : customCommandClasses) {
      Object exampleInstance = instantiateClass(customClass);
      if (exampleInstance == null) {
        continue;
      }
      if (!(exampleInstance instanceof CustomCommand)) {
        logger.warning("Class '" + customClass.getName() + "' does not implement the CustomCommand interface. It won't be usable in the Inspector.");
        continue;
      }
      CustomCommand customCommand = (CustomCommand) exampleInstance;
      for (String name : customCommand.getNames()) {
        customCommands.put(name, customCommand);
      }
    }
  }

  private Object instantiateClass(Class<?> classToInstantiate) {
    Object instance;
    try {
      instance = classToInstantiate.newInstance();
    } catch (InstantiationException e) {
      logger.warning("Class '" + classToInstantiate.getName() + "' cannot be instantiated because of the InstantiationException: " + e.toString() + ". It won't be usable in the Inspector.");
      return null;
    } catch (IllegalAccessException e) {
      logger.warning("Class '" + classToInstantiate.getName() + "' cannot be instantiated because of the IllegalAccessException: " + e.toString()+ ". It won't be usable in the Inspector.");
      return null;
    }
    return instance;
  }

  /**
   * Creates an instance of a custom hit condition that uses the specified name.
   * @param name Name of the hit condition. For example, if the hit condition is called like this: "hello(2)", the name
   *             would be "hello".
   * @return An instance of the hit condition, or null if the hit condition does not exist or could not be instantiated.
   */
  public CustomHitCondition instantiateCustomHitCondition(String name) {
    if (!customHitConditions.containsKey(name)) {
      return null;
    }
    try {
      CustomHitCondition customHitCondition = customHitConditions.get(name).newInstance();
      return customHitCondition;
    } catch (InstantiationException e) {
      logger.warning("Class '" + customHitConditions.get(name).getName() + "' cannot be instantiated because of the InstantiationException: " + e.toString()+ ". It won't be usable in the Inspector.");
      return  null;
    } catch (IllegalAccessException e) {
      logger.warning("Class '" + customHitConditions.get(name).getName() + "' cannot be instantiated because of the IllegalAccessException: " + e.toString()+ ". It won't be usable in the Inspector.");
      return  null;
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

  /**
   * Returns true if breakpoints that happen while the topmost stack frame is of a method in the given class should be ignored.
   * This is set in the Inspector's configuration files.
   *
   * @param className Full qualified name of a Java class.
   */
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
    return config.getBoolean("jpf-inspector.safe_mode", true);
  }

  /**
   * Indicates whether, after the "run" command is executed, the JPF Inspector console should retain visibility.
   */
  public boolean shouldRequestFocusOnVerify() {
    return config.getBoolean("jpf-inspector.request_focus_on_verify", true);
  }

  /**
   * Indicates whether beeps should be suppressed, on Windows, when executing a command or pressing Backspace.
   */
  public boolean shouldPreventBeeps() {
    return config.getBoolean("jpf-inspector.prevent_beeps", true);
  }

  public boolean isBatchModeActive() {
    return config.getBoolean("jpf-inspector.batch_mode", false);
  }

  public boolean shouldEchoInput() {
    return isBatchModeActive() && config.getBoolean("jpf-inspector.batch_mode.echo_input", false);
  }
  public boolean doesCustomHitConditionExceptionBreak() {
    return config.getBoolean("jpf-inspector.custom_breakpoint_exception_breaks", true);
  }
  public boolean shouldWaitAfterRun() {
    return config.getBoolean("jpf-inspector.wait_after_run", false);
  }


  /**
   * Reset the state of the Inspector to a state that is as close as possible to a first launch.
   * This is useful for unit tests.
   */
  public static void staticReset() {
    InternalBreakpointHolder.bpIDCounter = 1;
    instance = null;
  }

  public CustomCommand getCustomCommandIfAny(String commandName) {
    if (customCommands.containsKey(commandName)) {
      CustomCommand exampleCommand = customCommands.get(commandName);
      Class<? extends CustomCommand> commandClass = exampleCommand.getClass();
      try {
        CustomCommand newInstance = commandClass.newInstance();
        return newInstance;
      } catch (InstantiationException | IllegalAccessException e) {
        logger.warning("Class '" + commandClass.getName() + "' could be instantiated because of the exception: " + e.toString()+ ". This should never actually happen because such a class instantiable at program start.");
        assert false : "We failed to instantiate a class that we've already instantiated before.";
        return  null;
      }
    }
    return null;
  }

  public CommandAlias getAliasIfAny(String commandName) {
    if (aliases.containsKey(commandName)) {
      return aliases.get(commandName);
    }
    else {
      return null;
    }
  }
  public Set<Map.Entry<String, CustomCommand>> getCustomCommands() {
    return customCommands.entrySet();
  }
  public Iterable<? extends CommandAlias> getAliases() {
    return aliases.values();
  }
}
