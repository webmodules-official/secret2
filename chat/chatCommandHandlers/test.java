package chat.chatCommandHandlers;


import item.ItemCache;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import timer.SystemTimer;


import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;



public class test implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		String CharName = (parameters[0]);
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		cur.fametitle = Integer.valueOf(parameters[0]);
		cur.sendToMap(cur.extCharPacket());
	}
}

	