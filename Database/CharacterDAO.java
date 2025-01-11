package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logging.ServerLogger;

import Player.Character;
import Player.Player;

public class CharacterDAO {
	private static ServerLogger log = ServerLogger.getInstance();
	/*
	 * Method to add new character entry in database. Also return character instance.
	 * Reason for instantiating Character here: database will implicitly ensure unique character ID.
	 * Up until adding new database entry for the character, we have no way of knowing it's ID-to-be.
	 * Add entry -> ID assigned -> retrieve entry with unique ID -> ??? -> profit
	 * Return: Character instance if success, null if failed
	 */
	public static Character addAndReturnNewCharacter(String name, byte[] stats, int chClass, byte statpoints, Player pl, int xCoords, int yCoords, int face, String expire) {
		try {
			SQLconnection.getInstance().execute(Queries.newCharacter(SQLconnection.getInstance().getaConnection(), name, stats, chClass, statpoints, xCoords, yCoords, pl.getAccountID(), face, expire));
				ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getCharacterByName(SQLconnection.getInstance().getaConnection(), name));
				boolean gotResults = rs.next();
				if(gotResults) {
					Character newCharacter = processCharacterTable(rs);
					newCharacter.setPlayer(pl);
					return newCharacter;
				} else {
					return null;
				}
		} catch (SQLException e) {
			////e.printStackTrace();
			log.severe(CharacterDAO.class, e.getMessage());
			return null;
		} catch (Exception e) {
			////e.printStackTrace();
			log.severe(CharacterDAO.class, e.getMessage());
			return null;
		}
	}
	
	 // --- inset into banned --- \\
	
	
	/*
	 * Load a single character with given name
	 * Return: Character object if found, null if no entry
	 */
	public static Character loadCharacter(String name) {
		ResultSet rs;
		try {
			rs = SQLconnection.getInstance().executeQuery(Queries.getCharacterByName(SQLconnection.getInstance().getaConnection(), name));
			//System.out.println(" ===>Banned in CharDAO: 2 " + name );
		boolean gotResults = rs.next();
		if(gotResults) {
			Character newCharacter = processCharacterTable(rs);
			return newCharacter;
		} else {
			return null;
		}
		} catch (SQLException e) {
			log.severe(CharacterDAO.class, "Database error: " + e.getMessage());
			////e.printStackTrace();
			return null;
		} catch (Exception e) {
			log.severe(CharacterDAO.class, e.getMessage());
			////e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Load a single character with given id
	 * Return: Character object if found, null if no entry
	 */
	public static Character loadCharacter(int id) {
		ResultSet rs;
		try {
			rs = SQLconnection.getInstance().executeQuery(Queries.getCharacterByID(SQLconnection.getInstance().getaConnection(), id));
		boolean gotResults = rs.next();
		if(gotResults) {
			Character newCharacter = processCharacterTable(rs);
			return newCharacter;
		} else {
			return null;
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block'
			log.severe(CharacterDAO.class, "Database error: " + e.getMessage());
			////e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe(CharacterDAO.class, e.getMessage());
			////e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Load list of characters with given account ID
	 * Return: List containing Character objects, or null if failed
	 */
	public static ArrayList<Character> loadCharacters(int id) {
		ArrayList<Character> chlist = new ArrayList<Character>();
		ResultSet rs;
		try {
			rs = SQLconnection.getInstance().executeQuery(Queries.getCharactersByOwnerID(SQLconnection.getInstance().getaConnection(), id));
				while(rs.next()) {
					chlist.add(processCharacterTable(rs));
				}
			if(!chlist.isEmpty()) {
				return chlist;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe(CharacterDAO.class, "Database error: " + e.getMessage());
			////e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe(CharacterDAO.class, e.getMessage());
			////e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Save given character object to database
	 * Return: true if success, false if fail
	 */
	public static boolean saveCharacter(Character chara) {
		try {
			return SQLconnection.getInstance().execute(Queries.saveCharacterData(SQLconnection.getInstance().getaConnection(), chara));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	/// custom shit \\\
	public static boolean putbanlol(int banned, String banreason) {
		try {
			////System.out.println(" ===>Banned in CharDAO: " + banned + banreason);
			return SQLconnection.getInstance().execute(Queries.banned(SQLconnection.getInstance().getaConnection(), banned, banreason));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean putbanlolbyname(int banned, String banreason, long banaxpire, String name) {
		try {
			return SQLconnection.getInstance().execute(Queries.bannedbyname(SQLconnection.getInstance().getaConnection(), banned, banreason, banaxpire,name));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean putbanlolbyAcntid(int banned, String banreason, long banaxpire, int acntid) {
		try {
			return SQLconnection.getInstance().execute(Queries.putbanlolbyAcntid(SQLconnection.getInstance().getaConnection(), banned, banreason, banaxpire,acntid));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	 // ipban
	public static boolean putipbanbyname(String ip, String banreason) {
		try {
			return SQLconnection.getInstance().execute(Queries.ipban(SQLconnection.getInstance().getaConnection(), ip, banreason ));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	// change name of any player to ?
	public static boolean changeplayername( String playernewname, String playercurrentname) {
		try {
			return SQLconnection.getInstance().execute(Queries.changeplayername(SQLconnection.getInstance().getaConnection(), playernewname, playercurrentname));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	// change GM LEVEL of any player  to ?
	public static boolean changeplayergmlevel( String setgmlevel , String playernewname ) {
		try {
			return SQLconnection.getInstance().execute(Queries.changeplayergmlevel(SQLconnection.getInstance().getaConnection(), setgmlevel , playernewname));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	// change face of any player  to ?
	public static boolean changeplayerface( int face , String playernewname ) {
		try {
			return SQLconnection.getInstance().execute(Queries.changeplayerface(SQLconnection.getInstance().getaConnection(), face , playernewname));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	// change level of any player  to ?
		public static boolean changeplayerlevel( int level , String playernewname ) {
			try {
				return SQLconnection.getInstance().execute(Queries.changeplayerlevel(SQLconnection.getInstance().getaConnection(), level , playernewname));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		// change faction of any player  to ?
				public static boolean changeplayerfaction( int faction , String playernewname ) {
					try {
						return SQLconnection.getInstance().execute(Queries.changeplayerfaction(SQLconnection.getInstance().getaConnection(), faction , playernewname));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// change Class of any player  to ?
				public static boolean changeplayerClass( int Class , String playernewname ) {
					try {
						return SQLconnection.getInstance().execute(Queries.changeplayerclass(SQLconnection.getInstance().getaConnection(), Class , playernewname));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				
				
				// set level 
				public static boolean setlevel( int level , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setlevel(SQLconnection.getInstance().getaConnection(), level , charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// set mhpoints 
				public static boolean setmhpoints( int mhpoints , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setmhpoints(SQLconnection.getInstance().getaConnection(), mhpoints , charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// set mhpoints 
				public static boolean insertprocedure(int itemid, int procedure) {
					try {
						return SQLconnection.getInstance().execute(Queries.insertprocedure(SQLconnection.getInstance().getaConnection(), itemid , procedure));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}

				public static boolean deletemail(int maild ) {
					try {
						return SQLconnection.getInstance().execute(Queries.deletemail(SQLconnection.getInstance().getaConnection(), maild));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean sendmail(int charid_SENDER, String name_SENDER, int charid_RECEIVER, String name_RECEIVER, long gold_REQUIRED, int item1, int stack1 ,int item2, int stack2 ,int item3, int stack3 ,int item4, int stack4 ,int item5, int stack5,int canceled) {
					try {
						return SQLconnection.getInstance().execute(Queries.sendmail(SQLconnection.getInstance().getaConnection(), charid_SENDER, name_SENDER,  charid_RECEIVER,  name_RECEIVER,  gold_REQUIRED,  item1,  stack1 , item2,  stack2 , item3,  stack3 , item4,  stack4 , item5,  stack5, canceled));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// set exp
				public static boolean setexp( long exp , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setexp(SQLconnection.getInstance().getaConnection(), exp , charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// INSERT FIRST equip on NEW character creation
				public static boolean setfirstequip(int Class , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setfirstequip(SQLconnection.getInstance().getaConnection(), Class , charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// INSERT FIRST inventory on NEW character creation
				public static boolean setfirstinventory(int Charclass, int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setfirstinventory(SQLconnection.getInstance().getaConnection(),Charclass, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				// INSERT FIRST skillbar on NEW character creation
				public static boolean setfirstskillbar(int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setfirstskillbar(SQLconnection.getInstance().getaConnection(), charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				// INSERT FIRST skills on NEW character creation
				public static boolean setfirstskills(int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setfirstskills(SQLconnection.getInstance().getaConnection(), charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// INSERT FIRST pots on NEW character creation
				public static boolean setfirstpots(int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setfirstpots(SQLconnection.getInstance().getaConnection(), charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// INSERT FIRST cargo on NEW character creation
				public static boolean setfirstcargo(int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setfirstcargo(SQLconnection.getInstance().getaConnection(), charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}	
				
				// INSERT FIRST expire on NEW character creation
				public static boolean setfirstexpire(int charid, String lol ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setfirstexpire(SQLconnection.getInstance().getaConnection(), charid, lol));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}	
				
				
				public static boolean setcargo(String one , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setcargo(SQLconnection.getInstance().getaConnection(), one, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}		
			
				public static boolean setinventory(String one , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setinventory(SQLconnection.getInstance().getaConnection(), one, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean setchar(float getlastknownX ,float getlastknownY, int getCurrentMap, int getHp, int getMana, int getStamina, long getExp, int getFame, long gold, String expire, String inventory, String cargo, String equip, String skillbar, String skills, String pots, int vp, int charid) {
					try {
						return SQLconnection.getInstance().execute(Queries.savechar(SQLconnection.getInstance().getaConnection(), getlastknownX , getlastknownY, getCurrentMap, getHp, getMana, getStamina, getExp, getFame, gold, expire, inventory, cargo, equip, skillbar, skills, pots, vp, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean saveinvcargo(String inventory, String cargo, int charid) {
					try {
						return SQLconnection.getInstance().execute(Queries.saveinvcargo(SQLconnection.getInstance().getaConnection(), inventory, cargo, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean setguildbasic(int type,String gname,int fame,int gold,int hat,int icon,String gnews,String members,int pvprating,int hiddenpvprating,int kills,int deaths,String waralliance,int BaptisteGiabiconi, int two ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setguildbasic(SQLconnection.getInstance().getaConnection(), type, gname, fame, gold, hat, icon, gnews,members,pvprating,hiddenpvprating,kills,deaths,waralliance, BaptisteGiabiconi, two ));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean setguildbuff(int one, int two) {
					try {
						return SQLconnection.getInstance().execute(Queries.setguildbuff(SQLconnection.getInstance().getaConnection(), one, two));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean setpewds(int itemid) {
					try {
						return SQLconnection.getInstance().execute(Queries.setpwds(SQLconnection.getInstance().getaConnection(), itemid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				
				public static boolean setequip(String one , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setequip(SQLconnection.getInstance().getaConnection(), one, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				public static boolean setskillbar(String one , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setskillbar(SQLconnection.getInstance().getaConnection(), one, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				public static boolean setskills(String one , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setskills(SQLconnection.getInstance().getaConnection(), one, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean setpots(String one , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setpots(SQLconnection.getInstance().getaConnection(), one, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				public static boolean setexpire(String one , int charid ) {
					try {
						return SQLconnection.getInstance().execute(Queries.setexpire(SQLconnection.getInstance().getaConnection(), one, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				
				
				
				
	
	public static boolean telexymap(String gox, String goy, String gomap, String getcharnamewhomadethecommand) {
		try {
			return SQLconnection.getInstance().execute(Queries.telexymap(SQLconnection.getInstance().getaConnection(), gox, goy, gomap, getcharnamewhomadethecommand));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	// same but using flaot, int der der for summon n appear \\
	public static boolean telexymapfloat(float gox, float goy, int gomap, String getcharnamewhomadethecommand) {
		try {
			return SQLconnection.getInstance().execute(Queries.telexymapfloat(SQLconnection.getInstance().getaConnection(), gox, goy, gomap, getcharnamewhomadethecommand));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	// save arributes\\
	public static boolean setarributesz(int Str, int Dex, int Vit, int Int, int Agi, int charid) {
		try {
			return SQLconnection.getInstance().execute(Queries.setarributes(SQLconnection.getInstance().getaConnection(), Str, Dex, Vit, Int ,Agi , charid));
		} catch (SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	// save statpoints \\
		public static boolean setstatpoints(int StatP, int charid) {
			try {
				return SQLconnection.getInstance().execute(Queries.setstatspoints(SQLconnection.getInstance().getaConnection(), StatP, charid));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		// save gold \\
				public static boolean setgold(long gold, int charid) {
					try {
						return SQLconnection.getInstance().execute(Queries.setgold(SQLconnection.getInstance().getaConnection(), gold, charid));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
				// save setendate \\
				public static boolean setendate(int charid, int itemid, long end_time_date) {
					try {
						return SQLconnection.getInstance().execute(Queries.setendate(SQLconnection.getInstance().getaConnection(), charid, itemid, end_time_date));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
	
	// DELETE CHARACTER \\
		public static boolean deletecharacter(String getcharnamewhomadethecommand) {
			try {
				return SQLconnection.getInstance().execute(Queries.deletechar(SQLconnection.getInstance().getaConnection(), getcharnamewhomadethecommand));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
	
		// save skill points \\
		public static boolean setskillpoints(int skillpoints, int charid) {
			try {
				return SQLconnection.getInstance().execute(Queries.setskillpoints(SQLconnection.getInstance().getaConnection(), skillpoints, charid));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		// save wholist \\
		public static boolean SetIpBan(String ip, String reason) {
			try {
				return SQLconnection.getInstance().execute(Queries.SetIpBan(SQLconnection.getInstance().getaConnection(), ip, reason));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		
		// save wholist \\
		public static boolean setwholist(int header1, int header2, String friendname, int charid) {
			try {
				return SQLconnection.getInstance().execute(Queries.setwholist(SQLconnection.getInstance().getaConnection(), header1, header2, friendname, charid));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		public static boolean setconsole(long time_n_date, String message, String by_who) {
			try {
				return SQLconnection.getInstance().execute(Queries.setconsole(SQLconnection.getInstance().getaConnection(), time_n_date, message, by_who));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
	// save fame lol derp\\
		public static boolean putfameza(int putfame, int charid) {
			try {
				return SQLconnection.getInstance().execute(Queries.putfamederp(SQLconnection.getInstance().getaConnection(), putfame, charid));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
				
		//delete skillbar
		public static boolean deleteskillbar(int charid, int SkillSlotId) {
			try {
				return SQLconnection.getInstance().execute(Queries.deleteskillbar(SQLconnection.getInstance().getaConnection(), charid, SkillSlotId));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		// delete skills
		public static boolean deleteskills(int charid, int skillid) {
			try {
				return SQLconnection.getInstance().execute(Queries.deleteskills(SQLconnection.getInstance().getaConnection(), charid, skillid));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		// delete wholist
		public static boolean deletewholist(int charid, int headerid1,int headerid2) {
			try { 
				return SQLconnection.getInstance().execute(Queries.deletewholist(SQLconnection.getInstance().getaConnection(), charid, headerid1, headerid2));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
	
	// save hp mana stamina in current coloums\\
		public static boolean savehpmanastam(int puthp, int putmana, int putstamina, String getcharnamewhomadethecommand) {
			try {
				return SQLconnection.getInstance().execute(Queries.savecurrenthpmanastam(SQLconnection.getInstance().getaConnection(), puthp, putmana, putstamina, getcharnamewhomadethecommand));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		// SAVE EQUIPMENT \\
		public static boolean saveequip(int EquipSLOT, int ItemID, int CharID) {
			try {
				return SQLconnection.getInstance().execute(Queries.saveequip(SQLconnection.getInstance().getaConnection(),EquipSLOT,ItemID , CharID));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		

		
		// create guild \\
		public static boolean createguild(int type, String name, int fame, int gold, int hat, int icon, String news, String members) {
			try {
				return SQLconnection.getInstance().execute(Queries.guildcreate(SQLconnection.getInstance().getaConnection(), type,  name,  fame,  gold,  hat,  icon,  news,  members));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		// delete guild \\
		public static boolean deleteguild(int GuildID) {
			try {
				return SQLconnection.getInstance().execute(Queries.removeguild(SQLconnection.getInstance().getaConnection(), GuildID));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		// putguildinchartable guild \\
		public static boolean putguildinchartable(int putguild, int charid) {
			try {
				return SQLconnection.getInstance().execute(Queries.putguildinchartable(SQLconnection.getInstance().getaConnection(), putguild,  charid));
			} catch (SQLException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		

				// REMOVE INVENTORY \\
				public static boolean removeinventory(int CharID) {
					try {
						return SQLconnection.getInstance().execute(Queries.removeinventory(SQLconnection.getInstance().getaConnection(), CharID));
					} catch (SQLException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				}
				
			
				
	
	/*
	 * Save a list of characters to database
	 * Return: true if all succeed, false if one or more fail
	 */
	public static boolean saveCharacters(List<Character> ch) {
		return true;
	}
	
	public static Character processCharacterTable(ResultSet rs) {
		Character newCharacter = new Character();
		try {
			
			newCharacter.setuid(rs.getInt("CharacterID"));
			newCharacter.setName(rs.getString("charname"));
			newCharacter.setLOGsetName(rs.getString("charname"));
			newCharacter.setCharacterClass(rs.getInt("charClass"));
			newCharacter.setFaction(rs.getInt("faction"));
			newCharacter.setLevel(rs.getInt("level"));
			newCharacter.setexpfromDAO(rs.getLong("exp"));
			newCharacter.setGuildID(rs.getInt("GuildID"));
			newCharacter.getequips(rs.getString("equip"));
			
			newCharacter.setX(rs.getInt(18));
			newCharacter.setY(rs.getInt(19));
			
			newCharacter.setCurrentMap(rs.getInt("map"));
			newCharacter.setFame(rs.getInt("fame"));
			
			newCharacter.setChargm(rs.getString("isgm"));
			newCharacter.funcommands = rs.getInt("funcommands");
			newCharacter.setFace(rs.getInt("face"));
			
			newCharacter.setStatPoints(rs.getInt("statpoints"));
			newCharacter.setSkillPoints(rs.getInt("skillpoints"));
			
			newCharacter.setmodelid(rs.getInt("modelid"));
			newCharacter.setgold(rs.getLong("gold"));
			newCharacter.getskillbardb(rs.getString("skillbar"));
			newCharacter.getskills(rs.getString("skills"));
			newCharacter.getcargo(rs.getString("cargo"));
			newCharacter.getinventorys(rs.getString("inventory"));
			newCharacter.getpots(rs.getString("pots"));
			newCharacter.getsexpire(rs.getString("expire"));
			
			newCharacter.setvp(rs.getInt("vp"));
		
			newCharacter.setintelligence(rs.getInt(21)); //INT 
			newCharacter.setvitality(rs.getInt(22)); //VIT 
			newCharacter.setstrength(rs.getInt(24)); //STR 
			newCharacter.setdextery(rs.getInt(25)); //DEX 
			newCharacter.setagility(rs.getInt(23)); //AGI 
			
	
			if ( rs.getInt("currentHP") <= 0)
			{
				newCharacter.setHp(132);
			} else { newCharacter.setHp(rs.getInt("currentHP")); }
				newCharacter.setMana(rs.getInt("currentMana"));
				newCharacter.setStamina(rs.getInt("currentStamina"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe(CharacterDAO.class, "Database error:WTFFFFF: " + e.getMessage());
			////e.printStackTrace();
		}
		return newCharacter;
	}

	
	
}
