package Player;

import item.ItemCache;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Configuration.ConfigurationManager;
import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;
import GameServer.GamePackets.Ping;
import GameServer.GamePackets.UseItemParser;
import Mob.MobController;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class Guildwar implements Cloneable{
	public static Guildwar instance; 
	public ConcurrentMap<Integer, Integer> stones = new ConcurrentHashMap<Integer, Integer>();
	private int inc = 0;
	private boolean GUILDWAR = false;
	private WMap wmap = WMap.getInstance();
	private boolean Yeokcheon = false;
	private boolean Geumgang = false;
	private boolean Myeongchen = false;
	private boolean Ilwui = false;
	private boolean Hyeoncheon = false;
	private boolean Cheonra = false;
	
	
    public synchronized static Guildwar getInstance(){
        if (instance == null){
                instance = new Guildwar();
        }
        return instance;
    }
    
    public boolean isguildwar(){
    	return this.GUILDWAR;
    }
    
	//end guildwar
	public void Endguildwar(){
	this.GUILDWAR = false;	
	this.announce("Guild War has ended!");
	}
	
	//setup up and start
	public void Setupguildwar(){	
	// announce guildwar has started.
	this.GUILDWAR = true;
	Charstuff.getInstance().guildbuffs.clear();
	CharacterDAO.setguildbuff(0, 96);
	CharacterDAO.setguildbuff(0, 97);
	CharacterDAO.setguildbuff(0, 98);
	CharacterDAO.setguildbuff(0, 99);
	CharacterDAO.setguildbuff(0, 100);
	CharacterDAO.setguildbuff(0, 101);
	this.announce("Guild War has started!");
		
	//put mobs to bufficonid
	this.stones.put(29501, 96); // Offence v
	this.stones.put(29502, 98); // Mana cap v
	this.stones.put(29503, 99); // HP cap v
	this.stones.put(29504, 97); // Defence v
	this.stones.put(29505, 100);// Attack succes v
	this.stones.put(29506, 101);// final attack points v
		
	// spawn stones
	this.spawnmobstone(29501, 1, 1, 5118, 16062, 3); //Yeokcheon Stone | 5% Offence | Haunted Zone in Dead sea   
	this.spawnmobstone(29502, 1, 1, -1729, 7137, 1); //Geumgang Stone  | 500+ Mana Total | Guryong village in V.V       
	this.spawnmobstone(29503, 1, 1, 13777, 7699, 2); //Myeongchen Stone| 500+ HP Total | Nachung Valley in K.C        
	this.spawnmobstone(29504, 1, 1, 5142, 16124, 3); //Ilwui Stone     | 5% Defence | Haunted Zone in Dead sea     
	this.spawnmobstone(29505, 1, 1, 3931, -993, 1);  //Hyeoncheon Stone| 5% Attack succes | Ilsin Temple in V.V          
	this.spawnmobstone(29506, 1, 1, 9661, 5390, 2);  //Cheonra Stone   | 5% Final attack bonus | Dungeon of SungHwa        
	
	this.announce("Yeokcheon Stone has spawned.");
	this.announce("Geumgang Stone has spawned.");
	this.announce("Myeongchen Stone has spawned.");
	this.announce("Ilwui Stone has spawned.");
	this.announce("Hyeoncheon Stone has spawned.");
	this.announce("Cheonra Stone has spawned.");

	}
	
	//handle from stone death to give buff
	public void stonedie(int mobid, Character Tplayer){
		if(Tplayer.guild == null){return;}
		//handle guild buff
		Charstuff.getInstance().guildbuffs.put(Tplayer.guild.GuildID, this.stones.get(mobid));
		
		//execute buff on guild and its alliances, i should just add a check for guildbuff it in guildrefresh :D
		Iterator<Map.Entry<Integer, Integer>> iter = Tplayer.guild.guildids.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<Integer, Integer> pairs = iter.next();
			Character tmpz = wmap.getCharacter(pairs.getValue()); //get character by charid from guildids
			if(tmpz != null){
			tmpz.guild.RefreshOnlyForMe(tmpz);
			}
		}
		
		//get data
		String stonename = null;
		if(mobid == 29501){stonename = "Yeokcheon"; this.Yeokcheon = true;}
		if(mobid == 29502){stonename = "Geumgang"; this.Geumgang = true;}
		if(mobid == 29503){stonename = "Myeongchen"; this.Myeongchen = true;}
		if(mobid == 29504){stonename = "Ilwui"; this.Ilwui = true;}
		if(mobid == 29505){stonename = "Hyeoncheon"; this.Hyeoncheon = true;}
		if(mobid == 29506){stonename = "Cheonra"; this.Cheonra = true;}
		
		String faction = null;
		if(Tplayer.getFaction() == 1){faction = "Lawful";}
		if(Tplayer.getFaction() == 2){faction = "Evil";}
		if(Tplayer.getFaction() == 0){faction = "Neutral";}
		
		String guildrank = null;
		int gr = Tplayer.guild.getguildranks(BitTools.ValueToKey(Tplayer.getCharID(), Tplayer.guild.guildids));
		if(gr == 3){guildrank = "Member";}
		if(gr == 4){guildrank = "TA Master";}
		if(gr == 5){guildrank = "Senior Master";}
		if(gr == 6){guildrank = "Sub-Master";}
		if(gr == 7){guildrank = "Master";}
		
		
		//announce stonedeath by who player
		this.announce(faction+" Faction type "+Tplayer.guild.guildname+" guild "+guildrank+" "+Tplayer.getLOGsetName()+" has destroyed "+stonename+" stone.");
	
		// check if all stones died then end war you fucking racist.
		if(this.Yeokcheon == true && this.Geumgang == true && this.Myeongchen == true && this.Ilwui == true && this.Hyeoncheon == true && this.Cheonra == true){this.Endguildwar();}
	}
	
	//announce
	public void announce(String one){
				byte[] gmsg = new byte[14+one.length()];
				byte[] msg = one.getBytes(); // <--- real gm msg lol
				gmsg[0] = (byte)gmsg.length;
				gmsg[4] = (byte)0x03;
				gmsg[6] = (byte)0x50;
				gmsg[7] = (byte)0xc3;
				gmsg[8] = (byte)0x01;
				gmsg[9] = (byte)one.length();
				for(int i=0;i<msg.length;i++) {
					gmsg[i+13] = msg[i];
				}
				Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
					Map.Entry<Integer, Character> pairs = iter.next();
					tmp = pairs.getValue();
					ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
				}
	}
	
	//spawn mob stone
	public void spawnmobstone(int mobid, int count, int spawnradius, int x, int y, int map){	
		int pool;
		int []data = new int[]{0,0,0,0,0,0,0};
		inc++;
		pool = ConfigurationManager.getConf("world").getIntVar("mobUIDPool") + inc;
		data[0] = map;
		data[1] = x;
		data[2] = y;
		data[3] = spawnradius;
		data[4] = 0;
		data[5] = 0;
		data[6] = 10;
		MobController run = new MobController(mobid, count, pool, data);
		pool += count;
	}
	
	
}
