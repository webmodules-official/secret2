package chat.chatCommandHandlers;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import Database.Queries;
import Database.SQLconnection;
import chat.ChatCommandExecutor;

public class endwar implements ChatCommandExecutor {
	private WMap wmap = WMap.getInstance();

	public void execute(String[] parameters, Connection con) {
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		boolean Valid = false;
		String finalnameparsed = parameters[0];
	    int TguildUID = 0;
	    
		// check first if guild exist
		try{
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getguildbyname(SQLconnection.getInstance().getaConnection(), finalnameparsed));
			if(rs != null){
			if(rs.next()){
			if(rs.getString("name").equals(finalnameparsed)){Valid = true; TguildUID = rs.getInt("GuildID");
			}else{if(cur.guild.War.containsKey(finalnameparsed)){Valid = true;}else{Charstuff.getInstance().respond("Guild does not exist.",con); return;}}
			}else{if(cur.guild.War.containsKey(finalnameparsed)){Valid = true;}else{Charstuff.getInstance().respond("Guild does not exist.",con); return;}}
			}else{if(cur.guild.War.containsKey(finalnameparsed)){Valid = true;}else{Charstuff.getInstance().respond("Guild does not exist.",con); return;}}
		    }catch (Exception e) {
				 System.out.println(e.getMessage());
			}
		
		
        int Key = BitTools.ValueToKey(cur.getCharID(), cur.guild.guildids); 
		if(cur.guild.getguildranks(Key) != 7){Charstuff.getInstance().respondguild("Only the guildmaster can end war.", cur.GetChannel());return;}
 
		
		if(cur.guild.War.containsKey(finalnameparsed) && TguildUID == 0){
			//put us
			cur.guild.War.remove(finalnameparsed);
			
			//lose 200g 
			int ngg = cur.guild.guildgold - 200;
			if(ngg < 0){return;}
			cur.guild.guildgold = ngg;
			
			//refresh for me
			cur.guild.RefreshOnlyForMe(cur);
			Charstuff.getInstance().respond("This guild does not exist, removed from list.",con);
			return;
		}else{
		
		if(TguildUID <= 0){return;}
		if(Valid == false){return;}
		
		//break war and lose 200guildgold
		if(cur.guild != null){
			if(!wmap.Guild.containsKey(TguildUID)){
				Charstuff.getInstance().respond("None of the "+finalnameparsed+" guildmembers has been online today.",con);
				return;
			}
			if(!cur.guild.War.containsKey(finalnameparsed)){
				Charstuff.getInstance().respond("This guild is not on the list.",con);
				return;
			}
			String FinalLine = cur.guild.getguildname()+" has end the war with "+finalnameparsed;
			byte[] gmsg = new byte[14+FinalLine.length()];
			byte[] msg = FinalLine.getBytes(); // <--- real gm msg lol
			gmsg[0] = (byte)gmsg.length;
			gmsg[4] = (byte)0x03;
			gmsg[6] = (byte)0x50;
			gmsg[7] = (byte)0xc3;
			gmsg[8] = (byte)0x01;
			gmsg[9] = (byte)FinalLine.length();
			
			//put us
			cur.guild.War.remove(finalnameparsed);
			
			//put them
			wmap.getGuild(TguildUID).War.remove(cur.guild.guildname);
			
			//lose 200g 
			int ngg = cur.guild.guildgold - 200;
			if(ngg < 0){return;}
			cur.guild.guildgold = ngg;
			
			//refresh for me
			cur.guild.RefreshOnlyForMe(cur);
			
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
		}
		
	}

}
