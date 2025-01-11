package GameServer.GamePackets;

import item.DroppedItem;
import item.Item;

import java.nio.ByteBuffer;

import npc.NpcController;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;
import World.Waypoint;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class Dropstuff implements Packet {
	private WMap wmap = WMap.getInstance();
	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling drop stuff window");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		//System.out.println("");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(decrypted[0] == 2){cur.KillInvFreeze();return null;}
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		int zero = BitTools.byteToInt(decrypted[0]);
		int one = BitTools.byteToInt(decrypted[1]);
		byte[] chid = BitTools.intToByteArray(cur.charID);
		byte[] spawnX = BitTools.floatToByteArray(cur.getlastknownX());
		byte[] spawnY = BitTools.floatToByteArray(cur.getlastknownY());
		byte[] itemStack = new byte[4];
		byte[] INVstack; 
		byte[] itid1;
		for(int i=0;i<4;i++){
		itemStack[i] = (byte)decrypted[i+4];
		}
		if(BitTools.byteArrayToInt(itemStack) > 2120000000){return null;}
		Item.inc++;
		byte[] itchid = BitTools.intToByteArray(Item.inc);
		
		byte[] item = new byte[28];
		item[0] = (byte)0x1c;
		item[4] = (byte)0x04; // 0x05 = for external packet ( to players)
		item[6] = (byte)0x0e;
		item[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			item[12+i] = chid[i]; 
			item[24+i] = itchid[i]; // header id of hashmap
			item[20+i] = decrypted[4+i]; // drop stack
		}
		item[16] = (byte)0x01;
		
		if(zero == 255){
		item[18] = (byte)0xff;
		item[19] = (byte)0x00;
		}else{
		item[18] = decrypted[0];
		item[19] = decrypted[1]; // inventory slot id that drops
		}
	
		
		if(zero == 255){
			INVstack = BitTools.intToByteArray(BitTools.byteArrayToInt(itemStack));
			itid1 = BitTools.intToByteArray(217000501);
			long newgold = cur.getgold() - BitTools.byteArrayToInt(itemStack);
			if(newgold < 0){return null;}
			cur.setgold(newgold);
			Item.iteMapTimedrop.put(Integer.valueOf(Item.inc), Long.valueOf(System.currentTimeMillis()));
			Item.iteMapcharid.put(Integer.valueOf(Item.inc), Integer.valueOf(cur.charID));
			Item.iteMap.put(Item.inc, 217000501);
			Item.iteMapSTACK.put(Item.inc, BitTools.byteArrayToInt(INVstack));
			//DroppedItem tmp = new DroppedItem(217000501, BitTools.byteArrayToInt(INVstack), Item.inc, cur.getCurrentMap(), cur.getlastknownX(), cur.getlastknownY());
		}else{
			if(cur.getInventorySLOT(one) == 0){
				cur.DeleteItemNOMESSAGE(one); 
				Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
				return null;
			}
			if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT(one))){cur.KillInvFreeze();return null;}
			INVstack = itemStack;
			itid1 = BitTools.intToByteArray(cur.getInventorySLOT(one));
			int stackable = Charstuff.getInstance().getStackable_items(cur.getInventorySLOT(one));
			if(stackable == 0){
				//System.out.println("dropstuff = 0");
				// if target inv slot is not 0 then return null;
				//if(cur.getInventorySLOT((int)decrypted[5]) != 0){System.out.println("npcshop2");cur.ClearInv();return null;}
				Item.iteMapTimedrop.put(Integer.valueOf(Item.inc), Long.valueOf(System.currentTimeMillis()));
				Item.iteMapcharid.put(Integer.valueOf(Item.inc), Integer.valueOf(cur.charID));
				Item.iteMap.put(Item.inc, cur.getInventorySLOT(one));
				Item.iteMapSTACK.put(Item.inc, 1);	
				cur.DeleteInvItem(one);
			}else
			// if itemstack == 1, might be splitting or one item.
			if(BitTools.byteArrayToInt(INVstack) == 1){
				//System.out.println("dropstuff 1 :"+cur.getInventorySLOT(one));
				Item.iteMapTimedrop.put(Integer.valueOf(Item.inc), Long.valueOf(System.currentTimeMillis()));
				Item.iteMapcharid.put(Integer.valueOf(Item.inc), Integer.valueOf(cur.charID));
				Item.iteMap.put(Item.inc, cur.getInventorySLOT(one));
				Item.iteMapSTACK.put(Item.inc, 1);	
				int newvaluez = cur.getInventorySTACK(one) - 1;
				if(newvaluez <= 0){cur.DeleteInvItem(one);}else{cur.setInventorySTACK(one, newvaluez);}	
			}else{
				//System.out.println("dropstuff 2 :"+	cur.getInventorySLOT(one));
				Item.iteMapTimedrop.put(Integer.valueOf(Item.inc), Long.valueOf(System.currentTimeMillis()));
				Item.iteMapcharid.put(Integer.valueOf(Item.inc), Integer.valueOf(cur.charID));
				Item.iteMap.put(Item.inc, cur.getInventorySLOT(one));
				Item.iteMapSTACK.put(Item.inc, cur.getInventorySTACK(one));	
				cur.DeleteInvItem(one);
			}

		}
		
		byte[] item1 = new byte[56];
		item1[0] = (byte)item1.length;
		item1[4] = (byte)0x05;
		item1[6] = (byte)0x0e;
		
		for(int i=0;i<4;i++) {
			item1[20+i] = itid1[i];    //itemid
			item1[28+i] = INVstack[i]; //stack
			item1[32+i] = itchid[i];   //uid
			item1[36+i] = spawnX[i];   //x
			item1[40+i] = spawnY[i];   //y
		}

	
		cur.sendToMap(item1);
		return item;
	}
	
}
