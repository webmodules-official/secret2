package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import chat.ChatCommandExecutor;

public class castspeed implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling movement speed 1/2/3/4 command");
		byte[] booth = new byte[28];
		
		booth[0] = (byte)booth.length;
		booth[4] = (byte)0x05;
		booth[6] = (byte)0x34;
		booth[8] = (byte)0x01;
		booth[9] = (byte)0x68;
		booth[10] = (byte)0x15;
		booth[11] = (byte)0x08;
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		for(int i=0;i<4;i++) {
			booth[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		
		//booth[12] = (byte)0x03; // c5 = charID , aka this [12] <-----------------
		//booth[13] = (byte)0xb7;
		//booth[14] = (byte)0x62;        ??????????????? extra bytes in orginal packet
		//booth[15] = (byte)0x02;
		booth[16] = (byte)0x01;
		booth[18] = (byte)0x0f;
		booth[19] = (byte)0xbf;
		booth[20] = (byte)0x0b;
		
		booth[25] = (byte)0xca;  
		booth[27] = (byte)0xa0; 
		booth[26] = (byte)0x04;
		
		
		source.addWrite(booth);
		
		//System.out.println("DONE");
	}

}
