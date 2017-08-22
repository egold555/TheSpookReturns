package org.golde.java.game.console;

import org.golde.java.game.GLog;
import org.golde.java.game.objects.player.EntityPlayer;
import org.lwjgl.util.vector.Vector3f;

public class CommandHandler {

	public static void onCommand(EntityPlayer player, String cmd) {
		String[] args = cmd.split("\\s+");
		
		
		if(cmd.toLowerCase().startsWith("tp")) {
			player.setPosition(new Vector3f(Float.parseFloat(args[1]),Float.parseFloat(args[2]),Float.parseFloat(args[3])));
		}
		
		
		
		else {
			GLog.info("Unknown cmd");
		}
	}
	
}
