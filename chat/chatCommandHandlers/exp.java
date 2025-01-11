package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class exp implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		long recexp = Long.parseLong(parameters[0]);

			byte[] chid = BitTools.intToByteArray(cur.getCharID());
			double Exp;
			if(cur.Exp_Tag_100 == 1){Exp = recexp * 2;}    // exp * 100%
			else
			if(cur.Exp_Tag_30 == 1){ Exp = recexp * 1.30;} // exp * 30%
			else
			if(cur.Exp_Tag_20 == 1){ Exp = recexp * 1.20;} // exp * 20%
			else
			if(cur.Exp_Tag_15 == 1){ Exp = recexp * 1.15;} // exp * 15%
			else
			if(cur.Exp_Tag_10 == 1){ Exp = recexp * 1.10;} // exp * 10%
			else
									{ Exp = recexp * 1;}    // normal * 1
			long FinalExp = (long)Exp;
			Exp = 0; // reset to * 1
			
			byte[] msg = BitTools.LongToByteArrayREVERSE(FinalExp);

			
			byte[] reexp = new byte[24];
			reexp[0] = (byte)0x18;
			reexp[4] = (byte)0x05;
			reexp[6] = (byte)0x0b;
			reexp[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				reexp[12+i] = chid[i]; 
			}
			
			reexp[8] = (byte)0x01;
			
			for(int b=0;b<msg.length;b++) {
				reexp[b+16] = msg[b];
			}
			
			long newexp = cur.getexp() + Long.valueOf(FinalExp);
			cur.setexp(Long.valueOf(newexp));
			System.out.println("TOTAL EXP: "+ newexp);
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), reexp); 
		}
	}
	
