package projectoreo.utils;

import java.util.HashMap;

/**
 * Front Controller Design Pattern :: also mixed with Simpleton Pattern --| It is a storage of
 * controllers, that can be added, accessed, or deleted.
 */
public class ControllersDispatcher {

  private static ControllersDispatcher instance;

  private HashMap<Class, Controller> controllerHashMap;

  private ControllersDispatcher() {
    controllerHashMap = new HashMap<>();
  }

  public void store(Class className, Controller controller) {
    if (!controllerHashMap.containsKey(className)) controllerHashMap.put(className, controller);
  }

  public Controller get(Class className) {
    return controllerHashMap.get(className);
  }

  public int size() {
    return controllerHashMap.size();
  }

  public static ControllersDispatcher getInstance() {
    if (instance == null) {
      instance = new ControllersDispatcher();
    }
    return instance;
  }
}
