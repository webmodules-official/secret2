package Mob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import Tools.BitTools;
import Tools.StringTools;
import World.Waypoint;
import logging.ServerLogger;

import Database.MobDAO;

public class MobController {
	private int mobID, mobCount, uidPool, privateID;
	private int map, spawnx, spawny, spawnRadius,wpCount,wpHop;
	MobData data;
	private ServerLogger log = ServerLogger.getInstance();
	private int slot = 0;
	

	
	public MobController(int ID, int Count, int Pool, int []data) {
		super();
		this.mobID = ID;
		this.mobCount = Count;
		this.uidPool = Pool;
		this.setMap(data[0]);
		this.spawnx = data[1];
		this.spawny = data[2];
		this.spawnRadius = data[3];
		this.wpCount = data[4];
		this.wpHop = data[5];
		this.init();
	}
	private void init(){
		this.data = MobDAO.getMobData(mobID);
		this.data.setGridID(getMap());
		this.data.setGeneralMobID(mobID);
		this.data.setWaypointCount(wpCount);
		this.data.setWaypointHop(wpHop);
		Random r = new Random();
		Waypoint spawn;
		Mob mob = null;
		int uid = uidPool;
		int x,y;
		//this.log.info(this, "Creating mob objects ");
		for (int i=0; i < this.mobCount; i++){
			// randomize spawn coordinates 
			x = this.spawnx + r.nextInt(2*this.spawnRadius) - this.spawnRadius;
			y = this.spawny + r.nextInt(2*this.spawnRadius) - this.spawnRadius;
			spawn = new Waypoint(x,y); 
			mob = new Mob(this.mobID, uid, spawn, this);
			uid++;
			try {
			mob.run();
			} catch (NullPointerException e) {
			//////e.printStackTrace();
			continue;
			}
		}
		//this.log.info(this, this.mobCount + " mobs succesfully created");
	}
		

	
	public MobData getData(){
		return this.data;
	}
	
	
	public int getmap(){
		return this.getMap();
	}
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}

}
