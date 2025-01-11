package GameServer.GamePackets;

import item.Item;
import item.ItemCache;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import timer.SystemTimer;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.lookuplevel;
import Player.manuals;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import Encryption.Decryptor;

public class ProcedureItem implements Packet {
	 private ConcurrentMap<Integer, Integer> TempStore = new ConcurrentHashMap<Integer, Integer>();
	 private ConcurrentMap<Integer, Integer> TempStack = new ConcurrentHashMap<Integer, Integer>();
	 
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public int getTempStack(int one) {
		if(TempStack.containsKey(one)){
		int two = TempStack.get(one);
		//System.out.println("InventorySTACK: " +invslot+" - " +invvalue);
		return two;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}
	
	public int getTempStore(int one) {
		if(TempStore.containsKey(one)){
		int two = TempStore.get(one);
		//System.out.println("InventorySTACK: " +invslot+" - " +invvalue);
		return two;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		byte[] Meowzie;
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		//System.out.println(" | ");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[168];
		String e;
		int min1,min2,min3,min4,min5,min6,min7,min8;
		int Time = 0;
		byte[] Product; 
		byte[] id = new byte[4];
		fury[0] = (byte)0xa8;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x28;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;
	
		for(int i=0;i<2;i++) {
			id[i] = decrypted[i]; 
		}
		int ProcedureID = BitTools.byteArrayToInt(id);
		boolean valid = false;
		/*System.out.println("Procedure1:"+Charstuff.getitemtoprocedure1(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure2:"+Charstuff.getitemtoprocedure2(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure3:"+Charstuff.getitemtoprocedure3(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure4:"+Charstuff.getitemtoprocedure4(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure5:"+Charstuff.getitemtoprocedure5(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure6:"+Charstuff.getitemtoprocedure6(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure7:"+Charstuff.getitemtoprocedure7(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure8:"+Charstuff.getitemtoprocedure8(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure9:"+Charstuff.getitemtoprocedure9(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure10:"+Charstuff.getitemtoprocedure10(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure11:"+Charstuff.getitemtoprocedure11(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure12:"+Charstuff.getitemtoprocedure12(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure13:"+Charstuff.getitemtoprocedure13(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure14:"+Charstuff.getitemtoprocedure14(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure15:"+Charstuff.getitemtoprocedure15(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure16:"+Charstuff.getitemtoprocedure16(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure17:"+Charstuff.getitemtoprocedure17(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure18:"+Charstuff.getitemtoprocedure18(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure19:"+Charstuff.getitemtoprocedure19(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure20:"+Charstuff.getitemtoprocedure20(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure20:"+Charstuff.getitemtoprocedure21(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure20:"+Charstuff.getitemtoprocedure22(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure20:"+Charstuff.getitemtoprocedure23(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);
		System.out.println("Procedure20:"+Charstuff.getitemtoprocedure24(cur.getInventorySLOT((int)decrypted[4]))+" - "+ProcedureID);*/

		if(Charstuff.getInstance().getitemtoprocedure1(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure2(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure3(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure4(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure5(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure6(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure7(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure8(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure9(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure10(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure11(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure12(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure13(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure14(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure15(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure16(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure17(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure18(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure19(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure20(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure21(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure22(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure23(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		if(Charstuff.getInstance().getitemtoprocedure24(cur.getInventorySLOT((int)decrypted[4])) == ProcedureID){valid = true;}
		
		
		
		if(valid == false){	System.out.println("FAILL");return null;}
		if(manuals.manuals.containsKey(ProcedureID))
		{
		e = manuals.manuals.get(ProcedureID);
		String[] procedure = e.split(",");	
			
		//itemid
		int itemid = Integer.valueOf(procedure[0]);
		
		//check if player has all the mats in his inventory
		if(Integer.valueOf(procedure[1]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[1]))){return null;}
		if(Integer.valueOf(procedure[3]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[3]))){return null;}
		if(Integer.valueOf(procedure[5]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[5]))){return null;}
		if(Integer.valueOf(procedure[7]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[7]))){return null;}
		if(Integer.valueOf(procedure[9]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[9]))){return null;}
		if(Integer.valueOf(procedure[11]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[11]))){return null;}
		if(Integer.valueOf(procedure[13]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[13]))){return null;}
		if(Integer.valueOf(procedure[15]) != 0 &&!cur.InventorySLOT.containsValue(Integer.valueOf(procedure[15]))){return null;}
		

		
		//set inventory keys 
		//manual
		int key = (int)decrypted[4];
		//mats
		int key1 = (int)decrypted[8];
		int key2 = (int)decrypted[12];
		int key3 = (int)decrypted[16];
		int key4 = (int)decrypted[20];
		int key5 = (int)decrypted[24];
		int key6 = (int)decrypted[28];
		int key7 = (int)decrypted[32];
		int key8 = (int)decrypted[36];
		
		//if(cur.getInventorySLOT(key) ){}
		
		if(cur.getInventorySLOT(key) == 0 && key != -1){
			cur.DeleteItemNOMESSAGE(key); 
			//Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key1) == 0 && key1 != -1){
			cur.DeleteItemNOMESSAGE(key1); 
			//Charstuff.getInstance().respondguild("Fake Item1!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key2) == 0 && key2 != -1){
			cur.DeleteItemNOMESSAGE(key2); 
			//Charstuff.getInstance().respondguild("Fake Item2!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key3) == 0 && key3 != -1){
			cur.DeleteItemNOMESSAGE(key3); 
			//Charstuff.getInstance().respondguild("Fake Item3!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key4) == 0 && key4 != -1){
			cur.DeleteItemNOMESSAGE(key4); 
			//Charstuff.getInstance().respondguild("Fake Item4!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key5) == 0 && key5 != -1){
			cur.DeleteItemNOMESSAGE(key5); 
			//Charstuff.getInstance().respondguild("Fake Item5!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key6) == 0 && key6 != -1){
			cur.DeleteItemNOMESSAGE(key6); 
			//Charstuff.getInstance().respondguild("Fake Item6!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key7) == 0 && key7 != -1){
			cur.DeleteItemNOMESSAGE(key7); 
			//Charstuff.getInstance().respondguild("Fake Item7!", cur.GetChannel());
			return null;
		}
		if(cur.getInventorySLOT(key8) == 0 && key8 != -1){
			cur.DeleteItemNOMESSAGE(key8); 
			//Charstuff.getInstance().respondguild("Fake Item8!", cur.GetChannel());
			return null;
		}
		
		
		//set the itemids && min from the manual db
		// material1, stack1 ETC ETC ETC.
		this.TempStore.put(Integer.valueOf(procedure[1]), Integer.valueOf(procedure[2]));
		this.TempStore.put(Integer.valueOf(procedure[3]), Integer.valueOf(procedure[4]));
		this.TempStore.put(Integer.valueOf(procedure[5]), Integer.valueOf(procedure[6]));
		this.TempStore.put(Integer.valueOf(procedure[7]), Integer.valueOf(procedure[8]));
		this.TempStore.put(Integer.valueOf(procedure[9]), Integer.valueOf(procedure[10]));
		this.TempStore.put(Integer.valueOf(procedure[11]), Integer.valueOf(procedure[12]));
		this.TempStore.put(Integer.valueOf(procedure[13]), Integer.valueOf(procedure[14]));
		this.TempStore.put(Integer.valueOf(procedure[15]), Integer.valueOf(procedure[16]));
		
		/*System.out.println("newvalue: "+Integer.valueOf(procedure[1])+"- "+Integer.valueOf(procedure[2]));
		System.out.println("newvalue: "+Integer.valueOf(procedure[3])+"- "+Integer.valueOf(procedure[4]));
		System.out.println("newvalue: "+Integer.valueOf(procedure[5])+"- "+Integer.valueOf(procedure[6]));
		System.out.println("newvalue: "+Integer.valueOf(procedure[7])+"- "+Integer.valueOf(procedure[8]));
		System.out.println("newvalue: "+Integer.valueOf(procedure[9])+"- "+Integer.valueOf(procedure[10]));
		System.out.println("newvalue: "+Integer.valueOf(procedure[11])+"- "+Integer.valueOf(procedure[12]));
		System.out.println("newvalue: "+Integer.valueOf(procedure[13])+"- "+Integer.valueOf(procedure[14]));
		System.out.println("newvalue: "+Integer.valueOf(procedure[15])+"- "+Integer.valueOf(procedure[16]));*/

		
		// calculate the new value
		this.TempStack.put(key1, cur.getInventorySTACK(key1));
		this.TempStack.put(key2, cur.getInventorySTACK(key2));
		this.TempStack.put(key3, cur.getInventorySTACK(key3));
		this.TempStack.put(key4, cur.getInventorySTACK(key4));
		this.TempStack.put(key5, cur.getInventorySTACK(key5));
		this.TempStack.put(key6, cur.getInventorySTACK(key6));
		this.TempStack.put(key7, cur.getInventorySTACK(key7));
		this.TempStack.put(key8, cur.getInventorySTACK(key8));
		
		
		//find splitted materials and merge them
		Iterator<Entry<Integer, Integer>> iter = this.TempStack.entrySet().iterator();   
		while(iter.hasNext()) {
			Entry<Integer, Integer> pairs = iter.next();
			int SLOT1 = pairs.getKey(); int STACK1 = pairs.getValue();
			//System.out.println("iter:"+SLOT1+" - "+STACK1);
			Iterator<Entry<Integer, Integer>> iter1 = this.TempStack.entrySet().iterator();   
			while(iter1.hasNext()) {
				Entry<Integer, Integer> pairs1 = iter1.next();
				int SLOT2 = pairs1.getKey(); int STACK2 = pairs1.getValue();
				//System.out.println("iter1:"+SLOT1+" - "+STACK1+" --- "+SLOT2+" - "+STACK2);
				if(cur.getInventorySLOT(SLOT1) == cur.getInventorySLOT(SLOT2) && SLOT1 != SLOT2 && STACK1 != 0 && STACK2 != 0){
				//System.out.println("OLD SLOT:"+SLOT1+" - "+SLOT2+" --- STACK:"+this.getTempStack(SLOT1)+"- "+this.getTempStack(SLOT2));
				this.TempStack.put(SLOT1, STACK1 + STACK2);
				this.TempStack.put(SLOT2, 0);
				//System.out.println("NEW SLOT:"+SLOT1+" - "+SLOT2+" --- STACK:"+this.getTempStack(SLOT1)+"- "+this.getTempStack(SLOT2));
				}
				
			}
		}
		
		
		
		//manual
		int newvalue = cur.getInventorySTACK(key) - 1;
		//mats
		int newvalue1 = 0;
		int newvalue2 = 0;
		int newvalue3 = 0;
		int newvalue4 = 0;
		int newvalue5 = 0;
		int newvalue6 = 0;
		int newvalue7 = 0;
		int newvalue8 = 0;
		
		
	if(this.getTempStack(key1) != 0){newvalue1 = this.getTempStack(key1) - this.getTempStore(cur.getInventorySLOT(key1));}
	if(this.getTempStack(key2) != 0){newvalue2 = this.getTempStack(key2) - this.getTempStore(cur.getInventorySLOT(key2));}
	if(this.getTempStack(key3) != 0){newvalue3 = this.getTempStack(key3) - this.getTempStore(cur.getInventorySLOT(key3));}
	if(this.getTempStack(key4) != 0){newvalue4 = this.getTempStack(key4) - this.getTempStore(cur.getInventorySLOT(key4));}
	if(this.getTempStack(key5) != 0){newvalue5 = this.getTempStack(key5) - this.getTempStore(cur.getInventorySLOT(key5));}
	if(this.getTempStack(key6) != 0){newvalue6 = this.getTempStack(key6) - this.getTempStore(cur.getInventorySLOT(key6));}
	if(this.getTempStack(key7) != 0){newvalue7 = this.getTempStack(key7) - this.getTempStore(cur.getInventorySLOT(key7));}
	if(this.getTempStack(key8) != 0){newvalue8 = this.getTempStack(key8) - this.getTempStore(cur.getInventorySLOT(key8));}
	
	
	
	//min from the manual db
	min1 = cur.getInventorySTACK(key1) - newvalue1;
	min2 = cur.getInventorySTACK(key2) - newvalue2;
	min3 = cur.getInventorySTACK(key3) - newvalue3;
	min4 = cur.getInventorySTACK(key4) - newvalue4;
	min5 = cur.getInventorySTACK(key5) - newvalue5;
	min6 = cur.getInventorySTACK(key6) - newvalue6;
	min7 = cur.getInventorySTACK(key7) - newvalue7;
	min8 = cur.getInventorySTACK(key8) - newvalue8;
		
	/*System.out.println("newvalue1: "+cur.getInventorySTACK(key1)+" - "+newvalue1+" = "+min1);
	System.out.println("newvalue2: "+cur.getInventorySTACK(key2)+" - "+newvalue2+" = "+min2);
	System.out.println("newvalue3: "+cur.getInventorySTACK(key3)+" - "+newvalue3+" = "+min3);
	System.out.println("newvalue4: "+cur.getInventorySTACK(key4)+" - "+newvalue4+" = "+min4);
	System.out.println("newvalue5: "+cur.getInventorySTACK(key5)+" - "+newvalue5+" = "+min5);
	System.out.println("newvalue6: "+cur.getInventorySTACK(key6)+" - "+newvalue6+" = "+min6);
	System.out.println("newvalue7: "+cur.getInventorySTACK(key7)+" - "+newvalue7+" = "+min7);
	System.out.println("newvalue8: "+cur.getInventorySTACK(key8)+" - "+newvalue8+" = "+min8);*/

		
	/*System.out.println("newvalue: "+key+"- "+cur.getInventorySLOT(key)+"- "+cur.getInventorySTACK(key)+" - "+1+" = "+newvalue);
	if(this.getTempStack(key1) != 0){System.out.println("newvalue1: "+key1+"- "+cur.getInventorySLOT(key1)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key1)), TempStore)+"- "+this.getTempStack(key1)+" - "+this.getTempStore(cur.getInventorySLOT(key1))+" = "+newvalue1);}
	if(this.getTempStack(key2) != 0){System.out.println("newvalue2: "+key2+"- "+cur.getInventorySLOT(key2)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key2)), TempStore)+"- "+this.getTempStack(key2)+" - "+this.getTempStore(cur.getInventorySLOT(key2))+" = "+newvalue2);}
	if(this.getTempStack(key3) != 0){System.out.println("newvalue3: "+key3+"- "+cur.getInventorySLOT(key3)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key3)), TempStore)+"- "+this.getTempStack(key3)+" - "+this.getTempStore(cur.getInventorySLOT(key3))+" = "+newvalue3);}
	if(this.getTempStack(key4) != 0){System.out.println("newvalue4: "+key4+"- "+cur.getInventorySLOT(key4)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key4)), TempStore)+"- "+this.getTempStack(key4)+" - "+this.getTempStore(cur.getInventorySLOT(key4))+" = "+newvalue4);}
	if(this.getTempStack(key5) != 0){System.out.println("newvalue5: "+key5+"- "+cur.getInventorySLOT(key5)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key5)), TempStore)+"- "+this.getTempStack(key5)+" - "+this.getTempStore(cur.getInventorySLOT(key5))+" = "+newvalue5);}
	if(this.getTempStack(key6) != 0){System.out.println("newvalue6: "+key6+"- "+cur.getInventorySLOT(key6)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key6)), TempStore)+"- "+this.getTempStack(key6)+" - "+this.getTempStore(cur.getInventorySLOT(key6))+" = "+newvalue6);}
	if(this.getTempStack(key7) != 0){System.out.println("newvalue7: "+key7+"- "+cur.getInventorySLOT(key7)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key7)), TempStore)+"- "+this.getTempStack(key7)+" - "+this.getTempStore(cur.getInventorySLOT(key7))+" = "+newvalue7);}
	if(this.getTempStack(key8) != 0){System.out.println("newvalue8: "+key8+"- "+cur.getInventorySLOT(key8)+"- "+BitTools.ValueToKey(this.getTempStore(cur.getInventorySLOT(key8)), TempStore)+"- "+this.getTempStack(key8)+" - "+this.getTempStore(cur.getInventorySLOT(key8))+" = "+newvalue8);}
		*/
		TempStore.clear();
		TempStack.clear();
		
		// be sure if player has the right inventorySTACKS.
		//manual
		if(newvalue < 0){return null;}
		//mats
		if(newvalue1 < 0){int need = newvalue1 - newvalue1 - newvalue1; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key1)), cur.GetChannel());return null;}
		if(newvalue2 < 0){int need = newvalue2 - newvalue2 - newvalue2; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key2)), cur.GetChannel());return null;}
		if(newvalue3 < 0){int need = newvalue3 - newvalue3 - newvalue3; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key3)), cur.GetChannel());return null;}
		if(newvalue4 < 0){int need = newvalue4 - newvalue4 - newvalue4; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key4)), cur.GetChannel());return null;}
		if(newvalue5 < 0){int need = newvalue5 - newvalue5 - newvalue5; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key5)), cur.GetChannel());return null;}
		if(newvalue6 < 0){int need = newvalue6 - newvalue6 - newvalue6; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key6)), cur.GetChannel());return null;}
		if(newvalue7 < 0){int need = newvalue7 - newvalue7 - newvalue7; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key7)), cur.GetChannel());return null;}
		if(newvalue8 < 0){int need = newvalue8 - newvalue8 - newvalue8; Charstuff.getInstance().respondguild("Need "+need+" more: "+Charstuff.getInstance().getitemtoname(cur.getInventorySLOT(key8)), cur.GetChannel());return null;}
		
