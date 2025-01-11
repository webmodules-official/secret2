package Mob;

import item.Item;

import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

import logging.ServerLogger;

import timer.MoveSyncTimer;
import timer.SystemTimer;
import Database.MobDAO;
import Database.Queries;
import Database.SQLconnection;
import Player.Character;
import ServerCore.ServerFacade;
import Tools.BitTools;
import Tools.StringTools;
import Player.Charstuff;
import Player.Guildwar;
import Player.Party;
import Player.Summon;
import World.Area;
import World.Grid;
import World.Location;
import World.WMap;
import World.Waypoint;


/*
 *  Mob.class
 *  Provides basic mob logic functions 
 */
public class Mob implements Location{
	private int mobID, uid, starstate;
	private MobData data;
	private MobController control;
	public ConcurrentMap<Integer, Integer>  iniPackets = new ConcurrentHashMap<Integer, Integer>();
	private ConcurrentMap<Integer, Character> TempBlock = new ConcurrentHashMap<Integer, Character>();
	public Waypoint spawn;
	public Waypoint location;
	public int hp, mana , FDD_FASR, FDD;
	int aggroID;
	int currentWaypoint;
	public int basexp;
	int basefame;
	private long aggrotimeout;
	private boolean alive, aggro;
	private Area area;
	private Grid grid;
	private long died = 0, New_RESPAWN_CD = 0;
	private int attackcooldown;
	private int Activecooldown = 1000;
	private int WPcooldown;
	private Waypoint TempPlrWaypoints;
	public long CdInterval, WaypInterval, CdActiveSync, one, CDonWP;
	private MobController Mobdata;
	public ConcurrentMap<Integer, Integer>  damage = new ConcurrentHashMap<Integer, Integer>();
	//private Map<Integer, Waypoint> GeneratedPath = Collections.synchronizedMap(new HashMap<Integer, Waypoint>());
	private List<Waypoint> GeneratedPath = new ArrayList<Waypoint>();
	private WMap wmap = WMap.getInstance();
	private static double inc = 0; 
	private int syncDistance = 20;
	private int speed = 20, NoMove = 0;
	public Map<Integer, Integer> DotsIconID = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> DotsValue = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> DotsTime = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> DotsSLOT = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> TempStoreBuffs = new HashMap<Integer, Integer>();
	private List<Waypoint> chain = new ArrayList<Waypoint>();
	private ServerLogger log = ServerLogger.getInstance();
	
	public Waypoint pop() {
		return this.chain.remove(0);
	}
	
	public void push(Waypoint wp) {
		this.chain.add(wp);
	}

	public void clearAll() {
		this.chain.clear();
	}
	
	public boolean isEmpty() {
		return this.chain.isEmpty();
	}
	
	public void populate(List<Waypoint> wps) {
		this.chain.addAll(wps);
	}
	
