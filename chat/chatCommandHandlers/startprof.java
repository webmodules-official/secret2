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

public class startprof implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		int Myou = Integer.valueOf(parameters[0]);

		byte[] decrpyted = new byte[16];
		decrpyted[0] = (byte)Myou;
		
		// CALC THIS SHIT AND GG create invs farm points in map, add determiner to it if herb /mining/ fishing
		
		//if player contains itemid thenge that slot
		int itemid = 215003115;
		int invSLOT = BitTools.ValueToKey(itemid, cur.InventorySLOT);
		decrpyted[8] = (byte)invSLOT; // TOOL ID
		decrpyted[11] = (byte)cur.getInventoryWEIGHT(invSLOT);// TOOL X
		decrpyted[12] = (byte)cur.getInventoryWEIGHT(invSLOT);// TOOL Y
		cur.Proffession(decrpyted);
	}
}
	
