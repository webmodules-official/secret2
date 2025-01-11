package GameServer.GamePackets.UseItemList;

import item.Item;

import java.nio.channels.SelectionKey;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Connections.Connection;
import Database.CharacterDAO;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Player.grinditems;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class Scrollteleport implements UseItemCommandExecutor {
	public  final int EQUIPMENT = 1;
	public  final int POTION = 2;
	public  final int TELEPORT = 3;
	public  final int UPGRADE = 4;
	public  final int MANUAL = 5;
	public  final int MATERIAL = 6;
	public  int inc = 0;
	public  int skill_count = 964;
	public  int skillbar_count = 788;
	public  int friendlist_count = 24;
	public  int ignorelist_count = 534;
	public  int inventory_count = 800;
	public long interval;
	public int cargo_count = 40;
	public int tothird = 0;

	public boolean execute(int ItemID, int DETERMINER, Connection con) {
		//System.out.println("ROLLING: " +ItemID+" - " +DETERMINER);
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chID = BitTools.intToByteArray(cur.getCharID());
		cur.BoothStance = 0;
		cur.TradeUID = 0;
		cur.proffpool = false;
		WMap.getInstance().vendinglist.remove(cur.getCharID());
		cur.leaveGameWorld();
		con.getWriteBuffer().clear(); //clear all packets pending write(prevent client from crashing as it returns to selection)
		cur.getitemdata();
		cur.statlist(); // refresh statlist after clearing the buffs;
		//cur.setguild();
		//ch.recexp(0);
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
		
		stuffLOL[20] = (byte)cur.getCurrentMap();
		
		byte[] cha = new byte[] {
				(byte)0x0c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x9d, (byte)0x0f, (byte)0xbf, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x2e
			};
		
			byte[] coordinates = new byte[8];
			
			// standard shit 
			boolean IsTeleported = false;
			
			// map 1
			if(ItemID == 212100001){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-1482);
				byte[] yCoord = BitTools.floatToByteArray(2577);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-1482);
				cur.setY(2577);
			}
			
			if(ItemID == 212100003){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-5621);
				byte[] yCoord = BitTools.floatToByteArray(-56);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-5621);
				cur.setY(-56);
			}
			
			if(ItemID == 212100004){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(2629);
				byte[] yCoord = BitTools.floatToByteArray(508);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(2629);
				cur.setY(508);
			}
			
			if(ItemID == 212100002){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-2583);
				byte[] yCoord = BitTools.floatToByteArray(-3686);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-2583);
				cur.setY(-3686);
			}
			
			if(ItemID == 212100006){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-5731);
				byte[] yCoord = BitTools.floatToByteArray(8504);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-5731);
				cur.setY(8504);
			}
			
			if(ItemID == 212100005){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-1605);
				byte[] yCoord = BitTools.floatToByteArray(7558);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-1605);
				cur.setY(7558);
			}
			
			if(ItemID == 212100143){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-3313);
				byte[] yCoord = BitTools.floatToByteArray(-2524);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-3313);
				cur.setY(-2524);
			}
			
			if(ItemID == 212020004){
				IsTeleported = true; 
				byte[] xCoord = BitTools.floatToByteArray(-6541);
				byte[] yCoord = BitTools.floatToByteArray(-4915);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-6541);
				cur.setY(-4915);
			}
			// map 2
			if(ItemID == 212100008){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(12896);
				byte[] yCoord = BitTools.floatToByteArray(7778);
				stuffLOL[20] = (byte)0x02;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(2);
				cur.setX(12896);
				cur.setY(7778);
			}
			
			if(ItemID == 212100007){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(13822);
				byte[] yCoord = BitTools.floatToByteArray(8944);
				stuffLOL[20] = (byte)0x02;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(2);
				cur.setX(13822);
				cur.setY(8944);
			}
			
			if(ItemID == 212100010){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(8688);
				byte[] yCoord = BitTools.floatToByteArray(8944);
				stuffLOL[20] = (byte)0x02;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(2);
				cur.setX(8688);
				cur.setY(8944);
			}
			
			if(ItemID == 212100009){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(8688);
				byte[] yCoord = BitTools.floatToByteArray(2464);
				stuffLOL[20] = (byte)0x02;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(2);
				cur.setX(8688);
				cur.setY(2464);
			}
			// map 1
			if(ItemID == 212100189){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-3542);
				byte[] yCoord = BitTools.floatToByteArray(605);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-3542);
				cur.setY(605);
			}
			
			if(ItemID == 212100190){
				IsTeleported = true; 
				byte[] xCoord = BitTools.floatToByteArray(-3457);
				byte[] yCoord = BitTools.floatToByteArray(5657);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-3457);
				cur.setY(5657);
			}
			
			if(ItemID == 212100191){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-384);
				byte[] yCoord = BitTools.floatToByteArray(-2519);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-384);
				cur.setY(-2519);
			}
			
			if(ItemID == 212100192){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-1819);
				byte[] yCoord = BitTools.floatToByteArray(1734);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-1819);
				cur.setY(1734);
			}
			
			if(ItemID == 212100193){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-2565);
				byte[] yCoord = BitTools.floatToByteArray(-1773);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-2565);
				cur.setY(-1773);
			}
			
			if(ItemID == 212100194){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(4470);
				byte[] yCoord = BitTools.floatToByteArray(1220);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(4470);
				cur.setY(1220);
			}
			
			if(ItemID == 212100195){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-1665);
				byte[] yCoord = BitTools.floatToByteArray(6959);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-1665);
				cur.setY(6959);
			}
			
			if(ItemID == 212100196){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-7263);
				byte[] yCoord = BitTools.floatToByteArray(1250);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-7263);
				cur.setY(1250);
			}
			
			if(ItemID == 212100197){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(535);
				byte[] yCoord = BitTools.floatToByteArray(1623);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(535);
				cur.setY(1623);
			}
			// map 3
			if(ItemID == 212100034){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(8885);
				byte[] yCoord = BitTools.floatToByteArray(16952);
				stuffLOL[20] = (byte)0x03;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(3);
				cur.setX(8885);
				cur.setY(16952);
			}
			
			if(ItemID == 212100038){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(5523);
				byte[] yCoord = BitTools.floatToByteArray(15938);
				stuffLOL[20] = (byte)0x03;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(3);
				cur.setX(5523);
				cur.setY(15938);
			}
			
			if(ItemID == 212100033){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(1529);
				byte[] yCoord = BitTools.floatToByteArray(16187);
				stuffLOL[20] = (byte)0x03;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(3);
				cur.setX(1529);
				cur.setY(16187);
			}
			
			
			// Grey Srolls \\ 
			
			//160 map
			if(ItemID == 283000271){ 
				if(cur.getLevel() >= 152){
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(2392);
				byte[] yCoord = BitTools.floatToByteArray(26783);
				stuffLOL[20] = (byte)0xc9;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(201);
				cur.setX(2392);
				cur.setY(26783);
			}else{Charstuff.getInstance().respond("You need to be level 152+ for New level 152 Map.",con);}}
		
			
			
			// v.v
			if(ItemID == 212100141){ 
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(-1502);
				byte[] yCoord = BitTools.floatToByteArray(2585);
				stuffLOL[20] = (byte)0x01;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(1);
				cur.setX(-1502);
				cur.setY(2585);
			}
			
			// o.c
			if(ItemID == 212100142){
				IsTeleported = true;
				byte[] xCoord = BitTools.floatToByteArray(12896);
				byte[] yCoord = BitTools.floatToByteArray(7778);
				stuffLOL[20] = (byte)0x02;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				cur.setCurrentMap(2);
				cur.setX(12896);
				cur.setY(7778);
			}
			
			// Tickets \\ 
			
			//RC SCROLLS
			if(ItemID == 212100199){ 
				if(cur.getLevel() >= 50){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(41592);
				byte[] yCoord1 = BitTools.floatToByteArray(54948);
				stuffLOL[20] = (byte)0x07;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(7);
				cur.setX(41592);
				cur.setY(54948);
				}}
		
			if(ItemID == 212100200){ 
				if(cur.getLevel() >= 70){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(48759);
				byte[] yCoord1 = BitTools.floatToByteArray(54948);
				stuffLOL[20] = (byte)0x07;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(7);
				cur.setX(48759);
				cur.setY(54948);
				}}
		
			if(ItemID == 212100201){ 
				if(cur.getLevel() >= 90){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(48759);
				byte[] yCoord1 = BitTools.floatToByteArray(62110);
				stuffLOL[20] = (byte)0x07;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(7);
				cur.setX(48759);
				cur.setY(62110);
				}}
		
			if(ItemID == 212100202){ 
				if(cur.getLevel() >= 110){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(38784);
				byte[] yCoord1 = BitTools.floatToByteArray(64944);
				stuffLOL[20] = (byte)0x07;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(7);
				cur.setX(38784);
				cur.setY(64944);
				}}
			
			if(ItemID == 213062870){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(45523);
				byte[] yCoord1 = BitTools.floatToByteArray(65060);
				stuffLOL[20] = (byte)0x07;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(7);
				cur.setX(45523);
				cur.setY(65060);
				}
			
			if(ItemID == 213062871){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(45493);
				byte[] yCoord1 = BitTools.floatToByteArray(57901);
				stuffLOL[20] = (byte)0x07;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(7);
				cur.setX(45493);
				cur.setY(57901);
				}
		
		
			
			//rc
			if(ItemID == 273001251 && DETERMINER == 273001255){ 
				if(cur.getLevel() >= 1){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(39165);
				byte[] yCoord1 = BitTools.floatToByteArray(56462);
				stuffLOL[20] = (byte)0x07;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(7);
				cur.setX(39165);
				cur.setY(56462);
				}}
			//mid
			if(ItemID == 273001251 && DETERMINER == 273001256){ 
				if(cur.getLevel() >= 1){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(23622);
				byte[] yCoord1 = BitTools.floatToByteArray(64966);
				stuffLOL[20] = (byte)0x08;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(8);
				cur.setX(23622);
				cur.setY(64966);
				}}
			//sz 
			if(ItemID == 273001251 && DETERMINER == 273001257){ 
				if(cur.getLevel() >= 144){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(15929);
				byte[] yCoord1 = BitTools.floatToByteArray(62121);
				stuffLOL[20] = (byte)0x09;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(9);
				cur.setX(15929);
				cur.setY(62121);
				}}
			//sz 
			if(ItemID == 283000235){ 
				if(cur.getLevel() >= 130){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(15929);
				byte[] yCoord1 = BitTools.floatToByteArray(62121);
				stuffLOL[20] = (byte)0x09;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(9);
				cur.setX(15929);
				cur.setY(62121);
				}}
			
			//144+
			if(ItemID == 273001251 && DETERMINER == 273001258){ 
				if(cur.getLevel() >= 144){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(39168);
				byte[] yCoord1 = BitTools.floatToByteArray(56396);
				stuffLOL[20] = (byte)0x0a;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(10);
				cur.setX(39168);
				cur.setY(56396);
				}}
			
			//144+
			if(ItemID == 273001251 && DETERMINER == 273001259){ 
				if(cur.getLevel() >= 144){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(45605);
				byte[] yCoord1 = BitTools.floatToByteArray(57832);
				stuffLOL[20] = (byte)0x0a;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(10);
				cur.setX(45605);
				cur.setY(57832);
				}}
			
			//144+
			if(ItemID == 273001251 && DETERMINER == 273001260){ 
				if(cur.getLevel() >= 144){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(38784);
				byte[] yCoord1 = BitTools.floatToByteArray(64944);
				stuffLOL[20] = (byte)0x0a;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(10);
				cur.setX(38784);
				cur.setY(64944);
				}}
			
			//144+
			if(ItemID == 273001251 && DETERMINER == 273001261){ 
				if(cur.getLevel() >= 144){
					IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(45542);
				byte[] yCoord1 = BitTools.floatToByteArray(65037);
				stuffLOL[20] = (byte)0x0a;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(10);
				cur.setX(45542);
				cur.setY(65037);
				}}
			
			
			
			//casino
			if(ItemID == 273000886 || ItemID == 273000887){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(24068);
				byte[] yCoord1 = BitTools.floatToByteArray(56132);
				stuffLOL[20] = (byte)0x64;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(100);
				cur.setX(24068);
				cur.setY(56132);
				}
			//rb
			if(ItemID == 273000275 || ItemID == 273000276 || ItemID == 273000277 || ItemID == 273000278){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(25102);
				byte[] yCoord1 = BitTools.floatToByteArray(10552);
				byte[] lolz = BitTools.intToByteArray(300);
				for(int i=0;i<2;i++) {
					stuffLOL[20+i] = lolz[i];
				}
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(300);
				cur.setX(25102);
				cur.setY(10552);
				}
			//12 bf
			/*if(DETERMINER == 212100146){ 
				byte[] xCoord1 = BitTools.floatToByteArray();
				byte[] yCoord1 = BitTools.floatToByteArray();
				stuffLOL[20] = (byte)0x0?;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap();
				cur.setX();
				cur.setY();
				}*/
			
			
			// CHUCK AMULETS \\ 
			// v.v
			if(DETERMINER == 212100146){ 
				IsTeleported = true;
			byte[] xCoord1 = BitTools.floatToByteArray(-1482);
			byte[] yCoord1 = BitTools.floatToByteArray(2577);
			stuffLOL[20] = (byte)0x01;
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord1[i];
				coordinates[i+4] = yCoord1[i];
			}
			cur.setCurrentMap(1);
			cur.setX(-1482);
			cur.setY(2577);
			}
			
			// p.v
			if(DETERMINER == 212100147){ 
				IsTeleported = true;
			byte[] xCoord1 = BitTools.floatToByteArray(-2583);
			byte[] yCoord1 = BitTools.floatToByteArray(-3686);
			stuffLOL[20] = (byte)0x01;
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord1[i];
				coordinates[i+4] = yCoord1[i];
			}
			cur.setCurrentMap(1);
			cur.setX(-2583);
			cur.setY(-3686);
			}
			
			// r.v
			if(DETERMINER == 212100148){ 
				IsTeleported = true;
			byte[] xCoord1 = BitTools.floatToByteArray(-5621);
			byte[] yCoord1 = BitTools.floatToByteArray(-56);
			stuffLOL[20] = (byte)0x01;
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord1[i];
				coordinates[i+4] = yCoord1[i];
			}
			cur.setCurrentMap(1);
			cur.setX(-5621);
			cur.setY(-56);
			}
			
			// b.t
			if(DETERMINER == 212100149){ 
				IsTeleported = true;
			byte[] xCoord1 = BitTools.floatToByteArray(2629);
			byte[] yCoord1 = BitTools.floatToByteArray(508);
			stuffLOL[20] = (byte)0x01;
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord1[i];
				coordinates[i+4] = yCoord1[i];
			}
			cur.setCurrentMap(1);
			cur.setX(2629);
			cur.setY(508);
			}
			
			
			// g.v
			if(DETERMINER == 212100150){ 
				IsTeleported = true;
			byte[] xCoord1 = BitTools.floatToByteArray(-1622);
			byte[] yCoord1 = BitTools.floatToByteArray(7546);
			stuffLOL[20] = (byte)0x01;
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord1[i];
				coordinates[i+4] = yCoord1[i];
			}
			cur.setCurrentMap(1);
			cur.setX(-1622);
			cur.setY(7546);
			}
			
			// o.c
			if(DETERMINER == 212100155){ 
				IsTeleported = true;
			byte[] xCoord1 = BitTools.floatToByteArray(12896);
			byte[] yCoord1 = BitTools.floatToByteArray(7778);
			stuffLOL[20] = (byte)0x02;
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord1[i];
				coordinates[i+4] = yCoord1[i];
			}
			cur.setCurrentMap(2);
			cur.setX(12896);
			cur.setY(7778);
			}
			
			// jauchang baraou
			if(DETERMINER == 212100156){ 
				IsTeleported = true;
			byte[] xCoord1 = BitTools.floatToByteArray(8688);
			byte[] yCoord1 = BitTools.floatToByteArray(8944);
			stuffLOL[20] = (byte)0x02;
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord1[i];
				coordinates[i+4] = yCoord1[i];
			}
			cur.setCurrentMap(2);
			cur.setX(8688);
			cur.setY(8944);
			}
			
			// saheyun villahge
			if(DETERMINER == 212100157){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(8688);
				byte[] yCoord1 = BitTools.floatToByteArray(2464);
				stuffLOL[20] = (byte)0x02;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(2);
				cur.setX(8688);
				cur.setY(2464);
				}
			
			// sungwa
			if(DETERMINER == 212100158){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(19185);
				byte[] yCoord1 = BitTools.floatToByteArray(26816);
				stuffLOL[20] = (byte)0xcb;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(203);
				cur.setX(19185);
				cur.setY(26816);
				}
			
			// at 1
			if(DETERMINER == 212100159){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(19183);
				byte[] yCoord1 = BitTools.floatToByteArray(36027);
				stuffLOL[20] = (byte)0xcc;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(204);
				cur.setX(19183);
				cur.setY(36027);
				}
			
			// at 2
			/*if(DETERMINER == 212100160){ 
				byte[] xCoord1 = BitTools.floatToByteArray(27607);
				byte[] yCoord1 = BitTools.floatToByteArray(26882);
				stuffLOL[20] = (byte)0xcd;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(205);
				cur.setX(27607);
				cur.setY(26882);
				}*/
			
			// at 3
			if(DETERMINER == 212100161){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(35263);
				byte[] yCoord1 = BitTools.floatToByteArray(35900);
				stuffLOL[20] = (byte)0x05;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(5);
				cur.setX(35263);
				cur.setY(35900);
				}
			
			// fighters inn
			if(DETERMINER == 212100162){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(42477);
				byte[] yCoord1 = BitTools.floatToByteArray(45812);
				stuffLOL[20] = (byte)0x04;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(4);
				cur.setX(42477);
				cur.setY(45812);
				}
			
			// shadow temple
			if(DETERMINER == 212100164){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(46472);
				byte[] yCoord1 = BitTools.floatToByteArray(45426);
				stuffLOL[20] = (byte)0x04;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(4);
				cur.setX(46472);
				cur.setY(45426);
				}
			// flex city 
			if(DETERMINER == 212100185){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(38419);
				byte[] yCoord1 = BitTools.floatToByteArray(46682);
				stuffLOL[20] = (byte)0x04;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(4);
				cur.setX(38419);
				cur.setY(46682);
				}
			// flower heaven
			if(DETERMINER == 212100187){ 
				IsTeleported = true;
				byte[] xCoord1 = BitTools.floatToByteArray(39519);
				byte[] yCoord1 = BitTools.floatToByteArray(41881);
				stuffLOL[20] = (byte)0x04;
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord1[i];
					coordinates[i+4] = yCoord1[i];
				}
				cur.setCurrentMap(4);
				cur.setX(39519);
				cur.setY(41881);
				}
			
			
			
			if(!IsTeleported){return false;}
			
			
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
			
					synchronized(con.getWriteBuffer()) {
					con.addWriteButDoNotFlipInterestOps(first);
					con.addWriteButDoNotFlipInterestOps(second);
					con.addWriteButDoNotFlipInterestOps(fourth);
					con.addWriteButDoNotFlipInterestOps(cha);
					con.addWriteButDoNotFlipInterestOps(healpckt);
				 	}
					con.flipOps(SelectionKey.OP_WRITE);
					//cur.joinGameWorld();
					if(cur.guild != null && cur.getGuildID() != 0){
					//System.out.println(cur.getLOGsetName()+"===WTFTWO===");	
					cur.getPlayer().Rarea = true;	
					}
					//cur.getPlayer().Rarea = true;
					return true;
	}
}