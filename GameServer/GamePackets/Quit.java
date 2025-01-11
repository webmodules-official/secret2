package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.logging.Level;

import logging.ServerLogger;

import Connections.Connection;
import Database.CharacterDAO;
import Player.Character;
import Player.Player;
import Player.PlayerConnection;
import World.WMap;


public class Quit implements Packet {
	private WMap wmap = WMap.getInstance();
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}

	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		Character ch = ((PlayerConnection)con).getActiveCharacter(); 
        if(ch != null) {
  		  if(wmap.Trade.containsKey(ch.TradeUID)){
  			  wmap.Trade.get(ch.TradeUID).CancelTrade();
  		  }
        	ch.killedbyplayer = false;
        	ch.TempPassives.clear();
        	ch.BuffPercentage.clear();
    		ch.DotsIconID.clear();
    		ch.DotsValue.clear();
    		ch.DotsTime.clear();
    		ch.DotsSLOT.clear();
    		ch.BoothStance = 0;
    		ch.TradeUID = 0;
    		WMap.getInstance().vendinglist.remove(ch.getCharID());
          	ch.savecharacter();
        	ch.leaveGameWorld();
        	ch.getPl().setActiveCharacter(null);
        }
		 con.getWriteBuffer().clear();
		final byte[] quit = new byte[] {(byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x64, (byte)0x00, (byte)0x00};
		return quit;
	}
	
}
