package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class mhmarketbuy implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling mhmarketbuy ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
	
		

		
		int one = 0;
		if(one == 0){Charstuff.getInstance().respond("Buy from www.globalmartialheroes.com/gmhmarket.php" ,con); return null;}else{
		//PERFECT WAY TO KNOW WHATS WHAT !
		int one_tan = 0;
		
		//for(int i=0;i<decrypted.length;i++) {System.out.print(one_tan+":"+decrypted[i]+" "); one_tan++;}
		Player curplayer = ((PlayerConnection)con).getPlayer();
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		byte[] fury = new byte[156];
		fury[0] = (byte)0x9c;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x4b;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
			fury[18+i] = decrypted[0];
		}
		fury[16] = (byte)0x01;
		
		fury[28] = (byte)0x00;
		fury[29] = (byte)0x96;
		fury[30] = (byte)0x77;
		fury[31] = (byte)0x2e;
		
		byte[] item1 = BitTools.intToByteArray(283000345);
		
		for(int i=0;i<4;i++) {
			fury[36+i] = item1[i];
		}
		
		fury[34] = decrypted[68];
		fury[35] = decrypted[58];
		fury[40] = (byte)0x01; // must be dynamic from DB
		
		int price = 1000;
		
		int newmhp = curplayer.getmhpoints() - price;
		curplayer.setmhpoints(newmhp);
		//CharacterDAO.setmhpoints();
		byte[] mhpleft = BitTools.intToByteArray(newmhp);
		for(int i=0;i<4;i++) {
			fury[152+i] = mhpleft[i]; 
		}
		
		//System.out.println("DONE");
		return fury;
	}}
	
}
