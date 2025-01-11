package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Player.Character;
import Player.Party;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Encryption.Decryptor;

public class PromotetoLeader implements Packet {
	 private WMap wmap = WMap.getInstance();
	 
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
		//PERFECT WAY TO KNOW WHATS WHAT !
		//System.out.println(" | ");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("");
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] newP = new byte[4];
		for(int i=0;i<4;i++) {
		newP[i] = decrypted[4+i];
		}
		Character Member = wmap.getCharacter(BitTools.byteArrayToInt(newP));
		
	  Party pt = wmap.getParty(cur.partyUID);
	  if(cur.partyUID != 0 && pt.partymembers.containsValue(Member.charID)){
	  int Key = BitTools.ValueToKey(Member.charID,pt.partymembers);
	  int curKey = BitTools.ValueToKey(cur.charID,pt.partymembers);
	  if(Key != 0 && curKey != 0){
		  pt.partymembers.put(Key,cur.charID);
		  pt.partymembers.put(curKey,Member.charID);
		  pt.PromoteToLeader(decrypted, cur);
	  }
	}	
   		
		return null;
	}
	
}
