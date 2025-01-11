package chat.chatCommandHandlers;


import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;



public class masskickplayer implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling massKick command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		
				//System.out.println("Param[" + (i+1) + "] is of integer type length lower then 17!");
				//System.out.println(" ===>Kicking Player: " + CharName );
				
				
				
				//---------masskick player----------\\

				Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				while(iter.hasNext()) {
					Map.Entry<Integer, Character> pairs = iter.next();
					tmp = pairs.getValue();
					tmp.leaveGameWorld();
					 ServerFacade.getInstance().finalizeConnection(tmp.GetChannel()); 
				    //System.out.println(" ===>Kicked played: " + tmp.getLOGsetName() );
				
			}
		
		

		
	}
}}

	