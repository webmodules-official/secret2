package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class greenannounce implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling partychat command");
		byte[] partychat = new byte[44+parameters[0].length()];
		byte[] msg = parameters[0].getBytes(); // <--- real gm msg lol
		partychat[0] = (byte)partychat.length;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x07;
		partychat[8] = (byte)0x01;
		partychat[9] = (byte)0xf7;
		partychat[17] = (byte)0x01;
		partychat[18] = (byte)0x69;
		partychat[40] = (byte)0x12; // = " : "
		
		Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
		String finalstring = parameters[0].toLowerCase();
		Iterator<Entry<String, String>> iterw = Charstuff.getInstance().CensoredWords.entrySet().iterator();
		while(iterw.hasNext()) {
			Entry<String, String> pairsw = iterw.next();
			//check if any of the words in hashmap is in this string.
			if(finalstring.contains(pairsw.getKey())){
				currenthcamp.respondguildTIMED("Censor Protection System has dectected foul language : "+pairsw.getKey(), currenthcamp.GetChannel()); return;
			}

		}
		byte[] name = currenthcamp.getLOGsetName().getBytes();
		for(int i=0;i<name.length;i++) {
			partychat[i+20] = name[i];
		}


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
