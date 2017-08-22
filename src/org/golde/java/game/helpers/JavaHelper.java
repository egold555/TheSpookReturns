package org.golde.java.game.helpers;

import java.util.Random;

/**
 * Java Utilities helper class
 * @author Eric
 *
 */
public class JavaHelper {

	private static Random random = new Random();
	
	/**
	 * Tests if the String is empty. Handles null "" " ";
	 * @param s Input String
	 * @return
	 */
	public static boolean isStringEmpty(String s) {
		
		if(s == null) {return true;}
		if(s.equalsIgnoreCase("")) {return true;}
		if(s.equalsIgnoreCase(" ")) {return true;}
		
		return false;
	}
	
	/**
	 * Get a random enum from the inputted enum class
	 * @param clazz CustomEnum.class class file
	 * @return
	 */
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

	
}
