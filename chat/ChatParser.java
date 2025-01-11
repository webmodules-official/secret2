package chat;

import java.util.HashMap;
import java.util.Map;
import Connections.Connection;
import chat.chatCommandHandlers.*;




public class ChatParser {

	private static volatile ChatParser instance = null;  // admin 
	private static volatile ChatParserz instancez = null; // gamemaster
	private static volatile ChatParserzgmh instancezgmh = null; // GMH@
	private static volatile ChatParserzplayer instancezplayer= null; // PLAYER COMMANDS
	private static volatile ChatParserzVINCE instancezVINCE= null; // VINCE COMMANDS
	private final String cmdDelimiter = ",";
	private final String paramDelimiter = ":";
	private Map<String, ChatCommandExecutor> commandList = new HashMap<String, ChatCommandExecutor>();
	
	
	private ChatParser() { // Administrator \\ and DEV <---?????????
		//check mass players
		this.commandList.put("scan", new getallchars()); 
		this.commandList.put("playcasino", new playcasino()); 
		
		this.commandList.put("guildwar", new guildwars()); // test shit
		
		//get anyplaterchar
		this.commandList.put("banip", new banip()); // perm ban player + account ip by ,banip:charname
		this.commandList.put("banplayer", new banplayer()); // perm ban by ,banplayer:charname:days:banreason
		this.commandList.put("unban", new unbanaccount()); // unban acccount by ,unbanaccount:charname
		this.commandList.put("show", new showinv()); // shows player inv, cargo, friends, skills, skillbar ,show:charname
		//when using show command u get visual of other player.
		//dont do anything with it or else you will fuck your own inventory up.
		//Its only a visual!!!
		
		this.commandList.put("clean", new clean()); // back to your normal self inv, cargo, friends, skills, skillbar ,clean:1
		this.commandList.put("shout", new shoutcommand()); // shout aka world chat ,shout:message
		this.commandList.put("kickplayer", new kickplayer()); // kick player ,kickplayer:charname
		this.commandList.put("endwar", new endwar());  // end war ,endwar:guildname
		this.commandList.put("playerinfo", new playerinfo()); // get player info by ,playerinfo:charname
		
		//dick pic
		this.commandList.put("endwar", new endwar());  // show commands
		this.commandList.put("refresharea", new refresharea());  // show commands
		this.commandList.put("revive", new revive()); // test shit
		
		this.commandList.put("stealth", new CopyOfrevive());  // show commands
		
		this.commandList.put("ri", new removeitem());  // show commands
		
		// General
		this.commandList.put("startprof", new startprof());  // show commands
		
		this.commandList.put("commands", new commands());  // show commands
		this.commandList.put("additem", new ItemSpawner());  // spawn item 
		this.commandList.put("massitemspawn", new massitemspawner()); // spawn item to every player in world map
		this.commandList.put("heal", new HealCommand()); // set current hp mana stamto ?:?:?
		this.commandList.put("shout", new shoutcommand()); // shout aka world chat
		this.commandList.put("whisper", new whisperchatcommand()); // whisper a player
		this.commandList.put("servernews", new servernews()); // server news
		this.commandList.put("shutdown", new servershutdown()); // servershutdown
		this.commandList.put("exit", new servershutdown()); // servershutdown
		this.commandList.put("restart", new servershutdown()); // servershutdown
		this.commandList.put("serverrestart", new serverrestart()); // serverrestart
		this.commandList.put("setplayerGM", new setplayergmlevel()); // setplayergmlevel by name: string value
		this.commandList.put("setplayerface", new setplayerface()); // setplayerface by name: int value
		this.commandList.put("setplayerlevel", new setplayerlevel()); // setplayerlevel by name: int value
		this.commandList.put("setplayerfaction", new setplayerfaction()); // setplayerfaction by name: int value
		this.commandList.put("setplayerclass", new setplayerclass()); // setplayerclass by name: int value
		this.commandList.put("setplayerfame", new setplayerfame()); // setplayerclass by name: int value
		this.commandList.put("setplayername", new changeplayername()); // change any player name to ?
		this.commandList.put("fame", new famecomamnd()); // set fame
		this.commandList.put("regen", new regencommand()); // set current hp mana stamto ?:?:?
		this.commandList.put("getstatsfromdb", new getheal()); // get character stats from db (such as current hp mana etc )
		this.commandList.put("quit", new quitcommand()); // force quitgame (usefull for when stuck -> like holding item etc)
		this.commandList.put("test", new test()); // test shit
		this.commandList.put("exp", new exp());
		this.commandList.put("icon", new icons()); 
		this.commandList.put("mobspawn", new mobspawn()); 
		this.commandList.put("gold", new gold()); 
		this.commandList.put("npcspawn", new NPC()); 
		this.commandList.put("showstats", new teststats()); // shows char stats in console
		this.commandList.put("porttele", new porttele()); // teleport your character to X,Y,MAP
		this.commandList.put("portplayer", new portplayer()); // teleport ANY PLAYER to X,Y,MAP
		this.commandList.put("appear", new appear()); // appear to player by name
	//	this.commandList.put("summon", new summon()); // summons a player by name
		this.commandList.put("masssummon", new masssummon()); // summons every player in the world map
		this.commandList.put("kickplayer", new kickplayer()); // kick player name 
		this.commandList.put("banip", new banip()); // perm ban player + account ip by player name
		this.commandList.put("masskickplayer", new masskickplayer()); // kick everyones in the server ( ultimate troll)
		this.commandList.put("banplayer", new banplayer()); // perm ban player name
		this.commandList.put("unbanaccount", new unbanaccount()); // unban acccount by username
		this.commandList.put("massbanplayer", new massbanplayer()); // ban everyones in the server ( ultimate troll)
		
		this.commandList.put("time", new time()); // time in console
		this.commandList.put("playernames", new playernames()); 
		this.commandList.put("debug", new ThreadDebug());
		
		// Announce
		this.commandList.put("a", new GMChat()); //  gm announce
		this.commandList.put("greena", new greenannounce()); // green announce 
		this.commandList.put("reda", new redannounce()); // red announce
//FAIL->this.commandList.put("reda2", new redannounce2()); // red announce
//FAIL->this.commandList.put("gzeow", new goldannounce()); // gold announce
	
		
		// Teleport
		this.commandList.put("playerinfo", new playerinfo()); // get player info by name
		
		// Character Modify
		this.commandList.put("setplayermodel", new modelidcommand()); // change modelid for any player
		this.commandList.put("model", new modelidself()); // change modelid for yourself
		
		// Booth
		this.commandList.put("booth", new boothspawncommand()); // spawns booth :String  booth-name
		
		// Guild
		this.commandList.put("guildchat", new guildchatcommand()); // guildchat as shout 
		this.commandList.put("guildcreate", new createguild()); // create guild : String guildname
		this.commandList.put("guildkick", new guildkick()); //  guildkick
		
		// Party
		this.commandList.put("partychat", new partychatcommand()); // partychat as shout 
		
		// Skills
		this.commandList.put("castbuff", new castbuff()); // castspell : id ?
		this.commandList.put("castspeed", new castspeed()); // castspeed : 1/2/3/4
		
		//Pvp
		this.commandList.put("flagred", new flagred()); // flag the player red by name
		
		// TEST
		
		this.commandList.put("level", new deathstate()); 
		this.commandList.put("pot", new hp()); 
		this.commandList.put("fury", new furybar()); 
		this.commandList.put("furyoff", new furyoff()); 
		
		
	}
			// GM commands is in Chatparserz.java
	
