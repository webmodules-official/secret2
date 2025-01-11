package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class icons implements ChatCommandExecutor {
private int inc = 0;
	
	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		int buffid1 = Integer.valueOf(parameters[0]);
		int bufftime1 = 4500;
		int buffvalue1 = 30;
		int buffslot1 = 10;

		byte[] buff = new byte[44];
		 buff[0] = (byte)0x2c; 
		 buff[4] = (byte)0x05;
		 buff[6] = (byte)0x1f;
		 buff[8] = (byte)0x01;
		 for(int i=0;i<4;i++){
		 buff[12+i] = chid[i]; 				
		 }	
		 buff[26] = (byte)0x01; 
		 buff[28] = (byte)0x89; 
		 buff[32] = (byte)0x89; 
		 buff[36] = (byte)0x7e; 
		 buff[38] = (byte)0x7e; 
		 buff[40] = (byte)0x60; 
		 buff[42] = (byte)0x60;
		 
		 byte[] buffidz1 = BitTools.intToByteArray(buffid1); 
		 byte[] bufftimez1 = BitTools.intToByteArray(bufftime1);
		 byte[] buffvaluez1 = BitTools.intToByteArray(buffvalue1);
		 byte[] buffslotz1 = BitTools.intToByteArray(buffslot1);
		 
			 for(int i=0;i<2;i++) {
				 buff[i+16] = buffslotz1[i];	 // buffslot
				 buff[i+20] = buffidz1[i];	 // buff id
				 buff[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
				 buff[i+24] = buffvaluez1[i]; // Value XXXXX
			 }	
			 inc++;
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), buff); 
			cur.sendToMap(buff);
		}
	}
	
