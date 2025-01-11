package Database;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import logging.ServerLogger;

import Player.Player;
import ServerCore.ServerFacade;


public class AccountDAO {
	private static ServerLogger log = ServerLogger.getInstance();
	/*
	 * Use this for authentication
	 * Return: Player instance if auth successful, null if failed
	 */
	/*public static Player authenticate(String username, String pass) {

		try {
			ResultSet rs = Queries.auth(SQLconnection.getInstance().getConnection(), username, pass).executeQuery();
			if(rs.next()) {
				return new Player(rs.getInt(1));
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe(AccountDAO.class, "Database error: " + e.getMessage());
			////e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe(AccountDAO.class, e.getMessage());
			////e.printStackTrace();
			return null;
		}
	}*/
	
	public static Player authipandloggedinVIP(String username, String digitpass, String VIP) {
		try {
			
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.authipVIP(SQLconnection.getInstance().getaConnection(), username, digitpass, VIP));
			if(rs.next()) {
				if(ServerFacade.getInstance().getCon().isaccountsRegistered(rs.getInt(1)))
				{	
					ServerFacade.getInstance().finalizeConnection(ServerFacade.getInstance().getCon().getaccount(rs.getInt(1)).getSc());
					Player ifmeowisgm = new Player(rs.getInt(1));;   // check if meowisgm 
					ServerFacade.getInstance().getCon().addaccount(rs.getInt(1), ifmeowisgm); 
					ifmeowisgm.setGM(rs.getString("gm"));
					ifmeowisgm.setUsername(rs.getString("username"));
					ifmeowisgm.setBanned(rs.getInt("banned"));
					ifmeowisgm.setip(rs.getString("lastip"));
					ifmeowisgm.setmhpoints(rs.getInt("mhpoints"));
					ifmeowisgm.setNick(rs.getString("nickname"));
					ifmeowisgm.setEmail(rs.getString("email"));
					//System.out.println("ALREADY ON");
					return ifmeowisgm;
				}else{
				Player ifmeowisgm = new Player(rs.getInt(1));;   // check if meowisgm 
				ServerFacade.getInstance().getCon().addaccount(rs.getInt(1), ifmeowisgm); 
				ifmeowisgm.setGM(rs.getString("gm"));
				ifmeowisgm.setUsername(rs.getString("username"));
				ifmeowisgm.setBanned(rs.getInt("banned"));
				ifmeowisgm.setip(rs.getString("lastip"));
				ifmeowisgm.setmhpoints(rs.getInt("mhpoints"));
				ifmeowisgm.setNick(rs.getString("nickname"));
				ifmeowisgm.setEmail(rs.getString("email"));
				//System.out.println("NOT ON");
				return ifmeowisgm;
				}
				//return new Player(rs.getInt(1)); // can also return username or password instead of accountID
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe(AccountDAO.class, "Database error: " + e.getMessage());
			////e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe(AccountDAO.class, e.getMessage());
			////e.printStackTrace();
			return null;
		}
	}
	
	// Authenticate with IP + Loggedin='yes' and checks if its banned or not \\
	public static Player authipandloggedin(String username, String digitpass) {
		try {
			
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.authip(SQLconnection.getInstance().getaConnection(), username, digitpass));
			if(rs.next()) {
				if(ServerFacade.getInstance().getCon().isaccountsRegistered(rs.getInt(1)))
				{	
					ServerFacade.getInstance().finalizeConnection(ServerFacade.getInstance().getCon().getaccount(rs.getInt(1)).getSc());
					Player ifmeowisgm = new Player(rs.getInt(1));;   // check if meowisgm 
					ServerFacade.getInstance().getCon().addaccount(rs.getInt(1), ifmeowisgm); 
					ifmeowisgm.setGM(rs.getString("gm"));
					ifmeowisgm.setUsername(rs.getString("username"));
					ifmeowisgm.setBanned(rs.getInt("banned"));
					ifmeowisgm.setip(rs.getString("lastip"));
					ifmeowisgm.setmhpoints(rs.getInt("mhpoints"));
					ifmeowisgm.setNick(rs.getString("nickname"));
					ifmeowisgm.setEmail(rs.getString("email"));
					//System.out.println("ALREADY ON");
					return ifmeowisgm;
				}else{
				Player ifmeowisgm = new Player(rs.getInt(1));;   // check if meowisgm 
				ServerFacade.getInstance().getCon().addaccount(rs.getInt(1), ifmeowisgm); 
				ifmeowisgm.setGM(rs.getString("gm"));
				ifmeowisgm.setUsername(rs.getString("username"));
				ifmeowisgm.setBanned(rs.getInt("banned"));
				ifmeowisgm.setip(rs.getString("lastip"));
				ifmeowisgm.setmhpoints(rs.getInt("mhpoints"));
				ifmeowisgm.setNick(rs.getString("nickname"));
				ifmeowisgm.setEmail(rs.getString("email"));
				//System.out.println("NOT ON");
				return ifmeowisgm;
				}
				//return new Player(rs.getInt(1)); // can also return username or password instead of accountID
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe(AccountDAO.class, "Database error: " + e.getMessage());
			////e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe(AccountDAO.class, e.getMessage());
			////e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	public static boolean setlog(int accountid, String loggedip, String TIME_DATE) {
		try {
			return SQLconnection.getInstance().execute(Queries.setlog(SQLconnection.getInstance().getaConnection(), accountid, loggedip ,TIME_DATE));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	

	
	
}


	
	
