package World;

import item.DroppedItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import oAreaTriggers.AreaTrigger;
import oAreaTriggers.AreaTriggerController;

import npc.Npc;
import npc.NpcController;


import Database.Queries;
import Database.SQLconnection;
import Mob.Mob;
import Mob.MobController;
import Player.Character;
import Player.Party;
import Player.Trade;
import Player.Guild;
import Player.Summon;
import Tools.BitTools;

import logging.ServerLogger;
// WMap.class
//singleton resource that keeps track of all the grids, connected Characters and mobs inside the game


public class WMap
{
	private final ConcurrentMap<Integer, Grid> grids = new ConcurrentHashMap<Integer, Grid>();	
	private final ConcurrentMap<Integer, Character>  Characters = new ConcurrentHashMap<Integer, Character>(100, 4, 100);	
	private final ConcurrentMap<Integer, Mob> mobs0 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs1 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs2 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs3 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs4 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs5 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs6 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs7 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs8 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs9 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs10 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs11 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs100 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs201 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs202 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs203 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs204 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs205 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs206 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs207 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs209 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs210 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Mob> mobs300 = new ConcurrentHashMap<Integer, Mob>(100, 4, 100);
	private final ConcurrentMap<Integer, Npc> npcs = new ConcurrentHashMap<Integer, Npc>();
	private final ConcurrentMap<Integer, Party> partys = new ConcurrentHashMap<Integer, Party>();
	public final ConcurrentMap<Integer, Guild> Guild = new ConcurrentHashMap<Integer, Guild>();
	private final ConcurrentMap<Integer, Summon> summons = new ConcurrentHashMap<Integer, Summon>();
	private final ConcurrentMap<Integer, DroppedItem> items = new ConcurrentHashMap<Integer, DroppedItem>(); 
	private final ConcurrentMap<Integer, AreaTrigger> AreaTriggers = new ConcurrentHashMap<Integer, AreaTrigger>();
	private final ConcurrentMap<Integer, Integer> Guildranks = new ConcurrentHashMap<Integer, Integer>();
	public final ConcurrentMap<Integer, Integer> vendinglist = new ConcurrentHashMap<Integer, Integer>();
	public final ConcurrentMap<Integer, Trade> Trade = new ConcurrentHashMap<Integer, Trade>();;
	private	static WMap instance; 
	
	   private WMap(){
           
       }
       public synchronized static WMap getInstance(){
               if (instance == null){
                       instance = new WMap();
               }
               return instance;
       }
       
   	public ConcurrentMap<Integer, Integer> getvendinglist()
   	{
   		return this.vendinglist;
   	}
      
      	public int getvendinglist(int one) {
       		if(vendinglist.containsKey(one)){
       		int invvalue = vendinglist.get(one);
       		//System.out.println("getvendinglist: " +one+" - " +invvalue);
       		return invvalue;}else
       		{ //System.out.println(one+" - 0 "); 
       		return 0;}
       	}

       	public void setvendinglist(int one, int two) {
       	vendinglist.put(Integer.valueOf(one), Integer.valueOf(two)); 
       	//System.out.println("setvendinglist: " +one+" - " +two);
       	}
       
   	public int getGuildranks(int one) {
   		if(Guildranks.containsKey(one)){
   		int invvalue = Guildranks.get(one);
   		//System.out.println("getGuildranks: " +one+" - " +invvalue);
   		return invvalue;}else
   		{ //System.out.println(one+" - null "); 
   		return 0;}
   	}

   	public void setGuildranks(int one, int two) {
   		Guildranks.put(Integer.valueOf(one), Integer.valueOf(two)); 
   //	System.out.println("setGuildranks: " +one+" - " +two);
   	}
       
       
   	public void getranks(){
			try {
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getguildranks(SQLconnection.getInstance().getaConnection()));
			int lol = 0;
			while(rs.next()){
			lol++;
			this.setGuildranks(rs.getInt("GuildID"), lol);
			}} catch (SQLException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			
		}
		
	
	// add mob that has been created to the list
	public void AddMob(int uid, Mob mb, int map) {
		this.getMobs(map).put(uid, mb);
	}
	
	public void Addparty(int uid, Party pt) {
		this.partys.put(uid, pt);
	}
	
	public void AddGuild(int GuildID, Guild G) {
		this.Guild.put(GuildID, G);
	}
	
	public void Addsummon(int uid, Summon sum) {
		this.summons.put(uid, sum);
	}
	
	/*public void AddItem(int uid, oItem item) {
		this.Items.put(uid, item);
	}
	
	public void AddMapController(int mapid, oItemsController MapCnt) {
		this.MapControllers.put(mapid, MapCnt);
	}*/
	
	public void AddAreaTrigger(int mapid, AreaTrigger MapCnt) {
		this.AreaTriggers.put(mapid, MapCnt);
	}
	
	public void AddNpc(int uid, Npc mb) {
		this.npcs.put(uid, mb);
	}
	// returns Mob identified by uid
	public MobController GetMobController(int uid, int map) {
		return this.getMobs(map).get(uid).getControl();
	}
	// returns Mob identified by uid
	public NpcController GetNpcController(int uid) {
		return this.npcs.get(uid).getControl();
	}
	
	// returns Mob identified by uid
	/*public oItemsController GetItemsController(int uid) {
		return this.Items.get(uid).getControl();
	}*/
	
	public AreaTriggerController GetAreaTriggerController(int uid) {
		return this.AreaTriggers.get(uid).getControl();
	}
		
	
	public Npc GetNpc(int uid) {
		return this.npcs.get(uid);
	}
	
	
	public boolean Npcexist(int uid)
	{
	  return this.npcs.containsKey(uid);
	}

