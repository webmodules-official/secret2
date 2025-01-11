package chat.chatCommandHandlers;


import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import ServerCore.ServerFacade;
import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;
import chat.ChatCommandExecutor;

public class NPC implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source)	{
		//System.out.println("Handling Npc spawn");
                Character cur = ((PlayerConnection)source).getActiveCharacter();
       
                byte[] boss = npc.NpcPackets.packeteer(cur.getlastknownX(), cur.getlastknownY(), Integer.parseInt(parameters[0]), parameters[1], cur.getCharID());
                
                cur.sendToMap(boss); 
                ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), boss);
                
            	Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
        		Character tmp;
        		while(iter.hasNext()) {
        			Map.Entry<Integer, Character> pairs = iter.next();
        			tmp = pairs.getValue();
        			if (tmp.getCharID() != cur.getCharID() && tmp.getCurrentMap() == cur.getCurrentMap()) { //[ Prevents spam & crashes ]
        				ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), tmp.extCharPacket());	
        			} 
        		}
        		cur.sendToMap(cur.extCharPacket());
                
                
                
	}

}