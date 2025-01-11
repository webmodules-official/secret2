package GameServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import timer.SystemTimer;
import timer.globalsave;
import timer.potstime;
import timer.statsregen;




import ThreadPool.PacketHandlerPool;
import logging.ServerLogger;
import Connections.Connection;
import PacketHandler.PacketHandler;
import ServerCore.SelectorThread;
import ServerCore.ServerFacade;
import Database.AccountDAO;
import Database.CharacterDAO;
import Encryption.Decryptor;
import GameServer.GamePackets.*;
import Player.Player;
import Player.PlayerConnection;
import Player.Character;
import Player.ipbanlist;
import Tools.BitTools;



public class Gameserver implements PacketHandler {
	private static Map<String, String> Accounts = new HashMap<String, String>();
	private Map<Integer, Packet> packetsByHeader = new HashMap<Integer, Packet>();
	private ServerFacade sf;
	private ServerLogger logging = ServerLogger.getInstance();
	private PacketHandlerPool tPool = PacketHandlerPool.getInstance();
	
	
	public void initialize(ServerFacade sf) {
 		this.sf = sf;
 		// it calces 4th byte x2 + 6th byte in DECI  = Value
		this.packetsByHeader.put(Integer.valueOf(1),	new Ping()); 				//Respond to client ping
		this.packetsByHeader.put(Integer.valueOf(670),	new Loginz()); 				//Logging in 
		this.packetsByHeader.put(Integer.valueOf(666), 	new Quit()); 				//client quits the game(clean quit)
		this.packetsByHeader.put(Integer.valueOf(672), 	new CreateNewCharacter()); 	//handle character creation requests
		this.packetsByHeader.put(Integer.valueOf(673),  new deletecharacter()); 	//delete character
		this.packetsByHeader.put(Integer.valueOf(675), 	new SelectedCharacter()); 	//Respond to selected character logging in game world
		this.packetsByHeader.put(Integer.valueOf(680),  new Movetovv()); 			//move to valley village
		this.packetsByHeader.put(Integer.valueOf(1332), new ReturnToSelection()); 	//player returns to character selection screen
		this.packetsByHeader.put(Integer.valueOf(1335), new TPbacktotown()); 		//TP back to town when died
		this.packetsByHeader.put(Integer.valueOf(1337), new useitem()); 			//Use item 
		this.packetsByHeader.put(Integer.valueOf(1338), new emoticon()); 			//emoticons
		this.packetsByHeader.put(Integer.valueOf(1339), new Chat()); 				//handle chat messages sent by client
		this.packetsByHeader.put(Integer.valueOf(1344), new Equip());				//equip an item
		this.packetsByHeader.put(Integer.valueOf(1345), new LocationSync()); 		//location sync packet
		this.packetsByHeader.put(Integer.valueOf(1346), new Dropstuff());			//drop shit
		this.packetsByHeader.put(Integer.valueOf(1347), new Pick()); 				//pick item from ground
		this.packetsByHeader.put(Integer.valueOf(1348), new InventoryManagement()); //move item in inventory(including unequipping)	
		this.packetsByHeader.put(Integer.valueOf(1349), new Skillbar());			//Skillbar ( put shit in it u know )
		this.packetsByHeader.put(Integer.valueOf(1351), new npcshop());				// npc shop
		this.packetsByHeader.put(Integer.valueOf(1352), new npcshopsell());			// npc shop sell
		this.packetsByHeader.put(Integer.valueOf(1353), new DeleteItem());			//delete item from inventory
		this.packetsByHeader.put(Integer.valueOf(1355), new TradeRequest());		//TradeRequest
		this.packetsByHeader.put(Integer.valueOf(1356), new TradePutItem());		//TradePutItem
		this.packetsByHeader.put(Integer.valueOf(1357), new TradeClose());			//TradeClose
		this.packetsByHeader.put(Integer.valueOf(1361), new statsarributes()); 		//set arributes 
		this.packetsByHeader.put(Integer.valueOf(1362), new looktargetplayerequip());//set arributes 
		this.packetsByHeader.put(Integer.valueOf(1367), new createparty());			//createparty
		this.packetsByHeader.put(Integer.valueOf(1368), new leaveparty());			//leaveparty
		this.packetsByHeader.put(Integer.valueOf(1369), new PromotetoLeader());		//PromotetoLeader
		this.packetsByHeader.put(Integer.valueOf(1372), new ProcedureItem());		//ProcedureItem
		this.packetsByHeader.put(Integer.valueOf(1373), new learnskill());		 	//lean skills
		this.packetsByHeader.put(Integer.valueOf(1374), new duel());		 		//duel
		this.packetsByHeader.put(Integer.valueOf(1376), new putcargofrominv());		//putcargofrominv
		this.packetsByHeader.put(Integer.valueOf(1377), new getfromcargotoinv());	//getfromcargotoinv
		this.packetsByHeader.put(Integer.valueOf(1378), new movecargo());			//move items in cargo
		this.packetsByHeader.put(Integer.valueOf(1379), new wtf());		 			//extra addon after player relog
		this.packetsByHeader.put(Integer.valueOf(1381), new friendlist());		 	//friends list
		this.packetsByHeader.put(Integer.valueOf(1382), new upgrade());		 		//upgrade weps, gear, stuff etc
		this.packetsByHeader.put(Integer.valueOf(1384), new attack()); 				//attack ( skills from hotbar , basic attack ETC)
		this.packetsByHeader.put(Integer.valueOf(1387), new VendingManagement()); 	//vending madafakaaa
		this.packetsByHeader.put(Integer.valueOf(1388), new ExternalGETVendingwindow()); //external player getting my chars vending window
		this.packetsByHeader.put(Integer.valueOf(1389), new Vendingwindow()); 		//vending window
		this.packetsByHeader.put(Integer.valueOf(1390), new buyfromEXvendingwindow());//buyfromEXvendingwindow
		this.packetsByHeader.put(Integer.valueOf(1393), new guildcreatenpc());		//guildcreatenpc
		this.packetsByHeader.put(Integer.valueOf(1394), new guildinvite());			//guildinvite
		this.packetsByHeader.put(Integer.valueOf(1395), new guildkick_leave());		//guildkick_leave
		this.packetsByHeader.put(Integer.valueOf(1396), new guildpromote()); 		//guildpromote
		this.packetsByHeader.put(Integer.valueOf(1397), new guildrefresh());		//guildrefresh
		this.packetsByHeader.put(Integer.valueOf(1402), new retrievecargo()); 		//retrievecargo mail + items by clicking on Recive button
		this.packetsByHeader.put(Integer.valueOf(1403), new checkcargo()); 			//checkcargo
		this.packetsByHeader.put(Integer.valueOf(1406), new vendinglist()); 		//vendinglist
		this.packetsByHeader.put(Integer.valueOf(1407), new mhmarketbuy()); 		//mhmarketbuy
		this.packetsByHeader.put(Integer.valueOf(1411), new Herbstart()); 			//Herbstart
		this.packetsByHeader.put(Integer.valueOf(1413), new guilddeclarewar()); 	//guilddeclarewar
		this.packetsByHeader.put(Integer.valueOf(1431), new casinoputgolds()); 		//casinoputgolds
		this.packetsByHeader.put(Integer.valueOf(1432), new casinowindowrequest());	//casinowindowrequest
		this.packetsByHeader.put(Integer.valueOf(1435), new guildnews()); 			//guildnews
		this.packetsByHeader.put(Integer.valueOf(1438), new fury()); 				//fury
		//this.packetsByHeader.put(Integer.valueOf(1444), new wtftwo()); 				//refresharea for objects
		
	}


