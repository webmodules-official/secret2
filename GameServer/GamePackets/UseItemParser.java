package GameServer.GamePackets;

import java.util.HashMap;
import java.util.Map;
import Connections.Connection;
import GameServer.GamePackets.UseItemList.Cleanse;
import GameServer.GamePackets.UseItemList.EvilnLawfulword;
import GameServer.GamePackets.UseItemList.Factionpill;
import GameServer.GamePackets.UseItemList.Junpot;
import GameServer.GamePackets.UseItemList.Lottery;
import GameServer.GamePackets.UseItemList.MHPointsGift;
import GameServer.GamePackets.UseItemList.POTSherb;
import GameServer.GamePackets.UseItemList.POTSone;
import GameServer.GamePackets.UseItemList.POTStwo;
import GameServer.GamePackets.UseItemList.Potion;
import GameServer.GamePackets.UseItemList.Purplebag;
import GameServer.GamePackets.UseItemList.Resetskills;
import GameServer.GamePackets.UseItemList.RevivalAmulet;
import GameServer.GamePackets.UseItemList.Scrollteleport;
import GameServer.GamePackets.UseItemList.Showtoothers;
import Player.Charstuff;


public class UseItemParser {
	private static volatile UseItemParser instance = null;  
	private Map<Integer, UseItemCommandExecutor> UseItemsList = new HashMap<Integer, UseItemCommandExecutor>();
	
