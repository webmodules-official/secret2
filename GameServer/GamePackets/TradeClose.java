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

public class TradeClose implements Packet {
	 private WMap wmap = WMap.getInstance();
	 private static int inc = 0;
	 
	 
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
		/*for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" "); }
		System.out.println("");*/
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		
		
		//click O.K aka Ready and finish.
		if(decrypted[0] == 1){
			if(wmap.Trade.containsKey(cur.TradeUID)){
				wmap.Trade.get(cur.TradeUID).ReadyandFinish(cur.charID ,decrypted);
				}else{
					Charstuff.getInstance().respondguild("You are in the wrong Trade window.2", cur.GetChannel());
				}
			return null;
		}
		
		
		//click close aka kill it.
		if(decrypted[0] == 0){
		// if trade exist
		if(wmap.Trade.containsKey(cur.TradeUID)){
		wmap.Trade.get(cur.TradeUID).CancelTrade();
		}else{
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[36];
		fury[0] = (byte)0x24;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x19;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		fury[12+i] = chid[i]; // charid
		fury[28+i] = chid[i]; // charid
		}
		fury[18] = (byte)0x9d;
		fury[19] = (byte)0x0f;
		fury[19] = (byte)0xbf;
		fury[33] = (byte)0x9d;
		fury[34] = (byte)0x0f;
		fury[35] = (byte)0xbf;
		cur.TradeUID = 0;
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);
		}
		return null;
		}
		return null;
	}	
}
