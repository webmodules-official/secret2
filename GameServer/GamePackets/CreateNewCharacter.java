package GameServer.GamePackets;

import item.Item;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map.Entry;

import timer.SystemTimer;

import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.Character;
import ServerCore.ServerFacade;


public class CreateNewCharacter implements Packet {


	public void execute(ByteBuffer buff) {

	}

	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		final byte[] createNewCharacter = new byte[] { //static packet to respond to clients' character creation request
				(byte)0x14, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x06, (byte)0x00, (byte)0x01, (byte)0x01, 
				(byte)0x12, (byte)0x2b, (byte)0x00, (byte)0xc0, (byte)0xb7, (byte)0xc4, (byte)0x00, (byte)0xe0, (byte)0x21, (byte)0x45 
		};
		byte[] NameTaken = new byte[] { 
				(byte)0x14, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x06, (byte)0x00, (byte)0x00, (byte)0x01, 
				(byte)0x12, (byte)0x2b, (byte)0x00, (byte)0xc0, (byte)0xb7, (byte)0xc4, (byte)0x00, (byte)0xe0, (byte)0x21, (byte)0x45 
		};
		
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];

		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		byte[] characterName = new byte[16]; //if memory serves - max name length = 13 characters
		byte characterClass;
		byte[] stats = new byte[] { (byte)0x0A, (byte)0x0A, (byte)0x0A, (byte)0x0A, (byte)0x0A };
		byte statusPointsleft = 0x05;
		byte[] realLengthname = null;
		
		for(int i=0;i<characterName.length;i++) {
			characterName[i] = decrypted[i];
			if(decrypted[i] == 0x00) {
				realLengthname = new byte[i];
				break; 
			}
		}
		
		for(int i=0;i<realLengthname.length;i++) {
			realLengthname[i] = characterName[i];
		}
		
		/*
		 * Character bytes:
		 * 01 = Warrior
		 * 02 = Assassin
		 * 03 = Mage
		 * 04 = Monk
		 */
		characterClass = decrypted[decrypted.length-14]; 
		int face = decrypted[18]; // SET THE FKING FACE
		//int lolzi = (int)decrypted[decrypted.length-4] + (int)decrypted[decrypted.length-6] + (int)decrypted[decrypted.length-8] + (int)decrypted[decrypted.length-10] + (int)decrypted[decrypted.length-12] + (int)decrypted[36];
		//System.out.println("WTF:"+(int)decrypted[decrypted.length-4]+" + "+(int)decrypted[decrypted.length-6]+" + "+(int)decrypted[decrypted.length-8]+" + "+(int)decrypted[decrypted.length-10]+" + "+(int)decrypted[decrypted.length-12]+" + "+(int)decrypted[36]+" = "+lolzi);
		if((int)decrypted[36] > 5) { //remember to make sure that no more than 5 points can be left after creating character
			//System.out.println(" cheater,ban this bitch : 1");
			ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken); 
			return null;
		}else
		if((int)decrypted[decrypted.length-4] + (int)decrypted[decrypted.length-6] + (int)decrypted[decrypted.length-8] + (int)decrypted[decrypted.length-10] + (int)decrypted[decrypted.length-12] + (int)decrypted[36] > 55) { 
			//System.out.println(" cheater,ban this bitch : 2");
			ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken); 
			return null;
		}else{
			statusPointsleft = decrypted[decrypted.length-2]; 
		}
		
		stats[0] = decrypted[decrypted.length-4]; //INT
		stats[1] = decrypted[decrypted.length-6]; //AGI
		stats[2] = decrypted[decrypted.length-8]; //VIT
		stats[3] = decrypted[decrypted.length-10];//DEX
		stats[4] = decrypted[decrypted.length-12];//STR
				
	
		String nameee = new String(realLengthname);
		
		// Censor Protection system :DDDDD fuck them advertisers n watnot
		// checks a word all uppercase & lowercase, so no worrys about capitals
		String string = nameee;
		String finalstring = string.toLowerCase();
		Iterator<Entry<String, String>> iterw = Charstuff.getInstance().CensoredWords.entrySet().iterator();
		while(iterw.hasNext()) {
			Entry<String, String> pairsw = iterw.next();
			//check if any of the words in hashmap is in this string.
			if(finalstring.contains(pairsw.getKey())){
				ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken);  return null;
			}

		}	
		
		String name = nameee.replaceAll("[^a-zA-Z0-9]+","");
		//System.out.println(" 1namezz :"+name+" - "+name);
		if(nameee.equals("")){
			//System.out.println(" 2namezz :"+name+" == +nothing");
			ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken); 
			return null;	 
		}else
		if(name.equals("")){
			//System.out.println(" 3namezz :"+name+" == +nothing");
			ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken); 
			return null;	 
		}else
		if(!nameee.equals(name)){ 
			//System.out.println(" 4namezz :"+name+" != "+name);
			ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken); 
			return null;	 
		}
			//System.out.println(" 5namezz :"+name+" == "+name);
		
		int spawnX = -1660, spawnY = 2344; //Coordinates the new character will spawn at
		
				//check if name is only  ususable for this account id
				try{
					ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getAccountBoundByName(SQLconnection.getInstance().getaConnection(), name));
					if(rs != null){
					if(rs.next()){
					if(rs.getInt("accountID") != 0 && rs.getInt("accountID") != ((PlayerConnection)con).getPlayer().getAccountID()){//if we got result then fk off?
						//System.out.println(rs.getInt("accountID")+" != "+((PlayerConnection)con).getPlayer().getAccountID());
					
						ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken); 
					return null;
					}}}
				    }catch (Exception e) {
						 System.out.println(e.getMessage());
					}
		
		//check first if name is already taken, if so then respond "Name Already Taken" mesasge.
		try{
			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getCharacterByName(SQLconnection.getInstance().getaConnection(), name));
			if(rs != null){
			if(rs.next()){
			int Wobble = 0;
			Wobble = rs.getInt("CharacterID");
			if(Wobble != 0){//if we got result then already taken

				ServerFacade.getInstance().addWriteByChannel(((PlayerConnection)con).getPlayer().getSc(), NameTaken); 
			return null;
			}}}	
		    }catch (Exception e) {
				 System.out.println(e.getMessage());
			}

		long minato = System.currentTimeMillis()+ SystemTimer.MinuteToMiliseconds(Long.valueOf(10080));
		long minato2 = System.currentTimeMillis()+ SystemTimer.MinuteToMiliseconds(Long.valueOf(43200));
		long minato3 = System.currentTimeMillis()+ SystemTimer.MinuteToMiliseconds(Long.valueOf(43200));
		String lol = ("283000002,"+minato+",213062709,"+minato2+",213062707,"+minato3);// starter item expiration 
		
		
		Character cur = CharacterDAO.addAndReturnNewCharacter(name, stats, characterClass, statusPointsleft, ((PlayerConnection)con).getPlayer(), spawnX, spawnY, face, lol);

		cur.setitem_end_date(283000002, minato);
		cur.setitem_end_date(213062709, minato2);
		cur.setitem_end_date(213062707, minato3);
		
		if(cur != null) {
			((PlayerConnection)con).getPlayer().addCharacter(cur);
		}
		
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" ");
		//}
		
		return createNewCharacter;
	}

	
}
