package GameServer.GamePackets;

import item.DroppedItem;
import item.Item;
import item.ItemCache;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import timer.SystemTimer;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;
import Player.Character;
import Player.Charstuff;
import Player.Party;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;

public class Pick implements Packet {
	private ConcurrentMap<Integer, Character> TempBlock = new ConcurrentHashMap<Integer, Character>();
	private WMap wmap = WMap.getInstance();
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}


	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		//System.out.println("Handling pick");
		int zero = BitTools.byteToInt(decrypted[0]);
		int one = BitTools.byteToInt(decrypted[1]);
		int two = BitTools.byteToInt(decrypted[2]);
		int three = BitTools.byteToInt(decrypted[3]);
		int four = BitTools.byteToInt(decrypted[4]);
		int five = BitTools.byteToInt(decrypted[5]);
		int six = BitTools.byteToInt(decrypted[6]);
		int seven = BitTools.byteToInt(decrypted[7]);
		//System.out.println("PACKET HEADER:[ "+zero +" "+ one+ " "+two+ " "+three+ " "+four+ " "+five+ " "+six+ " "+seven+" ]");
		
		byte[] pick = new byte[40];
		byte[] uid = new byte[4];
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		for(int i=0;i<4;i++) {
			uid[i] = decrypted[i];
		}
		
		int iuid = BitTools.byteArrayToInt(uid);
		//System.out.println("Handling pick :"+Item.iteMapSTACK.get(Integer.valueOf(iuid)));
		if((ServerFacade.getInstance().getConnectionByChannel(cur.GetChannel())) == null){
			cur.respondguildTIMED("Loot: Fake Item1!", cur.GetChannel());
			pick[0] = (byte)pick.length;
			pick[4] = (byte)0x04;
			pick[6] = (byte)0x0F;
			pick[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				pick[12+i] = chid[i];
			}
			pick[17] = (byte)0x03;
			pick[24] = (byte)0xff;
			pick[25] = (byte)0xff;
			pick[26] = (byte)0xff;
			pick[30] = (byte)0xff;
			pick[31] = (byte)0xff;
			return pick;
		}
		if(Item.iteMapSTACK.get(Integer.valueOf(iuid)) == null){
			cur.respondguildTIMED("Loot: item == null!", cur.GetChannel());
			pick[0] = (byte)pick.length;
			pick[4] = (byte)0x04;
			pick[6] = (byte)0x0F;
			pick[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				pick[12+i] = chid[i];
			}
			pick[17] = (byte)0x03;
			pick[24] = (byte)0xff;
			pick[25] = (byte)0xff;
			pick[26] = (byte)0xff;
			pick[30] = (byte)0xff;
			pick[31] = (byte)0xff;
			return pick;
		}
		if(Item.iteMap.get(Integer.valueOf(iuid)) == null){
			cur.respondguildTIMED("Loot: item == null!", cur.GetChannel());
			pick[0] = (byte)pick.length;
			pick[4] = (byte)0x04;
			pick[6] = (byte)0x0F;
			pick[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				pick[12+i] = chid[i];
			}
			pick[17] = (byte)0x03;
			pick[24] = (byte)0xff;
			pick[25] = (byte)0xff;
			pick[26] = (byte)0xff;
			pick[30] = (byte)0xff;
			pick[31] = (byte)0xff;
			return pick;
		}
		if(Item.iteMap.get(Integer.valueOf(iuid)) < 0){
			cur.respondguildTIMED("Loot: Fake Item5!", cur.GetChannel());
			pick[0] = (byte)pick.length;
			pick[4] = (byte)0x04;
			pick[6] = (byte)0x0F;
			pick[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				pick[12+i] = chid[i];
			}
			pick[17] = (byte)0x03;
			pick[24] = (byte)0xff;
			pick[25] = (byte)0xff;
			pick[26] = (byte)0xff;
			pick[30] = (byte)0xff;
			pick[31] = (byte)0xff;
			return pick;
		}
		
		if(cur.partyUID != 0 && Item.iteMapcharid.get(Integer.valueOf(iuid)) != cur.charID && System.currentTimeMillis() - Item.iteMapTimedrop.get(Integer.valueOf(iuid)) < 30000){
			Character Tplayer = wmap.getCharacter(Item.iteMapcharid.get(Integer.valueOf(iuid)));
			if(Tplayer != null && Tplayer.partyUID == cur.partyUID){
				// dont return nulll
			}else
			if(Item.iteMapcharid.get(Integer.valueOf(iuid)) != cur.charID && System.currentTimeMillis() - Item.iteMapTimedrop.get(Integer.valueOf(iuid)) < 30000){
				pick[0] = (byte)pick.length;
				pick[4] = (byte)0x04;
				pick[6] = (byte)0x0F;
				pick[8] = (byte)0x01;
				for(int i=0;i<4;i++) {
					pick[12+i] = chid[i];
				}
				pick[17] = (byte)0x03;
				pick[24] = (byte)0xff;
				pick[25] = (byte)0xff;
				pick[26] = (byte)0xff;
				pick[30] = (byte)0xff;
				pick[31] = (byte)0xff;
				return pick;
			}
			// dont return nulll
		}else
		if(Item.iteMapcharid.get(Integer.valueOf(iuid)) != cur.charID && System.currentTimeMillis() - Item.iteMapTimedrop.get(Integer.valueOf(iuid)) < 30000){
			pick[0] = (byte)pick.length;
			pick[4] = (byte)0x04;
			pick[6] = (byte)0x0F;
			pick[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				pick[12+i] = chid[i];
			}
			pick[17] = (byte)0x03;
			pick[24] = (byte)0xff;
			pick[25] = (byte)0xff;
			pick[26] = (byte)0xff;
			pick[30] = (byte)0xff;
			pick[31] = (byte)0xff;
			return pick;
		}

		byte[] itid = BitTools.intToByteArray(Item.iteMap.get(Integer.valueOf(iuid)));  
		int intitnewstack = cur.getInventorySTACK(four) + Item.iteMapSTACK.get(Integer.valueOf(iuid));
		byte[] itidSTACk;
		itidSTACk = BitTools.intToByteArray(Item.iteMapSTACK.get(Integer.valueOf(iuid))); 
		int stackable = Charstuff.getInstance().getStackable_items(Item.iteMap.get(Integer.valueOf(iuid)));
		if(stackable == 0 && Item.iteMap.get(Integer.valueOf(iuid)) != 217000501){
			//System.out.println("pick = 0");
			// if target inv slot is not 0 then return null;
			//if(cur.getInventorySLOT((int)decrypted[5]) != 0){System.out.println("npcshop2");cur.ClearInv();return null;}
			intitnewstack = 1;
			itidSTACk = BitTools.intToByteArray(1); 
		}

		
		
		//gold
		if(Item.iteMap.get(Integer.valueOf(iuid)) == 217000501){
			
			 int min = cur.getLevel() - 15;
			 int max = cur.getLevel() + 15;
			 int partyexp = 0;
			 int recexp = Integer.valueOf(Item.iteMapSTACK.get(Integer.valueOf(iuid)));
			 
				if(cur.partyUID != 0)
				{
				Party pt = wmap.getParty(cur.partyUID);
				if(pt != null){
				partyexp = recexp / pt.partymembers.size();
				}
				}else{
					 partyexp = recexp;	//normal single player no party
				}
				//System.out.println("Charid"+cur.charID+" - 217000501 - "+Integer.valueOf((int)partyexp));
				itidSTACk = BitTools.intToByteArray(partyexp); 
				if(partyexp >= 2147483647){return null;}
				if(partyexp <= 0){return null;}
				cur.setgold(cur.getgold() + partyexp);
			 
				if(cur.partyUID != 0)
				{

			 Iterator<Integer> iter = cur.getIniPackets().keySet().iterator();
				while(iter.hasNext()) {   
						Character ch = this.wmap.getCharacter(iter.next().intValue());
						if(ch != null && ch.charID != cur.charID) {
							if(cur.partyUID != 0 && ch.partyUID != 0 && ch.partyUID == cur.partyUID && ch.getLevel() >= min && ch.getLevel() <= max && ch.getHp() > 0 && !this.TempBlock.containsKey(ch.charID) && ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()) != null){
								this.TempBlock.put(ch.charID, ch);	 
								ch.recgold(partyexp);
						}
					}
				}
			this.TempBlock.clear();	 
		}
		}
		
		
		pick[0] = (byte)pick.length;
		pick[4] = (byte)0x04;
		pick[6] = (byte)0x0F;
		
		byte[] iv = new byte[20];
		iv[0] = (byte)iv.length;
		iv[4] = (byte)0x05;
		iv[6] = (byte)0x0F;
		iv[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			pick[12+i] = chid[i];
			pick[28+i] = uid[i];
			pick[32+i] = itid[i];
			pick[36+i] = itidSTACk[i];
			iv[16+i] = uid[i];
		}
		

		pick[8]  = (byte)0x01;
		pick[9]  = (byte)0x9C;
		pick[10] = (byte)0x04;
		pick[11] = (byte)0x08;
		pick[16] = (byte)0x01;
		pick[18] = (byte)0xCF;
		pick[19] = (byte)0x2D;
		pick[20] = (byte)0x03;
		pick[24] = (byte)decrypted[4];
		pick[25] = (byte)decrypted[5]; 
		pick[26] = (byte)decrypted[6];
		pick[30] = (byte)decrypted[5];
		pick[31] = (byte)decrypted[6];
		
		

		int invslot = BitTools.byteToInt(decrypted[4]);
		int invheight = BitTools.byteToInt(decrypted[5]);
		int invweight = BitTools.byteToInt(decrypted[6]);

		cur.setInventory(invslot, Item.iteMap.get(Integer.valueOf(iuid)), invheight, invweight, intitnewstack);
		
		String e0 = null;
		if(ItemCache.Items.containsKey(Item.iteMap.get(Integer.valueOf(iuid)))){e0 = ItemCache.Items.get(Item.iteMap.get(Integer.valueOf(iuid)));}
		if(e0 != null){
		String[] item0 = e0.split(",");
		if(Long.valueOf(item0[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item0[22])) + System.currentTimeMillis();
		cur.setitem_end_date(Item.iteMap.get(Integer.valueOf(iuid)), minato);
		cur.expireactive();
		}}
		Item.iteMap.remove(Integer.valueOf(iuid));
		Item.iteMapSTACK.remove(Integer.valueOf(iuid));
		Item.iteMapcharid.remove(Integer.valueOf(iuid));
		Item.iteMapTimedrop.remove(Integer.valueOf(iuid));
		con.addWrite(iv);
		cur.sendToMap(iv);
		
		//DroppedItem DR = wmap.getItem(iuid);
		//DR.leaveGameWorld();
		return pick;
	}

}
