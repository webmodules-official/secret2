package oAreaTriggers;

import item.Item;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

import logging.ServerLogger;

import ServerCore.ServerFacade;
import Tools.StringTools;
import Database.Queries;
import Database.SQLconnection;
import Mob.MobController;
import Player.Character;
import Player.Charstuff;
import World.Area;
import World.Grid;
import World.Location;
import World.WMap;
import World.Waypoint;


/*
 *  Mob.class
 *  Provides basic mob logic functions 
 */
public class AreaTrigger implements Location{
        
        
	private int npcID, uid, factionID, Fmap, Tmap;
	private AreaTriggerData data;
	private AreaTriggerController control;
	private ConcurrentMap<Integer, Integer>  iniPackets = new ConcurrentHashMap<Integer, Integer>();	
	private Waypoint spawn;
	private Waypoint To;
	int currentWaypoint;
	private boolean alive;
	private Area area;
	private Grid grid;
	private  Map<Integer, Integer> shopitemids = new HashMap<Integer, Integer>();
	private WMap wmap = WMap.getInstance();    
	/*
	 * Initializes the mob
	 * Params:
	 * mobID = type of mob in question
	 * id = unique ID of mob
	 * mdata = pointer to mobs data object 
	 * * cont = pointer to this mobs MobController object
	 */
        
	public AreaTrigger(int uid, int []data, int []To, int factionID, AreaTriggerController cont) {
		super();
		this.uid = uid;
		this.Fmap = data[0];
		this.spawn = new Waypoint(data[1], data[2]);
		this.Tmap = To[0];
		this.To = new Waypoint(To[1], To[2]);
		this.factionID = factionID;
		this.data = cont.getData();
		this.alive = true;
		this.control = cont;
		this.wmap.AddAreaTrigger(uid, this);
	}
        
	public int getMobID() {
		return npcID;
	}
	@Override
	public int getuid() {
		return this.uid;
	}
	@Override
	public void setuid(int uid) {
		this.uid = uid;
	}
	@Override
	public float getlastknownX() {
		return this.spawn.getX();
	}
	@Override
	public float getlastknownY() {
		return this.spawn.getY();
	}
	@Override
	public SocketChannel GetChannel() {
		return null;
	}
	@Override
    public short getState() {
		return 0;
	}
	
	public int getshopitemids(int invslot) {
		if(shopitemids.containsKey(invslot)){
		int invvalue = shopitemids.get(invslot);
		//System.out.println("shopitemids: " +invslot+" - " +invvalue);
		return invvalue;}else
		{ //System.out.println(invslot+" - null "); 
		return 0;}
	}

	public void setshopitemids(int INVslot, int itemid) {
		shopitemids.put(Integer.valueOf(INVslot), Integer.valueOf(itemid)); 
	////System.out.println("shopitemids: " +INVslot+" - " +itemid);
	}
	
	
	// Join mob into the grid based proximity system 
	private void joinGrid(int grid) {
		try {
			this.grid = this.wmap.getGrid(grid);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			//////e.printStackTrace();
		}
		if (!this.hasGrid()){ //System.out.println("FUUUUUUUUUuuuuuuuuuuu!!!!!");
			
		}
		else {
			Area a;
			try {
				a = this.grid.update(this);
			////System.out.println("Got area " + a.getuid());
			this.setMyArea(a);
			this.iniPackets.putAll(a.addMemberAndGetMembers(this));
			this.sendInitToAll();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				//////e.printStackTrace();
			}
		}
	}
	private void sendInitToAll() {
			Iterator<Integer> it = this.iniPackets.keySet().iterator();
				Integer t = null;
				while(it.hasNext()){
					t = it.next();
					//System.out.println("Got uid from area: " + t);
					if (this.wmap.CharacterExists(t)){
						Character tmp = this.wmap.getCharacter(t);
						if (ServerFacade.getInstance().getCon().getConnection(tmp.GetChannel()) != null){
							//ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), getInitPacket());
							}
							else{
								it.remove();
							}
					} 
					else {
						it.remove();
					}
				}
				
			}
	private boolean hasGrid() {
	
		return (this.grid != null);
	}


	private void setMyArea(Area a) {
		this.area = a;
		
	}

