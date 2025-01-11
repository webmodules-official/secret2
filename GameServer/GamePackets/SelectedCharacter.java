
package GameServer.GamePackets;


import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Connections.Connection;
import java.nio.channels.SelectionKey;

import timer.SystemTimer;
import timer.globalsave;
import Database.CharacterDAO;
import Encryption.Decryptor;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Player.Character;
import Player.grinditems;
import Player.manuals;
import Player.skillpasives;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;

public class SelectedCharacter implements Packet {
	public int skill_count = 964;
	public int skillbar_count = 788;
	public int friendlist_count = 24;
	public int ignorelist_count = 534;
	public int inventory_count = 800;
	public int cargo_count = 40;
	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}
	

	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		Character ch = null;
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];

		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		Player polishPlayer = ((PlayerConnection)con).getPlayer();
		if(polishPlayer != null) {
			ch = polishPlayer.getCharacters().get((int)decrypted[0]);
			polishPlayer.setActiveCharacter(ch);
			//Character tempMofo = CharacterDAO.loadCharacter(ch.getCharID());
			//sz,rc,mid
			if(!ch.InventorySLOT.containsValue(273001251)){
				if(ch.getCurrentMap() == 7||ch.getCurrentMap() == 8||ch.getCurrentMap() == 9 && !ch.InventorySLOT.containsValue(283000235)){
				ch.setCurrentMap(1);
				ch.setX(-1660);
				ch.setY(2344);
				}
			}
			if(!ch.InventorySLOT.containsValue(283000235)){
				if(ch.getCurrentMap() == 9 && !ch.InventorySLOT.containsValue(273001251)){
				ch.setCurrentMap(1);
				ch.setX(-1660);
				ch.setY(2344);
				}
			}
			
			// Must be on top when going obt
			//ch.setCurrentMap(tempMofo.getCurrentMap());
			//ch.setX(tempMofo.getlastknownX());
			//ch.setY(tempMofo.getlastknownY());
			/*==========================================*/
			
			ch.partyUID = 0; // remove char from any party
			ch.furycheck = 0; // set furt OFF
			ch.Budoong = 0;// Budoong OFF
			ch.TempStoreBuffs.clear(); // clear temp buffs
			ch.getitemdata();
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
		
		byte[] mapz = BitTools.intToByteArray(polishPlayer.getActiveCharacter().getCurrentMap());
		for(int i=0;i<2;i++) {
			stuffLOL[20+i] = mapz[i];
		}
		
		byte[] cha = new byte[] {
				(byte)0x0c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x9d, (byte)0x0f, (byte)0xbf, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x2e
			};
		
		byte[] coordinates = new byte[8];
		byte[] xCoord = BitTools.floatToByteArray(ch.getlastknownX());
		byte[] yCoord = BitTools.floatToByteArray(ch.getlastknownY());
		byte[] chID = BitTools.intToByteArray(ch.getCharID());
		
		for(int i=0;i<4;i++) {
			stuffLOL[i+12] = chID[i];
			coordinates[i] = xCoord[i];
			coordinates[i+4] = yCoord[i];
		}

		//second[712] = (byte)0x01; // <- QUESTS
		
		// friends & ignore list \\
		 for(int a=0;a<24;a++){
		 if(ch.friendslist.containsKey(a)){
		 String friends = ch.friendslist.get(a); 
		 byte[] ininame = BitTools.stringToByteArray(friends);
		 //System.out.println("Friend" +friendlist_count);
			for(int w=0;w<ininame.length;w++){
				second[friendlist_count+w] = ininame[w]; // name
			}}friendlist_count = friendlist_count+17;}
		 friendlist_count = 24;
		 
		 for(int a=0;a<24;a++){
		 if(ch.ignorelist.containsKey(a)){
		 String ignore = ch.ignorelist.get(a); 
		 byte[] ininame = BitTools.stringToByteArray(ignore);
		 //System.out.println("Ignore" +ignorelist_count);
			for(int w=0;w<ininame.length;w++){
				second[ignorelist_count+w] = ininame[w]; // name
			}}ignorelist_count = ignorelist_count+17;}
		 ignorelist_count = 534;
		 
		 
			// cargo storage \\
		 for(int a=0;a<120;a++){
		 if(ch.CargoSLOT.containsKey(a)){
		 int InvSLOT = ch.getCargoSLOT(a); 
		 int InvY = ch.getCargoHEIGHT(a); 
		 int InvX = ch.getCargoWEIGHT(a); 
		 int InvSTACK = ch.getCargoSTACK(a); 
		// System.out.println("CargoSLOT: " +a+" - "+ch.getCargoSLOT(a)+" - "+ch.getCargoSTACK(a));
		 byte[] invSLOT = BitTools.intToByteArray(InvSLOT);
		 byte[] invSTACK = BitTools.intToByteArray(InvSTACK);
		 
			first[cargo_count-2] = (byte)InvY; 
			first[cargo_count-1] = (byte)InvX; 
			for(int w=0;w<4;w++){
				first[cargo_count+w] = invSLOT[w]; //itemd id
			}
			if(ch.item_end_date.containsKey(ch.getCargoSLOT(a))){	
				first[cargo_count-3] = (byte)0xff; 
			long timedate = ch.item_end_date.get(ch.getCargoSLOT(a)) / 1000;
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
		 if(ch.InventorySLOT.containsKey(a) && ch.getInventorySLOT(a) != 0 && ch.getInventorySTACK(a) != 0){ // if contains key and itemid does exist
		 //System.out.println("InventorySLOT" +a+" : "+ch.getInventorySLOT(a)+" - "+ch.getInventorySTACK(a));
		 int InvSLOT = ch.getInventorySLOT(a); 
		 int InvY = ch.getInventoryHEIGHT(a); 
		 int InvX = ch.getInventoryWEIGHT(a); 
		 int InvSTACK = ch.getInventorySTACK(a); 
		 byte[] invSLOT = BitTools.intToByteArray(InvSLOT);
		 byte[] invSTACK = BitTools.intToByteArray(InvSTACK);
		 
		 
		 second[inventory_count-2] = (byte)InvY; 
		 second[inventory_count-1] = (byte)InvX; 
			for(int w=0;w<4;w++){
				 second[inventory_count+w] = invSLOT[w];
			}
			
			if(grinditems.grinditems.containsKey(ch.getInventorySLOT(a))){
			second[inventory_count-3] = (byte)0xfe;
			int Durance = grinditems.grinditems.get(ch.getInventorySLOT(a));
			byte[] durance = BitTools.intToByteArray(Durance);
			for(int w=0;w<4;w++){
					 second[inventory_count+4+w] = durance[w]; 			
			}
			}else
			if(ch.item_end_date.containsKey(ch.getInventorySLOT(a))){	
			second[inventory_count-3] = (byte)0xff; 
			long timedate = ch.item_end_date.get(ch.getInventorySLOT(a)) / 1000;
			byte[] TimenDate = BitTools.intToByteArray((int)timedate);
			for(int w=0;w<4;w++){	
					 second[inventory_count+4+w] = TimenDate[w]; 			
			}
			}else{
			for(int w=0;w<2;w++){
				second[inventory_count+4+w] = invSTACK[w]; //stack			
			}}
			}else{ch.DeleteInvItem(a);} // else if itemid == 0 then delete the whole set
		 // if stack is not set (i higly doubt that then tohird = 1;
		 inventory_count = inventory_count+12;
		 }
		 inventory_count = 800;

		 
		 // gold
		 byte[] komkkrr = BitTools.LongToByteArrayREVERSE(ch.getgold());
		 for(int w=0;w<komkkrr.length;w++){
			 fourth[772+w] = komkkrr[w]; // GOLD xd
		 }
		
		 
		 // skillbar
		  for(int a=0;a<22;a++){
		 if(ch.skillbar.containsKey(a)){
		 //System.out.println("Skillbar" +skillbar_count);
		 int skillbar = ch.skillbar.get(a); 
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
		 if(ch.skills.containsKey(a)){

		 int skill = ch.skills.get(a); 
		 //System.out.println("skills" +skill_count+" - "+skill);
		 
		 //if its a passive buff
		 if(skillpasives.tryskillpassives(skill)){
			String e = skillpasives.getskillpassives(skill);
			//System.out.println("WINNER -> " +skill+" - "+e);
			String[] Passive = e.split(",");	
					
			//TypeID (what cathegory is this passive)
			int TypeID = Integer.valueOf(Passive[0]); 
			int Value1 = Integer.valueOf(Passive[1]); 
			int Value2 = Integer.valueOf(Passive[2]); 
			
			// If type == HP
			if(TypeID == 1){ch.setTempPassives(1,Value1);}//done
			
			// If type == Damage
			if(TypeID == 2){ch.setTempPassives(2,Value1);}//done
			
			// If type == Life Recovery
			if(TypeID == 3){ch.setTempPassives(3,Value1);}//done
			
			// If type == Defence 
			if(TypeID == 4){ch.setTempPassives(4,Value1);}//done
			
			// If type == Critical Chance Rate
			if(TypeID == 6){ch.setTempPassives(6,Value1);}//done
			
			// If type == Attack Distance
			if(TypeID == 7){ch.setTempPassives(7,Value1);}//done
			
			// If type == Critical Bonus Damage
			if(TypeID == 8){ch.setTempPassives(8,Value1);}//done
			
			// If type == Mana
			if(TypeID == 9){ch.setTempPassives(9,Value1);}//done
			
			// If type == Defence + HP
			if(TypeID == 10){ch.setTempPassives(10,Value1);ch.setTempPassives(100,Value2);}//done
			
			// If type == Offence Succes Rate
			if(TypeID == 11){ch.setTempPassives(11,Value1);}//done	
			
			// If type == Mana Recovery
			if(TypeID == 12){ch.setTempPassives(12,Value1);}//done	 
			 
		 }
		 
		 byte[] ininame = BitTools.intToByteArray(skill);
		 fourth[skill_count+4] = (byte)0x04;
			for(int w=0;w<4;w++){
				fourth[skill_count+w] = ininame[w]; // skill
			}}skill_count = skill_count+8;}
		  skill_count = 964;
		 
		ch.statlist(); // refresh statlist after clearing the buffs;
		
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
		byte[] hp = BitTools.intToByteArray(ch.getHp());  
		byte[] mana = BitTools.intToByteArray(ch.getMana()); 
		byte[] stam = BitTools.intToByteArray(ch.getStamina()); 
		healpckt[24] = hp[0];
		healpckt[25] = hp[1];
		healpckt[28] = mana[0];
		healpckt[29] = mana[1];
		healpckt[30] = stam[0];
		healpckt[31] = stam[1];			
		healpckt[16] = (byte)0x03; // guild type?
		if(ch.guild != null){
	    int Key = BitTools.ValueToKey(ch.getCharID(), ch.guild.guildids); // how to get it without conquerent exception
		healpckt[18] = (byte)ch.guild.getguildranks(Key); //guild rank idk why
		}
		
				synchronized(con.getWriteBuffer()) {
				con.addWriteButDoNotFlipInterestOps(first);
				con.addWriteButDoNotFlipInterestOps(second);
				con.addWriteButDoNotFlipInterestOps(fourth);
				con.addWriteButDoNotFlipInterestOps(cha);
				con.addWriteButDoNotFlipInterestOps(healpckt);
			 	}
				con.flipOps(SelectionKey.OP_WRITE);
				//polishPlayer.getActiveCharacter().getPlayer().Rarea = true;
			//polishPlayer.getActiveCharacter().joinGameWorld();
			ch.blackslave(ch.getexp());
			ch.vpz(chID, false);
			Charstuff.getInstance().Arespond("Welcome to my Martial Heroes Private Server.",con);
		
		// remove the ring here
		
		ch.getPlayer().EL = 0;
		
		}
		return null;
	}	
}
