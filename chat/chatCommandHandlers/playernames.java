package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class playernames implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		String recexp = parameters[0];


			Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			
			while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
			Charstuff.getInstance().respondguild(tmp.getLOGsetName(), cur.GetChannel());
			}
	}
}
	
