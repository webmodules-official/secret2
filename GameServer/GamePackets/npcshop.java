package GameServer.GamePackets;

import item.Item;
import item.ItemCache;

import java.nio.ByteBuffer;

import timer.SystemTimer;

import npc.Npc;

import Mob.Mob;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.itemprice;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class npcshop implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling npc shop BUY ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		int zero = BitTools.byteToInt(decrypted[0]);
		int one = BitTools.byteToInt(decrypted[1]);
		int two = BitTools.byteToInt(decrypted[2]);
		int three = BitTools.byteToInt(decrypted[3]);
		int four = BitTools.byteToInt(decrypted[4]);
		int five = BitTools.byteToInt(decrypted[5]);
		int six = BitTools.byteToInt(decrypted[6]);
		int seven = BitTools.byteToInt(decrypted[7]);
		int eight = BitTools.byteToInt(decrypted[8]);
		
		int invslot = BitTools.byteToInt(decrypted[5]);
		int invheight = BitTools.byteToInt(decrypted[6]);
		int invweight = BitTools.byteToInt(decrypted[7]);
	
		/* decrypted[4] = npcshop windowslotid 
		 * decrypted[5] = invslot 
		 * decrypted[6] = invslot y
		 * decrypted[7] = invslot x
		 * decrypted[8] = stack
		 */
		byte[] npcidz = new byte[4];
		for(int i=0;i<2;i++) {
			npcidz[i] = decrypted[i];
		}
		//System.out.print("PACKET HEADER:["+BitTools.byteArrayToInt(npcidz)+"]");
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		//System.out.println("");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		
		Npc Targetnpc = WMap.getInstance().GetNpc(BitTools.byteArrayToInt(npcidz)); 
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		byte[] fury = new byte[56];
		fury[0] = (byte)0x38;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x13;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
		}
		if(decrypted[8] != 0) // = pots & merchandise & items ( equips etc ) 
		{
			int itemid = Targetnpc.getshopitemids(four);
			if(!Charstuff.getInstance().tryStackable_items(itemid)){/*System.out.println("npcshop1");*/cur.KillInvFreeze();return null;}
			int stackable = Charstuff.getInstance().getStackable_items(itemid);
			int invSTACK = cur.getInventorySTACK(five) + eight;
			long sellpricee = itemprice.getBuyprice(itemid) * eight;
			
			//if is not stackable
			if(stackable == 0){
				// if target inv slot is not 0 then return null;
				//if(cur.getInventorySLOT((int)decrypted[5]) != 0){System.out.println("npcshop2");cur.ClearInv();return null;}
				invSTACK = 1;
				sellpricee = itemprice.getBuyprice(itemid) * 1;
			}
			
			if(cur.getInventorySLOT(invslot) != 0 && stackable == 0){
				cur.respondguildTIMED("Select an empty Inventory slot.", cur.GetChannel());
				cur.KillInvFreeze();
				return null;
			}
			
			if(stackable != 0 && cur.getInventorySLOT(invslot) != 0 && cur.getInventorySLOT(invslot) != itemid){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", cur.GetChannel()); return null;}
			
			
			long newgoldz = cur.getgold() - sellpricee;
			if(newgoldz < 0 ){return null;}
			//System.out.println("1:"+cur.getgold()+"2:"+ itemprice.getBuyprice(itemid)+" * "+eight+" = "+sellpricee+" 3:"+newgoldz);
			cur.setgold(newgoldz);
			CharacterDAO.setgold(newgoldz, cur.charID);
			
			byte[] newgold = BitTools.LongToByteArrayREVERSE(newgoldz);
			byte[] itid = BitTools.intToByteArray(itemid);
			
			for(int i=0;i<4;i++) {
			    fury[48+i] = itid[i];
			}
			for(int i=0;i<newgold.length;i++) {
			    fury[16+i] = newgold[i];
			}
			
			fury[24] = (byte)0x01;
			
			fury[26] = decrypted[5]; // invSLOT
			fury[27] = decrypted[6]; // invY
			fury[28] = decrypted[7]; // invX
			fury[29] = (byte)0x00;  // vending points WTFF
	
			fury[52] = decrypted[8]; // stack
			

			cur.inventory = cur.getInventorySLOT(invslot);
			cur.inventorystacktwo = cur.getInventorySTACK(invslot);
			
			cur.setInventory(invslot, itemid, invheight, invweight, invSTACK);
			String e0 = null;
			if(ItemCache.Items.containsKey(itemid)){e0 = ItemCache.Items.get(itemid);}
			if(e0 != null){
			String[] item0 = e0.split(",");
			if(Long.valueOf(item0[22]) != 0){
			long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item0[22])) + System.currentTimeMillis();
			cur.setitem_end_date(itemid, minato);
			cur.expireactive();
			}}
		}
		else 
		{	// open shop window \\ 
			fury[16] = (byte)0x47;
			fury[17] = (byte)0x76;
			fury[18] = decrypted[0];
			
			fury[24] = (byte)0x01;
			
			fury[29] = (byte)0x47;
			
			fury[32] = (byte)0x4b;
			fury[33] = (byte)0x6b;
			
			fury[38] = (byte)0x80;
			fury[39] = (byte)0x3f;
			
			fury[42] = (byte)0x80;
			fury[43] = (byte)0x3f;
			
		}
		
		
		//System.out.println("DONE");
		return fury;
	}
	
}
