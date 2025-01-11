package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class commands implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling ,commands command");
		

	  String commands =  ",commands,a,additem,massitemspawn,heal,shout,fame,regen,quit" ;
	  String commands2 = ",showstats,porttele,portplayer,appear,summon,masssummon,test" ;
	  String commands3 = ",getstatsfromdb,kickplayer,masskickplayer,banplayer,massbanplayer" ;
		
	  Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
		byte[] name = "[Server]".getBytes();
	  
	  
		byte[] gmsg = new byte[45+commands.length()];
		byte[] msg = commands.getBytes(); // <--- real gm msg lol
		
		gmsg[0] = (byte)gmsg.length;
		gmsg[4] = (byte)0x05;
		gmsg[6] = (byte)0x07;
		gmsg[8] = (byte)0x01;
		gmsg[17] = (byte)0x01;
		gmsg[18] = (byte)0x06;
		
		

		gmsg[20] = (byte)0xa4; // begin letter name
		gmsg[21] = (byte)0xd1;
		//gmsg[22] = (byte)0xbe;
		//gmsg[23] = (byte)0xe1;					   | --> can be used for names such as "System news" ?
		//gmsg[24] = (byte)0xb1;
		//gmsg[25] = (byte)0xc3; // end letter name
		
		

		
		for(int i=0;i<name.length;i++) {
			gmsg[i+22] = name[i];
		}
		
		gmsg[40] = (byte)0x44; // = " : "
		
	
		
		for(int i=0;i<msg.length;i++) {
			gmsg[i+44] = msg[i];
		}
		ServerFacade.getInstance().addWriteByChannel(currenthcamp.GetChannel(), gmsg);
		
		
		
//-------->	// line 2 \\ <-----------------------------------------------------------------------------------------------------\\
		byte[] namez = "[Server]".getBytes();
		  
		  
		byte[] gmsgz = new byte[45+commands2.length()];
		byte[] msgz = commands2.getBytes(); // <--- real gm msg lol
		
		gmsgz[0] = (byte)gmsgz.length;
		gmsgz[4] = (byte)0x05;
		gmsgz[6] = (byte)0x07;
		gmsgz[8] = (byte)0x01;
		gmsgz[17] = (byte)0x01;
		gmsgz[18] = (byte)0x06;
		
		

		gmsgz[20] = (byte)0xa4; // begin letter name
		gmsgz[21] = (byte)0xd1;
		//gmsg[22] = (byte)0xbe;
		//gmsg[23] = (byte)0xe1;					   | --> can be used for names such as "System news" ?
		//gmsg[24] = (byte)0xb1;
		//gmsg[25] = (byte)0xc3; // end letter name
		
		

		
		for(int i=0;i<namez.length;i++) {
			gmsgz[i+22] = namez[i];
		}
		
		gmsgz[40] = (byte)0x44; // = " : "
		
	
		
		for(int i=0;i<msgz.length;i++) {
			gmsgz[i+44] = msgz[i];
		}
		ServerFacade.getInstance().addWriteByChannel(currenthcamp.GetChannel(), gmsgz);
		
		//-------->	// line 3 \\ <-----------------------------------------------------------------------------------------------------\\
				byte[] namezz = "[Server]".getBytes();
				  
				  
				byte[] gmsgzz = new byte[45+commands3.length()];
				byte[] msgzz = commands3.getBytes(); // <--- real gm msg lol
				
				gmsgzz[0] = (byte)gmsgzz.length;
				gmsgzz[4] = (byte)0x05;
				gmsgzz[6] = (byte)0x07;
				gmsgzz[8] = (byte)0x01;
				gmsgzz[17] = (byte)0x01;
				gmsgzz[18] = (byte)0x06;
				
				

				gmsgzz[20] = (byte)0xa4; // begin letter name
				gmsgzz[21] = (byte)0xd1;
				//gmsg[22] = (byte)0xbe;
				//gmsg[23] = (byte)0xe1;					   | --> can be used for names such as "System news" ?
				//gmsg[24] = (byte)0xb1;
				//gmsg[25] = (byte)0xc3; // end letter name
				
				

				
				for(int i=0;i<namezz.length;i++) {
					gmsgzz[i+22] = namezz[i];
				}
				
				gmsgzz[40] = (byte)0x44; // = " : "
				
			
				
				for(int i=0;i<msgzz.length;i++) {
					gmsgzz[i+44] = msgzz[i];
				}
				ServerFacade.getInstance().addWriteByChannel(currenthcamp.GetChannel(), gmsgzz);
	
		
	}

}
