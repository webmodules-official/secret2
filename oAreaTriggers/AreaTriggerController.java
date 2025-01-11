package oAreaTriggers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import World.Waypoint;

import logging.ServerLogger;

import Database.ItemDAO;
import Database.MobDAO;
import Database.Queries;
import Database.SQLconnection;
import Mob.Mob;
import Mob.MobData;

public class AreaTriggerController implements Runnable{
	private ConcurrentMap<Integer, AreaTrigger> AreaTriggers = new ConcurrentHashMap<Integer, AreaTrigger>();
	private int map, spawnx, spawny;
	private volatile boolean active;
	public AreaTriggerData data;
	private ServerLogger log = ServerLogger.getInstance();
	
	public AreaTriggerController(int uid, int []data, int []To, int factionID) {
		super();
		this.setMap(data[0]);
		this.spawnx = data[1];
		this.spawny = data[2];
		this.data = new AreaTriggerData();
		this.data.setGridID(getMap());
		this.setActive(false);
		this.init(uid, data, To, factionID);
	}
	private void init(int uid, int []data, int []To, int factionID){
			AreaTrigger areatrigger = null;
			areatrigger = new AreaTrigger(uid, data, To, factionID, this);
			AreaTriggers.put(uid, areatrigger);	
			areatrigger.run();
	}
	
	public boolean isActive() {
		return active;
	}
	
	private void setActive(boolean active) {
		this.active = active;
	}
	
	public void run(){
		if (!this.active){
			this.setActive(true);
			//this.log.info(this, "Controller active in thread " + Thread.currentThread());
			boolean hasPlayers;
		
			while(this.active){
				hasPlayers = false;
					Iterator<Map.Entry<Integer, AreaTrigger>> iter = this.AreaTriggers.entrySet().iterator();
					while(iter.hasNext()) {
						Map.Entry<Integer, AreaTrigger> pairs = iter.next();
						if (hasPlayers){ pairs.getValue().run(); }
						else{ hasPlayers = pairs.getValue().run(); } 
					}
				try {
					if (!hasPlayers) this.setActive(false);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					////e.printStackTrace();
				}
			
			}
			//this.log.info(this, "Controller deactivated in thread " + Thread.currentThread());
		}
		
	}
	protected AreaTriggerData getData(){
		return this.data;
	}
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
}
