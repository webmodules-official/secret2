package timer;

import java.util.TimerTask;
import Connections.Connection;
import Player.Character;
import Player.PlayerConnection;


public class potstime extends TimerTask{
	private Connection con;
	
	public potstime(Connection con){
		this.con = con;
	}

	@Override
	public void run() {//9000 x 4 = 36000 seconds = 600 minutes = 10 hours
		if(con.getChan().isOpen()) {
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur != null){
		if(cur.getCurrentMap() != 100){
		 for(int a=0;a<10;a++){
		 if(cur.PotTime.containsKey(a)){
		 int newtime = cur.PotTime.get(a) - 3; // current time - 12 seconds each 10 sec
		 if(newtime <= 0){

			 	if(cur.PotIconID.get(a) == 92){cur.JACKPOT_TAG = 0;}
			 	if(cur.PotIconID.get(a) == 91){cur.DOUBLE_ITEM_DROP_TAG = 0;}
			 	if(cur.PotIconID.get(a) == 84){cur.FDD = 0;} 
			 	if(cur.PotIconID.get(a) == 87){cur.CASR = 0;}
			 	if(cur.PotIconID.get(a) == 83){cur.FAD = 0;} 
			 	if(cur.PotIconID.get(a) == 85){cur.FASR = 0;}
			 	if(cur.PotIconID.get(a) == 90){cur.Fame_Tag_100 = 0;}
			 	if(cur.PotIconID.get(a) == 82 && cur.PotValue.containsValue(10)){cur.Exp_Tag_10 = 0;}
			 	if(cur.PotIconID.get(a) == 82 && cur.PotValue.containsValue(15)){cur.Exp_Tag_15 = 0;}
			 	if(cur.PotIconID.get(a) == 82 && cur.PotValue.containsValue(20)){cur.Exp_Tag_20 = 0;}
			 	if(cur.PotIconID.get(a) == 82 && cur.PotValue.containsValue(30)){cur.Exp_Tag_30 = 0;}
			 	if(cur.PotIconID.get(a) == 82 && cur.PotValue.containsValue(100)){cur.Exp_Tag_100 = 0;}
			 	if(cur.PotIconID.get(a) == 86){cur.FD = 0;}
			 	if(cur.PotIconID.get(a) == 94){cur.SAP = 0;}
			 	if(cur.PotIconID.get(a) == 93){cur.NEW_STATUS_SUNDAN = 0;}
				
				cur.PotSLOT.remove(a);	 
				cur.PotIconID.remove(a);	
				cur.PotTime.remove(a); // x 4 = 36000 seconds = 600 minutes = 10 hours
				cur.PotValue.remove(a);	
			//TODO: send packet to player to notify that its over for the slot to 0;
			// System.out.println(a+":" +newtime+" - Death");	 
		 }else{
		 cur.setPotTime(a, newtime);
		 //System.out.println(a+":" +newtime+" - Continue");
		 }}}}}
	   }else{this.cancel();}
	}
}
