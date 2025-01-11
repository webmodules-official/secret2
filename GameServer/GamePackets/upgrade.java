package GameServer.GamePackets;
import item.ItemCache;

import java.nio.ByteBuffer;
import java.util.Random;

import timer.SystemTimer;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.upgradelist;
import Tools.BitTools;

import Connections.Connection;
import Encryption.Decryptor;

public class upgrade implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling upgrade");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		/*for(int i=0;i<decrypted.length;i++) {System.out.print(i+":"+decrypted[i]+" ");}
		System.out.println("");
		for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		System.out.println("");*/
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[36];
		int invslot = (int)decrypted[8];
		int UpgradeItemSLOT = (int)decrypted[9];
		int newitem = 0;
		
		if(cur.getInventorySLOT(UpgradeItemSLOT) == 0){
			cur.DeleteItemNOMESSAGE(UpgradeItemSLOT); 
			Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		
		// Siccors: Silver and Gold
		if(cur.getInventorySLOT(UpgradeItemSLOT) == 215003011||cur.getInventorySLOT(UpgradeItemSLOT) == 215003012){
			newitem = cur.getInventorySLOT(invslot) + 13;
			
			int itemlevel = 0;
			String e0 = null;
			if(ItemCache.Items.containsKey(cur.getInventorySLOT(invslot))){e0 = ItemCache.Items.get(cur.getInventorySLOT(invslot));}
			if(e0 != null){
			String[] item0 = e0.split(",");
			itemlevel = Integer.valueOf(item0[29]);
			}
			
			int itemlevel1 = 0;
			String e1 = null;
			if(ItemCache.Items.containsKey(newitem)){e1 = ItemCache.Items.get(newitem);}
			if(e1 != null){
			String[] item1 = e1.split(",");
			itemlevel1 = Integer.valueOf(item1[29]);
			}
			
			//System.out.println("Level: "+itemlevel+" == "+itemlevel1);
			
			if(upgradelist.tryIDtoNewID(newitem) && itemlevel == itemlevel1){
				// do nothing and move on
				//System.out.println("win");
			}else{
			newitem = cur.getInventorySLOT(invslot) - 13;	
			//System.out.println("lose");
			}
		}else
		// Weapon Tae Class Changing Stones	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000259){ // warrior - simitar
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 1);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 1);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000260){ // warrior - axe
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 2);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 1);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000261){ // warrior - lance
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 3);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 1);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000262){ // assassin - dagger
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 1);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 2);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 2);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000263){ // assassin - gaulet
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 2);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 2);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 2);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000264){ // assassin - bow
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 3);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 2);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 2);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000265){ // mage - glove
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 1);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 3);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000266){ // mage - fan
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 2);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 3);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000267){ // mage - sword
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 3);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 3);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else		
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000268){ // monk - rod
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 1);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 4);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000269){ // monk - wheel
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 2);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 4);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else	
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000270){ // monk - plummet
				int herp =BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 5, 3);
				int lol = BitTools.ChangeAnyDigit(herp, 4, 4);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else		
			
		// Clothes Tae Class Changing Stones
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 283000024||cur.getInventorySLOT(UpgradeItemSLOT) == 213062894){ // warrior
				int lol = BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 4, 1);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 283000025||cur.getInventorySLOT(UpgradeItemSLOT) == 213062895){ // assassin
				int lol = BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 4, 2);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 2);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 283000026||cur.getInventorySLOT(UpgradeItemSLOT) == 213062896){ // mage
				int lol = BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 4, 3);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 283000027||cur.getInventorySLOT(UpgradeItemSLOT) == 213062897){ // monk
				int lol = BitTools.ChangeAnyDigit(cur.getInventorySLOT(invslot), 4, 4);
				newitem = BitTools.ChangeAnyDigit(lol, 3, 1);
				if(upgradelist.tryIDtoNewID(newitem)){
					// do nothing and move on
				}else{return null;}
		}else
			
		// Look first in the manual List - MOUNTS
		if(upgradelist.tryUpgradelist(cur.getInventorySLOT(invslot))){
		newitem = upgradelist.getUpgradelist(cur.getInventorySLOT(invslot));
		}else{
		//Automatic List
		newitem = upgradelist.getIDtoNewID(cur.getInventorySLOT(invslot));
		
		boolean FAIL = false;
		
		if(cur.getInventorySLOT(UpgradeItemSLOT) == 215003001){}
		else
			if(cur.getInventorySLOT(UpgradeItemSLOT) == 215003002){}
			else
				if(cur.getInventorySLOT(UpgradeItemSLOT) == 215003003){}
				else
					if(cur.getInventorySLOT(UpgradeItemSLOT) == 215003408){}
					else
						if(cur.getInventorySLOT(UpgradeItemSLOT) == 215003708){}
						else
							if(cur.getInventorySLOT(UpgradeItemSLOT) == 215004008){}
							else
								if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000080){}
								else	
									if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000766){}
									else
						{return null;}
		
		//Upgrade rating here
		String e;
		if(ItemCache.Items.containsKey(newitem)){e = ItemCache.Items.get(newitem);}else{e = "";}
		String[] item = e.split(",");
		if(cur.getInventorySLOT(UpgradeItemSLOT) == 273000080){
		// no bet
		//System.out.println("100$ SAM STONE");
		}else
		if(Integer.valueOf(item[29]) >= 144){ //144-160 = 20%
		Random r = new Random();	 
		int Random = 1+r.nextInt(5);	
		if(Random == 1){FAIL = true;}
		if(Random == 2){FAIL = true;}
		if(Random == 4){FAIL = true;}
		if(Random == 5){FAIL = true;}	
		//System.out.println("Level 145-160 -- R="+Random+"/5 -- FAIL="+FAIL);
		}else
		if(Integer.valueOf(item[29]) >= 121){ //121-143 = 25% 
		Random r = new Random();	 
		int Random = 1+r.nextInt(4);	
		if(Random == 1){FAIL = true;}
		if(Random == 2){FAIL = true;}
		if(Random == 3){FAIL = true;}
		//System.out.println("Level 121-144 -- R="+Random+"/4 -- FAIL="+FAIL);
		}else
		if(Integer.valueOf(item[29]) >= 85){ //85-120 = 33%
		Random r = new Random();	 
		int Random = 1+r.nextInt(3);	
		if(Random == 2){FAIL = true;}
		if(Random == 3){FAIL = true;}
		//System.out.println("Level 85-120 -- R="+Random+"/3 -- FAIL="+FAIL);
		}else
		if(Integer.valueOf(item[29]) >= 53){ //53-84 = 50%
		Random r = new Random();	 
		int Random = 1+r.nextInt(2);	
		if(Random == 2){FAIL = true;}	
		//System.out.println("Level 53-84 -- R="+Random+"/2 -- FAIL="+FAIL);
		}else
		if(Integer.valueOf(item[29]) >= 37){ //37-52 = 70%
		Random r = new Random();	 
		int Random = 1+r.nextInt(10);	
		if(Random == 3){FAIL = true;}	
		if(Random == 5){FAIL = true;}	
		if(Random == 8){FAIL = true;}		
		//System.out.println("Level 37-52 -- R="+Random+"/10-- FAIL="+FAIL);
		}else
		if(Integer.valueOf(item[29]) >= 1){ //1-36 = 100%
		//System.out.println("Level 1-36 -- R=1/1 -- FAIL="+FAIL);
		 // insta win yay
		}else				
		{return null;}
		
		//return fail
		if(FAIL == true){
		//System.out.println("FAILED");
		cur.DeleteItemNOMESSAGE(UpgradeItemSLOT);
		fury[0] = (byte)0x24;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x32;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		
		fury[16] = (byte)0x00; // 0x01 = win  || 0x00 = fail 

		fury[18] = (byte)0x00; 
		fury[19] = (byte)0xff; // inv slot
		
		fury[20] = (byte)0x44; // ?????
		fury[21] = (byte)0x06;
		fury[22] = (byte)0x06;
		fury[23] = (byte)0x08;
		
		fury[26] = (byte)0xff; // inv slot y
		fury[27] = (byte)0xff; // inv slot x
		String e0 = null;
		if(ItemCache.Items.containsKey(newitem)){e0 = ItemCache.Items.get(newitem);}
		if(e0 != null){
		String[] item0 = e0.split(",");
		if(Long.valueOf(item0[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item0[22])) + System.currentTimeMillis();
		cur.setitem_end_date(newitem, minato);
		cur.expireactive();
		}}
		return fury; // stop here
		}}
		
		
		if(newitem != 0){
		cur.DeleteItemNOMESSAGE(UpgradeItemSLOT);
		byte[] newupgradeditem = BitTools.intToByteArray(newitem);
		cur.setInventorySLOT(invslot,newitem);
		fury[0] = (byte)0x24;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x32;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
			fury[28+i] = newupgradeditem[i];
		}
		fury[16] = (byte)0x01; // 0x01 = win  || 0x00 = fail ?????????

		fury[18] = (byte)0x00; // 0x01 = win  || 0x00 = fail ?????????
		fury[19] = (byte)invslot; // inv slot
		
		fury[20] = (byte)0x44; // ?????
		fury[21] = (byte)0x06;
		fury[22] = (byte)0x06;
		fury[23] = (byte)0x08;
		
		fury[26] = (byte)cur.getInventoryHEIGHT(invslot); // inv slot y
		fury[27] = (byte)cur.getInventoryWEIGHT(invslot); // inv slot x
		String e0 = null;
		if(ItemCache.Items.containsKey(newitem)){e0 = ItemCache.Items.get(newitem);}
		if(e0 != null){
		String[] item0 = e0.split(",");
		if(Long.valueOf(item0[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item0[22])) + System.currentTimeMillis();
		cur.setitem_end_date(newitem, minato);
		cur.expireactive();
		}}
		return fury;
		}else{cur.KillInvFreeze(); return null;}
	}
	
}
