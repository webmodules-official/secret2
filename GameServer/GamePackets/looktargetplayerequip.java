package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Encryption.Decryptor;

public class looktargetplayerequip implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling looktargetplayerequip ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" ");
			
		}
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[28];
		
	
		fury[0] = (byte)0x1c;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x1e;
		fury[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
			fury[20+i] = decrypted[4+i];
		}
		fury[16] = (byte)0x01;
		
		fury[18] = (byte)0xf8;
		fury[19] = (byte)0x01;
		
	  /*fury[20] = (byte)0x8c;
		fury[21] = (byte)0x0b;
		fury[22] = (byte)0xa1;*/
		
		fury[25] = (byte)0x9e;
		fury[26] = (byte)0x0f;
		fury[27] = (byte)0xbf;


		//System.out.println("DONE");
		return fury;
	}
	
}
