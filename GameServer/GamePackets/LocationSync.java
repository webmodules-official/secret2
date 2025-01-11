package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Connections.Connection;
import Player.PlayerConnection;
import Player.Character;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Encryption.Decryptor;

public class LocationSync implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}

	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		Character cur = ((PlayerConnection)con).getActiveCharacter();

		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}

		
		byte[] x = new byte[4];
		byte[] y = new byte[4];
		
		for(int i=0;i<4;i++) {
			x[i] = decrypted[7-i];
			y[i] = decrypted[11-i];
		}
		
		//System.out.println("Old X:"+cur.getlastknownX()+" Old Y:"+cur.getlastknownY());
		//System.out.println("New X:"+BitTools.byteArrayToFloat(x)+" New Y:"+BitTools.byteArrayToFloat(y));
		
		    //float sx = cur.getlastknownX();     float dx = BitTools.byteArrayToFloat(x);
			//float sy = cur.getlastknownY();     float dy = BitTools.byteArrayToFloat(y);		
		
			//float totaldistance = WMap.distance(sx,sy,dx,dy);
			//long SPHT = System.currentTimeMillis() - cur.SpeedHackTimer;
			//cur.SpeedHackTimer = System.currentTimeMillis();
			
			//long hacktime = 3300;
			//if(SPHT >)
			
		// mesure distance and time travel
		//System.out.println("Distance: "+totaldistance);
		//System.out.println("Time Travel: "+SPHT);
		
		//System.out.println(" ");
		cur.updateLocation(BitTools.byteArrayToFloat(x), BitTools.byteArrayToFloat(y));
		
		byte[] locSync = new byte[56]; 
		
		locSync[0] = (byte)locSync.length;
		locSync[4] = (byte)0x04;
		locSync[6] = (byte)0x0D;
		
		byte[] id = BitTools.intToByteArray(cur.getCharID());
		byte[] AREAID = BitTools.intToByteArray(0);
		byte externmove[] = new byte[48]; 
		
		externmove[0] = (byte)externmove.length;
		externmove[4] = (byte)0x05;
		externmove[6] = (byte)0x0D;
		
		externmove[8]  = (byte)0x01;
		
		externmove[36] = decrypted[13];
		//byte[] mahtimuna = new byte[] {(byte)0x01, (byte)0xed, (byte)0x5f, (byte)0xbf, (byte)0x00, (byte)0x00, (byte)0x80, (byte)0x3f, (byte)0x03, (byte)0x00, (byte)0x00, (byte)0x36};
		//byte[] mahtileka = new byte[] {(byte)0x13, (byte)0xad, (byte)0xbc, (byte)0x3e};
		
		//for(int i=0;i<mahtimuna.length;i++) {
		//	externmove[i+36] = mahtimuna[i];
		//}
		
		/*
		 * location sync has 2 sets of coordinates: 1st is current location, 2nd is next location sync will take place at
		 * but I'm taking a little shortcut here and just setting both as new intented location as told to us by client
		 */
		for(int i=0;i<4;i++) {
			//1st set
			locSync[16+i] = x[i];   
			locSync[20+i] = y[i]; 
			//2nd set 
			locSync[24+i] = x[i];
			locSync[28+i] = y[i];
			//character id
			locSync[i+12] = id[i];
			
			locSync[i+32] = AREAID[i];
			
			//externmove is same thing, except this time the packet is to be sent to other players nearby telling them our character moved
			externmove[i+20] = x[3-i];
			externmove[i+24] = y[3-i];			   
			externmove[i+28] = x[3-i];
			externmove[i+32] = y[3-i];			
			externmove[i+12] = id[i];
			//externmove[i+16] = mahtileka[i];
		}
		
		// Area ID
		/*locSync[32] = (byte)0xe2;
		locSync[33] = (byte)0x0b;
		locSync[34] = (byte)0x48;
		locSync[35] = (byte)0xc0;*/
		
		locSync[38] = (byte)0x80;
		locSync[39] = (byte)0x3f;
		locSync[40] = (byte)0x01;
		locSync[41] = (byte)0x03;
		locSync[42] = (byte)0x05;
		locSync[43] = (byte)0x08;
		locSync[44] = (byte)0xbd;
		locSync[45] = (byte)0x03;
		
		locSync[48] = (byte)0x5e;
		locSync[49] = (byte)0x02;
		locSync[50] = (byte)0xbc;
		locSync[51] = (byte)0x01;
		locSync[52] = (byte)0x18;
		
		cur.sendToMap(externmove);
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), locSync);
		cur.KillInvFreeze();
		return null;
	}

}
