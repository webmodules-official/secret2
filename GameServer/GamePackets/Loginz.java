package GameServer.GamePackets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import timer.SystemTimer;
import timer.furyoff;
import timer.globalsave;

import Player.Character;
import Player.Player;
import Player.PlayerConnection;
import Player.ipbanlist;
import ServerCore.ConnectionFinalizer;
import ServerCore.ServerFacade;
import Tools.BitTools;

import Connections.Connection;
import Database.AccountDAO;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class Loginz implements Packet {

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
		/*
		 *  Handle all your authentication logic in this block
		 */
		/*System.out.println(" ");
		System.out.println(" ");
		System.out.print("HEX:");
		for(int i=0;i<decrypted.length;i++){System.out.printf("%02x ", (decrypted[i]&0xFF));}
		System.out.println(" ");
	
		
		
		byte[] USERNAME = new byte[16];
		byte[] PASSWORD = new byte[16];
		byte[] DIGITPASS = new byte[4];
		
		byte[] realLengthname = null;
		byte[] realpassword = null;
		byte[] real4digitpass = null;
		
		for(int i=0;i<USERNAME.length;i++) {
			if(decrypted[5+i] != 0x00 || decrypted[5+i] != 0x1d) {
			USERNAME[i] = decrypted[5+i];
			if(decrypted[5+i] == 0x00) {
				realLengthname = new byte[i];
				break; 
			}}
		}
		for(int i=0;i<realLengthname.length;i++) {
			realLengthname[i] = USERNAME[i];
		}
		
		String username = null;
		String password = null;
		int digitpass = 0;
		
	
	        		for(int i=0;i<4;i++) {
	        			DIGITPASS[i] = decrypted[5+realLengthname.length+5+i];
	        		}
	        		
	         		username = new String(realLengthname);
	         		int newdigit = (int)decrypted[5+realLengthname.length+5] + 3;
	         		String lol = Byte.toString((byte)newdigit);
	         		System.out.println("1st D: "+Integer.valueOf(lol)+" - "+ lol);
	        		digitpass = BitTools.ChangeAnyDigit4DIGITPASS(BitTools.byteArrayToInt(DIGITPASS), 0, Integer.valueOf(lol));
					System.out.println("Username:"+username+" password:"+password+" digitpass:"+digitpass);
					/*Player tmpl = AccountDAO.authipandloggedin(username, digitpass, 1);
					
					PlayerConnection plc = (PlayerConnection)ServerFacade.getInstance().getConnections().get(con.getChan());

		if(tmpl != null) {	
			 if (con.getIp().equals(ipbanlist.getipbanlist(con.getIp()))){ // ip banned
					plc.addWrite((Login.banned1));
					plc.addWrite((Login.banned2)); // 2nd respond in a row just makes it work idk why
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						////e.printStackTrace();
					}
					con.threadSafeDisconnect();
				//this.logging.logMessage(Level.INFO, this, " ===>IP BANNED: " + con.getIp()+" <=== ");
				}
				else
				if(tmpl.getBanned() == 1) { // if has logged in but banned is on 1 in "accounts" then close everything directly
					plc.addWrite((Login.banned1));
					plc.addWrite((Login.banned2)); // 2nd respond in a row just makes it work idk why	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						////e.printStackTrace();
					}
					con.threadSafeDisconnect();
				//this.logging.logMessage(Level.INFO, this, " ===>ACCOUNT BANNED: " + con.getIp()+" <=== ");
				}else{	
			 try {
			 PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Login_logs.txt", true)));
			 Calendar Date = Calendar.getInstance(Locale.FRANCE);	
			 String timendate = Date.get(Calendar.HOUR_OF_DAY)+":"+Date.get(Calendar.MINUTE)
			 +" "+Date.get(Calendar.DAY_OF_MONTH)+"/"+Date.get(Calendar.MONTH)+"/"+Date.get(Calendar.YEAR);
			 out.println("ID:"+tmpl.getAccountID()+" Username:"+tmpl.getUsername()+" IP:"+con.getIp()+" Time:"+timendate);
			 out.close();	
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			 }
			tmpl.setChannel(con.getChan());
			plc.setPlayer(tmpl);
			plc.addWrite((Login.Authsucces));
			ArrayList<Character> characters = CharacterDAO.loadCharacters(tmpl.getAccountID());
			
			if(characters != null) {
				tmpl.setCharacters(characters);
				Iterator<Character> citer = characters.iterator();
				ByteBuffer all = ByteBuffer.allocate((characters.size()*649)+8+3);
				byte[] size = BitTools.shortToByteArray((short)all.capacity());
				all.put(size);
				all.put(new byte[] { (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x04, (byte)0x00 });
				
				all.put(new byte[] { (byte)0x01, (byte)0x01, (byte)0x01 });
				
				Character ctm = citer.next();
				ctm.setPlayer(plc.getPlayer());
				
				ctm.getwholist(ctm.getCharID());
				ctm.getskills(ctm.getCharID());
				ctm.getskillbardb(ctm.getCharID());
				ctm.getcargo(ctm.getCharID());
				ctm.getinventorys(ctm.getCharID());
				ctm.getpots(ctm.getCharID());
				ctm.getsexpire(ctm.getCharID());
				
												
				byte[] tmp = ctm.initCharPacket();
				for(int i=0;i<tmp.length;i++) {
					all.put(tmp[i]);
				}
				
				while(citer.hasNext()) {

					Character ctmp = citer.next();
					ctmp.setPlayer(plc.getPlayer());
					
					ctmp.getwholist(ctmp.getCharID());
					ctmp.getskills(ctmp.getCharID());
					ctmp.getskillbardb(ctmp.getCharID());
					ctmp.getcargo(ctmp.getCharID());
					ctmp.getinventorys(ctmp.getCharID());
					ctmp.getpots(ctmp.getCharID());
					ctmp.getsexpire(ctmp.getCharID());
					
					byte[] tmpb = ctmp.initCharPacket();
					for(int i=0;i<tmpb.length;i++) {
						all.put(tmpb[i]);
					}
					
					all.put(10, (byte)((all.get(10)*2)+1)); //required increment depending on amount of characters on account
				}
				all.flip();
				byte[] meh = new byte[all.limit()];
				all.get(meh);
				plc.addWrite(meh);
			}
		}
		} else { // not logged in website
			plc.addWrite((Login.pleaseloginwebsite));
			plc.addWrite((Login.pleaselogin_respondtoclient)); // 2nd respond in a row just makes it work idk why
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			con.threadSafeDisconnect();
			//System.out.println("Not logged in the website: " + con.getIp());
		}*/
		
		PlayerConnection plc = (PlayerConnection)ServerFacade.getInstance().getConnections().get(con.getChan());
		//authenticate with IP + hasloggedin='yes'
		Player tmpl = AccountDAO.authipandloggedin(con.getIp(), "Yes"); // VIP for testing
		//Player tmpl = AccountDAO.authipandloggedin(con.getIp(), "Yes"); // the real deal
	    	if(tmpl != null) {	
			 if (con.getIp().equals(ipbanlist.getipbanlist(con.getIp()))){ // ip banned
					plc.addWrite((Login.banned1));
					plc.addWrite((Login.banned2)); // 2nd respond in a row just makes it work idk why
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						////e.printStackTrace();
					}
					ServerFacade.getInstance().getCon().removeAccounts(tmpl.getAccountID());
					con.threadSafeDisconnect();
				//this.logging.logMessage(Level.INFO, this, " ===>IP BANNED: " + con.getIp()+" <=== ");
				}
				else
				if(tmpl.getBanned() == 1) { // if has logged in but banned is on 1 in "accounts" then close everything directly
					plc.addWrite((Login.banned1));
					plc.addWrite((Login.banned2)); // 2nd respond in a row just makes it work idk why	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						////e.printStackTrace();
					}
					ServerFacade.getInstance().getCon().removeAccounts(tmpl.getAccountID());
					con.threadSafeDisconnect();
				//this.logging.logMessage(Level.INFO, this, " ===>ACCOUNT BANNED: " + con.getIp()+" <=== ");
				}else{	
			 try {
			 PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Login_logs.txt", true)));
			 Calendar Date = Calendar.getInstance(Locale.FRANCE);	
			 String timendate = Date.get(Calendar.HOUR_OF_DAY)+":"+Date.get(Calendar.MINUTE)
			 +" "+Date.get(Calendar.DAY_OF_MONTH)+"/"+Date.get(Calendar.MONTH)+"/"+Date.get(Calendar.YEAR);
			 out.println("ID:"+tmpl.getAccountID()+" Username:"+tmpl.getUsername()+" IP:"+con.getIp()+" Time:"+timendate);
			 out.close();	
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			 }
			tmpl.setChannel(con.getChan());
			tmpl.PlayerTimer();
			plc.setPlayer(tmpl);
			plc.addWrite((Login.Authsucces));
			ArrayList<Character> characters = CharacterDAO.loadCharacters(tmpl.getAccountID());
			
			if(characters != null) {
				tmpl.setCharacters(characters);
				Iterator<Character> citer = characters.iterator();
				ByteBuffer all = ByteBuffer.allocate((characters.size()*653)+8+3);
				byte[] size = BitTools.shortToByteArray((short)all.capacity());
				all.put(size);
				all.put(new byte[] { (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x04, (byte)0x00 });
				
				all.put(new byte[] { (byte)0x01, (byte)0x01, (byte)0x01 });
				
				Character ctm = citer.next();
				ctm.setPlayer(plc.getPlayer());
				ctm.getwholist(ctm.getCharID());
				//ctm.setguild();
												
				byte[] tmp = ctm.initCharPacket();
				for(int i=0;i<tmp.length;i++) {
					all.put(tmp[i]);
				}
				
				while(citer.hasNext()) {
					
					Character ctmp = citer.next();
					ctmp.setPlayer(plc.getPlayer());
					ctmp.getwholist(ctmp.getCharID());
					//ctmp.setguild();
					byte[] tmpb = ctmp.initCharPacket();
					for(int i=0;i<tmpb.length;i++) {
						all.put(tmpb[i]);
					}
					
					all.put(10, (byte)((all.get(10)*2)+1)); //required increment depending on amount of characters on account
				}
				all.flip();
				byte[] meh = new byte[all.limit()];
				all.get(meh);
				plc.addWrite(meh);
			}
		}
		} else { // not logged in website
			plc.addWrite((Login.pleaseloginwebsite));
			plc.addWrite((Login.pleaselogin_respondtoclient)); // 2nd respond in a row just makes it work idk why
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			con.threadSafeDisconnect();
			//System.out.println("Not logged in the website: " + con.getIp());
		}
	//} else { //in any other case assume failed authentication
	//	con.addWrite((Login.authFail));
	//	con.threadSafeDisconnect();
		//this.logging.logMessage(Level.INFO, this, "Authentication failed for connection: " + con.getIp());
	//}
		
		return null;
	}
}
