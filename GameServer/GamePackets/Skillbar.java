package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class Skillbar implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling Skillbar");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" "); }

		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		
		byte[] booth = new byte[24];
		
		booth[0] = (byte)booth.length;
		booth[4] = (byte)0x04;
		booth[6] = (byte)0x11;
		booth[8] = (byte)0x01;
		booth[9] = (byte)0x06;
		booth[10] = (byte)0x15; 
		booth[11] = (byte)0x08;
		
		for(int i=0;i<4;i++) {
			booth[12+i] = chid[i]; 
		}
		booth[16] = (byte)0x01;
		booth[18] = (byte)0x01;	
		booth[19] = (byte)0x01;	
		
		booth[18] = decrypted[0];
		booth[19] = decrypted[1];
		booth[20] = decrypted[4]; 
		booth[21] = decrypted[5]; 
		booth[22] = decrypted[6]; 
		booth[23] = decrypted[7];
		
		byte[] ItemOrSkill = new byte[4];
		for(int i=0;i<4;i++) {
			ItemOrSkill[i] = decrypted[4+i];
		}
		
		int ItemOrSkillz = BitTools.byteArrayToInt(ItemOrSkill); 
		cur.setskillbar(decrypted[0], decrypted[1], ItemOrSkillz); 
				
		//System.out.println("DONE");
		return booth;
	}
	
}
