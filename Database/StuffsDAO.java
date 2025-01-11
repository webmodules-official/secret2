package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import oAreaTriggers.AreaTriggerController;

import logging.ServerLogger;

import Configuration.ConfigurationManager;
import Mob.MobController;
import Player.Charstuff;
import Player.UseitemList;
import Player.ipbanlist;
import World.WMap;

/*
 * ipbanlist.class
 * Access the database and parses the data to the needed form for skills
 */

public class StuffsDAO {
	private static ServerLogger log = ServerLogger.getInstance();
	
	public static boolean setPOnline(int one , int two , int three , int four , int five ) {
		try {
			return SQLconnection.getInstance().execute(Queries.setPOnline(SQLconnection.getInstance().getaConnection(), one, two, three, four, five));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean setserverstatus(int one) {
		try {
			return SQLconnection.getInstance().execute(Queries.setserverstatus(SQLconnection.getInstance().getaConnection(), one));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/*public static void initMapControllers(){
		int []data = new int[]{0};
		try{
			ResultSet rs = Queries.getMapControllers(SQLconnection.getInstance().getConnection()).executeQuery();
			while(rs.next()){
				data[0] = rs.getInt("map");
				oItemsController run = new oItemsController(data[0]);
				WMap.getInstance().getGrid(data[0]).getThreadPool().executeProcess(run);
			}
		} catch (SQLException e){
			log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
		} catch (Exception e){
			log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
		}
	}*/
	public static Charstuff censoredwords(){
		try{
			//System.out.println("Loading ipbanlist...");
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getcensoredwords(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
					Charstuff.getInstance().CensoredWords.put(rs.getString("censoredwords"), "*");
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, IPbanlistDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	public static Charstuff itemtoname(){
		try{
			//System.out.println("Loading ipbanlist...");
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getitemtoname(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
					Charstuff.getInstance().setitemtoname(rs.getInt("itemid"), rs.getString("itemname"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, IPbanlistDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	public static void initAreaTriggers(){
		int []data = new int[]{0,0,0};
		int []To = new int[]{0,0,0};
		try{
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getareatriggers(SQLconnection.getInstance().getaConnection()));
			while(rs.next()){
				int uid = rs.getInt("uid");
				data[0] = rs.getInt("Fmap");
				data[1] = rs.getInt("Fx");
				data[2] = rs.getInt("Fy");					
				To[0] = rs.getInt("Tmap");
				To[1] = rs.getInt("Tx");
				To[2] = rs.getInt("Ty");
				int factionID = rs.getInt("factionID");
				AreaTriggerController run = new AreaTriggerController(uid, data, To, factionID);
				//WMap.getInstance().getGrid(data[0]).getThreadPool().executeProcess(run);
			}
		} catch (SQLException e){
			log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
		} catch (Exception e){
			log.logMessage(Level.SEVERE, MobDAO.class, e.getMessage());
		}
	}
	
}
