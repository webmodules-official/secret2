package World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.nio.channels.*;

import npc.Npc;
import Connections.Connection;
import Player.Character;
import Player.Charstuff;
import ServerCore.ServerFacade;



// Area.class
// Keeps track of players within small area in side the game
// Each Area are members of some Grid, but Area is unaware of which
// Area is always a square as is its Grid

public class Area
{
  private ConcurrentMap<Integer, Location> members = new ConcurrentHashMap<Integer, Location>();
  private ConcurrentMap<Integer, Location> near = new ConcurrentHashMap<Integer, Location>();  
  private WMap wmap = WMap.getInstance();   
  private int gridx,gridy;
  private int uid;
  private int zone;
  private Grid grid;
  private WMap wmp;
  
  /** initializes Area
   parameter descriptions:
   gx, gy  -  (x,y) coordinates of the area within the grid that it belongs to
   mygrid  -  Pointer to the grid object
  */
  public Area(int gx, int gy, Grid mygrid)
  {
	  	this.wmp = WMap.getInstance();
        this.gridx = gx;
        this.gridy = gy;
        this.grid = mygrid;
        this.zone = 0;
  }
  // adds new member (player, mob, npc, dropped item) to the Area
  public void addMember(Location ob)
  {
    // System.out.println("Item added to area " + this.uid);
    this.getMembers().put(ob.getuid(), ob);
	this.updateNear(true, ob, false);
  }
  
  public ConcurrentMap<Integer, Integer> addMemberAndGetMembers(Location loc){
  	ConcurrentMap<Integer, Integer> ls = new ConcurrentHashMap<Integer, Integer>();
    Iterator<Integer> it = this.near.keySet().iterator();
    Character cur = null;
    boolean Spawnzie = false;
     while (it.hasNext()){
    	Integer object = it.next();// can be player | npc | mob
    	
    	// npcs must be first within the spawn chain
    	if(wmap.CharacterExists(loc.getuid()) && wmap.Npcexist(object)){
	    	 cur = wmap.getCharacter(loc.getuid());	
	    	if(cur != null){
	    	Npc npc = wmap.GetNpc(object);
			// add player if it does not have it already and its within 300 yards
			if (!npc.iniPackets.containsKey(cur.getCharID()) && WMap.distance(npc.location.getX(), npc.location.getY(), cur.getlastknownX(), cur.getlastknownY()) < 325){
				//System.out.println("Area Add =>"+npc.npcID+" - "+cur.getLOGsetName());
				npc.iniPackets.put(cur.getCharID(), cur.getCharID());
				npc.sendInit(cur.getCharID());
				//if npc spawned then put in my inipackets a sign that shit has moved and then if he moves he resends his exct packet
				Spawnzie = true;
			}	
			
	    }}
    	ls.put(object, object);
	 }
     
     if(cur != null && Spawnzie == true){
    		cur.AddToTheirRespawn(cur.getCharID());
    		Spawnzie = false;
     }
     
	    this.addMember(loc);
	  	  return ls;
	 }
 

  
  // add new near member to list
  protected void addNear(Location loc){
	  if (!this.getNear().containsValue(loc)){
          this.getNear().put(loc.getuid(), loc);
	  }
          this.notifyMembers(loc.getuid(), true, false);
  }
  // remove member from near list
  protected void rmNear(Location loc, boolean leavegameworld){
	  if (getNear().containsValue(loc)){
		  this.getNear().remove(loc.getuid());
		  this.notifyMembers(loc.getuid(), false, leavegameworld);
      }
  }
  
   //update in range fail
  public void UpdateNearInrange(Character cur){
	 /* System.out.println("PING 1");
      Location loc;
      try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		////e.printStackTrace();
	}
	   Iterator<Map.Entry<Integer, Location>> iter = this.getNear().entrySet().iterator();
		 while (iter.hasNext()){
			 Map.Entry<Integer, Location> pairs = iter.next();
             loc = pairs.getValue();
			  if (this.wmap.npcExists(loc.getuid())){
				  Npc npc = this.wmap.GetNpc(pairs.getKey());
				  if(npc != null){
					  System.out.println("PING 2");
						  npc.sendInit(cur.getCharID());
				  }
			  }
			 
		 }
	  */
  }
  
