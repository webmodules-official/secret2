package Database;

import java.sql.ResultSet;
import java.util.logging.Level;

import logging.ServerLogger;
import Player.Character;
import Player.Charstuff;
import Player.buffdata;
import Player.exptable;
import Player.lookuplevel;
import Player.manuals;
import Player.skillpasives;
import Player.skillpointscost;
import Player.skilldata;
import Player.skilleffects;

/*
 * SKILLDAO.class
 * Access the database and parses the data to the needed form for skills
 */

public class SkillDAO {
	private static ServerLogger log = ServerLogger.getInstance();
	
	public static buffdata getbuffdata(){
		buffdata data = null;
		try{
			//System.out.println("Loading buffdata...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getbuffdata(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				data = new buffdata();
				buffdata.setbuffid(rs.getInt("skillid"), rs.getInt("buffid"));
				buffdata.setbufftime(rs.getInt("skillid"), rs.getInt("time"));
				buffdata.setbuffvalue(rs.getInt("skillid"), rs.getInt("value"));
				buffdata.setbuffslot(rs.getInt("skillid"), rs.getInt("buffslot"));
				buffdata.setbufflist(rs.getInt("skillid"), rs.getInt("skillid"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	
	
	public static skillpasives getskillpasives(){
		try{
			//System.out.println("Loading getmanuals...");
			String one;
			ResultSet rs1 = SQLconnection.getInstance().executeQuery(Queries.getskillpasives(SQLconnection.getInstance().getaConnection()));
			
				while( rs1.next()){
					one = "";
					one += Integer.toString(rs1.getInt("passivetypeid"));	
					one +=",";
					one += Integer.toString(rs1.getInt("passivevalue1"));
					one +=",";
					one += Integer.toString(rs1.getInt("passivevalue2"));
					skillpasives.setskillpassives(rs1.getInt("skillid") ,one);
					//System.out.println(rs1.getInt("skillid")+","+one);
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	
	public static skillpointscost getskillpointscost(){
		skillpointscost data = null;
		try{
			//System.out.println("Loading skillpointscost...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getskillpointscost(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				data = new skillpointscost();
				skillpointscost.setskillpointscost(rs.getInt("skillid"), rs.getInt("skillpointscost"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	
	public static lookuplevel lookuplevelstp(){
		lookuplevel data = null;
		try{
			//System.out.println("Loading lookuplevelstp...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.lookuplevelstp(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				data = new lookuplevel();
				lookuplevel.setstatP(rs.getInt("level"), rs.getInt("levelstp"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	
	public static lookuplevel lookupitems(){
		try{
			//System.out.println("Loading lookupitems...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.lookupitem(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				lookuplevel.setlookupitems(rs.getInt("itemid"), rs.getString("name"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	
	public static lookuplevel lookuplevelskp(){
		lookuplevel data = null;
		try{
			//System.out.println("Loading lookuplevelskp...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.lookuplevelskp(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				data = new lookuplevel();
				lookuplevel.setskillP(rs.getInt("level"), rs.getInt("levelskp"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	// skilldata
	public static skilldata skilldata(){
		skilldata data = null;
		try{
			//System.out.println("Loading skilldata...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getskilldata(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				data = new skilldata();
				skilldata.setskilldmg(rs.getInt("skillid"), rs.getInt("damage"));
				skilldata.setskillcritchance(rs.getInt("skillid"), rs.getInt("critchance"));
				skilldata.setskillmanaconsume(rs.getInt("skillid"), rs.getInt("manacost"));
				skilldata.setskillcooldowns(rs.getInt("skillid"), rs.getInt("cooldown"));
				skilldata.setskilllevel(rs.getInt("skillid"), rs.getInt("level"));
				skilldata.setskillcategory(rs.getInt("skillid"), rs.getInt("skillcategory"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	
	// skillbedoong
	public static buffdata skillbedoong(){
		buffdata data = null;
		try{
			//System.out.println("Loading skillbedoong...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getskillbedoong(SQLconnection.getInstance().getaConnection()));
				while(rs.next()){
				data = new buffdata();
				buffdata.setBedoonglist(rs.getInt("bedoong_skillid"), rs.getInt("bedoong_skillid"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	
	// LOLZ EXPTABLE
	public static exptable maxexptable(){
		exptable data = null;
		try{
			//System.out.println("Loading Maxexptable...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.Maxexptable(SQLconnection.getInstance().getaConnection()));
			long finalexp;
				while( rs.next()){
				data = new exptable();
				
				// if level is AND between 8 and 59 then get 19% of it and add it to total value
				double twentypercent = 0;
				if(rs.getInt("level") >= 8 && rs.getInt("level") <= 59){
				 twentypercent = rs.getLong("maxexp") * 0.250; // get 20%	
				}
				finalexp = rs.getLong("maxexp") + (int)twentypercent;
				exptable.setMAXexptable(rs.getInt("level"), finalexp);
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	
	// LOLZ MY BALLZ R BLUE 
	public static exptable maxfurytable(){
		exptable data = null;
		try{
			//System.out.println("Loading Maxexptable...");
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.Maxfurytable(SQLconnection.getInstance().getaConnection()));
				while( rs.next()){
				data = new exptable();
				exptable.setfuryV(rs.getInt("level"), rs.getInt("fury"));
				exptable.setfuryT(rs.getInt("level"), rs.getInt("time"));
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, SkillDAO.class, e.getMessage());
		}
		//System.out.println("......done!");
		return data;
	}
	
	
	public static skilleffects getskilleffects(){
		try{
			//System.out.println("Loading getmanuals...");
			String one;
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getskilleffects(SQLconnection.getInstance().getaConnection()));
			
				while( rs.next()){
					one = "";
					one += Integer.toString(rs.getInt("effectid"));	
					one +=",";
					one += Integer.toString(rs.getInt("value"));
					one +=",";
					one += Integer.toString(rs.getInt("time"));
					one +=",";
					one += Integer.toString(rs.getInt("slot"));
					one +=",";
					one += Integer.toString(rs.getInt("effectid2"));
					one +=",";
					one += Integer.toString(rs.getInt("value2"));
					one +=",";
					one += Integer.toString(rs.getInt("time2"));
					one +=",";
					one += Integer.toString(rs.getInt("slot2"));
					one +=",";
					one += Integer.toString(rs.getInt("effectid3"));
					one +=",";
					one += Integer.toString(rs.getInt("value3"));
					one +=",";
					one += Integer.toString(rs.getInt("time3"));
					one +=",";
					one += Integer.toString(rs.getInt("slot3"));
					one +=",";
					one += Integer.toString(rs.getInt("rate"));
					one +=",";
					one += Integer.toString(rs.getInt("limit"));
					skilleffects.setskilleffects(rs.getInt("skillid") ,one);
					//System.out.println(rs.getInt("skillid")+","+one);
				}
			}
		catch (Exception e) {
			log.logMessage(Level.SEVERE, Character.class, e.getMessage());
		}
		//System.out.println("......done!");
		return null;
	}
	

}