	public void processPacket(ByteBuffer buf, SocketChannel chan) {
	}

	@Override
	public void newConnection(SocketChannel chan) {
		// TODO Auto-generated method stub
		
	}
	public String getAccount(String username) {
		if(Accounts.containsKey(username)){
		String usernamez = Accounts.get(username);
		//System.out.println("Accounts: " +username+" - " +usernamez);
		return usernamez;}else
		{ //System.out.println(username+" - null "); 
		return "0";}
	}

	public void setAccount(String username, String usernamez) {
		Accounts.put(username, usernamez); 
		//System.out.println("Accounts: " +username+" - " +usernamez);
	}

	public void newConnection(Connection con) {
		/*
		 *  Handle all your authentication logic in this block
		 *  
		 */
				//CharacterDAO.SetIpBan(con.getip)
				//System.out.println(con.getIp());
				SocketChannel currentChan = con.getChan();
				SelectorThread sp = con.getRegisteredSelector();
				this.sf.getConnections().put(currentChan, new PlayerConnection(currentChan, 200, 800, sp)); //upon succesful authentication - Connection becomes a PlayerConnection
				PlayerConnection plc = (PlayerConnection)this.sf.getConnections().get(currentChan);
				plc.addWrite((Login.Authentication));	
	}


	@Override
	public ByteBuffer processPacket(ByteBuffer boss) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void processPacket(ByteBuffer buf, Connection con) {
		// TODO Auto-generated method stub
		
	}/*
	
	 * Can easly trace incomming packets by wireshark and gamestarter.java down, mod it and show with system.out.println("Byte 1 4 6 8")
	 *
	 */
	public void processList(Connection con) {
		LinkedBlockingQueue<byte[]> tmpQQ = con.getReadBuffer();
		class nonBlockingProcess implements Runnable {
			private Connection con;
			private LinkedBlockingQueue<byte[]> tmpQQ;
			private Map<Integer, Packet> packetsByHeader;
			
			public nonBlockingProcess(Connection con, LinkedBlockingQueue<byte[]> q, Map<Integer, Packet> packets) {
				this.con = con;
				this.tmpQQ = q;
				this.packetsByHeader = packets;
			}
			
			@Override
			public void run() {
				synchronized(tmpQQ) {
					while(!tmpQQ.isEmpty()) {
						byte[] buf = tmpQQ.poll();
						if(buf[7] == 0x00) {
							int header = (int)((buf[4] & 0xFF)*666) + (int)(buf[6] & 0xFF); //get packet type from header (multiplying by 666 just cause we can :)
							if(this.packetsByHeader.containsKey(Integer.valueOf(header))) {
							//System.out.println("PH Exist: "+header);
							byte[] tmp = this.packetsByHeader.get(Integer.valueOf(header)).returnWritableByteBuffer(buf, con);
								if(tmp != null) {	
									con.addWrite(tmp);
								}
							}else{
								/*System.out.println("PH NOT Exist : "+header);
								byte[] decrypted = new byte[(buf[0] & 0xFF)-8];
								for(int i=0;i<decrypted.length;i++) {
									decrypted[i] = (byte)(buf[i+8] & 0xFF);
								}
								decrypted = Decryptor.Decrypt(decrypted);
								
								//PERFECT WAY TO KNOW WHATS WHAT !
								for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" "); }
								System.out.println("");
								for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
								System.out.println("");*/
							}
						} else {
							con.addWrite(this.packetsByHeader.get(Integer.valueOf(1)).returnWritableByteBuffer(buf, con)); //ping
						}				
					}
				}	
			}			
		}
		this.tPool.executeProcess(new nonBlockingProcess(con, tmpQQ, this.packetsByHeader));
	}

}
