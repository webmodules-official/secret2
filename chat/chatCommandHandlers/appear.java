package chat.chatCommandHandlers;


import java.nio.channels.SelectionKey;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Connections.Connection;
import Database.CharacterDAO;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.grinditems;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import chat.ChatCommandExecutor;


public class appear implements ChatCommandExecutor {
	private Map<String, Character> names = new HashMap<String, Character>();
	public  int inc = 0;
	public  int skill_count = 964;
	public  int skillbar_count = 788;
	public  int friendlist_count = 24;
	public  int ignorelist_count = 534;
	public  int inventory_count = 800;
	public long interval;
	public int cargo_count = 40;
	public int tothird = 0;
	

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling teleport command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();

		String CharName = (parameters[0]);

		cur.Budoong = 0;
		
		
		//---------tele player----------\\
		Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		
		while(iter.hasNext()) {
		Map.Entry<Integer, Character> pairs = iter.next();
		tmp = pairs.getValue(); 
		
		if(CharName.equals(tmp.getLOGsetName())) {
		names.put(tmp.getLOGsetName(), tmp);
		Character anyplayer = names.get(CharName); // select player from string
		
		if(anyplayer.getCurrentMap() == 9 && cur.getLevel() < 130){Charstuff.getInstance().respondguild("You need to be at least Level 130 in order to enter Sky Zone.", cur.GetChannel()); return;}
		
		float gox = anyplayer.getlastknownX();
		float goy = anyplayer.getlastknownY();
		int gomap = anyplayer.getCurrentMap();
		
		cur.setX(gox);
		cur.setY(goy);
		cur.setCurrentMap(gomap);
		
		//System.out.println("Handling scroll teleport");
		byte[] chID = BitTools.intToByteArray(cur.getCharID());
		cur.leaveGameWorld();
		source.getWriteBuffer().clear(); //clear all packets pending write(prevent client from crashing as it returns to selection)
		cur.getitemdata();
		cur.statlist(); // refresh statlist after clearing the buffs;
		//ch.recexp(0);
		//ch.setguild();
		byte[] first = new byte[1452];
		byte[] second = new byte[2904];
		
		byte[] fourth = new byte[1452];
		
		for(int i=0;i<1452;i++) {
			first[i] = 0x00;
			second[i] = 0x00;
			fourth[i] = 0x00;
		}
		Calendar Date = Calendar.getInstance(Locale.FRANCE);
		
		byte[] stuffLOL = new byte[] {(byte)0xc0, (byte)0x16, (byte)0x00, (byte)0x00, (byte)0x04, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,  
			(byte)0x00, (byte)	0x00, (byte)0x00, (byte)0x01,  
			(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
			(byte)0x64, 
			(byte)0x00, (byte)0x00, (byte)0x00, 
			(byte)0x08, // YEAR OF MH EXISTANCE
			(byte)0x00, (byte)0x00, (byte)0x00, 
			(byte)0x05, (byte)0x05, (byte)Date.get(Calendar.HOUR_OF_DAY)};// MONTH, DAY, HOUR
		
		byte[] mapz = BitTools.intToByteArray(cur.getCurrentMap());
		for(int i=0;i<2;i++) {
			stuffLOL[20+i] = mapz[i];
		}
		
		byte[] cha = new byte[] {
				(byte)0x0c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x9d, (byte)0x0f, (byte)0xbf, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x2e
			};
		
			byte[] coordinates = new byte[8];
			
			// standard shit 
			byte[] xCoord0 = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord0 = BitTools.floatToByteArray(cur.getlastknownY());
			stuffLOL[20] = (byte)(cur.getCurrentMap()); //map
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord0[i];
				coordinates[i+4] = yCoord0[i];
			}
			
			
			for(int i=0;i<stuffLOL.length;i++) {
				first[i] = stuffLOL[i];
			}
			for(int i=0;i<coordinates.length;i++) {
				fourth[i+(1452-coordinates.length)] = coordinates[i];
			}
			

			//second[712] = (byte)0x01; // <- QUESTS
			
			// friends & ignore list \\
			 for(int a=0;a<24;a++){
			 if(cur.friendslist.containsKey(a)){
			 String friends = cur.friendslist.get(a); 
			 byte[] ininame = BitTools.stringToByteArray(friends);
			 //System.out.println("Friend" +friendlist_count);
				for(int w=0;w<ininame.length;w++){
					second[friendlist_count+w] = ininame[w]; // name
				}}friendlist_count = friendlist_count+17;}
			 friendlist_count = 24;
			 
			 for(int a=0;a<24;a++){
			 if(cur.ignorelist.containsKey(a)){
			 String ignore = cur.ignorelist.get(a); 
			 byte[] ininame = BitTools.stringToByteArray(ignore);
			 //System.out.println("Ignore" +ignorelist_count);
				for(int w=0;w<ininame.length;w++){
					second[ignorelist_count+w] = ininame[w]; // name
				}}ignorelist_count = ignorelist_count+17;}
			 ignorelist_count = 534;
			 
			 
				// cargo storage \\
			 for(int a=0;a<120;a++){
			 if(cur.CargoSLOT.containsKey(a)){
			 int InvSLOT = cur.getCargoSLOT(a); 
			 int InvY = cur.getCargoHEIGHT(a); 
			 int InvX = cur.getCargoWEIGHT(a); 
			 int InvSTACK = cur.getCargoSTACK(a); 
			 byte[] invSLOT = BitTools.intToByteArray(InvSLOT);
			 byte[] invSTACK = BitTools.intToByteArray(InvSTACK);
			 
				first[cargo_count-2] = (byte)InvY; 
				first[cargo_count-1] = (byte)InvX; 
				for(int w=0;w<4;w++){
					first[cargo_count+w] = invSLOT[w]; //itemd id
				}
				if(cur.item_end_date.containsKey(cur.getCargoSLOT(a))){	
					first[cargo_count-3] = (byte)0xff; 
				long timedate = cur.item_end_date.get(cur.getCargoSLOT(a)) / 1000;
				byte[] TimenDate = BitTools.intToByteArray((int)timedate);
				for(int w=0;w<4;w++){
					first[cargo_count+4+w] = TimenDate[w]; 	
				}
				}else{
				for(int w=0;w<2;w++){
					first[cargo_count+4+w] = invSTACK[w]; //stack			
				}}
				}
			 	cargo_count = cargo_count+12;
			 }
			 cargo_count = 40;
			 
			 
				// inventory \\
			 for(int a=0;a<121;a++){
			 if(cur.InventorySLOT.containsKey(a) && cur.getInventorySLOT(a) != 0 && cur.getInventorySTACK(a) != 0){ // if contains key and itemid does exist
			 //System.out.println("InventorySLOT" +a+" : "+ch.getInventorySLOT(a)+" - "+ch.getInventorySTACK(a));
			 int InvSLOT = cur.getInventorySLOT(a); 
			 int InvY = cur.getInventoryHEIGHT(a); 
			 int InvX = cur.getInventoryWEIGHT(a); 
			 int InvSTACK = cur.getInventorySTACK(a); 
			 byte[] invSLOT = BitTools.intToByteArray(InvSLOT);
			 byte[] invSTACK = BitTools.intToByteArray(InvSTACK);
			 
			 
			 second[inventory_count-2] = (byte)InvY; 
			 second[inventory_count-1] = (byte)InvX; 
				for(int w=0;w<4;w++){
					 second[inventory_count+w] = invSLOT[w];
				}
				
				if(grinditems.grinditems.containsKey(cur.getInventorySLOT(a))){
				second[inventory_count-3] = (byte)0xfe;
				int Durance = grinditems.grinditems.get(cur.getInventorySLOT(a));
				byte[] durance = BitTools.intToByteArray(Durance);
				for(int w=0;w<4;w++){
						 second[inventory_count+4+w] = durance[w]; 			
				}
				}else
				if(cur.item_end_date.containsKey(cur.getInventorySLOT(a))){	
				second[inventory_count-3] = (byte)0xff; 
				long timedate = cur.item_end_date.get(cur.getInventorySLOT(a)) / 1000;
				byte[] TimenDate = BitTools.intToByteArray((int)timedate);
				for(int w=0;w<4;w++){	
						 second[inventory_count+4+w] = TimenDate[w]; 			
				}
				}else{
				for(int w=0;w<2;w++){
					second[inventory_count+4+w] = invSTACK[w]; //stack			
				}}
				}else{cur.DeleteInvItem(a);} // else if itemid == 0 then delete the whole set
			 // if stack is not set (i higly doubt that then tohird = 1;
			 inventory_count = inventory_count+12;
			 }
			 inventory_count = 800;
			 
			 // gold
			 byte[] komkkrr = BitTools.LongToByteArrayREVERSE(cur.getgold());
			 for(int w=0;w<komkkrr.length;w++){
				 fourth[772+w] = komkkrr[w]; // GOLD xd
			 }
			 
			 
			 // skillbar
			  for(int a=0;a<22;a++){
			 if(cur.skillbar.containsKey(a)){
			 //System.out.println("Skillbar" +skillbar_count);
			 int skillbar = cur.skillbar.get(a); 
			 if(skillbar > 100){ //100 just to be sure
			 byte[] ininame = BitTools.intToByteArray(skillbar);
			  fourth[skillbar_count-4] = (byte)0x01;// 0x01 = item
				for(int w=0;w<4;w++){
					fourth[skillbar_count+w] = ininame[w]; // itemid, or skill_slot_id
				}
			 }else{
			 fourth[skillbar_count-4] = (byte)0x02;// 0x02 = skill
			 fourth[skillbar_count] = (byte)skillbar;
			 }}skillbar_count = skillbar_count+8;}
			 skillbar_count = 788;
			 
			 
			 // skills
			  for(int a=0;a<60;a++){
			 if(cur.skills.containsKey(a)){
			 //System.out.println("skills" +skill_count);
			 int skill = cur.skills.get(a); 
			 byte[] ininame = BitTools.intToByteArray(skill);
			 fourth[skill_count+4] = (byte)0x04;
				for(int w=0;w<4;w++){
					fourth[skill_count+w] = ininame[w]; // skill
				}}skill_count = skill_count+8;}
			  skill_count = 964;
			 
			
			
			for(int i=0;i<stuffLOL.length;i++) {
				first[i] = stuffLOL[i];
			}
			
			for(int i=0;i<coordinates.length;i++) {
				fourth[i+(1452-coordinates.length)] = coordinates[i];
			}
			
			byte[] healpckt = new byte[32];
			healpckt[0] = (byte)healpckt.length;
			healpckt[4] = (byte)0x05;
			healpckt[6] = (byte)0x35;
			healpckt[8] = (byte)0x08; 
			healpckt[9] = (byte)0x60; 
			healpckt[10] = (byte)0x22;
			healpckt[11] = (byte)0x45;
			for(int i=0;i<4;i++) {healpckt[12+i] = chID[i];}
			byte[] hp = BitTools.intToByteArray(cur.getHp());  
			byte[] mana = BitTools.intToByteArray(cur.getMana()); 
			byte[] stam = BitTools.intToByteArray(cur.getStamina()); 
			healpckt[24] = hp[0];
			healpckt[25] = hp[1];
			healpckt[28] = mana[0];
			healpckt[29] = mana[1];
			healpckt[30] = stam[0];
			healpckt[31] = stam[1];			
			healpckt[16] = (byte)0x03; // guild type?
			if(cur.guild != null){
		    int Key = BitTools.ValueToKey(cur.getCharID(), cur.guild.guildids); // how to get it without conquerent exception
			healpckt[18] = (byte)cur.guild.getguildranks(Key); //guild rank idk why
			}
			
					synchronized(source.getWriteBuffer()) {
						source.addWriteButDoNotFlipInterestOps(first);
						source.addWriteButDoNotFlipInterestOps(second);
						source.addWriteButDoNotFlipInterestOps(fourth);
						source.addWriteButDoNotFlipInterestOps(cha);
						source.addWriteButDoNotFlipInterestOps(healpckt);
				 	}
					source.flipOps(SelectionKey.OP_WRITE);
					cur.joinGameWorld();

		cur.sendToMap(cur.extCharPacket());	
		System.out.println("Summoned: " + CharName);
		}
		}
}}
		
		
  





	