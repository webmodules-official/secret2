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

public class guildchatcommand implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling guildchat command");
		byte[] partychat = new byte[44+parameters[0].length()];
		byte[] msg = parameters[0].getBytes(); // <--- real gm msg lol
		
		partychat[0] = (byte)partychat.length;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x07;
		partychat[8] = (byte)0x01;

		partychat[17] = (byte)0x01;
		partychat[18] = (byte)0x03;
		
		
		Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
		byte[] name = currenthcamp.getLOGsetName().getBytes();
		
		for(int i=0;i<name.length;i++) {
			partychat[i+20] = name[i];
		}
		
		partychat[37] = (byte)0x9e; // = " : "
		partychat[38] = (byte)0x0f; // = " : "
		partychat[39] = (byte)0xbf; // = " : "
		partychat[40] = (byte)0x04; // = " : "
		
	
		
		for(int i=0;i<msg.length;i++) {
			partychat[i+44] = msg[i];
		}
		
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
			ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), partychat);
		}
		
	}

}
