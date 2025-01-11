package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import timer.SystemTimer;

import logging.ServerLogger;
import Player.Character;
import Configuration.ConfigurationManager;

public class Queries {
	private static ServerLogger log = ServerLogger.getInstance();
	
	public static PreparedStatement auth(Connection sqlc, String username, String hash) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM accounts WHERE username = ? AND password = ?;");
		st.setString(1, username);
		st.setString(2, hash);		
		return st;
	}
	
	// Authenticate with ip  etc\\
	public static PreparedStatement authipVIP(Connection sqlcz, String username ,String digitpass, String VIP) throws Exception {
		PreparedStatement stz = sqlcz.prepareStatement("SELECT * FROM accounts WHERE lastip = ? AND loggedin = ? AND VIP = ?");
		stz.setString(1, username);	
		stz.setString(2, digitpass);
		stz.setString(3, VIP);
		return stz;
	}
	
	// Authenticate with ip  etc\\
	public static PreparedStatement authip(Connection sqlcz, String username ,String digitpass) throws Exception {
		PreparedStatement stz = sqlcz.prepareStatement("SELECT * FROM accounts WHERE lastip = ? AND loggedin = ?");
		stz.setString(1, username);	
		stz.setString(2, digitpass);
		return stz;
	}
	
	public static PreparedStatement gettopfame(Connection connection, int faction) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT fame, CharacterID,charname FROM characters WHERE faction = ? ORDER BY fame DESC;");
		st.setInt(1, faction);
		return st;
	}
	
	public static PreparedStatement newCharacter(Connection sqlc, String name, byte[] stats, int chClass, byte statpoints, int xCoords, int yCoords, int owner, int face, String expire) {
		PreparedStatement st = null;
		try {
			st = sqlc.prepareStatement("INSERT INTO " +
					"characters(charname, charClass, faction, level, exp, maxHP, currentHP, maxMana, currentMana, maxStamina, currentStamina, attack, defence, gold, fame, flags, locationX, locationY, map, intelligence, vitality, agility, strength, dexterity, statpoints, skillpoints, ownerID, fameimgpath, isgm, face, modelid, GuildID, expire, inventory, cargo, equip, skillbar, skills, pots) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
	
		st.setString(1, name);  //character name
		st.setInt(2, chClass);  //class
		st.setInt(3, 0);	    //faction
		st.setInt(4, 1);	    //level
		st.setInt(5, 0);	    //exp 0 = 0% exp
		st.setInt(6, 10);	    //max HP
		st.setInt(7, 10);		//current HP	 	
		st.setInt(8, 10);		//max mana
		st.setInt(9, 10);		//current mana
		st.setInt(10, 10);		//max stamina
		st.setInt(11, 10);		//current stamina
		st.setInt(12, 10);		//attack
		st.setInt(13, 10);		//defence
		st.setInt(14, 0);		//gold
		st.setInt(15, 0);		//fame
		st.setInt(16, 10);		//flags
		
	
		
		st.setInt(17, xCoords);		//x coords
		st.setInt(18, yCoords);		//y coords
		
		st.setInt(19, 1);		//map
		
		st.setInt(20, (int)stats[0]);		//INT
		st.setInt(22, (int)stats[1]);		//AGI
		st.setInt(21, (int)stats[2]);		//VIT
		st.setInt(24, (int)stats[3]);		//DEX
		st.setInt(23, (int)stats[4]);		//STR
		
		st.setInt(25, (int)statpoints);		//statpoints
		st.setInt(26, 7);					//skillpoints
		st.setInt(27, owner); //owner account
		st.setString(28, "0.jpg"); 
		st.setString(29, "0");
		st.setInt(30, face); 
		st.setInt(31, 1); //modelid
		
		st.setInt(32, 0); 
		
		
		//expire, inventory, cargo, equip, skillbar, skills, pots)
		st.setString(33, expire); 
		
		//warrior
		if(chClass == 1)
		{
			st.setString(34, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
		"8,241111001,0,2,1,9,241112001,1,2,1,10,241113001,2,1,1,11,213062707,1,0,250");	
		}
		
		// assassin
		if(chClass == 2)
		{
			st.setString(34, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
		"8,241221001,0,3,1,9,241222001,1,2,1,10,241223001,2,2,1,11,213062707,1,0,250");	
		}
		
		// mage
		if(chClass == 3)
		{
			st.setString(34, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
		"8,241131001,0,3,1,9,241132001,2,3,1,10,241133001,4,2,1,11,213062707,1,0,250");	
		}
		
		//monk
		if(chClass == 4)
		{
			st.setString(34, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
		"8,241142001,0,3,1,9,241141001,2,1,1,10,241143001,3,2,1,11,213062707,1,0,250");	
		}
		
		st.setString(35, "0");
		
		//warrior
		if(chClass == 1)
		{
		st.setString(36, "0,210110101,1,207114101,2,0,3,202110103,4,203110102,5,0,6,209114101,7,201011002,8,0,9,208114101,10,208114101,11,206110102,12,0,13,0,14,0,15,0,16,0");	
		}
		
		// assassin
		if(chClass == 2)
		{
		st.setString(36, "0,210220101,1,207224101,2,0,3,202220103,4,203220102,5,0,6,209225101,7,201011008,8,0,9,208224101,10,208224101,11,206220102,12,0,13,0,14,0,15,0,16,0");	
		}
		
		// mage
		if(chClass == 3)
		{
		st.setString(36, "0,210130101,1,207134101,2,0,3,202130103,4,203130102,5,0,6,209135101,7,201011014,8,0,9,208134101,10,208134101,11,206130102,12,0,13,0,14,0,15,0,16,0");		
		}
		
		//monk
		if(chClass == 4)
		{
		st.setString(36, "0,210140101,1,207144101,2,0,3,202140103,4,203140102,5,0,6,209140101,7,201011020,8,0,9,208144101,10,208144101,11,206140102,12,0,13,0,14,0,15,0,16,0");	
		}
		st.setString(37, "0"); 
		st.setString(38, "0"); 
		st.setString(39, "0"); 
		
		} catch (SQLException e) {
			log.logMessage(Level.WARNING, Queries.class, e.getMessage());
		}
		
		
		return st;
	}
	
	public static PreparedStatement getAccountBoundByName(Connection sqlc, String name) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM accountbound WHERE namereserverd = ?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement getAccountByName(Connection sqlc, String name) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM accounts WHERE username = ?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement getCharacterByName(Connection sqlc, String name) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM characters WHERE charname = ?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement getCharacterByID(Connection sqlc, int id) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM characters WHERE CharacterID = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement getCharactersByOwnerID(Connection sqlc, int id) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM characters WHERE ownerID = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement getCharacters(Connection sqlc) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM characters ORDER BY level DESC, exp DESC;");
		return st;
	}
	public static PreparedStatement showTables(Connection sqlc) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SHOW TABLES;");
		return st;
	}
	public static PreparedStatement showDatabases(Connection sqlc) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SHOW DATABASES;");
		return st;
	}
	public static PreparedStatement createAccountTable(Connection sqlc) throws Exception{
		PreparedStatement st =  sqlc.prepareStatement("CREATE TABLE `accounts` ("+
														"`accountID` int(10) unsigned NOT NULL AUTO_INCREMENT," +
														"`username` char(13) NOT NULL," +
														"`password` char(255) NOT NULL," +
														"`flags` int(11) DEFAULT NULL," +
														"PRIMARY KEY (`accountID`), " +
														"UNIQUE KEY `uniqueuser` (`username`) USING HASH " +
														") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}
	// some1's gotta write us parser for these -.-
	public static PreparedStatement createCharactersTable(Connection sqlc) throws Exception{
		PreparedStatement st =  sqlc.prepareStatement("CREATE TABLE `characters` (" +
							" `CharacterID` int(10) unsigned NOT NULL AUTO_INCREMENT,"+
							" `charname` char(16) NOT NULL," +
							" `charClass` int(11) NOT NULL, " +
							" `faction` int(11) DEFAULT NULL,"+
							" `level` int(11) NOT NULL DEFAULT '1',"+
							" `maxHP` int(11) NOT NULL, " +
							" `currentHP` int(11) NOT NULL, "+
							" `maxMana` int(11) NOT NULL," +
							" `currentMana` int(11) NOT NULL,"+
							" `maxStamina` int(11) NOT NULL, "+
							" `currentStamina` int(11) NOT NULL, "+
							" `attack` int(11) NOT NULL,"+
							" `defence` int(11) NOT NULL, "+
							" `fame` int(11) NOT NULL,"+
							" `flags` int(11) DEFAULT NULL,"+
							" `locationX` int(11) NOT NULL,"+
							" `locationY` int(11) NOT NULL,"+
							" `map` int(11) NOT NULL,"+
							" `intelligence` int(11) NOT NULL,"+
							" `vitality` int(11) NOT NULL,"+
							" `agility` int(11) NOT NULL,"+
							" `strength` int(11) NOT NULL,"+
							" `dexterity` int(11) NOT NULL,"+
							" `statpoints` int(11) NOT NULL,"+
							" `skillpoints` int(11) NOT NULL,"+
							" `ownerID` int(10) unsigned NOT NULL,"+
							"PRIMARY KEY (`CharacterID`),"+
							" UNIQUE KEY `uniquename` (`charname`) USING HASH,"+
							"KEY `owner` (`ownerID`),"+
							"CONSTRAINT `mapID` FOREIGN KEY (`map`) REFERENCES `maps` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
							"CONSTRAINT `owner` FOREIGN KEY (`ownerID`) REFERENCES `accounts` (`accountID`) ON DELETE CASCADE ON UPDATE CASCADE"+
							") ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=ascii;");
		return st;
	}
	public static PreparedStatement createMapTable(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("CREATE TABLE `maps` ("+
													" `id` int(11) NOT NULL,"+
													"`name` varchar(45) NOT NULL,"+
													"`gridSize` int(11) NOT NULL,"+
													"`areaSize` int(11) NOT NULL,"+
													" `mapx` int(11) NOT NULL,"+
													"`mapy` int(11) NOT NULL,"+
													"`poolSize` int(11) NOT NULL,"+
													"PRIMARY KEY (`id`),"+
													"UNIQUE KEY `id_UNIQUE` (`id`)"+
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;

	}
	public static PreparedStatement createMobDataTable(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("CREATE TABLE `mobData` ("+
													"`mobID` int(11) NOT NULL,"+
													"`lvl` int(11) NOT NULL,"+
													"`attack` int(11) NOT NULL,"+
													"`defence` int(11) NOT NULL,"+
													"`maxhp` int(11) NOT NULL,"+
													"`basexp` int(11) NOT NULL,"+
													"`basefame` int(11) NOT NULL,"+
													"`aggroRange` int(11) NOT NULL,"+
													"`attackRange` int(11) NOT NULL,"+
													"`followRange` int(11) NOT NULL,"+
													"`moveRange` int(11) NOT NULL,"+
													"PRIMARY KEY (`mobID`),"+
													"UNIQUE KEY `mobID_UNIQUE` (`mobID`)"+
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}
	public static PreparedStatement createMobsTable(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("CREATE TABLE `mobs` ("+
													"`id` int(11) NOT NULL,"+
													"`mobType` int(11) NOT NULL,"+
													"`map` int(11) NOT NULL,"+
													"`spawnCount` int(11) NOT NULL,"+
													"`spawnRadius` int(11) NOT NULL," +
													"`spawnX` int(11) NOT NULL,"+
													"`spawnY` int(11) NOT NULL,"+
													"`respawnTime` int(11) NOT NULL,"+
													"`waypointCount` int(11) NOT NULL,"+
													"`waypointHop` int(11) NOT NULL,"+
													"PRIMARY KEY (`id`),"+
													"UNIQUE KEY `id_UNIQUE` (`id`),"+
													"KEY `type` (`mobType`),"+
													"KEY `map` (`map`),"+
													"CONSTRAINT `map` FOREIGN KEY (`map`) REFERENCES `maps` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,"+
													"CONSTRAINT `type` FOREIGN KEY (`mobType`) REFERENCES `mobData` (`mobID`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
		
	}
	public static PreparedStatement addMap(Connection con, int id, String name, int gridsize, int areasize, int x, int y, int pool) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO maps(id, name, gridSize, areaSize, mapx, mapy, poolSize) VALUES (?, ?, ?, ?, ?, ?, ?);");
		st.setInt(1, id);
		st.setString(2, name);
		st.setInt(3, gridsize);
		st.setInt(4, areasize);
		st.setInt(5, x);
		st.setInt(6, y);
		st.setInt(7, pool);
		return st;
	}
	public static PreparedStatement dropCharacterTable(Connection sqlc) throws Exception{
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  sqlc.prepareStatement("DROP TABLE characters;");
			return st;
		}
		throw new Exception();
		//return null;
	}
	public static PreparedStatement dropAccountTable(Connection sqlc) throws Exception{
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  sqlc.prepareStatement("DROP TABLE accounts;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement getMaps(Connection connection) throws Exception{
		PreparedStatement st = connection.prepareStatement("SELECT * FROM maps;");
		return st;
	}

	public static PreparedStatement dropMapTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE maps;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement getMobData(Connection con, int mobID) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM mobData WHERE mobID = ?;");
		st.setInt(1, mobID);
		return st;
	}
	// get mob drop items
	public static PreparedStatement getMobItemdrops(Connection con, int mobID) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM mobdrop WHERE mobid = ?;");
		st.setInt(1, mobID);
		return st;
	}
	
	// get RARE mob drop items
		public static PreparedStatement getRAREItemdrops(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM rareitems");
			return st;
		}
	
	// GET FRIENDS & IGNORES
	public static PreparedStatement getwholist(Connection con, int charid) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM wholist WHERE charid = ?;");
		st.setInt(1, charid);
		return st;
	}
	
	// GET skillbar
	public static PreparedStatement getskillsbar(Connection con, int charid) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM skillbar WHERE charid = ?;");
		st.setInt(1, charid);
		return st;
	}
	
	// GET skills
		public static PreparedStatement getskills(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM skills WHERE charid = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		// GET equips
		public static PreparedStatement getequips(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM equips WHERE CharID = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		// GET guilid
		public static PreparedStatement getguilid(Connection con, String name) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM guild WHERE name = ?;");
			st.setString(1, name);
			return st;
		}
		
		// GET cargo
		public static PreparedStatement getcargo(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM cargo WHERE charid = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		
		// GET inventorys
		public static PreparedStatement getinventorys(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM inventorys WHERE charid = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		// DELETE mail
		public static PreparedStatement deletemail(Connection con, int mailid) throws Exception {
			PreparedStatement st = con.prepareStatement("DELETE FROM mailbox WHERE mailid = ?;");
			st.setInt(1, mailid);
			return st;
		}
		
		// GET mails
		public static PreparedStatement getmailbyreceiver(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM mailbox WHERE charid_RECEIVER = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		public static PreparedStatement getmailbysender(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM mailbox WHERE charid_SENDER = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		public static PreparedStatement getmailbymailid(Connection con, int mailid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM mailbox WHERE mailid = ?;");
			st.setInt(1, mailid);
			return st;
		}
		
		public static PreparedStatement getguildbyname(Connection con, String guildname) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM guild WHERE name = ?;");
			st.setString(1, guildname);
			return st;
		}
		
		// GET playerpots
		public static PreparedStatement getpots(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM playerpots WHERE chaird = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		// GET item_char_enddate
		public static PreparedStatement getsexpire(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM item_char_enddate WHERE charid = ?;");
			st.setInt(1, charid);
			return st;
		}
		
		// GET item from itemdata
		public static PreparedStatement getitemdata(Connection con, int itemid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM itemdata WHERE ID = ?;");
			st.setInt(1, itemid);
			return st;
		}
		
		// GET item from guild
				public static PreparedStatement getguilddata(Connection con, int itemid) throws Exception {
					PreparedStatement st = con.prepareStatement("SELECT * FROM guild WHERE GuildID = ?;");
					st.setInt(1, itemid);
					return st;
				}
				
				// GET item from guild
				public static PreparedStatement getguildranks(Connection con) throws Exception {
					PreparedStatement st = con.prepareStatement("SELECT * FROM guild GROUP BY pvprating ORDER BY `pvprating` DESC;");
					return st;
				}
		
		// GET item from itemdata2
		public static PreparedStatement getITEM_DATA(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM itemdata;");
			return st;
		}
		
		public static PreparedStatement getskillpasives(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM passivedata;");
			return st;
		}
		
		// GET manual from manuals
		public static PreparedStatement getManuals(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM manuals;");
			return st;
		}
		
		// GET item from itemdata2
		public static PreparedStatement getITEM_price(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM itemprice;");
			return st;
		}
		
		
		public static PreparedStatement getfishing(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM farmfishing;");
			return st;
		}
		public static PreparedStatement getherbing(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM farmherbing;");
			return st;
		}
		public static PreparedStatement getmining(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM farmmining;");
			return st;
		}
		public static PreparedStatement getgraveyard(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM farmgraveyard;");
			return st;
		}
		
		public static PreparedStatement getgrinditems(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM grinditems;");
			return st;
		}
		
		public static PreparedStatement getInvincible_items(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM invincible_items;");
			return st;
		}
		
		public static PreparedStatement getNon_Tradable_items(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM non_tradable_items;");
			return st;
		}
		
		public static PreparedStatement guildbuffs(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM guildbuffs;");
			return st;
		}
		
		public static PreparedStatement getequipignore(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM equipignore;");
			return st;
		}
		
		public static PreparedStatement getitemtoprocedure(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM itemidtomanualid;");
			return st;
		}
		
		public static PreparedStatement getStackable_items(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM items_stackable;");
			return st;
		}
		
		public static PreparedStatement getMobdrops_skyzone(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM mobdrop_skyzone;");
			return st;
		}
		
		// GET item from itemdata2
		public static PreparedStatement getupgradelist(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM upgradelist;");
			return st;
		}
		
		
		// GET item from itemdata2
		public static PreparedStatement getITEM_upgrades(Connection con) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM upgrade;");
			return st;
		}
		
		
		public static PreparedStatement setskills(Connection con, int charid) throws Exception {
			PreparedStatement st = con.prepareStatement("SELECT * FROM skills WHERE charid = ?;");
			st.setInt(1, charid);
			return st;
		}
	
	// LOOOOOOOL get buffid
	public static PreparedStatement getbuffdata(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT * FROM buffdata;");
		return st;
	}
	
	// LOOOOOOOL get ipban
	public static PreparedStatement getipbanlist(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT * FROM ipban;");
		return st;
	}
	
	public static PreparedStatement getitemtoname(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT * FROM itemtoname;");
		return st;
	}
	
	public static PreparedStatement getcensoredwords(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT * FROM censoredwords;");
		return st;
	}
	
	
	// get skillpoints cost for skills
	public static PreparedStatement getskillpointscost(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT * FROM skillpointscost;");
		return st;
	}
	
	// get skilldata cost for skills
	public static PreparedStatement getskilldata(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT * FROM skilldata;");
		return st;
	}
	
	// get skilldata cost for skills
		public static PreparedStatement getskillbedoong(Connection connection) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM skillbedoong;");
			return st;
		}
	
	
	// get getskilleffects cost for skills
	public static PreparedStatement getskilleffects(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SELECT * FROM skilleffects;");
		return st;
	}
	
	// get lookuplevelstp
		public static PreparedStatement lookuplevelstp(Connection connection) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM lookuplevelstp;");
			return st;
		}
		
		// get lookupitem
		public static PreparedStatement lookupitem(Connection connection) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM lookupitem;");
			return st;
		}
		
		// get lookuplevelskp
		public static PreparedStatement lookuplevelskp(Connection connection) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM lookuplevelskp;");
			return st;
		}
	
		
		// get MAX EXP TABLE
		public static PreparedStatement Maxexptable(Connection connection) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM exptable;");
			return st;
		}
		
		// get MAX EXP TABLE
		public static PreparedStatement Maxfurytable(Connection connection) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM furytable;");
			return st;
		}
		
		// get realmstatus
		public static PreparedStatement getrealmstatus(Connection connection) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM realmstatus;");
			return st;
		}
		
		// get npcs
		public static PreparedStatement getnpcs(Connection connection, int id) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM npcs WHERE id = ?;");
			st.setInt(1, id);
			return st;
		}
		
		
		public static PreparedStatement getbanned(Connection connection, int id) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM accounts WHERE banned = ?;");
			st.setInt(1, id);
			return st;
		}
		
		public static PreparedStatement npcshops(Connection connection, int npcid) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM npcshops WHERE npcid = ?;");
			st.setInt(1, npcid);
			return st;
		}
		
		public static PreparedStatement getmhp(Connection connection, String username) throws Exception {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM accounts WHERE username = ?;");
			st.setString(1, username);
			return st;
		}
		
				public static PreparedStatement getdeathcd(Connection connection, int mobuid) throws Exception {
					PreparedStatement st = connection.prepareStatement("SELECT * FROM mobcooldowns WHERE mobuid = ?;");
					st.setInt(1, mobuid);
					return st;
				}
		
	public static PreparedStatement getMobs(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM mobs;");
		return st;
	}
	
	public static PreparedStatement getMapControllers(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM MapControllers;");
		return st;
	}
	
	public static PreparedStatement getareatriggers(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM areatriggers;");
		return st;
	}
	
	public static PreparedStatement getNpcgroups(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM npcgroups;");
		return st;
	}

	public static PreparedStatement getZones(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM zones;");
		return st;
	}

	public static PreparedStatement dropZonesTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE zones;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement dropMobsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE mobs;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement dropMobDataTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE mobData;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement createZoneTable(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("CREATE TABLE `zones` ("+
													"`id` int(11) NOT NULL AUTO_INCREMENT,"+
													"`map` int(11) NOT NULL,"+
													"`startx` int(11) NOT NULL,"+
													"`starty` int(11) NOT NULL,"+
													"`width` int(11) NOT NULL,"+
													"`length` int(11) NOT NULL,"+
													"`type` varchar(45) NOT NULL,"+
													"PRIMARY KEY (`id`),"+
													"UNIQUE KEY `id_UNIQUE` (`id`),"+
													"KEY `myMap` (`map`),"+
													"CONSTRAINT `myMap` FOREIGN KEY (`map`) REFERENCES `maps` (`id`) ON DELETE CASCADE ON UPDATE CASCADE"+
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}

	public static PreparedStatement CreateUserAccount(Connection connection, int accountID, String username, String password, int flags) throws Exception {
		PreparedStatement st = connection.prepareStatement("INSERT INTO accounts(accountID, username, password, flags) VALUES(?, ?, ?, ?);");
		st.setInt(1, accountID);
		st.setString(2, username);
		st.setString(3, password);
		st.setInt(4, flags);
		return st;
	}
	public static PreparedStatement createMobDataEntry(Connection con, int id, int lvl, int deff, int atk, int hp, int baseExp, int baseFame, int aggro, int follow, int move, int attrange) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO mobData(mobID, lvl, attack, defence, maxhp, basexp, basefame, aggroRange, attackRange, followRange, moveRange) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		st.setInt(1, id);
		st.setInt(2, lvl);
		st.setInt(3, atk);
		st.setInt(4, deff);
		st.setInt(5, hp);
		st.setInt(6, baseExp);
		st.setInt(7, baseFame);
		st.setInt(8, aggro);
		st.setInt(9, attrange);
		st.setInt(10, follow);
		st.setInt(11, move);
		return st;
	}
	
	public static PreparedStatement createItemsData(Connection con, int id, String name,String discription ,int category, int equip_slot, int width, int height, int is_consumable, int is_permanent, int enchantment_lvl,
			int Rfaction, int Rmin_lvl, int Rmax_lvl, int Rmonk, int Rsin, int Rwar, int Rmage, int Rstr, int Rint, int Rvit, int Ragi, int Rdex, 
			int Astr, int Aint, int Avit, int Aagi, int Adex, int Ahp, int Amana, int Aatk, int Adef, int Aatk_success, int Adeff_success, int Acrit_rate, int Acrit_dmg, int Aagainst_type, int Atype_dmg, 
			int SBset_hash, int SBpieces, int SBstr, int SBint, int SBvit, int SBagi, int SBdex, int SBhp, int SBmana, int SBstamina, int SBatk, int SBdeff, int SBatk_success, int SBdeff_success, int SBcrit_rate, int SBcrit_dmg, int SBtype_dmg,
			int pvp_dmg_inc,int time_to_expire,int base_item_id,int move_speed,	int npc_price) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO ItemData(itemid ,name, discription , category,  equip_slot,  width,  height,  is_consumable,  is_permanent,  enchantment_lvl, Rfaction,  Rmin_lvl,  Rmax_lvl,  Rmonk,  Rsin,  Rwar,  Rmage,  Rstr,  Rint,  Rvit,  Ragi,  Rdex,  Astr,  Aint,  Avit,  Aagi,  Adex, Ahp,  Amana, Astamina,  Aatk,  Adef,  Aatk_success,  Adeff_success,  Acrit_rate,  Acrit_dmg,  Aagainst_type,  Atype_dmg, SBset_hash,  SBpieces,  SBstr,  SBint,  SBvit,  SBagi,  SBdex,  SBhp,  SBmana,  SBstamina,  SBatk,  SBdeff,  SBatk_success,  SBdeff_success,  SBcrit_rate,  SBcrit_dmg, SBtype_dmg,  SBtype_dmg,pvp_dmg_inc, time_to_expire, base_item_id, move_speed,	 npc_price) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		
		st.setInt(1, id);st.setString(2, name);st.setString(3, discription);st.setInt(4, category);st.setInt(5, equip_slot);
		st.setInt(6, width);st.setInt(7, height);st.setInt(8, is_consumable);st.setInt(9, is_permanent);st.setInt(10, enchantment_lvl);
		
		st.setInt(11, Rfaction);st.setInt(12, Rmin_lvl);st.setInt(13, Rmax_lvl);st.setInt(14, Rmonk);st.setInt(15, Rsin);st.setInt(16, Rwar);st.setInt(17, Rmage);
		st.setInt(18, Rstr);st.setInt(19, Rint);st.setInt(20, Rvit);st.setInt(21, Ragi);st.setInt(22, Rdex);
		
		st.setInt(23, Astr);st.setInt(24, Aint);st.setInt(25, Aint);st.setInt(26, Adex);st.setInt(27, Ahp);st.setInt(28, Amana);st.setInt(29, Aatk);st.setInt(30, Adef);
		st.setInt(31, Aatk_success);st.setInt(32, Adeff_success);st.setInt(33, Acrit_rate);st.setInt(34, Acrit_dmg);st.setInt(35, Aagainst_type);st.setInt(36, Atype_dmg);
		
		st.setInt(37, SBset_hash);st.setInt(38, SBpieces);st.setInt(39, SBstr);st.setInt(40, SBint);st.setInt(41, SBvit);st.setInt(42, SBagi);st.setInt(43, SBdex);st.setInt(44, SBhp);
		st.setInt(45, SBmana);st.setInt(46, SBstamina);st.setInt(47, SBatk);st.setInt(48, SBdeff);st.setInt(49, SBatk_success);st.setInt(50, SBdeff_success);st.setInt(51, SBcrit_rate);
		st.setInt(52, SBcrit_dmg);st.setInt(53, SBtype_dmg);
		
		st.setInt(54, pvp_dmg_inc);st.setInt(55, time_to_expire);st.setInt(56, base_item_id);st.setInt(57, move_speed);st.setInt(58, npc_price);
		

		
		return st;
	}
	
	public static PreparedStatement saveCharacterData(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("INSERT INTO characters(locationX, locationY, map) VALUES(?, ?, ?);");
		st.setInt(1, (int)ch.getlastknownX());
		st.setInt(2, (int)ch.getlastknownY());
		st.setInt(3, ch.getCurrentMap());
		return st;
	}

	public static PreparedStatement savemobrespawncd(Connection con, int uid, long died, long newcd) throws Exception {
		PreparedStatement st = con.prepareStatement("INSERT INTO mobcooldowns(mobuid, respawncooldown, New_RESPAWN_CD) VALUES(?, ?, ?);");
		st.setInt(1, uid);
		st.setLong(2, died);
		st.setLong(3, newcd);
		return st;
	}
	
	// custom put in banned coloum --> 1 and banreason string reason lol
	public static PreparedStatement banned(Connection con, int banned, String banreason) throws Exception {
		////System.out.println(" ===>Banned in Queries 1: " + banned + banreason);
		PreparedStatement sthh = con.prepareStatement("UPDATE accounts SET banned = ? , banreason = ?;");
		////System.out.println(" ===>Banned in Queries 2: " + banned + banreason);
		sthh.setInt(1, banned);
		////System.out.println(" ===>Banned in Queries 3: " + banned + banreason);
		sthh.setString(2, banreason);
		////System.out.println(" ===>Banned in Queries 4: " + banned + banreason);
		return sthh;
	}
	
	
	public static PreparedStatement ipban(Connection con, String ip, String banreason) throws Exception {
		PreparedStatement sthh6 = con.prepareStatement("INSERT INTO ipban(ip, reason) VALUES(?, ?);");
		sthh6.setString(1, ip);
		sthh6.setString(2, banreason);
		return sthh6;
	}
	
	public static PreparedStatement bannedbyname(Connection con, int banned, String banreason,long banaxpire, String name) throws Exception {
		PreparedStatement sthh6 = con.prepareStatement("UPDATE accounts SET banned = ? , banreason = ?, banexpire = ? WHERE  username = ?;");
		sthh6.setInt(1, banned);
		sthh6.setString(2, banreason);
		sthh6.setString(4, name);
		sthh6.setLong(3, banaxpire);
		return sthh6;
	}
	
	public static PreparedStatement putbanlolbyAcntid(Connection con, int banned, String banreason,long banaxpire, int acntid) throws Exception {
		PreparedStatement sthh6 = con.prepareStatement("UPDATE accounts SET banned = ? , banreason = ?, banexpire = ? WHERE  accountID = ?;");
		sthh6.setInt(1, banned);
		sthh6.setString(2, banreason);
		sthh6.setInt(4, acntid);
		sthh6.setLong(3, banaxpire);
		return sthh6;
	}
	
	// players online in db
	public static PreparedStatement setPOnline(Connection con, int one , int two , int three , int four , int five) throws Exception {
		PreparedStatement sthh6 = con.prepareStatement("UPDATE serverstatus SET playersonlinecurrent = ? ,playersonlinetoday = ? ,hours = ? ,minutes = ? ,seconds = ?   WHERE  header = 1;");
		sthh6.setInt(1, one);
		sthh6.setInt(2, two);
		sthh6.setInt(3, three);
		sthh6.setInt(4, four);
		sthh6.setInt(5, five);
		return sthh6;
	}
	
	public static PreparedStatement setserverstatus(Connection con, int one) throws Exception {
		PreparedStatement sthh6 = con.prepareStatement("UPDATE serverstatus SET online = ?  WHERE  header = 1;");
		sthh6.setInt(1, one);
		return sthh6;
	}
	
	// set arributes WHERE, Charid
	public static PreparedStatement setarributes(Connection con, int Strength, int Dextery, int Vitality, int Intelligence, int Agility, int charid ) throws Exception {
		PreparedStatement sthh6 = con.prepareStatement("UPDATE characters SET strength = ? ,dexterity = ? ,vitality = ? ,intelligence = ? , agility = ? WHERE  CharacterID = ?;");
		sthh6.setInt(1, Strength);
		sthh6.setInt(2, Dextery);
		sthh6.setInt(3, Vitality);
		sthh6.setInt(4, Intelligence);
		sthh6.setInt(5, Agility);
		sthh6.setInt(6, charid);
		return sthh6;
	}
	
	// skills
	public static PreparedStatement setstatspoints(Connection con, int skillP , int CharacterID) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET statpoints = ? WHERE  CharacterID = ?;");
		sthh61.setInt(1, skillP);
		sthh61.setInt(2, CharacterID);
		return sthh61;
	}
	
	// set gold
	public static PreparedStatement setgold(Connection con, long gold , int CharacterID) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET gold = ? WHERE  CharacterID = ?;");
		sthh61.setLong(1, gold);
		sthh61.setInt(2, CharacterID);
		return sthh61;
	}
	
	// set setendate
	public static PreparedStatement setendate(Connection con, int charid, int itemid, long end_time_date) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("INSERT INTO item_char_enddate(charid, itemid, end_time_date) VALUES(?, ?, ?);");
		sthh61.setInt(1, charid);
		sthh61.setInt(2, itemid);
		sthh61.setLong(3, end_time_date);
		return sthh61;
	}
	
	// INSERT first equipment upon
			public static PreparedStatement setfirstequip(Connection con,int Class , int Charid) throws Exception {
				PreparedStatement st = con.prepareStatement("UPDATE characters SET equip = ? WHERE CharacterID = ?;");
				st.setInt(1, Charid);
				
				//warrior
				if(Class == 1)
				{
				st.setString(2, "0,210110101,1,207114101,2,0,3,202110103,4,203110102,5,0,6,209114101,7,201011002,8,0,9,208114101,10,208114101,11,206110102,12,0,13,0,14,0,15,0,16,0");	
				}
				
				// assassin
				if(Class == 2)
				{
				st.setString(2, "0,210220101,1,207224101,2,0,3,202220103,4,203220102,5,0,6,209225101,7,201011008,8,0,9,208224101,10,208224101,11,206220102,12,0,13,0,14,0,15,0,16,0");	
				}
				
				// mage
				if(Class == 3)
				{
				st.setString(2, "0,210130101,1,207134101,2,0,3,202130103,4,203130102,5,0,6,209135101,7,201011014,8,0,9,208134101,10,208134101,11,206130102,12,0,13,0,14,0,15,0,16,0");		
				}
				
				//monk
				if(Class == 4)
				{
				st.setString(2, "0,210140101,1,207144101,2,0,3,202140103,4,203140102,5,0,6,209140101,7,201011020,8,0,9,208144101,10,208144101,11,206140102,12,0,13,0,14,0,15,0,16,0");	
				}
				return st;
			}
			
			// first inventory \\
			public static PreparedStatement setfirstinventory(Connection con,int Class, int CharID) throws Exception {
				PreparedStatement sthh32 = con.prepareStatement("UPDATE characters SET inventory = ? WHERE CharacterID = ?;");
				sthh32.setInt(1, CharID);
				
				
				//warrior
				if(Class == 1)
				{
				sthh32.setString(2, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
				"8,241111001,0,2,1,9,241112001,1,2,1,10,241113001,2,1,1,11,213062707,1,0,250");	
				}
				
				// assassin
				if(Class == 2)
				{
					sthh32.setString(2, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
				"8,241221001,0,3,1,9,241222001,1,2,1,10,241223001,2,2,1,11,213062707,1,0,250");	
				}
				
				// mage
				if(Class == 3)
				{
					sthh32.setString(2, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
				"8,241131001,0,3,1,9,241132001,2,3,1,10,241133001,4,2,1,11,213062707,1,0,250");	
				}
				
				//monk
				if(Class == 4)
				{
					sthh32.setString(2, "0,213010007,0,0,9500,1,213020007,0,1,9500,2,213062709,1,1,500,3,283000209,2,0,1,4,283000207,3,0,1,5,283000208,4,0,1,6,273001227,3,1,1,7,283000002,4,1,250,"+
				"8,241142001,0,3,1,9,241141001,2,1,1,10,241143001,3,2,1,11,213062707,1,0,250");	
				}	
				return sthh32;
			}
			
			// send mail
			public static PreparedStatement sendmail(Connection con,int charid_SENDER, String name_SENDER, int charid_RECEIVER, String name_RECEIVER, long gold_REQUIRED, int item1, int stack1 ,int item2, int stack2 ,int item3, int stack3 ,int item4, int stack4 ,int item5, int stack5, int canceled) throws Exception {
				PreparedStatement sthh61 = con.prepareStatement("INSERT INTO mailbox(charid_SENDER, name_SENDER, charid_RECEIVER, name_RECEIVER, gold_REQUIRED, item1, stack1 , item2, stack2 , item3, stack3, item4, stack4 , item5, stack5, canceled) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
				sthh61.setInt(1, charid_SENDER);
				sthh61.setString(2, name_SENDER);
				sthh61.setInt(3, charid_RECEIVER);
				sthh61.setString(4, name_RECEIVER);
				sthh61.setLong(5, gold_REQUIRED);
				sthh61.setInt(6, item1);
				sthh61.setInt(7, stack1);
				sthh61.setInt(8, item2);
				sthh61.setInt(9, stack2);
				sthh61.setInt(10, item3);
				sthh61.setInt(11, stack3);
				sthh61.setInt(12, item4);
				sthh61.setInt(13, stack4);
				sthh61.setInt(14, item5);
				sthh61.setInt(15, stack5);
				sthh61.setInt(16, canceled);
				return sthh61;
			}
			
			// logg dat whore
			public static PreparedStatement setlog(Connection con, int accountid, String loggedip, String TIME_DATE) throws Exception {
				PreparedStatement sthh61 = con.prepareStatement("INSERT INTO login_logs(accountid, loggedip, TIME_DATE) VALUES(?, ?, ?);");
				sthh61.setInt(1, accountid);
				sthh61.setString(2, loggedip);
				sthh61.setString(3, TIME_DATE);
				return sthh61;
			}
	
		// first skillbar
		public static PreparedStatement setfirstskillbar(Connection con,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET skillbar = ? WHERE CharacterID = ?;");
			sthh61.setInt(1, CharacterID);
			return sthh61;
		}
		
		// first skills
		public static PreparedStatement setfirstskills(Connection con,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET skills = ? WHERE CharacterID = ?;");
			sthh61.setInt(1, CharacterID);
			return sthh61;
		}
		
		// first pots
		public static PreparedStatement setfirstpots(Connection con,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET pots = ? WHERE CharacterID = ?;");
			sthh61.setInt(1, CharacterID);
			return sthh61;
		}
		
		// first cargo
		public static PreparedStatement setfirstcargo(Connection con,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET cargo = ? WHERE CharacterID = ?;");
			sthh61.setInt(1, CharacterID);
			return sthh61;
		}
		
		// first expire
		public static PreparedStatement setfirstexpire(Connection con,int CharacterID, String lol) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET expire = ? WHERE CharacterID = ?;");
			sthh61.setInt(1, CharacterID);
			sthh61.setString(2, lol);
			return sthh61;
		}
		
		
		
		public static PreparedStatement setcargo(Connection con, String one ,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE cargo SET cargo = ? WHERE charid = ?;");
			sthh61.setString(1, one);
			sthh61.setInt(2, CharacterID);
			return sthh61;
		}
		
		public static PreparedStatement setinventory(Connection con, String one ,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE inventorys SET inventory = ? WHERE charid = ?;");
			sthh61.setString(1, one);
			sthh61.setInt(2, CharacterID);
			return sthh61;
		}
		
		public static PreparedStatement savechar(Connection con,float getlastknownX ,float getlastknownY, int getCurrentMap, int getHp, int getMana, int getStamina, long getExp, int getFame, long gold, String expire, String inventory, String cargo, String equip, String skillbar, String skills, String pots, int vp, int charid) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET locationX = ? , locationY = ?, map = ?, currentHP = ?, currentMana = ?, currentStamina = ?, exp = ?, fame = ?, gold = ?, expire = ?, inventory = ?, cargo = ?, equip = ?, skillbar = ?, skills = ?, pots = ?, vp = ? WHERE CharacterID = ?;");
			sthh61.setFloat(1, getlastknownX);
			sthh61.setFloat(2, getlastknownY);
			sthh61.setInt(3, getCurrentMap);
			sthh61.setInt(4, getHp);
			sthh61.setInt(5, getMana);
			sthh61.setInt(6, getStamina);
			sthh61.setLong(7, getExp);
			sthh61.setInt(8, getFame);
			sthh61.setLong(9, gold);
			sthh61.setString(10, expire);
			sthh61.setString(11, inventory);
			sthh61.setString(12, cargo);
			sthh61.setString(13, equip);
			sthh61.setString(14, skillbar);
			sthh61.setString(15, skills);
			sthh61.setString(16, pots);
			sthh61.setInt(17, vp);
			sthh61.setInt(18, charid);
			return sthh61;
		}
		
		public static PreparedStatement saveinvcargo(Connection con, String inventory, String cargo, int charid) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET inventory = ?, cargo = ? WHERE CharacterID = ?;");
			sthh61.setString(1, inventory);
			sthh61.setString(2, cargo);
			sthh61.setInt(3, charid);
			return sthh61;
		}
		
		

		public static PreparedStatement setguildbasic(Connection con,int type,String gname,int fame,int gold,int hat,int icon,String gnews,String members,int pvprating,int hiddenpvprating,int kills,int deaths,String waralliance, int BaptisteGiabiconi, int two) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE guild SET type = ? , name = ?, fame = ?, gold = ?, hat = ?, icon = ?, news = ?, members = ?, pvprating = ?, hiddenpvprating = ?, kills = ?, deaths = ?, waralliance = ?, BaptisteGiabiconi =? WHERE GuildID = ?;");
			sthh61.setInt(1, type);
			sthh61.setString(2, gname);
			sthh61.setInt(3, fame);
			sthh61.setInt(4, gold);
			sthh61.setInt(5, hat);
			sthh61.setInt(6, icon);
			sthh61.setString(7, gnews);
			sthh61.setString(8, members);
			sthh61.setInt(9, pvprating);
			sthh61.setInt(10, hiddenpvprating);
			sthh61.setInt(11, kills);
			sthh61.setInt(12, deaths);
			sthh61.setString(13, waralliance);
			sthh61.setInt(14, BaptisteGiabiconi);
			sthh61.setInt(15, two);
			return sthh61;
		}
		public static PreparedStatement setequip(Connection con, String one ,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE equips SET equip = ? WHERE CharID = ?;");
			sthh61.setString(1, one);
			sthh61.setInt(2, CharacterID);
			return sthh61;
		}
		
		public static PreparedStatement setguildbuff(Connection con, int one ,int two) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE guildbuffs SET guilduid = ? WHERE buffid = ?;");
			sthh61.setInt(1, one);
			sthh61.setInt(2, two);
			return sthh61;
		}
		
		public static PreparedStatement setskillbar(Connection con, String one ,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE skillbar SET skillbar = ? WHERE charid = ?;");
			sthh61.setString(1, one);
			sthh61.setInt(2, CharacterID);
			return sthh61;
		}
		public static PreparedStatement setskills(Connection con, String one ,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE skills SET skills = ? WHERE charid = ?;");
			sthh61.setString(1, one);
			sthh61.setInt(2, CharacterID);
			return sthh61;
		}
		
		public static PreparedStatement setpots(Connection con, String one ,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE playerpots SET pots = ? WHERE chaird = ?;");
			sthh61.setString(1, one);
			sthh61.setInt(2, CharacterID);
			return sthh61;
		}
		
		
		
		public static PreparedStatement setexpire(Connection con, String one ,int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET expire = ? WHERE CharacterID = ?;");
			sthh61.setString(1, one);
			sthh61.setInt(2, CharacterID);
			return sthh61;
		}
		

		// ????????/
		public static PreparedStatement SetIpBan(Connection con, String ip, String reason) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("INSERT INTO ipban(ip, reason) VALUES(?, ?);");
			sthh61.setString(1, ip);
			sthh61.setString(2, reason);
			return sthh61;
		}
	
		
		public static PreparedStatement setpwds(Connection con, int itemd) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("INSERT INTO stackable_items(itemid) VALUES(?);");
			sthh61.setInt(1, itemd);
			return sthh61;
		}
		
		
		// friends & ignore list
		public static PreparedStatement setwholist(Connection con,int header1 ,int header2 ,String friendname , int CharacterID) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("INSERT INTO wholist(charid, headerid1, headerid2, friendname) VALUES(?, ?, ?, ?);");
			sthh61.setInt(1, CharacterID);
			sthh61.setInt(2, header1);
			sthh61.setInt(3, header2);
			sthh61.setString(4, friendname);
			return sthh61;
		}
		
		public static PreparedStatement setconsole(Connection con,long time_n_date, String message, String by_who) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("INSERT INTO console(time_n_date, message, by_who)VALUES(?, ?, ?);");
			sthh61.setLong(1, time_n_date);
			sthh61.setString(2, message);
			sthh61.setString(3, by_who);
			return sthh61;
		}
	
	// skill point
	public static PreparedStatement setskillpoints(Connection con,int skillpoints , int CharacterID) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET skillpoints = ? WHERE CharacterID = ?;");
		sthh61.setInt(1, skillpoints);
		sthh61.setInt(2, CharacterID);
		return sthh61;
	}
	public static PreparedStatement changeplayername(Connection con,String newplrname , String plrcurrnt) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET charname = ? WHERE charname = ?;");
		sthh61.setString(1, newplrname);
		sthh61.setString(2, plrcurrnt);
		return sthh61;
	}
	// set character gm level by player name
	public static PreparedStatement changeplayergmlevel(Connection con, String setgmlevel, String newplrname ) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET isgm = ? WHERE charname = ?;");
		sthh61.setString(1, setgmlevel);
		sthh61.setString(2, newplrname);
		return sthh61;
	}
	
	// set character face by player name
	public static PreparedStatement changeplayerface(Connection con, int face, String newplrname ) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET face = ? WHERE charname = ?;");
		sthh61.setInt(1, face);
		sthh61.setString(2, newplrname);
		return sthh61;
	}
	
	// set character level by player name
	public static PreparedStatement changeplayerlevel(Connection con, int level, String newplrname ) throws Exception {
		PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET level = ? WHERE charname = ?;");
		sthh61.setInt(1, level);
		sthh61.setString(2, newplrname);
		return sthh61;
	}
	// set character faction by player name
		public static PreparedStatement changeplayerfaction(Connection con, int faction, String newplrname ) throws Exception {
			PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET faction = ? WHERE charname = ?;");
			sthh61.setInt(1, faction);
			sthh61.setString(2, newplrname);
			return sthh61;
		}
		
		// set character class by player name
				public static PreparedStatement changeplayerclass(Connection con, int Class, String newplrname ) throws Exception {
					PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET charClass = ? WHERE charname = ?;");
					sthh61.setInt(1, Class);
					sthh61.setString(2, newplrname);
					return sthh61;
				}
	
				
				// set level by charid
				public static PreparedStatement setlevel(Connection con, int level, int charid ) throws Exception {
					PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET level = ? WHERE CharacterID = ?;");
					sthh61.setInt(1, level);
					sthh61.setInt(2, charid);
					return sthh61;
				}
				
				// set mhpoints by charid
				public static PreparedStatement setmhpoints(Connection con, int mhpoints, int charid ) throws Exception {
					PreparedStatement sthh61 = con.prepareStatement("UPDATE accounts SET mhpoints = ? WHERE accountID = ?;");
					sthh61.setInt(1, mhpoints);
					sthh61.setInt(2, charid);
					return sthh61;
				}
				
				public static PreparedStatement insertprocedure(Connection con, int itemid, int procedureid ) throws Exception {
					PreparedStatement sthh61 = con.prepareStatement("INSERT INTO itemidtoprocedureid(itemid, manualid)VALUES(?, ?);");
					sthh61.setInt(1, itemid);
					sthh61.setInt(2, procedureid);
					return sthh61;
				}
				
				// set EXP by charid
				public static PreparedStatement setexp(Connection con, long exp, int charid ) throws Exception {
					PreparedStatement sthh61 = con.prepareStatement("UPDATE characters SET exp = ? WHERE CharacterID = ?;");
					sthh61.setLong(1, exp);
					sthh61.setInt(2, charid);
					return sthh61;
				}
	public static PreparedStatement telexymap(Connection con, String gox, String goy, String gomap, String getcharnamewhomadethecommand) throws Exception {
		PreparedStatement sthh3 = con.prepareStatement("UPDATE characters SET locationX = ? , locationY = ?, map = ? WHERE charname = ?;");
		sthh3.setString(1, gox);
		sthh3.setString(2, goy);
		sthh3.setString(3, gomap);
		sthh3.setString(4, getcharnamewhomadethecommand);
		return sthh3;
	}
	//using float , int to summon and appear \\
	public static PreparedStatement telexymapfloat(Connection con, float gox, float goy, int gomap, String getcharnamewhomadethecommand) throws Exception {
		PreparedStatement sthh31 = con.prepareStatement("UPDATE characters SET locationX = ? , locationY = ?, map = ? WHERE charname = ?;");
		sthh31.setFloat(1, gox);
		sthh31.setFloat(2, goy);
		sthh31.setInt(3, gomap);
		sthh31.setString(4, getcharnamewhomadethecommand);
		return sthh31;
	}
	
	public static PreparedStatement deletemobrespawncd(Connection con, int mobuid) throws Exception {
		PreparedStatement sthh31 = con.prepareStatement("DELETE FROM mobcooldowns WHERE mobuid = ?;");
		sthh31.setInt(1, mobuid);
		return sthh31;
	}
	
	// DELETE CHARACTER \\
	public static PreparedStatement deletechar(Connection con, String getcharnamewhomadethecommand) throws Exception {
		PreparedStatement sthh31 = con.prepareStatement("DELETE FROM characters WHERE charname = ?;");
		sthh31.setString(1, getcharnamewhomadethecommand);
		return sthh31;
	}
	public static PreparedStatement deleteskillbar(Connection con, int charid, int SkillSlotId) throws Exception {
		PreparedStatement sthh31 = con.prepareStatement("DELETE FROM skillbar WHERE charid = ? AND SkillSlotId = ?;");
		sthh31.setInt(1, charid);
		sthh31.setInt(2, SkillSlotId);
		return sthh31;
	}
	public static PreparedStatement deleteskills(Connection con, int charid, int skillid) throws Exception {
		PreparedStatement sthh31 = con.prepareStatement("DELETE FROM skills WHERE charid = ? AND skillid = ?;");
		sthh31.setInt(1, charid);
		sthh31.setInt(2, skillid);
		return sthh31;
	}
	public static PreparedStatement deletewholist(Connection con, int charid,int headerid1, int headerid2) throws Exception {
		PreparedStatement sthh31 = con.prepareStatement("DELETE FROM wholist WHERE charid = ? AND headerid1 = ? AND headerid2 = ?;");
		sthh31.setInt(1, charid);
		sthh31.setInt(2, headerid1);
		sthh31.setInt(3, headerid2);
		return sthh31;
	}
	
	//put query fame lol \\
		public static PreparedStatement putfamederp(Connection con, int putfame, int charid) throws Exception {
			PreparedStatement sthh37 = con.prepareStatement("UPDATE characters SET fame = ? WHERE CharacterID = ?;");
			sthh37.setInt(1, putfame);
			sthh37.setInt(2, charid);
			return sthh37;
		}
		
		//put guild in character table\\
				public static PreparedStatement putguildinchartable(Connection con, int putguild, int charid) throws Exception {
					PreparedStatement sthh37 = con.prepareStatement("UPDATE characters SET GuildID = ? WHERE CharacterID = ?;");
					sthh37.setInt(1, putguild);
					sthh37.setInt(2, charid);
					return sthh37;
				}
		
	
	// save current hp mana stam lolzderp wannabe is a huge chickennugget ,oh wait kfc... \\
		public static PreparedStatement savecurrenthpmanastam(Connection con, int puthp, int putmana, int putstamina, String getcharnamewhomadethecommand) throws Exception {
			PreparedStatement sthh32 = con.prepareStatement("UPDATE characters SET currentHP = ? , currentMana = ?, currentStamina = ? WHERE charname = ?;");
			sthh32.setInt(1, puthp);
			sthh32.setInt(2, putmana);
			sthh32.setInt(3, putstamina);
			sthh32.setString(4, getcharnamewhomadethecommand);
			return sthh32;
		}
		
		// save equipment \\
		public static PreparedStatement saveequip(Connection con, int equipSLOT, int ItemID, int CharID) throws Exception {
			PreparedStatement sthh32 = con.prepareStatement("UPDATE equips SET e? = ?  WHERE CharID = ?;");
			sthh32.setInt(1, equipSLOT);
			sthh32.setInt(2, ItemID);
			sthh32.setInt(3, CharID);
			return sthh32;
		}
		
		
		// guildcreate \\
		public static PreparedStatement guildcreate(Connection con,int type, String name, int fame, int gold, int hat, int icon, String news, String members) throws Exception {
			PreparedStatement sthh32 = con.prepareStatement("INSERT INTO guild(type, name, fame, gold, hat, icon, news, members) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
			sthh32.setInt(1, type);
			sthh32.setString(2, name);
			sthh32.setInt(3, fame);
			sthh32.setInt(4, gold);
			sthh32.setInt(5, hat);
			sthh32.setInt(6, icon);
			sthh32.setString(7, news);
			sthh32.setString(8, members);
			return sthh32;
		}
		
				
				// remove guild \\
				public static PreparedStatement removeguild(Connection con, int GuildID) throws Exception {
					PreparedStatement sthh31 = con.prepareStatement("DELETE FROM guild WHERE GuildID = ?;");
					sthh31.setInt(1, GuildID);
					return sthh31;
				}
		
		// remove inventory \\
		public static PreparedStatement removeinventory(Connection con, int CharID) throws Exception {
			PreparedStatement sthh31 = con.prepareStatement("DELETE FROM inventorys WHERE charid = ?;");
			sthh31.setInt(1, CharID);
			return sthh31;
		}

	public static PreparedStatement showGrants(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SHOW GRANTS;");
		return st;
	}
	
}