	public Mob getMob(int uid,int map) {
		Mob TMob = this.getMobs(map).get(uid); 
		if(TMob == null){return null;}
		else
			if(TMob.getHp() <= 0){return null;
			}else{
			return this.getMobs(map).get(uid);
			}

	}
	
	public Party getParty(int uid) {
		return this.partys.get(uid);
	}
	
	public Guild getGuild(int GuildID) {
		return this.Guild.get(GuildID);
	}
	
	public Summon getsummons(int uid) {
		return this.summons.get(uid);
	}
	
	/*public oItem getItem(int uid) {
		return this.Items.get(uid);
	}
	
	public oItemsController getMapController(int mapid) {
		return this.MapControllers.get(mapid);
	}*/
	
	public AreaTrigger getAreaTrigger(int mapid) {
		return this.AreaTriggers.get(mapid);
	}
		
	public boolean mobExists(int uid,int map){
		return this.getMobs(map).containsKey(uid);
	}
	
	public boolean npcExists(int uid){
		return this.npcs.containsKey(uid);
	}
	
	public boolean guildIsLoaded(int GuilID){
		return this.Guild.containsKey(GuilID);
	}
	
	public boolean AreaTriggersExist(int uid){
		return this.AreaTriggers.containsKey(uid);
	}
	
	/*public boolean GridItemsExist(int uid){
		return this.Items.containsKey(uid);
	}*/
	// add grid that has been created to the list
	public void addGrid(Grid g){
		//ServerLogger.getInstance().info(this, "Added grid " + g.getuid() + " To wmap");
	this.grids.put(g.getuid(), g);
	}
	// returns true if Grid identified by uid exists in the WMap, false if not
	public boolean gridExist(int uid)
	{
	  return this.grids.containsKey(uid);
	}
	// adds new Character to the list
	public void addCharacter(Character obj){
	 //System.out.println("New Character "+ obj.getuid()+" added to list");
		this.Characters.put(obj.getuid(), obj);
	}
	// returns Character identified by uid
	public Character getCharacter(int uid){
	  return this.Characters.get(uid);
	}
	

	
		

	// return true if Character obj is in the list, otherwise returns false
	public boolean CharacterExists(Character obj)
    {
      return this.Characters.containsValue(obj);
    }
	// returns true if Character with UID id exists in the list, otherwise returns false
    public boolean CharacterExists(int id)
    {
    	return this.Characters.containsKey(id);
    } 
	// removes Character identified by uid from the list
	public void rmCharacter(int uid)
	{
		this.Characters.remove(uid);
	}
	
	public void rmGuild(int uid)
	{
		this.Guild.remove(uid);
	}
	/*public void RemoveGridItem(int uid)
	{
		this.Items.remove(uid);
	}*/
	// returns Map containing all the Characters currently in the game
	public Map<Integer, Character> getCharacterMap()
	{ 
		return this.Characters;
	}	
	
	public Map<Integer, Guild> getGuildMap()
	{ 
		return this.Guild;
	}
	// returns the Grid designated by the uid
	public Grid getGrid(int uid)
	{
		if(!this.grids.containsKey(uid));
		return this.grids.get(uid);
	}
	// return map containing all the grids
	public Map<Integer, Grid> returnMap()
	{
		return this.grids;
	}
	// calculates in-game distance between point a and b
	public static float distance(float a, float b)
	{
	   return (float) Math.sqrt(Math.pow( (double)(a - b), 2));
	}
	public static int distance(int a, int b)
	{
	   return (int)Math.sqrt(Math.pow( (double)(a - b), 2));
	}
	// calculates in-game distance between coordinates (tx,ty) and (dx,dy)
	public static float distance(float tx, float ty, float dx, float dy)
	{
	  return ((float) (Math.sqrt( (Math.pow( (double)(tx - dx), (double)2 ) + Math.pow( (double)(ty - dy), (double)2) )) ));	  
	}

	public boolean itemExist(int uid){
		return this.items.containsKey(uid);
	}
	/*public boolean isUIDFree(int uid){
		if (!this.CharacterExists(uid) && !this.mobExists(uid) && !this.itemExist(uid)){
			return true;
		}
		return false;
	}*/
	// add dopped item to be tracked
	public boolean addItem(DroppedItem it){
		if (!this.itemExist(it.getuid())){
			this.items.put(it.getuid(), it);
			return true;
		}
		return false;
	}
	// get dropped item instance
	public DroppedItem getItem(int uid){
		return this.items.get(uid);
	}
	// free uids for all ???? profit

	// remove droppped item
	public void removeItem(Integer uid) {
		this.items.remove(uid);
		
	}
	public ConcurrentMap<Integer, Mob> getMobs(int map) {
		if(map == 0){return mobs0;}
		else
		if(map == 1){return mobs1;}
		else
		if(map == 2){return mobs2;}
		else
		if(map == 3){return mobs3;}
		else
		if(map == 4){return mobs4;}
		else
		if(map == 5){return mobs5;}
		else
		if(map == 6){return mobs6;}
		else
		if(map == 7){return mobs7;}
		else
		if(map == 8){return mobs8;}
		else
		if(map == 9){return mobs9;}
		else
		if(map == 10){return mobs10;}
		else
		if(map == 11){return mobs11;}
		else
		if(map == 100){return mobs100;}
		else
		if(map == 201){return mobs201;}
		else
		if(map == 202){return mobs202;}
		else
		if(map == 203){return mobs203;}
		else
		if(map == 204){return mobs204;}
		else
		if(map == 205){return mobs205;}
		else
		if(map == 206){return mobs206;}
		else
		if(map == 207){return mobs207;}
		else
		if(map == 209){return mobs209;}
		else
		if(map == 210){return mobs210;}
		else
		if(map == 300){return mobs300;}
																										
		return null;
	}

		
	
}
 