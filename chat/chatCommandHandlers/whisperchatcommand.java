package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class whisperchatcommand implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling whisper command");
		String CharName = (parameters[0]);
		byte[] partychat = new byte[44+parameters[1].length()];
		byte[] msg = parameters[1].getBytes(); // <--- real gm msg lol
		boolean isValid = false;
		
		partychat[0] = (byte)partychat.length;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x07;
		partychat[8] = (byte)0x01;
		partychat[9] = (byte)0x59;
		partychat[10] = (byte)0x15;
		partychat[11] = (byte)0x08;
		partychat[12] = (byte)0xb4;
		partychat[13] = (byte)0xb7;
		partychat[14] = (byte)0x62;
		partychat[15] = (byte)0x02;
		partychat[17] = (byte)0x01;
		partychat[18] = (byte)0x01;
		
		//partychat[20] = (byte)0xa4; // begin letter name
		//partychat[21] = (byte)0xd1;
		
		Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
		byte[] name = currenthcamp.getLOGsetName().getBytes();
		
		for(int i=0;i<name.length;i++) {
			partychat[i+20] = name[i];
		}
		
		partychat[37] = (byte)0x1b; // = " : "
		partychat[38] = (byte)0x37; // = " : "
		partychat[39] = (byte)0x08; // = " : "
		partychat[40] = (byte)0x07; // = " : "
		
	
		
		for(int i=0;i<msg.length;i++) {
			partychat[i+44] = msg[i];
		}
		
		Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;


		Map.Entry<Integer, Character> pairs = iter.next();
		tmp = pairs.getValue(); 
		if(CharName.equals(tmp.getLOGsetName())) {
		names.put(tmp.getLOGsetName(), tmp);
		Character anyplayer = names.get(CharName); // select player from string
		//System.out.println("Name: " + anyplayer.getLOGsetName());
		isValid = true;
				
		ServerFacade.getInstance().addWriteByChannel(anyplayer.GetChannel(), partychat); // send whisper
							 
		}
		
		if(!isValid) {
			//System.out.println("Name doesnt exist!");
			
			//TODO: resond party chat that name doesnt exist
		}
		
	}

}
