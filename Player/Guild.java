package Player;

import item.ItemCache;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;
import GameServer.GamePackets.UseItemParser;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


/*
 * party.class
 * Stores all party data 
 */

public class Guild implements Cloneable{
	public int inc = 1;
	public int GuildID;
	public int guildtype;
	public int guildfame;
	private int guildpvprating;
	public int guildgold;
	public int guildhat;
	public int guildicon, guildkills,guilddeaths; 
	public String guildname;
	private byte[] guildnews = new byte[196]; 
	private WMap wmap = WMap.getInstance();
	
	//GUILD PLAYER STUFF           |         int initialCapacity, float loadFactor, int concurrencyLevel
		public ConcurrentMap<Integer, Integer> guildids = new ConcurrentHashMap<Integer, Integer>(100, 4, 100);
		public ConcurrentMap<Integer, String> guildnames = new ConcurrentHashMap<Integer, String>(50, 2, 50);
		public ConcurrentMap<Integer, Integer> guildranks = new ConcurrentHashMap<Integer, Integer>(50, 2, 50);
		public ConcurrentMap<Integer, Integer> guildclass = new ConcurrentHashMap<Integer, Integer>(50, 2, 50);
		public ConcurrentMap<Integer, Integer> guildfames = new ConcurrentHashMap<Integer, Integer>(50, 2, 50);
		public ConcurrentMap<String, Integer> War = new ConcurrentHashMap<String, Integer>();
		public ConcurrentMap<String, Integer> Alliance = new ConcurrentHashMap<String, Integer>();
		
		
		public void setWar(String one, int two) {
			War.put(one, two); 
		}	
		
		public int getWar(String one) {
			if(War.containsKey(one)){return War.get(one);}else{return 0;}
		}
		
		public void setAlliance(String one, int two) {
			Alliance.put(one, two); 
		}	
		
		public int getAlliance(String one) {
			if(Alliance.containsKey(one)){return Alliance.get(one);}else{return 0;}
		}
			
			//guild fames
			public int getguildclass(int key) {
				if(guildclass.containsKey(key)){
					int guildvalue = guildclass.get(key);
					//System.out.println("guildfames: " +key+" - " +guildvalue);
					return guildvalue;}else
					{ //System.out.println(guildslot+" - null "); 
					return 0;}
			}
			public void setguildclass(int key, int guildvalue) {
				guildclass.put(Integer.valueOf(key), Integer.valueOf(guildvalue)); 
				////System.out.println("guildids: " +guildslot+" - " +guildvalue);
			}
			
			//guild fames
			public int getguildfames(int key) {
				if(guildfames.containsKey(key)){
					int guildvalue = guildfames.get(key);
					//System.out.println("guildfames: " +key+" - " +guildvalue);
					return guildvalue;}else
					{ //System.out.println(guildslot+" - null "); 
					return 0;}
			}
			public void setguildfames(int key, int guildvalue) {
				guildfames.put(Integer.valueOf(key), Integer.valueOf(guildvalue)); 
				////System.out.println("guildids: " +guildslot+" - " +guildvalue);
			}
			
			//guild ranks
			public int getguildranks(int key) {
				if(guildranks.containsKey(key)){
					int guildvalue = guildranks.get(key);
					//System.out.println("guildranks: " +key+" - " +guildvalue);
					return guildvalue;}else
					{ //System.out.println(guildslot+" - null "); 
					return 0;}
			}
			public void setguildranks(int key, int guildvalue) {
				guildranks.put(Integer.valueOf(key), Integer.valueOf(guildvalue)); 
				////System.out.println("guildids: " +guildslot+" - " +guildvalue);
			}
			
			//guild ids
			public int getguildids(int guildslot) {
				if(guildids.containsKey(guildslot)){
					int guildvalue = guildids.get(guildslot);
					//System.out.println("guildids: " +guildslot+" - " +guildvalue);
					return guildvalue;}else
					{ //System.out.println(guildslot+" - null "); 
					return 0;}
			}
			public void setguildids(int guildslot, int guildvalue) {
				guildids.put(Integer.valueOf(guildslot), Integer.valueOf(guildvalue)); 
				////System.out.println("guildids: " +guildslot+" - " +guildvalue);
			}
			
			public void setguildnames(int guildslot, String namesaz) {
				guildnames.put(Integer.valueOf(guildslot), namesaz); 
				////System.out.println("guildnames: " +guildslot+" - " +namesaz);
			}	
			
			public String getguildnames(int i) {
				if(guildnames.containsKey(i)){
				String one = guildnames.get(i);
				//System.out.println("guildnames: " +i+" - " +one);
				return one;}else{return "0";}
			}
		
		public int getGuildID(){
			return GuildID;
		}	
		
		public void setGuildID(int GuildID){	
			this.GuildID = GuildID;	
		}
	

		//add member
		public void addmember(Character cur) {
		if(cur != null){
			//lookup which spots are empty in within the 50 spots
			int SLOT = 1337;
			for(int i=1;i<50;i++){
				SLOT = this.getguildids(i) ;
				if(SLOT == 0){ SLOT = i; break;} // slot 0 = free slot so make SLOT = Key afterwards
			}
			//System.out.println("member free slot : " +SLOT);
			//add member
			if(SLOT != 1337){
			ManageMember(SLOT, cur.getCharID(), cur.getLOGsetName(), 3, cur.getCharacterClass(), 0);// CharID, Name, Rank(member), fame(starting 0)
			}else{// tell them there is no free slot left in guild. 
			Charstuff.getInstance().respondguild("No free slots left in guild.", cur.GetChannel()); 	
			}
		}}
		
