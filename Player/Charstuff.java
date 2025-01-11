package Player;

import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Connections.Connection;
import Database.Queries;
import Database.SQLconnection;
import Mob.Mob;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


/*
 * Charstuff.getInstance().class
 * Stores all buff data 
 */

public class Charstuff 
{
	public int count = 0;
	public  long UpTime;
	public  int mostplayersonline;
	private	static Charstuff instance; 
	public Map<Integer, Integer> Stackable_items = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> Invincible_items = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> Non_Tradable_items = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> mobdrops_skyzone = new HashMap<Integer, Integer>();
	public Map<Integer, String> itemtoname = new HashMap<Integer, String>();
	public Map<Integer, Integer> Equipignore = new HashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> guildbuffs = new ConcurrentHashMap<Integer, Integer>(); // save ewach 5 mins ?
	public Map<Integer, Integer> itemtoprocedure1 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure2 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure3 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure4 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure5 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure6 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure7 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure8 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure9 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure10 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure11 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure12 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure13 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure14 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure15 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure16 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure17 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure18 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure19 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure20 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure21 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure22 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure23 = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> itemtoprocedure24 = new HashMap<Integer, Integer>();
	public ConcurrentMap<String, String>  CensoredWords = new ConcurrentHashMap<String, String>(100, 4, 100);
	public long size = 0;
	public long iteravg = 0;
	

    public synchronized static Charstuff getInstance(){
        if (instance == null){
                instance = new Charstuff();
        }
        return instance;
    }
    