	public static synchronized ChatParser getInstance(){
		if (instance == null){
			instance = new ChatParser();
		}
		return instance;
	}
	 // gamemaster \\
	public static synchronized ChatParserz getInstance1(){
		if (instancez == null){
			instancez = new ChatParserz();
		}
		return instancez;
	}
	  // GMH@ \\
	public static synchronized ChatParserzgmh getInstancegmh(){
		if (instancezgmh == null){
			instancezgmh = new ChatParserzgmh();
		}
		return instancezgmh;
	}
	
	  // PLAYER COMMANDS \\
	public static synchronized ChatParserzplayer getInstanceplayer(){
		if (instancezplayer == null){
			instancezplayer = new ChatParserzplayer();
		}
		return instancezplayer;
	}
	
	public static synchronized ChatParserzVINCE getInstanceVINCE(){
		if (instancezVINCE == null){
			instancezVINCE = new ChatParserzVINCE();
		}
		return instancezVINCE;
	}
	
	public String getCommandDelimiter() {
		return  cmdDelimiter;
	}
	
	public String getParameterDelimiter() {
		return this.paramDelimiter;
	}

	public Map<String, ChatCommandExecutor> getCommandList() {
		return commandList;
	}

	public boolean parseAndExecuteChatCommand(String msg, Connection con) {
		////System.out.println("Parsing a chat command: " + msg);
		String[] commands = msg.split(this.cmdDelimiter);
		for(int i=0;i<commands.length;i++) {
			String[] splat = commands[i].split(paramDelimiter);
			if(this.commandList.containsKey(splat[0])) {
				String[] params = new String[splat.length-1];
				for(int ri=1;ri<splat.length;ri++) {
					params[ri-1] = splat[ri]; 
				}
				this.commandList.get(splat[0]).execute(params, con);
			}
		}
		return true;
	}
	
}
