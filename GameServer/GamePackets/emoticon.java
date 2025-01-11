package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Encryption.Decryptor;

public class emoticon implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling emoticons");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
	/*	for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" "); 
			
		}
		//System.out.println(" | ");
		for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));
			
		}*/
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[24];
		
	
		fury[0] = (byte)0x18;
		fury[4] = (byte)0x05;
		fury[6] = (byte)0x06;
		fury[8] = (byte)0x01;
		
	
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		fury[16] = decrypted[0];
		
		fury[18] = decrypted[2];
		fury[19] = decrypted[3];

		cur.sendToMap(fury);
		//System.out.println("DONE");
		return fury;
	}
	
}
