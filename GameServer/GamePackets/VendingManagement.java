package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map.Entry;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import Encryption.Decryptor;

public class VendingManagement implements Packet {
	public static int inc = 0;
	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling Vending");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");	}
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] decryptedname = new byte[30];
		for(int i=0;i<30;i++) {
			decryptedname[i] = decrypted[i+1];
		}
		cur.BoothName =  BitTools.byteArrayToString(decryptedname);
		cur.BoothStance = decrypted[0];
		
		if(decrypted[0] == 0)
		{
		cur.IsVending = false;
		//System.out.println("Clearing BOOTH CACHE");
		cur.boothwindowinvtobooth.clear();
		cur.boothwindowSLOT.clear();
		cur.boothwindowX.clear();
		cur.boothwindowY.clear();
		cur.boothwindowSTACK.clear();
		cur.boothwindowPRICE.clear();
		WMap.getInstance().vendinglist.remove(cur.getCharID());
		}else{
			// Censor Protection system :DDDDD fuck them advertisers n watnot
			// checks a word all uppercase & lowercase, so no worrys about capitals
			String string = new String(decryptedname);
			String finalstring = string.toLowerCase();
			System.out.print(finalstring);
			Iterator<Entry<String, String>> iterw = Charstuff.getInstance().CensoredWords.entrySet().iterator();
			while(iterw.hasNext()) {
				Entry<String, String> pairsw = iterw.next();
				//check if any of the words in hashmap is in this string.
				if(finalstring.contains(pairsw.getKey())){
					cur.respondguildTIMED("Censor Protection System has dectected foul language : "+pairsw.getKey(), cur.GetChannel()); return null;
				}

			}	
			
		cur.IsVending = true;
		WMap.getInstance().setvendinglist(cur.getCharID(), cur.getCharID());
		}
		byte[] booth = new byte[52];
		booth[0] = (byte)booth.length;
		booth[4] = (byte)0x04; // 0x04 = me 
		booth[6] = (byte)0x37;
		booth[8] = (byte)0x01;
		booth[9] = (byte)0x9d;
		
		booth[10] = (byte)0xbf; 
		booth[11] = (byte)0x0f;
		
		for(int i=0;i<4;i++) {
			booth[12+i] = chid[i];
		}

		booth[16] = (byte)0x01;	
		booth[18] = decrypted[0];	
		
		for(int i=0;i<30;i++) {
			booth[19+i] = decrypted[i+1];
		}
		
		booth[50] =  (byte)0xbf; // decrypt that  shit 
		booth[51] =  (byte)0x0f;
		//System.out.println("DONE");
		cur.extCharBooth(1);
		return booth; //Return bytebuffer??? should be null
	}
	
}
