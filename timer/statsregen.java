package timer;

import java.util.TimerTask;
import Connections.Connection;
import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;


public class statsregen extends TimerTask{
	private Connection con;
	
	public statsregen(Connection con){
		this.con = con;
	}

	@Override
	public void run() {
		if(con.getChan().isOpen()) {
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur != null){
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] healpckt = new byte[32];
		healpckt[0] = (byte)healpckt.length;
		healpckt[4] = (byte)0x05;
		healpckt[6] = (byte)0x35;
		healpckt[8] = (byte)0x08; 
		healpckt[9] = (byte)0x60; 
		healpckt[10] = (byte)0x22;
		healpckt[11] = (byte)0x45;
		for(int i=0;i<4;i++) {
			healpckt[12+i] = chid[i]; 
		}
		
		double oldhp = cur.getMaxHp() * 0.030; // 2.50% 
		double oldmana = cur.getMAXMana() * 0.030; // 2.50% 
		double oldstam = cur.getMaxstamina() * 0.030; // 2.50% 
		int setcurrenthp = cur.getHp()+(int)oldhp;
		int setcurrentmana = cur.getMana()+(int)oldmana;
		int setcurrentstamina = cur.getStamina()+(int)oldstam;
		cur.setHp(setcurrenthp);
		cur.setMana(setcurrentmana);
		cur.setStamina(setcurrentstamina);	
		
		byte[] hp = BitTools.shortToByteArray((short)setcurrenthp);
		byte[] mana = BitTools.shortToByteArray((short)setcurrentmana);
		byte[] stam = BitTools.shortToByteArray((short)setcurrentstamina);
		
		healpckt[24] = hp[0];
		healpckt[25] = hp[1];
		healpckt[28] = mana[0];
		healpckt[29] = mana[1];
		healpckt[30] = stam[0];
		healpckt[31] = stam[1];			
		healpckt[16] = (byte)0x03;
		healpckt[18] = (byte)0x02;
	    ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), healpckt); 	
	   }}else{this.cancel();}
	}
}
