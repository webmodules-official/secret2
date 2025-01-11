package chat.chatCommandHandlers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.channels.SocketChannel;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Connections.Connection;
import Database.StuffsDAO;
import GameServer.StartGameserver;
import Player.Character;
import Player.Charstuff;
import Player.Guild;
import Player.Player;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import World.WMap;
import chat.ChatCommandExecutor;

public class servershutdown implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		Character curd = ((PlayerConnection)source).getActiveCharacter();
		Charstuff.getInstance().respondguild("Shutting Down!", curd.GetChannel());
		// Put Realm Offline
		try {
			PrintWriter output = new PrintWriter("realmstatus.txt");
			output.print("0");
			output.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character cur;
			try {
			while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			cur = pairs.getValue();
			if(cur != null){
		    			cur.leaveGameWorld();
		    			cur.savecharacter();
						String Name = cur.getLOGsetName();
						
						 //if character connection is connected then DC him
						 if(ServerFacade.getInstance().getCon().isChannelRegistered(cur.GetChannel())){
									Connection tmp = ServerFacade.getInstance().getCon().getConnection(cur.GetChannel());
									if(tmp.isPlayerConnection()){
										PlayerConnection tmplc = (PlayerConnection) tmp;
										tmplc.getWriteBuffer().clear();
										tmplc.setActiveCharacter(null);
										tmplc.threadSafeDisconnect();
										System.out.println("Character Disconnected: "+Name);
									}	 
						 }			
		    		}
		    	}
			}catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
			}
				
    			Iterator<Entry<Integer, Guild>> iterwa = WMap.getInstance().getGuildMap().entrySet().iterator();
    			while(iterwa.hasNext()) {
    				Entry<Integer, Guild> pairs = iterwa.next();
    				//int GuildID = pairs.getKey();
    				Guild GUILD = pairs.getValue();
    					if(GUILD != null){
    						GUILD.guildsave();
    						//System.out.println("SAVED GUILD : "+GUILD.getguildname());
    					}
    				
    			}
			
    		  	StuffsDAO.setserverstatus(0);		
    	StuffsDAO.setPOnline(0, 0, 0, 0, 0);
	  	System.exit(0);

	}
}