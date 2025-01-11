package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class statsarributes implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling statpoints arributes ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		
		byte[] statsarributes = new byte[32];
		
		statsarributes[0] = (byte)statsarributes.length;
		statsarributes[4] = (byte)0x04;
		statsarributes[6] = (byte)0x1d;
		statsarributes[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			statsarributes[12+i] = chid[i]; // char id
		}

		statsarributes[16] = (byte)0x01;
		
		
		byte[] strengthz = new byte[4];
		
		byte[] Dexteryz = new byte[4];
		
		byte[] Vitalityz = new byte[4];
		
		byte[] Intelligencez = new byte[4];
		
		byte[] Agilityz = new byte[4];
		

		for(int i=0;i<2;i++) {
			statsarributes[18+i] = decrypted[0+i]; // Strength
			strengthz[i] = decrypted[0+i];
			
			statsarributes[20+i] = decrypted[2+i]; // Dextery
			Dexteryz[i] = decrypted[2+i];
			
			statsarributes[22+i] = decrypted[4+i]; // Vitality
			Vitalityz[i] = decrypted[4+i];
			
			statsarributes[24+i] = decrypted[6+i]; // Intelligence
			Intelligencez[i] = decrypted[6+i];
			
			statsarributes[26+i] = decrypted[8+i]; // Agility
			Agilityz[i] = decrypted[8+i];
		}
		
		
		int skid1 = BitTools.byteArrayToInt(strengthz);
		int strength =  skid1 - cur.getstrength(); 
		if(strength < 0){return null;}
		//System.out.println("strength: " + strength);	
		
		int skid2 = BitTools.byteArrayToInt(Dexteryz);
		int Dextery = skid2 - cur.getdextery(); 
		if(Dextery < 0){return null;}
		//System.out.println("Dextery: " + Dextery);
		
		int skid3 = BitTools.byteArrayToInt(Vitalityz);
		int Vitality = skid3 - cur.getvitality(); 
		if(Vitality < 0){return null;}
		//System.out.println("Vitality: " + Vitality);
		
		int skid4 = BitTools.byteArrayToInt(Intelligencez);
		int Intelligence = skid4 -  cur.getintelligence(); 
		if(Intelligence < 0){return null;}
		//System.out.println("Intelligence: " + Intelligence);
		
		int skid5 = BitTools.byteArrayToInt(Agilityz);
		int Agility =  skid5 - cur.getagility();
		if(Agility < 0){return null;}
		//System.out.println("Agility: " + Agility);
		
		int totalSP = strength + Dextery + Vitality + Intelligence + Agility;
		int statpointsleft = cur.getStatPoints() - totalSP;
		
		
		
		
		if (statpointsleft < 0){return null;} else{
		
		
		//System.out.println("statpointsleft: " + statpointsleft);
		byte[] SP = BitTools.intToByteArray(statpointsleft); 
	
		final int Str = cur.getstrength() + strength;
		final int Dex = cur.getdextery() + Dextery;
		final int Vit = cur.getvitality() + Vitality;
		final int Int= cur.getintelligence() + Intelligence;
		final int Agi = cur.getagility() + Agility;
		
		cur.setstrength(Str);
		cur.setdextery(Dex);
		cur.setvitality(Vit);
		cur.setintelligence(Int);
		cur.setagility(Agi);
		cur.setStatPoints(statpointsleft);
	
		CharacterDAO.setarributesz(Str,Dex,Vit,Int,Agi,cur.getCharID());
		CharacterDAO.setstatpoints(statpointsleft, cur.getCharID());
		for(int i=0;i<2;i++) {
		statsarributes[28+i] = SP[i]; // how many points r left in the pot
		}
		
		
		statsarributes[30] = (byte)0x40;
		statsarributes[31] = (byte)0x2a;

	
		//PERFECT WAY TO KNOW WHATS WHAT !
		for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" "); 
			
		}
				


		//TODO: save statsarributes to db =P
		
		//System.out.println("DONE");
		return statsarributes;
		}
	}
	
}
