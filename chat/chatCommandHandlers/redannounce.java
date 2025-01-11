package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class redannounce implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling red announce command");
		byte[] partychat = new byte[46];
		
		partychat[0] = (byte)partychat.length;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x07;
		partychat[17] = (byte)0x01;
		partychat[18] = (byte)0x6e;
		
		partychat[40] = (byte)0x02;
		
		partychat[44] = (byte)0x31; 
		partychat[45] = (byte)0x34; // = " : "
		
	
		
		/*for(int i=0;i<msg.length;i++) {
			partychat[i+44] = msg[i];
		}*/
		
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
			ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), partychat);
		}
		
	}

}
