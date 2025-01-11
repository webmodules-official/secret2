package chat.chatCommandHandlers;


import item.ItemCache;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import timer.SystemTimer;
import timer.globalsave;


import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;



public class time implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		String CharName = (parameters[0]);

		SystemTimer.GetTime();
		SystemTimer.display();
	}
}

	