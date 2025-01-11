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

public class leaveparty implements Packet {
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
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] newP = new byte[4];
		for(int i=0;i<4;i++) {
		newP[i] = decrypted[4+i];
		}
		int newp = BitTools.byteArrayToInt(newP);
		Character Member = wmap.getCharacter(newp);

		
  if(decrypted[0] == 0){ // = leave
	  Party pt = wmap.getParty(cur.partyUID);
	  if(cur.partyUID != 0 && pt.partymembers.containsValue(cur.charID)){
	  int Key = BitTools.ValueToKey(cur.charID,pt.partymembers);
	  if(Key != 0){pt.ReOrder(Key);}}
	  //System.out.println("cur: " +cur.getLOGsetName());
	  pt.leave_kicK(decrypted, cur);
	  cur.partyUID = 0; 	
  }
 
  if(decrypted[0] == 1){ // = kick
	  Party pt = wmap.getParty(Member.partyUID);
	  if(Member.partyUID != 0 && pt.partymembers.containsValue(cur.charID)){
	  int Key = BitTools.ValueToKey(Member.charID,pt.partymembers);
	  if(Key != 0){pt.ReOrder(Key);}}
	  //System.out.println("Member: " +Member.getLOGsetName());
	  pt.leave_kicK(decrypted, Member);
	  Member.partyUID = 0;  
  } 
   		
		//System.out.println("DONE");
		return null;
	}
	
}
