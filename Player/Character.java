package Player;


import item.ItemCache;

import java.math.BigInteger;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import npc.Npc;
import npc.NpcController;
import oAreaTriggers.AreaTrigger;
import oAreaTriggers.AreaTriggerController;
import timer.MoveSyncTimer;
import timer.SystemTimer;
import timer.globalsave;

import logging.ServerLogger;

import Connections.Connection;
import Mob.Mob;
import Mob.MobController;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import GameServer.StartGameserver;
import GameServer.GamePackets.UseItemParser;
import Tools.BitTools;
import World.Grid;
import World.Location;
import World.WMap;
import World.Area;
import World.Waypoint;

public class Character implements Location {
	public static final int factionNeutral = 1;
	public static final int factionLawful = 2;
	public static final int factionEvil = 3;
	private String name = "NOP";
	private String LOGsetName;	
	private String isgm;  // if the character isgm -> az(ADMIN), 3(DEV) ,2(GM) ,1(GMH@)	
	public boolean IsTrading = false;
	public boolean IsVending = false;
	public int bufficon1 = 0;
	public int bufficon2 = 0;
	private int level;
	private int strength;
	private int agility;
	private int intelligence;
	private int vitality;
	private int dextery;
	public int furycheck;
	public int furyactive;
	public int furyvalue; 
	public int FuryTime;
	private int statPoints;
	private int skillPoints;
	private int farmmodule = 0;
	private int fame;
	public int fametitle = 7; // fame images depending on: fametitleid 1-8 & faction 1-2
	public boolean proffpool = false;
	public boolean killedbyplayer = false; // no exp :DD
	private int model; // model id ftw	
	public int FASR, CASR, FD, SAP, NEW_STATUS_SUNDAN; // FULL POT
	public int Exp_Tag_10, Exp_Tag_15, Exp_Tag_20, Exp_Tag_30, Exp_Tag_100, Fame_Tag_100, DOUBLE_ITEM_DROP_TAG, GREATER_DOUBLE_ITEM_DROP_TAG, JACKPOT_TAG, GREATER_JACKPOT_TAG; // Tags
	public int FAD, FDD ,FADR, DSR, ASR, FD_CCSR; // other pots
	public int SA_Red_Caste_Amulet, Sky_Zone_Amulet, Revival_Amulet; // Tickets
	private int characterClass;
	private Waypoint location;
	private Waypoint lastloc;
	private int GuildID; 
	public Guild guild;
	public String BoothName; public byte BoothStance; 
	public int partyUID;
	public int funcommands;
	public int TradeUID;
	public  int Summonid; // this clone PET ID
	private int faction;
	private int face;
	public double FPDefRate;
	public int FPCritRate;
	public double FPCritAbsorb;
	public int maxhp;
	public int hp;
	public int maxmana;
	public int mana;
	public long SpeedHackTimer;
	private long proffcd, proffDynamiccd;
	public int maxstamina;
	private int stamina;
	public int attack;
	public int defence;
	public int charID;
	private int currentMap;
	private int [] areaCoords;
	public Area area;
	private Grid grid;
	private byte[] characterDataPacket;
	public ConcurrentMap<Integer, Integer>  iniPackets = new ConcurrentHashMap<Integer, Integer>(50, 2, 50);
	private ServerLogger log = ServerLogger.getInstance();
	private WMap wmap = WMap.getInstance();
	private Player pl;
	private MoveSyncTimer timer;
	private int speed = 7;
	public int Walk_Or_Run = 1, Running, Runskill, Budoong, BudoongSkill;
	private int syncDistance = 30;
	private int ITEMatk, ITEMdef, ITEMlife, ITEMstr, ITEMdex, ITEMvit, ITEMint, ITEMagi, ITEMmana;
	private int SBITEMatk, SBITEMdef, SBITEMlife, SBITEMstr, SBITEMdex, SBITEMvit, SBITEMint, SBITEMagi, SBITEMdmgbonus, SBITEMmana;
	private long exp;
	private long gold;
	private int deletestate;
	public int equip = 0, equip2 = 0, REequip = 0; 
	public int inventory = 0, inventory2 = 0; 
	public int inventorystack = 0, inventorstack2 = 0 ,inventorystacktwo = 0, inventorstacktwo2 = 0; ; 
	public int cargo = 0, cargo2 = 0; 
	public int cargostack = 0, cargostack2 = 0; 
	public int Duel = 0;
	private double ATK = 0, DEF = 0;
	//private double Exp = 0, Fame = 0; //exp tags & fame tags  VALUES (normal = * 1)
	private int Sap = 0, New_Status_Sundan = 0; //FULL POT VALUES
	private int Fad = 1, Fdd = 1, Fadr = 1, Dsr = 1, Asr = 1; // other VALUES
	private int Great_Jackpot_Tag = 1, Double_item_drop_tag = 1, Jackpot_tag = 1; // GJT VALUES ( 1 is *1 = NORMAL STANDARD )
	public int MANA_SHIELD_PROTECTION = 0, HIDING = 0;
	public long TimestampFury; 
	public int vendingpoint = 0;
	private long RespawnCD;
	public long attackskillcd;
	public ConcurrentMap<Integer, Integer>  Respawn = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer>  BuffPercentage = new ConcurrentHashMap<Integer, Integer>();
	public  long CDone;
	//---------------------------------------------------HASH MAPPIE---------------------------------------------------------------\\
	
	
	public void respondguildTIMED(String message, SocketChannel wtf)
	{
		if (System.currentTimeMillis() - CDone > 1000 ){ 
		byte[] gmsg = new byte[45+message.length()];
		byte[] msg = message.getBytes(); 
		gmsg[0] = (byte)gmsg.length;
		gmsg[4] = (byte)0x05;
		gmsg[6] = (byte)0x07;
		gmsg[8] = (byte)0x01;
		gmsg[17] = (byte)0x01;
		gmsg[18] = (byte)0x03;
		
		gmsg[20] = (byte)0xa4; // begin letter name
		gmsg[21] = (byte)0xd1;
		byte[] name = "[Server]".getBytes();
		for(int i=0;i<name.length;i++) {
			gmsg[i+20] = name[i];
		}
		gmsg[40] = (byte)0x44; // = " : "
		for(int i=0;i<msg.length;i++) {
			gmsg[i+44] = msg[i];
		}
		if(ServerFacade.getInstance().getConnectionByChannel(wtf) != null){
		ServerFacade.getInstance().addWriteByChannel(wtf, gmsg);
		}
		CDone = System.currentTimeMillis();
		}
	}
	
	
	public Map<Integer, Integer> TempPassives = new HashMap<Integer, Integer>();
	
