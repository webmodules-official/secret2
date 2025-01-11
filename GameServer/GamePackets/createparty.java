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

public class createparty implements Packet {
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
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] newP = new byte[4];
		byte[] fury = new byte[64];
		fury[0] = (byte)0x40;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x23;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		fury[12+i] = chid[i]; // charid
		newP[i] = decrypted[4+i];
		}
		fury[16] = (byte)0x01;
		fury[18] = decrypted[0]; // 0x00 = null | 0x01 = accepted | 0x02 is requesting party
		fury[19] = (byte)0x08;
	
		int newp = BitTools.byteArrayToInt(newP);
		Character Leader = wmap.getCharacter(newp);
		
		 int min = cur.getLevel() - 15;
		 int max = cur.getLevel() + 15;
		 if(Leader.getLevel() >= min && Leader.getLevel() <= max){
	
		if(decrypted[0] == 0){ // if decline
			return null;
		}
		
		if(decrypted[0] == 2){ // request party
			if(Leader.partyUID == 0){
			for(int i=0;i<4;i++) {
				fury[20+i] = chid[i]; 
				fury[60+i] = chid[i]; 
			}
			ServerFacade.getInstance().addWriteByChannel(Leader.GetChannel(), fury);
		}else{Charstuff.getInstance().respond("Character is already in party!", con);}
		}
		
		if(decrypted[0] == 1){ // if accept
		if(Leader.partyUID != 0){	
		Party pt = wmap.getParty(Leader.partyUID);
		if(pt.inc >= 8){return null;} else{
		pt.addmember(cur);
		cur.partyUID = Leader.partyUID;
		pt.Refresh_Party(cur);
		}}else{	
		inc++;
		Party party = new Party(inc, decrypted, cur);
		wmap.Addparty(inc, party);
		}}
		}else{Charstuff.getInstance().respond("Character level too high / low!",con);}
		return null;
	}	
}
