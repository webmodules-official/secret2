package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Casino;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import chat.ChatCommandExecutor;

public class playcasino implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		
		Casino.getInstance().DicesFallign(cur, Integer.valueOf(parameters[0]), Integer.valueOf(parameters[1]));
	
			
			/*byte[] famePacket = new byte[192];		
			famePacket[0] = (byte)famePacket.length;
			famePacket[4] = (byte)0x04;
			famePacket[6] = (byte)0x64;
			famePacket[8] = (byte)0x01;
			famePacket[16] = (byte)0x05;
			famePacket[18] = (byte)(int)Integer.valueOf(parameters[0]);
			famePacket[20] = (byte)(int)Integer.valueOf(parameters[1]);
			cur.setFame(cur.getFame());
			byte[] chid = BitTools.intToByteArray(cur.getCharID());
			byte[] fset = BitTools.intToByteArray(cur.getCharID());
			for(int a=0;a<4;a++) {
				famePacket[12+a] = chid[a];
				famePacket[24+a] = fset[a];
			}
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), famePacket); 
		*/
	}

}
