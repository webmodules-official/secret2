package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.grinditems;
import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import chat.ChatCommandExecutor;

public class getallchars implements ChatCommandExecutor {
	
	public ConcurrentMap<Integer, Integer> CargoSLOT = new ConcurrentHashMap<Integer, Integer>(); // SLOT
	public ConcurrentMap<Integer, Integer> CargoHEIGHT = new ConcurrentHashMap<Integer, Integer>(); // HEIGHT
	public ConcurrentMap<Integer, Integer> CargoWEIGHT = new ConcurrentHashMap<Integer, Integer>(); // WEIGHT
	public ConcurrentMap<Integer, Integer> CargoSTACK = new ConcurrentHashMap<Integer, Integer>();// ITEM STACK
	
	public ConcurrentMap<Integer, Integer> InventorySLOT = new ConcurrentHashMap<Integer, Integer>(); // SLOT
	public ConcurrentMap<Integer, Integer> InventoryHEIGHT = new ConcurrentHashMap<Integer, Integer>(); // HEIGHT
	public ConcurrentMap<Integer, Integer> InventoryWEIGHT = new ConcurrentHashMap<Integer, Integer>(); // WEIGHT
	public ConcurrentMap<Integer, Integer> InventorySTACK = new ConcurrentHashMap<Integer, Integer>();// ITEM STACK
	
	// 4-Way Set Inventory
	public void setInventory(int SLOT, int ItemID, int y, int x, int Stack) {
		InventorySLOT.put(SLOT, ItemID);
		InventoryHEIGHT.put(SLOT, y); 
		InventoryWEIGHT.put(SLOT, x); 
		
	if(this.getInventorySLOT(SLOT) == 213010007 
	||this.getInventorySLOT(SLOT) == 213020007
	||this.getInventorySLOT(SLOT) == 273001251
	||this.getInventorySLOT(SLOT) == 283000001
	||this.getInventorySLOT(SLOT) == 283000002
	||this.getInventorySLOT(SLOT) == 213062709
	){InventorySTACK.put(Integer.valueOf(SLOT), Integer.valueOf(Stack));}
	else 
	if(Stack > 100){ 
	InventorySTACK.put(Integer.valueOf(SLOT), Integer.valueOf(100)); 
	}else 
	if(Stack <= 0){ 
	// Dont put stack if its 0 or below
	}else{
	InventorySTACK.put(Integer.valueOf(SLOT), Integer.valueOf(Stack));} 
	//System.out.println("setInventory: " +SLOT+" - " +ItemID+" - " +x+" - " +y+" - " +Stack);
	}
	

	// 4-Way Set Cargo
	public void setCargo(int SLOT, int ItemID, int y, int x, int Stack) {
		CargoSLOT.put(SLOT, ItemID);
		CargoHEIGHT.put(SLOT, y); 
		CargoWEIGHT.put(SLOT, x); 
		
	if(this.getInventorySLOT(SLOT) == 213010007 
	||this.getInventorySLOT(SLOT) == 213020007
	||this.getInventorySLOT(SLOT) == 273001251
	||this.getInventorySLOT(SLOT) == 283000001
	||this.getInventorySLOT(SLOT) == 283000002
	||this.getInventorySLOT(SLOT) == 213062709
	){CargoSTACK.put(Integer.valueOf(SLOT), Integer.valueOf(Stack));}
	else 
	if(Stack > 100){ 
		CargoSTACK.put(Integer.valueOf(SLOT), Integer.valueOf(100)); 
	}else 
	if(Stack <= 0){ 
	// Dont put stack if its 0 or below
	}else{
	CargoSTACK.put(Integer.valueOf(SLOT), Integer.valueOf(Stack));} 
	//System.out.println("setInventory: " +SLOT+" - " +ItemID+" - " +x+" - " +y+" - " +Stack);
	}
	
	
	public int getCargoSTACK(int invslot) {
		if(CargoSTACK.containsKey(invslot)){
		int invvalue = CargoSTACK.get(invslot);
		//System.out.println("getCargoSTACK: " +invslot+" - " +invvalue);
		return invvalue;}else 
		{return 0;}
	}