	public int getTempPassives(int one) {
		if(TempPassives.containsKey(one)){
		int two = TempPassives.get(one);
		//System.out.println("TempPassives: " +one+" - " +two);
		return two;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setTempPassives(int one, int passive) {
		TempPassives.put(Integer.valueOf(one), passive); 
		//System.out.println("TempPassives: " +one+" - " +passive);
	}
	
public Map<Integer, Integer> ExpireNotification = new HashMap<Integer, Integer>();
	
	public int getExpireNotification(int invslot) {
		if(ExpireNotification.containsKey(invslot)){
		int invvalue = ExpireNotification.get(invslot);
		//System.out.println("boothwindowPRICE: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setExpireNotification(int INVslot, int item_end_datez) {
		ExpireNotification.put(Integer.valueOf(INVslot), item_end_datez); 
		//System.out.println("item_end_date: " +INVslot+" - " +item_end_datez);
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
	
	
	public Map<Integer, Integer> skills = new HashMap<Integer, Integer>();
	public Map<Integer, String> friendslist = new HashMap<Integer, String>();
	public Map<Integer, String> ignorelist = new HashMap<Integer, String>();
	public Map<Integer, Integer> skillbar = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> TempStoreBuffs = new HashMap<Integer, Integer>();
	

	public Map<Integer, Integer> DotsIconID = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> DotsValue = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> DotsTime = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> DotsSLOT = new HashMap<Integer, Integer>();
	
	// 4-Way Set DOTS
	public void setDots(int DotsSLOTz, int DotsIconIDz, int DotsTimez, int DotsValuez){
		
		this.DotsIconID.put(DotsSLOTz, DotsIconIDz);
		this.DotsTime.put(DotsSLOTz, DotsTimez); 
		this.DotsValue.put(DotsSLOTz, DotsValuez); 
		this.DotsSLOT.put(DotsSLOTz, DotsSLOTz);
		
		//Custom functions for special buffs
		if(DotsIconIDz == 42){this.MANA_SHIELD_PROTECTION = 1;}
		else
		if(DotsIconIDz == 44){this.HIDING = 1;}// 2 other buffs are in going to be in SetTempStoreBuff
		else
		if(DotsIconIDz == 48){
			if(this.DotsIconID.containsKey(5) && this.DotsIconID.containsValue(47)){
				this.DotsSLOT.remove(5);	 
				this.DotsIconID.remove(5);	
				this.DotsTime.remove(5);
				this.DotsValue.remove(5);
				this.RemoveDot(this.getDotsIconID(47), this.getDotsSLOT(5));
			}
		}else
		if(DotsIconIDz == 58){
  		if(this.Summonid != 0){	
			Summon sum = wmap.getsummons(this.Summonid);
			}else{	
		    Summon sum = new Summon(this);
		}}
		else //DotIcon , DotValue -> Get value by icon!!
		{
			this.settempstore(DotsIconIDz, DotsValuez);// this will Handle the stats
		}
		//System.out.println("==>:"+this.getLOGsetName()+" setDots:"+DotsSLOTz+" - "+DotsIconIDz+" - "+DotsTimez+" - "+DotsValuez);
	}
	
	public int getDotsSLOT(int one) {
		if(DotsSLOT.containsKey(one)){
		int invvalue = DotsSLOT.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsSLOT(int one, int two) {
		DotsSLOT.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("POTSLOT " +one+" - " +two);
	}
	
	public int getDotsIconID(int one) {
		if(DotsIconID.containsKey(one)){
		int invvalue = DotsIconID.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsIconID(int one, int two) {
		DotsIconID.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotIconID " +one+" - " +two);
	}
	
	public int getDotsTime(int one) {
		if(DotsTime.containsKey(one)){
		int invvalue = DotsTime.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsTime(int one, int two) {
		DotsTime.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotTime " +one+" - " +two);
	}
	
	public int getDotsValue(int one) {
		if(DotsValue.containsKey(one)){
		int invvalue = DotsValue.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsValue(int one, int two) {
		DotsValue.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotValue" +one+" - " +two);
	}
	
	
	public ConcurrentMap<Integer, Integer> boothwindowSLOT = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> boothwindowX = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> boothwindowY = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> boothwindowSTACK = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Long> boothwindowPRICE = new ConcurrentHashMap<Integer, Long>();
	public ConcurrentMap<Integer, Integer> boothwindowinvtobooth = new ConcurrentHashMap<Integer, Integer>();
	
	
	public long getboothwindowPRICE(int invslot) {
		if(boothwindowPRICE.containsKey(invslot)){
		long invvalue = boothwindowPRICE.get(invslot);
		//System.out.println("boothwindowPRICE: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setboothwindowPRICE(int INVslot, long itemstack) {
		boothwindowPRICE.put(Integer.valueOf(INVslot), itemstack); 
	//System.out.println("boothwindowPRICE: " +INVslot+" - " +itemstack);
	}
	
		public int getboothwindowinvtobooth(int invslot) {
			if(boothwindowinvtobooth.containsKey(invslot)){
			int invvalue = boothwindowinvtobooth.get(invslot);
			//System.out.println("boothwindowinvtobooth: " +invslot+" - " +invvalue);
			return invvalue;}else
			{ //System.out.println(invslot+" - null "); 
			return 0;}
		}

		public void seboothwindowinvtobooth(int INVslot, int itemstack) {
		boothwindowinvtobooth.put(Integer.valueOf(INVslot), Integer.valueOf(itemstack)); 
		//System.out.println("boothwindowinvtobooth: " +INVslot+" - " +itemstack);
		}
	
	 // inv stack
	public int getboothwindowSTACK(int invslot) {
		if(boothwindowSTACK.containsKey(invslot)){
		int invvalue = boothwindowSTACK.get(invslot);
		//System.out.println("boothwindowSTACK: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setboothwindowSTACK(int INVslot, int itemstack) {
		boothwindowSTACK.put(Integer.valueOf(INVslot), Integer.valueOf(itemstack)); 
	//System.out.println("boothwindowSTACK: " +INVslot+" - " +itemstack);
	}	

 // inv slot
public int getboothwindowSLOT(int invslot) {
	if(boothwindowSLOT.containsKey(invslot)){
	int invvalue = boothwindowSLOT.get(invslot);
	//System.out.println("boothwindowSLOT: " +invslot+" - " +invvalue);
	return invvalue;}else
	{ //System.out.println(invslot+" - null "); 
	return 1337;}
}

public void setboothwindowSLOT(int INVslot, int itemid) {
	boothwindowSLOT.put(Integer.valueOf(INVslot), Integer.valueOf(itemid)); 
//System.out.println("boothwindowSLOT: " +INVslot+" - " +itemid);
}	

 // inv height
public int getboothwindowY(int invslot) {
	if(boothwindowY.containsKey(invslot)){
	int invvalue = boothwindowY.get(invslot);
	//System.out.println("boothwindowY: " +invslot+" - " +invvalue);
	return invvalue;}else
	{ //System.out.println(invslot+" - null "); 
	return 0;}
}

public void setboothwindowY(int INVslot, int invheight) {
	boothwindowY.put(Integer.valueOf(INVslot), Integer.valueOf(invheight)); 
//System.out.println("boothwindowY: " +INVslot+" - " +invheight);
}

// inv weight
public int getboothwindowX(int invslot) {
	if(boothwindowX.containsKey(invslot)){
		int invvalue = boothwindowX.get(invslot);
		//System.out.println("boothwindowX: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
}

public void setboothwindowX(int INVslot, int invweight) {
	boothwindowX.put(Integer.valueOf(INVslot), Integer.valueOf(invweight)); 
	//System.out.println("boothwindowX: " +INVslot+" - " +invweight);
}
	
	public Map<Integer, Integer> EquipSLOT = new HashMap<Integer, Integer>();// SLOT
/* 	decrypted 0 = [0 = remove item] [1 = put item from inventory]
	decrypted 1 = from inventory slot
	decrypted 2 = to item equipment slot
*/
   // equip slot
	public int getequipSLOT(int equipslot) {
		if(EquipSLOT.containsKey(equipslot)){
		int equipvalue = EquipSLOT.get(equipslot);
		////System.out.println("EquipSLOT: " +equipslot+" - " +equipvalue);
		return equipvalue;}else
		{ ////System.out.println(equipslot+" - null "); 
		return 0;}
	}
	public void setequipSLOT(int equipslot, int invslot) {
	EquipSLOT.put(Integer.valueOf(equipslot), Integer.valueOf(invslot)); 
	//System.out.println("EquipSLOT: " +equipslot+" - " +invslot);
	}	
	
	// cargostorage
	public Map<Integer, Integer> CargoSLOT = new HashMap<Integer, Integer>(); // SLOT
	public Map<Integer, Integer> CargoHEIGHT = new HashMap<Integer, Integer>(); // HEIGHT
	public Map<Integer, Integer> CargoWEIGHT = new HashMap<Integer, Integer>(); // WEIGHT
	public Map<Integer, Integer> CargoSTACK = new HashMap<Integer, Integer>();// ITEM STACK

	
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


public Map<Integer, Long> skillcooldowns = new HashMap<Integer, Long>();
	
	public long getskillcooldowns(int invslot) {
		if(skillcooldowns.containsKey(invslot)){
		long invvalue = skillcooldowns.get(invslot);
		//System.out.println("boothwindowPRICE: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setskillcooldowns(int INVslot, long skillcooldownsz) {
		skillcooldowns.put(Integer.valueOf(INVslot), skillcooldownsz); 
	//System.out.println("SETskillcooldowns: " +INVslot+" - " +skillcooldownsz);
	}
	
	
	// inventory
	public ConcurrentMap<Integer, Integer> InventorySLOT = new ConcurrentHashMap<Integer, Integer>(); // SLOT
	public ConcurrentMap<Integer, Integer> InventoryHEIGHT = new ConcurrentHashMap<Integer, Integer>(); // HEIGHT
	public ConcurrentMap<Integer, Integer> InventoryWEIGHT = new ConcurrentHashMap<Integer, Integer>(); // WEIGHT
	public ConcurrentMap<Integer, Integer> InventorySTACK = new ConcurrentHashMap<Integer, Integer>();// ITEM STACK
/*  decrypted 0 = [0 = remove item from equip] [1 = put item (& MOVE)]
    decrypted 1 = from item SLOT ( can be from anywhre like equip etc. )
	decrypted 2 = to inventory slot 
	decrypted 3 = to inventory hight
	decrypted 4 = to inventory wight
	decrypted 8-9 = inventory stack  ( 4 INT digits is max ) 
*/

	
/*  <=== PICK ===>
    decrypted 0 = HOW MANY ITEMS IN TOTAL in inventory
	decrypted 4 = to new inventory slot
	decrypted 5 = to inventory height
	decrypted 6 = to inventory weight
*/	
	
	
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


	public Map<Integer, Integer> PotSLOT = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> PotIconID = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> PotTime = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> PotValue = new HashMap<Integer, Integer>();
	
	
	public int getPotSLOT(int one) {
		if(PotSLOT.containsKey(one)){
		int invvalue = PotSLOT.get(one);
		return invvalue;}else{return 0;}
	}
	public void setPotSLOT(int one, int two) {
		PotSLOT.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("POTSLOT " +one+" - " +two);
	}
	
	public int getPotIconID(int one) {
		if(PotIconID.containsKey(one)){
		int invvalue = PotIconID.get(one);
		return invvalue;}else{return 0;}
	}
	public void setPotIconID(int one, int two) {
		PotIconID.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotIconID " +one+" - " +two);
	}
	
	public int getPotTime(int one) {
		if(PotTime.containsKey(one)){
		int invvalue = PotTime.get(one);
		return invvalue;}else{return 0;}
	}
	public void setPotTime(int one, int two) {
		PotTime.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotTime " +one+" - " +two);
	}
	
	public int getPotValue(int one) {
		if(PotValue.containsKey(one)){
		int invvalue = PotValue.get(one);
		return invvalue;}else{return 0;}
	}
	public void setPotValue(int one, int two) {
		PotValue.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotValue" +one+" - " +two);
	}
	
	
	public Map<Integer, Integer> mailid = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> mailCanceled = new HashMap<Integer, Integer>();
	
	public Map<Integer, Integer> charid_SENDER = new HashMap<Integer, Integer>();
	public Map<Integer, String>  name_SENDER = new HashMap<Integer, String>();
	public Map<Integer, Integer> charid_RECEIVER = new HashMap<Integer, Integer>();
	public Map<Integer, String>  name_RECEIVER = new HashMap<Integer, String>();
	public Map<Integer, Long> gold_REQUIRED = new HashMap<Integer, Long>();
	
	public Map<Integer, Integer> item1 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> stack1 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> item2 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> stack2 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> item3 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> stack3 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> item4 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> stack4 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> item5 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> stack5 = new HashMap<Integer, Integer>();

	
	public int getmailCanceled(int one) {
		if(mailCanceled.containsKey(one)){
		int invvalue = mailCanceled.get(one);
		return invvalue;}else{return 0;}
	}
	
	public int getmailid(int one) {
		if(mailid.containsKey(one)){
		int invvalue = mailid.get(one);
		return invvalue;}else{return 0;}
	}
	public int getcharid_SENDER(int one) {
		if(charid_SENDER.containsKey(one)){
		int invvalue = charid_SENDER.get(one);
		//System.out.println("charid_SENDER: "+invvalue);
		return invvalue;}else{return 0;}
	}
	public String getname_SENDER(int one) {
		if(name_SENDER.containsKey(one)){
		String invvalue = name_SENDER.get(one);
		//System.out.println("getname_SENDER: "+invvalue);
		return invvalue;}else{return "0";}
	}
	public int getcharid_RECEIVER(int one) {
		if(charid_RECEIVER.containsKey(one)){
		int invvalue = charid_RECEIVER.get(one);
		return invvalue;}else{return 0;}
	}
	public String getname_RECEIVER(int one) {
		if(name_RECEIVER.containsKey(one)){
		String invvalue = name_RECEIVER.get(one);
		return invvalue;}else{return "0";}
	}
	public long getgold_REQUIRED(int one) {
		if(gold_REQUIRED.containsKey(one)){
		long invvalue = gold_REQUIRED.get(one);
		return invvalue;}else{return 0;}
	}
	public int getitem1(int one) {
		if(item1.containsKey(one)){
		int invvalue = item1.get(one);
		return invvalue;}else{return 0;}
	}
	public int getstack1(int one) {
		if(stack1.containsKey(one)){
		int invvalue = stack1.get(one);
		return invvalue;}else{return 0;}
	}
	public int getitem2(int one) {
		if(item2.containsKey(one)){
		int invvalue = item2.get(one);
		return invvalue;}else{return 0;}
	}
	public int getstack2(int one) {
		if(stack2.containsKey(one)){
		int invvalue = stack2.get(one);
		return invvalue;}else{return 0;}
	}
	public int getitem3(int one) {
		if(item3.containsKey(one)){
		int invvalue = item3.get(one);
		return invvalue;}else{return 0;}
	}
	public int getstack3(int one) {
		if(stack3.containsKey(one)){
		int invvalue = stack3.get(one);
		return invvalue;}else{return 0;}
	}
	public int getitem4(int one) {
		if(item4.containsKey(one)){
		int invvalue = item4.get(one);
		return invvalue;}else{return 0;}
	}
	public int getstack4(int one) {
		if(stack4.containsKey(one)){
		int invvalue = stack4.get(one);
		return invvalue;}else{return 0;}
	}
	public int getitem5(int one) {
		if(item5.containsKey(one)){
		int invvalue = item5.get(one);
		return invvalue;}else{return 0;}
	}
	public int getstack5(int one) {
		if(stack5.containsKey(one)){
		int invvalue = stack5.get(one);
		return invvalue;}else{return 0;}
	}
	
	//UNUSED
	/*public void CheckMail(){
		this.mailid.clear();
		try{
			ResultSet rs = Queries.getmails(SQLconnection.getInstance().getConnection(), this.getCharID()).executeQuery();
			int inc = 0;
			while(rs.next()){
			inc++;	
			this.mailid.put(inc,rs.getInt("mailid"));
			}
			inc = 0;
		}
	 catch (Exception e) {
	 System.out.println(e.getMessage());
	}
		if(!mailid.isEmpty()){
			byte[] chid = BitTools.intToByteArray(this.getCharID());
			byte[] mairuru = new byte[16];
			mairuru[0] = (byte)0x10;
			mairuru[4] = (byte)0x05;
			mairuru[6] = (byte)0x79;
			for(int a=0;a<4;a++) {
				mairuru[8+a] = chid[a];
			}
			ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), mairuru); 
			//System.out.println("GG");
		}
	}*/
	
	public void refreshmailbox(){
		this.mailid.clear();
		this.mailCanceled.clear();
		this.charid_SENDER.clear();
		this.name_SENDER.clear();
		this.charid_RECEIVER.clear();
		this.name_RECEIVER.clear();
		this.gold_REQUIRED.clear();
		this.item1.clear();
		this.stack1.clear();
		this.item2.clear();
		this.stack2.clear();
		this.item3.clear();
		this.stack3.clear();
		this.item4.clear();
		this.stack4.clear();
		this.item5.clear();
		this.stack5.clear();
		
		try{
			// SOMEHOW IT DISPLAYS THE LAST MAIL IN EVERY MAIL 
			int inc = 0;
			
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getmailbysender(SQLconnection.getInstance().getaConnection(), this.getCharID()));
			while(rs.next()){
			if(rs.getInt("item1") != 273000229 && rs.getInt("canceled") != 1){
			inc++;
			this.mailid.put(inc,rs.getInt("mailid"));
			this.mailCanceled.put(rs.getInt("mailid"),rs.getInt("canceled"));
			
			this.charid_SENDER.put(rs.getInt("mailid"),rs.getInt("charid_SENDER"));
			this.name_SENDER.put(rs.getInt("mailid"),rs.getString("name_SENDER"));
			this.charid_RECEIVER.put(rs.getInt("mailid"),rs.getInt("charid_RECEIVER"));
			this.name_RECEIVER.put(rs.getInt("mailid"),rs.getString("name_RECEIVER"));
			this.gold_REQUIRED.put(rs.getInt("mailid"),rs.getLong("gold_REQUIRED"));
			
			this.item1.put(rs.getInt("mailid"),rs.getInt("item1"));
			this.stack1.put(rs.getInt("mailid"),rs.getInt("stack1"));
			this.item2.put(rs.getInt("mailid"),rs.getInt("item2"));
			this.stack2.put(rs.getInt("mailid"),rs.getInt("stack2"));
			this.item3.put(rs.getInt("mailid"),rs.getInt("item3"));
			this.stack3.put(rs.getInt("mailid"),rs.getInt("stack3"));
			this.item4.put(rs.getInt("mailid"),rs.getInt("item4"));
			this.stack4.put(rs.getInt("mailid"),rs.getInt("stack4"));
			this.item5.put(rs.getInt("mailid"),rs.getInt("item5"));
			this.stack5.put(rs.getInt("mailid"),rs.getInt("stack5"));
			}
			}
			
			ResultSet rs0 = SQLconnection.getInstance().executeQuery(Queries.getmailbyreceiver(SQLconnection.getInstance().getaConnection(), this.getCharID()));
			while(rs0.next()){
			inc++;
			this.mailid.put(inc,rs0.getInt("mailid"));
			this.mailCanceled.put(rs0.getInt("mailid"),rs0.getInt("canceled"));
			
			this.charid_SENDER.put(rs0.getInt("mailid"),rs0.getInt("charid_SENDER"));
			this.name_SENDER.put(rs0.getInt("mailid"),rs0.getString("name_SENDER"));
			this.charid_RECEIVER.put(rs0.getInt("mailid"),rs0.getInt("charid_RECEIVER"));
			this.name_RECEIVER.put(rs0.getInt("mailid"),rs0.getString("name_RECEIVER"));
			this.gold_REQUIRED.put(rs0.getInt("mailid"),rs0.getLong("gold_REQUIRED"));
			
			this.item1.put(rs0.getInt("mailid"),rs0.getInt("item1"));
			this.stack1.put(rs0.getInt("mailid"),rs0.getInt("stack1"));
			this.item2.put(rs0.getInt("mailid"),rs0.getInt("item2"));
			this.stack2.put(rs0.getInt("mailid"),rs0.getInt("stack2"));
			this.item3.put(rs0.getInt("mailid"),rs0.getInt("item3"));
			this.stack3.put(rs0.getInt("mailid"),rs0.getInt("stack3"));
			this.item4.put(rs0.getInt("mailid"),rs0.getInt("item4"));
			this.stack4.put(rs0.getInt("mailid"),rs0.getInt("stack4"));
			this.item5.put(rs0.getInt("mailid"),rs0.getInt("item5"));
			this.stack5.put(rs0.getInt("mailid"),rs0.getInt("stack5"));
			}
				
		}
	 catch (Exception e) {
	 System.out.println(e.getMessage());
	}
	
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		
		byte[] Mailid1 = BitTools.intToByteArray(this.getmailid(1));
	    byte[] Mailid2 = BitTools.intToByteArray(this.getmailid(2));
		byte[] Mailid3 = BitTools.intToByteArray(this.getmailid(3));
		byte[] Mailid4 = BitTools.intToByteArray(this.getmailid(4));
		byte[] Mailid5 = BitTools.intToByteArray(this.getmailid(5));
		byte[] Mailid6 = BitTools.intToByteArray(this.getmailid(6));
		byte[] Mailid7 = BitTools.intToByteArray(this.getmailid(7));
		byte[] Mailid8 = BitTools.intToByteArray(this.getmailid(8));
		byte[] Tplayer1 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(1)));
	    byte[] Tplayer2 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(2)));
		byte[] Tplayer3 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(3)));
		byte[] Tplayer4 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(4)));
		byte[] Tplayer5 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(5)));
		byte[] Tplayer6 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(6)));
		byte[] Tplayer7 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(7)));
		byte[] Tplayer8 = BitTools.intToByteArray(this.getcharid_SENDER(this.getmailid(8)));
		byte[] Rplayer1 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(1)));
	    byte[] Rplayer2 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(2)));
		byte[] Rplayer3 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(3)));
		byte[] Rplayer4 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(4)));
		byte[] Rplayer5 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(5)));
		byte[] Rplayer6 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(6)));
		byte[] Rplayer7 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(7)));
		byte[] Rplayer8 = BitTools.intToByteArray(this.getcharid_RECEIVER(this.getmailid(8)));
		byte[] Gold1 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(1)));
	    byte[] Gold2 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(2)));
		byte[] Gold3 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(3)));
		byte[] Gold4 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(4)));
		byte[] Gold5 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(5)));
		byte[] Gold6 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(6)));
		byte[] Gold7 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(7)));
		byte[] Gold8 = BitTools.LongToByteArrayREVERSE(this.getgold_REQUIRED(this.getmailid(8)));
		
		byte[] name1 = this.getname_SENDER(this.getmailid(1)).getBytes();
		byte[] name2 = this.getname_SENDER(this.getmailid(2)).getBytes();
		byte[] name3 = this.getname_SENDER(this.getmailid(3)).getBytes();
		byte[] name4 = this.getname_SENDER(this.getmailid(4)).getBytes();
		byte[] name5 = this.getname_SENDER(this.getmailid(5)).getBytes();
		byte[] name6 = this.getname_SENDER(this.getmailid(6)).getBytes();
		byte[] name7 = this.getname_SENDER(this.getmailid(7)).getBytes();
		byte[] name8 = this.getname_SENDER(this.getmailid(8)).getBytes();
		
		byte[] myname1 = this.getname_RECEIVER(this.getmailid(1)).getBytes();
		byte[] myname2 = this.getname_RECEIVER(this.getmailid(2)).getBytes();
		byte[] myname3 = this.getname_RECEIVER(this.getmailid(3)).getBytes();
		byte[] myname4 = this.getname_RECEIVER(this.getmailid(4)).getBytes();
		byte[] myname5 = this.getname_RECEIVER(this.getmailid(5)).getBytes();
		byte[] myname6 = this.getname_RECEIVER(this.getmailid(6)).getBytes();
		byte[] myname7 = this.getname_RECEIVER(this.getmailid(7)).getBytes();
		byte[] myname8 = this.getname_RECEIVER(this.getmailid(8)).getBytes();
		
		byte[] item1_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(1)));
		byte[] stack1_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(1)));
		byte[] item1_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(1)));
		byte[] stack1_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(1)));
		byte[] item1_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(1)));
		byte[] stack1_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(1)));
		byte[] item1_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(1)));
		byte[] stack1_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(1)));
		byte[] item1_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(1)));
		byte[] stack1_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(1)));
		
		byte[] item2_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(2)));
		byte[] stack2_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(2)));
		byte[] item2_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(2)));
		byte[] stack2_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(2)));
		byte[] item2_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(2)));
		byte[] stack2_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(2)));
		byte[] item2_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(2)));
		byte[] stack2_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(2)));
		byte[] item2_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(2)));
		byte[] stack2_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(2)));
		
		byte[] item3_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(3)));
		byte[] stack3_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(3)));
		byte[] item3_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(3)));
		byte[] stack3_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(3)));
		byte[] item3_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(3)));
		byte[] stack3_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(3)));
		byte[] item3_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(3)));
		byte[] stack3_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(3)));
		byte[] item3_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(3)));
		byte[] stack3_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(3)));
		
		byte[] item4_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(4)));
		byte[] stack4_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(4)));
		byte[] item4_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(4)));
		byte[] stack4_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(4)));
		byte[] item4_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(4)));
		byte[] stack4_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(4)));
		byte[] item4_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(4)));
		byte[] stack4_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(4)));
		byte[] item4_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(4)));
		byte[] stack4_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(4)));
		
		byte[] item5_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(5)));
		byte[] stack5_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(5)));
		byte[] item5_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(5)));
		byte[] stack5_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(5)));
		byte[] item5_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(5)));
		byte[] stack5_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(5)));
		byte[] item5_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(5)));
		byte[] stack5_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(5)));
		byte[] item5_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(5)));
		byte[] stack5_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(5)));
		
		byte[] item6_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(6)));
		byte[] stack6_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(6)));
		byte[] item6_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(6)));
		byte[] stack6_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(6)));
		byte[] item6_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(6)));
		byte[] stack6_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(6)));
		byte[] item6_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(6)));
		byte[] stack6_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(6)));
		byte[] item6_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(6)));
		byte[] stack6_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(6)));
		
		byte[] item7_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(7)));
		byte[] stack7_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(7)));
		byte[] item7_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(7)));
		byte[] stack7_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(7)));
		byte[] item7_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(7)));
		byte[] stack7_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(7)));
		byte[] item7_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(7)));
		byte[] stack7_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(7)));
		byte[] item7_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(7)));
		byte[] stack7_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(7)));
		
		byte[] item8_1 = BitTools.intToByteArray(this.getitem1(this.getmailid(8)));
		byte[] stack8_1 = BitTools.intToByteArray(this.getstack1(this.getmailid(8)));
		byte[] item8_2 = BitTools.intToByteArray(this.getitem2(this.getmailid(8)));
		byte[] stack8_2 = BitTools.intToByteArray(this.getstack2(this.getmailid(8)));
		byte[] item8_3 = BitTools.intToByteArray(this.getitem3(this.getmailid(8)));
		byte[] stack8_3 = BitTools.intToByteArray(this.getstack3(this.getmailid(8)));
		byte[] item8_4 = BitTools.intToByteArray(this.getitem4(this.getmailid(8)));
		byte[] stack8_4 = BitTools.intToByteArray(this.getstack4(this.getmailid(8)));
		byte[] item8_5 = BitTools.intToByteArray(this.getitem5(this.getmailid(8)));
		byte[] stack8_5 = BitTools.intToByteArray(this.getstack5(this.getmailid(8)));
		
		
		byte[] fury = new byte[940];
		fury[0] = (byte)0xac;
		fury[1] = (byte)0x03;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x47;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
			
			fury[396+i] = Gold1[i];
			fury[404+i] = Gold2[i];
			fury[412+i] = Gold3[i];
			fury[420+i] = Gold4[i];
			fury[428+i] = Gold5[i];
			fury[436+i] = Gold6[i];
			fury[444+i] = Gold7[i];
			fury[452+i] = Gold8[i];
			
			fury[20+i] = Mailid1[i];
			fury[24+i] = Mailid2[i];
			fury[28+i] = Mailid3[i];
			fury[32+i] = Mailid4[i];
			fury[36+i] = Mailid5[i];
			fury[40+i] = Mailid6[i];
			fury[44+i] = Mailid7[i];
			fury[48+i] = Mailid8[i];
			
			fury[60+i] = Tplayer1[i];
		    fury[64+i] = Tplayer2[i];
			fury[68+i] = Tplayer3[i];
			fury[72+i] = Tplayer4[i];
			fury[76+i] = Tplayer5[i];
			fury[80+i] = Tplayer6[i];
			fury[82+i] = Tplayer7[i];
			fury[86+i] = Tplayer8[i];
			
			fury[228+i] = Rplayer1[i];
			fury[232+i] = Rplayer2[i];
			fury[236+i] = Rplayer3[i];
			fury[240+i] = Rplayer4[i];
			fury[244+i] = Rplayer5[i];
			fury[248+i] = Rplayer6[i];
			fury[252+i] = Rplayer7[i];
			fury[256+i] = Rplayer8[i];
		}
		fury[16] = (byte)0x01;

		
		for(int i=0;i<name1.length;i++) {
			fury[92+i] = name1[i];
		}
		for(int i=0;i<name2.length;i++) {
			fury[109+i] = name2[i];
		}
		for(int i=0;i<name3.length;i++) {
			fury[126+i] = name3[i];
		}
		for(int i=0;i<name4.length;i++) {
			fury[143+i] = name4[i];
		}
		for(int i=0;i<name5.length;i++) {
			fury[160+i] = name5[i];
		}
		for(int i=0;i<name6.length;i++) {
			fury[177+i] = name6[i];
		}
		for(int i=0;i<name7.length;i++) {
			fury[194+i] = name7[i];
		}
		for(int i=0;i<name8.length;i++) {
			fury[211+i] = name8[i];
		}
		
		
		for(int i=0;i<myname1.length;i++) {
			fury[260+i] = myname1[i];
		}
		for(int i=0;i<myname2.length;i++) {
			fury[277+i] = myname2[i];
		}
		for(int i=0;i<myname3.length;i++) {
			fury[294+i] = myname3[i];
		}
		for(int i=0;i<myname4.length;i++) {
			fury[311+i] = myname4[i];
		}
		for(int i=0;i<myname5.length;i++) {
			fury[328+i] = myname5[i];
		}
		for(int i=0;i<myname6.length;i++) {
			fury[345+i] = myname6[i];
		}
		for(int i=0;i<myname7.length;i++) {
			fury[362+i] = myname7[i];
		}
		for(int i=0;i<myname8.length;i++) {
			fury[379+i] = myname8[i];
		}
			
		
		// items
		for(int i=0;i<4;i++) {
			fury[464+i] = item1_1[i];
			fury[476+i] = item1_2[i];
			fury[488+i] = item1_3[i];
			fury[500+i] = item1_4[i];
			fury[512+i] = item1_5[i];
			
			fury[524+i] = item2_1[i];
			fury[536+i] = item2_2[i];
			fury[548+i] = item2_3[i];
			fury[560+i] = item2_4[i];
			fury[572+i] = item2_5[i];
			
			fury[584+i] = item3_1[i];
			fury[596+i] = item3_2[i];
			fury[608+i] = item3_3[i];
			fury[620+i] = item3_4[i];
			fury[632+i] = item3_5[i];
			
			fury[644+i] = item4_1[i];
			fury[656+i] = item4_2[i];
			fury[668+i] = item4_3[i];
			fury[680+i] = item4_4[i];
			fury[692+i] = item4_5[i];
			
			fury[704+i] = item5_1[i];
			fury[716+i] = item5_2[i];
			fury[728+i] = item5_3[i];
			fury[740+i] = item5_4[i];
			fury[752+i] = item5_5[i];
			
			fury[764+i] = item6_1[i];
			fury[776+i] = item6_2[i];
			fury[788+i] = item6_3[i];
			fury[800+i] = item6_4[i];
			fury[812+i] = item6_5[i];
			
			fury[824+i] = item7_1[i];
			fury[836+i] = item7_2[i];
			fury[848+i] = item7_3[i];
			fury[860+i] = item7_4[i];
			fury[872+i] = item7_5[i];
			
			fury[884+i] = item8_1[i];
			fury[896+i] = item8_2[i];
			fury[908+i] = item8_3[i];
			fury[920+i] = item8_4[i];
			fury[932+i] = item8_5[i];
		}
		// stack
		for(int i=0;i<2;i++) {
			fury[468+i] = stack1_1[i];
			fury[480+i] = stack1_2[i];
			fury[492+i] = stack1_3[i];
			fury[504+i] = stack1_4[i];
			fury[516+i] = stack1_5[i];
			
			fury[528+i] = stack2_1[i];
			fury[540+i] = stack2_2[i];
			fury[552+i] = stack2_3[i];
			fury[564+i] = stack2_4[i];
			fury[576+i] = stack2_5[i];
			
			fury[588+i] = stack3_1[i];
			fury[600+i] = stack3_2[i];
			fury[612+i] = stack3_3[i];
			fury[624+i] = stack3_4[i];
			fury[636+i] = stack3_5[i];
			
			fury[648+i] = stack4_1[i];
			fury[660+i] = stack4_2[i];
			fury[672+i] = stack4_3[i];
			fury[684+i] = stack4_4[i];
			fury[696+i] = stack4_5[i];
			
			fury[708+i] = stack5_1[i];
			fury[720+i] = stack5_2[i];
			fury[732+i] = stack5_3[i];
			fury[744+i] = stack5_4[i];
			fury[756+i] = stack5_5[i];
			
			fury[768+i] = stack6_1[i];
			fury[780+i] = stack6_2[i];
			fury[792+i] = stack6_3[i];
			fury[804+i] = stack6_4[i];
			fury[816+i] = stack6_5[i];
			
			fury[828+i] = stack7_1[i];
			fury[840+i] = stack7_2[i];
			fury[852+i] = stack7_3[i];
			fury[864+i] = stack7_4[i];
			fury[876+i] = stack7_5[i];
			
			fury[888+i] = stack8_1[i];
			fury[900+i] = stack8_2[i];
			fury[912+i] = stack8_3[i];
			fury[924+i] = stack8_4[i];
			fury[936+i] = stack8_5[i];	
			ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), fury); 
		}
	}
	
	public void SetHpManaStam(int newhp, int newmana, int newstam){
		if(this != null && this.getHp() > 0){
			byte[] chid = BitTools.intToByteArray(this.getCharID());
			byte[] healpckt = new byte[32];
			healpckt[0] = (byte)healpckt.length;
			healpckt[4] = (byte)0x05;
			healpckt[6] = (byte)0x35;
			healpckt[8] = (byte)0x08; 
			healpckt[9] = (byte)0x60; 
			healpckt[10] = (byte)0x22;
			healpckt[11] = (byte)0x45;
			for(int i=0;i<4;i++) {
				healpckt[12+i] = chid[i]; 
			}
		
			if(newhp <= 0){this.killedbyplayer = true;}
			
			int setcurrenthp = newhp;
			int setcurrentmana = newmana;
			int setcurrentstamina = newstam;
			this.setHp(setcurrenthp);
			this.setMana(setcurrentmana);
			this.setStamina(setcurrentstamina);	
			
			byte[] hp = BitTools.shortToByteArray((short)setcurrenthp);
			byte[] mana = BitTools.shortToByteArray((short)setcurrentmana);
			byte[] stam = BitTools.shortToByteArray((short)setcurrentstamina);
			
			healpckt[24] = hp[0];
			healpckt[25] = hp[1];
			healpckt[28] = mana[0];
			healpckt[29] = mana[1];
			healpckt[30] = stam[0];
			healpckt[31] = stam[1];			
			healpckt[16] = (byte)0x03;
			healpckt[18] = (byte)0x02;
		    ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), healpckt);
			}
	}
	
	//pvp1
	public void vpz(byte[] chid, boolean go){
		byte[] fury = new byte[32];
		fury[0] = (byte)fury.length;
		fury[4] = (byte)0x05;
		fury[6] = (byte)0x43;
		fury[8] = (byte)0x01;
		if(go == true){this.vendingpoint = this.vendingpoint + 1;}
		byte[] vp = BitTools.intToByteArray(this.vendingpoint);
		  byte[] fame = BitTools.intToByteArray(this.fame);   
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
			fury[21+i] = vp[i]; 
			fury[28+i] = fame[i]; 
		}
		fury[16] = (byte)0x00;
		fury[20] = (byte)this.fametitle;
		
	    ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), fury); 		
		//System.out.println("added vp, total :"+This.vendingpoint); 
	}
	
	public void flagred(){
	byte[] TPchid = BitTools.intToByteArray(this.getCharID());	
	byte[] fury1 = new byte[64];
	fury1[0] = (byte)0x40;
	fury1[4] = (byte)0x04;
	fury1[6] = (byte)0x2a;
	fury1[8] = (byte)0x01;
	for(int i=0;i<4;i++) {
	fury1[12+i] = TPchid[i];
	}
	fury1[16] = (byte)0x01;
	fury1[18] = (byte)0x03; 
	fury1[19] = (byte)0x08;
	fury1[24] = (byte)0x01;
	fury1[36] = (byte)0x01;

	byte[] TPxCoord = BitTools.floatToByteArray(this.getlastknownX());
	byte[] TPyCoord = BitTools.floatToByteArray(this.getlastknownY());
	
	byte[] TOPX = BitTools.floatToByteArray(-1779);
	byte[] TOPY = BitTools.floatToByteArray(2336);
	byte[] BOTX = BitTools.floatToByteArray(-1337);
	byte[] BOTY = BitTools.floatToByteArray(2772);
	

	
	for(int i=0;i<4;i++) {
		fury1[28+i] = TPxCoord[i];
		fury1[32+i] = TPyCoord[i];
		
	    fury1[40+i] = TOPX[i];
		fury1[44+i] = TOPY[i];
		fury1[48+i] = BOTX[i];
		fury1[52+i] = BOTY[i];
	}
	
	for(int i=0;i<4;i++) {
		fury1[20+i] = TPchid[i]; 
		fury1[24+i] = TPchid[i]; 
		fury1[56+i] = TPchid[i]; 
		fury1[60+i] = TPchid[i];
	}
	this.Duel = 1;
	this.sendToMap(fury1);
	}
	
	public void duelover(){
		byte[] TPchid = BitTools.intToByteArray(this.getCharID());	
		byte[] myname = "Logged out!".getBytes();
		byte[] hisname = this.getLOGsetName().getBytes();

		byte[] fury1 = new byte[68];
		fury1[0] = (byte)0x44;
		fury1[4] = (byte)0x04;
		fury1[6] = (byte)0x2b;
		fury1[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		fury1[12+i] = TPchid[i];
		fury1[20+i] = TPchid[i];
		fury1[44+i] = TPchid[i];
		}
		fury1[17] = (byte)0xfa;
		fury1[18] = (byte)0x60; 
		fury1[19] = (byte)0x2a;
		
		for(int i=0;i<myname.length;i++) {//me
			fury1[24+i] = myname[i];
		}
		for(int i=0;i<hisname.length;i++) {//him
			fury1[48+i] = hisname[i];
		}
		fury1[65] = (byte)0x98;
		fury1[66] = (byte)0x0f; 
		fury1[67] = (byte)0xbf;	
		this.sendToMap(fury1);
		}
	
	public void RemoveBudoong(int seqway){
		byte[] skils = BitTools.intToByteArray(seqway); 
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		 byte[] buff1 = new byte[28];
		   buff1[0] = (byte)0x1c; 
		   buff1[4] = (byte)0x05;
		   buff1[6] = (byte)0x34;
		   buff1[8] = (byte)0x01;
		   for(int i=0;i<4;i++) {
			  buff1[12+i] = chid[i];	
			  buff1[20+i] = skils[i];
		   }
		   buff1[17] = (byte)0xff;
		   this.Budoong = 0;
		   this.BudoongSkill = 0;
		   this.sendToMap(buff1);
		 ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), buff1); 	
	}
	
	public void RemoveDot(int DotsIconID, int DotsSLOT){//DETERMINE: 1 = player | 2 = mob	
		if(this != null){
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		byte[] buff = new byte[44];
		 buff[0] = (byte)0x2c; 
		 buff[4] = (byte)0x05;
		 buff[6] = (byte)0x1f;
		 buff[8] = (byte)0x01;// 1 = player | 2 = mob
		 for(int i=0;i<4;i++){
		 buff[12+i] = chid[i]; 				
		 }	
		 buff[26] = (byte)0x01; 
		 buff[28] = (byte)0x89; 
		 buff[32] = (byte)0x89; 
		 buff[36] = (byte)0x7e; 
		 buff[38] = (byte)0x7e; 
		 buff[40] = (byte)0x60; 
		 buff[42] = (byte)0x60;	 
		 
		 byte[] buffidz1 = BitTools.intToByteArray(0); 
		 byte[] bufftimez1 = BitTools.intToByteArray(0);
		 byte[] buffvaluez1 = BitTools.intToByteArray(0);
		 byte[] buffslotz1 = BitTools.intToByteArray(DotsSLOT);
		 
			 for(int i=0;i<2;i++) {
				 buff[i+16] = buffslotz1[i];	 // buffslot
				 buff[i+20] = buffidz1[i];	 // buff id
				 buff[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
				 buff[i+24] = buffvaluez1[i]; // Value XXXXX
			 }	
		 ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), buff); 
		 if(DotsIconID == 43||DotsIconID == 46||DotsIconID == 49||DotsIconID == 44||DotsIconID == 45||DotsIconID == 47||DotsIconID == 48){this.sendToMap(buff);}
	}}
	
	public void refreshvendinglist(){
		//System.out.println("Size : "+WMap.getInstance().vendinglist.size()+" ....... Me : "+this.getLOGsetName());
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		
		//calc how many vendings there are
		int calc = 14;
		for(int i=0;i<WMap.getInstance().vendinglist.size();i++) {
		calc = calc + 36;
		}
		
		byte[] fury = new byte[calc];
		byte[] Tbone = BitTools.intToByteArray(calc);
		for(int i=0;i<2;i++) {
		fury[i] = Tbone[i]; 
		}
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x4a;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[9+i] = chid[i]; 
		}
		fury[13] = (byte)0x01;
		
		int inc = 14;
		if(!WMap.getInstance().vendinglist.isEmpty()){
		Iterator<Map.Entry<Integer, Integer>> iter = WMap.getInstance().vendinglist.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<Integer, Integer> pairs = iter.next();
			int i = pairs.getKey();
		if(WMap.getInstance().CharacterExists(WMap.getInstance().getvendinglist(i))){
			Character Tplayer = WMap.getInstance().getCharacter(WMap.getInstance().getvendinglist(i));
			if(Tplayer.BoothStance == 1){
			fury[inc-1] = (byte)0x08;
			byte[] Tchid = BitTools.intToByteArray(Tplayer.getCharID());
			for(int a=0;a<4;a++) {
				fury[inc+a] = Tchid[a];
			}
			byte[] Tname = Tplayer.BoothName.getBytes();
			for(int a=0;a<Tname.length;a++) {
				fury[inc+4+a] = Tname[a];
			}
		inc = inc + 36;
		}}}}
		
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), fury);
	}
	
	private int Proffone = 0, Profftwo = 0, Proffthree = 0;

	public void Proffession(byte[] decrypted){
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println();
		
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		byte[] extfury = new byte[28];
		extfury[0] = (byte)0x1c;
		extfury[4] = (byte)0x05;
		extfury[6] = (byte)0x4f;
		extfury[8] = (byte)0x01;
		extfury[9] = (byte)0x07;
		
		extfury[17] = (byte)0x43;
		extfury[18] = (byte)0x45;
		extfury[19] = (byte)0x28;
		
		extfury[24] = (byte)0x65;
		
		byte[] fury = new byte[52];
		fury[0] = (byte)0x34;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x4f;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		extfury[12+i] = chid[i]; // charid
		fury[12+i] = chid[i];    // charid
		}
		fury[16] = (byte)0x01;
		fury[18] = decrypted[0];// 0 = starting herb & -1 on value of the TOOL | 1 = clicking the red button | 2 = retrieving item or nothing| 3 is STO

		
		// sometimes the player gets exp 00.01%??
		// decrypted[0] == 0&2 then decrypted[10] = SLOT ,decrypted[11] = Inv Y, decrypted[12] = Inv X
		// decrypted[0] == 1 then decrypted[10],decrypted[11], decrypted[12] = Red button
		
		
		
		// Starting proff
		if((int)decrypted[0] == 0){
			fury[19] = (byte)0xbf;
			fury[20] = (byte)0x06; // determines finshing or mining or herbibg etc
			
			fury[36] = decrypted[8]; //0x00 = receiving item | ff = for griding 
			fury[37] = (byte)0x52;
			fury[38] = (byte)0x09;
			fury[39] = (byte)0x08;
			
			fury[41] = (byte)0xfe; // decrypted[0] == 1 is indicate of that mining/herb/fishing duriontation | decrypted[0] == 2 is new slot ID?
			
			fury[42] = (byte)this.getInventoryHEIGHT((int)decrypted[8]); //inv y = WRONG
			fury[43] = (byte)this.getInventoryWEIGHT((int)decrypted[8]); //inv x = WRONG 
			
			byte[] Proffitemid = BitTools.intToByteArray(this.getInventorySLOT((int)decrypted[8]));
			byte[] Duriotation = BitTools.intToByteArray(2000);
			
			for(int i=0;i<4;i++) {
				extfury[20+i] = Proffitemid[i]; 
				fury[44+i] = Proffitemid[i];  //item id
				fury[48+i] = Duriotation[i];  // decrypted[0] == 0 is mining/herb/fishing duriontation | decrypted[0] == 2 is totalnew itemstack
			}
		
			this.sendToMap(extfury);
			this.Proffone = 0;
			this.Profftwo = 0;
		    this.Proffthree = 0;
		    if(this.proffpool == false){
		    this.proffDynamiccd = 12000;
			this.proffcd = System.currentTimeMillis();
			}
		}
		
		
		// Red buttons
		if((int)decrypted[0] == 1){
			//make a randomizer so that we can proc| 0x01 = +1 pickaxe, 0x02 = x2 baloon, 0x03 = 100% item ,0x04 = ? ,0x05 = ? , 0x06 = ?
			int one = decrypted[10];
			int two = decrypted[11];
			int three = decrypted[12];
			
			// random between 1-3 ( maybe even add NULLS )
			 Random r = new Random();
			 int a = 1+r.nextInt(3); // 1/3th chance when click red button
			 if(a == 1 && one != 0){this.Proffone = 1;}
			 if(a == 2 && one != 0){this.Proffone = 2;}
			 if(a == 3 && one != 0){this.Proffone = 3;}
			 //if(a == 4 && one != 0){this.Proffone = 4;}
			 //if(a == 5 && one != 0){this.Proffone = 5;}
			 //if(a == 6 && one != 0){this.Proffone = 6;}
			 
			 if(a == 1 && two != 0){this.Profftwo = 1;}
			 if(a == 2 && two != 0){this.Profftwo = 2;}
			 if(a == 3 && two != 0){this.Profftwo = 3;}
			// if(a == 4 && two != 0){this.Profftwo = 4;}
			 //if(a == 5 && two != 0){this.Profftwo = 5;}
			 //if(a == 6 && two != 0){this.Profftwo = 6;}
			 
			 if(a == 1 && three != 0){this.Proffthree = 1;}
			 if(a == 2 && three != 0){this.Proffthree = 2;}
			 if(a == 3 && three != 0){this.Proffthree = 3;}
			 //if(a == 4 && three != 0){this.Proffthree = 4;}
			 //if(a == 5 && three != 0){this.Proffthree = 5;}
			 //if(a == 6 && three != 0){this.Proffthree = 6;}
			
			fury[20] = (byte)this.Proffone;
			fury[24] = (byte)this.Profftwo;
			fury[28] = (byte)this.Proffthree;
		}
		
		//long lol = System.currentTimeMillis() - this.proffcd;
		//System.out.println("OUT: "+ System.currentTimeMillis()+" - "+this.proffcd+" = "+lol); 
		
		// Retrieve item 12000 / 14500 
		if((int)decrypted[0] == 2){	
			if(System.currentTimeMillis() - this.proffcd >= this.proffDynamiccd){
			//System.out.println("IN: "+ System.currentTimeMillis()+" - "+this.proffcd+" = "+lol); 	
			 Random r = new Random();
			 int a;
			 
			 if(this.farmmodule == 18){//mining
			 a = 1+r.nextInt(80); // 1-20 = 30in3hors. 1-40 = 30in6hours. 1-60 = 30in9hours 1-80 = 30in12hours( = 2-3 per hour)
			 //System.out.println("Mining : "+a+" / "+80);
			 }else if(this.farmmodule == 19){//herbing
			 a = 1+r.nextInt(40); 
			 //System.out.println("Herbing : "+a+" / "+40);
			 }else{//rest ( graveyard & fishing )
			 a = 1+r.nextInt(40);  
			 //System.out.println("Rest : "+a+" / "+20);
			 }
			 int newitem = 0;
			 
			 // mining
			 if(this.farmmodule == 18){newitem = Proffession.getfarmmining(a);}
			 // herb
			 if(this.farmmodule == 19){newitem = Proffession.getfarmherbing(a);}
			 // fishing
			 if(this.farmmodule == 20){newitem = Proffession.getfarmfishing(a);}
			 // graveyard
			 if(this.farmmodule == 21){newitem = Proffession.getfarmgraveyard(a);}
			 
			//check if red buttons are the same 
			int stack = 1;
			if(this.Proffone == this.Profftwo && this.Proffone == this.Proffthree && this.Proffone != 0 && this.Profftwo != 0 && this.Proffthree != 0){
			if(this.Proffone == 1 && this.Profftwo == 1 && this.Proffthree == 1){stack = stack + 1;}	
			if(this.Proffone == 2 && this.Profftwo == 2 && this.Proffthree == 2){stack = stack * 2;}	
			if(this.Proffone == 3 && this.Profftwo == 3 && this.Proffthree == 3){}	
			
			if(newitem == 0 && this.farmmodule == 18){newitem = Proffession.getfarmmining(1);}
			if(newitem == 0 && this.farmmodule == 19){newitem = Proffession.getfarmherbing(1);}
			if(newitem == 0 && this.farmmodule == 20){newitem = Proffession.getfarmfishing(1);}
			if(newitem == 0 && this.farmmodule == 21){newitem = Proffession.getfarmgraveyard(1);}
			}
			

			if(newitem == 0){
			fury[20] = (byte)0x18;
			fury[24] = (byte)0xff;
			fury[25] = (byte)0xff;
			fury[26] = (byte)0xff;
			fury[27] = (byte)0xff;
			fury[36] = (byte)0xff;
			}else{
			fury[19] = (byte)0xbf;
			fury[20] = (byte)0x18;
			
			fury[24] = (byte)0xff;
			fury[25] = (byte)0xff;
			fury[26] = (byte)0xff;
			fury[27] = (byte)0xff;
			
			fury[36] = decrypted[10]; //inv SLOT = receiving item | ff = for griding 
			
			fury[42] = decrypted[11]; //inv y
			fury[43] = decrypted[12]; //inv x
			
			byte[] Itemid = BitTools.intToByteArray(newitem);
			
			//Get the right inventoryslot
			int InvSLOT = (int)decrypted[10];
			//System.out.println("woot"+newitem+" - "+this.getInventorySLOT(InvSLOT)); 
			/*if(this.getInventorySLOT(InvSLOT) != 0 && newitem != this.getInventorySLOT(InvSLOT)){
			//System.out.println("hacker"); 
			return;
			}*/
			if(BitTools.ValueToKey_InvCheckSTACK(this, stack, newitem, this.InventorySLOT) != 1337){
			InvSLOT = BitTools.ValueToKey_InvCheckSTACK(this, stack, newitem, this.InventorySLOT);
			fury[42] = (byte)this.getInventoryHEIGHT(InvSLOT); //inv y
			fury[43] = (byte)this.getInventoryWEIGHT(InvSLOT); //inv x
			fury[36] = (byte)InvSLOT;
			}
			int newstack = stack + this.getInventorySTACK(InvSLOT);
			//System.out.println("ITEM: "+newitem+" - "+stack+" + "+this.getInventorySTACK(InvSLOT)+" = "+newstack); 
			
		
			
			this.setInventory(Integer.valueOf(fury[36]), newitem, Integer.valueOf(fury[42]), Integer.valueOf(fury[43]), newstack);
			byte[] Stack = BitTools.intToByteArray(newstack);
			for(int i=0;i<4;i++) {
				fury[44+i] = Itemid[i];  //item id
				fury[48+i] = Stack[i];  // decrypted[0] == 0 is mining/herb/fishing duriontation | decrypted[0] == 2 is totalnew itemstack
			}
			}
			//this.sendToMap(extfury); MAYBE??????
			this.Proffone = 0;
			this.Profftwo = 0;
		    this.Proffthree = 0;
		    this.proffpool = true;
		    this.proffDynamiccd = 13000;
		    this.proffcd = System.currentTimeMillis();
		    }else{
		    	//System.out.println("hacker"); 
		    return;	
		    }
			
		}
		
		
		// Stop proff
		if((int)decrypted[0] == 3){
			fury[19] = (byte)0xbf;
			fury[20] = (byte)0x03;
			//System.out.println("gtfo 3"); 
			fury[36] = decrypted[10]; //0x00 = receiving item | ff = for griding 
			this.proffpool = false;
			this.sendToMap(extfury);
			this.Proffone = 0;
			this.Profftwo = 0;
		    this.Proffthree = 0;
		}
		
	
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), fury);
	}
	
	public void AddDeffpercentagebuff(int buffid, int value){
		//System.out.println("Added: "+buffid+" - "+value);	
		this.BuffPercentage.put(buffid,value);
	}
	
	// spell cathegory which determines if attack or if buff and what kind of attack or buff type.
	public int Calcdmgtroughdefbuffpercent(int dmg){
		double tmpdef = 0;
		Iterator<Entry<Integer, Integer>> iter = this.BuffPercentage.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> pairs = iter.next();
			double temp =  pairs.getValue() * 0.010; // get 0.X value	
			tmpdef = tmpdef + temp; // make total substraction 
		}
		//System.out.println("Total subtract debuff: "+tmpdef);	
		if(tmpdef >= 1.0){return 0;}// if is bigger then 1.000 then it has taken(absorbed) 100% of all dmg
		double subfromskill = dmg * tmpdef; // get total substration from dmg
		//System.out.println("Total subtract damage: "+subfromskill);	
		double newtotaldmg = dmg - subfromskill; // dmg - substraction = new dmg
		return (int)newtotaldmg;
	}
	
	
	
	//delete the deff buff effect and turn into percentage
	
	
	
	
	public Character(Player pl) {
		this.setPl(pl);
		 //warrior = 121103060
		 //monk = 121413050
		 //assssin = 122206060
		 //mage = 121309060 
		 this.lastloc = new Waypoint(0,0);
		 this.location = new Waypoint(0,0);
	}

	public Character() {
		//TODO: durp durp
		//USAGES: SkillDAO.getbuffdata(1);
		 this.lastloc = new Waypoint(0,0);
		 this.location = new Waypoint(0,0);
	}
	
	/*
	 * Handle all logic required when character is selected in selection screen
	 * and player enters the game
	 */
	public void joinGameWorld() {
		if(!this.wmap.CharacterExists(this)){this.wmap.addCharacter(this);}
		try{
		if (this.wmap.gridExist(currentMap)){
			this.grid = this.wmap.getGrid(this.currentMap);
			this.area = this.grid.update(this);
			this.iniPackets.putAll(this.area.addMemberAndGetMembers(this));
			this.sendInitToAll();
		}
		}catch (NullPointerException e) {
			this.getPlayer().Rarea = true;
			//log.logMessage(Level.SEVERE, this, "Failed to load grid for character "+this.charID +" map:" +this.currentMap + ", disconnecting");
		}

	}
	
	private void sendInitToAll() {
		this.sendInitToList(iniPackets);
	}
	
	/*
	 * Quite the opposite of joining the game world
	 */
	
	public void leaveGameWorld() {
		try{
		this.area.rmMember(this, true); // leavegameworld
		this.wmap.rmCharacter(charID);
		Iterator<Integer> it = this.iniPackets.keySet().iterator();
		Integer player;
		Character nopiggy;
		while (it.hasNext()){
			player = it.next();
			nopiggy = this.wmap.getCharacter(player);
			if(nopiggy != null){
			ServerFacade.getInstance().addWriteByChannel(nopiggy.GetChannel(), this.getVanishByID(this.charID));
			}
		}
		this.iniPackets.clear();
		}catch(NullPointerException e){
		}
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public ConcurrentMap<Integer, Integer> getIniPackets() {
		return iniPackets;
	}

	public void setIniPackets(ConcurrentMap<Integer, Integer> iniPackets) {
		this.iniPackets = iniPackets;
	}
	
	public int getGuildID(){
		return GuildID;
	}	
	
	public void setGuildID(int GuildID){	
	  this.GuildID = GuildID;	
	}
	
	public void RockCityBoi(){	// fairy tailrurur natsu do dem fire flaimzie D:
			  if(this.GuildID != 0){
				//if is already loaded in server then connect me to it
				if(wmap.guildIsLoaded(this.GuildID)){
					//System.out.println("guildIsLoaded");
				this.guild = wmap.getGuild(this.GuildID);	
				}else{
				// else setup the guild
					//System.out.println("setting up the guild");
				Guild GUILD = new Guild();	
				GUILD.guildsetup(this.GuildID);	
				wmap.AddGuild(this.GuildID, GUILD);
				this.guild = wmap.getGuild(this.GuildID);	
				}
			  }
	}
	
	public void fametopcheck(){	
			if(this.faction == 1){
				if(StartGameserver.Lawful.containsKey(this.charID)){
					this.fametitle = StartGameserver.Lawful.get(this.charID);
				}else{this.fametitle = 0;}
			}else
			if(this.faction == 2){
				if(StartGameserver.Evil.containsKey(this.charID)){
					this.fametitle = StartGameserver.Evil.get(this.charID);
				}else{this.fametitle = 0;}
			}else{this.fametitle = 0;}

}
	
	public int getfuryvalue() 					{		return furyvalue;	}	
	public void setfuryvalue(int furyvalue)		{		this.furyvalue = furyvalue;	}		
	public String getLOGsetName() 				{		return LOGsetName;	}	
	public void setLOGsetName(String LOGsetName){		this.LOGsetName = LOGsetName;	}	
	public int getmodelid() 					{		return model;	}	
	public void setmodelid(int model) 			{		this.model = model;	}		
	public String getChargm() 					{		return isgm;	}	
	public void setChargm(String isgm) 			{		this.isgm = isgm;	}		
	public String getName() 					{		return name; 	}
	public void setName(String name) 			{		this.name = name;	}
	public int getFace() 						{		return face;	}	
	public void setFace(int face) 				{		this.face = face;	}	
	public int getFame() 						{		return fame;	}	
	public void setFame(int fame){
		this.fame = fame;	
	}	
	
	public void setfameclap(int fame){
		byte[] famePacket = new byte[192];		
		famePacket[0] = (byte)famePacket.length;
		famePacket[4] = (byte)0x04;
		famePacket[6] = (byte)0x64;
		famePacket[8] = (byte)0x01;
		famePacket[16] = (byte)0x05;
		famePacket[18] = (byte)0x05;
		famePacket[20] = (byte)0x0F;
		this.setFame(fame);
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		byte[] fset = BitTools.intToByteArray(fame);
		for(int a=0;a<4;a++) {
			famePacket[12+a] = chid[a];
			famePacket[24+a] = fset[a];
		}
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), famePacket); 
	}
	
	public void recfame(int recfame) {
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		double Fame;
		if(this.Fame_Tag_100 == 1){Fame = recfame * 2;}    // fame * 100%
		else 				 
								 {Fame = recfame * 1;}    // normal * 1
		int FinalFame = this.getFame() + (int)Fame; // current fame + new fame iget from mob = total fame
		Fame = 1; // reset to * 1
		
		//System.out.println("Setting character fame to: " + FinalFame);
		byte[] fset = BitTools.intToByteArray(FinalFame);
		
		byte[] famePacket = new byte[192];
		famePacket[0] = (byte)famePacket.length;
		famePacket[4] = (byte)0x04;
		famePacket[6] = (byte)0x64;
		famePacket[8] = (byte)0x01;
		famePacket[16] = (byte)0x05;
		famePacket[18] = (byte)0x05;
		famePacket[20] = (byte)0x0F;
		
		for(int a=0;a<4;a++) {
			famePacket[12+a] = chid[a];
			famePacket[24+a] = fset[a];
		}
		this.setFame(FinalFame);
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), famePacket); 
	}
	
	
	
	public int getdeletestate() {
		return deletestate;
	}


	public void setdeletestate(int deletestate) {
		this.deletestate = deletestate;
	}
	
	
	public void DeleteItemMESSAGE(int SLOT) {
		int zero = 1;
		int one = SLOT;
		//System.out.println("delete item: "+zero +" "+ one);
		byte[] chid = BitTools.intToByteArray(this.getCharID());

		byte[] delete = new byte[20];
		delete[0] = (byte)0x14;
		delete[4] = (byte)0x05;
		delete[6] = (byte)0x15;
		delete[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			delete[12+i] = chid[i];
		}
			
		delete[16] = (byte)0x01;
		delete[17] = (byte)SLOT;
		delete[18] = (byte)0x0f;
		delete[19] = (byte)0x0bf;
		
		byte[] delete1 = new byte[20];
		delete1[0] = (byte)delete1.length;
		delete1[4] = (byte)0x04;
		delete1[6] = (byte)0x15;
			
		for(int i=0;i<4;i++) {
			delete1[12+i] = chid[i];
		}
			
		delete1[16] = (byte)0x01;
		delete1[18] = (byte)zero;
		delete1[19] = (byte)SLOT;
		
		 // remove inventory item by slot
		this.InventorySLOT.remove(Integer.valueOf(one)); 
		this.InventoryHEIGHT.remove(Integer.valueOf(one));
		this.InventoryWEIGHT.remove(Integer.valueOf(one));
		this.InventorySTACK.remove(Integer.valueOf(one));
		
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), delete); 
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), delete1); 
		//System.out.println("RADICAL JIHADI");
	}
	
	
	
	public void DeleteItemNOMESSAGE(int SLOT) {
		int zero = 1;
		int one = SLOT;
		//System.out.println("delete item: "+zero +" "+ one);
		byte[] chid = BitTools.intToByteArray(this.getCharID());

		byte[] delete = new byte[20];
		delete[0] = (byte)delete.length;
		delete[4] = (byte)0x04;
		delete[6] = (byte)0x15;
			
		for(int i=0;i<4;i++) {
			delete[12+i] = chid[i];
		}
			
		delete[16] = (byte)0x01;
		delete[18] = (byte)zero;
		delete[19] = (byte)SLOT;
		 // remove inventory item by slot
		this.InventorySLOT.remove(Integer.valueOf(one)); 
		this.InventoryHEIGHT.remove(Integer.valueOf(one));
		this.InventoryWEIGHT.remove(Integer.valueOf(one));
		this.InventorySTACK.remove(Integer.valueOf(one));
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), delete); 
	}
	
	
	public void DeleteItemVisual(int SLOT) {
		int zero = 1;
		int one = SLOT;
		//System.out.println("delete item: "+zero +" "+ one);
		byte[] chid = BitTools.intToByteArray(this.getCharID());

		byte[] delete = new byte[20];
		delete[0] = (byte)delete.length;
		delete[4] = (byte)0x04;
		delete[6] = (byte)0x15;
			
		for(int i=0;i<4;i++) {
			delete[12+i] = chid[i];
		}
			
		delete[16] = (byte)0x01;
		delete[18] = (byte)zero;
		delete[19] = (byte)SLOT;
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), delete); 
	}
	
	
	// This kills the blocking/tilt of inventory
	public void KillInvFreeze() {
		int zero = 1;
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		byte[] delete = new byte[20];
		delete[0] = (byte)delete.length;
		delete[4] = (byte)0x04;
		delete[6] = (byte)0x15;
		for(int i=0;i<4;i++) {delete[12+i] = chid[i];}
		delete[16] = (byte)0x01;
		delete[18] = (byte)zero;
		delete[19] = (byte)0xff;
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), delete); 
	}
	
	public void DeleteInvItem(int SLOT) {
		 // remove inventory item by slot
		//System.out.println("DELETED SLOT: "+SLOT);
		this.InventorySLOT.remove(SLOT);
		this.InventoryHEIGHT.remove(SLOT);
		this.InventoryWEIGHT.remove(SLOT);
		this.InventorySTACK.remove(SLOT);
	}
	
	
	public void DeleteEquipItem(int SLOT) {
		 // remove equip item by slot
		if(EquipSLOT.containsKey(SLOT)){this.EquipSLOT.remove(Integer.valueOf(SLOT));}
	}
	

	public void setexpfromDAO(long exp) {
		this.setExp(exp);
	}
	
	public void setexp(long exp){
	if (exp < 0){this.setExp(0);} 
	else{ // exp is not below the 0 so continue 
		this.setExp(exp);
		if(this.getLevel() < 167){
		long Hexp = exptable.getMAXexptable(this.getLevel()); 
		if (exp >= Hexp){
		long Rexp = exp - Hexp;  
		this.levelup((this.getLevel()+1),Rexp);
		}
		}
   	  }
	}
	
	public long getexp() {
		return getExp();
	}
	
	// rec exp from mob!!
		public void recexp(long recexp) { 
			if(this.hp <= 0){return;}
			byte[] chid = BitTools.intToByteArray(this.getCharID());
			double Exp;
			// if this guy is in party and in rc / mid / sz then add 25% exp

		
			if(this.Exp_Tag_15 == 1){Exp = recexp * 2.15;}
			else
			if(this.Exp_Tag_20 == 1){ Exp = recexp * 2.20;} 
			else
			if(this.Exp_Tag_30 == 1){ Exp = recexp * 2.30;}
			else
			if(this.Exp_Tag_100 == 1){ Exp = recexp * 3;}
			else
			if(this.Exp_Tag_10 == 1){Exp = recexp * 2.10;}
			else
									{ Exp = recexp * 2;}    // normal * 1
			
			
			byte[] msg = BitTools.LongToByteArrayREVERSE((long)Exp);
			byte[] reexp = new byte[24];
			reexp[0] = (byte)0x18;
			reexp[4] = (byte)0x05;
			reexp[6] = (byte)0x0b;
			reexp[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				reexp[12+i] = chid[i]; 
			}
			for(int b=0;b<msg.length;b++) {
				reexp[b+16] = msg[b];
			}
			long newexp = this.getexp() + (long)Exp;
			//System.out.println("TOTAL EXP: "+ newexp);
			ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), reexp); 
			this.setexp(newexp);
		}
		
	
	// rec exp from mobs!!
	public void blackslave(long curexp) { 
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		byte[] msg = BitTools.LongToByteArrayREVERSE(curexp);
		byte[] reexp = new byte[24];
		reexp[0] = (byte)0x18;
		reexp[4] = (byte)0x05;
		reexp[6] = (byte)0x0b;
		reexp[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			reexp[12+i] = chid[i]; 
		}
		for(int b=0;b<msg.length;b++) {
			reexp[b+16] = msg[b];
		}
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), reexp); 
	}
	
	
	public void ExternalGETVendingwindow(Character otherguy) { 
		byte[] otherguyid = BitTools.intToByteArray(otherguy.getCharID());
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		byte[] fury = new byte[1304];
		fury[0] = (byte)0x18;
		fury[1] = (byte)0x05;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x38;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = otherguyid[i];
			fury[20+i] = chid[i]; //target id
		}
		fury[16] = (byte)0x01;
		
		 int inc = 0;
		 Character Tplayer = this;
		
		 int VBS0 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX0 = Tplayer.getboothwindowX(inc);
		 int VBY0 = Tplayer.getboothwindowY(inc);
		 int VBZ0 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS1 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX1 = Tplayer.getboothwindowX(inc);
		 int VBY1 = Tplayer.getboothwindowY(inc);
		 int VBZ1 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS2 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX2 = Tplayer.getboothwindowX(inc);
		 int VBY2 = Tplayer.getboothwindowY(inc);
		 int VBZ2 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS3 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX3 = Tplayer.getboothwindowX(inc);
		 int VBY3 = Tplayer.getboothwindowY(inc);
		 int VBZ3 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS4 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX4 = Tplayer.getboothwindowX(inc);
		 int VBY4 = Tplayer.getboothwindowY(inc);
		 int VBZ4 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS5 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX5 = Tplayer.getboothwindowX(inc);
		 int VBY5 = Tplayer.getboothwindowY(inc);
		 int VBZ5 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS6 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX6 = Tplayer.getboothwindowX(inc);
		 int VBY6 = Tplayer.getboothwindowY(inc);
		 int VBZ6 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS7 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX7 = Tplayer.getboothwindowX(inc);
		 int VBY7 = Tplayer.getboothwindowY(inc);
		 int VBZ7 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS8 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX8 = Tplayer.getboothwindowX(inc);
		 int VBY8 = Tplayer.getboothwindowY(inc);
		 int VBZ8 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS9 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX9 = Tplayer.getboothwindowX(inc);
		 int VBY9 = Tplayer.getboothwindowY(inc);
		 int VBZ9 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS10 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX10 = Tplayer.getboothwindowX(inc);
		 int VBY10 = Tplayer.getboothwindowY(inc);
		 int VBZ10 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS11 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX11 = Tplayer.getboothwindowX(inc);
		 int VBY11 = Tplayer.getboothwindowY(inc);
		 int VBZ11 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS12 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX12 = Tplayer.getboothwindowX(inc);
		 int VBY12 = Tplayer.getboothwindowY(inc);
		 int VBZ12 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS13 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX13 = Tplayer.getboothwindowX(inc);
		 int VBY13 = Tplayer.getboothwindowY(inc);
		 int VBZ13 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS14 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX14 = Tplayer.getboothwindowX(inc);
		 int VBY14 = Tplayer.getboothwindowY(inc);
		 int VBZ14 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS15 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX15 = Tplayer.getboothwindowX(inc);
		 int VBY15 = Tplayer.getboothwindowY(inc);
		 int VBZ15 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS16 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX16 = Tplayer.getboothwindowX(inc);
		 int VBY16 = Tplayer.getboothwindowY(inc);
		 int VBZ16 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS17 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX17 = Tplayer.getboothwindowX(inc);
		 int VBY17 = Tplayer.getboothwindowY(inc);
		 int VBZ17 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS18 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX18 = Tplayer.getboothwindowX(inc);
		 int VBY18 = Tplayer.getboothwindowY(inc);
		 int VBZ18 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS19 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX19 = Tplayer.getboothwindowX(inc);
		 int VBY19 = Tplayer.getboothwindowY(inc);
		 int VBZ19 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS20 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX20 = Tplayer.getboothwindowX(inc);
		 int VBY20 = Tplayer.getboothwindowY(inc);
		 int VBZ20 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS21 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX21 = Tplayer.getboothwindowX(inc);
		 int VBY21 = Tplayer.getboothwindowY(inc);
		 int VBZ21 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS22 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX22 = Tplayer.getboothwindowX(inc);
		 int VBY22 = Tplayer.getboothwindowY(inc);
		 int VBZ22 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS23 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX23 = Tplayer.getboothwindowX(inc);
		 int VBY23 = Tplayer.getboothwindowY(inc);
		 int VBZ23 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS24 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX24 = Tplayer.getboothwindowX(inc);
		 int VBY24 = Tplayer.getboothwindowY(inc);
		 int VBZ24 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS25 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX25 = Tplayer.getboothwindowX(inc);
		 int VBY25 = Tplayer.getboothwindowY(inc);
		 int VBZ25 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS26 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX26 = Tplayer.getboothwindowX(inc);
		 int VBY26 = Tplayer.getboothwindowY(inc);
		 int VBZ26 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS27 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX27 = Tplayer.getboothwindowX(inc);
		 int VBY27 = Tplayer.getboothwindowY(inc);
		 int VBZ27 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS28 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX28 = Tplayer.getboothwindowX(inc);
		 int VBY28 = Tplayer.getboothwindowY(inc);
		 int VBZ28 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS29 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX29 = Tplayer.getboothwindowX(inc);
		 int VBY29 = Tplayer.getboothwindowY(inc);
		 int VBZ29 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS30 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX30 = Tplayer.getboothwindowX(inc);
		 int VBY30 = Tplayer.getboothwindowY(inc);
		 int VBZ30 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS31 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX31 = Tplayer.getboothwindowX(inc);
		 int VBY31 = Tplayer.getboothwindowY(inc);
		 int VBZ31 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS32 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX32 = Tplayer.getboothwindowX(inc);
		 int VBY32 = Tplayer.getboothwindowY(inc);
		 int VBZ32 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS33 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX33 = Tplayer.getboothwindowX(inc);
		 int VBY33 = Tplayer.getboothwindowY(inc);
		 int VBZ33 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS34 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX34 = Tplayer.getboothwindowX(inc);
		 int VBY34 = Tplayer.getboothwindowY(inc);
		 int VBZ34 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS35 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX35 = Tplayer.getboothwindowX(inc);
		 int VBY35 = Tplayer.getboothwindowY(inc);
		 int VBZ35 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS36 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX36 = Tplayer.getboothwindowX(inc);
		 int VBY36 = Tplayer.getboothwindowY(inc);
		 int VBZ36 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS37 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX37 = Tplayer.getboothwindowX(inc);
		 int VBY37 = Tplayer.getboothwindowY(inc);
		 int VBZ37 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS38 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX38 = Tplayer.getboothwindowX(inc);
		 int VBY38 = Tplayer.getboothwindowY(inc);
		 int VBZ38 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS39 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX39 = Tplayer.getboothwindowX(inc);
		 int VBY39 = Tplayer.getboothwindowY(inc);
		 int VBZ39 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS40 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX40 = Tplayer.getboothwindowX(inc);
		 int VBY40 = Tplayer.getboothwindowY(inc);
		 int VBZ40 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS41 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX41 = Tplayer.getboothwindowX(inc);
		 int VBY41 = Tplayer.getboothwindowY(inc);
		 int VBZ41 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS42 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX42 = Tplayer.getboothwindowX(inc);
		 int VBY42 = Tplayer.getboothwindowY(inc);
		 int VBZ42 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS43 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX43 = Tplayer.getboothwindowX(inc);
		 int VBY43 = Tplayer.getboothwindowY(inc);
		 int VBZ43 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS44 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX44 = Tplayer.getboothwindowX(inc);
		 int VBY44 = Tplayer.getboothwindowY(inc);
		 int VBZ44 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS45 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX45 = Tplayer.getboothwindowX(inc);
		 int VBY45 = Tplayer.getboothwindowY(inc);
		 int VBZ45 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS46 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX46 = Tplayer.getboothwindowX(inc);
		 int VBY46 = Tplayer.getboothwindowY(inc);
		 int VBZ46 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS47 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX47 = Tplayer.getboothwindowX(inc);
		 int VBY47 = Tplayer.getboothwindowY(inc);
		 int VBZ47 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS48 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX48 = Tplayer.getboothwindowX(inc);
		 int VBY48 = Tplayer.getboothwindowY(inc);
		 int VBZ48 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS49 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX49 = Tplayer.getboothwindowX(inc);
		 int VBY49 = Tplayer.getboothwindowY(inc);
		 int VBZ49 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS50 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX50 = Tplayer.getboothwindowX(inc);
		 int VBY50 = Tplayer.getboothwindowY(inc);
		 int VBZ50 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS51 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX51 = Tplayer.getboothwindowX(inc);
		 int VBY51 = Tplayer.getboothwindowY(inc);
		 int VBZ51 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS52 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX52 = Tplayer.getboothwindowX(inc);
		 int VBY52 = Tplayer.getboothwindowY(inc);
		 int VBZ52 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS53 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX53 = Tplayer.getboothwindowX(inc);
		 int VBY53 = Tplayer.getboothwindowY(inc);
		 int VBZ53 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS54 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX54 = Tplayer.getboothwindowX(inc);
		 int VBY54 = Tplayer.getboothwindowY(inc);
		 int VBZ54 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS55 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX55 = Tplayer.getboothwindowX(inc);
		 int VBY55 = Tplayer.getboothwindowY(inc);
		 int VBZ55 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS56 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX56 = Tplayer.getboothwindowX(inc);
		 int VBY56 = Tplayer.getboothwindowY(inc);
		 int VBZ56 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS57 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX57 = Tplayer.getboothwindowX(inc);
		 int VBY57 = Tplayer.getboothwindowY(inc);
		 int VBZ57 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS58 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX58 = Tplayer.getboothwindowX(inc);
		 int VBY58 = Tplayer.getboothwindowY(inc);
		 int VBZ58 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS59 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX59 = Tplayer.getboothwindowX(inc);
		 int VBY59 = Tplayer.getboothwindowY(inc);
		 int VBZ59 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS60 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX60 = Tplayer.getboothwindowX(inc);
		 int VBY60 = Tplayer.getboothwindowY(inc);
		 int VBZ60 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS61 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX61 = Tplayer.getboothwindowX(inc);
		 int VBY61 = Tplayer.getboothwindowY(inc);
		 int VBZ61 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS62 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX62 = Tplayer.getboothwindowX(inc);
		 int VBY62 = Tplayer.getboothwindowY(inc);
		 int VBZ62 = Tplayer.getboothwindowSTACK(inc);
		 inc++;
		 int VBS63 = Tplayer.getboothwindowSLOT(inc); 
		 int VBX63 = Tplayer.getboothwindowX(inc);
		 int VBY63 = Tplayer.getboothwindowY(inc);
		 int VBZ63 = Tplayer.getboothwindowSTACK(inc);
		 inc = 0; // reset back to 0
		 
		 byte[] VBSz0 = BitTools.intToByteArray(VBS0);
		 byte[] VBSz1 = BitTools.intToByteArray(VBS1);
		 byte[] VBSz2 = BitTools.intToByteArray(VBS2);
		 byte[] VBSz3 = BitTools.intToByteArray(VBS3);
		 byte[] VBSz4 = BitTools.intToByteArray(VBS4);
		 byte[] VBSz5 = BitTools.intToByteArray(VBS5);
		 byte[] VBSz6 = BitTools.intToByteArray(VBS6);
		 byte[] VBSz7 = BitTools.intToByteArray(VBS7);
		 byte[] VBSz8 = BitTools.intToByteArray(VBS8);
		 byte[] VBSz9 = BitTools.intToByteArray(VBS9);
		 byte[] VBSz10 = BitTools.intToByteArray(VBS10);
		 byte[] VBSz11 = BitTools.intToByteArray(VBS11);
		 byte[] VBSz12 = BitTools.intToByteArray(VBS12);
		 byte[] VBSz13 = BitTools.intToByteArray(VBS13);
		 byte[] VBSz14 = BitTools.intToByteArray(VBS14);
		 byte[] VBSz15 = BitTools.intToByteArray(VBS15);
		 byte[] VBSz16 = BitTools.intToByteArray(VBS16);
		 byte[] VBSz17 = BitTools.intToByteArray(VBS17);
		 byte[] VBSz18 = BitTools.intToByteArray(VBS18);
		 byte[] VBSz19 = BitTools.intToByteArray(VBS19);
		 byte[] VBSz20 = BitTools.intToByteArray(VBS20);
		 byte[] VBSz21 = BitTools.intToByteArray(VBS21);
		 byte[] VBSz22 = BitTools.intToByteArray(VBS22);
		 byte[] VBSz23 = BitTools.intToByteArray(VBS23);
		 byte[] VBSz24 = BitTools.intToByteArray(VBS24);
		 byte[] VBSz25 = BitTools.intToByteArray(VBS25);
		 byte[] VBSz26 = BitTools.intToByteArray(VBS26);
		 byte[] VBSz27 = BitTools.intToByteArray(VBS27);
		 byte[] VBSz28 = BitTools.intToByteArray(VBS28);
		 byte[] VBSz29 = BitTools.intToByteArray(VBS29);
		 byte[] VBSz30 = BitTools.intToByteArray(VBS30);
		 byte[] VBSz31 = BitTools.intToByteArray(VBS31);
		 byte[] VBSz32 = BitTools.intToByteArray(VBS32);
		 byte[] VBSz33 = BitTools.intToByteArray(VBS33);
		 byte[] VBSz34 = BitTools.intToByteArray(VBS34);
		 byte[] VBSz35 = BitTools.intToByteArray(VBS35);
		 byte[] VBSz36 = BitTools.intToByteArray(VBS36);
		 byte[] VBSz37 = BitTools.intToByteArray(VBS37);
		 byte[] VBSz38 = BitTools.intToByteArray(VBS38);
		 byte[] VBSz39 = BitTools.intToByteArray(VBS39);
		 byte[] VBSz40 = BitTools.intToByteArray(VBS40);
		 byte[] VBSz41 = BitTools.intToByteArray(VBS41);
		 byte[] VBSz42 = BitTools.intToByteArray(VBS42);
		 byte[] VBSz43 = BitTools.intToByteArray(VBS43);
		 byte[] VBSz44 = BitTools.intToByteArray(VBS44);
		 byte[] VBSz45 = BitTools.intToByteArray(VBS45);
		 byte[] VBSz46 = BitTools.intToByteArray(VBS46);
		 byte[] VBSz47 = BitTools.intToByteArray(VBS47);
		 byte[] VBSz48 = BitTools.intToByteArray(VBS48);
		 byte[] VBSz49 = BitTools.intToByteArray(VBS49);
		 byte[] VBSz50 = BitTools.intToByteArray(VBS50);
		 byte[] VBSz51 = BitTools.intToByteArray(VBS51);
		 byte[] VBSz52 = BitTools.intToByteArray(VBS52);
		 byte[] VBSz53 = BitTools.intToByteArray(VBS53);
		 byte[] VBSz54 = BitTools.intToByteArray(VBS54);
		 byte[] VBSz55 = BitTools.intToByteArray(VBS55);
		 byte[] VBSz56 = BitTools.intToByteArray(VBS56);
		 byte[] VBSz57 = BitTools.intToByteArray(VBS57);
		 byte[] VBSz58 = BitTools.intToByteArray(VBS58);
		 byte[] VBSz59 = BitTools.intToByteArray(VBS59);
		 byte[] VBSz60 = BitTools.intToByteArray(VBS60);
		 byte[] VBSz61 = BitTools.intToByteArray(VBS61);
		 byte[] VBSz62 = BitTools.intToByteArray(VBS62);
		 byte[] VBSz63 = BitTools.intToByteArray(VBS63);
				
		
		for(int i=0;i<4;i++) {
			fury[28+i] = VBSz0[i]; // item 1
			fury[40+i] = VBSz1[i]; 
			fury[52+i] = VBSz2[i]; 
			fury[64+i] = VBSz3[i]; 
			fury[76+i] = VBSz4[i]; 
			fury[88+i] = VBSz5[i]; 
			fury[100+i] = VBSz6[i]; 
			fury[112+i] = VBSz7[i]; 
			fury[124+i] = VBSz8[i]; 
			fury[136+i] = VBSz9[i]; 
			fury[148+i] = VBSz10[i]; 
			fury[160+i] = VBSz11[i]; 
			fury[172+i] = VBSz12[i]; 
			fury[184+i] = VBSz13[i]; 
			fury[196+i] = VBSz14[i]; 
			fury[208+i] = VBSz15[i]; 
			fury[220+i] = VBSz16[i]; 
			fury[232+i] = VBSz17[i]; 
			fury[244+i] = VBSz18[i]; 
			fury[256+i] = VBSz19[i]; 
			fury[268+i] = VBSz20[i]; 
			fury[280+i] = VBSz21[i]; 
			fury[292+i] = VBSz22[i]; 
			fury[304+i] = VBSz23[i]; 
			fury[316+i] = VBSz24[i]; 
			fury[328+i] = VBSz25[i]; 
			fury[340+i] = VBSz26[i]; 
			fury[352+i] = VBSz27[i]; 
			fury[364+i] = VBSz28[i]; 
			fury[376+i] = VBSz29[i]; 
			fury[388+i] = VBSz30[i]; 
			fury[400+i] = VBSz31[i]; 
			fury[412+i] = VBSz32[i]; 
			fury[424+i] = VBSz33[i]; 
			fury[436+i] = VBSz34[i]; 
			fury[448+i] = VBSz35[i]; 
			fury[460+i] = VBSz36[i]; 
			fury[472+i] = VBSz37[i]; 
			fury[484+i] = VBSz38[i]; 
			fury[496+i] = VBSz39[i]; 
			fury[508+i] = VBSz40[i]; 
			fury[520+i] = VBSz41[i]; 
			fury[532+i] = VBSz42[i]; 
			fury[544+i] = VBSz43[i]; 
			fury[556+i] = VBSz44[i]; 
			fury[568+i] = VBSz45[i]; 
			fury[580+i] = VBSz46[i]; 
			fury[592+i] = VBSz47[i]; 
			fury[604+i] = VBSz48[i]; 
			fury[616+i] = VBSz49[i]; 
			fury[628+i] = VBSz50[i]; 
			fury[640+i] = VBSz51[i]; 
			fury[652+i] = VBSz52[i]; 
			fury[664+i] = VBSz53[i]; 
			fury[676+i] = VBSz54[i]; 
			fury[688+i] = VBSz55[i]; 
			fury[700+i] = VBSz56[i]; 
			fury[712+i] = VBSz57[i]; 
			fury[724+i] = VBSz58[i]; 
			fury[736+i] = VBSz59[i]; 
			fury[748+i] = VBSz60[i]; 
			fury[760+i] = VBSz61[i]; 
			fury[772+i] = VBSz62[i]; 
			fury[784+i] = VBSz63[i]; 
			
		}
	/*	int zero = 0;
		int one = 0;
		int two = 0;
		int three = 0;
		while(zero != 56)
		{
			one++;
			two++;
			three++;
			fury[one] = (byte)VBX0; // window x
			fury[two] = (byte)VBY0; // window y
			fury[three] = (byte)VBZ0; // quantity ( how many x ) | 0x00 = no stack
			
			
			
		zero++;	
		}*/
		
		fury[26] = (byte)VBX0; // window x
		fury[27] = (byte)VBY0; // window y
		fury[32] = (byte)VBZ0; // quantity ( how many x ) | 0x00 = no stack
		
		fury[38] = (byte)VBX1; 
		fury[39] = (byte)VBY1; 
		fury[44] = (byte)VBZ1; 
		
		fury[50] = (byte)VBX2; 
		fury[51] = (byte)VBY2; 
		fury[56] = (byte)VBZ2; 
		
		fury[62] = (byte)VBX3; 
		fury[63] = (byte)VBY3; 
		fury[68] = (byte)VBZ3; 
		
		fury[74] = (byte)VBX4; 
		fury[75] = (byte)VBY4; 
		fury[80] = (byte)VBZ4; 
		
		fury[86] = (byte)VBX5; 
		fury[87] = (byte)VBY5; 
		fury[92] = (byte)VBZ5; 
		
		fury[98] = (byte)VBX6; 
		fury[99] = (byte)VBY6; 
		fury[104] = (byte)VBZ6; 
		
		fury[110] = (byte)VBX7; 
		fury[111] = (byte)VBY7; 
		fury[116] = (byte)VBZ7; 
		
		fury[122] = (byte)VBX8; 
		fury[123] = (byte)VBY8; 
		fury[128] = (byte)VBZ8; 
		
		fury[134] = (byte)VBX9; 
		fury[135] = (byte)VBY9; 
		fury[140] = (byte)VBZ9; 
		
		fury[146] = (byte)VBX10; 
		fury[147] = (byte)VBY10; 
		fury[152] = (byte)VBZ10; 
		
		fury[158] = (byte)VBX11; 
		fury[159] = (byte)VBY11; 
		fury[164] = (byte)VBZ11; 
		
		fury[170] = (byte)VBX12; 
		fury[171] = (byte)VBY12; 
		fury[176] = (byte)VBZ12; 
		
		fury[182] = (byte)VBX13; 
		fury[183] = (byte)VBY13; 
		fury[188] = (byte)VBZ13; 
		
		fury[194] = (byte)VBX14; 
		fury[195] = (byte)VBY14; 
		fury[200] = (byte)VBZ14; 
		
		fury[206] = (byte)VBX15; 
		fury[207] = (byte)VBY15; 
		fury[212] = (byte)VBZ15; 
		
		fury[218] = (byte)VBX16; 
		fury[219] = (byte)VBY16; 
		fury[224] = (byte)VBZ16; 
		
		fury[230] = (byte)VBX17; 
		fury[231] = (byte)VBY17; 
		fury[236] = (byte)VBZ17; 
		
		fury[242] = (byte)VBX18; 
		fury[243] = (byte)VBY18; 
		fury[248] = (byte)VBZ18; 
		
		fury[254] = (byte)VBX19; 
		fury[255] = (byte)VBY19; 
		fury[260] = (byte)VBZ19; 
		
		fury[266] = (byte)VBX20; 
		fury[267] = (byte)VBY20; 
		fury[272] = (byte)VBZ20; 
		
		fury[278] = (byte)VBX21; 
		fury[279] = (byte)VBY21; 
		fury[284] = (byte)VBZ21; 
		
		fury[290] = (byte)VBX22; 
		fury[291] = (byte)VBY22; 
		fury[296] = (byte)VBZ22; 
		
		fury[302] = (byte)VBX23; 
		fury[303] = (byte)VBY23; 
		fury[308] = (byte)VBZ23; 
		
		fury[314] = (byte)VBX24; 
		fury[315] = (byte)VBY24; 
		fury[320] = (byte)VBZ24; 
		
		fury[326] = (byte)VBX25; 
		fury[327] = (byte)VBY25; 
		fury[332] = (byte)VBZ25; 
		
		fury[338] = (byte)VBX26; 
		fury[339] = (byte)VBY26; 
		fury[344] = (byte)VBZ26; 
		
		fury[350] = (byte)VBX27; 
		fury[351] = (byte)VBY27; 
		fury[356] = (byte)VBZ27; 
		
		fury[362] = (byte)VBX28; 
		fury[363] = (byte)VBY28; 
		fury[368] = (byte)VBZ28; 
		
		fury[374] = (byte)VBX29; 
		fury[375] = (byte)VBY29; 
		fury[380] = (byte)VBZ29; 
		
		fury[386] = (byte)VBX30; 
		fury[387] = (byte)VBY30; 
		fury[392] = (byte)VBZ30; 
		
		fury[398] = (byte)VBX31; 
		fury[399] = (byte)VBY31; 
		fury[404] = (byte)VBZ31; 
		
		fury[410] = (byte)VBX32; 
		fury[411] = (byte)VBY32; 
		fury[416] = (byte)VBZ32; 
		
		fury[422] = (byte)VBX33; 
		fury[423] = (byte)VBY33; 
		fury[428] = (byte)VBZ33; 
		
		fury[434] = (byte)VBX34; 
		fury[435] = (byte)VBY34; 
		fury[440] = (byte)VBZ34; 
		
		fury[446] = (byte)VBX35; 
		fury[447] = (byte)VBY35; 
		fury[452] = (byte)VBZ35; 
		
		fury[458] = (byte)VBX36; 
		fury[459] = (byte)VBY36; 
		fury[464] = (byte)VBZ36; 
		
		fury[470] = (byte)VBX37; 
		fury[471] = (byte)VBY37; 
		fury[476] = (byte)VBZ37; 
		
		fury[482] = (byte)VBX38; 
		fury[483] = (byte)VBY38; 
		fury[488] = (byte)VBZ38; 
		
		fury[494] = (byte)VBX39; 
		fury[495] = (byte)VBY39; 
		fury[500] = (byte)VBZ39; 
		
		fury[506] = (byte)VBX40; 
		fury[507] = (byte)VBY40; 
		fury[512] = (byte)VBZ40; 
		
		fury[518] = (byte)VBX41; 
		fury[519] = (byte)VBY41; 
		fury[524] = (byte)VBZ41; 
		
		fury[530] = (byte)VBX42; 
		fury[531] = (byte)VBY42; 
		fury[536] = (byte)VBZ42; 
		
		fury[542] = (byte)VBX43; 
		fury[543] = (byte)VBY43; 
		fury[548] = (byte)VBZ43; 
		
		fury[554] = (byte)VBX44; 
		fury[555] = (byte)VBY44; 
		fury[560] = (byte)VBZ44; 
		
		fury[566] = (byte)VBX45; 
		fury[567] = (byte)VBY45; 
		fury[572] = (byte)VBZ45; 
	
		fury[578] = (byte)VBX46; 
		fury[579] = (byte)VBY46; 
		fury[584] = (byte)VBZ46; 
		
		fury[590] = (byte)VBX47; 
		fury[591] = (byte)VBY47; 
		fury[596] = (byte)VBZ47; 
		
		fury[602] = (byte)VBX48; 
		fury[603] = (byte)VBY48; 
		fury[608] = (byte)VBZ48; 
		
		fury[614] = (byte)VBX49; 
		fury[615] = (byte)VBY49; 
		fury[620] = (byte)VBZ49; 
		
		fury[626] = (byte)VBX50; 
		fury[627] = (byte)VBY50; 
		fury[632] = (byte)VBZ50; 
		
		fury[638] = (byte)VBX51; 
		fury[639] = (byte)VBY51; 
		fury[644] = (byte)VBZ51; 
		
		fury[650] = (byte)VBX52; 
		fury[651] = (byte)VBY52; 
		fury[656] = (byte)VBZ52; 
		
		fury[662] = (byte)VBX53; 
		fury[663] = (byte)VBY53; 
		fury[668] = (byte)VBZ53; 
		
		fury[674] = (byte)VBX54; 
		fury[675] = (byte)VBY54; 
		fury[680] = (byte)VBZ54; 
		
		fury[686] = (byte)VBX55; 
		fury[687] = (byte)VBY55; 
		fury[692] = (byte)VBZ55; 
		
		fury[698] = (byte)VBX56; 
		fury[699] = (byte)VBY56; 
		fury[704] = (byte)VBZ56; 
		
		fury[710] = (byte)VBX57; 
		fury[711] = (byte)VBY57; 
		fury[716] = (byte)VBZ57; 
		
		fury[722] = (byte)VBX58; 
		fury[723] = (byte)VBY58; 
		fury[728] = (byte)VBZ58; 

		fury[734] = (byte)VBX59; 
		fury[735] = (byte)VBY59; 
		fury[740] = (byte)VBZ59; 

		fury[746] = (byte)VBX60; 
		fury[747] = (byte)VBY60; 
		fury[752] = (byte)VBZ60; 

		fury[758] = (byte)VBX61; 
		fury[759] = (byte)VBY61; 
		fury[764] = (byte)VBZ61; 

		fury[770] = (byte)VBX62; 
		fury[771] = (byte)VBY62; 
		fury[776] = (byte)VBZ62; 

		fury[782] = (byte)VBX63; 
		fury[783] = (byte)VBY63; 
		fury[788] = (byte)VBZ63; 

		long Prize0 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize1 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize2 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize3 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize4 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize5 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize6 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize7 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize8 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize9 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize10 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize11 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize12 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize13 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize14 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize15 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize16 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize17 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize18 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize19 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize20 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize21 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize22 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize23 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize24 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize25 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize26 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize27 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize28 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize29 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize30 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize31 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize32 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize33 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize34 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize35 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize36 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize37 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize38 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize39 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize40 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize41 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize42 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize43 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize44 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize45 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize46 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize47 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize48 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize49 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize50 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize51 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize52 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize53 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize54 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize55 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize56 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize57 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize58 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize59 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize60 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize61 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize62 = Tplayer.getboothwindowPRICE(inc);
		inc++;
		long Prize63 = Tplayer.getboothwindowPRICE(inc);
		inc = 0; // reset to 0
		byte[] Price0 = BitTools.LongToByteArrayREVERSE(Prize0);
		byte[] Price1 = BitTools.LongToByteArrayREVERSE(Prize1);
		byte[] Price2 = BitTools.LongToByteArrayREVERSE(Prize2);
		byte[] Price3 = BitTools.LongToByteArrayREVERSE(Prize3);
		byte[] Price4 = BitTools.LongToByteArrayREVERSE(Prize4);
		byte[] Price5 = BitTools.LongToByteArrayREVERSE(Prize5);
		byte[] Price6 = BitTools.LongToByteArrayREVERSE(Prize6);
		byte[] Price7 = BitTools.LongToByteArrayREVERSE(Prize7);
		byte[] Price8 = BitTools.LongToByteArrayREVERSE(Prize8);
		byte[] Price9 = BitTools.LongToByteArrayREVERSE(Prize9);
		byte[] Price10 = BitTools.LongToByteArrayREVERSE(Prize10);
		byte[] Price11 = BitTools.LongToByteArrayREVERSE(Prize11);
		byte[] Price12 = BitTools.LongToByteArrayREVERSE(Prize12);
		byte[] Price13 = BitTools.LongToByteArrayREVERSE(Prize13);
		byte[] Price14 = BitTools.LongToByteArrayREVERSE(Prize14);
		byte[] Price15 = BitTools.LongToByteArrayREVERSE(Prize15);
		byte[] Price16 = BitTools.LongToByteArrayREVERSE(Prize16);
		byte[] Price17 = BitTools.LongToByteArrayREVERSE(Prize17);
		byte[] Price18 = BitTools.LongToByteArrayREVERSE(Prize18);
		byte[] Price19 = BitTools.LongToByteArrayREVERSE(Prize19);
		byte[] Price20 = BitTools.LongToByteArrayREVERSE(Prize20);
		byte[] Price21 = BitTools.LongToByteArrayREVERSE(Prize21);
		byte[] Price22 = BitTools.LongToByteArrayREVERSE(Prize22);
		byte[] Price23 = BitTools.LongToByteArrayREVERSE(Prize23);
		byte[] Price24 = BitTools.LongToByteArrayREVERSE(Prize24);
		byte[] Price25 = BitTools.LongToByteArrayREVERSE(Prize25);
		byte[] Price26 = BitTools.LongToByteArrayREVERSE(Prize26);
		byte[] Price27 = BitTools.LongToByteArrayREVERSE(Prize27);
		byte[] Price28 = BitTools.LongToByteArrayREVERSE(Prize28);
		byte[] Price29 = BitTools.LongToByteArrayREVERSE(Prize29);
		byte[] Price30 = BitTools.LongToByteArrayREVERSE(Prize30);
		byte[] Price31 = BitTools.LongToByteArrayREVERSE(Prize31);
		byte[] Price32 = BitTools.LongToByteArrayREVERSE(Prize32);
		byte[] Price33 = BitTools.LongToByteArrayREVERSE(Prize33);
		byte[] Price34 = BitTools.LongToByteArrayREVERSE(Prize34);
		byte[] Price35 = BitTools.LongToByteArrayREVERSE(Prize35);
		byte[] Price36 = BitTools.LongToByteArrayREVERSE(Prize36);
		byte[] Price37 = BitTools.LongToByteArrayREVERSE(Prize37);
		byte[] Price38 = BitTools.LongToByteArrayREVERSE(Prize38);
		byte[] Price39 = BitTools.LongToByteArrayREVERSE(Prize39);
		byte[] Price40 = BitTools.LongToByteArrayREVERSE(Prize40);
		byte[] Price41 = BitTools.LongToByteArrayREVERSE(Prize41);
		byte[] Price42 = BitTools.LongToByteArrayREVERSE(Prize42);
		byte[] Price43 = BitTools.LongToByteArrayREVERSE(Prize43);
		byte[] Price44 = BitTools.LongToByteArrayREVERSE(Prize44);
		byte[] Price45 = BitTools.LongToByteArrayREVERSE(Prize45);
		byte[] Price46 = BitTools.LongToByteArrayREVERSE(Prize46);
		byte[] Price47 = BitTools.LongToByteArrayREVERSE(Prize47);
		byte[] Price48 = BitTools.LongToByteArrayREVERSE(Prize48);
		byte[] Price49 = BitTools.LongToByteArrayREVERSE(Prize49);
		byte[] Price50 = BitTools.LongToByteArrayREVERSE(Prize50);
		byte[] Price51 = BitTools.LongToByteArrayREVERSE(Prize51);
		byte[] Price52 = BitTools.LongToByteArrayREVERSE(Prize52);
		byte[] Price53 = BitTools.LongToByteArrayREVERSE(Prize53);
		byte[] Price54 = BitTools.LongToByteArrayREVERSE(Prize54);
		byte[] Price55 = BitTools.LongToByteArrayREVERSE(Prize55);
		byte[] Price56 = BitTools.LongToByteArrayREVERSE(Prize56);
		byte[] Price57 = BitTools.LongToByteArrayREVERSE(Prize57);
		byte[] Price58 = BitTools.LongToByteArrayREVERSE(Prize58);
		byte[] Price59 = BitTools.LongToByteArrayREVERSE(Prize59);
		byte[] Price60 = BitTools.LongToByteArrayREVERSE(Prize60);
		byte[] Price61 = BitTools.LongToByteArrayREVERSE(Prize61);
		byte[] Price62 = BitTools.LongToByteArrayREVERSE(Prize62);
		byte[] Price63 = BitTools.LongToByteArrayREVERSE(Prize63);

		
		for(int i=0;i<8;i++) {
			fury[792+i] = Price0[i]; // 1st price
			fury[800+i] = Price1[i];
			fury[808+i] = Price2[i];
			fury[816+i] = Price3[i];
			fury[824+i] = Price4[i];
			fury[832+i] = Price5[i];
			fury[840+i] = Price6[i];
			fury[848+i] = Price7[i];
			fury[856+i] = Price8[i];
			fury[864+i] = Price9[i];
			fury[872+i] = Price10[i];
			fury[880+i] = Price11[i];
			fury[888+i] = Price12[i];
			fury[896+i] = Price13[i];
			fury[904+i] = Price14[i];
			fury[912+i] = Price15[i];
			fury[920+i] = Price16[i];
			fury[928+i] = Price17[i];
			fury[936+i] = Price18[i];
			fury[944+i] = Price19[i];
			fury[952+i] = Price20[i];
			fury[960+i] = Price21[i];
			fury[968+i] = Price22[i];
			fury[976+i] = Price23[i];
			fury[984+i] = Price24[i];
			fury[992+i] = Price25[i];
			fury[1000+i] = Price26[i];
			fury[1008+i] = Price27[i];
			fury[1016+i] = Price28[i];
			fury[1024+i] = Price29[i];
			fury[1032+i] = Price30[i];
			fury[1040+i] = Price31[i];
			fury[1048+i] = Price32[i];
			fury[1056+i] = Price33[i];
			fury[1064+i] = Price34[i];
			fury[1072+i] = Price35[i];
			fury[1080+i] = Price36[i];
			fury[1088+i] = Price37[i];
			fury[1096+i] = Price38[i];
			fury[1104+i] = Price39[i];
			fury[1112+i] = Price40[i];
			fury[1120+i] = Price41[i];
			fury[1128+i] = Price42[i];
			fury[1136+i] = Price43[i];
			fury[1144+i] = Price44[i];
			fury[1152+i] = Price45[i];
			fury[1160+i] = Price46[i];
			fury[1168+i] = Price47[i];
			fury[1176+i] = Price48[i];
			fury[1184+i] = Price49[i];
			fury[1192+i] = Price50[i];
			fury[1200+i] = Price51[i];
			fury[1208+i] = Price52[i];
			fury[1216+i] = Price53[i];
			fury[1224+i] = Price54[i];
			fury[1232+i] = Price55[i];
			fury[1240+i] = Price56[i];
			fury[1248+i] = Price57[i];
			fury[1256+i] = Price58[i];
			fury[1264+i] = Price59[i];
			fury[1272+i] = Price60[i];
			fury[1280+i] = Price61[i];
			fury[1288+i] = Price62[i];
			fury[1296+i] = Price63[i];
		}
		ServerFacade.getInstance().addWriteByChannel(otherguy.GetChannel(), fury); 
	}
	
	
	// recfury bar 
	public void recfury(int fury) { 
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		int newfury = this.furyvalue + fury;
		this.furyvalue = newfury;
		//System.out.println("this.furyvalue: " +this.furyvalue);
		if(newfury >= exptable.getfuryV(this.getLevel())||newfury == 20||newfury == 40||newfury == 60||newfury == 80||newfury == 100||newfury == 120||newfury == 140||newfury == 160||
				newfury == 180||newfury == 200||newfury == 220||newfury == 240||newfury == 260||newfury == 280||newfury == 300||newfury == 320||
				newfury == 350||newfury == 380||newfury == 400||newfury == 420||newfury == 450||newfury == 480||newfury == 500||newfury == 520||
				newfury == 550||newfury == 580||newfury == 600||newfury == 620||newfury == 650||newfury == 680||newfury == 700){
	
		byte[] WTF = BitTools.intToByteArray(Integer.valueOf(newfury));
		byte[] healpckt = new byte[20];
		healpckt[0] = (byte)0x14;
		healpckt[4] = (byte)0x04;
		healpckt[6] = (byte)0x69;
		healpckt[8] = (byte)0x01; 
		for(int i=0;i<4;i++) {
			healpckt[12+i] = chid[i]; 
		}
		for(int i=0;i<2;i++) {
			healpckt[16+i] = WTF[i]; 
		}
		healpckt[18] = (byte)0x0f;
		healpckt[19] = (byte)0xbf;
		//System.out.println("this.furyvalue1: " +this.furyvalue);
		if(newfury >= exptable.getfuryV(this.getLevel())){this.furyactive = 1;this.FuryTime = exptable.getfuryT(this.getLevel());}

		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), healpckt); 
		}
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	// level up 
	public void levelup(int levell, long exp) {
			////System.out.println("================== LEVEL UP! ==================== |");
			this.setLevel(levell);
			int setstp = this.getStatPoints() + lookuplevel.getstatP(levell); // final value of statpoints
			int setskp = this.getSkillPoints() + lookuplevel.getskillP(levell); // final value of skillpoints
			this.setStatPoints(setstp);
			this.setSkillPoints(setskp);

			if(exp >= exptable.getMAXexptable(levell)){this.setexp(exp);} // prevents from spam n lagging 
			else{
			CharacterDAO.setlevel(levell,this.charID); // save it DB
			
			byte[] cid = BitTools.intToByteArray(this.getCharID());
			byte[] level = BitTools.shortToByteArray((short)levell);
			byte[] remainingexp = BitTools.LongToByteArrayREVERSE(exp);
			//System.out.println("exp: " +exp);
			byte[] healpckt = new byte[40];
			healpckt[0] = (byte)0x28;
			healpckt[4] = (byte)0x05;
			healpckt[6] = (byte)0x20;
			healpckt[8] = (byte)0x01; 
			healpckt[9] = (byte)0x39; 
			healpckt[10] = (byte)0x07;
			healpckt[11] = (byte)0x08;
			
			for(int i=0;i<4;i++) {
				healpckt[12+i] = cid[i];
			}
			for(int i=0;i<level.length;i++) {
					healpckt[16+i] = level[i]; // level
			 }
			
			healpckt[30] = (byte)0x00; // 0xa0
			healpckt[31] = (byte)0x00; // 0x41    IDK ?????


			 for(int i=0;i<4;i++) {
					healpckt[36+i] = remainingexp[i];// exp left and put upon leveling up
			 }

			 final int hp = this.getHp(); 	 // should be max HP
			 final int mana = this.getMana(); // should be max mana
			 this.setHp(Integer.valueOf(hp));
			 this.setMana(Integer.valueOf(mana));
		 
		 

		 
		 byte[] fstp = BitTools.intToByteArray(setstp);
		 byte[] fskp = BitTools.intToByteArray(setskp);
		 byte[] fhp = BitTools.intToByteArray(hp);
		 byte[] fmana = BitTools.intToByteArray(mana);
		 

			
		 for(int i=0;i<2;i++) {

				healpckt[18+i] = fstp[i]; // stat points
				
				healpckt[20+i] = fskp[i]; // skill points
				
				healpckt[24+i] = fhp[i]; // hp
				
				healpckt[28+i] = fmana[i]; // mana
			}
		 
		 	CharacterDAO.setstatpoints(setstp, this.getCharID());
			CharacterDAO.setskillpoints(setskp,this.getCharID());
			this.sendToMap(healpckt);
			ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), healpckt); 
			this.setexp(exp); // has to be last to redo the whole damn thing
			this.statlist();
			if(levell == 81){this.setfameclap(this.fame + 20000);CharacterDAO.sendmail(1,"<System Reward>", this.getCharID(), this.getLOGsetName(), 0, 283000005,1,0,0,0,0,0,0,0,0,0); Charstuff.getInstance().respondguild("Added 20000 fame, Check your Delivery for your Banno.", this.GetChannel());}
			if(levell == 144){this.setfameclap(this.fame + 1000000);CharacterDAO.sendmail(1,"<System Reward>", this.getCharID(), this.getLOGsetName(), 0, 283000005,1,0,0,0,0,0,0,0,0,0); Charstuff.getInstance().respondguild("Added 1000000 fame, Check your Delivery for your Banno.", this.GetChannel());}
			if(levell == 91){CharacterDAO.sendmail(1,"<System Reward>", this.getCharID(), this.getLOGsetName(), 0, 283000006,1,0,0,0,0,0,0,0,0,0); Charstuff.getInstance().respondguild("Check your Delivery for your Jun Pot.", this.GetChannel());}
			Party pt = getWmap().getParty(this.partyUID);
			if (pt != null){pt.Refresh_Party(this);}
	}
	}	
	
	// recdamage test saaaaaaaaaaan!
	public void recDamge( int damage ) { // int charid, int damage ?
		byte[] healpckt = new byte[32];
		healpckt[0] = (byte)healpckt.length;
		healpckt[4] = (byte)0x05;
		healpckt[6] = (byte)0x35;
		healpckt[8] = (byte)0x08; 
		healpckt[9] = (byte)0x60; 
		healpckt[10] = (byte)0x22;
		healpckt[11] = (byte)0x45;
		
		
		int newhp = this.hp - damage;
		this.setHp(newhp);
		 //System.out.println("NEW HP : "+ newhp);
			byte[] cid = BitTools.intToByteArray(this.getCharID());
			byte[] hp = BitTools.intToByteArray(newhp);
			byte[] mana = BitTools.intToByteArray(this.mana);
			byte[] stam = BitTools.intToByteArray(this.stamina);
			
			healpckt[24] = hp[0];
			healpckt[25] = hp[1];
			
			healpckt[28] = mana[0];
			healpckt[29] = mana[1];
			
			healpckt[30] = stam[0];
			healpckt[31] = stam[1];			
		
			for(int i=0;i<4;i++) {
				healpckt[12+i] = cid[i];
			}
			
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), healpckt); 
	}

	public int getstrength() {
		return strength;
	}

	public void setstrength(int strength) {
		this.strength = strength;
	}
	
	public int getintelligence() {
		return intelligence;
	}

	public void setintelligence(int intelligence) {
		this.intelligence = intelligence;
	}
	
	public int getvitality() {
		return vitality;
	}

	public void setvitality(int vitality) {
		this.vitality = vitality;
	}
	
	public int getdextery() {
		return dextery;
	}

	public void setdextery(int dextery) {
		this.dextery = dextery;
	}

	public int getagility() {
		return agility;
	}

	public void setagility(int agility) {
		this.agility = agility;
		this.statlist();
		
	}

	public int getStatPoints() {
		return statPoints;
	}

	public void setStatPoints(int statPoints) {
		this.statPoints = statPoints;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}

	public int getCharacterClass() {
		return characterClass;
	}

	public void setCharacterClass(int characterClass) {
	   	if (characterClass == 1){ skills.put(Integer.valueOf(-1), Integer.valueOf(121103060)); }
		if (characterClass == 2){ skills.put(Integer.valueOf(-1), Integer.valueOf(122206060)); }
		if (characterClass == 3){ skills.put(Integer.valueOf(-1), Integer.valueOf(121309060)); }
		if (characterClass == 4){ skills.put(Integer.valueOf(-1), Integer.valueOf(121413050)); }
		this.characterClass = characterClass;
	}

	public int getFaction() {
		return faction;
	}

	public void setFaction(int faction) {
		this.faction = faction;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getHp() {
		return hp;
	}
	

	public int getMana() {
		return mana;
	}


	public int getStamina() {
		return stamina;
	}


	public Player getPlayer() {
		return getPl();
	}

	public void setPlayer(Player pl) {
		this.setPl(pl);
	}
	
	// temporary store buffs
	public int gettempstore(int buffslot) {
		if(TempStoreBuffs.containsKey(buffslot)){
		int buffvalue = TempStoreBuffs.get(buffslot);
		//System.out.println("TempStoreBuffs: " +buffslot+" - " +buffvalue);
		return buffvalue;}else
		{ //System.out.println(buffslot+" - null "); 
		return 0;}
	}

	public void settempstore(int bufficon, int buffvalue) {
	TempStoreBuffs.put(Integer.valueOf(bufficon), Integer.valueOf(buffvalue)); 
	//System.out.println("TempStoreBuffs: " +buffslot+" - " +buffvalue);
	}
	
	// skillbar
	public int getskillbar(int i) {
		if(skillbar.containsKey(i)){
		int ItemOrSkill = skillbar.get(i);
		//System.out.println("getskillbar: " +i+" - " +ItemOrSkill);
		return ItemOrSkill;}else
		{ //System.out.println(i+" - null "); 
		return 0;}
	}

	public void setskillbar(int SkillSlotId, int headerid, int ItemOrSkill) {
	if(headerid == 0) {
	this.skillbar.remove(Integer.valueOf(SkillSlotId));
	//System.out.println("skillbar: " +SkillSlotId+" - " +" null ");
	}else{
	skillbar.put(Integer.valueOf(SkillSlotId), Integer.valueOf(ItemOrSkill)); 
	//System.out.println("skillbar: " +SkillSlotId+" - " +ItemOrSkill);
	}


	}
	
	/*<@knock> if (skilss.containsValue(i))
	  <@knock> or containsKey*/
	
	public int getlearnedskill(int i) {
		if(skills.containsKey(i)){ //System.out.-println("FOUND:----- ");
			int skillid = skills.get(i);
			//System.out.println("skillhandler: " +i+" - " +skillid);
			return skillid;
		}else{ //System.out.println(i+" - null "); 
		return 0; 
		}
	}

	public void setLearnedSkill(int headerid, int skillid) {
		skills.put(Integer.valueOf(headerid), Integer.valueOf(skillid)); 
		//System.out.println("skillhandler: " +headerid+" - " +skillid);
	}
	
	public int getignorelist(int i) {
		if(ignorelist.containsKey(i)){String ignorename = ignorelist.get(i); 
		//System.out.println("getignorelist: " +i+" - " +ignorename);
		
		byte[] ignorenameinbyte  = BitTools.stringToByteArray(ignorename); ;
		
		byte[] decryptedname = new byte[15];
		for(int z=0;z<15;z++) {
			decryptedname[z] = ignorenameinbyte[z];
		}
		int finalname = BitTools.byteArrayToInt(decryptedname); 
		//System.out.println("FINAL NAME INT: " +i+" - " +finalname);
		return finalname;}else {return 0;}
	}
	
	public int getfriendslist(int i) {
		if(friendslist.containsKey(i)){ //System.out.println("FOUND FRIEND:----- ");
		String friendname = friendslist.get(i); 
		//System.out.println("getfriendslist: " +i+" - " +friendname);
		
		byte[] friendnameinbyte  = BitTools.stringToByteArray(friendname); ;
		//System.out.println("MEEP LMOST AT FINAL INT");
		byte[] decryptedname = new byte[15];
		for(int z=0;z<15;z++) {
			decryptedname[z] = friendnameinbyte[z];
		}
		//System.out.println("MEEP -- to go");
		int finalname = BitTools.byteArrayToInt(decryptedname); 
		//System.out.println("FINAL NAME INT: " +i+" - " +finalname);
		return finalname;}else { //System.out.println(" null "); 
			return 0;}
		}
	

	public void setwholist(int headerid1, int headerid2, String friendname) {
		 
		if (headerid1 == 0){
		friendslist.put(Integer.valueOf(headerid2), friendname); 
		//System.out.println("friendslist: " +headerid2+" - " +friendname);
		}
		
		if (headerid1 == 1){
		ignorelist.put(Integer.valueOf(headerid2), friendname); 
		//System.out.println("ignorelist: " +headerid2+" - " +friendname);
		} 
		
		if (headerid1 == 2){ //= delete friendslist
		this.friendslist.remove(Integer.valueOf(headerid2));
		CharacterDAO.deletewholist(this.charID,0,headerid2);
		//System.out.println("friendslist: " +headerid2+" - " +" null ");
		};
		
		if (headerid1 == 3){ //	= delete ignorelist
		this.ignorelist.remove(Integer.valueOf(headerid2));
		CharacterDAO.deletewholist(this.charID,1,headerid2);
		//System.out.println("ignorelist: " +headerid2+" - " +" null ");
		};
	}	
	

	public Area getArea() {
		return area;
	}

	public Grid getGrid() {
		return grid;
	}

	public int[] getAreaCoords() {
		return areaCoords;
	}

	@Override
	public int getuid() {
		// TODO Auto-generated method stub
		return this.charID;
	}

	@Override
	public void setuid(int uid) {
		// TODO Auto-generated method stub
		this.charID = uid;		
	}

	public String getip() {
		return this.getPl().getip();
	}
	
	@Override
	public SocketChannel GetChannel() {
		//if(this.getPl().getSc() != null){
		return this.getPl().getSc();
		//}
		//return null;
	}

	@Override
	public short getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCharID() {
		return charID;
	}
	
	public int getMAXMana() {
		return this.maxmana;
	}

	public void setCharID(int charID) {
		this.charID = charID;
	}
	
	public void setX(float x) {
		this.location.setX(x);
	}
	
	public void setvp(int vp) {
		this.vendingpoint = vp;
	}
	
	public int getvp() {
		return this.vendingpoint;
	}
	
	public void setY(float y) {	
		this.location.setY(y);
	}
	
	@Override
	public float getlastknownX() {
		// TODO Auto-generated method stub	
		return this.location.getX();
	}

	@Override
	public float getlastknownY() {
		// TODO Auto-generated method stub
		return this.location.getY();
	}
	
	public Waypoint getLocation() {
		return this.location;
	}
	
	public Waypoint getlastloc() {
		return this.lastloc;
	}

	public int getMaxHp() {
		return maxhp;
	}

	public void setMaxHp(int max) {
		this.maxhp = max;
	}

	public int getCurrentMap() {
		return currentMap;
	}

	public void setCurrentMap(int currentMap) {
		this.currentMap = currentMap;
	}

	public byte[] getCharacterDataPacket() {
		return characterDataPacket;
	}

	public void setCharacterDataPacket(byte[] characterDataPacket) {
		this.characterDataPacket = characterDataPacket;
	}
	
	public void setgold(long gold) {
		if (gold >=  1000000000000000L)
		{this.gold = 1000000000000000L;
		}else if(gold <= 0){this.gold = 0;}
		else{
		this.gold = gold;}
	}
	
	public long getgold() {
		return gold;
	}
	

	
	public Character getitemdata(){
		try{
			//System.out.println("Loading ITEM ARRIBUTES...");
			String e0,e1,e2,e3,e4,e5,e6,e7,/*e8,*/e9,e10,e11,e12,e13,e14,e15,e16;
			
			if(ItemCache.Items.containsKey(this.getequipSLOT(0))){e0 = ItemCache.Items.get(this.getequipSLOT(0));}else{e0 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(1))){e1 = ItemCache.Items.get(this.getequipSLOT(1));}else{e1 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(2))){e2 = ItemCache.Items.get(this.getequipSLOT(2));}else{e2 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(3))){e3 = ItemCache.Items.get(this.getequipSLOT(3));}else{e3 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(4))){e4 = ItemCache.Items.get(this.getequipSLOT(4));}else{e4 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(5))){e5 = ItemCache.Items.get(this.getequipSLOT(5));}else{e5 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(6))){e6 = ItemCache.Items.get(this.getequipSLOT(6));}else{e6 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(7))){e7 = ItemCache.Items.get(this.getequipSLOT(7));}else{e7 = "";}
			/*if(ItemCache.Items.containsKey(this.getequipSLOT(8))){e8 = ItemCache.Items.get(this.getequipSLOT(8));}else{e8 = "";}*/
			if(ItemCache.Items.containsKey(this.getequipSLOT(9))){e9 = ItemCache.Items.get(this.getequipSLOT(9));}else{e9 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(10))){e10 = ItemCache.Items.get(this.getequipSLOT(10));}else{e10 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(11))){e11 = ItemCache.Items.get(this.getequipSLOT(11));}else{e11 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(12))){e12 = ItemCache.Items.get(this.getequipSLOT(12));}else{e12 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(13))){e13 = ItemCache.Items.get(this.getequipSLOT(13));}else{e13 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(14))){e14 = ItemCache.Items.get(this.getequipSLOT(14));}else{e14 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(15))){e15 = ItemCache.Items.get(this.getequipSLOT(15));}else{e15 = "";}
			if(ItemCache.Items.containsKey(this.getequipSLOT(16))){e16 = ItemCache.Items.get(this.getequipSLOT(16));}else{e16 = "";}
			
			
			
			String[] item0 = e0.split(",");
			String[] item1 = e1.split(",");
			String[] item2 = e2.split(",");
			String[] item3 = e3.split(",");
			String[] item4 = e4.split(",");
			String[] item5 = e5.split(",");
			String[] item6 = e6.split(",");
			String[] item7 = e7.split(",");
			//String[] item8 = e8.split(",");
			String[] item9 = e9.split(",");
			String[] item10 = e10.split(",");
			String[] item11 = e11.split(",");
			String[] item12 = e12.split(",");
			String[] item13 = e13.split(",");
			String[] item14 = e14.split(",");
			String[] item15 = e15.split(",");
			String[] item16 = e16.split(",");
		
				
			//SET ITEM BONUSSES 
			if(item1[0].equals(item0[0]) && item2[0].equals(item0[0]) && item3[0].equals(item0[0])
			&& item4[0].equals(item0[0]) && item5[0].equals(item0[0]) && item6[0].equals(item0[0]) && 
			item9[0].equals(item0[0]) && item10[0].equals(item0[0]) && item11[0].equals(item0[0]))
			{
				 //System.out.println("==> 10x"); 
			SBITEMdmgbonus = Integer.valueOf(item0[1]) + Integer.valueOf(item1[1]) + Integer.valueOf(item2[1]) + Integer.valueOf(item3[1]) + Integer.valueOf(item4[1]) + 
					Integer.valueOf(item5[1]) + Integer.valueOf(item6[1]) + Integer.valueOf(item7[1]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[1]) + 
					Integer.valueOf(item10[1]) + Integer.valueOf(item11[1]) + Integer.valueOf(item12[1]) + Integer.valueOf(item13[1]) + Integer.valueOf(item14[1]) + 
					Integer.valueOf(item15[1]) + Integer.valueOf(item16[1]);	
				

			SBITEMstr = Integer.valueOf(item0[2]) + Integer.valueOf(item1[2]) + Integer.valueOf(item2[2]) + Integer.valueOf(item3[2]) + Integer.valueOf(item4[2]) + 
					Integer.valueOf(item5[2]) + Integer.valueOf(item6[2]) + Integer.valueOf(item7[2]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[2]) + 
					Integer.valueOf(item10[2]) + Integer.valueOf(item11[2]) + Integer.valueOf(item12[2]) + Integer.valueOf(item13[2]) + Integer.valueOf(item14[2]) + 
					Integer.valueOf(item15[2]) + Integer.valueOf(item16[2]);	
				
				
			SBITEMdex = Integer.valueOf(item0[3]) + Integer.valueOf(item1[3]) + Integer.valueOf(item2[3]) + Integer.valueOf(item3[3]) + Integer.valueOf(item4[3]) + 
					Integer.valueOf(item5[3]) + Integer.valueOf(item6[3]) + Integer.valueOf(item7[3]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[3]) + 
					Integer.valueOf(item10[3]) + Integer.valueOf(item11[3]) + Integer.valueOf(item12[3]) + Integer.valueOf(item13[3]) + Integer.valueOf(item14[3]) + 
					Integer.valueOf(item15[3]) + Integer.valueOf(item16[3]);		
				
			SBITEMvit = Integer.valueOf(item0[4]) + Integer.valueOf(item1[4]) + Integer.valueOf(item2[4]) + Integer.valueOf(item3[4]) + Integer.valueOf(item4[4]) + 
					Integer.valueOf(item5[4]) + Integer.valueOf(item6[4]) + Integer.valueOf(item7[4]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[4]) + 
					Integer.valueOf(item10[4]) + Integer.valueOf(item11[4]) + Integer.valueOf(item12[4]) + Integer.valueOf(item13[4]) + Integer.valueOf(item14[4]) + 
					Integer.valueOf(item15[4]) + Integer.valueOf(item16[4]);		
				
			SBITEMint = Integer.valueOf(item0[5]) + Integer.valueOf(item1[5]) + Integer.valueOf(item2[5]) + Integer.valueOf(item3[5]) + Integer.valueOf(item4[5]) + 
					Integer.valueOf(item5[5]) + Integer.valueOf(item6[5]) + Integer.valueOf(item7[5]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[5]) + 
					Integer.valueOf(item10[5]) + Integer.valueOf(item11[5]) + Integer.valueOf(item12[5]) + Integer.valueOf(item13[5]) + Integer.valueOf(item14[5]) + 
					Integer.valueOf(item15[5]) + Integer.valueOf(item16[5]);			
				
			SBITEMagi = Integer.valueOf(item0[6]) + Integer.valueOf(item1[6]) + Integer.valueOf(item2[6]) + Integer.valueOf(item3[6]) + Integer.valueOf(item4[6]) + 
					Integer.valueOf(item5[6]) + Integer.valueOf(item6[6]) + Integer.valueOf(item7[6]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[6]) + 
					Integer.valueOf(item10[6]) + Integer.valueOf(item11[6]) + Integer.valueOf(item12[6]) + Integer.valueOf(item13[6]) + Integer.valueOf(item14[6]) + 
					Integer.valueOf(item15[6]) + Integer.valueOf(item16[6]);			
			

			SBITEMatk = Integer.valueOf(item0[7]) + Integer.valueOf(item1[7]) + Integer.valueOf(item2[7]) + Integer.valueOf(item3[7]) + Integer.valueOf(item4[7]) + 
					Integer.valueOf(item5[7]) + Integer.valueOf(item6[7]) + Integer.valueOf(item7[7]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[7]) + 
					Integer.valueOf(item10[7]) + Integer.valueOf(item11[7]) + Integer.valueOf(item12[7]) + Integer.valueOf(item13[7]) + Integer.valueOf(item14[7]) + 
					Integer.valueOf(item15[7]) + Integer.valueOf(item16[7]);	
			
			SBITEMdef = Integer.valueOf(item0[8]) + Integer.valueOf(item1[8]) + Integer.valueOf(item2[8]) + Integer.valueOf(item3[8]) + Integer.valueOf(item4[8]) + 
					Integer.valueOf(item5[8]) + Integer.valueOf(item6[8]) + Integer.valueOf(item7[8]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[8]) + 
					Integer.valueOf(item10[8]) + Integer.valueOf(item11[8]) + Integer.valueOf(item12[8]) + Integer.valueOf(item13[8]) + Integer.valueOf(item14[8]) + 
					Integer.valueOf(item15[8]) + Integer.valueOf(item16[8]);	
			
			SBITEMlife = Integer.valueOf(item0[9]) + Integer.valueOf(item1[9]) + Integer.valueOf(item2[9]) + Integer.valueOf(item3[9]) + Integer.valueOf(item4[9]) + 
					Integer.valueOf(item5[9]) + Integer.valueOf(item6[9]) + Integer.valueOf(item7[9]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[9]) + 
					Integer.valueOf(item10[9]) + Integer.valueOf(item11[9]) + Integer.valueOf(item12[9]) + Integer.valueOf(item13[9]) + Integer.valueOf(item14[9]) + 
					Integer.valueOf(item15[9]) + Integer.valueOf(item16[9]);	
				
			SBITEMmana = Integer.valueOf(item0[10]) + Integer.valueOf(item1[10]) + Integer.valueOf(item2[10]) + Integer.valueOf(item3[10]) + Integer.valueOf(item4[10]) + 
					Integer.valueOf(item5[10]) + Integer.valueOf(item6[10]) + Integer.valueOf(item7[10]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[10]) + 
					Integer.valueOf(item10[10]) + Integer.valueOf(item11[10]) + Integer.valueOf(item12[10]) + Integer.valueOf(item13[10]) + Integer.valueOf(item14[10]) + 
					Integer.valueOf(item15[10]) + Integer.valueOf(item16[10]);	
			 }else
			if(Integer.valueOf(item1[0]) < 12 && item6[0].equals(item1[0]) && item9[0].equals(item1[0]) && item10[0].equals(item1[0]))
			{
				// System.out.println("==> 4x"); 
				
				SBITEMdmgbonus = Integer.valueOf(item1[1]) + Integer.valueOf(item6[1]) + Integer.valueOf(item9[1]) + Integer.valueOf(item10[1]);		

				SBITEMstr =  Integer.valueOf(item1[2]) + Integer.valueOf(item6[2]) + Integer.valueOf(item9[2]) + Integer.valueOf(item10[2]);
				
				SBITEMdex =  Integer.valueOf(item1[3]) + Integer.valueOf(item6[3]) + Integer.valueOf(item9[3]) + Integer.valueOf(item10[3]);
					
				SBITEMvit =  Integer.valueOf(item1[4]) + Integer.valueOf(item6[4]) + Integer.valueOf(item9[4]) + Integer.valueOf(item10[4]);
					
				SBITEMint =  Integer.valueOf(item1[5]) + Integer.valueOf(item6[5]) + Integer.valueOf(item9[5]) + Integer.valueOf(item10[5]);		
					
				SBITEMagi =  Integer.valueOf(item1[6]) + Integer.valueOf(item6[6]) + Integer.valueOf(item9[6]) + Integer.valueOf(item10[6]);	

				SBITEMatk =  Integer.valueOf(item1[7]) + Integer.valueOf(item6[7]) + Integer.valueOf(item9[7]) + Integer.valueOf(item10[7]);	
				
				SBITEMdef =  Integer.valueOf(item1[8]) + Integer.valueOf(item6[8]) + Integer.valueOf(item9[8]) + Integer.valueOf(item10[8]);
				
				SBITEMlife =  Integer.valueOf(item1[9]) + Integer.valueOf(item6[9]) + Integer.valueOf(item9[9]) + Integer.valueOf(item10[9]);
					
				SBITEMmana =  Integer.valueOf(item1[10]) + Integer.valueOf(item6[10]) + Integer.valueOf(item9[10]) + Integer.valueOf(item10[10]);
			
				}else { // if SET is OFF, then put to 0;
					// System.out.println("==> 0x"); 
				SBITEMdmgbonus = 0; 
				SBITEMstr = 0;
				SBITEMdex = 0;
				SBITEMvit = 0;
				SBITEMint = 0;
				SBITEMagi = 0;
				SBITEMatk = 0;
				SBITEMdef = 0;
				SBITEMlife = 0;
				SBITEMmana = 0;
				}
				
				
				
				// NORMAL CHAR STATS \\
			ITEMstr =Integer.valueOf(item0[11]) + Integer.valueOf(item1[11]) + Integer.valueOf(item2[11]) + Integer.valueOf(item3[11]) + Integer.valueOf(item4[11]) + 
					Integer.valueOf(item5[11]) + Integer.valueOf(item6[11]) + Integer.valueOf(item7[11]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[11]) + 
					Integer.valueOf(item10[11]) + Integer.valueOf(item11[11]) + Integer.valueOf(item12[11]) + Integer.valueOf(item13[11]) + Integer.valueOf(item14[11]) + 
					Integer.valueOf(item15[11]) + Integer.valueOf(item16[11])+ SBITEMstr;		
				
				
			ITEMdex = Integer.valueOf(item0[12]) + Integer.valueOf(item1[12]) + Integer.valueOf(item2[12]) + Integer.valueOf(item3[12]) + Integer.valueOf(item4[12]) + 
					Integer.valueOf(item5[12]) + Integer.valueOf(item6[12]) + Integer.valueOf(item7[12]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[12]) + 
					Integer.valueOf(item10[12]) + Integer.valueOf(item11[12]) + Integer.valueOf(item12[12]) + Integer.valueOf(item13[12]) + Integer.valueOf(item14[12]) + 
					Integer.valueOf(item15[12]) + Integer.valueOf(item16[12]) + SBITEMdex;		
				
		    ITEMvit = Integer.valueOf(item0[13]) + Integer.valueOf(item1[13]) + Integer.valueOf(item2[13]) + Integer.valueOf(item3[13]) + Integer.valueOf(item4[13]) + 
					Integer.valueOf(item5[13]) + Integer.valueOf(item6[13]) + Integer.valueOf(item7[13]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[13]) + 
					Integer.valueOf(item10[13]) + Integer.valueOf(item11[13]) + Integer.valueOf(item12[13]) + Integer.valueOf(item13[13]) + Integer.valueOf(item14[13]) + 
					Integer.valueOf(item15[13]) + Integer.valueOf(item16[13]) + SBITEMvit;		
				
			ITEMint = Integer.valueOf(item0[14]) + Integer.valueOf(item1[14]) + Integer.valueOf(item2[14]) + Integer.valueOf(item3[14]) + Integer.valueOf(item4[14]) + 
					Integer.valueOf(item5[14]) + Integer.valueOf(item6[14]) + Integer.valueOf(item7[14]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[14]) + 
					Integer.valueOf(item10[14]) + Integer.valueOf(item11[14]) + Integer.valueOf(item12[14]) + Integer.valueOf(item13[14]) + Integer.valueOf(item14[14]) + 
					Integer.valueOf(item15[14]) + Integer.valueOf(item16[14]) + SBITEMint;		
				
			ITEMagi = Integer.valueOf(item0[15]) + Integer.valueOf(item1[15]) + Integer.valueOf(item2[15]) + Integer.valueOf(item3[15]) + Integer.valueOf(item4[15]) + 
					Integer.valueOf(item5[15]) + Integer.valueOf(item6[15]) + Integer.valueOf(item7[15]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[15]) + 
					Integer.valueOf(item10[15]) + Integer.valueOf(item11[15]) + Integer.valueOf(item12[15]) + Integer.valueOf(item13[15]) + Integer.valueOf(item14[15]) + 
					Integer.valueOf(item15[15]) + Integer.valueOf(item16[15]) + SBITEMagi;		
			

			ITEMatk = Integer.valueOf(item0[16]) + Integer.valueOf(item1[16]) + Integer.valueOf(item2[16]) + Integer.valueOf(item3[16]) + Integer.valueOf(item4[16]) + 
					Integer.valueOf(item5[16]) + Integer.valueOf(item6[16]) + Integer.valueOf(item7[16]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[16]) + 
					Integer.valueOf(item10[16]) + Integer.valueOf(item11[16]) + Integer.valueOf(item12[16]) + Integer.valueOf(item13[16]) + Integer.valueOf(item14[16]) + 
					Integer.valueOf(item15[16]) + Integer.valueOf(item16[16])  + Integer.valueOf(item7[21]) + SBITEMdmgbonus + SBITEMatk;	
			
			ITEMdef = Integer.valueOf(item0[17]) + Integer.valueOf(item1[17]) + Integer.valueOf(item2[17]) + Integer.valueOf(item3[17]) + Integer.valueOf(item4[17]) + 
					Integer.valueOf(item5[17]) + Integer.valueOf(item6[17]) + Integer.valueOf(item7[17]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[17]) + 
					Integer.valueOf(item10[17]) + Integer.valueOf(item11[17]) + Integer.valueOf(item12[17]) + Integer.valueOf(item13[17]) + Integer.valueOf(item14[17]) + 
					Integer.valueOf(item15[17]) + Integer.valueOf(item16[17]) + SBITEMdef;	
			
		    ITEMlife = Integer.valueOf(item0[18]) + Integer.valueOf(item1[18]) + Integer.valueOf(item2[18]) + Integer.valueOf(item3[18]) + Integer.valueOf(item4[18]) + 
					Integer.valueOf(item5[18]) + Integer.valueOf(item6[18]) + Integer.valueOf(item7[18]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[18]) + 
					Integer.valueOf(item10[18]) + Integer.valueOf(item11[18]) + Integer.valueOf(item12[18]) + Integer.valueOf(item13[18]) + Integer.valueOf(item14[18]) + 
					Integer.valueOf(item15[18]) + Integer.valueOf(item16[18])+ SBITEMlife;
			
			ITEMmana = Integer.valueOf(item0[19]) + Integer.valueOf(item1[19]) + Integer.valueOf(item2[19]) + Integer.valueOf(item3[19]) + Integer.valueOf(item4[19]) + 
					Integer.valueOf(item5[19]) + Integer.valueOf(item6[19]) + Integer.valueOf(item7[19]) /*+ rs8.getInt("STR")*/ + Integer.valueOf(item9[19]) + 
					Integer.valueOf(item10[19]) + Integer.valueOf(item11[19]) + Integer.valueOf(item12[19]) + Integer.valueOf(item13[19]) + Integer.valueOf(item14[19]) + 
					Integer.valueOf(item15[19]) + Integer.valueOf(item16[19]) + SBITEMmana;	
				
			
			// check famepad itemid for buff. Def Rate | Crit Rate | Crit Dmg Absorb 
			if(this.getequipSLOT(14) == 221111013){
				FPDefRate = .06;
				FPCritRate = 2;
				FPCritAbsorb = .06;
			}else
			if(this.getequipSLOT(14) == 221112013){
				FPDefRate = .06;
				FPCritRate = 2;
				FPCritAbsorb = .06;
			}else
			if(this.getequipSLOT(14) == 221111014){
				FPDefRate = .07;
				FPCritRate = 3;
				FPCritAbsorb = .07;
			}else
			if(this.getequipSLOT(14) == 221112014){
				FPDefRate = .07;
				FPCritRate = 3;
				FPCritAbsorb = .07;
			}else
			if(this.getequipSLOT(14) == 221111015){
				FPDefRate = .08;
				FPCritRate = 4;
				FPCritAbsorb = .08;
			}else
			if(this.getequipSLOT(14) == 221112015){
				FPDefRate = .08;
				FPCritRate = 4;
				FPCritAbsorb = .08;
			}else
			if(this.getequipSLOT(14) == 221111016){
				FPDefRate = .10;
				FPCritRate = 5;
				FPCritAbsorb = .10;
			}else
			if(this.getequipSLOT(14) == 221112016){
				FPDefRate = .10;
				FPCritRate = 5;
				FPCritAbsorb = .10;
			}else
			if(this.getequipSLOT(14) == 221111017){
				FPDefRate = .11;
				FPCritRate = 6;
				FPCritAbsorb = .11;
			}else
			if(this.getequipSLOT(14) == 221112017){
				FPDefRate = .11;
				FPCritRate = 6;
				FPCritAbsorb = .11;
			}else
			if(this.getequipSLOT(14) == 221111018){
				FPDefRate = .12;
				FPCritRate = 7;
				FPCritAbsorb = .12;
			}else
			if(this.getequipSLOT(14) == 221112018){
				FPDefRate = .12;
				FPCritRate = 7;
				FPCritAbsorb = .12;
			}else
			if(this.getequipSLOT(14) == 221111019){
				FPDefRate = .13;
				FPCritRate = 8;
				FPCritAbsorb = .13;
			}else
			if(this.getequipSLOT(14) == 221112019){
				FPDefRate = .13;
				FPCritRate = 8;
				FPCritAbsorb = .13;
			}else
			if(this.getequipSLOT(14) == 221111020){
				FPDefRate = .14;
				FPCritRate = 9;
				FPCritAbsorb = .14;
			}else
			if(this.getequipSLOT(14) == 221112020){
				FPDefRate = .14;
				FPCritRate = 9;
				FPCritAbsorb = .14;
			}else
			if(this.getequipSLOT(14) == 221111021){
				FPDefRate = .15;
				FPCritRate = 10;
				FPCritAbsorb = .15;
			}else
			if(this.getequipSLOT(14) == 221112021){
				FPDefRate = .15;
				FPCritRate = 10;
				FPCritAbsorb = .15;
			}else{
				FPDefRate = 0;
				FPCritRate = 0;
				FPCritAbsorb = 0;
			}
			
			
			
				
			 //System.out.println("FPDefRate = "+ FPDefRate );
			 //System.out.println("FPCritRate = "+ FPCritRate );
			 //System.out.println("FPCritAbsorb = "+ FPCritAbsorb );
			
		/*	 System.out.println("ITEMatk = "+ ITEMatk );
			 System.out.println("ITEMdef = "+ ITEMdef );
			 System.out.println("ITEMlife = "+ ITEMlife );
			 System.out.println("ITEMmana = "+ ITEMmana );
			 System.out.println("ITEMstr = "+ ITEMstr );
			 System.out.println("ITEMdex = "+ ITEMdex );
			 System.out.println("ITEMvit = "+ ITEMvit );
			 System.out.println("ITEMint = "+ ITEMint );
			 System.out.println("ITEMagi = "+ ITEMagi );*/
			// System.out.println("......done!");
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		return null;
	}

	// stats list ( Calculates everything ) \\
	public void statlist() {
		
	if(this.SAP == 1){this.Sap = 60;}    // if has SAP then sap value = 60
	else 			 {this.Sap = 0; }    // else value = 0

	if(this.NEW_STATUS_SUNDAN == 1){this.New_Status_Sundan = 10;} 
	else{this.New_Status_Sundan = 0;}
	
	  
	int STR = this.strength  + New_Status_Sundan + ITEMstr;	
	double strength_life = 2.19 * STR;	
	double strength_mana = 1.40 * STR;
	double strength_stamina = 1.88 * STR;
	double strength_attack = 0.53 * STR;
	double strength_defense = 0.32 * STR;
		
	int DEX = this.dextery  + New_Status_Sundan + ITEMdex;	
	double dextery_life = 2.40 * DEX;
	double dextery_mana = 1.70 * DEX;
	double dextery_stamina = 1.30 * DEX;
	double dextery_attack = 0.50 * DEX;
	double dextery_defense = 0.34 * DEX;
	
	int VIT = this.vitality  + New_Status_Sundan + ITEMvit;	
	double vitality_life = 2.49* VIT; 
	double vitality_mana = 1.51* VIT; 
	double vitality_stamina = 1.51* VIT; 
	double vitality_attack = 0.44* VIT; 
	double vitality_defense = 0.58 * VIT; 
	
	int INT = this.intelligence  + New_Status_Sundan + ITEMint;	
	double intelligence_life = 1.60 * INT;
	double intelligence_mana = 3.49 * INT;
	double intelligence_stamina = 1.70 * INT;
	double intelligence_attack = 0.25 * INT;
	double intelligence_defense = 0.25 * INT;
	
	int AGI = this.agility  + New_Status_Sundan + ITEMagi;	
	double agility_life = 1.50 * AGI;
	double agility_mana = 1.50 * AGI;
	double agility_stamina = 1.30 * AGI;
	double agility_attack = 0.25 * AGI;
	double agility_defense = 0.45 * AGI;

	
	double newmaxhp = strength_life + dextery_life + vitality_life + intelligence_life +  agility_life;
	double newmaxmana = strength_mana + dextery_mana + vitality_mana + intelligence_mana +  agility_mana;
	double newmaxstamina = strength_stamina + dextery_stamina + vitality_stamina + intelligence_stamina +  agility_stamina;
	double newattack = strength_attack + dextery_attack + vitality_attack + intelligence_attack +  agility_attack;
	double newdefence = strength_defense + dextery_defense + vitality_defense + intelligence_defense +  agility_defense;
	
	// add extra Offence and Defence depending on level
	double Offence = 0; 
	double Defence = 0; 
	for(int i=1;i<=this.getLevel();i++){
	Offence = Offence + 0.37; // + 3 per 5 levels
	Defence = Defence + 0.53; // + 2.45 per 5 levels	
	}
	
	//System.out.println("WTF Offence = "+ Offence);
	//System.out.println("WTF Defence = "+ Defence);
	int hpz = 0;
	if(bufficon1 == 99|| bufficon2 == 99){/*System.out.println("hp cap");*/ hpz = 500;}
	this.maxhp = (int)newmaxhp + 30 + ITEMlife + this.gettempstore(7) + this.getTempPassives(1) + this.getTempPassives(100)+ hpz;
	int manaz = 0;
	if(bufficon1 == 98|| bufficon2 == 98){/*System.out.println("mana cap");*/ manaz = 500;}
	this.maxmana = (int)newmaxmana + 30 + ITEMmana + this.getTempPassives(9) + manaz;
	this.setMaxstamina((int)newmaxstamina + 30);
	this.attack = (int)newattack + (int)Offence + this.Sap + ITEMatk + this.gettempstore(15) + this.getTempPassives(2);
	this.defence = (int)newdefence + (int)Defence + ITEMdef + this.gettempstore(12) + this.gettempstore(16) + this.getTempPassives(4) + this.getTempPassives(10);		
	if(bufficon1 == 97|| bufficon2 == 97){/*System.out.println("offence");*/double ATK = this.defence * 1.05;  this.defence = (int)ATK;}
	
	if(this.FAD == 1){double ATK = this.attack * 1.25;  this.attack = (int)ATK;}  
	if(bufficon1 == 96|| bufficon2 == 96){/*System.out.println("deffence");*/double ATK = this.attack * 1.05;  this.attack = (int)ATK;}
     /*System.out.println("maxhp = "+ this.maxhp );
	 System.out.println("maxmana  = "+ this.maxmana  );
	 System.out.println("maxstamina  = "+ this.maxstamina);
	 System.out.println("attack  = "+ this.attack );
	 System.out.println("defence  = "+ this.defence); */
	
	this.Sap = 0; this.New_Status_Sundan = 0;
	// ITEMatk = 0;  ITEMdef = 0; 		
	}
	
	public void recMana(int mana) {
		////System.out.println("<==Rec Mana!==>");
		
		byte[] cid = BitTools.intToByteArray(this.getCharID());
		byte[] hp = BitTools.intToByteArray(this.hp);
		byte[] newmana = BitTools.intToByteArray(mana);
		byte[] stam = BitTools.intToByteArray(this.stamina);
		
		byte[] healpckt = new byte[32];
		healpckt[0] = (byte)healpckt.length;
		healpckt[4] = (byte)0x05;
		healpckt[6] = (byte)0x35;
		healpckt[8] = (byte)0x08; 
		healpckt[9] = (byte)0x60; 
		healpckt[10] = (byte)0x22;
		healpckt[11] = (byte)0x45;
		
		healpckt[24] = hp[0];
		healpckt[25] = hp[1];
		
		healpckt[28] = newmana[0];
		healpckt[29] = newmana[1];
		
		healpckt[30] = stam[0];
		healpckt[31] = stam[1];			
	
	
	healpckt[16] = (byte)0x03;
	healpckt[18] = (byte)0x02;
	
	for(int i=0;i<4;i++) {
		healpckt[12+i] = cid[i];
	}
	
	ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), healpckt);
	
	}
	
	public void setMana(int mana) {
		if(mana <= 0){this.mana = 0;}else
		if(mana > this.maxmana){this.mana = this.maxmana;}
		else{
		this.mana = mana;
		}
	}
	
	public void setStamina(int stamina) {
		if(stamina > this.getMaxstamina()){this.stamina = this.getMaxstamina();}
		else{
		this.stamina = stamina;
		}
	}
	
	// die
	public void setHp(int hp) {
		if(hp > this.maxhp){this.hp = this.maxhp;}
		else{	
		this.hp = hp;
		
		if(hp <= 0) {	
		if(this.Duel != 1){
		byte[] cid = BitTools.intToByteArray(this.getCharID());
		byte[] healpckt = new byte[20];
		healpckt[0] = (byte)0x14;
		healpckt[4] = (byte)0x05;
		healpckt[6] = (byte)0x0a;
		healpckt[8] = (byte)0x01; 
		
		for(int i=0;i<4;i++) {
			healpckt[12+i] = cid[i];
		}
			this.TempStoreBuffs.clear(); // remove buffs when died.
			this.BuffPercentage.clear();
			this.vpz(cid, false);
			ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), healpckt); 
			this.sendToMap(healpckt);
		}else{this.Duel = 0; this.setHp(1);}}
		}
	}
	
	// 
	public void ExpirationDate() {
		Iterator<Entry<Integer, Long>> iter = this.item_end_date.entrySet().iterator();
		Integer Itemid;
		Long time_end_date;
		while(iter.hasNext()) {
			Map.Entry<Integer, Long> pairs = iter.next();
		      Itemid = pairs.getKey(); time_end_date = pairs.getValue();
			 if (System.currentTimeMillis() > time_end_date ){ 
			 while(this.CargoSLOT.containsValue(Itemid)){ 
				 int Key = BitTools.ValueToKey(Itemid, this.CargoSLOT); // get Key from InventorySLOT by Value
				 this.CargoSLOT.remove(Key);
				 this.CargoWEIGHT.remove(Key);
				 this.CargoHEIGHT.remove(Key);
				 this.CargoSTACK.remove(Key);
			 }
			 while(this.EquipSLOT.containsValue(Itemid)){
				 int Key = BitTools.ValueToKey(Itemid, this.EquipSLOT); // get Key from InventorySLOT by Value
				 this.EquipSLOT.remove(Key);
			 } 
		   }
	    }
	}
	
	
	public void recgold(int addgold) {
		if(addgold <= 0){return;}
		//System.out.println("Putting gold");
		byte[] chid = BitTools.intToByteArray(this.getCharID());
		
		long newgold = this.getgold()  + addgold;
		this.setgold(newgold);
		byte[] gold = BitTools.intToByteArray(addgold);
		byte[] partychat = new byte[40];
		partychat[0] = (byte)0x28;
		partychat[4] = (byte)0x04;
		partychat[6] = (byte)0x0f;
		partychat[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			partychat[12+i] = chid[i]; 
		}
		
		for(int i=0;i<4;i++) {
			partychat[36+i] = gold[i]; 
		}
		
		partychat[16] = (byte)0x01;

		partychat[18] = (byte)0x75;
		partychat[19] = (byte)0x2e;
		partychat[20] = (byte)0xff;//inv slot 255
		
		partychat[24] = (byte)0xf0; 
		
		partychat[27] = (byte)0x2a;
		
		partychat[32] = (byte)0x35;
		partychat[33] = (byte)0x2a;
		partychat[34] = (byte)0xef;
		partychat[35] = (byte)0x0c;
		ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), partychat);
		//System.out.println("DONE");
	}
	
	public void AddToTheirRespawn(int CharID) {
		Iterator<Integer> it = this.iniPackets.keySet().iterator();
			   		Integer tmp = null;
			   		while(it.hasNext()){
			   			tmp = it.next();
			   			if (this.wmap.CharacterExists(tmp) && tmp != this.getuid()){
			   				Character t = this.wmap.getCharacter(tmp);
			   				// also check range
			   				if(WMap.distance(t.getlastknownX(), t.getlastknownY(), this.getlastknownX(), this.getlastknownY()) < 325 && ServerFacade.getInstance().getConnectionByChannel(t.GetChannel()) != null){
			   				//System.out.println(this.getCharID()+" Adding respawn request to "+t.getCharID());
			   				t.Respawn.put(CharID,CharID);	
			   				}
			   			}else {
			   				it.remove();
			    			}
			    		}
	 this.RespawnCD = System.currentTimeMillis();
	 }
	
	public void updateLocation(float x, float y){
		//if (this.timer != null){ if (!this.timer.isCompleted()) this.timer.cancel(); }
		//if (WMap.distance(x, y, this.getlastknownX(), this.getlastknownY()) > this.syncDistance){
			//this.activesyncTest(this.getlastknownX(), this.getlastknownY(), x, y);
		//}
		//this.lastloc.setX(this.getlastknownX());
		//this.lastloc.setY(this.getlastknownY());
		
		//if (WMap.distance(x, y, this.getlastknownX(), this.getlastknownY()) < 35){
	
		if(!this.Respawn.isEmpty()){
			if(System.currentTimeMillis() - this.RespawnCD < 30000){
			Iterator<Integer> it = this.Respawn.keySet().iterator();
	   		Integer tmp = null;
	   		while(it.hasNext()){
	   			tmp = it.next();
	   			if (this.wmap.CharacterExists(tmp) && tmp != this.getuid()){
	   				Character t = this.wmap.getCharacter(tmp);
	   				if(WMap.distance(t.getlastknownX(), t.getlastknownY(), this.getlastknownX(), this.getlastknownY()) < 325 && ServerFacade.getInstance().getConnectionByChannel(t.GetChannel()) != null){
	   				//System.out.println(this.getCharID()+" Spawns to "+t.getCharID());
	   				this.sendInit(t.getCharID());
	   				}
	   			}else {
	   				it.remove();
	    			}
	    		}
	   		}	
		this.Respawn.clear();
		}

		
		
		this.setX(x);
		this.setY(y);
		try{
		Area t = this.grid.update(this);
		if (t != null){
			if (t != this.area){
				this.area.moveTo(this, t);
				this.area = t;
				   ConcurrentMap<Integer, Integer> ls;
				   	 ls = this.area.addMemberAndGetMembers(this);
				   	Iterator<Integer> it = this.iniPackets.keySet().iterator();
				   		 while (it.hasNext()){
				   			   Integer i = it.next();
				   			    if (!ls.containsKey(i)){
				   			    		it.remove();
				   			    		//System.out.println(this.charID + " removed player: " + i);
				   			    		ServerFacade.getInstance().addWriteByChannel(this.wmap.getCharacter(i).GetChannel(), this.getVanishByID(this.charID));
				   			    	}
				   			    else
				   			    if (ls.containsKey(i)){
			   			    		//System.out.println(this.charID + " has player: " + i);
			   			    	}
				   			    }
				   			    //ls.removeAll(iniPackets);
				   			    this.removeAll(ls, iniPackets);
				   			    this.sendInitToList(ls);
				   			    this.iniPackets.putAll(ls);
				   			}
							} 
		}catch (NullPointerException e) {
			//log.logMessage(Level.SEVERE, this, "updateLocation for "+this.charID +" map:" +this.currentMap + ", did not work, executing joinmap();");
			this.getPlayer().Rarea = true;
		}
		
	}
	
		// remove inipackets from ls
	  public void removeAll(ConcurrentMap<Integer, Integer> ls, ConcurrentMap<Integer, Integer> iniPackets){
		    Iterator<Integer> it = iniPackets.keySet().iterator();
		     while (it.hasNext()){
		    	 Integer player = it.next();
		    	if(ls.containsKey(player)){
		    	  ls.remove(player);
		    	}

			 }
	  }
	
		private void sendInitToList(ConcurrentMap<Integer, Integer> ls) {
			Iterator<Integer> it = ls.keySet().iterator();
				   		Integer tmp = null;
				   		while(it.hasNext()){
				   			tmp = it.next();
				   			if (this.wmap.CharacterExists(tmp) && tmp != this.getuid() && this.HIDING == 0){
				   				Character t = this.wmap.getCharacter(tmp);
				   				ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.extCharPacket());
				   				
				   				//if guildwarthen activate pvp enable
				   				if(Guildwar.getInstance().isguildwar()){ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.PvPActive());}
								
								  // if both guild is at least grade 10 or lower
								if(this.guild != null && t.guild != null && t.guild.guildtype != 1){ 
									if(this.guild.War.containsKey(t.guild.guildname)){	
									ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.PvPActive());
									}
								}else // if both is not in guild
									if(this.guild == null && t.guild == null){
									ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.PvPActive());
									}
							

				   				    if (this.guild != null && this.guild.getGuildtype() != 0){
				   				    	ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.extCharGuild());
				   				    }
				   				    if (this.BoothName != null && this.BoothStance != 0){
				   				    	ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.extCharBooth(0));
				   				    }
				   				    if(this.Running == 1){
				   				    ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.Runnskill());
				   				    }
				   			}
				   			else {
				   				it.remove();
				    			}
				    		}
		
		 }

				   
				   
				
	// this method should only be called by active sync timer
	public void syncLocation(float x, float y){
		this.setX(x);
		this.setY(y);
			Area t = this.grid.update(this);
			if (t != null){
			if (t != this.area){
				this.area.moveTo(this, t);
				this.area = t;
				this.area.addMember(this);
			}
			} 	//ServerFacade.getInstance().finalizeConnection(this.GetChannel());
	}
	
	
	@SuppressWarnings("unused")
	private void activesyncTest(float x, float y, float dx, float dy) {
		float deltax = WMap.distance(x, dx);
		float deltay = WMap.distance(y, dy);
		float hyp = WMap.distance(x, y, dx, dy);
		float sina = deltay / hyp;
		float cosa = deltax / hyp;
		float mpx = 1;
		float mpy = 1;
		float done = this.syncDistance;
		long period = (long)( (hyp / this.speed) * 1000);
		long delay = (long)((this.syncDistance / this.speed) * 1000);
		List<Waypoint> wplist = new ArrayList<Waypoint>();
		Waypoint wp = null;
		
		if (dx < x) mpx = -1;
		if (dy < y) mpy = -1;
		if (cosa == 1) cosa = 0;
		if (sina == 1) sina = 0;
		
		//System.out.println("Estimated total time to travel: " + period +"ms Interval time" +  delay + "ms");
		System.out.println("Start coords x: " + x + " y: " +y);
		for (done = this.syncDistance; done < hyp; done += this.syncDistance){
			x += mpx * (cosa * this.syncDistance);
			y += mpy * (sina * this.syncDistance);
			wp = new Waypoint(x,y);
			wplist.add(wp);
			//System.out.println("inteval coords x: " + x + " y:" + y);
		}
		this.timer = new MoveSyncTimer(wplist, this);
		SystemTimer.getInstance().addTask(this.timer, delay, delay);
		System.out.println("end coords x: " + dx + " y: " +dy);
	}
	
	

	/*
	 * Holy mother of a monster packet.
	 * Format a character data packet for this character based on it's attributes.
	 */
		//Internal char data packet	
	public byte[] initCharPacket() {
		this.ExpirationDate();
		this.RockCityBoi();
		this.fametopcheck();
        byte[] cdata = new byte[653];
        byte[] charname = this.getName().getBytes();
        
        for(int i=0;i<charname.length;i++) {
                cdata[i] = charname[i];
        }
        
        byte[] stuff = new byte[] { (byte)0x00, (byte)0xa0, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x7c, (byte)0x01, (byte)0x00,                               
            	(byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, 
     /*X*/   	(byte)0xf3, (byte)0xd4, (byte)0x13,(byte)0xc5, (byte)0x9a,        
     /*Y*/   	(byte)0xb9, (byte)0xbd, (byte)0xc5, (byte)0x00, (byte)0x00,                
     			(byte)0x02, (byte)0x00 }; 
    
 
        for(int i=0;i<stuff.length;i++) {
            cdata[i+66-8-3] = stuff[i];    
        }
        
        if(this.getCharacterClass() == 2) {
                cdata[40] = 0x02;       //gender byte(2 = female(yeah.. don't ask me why CRS add gender byte in their packet))
                cdata[48] = 0x02;       //sin class is 2
        } else {
        		cdata[40] = (byte)this.model; // model ID (only works for MALE [ not assassin])                
        		cdata[48] = (byte)this.characterClass; //class byte
        }
        
        // guild
		if (this.guild != null && this.guild.getGuildtype() != 0){
        byte[] guildicon = BitTools.intToByteArray(this.guild.guildicon); // guild ID ?? (or icon)
        for(int i=0;i<2;i++){
        cdata[i+50] = guildicon[i]; 
        }
        int Key = BitTools.ValueToKey(this.getCharID(), this.guild.guildids); // how to get it without conquerent exception
        cdata[52] = (byte)this.guild.getguildranks(Key); // guild something
		}
        cdata[54] = (byte)this.getLevel();
        
		//byte[] hp = BitTools.intToByteArray(this.getHp());  
		//byte[] mana = BitTools.intToByteArray(this.getMana()); 
		//byte[] stam = BitTools.intToByteArray(this.getStamina()); 
		
    
    
            
        			// ITEMS: Equipment                 
        // Head        
        cdata[78] = (byte)0x01;        
        cdata[79] = (byte)0x06;        
        byte[] head = BitTools.intToByteArray(this.getequipSLOT(0));        for(int i=0;i<4;i++) { cdata[i+80] = head[i]; }       
		if(this.item_end_date.containsKey(this.getequipSLOT(0))){	
		cdata[80-3] = (byte)0xff; 
		long timedate = this.item_end_date.get(this.getequipSLOT(0)) / 1000;
		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
		for(int w=0;w<4;w++){ cdata[84+w] = TimenDate[w]; }
		}else{ cdata[84] = (byte)0x01; // End }
		}
		
        // Neck       
        cdata[90] = (byte)0x01; // <- somekind of chest indicator for warrior      
        cdata[91] = (byte)0x05; // Equipment SLOT i think        
        byte[] neck = BitTools.intToByteArray(this.getequipSLOT(1));        for(int i=0;i<4;i++) { cdata[i+92] = neck[i]; }        
		if(this.item_end_date.containsKey(this.getequipSLOT(1))){	
		cdata[92-3] = (byte)0xff; 
		long timedate = this.item_end_date.get(this.getequipSLOT(1)) / 1000;
		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
		for(int w=0;w<4;w++){ cdata[96+w] = TimenDate[w]; }
		}else{ cdata[96] = (byte)0x01; // End }                     
		}
		
        // Cape       
        cdata[102] = (byte)0x0d;    
        cdata[103] = (byte)0x06;  
        byte[] cape = BitTools.intToByteArray(this.getequipSLOT(2));        for(int i=0;i<4;i++) { cdata[i+104] = cape[i]; }  
		if(this.item_end_date.containsKey(this.getequipSLOT(2))){	
		cdata[104-3] = (byte)0xff; 
		long timedate = this.item_end_date.get(this.getequipSLOT(2)) / 1000;
		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
		for(int w=0;w<4;w++){ cdata[108+w] = TimenDate[w]; }
		}else{ cdata[108] = (byte)0x01; // End }
		}
		
        // Chest      
        cdata[114] = (byte)0x02; // <- somekind of chest indicator for warrior        
        cdata[115] = (byte)0x04;         
        byte[] jacket = BitTools.intToByteArray(this.getequipSLOT(3));     for(int i=0;i<4;i++) { cdata[i+116] = jacket[i]; }    
        //cdata[116] = (byte)0x97; //---|      
        //cdata[117] = (byte)0xf4; //---|   
        //cdata[118] = (byte)0x0b;      |---- is  chest ID    
        //cdata[119] = (byte)0x0c; //---|      
        if(this.item_end_date.containsKey(this.getequipSLOT(3))){	
    		cdata[116-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(3)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[120+w] = TimenDate[w]; }
    		}else{ cdata[120] = (byte)0x01; // End }           
    		}
        // Pants      
        cdata[126] = (byte)0x03;      
        cdata[127] = (byte)0x02;    
        byte[] pants = BitTools.intToByteArray(this.getequipSLOT(4));        for(int i=0;i<4;i++) { cdata[i+128] = pants[i]; }        
        if(this.item_end_date.containsKey(this.getequipSLOT(4))){	
    		cdata[128-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(4)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[132+w] = TimenDate[w]; }
    		}else{ cdata[132] = (byte)0x01; // End }           
    		}
        
        // Armor     
        cdata[138] = (byte)0x0a;    
        cdata[139] = (byte)0x02;      
        byte[] armor = BitTools.intToByteArray(this.getequipSLOT(5));        for(int i=0;i<4;i++) { cdata[i+140] = armor[i]; }        
        if(this.item_end_date.containsKey(this.getequipSLOT(5))){	
    		cdata[140-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(5)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[144+w] = TimenDate[w]; }
    		}else{ cdata[144] = (byte)0x01; // End }           
    		}
        // Bracer       
        cdata[151] = (byte)0x05;     
        byte[] bracer = BitTools.intToByteArray(this.getequipSLOT(6));      for(int i=0;i<4;i++) { cdata[i+152] = bracer[i]; }      
        if(this.item_end_date.containsKey(this.getequipSLOT(6))){	
    		cdata[152-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(6)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[156+w] = TimenDate[w]; }
    		}else{ cdata[156] = (byte)0x01; // End }           
    		}               

        // Primary Weapon        
        cdata[162] = (byte)0x0a; // <-- weapon indicator      
        byte[] primaryweapon = BitTools.intToByteArray(this.getequipSLOT(7));  for(int i=0;i<4;i++) { cdata[i+164] = primaryweapon[i]; } 
        if(this.item_end_date.containsKey(this.getequipSLOT(7))){	
    		cdata[164-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(7)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[168+w] = TimenDate[w]; }
    		}else{ cdata[168] = (byte)0x01; // End }           
    		}
        
     // secondweapon     
    	byte[] secondweapon = BitTools.intToByteArray(this.getequipSLOT(8));  
       for(int i=0;i<4;i++) { cdata[i+176] = secondweapon[i]; } 
        if(this.item_end_date.containsKey(this.getequipSLOT(8))){	
    		cdata[176-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(8)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[180+w] = TimenDate[w]; }
    		}else{ cdata[180] = (byte)0x01; // End }           
    		} 
       
        
        // Ring 1    
     if(this.getequipSLOT(9) != 0){
 	  // System.out.println("yes this.getequipSLOT(9) : "+this.getequipSLOT(9));
        cdata[186] = (byte)0x02;     
        cdata[187] = (byte)0x06;       
        byte[] ring1 = BitTools.intToByteArray(this.getequipSLOT(9));        for(int i=0;i<4;i++) { cdata[i+188] = ring1[i]; }      
        if(this.item_end_date.containsKey(this.getequipSLOT(9))){	
    		cdata[188-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(9)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[192+w] = TimenDate[w]; }
    		}else{ cdata[192] = (byte)0x01; // End }           
    		}  
    	}else{
    			 // warrior
    		if(this.characterClass == 1){
    	    this.setequipSLOT(9, 208114101);
    		}else// assassin
    		if(this.characterClass == 2){
    	    this.setequipSLOT(9, 208224101);   		
        	}else// mage
        	if(this.characterClass == 3){
           	this.setequipSLOT(9, 208134101); 			
            }else// monk
            if(this.characterClass == 4){
           	this.setequipSLOT(9, 208144101);  		
        	}
        				
          byte[] secondweaponz = BitTools.intToByteArray(this.getequipSLOT(9));  for(int i=0;i<4;i++) { cdata[i+188] = secondweaponz[i]; } 	
     	 //System.out.println(" nop this.getequipSLOT(9) : "+this.getequipSLOT(9));
         cdata[180] = (byte)0x01; 
      }
        
        // Ring 2      
        cdata[199] = (byte)0x06;     
        byte[] ring2 = BitTools.intToByteArray(this.getequipSLOT(10));        for(int i=0;i<4;i++) { cdata[i+200] = ring2[i]; }      
        if(this.item_end_date.containsKey(this.getequipSLOT(10))){	
    		cdata[200-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(10)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[204+w] = TimenDate[w]; }
    		}else{ cdata[204] = (byte)0x01; // End }           
    		}                
        
        // Shoes      
        cdata[210] = (byte)0x03;   
        cdata[211] = (byte)0x06;    
        byte[] shoes = BitTools.intToByteArray(this.getequipSLOT(11));        for(int i=0;i<4;i++) { cdata[i+212] = shoes[i]; }    
        if(this.item_end_date.containsKey(this.getequipSLOT(11))){	
    		cdata[212-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(11)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[216+w] = TimenDate[w]; }
    		}else{ cdata[216] = (byte)0x01; // End }           
    		}              
        
        byte[] medal1 = BitTools.intToByteArray(this.getequipSLOT(12));        for(int i=0;i<4;i++) { cdata[i+224] = medal1[i]; }    
        if(this.item_end_date.containsKey(this.getequipSLOT(12))){	
    		cdata[224-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(12)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[228+w] = TimenDate[w]; }
    		}else{ cdata[228] = (byte)0x01; // End }           
    		}
        
        byte[] medal2 = BitTools.intToByteArray(this.getequipSLOT(13));        for(int i=0;i<4;i++) { cdata[i+236] = medal2[i]; }    
        if(this.item_end_date.containsKey(this.getequipSLOT(13))){	
    		cdata[236-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(13)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[240+w] = TimenDate[w]; }
    		}else{ cdata[240] = (byte)0x01; // End }           
    		}
        
        byte[] medal3 = BitTools.intToByteArray(this.getequipSLOT(14));        for(int i=0;i<4;i++) { cdata[i+248] = medal3[i]; }    
        if(this.item_end_date.containsKey(this.getequipSLOT(14))){	
    		cdata[248-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(14)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[252+w] = TimenDate[w]; }
    		}else{ cdata[252] = (byte)0x01; // End }           
    		}
        
        // Mount       
        cdata[259] = (byte)0x01;       
        byte[] mount = BitTools.intToByteArray(this.getequipSLOT(15));        for(int i=0;i<4;i++) { cdata[i+260] = mount[i]; }     
        if(this.item_end_date.containsKey(this.getequipSLOT(15))){	
    		cdata[260-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(15)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[264+w] = TimenDate[w]; }
    		}else{ cdata[264] = (byte)0x01; // End }           
    		}
        
        //bird
        byte[] bird = BitTools.intToByteArray(this.getequipSLOT(16));        for(int i=0;i<4;i++) { cdata[i+272] = bird[i]; }    
        if(this.item_end_date.containsKey(this.getequipSLOT(16))){	
    		cdata[272-3] = (byte)0xff; 
    		long timedate = this.item_end_date.get(this.getequipSLOT(16)) / 1000;
    		byte[] TimenDate = BitTools.intToByteArray((int)timedate);
    		for(int w=0;w<4;w++){ cdata[276+w] = TimenDate[w]; }
    		}else{ cdata[276] = (byte)0x01; // End }           
    		}
        // End of ITEMS                
        
        
        
         int potslot, poticonid = 380, pottime = 382, potvalue = 384;
		 for(int a=0;a<11;a++){
		 //System.out.println("pots inicharpacket: "+a+" - "+this.getPotIconID(a)+" - "+this.getPotTime(a)+" - "+this.getPotValue(a));
		 if(this.PotSLOT.containsKey(a)){
		// int PotSLOT = this.PotSLOT.get(a); 
		 int PotIconID = this.getPotIconID(a); 
		 int PotTime = this.getPotTime(a); 
		 int PotValue = this.getPotValue(a); 
		 
		 
		// byte[] Potslot = BitTools.intToByteArray(PotSLOT);
		 byte[] Poticonid = BitTools.intToByteArray(PotIconID);
		 byte[] Pottime = BitTools.intToByteArray(PotTime);
		 byte[] Potvalue = BitTools.intToByteArray(PotValue);

			/*for(int w=0;w<2;w++){
				cdata[+w] = Potslot[w]; 
			}*/
			for(int w=0;w<2;w++){
				cdata[poticonid+w] = Poticonid[w]; 
			}
			for(int w=0;w<2;w++){
				cdata[pottime+w] = Pottime[w]; 
			}
			for(int w=0;w<2;w++){
				cdata[potvalue+w] = Potvalue[w]; 
			}
			poticonid = poticonid + 8;
			pottime = pottime + 8;
			potvalue = potvalue + 8;
		 }}
		 
		// cdata[462] = (byte)0x01; 
		// cdata[463] = (byte)0x07; 
		   cdata[464] = (byte)this.fametitle; 

		  
		// cdata[469] = (byte)??; 

        
        for(int i=0;i<16;i++) { cdata[i+17] = (byte)0x30; } // 16x 30 = 0 in ASCII
        
        cdata[34] = (byte)this.faction;//faction       
        byte[] fame = BitTools.intToByteArray(this.fame);  
        for(int i=0;i<4;i++) { cdata[36+i] = fame[i]; }// fame   
        cdata[42] = (byte)this.face; // face   
        cdata[44] = (byte)0x01; //standard its 01 but 00 gives no errors, no explanation yet
        //cdata[52] = (byte)0x02; //standard its 02 but 00 goes as well nothing changes though, no explanation yet
                
        
        
     	byte[] a0 = BitTools.intToByteArray(this.strength);
     	byte[] a1 = BitTools.intToByteArray(this.dextery);
     	byte[] a2 = BitTools.intToByteArray(this.vitality);
     	byte[] a3 = BitTools.intToByteArray(this.intelligence);
     	byte[] a4 = BitTools.intToByteArray(this.agility);
        for(int i=0;i<2;i++) {
          	//cdata[56] = hp[i];
        	//cdata[60] = mana[i];
        	//cdata[68] = stam[i];
        	
        	cdata[i+576] = a0[i]; 
        	cdata[i+578] = a1[i]; 
        	cdata[i+580] = a2[i]; 
        	cdata[i+582] = a3[i]; 
        	cdata[i+584] = a4[i]; 
        }    

        
		/* int MultiPlier = 1;
         long FinalExp = this.exp;
         if(FinalExp > 4294967295L){
		 for(int i=1;i<255;i++){
		 FinalExp = this.exp / i;
		 if(FinalExp <= 4294967295L && i <= 255){FinalExp = this.exp / i+1; MultiPlier = i+1; break;}
		 }}*/
        
	
       // cdata[582] = (byte)0x0c; // EXP % Indicator  ??? IDKK     
       // cdata[583] = (byte)0x02;    
        //cdata[584] = (byte)MultiPlier; // EXP % MULTIPLIER
        byte[] exp = BitTools.LongToByteArrayREVERSE(0);
        //System.out.println("TOTAL EXP: "+  BitTools.byteArrayToInt(exp));
        for(int i=0;i<exp.length;i++){cdata[i+592] = exp[i];} // EXP % (03 is 30.00% on level 1)  

        
        
        byte[] statPoints = BitTools.intToByteArray(this.statPoints);   
        for(int i=0;i<2;i++) { cdata[i+604] = statPoints[i]; }
        
        byte[] skillPoints = BitTools.intToByteArray(this.skillPoints);   
        for(int i=0;i<2;i++) { cdata[i+606] = skillPoints[i]; }          
                
        
        cdata[648] = (byte)this.deletestate; 
        
           this.setCharacterDataPacket(cdata);       
           return cdata;
	}
	
	// send packet buf to all nearby players
	public void RefreshArea() {
			Iterator<Integer> iter = this.iniPackets.keySet().iterator();
				while(iter.hasNext()) {
					Integer plUid = iter.next();               
					
					//if characters
					if (plUid.intValue() != this.charID && this.wmap.CharacterExists(plUid.intValue())){
						Character ch = this.getWmap().getCharacter(plUid.intValue());
						if(ch != null && ch.HIDING == 0) {
							ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), ch.extCharPacket());
							
			   				//if guildwarthen activate pvp enable
			   				if(Guildwar.getInstance().isguildwar()){ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.PvPActive());}
							
							  // if both guild is at least grade 10 or lower
							if(this.guild != null && ch.guild != null && ch.guild.guildtype != 1){ 
								if(this.guild.War.containsKey(ch.guild.guildname)){	
								ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.PvPActive());
								}
							}else // if both is not in guild
								if(this.guild == null && ch.guild == null){
								ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.PvPActive());
								}
						
							if (this.guild != null && this.guild.getGuildtype() != 0){
						    	ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.extCharGuild());
						    }
						    if (this.BoothName != null && this.BoothStance != 0){
						    	ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.extCharBooth(0));
						    }
						    if(this.Running == 1){
						    ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.Runnskill());
						    }
						}
						// else if npcs
					}
				}

		//this.area.sendToMembers(this.getuid(), buf);
	}
	
	// send packet buf to all nearby players
	public void sendToMap(byte[] buf) {
			Iterator<Integer> iter = this.iniPackets.keySet().iterator();
				while(iter.hasNext()) {
					Integer plUid = iter.next();               
					if (plUid.intValue() != this.charID){
						Character ch = this.getWmap().getCharacter(plUid.intValue());
						if(ch != null && ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()) != null) {
							ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), buf);
						}
					}
				}
		//this.area.sendToMembers(this.getuid(), buf);
	}
	
	
	//check area for hiding sins
	public void Check_ForHiding(int slot, int iconid, int time, int value){
			Iterator<Integer> iter = this.iniPackets.keySet().iterator();
				while(iter.hasNext()) {
					Integer plUid = iter.next();               
					if (plUid.intValue() != this.charID){
						Character ch = this.getWmap().getCharacter(plUid.intValue());
						if(ch != null && ch.HIDING == 1) {
							ch.sendToMap_ADMIN_CHECK_ON_extCharPacket();
						    if (ch.guild != null && ch.guild.getGuildtype() != 0){
						    	ch.sendToMap(ch.extCharGuild());
						    }
						    if (ch.BoothName != null && ch.BoothStance != 0){
						    	ch.sendToMap(ch.extCharBooth(0));
						    }
						    if(ch.Running == 1){
						    	ch.sendToMap(ch.Runnskill());
						    }
						    ch.HIDING = 0;
						    
							this.DotsSLOT.remove(6);	 
							this.DotsIconID.remove(6);	
							this.DotsTime.remove(6);
							this.DotsValue.remove(6);
						    this.RemoveDot(ch.getDotsIconID(44), ch.getDotsSLOT(6));
						    Charstuff.getInstance().AddDot(ch.charID, iconid, value, time, slot, 1, this);
						}
					}
				}
		//this.area.sendToMembers(this.getuid(), buf);
	}
	
	//send to map admin check
	public void sendToMap_ADMIN_CHECK_ON_extCharPacket() {
			Iterator<Integer> iter = this.iniPackets.keySet().iterator();
				while(iter.hasNext()) {
					Integer plUid = iter.next();               
					if (plUid.intValue() != this.charID){
						Character ch = this.getWmap().getCharacter(plUid.intValue());
						if(ch != null) {
							ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.extCharPacket());
							
			   				//if guildwarthen activate pvp enable
			   				if(Guildwar.getInstance().isguildwar()){ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.PvPActive());}
							
							  // if both guild is at least grade 10 or lower
							if(this.guild != null && ch.guild != null && ch.guild.guildtype != 1){ 
								if(this.guild.War.containsKey(ch.guild.guildname)){	
								ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.PvPActive());
								}
							}else // if both is not in guild
								if(this.guild == null && ch.guild == null){
								ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.PvPActive());
								}
						
							if (this.guild != null && this.guild.getGuildtype() != 0){
						    	ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.extCharGuild());
						    }
						    if (this.BoothName != null && this.BoothStance != 0){
						    	ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.extCharBooth(0));
						    }
						    if(this.Running == 1){
						    ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), this.Runnskill());
						    }
						}
					}
				}
		//this.area.sendToMembers(this.getuid(), buf);
	}
	
	


	// receive updated list for nearby objects
	public void updateEnvironment(Integer player, boolean add, boolean leavegameworld) {
		////System.out.println("Character: " + this.charID + " has got player list of size: " + players.size());
		    	if(wmap.CharacterExists(player)){
			    	Character Tplayer = wmap.getCharacter(player);	
			    	if(Tplayer != null){
			    		
			        	// if boolean leavegameworld = true then 
				    if(this.iniPackets.containsKey(Tplayer.getCharID()) && leavegameworld){
				    	this.iniPackets.remove(Tplayer.getCharID());
				    	//System.out.println("Left =>"+this.getLOGsetName()+" - "+Tplayer.getLOGsetName());
				    	ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), this.getVanishByID(this.charID));
				    	return;
				    }
			    		
			    	// if its already has it and  on or outside 300 yards
					if (!add && this.iniPackets.containsKey(Tplayer.getCharID()) && WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) >= 325){
					//System.out.println("Remove =>"+this.getLOGsetName()+" - "+Tplayer.getLOGsetName());
					this.iniPackets.remove(Tplayer.getCharID());
					ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), this.getVanishByID(this.charID));			
					}	
			    	
					// add player if it does not have it already and its within 300 yards
					if (add && !this.iniPackets.containsKey(Tplayer.getCharID()) && WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 325){
					//System.out.println("Add =>"+this.getLOGsetName()+" - "+Tplayer.getLOGsetName());
					this.iniPackets.put(Tplayer.getCharID(), Tplayer.getCharID());
					this.sendInit(Tplayer.getCharID());
					}	
					
			    }}
		
		//this.sendInit(player);
		//this.sendVanish();
	}
	/*	private void sendVanish() {				synchronized(this.vanish){			Iterator<Map.Entry<Integer, Integer>> iter = this.vanish.entrySet().iterator();			while(iter.hasNext()){				Map.Entry<Integer, Integer> pairs = iter.next();				if (pairs.getValue() > 1){					ServerFacade.getInstance().addWriteByChannel(this.GetChannel(), this.getVanishByID(pairs.getKey())));					this.iniPackets.remove(pairs.getKey());				}			}		}			}		private void incVanish(Integer id) {		int i = 0;		if (this.vanish.containsKey(id)){			 i = this.vanish.get(id) +1;		}		else { 			i = 1;		}		this.vanish.put(id, i);	}	*/	// send initial packets to players who don't already have ours
	public void sendInit(Integer tmp) {
			if (this.getWmap().CharacterExists(tmp) && tmp != this.getuid() && this.HIDING == 0){
			Character t = this.getWmap().getCharacter(tmp);
				ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.extCharPacket());
				
   				//if guildwarthen activate pvp enable
   				if(Guildwar.getInstance().isguildwar()){ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.PvPActive());}
				
					  // if both guild is at least grade 10 or lower
				if(this.guild != null && t.guild != null && t.guild.guildtype != 1){ 
					if(this.guild.War.containsKey(t.guild.guildname)){	
					ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.PvPActive());
					}
				}else // if both is not in guild
					if(this.guild == null && t.guild == null){
					ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.PvPActive());
					}
				
				if (this.guild != null && this.guild.getGuildtype() != 0){
			    	ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.extCharGuild());
			    }
			    if (this.BoothName != null && this.BoothStance != 0){
			    	ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.extCharBooth(0));
			    }
			    if(this.Running == 1){
			    ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.Runnskill());
			    }
			}
	}
	
	public byte[] Runnskill() {
		 byte[] skill = BitTools.intToByteArray(this.Runskill); 
		 byte[] chid = BitTools.intToByteArray(this.getCharID());
		 byte[] buff1 = new byte[28];
		   buff1[0] = (byte)0x1c; 
		   buff1[4] = (byte)0x05;
		   buff1[6] = (byte)0x34;
		   buff1[8] = (byte)0x01;
		   for(int i=0;i<4;i++) {
			  buff1[12+i] = chid[i];	
			  buff1[20+i] = skill[i];
		   }
		   buff1[16] = (byte)0x01;
		   
		   if(this.Running == 1){
		   buff1[24] = (byte)0xca;
		   buff1[25] = (byte)0xa0;
		   buff1[26] = (byte)0x04;
		   }
		   else
		   if(this.Running == 0){
		   buff1[24] = (byte)0xcb;
		   buff1[25] = (byte)0x8e;
		   buff1[26] = (byte)0x0d;
		   }
		return buff1;	
	}
	
	// return external character booth
		public byte[] extCharBooth(int inc) {
				byte[] booth1 = new byte[48];
				byte[] msg = this.BoothName.getBytes();
				byte[] chID = BitTools.intToByteArray(this.charID);
				
				booth1[0] = (byte)0x30;
				booth1[4] = (byte)0x05; // 0x05 = to show other players
				booth1[6] = (byte)0x37;
				booth1[8] = (byte)0x01;
				
				for(int i=0;i<4;i++) {
					booth1[12+i] = chID[i];
				}
				
				if(BoothStance == 0){booth1[16] = (byte)0x00;}
				if(BoothStance == 1){booth1[16] = (byte)0x01;}
				if(BoothStance == 2){booth1[16] = (byte)0x01;} // 2 = 0x01 ??? BUG I THINK
				
				for(int i=0;i<30;i++) {
					booth1[17+i] = msg[i];
				}
				if (inc == 1){this.sendToMap(booth1);}
				else
				if (inc == 0){return booth1;}	
				return null;
		}
	
	public byte[] extCharGuild() {
			byte[] egdata = new byte[40];
			byte[] msg = this.guild.getguildname().getBytes();
			byte[] chID = BitTools.intToByteArray(this.charID);
			
			egdata[0] = (byte)0x28;
			egdata[4] = (byte)0x05;
			egdata[6] = (byte)0x41;
			egdata[8] = (byte)0x01;
			
			for(int i=0;i<4;i++) {
				egdata[12+i] = chID[i]; // charid
			}
			egdata[16] = (byte)this.guild.getGuildtype();
			
			for(int i=0;i<msg.length;i++) {
				egdata[17+i] = msg[i];
			}
	        byte[] guildicon = BitTools.intToByteArray(this.guild.guildicon); // guild ID ?? (or icon)
	        for(int i=0;i<2;i++){
	        egdata[i+34] = guildicon[i]; 
	        }
			egdata[36] = (byte)this.guild.getGuildhat(); 
			return egdata;
	}
	

	//pvp2
	public byte[] PvPActive() {
		byte[] chID = BitTools.intToByteArray(this.charID);
		byte[] egdata = new byte[32];
		egdata[0] = (byte)0x20;
		egdata[4] = (byte)0x05;
		egdata[6] = (byte)0x43;
		egdata[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			egdata[12+i] = chID[i]; // charid
		}
		egdata[16] = (byte)0x01; // 0x01 = shift+click (no client message) | 0x02 = Red name | 0x03 = ?
		
		egdata[20] = (byte)this.fametitle;
		
	    byte[] fame = BitTools.intToByteArray(this.fame);       
	    for(int i=0;i<4;i++) { egdata[i+28] = fame[i]; }// fame 
		return egdata;
}
	
	// return external character packet
	public byte[] extCharPacket() {
		byte[] cedata = new byte[612];
		short length = (short)cedata.length;
		byte[] lengthbytes = BitTools.shortToByteArray(length);
		byte[] chID = BitTools.intToByteArray(this.charID);
		byte[] xCoords = BitTools.floatToByteArray(this.location.getX());		
		byte[] yCoords = BitTools.floatToByteArray(this.location.getY());		
		cedata[0] = lengthbytes[0];
		cedata[1] = lengthbytes[1];
		cedata[4] = (byte)0x05;
		cedata[6] = (byte)0x01;
		cedata[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			cedata[i+12] = chID[i]; //character ID
			cedata[i+88] = xCoords[i]; //location x
			cedata[i+92] = yCoords[i]; //location y
		}
		
		/*if(!this.getChargm().equals("az")){
		byte[] chName = this.name.getBytes();
		for(int i=0;i<chName.length;i++) {
			cedata[i+20] = chName[i]; //characters Name
		}
		}*/

		byte[] chName = this.name.getBytes();
		for(int i=0;i<chName.length;i++) {
		cedata[i+20] = chName[i]; //characters Name
		}

		
		for(int i=0;i<16;i++) {
			cedata[37+i] = (byte)0x30; //character packets have 16 times 30(0 in ASCII) in row. Mysteries of CRS.
		}
		
		cedata[54] = (byte)this.faction;//faction	
	    byte[] fame = BitTools.intToByteArray(this.fame);       
	    for(int i=0;i<4;i++) { cedata[i+56] = fame[i]; }// fame 
		cedata[62] = (byte)this.face; // face
		
		if(this.characterClass == 2) {
			cedata[60] = (byte)0x02; //gender byte
			cedata[68] = (byte)0x02; //class byte
		} else {
			cedata[60] = (byte)0x01; //gender byte
			cedata[68] = (byte)this.characterClass; //class byte
		}
		if (this.guild != null && this.guild.getGuildtype() != 0){
	        byte[] guildicon = BitTools.intToByteArray(this.guild.guildicon); // guild ID ?? (or icon)
	        for(int i=0;i<2;i++){
	        cedata[i+70] = guildicon[i]; 
	        }
		int Key = BitTools.ValueToKey(this.getCharID(), this.guild.guildids);
		cedata[72] =(byte)this.guild.getguildranks(Key); // guild something
		}
		cedata[74] = (byte)this.getLevel();
		   					
	
		//SHOW my ITEMS:Equipment TO OTHER PLAYERS				
		
		// Head		
		cedata[98] = (byte)0x0b; // <- somekind of item ( head ) indicator         
		byte[] head = BitTools.intToByteArray(this.getequipSLOT(0));        for(int i=0;i<4;i++) { cedata[i+100] = head[i]; }                
		// neck       
		byte[] neck = BitTools.intToByteArray(this.getequipSLOT(1));        for(int i=0;i<4;i++) { cedata[i+112] = neck[i]; }        
		cedata[111] = (byte)0x01;                        
		// cape <-- bugged ?        
		byte[] cape = BitTools.intToByteArray(this.getequipSLOT(2));        for(int i=0;i<4;i++) { cedata[i+124] = cape[i]; }                
		// chest        
		cedata[134] = (byte)0x0a;         
		byte[] chest = BitTools.intToByteArray(this.getequipSLOT(3));      for(int i=0;i<4;i++) { cedata[i+136] = chest[i]; }                 
		// pants        
		cedata[146] = (byte)0x0a;         
		cedata[147] = (byte)0x02;         
		byte[] pants = BitTools.intToByteArray(this.getequipSLOT(4));      for(int i=0;i<4;i++) { cedata[i+148] = pants[i]; }                        
		// armor        
		cedata[158] = (byte)0x05;        
		byte[] armor = BitTools.intToByteArray(this.getequipSLOT(5));      for(int i=0;i<4;i++) { cedata[i+160] = armor[i]; }                        
		// bracer        
		cedata[170] = (byte)0x03;        
		cedata[171] = (byte)0x07;        
		byte[] bracer = BitTools.intToByteArray(this.getequipSLOT(6));    for(int i=0;i<4;i++) { cedata[i+172] = bracer[i]; }                         
		// primary weapon        
		byte[] primaryweapon = BitTools.intToByteArray(this.getequipSLOT(7));  for(int i=0;i<4;i++) { cedata[i+184] = primaryweapon[i]; }  
		// second weapon        
		byte[] secondweapon = BitTools.intToByteArray(this.getequipSLOT(8));  for(int i=0;i<4;i++) { cedata[i+196] = secondweapon[i]; } 
		// ring 1       
		byte[] ring1 = BitTools.intToByteArray(this.getequipSLOT(9));      for(int i=0;i<4;i++) { cedata[i+208] = ring1[i]; }            
		// ring 2        
		byte[] ring2 = BitTools.intToByteArray(this.getequipSLOT(10));      for(int i=0;i<4;i++) { cedata[i+220] = ring2[i]; }        
		// shoes      
		byte[] shoes = BitTools.intToByteArray(this.getequipSLOT(11));      for(int i=0;i<4;i++) { cedata[i+232] = shoes[i]; }   
    
		byte[] medal1 = BitTools.intToByteArray(this.getequipSLOT(12));      for(int i=0;i<4;i++) { cedata[i+244] = medal1[i]; }  
		
		byte[] medal2 = BitTools.intToByteArray(this.getequipSLOT(13));      for(int i=0;i<4;i++) { cedata[i+256] = medal2[i]; }  
		
		byte[] medal3 = BitTools.intToByteArray(this.getequipSLOT(14));      for(int i=0;i<4;i++) { cedata[i+268] = medal3[i]; }  
		
		// mount        
		byte[] mount = BitTools.intToByteArray(this.getequipSLOT(15));      for(int i=0;i<4;i++) { cedata[i+280] = mount[i]; }  
		
		// next to mount (DT)        
		byte[] bird = BitTools.intToByteArray(this.getequipSLOT(16));      for(int i=0;i<4;i++) { cedata[i+292] = bird[i]; }  
		
		//cedata[482] = (byte)0x01; ??
		//cedata[483] = (byte)0x07; ??
		cedata[484] = (byte)this.fametitle;  // 0x07 = couragious
		
		//cedata[489] = (byte)??
		
		
		cedata[610] = (byte)0x50;        
		cedata[611] = (byte)0x2a;

		return cedata;
	}
	
	
	
	public byte[] getVanishByID(int uid) {
		byte[] vanish = new byte[20];
		byte[] bUid = BitTools.intToByteArray(uid);
		byte[] stuffz = new byte[] {(byte)0x01, (byte)0x10, (byte)0xa0, (byte)0x36, (byte)0x00, (byte)0xee, (byte)0x5f, (byte)0xbf};
		//8 -> (byte)0x01, (byte)0x10, (byte)0xa0, (byte)0x36, 	 16->	(byte)0x00, (byte)0xee, (byte)0x5f, (byte)0xbf
		
		
		vanish[0] = (byte)vanish.length;
		vanish[4] = (byte)0x05;
		
		
		for(int i=0;i<4;i++) { 
			vanish[12+i] = bUid[i];
			vanish[i+8] = stuffz[i];
			vanish[i+16] = stuffz[i+4];
		}
		
		return vanish;
	}
