package GameServer.GamePackets.UseItemList;

import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class RevivalAmulet implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] tpbacktotown = new byte[48];
		
		tpbacktotown[0] = (byte)0x30;
		tpbacktotown[4] = (byte)0x04;
		tpbacktotown[6] = (byte)0x03;
		tpbacktotown[8] = (byte)0x01;
		
		tpbacktotown[9] = (byte)0x9e;
		tpbacktotown[10] = (byte)0x0f;
		tpbacktotown[11] = (byte)0xbf;
		
		for(int i=0;i<4;i++) {
			tpbacktotown[12+i] = chid[i]; //charID , aka this[12] <-----------------
		}
		tpbacktotown[16] = (byte)0x01;
		
		/*tpbacktotown[18] = (byte)0x15;
		tpbacktotown[19] = (byte)0x08;
		tpbacktotown[20] = (byte)0x75;
		
		tpbacktotown[24] = (byte)0x67;
		tpbacktotown[25] = (byte)0x01;*/
		
		byte[] Fame = BitTools.intToByteArray(cur.getFame());
		for(int i=0;i<4;i++) {
			tpbacktotown[36+i] = Fame[i];// exp left and put upon leveling up
		}
		
		cur.setHp(cur.maxhp);
		cur.setMana(cur.maxmana);
		cur.setStamina(cur.maxstamina);
		
		byte[] coordinates = new byte[8];
		/*byte[] remainingexp = BitTools.LongToByteArrayREVERSE(0);
		for(int i=0;i<remainingexp.length;i++) {
			tpbacktotown[28+i] = remainingexp[i];// exp left and put upon leveling up
		}*/
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}
		
		
		for(int i=0;i<coordinates.length;i++) {
			tpbacktotown[i+(48-coordinates.length)] = coordinates[i];
		}
		
		cur.statlist(); // refresh dat statlist when i revived
		cur.leaveGameWorld();
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), tpbacktotown); 
		cur.getPlayer().Rarea = true;
		cur.blackslave(cur.getexp());
		cur.killedbyplayer = false;
		return true;
	}
}