	// 4-Way Set DOTS
	public void setDots(int DotsSLOTz, int DotsIconIDz, int DotsTimez, int DotsValuez){ 	
		this.DotsIconID.put(DotsSLOTz, DotsIconIDz);
		this.DotsTime.put(DotsSLOTz, DotsTimez); 
		this.DotsValue.put(DotsSLOTz, DotsValuez); 
		this.DotsSLOT.put(DotsSLOTz, DotsSLOTz);
		
		//DotIcon , DotValue -> Get value by icon!!
	    if(DotsIconIDz == 43){this.NoMove = 1;}
	    if(DotsIconIDz == 46){this.NoMove = 1;}
	 	if(DotsIconIDz == 49){this.NoMove = 1;}
		this.settempstore(DotsIconIDz, DotsValuez);
		//System.out.println("setDots MOB:"+DotsSLOTz+" - "+DotsIconIDz+" - "+DotsTimez+" - "+DotsValuez);
	}
	
	
	public int getDotsSLOT(int one) {
		if(DotsSLOT.containsKey(one)){
		int invvalue = DotsSLOT.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsSLOT(int one, int two) {
		DotsSLOT.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("POTSLOT " +one+" - " +two);
	}
	
	public int getDotsIconID(int one) {
		if(DotsIconID.containsKey(one)){
		int invvalue = DotsIconID.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsIconID(int one, int two) {
		DotsIconID.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotIconID " +one+" - " +two);
	}
	
	public int getDotsTime(int one) {
		if(DotsTime.containsKey(one)){
		int invvalue = DotsTime.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsTime(int one, int two) {
		DotsTime.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotTime " +one+" - " +two);
	}
	
	public int getDotsValue(int one) {
		if(DotsValue.containsKey(one)){
		int invvalue = DotsValue.get(one);
		return invvalue;}else{return 0;}
	}
	public void setDotsValue(int one, int two) {
		DotsValue.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("PotValue" +one+" - " +two);
	}
	
	// temporary store buffs
	public int gettempstore(int buffslot) {
		if(TempStoreBuffs.containsKey(buffslot)){
		int buffvalue = TempStoreBuffs.get(buffslot);
		//System.out.println("TempStoreBuffs: " +buffslot+" - " +buffvalue);
		return buffvalue;}else
		{ //System.out.println(buffslot+" - null "); 
		return 0;}
	}

	public void settempstore(int bufficon, int buffvalue) {
	TempStoreBuffs.put(Integer.valueOf(bufficon), Integer.valueOf(buffvalue)); 
	//System.out.println("TempStoreBuffs: " +buffslot+" - " +buffvalue);
	}
	
	/*
	 * Initializes the mob
	 * Params:
	 * mobID = type of mob in question
	 * id = unique ID of mob
	 * mdata = pointer to mobs data object 
	 * * cont = pointer to this mobs MobController object
	 */
        
	public Mob(int mobID, int id, Waypoint mdata, MobController cont) {
		this.uid = id;
		this.setMobdata(cont); 
		this.attackcooldown = cont.data.attackcooldown;
		this.mobID = mobID;
		this.spawn = mdata;
		this.location = new Waypoint(mdata.getX(), mdata.getY());
		this.TempPlrWaypoints = new Waypoint(0, 0);
		////System.out.println("MOBlocation X, Y = : " + this.location.getX() +" - "+ this.location.getY()); 
		this.setData(cont.getData());
		this.setCurentWaypoint(0);
		this.currentWaypoint = 0;
		this.control = cont;
		
		if(getMobdata().data.getfamerate100() == 1){
			boolean rsnext = false;
		try {
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getdeathcd(SQLconnection.getInstance().getaConnection(), this.uid));
				if(rs.next()){
					this.setDied(rs.getLong("respawncooldown"));
					this.New_RESPAWN_CD = rs.getLong("New_RESPAWN_CD");
					rsnext = true;
				 }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			
			
		if(rsnext == true){
		this.alive = false;	
		}else{this.alive = true;}
		}else{this.alive = true;}
		// this.joinGrid(this.data.getGridID());
		this.wmap.AddMob(id, this, cont.getmap());
	}
	
	
	public void Run(){
		this.run(); 				
	}
	
        
	public int getMobID() {
		return mobID;
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
	// Join mob into the grid based proximity system 
	private void joinGrid(int grid){
		this.grid = this.wmap.getGrid(grid);
		if (!this.hasGrid()){ //System.out.println("FUUUUUUUUUuuuuuuuuuuu!!!!!"); 
		}
		else {
			Area a = this.grid.update(this);
			//System.out.println("Got area " + a.getuid());
			//System.out.print(".");
			this.setMyArea(a);
			this.iniPackets.putAll(a.addMemberAndGetMembers(this));
			this.reset(false);
			
		}
	}
	
	private boolean hasGrid() {
	
		return (this.grid != null);
	}


	private void setMyArea(Area a) {
		this.area = a;
		
	}

	// update our area
	private void updateArea(){
		Area a = this.grid.update(this);
		if (a != null){
		if (this.getMyArea() != a){
			this.getMyArea().moveTo(this, a);
			this.setMyArea(a);
			//a.addMember(this);
			ConcurrentMap<Integer,Integer> ls;
			  ls = this.area.addMemberAndGetMembers(this);
				Iterator<Integer> it = this.iniPackets.keySet().iterator();
				while (it.hasNext()){
					Integer i = it.next();
					if (!WMap.getInstance().CharacterExists(i)){
						it.remove();
						}
						else if (!ls.containsKey(i)){
					    it.remove();
					    //ServerFacade.getInstance().addWriteByChannel(this.wmap.getCharacter(i).GetChannel(), MobPackets.getDeathPacket(this.uid));
					    }
				 }
				this.removeAll(ls, iniPackets);
			   this.sendInitToList(ls);
			   this.iniPackets.putAll(ls);
		}
		}
	}
	
	private void sendInitToList(ConcurrentMap<Integer, Integer> ls) {
		Iterator<Integer> it = ls.keySet().iterator();
				Integer t = null;
				while(it.hasNext()){
					t = it.next();
					if (this.wmap.CharacterExists(t)){
						Character tmp = this.wmap.getCharacter(t);
						if(tmp != null && ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null && WMap.distance(this.location.getX(), this.location.getY(), tmp.getlastknownX(), tmp.getlastknownY()) <= 500){
						ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), this.getInitPacket());
						}else {
							it.remove();
						}
					}
					else {
						it.remove();
					}
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
	
	private Area getMyArea() {
		return this.area;
	}

	
	/* generates the waypoint needed for the idle moving pattern
	 * Params:
	 * wpc = waypoints per each side
	 * hop = distance between waypoints
	 * x = starting x coordinate
	 * y = starting y coordinate
	 */

		
	private void generateNewChain(int count){
				Random r = new Random();
				List<Waypoint> ls = new ArrayList<Waypoint>();
				for (int i=0; i<count; i++){
					int startx = -1* this.data.getMoveSpeed();
					int starty = -1* this.data.getMoveSpeed();
					int x = startx + r.nextInt(2 * this.data.getMoveSpeed());
					int y = starty + r.nextInt(2 * this.data.getMoveSpeed());
					
					ls.add(new Waypoint(this.getlastknownX() + (float)x, this.getlastknownY() + (float)y));
						for(int u=0;u<this.data.getWaypointDelay();u++) {
						ls.add(null);
					}
				}
				this.populate(ls);
	}
			
			public void run(){
			if(!this.hasGrid()) {
				this.joinGrid(this.data.getGridID());
			}
			
			try{	
			if(!this.DotsTime.isEmpty()){
			if (System.currentTimeMillis() - this.one > 4000 ){ 
				this.DotsTime();
				this.one = System.currentTimeMillis();
			}
			}else{
				this.one = System.currentTimeMillis();
			}
			
			//int i = this.getCurentWaypoint();
			//Waypoint wp;
			//MobController Mobdata = WMap.getInstance().GetMobController(this.uid); 
			 Character Tplayer = WMap.getInstance().getCharacter(this.getAggroID()); 
			// check if mob is alive
			if (this.isAlive()){
				//Do nothing if stun, root, sleep, No MASS BRO!
				if(NoMove == 1){
				// Do something if mob isnt moving
				}else
				// check if mob has gone too far from its initial spawn point
				if (WMap.distance(this.location.getX(), this.location.getY(), this.getSpawnx(), this.getSpawny()) > 500){
					//System.out.println(this.uid + " is too far from spawn");
					this.resetcurhp(true);
				}
				// logic if mob has been aggroed
				else 
					if(this.isAggro() && this.wmap.CharacterExists(this.getAggroID()) && Tplayer != null && this.iniPackets.containsKey(Tplayer.charID) && Tplayer.getHp() > 0 && ServerFacade.getInstance().getConnectionByChannel(Tplayer.GetChannel()) != null && WMap.distance(Tplayer.getlastknownX(), Tplayer.getlastknownY(), this.getSpawnx(), this.getSpawny()) <= 500 && Tplayer.getCurrentMap() == this.control.getmap()){
					//System.out.println(this.uid + " is aggroed by " + this.getAggroID());
							// attack target and/or move towards it
							if (WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) <= 20){
							if (System.currentTimeMillis() - this.CdInterval > this.attackcooldown ){ // attack cooldown
								this.attack();
								 Random s4 = new Random(); int s5 = 1+s4.nextInt(2); int newattackcooldown = 0;// 1 or 2 | + or -
								 Random s1 = new Random(); int s2 = 1+s1.nextInt(4); int Range = s2 * 100;// is 100 to 400 range
								if(s5 == 1){newattackcooldown = this.getMobdata().data.attackcooldown + Range;}else
								if(s5 == 2){newattackcooldown = this.getMobdata().data.attackcooldown - Range;}
								this.attackcooldown =  newattackcooldown; 
								this.CdInterval = System.currentTimeMillis();
								//System.out.println(this.uid + " is attacking " + this.getAggroID()+"  Cd:"+this.attackcooldown+" Range:"+Range);
							}}
							
							//TODO: get theright mob movement speed
							if (System.currentTimeMillis() - this.CdActiveSync > this.Activecooldown){ 
							if (this.TempPlrWaypoints.getX() !=  Tplayer.getlastknownX() && this.TempPlrWaypoints.getY() !=  Tplayer.getlastknownY()){	
							this.TempPlrWaypoints.setX(Tplayer.getlastknownX());this.TempPlrWaypoints.setY(Tplayer.getlastknownY());
							this.currentWaypoint = 0; if(this.GeneratedPath != null){this.GeneratedPath.clear();}
							if (WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) > 20){
							this.activesyncTest(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY());
							}}
						
								if (this.currentWaypoint < this.GeneratedPath.size()){
								//System.out.println("WTF:"+this.currentWaypoint+" - "+this.GeneratedPath.size());
								float x = this.GeneratedPath.get(this.currentWaypoint).getX();
								float y = this.GeneratedPath.get(this.currentWaypoint).getY();
							    this.syncLocation(x, y);
								this.currentWaypoint++;
								}this.CdActiveSync = System.currentTimeMillis();}
											
					}		
				// mob hasn't been aggroed
				else {
					this.resetDamage(); 
					// move mob to it's next waypoint
					this.checkAggro();
					if (!this.hasNextWaypoint()){
 						this.generateNewChain(20);
 					}
					if(System.currentTimeMillis() - this.CDonWP > this.WPcooldown){
						Waypoint wp = this.pop();
						if(wp != null) {
							this.setLocation(wp); // might cause the mob to go WTF LAND 
						}
						Random sWP = new Random();
						int sWP5 = sWP.nextInt(4);
						this.WPcooldown =  1000 * sWP5; 
						//System.out.println("this.WPcooldown :"+this.WPcooldown);
						this.CDonWP = System.currentTimeMillis();
					}
					
				}
				
			}
			/// check if its time to respawn
			else if (System.currentTimeMillis() - this.getDied() > this.New_RESPAWN_CD){
				//check for stones or so that cannot be respawned ever...... should just make a hashmap with collected mobids.. IGAF okaY?
				  if(this.mobID == 29501){}
				  else
				  if(this.mobID == 29502){}
				  else
				  if(this.mobID == 29503){}
				  else
				  if(this.mobID == 29504){}
				  else
				  if(this.mobID == 29505){}
				  else
				  if(this.mobID == 29506){}
				  else{
				  this.setAlive(true);
				  this.send(this.getInitPacket());
				  }
			}
			} catch (Exception e) {
				//////e.printStackTrace();
				log.logMessage(Level.SEVERE, Mob.class, "mobID:"+mobID+"  map:"+this.control.getMap()+" ___ "+StringTools.readStackTrace(e));
			}
		}
		
		

			
			
		      public void DotsTime(){
					  if(!this.DotsTime.isEmpty()){
							 for(int a=0;a<10;a++){
							 if(this.DotsTime.containsKey(a)){
							 int newtime = this.DotsTime.get(a) - 1; // current time - 12 seconds each 10 sec
							 if(newtime <= 0){
								    if(this.DotsIconID.get(a) == 43){this.NoMove = 0;}
								    if(this.DotsIconID.get(a) == 46){this.NoMove = 0;}
								 	if(this.DotsIconID.get(a) == 49){this.NoMove = 0;}
									if(this.DotsIconID.get(a) == 16){this.settempstore(16,0);}
									if(this.DotsIconID.get(a) == 15){this.settempstore(15,0);}
									this.RemoveDot(this.getDotsIconID(a), this.getDotsSLOT(a));
									this.DotsSLOT.remove(a);	 
									this.DotsIconID.remove(a);	
									this.DotsTime.remove(a); // x 4 = 36000 seconds = 600 minutes = 10 hours
									this.DotsValue.remove(a);
								 //System.out.println(a+":" +newtime+" - Death");	 
							 }else{
								 this.setDotsTime(a, newtime);
								 //System.out.println("newtime:"+ this.getHp());
								 if(this.DotsIconID.get(a) == 47){this.setHp(0,0,this.getHp()-this.DotsValue.get(a));}
								 //System.out.println("newtime:"+ this.getHp());
								 //System.out.println(a+":" +newtime+" - Continue");
							 }}}}
		    }
	
	
	// this method should only be called by active sync timer
	public void syncLocation(float x, float y){
		this.setX(x);
		this.setY(y);
		this.setPlayerLocation(this.location);
		Area t = this.grid.update(this);
		if (t != null){
			if (t != this.area){
				this.area.moveTo(this, t);
				this.area = t;
				this.area.addMember(this);
			}
		}
	}
	
	
//	@SuppressWarnings("unused")
	private void activesyncTest(float x, float y, float dx, float dy) {
		float deltax = WMap.distance(x, dx);
		float deltay = WMap.distance(y, dy);
		float hyp = WMap.distance(x, y, dx, dy);
		float sina = deltay / hyp;
		float cosa = deltax / hyp;
		float mpx = 1;
		float mpy = 1;
		float done = this.syncDistance;
		long period = (long)( (hyp / this.speed) * 1000);
		long delay = (long)((this.syncDistance / this.speed) * 1000);
		Waypoint wp = null;
		
		if (dx < x) mpx = -1;
		if (dy < y) mpy = -1;
		if (cosa == 1) cosa = 0;
		if (sina == 1) sina = 0;
		
		//System.out.println("Estimated total time to travel: " + period +"ms Interval time" +  delay + "ms");
		//System.out.println("Start coords x: " + x + " y: " +y);
		for (done = this.syncDistance; done < hyp; done += this.syncDistance){
			x += mpx * (cosa * this.syncDistance);
			y += mpy * (sina * this.syncDistance);
			wp = new Waypoint(x,y);
			this.GeneratedPath.add(wp);
			//System.out.println("inteval coords x: " + x + " y:" + y);
		}
		//System.out.println("end coords x: " + dx + " y: " +dy);
	}
	
	// resets without setting new HP
	private void resetcurhp(boolean sendMove){
		this.damage.clear();
		this.resetDamage();
		//this.setHp(this.hp); 
		//this.setCurentWaypoint(0);
		if (sendMove) this.send(MobPackets.getInitialPacket(mobID, uid, this.spawn, this.hp));
		this.resetToSpawn();      
		this.clearAll();
		this.updateArea();   
	}
	// resets mobs data
	private void reset(boolean sendMove){
		this.damage.clear();
		this.resetDamage();
		if (sendMove) this.send(MobPackets.getInitialPacket(mobID, uid, this.spawn, this.data.getMaxhp()));   
		this.hp = this.getData().getMaxhp(); 
		this.setCurentWaypoint(0);
		this.resetToSpawn();
		this.clearAll();
		this.updateArea();
              
	}
	
	// set hp and record dmg and the char uid
	public void setHp(int uid, int dmg,  int hp) {
		if(this.isAlive()){
		this.hp = hp;
		if(hp <= 0){this.setAlive(false);}
		
		if(uid != 0 && dmg != 0){this.recDamge(uid, dmg);}
		
		if(hp <= 0){
		this.NoMove = 0;
		this.DotsIconID.clear();
		this.DotsValue.clear();
		this.DotsTime.clear();
		this.DotsSLOT.clear();
		this.die();
		this.reset(false);	
		}
	   }
	}
	
	public int getHp(){
		return this.hp;
	}
	
	// perform actions needed to finalize mob's death
		private void die(){
		  Character Tplayer =  this.wmap.getCharacter(this.getAggroID());
		  try{
			if(Tplayer != null && ServerFacade.getInstance().getConnectionByChannel(Tplayer.GetChannel()) != null){
			int min = Tplayer.getLevel() - 8; // if char is 8 levels lower max
			if(getMobdata().data.getLvl() < min){Tplayer.recexp(0);}else{

				double Mobexp = 0; long FINALEXP;
				 long mobexp = this.getbasexp();	 
				 Random mobbexpdice = new Random();	 
				 int randommobexp = 1+mobbexpdice.nextInt(10);  // RANDOMIZER devided by 20% each!
				 if (randommobexp == 1){Mobexp = mobexp * 1.050;} 
				 if (randommobexp == 2){Mobexp = mobexp * 1.040;}
				 if (randommobexp == 3){Mobexp = mobexp * 1.035;} 
				 if (randommobexp == 4){Mobexp = mobexp * 1.025;}
				 if (randommobexp == 5){Mobexp = mobexp * 1.000;} 
				 if (randommobexp == 6){Mobexp = mobexp * 0.990;}
				 if (randommobexp == 7){Mobexp = mobexp * 0.985;} 
				 if (randommobexp == 8){Mobexp = mobexp * 0.975;}
				 if (randommobexp == 9){Mobexp = mobexp * 0.960;} 
				 if (randommobexp == 10){Mobexp = mobexp *0.950;}
				 
					if(getMobdata().data.getfamerate100() == 1)
					{
					FINALEXP = (long)Mobexp;
					this.sendToMapPARTYEXP(FINALEXP * 1,Tplayer);
					this.setStarstate(1);	
					}else if(Tplayer.GREATER_JACKPOT_TAG == 1){
					 Random Rfnz= new Random();	 
					 int Randomfinalexp = 1+Rfnz.nextInt(65);  // RANDOMIZER devided by 1/50! ( = 2%)
					 if (Randomfinalexp == 34){ FINALEXP = (long)Mobexp * 45;this.setStarstate(1);}
					 else {FINALEXP = (long)Mobexp;}
					 //System.out.println("Mob: GREATER_JACKPOT_TAG : "+ Randomfinalexp+"/60");
					 this.sendToMapPARTYEXP(FINALEXP * 1,Tplayer);
					}else if(Tplayer.JACKPOT_TAG == 1){
					 Random Rfnz= new Random();	 
					 int Randomfinalexp = 1+Rfnz.nextInt(90);  // RANDOMIZER devided by 1/75! ( = 1.50%)
					 if (Randomfinalexp == 34){ FINALEXP = (long)Mobexp * 45;this.setStarstate(1);}
					 else {FINALEXP = (long)Mobexp;}
					 //System.out.println("Mob: GREATER_JACKPOT_TAG : "+ Randomfinalexp+"/80");
					 this.sendToMapPARTYEXP(FINALEXP * 1,Tplayer);
					 }else{
					 Random Rfnz= new Random();	 
					 int Randomfinalexp = 1+Rfnz.nextInt(750);  // RANDOMIZER devided by 1/750 !( = 0.15%)
					 if (Randomfinalexp == 179){ FINALEXP = (long)Mobexp * 45;this.setStarstate(1);}
					 else {FINALEXP = (long)Mobexp;}
					 //System.out.println("Mob: GREATER_JACKPOT_TAG : "+ Randomfinalexp+"/750");
					 this.sendToMapPARTYEXP(FINALEXP * 1,Tplayer);
					 }
					
					//int EventDropLimit = getMobdata().data.getRAREdropSize() * 100;  
			
			// make normal drop levelbased
					
					/*if(Tplayer.getCurrentMap() == 7||Tplayer.getCurrentMap() == 8||Tplayer.getCurrentMap() == 9){
					if(Tplayer.getLevel() >= 1 && Tplayer.getLevel() <= 60){	
						if(getMobdata().data.getboss() == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
						else		
						if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
						else
						if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 3);}
						else
						{this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 10);}
						}else 		
						if(Tplayer.getLevel() >= 61 && Tplayer.getLevel() <= 100){		
						if(getMobdata().data.getboss() == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
						else		
						if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 6);}
						else
						if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 4);}
						else
						{this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 15);}
						}else 	
							if(Tplayer.getLevel() > 100){
								if(getMobdata().data.getboss() == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
								else		
								if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 7);}
								else
								if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
								else
								{this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 20);}	
							}
			}else{*/
				if(Tplayer.getLevel() >= 1 && Tplayer.getLevel() <= 48 && this.data.getLvl() < 65){	
				if(getMobdata().data.getboss() == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
				else		
				if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 3);}
				else
				if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 2);}
				else
				{this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
				}else 			
			if(Tplayer.getLevel() >= 1 && Tplayer.getLevel() <= 60){	
			if(getMobdata().data.getboss() == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 6);}
			else		
			if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 7);}
			else
			if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 5);}
			else
			{this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 15);}
			}else 		
			if(Tplayer.getLevel() >= 61 && Tplayer.getLevel() <= 100){		
			if(getMobdata().data.getboss() == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 8);}
			else		
			if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 10);}
			else
			if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 7);}
			else
			{this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 20);}
			}else 
				if(Tplayer.getLevel() > 100){
					if(getMobdata().data.getboss() == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 10);}
					else		
					if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 14);}
					else
					if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 10);}
					else
					{this.sendNORMALitemtomap(getMobdata().data.getNORMALdropSize() * 30);}	
				}
			//}
			
			
			// sky zone map jizz
			if(getMobdata().data.getLvl() >= 130 && getMobdata().getmap() == 9){
			int random_size;
			if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){random_size = Charstuff.getInstance().mobdrops_skyzone.size() * 10;}
			else
			if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){random_size = Charstuff.getInstance().mobdrops_skyzone.size() * 7;}
			else
			{random_size = Charstuff.getInstance().mobdrops_skyzone.size() * 20;}	
			
			 Random randomdropdice = new Random();	 
			 int randomdrop = 1+randomdropdice.nextInt(random_size); 
			// System.out.println("sendNORMALitemtomap: "+Charstuff.getmobdrops_skyzone(randomdrop)+" = "+randomdrop+" / "+ random_size);
			int ItemID = Charstuff.getInstance().getmobdrops_skyzone(randomdrop);
			if(ItemID != 0){
				Item.inc++;
				this.send(MobPackets.mobitemSpawnPacket(this.getAggroID(), ItemID, 1, this.getlastknownX(), this.getlastknownY()));
			}}
			
			//mob must be 36+ in order to drop rare
			if(getMobdata().data.getLvl() >= 36){
			if(Tplayer.DOUBLE_ITEM_DROP_TAG == 1){this.sendRAREitemtomap(getMobdata().data.getRAREdropSize() * 300);}//300
			else
			if(Tplayer.GREATER_DOUBLE_ITEM_DROP_TAG == 1){this.sendRAREitemtomap(getMobdata().data.getRAREdropSize() * 250);}//250
			else
			{this.sendRAREitemtomap(getMobdata().data.getRAREdropSize() * 400);}//400
			}
			}
			if(getMobdata().data.getLvl() >= 36){
			if(getMobdata().data.getfamerate100() != 1){
			 Random r = new Random();
			 int a = 1+r.nextInt(15); // 10 = 10% 20 = 5% 40 = 2.5%
			 if(a == 7){
			 Random mobbfamedice = new Random();
			 double fame = 0;
			 int randommobfame = 1+mobbfamedice.nextInt(10);  // RANDOMIZER devided by 10% each!
			 if (randommobfame == 1){fame = getMobdata().data.getBasefame() * 1.300;} 
			 if (randommobfame == 2){fame = getMobdata().data.getBasefame() * 1.250;}
			 if (randommobfame == 3){fame = getMobdata().data.getBasefame() * 1.200;} 
			 if (randommobfame == 4){fame = getMobdata().data.getBasefame() * 1.150;}
			 if (randommobfame == 5){fame = getMobdata().data.getBasefame() * 1.000;} 
			 if (randommobfame == 6){fame = getMobdata().data.getBasefame() * 0.850;}
			 if (randommobfame == 7){fame = getMobdata().data.getBasefame() * 0.800;} 
			 if (randommobfame == 8){fame = getMobdata().data.getBasefame() * 0.750;}
			 if (randommobfame == 9){fame = getMobdata().data.getBasefame() * 0.700;} 
			 if (randommobfame == 10){fame = getMobdata().data.getBasefame() *0.650;}
			 this.sendToMapPARTYFAME((int)fame,Tplayer);
			 }}else{
				 Random mobbfamedice = new Random();
				 double fame = 0;
				 int randommobfame = 1+mobbfamedice.nextInt(10);  // RANDOMIZER devided by 10% each!
				 if (randommobfame == 1){fame = getMobdata().data.getBasefame() * 1.300;} 
				 if (randommobfame == 2){fame = getMobdata().data.getBasefame() * 1.250;}
				 if (randommobfame == 3){fame = getMobdata().data.getBasefame() * 1.200;} 
				 if (randommobfame == 4){fame = getMobdata().data.getBasefame() * 1.150;}
				 if (randommobfame == 5){fame = getMobdata().data.getBasefame() * 1.000;} 
				 if (randommobfame == 6){fame = getMobdata().data.getBasefame() * 0.850;}
				 if (randommobfame == 7){fame = getMobdata().data.getBasefame() * 0.800;} 
				 if (randommobfame == 8){fame = getMobdata().data.getBasefame() * 0.750;}
				 if (randommobfame == 9){fame = getMobdata().data.getBasefame() * 0.700;} 
				 if (randommobfame == 10){fame = getMobdata().data.getBasefame() *0.650;}
				 this.sendToMapPARTYFAME((int)fame,Tplayer);	
			}}
			}
			 int golddrop = getMobdata().data.golddrop;
			 if(golddrop <= 0){golddrop = 1;}
			 if(golddrop >= 88000000){golddrop = 2;}
			Item.inc++;
			this.send(MobPackets.mobitemSpawnPacket(this.getAggroID(), 217000501, golddrop, this.getlastknownX(), this.getlastknownY()));
			if(this.starstate == 1){this.send(MobPackets.getDeathPacketSTAR(this.uid));this.setStarstate(0);}else{this.send(MobPackets.getDeathPacket(this.uid));}
			this.setDied(System.currentTimeMillis());
			 long Original_RT = this.getMobdata().data.getRespawnTime();
			 long New_RT = 0;
			 Random r = new Random();
			 int R = 1+r.nextInt(3);
			 if(R == 1){New_RT = Original_RT * 1;}
			 if(R == 2){New_RT = Original_RT * 2;}
			 if(R == 3){New_RT = Original_RT * 3;}
			 this.New_RESPAWN_CD = New_RT;
			 if(getMobdata().data.getfamerate100() == 1){
			 //update the death cooldown in db = gg
			 MobDAO.deletemobrespawncd(this.uid);	 
			 MobDAO.savemobrespawncd(this.uid, this.getDied(), New_RT);
			 }
			  if(Guildwar.getInstance().isguildwar() && this.mobID == 29501){
				  Guildwar.getInstance().stonedie(this.mobID, Tplayer);
			  }
			  if(Guildwar.getInstance().isguildwar() && this.mobID == 29502){
				  Guildwar.getInstance().stonedie(this.mobID, Tplayer);				 
			  }
			  if(Guildwar.getInstance().isguildwar() && this.mobID == 29503){
				  Guildwar.getInstance().stonedie(this.mobID, Tplayer); 
			  }
			  if(Guildwar.getInstance().isguildwar() && this.mobID == 29504){
				  Guildwar.getInstance().stonedie(this.mobID, Tplayer); 
			  }
			  if(Guildwar.getInstance().isguildwar() && this.mobID == 29505){
				  Guildwar.getInstance().stonedie(this.mobID, Tplayer); 
			  }
			  if(Guildwar.getInstance().isguildwar() && this.mobID == 29506){
				  Guildwar.getInstance().stonedie(this.mobID, Tplayer); 
			  }
			 //System.out.println("Mob: this.New_RESPAWN_CD = " +R+" - "+this.New_RESPAWN_CD);
			} catch (Exception e) {
				//////e.printStackTrace();
				log.logMessage(Level.SEVERE, Mob.class, "mobID:"+mobID+"  map:"+this.control.getMap()+" ___ "+StringTools.readStackTrace(e));
			}
	}
		
		
		// set TPlayer PARTY EXP / EXP
		public void sendToMapPARTYEXP(double kuntakinte, Character Tplayer) {
				 long recexp = (long)kuntakinte;
				 int min = Tplayer.getLevel() - 15;
				 int max = Tplayer.getLevel() + 15;
				 double partyexp = 1;
				 
					if(Tplayer.partyUID != 0)
					{
					Party pt = wmap.getParty(Tplayer.partyUID);
					if(pt != null){
					// if this guy is in party and in rc / mid / sz then add 25% exp
					if(Tplayer.getCurrentMap() == 7||Tplayer.getCurrentMap() == 8||Tplayer.getCurrentMap() == 9){
					if(pt.partymembers.size() == 2){partyexp = recexp * 1.25;}
					else
					if(pt.partymembers.size() == 3){partyexp = recexp * 1.10;}
					else
					if(pt.partymembers.size() == 4){partyexp = recexp * 0.95;}
					else
					if(pt.partymembers.size() == 5){partyexp = recexp * 0.80;}
					else
					if(pt.partymembers.size() == 6){partyexp = recexp * 0.65;}
					else
					if(pt.partymembers.size() == 7){partyexp = recexp * 0.50;}
					else
					if(pt.partymembers.size() >= 8){partyexp = recexp * 0.35;}
					else						   {partyexp = recexp * 1.00;}
					}else{
						if(pt.partymembers.size() == 2){partyexp = recexp * 0.75;}
						else
						if(pt.partymembers.size() == 3){partyexp = recexp * 0.65;}
						else
						if(pt.partymembers.size() == 4){partyexp = recexp * 0.55;}
						else
						if(pt.partymembers.size() == 5){partyexp = recexp * 0.45;}
						else
						if(pt.partymembers.size() == 6){partyexp = recexp * 0.35;}
						else
						if(pt.partymembers.size() == 7){partyexp = recexp * 0.25;}
						else
						if(pt.partymembers.size() >= 8){partyexp = recexp * 0.20;}
						else						   {partyexp = recexp * 1.00;}
					}
					}
					}else{
						 partyexp = recexp * 1.00;	//normal single player no party
					}
					
					Tplayer.recexp(this.LevelDifference(Tplayer.getLevel(), (long)partyexp, 1));
				 
					if(Tplayer.partyUID != 0)
					{
						Iterator<Integer> iter = this.iniPackets.keySet().iterator();
					while(iter.hasNext()) {   
							Character ch = this.wmap.getCharacter(iter.next().intValue());
							if(ch != null && ch.charID != Tplayer.charID && ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()) != null) {
								if(Tplayer.partyUID != 0 && ch.partyUID != 0 && ch.partyUID == Tplayer.partyUID && ch.getLevel() >= min && ch.getLevel() <= max && ch.getHp() > 0 && !this.TempBlock.containsKey(ch.charID)){
									this.TempBlock.put(ch.charID, ch);	 
									ch.recexp(this.LevelDifference(ch.getLevel(), (long)partyexp, 1));
							}
						}
					}
				this.TempBlock.clear();	 
			}
		}
		
		// set TPlayer PARTY FAME / FAME
				public void sendToMapPARTYFAME(int recfame, Character Tplayer) {
						 int min = Tplayer.getLevel() - 15;
						 int max = Tplayer.getLevel() + 15;
						 double partyfame = 0;
						 
							if(Tplayer.partyUID != 0)
							{
							Party pt = wmap.getParty(Tplayer.partyUID);
							if(pt != null){
							// if this guy is in party and in rc / mid / sz then add 25% exp
							if(Tplayer.getCurrentMap() == 7||Tplayer.getCurrentMap() == 8||Tplayer.getCurrentMap() == 9){
							if(pt.partymembers.size() == 2){partyfame = recfame * 1.25;}
							else
							if(pt.partymembers.size() == 3){partyfame = recfame * 1.10;}
							else
							if(pt.partymembers.size() == 4){partyfame = recfame * 0.95;}
							else
							if(pt.partymembers.size() == 5){partyfame = recfame * 0.80;}
							else
							if(pt.partymembers.size() == 6){partyfame = recfame * 0.65;}
							else
							if(pt.partymembers.size() == 7){partyfame = recfame * 0.50;}
							else
							if(pt.partymembers.size() >= 8){partyfame = recfame * 0.35;}
							else						   {partyfame = recfame * 1.00;}
							}else{
								if(pt.partymembers.size() == 2){partyfame = recfame * 1.00;}
								else
								if(pt.partymembers.size() == 3){partyfame = recfame * 0.85;}
								else
								if(pt.partymembers.size() == 4){partyfame = recfame * 0.70;}
								else
								if(pt.partymembers.size() == 5){partyfame = recfame * 0.55;}
								else
								if(pt.partymembers.size() == 6){partyfame = recfame * 0.40;}
								else
								if(pt.partymembers.size() == 7){partyfame = recfame * 0.35;}
								else
								if(pt.partymembers.size() >= 8){partyfame = recfame * 0.20;}
								else						   {partyfame = recfame * 1.00;}
							}
							}
							}else{
								partyfame = recfame * 1.00;	//normal single player no party
							}
							
							Tplayer.setFame(Tplayer.getFame() + (int)partyfame);
							this.send(MobPackets.famepacket(this.uid, Tplayer, (int)partyfame));
							
							if(Tplayer.partyUID != 0)
							{
								Iterator<Integer> iter = this.iniPackets.keySet().iterator();
							while(iter.hasNext()) {   
									Character ch = this.wmap.getCharacter(iter.next().intValue());
									if(ch != null && ch.charID != Tplayer.charID && ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()) != null) {
											 if(Tplayer.partyUID != 0 && ch.partyUID != 0 && ch.partyUID == Tplayer.partyUID && ch.getLevel() >= min && ch.getLevel() <= max && ch.getHp() > 0 && !this.TempBlock.containsKey(ch.charID)){// if is within level range
											 this.TempBlock.put(ch.charID, ch);	 
											ch.setFame(ch.getFame() + (int)partyfame);
											this.send(MobPackets.famepacket(this.uid, ch, (int)partyfame));
									}
								}
							}
							this.TempBlock.clear();	 
					}
				}
		
		
		private void sendRAREitemtomap(int Limit) { 
			if(Limit <= 0){return;}
			 Random randomdropdice = new Random();	 
			 int randomdrop = 1+randomdropdice.nextInt(Limit); 
				//System.out.println("randomdropdice: "+randomdrop+" - "+ Limit);
			int ItemID = getMobdata().data.getRAREdropitemid(randomdrop);
			if(ItemID != 0){
				Item.inc++;
				this.send(MobPackets.mobitemSpawnPacket(this.getAggroID(), ItemID, 1, this.getlastknownX(), this.getlastknownY()));
				//System.out.println("ItemID: "+ItemID);
				if(ItemID == 215003004){// if is jade then also dorp sht else
					Item.inc++;
					//System.out.println("Dropping pb");
					this.send(MobPackets.mobitemSpawnPacket(this.getAggroID(), 273001207, 1, this.getlastknownX(), this.getlastknownY()));
				}
			}
		}	
		
		private void sendNORMALitemtomap(int Limit) {
			if(Limit <= 0){return;}
			 Random randomdropdice = new Random();	 
			 int randomdrop = 1+randomdropdice.nextInt(Limit); 
			 //System.out.println("sendNORMALitemtomap: "+randomdrop+" - "+ Limit);
			int ItemID = getMobdata().data.getdropitemid(randomdrop);
			if(ItemID != 0){
				this.send(MobPackets.mobitemSpawnPacket(this.getAggroID(), ItemID, 1, this.getlastknownX(), this.getlastknownY()));
			}
		}	
		
		
		public int getAttack() {
			////System.out.println("MOB: General_ID = "+ this.uid);
			return getMobdata().data.getAttack() + this.gettempstore(15);
			
		}
		public int getDefence() {
			//System.out.println("MOB: getDefence = "+ getMobdata().data.getDefence() + this.gettempstore(16));
			if (this.wmap.CharacterExists(this.getAggroID())){
				Character Tplayer = this.wmap.getCharacter(this.getAggroID());
				//System.out.println("MOB: DEF = "+ (int)Charstuff.getInstance().LevelDifference(this, Tplayer.getLevel(), getMobdata().data.getDefence() + this.gettempstore(16), 3));
				return (int)this.LevelDifference(Tplayer.getLevel(), getMobdata().data.getDefence() + this.gettempstore(16), 3);
			}else{
			return getMobdata().data.getDefence() + this.gettempstore(16);
			}
			
		}
		
		
		public long LevelDifference(int TLevel, long one, int DETERMINER){
			
			// new level	
			int newlevel = this.getMobdata().getData().getLvl() - TLevel;
			
			if(DETERMINER == 1){ // Experiance from mob based on level difference
			if(newlevel <= 50){return one;}
			
			double ManualEquiv = 0;
			//if its positive then we have to substract some exp [ 66875914 = 150 insect ]
																		 //66875914 <= 50 | 50 levels lower of the mob = 0%  off
			if(newlevel > 50 && newlevel <= 63){  ManualEquiv = 0.0024;} //56875914 == 63 | 63 levels lower of the mob = 15% off
			if(newlevel > 63 && newlevel <= 75){  ManualEquiv = 0.0040;} //46875914 == 75 | 75 levels lower of the mob = 25% off
			if(newlevel > 75 && newlevel <= 83){  ManualEquiv = 0.0070;} //36875914 == 83 | 83 levels lower of the mob = 65% off
			if(newlevel > 83 && newlevel <= 100){ ManualEquiv = 0.0080;} //26875914 == 100| 100levels lower of the mob = 75% off
			if(newlevel > 100){ 				  ManualEquiv = 0.0090;}
			
			double Nipple = one * ManualEquiv; // get the right equiv
			double NewEquiv = (long)Nipple * newlevel;// Equiv * the levels = exp we are going to substract from "one" exp.
			long newexp = one - (long)NewEquiv;// substract with NewEquiv..

			return newexp;
			}else if(DETERMINER == 2){// Mob Critical Chance On Player
				
			boolean win = false; // 1/20 = max
			
			// if its a boss then diff calcs
			if(this.getMobID() == 14529
			||this.getMobID() == 14530
			||this.getMobID() == 14531
			||this.getMobID() == 14532
			){
			win = Dice(1,3);	
			}else{// else if not a boss then normal calc
			if(					newlevel <= 50){ win = Dice(1,5);}
			if(newlevel > 50 && newlevel <= 63){ win = Dice(1,4);}
			if(newlevel > 63 && newlevel <= 75){ win = Dice(1,3);} 
			if(newlevel > 75 && newlevel <= 83){ win = Dice(1,2);} 
			if(newlevel > 83 && newlevel <= 100){win = Dice(1,1);} 
			if(newlevel > 100){ 				 win = Dice(1,1);}
			}
			if(win){return 1;}else{return 0;}
			}else if(DETERMINER == 3){// Higher mobs have more DEF to lower level players!
				double win = 0;
				if(					newlevel <= 50){ win = one * 1.000;}
				if(newlevel > 50 && newlevel <= 63){ win = one * 1.100;}
				if(newlevel > 63 && newlevel <= 75){ win = one * 1.200;} 
				if(newlevel > 75 && newlevel <= 83){ win = one * 1.300;} 
				if(newlevel > 83 && newlevel <= 100){win = one * 1.400;} 
				if(newlevel > 100){					 win = one * 1.500;}
				return (long)win;
			}
			return 0;
		}
		
		public void RemoveDot(int DotsIconID, int DotsSLOT){//DETERMINE: 1 = player | 2 = mob
			if(this != null){
			byte[] chid = BitTools.intToByteArray(this.getUid());
			byte[] buff = new byte[44];
			 buff[0] = (byte)0x2c; 
			 buff[4] = (byte)0x05;
			 buff[6] = (byte)0x1f;
			 buff[8] = (byte)0x02;// 1 = player | 2 = mob
			 for(int i=0;i<4;i++){
			 buff[12+i] = chid[i]; 				
			 }	
			 buff[26] = (byte)0x01; 
			 buff[28] = (byte)0x89; 
			 buff[32] = (byte)0x89; 
			 buff[36] = (byte)0x7e; 
			 buff[38] = (byte)0x7e; 
			 buff[40] = (byte)0x60; 
			 buff[42] = (byte)0x60;	 
			 
			 byte[] buffidz1 = BitTools.intToByteArray(0); 
			 byte[] bufftimez1 = BitTools.intToByteArray(0);
			 byte[] buffvaluez1 = BitTools.intToByteArray(0);
			 byte[] buffslotz1 = BitTools.intToByteArray(DotsSLOT);
			 
				 for(int i=0;i<2;i++) {
					 buff[i+16] = buffslotz1[i];	 // buffslot
					 buff[i+20] = buffidz1[i];	 // buff id
					 buff[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
					 buff[i+24] = buffvaluez1[i]; // Value XXXXX
				 }	
			 if(DotsIconID == 43||DotsIconID == 46||DotsIconID == 49||DotsIconID == 44||DotsIconID == 45||DotsIconID == 47||DotsIconID == 48){this.send(buff);}
		}}
		
		
		public boolean Dice(int rate, int limit){
			Random dice = new Random();
			 int Hit = 1+dice.nextInt(limit);  // 1/4 on miss
			 if(rate == 1){
			 if(Hit == 1){return true;}
			 }else
			 if(rate == 2){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 }else
			 if(rate == 3){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
		     }else
			 if(rate == 4){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
		     }	else
			 if(rate == 5){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
		     }else
			 if(rate == 6){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
		     }else
			 if(rate == 7){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
		     }else
			 if(rate == 8){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
		     }else
			 if(rate == 9){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
		     }else
			 if(rate == 10){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
		     }else
			 if(rate == 11){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
		     }else
			 if(rate == 12){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
		     }else
			 if(rate == 13){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
		     }else
			 if(rate == 14){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
			 if(Hit == 14){return true;}
		     }else
			 if(rate == 15){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
			 if(Hit == 14){return true;}
			 if(Hit == 15){return true;}
		     }else
			 if(rate == 16){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
			 if(Hit == 14){return true;}
			 if(Hit == 15){return true;}
			 if(Hit == 16){return true;}
		     }else
			 if(rate == 17){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
			 if(Hit == 14){return true;}
			 if(Hit == 15){return true;}
			 if(Hit == 16){return true;}
			 if(Hit == 17){return true;}
		     }else
			 if(rate == 18){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
			 if(Hit == 14){return true;}
			 if(Hit == 15){return true;}
			 if(Hit == 16){return true;}
			 if(Hit == 17){return true;}
			 if(Hit == 18){return true;}
		     }else
			 if(rate == 19){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
			 if(Hit == 14){return true;}
			 if(Hit == 15){return true;}
			 if(Hit == 16){return true;}
			 if(Hit == 17){return true;}
			 if(Hit == 18){return true;}
		     if(Hit == 19){return true;}
		     }else
			 if(rate == 20){
			 if(Hit == 1){return true;}
			 if(Hit == 2){return true;}
			 if(Hit == 3){return true;}
			 if(Hit == 4){return true;}
			 if(Hit == 5){return true;}
			 if(Hit == 6){return true;}
			 if(Hit == 7){return true;}
			 if(Hit == 8){return true;}
			 if(Hit == 9){return true;}
			 if(Hit == 10){return true;}
			 if(Hit == 11){return true;}
			 if(Hit == 12){return true;}
			 if(Hit == 13){return true;}
			 if(Hit == 14){return true;}
			 if(Hit == 15){return true;}
			 if(Hit == 16){return true;}
			 if(Hit == 17){return true;}
			 if(Hit == 18){return true;}
			 if(Hit == 19){return true;}
			 if(Hit == 20){return true;}
		     }
		return false;
		}

		public long getbasexp() {
			////System.out.println("MOB: General_ID = "+ this.uid);
			return getMobdata().data.getBasexp();
			
		}
		
		public int getbasefame() {
			return getMobdata().data.getBasefame();
		}
		
		// attack target loc
		private void attack() {
			 int mobatk = this.getAttack();	
			 int skill = 0;
			 Random r = new Random();
			 int R = 1+r.nextInt(3);
			 if(R == 1){skill = this.getMobdata().data.skill1; inc = mobatk * 1.000;}
			 if(R == 2){skill = this.getMobdata().data.skill2; inc = mobatk * 1.050;}
			 if(R == 3){skill = this.getMobdata().data.skill3; inc = mobatk * 1.100;}
			 if(skill == 0){skill = this.getMobdata().data.skill1;}
			 //System.out.println("MOB: inc = "+inc);
			 Random mobbexpdice = new Random();	 
			 int randommobexp = 1+mobbexpdice.nextInt(10);  // RANDOMIZER devided by 20% each!
			 if (randommobexp == 1){inc = inc * 1.050;} 
			 if (randommobexp == 2){inc = inc * 1.040;}
			 if (randommobexp == 3){inc = inc * 1.035;} 
			 if (randommobexp == 4){inc = inc * 1.025;}
			 if (randommobexp == 5){inc = inc * 1.000;} 
			 if (randommobexp == 6){inc = inc * 0.990;}
			 if (randommobexp == 7){inc = inc * 0.985;} 
			 if (randommobexp == 8){inc = inc * 0.975;}
			 if (randommobexp == 9){inc = inc * 0.960;} 
			 if (randommobexp == 10){inc = inc *0.950;}
			// System.out.println("MOB: inc = "+inc);
			 if(this.mobID == 14529 && R == 1
			 ||this.mobID == 14530 && R == 1
			 ||this.mobID == 14531 && R == 1
			 ||this.mobID == 14532 && R == 1
			 ){
					Iterator<Integer> iter = this.iniPackets.keySet().iterator();
						while(iter.hasNext()) {   
								Character ch = this.wmap.getCharacter(iter.next().intValue());
								if(ch != null &&  ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()) != null) {
										 if(ch.getHp() > 0 && !this.TempBlock.containsKey(ch.charID)){// if is within level range
										 this.TempBlock.put(ch.charID, ch);	 
										 if (WMap.distance(this.location.getX(), this.location.getY(), ch.getlastknownX(), ch.getlastknownY()) <= 50){
										 this.send(MobPackets.attackplayer(this, this.uid, (int)inc, ch.charID, skill));
										 }
								}
							}
						}

			 this.TempBlock.clear();	 	 
			 }else{
			 this.send(MobPackets.attackplayer(this, this.uid, (int)inc, this.getAggroID(), skill));}
			inc = 0; // reset to 0
		}

		private void resetToSpawn() {
			this.setX(this.getSpawnx());
			this.setY(this.getSpawny());
		}

		// handle damages receiving
		public void recDamge(int uid, int dmg){
			if (this.hasPlayerDamage(uid)){
				int tmp = this.getPlayerDamage(uid);
				tmp += dmg;
				this.setDamage(uid, tmp);
			}
			else{
				this.setDamage(uid, dmg);
			}

				Iterator <Map.Entry<Integer, Integer>> it = this.damage.entrySet().iterator();
				int key;
				int value = 0;
				int hiDmg = 0;
				int hiID = 0;
				while (it.hasNext()) {
					Map.Entry<Integer, Integer> pairs = it.next();
					key = pairs.getKey();
					value = pairs.getValue();
					if (value > hiDmg){
						hiDmg = value;
						hiID = key;
					}
				}
				//System.out.println("recDamge chid, highestdmg : "+hiID+" - "+hiDmg);
				this.setAggro(true);
				this.setAggroID(hiID);              

			/*this.reduceHp(dmg);
			if (this.hp <= 0) {this.die();}*/
		}
