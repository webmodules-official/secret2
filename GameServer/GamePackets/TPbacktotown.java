package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import Player.Character;
import Player.PlayerConnection;
import Player.exptable;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Encryption.Decryptor;

public class TPbacktotown implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling TP BACK TO TOWN ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);

		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
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
		
	/*	tpbacktotown[18] = (byte)0x15;
		tpbacktotown[19] = (byte)0x08;
		tpbacktotown[20] = (byte)0x75;
		
		tpbacktotown[24] = (byte)0x67;
		tpbacktotown[25] = (byte)0x01;*/
		
		byte[] Fame = BitTools.intToByteArray(cur.getFame());
		for(int i=0;i<4;i++) {
			tpbacktotown[36+i] = Fame[i];// exp left and put upon leveling up
		}
		
		cur.setHp(147);
		cur.setMana(143);
		cur.setStamina(142);
		
		float wootx = cur.getlastknownX();
		float wooty = cur.getlastknownY();
		
		long MAXexp = exptable.getMAXexptable(cur.getLevel());
		byte[] coordinates = new byte[8];
		
		if(decrypted[0] == 3) // If player has clicked Inmidiate Respawn.
		{
		/*byte[] remainingexp = BitTools.LongToByteArrayREVERSE(0);
		for(int i=0;i<remainingexp.length;i++) {
			tpbacktotown[28+i] = remainingexp[i];// exp left and put upon leveling up
		}*/
			byte[] xCoord = BitTools.floatToByteArray( cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray( cur.getlastknownY());
			
			for(int a=0;a<4;a++) {
				coordinates[a] = xCoord[a];
				coordinates[a+4] = yCoord[a];
			}

		}else if(cur.killedbyplayer == true){
			/*byte[] remainingexp = BitTools.LongToByteArrayREVERSE(0);
			for(int i=0;i<remainingexp.length;i++) {
				tpbacktotown[28+i] = remainingexp[i];// exp left and put upon leveling up
			}*/

			if (cur.getCurrentMap() == 1){ // v.v
			byte[] xCoord = BitTools.floatToByteArray(-1502);
			byte[] yCoord = BitTools.floatToByteArray(2585);
			cur.setX(-1502);
			cur.setY(2585);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
			
			if (cur.getCurrentMap() == 2){ // o.c
				byte[] xCoord = BitTools.floatToByteArray(12896);
				byte[] yCoord = BitTools.floatToByteArray(7778);
				cur.setX(12896);
				cur.setY(7778);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 3){ // death land
				if (cur.getFaction() == 2){ //evil
				byte[] xCoord = BitTools.floatToByteArray(8882);
				byte[] yCoord = BitTools.floatToByteArray(17599);
				cur.setX(8882);
				cur.setY(17599);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
				}else {  // lawful
					byte[] xCoord = BitTools.floatToByteArray(1527);
					byte[] yCoord = BitTools.floatToByteArray(16568);
					cur.setX(1527);
					cur.setY(16568);
					for(int i=0;i<4;i++) {
						coordinates[i] = xCoord[i];
						coordinates[i+4] = yCoord[i];
					}
				}
			}
			
			if (cur.getCurrentMap() == 4){ // tibet
				byte[] xCoord = BitTools.floatToByteArray(42477);
				byte[] yCoord = BitTools.floatToByteArray(45812);
				cur.setX(42477);
				cur.setY(45812);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 7){ // rc
				byte[] xCoord = BitTools.floatToByteArray(39165);
				byte[] yCoord = BitTools.floatToByteArray(56462);
				cur.setX(39165);
				cur.setY(56462);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 8){ // mid c
				byte[] xCoord = BitTools.floatToByteArray(23600);
				byte[] yCoord = BitTools.floatToByteArray(64981);
				cur.setX(23600);
				cur.setY(64981);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 9){ // Sky zone
				byte[] xCoord = BitTools.floatToByteArray(15730);
				byte[] yCoord = BitTools.floatToByteArray(62100);
				cur.setX(15730);
				cur.setY(62100);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 100){ // Casino
				byte[] xCoord = BitTools.floatToByteArray(24068);
				byte[] yCoord = BitTools.floatToByteArray(56132);
				cur.setX(24068);
				cur.setY(56132);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 10){ // 144+ map
				byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
				byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
				
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 11){ // 144+ cave
				byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
				byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
				
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 204){ // at 1
				byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
				byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
				
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 205){ // at 2
				byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
				byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
				
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 5){ // at 3
				byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
				byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
				
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
			
			if (cur.getCurrentMap() == 300){ // revenge battle
				byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
				byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
				
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}}
		}else{
		double num = MAXexp * .02; // is 2.00% lost
		long remexp = cur.getexp() - (long)num; // current exp - 2.50%(from MAXEXP) = new exp
		cur.setexp(remexp); // save it
		/*byte[] remainingexp = BitTools.LongToByteArrayREVERSE(0);
		for(int i=0;i<remainingexp.length;i++) {
			tpbacktotown[28+i] = remainingexp[i];// exp left and put upon leveling up
		}*/
		
		/*tpbacktotown[41] = (byte)0x00; // x
		tpbacktotown[42] = (byte)0x00;
		tpbacktotown[43] = (byte)0x00;
		
		tpbacktotown[45] = (byte)0x00; // y
		tpbacktotown[46] = (byte)0x00;
		tpbacktotown[47] = (byte)0x00;*/
		

	
		byte[] xCoordqq = BitTools.floatToByteArray(wootx);
		byte[] yCoordqq = BitTools.floatToByteArray(wooty);
		cur.setX(wootx);
		cur.setY(wooty);
		for(int i=0;i<4;i++) {
			coordinates[i] = xCoordqq[i];
			coordinates[i+4] = yCoordqq[i];
		}
		
	
		
		if (cur.getCurrentMap() == 1){ // v.v
		byte[] xCoord = BitTools.floatToByteArray(-1502);
		byte[] yCoord = BitTools.floatToByteArray(2585);
		cur.setX(-1502);
		cur.setY(2585);
		for(int i=0;i<4;i++) {
			coordinates[i] = xCoord[i];
			coordinates[i+4] = yCoord[i];
		}}
		
		if (cur.getCurrentMap() == 2){ // o.c
			byte[] xCoord = BitTools.floatToByteArray(12896);
			byte[] yCoord = BitTools.floatToByteArray(7778);
			cur.setX(12896);
			cur.setY(7778);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 3){ // death land
			if (cur.getFaction() == 2){ //evil
			byte[] xCoord = BitTools.floatToByteArray(8882);
			byte[] yCoord = BitTools.floatToByteArray(17599);
			cur.setX(8882);
			cur.setY(17599);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}
			}else {  // lawful
				byte[] xCoord = BitTools.floatToByteArray(1527);
				byte[] yCoord = BitTools.floatToByteArray(16568);
				cur.setX(1527);
				cur.setY(16568);
				for(int i=0;i<4;i++) {
					coordinates[i] = xCoord[i];
					coordinates[i+4] = yCoord[i];
				}
			}
		}
		
		if (cur.getCurrentMap() == 4){ // tibet
			byte[] xCoord = BitTools.floatToByteArray(42477);
			byte[] yCoord = BitTools.floatToByteArray(45812);
			cur.setX(42477);
			cur.setY(45812);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 7){ // rc
			byte[] xCoord = BitTools.floatToByteArray(39165);
			byte[] yCoord = BitTools.floatToByteArray(56462);
			cur.setX(39165);
			cur.setY(56462);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 8){ // mid c
			byte[] xCoord = BitTools.floatToByteArray(23600);
			byte[] yCoord = BitTools.floatToByteArray(64981);
			cur.setX(23600);
			cur.setY(64981);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 9){ // Sky zone
			byte[] xCoord = BitTools.floatToByteArray(15730);
			byte[] yCoord = BitTools.floatToByteArray(62100);
			cur.setX(15730);
			cur.setY(62100);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 100){ // Casino
			byte[] xCoord = BitTools.floatToByteArray(24068);
			byte[] yCoord = BitTools.floatToByteArray(56132);
			cur.setX(24068);
			cur.setY(56132);
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 10){ // 144+ map
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 11){ // 144+ cave
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 204){ // at 1
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 205){ // at 2
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 5){ // at 3
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		
		if (cur.getCurrentMap() == 300){ // revenge battle
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			
			for(int i=0;i<4;i++) {
				coordinates[i] = xCoord[i];
				coordinates[i+4] = yCoord[i];
			}}
		}
		
		if(decrypted[0] == 1 || decrypted[0] == 2) // monk revival
		{
			byte[] xCoord = BitTools.floatToByteArray(wootx);
			byte[] yCoord = BitTools.floatToByteArray(wooty);
			
			for(int a=0;a<4;a++) {
				coordinates[a] = xCoord[a];
				coordinates[a+4] = yCoord[a];
			}

		}
		
		
		for(int i=0;i<coordinates.length;i++) {
			tpbacktotown[i+(48-coordinates.length)] = coordinates[i];
		}
		
		cur.statlist(); // refresh dat statlist when i revived
		cur.leaveGameWorld();
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), tpbacktotown); 
		

		//ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), tmp.extCharPacket());	
			
		//System.out.println(cur.getLOGsetName()+"===WTFTWO===");
		//cur.RefreshArea();
		//cur.sendToMap(cur.extCharPacket());
		cur.getPlayer().Rarea = true;	
		cur.blackslave(cur.getexp());
		cur.killedbyplayer = false;
		//System.out.println("DONE");
		return null;
	}
	
}
