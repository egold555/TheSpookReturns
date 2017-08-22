package org.golde.java.game.helpers;

import org.lwjgl.input.Keyboard;

/**
 * An extention to the keyboard class to add a useful method.
 * @author Eric
 *
 */
public class BetterKeyboard {

	private static boolean[] wasDown = new boolean[Keyboard.KEYBOARD_SIZE];
	
	/**
	 * 
	 * @param key The Keyboard.KEY_ key
	 * @return was it pressed?
	 */
	public static boolean wasKeyPressed(int key) {
		if(key < 0 || key >=Keyboard.KEYBOARD_SIZE) {
			return false;
		}
		
		boolean wasPressed = Keyboard.isKeyDown(key) && !wasDown[key];
		wasDown[key] = Keyboard.isKeyDown(key);
		return wasPressed;
	}
}