		// set the remaining stack back into the players inventory
		//manual
		if(newvalue == 0){cur.DeleteInvItem(key);}else{cur.setInventorySTACK(key, newvalue);}
		//mats
		if(newvalue1 == 0){cur.DeleteInvItem(key1);}else{cur.setInventorySTACK(key1, newvalue1);}
		if(newvalue2 == 0){cur.DeleteInvItem(key2);}else{cur.setInventorySTACK(key2, newvalue2);}
		if(newvalue3 == 0){cur.DeleteInvItem(key3);}else{cur.setInventorySTACK(key3, newvalue3);}
		if(newvalue4 == 0){cur.DeleteInvItem(key4);}else{cur.setInventorySTACK(key4, newvalue4);}
		if(newvalue5 == 0){cur.DeleteInvItem(key5);}else{cur.setInventorySTACK(key5, newvalue5);}
		if(newvalue6 == 0){cur.DeleteInvItem(key6);}else{cur.setInventorySTACK(key6, newvalue6);}
		if(newvalue7 == 0){cur.DeleteInvItem(key7);}else{cur.setInventorySTACK(key7, newvalue7);}
		if(newvalue8 == 0){cur.DeleteInvItem(key8);}else{cur.setInventorySTACK(key8, newvalue8);}
		
		// it has succeded all the checks n stuff now on to kfc.
		cur.setInventory((int)decrypted[68], itemid, (int)decrypted[69], (int)decrypted[70], 1);
		
