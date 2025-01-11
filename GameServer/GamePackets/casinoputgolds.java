package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Casino;
import Player.Character;
import Player.Charstuff;
import Player.Party;
import Player.PlayerConnection;
import Player.Trade;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;

import Encryption.Decryptor;

public class casinoputgolds implements Packet {
	 private WMap wmap = WMap.getInstance();
	 private static int inc = 1;
	 
	 
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		//System.out.println("");
		///for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("");
		//System.out.println("");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		
			if(cur != null){
			// SLOT 1 - 14 = 2g,10g,100g (decrypted[1] = 2 , 3 , 4)
			// SLOT 15 - 18 = 5g,50g,300g (decrypted[1] = 2 , 3 , 4)	
				int SLOT = (int)decrypted[0];
				int Gslot = (int)decrypted[1];
				int gold = 0;
				if(SLOT > 0 && SLOT < 15 && Gslot == 2){gold = 10;}
				if(SLOT > 0 && SLOT < 15 && Gslot == 3){gold = 100;}
				if(SLOT > 0 && SLOT < 15 && Gslot == 4){gold = 1000;}
				
				if(SLOT > 14 && SLOT < 19 && Gslot == 2){gold = 50;}
				if(SLOT > 14 && SLOT < 19 && Gslot == 3){gold = 500;}
				if(SLOT > 14 && SLOT < 19 && Gslot == 4){gold = 5000;}
				Casino.getInstance().addgold(SLOT, cur.getCharID(), gold);
			}

		
		return null;
	}	
}
