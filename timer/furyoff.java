package timer;

import java.util.TimerTask;
import Connections.Connection;
import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;


public class furyoff extends TimerTask{
	private Connection con;
	
	public furyoff(Connection con){
		this.con = con;
	}

	@Override
	public void run() {
		if(con.getChan().isOpen()) {
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur != null){
		cur.furycheck = 0;
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[20];
		fury[0] = (byte)fury.length;
		fury[4] = (byte)0x05;
		fury[6] = (byte)0x6a;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		fury[16] = (byte)0x00;
		cur.sendToMap(fury);
	    ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 	
	   }else{this.cancel();}}
	}
}