		Product = BitTools.intToByteArray(itemid);	
		
		//set expire if has expire in itemdata
		String e0 = null;
		if(ItemCache.Items.containsKey(itemid)){e0 = ItemCache.Items.get(itemid);}
		if(e0 != null){
		String[] item0 = e0.split(",");
		if(Long.valueOf(item0[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item0[22])) + System.currentTimeMillis();
		cur.setitem_end_date(itemid, minato);
		cur.expireactive();
		Time = 1;
		}}
		
		// green announcement
		String Itemname = lookuplevel.getlookupitems(itemid);
		byte[] partychat = new byte[44+Itemname.length()];
		byte[] msg = Itemname.getBytes(); // <--- real gm msg lol
		partychat[0] = (byte)partychat.length;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x07;
		partychat[8] = (byte)0x01;
		partychat[9] = (byte)0xf7;
		partychat[10] = (byte)0x0f;
		partychat[11] = (byte)0x2b;
		partychat[12] = (byte)0x02;
		partychat[13] = (byte)0xad;
		partychat[14] = (byte)0xa0;
		partychat[17] = (byte)0x01;
		partychat[18] = (byte)0x69;
		byte[] name = cur.getLOGsetName().getBytes();
		for(int i=0;i<name.length;i++) {
			partychat[i+20] = name[i];
		}
		partychat[37] = (byte)0x9d; // = " : "
		partychat[38] = (byte)0x0f; // = " : "
		partychat[39] = (byte)0xbf; // = " : "
		partychat[40] = (byte)0x12; // = " : "
		for(int i=0;i<msg.length;i++) {
			partychat[i+44] = msg[i];
		}	
		
