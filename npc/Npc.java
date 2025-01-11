package npc;

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

import ServerCore.ServerFacade;
import Database.Queries;
import Database.SQLconnection;
import Player.Character;
import World.Area;
import World.Grid;
import World.Location;
import World.WMap;
import World.Waypoint;


/*
 *  Mob.class
 *  Provides basic mob logic functions 
 */
public class Npc implements Location{
        
        
	public int npcID;
	private int uid;
	private int module;
	private NpcData data;
	private NpcController control;
	public ConcurrentMap<Integer, Integer>  iniPackets = new ConcurrentHashMap<Integer, Integer>();
	private ServerFacade sface;
	private Waypoint spawn;
	public Waypoint location;
	int currentWaypoint;
	private boolean alive = true;
	private Area area;
	private Grid grid;
	private String name;
	private ConcurrentMap<Integer, Integer>  shopitemids = new ConcurrentHashMap<Integer, Integer>();
	private WMap wmap = WMap.getInstance();    
	/*
	 * Initializes the mob
	 * Params:
	 * mobID = type of mob in question
	 * id = unique ID of mob
	 * mdata = pointer to mobs data object 
	 * * cont = pointer to this mobs MobController object
	 */
        
	public Npc(int npcID, int uid, int module, Waypoint mdata, NpcController cont, String name) {
		
		try {
		ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.npcshops(SQLconnection.getInstance().getaConnection(), npcID));
			while(rs.next()){
				this.setshopitemids(rs.getInt("windowslot"), rs.getInt("itemid"));
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
		this.uid = uid;
		this.npcID = npcID;
		this.spawn = mdata;
		this.name = name;
		this.module = module;
		this.location = new Waypoint(mdata.getX(), mdata.getY());
		////System.out.println("NPC:"+name+ " Loc X, Y = : " + this.location.getX() +" - "+ this.location.getY()); 
		this.sface = ServerFacade.getInstance();
		this.data = cont.getData();
		this.alive = true;
		this.control = cont;
		this.wmap.AddNpc(uid, this);
	}
	
	public void Run(){
		this.run();
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
		return this.location.getX();
	}
	@Override
	public float getlastknownY() {
		return this.location.getY();
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
	private void joinGrid(int grid){
		this.grid = this.wmap.getGrid(grid);
		if (!this.hasGrid()){ //System.out.println("FUUUUUUUUUuuuuuuuuuuu!!!!!");
			
		}
		else {
			Area a = this.grid.update(this);
			////System.out.println("Got area " + a.getuid());
			this.setMyArea(a);
			this.iniPackets.putAll(a.addMemberAndGetMembers(this));
			this.sendInitToAll();
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
							ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), this.getInitPacket(tmp));
							}else{
							it.remove();
						}
					}else{
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
	/*private void updateArea() throws OutOfGridException {
		Area a = this.grid.update(this);
		if (this.getMyArea() != a){
			this.getMyArea().moveTo(this, a);
			this.setMyArea(a);
			a.addMember(this);
			List<Integer> ls;
			 ls = this.area.addMemberAndGetMembers(this);
					    Iterator<Integer> it = this.iniPackets.iterator();
					    while (it.hasNext()){
					    	Integer i = it.next();
					    	if (!ls.contains(i)){
					    		it.remove();
					    		// ServerFacade.getInstance().addWriteByChannel(this.wmap.getCharacter(i).GetChannel(), this.);
					    	}
					    }
					    ls.removeAll(iniPackets);
					    this.sendInitToList(ls);
					    this.iniPackets.addAll(ls);
		}
	}
	
	private void sendInitToList(List<Integer> ls) {
				Iterator<Integer> it = ls.iterator();
				Integer t = null;
				while(it.hasNext()){
					t = it.next();
					if (this.wmap.CharacterExists(t) && t != this.getuid()){
						Character tmp = this.wmap.getCharacter(t);
						SocketChannel sc = tmp.GetChannel();
						ServerFacade.getInstance().addWriteByChannel(sc).addWrite(this.getInitPacket(tmp.getCharID()));
					}
					else {
						it.remove();
					}
				}
			
		}*/
	
	private Area getMyArea() {
		return this.area;
	}

	// remove mob from its current area
	private void rmAreaMember() {
		this.area.rmMember(this, true);
	}

	
	// perform 1 action on the map based on the status of mob
	// return true if players are near, false otherwise
	protected boolean run() {
		if (!this.hasGrid()){ 
			//System.out.print("Npc " + this.uid + " Joining grid..");
			try {
				this.joinGrid(this.data.getGridID());
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				//////e.printStackTrace();
			}
			//System.out.println("done");
		}
		boolean hasPlayers = !this.iniPackets.isEmpty();
		//System.out.println("Npc has " + this.iniPackets.size() + " players");
		return hasPlayers;
	}
	
	// check if mob is close enough to player to aggro it
	/*	private boolean checkAggro() {
			////System.out.println("Mob" + this.uid +" aggrocheck"  + Thread.currentThread());
			boolean hasPlayers = false;
			    synchronized(this.iniPackets){
			      	
			   	Iterator<Integer> iter = this.iniPackets.iterator();
	        	while(iter.hasNext()) {
	        		Integer it =  iter.next();
	        		      		Location loc = this.wmap.getCharacter(it);
	        		        		if (loc != null){
	        		        			if (loc.GetChannel() != null){
	        		        				hasPlayers = true;
	        		        					break;            
	        		        			}
	        		        		}
	        		        		else{
	        		        			iter.remove();
	        		        		}
	       		}
	        }
	        return hasPlayers;
	    }
	*/
	
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
	public NpcController getControl() {
		return control;
	}
	public byte[] getInitPacket(Character cur){
		if(this.module == 18||this.module == 19||this.module == 20||this.module == 21){cur.setFarmmodule(this.module);}
		return NpcPackets.npcspawn(cur, this.npcID, this.uid, this.module, this.getLocation(), this.name);
	}
	// update near by objects, called by area
	// receive updated list for nearby objects
		public synchronized void updateEnvironment(Integer player, boolean add, boolean leavegameworld) {
	    	// if area contains npc then
	    	if(wmap.CharacterExists(player)){
	    	Character cur = wmap.getCharacter(player);	
	    	if(cur != null){	
	    	
	    	// if boolean leavegameworld = true then 
	    	if(this.iniPackets.containsKey(cur.getCharID()) && leavegameworld){
	    		//System.out.println("Left =>"+this.npcID+" - "+cur.getLOGsetName());
	    		this.iniPackets.remove(cur.getCharID());
	    		return;
	    	}
	    		
	    	// if its already has it and  on or outside 300 yards
			if (!add && this.iniPackets.containsKey(cur.getCharID()) && WMap.distance(this.location.getX(), this.location.getY(), cur.getlastknownX(), cur.getlastknownY()) >= 325){
			//System.out.println("Remove =>"+this.npcID+" - "+cur.getLOGsetName());
			this.iniPackets.remove(cur.getCharID());
			}	
	    	
			// add player if it does not have it already and its within 300 yards
			if (add && !this.iniPackets.containsKey(cur.getCharID()) && WMap.distance(this.location.getX(), this.location.getY(), cur.getlastknownX(), cur.getlastknownY()) < 325){
			//System.out.println("Add =>"+this.npcID+" - "+cur.getLOGsetName());
			this.iniPackets.put(cur.getCharID(), cur.getCharID());
			this.sendInit(cur.getCharID());
			}	
			
	    	}}
		}
		// send initial packets to players who don't already have ours
		public void sendInit(Integer tmp) {
			if (this.wmap.CharacterExists(tmp)){
				Character t = this.wmap.getCharacter(tmp);
				//t.NPCQueue.put(t.NPCQueue.size()+1, this.getInitPacket(t));
				ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.getInitPacket(t));
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
				this.needIni.remove(tmp);
			} else if(!this.needIni.contains(tmp)) { //if character is no longer in range
				plIter.remove(); //remove it
			} else{
				this.needIni.remove(tmp);
			}
		}
		this.sendInit();
	}
	// send initial packet to everyone in needIni list
	private void sendInit() {
		synchronized(this.needIni){
			Iterator<Integer> siter = this.needIni.iterator();
			Integer tmp = null;
			while(siter.hasNext()) {
				tmp = siter.next();
				if (this.wmap.CharacterExists(tmp)){
					SocketChannel sc =this.wmap.getCharacter(tmp).GetChannel();
					// prevent the mob from crashing because there aren't initial packet yet
					if (sc != null){
						//this.sface.addWriteByChannel(sc).addWrite(buf);
						this.sface.addWriteByChannel(sc, this.getInitPacket());
					}
				}
				else {
					siter.remove();
				}
			}
			this.iniPackets.addAll(this.needIni);
			this.needIni.clear();
		}
	}
	*/
	public boolean isAlive() {
		return alive;
	}
	private void setX(float x) {
		this.location.setX(x);
	}

	private void setY(float y) {
		this.location.setY(y);
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
		return this.location;
	}
	
        
}
