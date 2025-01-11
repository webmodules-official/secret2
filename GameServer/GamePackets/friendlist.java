package GameServer.GamePackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class friendlist implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling friends window");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" "); 
			
		}
		//System.out.println(" | ");
		for(int i=0;i<decrypted.length;i++) {//System.out.printf("%02x ", (decrypted[i]&0xFF)); 
			
		}
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		
		byte[] fiendslist = new byte[40];
		
		fiendslist[0] = (byte)fiendslist.length;
		fiendslist[4] = (byte)0x04;
		fiendslist[6] = (byte)0x31;
		fiendslist[8] = (byte)0x01;
		
		fiendslist[9] = (byte)0xa2;
		fiendslist[10] = (byte)0x06;
		fiendslist[11] = (byte)0x08;
		
		for(int i=0;i<4;i++) {
			fiendslist[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		
		//booth[12] = (byte)0xb4; // c5 = charID , aka this [12] <-----------------
		//booth[13] = (byte)0xb7;
		//booth[14] = (byte)0x62;       
		//booth[15] = (byte)0x02;
		fiendslist[16] = (byte)0x01;
		
		fiendslist[18] = decrypted[0]; // <-- i guess putting a " decrypted[0]" works fine?
		fiendslist[19] = decrypted[1];
		for(int i=0;i<15;i++) {
			fiendslist[20+i] = decrypted[2+i];
		}

		fiendslist[37] = (byte)0x9e;
		fiendslist[38] = (byte)0x0f;
		fiendslist[39] = (byte)0xbf;
		
		
		byte[] friendzname = new byte[17];
		for(int i=0;i<17;i++) {
			friendzname[i] = decrypted[2+i];
		}
		String CharName = BitTools.byteArrayToString(friendzname); 

        byte[] hisname = new String(CharName).getBytes();
        
        byte arr[] = hisname;
        try {
        		int i;
        		for (i = 0; i < arr.length && arr[i] != 0; i++) { }
        		String finalnameparsed = new String(arr, 0, i, "UTF-8");
	
		
		// decrypted[0] = disregard ( ignore )
		// decrypted[1] = friends list 
		if (decrypted[0] == 0){
		CharacterDAO.setwholist(decrypted[0], decrypted[1],finalnameparsed,cur.getCharID());}
		if (decrypted[0] == 1){
		CharacterDAO.setwholist(decrypted[0], decrypted[1],finalnameparsed,cur.getCharID());}
		cur.setwholist(decrypted[0], decrypted[1], finalnameparsed); 
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
		//System.out.println("DONE");
		return fiendslist;
		
	}
	
}
