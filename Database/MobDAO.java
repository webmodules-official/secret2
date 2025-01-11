package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import logging.ServerLogger;

import Configuration.ConfigurationManager;
import Mob.MobController;
import Mob.MobData;
import Player.Character;
import World.WMap;

/*
 * MobDAO.class
 * Access the database and parses the data to the needed form for mobs
 */

public class MobDAO {
	private static ServerLogger log = ServerLogger.getInstance();
	
	public static boolean deletemobrespawncd(int uid) {
		try {
			return SQLconnection.getInstance().execute(Queries.deletemobrespawncd(SQLconnection.getInstance().getaConnection(), uid));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean savemobrespawncd(int uid, long died, long newcd) {
		try {
			return SQLconnection.getInstance().execute(Queries.savemobrespawncd(SQLconnection.getInstance().getaConnection(), uid, died, newcd));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static MobData getMobData(int mobID){
		MobData data = null;
		try{ 
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getMobData(SQLconnection.getInstance().getaConnection(), mobID));
			data = new MobData();
			if (rs.next()){
				data.setLvl(rs.getInt("lvl"));
				data.setAttack(rs.getInt("attack"));
				data.setDefence(rs.getInt("defence"));
				data.setMaxhp(rs.getInt("maxhp"));
				data.setBasexp(rs.getLong("basexp"));
				data.setBasefame(rs.getInt("basefame"));
				//data.setAggroRange(rs.getInt("aggroRange"));
				data.setAttackRange(rs.getInt("attackRange"));
				data.setFollowRange(rs.getInt("followRange"));
				data.setMoveRange(rs.getInt("moveRange"));
				data.setgolddrop(rs.getInt("golddrop"));
				data.attackcooldown = rs.getInt("attackcooldown");
				data.skill1 = rs.getInt("skill1");
				data.skill2 = rs.getInt("skill2");
				data.skill3 = rs.getInt("skill3");
				data.skill3 = rs.getInt("skill3");
				data.setRespawnTime(rs.getLong("respawntime"));
				data.setfamerate100(rs.getInt("famerate100"));
				data.setboss(rs.getInt("boss"));
			}
		ResultSet rsa = SQLconnection.getInstance().executeQuery(Queries.getMobItemdrops(SQLconnection.getInstance().getaConnection(), mobID));
		 while(rsa.next()){
		 data.setdropitemid(rsa.getInt("itemid"));
		 }
		 	
			ResultSet rsa1 = SQLconnection.getInstance().executeQuery(Queries.getRAREItemdrops(SQLconnection.getInstance().getaConnection()));
			 while(rsa1.next()){
				 data.setRAREdropitemid(rsa1.getInt("slot"), rsa1.getInt("rareitemid"));
			 }
		}catch (SQLException e) {
			log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
		}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
		}
		return data;
	}
	
	
	public static void initMobs(){
		int mobid, count, pool;
		int []data = new int[]{0,0,0,0,0,0,0};
		pool = ConfigurationManager.getConf("world").getIntVar("mobUIDPool");
		try{
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getMobs(SQLconnection.getInstance().getaConnection()));
			while(rs.next()){
				mobid = rs.getInt("mobType");
				count = rs.getInt("spawnCount");
				data[0] = rs.getInt("map");
				data[1] = rs.getInt("spawnX");
				data[2] = rs.getInt("spawnY");
				data[3] = rs.getInt("spawnRadius");
				data[4] = rs.getInt("waypointCount");
				data[5] = rs.getInt("waypointHop");
				data[6] = rs.getInt("respawnTime");
				//System.out.println("Creating controller with x: " + data[1] + " y: " + data[2]);
				new MobController(mobid, count, pool, data);
				pool += count;
			}
			WMap.getInstance().getGrid(0).getThreadPool().executeProcess(WMap.getInstance().getGrid(0));
			WMap.getInstance().getGrid(1).getThreadPool().executeProcess(WMap.getInstance().getGrid(1));
			WMap.getInstance().getGrid(2).getThreadPool().executeProcess(WMap.getInstance().getGrid(2));
			WMap.getInstance().getGrid(3).getThreadPool().executeProcess(WMap.getInstance().getGrid(3));
			WMap.getInstance().getGrid(4).getThreadPool().executeProcess(WMap.getInstance().getGrid(4));
			WMap.getInstance().getGrid(5).getThreadPool().executeProcess(WMap.getInstance().getGrid(5));
			WMap.getInstance().getGrid(6).getThreadPool().executeProcess(WMap.getInstance().getGrid(6));
			WMap.getInstance().getGrid(7).getThreadPool().executeProcess(WMap.getInstance().getGrid(7));
			WMap.getInstance().getGrid(8).getThreadPool().executeProcess(WMap.getInstance().getGrid(8));
			WMap.getInstance().getGrid(9).getThreadPool().executeProcess(WMap.getInstance().getGrid(9));
			WMap.getInstance().getGrid(10).getThreadPool().executeProcess(WMap.getInstance().getGrid(10));
			WMap.getInstance().getGrid(11).getThreadPool().executeProcess(WMap.getInstance().getGrid(11));
			WMap.getInstance().getGrid(100).getThreadPool().executeProcess(WMap.getInstance().getGrid(100));
			WMap.getInstance().getGrid(201).getThreadPool().executeProcess(WMap.getInstance().getGrid(201));
			WMap.getInstance().getGrid(202).getThreadPool().executeProcess(WMap.getInstance().getGrid(202));
			WMap.getInstance().getGrid(203).getThreadPool().executeProcess(WMap.getInstance().getGrid(203));
			WMap.getInstance().getGrid(204).getThreadPool().executeProcess(WMap.getInstance().getGrid(204));
			WMap.getInstance().getGrid(205).getThreadPool().executeProcess(WMap.getInstance().getGrid(205));
			WMap.getInstance().getGrid(206).getThreadPool().executeProcess(WMap.getInstance().getGrid(206));
			WMap.getInstance().getGrid(207).getThreadPool().executeProcess(WMap.getInstance().getGrid(207));
			WMap.getInstance().getGrid(209).getThreadPool().executeProcess(WMap.getInstance().getGrid(209));
			WMap.getInstance().getGrid(210).getThreadPool().executeProcess(WMap.getInstance().getGrid(210));
			WMap.getInstance().getGrid(300).getThreadPool().executeProcess(WMap.getInstance().getGrid(300));
		} catch (SQLException e){
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
