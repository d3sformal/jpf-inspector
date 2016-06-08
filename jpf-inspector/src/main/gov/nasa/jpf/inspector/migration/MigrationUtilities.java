package gov.nasa.jpf.inspector.migration;

import gov.nasa.jpf.inspector.server.jpf.InspectorListenerModeNotifications;
import gov.nasa.jpf.vm.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class contains static functions that help with the migration of the JPF Inspector codebase
 * from JPF6 to JPF8 during the 2016 update.
 * <p>
 * Because the update may break things, some modifications that could have unintended consequences pass through this class so that when something breaks, we can fix it everywhere we broke it at the same time. When we are sure it works, this class will be removed and the functions' code inlined at the appropriate places.
 */
public final class MigrationUtilities {


  /**
   * Returns the instruction that is about to be executed.
   * @param vm The VM instance.
   */
  public static Instruction getLastInstruction(VM vm) {
    return vm.getInstruction(); // This is sketchy.
  }

  public static ChoiceGenerator<?> getLastChoiceGenerator(VM vm) {
    return vm.getChoiceGenerator();
  }

  public static ThreadInfo getLastThreadInfo(VM vm) {
    return vm.getCurrentThread();
  }



  public static ClassInfo getResolvedClassInfo(String className) {
    return ClassLoaderInfo.getCurrentResolvedClassInfo(className); // or "system" resolved?


  }

  /**
   * Replaces ClassInfo.getLoadedClass().
   */
  public static ClassInfo[] getLoadedClasses() {
    ArrayList<ClassInfo> loadedClasses = new ArrayList<>();
    for (ClassInfo loadedClass : ClassLoaderInfo.getCurrentClassLoader()) {
      loadedClasses.add(loadedClass);
    }
    ClassInfo[] array = new ClassInfo[loadedClasses.size()];
    array = loadedClasses.toArray(array);
    return array;
  }

  /**
   * I still don't understand this...
   * Test it.
   */
  public static StaticElementInfo getStaticElementInfoFromName(String varName) {
    Statics statics = ClassLoaderInfo.getCurrentClassLoader().getStatics();
    for (int i = 0; i < statics.size(); i++) {
      StaticElementInfo sei = statics.get(i);
      if (varName.equals(sei.getClassInfo().getName())) {
        return sei;
      }
    }
    return null;
  }

  /**
   * Method called from within StateValueStackSlot.java only, under special circumstances,
   * so it may call for a different migration that all the others (the others only call basic
   * primitives and system types (Object, String)).
   */
  public static ClassInfo getResolvedClassInfo_StateValueStackSlot(String className) {
    return MigrationUtilities.getResolvedClassInfo(className);
  }


}
