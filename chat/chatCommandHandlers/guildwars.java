package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Mob.MobController;
import Player.Character;
import Player.Guild;
import Player.Guildwar;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Configuration.ConfigurationManager;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class guildwars implements ChatCommandExecutor {
private static int inc = 0;
	
	public void execute(String[] parameters, Connection source) {
		int one = Integer.valueOf(parameters[0]);	// mob id
		Guildwar.getInstance().Setupguildwar();	
		}
	
	}
	
