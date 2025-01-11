package chat;

import java.util.HashMap;
import java.util.Map;

import chat.chatCommandHandlers.CopyOfrevive;
import chat.chatCommandHandlers.GMChat;
import chat.chatCommandHandlers.HealCommand;
import chat.chatCommandHandlers.ItemSpawner;
import chat.chatCommandHandlers.appear;
import chat.chatCommandHandlers.banip;
import chat.chatCommandHandlers.banplayer;
import chat.chatCommandHandlers.clean;
import chat.chatCommandHandlers.commands;
import chat.chatCommandHandlers.createguild;
import chat.chatCommandHandlers.endwar;
import chat.chatCommandHandlers.kickplayer;
import chat.chatCommandHandlers.playerinfo;
import chat.chatCommandHandlers.playernames;
import chat.chatCommandHandlers.potspawn;
import chat.chatCommandHandlers.quitcommand;
import chat.chatCommandHandlers.shoutcommand;
import chat.chatCommandHandlers.showinv;
import chat.chatCommandHandlers.unbanaccount;

import Connections.Connection;



public class ChatParserzVINCE {
	
	private final String cmdDelimiter = ",";
	private final String paramDelimiter = ":";
	private Map<String, ChatCommandExecutor> commandList = new HashMap<String, ChatCommandExecutor>();
	
	
	

	ChatParserzVINCE() { //  VINCE \\
		this.commandList.put("banip", new banip()); // perm ban player + account ip by ,banip:charname
		this.commandList.put("banplayer", new banplayer()); // perm ban by ,banplayer:charname:days:banreason
		this.commandList.put("unban", new unbanaccount()); // unban acccount by ,unban:charname
		this.commandList.put("show", new showinv()); // shows player inv, cargo, friends, skills, skillbar ,show:charname
		//when using show command u get visual of other player.
		//dont do anything with it or else you will fuck your own inventory up.
		//Its only a visual!!!
		
		this.commandList.put("clean", new clean()); // back to your normal self inv, cargo, friends, skills, skillbar ,clean:1
		this.commandList.put("shout", new shoutcommand()); // shout aka world chat ,shout:message
		this.commandList.put("kickplayer", new kickplayer()); // kick player ,kickplayer:charname
		this.commandList.put("endwar", new endwar());  // end war ,endwar:guildname
		this.commandList.put("playerinfo", new playerinfo()); // get player info by ,playerinfo:charname
		
		this.commandList.put("a", new GMChat()); //  gm announce
		this.commandList.put("playerinfo", new playerinfo()); // get player info by name
		this.commandList.put("appear", new appear()); // appear to player by name
		//this.commandList.put("summon", new summon()); // summons a player by name
		this.commandList.put("kickplayer", new kickplayer()); // kick player name 
		this.commandList.put("banip", new banip()); // perm ban player + account ip by player name
		this.commandList.put("banplayer", new banplayer()); // perm ban by player name
		this.commandList.put("unbanaccount", new unbanaccount()); // unban acccount by username
		this.commandList.put("playernames", new playernames()); 
		this.commandList.put("stealth", new CopyOfrevive());  // show commands
		this.commandList.put("endwar", new endwar());  // show commands
		this.commandList.put("show", new showinv()); // test shit
	}
	
	

	public boolean parseAndExecuteChatCommand(String msg, Connection con) {
		//System.out.println("Parsing a chat command: " + msg);
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