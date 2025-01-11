package item;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import logging.ServerLogger;

import Player.Character;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.Grid;
import World.Area;
import World.Location;
import World.WMap;
import World.Waypoint;

public class DroppedItem implements Location{
	private WMap wmap = WMap.getInstance();
	private List<Integer> iniPackets = new ArrayList<Integer>();
	private int Uid, stack, item; // unique id for grid n such
	private Waypoint location;
	private int map;
	private Grid grid;
	private Area area;
	private ServerLogger log = ServerLogger.getInstance();
	
	public DroppedItem(int itemid, int stack, int uid, int map, float x, float y){
		this.Uid = uid;
		this.item = itemid;
		this.stack = stack;
		this.map = map;
		this.location = new Waypoint(x, y);
		this.joinGameWorld();
	}

	public void setuid(int uid) {
		this.Uid = uid;
	}

	public int getuid() {
		return Uid;
	}
	public Waypoint getLocation() {
		return this.location;
	}
	public synchronized void updateEnvironment(List<Integer> players) {
		System.out.println("updateEnvironment:"+this.Uid+"- "+this.stack+" - "+this.item+" - "+this.location.getX()+" - "+this.location.getY());
		Iterator<Integer> plIter = this.iniPackets.iterator();
		Integer tmp = null;
		
		while(plIter.hasNext()) {
			tmp = plIter.next();
			if(!players.contains(tmp)) {//if character is no longer in range, remove it
				plIter.remove();	
			}
			else { // remove from need ini list if we already have it on the list
				players.remove(tmp);
			}
			if(!this.wmap.CharacterExists(tmp)) { // remove if not a valid character
				players.remove(tmp);
			} 
		}
		
		this.sendInit(players);
	}
	public void joinGameWorld() {
		this.wmap.addItem(this);
		if (this.wmap.gridExist(map)){
			try {
				this.grid = this.wmap.getGrid(this.map);
				this.area = this.grid.update(this);
			} catch (NullPointerException e) {
				//this.log.warning(this, e.getMessage() + " Somehow, someone, somewhere dropped an item outside grid.");
			}
			this.area.addMember(this);
		}
		else {
			//ServerLogger.getInstance().logMessage(Level.SEVERE, this, "Failed to load grid for teim "+this.Uid +" map:" +this.map + ", disconnecting");
		}
	}
	
	/*
	 * item has been picked and this instance is no longer required
	 */
	public void leaveGameWorld() {
		this.area.rmMember(this, true);
		this.wmap.removeItem(Uid);
		this.iniPackets.clear();
	}
	private void sendInit(List<Integer> sendList) {
		Iterator<Integer> siter = sendList.iterator();
		Integer tmp = null;
		while(siter.hasNext()) {
			tmp = siter.next();
			if (this.wmap.CharacterExists(tmp)){
				Character t = this.wmap.getCharacter(tmp);
				ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.itemSpawnPacket());
				// if (this.vanish.containsKey(tmp)) this.vanish.remove(tmp);
			}
		}
		this.iniPackets.addAll(sendList);
	}
	public byte[] itemSpawnPacket() {
		System.out.println("itemSpawnPacket:"+this.Uid+"- "+this.stack+" - "+this.item+" - "+this.location.getX()+" - "+this.location.getY());
		byte[] spawnX = BitTools.floatToByteArray(this.location.getX());
		byte[] spawnY = BitTools.floatToByteArray(this.location.getY());
		byte[] itid = BitTools.intToByteArray(this.item);
		byte[] Stack = BitTools.floatToByteArray(this.stack);
		byte[] uid = BitTools.intToByteArray(this.Uid);
		
		byte[] item1 = new byte[56];
		item1[0] = (byte)item1.length;
		item1[4] = (byte)0x05;
		item1[6] = (byte)0x0e;
		
		for(int i=0;i<4;i++) {
			item1[20+i] = itid[i];    //itemid
			item1[28+i] = Stack[i]; //stack
			item1[32+i] = uid[i];   //uid
			item1[36+i] = spawnX[i];   //x
			item1[40+i] = spawnY[i];   //y
		}
		return item1;
	}

	@Override
	public float getlastknownX() {
		// TODO Auto-generated method stub
		return this.location.getX();
	}

	@Override
	public float getlastknownY() {
		return this.location.getY();
	}

	@Override
	public SocketChannel GetChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateEnvironment(Integer players, boolean add, boolean leavegameworld) {
		// TODO Auto-generated method stub
		
	}
}
