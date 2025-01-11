package chat.chatCommandHandlers;


import java.sql.ResultSet;

import Player.Character;
import Player.Charstuff;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;

import chat.ChatCommandExecutor;



public class unbanaccount implements ChatCommandExecutor {
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling unban account command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
		Character getplrcon = ((PlayerConnection)source).getActiveCharacter();
		
				String charname = (parameters[0]);
				int Accountid = 0;
				boolean fuck = false;
        		try{
        			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getCharacterByName(SQLconnection.getInstance().getaConnection(), charname));
        			if(rs != null){
        			if(rs.next()){
        			fuck = true;
        			Accountid = rs.getInt("ownerID");
        			}else{Charstuff.getInstance().respond("charname name does not exist!",source); return;}
        			}else{Charstuff.getInstance().respond("charname name does not exist!",source); return;}
        		    }catch (Exception e) {
        				 System.out.println(e.getMessage());
        			}

				if(fuck){
				String banreason = "unbanned by "+getplrcon.getLOGsetName();
				//---------unban player---------\\
				CharacterDAO.putbanlolbyAcntid(0, banreason,0, Accountid);
				Charstuff.getInstance().respondguild("Player with the charname "+charname+" unbanned.", getplrcon.GetChannel());
				CharacterDAO.setconsole(System.currentTimeMillis(), "Player with the charname "+charname+" unbanned by "+getplrcon.getLOGsetName() , "Server");
				}else{
				Charstuff.getInstance().respondguild(charname+" does not exist.", getplrcon.GetChannel());	
				}
		}
}


	