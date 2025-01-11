package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Tools.BitTools;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import chat.ChatCommandExecutor;

public class gold implements ChatCommandExecutor {
 

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling gold");

		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		
		
		long goldz = cur.getgold()  + Integer.valueOf(parameters[0]);
		cur.setgold(goldz);
		byte[] gold = BitTools.intToByteArray(Integer.valueOf(parameters[0]));
		byte[] partychat = new byte[40];
		partychat[0] = (byte)0x28;
		partychat[4] = (byte)0x04;
		partychat[6] = (byte)0x0f;
		partychat[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			partychat[12+i] = chid[i]; 
			partychat[36+i] = gold[i]; 
		}
		partychat[16] = (byte)0x01;

		partychat[18] = (byte)0x75;
		partychat[19] = (byte)0x2e;
		partychat[20] = (byte)0xff;//inv slot 255
		
		partychat[24] = (byte)0xf0; 
		
		partychat[27] = (byte)0x2a;
		
		partychat[32] = (byte)0x35;
		partychat[33] = (byte)0x2a;
		partychat[34] = (byte)0xef;
		partychat[35] = (byte)0x0c;
		CharacterDAO.setgold(goldz, cur.charID);
		ServerFacade.getInstance().addWriteByChannel(source.getChan(), partychat);
	}

}
