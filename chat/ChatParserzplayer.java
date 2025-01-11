package chat;

import java.util.HashMap;
import java.util.Map;

import chat.chatCommandHandlers.GMChat;
import chat.chatCommandHandlers.HealCommand;
import chat.chatCommandHandlers.commands;
import chat.chatCommandHandlers.createguild;
import chat.chatCommandHandlers.endwar;
import chat.chatCommandHandlers.potspawn;
import chat.chatCommandHandlers.quitcommand;
import chat.chatCommandHandlers.refresharea;

import Connections.Connection;



public class ChatParserzplayer {
	
	private final String cmdDelimiter = ",";
	private final String paramDelimiter = ":";
	private Map<String, ChatCommandExecutor> commandList = new HashMap<String, ChatCommandExecutor>();
	
	
	

	ChatParserzplayer() { 
		this.commandList.put("endwar", new endwar());  // show commands
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