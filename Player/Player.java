package Player;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import timer.SystemTimer;

import Connections.Connection;
import Database.StuffsDAO;
import GameServer.GamePackets.UseItemParser;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class Player {
	private ConcurrentMap<Integer, Character> characters = new ConcurrentHashMap<Integer, Character>(); 
	private int accountID;
	private int flag;
	private String gm;  
	private int banned; 
	private int mhpoints; 
	private String ip;
	private String username;
	private String password;
	private String Nick;
	private String Email;
	private SocketChannel sc;
	private Character activeCharacter;
	private boolean running = true;
	public boolean Rarea = false; 
	public boolean EnterWorld = false; 
	public int ItemQueue1 = 1337;
	public int ItemQueue2 = 1337;
	public int lastvendingcount = 0;
	public long EL = 0; 
	 
	public Player(int id) {
		this.accountID = id;
	}
	
	public Player() {
		//TODO: durp durp
	}
	
	
	  public List<Character> Re_Order(ConcurrentMap<Integer, Character> two){
		  List<Character> one = new ArrayList<Character>();
				for(int i=0;i<10;i++){
				if(two.containsKey(i)){	
				     one.add(two.get(i));	
				}
				}
				
			//empty now
		     this.characters.clear(); 
		     
		     // set new list
			for(int i=0;i<one.size();i++){
				this.characters.put(i, one.get(i));
			}

			//show list
			//for(int i=0;i<this.characters.size();i++) { System.out.println(this.characters.get(i));}
			return one; // return new list
	  }
	  

	
	public byte[] Refresh_Characters(){
		List<Character> characters = this.Re_Order(this.characters);
		Iterator<Character> citer = characters.iterator();
		ByteBuffer all = ByteBuffer.allocate((characters.size()*653)+8+3);
		byte[] size = BitTools.shortToByteArray((short)all.capacity());
		all.put(size);
		all.put(new byte[] { (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x01, (byte)0x00 }); //almost same header/packet as when logging in game(0x04 -> 0x01)
		
		all.put(new byte[] { (byte)0x01, (byte)0x01, (byte)0x01 });
		
		Character ctm = citer.next();
										
		byte[] tmp = ctm.initCharPacket();
		for(int i=0;i<tmp.length;i++) {
			all.put(tmp[i]);
		}
		
		while(citer.hasNext()) {

			Character ctmp = citer.next();
											
			byte[] tmpb = ctmp.initCharPacket();
			for(int i=0;i<tmpb.length;i++) {
				all.put(tmpb[i]);
			}
			
			all.put(10, (byte)((all.get(10)*2)+1)); //required increment depending on amount of characters on account
		}
		
		all.flip();
		byte[] meh = new byte[all.limit()];
		all.get(meh);
		return meh;
	}
	
	
	//---------------------------------------------------ZANPAKTO---------------------------------------------------------------\\
	public void PlayerTimer() {
		 new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
				public long TimestampQueueItems;
				public long TimestampStatsRegen;
				public long TimestampPotsTime; 
				public long TimestampSkillDots; 
				public long TimestampSaveCharacter = 0; 
				public long vpcd = 0;

			 public void run() {
		    		while(running) {
		    		Character This = activeCharacter;
		    		if(ServerFacade.getInstance().getCon().getConnection(sc) != null){	
		    		if(getActiveCharacter() != null){
		    		if(This != null){
		    		byte[] chid = BitTools.intToByteArray(This.getCharID());
		    		
		    		
		    		// Refresh area
		    		if(Rarea == true){
					    long CdActiveSync = System.currentTimeMillis();
						while(System.currentTimeMillis() - CdActiveSync < 250){}
						This.joinGameWorld();
						Rarea = false;
		    		}
		    		
		    		
		    		// vending points 
		    		if(This.BoothStance == (byte)0x01){
		    			if(System.currentTimeMillis() - this.vpcd > 300000){ //300000
			    		This.vpz(chid, true);
		    			this.vpcd = System.currentTimeMillis();	
		    			}
		    		}else{
		    		this.vpcd = System.currentTimeMillis();	
		    		}
		    		
		    		
		    		
		    		/*
		    		 * TOP: idk how teleport npc bugs crash client
		    		 * 3:set chop them in groups of 6s like original server
		    		 *
		    		 * could do sth like a start chain foreach 6 npcs, will be a new one, like 1 = start and 6th is end, 7th is new start chain
		    		 * update the queue pushing the 1000ms, if nothing happens after it, then spawn them all
		    		 *  
		    		 */
		    		
		    		/*if(!This.NPCQueue.isEmpty()){
		    			

		    			
		    			Iterator<Entry<Integer, byte[]>> iter = This.NPCQueue.entrySet().iterator();
		    			while(iter.hasNext()) {
		    				Entry<Integer, byte[]> pairs = iter.next();
		    				ServerFacade.getInstance().addWriteByChannel(This.GetChannel(), pairs.getValue());
		    				This.NPCQueue.remove(pairs.getKey());
		    			}
		    		}*/
		    		
		    		
		    		//re fresh vending list
		    		if(System.currentTimeMillis() - EL > 30000 && lastvendingcount != WMap.getInstance().vendinglist.size()){
		    		This.refreshvendinglist();	
		    		lastvendingcount = WMap.getInstance().vendinglist.size();
		    		EL = System.currentTimeMillis();
		    		}
		    		
		    		
		    		//prevents the every character to be saved upon 
		    		if(TimestampSaveCharacter == 0){TimestampSaveCharacter = System.currentTimeMillis();}
		    		
		    		// Save Character
		    		if(System.currentTimeMillis() - TimestampSaveCharacter > 300000){
		    		try{
			    		int curPOnline = ServerFacade.getInstance().getConnectionCount();
			    		if(curPOnline > Charstuff.getInstance().mostplayersonline){Charstuff.getInstance().mostplayersonline = curPOnline;}
			    		
			    		long timeMillis = System.currentTimeMillis() - Charstuff.getInstance().UpTime;
			    		long time = timeMillis / 1000;  
			    		String seconds = Integer.toString((int)(time % 60));  
			    		String minutes = Integer.toString((int)((time % 3600) / 60));  
			    		String hours = Integer.toString((int)(time / 3600));  
			    		for (int i = 0; i < 2; i++) {  
			    		if (seconds.length() < 2) {  
			    		seconds = "0" + seconds;  
			    		}  
			    		if (minutes.length() < 2) {  
			    		minutes = "0" + minutes;  
			    		}  
			    		if (hours.length() < 2) {  
			    		hours = "0" + hours;  
			    		}} 

			    		
			    		This.savecharacter();
						Charstuff.getInstance().respondyellowshout("Saved players:"+curPOnline+"  Most players online:"+ Charstuff.getInstance().mostplayersonline, This.GetChannel());
						Charstuff.getInstance().respondyellowshout("Uptime: "+hours+ " hours " + minutes + " minutes " + seconds + " seconds", This.GetChannel());
			    		
						
		    			}catch (NullPointerException e) {
							 System.out.println("SOO YOUNG02");
					     }
			    		 catch (Exception e) {
			    			 System.out.println("SOO YOUNG03");
					     }
		    		TimestampSaveCharacter = System.currentTimeMillis();
		    		}
		    		
		    		
		    		// Fury
		    		if(This.furycheck == 1 && System.currentTimeMillis() -This.TimestampFury > This.FuryTime){// 20 sec
		    			This.FuryTime = 0;
		    			This.furycheck = 0;
		    			byte[] fury = new byte[20];
		    			fury[0] = (byte)fury.length;
		    			fury[4] = (byte)0x05;
		    			fury[6] = (byte)0x6a;
		    			fury[8] = (byte)0x01;
		    			for(int i=0;i<4;i++) {
		    				fury[12+i] = chid[i]; 
		    			}
		    			fury[16] = (byte)0x00;
		    			This.sendToMap(fury);
		    		    ServerFacade.getInstance().addWriteByChannel(This.GetChannel(), fury); 		
		    		}
		    		
		    		
		    		// Stats Regen	
		    		try{
		    		if(This.getHp() > 0 && System.currentTimeMillis() - this.TimestampStatsRegen > 10000){
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
							
							healpckt[16] = (byte)0x03; // guild type?
							if(This.guild != null){
						     int Key = BitTools.ValueToKey(This.getCharID(), This.guild.guildids); // how to get it without conquerent exception
							healpckt[18] = (byte)This.guild.getguildranks(Key); //guild rank idk why
		 					}
							double newhp = (double)This.getTempPassives(3) / 100; 
							double newmana = (double)This.getTempPassives(12) / 100; 
							
							double HPRec = newhp + 0.030;     // value1 + 3%
							double ManaRec = newmana + 0.030; // value1 + 3%  
							
							double oldhp = This.getMaxHp() * HPRec; 
							double oldmana = This.getMAXMana() * ManaRec; 
							double oldstam = This.getMaxstamina() * 0.030; // 3% 
							int setcurrenthp = This.getHp()+(int)oldhp;
							int setcurrentmana = This.getMana()+(int)oldmana;
							int setcurrentstamina = This.getStamina()+(int)oldstam;
							This.setHp(setcurrenthp);
							This.setMana(setcurrentmana);
							This.setStamina(setcurrentstamina);	
							
							byte[] hp = BitTools.shortToByteArray((short)setcurrenthp);
							byte[] mana = BitTools.shortToByteArray((short)setcurrentmana);
							byte[] stam = BitTools.shortToByteArray((short)setcurrentstamina);
							
							healpckt[24] = hp[0];
							healpckt[25] = hp[1];
							healpckt[28] = mana[0];
							healpckt[29] = mana[1];
							healpckt[30] = stam[0];
							healpckt[31] = stam[1];			
						    ServerFacade.getInstance().addWriteByChannel(This.GetChannel(), healpckt);
						    this.TimestampStatsRegen = System.currentTimeMillis();
					}
		    		}catch (ConcurrentModificationException e) {
		    			 System.out.println(This.getLOGsetName()+"- Stats Regen1: "+e);
				     }
					 catch (NullPointerException e) {
						 System.out.println(This.getLOGsetName()+"- Stats Regen2: "+e);
				     }
		    		 catch (Exception e) {
		    			 System.out.println(This.getLOGsetName()+"- Stats Regen3: "+e);
				     }
		    		
		    		// Pots Time
		    		try{
					  if(!This.PotTime.isEmpty() && System.currentTimeMillis() - this.TimestampPotsTime > 12000){
						if(This.getCurrentMap() != 100){
							 for(int a=0;a<10;a++){
							 if(This.PotTime.containsKey(a)){
							 int newtime = This.PotTime.get(a) - 3; // current time - 12 seconds each 10 sec
							 if(newtime <= 0){
								 
								 String Pot = "";
								 
								 	if(This.PotIconID.get(a) == 92 && This.PotValue.containsValue(8)){Pot = "Greater jackpot tag";This.JACKPOT_TAG = 0;}
								 	if(This.PotIconID.get(a) == 91 && This.PotValue.containsValue(40)){This.DOUBLE_ITEM_DROP_TAG = 0;}
									if(This.PotIconID.get(a) == 92 && This.PotValue.containsValue(12)){Pot = "Big Greater jackpot tag";This.GREATER_JACKPOT_TAG = 0;}
									if(This.PotIconID.get(a) == 91 && This.PotValue.containsValue(60)){This.GREATER_DOUBLE_ITEM_DROP_TAG = 0;}
								 	if(This.PotIconID.get(a) == 84){Pot = "FDD";This.FDD = 0;} 
								 	if(This.PotIconID.get(a) == 87){Pot = "CASR";This.CASR = 0;}
								 	if(This.PotIconID.get(a) == 83){Pot = "FAD";This.FAD = 0;} 
								 	if(This.PotIconID.get(a) == 85){Pot = "FASR";This.FASR = 0;}
								 	if(This.PotIconID.get(a) == 90){Pot = "Fame tag";This.Fame_Tag_100 = 0;}
								 	if(This.PotIconID.get(a) == 82 && This.PotValue.containsValue(10)){Pot = "Exp tag 10";This.Exp_Tag_10 = 0;}
								 	if(This.PotIconID.get(a) == 82 && This.PotValue.containsValue(15)){Pot = "Exp tag 15";This.Exp_Tag_15 = 0;}
								 	if(This.PotIconID.get(a) == 82 && This.PotValue.containsValue(20)){Pot = "Exp tag 20";This.Exp_Tag_20 = 0;}
								 	if(This.PotIconID.get(a) == 82 && This.PotValue.containsValue(30)){Pot = "Exp tag 30";This.Exp_Tag_30 = 0;}
								 	if(This.PotIconID.get(a) == 82 && This.PotValue.containsValue(100)){Pot = "Exp tag 100";This.Exp_Tag_100 = 0;}
								 	if(This.PotIconID.get(a) == 86){Pot = "FD";This.FD = 0;}
								 	if(This.PotIconID.get(a) == 94){Pot = "SAP";This.SAP = 0;}
								 	if(This.PotIconID.get(a) == 93){Pot = "Sundan";This.NEW_STATUS_SUNDAN = 0;}

								 	This.statlist();// refresh stats list
								 	This.RemoveDot(This.getPotIconID(a), This.getPotSLOT(a));
									
								 	This.PotSLOT.remove(a);	 
								 	This.PotIconID.remove(a);	
								 	This.PotTime.remove(a); // x 4 = 36000 seconds = 600 minutes = 10 hours
									This.PotValue.remove(a);	
									if(!Pot.equals("")){Charstuff.getInstance().respondguild(Pot+" has just expired.", This.GetChannel());}
								 //System.out.println(a+":" +newtime+" - Death");	 
							 }else{
								 This.setPotTime(a, newtime);
								 //System.out.println(a+":" +newtime+" - Continue");
							 }}}}
						this.TimestampPotsTime = System.currentTimeMillis();
					  }
		    		}catch (ConcurrentModificationException e) {
		    			 System.out.println(This.getLOGsetName()+"- Pots Time1: "+e);
				     }
					 catch (NullPointerException e) {
						 System.out.println(This.getLOGsetName()+"- Pots Time2: "+e);
				     }
		    		 catch (Exception e) {
		    			 System.out.println(This.getLOGsetName()+"- Pots Time3: "+e);
				     }
		    			
		    		
					  // Skill Dots
					  try{
					  if(!This.DotsTime.isEmpty() && System.currentTimeMillis() - this.TimestampSkillDots > 4000){
						if(This.getCurrentMap() != 100){
							 for(int a=0;a<8;a++){
							 if(This.DotsTime.containsKey(a)){
							 int newtime = This.DotsTime.get(a) - 1; // current time - 12 seconds each 10 sec
							 if(newtime <= 0){
									if(This.DotsIconID.get(a) == 42){This.settempstore(42,0);This.MANA_SHIELD_PROTECTION = 0;}
									if(This.DotsIconID.get(a) == 44){This.settempstore(44,0);This.HIDING = 0;}
									if(This.DotsIconID.get(a) == 45){This.settempstore(45,0);}
									if(This.DotsIconID.get(a) == 42){This.settempstore(47,0);}
									if(This.DotsIconID.get(a) == 22){This.settempstore(22,0);}
									if(This.DotsIconID.get(a) == 21){This.settempstore(21,0);}
									if(This.DotsIconID.get(a) == 12){This.settempstore(12,0);}
									if(This.DotsIconID.get(a) == 6){This.settempstore(6,0);}
									if(This.DotsIconID.get(a) == 7){This.settempstore(7,0);}
									if(This.DotsIconID.get(a) == 16){This.settempstore(16,0);}
									if(This.DotsIconID.get(a) == 15){This.settempstore(15,0);}
									if(This.DotsIconID.get(a) == 58){	
										This.sendToMap(This.getVanishByID(This.Summonid));
										This.Summonid = 0;
									}
									This.RemoveDot(This.getDotsIconID(a), This.getDotsSLOT(a));
									This.DotsSLOT.remove(a);	 
									This.DotsIconID.remove(a);	
									This.DotsTime.remove(a); // x 4 = 36000 seconds = 600 minutes = 10 hours
									This.DotsValue.remove(a);
									This.statlist();
								 //System.out.println(a+":" +newtime+" - Death");	 
							 }else{
								 This.setDotsTime(a, newtime);
								 if(This.DotsIconID.get(a) == 6){This.SetHpManaStam(This.getHp()+This.DotsValue.get(a), This.getMana(), This.getStamina());}
								 if(This.DotsIconID.get(a) == 47){This.SetHpManaStam(This.getHp()-This.DotsValue.get(a), This.getMana(), This.getStamina());}
							 //System.out.println(a+":" +newtime+" - Continue");
							 }}}}
						this.TimestampSkillDots = System.currentTimeMillis();
					  }
		    		}catch (ConcurrentModificationException e) {
		    			 System.out.println(This.getLOGsetName()+"- Skill Dots1: "+e);
				     }
					 catch (NullPointerException e) {
						 System.out.println(This.getLOGsetName()+"- Skill Dots2: "+e);
				     }
		    		 catch (Exception e) {
		    			 System.out.println(This.getLOGsetName()+"- Skill Dots3: "+e);
				     }
		    			
		    			
		    		// Queue UseItems	
		    		try{
					if(ItemQueue1 != 1337 && System.currentTimeMillis() - this.TimestampQueueItems > 500){
						int one = ItemQueue1; //InventorySLOT
						int two = ItemQueue2; //Determiner
						//System.out.println("Queue UseItems: " +This.getInventorySLOT(one)+" - " +two);
						byte[] item = new byte[52];
						int newstack;
						if(Charstuff.getInstance().tryInvincible_items(This.getInventorySLOT(one))){
						newstack = 1;// if its in the invinceble list, then dont substract
						}else{
						newstack = This.getInventorySTACK(one) - 1;
						}
						item[19] = (byte)one; // inventory slot
						boolean used = UseItemParser.getInstance().parseAndExecuteUseItem(new Integer (This.getInventorySLOT(one)), two, ServerFacade.getInstance().getCon().getConnection(This.GetChannel()));
						//System.out.println("Queue used: "+used);
						if(used == true){
						if(newstack <= 0){	
							This.DeleteInvItem(Integer.valueOf(one));
						}else{This.setInventorySTACK(one, newstack);}

						byte[] NEWSTACK = BitTools.intToByteArray(newstack);
						item[0] = (byte)0x34;
						item[4] = (byte)0x04; // 0x05 = for external packet ( to players)
						item[6] = (byte)0x05;
						item[8] = (byte)0x01;
						for(int i=0;i<4;i++) {
							item[12+i] = chid[i]; 
							item[28+i] = chid[i]; 
						}
						if(newstack <= 0){
							item[20] = (byte)0x00;
							item[21] = (byte)0x00;	
						}
						else{
							for(int i=0;i<2;i++) {
								item[20+i] = NEWSTACK[i]; // New stack on inv slot
							}
						}
						item[16] = (byte)0x01;
						item[18] = (byte)0x01;
						item[24] = (byte)0x01;
						item[49] = (byte)0x15;
						item[50] = (byte)0x76;
						item[51] = (byte)0x2a;
						ServerFacade.getInstance().addWriteByChannel(This.GetChannel(), item);
						}else{This.KillInvFreeze();}
						ItemQueue1 = 1337;
						ItemQueue2 = 1337;
						this.TimestampQueueItems = System.currentTimeMillis();
					}
					}catch (ConcurrentModificationException e) {
		    			 System.out.println(This.getLOGsetName()+"- UseItems1: "+e);
				     }
					 catch (NullPointerException e) {
						 //System.out.println(This.getLOGsetName()+"- UseItems2: "+e);
				     }
		    		 catch (Exception e) {
		    			 System.out.println(This.getLOGsetName()+"- UseItems3: "+e);
				     }
					
		    		}}}else{break;}
		    	}	
		    		 //System.out.println("AcntID: "+accountID+" has ended!");
		    }
		    }).start();
	 }
		
	public Map<Integer, Character> getCharacters() {
		return characters;
	}
	public void setCharacters(ArrayList<Character> characters) {
		for(int i=0;i<characters.size();i++) {
			this.characters.put(i, characters.get(i));
		}
	}
	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public String getGM() {
		return gm;
	}

	public void setGM(String gm) {
		this.gm = gm;
	//	//System.out.print(" =====>Player: set gm to "+ gm);
	}
	
	public String getNick() {
		return Nick;
	}

	public void setNick(String Nick) {
		this.Nick = Nick;
	}
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}
	
	public int getBanned() {
		return banned;
	}
	
	public boolean hasActiveCharacter() {
		return (this.activeCharacter == null) ? false : true;
	}

	public void setBanned(int returnedCharacter) {
		this.banned = returnedCharacter;
	}
	
	public String getip() {
		return ip;
	}
	public void setmhpoints(int mhpoints) { // set ip
		this.mhpoints = mhpoints;
	}
	
	public int getmhpoints() {
		return mhpoints;
	}
	
	public void setip(String ip) { // set ip
		this.ip = ip;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) { // <--- LOL
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public SocketChannel getSc() {
		return sc;
	}
	public void setChannel(SocketChannel chan){
		this.sc = chan;
	}

	public Character getActiveCharacter() {
		return activeCharacter;
	}

	
	public void setActiveCharacter(Character activeCharacter) {
		this.activeCharacter = activeCharacter;
	}
	
	public void addCharacter(Character ch) {
		for(int i=0;i<10;i++) {
			if(this.characters.get(i) == null){
			this.characters.put(i,ch);
			break;
			}
		}
	}
	
	public void removeCharacter(int one) {
		this.characters.remove(one);
	}

	public int getState() {
		// TODO Auto-generated method stub
		return 1;
	}
}
