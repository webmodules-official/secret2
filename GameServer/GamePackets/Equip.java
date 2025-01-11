
package GameServer.GamePackets;

import item.ItemCache;

import java.nio.ByteBuffer;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.Character;
import ServerCore.ServerFacade;
import Tools.BitTools;

public class Equip implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}


	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling equip");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++){System.out.print(decrypted[i]+" ");}
		//System.out.println("");
		String e0;
		byte[] eq = new byte[24];
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		int zero = BitTools.byteToInt(decrypted[0]);
		int one = BitTools.byteToInt(decrypted[1]);
		int two = BitTools.byteToInt(decrypted[2]);
		byte[] itemid = BitTools.intToByteArray(cur.getInventorySLOT(one));
		byte[] cid = BitTools.intToByteArray(cur.getCharID());
		//System.out.println("EQUIPMENT: "+zero +" "+ one+ " "+two);
		eq[0] = (byte)eq.length;
		eq[4] = (byte)0x04;
		eq[6] = (byte)0x0C;
		eq[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			eq[12+i] = cid[i];
		}
		eq[16] = (byte)0x01;
		eq[18] = decrypted[0];
		eq[19] = decrypted[1];
		eq[20] = decrypted[2];
		
		byte[] EXeq = new byte[24];
		EXeq[0] = (byte)0x18;
		EXeq[4] = (byte)0x05;
		EXeq[6] = (byte)0x0c;
		EXeq[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			EXeq[12+i] = cid[i];
			EXeq[16+i] = itemid[i];
		}
		
		EXeq[20] = decrypted[2];
		
		EXeq[21] = (byte)0x9e;
		EXeq[22] = (byte)0x0f;
		EXeq[23] = (byte)0xbf;
	
		if(decrypted[0] == 0){ //switch aka ALT+Z (move from equip to equip)
			int JanuxLikesBeer = cur.getequipSLOT(one);
			cur.setequipSLOT(one, cur.getequipSLOT(two));
			cur.setequipSLOT(two, JanuxLikesBeer);
		}
		int level = 1;
		if(decrypted[0] == 1){ // move item 1st
			if(cur.getInventorySLOT(one) == 0){
				cur.DeleteItemNOMESSAGE(one); 
				Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
				return null;
			}
			
			if(cur.getInventorySLOT(one) == 1337){
				Charstuff.getInstance().respondguild("Item Id == 1337, contact staff.", cur.GetChannel());
				return null;
			}
			if(cur.getequipSLOT(two) != 0){
				cur.respondguildTIMED("Take out the Equip item first. / Najpierw zdejmij ekwipowany item.", cur.GetChannel());
				cur.KillInvFreeze();
				return null;
			}
		if(ItemCache.Items.containsKey(cur.getInventorySLOT(one))){e0 = ItemCache.Items.get(cur.getInventorySLOT(one));}else{Charstuff.getInstance().respondguild("Item not registered in database, contact the staff!", cur.GetChannel()); return null;}
		String[] item0 = e0.split(",");
		if(item0 == null){return null;}
		if(!Charstuff.getInstance().Equipignore.containsKey(cur.getInventorySLOT(one))){
			level = Integer.valueOf(item0[29]);
		}
		int nigget = Integer.valueOf(item0[30]);
		if(nigget == 1337){
			nigget = two;
		}
		//System.out.println("Equip: "+cur.getInventorySLOT(one)+" ____ "+Integer.valueOf(item0[23])+" <= "+cur.getstrength()+" ____ "+Integer.valueOf(item0[24])+" <= "+cur.getdextery()+" ____ "+Integer.valueOf(item0[25])+" <= "+cur.getvitality()+" ____ "+Integer.valueOf(item0[26])+" <= "+cur.getintelligence()+" ____ "+Integer.valueOf(item0[27])+" <= "+cur.getagility()+" ____ "+level+" <= "+cur.getLevel()+" ____ "+nigget+" == "+two);
		if(Integer.valueOf(item0[23]) <= cur.getstrength() && Integer.valueOf(item0[24]) <= cur.getdextery() && Integer.valueOf(item0[25]) <= cur.getvitality() && Integer.valueOf(item0[26]) <= cur.getintelligence() && Integer.valueOf(item0[27]) <= cur.getagility() && level <= cur.getLevel()){
		if(nigget != 9 && two != 10 && nigget != 7){
		if(nigget != two){return null;}
		}
		cur.equip = cur.getequipSLOT(two);
		cur.setequipSLOT(two, cur.getInventorySLOT(one)); //set EQUIP slot , get inventory item by slot
		cur.DeleteInvItem(one); // remove inventory item by slot
		}else{/*Charstuff.getInstance().respond("Has not the required stats.",cur.GetChannel()); */return null;}
		}else
		if(decrypted[0] == 2){
			/*
			// move item 2nd
			// replace code here
			if (cur.inventory != 0){ 
				if(ItemCache.Items.containsKey(cur.inventory)){e0 = ItemCache.Items.get(cur.inventory);}else{Charstuff.getInstance().respondguild("Item not registered in database, contact the staff!", cur.GetChannel()); return null;}
				String[] item0 = e0.split(",");
				if(item0 == null){return null;}
				if(Integer.valueOf(item0[23]) <= cur.getstrength() && Integer.valueOf(item0[24]) <= cur.getdextery() && Integer.valueOf(item0[25]) <= cur.getvitality() && Integer.valueOf(item0[26]) <= cur.getintelligence() && Integer.valueOf(item0[27]) <= cur.getagility() && level <= cur.getLevel()){
				if(nigget != 9 && two != 10 && nigget != 7){
				if(nigget != two){return null;}
				}
				cur.equip2 = cur.getequipSLOT(two);
				cur.setequipSLOT(two, cur.inventory);
				cur.inventory = 0;
				cur.inventorystacktwo = 0;
				}else{Charstuff.getInstance().respond("Has not the required stats.",cur.GetChannel());return null;}
				}
			else
			if (cur.inventory2 != 0){ 
				if(ItemCache.Items.containsKey(cur.inventory2)){e0 = ItemCache.Items.get(cur.inventory2);}else{Charstuff.getInstance().respondguild("Item not registered in database, contact the staff!", cur.GetChannel()); return null;}
				String[] item0 = e0.split(",");
				if(item0 == null){return null;}
				if(Integer.valueOf(item0[23]) <= cur.getstrength() && Integer.valueOf(item0[24]) <= cur.getdextery() && Integer.valueOf(item0[25]) <= cur.getvitality() && Integer.valueOf(item0[26]) <= cur.getintelligence() && Integer.valueOf(item0[27]) <= cur.getagility() && level <= cur.getLevel()){
				if(nigget != 9 && two != 10 && nigget != 7){
				if(nigget != two){return null;}
				}	
				cur.equip = cur.getequipSLOT(two);
				cur.setequipSLOT(two, cur.inventory2); 
				cur.inventory2 = 0;
				cur.inventorystack = 0;
				}else{Charstuff.getInstance().respond("Has not the required stats.",cur.GetChannel());return null;}
				}
			else 
			if(cur.equip != 0) {
				if(ItemCache.Items.containsKey(cur.equip)){e0 = ItemCache.Items.get(cur.equip);}else{Charstuff.getInstance().respondguild("Item not registered in database, contact the staff!", cur.GetChannel()); return null;}
				String[] item0 = e0.split(",");
				if(item0 == null){return null;}
				if(Integer.valueOf(item0[23]) <= cur.getstrength() && Integer.valueOf(item0[24]) <= cur.getdextery() && Integer.valueOf(item0[25]) <= cur.getvitality() && Integer.valueOf(item0[26]) <= cur.getintelligence() && Integer.valueOf(item0[27]) <= cur.getagility() && level <= cur.getLevel()){
				if(nigget != 9 && two != 10 && nigget != 7){
				if(nigget != two){return null;}
				}
				cur.equip2 = cur.getequipSLOT(two);
				cur.setequipSLOT(two, cur.equip);
				cur.equip = 0;
				}else{Charstuff.getInstance().respond("Has not the required stats.",cur.GetChannel());return null;}
				}
			else											
			if(cur.equip2 != 0){ 
				if(ItemCache.Items.containsKey(cur.equip2)){e0 = ItemCache.Items.get(cur.equip2);}else{Charstuff.getInstance().respondguild("Item not registered in database, contact the staff!", cur.GetChannel()); return null;}
				String[] item0 = e0.split(",");
				if(item0 == null){return null;}
				if(Integer.valueOf(item0[23]) <= cur.getstrength() && Integer.valueOf(item0[24]) <= cur.getdextery() && Integer.valueOf(item0[25]) <= cur.getvitality() && Integer.valueOf(item0[26]) <= cur.getintelligence() && Integer.valueOf(item0[27]) <= cur.getagility() && level <= cur.getLevel()){
				if(nigget != 9 && two != 10 && nigget != 7){
				if(nigget != two){return null;}
				}
				cur.equip = cur.getequipSLOT(two); 
				cur.setequipSLOT(two, cur.equip2);
				cur.equip2 = 0;
				}else{Charstuff.getInstance().respond("Has not the required stats.",cur.GetChannel());return null;}
				}
			else
				if(cur.equip == 0 && cur.equip2 == 0 && cur.inventory == 0 && cur.inventory2 == 0){
					Charstuff.getInstance().respondguildTIMED("Please select an empty Inventory slot!", cur.GetChannel());
					cur.ClearInv();
					return null;
				 }
			*/
		}
		cur.sendToMap(EXeq);
		cur.getitemdata();
		cur.statlist();
		//System.out.println("DONE");
		return eq;
	}
}