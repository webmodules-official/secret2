package chat.chatCommandHandlers;

import java.util.Iterator;
import java.util.Map;

import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class furybar implements ChatCommandExecutor {
 

	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		String CharName = (parameters[0]);
		cur.recfury(Integer.valueOf(CharName));
	}

}
