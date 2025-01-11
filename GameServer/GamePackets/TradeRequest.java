package GameServer.GamePackets;

import java.nio.ByteBuffer;
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

public class TradeRequest implements Packet {
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
		/*for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		System.out.println("");
		for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		System.out.println("");
		System.out.println("");*/
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] newP = new byte[4];
		byte[] fury = new byte[28];
		fury[0] = (byte)0x1c;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x17;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		fury[12+i] = chid[i]; // charid
		newP[i] = decrypted[4+i];
		}
		fury[16] = (byte)0x01;
		fury[18] = decrypted[0]; // 0x00 = null | 0x01 = accepted | 0x02 is requesting party
		fury[19] = (byte)0x08;
	
		int newp = BitTools.byteArrayToInt(newP);
		Character Leader = wmap.getCharacter(newp);
		
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
			fury[20+i] = chid[i]; 
			fury[24+i] = chid[i]; 
		}
	
		if(decrypted[0] == 0){ // if decline
			if(cur != null){
			//p1
			byte[] P1chid = BitTools.intToByteArray(cur.getCharID());
			byte[] P2chid = BitTools.intToByteArray(Leader.getCharID());
			byte[] fury1 = new byte[28];
			fury1[0] = (byte)0x1c;
			fury1[4] = (byte)0x04;
			fury1[6] = (byte)0x17;
			fury1[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
			fury1[12+i] = P1chid[i]; // charid
			fury1[20+i] = P1chid[i]; // charid
			fury1[24+i] = P2chid[i]; // charid
			}
			fury1[16] = (byte)0x01;
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury1);
			}
			return null;
		}
		
		if(decrypted[0] == 1){ // if accept
			if(Leader.TradeUID == 0){	
				inc++;
				cur.TradeUID = inc;
				Leader.TradeUID = inc;
				Trade trade = new Trade(cur.charID, Leader.charID);
				wmap.Trade.put(inc, trade);
				}else{Charstuff.getInstance().respond("Character is already Trading!", con);}
			return null;
		}
		
		if(decrypted[0] == 2){ // request party
		if(Leader.TradeUID == 0){
			ServerFacade.getInstance().addWriteByChannel(Leader.GetChannel(), fury);
		}else{Charstuff.getInstance().respond("Character is already Trading!", con);}
		}
		return null;
	}	
}