	// update our area
	@SuppressWarnings("unused")
	private void updateArea(){
		Area a = this.grid.update(this);
		if (this.getMyArea() != a){
			this.getMyArea().moveTo(this, a);
			this.setMyArea(a);
			a.addMember(this);
			ConcurrentMap<Integer,Integer> ls;
					    ls = this.area.addMemberAndGetMembers(this);
					    Iterator<Integer> it = this.iniPackets.keySet().iterator();
					    while (it.hasNext()){
					    	Integer i = it.next();
					    	if (!ls.containsKey(i)){
					    		it.remove();
					    		// ServerFacade.getInstance().addWriteByChannel(this.wmap.getCharacter(i).GetChannel(), this.);
					    	}
					    }
					    this.removeAll(ls, iniPackets);
					    this.sendInitToList(ls);
				    this.iniPackets.putAll(ls);
		}
	}
	
	// remove inipackets from ls
	  public void removeAll(ConcurrentMap<Integer, Integer> ls, ConcurrentMap<Integer, Integer> iniPackets){
		    Iterator<Integer> it = iniPackets.keySet().iterator();
		     while (it.hasNext()){
		    	 Integer player = it.next();
		    	if(ls.containsKey(player)){
		    	  ls.remove(player);
		    	}

			 }
	  }
	
	private void sendInitToList(ConcurrentMap<Integer,Integer> ls) {
	    Iterator<Integer> it = ls.keySet().iterator();
				Integer t = null;
				while(it.hasNext()){
					t = it.next();
					if (this.wmap.CharacterExists(t) && t != this.getuid()){
						//Character tmp = this.wmap.getCharacter(t);
						//SocketChannel sc = tmp.GetChannel();
						//ServerFacade.getInstance().addWriteByChannel(sc).addWrite(this.getInitPacket());
					}
					else {
						it.remove();
				}
			}
				
		}
	
	private Area getMyArea() {
		return this.area;
	}

	// remove mob from its current area
	private void rmAreaMember() {
		this.area.rmMember(this,true);
	}
	public void Run(){
		this.run();
	}
	private ServerLogger log = ServerLogger.getInstance();
	// perform 1 action on the map based on the status of mob
	// return true if players are near, false otherwise
	protected boolean run() {
		if (!this.hasGrid()){ 
			//System.out.print("Npc " + this.uid + " Joining grid..");
			this.joinGrid(this.data.getGridID());
			//System.out.println("done");
		}
		boolean hasPlayers = !this.iniPackets.isEmpty();
	    //System.out.println("Mob has " + this.iniPackets.size() + " players");
		try{
	    this.checkAggro();
		} catch (Exception e) {
		//////e.printStackTrace();
		log.logMessage(Level.SEVERE, AreaTrigger.class, "mobID:"+uid+"  map:"+this.control.getMap()+" ___ "+StringTools.readStackTrace(e));
	}
		return hasPlayers;
	}
	
