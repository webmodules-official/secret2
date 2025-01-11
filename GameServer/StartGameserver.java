package GameServer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import npc.Npc;
import npc.NpcController;

import timer.SystemTimer;

import logging.ServerLogger;
import Configuration.ConfigurationManager;
import Connections.Connection;
import Lobby.Lobby;
import Player.Character;
import Player.Charstuff;
import Player.Guild;
import Player.Player;
import Player.PlayerConnection;

import ServerCore.ServerFacade;
import Tools.BitTools;
import Tools.StringTools;
import World.WMap;
import World.Waypoint;
import Database.CharacterDAO;
import Database.IPbanlistDAO;
import Database.ItemDAO;
import Database.MobDAO;
import Database.Queries;
import Database.SQLconnection;
import Database.SkillDAO;
import Database.StartupDAO;
import Database.StuffsDAO;
import GameServer.GamePackets.UseItemParser;


public class StartGameserver {
	private static boolean running = true;
	 private static Map<String, Character> names = new HashMap<String, Character>();
	 public static ConcurrentMap<Integer, Integer>  Lawful = new ConcurrentHashMap<Integer, Integer>(100, 4, 100);
	 public static ConcurrentMap<Integer, Integer>  Evil = new ConcurrentHashMap<Integer, Integer>(100, 4, 100);
		private static WMap wmap = WMap.getInstance();
		
