package GameServer.GamePackets.UseItemList;

import item.Item;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import Connections.Connection;
import Database.CharacterDAO;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Player.lookuplevel;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class Lottery implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		
		int limit = 11;// Size of every
		
		Random r = new Random();
		int inc = 1+r.nextInt(limit);
		Item.inc++;
		if(inc == 1){
			source.addWrite(Item.itemSpawnPacket(cur.getCharID(), 283000080, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
		}else
			if(inc == 2){
				source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  283000079, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
			}else
				if(inc == 3){
					source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  283000019, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
				}else
					if(inc == 4){
						source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  273001206, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
					}else
						if(inc == 5){
							source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  273001205, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
						}else
							if(inc == 6){
								source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  273001206, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
							}else
								if(inc == 7){
									source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  273001205, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
								}else
									if(inc == 8){
										source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  273001206, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
									}else 
										if(inc == 9){
											source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  273001205, 1, cur.getlastknownX(), cur.getlastknownY(), source));	
									}else
										if(inc == 10){
											source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  273001205, 1, cur.getlastknownX(), cur.getlastknownY(), source));		
									}
									else if(inc == 11){
										source.addWrite(Item.itemSpawnPacket(cur.getCharID(),  215003004, 1, cur.getlastknownX(), cur.getlastknownY(), source));		
									}
		return true;
	}
}