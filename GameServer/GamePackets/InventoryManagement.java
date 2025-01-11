package GameServer.GamePackets;

import item.ItemCache;

import java.nio.ByteBuffer;

import timer.SystemTimer;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.itemprice;
import ServerCore.ServerFacade;
import Tools.BitTools;

public class InventoryManagement implements Packet {
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}

	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling inventory");
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
		
		/*
		 * 
		 * WARNING: DUPE STILL EXIST!!!!
		 * 
		 * 
		 * if i ever gonna make new project or functions then keep track of every fucking detail
		 * do a IsTrading = true;
		 * and auto add it in failbarrier();
		 * then every other function will go trough failbarrier(); first, if returns false then null else if returns true then continue
		 * 
		 * 
		 * 
		 */
		byte[] inv = new byte[28];
		byte[] stck = new byte[4];
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		inv[0] = (byte)inv.length;
		inv[4] = (byte)0x04;
		inv[6] = (byte)0x10;
		
		
		inv[16] = (byte)0x01;
		inv[18] = decrypted[0];
		for(int i=0;i<4;i++) {
			inv[12+i] = chid[i];
			inv[19+i] = decrypted[i+1];
			stck[i] = decrypted[i+8];
		}
		inv[24] = decrypted[8];
		
		int one = BitTools.byteToInt(decrypted[1]);   // from item SLOT ( can be from anywhre like equip etc. )
		int two = BitTools.byteToInt(decrypted[2]);   // to inventory slot 
		int three = BitTools.byteToInt(decrypted[3]); // to inventory height
		int four = BitTools.byteToInt(decrypted[4]);  // to inventory weight
		int five = BitTools.byteArrayToInt(stck);  // stack
		//System.out.println("Itemid, stack: "+cur.getInventorySLOT(one)+" - "+cur.getInventorySTACK(one)+" - "+five);
		//System.out.println("Itemid, stack: "+cur.getCargoSLOT(one)+" - "+cur.getCargoSTACK(one)+" - "+five);
		
		if(decrypted[0] == 0) // from equip slot to inventory slot
		{
			
			if(cur.getInventorySLOT(two) != 0){
				cur.respondguildTIMED("Select an empty Inventory slot.", cur.GetChannel());
				cur.KillInvFreeze();
				return null;
			}
			byte[] EXeq = new byte[24];
			EXeq[0] = (byte)0x18;
			EXeq[4] = (byte)0x05;
			EXeq[6] = (byte)0x0c;
			EXeq[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				EXeq[12+i] = chid[i];
			}
			EXeq[20] = decrypted[1];
			
			EXeq[21] = (byte)0x9e;
			EXeq[22] = (byte)0x0f;
			EXeq[23] = (byte)0xbf;

			
			cur.sendToMap(EXeq); // let other knows that i have unequipped something
			cur.inventory = cur.getInventorySLOT(two);
			cur.setInventory(two, cur.getequipSLOT(one), three, four, 1);
			cur.DeleteEquipItem(Integer.valueOf(one)); // remove equip itemid by equip slot
			cur.getitemdata();
			cur.statlist();
		}
		if(decrypted[0] == 1) // move inventory slot
		{
			
			String e0 = null;
			if(ItemCache.Items.containsKey(cur.getInventorySLOT(one))){e0 = ItemCache.Items.get(cur.getInventorySLOT(one));}
			if(e0 != null){
			String[] item0 = e0.split(",");
			if(Long.valueOf(item0[22]) != 0){
			long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item0[22]));
			//System.out.println("minato"+minato);
			if(minato != 0){five = 1;}
			}}
			
			if(five > 100){cur.KillInvFreeze();return null;}
			if(five <= 0){cur.KillInvFreeze();return null;}
			
			
			if(cur.getInventorySLOT(one) == 0){
				cur.DeleteItemNOMESSAGE(one); 
				Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
				return null;
			}
			if(!Charstuff.getInstance().tryStackable_items(cur.getInventorySLOT(one))){/*System.out.println("npcshop1");*/cur.KillInvFreeze();return null;}
			int stackable1 = Charstuff.getInstance().getStackable_items(cur.getInventorySLOT(one));
			int stackable2 = Charstuff.getInstance().getStackable_items(cur.getInventorySLOT(two));
			cur.inventory = cur.getInventorySLOT(one);	// itemid one
			cur.inventorystack = cur.getInventorySTACK(one);   // stack one
			//cur.inventorystacktwo = cur.getInventorySTACK(two);   // stack two
			
			//MESURE STACK FIRST U FUCKING OBLEDeriaTED IDIOT, U DUMNUT. thEN APPLY ITEMID
			//Tempfix: mesure what he can and cant do

			//nonstackable switching disable
			if(one != two && cur.getInventorySLOT(two) != 0 && stackable1 == 0){
				cur.respondguildTIMED("Select an empty Inventory slot.", cur.GetChannel());
				cur.KillInvFreeze();
				return null;
			}else
			if(one != two && cur.getInventorySLOT(two) != 0 && stackable2 == 0){ 
				cur.respondguildTIMED("Select an empty Inventory slot.", cur.GetChannel());
				cur.KillInvFreeze();
				return null;
			}	
			
			//stackable switching disable except: splitting,merging
			if(one != two && cur.getInventorySLOT(one) != cur.getInventorySLOT(two) && cur.getInventorySLOT(two) != 0){
				cur.respondguildTIMED("Select an empty Inventory slot.", cur.GetChannel());
				cur.KillInvFreeze();
				return null;
			}
			
			
			//-- Merging --\\
			//========== One ============/
			if(one != two){	// one: if not same slot then its merge or split ( check for uh     )
			cur.setInventorySTACK(one, cur.getInventorySTACK(one) - five); // splitting from one (becomes -0 on moving with diff item)
			if(cur.getInventorySTACK(one) <= 0){cur.DeleteInvItem(one);}
			}
			
			int stacktwo = 0; // qq 
			if(one != two && cur.inventory  == cur.getInventorySLOT(two) && five == 1 && stackable1 != 0){ // merging item +1
				stacktwo = cur.getInventorySTACK(two) + 1;
			}else 
			if(one != two && cur.inventory  == cur.getInventorySLOT(two) && five == cur.inventorystack && stackable1 != 0){ // merging item + any stack
				stacktwo = cur.getInventorySTACK(two) + cur.inventorystack;
			}else 
			if(one == two && stackable1 == 0){ //move OR replace non stackable <-- now just move nonstack to empty slot
				stacktwo = 1;
			}else
			if(one == two && five == cur.inventorystack && stackable1 != 0){ //move stackable
				stacktwo = cur.inventorystack;
			}else
			if(one != two && five == 1 && stackable1 != 0  && cur.getInventorySLOT(two) == 0){ //splitting stackbale <- not to other items anymore
				stacktwo = 1;
			}else{
				cur.respondguildTIMED("Select an empty Inventory slot.", cur.GetChannel());
				cur.KillInvFreeze();
				return null;
				//cur.setInventorySTACK(two, cur.inventorystack);	// fuck it but still serverside <-- FAIL LOL
			}
			
			if(stacktwo > 100){cur.KillInvFreeze();return null;}
			if(stacktwo <= 0){cur.KillInvFreeze();return null;}
			
			
			
			//  doesnt work 100% yet when splitting merging due i changed top to bot
			
			
			// now set item
			cur.setInventory(two, cur.inventory, three, four, stacktwo);
			
			//System.out.println("1 one:"+one+" - "+two+" : "+ Charstuff.getitemtoname(cur.getInventorySLOT(one))+" - "+cur.getInventorySTACK(one));
			//System.out.println("1 two:"+one+" - "+two+" : "+ Charstuff.getitemtoname(cur.getInventorySLOT(two))+" - "+cur.getInventorySTACK(two));
		}
		if(decrypted[0] == 2) // move 2nd ITEM inventory slot ( LIKE REPLACE ITEM BLUE blockje je weet )
		{
			cur.KillInvFreeze();
			return null;
			/*
			// replace code here
			int itemid = 0;
			int stack = 0;
		if (cur.equip != 0){ 
			itemid = cur.getInventorySLOT(two);
			stack = 1;
			cur.inventory2 = cur.getInventorySLOT(two); 
			cur.inventorystack = cur.getInventorySTACK(two);
			cur.setInventorySLOT(two, cur.equip); 
			cur.equip = 0;
			} // fix diz shit
		else 
		if (cur.equip2 != 0){ 
			itemid = cur.getInventorySLOT(two);
			stack = 1;
			cur.inventory = cur.getInventorySLOT(two); 
			cur.inventorystacktwo = cur.getInventorySTACK(two);
			cur.setInventorySLOT(two, cur.equip2);
			cur.equip2 = 0;
			}
		else 
		if(cur.inventory != 0) {
			itemid = cur.getInventorySLOT(two);
			stack = cur.inventorystacktwo;
			cur.inventory2 = cur.getInventorySLOT(two); 
			cur.inventorystack = cur.getInventorySTACK(two);
			cur.setInventorySLOT(two, cur.inventory); 
			cur.inventorystacktwo = 0;
			cur.inventory = 0;
			}// SET inevntory slot -> equip itemid by equip slot
		else											
		if(cur.inventory2 != 0){ 
			itemid = cur.getInventorySLOT(two);
			stack = cur.inventorystack;
			cur.inventory = cur.getInventorySLOT(two);
			cur.inventorystacktwo = cur.getInventorySTACK(two);
			cur.setInventorySLOT(two, cur.inventory2);
			cur.inventorystack = 0;
			cur.inventory2 = 0;
			}
		else	// if a null item gets passed on in 2nd
		if(cur.equip == 0 && cur.equip2 == 0 && cur.inventory == 0 && cur.inventory2 == 0){
		    if(cur.getInventorySLOT(two) != 0 && cur.getInventorySTACK(two) != 0){ // itemid && if stack != 0
			Charstuff.getInstance().respondguildTIMED("Please select an empty Inventory slot!", cur.GetChannel());
			cur.ClearInv();
			return null;
		    }
		}
			
			if(!Charstuff.getInstance().tryStackable_items(cur.getInventorySLOT(one))){cur.ClearInv();return null;}
			int stackable = Charstuff.getInstance().getStackable_items(cur.getInventorySLOT(two));
			if(stackable == 0){
				five = 1;
				stack= 1;
			}
			
	    	//Determine x,y,stack
			cur.setInventoryHEIGHT(two, three);
			cur.setInventoryWEIGHT(two, four);
				

			//if item is stackable && stack is or below 0 ????????????????????????????????????????????????????????????? <--- HELP FIX THIS SHITTTTT
			if(five <= 0 && stackable != 0){
			//System.out.println("===> : 0 ");	
			cur.setInventorySTACK(two, stack);
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), inv); 
			cur.DeleteItemNOMESSAGE(two);
			Charstuff.getInstance().respondguildTIMED("Stack is OR below 0, Fake item!", cur.GetChannel());
			return null;
			} 
			
		  
			//========== Two ============/
			if(itemid == cur.getInventorySLOT(two) && five == 1 && stackable != 0){ // merging item +1
				//System.out.println("===> : 1");
			cur.setInventorySTACK(two, cur.getInventorySTACK(two) + 1);	
			}else 
			if(itemid == cur.getInventorySLOT(two) && five == stack && stackable != 0){ // merging item + any stack
				//System.out.println("===> : 1.5");
			cur.setInventorySTACK(two, cur.getInventorySTACK(two) + stack);	
			}else 
			if(stackable == 0){ //move OR replace non stackable
				//System.out.println("===> : 1.8");
				cur.setInventorySTACK(two, 1);	
			}else{
				//System.out.println("===> : 2");
				cur.setInventorySTACK(two, stack);	// fuck it but still serverside
			}
			//System.out.println("2 same?:"+itemid+" == "+cur.getInventorySLOT(two));
			//System.out.println("2 two:"+one+" - "+two+" : "+Charstuff.getitemtoname(cur.getInventorySLOT(two))+" - "+cur.getInventorySTACK(two));
			*/
		
		}
		return inv;
	}

}