		public static void main(String[] args) {
			ConfigurationManager.setProcessName("GameServer");
			boolean start = true;
			String file = new String("server.xml");
			for (int i=0; i< args.length; i++){
				if (args[i].contentEquals("-f") || args[i].contentEquals("--file")){
					file = args[i+1];
					i++;
				}
				else if (args[i].contentEquals("-h") || args[i].contentEquals("--help")){
					usage();
					return;
				}
				else {
					System.out.println("option " + args[i] + " not supported, exiting");
					usage();
					return;
				}
			}
			start = ConfigurationManager.init(file, true);
			if (start) start();
		}
		public static void usage(){
			System.out.println("Usage: java GameServer.StartGamerserver <options>");
			System.out.println("Available options:");
			System.out.println("-f, --file [filename]    Specify configuration file to use");
			System.out.println("-h, --help               Show help");
		}
		public static void start(){
			Charstuff.getInstance().UpTime = System.currentTimeMillis();
			try {
				PrintWriter output = new PrintWriter("realmstatus.txt");
				output.print("0");
				output.close();
			  	StuffsDAO.setPOnline(0, 0, 0, 0, 0);
				StuffsDAO.setserverstatus(0);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			playSound("hello.wav");
			System.out.println("    .d8888b.  888b     d888 888    888     Y88b  Y88b  Y88b            ");
			System.out.println("   d88P  Y88b 8888b   d8888 888    888      Y88b  Y88b  Y88b      ");
			System.out.println("   888    888 88888b.d88888 888    888       Y88b  Y88b  Y88b      ");
			System.out.println("   888        888Y88888P888 8888888888        Y88b  Y88b  Y88b         ");
			System.out.println("   888  88888 888 Y888P 888 888    888        d88P  d88P  d88P               ");
			System.out.println("   888    888 888  Y8P  888 888    888       d88P  d88P  d88P                ");
			System.out.println("   Y88b  d88P 888   '   888 888    888      d88P  d88P  d88P            ");
			System.out.println("    'Y8888P88 888       888 888    888     d88P  d88P  d88P          ");
			System.out.println("");	
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			System.out.println("             _.-Project Global Martial Heroes-._");	
			System.out.println("");
			System.out.println("");
			System.out.println("");

			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			System.out.println("  888    888 8888888888 888      888      .d88888b.   ");
			System.out.println("  888    888 888        888      888     d88P' 'Y88b  ");
			System.out.println("  888    888 888        888      888     888     888  ");
			System.out.println("  8888888888 8888888    888      888     888     888 ");
			System.out.println("  888    888 888        888      888     888     888  ");
			System.out.println("  888    888 888        888      888     888     888 ");
			System.out.println("  888    888 888        888      888     Y88b. .d88P  d8b d8b d8b ");
			System.out.println("  888    888 8888888888 88888888 88888888 'Y88888P'   Y8P Y8P Y8P ");
			System.out.println("");	 
			try {
			Thread.sleep(1000);
	 
			playSound("1.wav");
			System.out.print( "Systems: ");
			Thread.sleep(700);
			System.out.println( "Initiating...");
			Thread.sleep(1500);
			playSound("Systems_online.wav");
			System.out.print( "Systems: ");
			Thread.sleep(700);
			System.out.println( "Online!");
			//playSound("orion.wav");
			Thread.sleep(1500);
			System.out.print( "Loading General Data...");
			//StuffsDAO.SetUseitemList();
			System.out.println( "done!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			System.out.print("Loading Maps...");
			StartupDAO.loadMaps();
			System.out.println("done!");
			System.out.print("Loading Buffdata...");
			SkillDAO.getbuffdata();
			System.out.println("done!");
			System.out.print("Loading Skilladds...");
			SkillDAO.getskillpointscost();
			System.out.println("done!");
			System.out.print("Loading Level STP...");
			SkillDAO.lookuplevelstp();
			System.out.println("done!");
			System.out.print("Loading Level SKP...");
			SkillDAO.lookuplevelskp();
			System.out.println("done!");
			System.out.print("Loading Lookup items...");
			SkillDAO.lookupitems();
			System.out.println("done!");
			System.out.print("Loading Skilldata...");
			SkillDAO.skilldata();
			System.out.println("done!");
			System.out.print("Loading SkillBedoong...");
			SkillDAO.skillbedoong();
			System.out.println("done!");
			System.out.print("Loading SkillPassive...");
			SkillDAO.getskillpasives();
			System.out.println("done!");
			System.out.print("Loading SkillEffects...");
			SkillDAO.getskilleffects();
			System.out.println("done!");
			System.out.print("Loading Exptable...");
			SkillDAO.maxexptable();
			System.out.println("done!");
			System.out.print("Loading Furytable...");
			SkillDAO.maxfurytable();
			System.out.println("done!");
			System.out.print("Loading Grinditems...");
			ItemDAO.GrindItems();
			System.out.println("done!");
			System.out.print("Loading Items...");
			ItemDAO.getitem();
			System.out.println("done!");
			System.out.print("Loading Ip-Ban list...");
			IPbanlistDAO.getipbanlist();
			System.out.println("done!");
			System.out.print("Loading Upgrades...");
			ItemDAO.Upgradelist();
			System.out.println("done!");
			System.out.print("Loading Manuals...");
			ItemDAO.getmanuals();
			System.out.println("done!");
			System.out.print("Loading Proffessions...");
			ItemDAO.Proffessios();
			System.out.println("done!");
			System.out.print("Loading Invincible_items...");
			ItemDAO.Invincible_items();
			System.out.println("done!");
			System.out.print("Loading Non_Tradable_items...");
			ItemDAO.Non_Tradable_items();
			System.out.println("done!");
			System.out.print("Loading Stackable_items...");
			ItemDAO.Stackable_items();
			System.out.println("done!");
			System.out.print("Loading Mobdrops_skyzone...");
			ItemDAO.Mobdrops_skyzone();
			System.out.println("done!");
			System.out.print("Loading censoredwords...");
			StuffsDAO.censoredwords();
			System.out.println("done!");
			System.out.print("Loading itemtoname...");
			StuffsDAO.itemtoname();
			System.out.println("done!");
			System.out.print("Loading itemtoprocedure...");
			ItemDAO.itemtoprocedure();
			System.out.println("done!");
			System.out.print("Loading equipignore...");
			ItemDAO.Equipignore();
			System.out.println("done!");
			System.out.print("Loading guildbuffs...");
			ItemDAO.guildbuffs();
			System.out.println("done!");
			//System.out.print("Loading MapControllers...");
			//StuffsDAO.initMapControllers();
			//System.out.println("done!");
			System.out.print("Loading AreaTriggers...");
			StuffsDAO.initAreaTriggers();
			System.out.println("done!");
			System.out.print("Loading Instances...");
			ServerFacade.getInstance();
			System.out.println("done!");
			try {
				playSound("2.wav");
				System.out.print("Core: ");
				Thread.sleep(650);
				System.out.println("Active!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			System.out.print("Loading Npcs...");
			ItemDAO.iniNpcsgroups();
			System.out.println("done!");
			System.out.print("Loading Mobs...");
			MobDAO.initMobs();
			System.out.println("done!");
			System.out.print("Loading Etc...");
			// load etc
			System.out.println("done!");
			SooYong(); 	// save all the shit from server side
			GuildTimer();
			BanningManagement();
			System.out.print("Setting 'Karma' Realm to Online in the Lobby.");
			try {
				PrintWriter output = new PrintWriter("realmstatus.txt");
				output.print("1");// 4 = FULL | 1 = Available
				output.close();
			 	StuffsDAO.setserverstatus(1);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("..done!");
			// Console
			Console();
		}
		
		 public static synchronized void BanningManagement() {
			 new Thread(new Runnable() {
					public boolean running = true;
					
				 public void run() {
			    		while(running) {
			    				try{   
			    				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getbanned(SQLconnection.getInstance().getaConnection(),1));
			    				while (rs.next()){
			    				if(ServerFacade.getInstance().getCon().isaccountsRegistered(rs.getInt("accountID"))){
			    				//System.out.println("BANHAMMER: Disconnected account by website panel. AccountID :"+rs.getInt("accountID"));
			    				ServerFacade.getInstance().finalizeConnection(ServerFacade.getInstance().getCon().getaccount(rs.getInt("accountID")).getSc());
			    				ServerFacade.getInstance().getCon().removeAccounts(rs.getInt("accountID"));
			    				CharacterDAO.setconsole(System.currentTimeMillis(), "Disconnected the banned account with the username: "+rs.getString("username")+"." , "Server");
			    				}
			    				
			    				if(System.currentTimeMillis() - rs.getLong("banexpire") > 1){
			    				CharacterDAO.putbanlolbyname(0, "ban expired", 0, rs.getString("username"));
			    				//System.out.println("BANHAMMER: unbanned AccountID :"+rs.getInt("accountID"));
			    				CharacterDAO.setconsole(System.currentTimeMillis(), "Unbanned the account with the username: "+rs.getString("username")+", ban expired." , "Server");
			    				
			    				}
			    				}
			    				}catch (SQLException e) {
			    					//log.logMessage(Level.SEVERE, NpcController.class, e.getMessage());
			    				}
			    				catch (Exception e) {
			    					//log.logMessage(Level.SEVERE, NpcController.class, e.getMessage());
			    				}
			    				
			    				 IPbanlistDAO.getipbanlist();
					    			try {
					    				Thread.sleep(30000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										////e.printStackTrace();
									}
			    		}	
			    }
			    }).start();
		 }
		
		 public static synchronized void GuildTimer() {
			 new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
					public boolean running = true;
					
				 public void run() {
			    		while(running) {
							try {
							// set lawfull
							ResultSet rs1 = SQLconnection.getInstance().executeQuery(Queries.gettopfame(SQLconnection.getInstance().getaConnection(), 1));
			    			int inc1 = 0;
			    			int rank1 = 1; // change this trough iteration
			    			while(rs1.next()){
			    				if(rs1.getInt("fame") != 0){
			    				inc1++;
			    				//System.out.println("Lawful |"+inc1+"<"+rs1.getInt("charname")+"> :"+rs1.getInt("fame")+" Rank:"+rank1);
			    				Lawful.put(rs1.getInt("CharacterID"), rank1);
			    				if(inc1 == 1){rank1 = 2;}
			    				if(inc1 == 3){rank1 = 3;}
			    				if(inc1 == 6){rank1 = 4;}
			    				if(inc1 == 10){rank1 = 5;}
			    				if(inc1 == 15){rank1 = 6;}
			    				if(inc1 == 21){rank1 = 7;}
			    				if(inc1 == 28){rank1 = 8;}
			    				if(inc1 == 36){rank1 = 9;}
			    				if(inc1 == 50){break;}
			    				}
			    			}
			    			
			    			// set evil
			    			ResultSet rs2 = SQLconnection.getInstance().executeQuery(Queries.gettopfame(SQLconnection.getInstance().getaConnection(), 2));
			    			int inc2 = 0;
			    			int rank2 = 1; // change this trough iteration
			    			while(rs2.next()){
			    				if(rs2.getInt("fame") != 0){
			    				inc2++;
			    				//System.out.println("Evil |"+inc2+"<"+rs2.getString("charname")+"> :"+rs2.getInt("fame")+" Rank:"+rank2);
			    				Evil.put(rs2.getInt("CharacterID"), rank2);
			    				if(inc2 == 1){rank2 = 2;}
			    				if(inc2 == 3){rank2 = 3;}
			    				if(inc2 == 6){rank2 = 4;}
			    				if(inc2 == 10){rank2 = 5;}
			    				if(inc2 == 15){rank2 = 6;}
			    				if(inc2 == 21){rank2 = 7;}
			    				if(inc2 == 28){rank2 = 8;}
			    				if(inc2 == 36){rank2 = 9;}
			    				if(inc2 == 50){break;}
			    				}
			    			}
			    			
			    			
			    			
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			
			    			try {
			    				Thread.sleep(5000);
			    				wmap.getranks();
								Thread.sleep(295000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								////e.printStackTrace();
							}
			    			
			    			Iterator<Map.Entry<Integer, Integer>> iter = Charstuff.getInstance().guildbuffs.entrySet().iterator();
			    			CharacterDAO.setguildbuff(0, 96);
			    			CharacterDAO.setguildbuff(0, 97);
			    			CharacterDAO.setguildbuff(0, 98);
			    			CharacterDAO.setguildbuff(0, 99);
			    			CharacterDAO.setguildbuff(0, 100);
			    			CharacterDAO.setguildbuff(0, 101);
			    			while(iter.hasNext()) {
			    				Map.Entry<Integer, Integer> pairs = iter.next();
			    				CharacterDAO.setguildbuff(pairs.getKey(), pairs.getValue());	
			    			}
			    			
			    			Iterator<Entry<Integer, Guild>> iterwa = WMap.getInstance().getGuildMap().entrySet().iterator();
			    			while(iterwa.hasNext()) {
			    				Entry<Integer, Guild> pairs = iterwa.next();
			    				//int GuildID = pairs.getKey();
			    				Guild GUILD = pairs.getValue();
			    					if(GUILD != null){
			    						GUILD.guildsave();
			    						//System.out.println("SAVED GUILD : "+GUILD.getguildname());
			    					}
			    				
			    			}
			    			
			    			
			    		}	
			    }
			    }).start();
		 }
		
		

		
		
		 public static synchronized void SooYong() {
			    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			      public void run() {
			    		while(running) {
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
			    		
			    		//if hours has passed by then wut wut
			    		if(Integer.valueOf(hours) >= 12){
			    			
			    			// Put Realm Offline
			    			try {
			    				PrintWriter output = new PrintWriter("realmstatus.txt");
			    				output.print("0");
			    				output.close();
			    			} catch (FileNotFoundException e1) {
			    				// TODO Auto-generated catch block
			    				e1.printStackTrace();
			    			}
			    			
			 
			    			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			    			Character cur;
			    			try {
							while(iter.hasNext()) {
							Map.Entry<Integer, Character> pairs = iter.next();
							cur = pairs.getValue();
							if(cur != null){
										String Name = cur.getLOGsetName();
						    			cur.leaveGameWorld();
						    			cur.savecharacter();
										 
										 //if character connection is connected then DC him
										 if(ServerFacade.getInstance().getCon().isChannelRegistered(cur.GetChannel())){
													Connection tmp = ServerFacade.getInstance().getCon().getConnection(cur.GetChannel());
													if(tmp.isPlayerConnection()){
														PlayerConnection tmplc = (PlayerConnection) tmp;
														tmplc.getWriteBuffer().clear();
														tmplc.setActiveCharacter(null);
														tmplc.threadSafeDisconnect();
														System.out.println("Character Disconnected: "+Name);
													}	 
										 }			
						    		}
						    	}
							}catch (Exception e) {
							// TODO Auto-generated catch block
							////e.printStackTrace();
							}
								
				    			Iterator<Entry<Integer, Guild>> iterwa = WMap.getInstance().getGuildMap().entrySet().iterator();
				    			while(iterwa.hasNext()) {
				    				Entry<Integer, Guild> pairs = iterwa.next();
				    				//int GuildID = pairs.getKey();
				    				Guild GUILD = pairs.getValue();
				    					if(GUILD != null){
				    						GUILD.guildsave();
				    						System.out.println("SAVED GUILD : "+GUILD.getguildname());
				    					}
				    				
				    			}
							
						
						try {
						  	StuffsDAO.setPOnline(0, 0, 0, 0, 0);
						  	StuffsDAO.setserverstatus(0);
							//System.out.println("TimeDelay: "+TimeDelay);
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							////e.printStackTrace();
						}
								
			    	  	//System.out.println("RESTARTING");
			    	  	System.exit(0);

			    		}else{
						// save it to db after we have no modification errors n shit durning DynamicList -> StaticList 
				    	StuffsDAO.setPOnline(curPOnline, Charstuff.getInstance().mostplayersonline,Integer.valueOf(hours),Integer.valueOf(minutes),Integer.valueOf(seconds));
			    		}
				    	try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							////e.printStackTrace();
						}
			    		}
			    }
			    }).start();
		 } 
		
		 
		 
		 
		 public static synchronized void Console() {
			    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			      public void run() {
			// play some sound you no..
			playSound("online.wav");
			// Console
			System.out.println("<============================== Online ==============================>");
			System.out.println(".commands / .help to display Console commands.");
			
			while(running) {
			try{
				Scanner comm = new Scanner(System.in);
				System.out.print("Admin: "); 
				String com = comm.nextLine(); 
				Commands(com);
				System.out.println("");
			}catch (NullPointerException e){}
			}
		}
	    }).start();
		}
		
		public static void Commands(String command) {
			if(command.equals(".commands")||command.equals(".help")){
				System.out.println("*Displying all console commands for Administrator*");
				System.out.println(".commands		// Display all commands.");
				System.out.println(".help			// Display all commands.");
				System.out.println(".w			// Red announce chat.");
				System.out.println(".wannounce		// Red announce chat.");
				System.out.println(".a			// Yellow announce chat.");
				System.out.println(".announce		// Yellow announce chat.");
				System.out.println(".playersonline 		// Shows how many players are online in the console.");
				System.out.println(".playernames		// Shows ALL the playernames who are online in the console.");
				System.out.println(".playerinfo		// Shows playerinfo by name.");
				System.out.println(".banip			// Ban player IP + Account by name.");
				System.out.println(".banplayer		// Ban player account by name.");
				System.out.println(".kickplayer		// Kick player by name.");
				System.out.println(".flagred 		// Flags the player red by name");
				System.out.println(".saveall 		// Saves all characters in world.");
				System.out.println(".exit			// Saves all chacters and shutsdown the server.");
				System.out.println(".shutdown		// Saves all chacters and shutsdown the server.");
				System.out.println(".serverstatus		// Shows & Changes Serverstatus.");
				System.out.println(".ping			// Shows ping??.");
			}
			
			else if(command.equals(".ping")){
				System.out.print("SelectorThread Ping: Size "); 
				System.out.println(Charstuff.getInstance().size + " Selections have passed. Average time of iterating over all selected keys: " +Charstuff.getInstance().iteravg);
			}
			else if(command.equals(".a")||command.equals(".announce")){
				Scanner comm = new Scanner(System.in);
				System.out.print("Message: "); 
				String com = comm.nextLine(); 
				
				byte[] gmsg = new byte[14+com.length()];
				byte[] msg = com.getBytes(); 
				gmsg[0] = (byte)gmsg.length;
				gmsg[4] = (byte)0x03;
				gmsg[6] = (byte)0x50;
				gmsg[7] = (byte)0xc3;
				gmsg[8] = (byte)0x01;
				gmsg[9] = (byte)com.length();
				for(int i=0;i<msg.length;i++) {gmsg[i+13] = msg[i];}
				Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
					Map.Entry<Integer, Character> pairs = iter.next();
					tmp = pairs.getValue();
					ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
				}
			}
			else if(command.equals(".w")||command.equals(".wannounce")){
				Scanner comm = new Scanner(System.in);
				System.out.print("Message: "); 
				String com = comm.nextLine(); 
				
				byte[] gmsg = new byte[45+com.length()];
				byte[] msg = com.getBytes(); // <--- real gm msg lol
				gmsg[0] = (byte)gmsg.length;
				gmsg[4] = (byte)0x05;
				gmsg[6] = (byte)0x07;
				gmsg[8] = (byte)0x01;
				gmsg[17] = (byte)0x01;
				gmsg[18] = (byte)0x06;
				gmsg[20] = (byte)0xa4; 
				gmsg[21] = (byte)0xd1;
				byte[] name = "[Console]".getBytes();
				for(int i=0;i<name.length;i++) {gmsg[i+22] = name[i];}
				gmsg[40] = (byte)0x44;
				for(int i=0;i<msg.length;i++) {gmsg[i+44] = msg[i];}
				Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
					Map.Entry<Integer, Character> pairs = iter.next();
					tmp = pairs.getValue();
					ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
				}
			}
			else if(command.equals(".playersonline")){
				System.out.println("Online Players: "+ServerFacade.getInstance().getConnectionCount());
			}
			else if(command.equals(".playernames")){
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue();
				System.out.print("Name:"+tmp.getLOGsetName()+" ");
				}
			}
			else if(command.equals(".playerinfo")){
				Scanner comm = new Scanner(System.in);
				System.out.print("Name: "); 
				String com = comm.nextLine(); 
				
				Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
					Map.Entry<Integer, Character> pairs = iter.next();
					tmp = pairs.getValue();
					if(com.equals(tmp.getLOGsetName())) {
						names.put(tmp.getLOGsetName(), tmp);
						Character anyplayer = names.get(com); 
						System.out.println("Character:"+anyplayer.getLOGsetName()+" Id:"+ anyplayer.getCharID() +" Lvl:"+anyplayer.getLevel() + " GM:"+anyplayer.getChargm()+ " Faction:"+anyplayer.getFaction()+" Fame:"+anyplayer.getFame()+" Model:"+anyplayer.getMana()
						+" X:"+anyplayer.getlastknownX() + " Y:"+anyplayer.getlastknownY()+" Map:"+anyplayer.getCurrentMap());
						Player getplyr= anyplayer.getPlayer(); 
						System.out.println("Attack:"+anyplayer.attack +" Defence:"+anyplayer.defence +" Username:"+getplyr.getUsername()+" IP:"+getplyr.getip());
					}}
			}
			else if(command.equals(".banip")){
				Scanner comm = new Scanner(System.in);
				System.out.print("Name: "); 
				String com = comm.nextLine(); 
				
				Scanner connw = new Scanner(System.in);
				System.out.print("Days: "); 
				String conw = connw.nextLine(); 
				
				Scanner conn = new Scanner(System.in);
				System.out.print("Ban Reason: "); 
				String con = conn.nextLine(); 
				
				int banned = 1;
				
				//---------ban player----------\\
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				if(com.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(com); // select player from string
								
				Player getplyrusername = anyplayer.getPlayer();  // go to Player.java tab (accounts etc)
				String anycharactersIP = getplyrusername.getip(); 
				String anycharactersusername = getplyrusername.getUsername(); 

				long banaxpire = System.currentTimeMillis() +SystemTimer.DaysToMiliseconds(Long.valueOf(conw)); // how many days
				String cow = anyplayer.getPlayer().getip()+"-"+con; 
						 CharacterDAO.putbanlolbyname(banned, cow,banaxpire, anycharactersusername);
						 CharacterDAO.putipbanbyname(anycharactersIP, cow);
						 anyplayer.leaveGameWorld();
						 ServerFacade.getInstance().finalizeConnection(anyplayer.GetChannel()); 
						 IPbanlistDAO.getipbanlist();
						 System.out.println("Character: " + anyplayer.getLOGsetName()+" ip + account banned for "+conw+" Days. Reason: "+con);
						 CharacterDAO.setconsole(System.currentTimeMillis(), "Character: " + anyplayer.getLOGsetName()+" account banned for "+conw+" Days. Reason: "+con+". by"+" Console." , "Server");
				}
				}
			}
			else if(command.equals(".banplayer")){
				Scanner comm = new Scanner(System.in);
				System.out.print("Name: "); 
				String com = comm.nextLine(); 
				
				Scanner connw = new Scanner(System.in);
				System.out.print("Days: "); 
				String conw = connw.nextLine(); 
				
				Scanner conn = new Scanner(System.in);
				System.out.print("Ban Reason: "); 
				String con = conn.nextLine(); 

				int banned = 1;
				
				//---------ban player----------\\
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 

				if(com.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(com); // select player from string
				
				Player getplyrusername = anyplayer.getPlayer();  // go to Player.java tab (accounts etc)
				String anycharactersusername = getplyrusername.getUsername(); 
				long banaxpire = System.currentTimeMillis() +SystemTimer.DaysToMiliseconds(Long.valueOf(conw)); // how many days
				String cow = anyplayer.getPlayer().getip()+"-"+con; 
					   	 CharacterDAO.putbanlolbyname(banned, cow,banaxpire, anycharactersusername);
						 anyplayer.leaveGameWorld();
						 ServerFacade.getInstance().finalizeConnection(anyplayer.GetChannel()); 
						 System.out.println("Character: " + anyplayer.getLOGsetName()+" account banned for "+conw+" Days. Reason: "+con);
						 CharacterDAO.setconsole(System.currentTimeMillis(), "Character: " + anyplayer.getLOGsetName()+" account banned for "+conw+" Days. Reason: "+con+". by"+" Console." , "Server");
								
					}
				}
			}
			else if(command.equals(".kickplayer")){
				Scanner comm = new Scanner(System.in);
				System.out.print("Name: "); 
				String com = comm.nextLine(); 
				
				//---------kick player----------\\
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;

				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				if(com.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(com); // select player from string
						
						 anyplayer.leaveGameWorld();
						 ServerFacade.getInstance().finalizeConnection(anyplayer.GetChannel()); 
						
							System.out.println("Character: " + anyplayer.getLOGsetName()+" kicked!");
							CharacterDAO.setconsole(System.currentTimeMillis(), "Character: " + anyplayer.getLOGsetName()+" kicked by "+"Console." , "Server");
					}
				}
			}
			else if(command.equals(".flagred")){
				Scanner comm = new Scanner(System.in);
				System.out.print("Character name: "); 
				String com = comm.nextLine(); 
				
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				if(com.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(com); 
				anyplayer.flagred(); 
				System.out.println("Character: " + anyplayer.getLOGsetName()+" set to red!");
					}
				}
			}
			else if(command.equals(".saveall")){
				int curPOnline = ServerFacade.getInstance().getConnectionCount();
	    		if(curPOnline > Charstuff.getInstance().mostplayersonline){Charstuff.getInstance().mostplayersonline = curPOnline;}
	    		System.out.println("Most players online Today: "+ Charstuff.getInstance().mostplayersonline);
				
    			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
    			Character cur;
    			try {
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				cur = pairs.getValue();
				if(cur != null){
			    			cur.savecharacter();	
							String Name = cur.getLOGsetName();
							System.out.println("Character saved: "+Name);
						}
			    	}
				}catch (Exception e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
				}
    			
    				wmap.getranks();
				
	    			Iterator<Entry<Integer, Guild>> iterwa = WMap.getInstance().getGuildMap().entrySet().iterator();
	    			while(iterwa.hasNext()) {
	    				Entry<Integer, Guild> pairs = iterwa.next();
	    				//int GuildID = pairs.getKey();
	    				Guild GUILD = pairs.getValue();
	    					if(GUILD != null){
	    						GUILD.guildsave();
	    					}
	    				
	    			}
				 

		    	//StuffsDAO.setPOnline(curPOnline, mostplayersonline);
				 System.out.println("Done");

			}
			else if(command.equals(".exit")||command.equals(".shutdown")){
				System.out.println("Saving characters...");
				System.out.println("Saving "+ServerFacade.getInstance().getConnectionCount()+" Players.");
				
				int curPOnline = ServerFacade.getInstance().getConnectionCount();
	    		if(curPOnline > Charstuff.getInstance().mostplayersonline){Charstuff.getInstance().mostplayersonline = curPOnline;}
	    		System.out.println("Most players online Today: "+ Charstuff.getInstance().mostplayersonline);
				
    			// Put Realm Offline
    			try {
    				PrintWriter output = new PrintWriter("realmstatus.txt");
    				output.print("0");
    				output.close();
    			} catch (FileNotFoundException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}

    			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
    			try {
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				Character cur = pairs.getValue();
				if(cur != null){
			    			cur.savecharacter();
							String Name = cur.getLOGsetName();
							System.out.println("Character Disconnected: "+Name);
							 //if character connection is connected then DC him
							 if(ServerFacade.getInstance().getCon().isChannelRegistered(cur.GetChannel())){
										Connection tmp = ServerFacade.getInstance().getCon().getConnection(cur.GetChannel());
										if(tmp.isPlayerConnection()){
											PlayerConnection tmplc = (PlayerConnection) tmp;
											tmplc.getWriteBuffer().clear();
											tmplc.setActiveCharacter(null);
											tmplc.threadSafeDisconnect();
										}	 
							 }			
			    		}
			    	}
				}catch (Exception e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
				}
					
	    			Iterator<Entry<Integer, Guild>> iterwa = WMap.getInstance().getGuildMap().entrySet().iterator();
	    			while(iterwa.hasNext()) {
	    				Entry<Integer, Guild> pairs = iterwa.next();
	    				//int GuildID = pairs.getKey();
	    				Guild GUILD = pairs.getValue();
	    					if(GUILD != null){
	    						GUILD.guildsave();
	    						System.out.println("SAVED GUILD : "+GUILD.getguildname());
	    					}
	    				
	    			}
				
					
	    	StuffsDAO.setPOnline(0, 0, 0, 0, 0);
	    	StuffsDAO.setserverstatus(0);
	    	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	  	System.exit(0);
			}
			/*else if(command.equals(".serverstatus")){
				System.out.println("Server Status: "+Lobby.getcurstats()); 
				System.out.println("Online = 1 | Busy = 2 | Full = 4"); 
				Scanner comm = new Scanner(System.in);
				System.out.print("Status: "); 
				String com = comm.nextLine(); 
				Lobby.setcurstats(Integer.valueOf(com));
				System.out.println("New Server Status: "+Lobby.getcurstats()); 
			}*/
			else System.out.println("Unkown command '"+command+"'");
		}
		
		 public static synchronized void playSound(final String url) {
			    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			      public void run() {
			        try {
			          Clip clip = AudioSystem.getClip();
			          AudioInputStream inputStream = AudioSystem.getAudioInputStream(StartGameserver.class.getResourceAsStream(url));
			          clip.open(inputStream);
			          clip.start(); 
			        } catch (Exception e) {
			          System.err.println(e.getMessage());
			        }
			      }
			    }).start();
			  }
	
}
