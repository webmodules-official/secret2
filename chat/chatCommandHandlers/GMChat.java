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

public class GMChat implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling GM red chat command");
		byte[] gmsg = new byte[14+parameters[0].length()];
		byte[] msg = parameters[0].getBytes(); // <--- real gm msg lol
		
		gmsg[0] = (byte)gmsg.length;
		gmsg[4] = (byte)0x03;
		gmsg[6] = (byte)0x50;
		gmsg[7] = (byte)0xc3;
		gmsg[8] = (byte)0x01;
		gmsg[9] = (byte)parameters[0].length();
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		
		String finalstring = parameters[0].toLowerCase();
		Iterator<Entry<String, String>> iterw = Charstuff.getInstance().CensoredWords.entrySet().iterator();
		while(iterw.hasNext()) {
			Entry<String, String> pairsw = iterw.next();
			//check if any of the words in hashmap is in this string.
			if(finalstring.contains(pairsw.getKey())){
				cur.respondguildTIMED("Censor Protection System has dectected foul language : "+pairsw.getKey(), cur.GetChannel()); return;
			}

		}
		
		
		for(int i=0;i<msg.length;i++) {
			gmsg[i+13] = msg[i];
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