/*
 * 
 * THE CAUSE OF THE BUG OMFG WTFFFFFFFFFFFFFFFFFFFFFFFFFF===================----------TWF MAN LOLOL
 * 
 */
	// check if mob is close enough to player to aggro it
	private boolean checkAggro() {
		////System.out.println("Mob" + this.uid +" aggrocheck"  + Thread.currentThread());
		boolean hasPlayers = false;
			Iterator<Integer> iter = this.iniPackets.keySet().iterator();
      	while(iter.hasNext()) {
      		Integer it =  iter.next();
      		      		Character loc = this.wmap.getCharacter(it);
      		        		if (loc != null && ServerFacade.getInstance().getCon().getConnection(loc.GetChannel()) != null){
      		        				hasPlayers = true;
      		        				if (WMap.distance(this.location.getX(), this.location.getY(), loc.getlastknownX(), loc.getlastknownY()) < this.data.getAggroRange()){
      		        				if(getMobdata().data.getLvl() > loc.getLevel()){
      		        					this.setAggro(true);
      		        					this.setAggroID(loc.getuid());
      		        					break;
      		        				}     
      		        			    }
      		        		}
      		        		else{
      		        			iter.remove();
      		        		}
     		}
      return hasPlayers;
  }
	

	// send packet to all nearby players
	public void send(byte[] buf) {			
		Iterator<Integer> iter = this.iniPackets.keySet().iterator();
						while(iter.hasNext()) {
							Integer plUid = iter.next();    
							if(this.wmap.CharacterExists(plUid.intValue())){
							Character ch = this.wmap.getCharacter(plUid.intValue());
							if(ch != null && ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()) != null && WMap.distance(this.location.getX(), this.location.getY(), ch.getlastknownX(), ch.getlastknownY()) <= 500) {
								ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), buf);
							}else{
								iter.remove();
							}
							}else{
								iter.remove();
							}
				}
		}
		// set mobs location on the map and send move packet to players
		private void setLocation(Waypoint wp){
			if(wp != null) {
				this.setX(wp.getX());
				this.setY(wp.getY());
				this.updateArea();	
				//	//System.out.println("Mob" + this.uid + "moving to coords x:" + this.location.getX() + ", Y: " + this.location.getY());
				this.send(MobPackets.getMovePacket(this.uid,0, this.location.getX(), this.location.getY()));
				
			}
		}
	
	// set mobs location ON PLAYER LOC
	private void setPlayerLocation(Waypoint wp){
		if(wp != null) {
			this.setX(wp.getX());
			this.setY(wp.getY());
			this.updateArea();	
			//	//System.out.println("Mob" + this.uid + "moving to coords x:" + this.location.getX() + ", Y: " + this.location.getY());
			this.send(MobPackets.getMovePacket(this.uid,1, this.location.getX(), this.location.getY()));
		}
	}
	// return reference to this mob's controller
	public MobController getControl() {
		return control;
	}
	public byte[] getInitPacket(){
		return MobPackets.getInitialPacket(this.mobID, this.uid, this.getLocation(), this.hp);
	}
	// update near by objects, called by area
		// receive updated list for nearby objects
		public synchronized void updateEnvironment(Integer player, boolean add, boolean leavegameworld) {
			//System.out.println("Character: " + this.charID + " has got player list of size: " + players.size());
			if (this.iniPackets.containsKey(player) && !add){
				this.iniPackets.remove(player);
			}

			if (add && !this.iniPackets.containsKey(player)){
				this.iniPackets.put(player, player);
				this.sendInit(player);
				if(!this.grid.isActive()){
				this.grid.getThreadPool().executeProcess(this.grid);
				}
			}
		}
		
		// send initial packets to players who don't already have ours
		private void sendInit(Integer tmp) {
			if (this.wmap.CharacterExists(tmp)){
				Character t = this.wmap.getCharacter(tmp);
				if(this.isAlive() && ServerFacade.getInstance().getConnectionByChannel(t.GetChannel()) != null){
				ServerFacade.getInstance().addWriteByChannel(t.GetChannel(), this.getInitPacket());
				}
			}
	}
		
	public Waypoint getAndSetNextWaypoint() {
		this.location = this.pop(); 
					return this.location;
				}
				
				public void addNewWaypoints(List<Waypoint> wps) {
					this.populate(wps);
				}
			 	
				public boolean hasNextWaypoint() {
					return !this.isEmpty();
	 }

	public boolean isAlive() {
		return alive;
	}
	private void setAlive(boolean alive) {
		this.alive = alive;
	}
	private void setX(float x) {
		this.location.setX(x);
	}
	
	//force argro by player attacking mob
	/*public void setagrrobyattack(int charid) {
		//1 sec cd on aggro chance
		if(this.isAlive() && System.currentTimeMillis() - this.aggrotimeout > 1000){
		this.setAggro(true);
		this.setAggroID(charid);
		this.aggrotimeout = System.currentTimeMillis();
		}
	}*/
	
	//force argro by player attacking mob
	public void noagrro() {
		this.setAggro(false);
		this.setAggroID(0);
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
	private void setDamage(int uid, int dmg) {
		this.damage.put(uid, dmg);
	}
	public void resetDamage(){
		if(!this.damage.isEmpty()){
			Iterator <Map.Entry<Integer, Integer>> it = this.damage.entrySet().iterator();
			int key;
			int value = 0;
			int hiDmg = 0;
			int hiID = 0;
			while (it.hasNext()) {
				Map.Entry<Integer, Integer> pairs = it.next();
				key = pairs.getKey();
				value = pairs.getValue();
				Character tmp = wmap.getCharacter(key);
				// if hiID isnt in inipacket and not withing 500 range etc etc etc then remove
				if(tmp != null && this.iniPackets.containsKey(tmp.charID) && tmp.getHp() > 0 && tmp.getCurrentMap() == this.control.getMap() && ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null && WMap.distance(tmp.getlastknownX(), tmp.getlastknownY(), this.getSpawnx(), this.getSpawny()) <= 500){	
				if (value > hiDmg){
					hiDmg = value;
					hiID = key;
				}
				}else{
					it.remove();
				}
			}
			if(!this.damage.isEmpty()){
			//System.out.println("new aggro chid, highestdmg : "+hiID+" - "+hiDmg);
			this.setAggro(true);
			this.setAggroID(hiID); 
			}else{
			//System.out.println("new aggro chid, highestdmg : CLEAR 1");
			this.setAggro(false);
			this.setAggroID(0);	
			}
		}else{
		this.setAggro(false);
		this.setAggroID(0);		
		}
	}
	private boolean hasPlayerDamage(int uid){
		return this.damage.containsKey(uid);
	}
	private boolean isAggro() {
		return aggro;
	}
	private void setAggro(boolean aggro) {
		this.aggro = aggro;
	}
	private int getAggroID() {
		return aggroID;
	}
	private void setAggroID(int aggroID) {
		this.aggroID = aggroID;
	}
	@SuppressWarnings("unused")
	private long getDied() {
		return died;
	}
	private void setDied(long died) {
		this.died = died;
	}

	private int getPlayerDamage(int uid) {
		return this.damage.get(uid);
	}
	private void reduceHp(int dmg) {
		this.hp -= dmg;
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

	public MobData getData() {
		return data;
	}

	public void setData(MobData data) {
		this.data = data;
	}

	public int getStarstate() {
		return starstate;
	}

	public void setStarstate(int starstate) {
		this.starstate = starstate;
	}

	public MobController getMobdata() {
		return Mobdata;
	}

	public void setMobdata(MobController mobdata) {
		Mobdata = mobdata;
	}
	
	public int getcurrentmap() {
		return getMobdata().getmap();
	}
	
	public int getUid() {
		return uid;
	}
        
}
