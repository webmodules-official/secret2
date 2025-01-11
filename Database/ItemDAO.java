package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import Configuration.ConfigurationManager;
import Mob.MobController;
import Mob.MobData;
import Player.Character;
import Player.Charstuff;
import Player.Proffession;
import Player.grinditems;
import Player.itemprice;
import Player.manuals;
import Player.upgradelist;
import World.WMap;

import npc.NpcController;
import npc.NpcData;
import item.Item;
import item.ItemCache;
import logging.ServerLogger;

public class ItemDAO {
	private static ServerLogger log = ServerLogger.getInstance();
		
		public static void iniNpcsgroups(){
			int groupid, count, pool;
			int []data = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			pool = ConfigurationManager.getConf("world").getIntVar("mobUIDPool");
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getNpcgroups(SQLconnection.getInstance().getaConnection()));
				while(rs.next()){
					groupid = rs.getInt("npcgroupID");
					count = rs.getInt("npcspawncount");
					data[0] = rs.getInt("map");
					data[1] = rs.getInt("x");
					data[2] = rs.getInt("y");
					data[3] = rs.getInt("id1");
					data[4] = rs.getInt("id2");
					data[5] = rs.getInt("id3");
					data[6] = rs.getInt("id4");
					data[7] = rs.getInt("id5");
					data[8] = rs.getInt("id6");
					data[9] = rs.getInt("id7");
					data[10] = rs.getInt("id8");
					data[11] = rs.getInt("id9");
					data[12] = rs.getInt("id10");
					data[13] = rs.getInt("id11");
					data[14] = rs.getInt("id12");
					data[15] = rs.getInt("id13");
					data[16] = rs.getInt("id14");
					data[17] = rs.getInt("id15");
					data[18] = rs.getInt("id16");
					data[19] = rs.getInt("id17");
					data[20] = rs.getInt("id18");
					data[21] = rs.getInt("id19");
					data[22] = rs.getInt("id20");
					data[23] = rs.getInt("id21");
					data[24] = rs.getInt("id22");
					data[25] = rs.getInt("id23");
					data[26] = rs.getInt("id24");
					data[27] = rs.getInt("id25");
					data[28] = rs.getInt("id26");
					data[29] = rs.getInt("id27");
					data[30] = rs.getInt("id28");
					data[31] = rs.getInt("id29");
					data[32] = rs.getInt("id30");
					data[33] = rs.getInt("id31");
					data[34] = rs.getInt("id32");
					data[35] = rs.getInt("id33");
					data[36] = rs.getInt("id34");
					data[37] = rs.getInt("id35");
					//System.out.println("Creating npcgroup controller with x: " + data[1] + " y: " + data[2]);
					NpcController run = new NpcController(groupid, count, pool, data);
					//WMap.getInstance().getGrid(data[0]).getThreadPool().executeProcess(run);
					pool += count;
				}
			} catch (SQLException e){
				log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
			} catch (Exception e){
				log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
			}
		}	
		
		public static ItemCache getitem(){
			try{
				//System.out.println("Loading Items...");
				String Item;
				ResultSet rs1 = SQLconnection.getInstance().executeQuery(Queries.getITEM_DATA(SQLconnection.getInstance().getaConnection()));
				
					while( rs1.next()){
						Item = "";
						Item += Integer.toString(rs1.getInt("SET_EFFECT_ID"));	
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_TYPE_DMG"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_STR"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_DEX"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_VIT"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_INT"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_AGI"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_OFF_POWER"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_DEF_POWER"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_LIFE"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("BONUS_MANA"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("STR"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("DEX"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("VIT"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("INT"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("AGI"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("OFF_POWER"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("DEF_POWER"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("LIFE"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("MANA"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("PRICE"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("MAX_DMG"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("TIME_TO_EXPIRE"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("REQ_STR"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("REQ_DEX"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("REQ_VIT"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("REQ_INT"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("REQ_AGI"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("IS_CONSUMABLE"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("MIN_LVL"));
						Item +=",";
						Item += Integer.toString(rs1.getInt("EQUIP_SLOT"));
						ItemCache.Items.put(rs1.getInt("ID") ,Item);
						//System.out.println(rs1.getInt("ID")+","+Item);
					}
				}
			catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static manuals getmanuals(){
			try{
				//System.out.println("Loading getmanuals...");
				String Manuals;
				ResultSet rs1 = SQLconnection.getInstance().executeQuery(Queries.getManuals(SQLconnection.getInstance().getaConnection()));
				
					while( rs1.next()){
						Manuals = "";
						Manuals += Integer.toString(rs1.getInt("product"));	
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material1"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack1"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material2"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack2"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material3"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack3"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material4"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack4"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material5"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack5"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material6"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack6"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material7"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack7"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("material8"));
						Manuals +=",";
						Manuals += Integer.toString(rs1.getInt("stack8"));
						
						manuals.manuals.put(rs1.getInt("id") ,Manuals);
						//System.out.println(rs1.getInt("id")+","+Manuals);
					}
				}
			catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static Proffession Proffessios(){
			try{
					// fishing
					ResultSet rs0 = SQLconnection.getInstance().executeQuery(Queries.getfishing(SQLconnection.getInstance().getaConnection()));
					while(rs0.next()){
						Proffession.setfarmfishing(rs0.getInt("slot") ,rs0.getInt("itemid"));
					}
					
					// herbing
					ResultSet rs1 = SQLconnection.getInstance().executeQuery(Queries.getherbing(SQLconnection.getInstance().getaConnection()));
					while(rs1.next()){
						Proffession.setfarmherbing(rs1.getInt("slot") ,rs1.getInt("itemid"));
					}
					
					// mining
					ResultSet rs2 = SQLconnection.getInstance().executeQuery(Queries.getmining(SQLconnection.getInstance().getaConnection()));
						while(rs2.next()){
							Proffession.setfarmmining(rs2.getInt("slot") ,rs2.getInt("itemid"));
						}
						
						// graveyard
					ResultSet rs3 = SQLconnection.getInstance().executeQuery(Queries.getgraveyard(SQLconnection.getInstance().getaConnection()));
						while(rs3.next()){
							Proffession.setfarmgraveyard(rs3.getInt("slot") ,rs3.getInt("itemid"));
						}
					
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		

		public static upgradelist Upgradelist(){
			try{
				//System.out.println("Loading upgradelist...");
				ResultSet rs0 = SQLconnection.getInstance().executeQuery(Queries.getITEM_price(SQLconnection.getInstance().getaConnection()));
				ResultSet rs1 = SQLconnection.getInstance().executeQuery(Queries.getITEM_upgrades(SQLconnection.getInstance().getaConnection()));
					while(rs0.next() && rs1.next()){
						upgradelist.setIDtoNewID(rs0.getInt("itemid") ,rs1.getInt("itemid"));
						itemprice.setBuyprice(rs0.getInt("itemid") ,rs0.getInt("price") );
						//System.out.println(rs0.getInt("itemid") +" - "+rs0.getInt("price") );
					}
					
					ResultSet rs3 = SQLconnection.getInstance().executeQuery(Queries.getupgradelist(SQLconnection.getInstance().getaConnection()));
						while(rs3.next()){
							upgradelist.setUpgradelist(rs3.getInt("itemid1") ,rs3.getInt("itemid2"));
							//System.out.println(rs3.getInt("itemid1") +" - "+rs3.getInt("itemid2") );
						}
					
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static grinditems GrindItems(){
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getgrinditems(SQLconnection.getInstance().getaConnection()));
					while(rs.next()){
						grinditems.setgrinditems(rs.getInt("itemid") ,rs.getInt("durance") );
					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static Charstuff Invincible_items(){
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getInvincible_items(SQLconnection.getInstance().getaConnection()));
					while(rs.next()){
						Charstuff.getInstance().setInvincible_items(rs.getInt("itemid") ,rs.getInt("itemid") );
					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static Charstuff guildbuffs(){
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.guildbuffs(SQLconnection.getInstance().getaConnection()));
					while(rs.next()){
						Charstuff.getInstance().guildbuffs.put(rs.getInt("guilduid") ,rs.getInt("buffid") );
					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		
		public static Charstuff Non_Tradable_items(){
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getNon_Tradable_items(SQLconnection.getInstance().getaConnection()));
					while(rs.next()){
						Charstuff.getInstance().setNon_Tradable_items(rs.getInt("itemid") ,rs.getInt("itemid") );
					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static Charstuff Equipignore(){
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getequipignore(SQLconnection.getInstance().getaConnection()));
					while(rs.next()){
						Charstuff.getInstance().Equipignore.put(rs.getInt("itemid") ,rs.getInt("itemid") );
					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static Charstuff itemtoprocedure(){
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getitemtoprocedure(SQLconnection.getInstance().getaConnection()));
					while(rs.next()){
						Charstuff.getInstance().itemtoprocedure1.put(rs.getInt("itemid") ,rs.getInt("procedureid1"));
						Charstuff.getInstance().itemtoprocedure2.put(rs.getInt("itemid") ,rs.getInt("procedureid2"));
						Charstuff.getInstance().itemtoprocedure3.put(rs.getInt("itemid") ,rs.getInt("procedureid3"));
						Charstuff.getInstance().itemtoprocedure4.put(rs.getInt("itemid") ,rs.getInt("procedureid4"));
						Charstuff.getInstance().itemtoprocedure5.put(rs.getInt("itemid") ,rs.getInt("procedureid5"));
						Charstuff.getInstance().itemtoprocedure6.put(rs.getInt("itemid") ,rs.getInt("procedureid6"));
						Charstuff.getInstance().itemtoprocedure7.put(rs.getInt("itemid") ,rs.getInt("procedureid7"));
						Charstuff.getInstance().itemtoprocedure8.put(rs.getInt("itemid") ,rs.getInt("procedureid8"));
						Charstuff.getInstance().itemtoprocedure9.put(rs.getInt("itemid") ,rs.getInt("procedureid9"));
						Charstuff.getInstance().itemtoprocedure10.put(rs.getInt("itemid") ,rs.getInt("procedureid10"));
						Charstuff.getInstance().itemtoprocedure11.put(rs.getInt("itemid") ,rs.getInt("procedureid11"));
						Charstuff.getInstance().itemtoprocedure12.put(rs.getInt("itemid") ,rs.getInt("procedureid12"));
						Charstuff.getInstance().itemtoprocedure13.put(rs.getInt("itemid") ,rs.getInt("procedureid13"));
						Charstuff.getInstance().itemtoprocedure14.put(rs.getInt("itemid") ,rs.getInt("procedureid14"));
						Charstuff.getInstance().itemtoprocedure15.put(rs.getInt("itemid") ,rs.getInt("procedureid15"));
						Charstuff.getInstance().itemtoprocedure16.put(rs.getInt("itemid") ,rs.getInt("procedureid16"));
						Charstuff.getInstance().itemtoprocedure17.put(rs.getInt("itemid") ,rs.getInt("procedureid17"));
						Charstuff.getInstance().itemtoprocedure18.put(rs.getInt("itemid") ,rs.getInt("procedureid18"));
						Charstuff.getInstance().itemtoprocedure19.put(rs.getInt("itemid") ,rs.getInt("procedureid19"));
						Charstuff.getInstance().itemtoprocedure20.put(rs.getInt("itemid") ,rs.getInt("procedureid20"));
						Charstuff.getInstance().itemtoprocedure21.put(rs.getInt("itemid") ,rs.getInt("procedureid21"));
						Charstuff.getInstance().itemtoprocedure22.put(rs.getInt("itemid") ,rs.getInt("procedureid22"));
						Charstuff.getInstance().itemtoprocedure23.put(rs.getInt("itemid") ,rs.getInt("procedureid23"));
						Charstuff.getInstance().itemtoprocedure24.put(rs.getInt("itemid") ,rs.getInt("procedureid24"));

					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static Charstuff Stackable_items(){
			try{
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getStackable_items(SQLconnection.getInstance().getaConnection()));
					Charstuff.getInstance().setStackable_items(0, 0);
					while(rs.next()){
						Charstuff.getInstance().setStackable_items(rs.getInt("ITEMID") ,rs.getInt("STACKABLE") );
					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}
		
		public static Charstuff Mobdrops_skyzone(){
			try{
				int inc = 0;
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getMobdrops_skyzone(SQLconnection.getInstance().getaConnection()));
					while(rs.next()){
						inc++;
						Charstuff.getInstance().setmobdrops_skyzone(inc, rs.getInt("itemid"));
					}		
		}catch (Exception e) {
				log.logMessage(Level.SEVERE, Character.class, e.getMessage());
			}
			//System.out.println("......done!");
			return null;
		}

	public static Item getItem(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