		//remove meember
		public void removemember(int SLOT) {
			guildids.remove(SLOT);
			guildnames.remove(SLOT); 
			guildranks.remove(SLOT); 
			guildclass.remove(SLOT); 
			guildfames.remove(SLOT); 
		}
		
		//4 way add
		public void ManageMember(int SLOT, int CharID, String Charname, int Rank, int Charclass, int Fame) {
			guildids.put(SLOT, CharID);
			guildnames.put(SLOT, Charname); 
			guildranks.put(SLOT, Rank); 
			guildclass.put(SLOT, Charclass); 
			guildfames.put(SLOT, Fame); 
		}
	

		public void SendToGuildWithTheirCharID(byte[] buf){
			Iterator<Map.Entry<Integer, Integer>> iter = this.guildids.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Integer, Integer> pairs = iter.next();
				int CharID = pairs.getValue();
				if(wmap.CharacterExists(CharID)){
					Character tmp = wmap.getCharacter(CharID);
					if(tmp != null){
						byte[] chid = BitTools.intToByteArray(tmp.getCharID());
						for(int a=0;a<4;a++){buf[12+a] = chid[a];}
						ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), buf);
					}}
			}
		}
		
		public void SendToGuild(byte[] buf){
			Iterator<Map.Entry<Integer, Integer>> iter = this.guildids.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Integer, Integer> pairs = iter.next();
				int CharID = pairs.getValue();
				if(wmap.CharacterExists(CharID)){
					Character tmp = wmap.getCharacter(CharID);
					if(tmp != null){
						ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), buf);
					}}
			}
		}
		
		//guildies sending to map
		public void ExternalWindowRefreshToMap(){
			Iterator<Map.Entry<Integer, Integer>> iter = this.guildids.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Integer, Integer> pairs = iter.next();
				int CharID = pairs.getValue();
				if(wmap.CharacterExists(CharID)){
					Character tmp = wmap.getCharacter(CharID);
					if(tmp != null){
						tmp.sendToMap(tmp.extCharGuild());
					}}
			}
		}
		
		public void refreshWarAlliance(Character tmp) {
			byte[] chid = BitTools.intToByteArray(tmp.getCharID());
			byte[] iniguild6 = new byte[260]; 
			iniguild6[0] = (byte)0x20; 
			iniguild6[4] = (byte)0x04; 
			iniguild6[6] = (byte)0x61; 
			iniguild6[8] = (byte)0x01; 
			for(int a=0;a<4;a++) {
				iniguild6[12+a] = chid[a];
				iniguild6[44+a] = chid[a];
			}
			iniguild6[32] = (byte)0xe4; 
			iniguild6[36] = (byte)0x04; 
			iniguild6[38] = (byte)0x51; 
			iniguild6[40] = (byte)0x01;
			iniguild6[48] = (byte)0x01;
			iniguild6[50] = (byte)0x06;
			
			
			byte[] mygname = this.guildname.getBytes();
			for(int a=0;a<mygname.length;a++) {
				iniguild6[51+a] = mygname[a];
			}
			//Wars
			iniguild6[68] = (byte)0x88;
			iniguild6[70] = (byte)0x90;
			iniguild6[72] = (byte)0x91;
			iniguild6[74] = (byte)0x92;
			iniguild6[76] = (byte)0x93;
			int inc1 = 0;
			int inc11 = 0;
			Iterator<Entry<String, Integer>> iter = this.War.entrySet().iterator();
			while(iter.hasNext()) {
			Entry<String, Integer> pairs = iter.next();
			inc1++;
			if(inc1 >= 6){break;}
			byte[] wargname = pairs.getKey().getBytes();
			for(int a=0;a<wargname.length;a++) {
				iniguild6[78+inc11+a] = wargname[a];
			}
			inc11 = inc11 + 17;
			}
			iniguild6[64] = (byte)inc1; //how many guildwars


			//Alliance
			iniguild6[164] = (byte)0xd7; 
			iniguild6[166] = (byte)0xd8; 
			iniguild6[168] = (byte)0xd9; 
			iniguild6[170] = (byte)0xda; 
			iniguild6[172] = (byte)0xdb; 
			int inc2 = 0;
			int inc22 = 0;
			Iterator<Entry<String, Integer>> iter1 = this.Alliance.entrySet().iterator();
			while(iter1.hasNext()) {
			Entry<String, Integer> pairs1 = iter1.next();
			inc2++;
			if(inc2 >= 6){break;}
			byte[] Alliancegname = pairs1.getKey().getBytes();
			for(int a=0;a<Alliancegname.length;a++) {
				iniguild6[174+inc22+a] = Alliancegname[a]; // guild news
			}
			inc22 = inc22 + 17;
			}
			iniguild6[234] = (byte)inc2; 
			
			ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), iniguild6);
		}	

		public void RefreshOnlyForMe(Character tmp){
						byte[] chid = BitTools.intToByteArray(tmp.getCharID());

						// INI GUILD WINDOW
						byte[] iniguild3 = new byte[1452]; 
						byte[] iniguild4 = new byte[192]; // +8
						iniguild3[0] = (byte)0x20;
						iniguild3[4] = (byte)0x04;
						iniguild3[6] = (byte)0x61;
						iniguild3[8] = (byte)0x01;
						for(int a=0;a<4;a++) {
							iniguild3[12+a] = chid[a];
							iniguild3[44+a] = chid[a];
						}
						iniguild3[32] = (byte)0x4c; 
						iniguild3[33] = (byte)0x06; 
						iniguild3[36] = (byte)0x04; 
						iniguild3[38] = (byte)0x41; 
						iniguild3[40] = (byte)0x01; 
						
						
						iniguild3[48] = (byte)0x01; 
						if(this.getguildname() == null){return;}
						byte[] ininame = BitTools.stringToByteArray(this.getguildname());
						for(int a=0;a<ininame.length;a++) {
							iniguild3[50+a] = ininame[a]; 
						}
				        byte[] guildicon = BitTools.intToByteArray(this.guildicon); // guild ID ?? (or icon)
				        for(int i=0;i<2;i++){
				        	iniguild3[i+68] = guildicon[i]; 
				        }
						iniguild3[70] = (byte)this.getGuildtype(); 	
						
						
						//guildscore
						int guildscorefame = this.guildfame / 100;
						int guildscoregold = this.guildgold / 10;
						byte[] GUILDSCORE = BitTools.intToByteArray(guildscorefame + guildscoregold);
						for(int a=0;a<GUILDSCORE.length;a++) {
							iniguild3[72+a] = GUILDSCORE[a]; 
						}
						
						
						//guildfame
						byte[] FAME = BitTools.intToByteArray(this.getGuildfame());
						for(int i=0;i<4;i++){
							iniguild3[76+i] = FAME[i]; //fame donate
						}
						
						//guildgold
						byte[] GOLD = BitTools.LongToByteArrayREVERSE(this.getGuildgold());
						 for(int w=0;w<GOLD.length;w++){
							 iniguild3[80+w] = GOLD[w]; //gold donate
						 }
						
						//guildfame
						//iniguild3[76] = (byte)0x4c; 
						//iniguild3[77] = (byte)0xe5; 
						
						//guildgold
						//iniguild3[80] = (byte)0x95; 
						//iniguild3[81] = (byte)0x05; 
						//TODO: get uh byte : HAT
						
						 iniguild3[88] = (byte)this.getGuildhat(); 
						 //iniguild3[90] = ????
						
						
						
						//Order them all in the LIST
						int NT = 92;
						int PN = 342;
						int PF = 1244;
						int PO = 1444;
						for(int a=0;a<50;a++) {
							//player charids
							byte[] Gchid = BitTools.intToByteArray(this.getguildids(a));
							for(int y=0;y<4;y++) {
							iniguild3[NT+y] = Gchid[y]; //guild master 96+a = further  members
							}
							
							// player names
							if(this.guildnames.containsKey(a)){
							byte[] Gname = BitTools.stringToByteArray(this.getguildnames(a));
							for(int y=0;y<Gname.length;y++) {
								iniguild3[PN+y] = Gname[y]; // 1st character Name and so on
							}
							}
							
							//players guild rank -> 7 = MASTER | 6 = SUB- | 5 = Senior | 4 = HYANG| 3 = member
							iniguild3[292+a] = (byte)this.getguildranks(a); // 1st character
							
							
							//players class
							iniguild3[1192+a] = (byte)this.getguildclass(a); 
							
							//players fame donated
							byte[] Gfame = BitTools.intToByteArray(this.getguildfames(a));
							for(int y=0;y<4;y++) {
							iniguild3[PF+y] = Gfame[y];
							}
							
							//online player check
							if(wmap.CharacterExists(this.getguildids(a))){
							Character tmp1 = wmap.getCharacter(this.getguildids(a));
							if(tmp1 != null){
							if(PO >= 1400){	
							iniguild3[PO] = (byte)0x01; // 1 = online | 0 = offline
							}else{
							iniguild4[PO] = (byte)0x01; // 1 = online | 0 = offline	
							}
							}}
							
							
							PO = PO + 4;
							if(PO == 1452){PO = 0;}
							PF = PF + 4;
							NT = NT + 4;
							PN = PN + 17;
						}
						
						// Guild News
						byte[] iniguild5 = new byte[212];
						iniguild5[0] = (byte)0xd4; 
						iniguild5[4] = (byte)0x04; 
						iniguild5[6] = (byte)0x67; 
						iniguild5[8] = (byte)0x01; 
						for(int a=0;a<4;a++) {
							iniguild5[12+a] = chid[a];
						}
						
						byte[] guildtext = this.getguildnews();
						for(int a=0;a<guildtext.length;a++) {
							iniguild5[16+a] = guildtext[a]; // guild news
						}
						

						
						
						if(ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null){
						Connection con = ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel());
						synchronized(con.getWriteBuffer()) {
							con.addWrite(iniguild3);
							con.addWrite(iniguild4);
							con.addWrite(iniguild5);
						}
						this.refreshWarAlliance(tmp);
						this.getguildbuffs(tmp);
						}
		}
		
		//get guildbuffs from this guild and alliances guild
		public void getguildbuffs(Character cur) {
			//only 2 buffs per guild
			int slotcount = 0;
			cur.bufficon1 = 0;
			cur.bufficon1 = 0;
			
			// this guild
			if(Charstuff.getInstance().getguildbuffs(this.GuildID) != 0){
				slotcount++;
				this.executebuff(cur, Charstuff.getInstance().getguildbuffs(this.GuildID), slotcount);
			}
			// alliances
			Iterator<Entry<String, Integer>> iter = this.Alliance.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Integer> pairs = iter.next();
				if(Charstuff.getInstance().getguildbuffs(pairs.getValue()) != 0){
					slotcount++;
					this.executebuff(cur, Charstuff.getInstance().getguildbuffs(pairs.getValue()), slotcount);
				}
				if(slotcount >= 2){break;}
			}
			
		}
		
		public void executebuff(Character cur, int bufficonid, int slotcount){
		if(bufficonid <= 0){return;}
		if(slotcount == 1){cur.bufficon1 = bufficonid;}
		if(slotcount == 2){cur.bufficon2 = bufficonid;}
		
		//get the buff
		int buffid1 = bufficonid;
		int bufftime1 = 9999;
		int buffvalue1 = 5; // 5%
		if(bufficonid == 98){buffvalue1 = 500;} //mana
		if(bufficonid == 99){buffvalue1 = 500;} //hp
		int buffslot1 = slotcount;// replace this 

		
		//manage the buffs serverside
		// or do it on Char side?
		
		//send packet of icon visual
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] buff = new byte[44];
		 buff[0] = (byte)0x2c; 
		 buff[4] = (byte)0x05;
		 buff[6] = (byte)0x1f;
		 buff[8] = (byte)0x01;
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
		 
		 byte[] buffidz1 = BitTools.intToByteArray(buffid1); 
		 byte[] bufftimez1 = BitTools.intToByteArray(bufftime1);
		 byte[] buffvaluez1 = BitTools.intToByteArray(buffvalue1);
		 byte[] buffslotz1 = BitTools.intToByteArray(buffslot1);
		 
			 for(int i=0;i<2;i++) {
				 buff[i+16] = buffslotz1[i];	 // buffslot
				 buff[i+20] = buffidz1[i];	 // buff id
				 buff[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
				 buff[i+24] = buffvaluez1[i]; // Value XXXXX
			 }	
			 cur.statlist();
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), buff); 
		}
		
		public void RefreshEXCEPT(int ExceptCharID){
			Iterator<Map.Entry<Integer, Integer>> iter = this.guildids.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Integer, Integer> pairs = iter.next();
				int CharID = pairs.getValue();
				if(wmap.CharacterExists(CharID) && ExceptCharID != CharID){
					Character tmp = wmap.getCharacter(CharID);
					if(tmp != null){
						byte[] chid = BitTools.intToByteArray(tmp.getCharID());

						// INI GUILD WINDOW
						byte[] iniguild3 = new byte[1452]; 
						byte[] iniguild4 = new byte[192]; // +8
						iniguild3[0] = (byte)0x20;
						iniguild3[4] = (byte)0x04;
						iniguild3[6] = (byte)0x61;
						iniguild3[8] = (byte)0x01;
						for(int a=0;a<4;a++) {
							iniguild3[12+a] = chid[a];
							iniguild3[44+a] = chid[a];
						}
						iniguild3[32] = (byte)0x4c; 
						iniguild3[33] = (byte)0x06; 
						iniguild3[36] = (byte)0x04; 
						iniguild3[38] = (byte)0x41; 
						iniguild3[40] = (byte)0x01; 
						
						iniguild3[48] = (byte)0x01; 
						
						byte[] ininame = BitTools.stringToByteArray(this.getguildname());
						for(int a=0;a<ininame.length;a++) {
							iniguild3[50+a] = ininame[a]; 
						}
				        byte[] guildicon = BitTools.intToByteArray(this.guildicon); // guild ID ?? (or icon)
				        for(int i=0;i<2;i++){
				        	iniguild3[i+68] = guildicon[i]; 
				        }
						iniguild3[70] = (byte)this.getGuildtype(); 	
						
						
						//guildscore
						int guildscorefame = this.guildfame / 100;
						int guildscoregold = this.guildgold / 10;
						byte[] GUILDSCORE = BitTools.intToByteArray(guildscorefame + guildscoregold);
						for(int a=0;a<GUILDSCORE.length;a++) {
							iniguild3[72+a] = GUILDSCORE[a]; 
						}
						
						//guildfame
						byte[] FAME = BitTools.intToByteArray(this.getGuildfame());
						for(int i=0;i<4;i++){
							iniguild3[76+i] = FAME[i]; //fame donate
						}
						
						//guildgold
						byte[] GOLD = BitTools.LongToByteArrayREVERSE(this.getGuildgold());
						 for(int w=0;w<GOLD.length;w++){
							 iniguild3[80+w] = GOLD[w]; //gold donate
						 }
						
						//guildfame
						//iniguild3[76] = (byte)0x4c; 
						//iniguild3[77] = (byte)0xe5; 
						
						//guildgold
						//iniguild3[80] = (byte)0x95; 
						//iniguild3[81] = (byte)0x05; 
						
						 iniguild3[88] = (byte)this.getGuildhat(); 
						 //iniguild3[90] = ????	
						
						
						//Order them all in the LIST
						int NT = 92;
						int PN = 342;
						int PF = 1244;
						int PO = 1444;
						for(int a=0;a<50;a++) {
							//player charids
							byte[] Gchid = BitTools.intToByteArray(this.getguildids(a));
							for(int y=0;y<4;y++) {
							iniguild3[NT+y] = Gchid[y]; //guild master 96+a = further  members
							}
							
							// player names
							if(this.guildnames.containsKey(a)){
							byte[] Gname = BitTools.stringToByteArray(this.getguildnames(a));
							for(int y=0;y<Gname.length;y++) {
								iniguild3[PN+y] = Gname[y]; // 1st character Name and so on
							}
							}
							
							//players guild rank -> 7 = MASTER | 6 = SUB- | 5 = Senior | 4 = HYANG| 3 = member
							iniguild3[292+a] = (byte)this.getguildranks(a); // 1st character
							
							
							//players class
							iniguild3[1192+a] = (byte)this.getguildclass(a); 
							
							//players fame donated
							byte[] Gfame = BitTools.intToByteArray(this.getguildfames(a));
							for(int y=0;y<4;y++) {
							iniguild3[PF+y] = Gfame[y];
							}
							
							//online player check
							if(wmap.CharacterExists(this.getguildids(a))){
							Character tmp1 = wmap.getCharacter(this.getguildids(a));
							if(tmp1 != null){
							if(PO >= 1400){	
							iniguild3[PO] = (byte)0x01; // 1 = online | 0 = offline
							}else{
							iniguild4[PO] = (byte)0x01; // 1 = online | 0 = offline	
							}
							}}
							
							
							PO = PO + 4;
							if(PO == 1452){PO = 0;}
							PF = PF + 4;
							NT = NT + 4;
							PN = PN + 17;
						}
						// Guild News
						byte[] iniguild5 = new byte[212];
						iniguild5[0] = (byte)0xd4; 
						iniguild5[4] = (byte)0x04; 
						iniguild5[6] = (byte)0x67; 
						iniguild5[8] = (byte)0x01; 
						for(int a=0;a<4;a++) {
							iniguild5[12+a] = chid[a];
						}
						
						byte[] guildtext = this.getguildnews();
						
						for(int a=0;a<guildtext.length;a++) {
							iniguild5[16+a] = guildtext[a]; // guild news
						}
						
						if(ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null){
						Connection con = ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel());
						synchronized(con.getWriteBuffer()) {
							con.addWrite(iniguild3);
							con.addWrite(iniguild4);
							con.addWrite(iniguild5);
						}
						this.refreshWarAlliance(tmp);
						this.getguildbuffs(tmp);
						}
					}}
			}
		}
		
		public void guildsave(){
			String guildmembers = "";
			for(int i=0;i<50;i++){
			if(this.guildids.containsKey(i)){
				guildmembers += Integer.toString(i);	
				guildmembers +=",";
				guildmembers += Integer.toString(this.getguildids(i));	
				guildmembers +=",";
				guildmembers += this.getguildnames(i);
				guildmembers +=",";
				guildmembers += Integer.toString(this.getguildranks(i));
				guildmembers +=",";
				guildmembers += Integer.toString(this.getguildclass(i));
				guildmembers +=",";
				guildmembers += Integer.toString(this.getguildfames(i));
				guildmembers +=",";
			}}
			
			String waralliance = "";
			Iterator<Entry<String, Integer>> iter = this.War.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, Integer> pairs = iter.next();
			if(this.War.containsKey(pairs.getKey())){
				waralliance += Integer.toString(0);	
				waralliance +=",";
				waralliance += pairs.getKey();	
				waralliance +=",";
				waralliance += Integer.toString(pairs.getValue());
				waralliance +=",";
			}
			}
			Iterator<Entry<String, Integer>> iter1 = this.Alliance.entrySet().iterator();
			while(iter1.hasNext()) {
				Entry<String, Integer> pairs1 = iter1.next();
			if(this.Alliance.containsKey(pairs1.getKey())){
				waralliance += Integer.toString(1);	
				waralliance +=",";
				waralliance += pairs1.getKey();	
				waralliance +=",";
				waralliance += Integer.toString(pairs1.getValue());
				waralliance +=",";
			}
			}
			
			int type = this.guildtype;
			String gname = this.guildname;
			int fame = this.guildfame; // Possible Long instead of int
			int gold = this.guildgold; // possible Long instead of int
			int hat = this.guildhat;
			int icon = this.guildicon;
			int pvprating = this.getGuildpvprating();
			int kills = this.guildkills;
			int deaths = this.guilddeaths;
			
			
			String finalguildnews = "";
			byte[] Guildnews = this.getguildnews();
			for(int i=0;i<Guildnews.length;i++){
				if((byte)Guildnews[i] == 0x00){
				finalguildnews += ("}");	
				}else{
				finalguildnews += (char)Guildnews[i]; //(char) = OP
				}
				//finalguildnews +=",";
			}
			
			//guildscore
			int guildscorefame = this.guildfame / 100;
			int guildscoregold = this.guildgold / 10;
			int BaptisteGiabiconi = guildscorefame + guildscoregold;
			
			CharacterDAO.setguildbasic(type,gname,fame,gold,hat,icon,finalguildnews,guildmembers,pvprating,pvprating,kills,deaths,waralliance,BaptisteGiabiconi, this.getGuildID());	
		
		}
		
		
		public void guildsetup(int GuildID){
			this.GuildID = GuildID;
			try {
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getguilddata(SQLconnection.getInstance().getaConnection(), GuildID));
			if (rs.next()){
			this.setGuildtype(rs.getInt("type"));
			this.setguildname(rs.getString("name"));
			this.setGuildfame(rs.getInt("fame"));
			this.setGuildgold(rs.getInt("gold"));
			this.setGuildhat(rs.getInt("hat"));
			this.setGuildicon(rs.getInt("icon"));
			this.setGuildpvprating(rs.getInt("pvprating"));
			this.guildkills = rs.getInt("kills");
			this.guilddeaths = rs.getInt("deaths");
			
			byte[] IamW00FingYo = BitTools.stringToByteArray(rs.getString("news"));
			byte[] Meowzie = new byte [IamW00FingYo.length];
			for(int i=0;i<IamW00FingYo.length;i++) {
		    //System.out.println(String.valueOf((char)IamW00FingYo[i]+" - }"));
			if(String.valueOf((char)IamW00FingYo[i]).equals("}")){
				Meowzie[i] = (byte)0x00;	
			}else{
				Meowzie[i] = IamW00FingYo[i];
			}		
			}
			this.setguildnews(Meowzie);
			
			// get members
			if(!rs.getString("members").equals("") || !rs.getString("members").equals("0")){
			String one = rs.getString("members");
			//System.out.println("Parsing: " + one);
			String[] splat1 = one.split(",");
			for(int i=0;i<splat1.length;i++) {
			this.setguildids(Integer.valueOf(splat1[i]), Integer.valueOf(splat1[i+1]));		// i - charid
			i++;
			i++;
			this.setguildnames(Integer.valueOf(splat1[i-2]), splat1[i]);					// i - name
			i++;
			this.setguildranks(Integer.valueOf(splat1[i-3]), Integer.valueOf(splat1[i]));   // i - rank
			i++;
			this.setguildclass(Integer.valueOf(splat1[i-4]), Integer.valueOf(splat1[i]));   // i - class
			i++;
			this.setguildfames(Integer.valueOf(splat1[i-5]), Integer.valueOf(splat1[i]));   // i - fame
			}}
			
			if(!rs.getString("waralliance").equals("") || !rs.getString("waralliance").equals("0")){
			String one = rs.getString("waralliance");
			//System.out.println("waralliance: " + one);
			String[] splat1 = one.split(",");
			for(int i=0;i<splat1.length;i++) {
			if(Integer.valueOf(splat1[i]) == 0){//War
			this.War.put(splat1[i+1], Integer.valueOf(splat1[i+2]));		
			i++;
			i++;					 
			}else
			if(Integer.valueOf(splat1[i]) == 1){//Alliance
			this.Alliance.put(splat1[i+1], Integer.valueOf(splat1[i+2]));		
			i++;
			i++;		
			}
			}}
		

			}} catch (SQLException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
		}
		
		
		
		// rock inilist bre
		public void iniguild(Character cur) {
			if (this.guildtype != 0){
			//System.out.println("Setting INITERNAL GUILD");
			byte[] chid = BitTools.intToByteArray(cur.getCharID());
			
			byte[] iniguild1 = new byte[480];
			iniguild1[0] = (byte)0x14;
			iniguild1[4] = (byte)0x04;
			iniguild1[6] = (byte)0x4e;
			iniguild1[8] = (byte)0x01;
			for(int a=0;a<4;a++) {
				iniguild1[12+a] = chid[a];
				iniguild1[32+a] = chid[a];
			}
			iniguild1[16] = (byte)0x51; // guild ID ???
			iniguild1[17] = (byte)0x03;
			iniguild1[18] = (byte)0xa7; 
			iniguild1[19] = (byte)0x50;
			
			//IDK new packet
			iniguild1[20] = (byte)0xcc;
			iniguild1[21] = (byte)0x01;
			iniguild1[24] = (byte)0x05;
			iniguild1[26] = (byte)0x44;
			iniguild1[28] = (byte)0x01;

			iniguild1[36] = (byte)0x01;
			
			iniguild1[60] = (byte)0x51;
			iniguild1[61] = (byte)0x03;
			
			iniguild1[64] = (byte)0xa7;
			
			iniguild1[68] = (byte)0x50;
			iniguild1[69] = (byte)0x50;
			
			iniguild1[72] = (byte)0x50;
			iniguild1[73] = (byte)0x50;
			
			iniguild1[76] = (byte)0x50;
			iniguild1[77] = (byte)0x50;
			
			iniguild1[80] = (byte)0x50;

			iniguild1[144] = (byte)0xb9;
			iniguild1[145] = (byte)0xab;
			iniguild1[146] = (byte)0xc7;
			iniguild1[147] = (byte)0xb3;
			iniguild1[148] = (byte)0xc1;
			iniguild1[149] = (byte)0xf6;
			iniguild1[150] = (byte)0xb4;
			iniguild1[151] = (byte)0xeb;
			
			iniguild1[153] = (byte)0x20;
			iniguild1[154] = (byte)0x20;
			iniguild1[155] = (byte)0x20;
			iniguild1[156] = (byte)0x20;
			iniguild1[157] = (byte)0x20;
			iniguild1[158] = (byte)0x20;
			iniguild1[159] = (byte)0x20;
			iniguild1[160] = (byte)0x20;
			iniguild1[161] = (byte)0x20;
			
			iniguild1[162] = (byte)0xbe;
			iniguild1[163] = (byte)0xc6;
			iniguild1[164] = (byte)0xb7;
			iniguild1[165] = (byte)0xb9;
			iniguild1[166] = (byte)0xbd;
			iniguild1[167] = (byte)0xba;
			
			iniguild1[169] = (byte)0x20;
			iniguild1[170] = (byte)0x20;
			iniguild1[171] = (byte)0x20;
			iniguild1[172] = (byte)0x20;
			iniguild1[173] = (byte)0x20;
			iniguild1[174] = (byte)0x20;
			iniguild1[175] = (byte)0x20;
			iniguild1[176] = (byte)0x20;
			iniguild1[177] = (byte)0x20;
			iniguild1[178] = (byte)0x20;
			
			iniguild1[179] = (byte)0xb9;
			iniguild1[180] = (byte)0xab;
			iniguild1[181] = (byte)0xc7;
			iniguild1[182] = (byte)0xb3;

			iniguild1[184] = (byte)0x20;
			iniguild1[185] = (byte)0x20;
			iniguild1[186] = (byte)0x20;
			iniguild1[187] = (byte)0x20;
			iniguild1[188] = (byte)0x20;
			iniguild1[189] = (byte)0x20;
			iniguild1[190] = (byte)0x20;
			iniguild1[191] = (byte)0x20;
			iniguild1[192] = (byte)0x20;
			iniguild1[193] = (byte)0x20;
			iniguild1[194] = (byte)0x20;
			iniguild1[195] = (byte)0x20;
			
			iniguild1[196] = (byte)0xb9;
			iniguild1[197] = (byte)0xab;
			iniguild1[198] = (byte)0xc7;
			iniguild1[199] = (byte)0xb3;
			iniguild1[200] = (byte)0xc1;
			iniguild1[201] = (byte)0xf6;
			iniguild1[202] = (byte)0xb4;
			iniguild1[203] = (byte)0xeb;
			
			iniguild1[204] = (byte)0x20;
			iniguild1[205] = (byte)0x20;
			iniguild1[206] = (byte)0x20;
			iniguild1[207] = (byte)0x20;
			iniguild1[208] = (byte)0x20;
			iniguild1[209] = (byte)0x20;
			iniguild1[210] = (byte)0x20;
			iniguild1[211] = (byte)0x20;
			
			iniguild1[212] = (byte)0xc1;
			iniguild1[213] = (byte)0xf6;
			iniguild1[214] = (byte)0xb6;
			iniguild1[215] = (byte)0xf5;
			iniguild1[216] = (byte)0xba;
			iniguild1[217] = (byte)0xf1;
			iniguild1[218] = (byte)0xb1;
			iniguild1[219] = (byte)0xc3;
			
			iniguild1[221] = (byte)0x20;
			iniguild1[222] = (byte)0x20;
			iniguild1[223] = (byte)0x20;
			iniguild1[224] = (byte)0x20;
			iniguild1[225] = (byte)0x20;
			iniguild1[226] = (byte)0x20;
			iniguild1[227] = (byte)0x20;
			iniguild1[228] = (byte)0x20;
			
			iniguild1[229] = (byte)0xbe;
			iniguild1[230] = (byte)0xc6;
			iniguild1[231] = (byte)0xb7;
			iniguild1[232] = (byte)0xb9;
			iniguild1[233] = (byte)0xbd;
			iniguild1[234] = (byte)0xba;
			
			iniguild1[236] = (byte)0x20;
			iniguild1[237] = (byte)0x20;
			iniguild1[238] = (byte)0x20;
			iniguild1[239] = (byte)0x20;
			iniguild1[240] = (byte)0x20;
			iniguild1[241] = (byte)0x20;
			iniguild1[242] = (byte)0x20;
			iniguild1[243] = (byte)0x20;
			iniguild1[244] = (byte)0x20;
			iniguild1[245] = (byte)0x20;

			
			// INI GUILD WINDOW
			byte[] iniguild3 = new byte[1452]; 
			byte[] iniguild4 = new byte[456];
			iniguild3[0] = (byte)0x20;
			iniguild3[4] = (byte)0x04;
			iniguild3[6] = (byte)0x61;
			iniguild3[8] = (byte)0x01;
			for(int a=0;a<4;a++) {
				iniguild3[12+a] = chid[a];
				iniguild3[44+a] = chid[a];
			}
			iniguild3[32] = (byte)0x4c; 
			iniguild3[33] = (byte)0x06; 
			iniguild3[36] = (byte)0x04; 
			iniguild3[38] = (byte)0x41; 
			iniguild3[40] = (byte)0x01; 
			
			iniguild3[48] = (byte)0x01; 
			
			byte[] ininame = BitTools.stringToByteArray(this.guildname);
			for(int a=0;a<ininame.length;a++) {
				iniguild3[50+a] = ininame[a]; 
			}
	        byte[] guildicon = BitTools.intToByteArray(this.guildicon); // guild ID ?? (or icon)
	        for(int i=0;i<2;i++){
	        	iniguild3[i+68] = guildicon[i]; 
	        }
			iniguild3[70] = (byte)this.getGuildtype(); 	
			
			
			//guildscore
			int guildscorefame = this.guildfame / 100;
			int guildscoregold = this.guildgold / 10;
			byte[] GUILDSCORE = BitTools.intToByteArray(guildscorefame + guildscoregold);
			for(int a=0;a<GUILDSCORE.length;a++) {
				iniguild3[72+a] = GUILDSCORE[a]; 
			}
					
			//guildfame
			byte[] FAME = BitTools.intToByteArray(this.getGuildfame());
			for(int i=0;i<4;i++){
				iniguild3[76+i] = FAME[i]; //fame donate
			}
			
			//guildgold
			byte[] GOLD = BitTools.LongToByteArrayREVERSE(this.getGuildgold());
			 for(int w=0;w<GOLD.length;w++){
				 iniguild3[80+w] = GOLD[w]; //gold donate
			 }
			
			//guildfame
			//iniguild3[76] = (byte)0x4c; 
			//iniguild3[77] = (byte)0xe5; 
			
			//guildgold
			//iniguild3[80] = (byte)0x95; 
			//iniguild3[81] = (byte)0x05; 
			//TODO: get uh byte : HAT
			//TODO: get uh byte : HAT

			 iniguild3[88] = (byte)this.getGuildhat(); 
			 //iniguild3[90] = ????
			
			
			//Order them all in the LIST
			int NT = 92;
			int PN = 342;
			int PF = 1244;
			int PO = 1444;
			for(int a=0;a<50;a++) {
				//player charids
				byte[] Gchid = BitTools.intToByteArray(this.getguildids(a));
				for(int y=0;y<4;y++) {
				iniguild3[NT+y] = Gchid[y]; //guild master 96+a = further  members
				}
				
				// player names
				if(this.guildnames.containsKey(a)){
				byte[] Gname = BitTools.stringToByteArray(this.getguildnames(a));
				for(int y=0;y<Gname.length;y++) {
					iniguild3[PN+y] = Gname[y]; // 1st character Name and so on
				}
				}
				
				//players guild rank -> 7 = MASTER | 6 = SUB- | 5 = Senior | 4 = HYANG| 3 = member
				iniguild3[292+a] = (byte)this.getguildranks(a); // 1st character
				
				
				//players class
				iniguild3[1192+a] = (byte)this.getguildclass(a); 
				
				//players fame donated
				byte[] Gfame = BitTools.intToByteArray(this.getguildfames(a));
				for(int y=0;y<4;y++) {
				iniguild3[PF+y] = Gfame[y];
				}
				
				//online player check
				if(wmap.CharacterExists(this.getguildids(a))){
				Character tmp = wmap.getCharacter(this.getguildids(a));
				if(tmp != null){
				if(PO >= 1400){	
				iniguild3[PO] = (byte)0x01; // 1 = online | 0 = offline
				}else{
				iniguild4[PO] = (byte)0x01; // 1 = online | 0 = offline	
				}
				}}
				
				
				PO = PO + 4;
				if(PO == 1452){PO = 0;}
				PF = PF + 4;
				NT = NT + 4;
				PN = PN + 17;
			}
	 
			
			// EXTEND + GUILD MESSAGE
			iniguild4[184+8] = (byte)0xd4; 
			iniguild4[188+8] = (byte)0x04; 
			iniguild4[190+8] = (byte)0x67; 
			iniguild4[192+8] = (byte)0x01; 
			for(int a=0;a<4;a++) {
				iniguild4[196+8+a] = chid[a];
				iniguild4[408+8+a] = chid[a];
			}
			// guild newsmessage
			byte[] guildtext = this.getguildnews();
			for(int a=0;a<guildtext.length;a++) {
				iniguild4[200+8+a] = guildtext[a]; // 1st Name
			}

			iniguild4[396+8] = (byte)0x34; 
			iniguild4[400+8] = (byte)0x04; 
			iniguild4[402+8] = (byte)0x36; 
			iniguild4[404+8] = (byte)0x01; 

			iniguild4[412+8] = (byte)0x01; 
			
			
			// Guild News
			byte[] iniguild5 = new byte[212];
			iniguild5[0] = (byte)0xd4; 
			iniguild5[4] = (byte)0x04; 
			iniguild5[6] = (byte)0x67; 
			iniguild5[8] = (byte)0x01; 
			for(int a=0;a<4;a++) {
				iniguild5[12+a] = chid[a];
			}
			for(int a=0;a<guildtext.length;a++) {
				iniguild5[16+a] = guildtext[a]; // guild news
			}
			

			
			if(ServerFacade.getInstance().getConnectionByChannel(cur.GetChannel()) != null){
			Connection con = ServerFacade.getInstance().getConnectionByChannel(cur.GetChannel());
			synchronized(con.getWriteBuffer()) {
				con.addWrite(iniguild1);
				con.addWrite(iniguild3);
				con.addWrite(iniguild4);
				con.addWrite(iniguild5);
			}
			this.refreshWarAlliance(cur);
			this.getguildbuffs(cur);
			}
			}
		}
		
		


