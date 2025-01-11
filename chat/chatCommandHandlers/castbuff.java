package chat.chatCommandHandlers;

import java.nio.ByteBuffer;



import Tools.BitTools;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class castbuff implements ChatCommandExecutor {
 

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling castspell command");
		byte[] partychat = new byte[96];
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		partychat[0] = (byte)0x2c;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x1f;
		partychat[8] = (byte)0x01;//end of skill indicator
		partychat[9] = (byte)0x99; // spell id ?
		partychat[10] = (byte)0x0f;
		partychat[11] = (byte)0xbf;
		for(int i=0;i<4;i++) {
			partychat[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		//partychat[12] = (byte)0xb4; // char ID ???
		//partychat[13] = (byte)0xb7;
		//partychat[14] = (byte)0x62;
		//partychat[15] = (byte)0x02;
		partychat[16] = (byte)0x04;
		partychat[20] = (byte)0x15;
		partychat[22] = (byte)0x2e; // spell effect id
		partychat[24] = (byte)0x09; // spell effect id
		partychat[26] = (byte)0x01;
		partychat[28] = (byte)0x74;
		partychat[29] = (byte)0x01;
		partychat[32] = (byte)0x74;
		partychat[33] = (byte)0x01;
		
		partychat[36] = (byte)0x35;
		partychat[37] = (byte)0x01;
		partychat[38] = (byte)0x35;
		partychat[39] = (byte)0x01;
		partychat[40] = (byte)0xe8;
		
		partychat[42] = (byte)0xe8;
		partychat[44] = (byte)0x34;
		partychat[48] = (byte)0x05;
		partychat[50] = (byte)0x34;
		partychat[52] = (byte)0x01;
		for(int i=0;i<4;i++) {
			partychat[56+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		//partychat[56] = (byte)0xb4; // char id ?? for animation perhaps??
		//partychat[57] = (byte)0xb7;
		//partychat[58] = (byte)0x62;
		//partychat[59] = (byte)0x02;
		partychat[60] = (byte)0x01; 
		partychat[64] = (byte)0xaa;
		partychat[65] = (byte)0x87;
		partychat[66] = (byte)0x01;
		

		partychat[69] = (byte)0x07;
		partychat[71] = (byte)0x01;
		partychat[72] = (byte)0x01;
		for(int i=0;i<4;i++) {
			partychat[76+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		//partychat[76] = (byte)0xb4; // some ID DAFUQ IS TIHS?  
		//partychat[77] = (byte)0xb7;
		//partychat[78] = (byte)0x62;
		//partychat[79] = (byte)0x02;
		partychat[80] = (byte)0x03;
		partychat[84] = (byte)0x74;
		partychat[85] = (byte)0x01;
		partychat[92] = (byte)0x35;
		partychat[93] = (byte)0x01;
		
		
	
			ServerFacade.getInstance().addWriteByChannel(source.getChan(), partychat);
		
			//System.out.println("DONE");
	}

}
