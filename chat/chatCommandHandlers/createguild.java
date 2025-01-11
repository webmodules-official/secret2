package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Tools.BitTools;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class createguild implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling guild create");
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] msg = parameters[0].getBytes(); // <--- real SHIT lol hue hue hue
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[56];
	
		fury[0] = (byte)0x38;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x3d;
		fury[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; // charid
		}
		fury[16] = (byte)0x01;
		
		fury[18] = (byte)0x01;
		fury[19] = (byte)0x02; // 0x01 = normal guild | 0x02 = MUN guild
		
		
		for(int i=0;i<msg.length;i++) {
			fury[20+i] = msg[i]; // parameter 0
		}

		
		fury[40] = (byte)0xd4;
		fury[41] = (byte)0x89;
		
		fury[44] = (byte)0x68;
		
		fury[48] = (byte)0x0d;
		fury[49] = (byte)0x29;
		fury[50] = (byte)0x05;
		
		byte[] fury1 = new byte[40];
		
		fury1[0] = (byte)0x28;
		fury1[4] = (byte)0x05;
		fury1[6] = (byte)0x41;
		fury1[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			fury1[12+i] = chid[i]; // charid
		}
		fury1[16] = (byte)0x02;
		
		//fury1[18] = (byte)0x01;
		//fury1[16] = (byte)0x02; // 0x01 = normal guild | 0x02 = MUN guild
		
		
		for(int i=0;i<msg.length;i++) {
			fury1[17+i] = msg[i]; // parameter 0
		}

		fury1[34] = (byte)0x68;


		
		
		cur.sendToMap(fury1);
		source.addWrite(fury); 
		
		//System.out.println("DONE");
		
	}

}