public int getGuildtype() {
	return guildtype;
}

public void setGuildtype(int guildtype) {
	this.guildtype = guildtype;
}

public int getGuildfame() {
	return guildfame;
}

public void setGuildfame(int guildfame) {
	this.guildfame = guildfame;
}

public int getGuildhat() {
	return guildhat;
}

public void setGuildhat(int guildhat) {
	this.guildhat = guildhat;
}

public int getGuildgold() {
	return guildgold;
}

public void setGuildgold(int guildgold) {
	this.guildgold = guildgold;
}

public int getGuildicon() {
	return guildicon;
}

public void setGuildicon(int guildicon) {
	this.guildicon = guildicon;
}


public String getguildname() 				{		return guildname;	}	
public void setguildname(String guildname) 	{		this.guildname = guildname;	}
public byte[] getguildnews(){
	int rank =  wmap.getGuildranks(this.GuildID);
	int pvprating = this.getGuildpvprating();
	int kills = this.guildkills;
	int deaths = this.guilddeaths;
	String PvP;
	if(this != null && this.guildgold >= 500 && this.getGuildfame() >= 25000){
	PvP= "[Guild Rank:#"+rank+"  PvP Rating:"+pvprating+"  K:"+kills+" D:"+deaths+"]";
	}else{
	PvP= "[You need to be at least Grade 10 for PvP.]";
	}
	byte[] FinalPvpRating = PvP.getBytes();
	
	for(int a=0;a<FinalPvpRating.length;a++) {
		this.guildnews[a] = FinalPvpRating[a]; 
	}
	return guildnews;
}	
public void setguildnews(byte[] guildnews){	
	for(int a=0;a<guildnews.length;a++) {
		this.guildnews[a] = guildnews[a]; 
	}
	
	int rank = wmap.getGuildranks(this.GuildID);
	int pvprating = this.getGuildpvprating();
	int kills = this.guildkills;
	int deaths = this.guilddeaths;
	String PvP = "[Guild Rank:#"+rank+"  PvP Rating:"+pvprating+"  K:"+kills+" D:"+deaths+"]";
	byte[] FinalPvpRating = PvP.getBytes();
	
	for(int a=0;a<FinalPvpRating.length;a++) {
		this.guildnews[a] = FinalPvpRating[a]; 
	}
	
}
public int getGuildpvprating() {
	return guildpvprating;
}
public void setGuildpvprating(int guildpvprating) {
	if(guildpvprating < 0){ guildpvprating = 0;
	}else{
	this.guildpvprating = guildpvprating;}
}


}