		Meowzie = partychat;
		}else{return null;}
		
		
		for(int i=0;i<4;i++) {
			fury[20+i] = decrypted[i]; 
		}
		
		for(int i=0;i<64;i++) {
			fury[24+i] = decrypted[4+i]; 
		}
		
		// the stack that -'s the item in inventory
		byte[] minvalue = BitTools.intToByteArray(1);
		byte[] minvalue1 = BitTools.intToByteArray(min1);
		byte[] minvalue2 = BitTools.intToByteArray(min2);
		byte[] minvalue3 = BitTools.intToByteArray(min3);
		byte[] minvalue4 = BitTools.intToByteArray(min4);
		byte[] minvalue5 = BitTools.intToByteArray(min5);
		byte[] minvalue6 = BitTools.intToByteArray(min6);
		byte[] minvalue7 = BitTools.intToByteArray(min7);
		byte[] minvalue8 = BitTools.intToByteArray(min8);
		for(int i=0;i<4;i++) {
			fury[88+i] =  minvalue[i];
			fury[92+i] =  minvalue1[i];
			fury[96+i] =  minvalue2[i];
			fury[100+i] =  minvalue3[i];
			fury[104+i] =  minvalue4[i];
			fury[108+i] =  minvalue5[i];
			fury[112+i] =  minvalue6[i];
			fury[116+i] =  minvalue7[i];
			fury[120+i] =  minvalue8[i];
		}
		
		if(Time == 1){
		fury[153] = (byte)0xff; 
		}
		fury[154] = decrypted[69]; 
		fury[155] = decrypted[70]; 
		
		for(int i=0;i<4;i++) {
			fury[156+i] = Product[i]; 
		}
		
		if(Time == 1){
		long timedate = cur.item_end_date.get(cur.getInventorySLOT((int)decrypted[68])) / 1000;
		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
		for(int i=0;i<4;i++) {
			fury[160+i] = TimenDate[i]; 
		}}
		
		fury[164] = decrypted[68]; 
		fury[165] = decrypted[69]; 
		fury[166] = decrypted[70];
		fury[167] = decrypted[71];
		
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);

		Iterator<Map.Entry<Integer, Character>> iter3 = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter3.hasNext()) {
			Map.Entry<Integer, Character> pairs3 = iter3.next();
			tmp = pairs3.getValue();
			ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), Meowzie);
		}
		
		return null;
	}
}
