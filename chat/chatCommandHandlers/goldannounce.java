package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import Tools.BitTools;
import World.WMap;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class goldannounce implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling gold announce command");
		byte[] partychat = new byte[40];
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		partychat[0] = (byte)partychat.length;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x0d;
		partychat[8] = (byte)0x01;//end of indicator
		for(int i=0;i<4;i++) {
			partychat[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		//partychat[12] = (byte)0xb4; // char ID ???
		//partychat[13] = (byte)0xb7;
		//partychat[14] = (byte)0x62;
		//partychat[15] = (byte)0x02;
		partychat[16] = (byte)0x44;
		partychat[17] = (byte)0x16;
		
		
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
			ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), partychat);
		}
		
	}

}
