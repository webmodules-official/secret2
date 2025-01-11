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

public class casinowindowrequest implements Packet {
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
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("");
		//System.out.println("");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		
		if(decrypted[0] == 0){ // get current global casino window that all the players are having.
			if(cur != null){
				Casino.getInstance().UpdateToMe(cur);
			}
			return null;
		}
		
		/*if(decrypted[0] == 1){ // ????????
			if(Leader.TradeUID == 0){	
				inc++;
				cur.TradeUID = inc;
				Leader.TradeUID = inc;
				Trade trade = new Trade(cur.charID, Leader.charID);
				wmap.Trade.put(inc, trade);
				}else{Charstuff.getInstance().respond("Character is already Trading!", con);}
			return null;
		}
		
		if(decrypted[0] == 2){ // ????????
		if(Leader.TradeUID == 0){
			ServerFacade.getInstance().addWriteByChannel(Leader.GetChannel(), fury);
		}else{Charstuff.getInstance().respond("Character is already Trading!", con);}
		}*/
		return null;
	}	
}
