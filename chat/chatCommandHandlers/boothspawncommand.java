package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import chat.ChatCommandExecutor;

public class boothspawncommand implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling booth spawning command");
		byte[] booth = new byte[52];
		
		booth[0] = (byte)booth.length;
		booth[4] = (byte)0x04;
		booth[6] = (byte)0x37;
		booth[8] = (byte)0x01;
		booth[9] = (byte)0x9d;
		booth[10] = (byte)0x0f;
		booth[11] = (byte)0xbf;
		
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
		booth[18] = (byte)0x01;
		
	
		byte[] msg = parameters[0].getBytes(); // <--- real gm msg lol
		for(int i=0;i<msg.length;i++) {
			booth[i+19] = msg[i];
		}
		
		booth[50] = (byte)0x0f; 
		booth[51] = (byte)0xbf; // = " end of packet "
		
		
		source.addWrite(booth);
		
		//System.out.println("DONE");
	}

}
