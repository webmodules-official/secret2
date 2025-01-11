package GameServer.GamePackets;

import java.nio.ByteBuffer;
import Player.Character;
import Player.Charstuff;
import Player.Party;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;

import Encryption.Decryptor;

public class duel implements Packet {
	 private WMap wmap = WMap.getInstance();
	 private static int inc = 0;
	 
	 
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling PARTY GOOGOG");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) {System.out.print(i+":"+decrypted[i]+" ");}
		//System.out.println("");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] newP = new byte[4];
		byte[] fury = new byte[64];
		fury[0] = (byte)0x40;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x2a;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		fury[12+i] = chid[i]; // charid
		newP[i] = decrypted[4+i];
		}
		fury[16] = (byte)0x01;
		fury[18] = decrypted[0]; // 0x00 = null | 0x01 = accepted | 0x02 is requesting party
		fury[19] = (byte)0x08;
	
		Character TPlayer = wmap.getCharacter(BitTools.byteArrayToInt(newP));
		byte[] TPchid = BitTools.intToByteArray(TPlayer.getCharID());
		
	
		if(decrypted[0] == 0){ // if decline
			return null;
		}
		
		if(decrypted[0] == 2){ // sent request	
			for(int i=0;i<4;i++) {
				fury[20+i] = chid[i]; 
				fury[60+i] = chid[i]; 
			}
			ServerFacade.getInstance().addWriteByChannel(TPlayer.GetChannel(), fury);
		}
		
		if(decrypted[0] == 1){ // if accept
			//PERFECT WAY TO KNOW WHATS WHAT !
			//for(int i=0;i<decrypted.length;i++) {System.out.print((int)decrypted[i]+" ");}
			//System.out.println("");
			
			byte[] fury1 = new byte[64];
			fury1[0] = (byte)0x40;
			fury1[4] = (byte)0x04;
			fury1[6] = (byte)0x2a;
			fury1[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
			fury1[12+i] = TPchid[i];
			}
			fury1[16] = (byte)0x01;
			fury1[18] = (byte)0x03; 
			fury1[19] = (byte)0x08;
			fury1[24] = (byte)0x01;
			fury1[36] = (byte)0x01;
			
			fury[18] = (byte)0x03;
			fury[24] = (byte)0x01;
			fury[36] = (byte)0x01;
			
			byte[] xCoord = BitTools.floatToByteArray(cur.getlastknownX());
			byte[] yCoord = BitTools.floatToByteArray(cur.getlastknownY());
			byte[] TPxCoord = BitTools.floatToByteArray(TPlayer.getlastknownX());
			byte[] TPyCoord = BitTools.floatToByteArray(TPlayer.getlastknownY());
			
			byte[] TOPX = BitTools.floatToByteArray(-1779);
			byte[] TOPY = BitTools.floatToByteArray(2336);
			byte[] BOTX = BitTools.floatToByteArray(-1337);
			byte[] BOTY = BitTools.floatToByteArray(2772);
			

			
			for(int i=0;i<4;i++) {
				fury[28+i] = xCoord[i];
				fury[32+i] = yCoord[i];
				
			    fury[40+i] = TOPX[i];
				fury[44+i] = TOPY[i];
				fury[48+i] = BOTX[i];
				fury[52+i] = BOTY[i];
				
				fury1[28+i] = TPxCoord[i];
				fury1[32+i] = TPyCoord[i];
				
			    fury1[40+i] = TOPX[i];
				fury1[44+i] = TOPY[i];
				fury1[48+i] = BOTX[i];
				fury1[52+i] = BOTY[i];
			}
			
			for(int i=0;i<4;i++) {
				fury[20+i] = chid[i]; 
				fury[24+i] = TPchid[i]; 
				fury[56+i] = chid[i]; 
				fury[60+i] = TPchid[i]; 
				
				fury1[20+i] = TPchid[i]; 
				fury1[24+i] = chid[i]; 
				fury1[56+i] = TPchid[i]; 
				fury1[60+i] = chid[i];
			}
			cur.Duel = 1;
			TPlayer.Duel = 1;
			ServerFacade.getInstance().addWriteByChannel(TPlayer.GetChannel(), fury1);
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);	
		}
		return null;
	}	
}
