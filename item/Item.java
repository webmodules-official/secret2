package item;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Connections.Connection;
import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
/*
 * TODO: uhhh.. lots of stuff
 */
public class Item {
	private int id;
	private int width, height;
	private boolean consumeable;
	private boolean permanent;
	private int npcPrice;
	private long expirationTime;
	private int minLvl, MaxLvl;
	private int type;
	
	public static final int EQUIPMENT = 1;
	public static final int POTION = 2;
	public static final int TELEPORT = 3;
	public static final int UPGRADE = 4;
	public static final int MANUAL = 5;
	public static final int MATERIAL = 6;
	
	public static int inc = 0;
	public static ConcurrentMap<Integer, Integer> iteMap = new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentMap<Integer, Integer> iteMapSTACK = new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentMap<Integer, Integer> iteMapcharid = new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentMap<Integer, Long> iteMapTimedrop = new ConcurrentHashMap<Integer, Long>();
	
	public Item(){
	}
	public Consumables getConsumable(){
		return null;
	}
	//public Equipment getEquipment(){
	//	return null;
	//}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isConsumeable() {
		return consumeable;
	}

	public void setConsumeable(boolean consumeable) {
		this.consumeable = consumeable;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public int getNpcPrice() {
		return npcPrice;
	}

	public void setNpcPrice(int npcPrice) {
		this.npcPrice = npcPrice;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public int getMinLvl() {
		return minLvl;
	}

	public void setMinLvl(int minLvl) {
		this.minLvl = minLvl;
	}

	public int getMaxLvl() {
		return MaxLvl;
	}

	public void setMaxLvl(int maxLvl) {
		MaxLvl = maxLvl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public static byte[] itemSpawnPacket(int charID, int itemID, int STACK, float x, float y, Connection con) {
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		inc++;
		byte[] item = new byte[56];
		byte[] spawnX = BitTools.floatToByteArray(x);
		byte[] spawnY = BitTools.floatToByteArray(y);
		byte[] chid = BitTools.intToByteArray(inc);
		byte[] itid = BitTools.intToByteArray(itemID);
		byte[] INVstack = BitTools.intToByteArray(STACK);
		
		item[0] = (byte)item.length;
		item[4] = (byte)0x05;
		item[6] = (byte)0x0e;
		
		for(int i=0;i<4;i++) {
			item[36+i] = spawnX[i];
			item[40+i] = spawnY[i];
			item[20+i] = itid[i];
			item[32+i] = chid[i]; 
			item[28+i] = INVstack[i]; // drop stack
		}
		iteMapTimedrop.put(Integer.valueOf(inc), Long.valueOf(System.currentTimeMillis()));
		iteMapcharid.put(Integer.valueOf(inc), Integer.valueOf(charID));
		iteMap.put(Integer.valueOf(inc), Integer.valueOf(itemID));
		iteMapSTACK.put(Integer.valueOf(inc), Integer.valueOf(STACK));
		cur.sendToMap(item);
		
		return item;
	}
	


}