	private boolean checkAggro() {
		////System.out.println("Mob" + this.uid +" aggrocheck"  + Thread.currentThread());
		boolean hasPlayers = false;
			Iterator<Integer> iter = this.iniPackets.keySet().iterator();
        	while(iter.hasNext()) {
        		Integer it =  iter.next();
        		Character loc = this.wmap.getCharacter(it);
        		if (loc != null && ServerFacade.getInstance().getCon().getConnection(loc.GetChannel()) != null){
       				hasPlayers = true;
       				if (WMap.distance(this.spawn.getX(), this.spawn.getY(), loc.getlastknownX(), loc.getlastknownY()) < 25){
       					if (loc.getHp() > 0){
       						//Charstuff.getInstance().respondguild("AreaTrigger: "+this.uid, loc.GetChannel());
       						//teleport packet here
       						//System.out.println("this.factionID:" + this.factionID+" && characterfaction:"+loc.getFaction());
       						if(this.factionID != 0){
       							//System.out.println("this.factionID IN");
       							if(loc.getFaction() == this.factionID){
       								//System.out.println(this.uid+" has player with :"+loc.getFaction());
       								AreaTriggerPackets.PortPlayer(loc, this.Tmap, this.To.getX(), this.To.getY());
       							}
       						}else{
       							//System.out.println(this.uid+" NOPE");
       							AreaTriggerPackets.PortPlayer(loc, this.Tmap, this.To.getX(), this.To.getY());
       						}
       					}
       				}      
       			}else{
       			 iter.remove();
       		  	}
        	}
        	
        return hasPlayers;
    }
	
	
	// send to ini list
	public void SendToiniList(byte[] packet) {
		Iterator<Integer> plIter = this.iniPackets.keySet().iterator();
		Integer tmp = null;
		Character tmpz = null;
		while(plIter.hasNext()) {
			tmp = plIter.next();
			if (this.wmap.CharacterExists(tmp)){
				tmpz = this.wmap.getCharacter(tmp);
				ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), packet); 
			}
		}
	}

	// send packet to all nearby players
	public void send(byte[] buf) {			
		Iterator<Integer> iter = this.iniPackets.keySet().iterator();
						while(iter.hasNext()) {
							Integer plUid = iter.next();               
							Character ch = this.wmap.getCharacter(plUid.intValue());
							if(ch != null) {
								ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), buf);
							}	
				}
		}
	
	// return reference to this mob's controller
	public AreaTriggerController getControl() {
		return control;
	}
	
	// update near by objects, called by area
	// receive updated list for nearby objects
		public synchronized void updateEnvironment(Integer player, boolean add, boolean leavegameworld) {
			//System.out.println("Character: " + this.charID + " has got player list of size: " + players.size());
			if (this.iniPackets.containsKey(player) && !add){
				this.iniPackets.remove(player);
				//System.out.println("Player:" + player + " was removed");
			}
			if (add && !this.iniPackets.containsKey(player)){
				this.iniPackets.put(player, player);
				//System.out.println("Player:" + player + " was added. List contains " + this.iniPackets.size() + " Elements");
				this.sendInit(player);
				if(!this.control.isActive()) {
					this.grid.getThreadPool().executeProcess(this.control);
				}
			}
		}
		// send initial packets to players who don't already have ours
		private void sendInit(Integer tmp) {
			if (this.wmap.CharacterExists(tmp)){
				//Character t = this.wmap.getCharacter(tmp);
				//SocketChannel sc = t.GetChannel();
				//ServerFacade.getInstance().addWriteByChannel(sc).addWrite(this.getInitPacket());
			}
		}
	// check if mob needs to send initial packet to players
		/*
	public void checkEnvironment() {
		Iterator<Integer> plIter = this.iniPackets.iterator();
		Integer tmp = null;
		while(plIter.hasNext()) {
			tmp = plIter.next();
			
			if (!this.wmap.CharacterExists(tmp)){
				this.getNeedIni().remove(tmp);
			} else if(!this.getNeedIni().contains(tmp)) { //if character is no longer in range
				plIter.remove(); //remove it
			} else{
				this.getNeedIni().remove(tmp);
			}
		}
		this.sendInit();
	}
	// send initial packet to everyone in needIni list
	private void sendInit() {
		synchronized(this.getNeedIni()){
			Iterator<Integer> siter = this.getNeedIni().iterator();
			Integer tmp = null;
			while(siter.hasNext()) {
				tmp = siter.next();
				if (this.wmap.CharacterExists(tmp)){
					SocketChannel sc =this.wmap.getCharacter(tmp).GetChannel();
					// prevent the mob from crashing because there aren't initial packet yet
					if (sc != null){
						//this.sface.addWriteByChannel(sc).addWrite(buf);
						//this.sface.addWriteByChannel(sc, this.getInitPacket());
					}
				}
				else {
					siter.remove();
				}
			}
			this.iniPackets.addAll(this.getNeedIni());
			this.getNeedIni().clear();
		}
	}
	*/
	private boolean isAlive() {
		return alive;
	}
	private void setAlive(boolean alive) {
		this.alive = alive;
	}
	private void setX(float x) {
		this.To.setX(x);
	}

	private void setY(float y) {
		this.To.setY(y);
	}
	private float getSpawnx() {
		return spawn.getX();
	}
	private float getSpawny() {
		return spawn.getY();
	}
	private int getCurentWaypoint() {
		return currentWaypoint;
	}
	private void setCurentWaypoint(int curentWaypoint) {
		this.currentWaypoint = curentWaypoint;
	}

	public Waypoint getLocation() {
		return this.spawn;
	}
	
        
}
