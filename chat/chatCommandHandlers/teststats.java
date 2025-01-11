package chat.chatCommandHandlers;


import Connections.Connection;
import Player.Character;
import Player.PlayerConnection;
import chat.ChatCommandExecutor;



public class teststats implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling test command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
		
		Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
		int currenthp = currenthcamp.getHp();
		//System.out.println("Current hp:" + currenthp);
		int currentmana = currenthcamp.getMana();
		//System.out.println("Current mana:" + currentmana);
		int currentstamina = currenthcamp.getStamina();
		//System.out.println("Current stamina:" + currentstamina);
	}
}

	