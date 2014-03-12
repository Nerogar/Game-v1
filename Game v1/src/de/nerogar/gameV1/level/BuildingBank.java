package de.nerogar.gameV1.level;

import java.util.HashMap;

public class BuildingBank {

	public static HashMap<Integer, String> buildings = new HashMap<Integer, String>();
	static {
		buildings.put(0, "houseBlue");
		buildings.put(1, "houseGreen");
		buildings.put(2, "houseOrange");
		buildings.put(3, "housePink");
		buildings.put(4, "houseRed");
	}

	/*public static EntityBuilding getNewInstance(int id, Object... ctorArgument) {
		if (!buildings.containsKey(id)) return null;
		Object object = null;
		try {
			Class<?> clazz = Class.forName(buildings.get(id));
			Constructor<?> ctor = clazz.getConstructor(String.class);
			object = ctor.newInstance(new Object[] { ctorArgument });
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
		if (object instanceof EntityBuilding) return (EntityBuilding) object;
		return null;
	}*/

	public static String getBuildingName(int i) {
		if (!buildings.containsKey(i)) return null;
		return buildings.get(i);
	}

}
