package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Mob.MobController;
import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Configuration.ConfigurationManager;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class mobspawn implements ChatCommandExecutor {
private static int inc = 0;
	
	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		int one = Integer.valueOf(parameters[0]);	// mob id
		int two = 1;   // mob count
		int three = 50; // spawn radius
		int mobid, count, pool;
		int []data = new int[]{0,0,0,0,0,0,0};
		inc++;
		pool = ConfigurationManager.getConf("world").getIntVar("mobUIDPool") + inc;
		
		mobid = one;
		count = two;
		data[0] = cur.getCurrentMap();
		data[1] = (int)cur.getlastknownX();
		data[2] = (int)cur.getlastknownY();
		data[3] = three;
		data[4] = 10;
		data[5] = 10;
		data[6] = 10;
		
		MobController run = new MobController(mobid, count, pool, data);
		pool += count;
		cur.leaveGameWorld();
		cur.getPlayer().Rarea = true;	
		}
	
	}
	
