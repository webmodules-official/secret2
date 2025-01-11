package chat.chatCommandHandlers;

import java.util.Iterator;
import java.util.Map;

import item.Item;
import Player.Character;
import Tools.StringTools;
import World.WMap;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class massitemspawner implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Received chat command to spawn an item!");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
			if(StringTools.isInteger(parameters[0]) && parameters[i].length() == 9) {
				//System.out.println("Param[" + (i+1) + "] is of integer type length 9!");
				int itemID = Integer.parseInt(parameters[0]);
				
				
				
				
				Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
					Map.Entry<Integer, Character> pairs = iter.next();
					tmp = pairs.getValue();
				//System.out.println("Spawning: " + itemID + " at coords: " + tmp.getlastknownX() + "," + tmp.getlastknownY());
				source.addWrite(Item.itemSpawnPacket(tmp.getCharID(), itemID,Integer.parseInt(parameters[1]), tmp.getlastknownX(), tmp.getlastknownY(), source));
			}
		}}
	}

}
