package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class shoutcommand implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling shout red chat command");
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		String Meowzi = parameters[0];
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
			Charstuff.getInstance().shoutkyaa(cur.getName(), Meowzi, tmp.GetChannel());
		}
		
	}
}
