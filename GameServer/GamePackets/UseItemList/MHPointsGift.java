package GameServer.GamePackets.UseItemList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Player.lookuplevel;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class MHPointsGift implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		Player curplayer = ((PlayerConnection)source).getPlayer();
		
		
		int newmhpoints = -1;
		
		
		try {
		ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getmhp(SQLconnection.getInstance().getaConnection(), curplayer.getUsername()));
			while(rs.next()){
				newmhpoints = rs.getInt("mhpoints");
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
		
		
		
		if(newmhpoints == -1){return false;}
		if(ItemID == 283000020){newmhpoints = newmhpoints + 1000;}
		if(ItemID == 283000021){newmhpoints = newmhpoints + 500;}
		if(ItemID == 283000022){newmhpoints = newmhpoints + 100;}
		
		curplayer.setmhpoints(newmhpoints);
		CharacterDAO.setmhpoints(newmhpoints, curplayer.getAccountID());
		// add mh points
		byte[] mhp = BitTools.intToByteArray(newmhpoints);
		byte[] MHP = new byte[12];
		MHP[0] = (byte)0x0c;
		MHP[4] = (byte)0x03;
		MHP[6] = (byte)0x08;
		for(int i=0;i<4;i++){MHP[8+i] = mhp[i];}
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), MHP);
		Charstuff.getInstance().respond("Added a new total of "+newmhpoints+" MH Points!" ,source);
		return true;
	}
}