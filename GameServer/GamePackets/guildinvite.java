package GameServer.GamePackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class guildinvite implements Packet {
	 private Map<String, Character> names = new HashMap<String, Character>();
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling guildinvite ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");	
		//}
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[88];
		byte[] namez = new byte[16];
		fury[0] = (byte)fury.length;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x3e;
		fury[8] = (byte)0x01;
		fury[16] = (byte)0x01;
		fury[18] = decrypted[0];// determines shit
		for(int i=0;i<decrypted[2+i];i++){
			namez[i] = decrypted[2+i];
		}
		String CharName = BitTools.byteArrayToString(namez);
			byte[] hisname = new String(CharName).getBytes();
	        byte arr[] = hisname;
	        try {
	        		int m;
	        		for (m = 0; m < arr.length && arr[m] != 0; m++) { }
	        		String finalnameparsed = new String(arr, 0, m, "UTF-8");
	        		////System.out.println("PARSED NAME: "+finalnameparsed);
	        		
  if(decrypted[0] == 0){
	  return null;
  }
 
  if(decrypted[0] == 1){
		Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;

		while(iter.hasNext()) {
		Map.Entry<Integer, Character> pairs = iter.next();
		tmp = pairs.getValue(); 
		if(finalnameparsed.equals(tmp.getLOGsetName())) {
		names.put(tmp.getLOGsetName(), tmp);
		Character anyplayer = names.get(finalnameparsed); // select player from string
		
		if(anyplayer.guild == null){return null;}
		
		boolean level = false;
		if(anyplayer.guild.guildtype == 1 && 36 <= cur.getLevel()){level = true;}	//bang
		if(anyplayer.guild.guildtype == 2 && 48 <= cur.getLevel()){level = true;}	//mun
		if(anyplayer.guild.guildtype == 3 && 48 <= cur.getLevel()){level = true;} //pa
		if(anyplayer.guild.guildtype == 4 && 36 <= cur.getLevel()){level = true;} // WTF???????? = nothing
		if(anyplayer.guild.guildtype == 5 && 110 <= cur.getLevel()){level = true;}//DAN
		if(anyplayer.guild.guildtype == 6 && 100 <= cur.getLevel()){level = true;}//gak
		if(anyplayer.guild.guildtype == 7 && 120 <= cur.getLevel()){level = true;}//GYO
		if(anyplayer.guild.guildtype == 8 && 130 <= cur.getLevel()){level = true;}//GUNG
		if(level == false){ Charstuff.getInstance().respondguild("Level not high enough.", cur.GetChannel());return null;}
		
		int exgid = anyplayer.getGuildID();
		  cur.setGuildID(exgid);
		  CharacterDAO.putguildinchartable(exgid, cur.charID);
		  cur.RockCityBoi();
		  if(cur.guild != null){
				byte[] Tchid = BitTools.intToByteArray(anyplayer.getCharID());
				fury[18] = (byte)0x03;// determines shit
				byte[] guildname = BitTools.stringToByteArray(cur.guild.getguildname());
					for(int i=0;i<guildname.length;i++){
						fury[20+i] = guildname[i];
				}
				byte[] targetname = BitTools.stringToByteArray(anyplayer.getLOGsetName());
				for(int i=0;i<targetname.length;i++){
					fury[44+i] = targetname[i];
				}
				byte[] myname = BitTools.stringToByteArray(cur.getLOGsetName());
				for(int i=0;i<myname.length;i++){ 
					fury[68+i] = myname[i];
				}
				for(int i=0;i<4;i++) {
					fury[12+i] = chid[i]; 
					fury[40+i] = Tchid[i]; 
					fury[64+i] = chid[i]; 
				}
			 cur.guild.addmember(cur);
			 cur.sendToMap(cur.extCharGuild());
			 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 
			 cur.guild.iniguild(cur);
		     cur.guild.RefreshEXCEPT(cur.getCharID());
		     cur.guild.guildsave();
		     Charstuff.getInstance().respondguild("Relog to take the FULL effect of the guild.", cur.GetChannel());
		   }	
		}
	 }
  } 
  
	        		
  if(decrypted[0] == 2){
	  	int Key = BitTools.ValueToKey(cur.getCharID(), cur.guild.guildids); // how to get it without conquerent exception
		if(cur.guild.getguildranks(Key) == 3){ //If its membe then return null 
		Charstuff.getInstance().respond("Only Masters can invite players into the guild, not Members.", con);	
		return null;
		}
	  if(cur.guild != null){
		 byte[] guildname = BitTools.stringToByteArray(cur.guild.getguildname());
		for(int i=0;i<guildname.length;i++){
			fury[20+i] = guildname[i];
		}
		}
		
		byte[] myname = BitTools.stringToByteArray(cur.getLOGsetName());
		for(int i=0;i<myname.length;i++){ 
			fury[44+i] = myname[i];
		}
		
		Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;

		while(iter.hasNext()) {
		Map.Entry<Integer, Character> pairs = iter.next();
		tmp = pairs.getValue(); 
		if(finalnameparsed.equals(tmp.getLOGsetName())){
		names.put(tmp.getLOGsetName(), tmp);
		Character anyplayer = names.get(finalnameparsed); // select player from string
		if (cur.guild.getGuildtype() == 7 ||cur.guild.getGuildtype() == 3){  
			byte[] Tchid = BitTools.intToByteArray(anyplayer.getCharID());
			for(int i=0;i<4;i++) {
				fury[12+i] = Tchid[i]; 
				fury[40+i] = chid[i]; 
				fury[64+i] = Tchid[i]; 
			}
			
			byte[] targetname = BitTools.stringToByteArray(anyplayer.getLOGsetName());
			for(int i=0;i<targetname.length;i++){
				fury[68+i] = targetname[i];
			}
			
			ServerFacade.getInstance().addWriteByChannel(anyplayer.GetChannel(), fury); 		 
			}else
		if (anyplayer.getFaction() == cur.getFaction()){  
		byte[] Tchid = BitTools.intToByteArray(anyplayer.getCharID());
		for(int i=0;i<4;i++) {
			fury[12+i] = Tchid[i]; 
			fury[40+i] = chid[i]; 
			fury[64+i] = Tchid[i]; 
		}
		
		byte[] targetname = BitTools.stringToByteArray(anyplayer.getLOGsetName());
		for(int i=0;i<targetname.length;i++){
			fury[68+i] = targetname[i];
		}
		
		ServerFacade.getInstance().addWriteByChannel(anyplayer.GetChannel(), fury); 		 
		}else
		if (anyplayer.getFaction() != cur.getFaction()){ 
		Charstuff.getInstance().respond("Player must be the same faction!", con);	
		}
		}}
  }
		
		
		
		
	        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
		
		

		//System.out.println("DONE");
		return null;
	}
	
}
