package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import Connections.Connection;
import Encryption.Decryptor;
import GameServer.GamePackets.UseItemList.Cleanse;
import GameServer.GamePackets.UseItemList.EvilnLawfulword;
import GameServer.GamePackets.UseItemList.Factionpill;
import GameServer.GamePackets.UseItemList.Junpot;
import GameServer.GamePackets.UseItemList.MHPointsGift;
import GameServer.GamePackets.UseItemList.POTSone;
import GameServer.GamePackets.UseItemList.POTStwo;
import GameServer.GamePackets.UseItemList.Potion;
import GameServer.GamePackets.UseItemList.Resetskills;
import GameServer.GamePackets.UseItemList.RevivalAmulet;
import GameServer.GamePackets.UseItemList.Scrollteleport;
import GameServer.GamePackets.UseItemList.Showtoothers;

public class useitem implements Packet {
	private static int inc = 0;
	private static long time = 0;
	
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
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
	    //PERFECT WAY TO KNOW WHATS WHAT !
		//for(int ic=0;ic<decrypted.length;ic++) {System.out.print(decrypted[ic]+" ");}
		//System.out.println("");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("");
		//System.out.println("");
		
		if(decrypted[0] == 2){cur.KillInvFreeze();return null;}
		
		if(cur.Duel == 1){cur.KillInvFreeze();return null;}
		int one = BitTools.byteToInt(decrypted[1]);

		byte[] DETERMINERR = new byte[4];
		for(int i=0;i<4;i++){DETERMINERR[i] = (byte)decrypted[i+4];}
		
		if(cur.getInventorySLOT(one) == 0){
			cur.DeleteItemNOMESSAGE(one); 
			Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		
		// some checkups
		if(cur.getInventorySLOT(one) == 212100199 && cur.getLevel() < 50){
			cur.KillInvFreeze();
			return null;
		}
		if(cur.getInventorySLOT(one) == 212100200 && cur.getLevel() < 70){
			cur.KillInvFreeze();
			return null;
		}
		if(cur.getInventorySLOT(one) == 212100201 && cur.getLevel() < 90){
			cur.KillInvFreeze();
			return null;
		}
		if(cur.getInventorySLOT(one) == 212100202 && cur.getLevel() < 110){
			cur.KillInvFreeze();
			return null;
		}
		
				// Direct use
		if(cur.getInventorySLOT(one) == 273001251
		||cur.getInventorySLOT(one) == 213062463
		||cur.getInventorySLOT(one) == 283000002
		||cur.getInventorySLOT(one) == 283000001
		){
			//System.out.println("WINNER;");
			byte[] chid = BitTools.intToByteArray(cur.getCharID());
			try{
					byte[] item = new byte[52];
					int newstack;
					if(Charstuff.getInstance().tryInvincible_items(cur.getInventorySLOT(one))){
					newstack = 1;// if its in the invinceble list, then dont substract
					}else{
					newstack = cur.getInventorySTACK(one) - 1;
					}
					item[19] = (byte)one; // inventory slot
					boolean used = UseItemParser.getInstance().parseAndExecuteUseItem(new Integer (cur.getInventorySLOT(one)), BitTools.byteArrayToInt(DETERMINERR), ServerFacade.getInstance().getCon().getConnection(cur.GetChannel()));
					if(used == true){
					if(newstack <= 0){	
						cur.DeleteInvItem(Integer.valueOf(one));
					}else{cur.setInventorySTACK(one, newstack);}

					byte[] NEWSTACK = BitTools.intToByteArray(newstack);
					item[0] = (byte)0x34;
					item[4] = (byte)0x04; // 0x05 = for external packet ( to players)
					item[6] = (byte)0x05;
					item[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						item[12+i] = chid[i]; 
						item[28+i] = chid[i]; 
					}
					if(newstack <= 0){
						item[20] = (byte)0x00;
						item[21] = (byte)0x00;	
					}
					else{
						for(int i=0;i<2;i++) {
							item[20+i] = NEWSTACK[i]; // New stack on inv slot
						}
					}
					item[16] = (byte)0x01;
					item[18] = (byte)0x01;
					item[24] = (byte)0x01;
					item[49] = (byte)0x15;
					item[50] = (byte)0x76;
					item[51] = (byte)0x2a;
					ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), item);
					}else{cur.KillInvFreeze();}
				}catch (ConcurrentModificationException e) {
	    			 //System.out.println(This.getLOGsetName()+"- UseItems1: "+e);
			     }
				 catch (NullPointerException e) {
					 //System.out.println(This.getLOGsetName()+"- UseItems2: "+e);
			     }
	    		 catch (Exception e) {
	    			 //System.out.println(This.getLOGsetName()+"- UseItems3: "+e);
			     }
			
		}else{ 	// Delayed use
		Connection tmp = ServerFacade.getInstance().getCon().getConnection(cur.GetChannel());
		if(tmp.isPlayerConnection()){
			PlayerConnection tmplc = (PlayerConnection) tmp;
			tmplc.getPlayer().ItemQueue1 = one;
			tmplc.getPlayer().ItemQueue2 = BitTools.byteArrayToInt(DETERMINERR);
		}}
		//System.out.println("USEITEM: " +cur.getInventorySLOT(one)+" - " +BitTools.byteArrayToInt(DETERMINERR));
		//System.out.println("return null;");
		return null;
	}
}
