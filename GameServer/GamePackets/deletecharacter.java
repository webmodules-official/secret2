package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.Player;
import Player.PlayerConnection;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class deletecharacter implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling delete character ");
		
		Character ch = null;
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		
		Player polishPlayer = ((PlayerConnection)con).getPlayer();
		ch = polishPlayer.getCharacters().get((int)decrypted[0]); // get char id from 1 to 5
		polishPlayer.setActiveCharacter(ch); // HURR DURR UNCLE LOVE BEAR FURR

		byte[] fury = new byte[11];
		
		fury[0] = (byte)fury.length;
		fury[4] = (byte)0x03;
		fury[6] = (byte)0x07;
		fury[8] = (byte)0x01;
		
		fury[9] = decrypted[0];
		

		if (ch.getdeletestate() == 1) { // state 
		if(decrypted[1] == 1){ // recover
		fury[10] = (byte)0x00; 
		ch.setdeletestate(0);
		}	
		else{
			
		fury[10] = (byte)0x02;  // Perm Delete
		CharacterDAO.deletecharacter(ch.getLOGsetName());		
		((PlayerConnection)con).getPlayer().removeCharacter((int)decrypted[0]);
		}
		}
		else 
		{ // state 0
			
		if(decrypted[1] == 0){ fury[10] = (byte)0x01; }	// turn your back
		ch.setdeletestate(1);
		}
	
		
		
		//System.out.println("DONE");
		return fury;
	}
	
}
