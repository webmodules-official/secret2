package Database;

import java.sql.ResultSet;
import java.util.logging.Level;

import logging.ServerLogger;
import Player.ipbanlist;


/*
 * ipbanlist.class
 * Access the database and parses the data to the needed form for skills
 */

public class IPbanlistDAO {
	private static ServerLogger log = ServerLogger.getInstance();
	
	public static ipbanlist getipbanlist(){
		ipbanlist.clearipbanlist();
		try{
			//System.out.println("Loading ipbanlist...");
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getipbanlist(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				ipbanlist.setipbanlist(rs.getString("ip"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, IPbanlistDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}

}