	public int getguildbuffs(int skillid) {
		if(guildbuffs.containsKey(skillid)){
			int skillidz = guildbuffs.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	
    
	public int getitemtoprocedure1(int skillid) {
		if(itemtoprocedure1.containsKey(skillid)){
			int skillidz = itemtoprocedure1.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure2(int skillid) {
		if(itemtoprocedure2.containsKey(skillid)){
			int skillidz = itemtoprocedure2.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure3(int skillid) {
		if(itemtoprocedure3.containsKey(skillid)){
			int skillidz = itemtoprocedure3.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure4(int skillid) {
		if(itemtoprocedure4.containsKey(skillid)){
			int skillidz = itemtoprocedure4.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure5(int skillid) {
		if(itemtoprocedure5.containsKey(skillid)){
			int skillidz = itemtoprocedure5.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure6(int skillid) {
		if(itemtoprocedure6.containsKey(skillid)){
			int skillidz = itemtoprocedure6.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure7(int skillid) {
		if(itemtoprocedure7.containsKey(skillid)){
			int skillidz = itemtoprocedure7.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure8(int skillid) {
		if(itemtoprocedure8.containsKey(skillid)){
			int skillidz = itemtoprocedure8.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure9(int skillid) {
		if(itemtoprocedure9.containsKey(skillid)){
			int skillidz = itemtoprocedure9.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure10(int skillid) {
		if(itemtoprocedure10.containsKey(skillid)){
			int skillidz = itemtoprocedure10.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure11(int skillid) {
		if(itemtoprocedure11.containsKey(skillid)){
			int skillidz = itemtoprocedure11.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure12(int skillid) {
		if(itemtoprocedure12.containsKey(skillid)){
			int skillidz = itemtoprocedure12.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure13(int skillid) {
		if(itemtoprocedure13.containsKey(skillid)){
			int skillidz = itemtoprocedure13.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure14(int skillid) {
		if(itemtoprocedure14.containsKey(skillid)){
			int skillidz = itemtoprocedure14.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure15(int skillid) {
		if(itemtoprocedure15.containsKey(skillid)){
			int skillidz = itemtoprocedure15.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure16(int skillid) {
		if(itemtoprocedure16.containsKey(skillid)){
			int skillidz = itemtoprocedure16.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure17(int skillid) {
		if(itemtoprocedure17.containsKey(skillid)){
			int skillidz = itemtoprocedure17.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure18(int skillid) {
		if(itemtoprocedure18.containsKey(skillid)){
			int skillidz = itemtoprocedure18.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure19(int skillid) {
		if(itemtoprocedure19.containsKey(skillid)){
			int skillidz = itemtoprocedure19.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure20(int skillid) {
		if(itemtoprocedure20.containsKey(skillid)){
			int skillidz = itemtoprocedure20.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure21(int skillid) {
		if(itemtoprocedure21.containsKey(skillid)){
			int skillidz = itemtoprocedure21.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure22(int skillid) {
		if(itemtoprocedure22.containsKey(skillid)){
			int skillidz = itemtoprocedure22.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure23(int skillid) {
		if(itemtoprocedure23.containsKey(skillid)){
			int skillidz = itemtoprocedure23.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
	public int getitemtoprocedure24(int skillid) {
		if(itemtoprocedure24.containsKey(skillid)){
			int skillidz = itemtoprocedure24.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
    
	public String getitemtoname(int skillid) {
		if(itemtoname.containsKey(skillid)){
			String skillidz = itemtoname.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return "NULL report to gm."; 
		}
	}
    
	public boolean tryitemtoname(int one) {
		return itemtoname.containsKey(one);
	}
	

	public void setitemtoname(int buff, String buffz) {
		itemtoname.put(Integer.valueOf(buff), buffz); 
		//System.out.println("Stackable_items: " +buff+" - " +buffz);
	}
    
	public int getStackable_items(int skillid) {
		if(Stackable_items.containsKey(skillid)){
			int skillidz = Stackable_items.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
    
	public boolean tryStackable_items(int one) {
		return Stackable_items.containsKey(one);
	}
	

	public void setStackable_items(int buff, int buffz) {
		Stackable_items.put(Integer.valueOf(buff), Integer.valueOf(buffz)); 
		//System.out.println("Stackable_items: " +buff+" - " +buffz);
	}
    
	
	public int getmobdrops_skyzone(int skillid) {
		if(mobdrops_skyzone.containsKey(skillid)){
			int skillidz = mobdrops_skyzone.get(skillid);
			//System.out.println("mobdrops_skyzone: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}
    
	public boolean trymobdrops_skyzone(int one) {
		return mobdrops_skyzone.containsKey(one);
	}
	

	public void setmobdrops_skyzone(int buff, int buffz) {
		mobdrops_skyzone.put(Integer.valueOf(buff), Integer.valueOf(buffz)); 
		//System.out.println("mobdrops_skyzone: " +buff+" - " +buffz);
	}
    
	
	public boolean tryInvincible_items(int one) {
		return Invincible_items.containsKey(one);
	}
	

	public void setInvincible_items(int buff, int buffz) {
		Invincible_items.put(Integer.valueOf(buff), Integer.valueOf(buffz)); 
		//System.out.println("Invincible_items: " +buff+" - " +buffz);
	}
	
	
	public boolean tryNon_Tradable_items(int one) {
		return Non_Tradable_items.containsKey(one);
	}
	

	public void setNon_Tradable_items(int buff, int buffz) {
		Non_Tradable_items.put(Integer.valueOf(buff), Integer.valueOf(buffz)); 
		//System.out.println("Non_Tradable_items: " +buff+" - " +buffz);
	}
	
	public void AddDot(int Target, int DotsIconID, int DotsValue, int DotsTime, int DotsSLOT, int DETERMINER, Character cur){//DETERMINE: 1 = player | 2 = mob
		if(DETERMINER == 1){
		Character Tplayer = WMap.getInstance().getCharacter(Target); 	
		if(Tplayer != null){
		if(DotsIconID == 43 && !Tplayer.DotsIconID.containsValue(43)
		||DotsIconID == 46 && !Tplayer.DotsIconID.containsValue(46)
		||DotsIconID == 47 && !Tplayer.DotsIconID.containsValue(47)
		||DotsIconID == 49 && !Tplayer.DotsIconID.containsValue(49)
		||DotsIconID == 58 && !Tplayer.DotsIconID.containsValue(58)
		||DotsIconID == 6||DotsIconID == 7||DotsIconID == 12||DotsIconID == 13||DotsIconID == 15||DotsIconID == 16||DotsIconID == 21||DotsIconID == 22
		||DotsIconID == 42||DotsIconID == 44||DotsIconID == 45||DotsIconID == 48||DotsIconID == 52||DotsIconID == 56||DotsIconID == 64
		){
		byte[] chid = BitTools.intToByteArray(Tplayer.getCharID());
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
		 
			if(DotsIconID == 43){DotsTime = DotsTime / 4;}
			if(DotsIconID == 46){DotsTime = DotsTime / 4;}
			if(DotsIconID == 49){DotsTime = DotsTime / 4;}
		 
		 Tplayer.setDots(DotsSLOT, DotsIconID, DotsTime, DotsValue);
		  
		 byte[] buffidz1 = BitTools.intToByteArray(DotsIconID); 
		 byte[] bufftimez1 = BitTools.intToByteArray(DotsTime);
		 byte[] buffvaluez1 = BitTools.intToByteArray(DotsValue);
		 byte[] buffslotz1 = BitTools.intToByteArray(DotsSLOT);
		 
			 for(int i=0;i<2;i++) {
				 buff[i+16] = buffslotz1[i];	 // buffslot
				 buff[i+20] = buffidz1[i];	 // buff id
				 buff[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
				 buff[i+24] = buffvaluez1[i]; // Value XXXXX
			 }	
			 
			if(DotsIconID == 16||DotsIconID == 15){Tplayer.statlist();} 
			ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), buff); 
			 if(DotsIconID == 43||DotsIconID == 46||DotsIconID == 49||DotsIconID == 44||DotsIconID == 45||DotsIconID == 47||DotsIconID == 48){Tplayer.sendToMap(buff);}
		}}}else{
		    Mob TMob = WMap.getInstance().getMob(Target, cur.getCurrentMap()); 
			if(TMob != null){
			if(//stun
			TMob.getMobID() == 14529 && DotsIconID == 43
			||TMob.getMobID() == 14530 && DotsIconID == 43
			||TMob.getMobID() == 14531 && DotsIconID == 43
			||TMob.getMobID() == 14532 && DotsIconID == 43
			){
				// dont apply when meet the If.
			}else if(
			TMob.getMobID() == 14529 && DotsIconID == 46
			||TMob.getMobID() == 14530 && DotsIconID == 46
			||TMob.getMobID() == 14531 && DotsIconID == 46
			||TMob.getMobID() == 14532 && DotsIconID == 46
			){
				// dont apply when meet the If.
			}else{
			byte[] chid = BitTools.intToByteArray(TMob.getUid());
			byte[] buff = new byte[44];
			 buff[0] = (byte)0x2c; 
			 buff[4] = (byte)0x05;
			 buff[6] = (byte)0x1f;
			 buff[8] = (byte)0x02;// 1 = player | 2 = mob
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
			 
				if(DotsIconID == 43){DotsTime = DotsTime / 4;}
				if(DotsIconID == 46){DotsTime = DotsTime / 4;}
				if(DotsIconID == 49){DotsTime = DotsTime / 4;}
			 
			 TMob.setDots(DotsSLOT, DotsIconID, DotsTime, DotsValue);
			 
			 byte[] buffidz1 = BitTools.intToByteArray(DotsIconID); 
			 byte[] bufftimez1 = BitTools.intToByteArray(DotsTime);
			 byte[] buffvaluez1 = BitTools.intToByteArray(DotsValue);
			 byte[] buffslotz1 = BitTools.intToByteArray(DotsSLOT);
			 
				 for(int i=0;i<2;i++) {
					 buff[i+16] = buffslotz1[i];	 // buffslot
					 buff[i+20] = buffidz1[i];	 // buff id
					 buff[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
					 buff[i+24] = buffvaluez1[i]; // Value XXXXX
				 }	
			 TMob.send(buff);
			}
		  }
		 }
		}

// respond from Character.java
public void respond(String message, SocketChannel wtf)
{
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
}


// respond to characte from any function	
public void respond(String message, Connection con)
{
	Character cur = ((PlayerConnection)con).getActiveCharacter();
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
	if(ServerFacade.getInstance().getConnectionByChannel(cur.GetChannel()) != null){
	ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), gmsg);
	}
}

public void Arespond(String message, Connection con)
{
	Character cur = ((PlayerConnection)con).getActiveCharacter();
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
	byte[] name = "New Announcement".getBytes();
	for(int i=0;i<name.length;i++) {
		gmsg[i+20] = name[i];
	}
	gmsg[40] = (byte)0x44; // = " : "
	for(int i=0;i<msg.length;i++) {
		gmsg[i+44] = msg[i];
	}
	if(ServerFacade.getInstance().getConnectionByChannel(cur.GetChannel()) != null){
	ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), gmsg);
	}
}

public void gmshout(String message)
{
	byte[] gmsg = new byte[14+message.length()];
	byte[] msg = message.getBytes(); 
	gmsg[0] = (byte)gmsg.length;
	gmsg[4] = (byte)0x03;
	gmsg[6] = (byte)0x50;
	gmsg[7] = (byte)0xc3;
	gmsg[8] = (byte)0x01;
	gmsg[9] = (byte)message.length();
	for(int i=0;i<msg.length;i++) {
		gmsg[i+13] = msg[i];
	}
	Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
	Character tmp;
	while(iter.hasNext()) {
		Map.Entry<Integer, Character> pairs = iter.next();
		tmp = pairs.getValue();
		if(ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null){
		ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
		}
	}
}

//respond from Character.java
public void shoutkyaa(String namez, String message, SocketChannel wtf)
{
	byte[] gmsg = new byte[45+message.length()];
	byte[] msg = message.getBytes(); 
	gmsg[0] = (byte)gmsg.length;
	gmsg[4] = (byte)0x05;
	gmsg[6] = (byte)0x07;
	gmsg[8] = (byte)0x01;
	gmsg[17] = (byte)0x01;
	gmsg[18] = (byte)0x06;
	
	gmsg[20] = (byte)0xa4; // begin letter name
	gmsg[21] = (byte)0xd1;
	
	String wtfwww = "["+namez+"]";
	byte[] name = wtfwww.getBytes();
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
}


//respond from Character.java
public void respondyellowshout(String message, SocketChannel wtf)
{
	byte[] gmsg = new byte[45+message.length()];
	byte[] msg = message.getBytes(); 
	gmsg[0] = (byte)gmsg.length;
	gmsg[4] = (byte)0x05;
	gmsg[6] = (byte)0x07;
	gmsg[8] = (byte)0x01;
	gmsg[17] = (byte)0x01;
	gmsg[18] = (byte)0x06;
	
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
}

public void guildshout(String message)
{
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
	Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
	Character tmp;
	while(iter.hasNext()) {
		Map.Entry<Integer, Character> pairs = iter.next();
		tmp = pairs.getValue();
		if(ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null){
		ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
		}
	}
}


public void respondguild(String message, SocketChannel wtf)
{
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
}








}
