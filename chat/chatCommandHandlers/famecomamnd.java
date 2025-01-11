package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class famecomamnd implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Received chat command to set fame!");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);

			
			
			
			

				int fame = Integer.parseInt(parameters[0]);
				byte[] famePacket = new byte[192];
				
				famePacket[0] = (byte)famePacket.length;
				famePacket[4] = (byte)0x04;
				famePacket[6] = (byte)0x64;
				famePacket[8] = (byte)0x01;
				famePacket[16] = (byte)0x05;
				famePacket[18] = (byte)0x05;
				famePacket[20] = (byte)0x0F;
				
				Character cur = ((PlayerConnection)source).getActiveCharacter();
				int setcurrentfame = Integer.parseInt(parameters[0]);
				//System.out.println("current fame:" + setcurrentfame);
				cur.setFame(setcurrentfame);
				
				byte[] chid = BitTools.intToByteArray(cur.getCharID());
				byte[] fset = BitTools.intToByteArray(fame);
				
				for(int a=0;a<4;a++) {
					famePacket[12+a] = chid[a];
					famePacket[24+a] = fset[a];
				}
				//System.out.println("Setting character fame to: " + fame);
				source.addWrite(famePacket);
			
		}
	}

}