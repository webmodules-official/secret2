package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Tools.BitTools;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import chat.ChatCommandExecutor;

public class refresharea implements ChatCommandExecutor {
 

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling gold");

		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		long goldz = Integer.valueOf(parameters[0]);
		
		cur.leaveGameWorld();
		cur.joinGameWorld();	
	}

}
