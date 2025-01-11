package npc;

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

public class NpcController{
	private ConcurrentMap<Integer, Npc> npcs = new ConcurrentHashMap<Integer, Npc>();
	private int GroupId, npcCount, uidPool, privateID;
	private int map, spawnx, spawny, spawnRadius = 500;
	private int inc = 3;
	private volatile boolean active;
	public NpcData data;
	private ServerLogger log = ServerLogger.getInstance();
	private static volatile int controlID;
	
	public NpcController(int ID, int Count, int Pool, int []data) {
		super();
		this.GroupId = ID;
		this.npcCount = Count;
		this.uidPool = Pool;
		this.map = data[0];
		this.spawnx = data[1];
		this.spawny = data[2];
		this.data = new NpcData();
		this.data.setGridID(map);
		this.data.setgroupID(GroupId);
		this.setActive(false);
		controlID++;
		this.privateID = controlID;
		this.init(data);
	}
	private void init(int []data){
		//this.log.info(this, "Creating npcs objects ");
		for (int i=0;i< this.npcCount;i++){
		try{     //get npc
		ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getnpcs(SQLconnection.getInstance().getaConnection(), data[inc]));
		inc++;
		if (rs.next()){
		Waypoint spawn;
		Npc npc = null;
		int x, y, npcID, uid, module;
		String name;
		npcID = rs.getInt("npcid");
		 uid = rs.getInt("uid");
		  module = rs.getInt("module");
			x = rs.getInt("x");
			y = rs.getInt("y");
			name = rs.getString("name");
			spawn = new Waypoint(x,y);
			npc = new Npc(npcID, uid, module, spawn, this, name);
			npcs.put(i, npc);	
			npc.run();
		}
		}catch (SQLException e) {
			log.logMessage(Level.SEVERE, NpcController.class, e.getMessage());
		}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, NpcController.class, e.getMessage());
		}
		}
		//this.log.info(this, this.npcCount + " npcs succesfully created");
	}
	public boolean isActive() {
		return active;
	}
	private void setActive(boolean active) {
		this.active = active;
	}
	
	protected NpcData getData(){
		return this.data;
	}
	
	

}