	public void setCargoSTACK(int INVslot, int itemstack) {
		CargoSTACK.put(Integer.valueOf(INVslot), Integer.valueOf(itemstack)); 
	}		
	
	public int getCargoSLOT(int invslot) {
		if(CargoSLOT.containsKey(invslot)){
		int invvalue = CargoSLOT.get(invslot);
	//	System.out.println("getCargoSLOT: " +invslot+" - " +invvalue);
		return invvalue;}else{return 0;}
	}

	public void setCargoSLOT(int INVslot, int itemid) {
		CargoSLOT.put(Integer.valueOf(INVslot), Integer.valueOf(itemid)); 
	}	
	
	public int getCargoHEIGHT(int invslot) {
		if(CargoHEIGHT.containsKey(invslot)){
		int invvalue = CargoHEIGHT.get(invslot);
	//	System.out.println("getCargoHEIGHT: " +invslot+" - " +invvalue);
		return invvalue;}else{return 0;}
	}

	public void setCargoHEIGHT(int INVslot, int invheight) {
		CargoHEIGHT.put(Integer.valueOf(INVslot), Integer.valueOf(invheight)); 
	}
	
	public int getCargoWEIGHT(int invslot) {
		if(CargoWEIGHT.containsKey(invslot)){
			int invvalue = CargoWEIGHT.get(invslot);
			//System.out.println("getCargoWEIGHT: " +invslot+" - " +invvalue);
			return invvalue;}else
			{return 0;}
	}

	public void setCargoWEIGHT(int INVslot, int invweight) {
		CargoWEIGHT.put(Integer.valueOf(INVslot), Integer.valueOf(invweight)); 
	}

	
	
	
	 // inv stack
		public int getInventorySTACK(int invslot) {
			if(InventorySTACK.containsKey(invslot)){
			int invvalue = InventorySTACK.get(invslot);
			//System.out.println("InventorySTACK: " +invslot+" - " +invvalue);
			return invvalue;}else
			{ //System.out.println(invslot+" - null "); 
			return 0;}
		}

		public void setInventorySTACK(int INVslot, int itemstack) {
			//System.out.println("InventorySTACK: " +this.getInventorySLOT(INVslot)+" - " +itemstack);
				   if(this.getInventorySLOT(INVslot) == 213010007 
					||this.getInventorySLOT(INVslot) == 213020007
					){ 
					InventorySTACK.put(Integer.valueOf(INVslot), Integer.valueOf(itemstack)); 
				  }else 
			if(itemstack > 100){ 
			InventorySTACK.put(Integer.valueOf(INVslot), Integer.valueOf(100)); 
			}else 
			if(itemstack < 0){ 
			InventorySTACK.put(Integer.valueOf(INVslot), Integer.valueOf(0)); 
			}else{
			InventorySTACK.put(Integer.valueOf(INVslot), Integer.valueOf(itemstack)); 
			}
		}	
	