  public void setuid(int id)
  {
    this.uid = id;
  }
  public int getuid()
  {
    return this.uid;
  }
  private void updateNear(boolean add, Location loc, boolean leavegameworld){
	  int area[] = new int[]{this.gridx -1, this.gridy -1};
		Area tmp = null;
		for (int a=0; a <3; a++){
			for (int b=0; b <3; b++){
				if (this.grid.areaExists(area)){
					tmp = this.grid.getArea(area);
					if (add)tmp.addNear(loc);
					else tmp.rmNear(loc, leavegameworld);
					//tmp.notifyMembers();
					area[1]++;
				}
			}
			area[1] = this.gridy -1;
			area[0]++;
		}
		// this.notifyMembers(loc.getuid(), add);
  }
  // removes existing member from Area
  public void rmMember(Location loc, boolean leavegameworld)
  {
    //System.out.println("Item removed from area: " + this.uid);
	if(getMembers().containsKey(loc.getuid())) {
		getMembers().remove(loc.getuid());
		this.updateNear(false, loc, leavegameworld);
	}
  }

  // returns true if Area has any members at all, false if not
  public boolean hasMembers()
  {
    return !this.getMembers().isEmpty();
  }
  public Location getMember(int uid)
  {
    return (Location)getMembers().get(uid);
  }
  // returns member map
  public Map<Integer, Location> getMemberMap()
  {
    return getMembers();
  }
  public int[] getcoords()
  {
    int [] tmp = new int []{this.gridx, this.gridy};
    return tmp;
  }
  // return true if obj is member of this Area, else returns false
  public boolean isMember(Location obj)
  {
    return getMembers().containsValue(obj);
  }
  // returns true if member whose uid is id is member of this Area, else returns false
  public boolean isMember(int id)
  {
    return getMembers().containsKey(id);
  }
  // send packet buf to all members except one specified by uid
  public void sendToMembers(int uid, byte[] buf)
  {
          SocketChannel tmp;
          Location loc;

                  Iterator<Map.Entry<Integer, Location>> iter = this.getNear().entrySet().iterator();
                  while(iter.hasNext()) {
                          Map.Entry<Integer, Location> pairs = iter.next();
                          loc = pairs.getValue();
                          tmp = loc.GetChannel();
                          
                          if (loc.getuid() != uid && tmp != null && ServerFacade.getInstance().getConnectionByChannel(tmp) != null){
                              // write buf to player loc
                        	  ServerFacade.getInstance().addWriteByChannel(tmp, buf);
                          }
                  
                  }
  }
  
  // notify all nearby objects of a change in members
  private void notifyMembers(Integer pl, boolean add, boolean leavegameworld) {
		  Iterator<Map.Entry<Integer, Location>> iter = this.getMembers().entrySet().iterator();
		  Location loc = null;
		  Integer locid = null;
		  while(iter.hasNext()) {
			  Map.Entry<Integer, Location> pairs = iter.next();
			  loc = pairs.getValue();
			  locid = pairs.getKey();
			  if(this.wmp.CharacterExists(pl) && !pl.equals(locid)) {
				  Character tmp = this.wmap.getCharacter(pl);
				  if(ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null){
			  loc.updateEnvironment(pl, add, leavegameworld);
				  }
			  }
		  }
  }
  
  public int getZone() {
	return this.zone;
  }
  
  public void setZone(int val) {
	  this.zone = val;
  }
  
  public void moveTo(Location loc, Area t) {
	if (this.getMembers().containsValue(loc)){
		int coords[] = t.getcoords();
		//System.out.println("Player " + loc.getuid() + " moved from area " + this.uid + " to " + t.getuid());
		int area[] = new int[]{this.gridx -1, this.gridy -1};
		Area tmp = null;
		for (int a=0; a <3; a++){
			for (int b=0; b <3; b++){
				if (this.grid.areaExists(area)){
					tmp = this.grid.getArea(area);
					if (WMap.distance(coords[0], area[0]) > 1){
						tmp.rmNear(loc,false);
					}
					if (WMap.distance(coords[1], area[1]) > 1){
						tmp.rmNear(loc,false);
					}
					area[1]++;
				}
			}
			area[1] = this.gridy -1;
			area[0]++;
		}
		this.getMembers().remove(loc.getuid());
	}
	
  }
public ConcurrentMap<Integer, Location> getMembers() {
	return members;
}
public void setMembers(ConcurrentMap<Integer, Location> members) {
	this.members = members;
}
public ConcurrentMap<Integer, Location> getNear() {
	return near;
}
public void setNear(ConcurrentMap<Integer, Location> near) {
	this.near = near;
}
  
}
