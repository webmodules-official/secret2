package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class redannounce2 implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling red announce2 command");
		byte[] partychat = new byte[32];
		byte[] msg = parameters[0].getBytes(); // <--- real gm msg lol
		
		partychat[0] = (byte)partychat.length;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x35;
		partychat[8] = (byte)0x08;
		partychat[10] = (byte)0x0b;
		partychat[11] = (byte)0x43;
		partychat[12] = (byte)0xc5;
		partychat[13] = (byte)0xb7;
		partychat[14] = (byte)0x62;
		partychat[15] = (byte)0x02;
		partychat[16] = (byte)0x02;
		
		partychat[24] = (byte)0x8b;
		
		partychat[28] = (byte)0x7f;
		partychat[30] = (byte)0x62; // = " : "
		
	
		
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