//-------------------------------------- Query's for specific character -----------------------------\\
	
	public void savecharacter(){
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
		
		
		String equip = "";
		for(int i=0;i<17;i++){
		if(this.EquipSLOT.containsKey(i)){
			equip += Integer.toString(i);	
			equip +=",";
			equip += Integer.toString(this.getequipSLOT(i));
			equip +=",";
		}}
		//System.out.println("Output: " + equip);
		
		String skillbar = "";
		for(int i=0;i<22;i++){
		if(this.skillbar.containsKey(i)){
			skillbar += Integer.toString(i);	
			skillbar +=",";
			skillbar += Integer.toString(this.getskillbar(i));
			skillbar +=",";
		}}
		//System.out.println("Output: " + skillbar);
		
		String skills = "";
		for(int i=0;i<60;i++){
		if(this.skills.containsKey(i)){
			skills += Integer.toString(i);	
			skills +=",";
			skills += Integer.toString(this.getlearnedskill(i));
			skills +=",";
		}}
		//System.out.println("Output: " + skills); 
		
		String pots = "";
		for(int i=0;i<11;i++){
		if(this.PotSLOT.containsKey(i)){
			pots += Integer.toString(this.getPotSLOT(i));	
			pots +=",";
			pots += Integer.toString(this.getPotIconID(i));
			pots +=",";
			pots += Integer.toString(this.getPotTime(i));
			pots +=",";
			pots += Integer.toString(this.getPotValue(i));
			pots +=",";
		}}
	
		
		Iterator<Entry<Integer, Long>> iter = this.item_end_date.entrySet().iterator();
		String expire = "";
		while(iter.hasNext()) {
			Map.Entry<Integer, Long> pairs = iter.next();
			expire += Integer.toString(pairs.getKey());	
			expire +=",";
			expire += Long.toString(pairs.getValue());
			expire +=",";
		}
		
		// save character to DB
		CharacterDAO.setchar(this.getlastknownX(), this.getlastknownY(), this.getCurrentMap(),this.getHp(), this.getMana(), this.getStamina(), this.getExp(), this.getFame(), this.gold, expire, inventory, cargo, equip, skillbar ,skills ,pots, this.vendingpoint, this.getCharID());
	}
	
	public void expireactive(){
		Iterator<Entry<Integer, Long>> iter = this.item_end_date.entrySet().iterator();
		String expire = "";
		while(iter.hasNext()) {
			Map.Entry<Integer, Long> pairs = iter.next();
			expire += Integer.toString(pairs.getKey());	
			expire +=",";
			expire += Long.toString(pairs.getValue());
			expire +=",";
		}
		CharacterDAO.setexpire(expire,this.getCharID());
	}
	

	
	
	
	
	
