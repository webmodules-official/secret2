package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class servernews implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling server news command");

		String charname = parameters[0];
		
		byte[] gmsg = new byte[45+charname.length()];
		byte[] msg = charname.getBytes(); // <--- real shout  msg lol
		
		gmsg[0] = (byte)gmsg.length;
		gmsg[4] = (byte)0x05;
		gmsg[6] = (byte)0x07;
		gmsg[8] = (byte)0x01;
		gmsg[17] = (byte)0x01;
		gmsg[18] = (byte)0x06;
		
		

		gmsg[20] = (byte)0xa4; // begin letter name
		gmsg[21] = (byte)0xd1;
		//gmsg[22] = (byte)0xbe;
		//gmsg[23] = (byte)0xe1;					   | --> can be used for names such as "System news" ?
		//gmsg[24] = (byte)0xb1;
		//gmsg[25] = (byte)0xc3; // end letter name
		
		
		byte[] name = "[Server News]".getBytes();
		
		for(int i=0;i<name.length;i++) {
			gmsg[i+22] = name[i];
		}
		
		gmsg[40] = (byte)0x44; // = " : "
		
	
		
		for(int i=0;i<msg.length;i++) {
			gmsg[i+44] = msg[i];
		}
		
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
			ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
		}
		
	}

}
