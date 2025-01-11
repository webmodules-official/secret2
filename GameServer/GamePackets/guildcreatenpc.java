package GameServer.GamePackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Player.Character;
import Player.Charstuff;
import Player.Guild;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;

public class guildcreatenpc implements Packet {
	private WMap wmap = WMap.getInstance();
	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling guildcreatenpc");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" "); }
		//System.out.println("");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[56];
		byte[] namez = new byte[16];
		fury[0] = (byte)0x38;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x3d;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++){
			fury[12+i] = chid[i]; // charid
		}
		fury[16] = (byte)0x01;
		fury[18] = (byte)0x01;
		fury[19] = decrypted[1];
		for(int i=0;i<decrypted[2+i];i++){
			fury[20+i] = decrypted[2+i];
			namez[i] = decrypted[2+i];
		}
		//fury[40] = (byte)0xd4; Fame Contribution upon creating idk why
		//fury[41] = (byte)0x89;
		fury[44] = (byte)0x68;
		int type = BitTools.byteToInt(decrypted[1]);
		String TargetName =  BitTools.byteArrayToString(namez);
		String nameee = null; 
		String finalnameparsed = null;
		 byte[] hisname = new String(TargetName).getBytes();
	        byte arr[] = hisname;
	        try {
	        		int z;
	        		for (z = 0; z < arr.length && arr[z] != 0; z++) { }
	        		 nameee = new String(arr, 0, z, "UTF-8");
	        			 finalnameparsed = nameee.replaceAll("[^a-zA-Z0-9]+","");
	        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
			}
	

		//disband
		if(cur.guild != null && decrypted[0] == 0)
		{	// already done clientsided
	        //int Key = BitTools.ValueToKey(cur.getCharID(), cur.guild.guildids); 
	        //if(Key != 0){Charstuff.getInstance().respondguild("Only the guildmaster can disband the guild.", cur.GetChannel()); cur.ClearInv();return null;}
			byte[] newgold = BitTools.LongToByteArrayREVERSE(cur.getgold());
			for(int i=0;i<newgold.length;i++){fury[48+i] = newgold[i];}
			
			int GUILDID = cur.getGuildID();
			Guild GUILD = wmap.getGuild(GUILDID);
			CharacterDAO.deleteguild(GUILDID);
			GUILD.setGuildtype(0);
			GUILD.setguildname("");
			GUILD.setGuildfame(0);
			GUILD.setGuildgold(0);
			GUILD.setGuildhat(0);
			GUILD.setGuildicon(0);
			byte[] nullz = new byte[4]; // NULL IT
			GUILD.setguildnews(nullz);
			wmap.rmGuild(GUILDID);
		
			Iterator<Map.Entry<Integer, Integer>> iter = GUILD.guildids.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Integer, Integer> pairs = iter.next();
				int CharID = pairs.getValue();
				CharacterDAO.putguildinchartable(0, CharID);
				if(wmap.CharacterExists(CharID)){
					Character Member = wmap.getCharacter(CharID);
					if(Member != null){
						Member.guild = null;
						Member.setGuildID(0);
						byte[] Mchid = BitTools.intToByteArray(Member.getCharID());
						byte[] mfury = new byte[1452];
						byte[] mfury1 = new byte[296];
						byte[] megdata = new byte[40];
						megdata[0] = (byte)0x28;
						megdata[4] = (byte)0x05;
						megdata[6] = (byte)0x41;
						megdata[8] = (byte)0x01;
						
						mfury[0] = (byte)0x20;
						mfury[4] = (byte)0x04;
						mfury[6] = (byte)0x61;
						mfury[8] = (byte)0x01;
						
						mfury[32] = (byte)0x48;
						mfury[36] = (byte)0x04;
						mfury[38] = (byte)0x3f;
						mfury[40] = (byte)0x01;
						
						mfury[48] = (byte)0x01;
						
						mfury[56] = (byte)0x35;
						mfury[57] = (byte)0x35;
						mfury[58] = (byte)0x35;
						
						mfury[73] = (byte)0xa5;
						mfury[74] = (byte)0x14;
						mfury[75] = (byte)0x08;
						
						mfury[80] = (byte)0x35;
						mfury[81] = (byte)0x35;
						mfury[82] = (byte)0x35;
						
						mfury[97] = (byte)0xd3;
						mfury[98] = (byte)0x94;
						mfury[99] = (byte)0x2e;
						
						mfury[104] = (byte)0x20;
						mfury[108] = (byte)0x04;
						mfury[110] = (byte)0x61;
						mfury[112] = (byte)0x01;
						
						mfury[136] = (byte)0x4c;
						mfury[137] = (byte)0x06;
						mfury[140] = (byte)0x04;
						mfury[142] = (byte)0x41;
						mfury[144] = (byte)0x01;
						
						
						for(int i=0;i<4;i++){
							mfury[12+i] = Mchid[i];
							mfury[44+i] = Mchid[i];
							mfury[52+i] = Mchid[i];
							mfury[76+i] = Mchid[i];
							mfury[116+i] = Mchid[i];
							mfury[148+i] = Mchid[i];
							
							megdata[12+i] = Mchid[i]; // charid
						}

						Member.sendToMap(megdata);
						ServerFacade.getInstance().addWriteByChannel(Member.GetChannel(), mfury);	
						ServerFacade.getInstance().addWriteByChannel(Member.GetChannel(), mfury1);	
					}
				}
			}
			
		return null;
		}
		
		//create
		if(decrypted[0] == 1)
		{
			if(nameee.equals("")){
				//System.out.println(" 2namezz :"+finalnameparsed+" == +nothing");
				return null;	 
			}else
			if(finalnameparsed.equals("")){
				//System.out.println(" 3namezz :"+finalnameparsed+" == +nothing");
				return null;	 
			}else
			if(!nameee.equals(finalnameparsed)){ 
				//System.out.println(" 4namezz :"+finalnameparsed+" != "+finalnameparsed);
				return null;	 
			}
			
			// Censor Protection system :DDDDD fuck them advertisers n watnot
			// checks a word all uppercase & lowercase, so no worrys about capitals
			String string = new String(finalnameparsed);
			String finalstring = string.toLowerCase();
			Iterator<Entry<String, String>> iterw = Charstuff.getInstance().CensoredWords.entrySet().iterator();
			while(iterw.hasNext()) {
				Entry<String, String> pairsw = iterw.next();
				//check if any of the words in hashmap is in this string.
				if(finalstring.contains(pairsw.getKey())){
					cur.respondguildTIMED("Censor Protection System has dectected foul language : "+pairsw.getKey(), cur.GetChannel()); return null;
				}

			}
			
			long inc = 0;
			//check if name is NOT already in db yet then continue creating guild.
    		try{
    			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getguilid(SQLconnection.getInstance().getaConnection(), finalnameparsed));
    			if(rs.next()){
    			// check first if its going from MUN TO PA
    			if((int)decrypted[1] == 3 && cur.guild != null){
    			if(cur.guild.getguildname().equals(rs.getString("name")) && cur.guild.getguildname().equals(finalnameparsed)){
    				cur.guild.guildtype = 3;
    				inc = cur.guild.getGuildgold() - 1000;
    				if(inc < 0){return null;}
    				cur.guild.setGuildgold((int)inc);
    		    	cur.guild.ExternalWindowRefreshToMap();
    		    	cur.guild.RefreshEXCEPT(0);
    		    	return null; // end it here
    			}
    			Charstuff.getInstance().respond("Guildname must be the same when upgrading to PA.",con); return null; // end it here
    			}else
    			if(finalnameparsed.equals(rs.getString("name"))){Charstuff.getInstance().respond("Guildname is already in use.",con); return null;}
    			}
    		    }catch (Exception e) {
    				 System.out.println(e.getMessage());
    			}

			if(decrypted[1] == 1){inc = cur.getgold() - 1000000;}   
			if(decrypted[1] == 2){inc = cur.getgold() - 1000000000;}
			if(decrypted[1] == 3){inc = cur.getgold() - 1000000000;}
			if(decrypted[1] == 4){inc = cur.getgold() - 1000000000;} // WTF??
			if(decrypted[1] == 5){inc = cur.getgold() - 10000000000L;}
			if(decrypted[1] == 6){inc = cur.getgold() - 10000000000L;}//gak
			if(decrypted[1] == 7){inc = cur.getgold() - 10000000000L;}
			if(decrypted[1] == 8){inc = cur.getgold() - 10000000000L;}
			if(inc < 0){/*Charstuff.getInstance().respond("guildcreatenpc1.",con);*/return null;}
			byte[] newgold = BitTools.LongToByteArrayREVERSE(inc);
		
			int newfame = 0;
			if(decrypted[1] == 1){newfame = cur.getFame() - 0;}		//bang
			if(decrypted[1] == 2){newfame = cur.getFame() - 4000;}	//mun
			if(decrypted[1] == 3){newfame = cur.getFame() - 0;} 	//pa
			if(decrypted[1] == 4){newfame = cur.getFame() - 0;} 	// WTF???????? = nothing
			if(decrypted[1] == 5){newfame = cur.getFame() - 96773;} //DAN
			if(decrypted[1] == 6){newfame = cur.getFame() - 0;}		//gak
			if(decrypted[1] == 7){newfame = cur.getFame() - 96773;}	//GYO
			if(decrypted[1] == 8){newfame = cur.getFame() - 96773;} //GUNG
			if(newfame < 0){/*Charstuff.getInstance().respond("guildcreatenpc2.",con);*/return null;}
			
			boolean level = false;
			if(decrypted[1] == 1 && 36 <= cur.getLevel()){level = true;}	//bang
			if(decrypted[1] == 2 && 48 <= cur.getLevel()){level = true;}	//mun
			if(decrypted[1] == 3 && 48 <= cur.getLevel()){level = true;} //pa
			if(decrypted[1] == 4 && 36 <= cur.getLevel()){level = true;} // WTF???????? = nothing
			if(decrypted[1] == 5 && 110 <= cur.getLevel()){level = true;}//DAN
			if(decrypted[1] == 6 && 100 <= cur.getLevel()){level = true;}//gak
			if(decrypted[1] == 7 && 120 <= cur.getLevel()){level = true;}//GYO
			if(decrypted[1] == 8 && 130 <= cur.getLevel()){level = true;}//GUNG
			if(level == false){return null;}
			
			
			byte[] fame = BitTools.intToByteArray(newfame);
		for(int i=0;i<newgold.length;i++){
			fury[48+i] = newgold[i];
		}
		for(int i=0;i<4;i++){
			fury[40+i] = fame[i];
		}
		cur.setFame(newfame);
		cur.setgold(inc);
		inc = 0;
		String firstmember = "0,0,0,0,0,0,";
		CharacterDAO.createguild(type, finalnameparsed, 0, 0, 0, 1013,"[Guild Rank:#0  PvP Rating:0  K:0 D:0]", firstmember);
		try {
		ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getguilid(SQLconnection.getInstance().getaConnection(), finalnameparsed));
		if(rs.next()){
		CharacterDAO.putguildinchartable(rs.getInt("GuildID"),  cur.charID);
		cur.setGuildID(rs.getInt("GuildID"));
		cur.RockCityBoi();//fairy tarurur guild activationo
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
	    if (cur.guild != null && cur.guild.getguildname() != null){
			cur.guild.ManageMember(0, cur.getCharID(), cur.getLOGsetName(), 7, cur.getCharacterClass(), 0);// CharID, Name, Rank(member), fame(starting 0)
	    	cur.sendToMap(cur.extCharGuild());
	    	ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);
	    	cur.guild.iniguild(cur);
		    cur.guild.guildsave();
		    Charstuff.getInstance().respondguild("Relog to take the FULL effect of the guild.", cur.GetChannel());
	    }
		return null;
		}
		
		
		//donate fame - gold to guild
		if(cur.guild != null && decrypted[0] == 2)
		{	
			fury[18] = (byte)0x02;
			
			byte[] golddonated = new byte[4];
			byte[] famedonated = new byte[4];
			for(int i=0;i<4;i++){
				golddonated[i] = decrypted[20+i]; //gold donate
				famedonated[i] = decrypted[28+i]; //fame donate
			}
			int donatedgold = BitTools.byteArrayToInt(golddonated); //gold donate
			int donatedfame = BitTools.byteArrayToInt(famedonated); //fame donate

			if(donatedgold < 0){return null;}// cant donate 0 or less
			if(donatedfame < 0){return null;}// cant donate 0 or less
			if(cur.getFame() - donatedfame < 0){return null;}
			if(cur.getgold() - donatedgold < 0){return null;}
			
			// get cur slot 
			int Key = BitTools.ValueToKey(cur.getCharID(), cur.guild.guildids);
			
			//handle fame
			cur.setFame(cur.getFame() - donatedfame);
			int lolnoway = donatedfame / 100;
			cur.guild.setguildfames(Key, cur.guild.getguildfames(Key) + lolnoway); // IS NO FAME BUT CONTRIBUTION
			cur.guild.setGuildfame(cur.guild.getGuildfame() + donatedfame);
			
			//handle gold
			cur.setgold(cur.getgold() - donatedgold);
			long newgold =  donatedgold / 1000000;
			long newgold2 = cur.guild.getGuildgold() + newgold;
			int lolnowaygold = donatedgold / 10000000;
			cur.guild.setguildfames(Key, cur.guild.getguildfames(Key) + lolnowaygold); // IS NO FAME BUT CONTRIBUTION
			cur.guild.setGuildgold((int)newgold2);
			
			//set new gold,fame visual in packet
			byte[] GOLD = BitTools.LongToByteArrayREVERSE(cur.getgold());
			 for(int w=0;w<GOLD.length;w++){
				fury[48+w] = GOLD[w]; //gold donate
			 }
			byte[] FAME = BitTools.intToByteArray(cur.getFame());
			for(int i=0;i<4;i++){
				fury[40+i] = FAME[i]; //fame donate
			}

			//send this packet so it substracs gold and fame from me on client visual
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);
			cur.guild.RefreshOnlyForMe(cur); // refresh me
			
		}
		
		//determine guild hat
		if(cur.guild != null && decrypted[0] == 3)
		{	
			//Apparently donating 10 gold gives Send button and HAT button
			int newguildgold = cur.guild.guildgold - 100;
			if(newguildgold < 0){return null;}
			cur.guild.guildgold = newguildgold;
			fury[18] = (byte)0x03;;
			cur.guild.guildhat = (int)decrypted[1]; // HAT
			cur.guild.ExternalWindowRefreshToMap();
			cur.guild.RefreshOnlyForMe(cur); // refresh me
		}
		
		
		
		return null;
	}
	
}
