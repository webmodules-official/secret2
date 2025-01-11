package GameServer.GamePackets;


import java.nio.ByteBuffer;


import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;
import Player.Player;
import Player.PlayerConnection;
import Player.Character;

public class Movetovv implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}
	
	

	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("=> Move to Valley Village");
		
		Character ch = null;
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];

		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" ");
			
		}
		
		Player polishPlayer = ((PlayerConnection)con).getPlayer();
		//if(polishPlayer != null) {
			ch = polishPlayer.getCharacters().get((int)decrypted[0]); // get char id from 1 to 5
			polishPlayer.setActiveCharacter(ch);
			//System.out.print(" Im in! ");
			
			float x = -1502;
			float y = 2585;
			int map = 1;
			
			ch.setCurrentMap(map);
			ch.setX(x);
			ch.setY(y);
			
			String getcharnamewhomadethecommand = ch.getLOGsetName();
			CharacterDAO.telexymapfloat(x, y, map, getcharnamewhomadethecommand);	
			
			byte[] movetovv = new byte[24];
			
			
			movetovv[0] = (byte)movetovv.length;
			movetovv[4] = (byte)0x03;
			movetovv[6] = (byte)0x0e;
			movetovv[8] = (byte)0x01; 
			movetovv[9] = decrypted[0]; // char id from select screen 1-5 
			//movetovv[10] = (byte)0x60; 
			//movetovv[11] = (byte)0x60; 
			
			movetovv[12] = (byte)0x01; // Map
			
			movetovv[17] = (byte)0x80; // X
			movetovv[18] = (byte)0xcf; 
			movetovv[19] = (byte)0xc4; 
			
			movetovv[21] = (byte)0x80; // Y
			movetovv[22] = (byte)0x12; 
			movetovv[23] = (byte)0x45; 
			
			
			//TODO: go from this packet to SelectedCharacter.java and also send the  MAP , X , Y
			// this packet only determines the MAP ,X, Y -> goes over to slectedcharacter
			
			
			return movetovv;
		//}
	//	return buffyTheVampireSlayer;
		
	}}