	private UseItemParser() { 
		this.UseItemsList.put(283000271, new Scrollteleport()); 
		
		this.UseItemsList.put(212100199, new Scrollteleport()); 
		this.UseItemsList.put(212100200, new Scrollteleport()); 
		this.UseItemsList.put(212100201, new Scrollteleport()); 
		this.UseItemsList.put(212100202, new Scrollteleport()); 
		this.UseItemsList.put(213062871, new Scrollteleport()); 
		this.UseItemsList.put(213062870, new Scrollteleport()); 
		
		
		this.UseItemsList.put(212100001, new Scrollteleport()); 
		this.UseItemsList.put(212100003, new Scrollteleport()); 
		this.UseItemsList.put(212100004, new Scrollteleport()); 
		this.UseItemsList.put(212100002, new Scrollteleport()); 
		this.UseItemsList.put(212100006, new Scrollteleport()); 
		this.UseItemsList.put(212100005, new Scrollteleport()); 
		this.UseItemsList.put(212100143, new Scrollteleport()); 
		this.UseItemsList.put(212020004, new Scrollteleport()); 
		this.UseItemsList.put(212100008, new Scrollteleport()); 
		this.UseItemsList.put(212100007, new Scrollteleport()); 
		this.UseItemsList.put(212100010, new Scrollteleport()); 
		this.UseItemsList.put(212100009, new Scrollteleport()); 
		this.UseItemsList.put(212100189, new Scrollteleport()); 
		this.UseItemsList.put(212100190, new Scrollteleport()); 
		this.UseItemsList.put(212100191, new Scrollteleport()); 
		this.UseItemsList.put(212100192, new Scrollteleport()); 
		this.UseItemsList.put(212100193, new Scrollteleport()); 
		this.UseItemsList.put(212100194, new Scrollteleport()); 
		this.UseItemsList.put(212100195, new Scrollteleport()); 
		this.UseItemsList.put(212100196, new Scrollteleport()); 
		this.UseItemsList.put(212100197, new Scrollteleport()); 
		this.UseItemsList.put(212100034, new Scrollteleport()); 
		this.UseItemsList.put(212100038, new Scrollteleport()); 
		this.UseItemsList.put(212100033, new Scrollteleport()); 
		
		this.UseItemsList.put(283000001, new Scrollteleport()); 
		this.UseItemsList.put(212100141, new Scrollteleport()); 
		this.UseItemsList.put(212100142, new Scrollteleport()); 
		this.UseItemsList.put(283000344, new Scrollteleport()); 
		this.UseItemsList.put(283000357, new Scrollteleport()); 
		this.UseItemsList.put(283000329, new Scrollteleport()); 
		this.UseItemsList.put(283000330, new Scrollteleport()); 
		this.UseItemsList.put(273000886, new Scrollteleport()); 
		this.UseItemsList.put(273000887, new Scrollteleport()); 
		this.UseItemsList.put(273000275, new Scrollteleport()); 
		this.UseItemsList.put(273000276, new Scrollteleport()); 
		this.UseItemsList.put(273000277, new Scrollteleport()); 
		this.UseItemsList.put(273000278, new Scrollteleport()); 
		this.UseItemsList.put(273001251, new Scrollteleport()); 
		this.UseItemsList.put(213062463, new Scrollteleport()); 
		this.UseItemsList.put(283000002, new Scrollteleport()); 
		this.UseItemsList.put(283000235, new Scrollteleport()); 
		
		this.UseItemsList.put(213062504, new Showtoothers()); 
		this.UseItemsList.put(213062505, new Showtoothers()); 
		this.UseItemsList.put(213062506, new Showtoothers()); 
		this.UseItemsList.put(213062507, new Showtoothers()); 
		this.UseItemsList.put(213062508, new Showtoothers()); 
		this.UseItemsList.put(213062509, new Showtoothers()); 
		this.UseItemsList.put(213062512, new Showtoothers()); 
		this.UseItemsList.put(213062575, new Showtoothers()); 
		this.UseItemsList.put(213062576, new Showtoothers()); 
		this.UseItemsList.put(213062578, new Showtoothers()); 
		this.UseItemsList.put(283000062, new Showtoothers()); 
		this.UseItemsList.put(283000063, new Showtoothers()); 
		this.UseItemsList.put(283000064, new Showtoothers()); 
		this.UseItemsList.put(283000065, new Showtoothers()); 
		this.UseItemsList.put(283000066, new Showtoothers()); 
		
		this.UseItemsList.put(299010017, new EvilnLawfulword()); 
		this.UseItemsList.put(299010018, new EvilnLawfulword()); 
		this.UseItemsList.put(273000111, new Factionpill()); 
		this.UseItemsList.put(213060102, new Junpot()); 
		this.UseItemsList.put(283000006, new Junpot()); 
		
		this.UseItemsList.put(273001227, new Resetskills()); // 12-80
		this.UseItemsList.put(213060101, new Resetskills()); // 12-80
		this.UseItemsList.put(213062383, new Resetskills()); // banno 81-160
		this.UseItemsList.put(283000005, new Resetskills()); // banno 1-160
		
		this.UseItemsList.put(283000019, new RevivalAmulet()); 
		this.UseItemsList.put(213062391, new RevivalAmulet()); 
		
		this.UseItemsList.put(283000273, new Lottery()); 
		this.UseItemsList.put(283000301, new Lottery()); 
		this.UseItemsList.put(273001207, new Purplebag()); 
		
		
		this.UseItemsList.put(273001205, new POTSone()); 
		this.UseItemsList.put(213062370, new POTSone()); 
		this.UseItemsList.put(213062707, new POTSone()); 
		this.UseItemsList.put(213062885, new POTSone()); 
		this.UseItemsList.put(213062249, new POTSone()); 
		this.UseItemsList.put(213062772, new POTSone()); 
		this.UseItemsList.put(213062709, new POTSone()); 
		this.UseItemsList.put(213062530, new POTSone()); 
		this.UseItemsList.put(283000079, new POTSone()); 
		this.UseItemsList.put(283000080, new POTSone()); 
		this.UseItemsList.put(273001205, new POTSone()); 
		this.UseItemsList.put(273001206, new POTSone()); 
		this.UseItemsList.put(283000106, new POTSone()); 
		this.UseItemsList.put(283000094, new POTSone()); 
		this.UseItemsList.put(283000095, new POTSone()); 
		this.UseItemsList.put(283000089, new POTSone()); 
		this.UseItemsList.put(283000130, new POTSone()); 
		
		this.UseItemsList.put(283000209, new POTStwo()); 
		this.UseItemsList.put(213062764, new POTStwo()); 
		this.UseItemsList.put(283000208, new POTStwo()); 
		this.UseItemsList.put(213062768, new POTStwo()); 
		this.UseItemsList.put(283000207, new POTStwo()); 
		this.UseItemsList.put(213062767, new POTStwo()); 
		this.UseItemsList.put(283000242, new POTStwo()); 
		
		this.UseItemsList.put(213010001, new Potion()); 
		this.UseItemsList.put(213010002, new Potion()); 
		this.UseItemsList.put(213010003, new Potion()); 
		this.UseItemsList.put(213010006, new Potion()); 
		this.UseItemsList.put(273000237, new Potion()); 
		this.UseItemsList.put(213020001, new Potion()); 
		this.UseItemsList.put(213020002, new Potion()); 
		this.UseItemsList.put(213020003, new Potion()); 
		this.UseItemsList.put(213020006, new Potion()); 
		this.UseItemsList.put(273000241, new Potion()); 
		this.UseItemsList.put(273000242, new Potion()); 
		this.UseItemsList.put(273000243, new Potion()); 
		this.UseItemsList.put(213020007, new Potion()); 
		this.UseItemsList.put(213010007, new Potion()); 
		
		this.UseItemsList.put(283000020, new MHPointsGift()); 
		this.UseItemsList.put(283000021, new MHPointsGift()); 
		this.UseItemsList.put(283000022, new MHPointsGift()); 
		
		this.UseItemsList.put(213030001, new Cleanse()); 
		this.UseItemsList.put(213030001, new Cleanse()); 
		
		this.UseItemsList.put(215003131, new POTSherb()); 
		this.UseItemsList.put(215003132, new POTSherb()); 
		this.UseItemsList.put(215003133, new POTSherb()); 
		this.UseItemsList.put(215003134, new POTSherb()); 
		this.UseItemsList.put(215003135, new POTSherb()); 
		this.UseItemsList.put(215003136, new POTSherb()); 
		this.UseItemsList.put(215003137, new POTSherb()); 
		this.UseItemsList.put(215003138, new POTSherb()); 
		this.UseItemsList.put(215003139, new POTSherb()); 
		this.UseItemsList.put(215003140, new POTSherb()); 
		this.UseItemsList.put(215003141, new POTSherb()); 
		this.UseItemsList.put(215003142, new POTSherb()); 
		this.UseItemsList.put(215003143, new POTSherb()); 
		this.UseItemsList.put(215003144, new POTSherb()); 
		this.UseItemsList.put(215003145, new POTSherb()); 
		
	}
	
	public static synchronized UseItemParser getInstance(){
		if (instance == null){
			instance = new UseItemParser();
		}
		return instance;
	}


	public boolean parseAndExecuteUseItem(int ItemID, int DETERMINER, Connection con) {
			//System.out.println("parseAndExecuteUseItem: " +ItemID+" - " +DETERMINER);
			if(this.UseItemsList.containsKey(ItemID)) {
				return this.UseItemsList.get(ItemID).execute(ItemID, DETERMINER, con);
			}//else{Charstuff.getInstance().respondyellowshout("ItemID: "+ItemID+" is not registered in the Database!", con.getChan());}
			else return true;
	}
}
