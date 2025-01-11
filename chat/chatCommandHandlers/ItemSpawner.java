package chat.chatCommandHandlers;

import item.Item;
import Player.PlayerConnection;
import Player.Character;
import Tools.StringTools;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class ItemSpawner implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Received chat command to spawn an item!");
				Character cur = ((PlayerConnection)source).getActiveCharacter();
				//System.out.println("About to spawn item: " + itemID + "x"+Integer.parseInt(parameters[1])+" at coordinates: " + cur.getlastknownX() + "," + cur.getlastknownY() );
				source.addWrite(Item.itemSpawnPacket(cur.getCharID(), Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), cur.getlastknownX(), cur.getlastknownY(), source));
	}
} 
		


