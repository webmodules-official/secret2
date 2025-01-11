package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import logging.ServerLogger;

public class InstallDAO {
	private ServerLogger log = ServerLogger.getInstance();
	private static InstallDAO instance;
	
	/*
	private InstallDAO(){
		this.log = ServerLogger.getInstance();
	}
	public static InstallDAO getInstance(){
		if (instance == null){
			instance = new InstallDAO();
		}
		return instance;
	}
	
	public boolean isEmpty(){
		boolean b = true;
		try{
			Queries.showGrants(SQLconnection.getInstance().getConnection()).executeQuery();
			// b = rs.next();
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean tableExists(String table){
		boolean b = false;
		try{
			ResultSet rs = Queries.showTables(SQLconnection.getInstance().getConnection()).executeQuery();
			while( rs.next()){
				if (table.contentEquals(rs.getString(1))) b = true;
			}
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean dropTable(int n){
		boolean b = false;
		try{
			if (n == 0){
				b = Queries.dropAccountTable(SQLconnection.getInstance().getConnection()).execute();
			}
			else if (n == 1){
				b = Queries.dropCharacterTable(SQLconnection.getInstance().getConnection()).execute();
			}
			else if (n == 2){
				b = Queries.dropZonesTable(SQLconnection.getInstance().getConnection()).execute();
			}
			else if (n == 3){
				b = Queries.dropMapTable(SQLconnection.getInstance().getConnection()).execute();
			}
			else if (n == 4){
				b = Queries.dropMobsTable(SQLconnection.getInstance().getConnection()).execute();
			}
			else if (n == 5){
				b = Queries.dropMobDataTable(SQLconnection.getInstance().getConnection()).execute();
			}
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean createAccountTable() {
		boolean b = true;
		try{
			Queries.createAccountTable(SQLconnection.getInstance().getConnection()).execute();
			
		}catch (SQLException e) {
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());

			b = false;
		}
		return b;
		
	}
	public boolean createCharacterTable() {
		boolean b = true;
		try{
			Queries.createCharactersTable(SQLconnection.getInstance().getConnection()).execute();
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
		
	}
	public boolean createMapTable() {
		boolean b = true;
		try{
			Queries.createMapTable(SQLconnection.getInstance().getConnection()).execute();
			
		}catch (SQLException e) {
			log.severe(this, "Database error: " +e.getMessage());
			// ////e.printStackTrace();
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean createMobDataTable() {
		boolean b = true;
		try{
			Queries.createMobDataTable(SQLconnection.getInstance().getConnection()).execute();
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean createMobsTable() {
		boolean b = true;
		try{
			Queries.createMobsTable(SQLconnection.getInstance().getConnection()).execute();
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean addMap(int id, String name, int gridsize, int areasize, int x, int y, int pool) {
		boolean b = true;
		try{
			Queries.addMap(SQLconnection.getInstance().getConnection(), id, name, gridsize, areasize,x,y,pool).execute();
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean createZoneTable() {
		boolean b = true;
		try{
			Queries.createZoneTable(SQLconnection.getInstance().getConnection()).execute();
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean CreateAccount(int accountID, String username, String password, int flags) {
		boolean b = true;
		try{
			Queries.CreateUserAccount(SQLconnection.getInstance().getConnection(), accountID, username, password, flags).execute();
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}
		return b;
	}
	public boolean createItemsData(Connection sql, int id, String name,String discription ,int category, int equip_slot, int width, int height, int is_consumable, int is_permanent, int enchantment_lvl,
	int Rfaction, int Rmin_lvl, int Rmax_lvl, int Rmonk, int Rsin, int Rwar, int Rmage, int Rstr, int Rint, int Rvit, int Ragi, int Rdex, int Astr, int Aint, int Avit, int Aagi, int Adex, 
	int Ahp, int Amana, int Aatk, int Adef, int Aatk_success, int Adeff_success, int Acrit_rate, int Acrit_dmg, int Aagainst_type, int Atype_dmg, 
	int SBset_hash, int SBpieces, int SBstr, int SBint, int SBvit, int SBagi, int SBdex, int SBhp, int SBmana, int SBstamina, int SBatk, int SBdeff, int SBatk_success, int SBdeff_success, int SBcrit_rate, int SBcrit_dmg, int SBtype_dmg,
	int pvp_dmg_inc,int time_to_expire,int base_item_id,int move_speed,	int npc_price) {
		boolean b = true;
		try{
			Queries.createItemsData(sql,  id,  name, discription , category,  equip_slot,  width,  height,  is_consumable,  is_permanent,  enchantment_lvl,
					 Rfaction,  Rmin_lvl,  Rmax_lvl,  Rmonk,  Rsin,  Rwar,  Rmage,  Rstr,  Rint,  Rvit,  Ragi,  Rdex,  
					 Astr,  Aint,  Avit,  Aagi,  Adex, Ahp,  Amana,  Aatk,  Adef,  Aatk_success,  Adeff_success,  Acrit_rate,  Acrit_dmg,  Aagainst_type,  Atype_dmg, 
					 SBset_hash,  SBpieces,  SBstr,  SBint,  SBvit,  SBagi,  SBdex,  SBhp,  SBmana,  SBstamina,  SBatk,  SBdeff,  SBatk_success,  SBdeff_success,  SBcrit_rate,  SBcrit_dmg,  SBtype_dmg,
					 pvp_dmg_inc, time_to_expire, base_item_id, move_speed,	 npc_price).execute();
			
		}catch (SQLException e) {
			// ////e.printStackTrace();
			log.severe(this, "Database error: " +e.getMessage());
			b = false;
		}
		catch (Exception e) {
			// ////e.printStackTrace();
			log.severe(this, "Unspecified error:" +e.getMessage());
			b = false;
		}

		return b;
		
	}*/

}
