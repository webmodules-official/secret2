package GameServer.GamePackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
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
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;

public class guilddeclarewar implements Packet {
	public long TimestampIniGuild;  
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
	/*	for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		System.out.println("");
		for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		System.out.println("");
		System.out.println("");*/
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		if(cur.guild == null){return null;}
		// decrypted[0] = determiner 
		// decrypted[1+ the rest is the name input]
		boolean Valid = false;
		
		byte[] namez = new byte[16];
		for(int i=0;i<decrypted[1+i];i++){
			namez[i] = decrypted[1+i];
		}
		String TargetName =  BitTools.byteArrayToString(namez);
		String finalnameparsed = null; 
		 byte[] hisname = new String(TargetName).getBytes();
	        byte arr[] = hisname;
	        try {
	        		int z;
	        		for (z = 0; z < arr.length && arr[z] != 0; z++) { }
	        		 finalnameparsed = new String(arr, 0, z, "UTF-8");
	        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
			}
	        
	        int TguildUID = 0;
	        
			// check first if guild exist
    		try{
    			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getguildbyname(SQLconnection.getInstance().getaConnection(), finalnameparsed));
    			if(rs != null){
    			if(rs.next()){
    			if(rs.getString("name").equals(finalnameparsed)){Valid = true; TguildUID = rs.getInt("GuildID");
    			}else{Charstuff.getInstance().respond("Guild does not exist.",con); return null;}
    			}else{Charstuff.getInstance().respond("Guild does not exist.",con); return null;}
    			}else{Charstuff.getInstance().respond("Guild does not exist.",con); return null;}
    		    }catch (Exception e) {
    				 System.out.println(e.getMessage());
    			}
		
    		if(TguildUID <= 0){return null;}
    		if(Valid == false){return null;}
    		
            int Key = BitTools.ValueToKey(cur.getCharID(), cur.guild.guildids); 


		// declare war and lose 1000guild gold
		if(cur.guild != null && decrypted[0] == 0){    		
			if(cur.guild.getguildranks(Key) != 7){Charstuff.getInstance().respondguild("Only the guildmaster can declare war.", cur.GetChannel());return null;}
			if(!wmap.Guild.containsKey(TguildUID)){
				Charstuff.getInstance().respond("None of the "+finalnameparsed+" guildmembers has been online today.",con);
				return null;
			}
			if(cur.guild.War.containsKey(finalnameparsed)){
				Charstuff.getInstance().respond("This guild is already on the list.",con);
				return null;
			}
			if(cur.guild.War.size() >= 5){
				Charstuff.getInstance().respond("War list is full.",con);
				return null;
			}
			if(cur.guild != null && cur.guild.guildgold >= 500 && cur.guild.getGuildfame() >= 25000 && wmap.getGuild(TguildUID).guildtype != 1){}else{Charstuff.getInstance().respond("Only Guild grade 10 or lower can declare war.",con);return null;}
			String FinalLine = cur.guild.getguildname()+" has declared war to "+finalnameparsed;
			byte[] gmsg = new byte[14+FinalLine.length()];
			byte[] msg = FinalLine.getBytes(); // <--- real gm msg lol
			gmsg[0] = (byte)gmsg.length;
			gmsg[4] = (byte)0x03;
			gmsg[6] = (byte)0x50;
			gmsg[7] = (byte)0xc3;
			gmsg[8] = (byte)0x01;
			gmsg[9] = (byte)FinalLine.length();
			
			//put us
			cur.guild.War.put(finalnameparsed,TguildUID);
				
			//put them
			wmap.getGuild(TguildUID).War.put(cur.guild.guildname, cur.guild.GuildID);
			
			
			//lose 200g 
			int ngg = cur.guild.guildgold - 200;
			if(ngg < 0){return null;}
			cur.guild.guildgold = ngg;
			
			//refresh for me
			cur.guild.RefreshOnlyForMe(cur);
			
			for(int i=0;i<msg.length;i++) {
				gmsg[i+13] = msg[i];
			}
			Charstuff.getInstance().respond("Type in guild chat to end war: ,endwar:guildname",cur.GetChannel());
			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue();
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
			}
			
		}
		
		//break war and lose 200guildgold
		if(cur.guild != null && decrypted[0] == 1){
			if(cur.guild.getguildranks(Key) != 7){Charstuff.getInstance().respondguild("Only the guildmaster can end war.", cur.GetChannel());return null;}
			if(!wmap.Guild.containsKey(TguildUID)){
				Charstuff.getInstance().respond("None of the "+finalnameparsed+" guildmembers has been online today.",con);
				return null;
			}
			if(!cur.guild.War.containsKey(finalnameparsed)){
				Charstuff.getInstance().respond("This guild has already been removed from the list.",con);
				return null;
			}
			String FinalLine = cur.guild.getguildname()+" has end the war with "+finalnameparsed;
			byte[] gmsg = new byte[14+FinalLine.length()];
			byte[] msg = FinalLine.getBytes(); // <--- real gm msg lol
			gmsg[0] = (byte)gmsg.length;
			gmsg[4] = (byte)0x03;
			gmsg[6] = (byte)0x50;
			gmsg[7] = (byte)0xc3;
			gmsg[8] = (byte)0x01;
			gmsg[9] = (byte)FinalLine.length();
			
			//put us
			cur.guild.War.remove(finalnameparsed);
			
			//put them
			wmap.getGuild(TguildUID).War.remove(cur.guild.guildname);
			
			//lose 200g 
			int ngg = cur.guild.guildgold - 200;
			if(ngg < 0){return null;}
			cur.guild.guildgold = ngg;
			
			//refresh for me
			cur.guild.RefreshOnlyForMe(cur);
			
			for(int i=0;i<msg.length;i++) {
				gmsg[i+13] = msg[i];
			}
			Charstuff.getInstance().respond("Type in guild chat to end war: ,endwar:guildname",cur.GetChannel());

			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue();
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
			}
			
		}
		
	
		//request alliance and lose 50guild gold 
		if(cur.guild != null && decrypted[0] == 2){
			if(cur.guild.getguildranks(Key) != 7){Charstuff.getInstance().respondguild("Only the guildmaster can handle alliance.", cur.GetChannel());return null;}
			   if(!wmap.Guild.containsKey(TguildUID)){
				Charstuff.getInstance().respond("None of the "+finalnameparsed+" guildmembers has been online today.",con);
				return null;
			}
		    Character Tplayer = wmap.getCharacter(wmap.getGuild(TguildUID).getguildids(0));
			if(Tplayer == null){Charstuff.getInstance().respond("The guildmaster of "+finalnameparsed+" is not online.",con);return null;}
			if(cur.guild.Alliance.containsKey(finalnameparsed)){
				Charstuff.getInstance().respond("This guild is already on the list.",con);
				return null;
			}
			if(cur.guild.Alliance.size() >= 5){
				Charstuff.getInstance().respond("Your Guild Alliance list is full.",con);
				return null;
			}
			if(Tplayer.guild.Alliance.size() >= 5){
				Charstuff.getInstance().respond("Target Guild Alliance list is full.",con);
				return null;
			}
			byte[] Tchid = BitTools.intToByteArray(Tplayer.getCharID());
			byte[] iniguild6 = new byte[260]; 
			iniguild6[0] = (byte)0x20; 
			iniguild6[4] = (byte)0x04; 
			iniguild6[6] = (byte)0x61; 
			iniguild6[8] = (byte)0x01; 
			for(int a=0;a<4;a++) {
				iniguild6[12+a] = Tchid[a];
				iniguild6[44+a] = Tchid[a];
			}
			iniguild6[32] = (byte)0xe4; 
			iniguild6[36] = (byte)0x04; 
			iniguild6[38] = (byte)0x51; 
			iniguild6[40] = (byte)0x01;
			iniguild6[48] = (byte)0x01;
			iniguild6[50] = (byte)0x02;// determiner
			byte[] mygname = cur.guild.guildname.getBytes();
			for(int a=0;a<mygname.length;a++) {
				iniguild6[51+a] = mygname[a];
			}
			
			//lose 50g upon rquest
			int ngg = cur.guild.guildgold - 50;
			if(ngg < 0){return null;}
			cur.guild.guildgold = ngg;
			
			//refresh for me
			cur.guild.RefreshOnlyForMe(cur);
			
			ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), iniguild6);
			
			String FinalLine = cur.guild.getguildname()+" has requested alliance to "+finalnameparsed;
			byte[] gmsg = new byte[14+FinalLine.length()];
			byte[] msg = FinalLine.getBytes(); // <--- real gm msg lol
			gmsg[0] = (byte)gmsg.length;
			gmsg[4] = (byte)0x03;
			gmsg[6] = (byte)0x50;
			gmsg[7] = (byte)0xc3;
			gmsg[8] = (byte)0x01;
			gmsg[9] = (byte)FinalLine.length();
			
			for(int i=0;i<msg.length;i++) {
				gmsg[i+13] = msg[i];
			}
			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue();
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
			}
			
		}
		
		//break alliance and lose 5% guild fame
		if(cur.guild != null && decrypted[0] == 3){
			if(cur.guild.getguildranks(Key) != 7){Charstuff.getInstance().respondguild("Only the guildmaster can handle alliance.", cur.GetChannel());return null;}
			   if(!wmap.Guild.containsKey(TguildUID)){
				Charstuff.getInstance().respond("None of the "+finalnameparsed+" guildmembers has been online today.",con);
				return null;
			}
			if(!cur.guild.Alliance.containsKey(finalnameparsed)){
				Charstuff.getInstance().respond("This guild has already been removed from the list.",con);
				return null;
			}
			String FinalLine = cur.guild.getguildname()+" has ended the alliance with "+finalnameparsed;
			byte[] gmsg = new byte[14+FinalLine.length()];
			byte[] msg = FinalLine.getBytes(); // <--- real gm msg lol
			gmsg[0] = (byte)gmsg.length;
			gmsg[4] = (byte)0x03;
			gmsg[6] = (byte)0x50;
			gmsg[7] = (byte)0xc3;
			gmsg[8] = (byte)0x01;
			gmsg[9] = (byte)FinalLine.length();
			
			//put us
			cur.guild.Alliance.remove(finalnameparsed);
			
			//put them
			wmap.getGuild(TguildUID).Alliance.remove(cur.guild.guildname);
			
			//lose 5% fame
			double nff = cur.guild.guildfame * .05;
			if(nff < 0){return null;}
			cur.guild.guildfame = cur.guild.guildfame - (int)nff;
			
			//refresh for me
			cur.guild.RefreshOnlyForMe(cur);
			
			for(int i=0;i<msg.length;i++) {
				gmsg[i+13] = msg[i];
			}
			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue();
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
			}
		}
		
		//complete alliance and lose gold
		if(cur.guild != null && decrypted[0] == 4){
			 if(!wmap.Guild.containsKey(TguildUID)){
				Charstuff.getInstance().respond("None of the "+finalnameparsed+" guildmembers has been online today.",con);
				return null;
			}
		    Character Tplayer = wmap.getCharacter(wmap.getGuild(TguildUID).getguildids(0));
			if(Tplayer == null){Charstuff.getInstance().respond("The guildmaster of "+finalnameparsed+" is not online.",con);return null;}
			if(cur.guild.Alliance.containsKey(finalnameparsed)){
				Charstuff.getInstance().respond("This guild is already on the list.",con);
				return null;
			}
			String FinalLine = finalnameparsed+" are now in alliance with "+cur.guild.getguildname();
			byte[] gmsg = new byte[14+FinalLine.length()];
			byte[] msg = FinalLine.getBytes(); // <--- real gm msg lol
			gmsg[0] = (byte)gmsg.length;
			gmsg[4] = (byte)0x03;
			gmsg[6] = (byte)0x50;
			gmsg[7] = (byte)0xc3;
			gmsg[8] = (byte)0x01;
			gmsg[9] = (byte)FinalLine.length();
			
			//put us
			cur.guild.Alliance.put(finalnameparsed,TguildUID);
			
			//put them
			wmap.getGuild(TguildUID).Alliance.put(cur.guild.guildname, cur.guild.GuildID);
			
			//lose 50g upon complete
			int ngg = cur.guild.guildgold - 50;
			if(ngg < 0){return null;}
			cur.guild.guildgold = ngg;
			
			//refresh for both
			cur.guild.RefreshOnlyForMe(cur);
			Tplayer.guild.RefreshOnlyForMe(Tplayer);
			
			for(int i=0;i<msg.length;i++) {
				gmsg[i+13] = msg[i];
			}
			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue();
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg);
			}
		}
		
		//get the war and alliance refreshed
		if(cur.guild != null && decrypted[0] == 6){
			cur.guild.refreshWarAlliance(cur);
		}
		
		return null;
	}

}
