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

public class TradePutItem implements Packet {
	 private WMap wmap = WMap.getInstance();
	 
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
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		
		int one = BitTools.byteToInt(decrypted[1]); // from invslot
		//int two = BitTools.byteToInt(decrypted[2]); // to slot ?
		
		int two = BitTools.byteToInt(decrypted[16]);// to slot
		int X = BitTools.byteToInt(decrypted[17]); // X
		int Y = BitTools.byteToInt(decrypted[18]); // Y
		
		//check for nontradable item
		
		// put gold
		if((byte)decrypted[0] == (byte)0xff){
			if(cur.TradeUID != 0 && wmap.Trade.containsKey(cur.TradeUID)){	
				byte[] golds = new byte[8];
				for(int i=0;i<8;i++) {
					golds[i] = decrypted[8+i];
				}
				long gold = BitTools.ByteArrayToLong(golds);
				
				if(gold >= 1000000000000L){Charstuff.getInstance().respondguild("Can put only 1 Million gold maximum.", cur.GetChannel()); return null;}
				if(cur.getgold() - gold < 0){return null;}
				
				wmap.Trade.get(cur.TradeUID).PutGold(cur.charID, gold);
			}else{
				Charstuff.getInstance().respondguild("Trade window ID == 0 or doesnt exist anymore.", cur.GetChannel());	
			}
			return null;
		}
		
		// check if item is 0 or 1337 or tradable
		if(cur.getInventorySLOT(one) == 0){
			cur.DeleteItemNOMESSAGE(one); 
			Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(one) == 1337){
			Charstuff.getInstance().respondguild("ItemID == 1337, report to GM", cur.GetChannel());
			return null;
		}
		
		if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT(one))){cur.KillInvFreeze();return null;}
		
		// put item
		if(decrypted[0] == 1){
			if(cur.TradeUID != 0 && wmap.Trade.containsKey(cur.TradeUID)){	
				//int CharID, int SLOT, int ItemID, int Stack, int X, int Y
				wmap.Trade.get(cur.TradeUID).PutItem(cur.charID, one, two, cur.getInventorySLOT(one), cur.getInventorySTACK(one), X, Y);
			}else{
				Charstuff.getInstance().respondguild("Trade window ID == 0 or doesnt exist anymore.", cur.GetChannel());	
			}
			return null;
		}
		
		return null;
	}	
}