	 // inv slot
	public int getInventorySLOT(int invslot) {
		if(InventorySLOT.containsKey(invslot)){
		int invvalue = InventorySLOT.get(invslot);
		//System.out.println("InventorySLOT: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setInventorySLOT(int INVslot, int itemid) {
		InventorySLOT.put(Integer.valueOf(INVslot), Integer.valueOf(itemid)); 
	//System.out.println("InventorySLOT: " +INVslot+" - " +itemid);
	}	
	
	 // inv height
	public int getInventoryHEIGHT(int invslot) {
		if(InventoryHEIGHT.containsKey(invslot)){
		int invvalue = InventoryHEIGHT.get(invslot);
		//System.out.println("InventoryHEIGHT: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setInventoryHEIGHT(int INVslot, int invheight) {
		InventoryHEIGHT.put(Integer.valueOf(INVslot), Integer.valueOf(invheight)); 
	//System.out.println("InventoryHEIGHT: " +INVslot+" - " +invheight);
	}
	
	// inv weight
	public int getInventoryWEIGHT(int invslot) {
		if(InventoryWEIGHT.containsKey(invslot)){
			int invvalue = InventoryWEIGHT.get(invslot);
			//System.out.println("InventoryWEIGHT: " +invslot+" - " +invvalue);
			return invvalue;}else
			{ //System.out.println(invslot+" - null "); 
			return 0;}
	}

	public void setInventoryWEIGHT(int INVslot, int invweight) {
		InventoryWEIGHT.put(Integer.valueOf(INVslot), Integer.valueOf(invweight)); 
		//System.out.println("InventoryWEIGHT: " +INVslot+" - " +invweight);
	}
	
	public ConcurrentMap<Integer, Long> item_end_date = new ConcurrentHashMap<Integer, Long>();
	
	public long getitem_end_date(int invslot) {
		if(item_end_date.containsKey(invslot)){
		long invvalue = item_end_date.get(invslot);
		//System.out.println("boothwindowPRICE: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setitem_end_date(int INVslot, long item_end_datez) {
		item_end_date.put(Integer.valueOf(INVslot), item_end_datez); 
		//System.out.println("item_end_date: " +INVslot+" - " +item_end_datez);
	}
	
	public void getcargo(String onez){
		try{

				// invslot, invitemid, invx, invy, invstack
				if(onez != null|| onez != "0"){
				String one = onez;
				//System.out.println("Parsing: " + one);
				String[] splat1 = one.split(",");
				for(int i=0;i<splat1.length;i++) {
				this.setCargoSLOT(Integer.valueOf(splat1[i]), Integer.valueOf(splat1[i+1]));
				i++;
				i++;
				this.setCargoWEIGHT(Integer.valueOf(splat1[i-2]), Integer.valueOf(splat1[i]));
				i++;
				this.setCargoHEIGHT(Integer.valueOf(splat1[i-3]), Integer.valueOf(splat1[i]));
				i++;
				this.setCargoSTACK(Integer.valueOf(splat1[i-4]), Integer.valueOf(splat1[i]));
				}}
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
	}
	
	
	public void getinventorys(String onez){
		try{

				// invslot, invitemid, invx, invy, invstack
				if(onez != null|| onez != "0"){
				String one = onez;
				//System.out.println("Parsing: " + one);
				String[] splat1 = one.split(",");
				for(int i=0;i<splat1.length;i++) {
				this.setInventorySLOT(Integer.valueOf(splat1[i]), Integer.valueOf(splat1[i+1]));
				i++;
				i++;
				this.setInventoryWEIGHT(Integer.valueOf(splat1[i-2]), Integer.valueOf(splat1[i]));
				i++;
				this.setInventoryHEIGHT(Integer.valueOf(splat1[i-3]), Integer.valueOf(splat1[i]));
				i++;
				this.setInventorySTACK(Integer.valueOf(splat1[i-4]), Integer.valueOf(splat1[i]));
				}}
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
	}
	
	public void getsexpire(String onez){
		try{
				if(onez != null && onez != "0"){
				String one = onez;
				String[] splat1 = one.split(",");
				for(int i=0;i<splat1.length;i++) {	
				this.setitem_end_date(Integer.valueOf(splat1[i]), Long.valueOf(splat1[i+1]));
				i++;
				}
			 }
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
	}
	
	public void saveinvcargo(int charid){
		// save equip, inventory, skillbar, skills
		String inventory = "";
		for(int i=0;i<120;i++){
		if(this.InventorySLOT.containsKey(i)){
		inventory += Integer.toString(i);	
		inventory +=",";
		inventory += Integer.toString(this.getInventorySLOT(i));
		inventory +=",";
		inventory += Integer.toString(this.getInventoryWEIGHT(i));
		inventory +=",";
		inventory += Integer.toString(this.getInventoryHEIGHT(i));	
		inventory +=",";
		inventory += Integer.toString(this.getInventorySTACK(i));
		inventory +=",";
		}}	
		//System.out.println("Output: " + inventory);
		
		// cargo
		String cargo = "";
		for(int i=0;i<120;i++){
		if(this.CargoSLOT.containsKey(i)){
			cargo += Integer.toString(i);	
			cargo +=",";
			cargo += Integer.toString(this.getCargoSLOT(i));
			cargo +=",";
			cargo += Integer.toString(this.getCargoWEIGHT(i));
			cargo +=",";
			cargo += Integer.toString(this.getCargoHEIGHT(i));	
			cargo +=",";
			cargo += Integer.toString(this.getCargoSTACK(i));
			cargo +=",";
		}}

		
		// save character to DB
		CharacterDAO.saveinvcargo(inventory, cargo, charid);
	}
	
	public void DeleteInvItem(int SLOT) {
		 // remove inventory item by slot
		//System.out.println("DELETED SLOT: "+SLOT);
		this.InventorySLOT.remove(SLOT);
		this.InventoryHEIGHT.remove(SLOT);
		this.InventoryWEIGHT.remove(SLOT);
		this.InventorySTACK.remove(SLOT);
	}
	
	public void DeleteCargoItem(int SLOT) {
		 // remove inventory item by slot
		//System.out.println("DELETED SLOT: "+SLOT);
		this.CargoSLOT.remove(SLOT);
		this.CargoHEIGHT.remove(SLOT);
		this.CargoWEIGHT.remove(SLOT);
		this.CargoSTACK.remove(SLOT);
	}
	
	public void clearall() {
		this.CargoSLOT.clear();
		this.CargoHEIGHT.clear();
		this.CargoWEIGHT.clear();
		this.CargoSTACK.clear();
		
		this.InventorySLOT.clear();
		this.InventoryHEIGHT.clear();
		this.InventoryWEIGHT.clear();
		this.InventorySTACK.clear();
	}
	

	public void execute(String[] parameters, Connection con) {
		//System.out.println("Handling teleport command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}	
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		Charstuff.getInstance().respond("Scanning all characters from Database...",con);
		int ITEMID = Integer.valueOf(parameters[0]);//itemid
		int STACK = Integer.valueOf(parameters[1]);//stack
		int delete = Integer.valueOf(parameters[2]);//0 = Scan | 1 = Delete
		int count = 0;//how many characters has duped items
		int howmanyfound = 0;// how many items found
		int howmanyfoundstack = 0;// how many total stacks found
		try{
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getCharacters(SQLconnection.getInstance().getaConnection()));
			if(rs != null){
			while(rs.next()){
		    boolean hacker = false;
		    
		    //load shit
		    this.getinventorys(rs.getString("inventory"));
		    this.getcargo(rs.getString("cargo"));
		    /*
		    exp tag c, fad fasr, ffd casr, sap l, sap m, 100 mhp, 500 mhp, 1000mhp, goldbars, ?
		    */
		    
		    //Inventory
			Iterator<Entry<Integer, Integer>> iter = this.InventorySLOT.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<Integer, Integer> pairs = iter.next();
				int itemid = pairs.getValue();
				int stack = this.getInventorySTACK(pairs.getKey());
				if(itemid == ITEMID && stack >= STACK){if(delete == 1){this.DeleteInvItem(pairs.getKey());} howmanyfound++; howmanyfoundstack = howmanyfoundstack + stack; hacker = true;}	
			}
			
			//cargo
			Iterator<Entry<Integer, Integer>> iter1 = this.CargoSLOT.entrySet().iterator();
			while(iter1.hasNext()) {
				Entry<Integer, Integer> pairs1 = iter1.next();
				int itemid = pairs1.getValue();
				int stack = this.getCargoSTACK(pairs1.getKey());
				if(itemid == ITEMID && stack >= STACK){if(delete == 1){this.DeleteCargoItem(pairs1.getKey());} howmanyfound++; howmanyfoundstack = howmanyfoundstack + stack; hacker = true;}
			}
			
		    this.saveinvcargo(rs.getInt("CharacterID"));
		    if(hacker){count++; System.out.println(count+" ID:"+rs.getInt("CharacterID")+" Lvl:"+rs.getInt("level")+" Name:"+rs.getString("charname"));}
		    
		    //clear all for recycle
		    this.clearall();
			}
			}else{Charstuff.getInstance().respond("Erorr cannot scan, dafuq.",con); return;}
		    }catch (Exception e){}
	        Charstuff.getInstance().respond("Found "+count+" chars and with "+howmanyfound+" duped "+Charstuff.getInstance().getitemtoname(ITEMID)+" with total stack "+howmanyfoundstack,con);
		
	
		
	}
}
