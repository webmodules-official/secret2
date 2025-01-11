package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import logging.ServerLogger;

import Connections.Connection;
import Database.CharacterDAO;
import Player.Character;
import Player.Charstuff;
import Player.Party;
import Player.Player;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;


public class ReturnToSelection implements Packet {
	private WMap wmap = WMap.getInstance();
	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}

	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		Character current = ((PlayerConnection)con).getActiveCharacter();
		  Party pt = wmap.getParty(current.partyUID);
		  if (pt != null){
			  if(current.partyUID != 0 && pt.partymembers.containsValue(current.charID)){
			  int Key = BitTools.ValueToKey(current.charID,pt.partymembers);
			  if(Key != 0){pt.ReOrder(Key);}}
		  byte[] decrypted = new byte[] {(byte)0x00};
		  pt.leave_kicK(decrypted, current);
		  }
		  if(wmap.Trade.containsKey(current.TradeUID)){
		  wmap.Trade.get(current.TradeUID).CancelTrade();
		  }
		 current.killedbyplayer = false;
		 current.IsTrading = false;
		 current.IsVending = false;
		if(current.Duel != 0){current.Duel = 0;current.duelover();}
		if(current.furycheck != 0){current.furycheck = 0;}
		if(current.furyvalue != 0){current.furyvalue = 0;}// reset fury value to 0
		if(current.furyactive != 0){current.furyactive= 0;}// cant be re-activated
		if(current.partyUID != 0){current.partyUID = 0;}
		if(current.Running != 0){current.Running = 0;}
		if(current.MANA_SHIELD_PROTECTION != 0){current.MANA_SHIELD_PROTECTION = 0;}
		if(current.HIDING != 0){current.HIDING = 0;}
		if(current.Summonid != 0){current.Summonid = 0;}
		if(current.Budoong != 0){current.Budoong = 0;}
		if(current.getFarmmodule() != 0){current.setFarmmodule(0);}
		current.BuffPercentage.clear();
		current.BoothStance = 0;
		current.TradeUID = 0;
		current.proffpool = false;
		WMap.getInstance().vendinglist.remove(current.getCharID());
		current.TempStoreBuffs.clear();
		current.TempPassives.clear();
		current.DotsIconID.clear();
		current.DotsValue.clear();
		current.DotsTime.clear();
		current.DotsSLOT.clear();
		current.savecharacter();
		current.leaveGameWorld(); //leave the gameworld
		con.getWriteBuffer().clear(); //clear all packets pending write(prevent client from crashing as it returns to selection)
		Player tmplayer = ((PlayerConnection)con).getPlayer();
		tmplayer.setActiveCharacter(null);
		return tmplayer.Refresh_Characters();
	}

}
