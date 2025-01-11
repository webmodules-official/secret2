package Mob;


import java.util.HashMap;
import java.util.Map;

/*
 * MobData.class
 * Stores all mobs data 
 */

public class MobData implements Cloneable{
	private int lvl, attack, defence, maxhp, basefame;
	private long basexp;
	public int golddrop;
	private int boss;
	private int aggroRange;
	private int followRange = 500;
	private int moveRange = 500;
	private int attackRange = 15;
	private long respawnTime;
	private int [] grid;
	private int mobID;
	private int famerate100;
	public int attackcooldown;
	public int skill1,skill2,skill3;
	private int GeneralmobID;
	private int gridID;
	private int waypointCount, waypointHop;
	public Map<Integer, Integer> ItemDrop = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> RAREItemDrop = new HashMap<Integer, Integer>();
	private int inc = 0; // inc++; dropitemid 
	private int moveSpeed = 50;
	private int waypointDelay = 4;
	
	
	public int getMoveSpeed() {
				return this.moveSpeed;
	}
	
	public int getWaypointDelay() {
		return waypointDelay;
	}
	public void setWaypointDelay(int waypointDelay) {
		this.waypointDelay = waypointDelay;
	}
	
	public int getNORMALdropSize() {
		//System.out.println("RAREItemDrop.size(): " +RAREItemDrop.size());
		return ItemDrop.size(); 
	}
	
	public int getdropitemid(int i) {
		if(ItemDrop.containsKey(i)){
			int dropitemid = ItemDrop.get(i);
			//System.out.println("ItemDrop: " +i+" - " +dropitemid);
			return dropitemid;
		}else{ //System.out.println(i+" - null "); 
		return 0; 
		}
	}

	public void setdropitemid(int dropitemid) {
		inc++;
		ItemDrop.put(Integer.valueOf(inc), Integer.valueOf(dropitemid)); 
		//System.out.println("ItemDrop: " +inc+" - " +dropitemid);
	}
	
	public int getRAREdropitemid(int i) {
		if(RAREItemDrop.containsKey(i)){
			int RAREdropitemid = RAREItemDrop.get(i);
			//System.out.println("RAREdropitemid: " +i+" - " +RAREdropitemid);
			return RAREdropitemid;
		}else{ //System.out.println(i+" - null "); 
		return 0; 
		}
	}
	
	public int getRAREdropSize() {
		//System.out.println("RAREItemDrop.size(): " +RAREItemDrop.size());
		return RAREItemDrop.size(); 
	}

	public void setRAREdropitemid(int slot, int RAREdropitemid) {
		RAREItemDrop.put(slot, RAREdropitemid); 
		//System.out.println("RAREdropitemid: " +RAREinc+" - " +RAREdropitemid);
	}
	
	public int getLvl() {
		return lvl;
	}
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefence() {
		return defence;
	}
	public void setDefence(int defence) {
		this.defence = defence;
	}
	public long getBasexp() {
		return basexp;
	}
	public void setBasexp(long basexp) {
		this.basexp = basexp;
	}

	public int getAggroRange() {
		return aggroRange;
	}
	public void setAggroRange(int aggroRange) {
		this.aggroRange = aggroRange;
	}
	public int getFollowRange() {
		return followRange;
	}
	public void setFollowRange(int followRange) {
		this.followRange = followRange;
	}
	public int getMoveRange() {
		return moveRange;
	}
	public void setMoveRange(int moveRange) {
		this.moveRange = moveRange;
	}
	public void setgolddrop(int golddrop) {
		this.golddrop = golddrop;
	}
	public long getRespawnTime() {
		return respawnTime;
	}
	public void setRespawnTime(long respawnTime) {
		this.respawnTime = respawnTime;
	}
	
	public void setfamerate100(int famerate100) {
		this.famerate100 = famerate100;
	}
	
	public long getfamerate100() {
		return famerate100;
	}

	public int[] getGrid() {
		return grid;
	}
	public void setGrid(int[] grid) {
		this.grid = grid;
	}
	public int getMobID() {
		return mobID;
	}
	public void setMobID(int mobID) {
		this.mobID = mobID;
	}
	
	public int getGeneralMobID() {
		return GeneralmobID;
	}
	public void setGeneralMobID(int GeneralmobID) {
		this.GeneralmobID = GeneralmobID;
		//System.out.println("MOBData MobID : " + GeneralmobID); 

		/*try {
			ResultSet  rsa = Queries.getMobItemdrops(SQLconnection.getInstance().getConnection(), mobID).executeQuery();
			if(rsa.next()){
				 this.setdropitemid(rsa.getInt("itemid"));
				 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}*/
		 
		
	}
	public int getGridID() {
		return gridID;
	}
	public void setGridID(int gridID) {
		this.gridID = gridID;
		if(gridID == 1){this.aggroRange = 15;}
		else
		if(gridID == 2){this.aggroRange = 30;}
		else
		{this.aggroRange = 45;}
		//System.out.println("this.aggroRange = "+this.aggroRange);
	}
	public int getWaypointHop() {
		return waypointHop;
	}
	public void setWaypointHop(int waypointHop) {
		this.waypointHop = waypointHop;
	}
	public int getWaypointCount() {
		return waypointCount;
	}
	public void setWaypointCount(int waypointCount) {
		this.waypointCount = waypointCount;
	}
	public int getAttackRange() {
		return attackRange;
	}
	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}
	public int getBasefame() {
		return basefame;
	}
	public void setBasefame(int basefame) {
		this.basefame = basefame;
	}
	public int getMaxhp() {
		return maxhp;
	}
	public void setMaxhp(int maxhp) {
		this.maxhp = maxhp;
	}
	
	public int getboss() {
		return boss;
	}
	public void setboss(int boss) {
		this.boss = boss;
	}
}