//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//

	public Character getwholist(int charid){
		try{
			//System.out.println("Loading wholist...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getwholist(SQLconnection.getInstance().getaConnection(), charid));
				while( rs.next()){
				this.setwholist(rs.getInt("headerid1"), rs.getInt("headerid2"), rs.getString("friendname"));
				}
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	public Character getequips(String onez){
		try{
				// HotBarSlotId , headerid(item, or skill), ItemOrSkillslot(itemid, or skillslotid)
				if(onez != null|| onez != "0" ){
				String one = onez;
				//System.out.println("Parsing: " + one);
				String[] splat1 = one.split(",");
				for(int i=0;i<splat1.length;i++){
				this.setequipSLOT(Integer.valueOf(splat1[i]), Integer.valueOf(splat1[i+1]));
				i++;
				}}
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	public Character getskillbardb(String onez){
		try{
				// HotBarSlotId , headerid(item, or skill), ItemOrSkillslot(itemid, or skillslotid)
				if(onez != null|| onez != "0" ){
				String one = onez;
				//System.out.println("Parsing: " + one);
				String[] splat1 = one.split(",");
				for(int i=0;i<splat1.length;i++){
				this.setskillbar(Integer.valueOf(splat1[i]), Integer.valueOf(1), Integer.valueOf(splat1[i+1]));
				i++;
				}}
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	public Character getskills(String onez){
		try{

				// skillslot , skillid
				if(onez != null || onez != "0" ){
				String one = onez;
				//System.out.println("Parsing: " + one);
				String[] splat1 = one.split(",");
				for(int i=0;i<splat1.length;i++) {
				this.setLearnedSkill(Integer.valueOf(splat1[i]), Integer.valueOf(splat1[i+1]));
				i++;
				}
				}
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			// http://images.4chan.org/hc/src/1358541097225.gif
		}
		//System.out.println("......done!"); 
		return null;
	}
	
	
	public Character getcargo(String onez){
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
		return null;
	}
	
	
	public Character getinventorys(String onez){
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
		return null;
	}
	
	
	
	public Character getpots(String onez){
		try{
				if(onez != null && onez != "0"){
				String one = onez;
				//System.out.println("get pots: "+one);
				String[] splat1 = one.split(",");
				for(int i=0;i<splat1.length;i++) {	
				this.setPotSLOT(Integer.valueOf(splat1[i]), Integer.valueOf(splat1[i]));
				i++;
				this.setPotIconID(Integer.valueOf(splat1[i-1]), Integer.valueOf(splat1[i]));
				i++;
				this.setPotTime(Integer.valueOf(splat1[i-2]), Integer.valueOf(splat1[i]));
				i++;
				this.setPotValue(Integer.valueOf(splat1[i-3]), Integer.valueOf(splat1[i]));
				}
				
				

				if(this.PotIconID.containsValue(92) && this.PotValue.containsValue(8)){this.JACKPOT_TAG = 1;}
				if(this.PotIconID.containsValue(91) && this.PotValue.containsValue(40)){this.DOUBLE_ITEM_DROP_TAG = 1;}
				if(this.PotIconID.containsValue(92) && this.PotValue.containsValue(12)){this.GREATER_JACKPOT_TAG = 1;}
				if(this.PotIconID.containsValue(91) && this.PotValue.containsValue(60)){this.GREATER_DOUBLE_ITEM_DROP_TAG = 1;}
				if(this.PotIconID.containsValue(84)){this.FDD = 1;} 
				if(this.PotIconID.containsValue(87)){this.CASR = 1;}
				if(this.PotIconID.containsValue(83)){this.FAD = 1;} 
				if(this.PotIconID.containsValue(85)){this.FASR = 1;}
				if(this.PotIconID.containsValue(90))  {this.Fame_Tag_100 = 1;}
				if(this.PotIconID.containsValue(82) && this.PotValue.containsValue(10)){this.Exp_Tag_10 = 1;}
				if(this.PotIconID.containsValue(82) && this.PotValue.containsValue(15)){this.Exp_Tag_15 = 1;}
				if(this.PotIconID.containsValue(82) && this.PotValue.containsValue(20)){this.Exp_Tag_20 = 1;}
				if(this.PotIconID.containsValue(82) && this.PotValue.containsValue(30)){this.Exp_Tag_30 = 1;}
				if(this.PotIconID.containsValue(82) && this.PotValue.containsValue(100)){this.Exp_Tag_100 = 1;}
				if(this.PotIconID.containsValue(86)){this.FD = 1;}
				if(this.PotIconID.containsValue(94)){this.SAP = 1;}
				if(this.PotIconID.containsValue(93)){this.NEW_STATUS_SUNDAN = 1;}
				}
			}
		catch (Exception e) {
			//log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	public Character getsexpire(String onez){
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
		return null;
	}

	
	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public WMap getWmap() {
		return wmap;
	}

	public void setWmap(WMap wmap) {
		this.wmap = wmap;
	}

	public int getMaxstamina() {
		return maxstamina;
	}

	public void setMaxstamina(int maxstamina) {
		this.maxstamina = maxstamina;
	}

	public Player getPl() {
		return pl;
	}

	public void setPl(Player pl) {
		this.pl = pl;
	}

	public int getFarmmodule() {
		return farmmodule;
	}

	public void setFarmmodule(int farmmodule) {
		//System.out.println("farmmodule: = "+farmmodule);
		this.farmmodule = farmmodule;
	}


}
